"""
APK测试图片分析Agent
使用多模态大模型分析APK测试截图，识别问题并提供优化建议
"""
import os
import json
from typing import Annotated
from langchain.agents import create_agent
from langchain_core.messages import AnyMessage, SystemMessage, HumanMessage
from langchain_openai import ChatOpenAI
from langgraph.graph import MessagesState
from langgraph.graph.message import add_messages
from coze_coding_utils.runtime_ctx.context import default_headers, new_context
from coze_coding_dev_sdk import LLMClient
from storage.memory.memory_saver import get_memory_saver
from tools.image_reader_tool import read_image_file, list_available_images, get_image_dimensions

LLM_CONFIG = "config/apk_image_analyzer_config.json"

# 默认保留最近 20 轮对话 (40 条消息)
MAX_MESSAGES = 40

def _windowed_messages(old, new):
    """滑动窗口: 只保留最近 MAX_MESSAGES 条消息"""
    return add_messages(old, new)[-MAX_MESSAGES:]

class AgentState(MessagesState):
    messages: Annotated[list[AnyMessage], _windowed_messages]


class APKImageAnalyzerAgent:
    """APK测试图片分析Agent"""
    
    def __init__(self):
        self.workspace_path = os.getenv("COZE_WORKSPACE_PATH", "/workspace/projects")
        self.config = self._load_config()
        self.llm_client = None
    
    def _load_config(self):
        """加载配置文件"""
        config_path = os.path.join(self.workspace_path, LLM_CONFIG)
        with open(config_path, 'r', encoding='utf-8') as f:
            return json.load(f)
    
    def _get_llm_client(self, ctx=None):
        """获取LLM客户端"""
        if self.llm_client is None:
            # 不传递config参数，使用默认环境变量配置
            self.llm_client = LLMClient(
                ctx=ctx if ctx else new_context(method="analyze_image")
            )
        return self.llm_client
    
    def analyze_image(self, image_path: str, question: str = "请分析这张APK测试截图，识别问题并提供优化建议") -> str:
        """
        分析APK测试图片
        
        Args:
            image_path: 图片文件路径
            question: 分析问题
            
        Returns:
            分析结果
        """
        # 读取图片
        image_result = read_image_file.invoke({"image_path": image_path})
        
        if image_result.startswith("错误："):
            return image_result
        
        # 构建消息
        messages = [
            SystemMessage(content=self.config.get("sp", "")),
            HumanMessage(content=[
                {
                    "type": "text",
                    "text": question
                },
                {
                    "type": "image_url",
                    "image_url": {
                        "url": image_result
                    }
                }
            ])
        ]
        
        # 调用多模态模型
        try:
            client = self._get_llm_client()
            response = client.invoke(
                messages=messages,
                model=self.config['config'].get("model", "doubao-seed-1-6-vision-250815"),
                temperature=self.config['config'].get('temperature', 0.7),
                max_completion_tokens=self.config['config'].get('max_completion_tokens', 10000)
            )
            
            # 提取文本内容
            if isinstance(response.content, str):
                return response.content
            elif isinstance(response.content, list):
                if response.content and isinstance(response.content[0], str):
                    return " ".join(response.content)
                else:
                    text_parts = [item.get("text", "") for item in response.content 
                                 if isinstance(item, dict) and item.get("type") == "text"]
                    return " ".join(text_parts)
            else:
                return str(response.content)
        
        except Exception as e:
            return f"分析失败：{str(e)}"
    
    def list_images(self) -> str:
        """列出可用的图片文件"""
        return list_available_images.invoke({})
    
    def get_image_info(self, image_path: str) -> str:
        """获取图片信息"""
        return get_image_dimensions.invoke({"image_path": image_path})


# 全局实例
_analyzer_instance = None

def get_analyzer():
    """获取分析器实例（单例）"""
    global _analyzer_instance
    if _analyzer_instance is None:
        _analyzer_instance = APKImageAnalyzerAgent()
    return _analyzer_instance


def build_agent(ctx=None):
    """
    构建Agent（用于LangGraph）
    
    注意：此Agent主要用于多模态图片分析，使用专门的LLMClient
    """
    workspace_path = os.getenv("COZE_WORKSPACE_PATH", "/workspace/projects")
    config_path = os.path.join(workspace_path, LLM_CONFIG)
    
    with open(config_path, 'r', encoding='utf-8') as f:
        cfg = json.load(f)
    
    api_key = os.getenv("COZE_WORKLOAD_IDENTITY_API_KEY")
    base_url = os.getenv("COZE_INTEGRATION_MODEL_BASE_URL")
    
    # 创建LLM（用于常规对话）
    llm = ChatOpenAI(
        model=cfg['config'].get("model", "doubao-seed-1-6-vision-250815"),
        api_key=api_key,
        base_url=base_url,
        temperature=cfg['config'].get('temperature', 0.7),
        streaming=True,
        timeout=cfg['config'].get('timeout', 600),
        extra_body={
            "thinking": {
                "type": cfg['config'].get('thinking', 'disabled')
            }
        },
        default_headers=default_headers(ctx) if ctx else {}
    )
    
    # 注意：图片分析使用专门的LLMClient，不在create_agent中处理
    # Agent主要用于工具调用和对话管理
    
    return create_agent(
        model=llm,
        system_prompt=cfg.get("sp"),
        tools=[read_image_file, list_available_images, get_image_dimensions],
        checkpointer=get_memory_saver(),
        state_schema=AgentState,
    )

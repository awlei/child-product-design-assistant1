import os
import json
from typing import Annotated
from langchain.agents import create_agent
from langchain_openai import ChatOpenAI
from langgraph.graph import MessagesState
from langgraph.graph.message import add_messages
from langchain_core.messages import AnyMessage
from coze_coding_utils.runtime_ctx.context import default_headers
from storage.memory.memory_saver import get_memory_saver

LLM_CONFIG = "config/agent_llm_config.json"

# 默认保留最近 20 轮对话 (40 条消息)
# 对于错误分析场景，需要记住之前的上下文，以便追踪问题解决进度
MAX_MESSAGES = 40

def _windowed_messages(old, new):
    """滑动窗口: 只保留最近 MAX_MESSAGES 条消息"""
    return add_messages(old, new)[-MAX_MESSAGES:] # type: ignore

class AgentState(MessagesState):
    messages: Annotated[list[AnyMessage], _windowed_messages]

def build_agent(ctx=None):
    """
    构建Android构建错误分析Agent
    
    该Agent专门用于分析Android构建错误日志，提供专业的解决方案。
    使用思考模型进行深度推理，能够识别KAPT、依赖冲突、缓存问题等常见错误。
    """
    workspace_path = os.getenv("COZE_WORKSPACE_PATH", "/workspace/projects")
    config_path = os.path.join(workspace_path, LLM_CONFIG)
    
    # 读取配置文件
    with open(config_path, 'r', encoding='utf-8') as f:
        cfg = json.load(f)
    
    # 获取API配置
    api_key = os.getenv("COZE_WORKLOAD_IDENTITY_API_KEY")
    base_url = os.getenv("COZE_INTEGRATION_MODEL_BASE_URL")
    
    # 创建LLM实例
    # 使用doubao-seed-1-6-thinking-250715思考模型进行深度错误分析
    # 启用thinking模式以支持复杂推理
    llm = ChatOpenAI(
        model=cfg['config'].get("model"),
        api_key=api_key,
        base_url=base_url,
        temperature=cfg['config'].get('temperature', 0.7),
        streaming=True,
        timeout=cfg['config'].get('timeout', 600),
        extra_body={
            "thinking": {
                "type": cfg['config'].get('thinking', 'enabled')
            }
        },
        default_headers=default_headers(ctx) if ctx else {}
    )
    
    # 创建Agent
    # 该Agent不使用工具，完全依赖大语言模型的分析能力
    # 通过精心设计的System Prompt，植入Android构建错误的知识库
    agent = create_agent(
        model=llm,
        system_prompt=cfg.get("sp"),
        tools=[],  # 无需工具，直接使用LLM分析能力
        checkpointer=get_memory_saver(),
        state_schema=AgentState,
    )
    
    return agent

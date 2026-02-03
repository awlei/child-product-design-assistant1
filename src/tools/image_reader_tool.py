"""
图片分析工具
用于读取APK测试图片并进行分析
"""
import os
import base64
from typing import Optional
from langchain.tools import tool, ToolRuntime
from coze_coding_utils.runtime_ctx.context import new_context

@tool
def read_image_file(image_path: str, runtime: ToolRuntime = None) -> str:
    """
    读取图片文件并返回base64编码的URL
    
    Args:
        image_path: 图片文件路径，相对于assets目录
        
    Returns:
        包含图片URL的字符串，格式为：data:image/{format};base64,{base64_data}
    """
    ctx = runtime.context if runtime else new_context(method="read_image_file")
    
    # 构建完整路径
    workspace_path = os.getenv("COZE_WORKSPACE_PATH", "/workspace/projects")
    
    # 支持绝对路径和相对路径
    if os.path.isabs(image_path):
        full_path = image_path
    elif image_path.startswith("assets/"):
        full_path = os.path.join(workspace_path, image_path)
    else:
        full_path = os.path.join(workspace_path, "assets", image_path)
    
    # 检查文件是否存在
    if not os.path.exists(full_path):
        return f"错误：图片文件不存在 - {full_path}"
    
    # 检查文件大小（限制10MB）
    file_size = os.path.getsize(full_path)
    if file_size > 10 * 1024 * 1024:
        return f"错误：图片文件过大 ({file_size / 1024 / 1024:.2f}MB)，请提供小于10MB的图片"
    
    # 读取文件并转换为base64
    try:
        with open(full_path, "rb") as f:
            image_data = base64.b64encode(f.read()).decode('utf-8')
        
        # 确定图片格式
        file_ext = os.path.splitext(full_path)[1].lower()
        mime_type = {
            '.jpg': 'image/jpeg',
            '.jpeg': 'image/jpeg',
            '.png': 'image/png',
            '.gif': 'image/gif',
            '.webp': 'image/webp'
        }.get(file_ext, 'image/jpeg')
        
        # 返回data URL
        return f"data:{mime_type};base64,{image_data}"
    
    except Exception as e:
        return f"错误：读取图片失败 - {str(e)}"


@tool
def list_available_images(runtime: ToolRuntime = None) -> str:
    """
    列出assets目录下所有可用的图片文件
    
    Returns:
        包含图片文件列表的JSON格式字符串
    """
    ctx = runtime.context if runtime else new_context(method="list_available_images")
    
    workspace_path = os.getenv("COZE_WORKSPACE_PATH", "/workspace/projects")
    assets_path = os.path.join(workspace_path, "assets")
    
    if not os.path.exists(assets_path):
        return "错误：assets目录不存在"
    
    image_extensions = {'.jpg', '.jpeg', '.png', '.gif', '.webp'}
    images = []
    
    for file_name in os.listdir(assets_path):
        if os.path.splitext(file_name)[1].lower() in image_extensions:
            file_path = os.path.join(assets_path, file_name)
            file_size = os.path.getsize(file_path)
            images.append({
                "name": file_name,
                "path": f"assets/{file_name}",
                "size": f"{file_size / 1024:.2f}KB"
            })
    
    if not images:
        return "assets目录下没有图片文件"
    
    # 格式化输出
    result = "可用的图片文件：\n"
    for img in images:
        result += f"- {img['name']} ({img['size']}) - 路径: {img['path']}\n"
    
    return result


@tool
def get_image_dimensions(image_path: str, runtime: ToolRuntime = None) -> str:
    """
    获取图片的尺寸信息
    
    Args:
        image_path: 图片文件路径
        
    Returns:
        图片尺寸信息字符串
    """
    ctx = runtime.context if runtime else new_context(method="get_image_dimensions")
    
    workspace_path = os.getenv("COZE_WORKSPACE_PATH", "/workspace/projects")
    
    # 构建完整路径
    if os.path.isabs(image_path):
        full_path = image_path
    elif image_path.startswith("assets/"):
        full_path = os.path.join(workspace_path, image_path)
    else:
        full_path = os.path.join(workspace_path, "assets", image_path)
    
    if not os.path.exists(full_path):
        return f"错误：图片文件不存在 - {full_path}"
    
    try:
        # 简单返回文件大小信息
        file_size = os.path.getsize(full_path)
        file_ext = os.path.splitext(full_path)[1].lower()
        
        return f"""图片信息：
- 文件路径: {full_path}
- 文件大小: {file_size / 1024:.2f}KB
- 文件格式: {file_ext}
- 状态: 可用于分析
        """
    except Exception as e:
        return f"错误：获取图片信息失败 - {str(e)}"

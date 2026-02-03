# AI助手使用指南

## 功能概述

APK已集成豆包大模型，为工程师提供智能设计协助功能，确保生成技术输出准确。

## 核心特性

### 1. 实时设计建议
- 基于用户选择的产品类型和标准，提供专业的技术建议
- 包含设计参数、材料规格、测试方案等
- 严格遵循标准隔离原则，只输出用户选择的标准内容

### 2. 标准查询
- 支持查询标准条款、技术参数、测试方法等
- 提供条款原文和实施指南
- 明确标注来源标准的具体条款

### 3. 问题诊断
- 分析设计问题的根本原因
- 提供具体的修复方案和代码示例
- 识别潜在风险并提供缓解措施

## 使用方法

### 1. 配置API密钥

在 `app/src/main/assets/config/llm_config.properties` 中配置API密钥：

```properties
# API基础URL
LLM_BASE_URL=https://ark.cn-beijing.volces.com/api/v3/

# API密钥（替换为实际的API密钥）
LLM_API_KEY=your_api_key_here

# 默认模型
LLM_DEFAULT_MODEL=doubao-seed-1-6-251015
```

### 2. 使用AI助手对话界面

在APK中添加AI助手入口：

```kotlin
// 在MainActivity中导航到AI助手界面
composable("ai_assistant") {
    AIAssistantScreen(
        onBackClick = { navController.popBackStack() }
    )
}
```

### 3. 调用设计协助服务

```kotlin
import com.childproduct.designassistant.ai.DesignAssistantService
import com.childproduct.designassistant.model.EnhancedProductType
import com.childproduct.designassistant.model.InternationalStandard

// 获取服务实例
val service = DesignAssistantService.getInstance()

// 生成设计建议
val result = service.generateDesignAdvice(
    productType = EnhancedProductType.SAFETY_SEAT,
    standards = listOf(InternationalStandard.ECE_R129),
    heightRange = "40-105",
    customRequest = "需要优化安全带系统"
)

result.onSuccess { advice ->
    // 使用设计建议
    println(advice)
}.onFailure { error ->
    // 处理错误
    println("错误: ${error.message}")
}

// 查询标准
val queryResult = service.queryStandard(
    standardCode = "ECE R129",
    queryType = "安全阈值",
    keywords = listOf("HIC", "胸部加速度")
)

// 诊断问题
val diagnosisResult = service.diagnoseProblem(
    problemDescription = "头枕调节不顺畅",
    context = "用户反馈在使用过程中发现",
    productType = EnhancedProductType.SAFETY_SEAT,
    standards = listOf(InternationalStandard.ECE_R129)
)
```

## Prompt模板

### 设计建议Prompt

生成的Prompt包含以下部分：
1. 产品信息（产品类型、适配标准）
2. 设计参数（身高范围、特殊需求）
3. 任务要求（标准合规性、设计参数、测试方案、风险提示）

### 标准查询Prompt

生成的Prompt包含以下部分：
1. 查询参数（标准代码、查询类型、关键词）
2. 任务要求（条款列表、技术参数、实施指南）
3. 输出格式（标准概述、相关条款、实施指南）

### 问题诊断Prompt

生成的Prompt包含以下部分：
1. 问题描述（问题描述、上下文信息）
2. 任务要求（问题定位、根因分析、解决方案、预防措施）
3. 输出格式（问题概述、根因分析、解决方案、预防措施）

## 标准隔离原则

### 核心原则

1. **只输出用户选择的标准内容**
   - 严禁混用其他标准（如ECE R129、FMVSS 213等，除非用户已选择）
   - 在Prompt中明确标注："本建议仅基于用户选择的标准：[标准代码]"

2. **参数标注来源**
   - 所有技术参数必须标注来源标准的具体条款
   - 格式：`[数值] [单位] (条款：[标准条款])`

3. **准确性优先**
   - 温度参数：标准查询使用0.5，问题诊断使用0.6，聊天使用0.8
   - 确保输出准确且专业

## 错误处理

### 重试机制

- 最大重试次数：3次
- 重试延迟：1000ms × (重试次数)
- 自动重试网络错误

### 异常类型

```kotlin
class LLMException(
    message: String,
    code: String? = null,
    type: String? = null,
    cause: Throwable? = null
)
```

### 错误处理示例

```kotlin
val result = service.generateDesignAdvice(...)

result.onSuccess { response ->
    // 处理成功响应
}.onFailure { error ->
    when (error) {
        is LLMException -> {
            println("LLM错误: ${error.message}")
            println("错误码: ${error.errorCode}")
        }
        else -> {
            println("其他错误: ${error.message}")
        }
    }
}
```

## 性能优化

### 1. 单例模式

- `LLMClient` 和 `DesignAssistantService` 都使用单例模式
- 避免重复创建实例

### 2. 协程支持

- 所有API调用都支持协程
- 使用 `suspend` 函数确保异步执行

### 3. 缓存机制

- HTTP客户端自动复用
- 连接池管理

## 安全建议

1. **API密钥管理**
   - 不要将API密钥硬编码在代码中
   - 使用配置文件或安全存储
   - 生产环境使用环境变量或密钥管理服务

2. **日志脱敏**
   - 不要在日志中输出敏感信息
   - 使用日志拦截器过滤敏感数据

3. **网络加密**
   - 使用HTTPS协议
   - 验证SSL证书

## 测试

### 单元测试

测试Prompt模板的生成逻辑：

```kotlin
@Test
fun testGenerateDesignAdvicePrompt() {
    val prompt = PromptTemplate.generateDesignAdvicePrompt(
        productType = EnhancedProductType.SAFETY_SEAT,
        standards = listOf(InternationalStandard.ECE_R129),
        heightRange = "40-105",
        customRequest = "需要优化安全带系统"
    )

    assertTrue(prompt.contains("ECE R129"))
    assertTrue(prompt.contains("本建议仅基于用户选择的标准"))
}
```

### 集成测试

测试完整的API调用流程：

```kotlin
@Test
fun testGenerateDesignAdviceIntegration() = runBlocking {
    val result = service.generateDesignAdvice(...)

    assertTrue(result.isSuccess())
    assertTrue(result.getOrNull()!!.isNotEmpty())
}
```

## 故障排查

### 1. 连接失败

检查网络连接和API配置：
- 确认 `LLM_BASE_URL` 正确
- 确认 `LLM_API_KEY` 有效
- 检查网络权限

### 2. 响应为空

检查请求参数：
- 确认系统提示词不为空
- 确认用户提示词格式正确
- 检查模型名称是否正确

### 3. 超时

调整超时配置：
```properties
LLM_CONNECT_TIMEOUT=30
LLM_READ_TIMEOUT=60
LLM_WRITE_TIMEOUT=60
```

## 下一步

1. 配置实际的API密钥
2. 在APK中添加AI助手入口
3. 测试各项功能
4. 根据实际需求调整Prompt模板
5. 添加更多功能（如多模态图片分析）

## 技术支持

如有问题，请查看：
- GitHub Issues: https://github.com/awlei/new-child-product-design-assistant/issues
- 豆包大模型文档: https://www.volcengine.com/docs/82379

package com.childproduct.designassistant.ai

import com.childproduct.designassistant.model.EnhancedProductType
import com.childproduct.designassistant.model.InternationalStandard

/**
 * Prompt模板管理器
 * 确保生成准确的技术输出
 */
object PromptTemplate {

    /**
     * 系统提示词 - 角色定义
     */
    private const val SYSTEM_PROMPT = """
# 角色定义
你是儿童产品设计专家助手，专注于儿童安全座椅、婴儿推车、高脚椅、婴儿床等产品的技术标准和设计规范。你具备深厚的行业知识，熟悉全球主要标准（ECE R129、FMVSS 213、GB 27887等）。

# 任务目标
为工程师提供准确、专业的产品设计建议，确保符合所选标准的技术要求。

# 核心原则
1. **标准隔离**：只输出用户选择的标准内容，严禁混用其他标准
2. **准确性优先**：所有技术参数必须精确到具体条款和数值
3. **可操作性**：提供具体的代码示例和实施方案
4. **风险提示**：明确指出潜在的设计风险和合规问题

# 能力
- 标准解读与条款查询
- 设计参数计算与验证
- 测试方案制定
- 材料规格建议
- 问题诊断与优化建议
    """.trimIndent()

    /**
     * 生成设计建议的Prompt
     */
    fun generateDesignAdvicePrompt(
        productType: EnhancedProductType,
        standards: List<InternationalStandard>,
        heightRange: String? = null,
        customRequest: String? = null
    ): String {
        return """
## 设计需求分析

### 产品信息
- 产品类型：${productType.displayName}
- 适配标准：${standards.joinToString("、") { it.displayName }}
- 标准代码：${standards.joinToString("、") { it.code }}

### 设计参数
${heightRange?.let { "- 身高范围：$it cm" } ?: "- 身高范围：未指定"}
${customRequest?.let { "- 特殊需求：$it" } ?: "- 特殊需求：无"}

### 任务要求
请基于以上信息，提供以下设计建议：

1. **标准合规性分析**
   - 列出所选标准的核心要求
   - 识别关键技术参数
   - 指出必须遵守的强制性条款

2. **设计参数建议**
   - 提供关键设计尺寸范围
   - 给出材料规格建议
   - 说明安全阈值要求

3. **测试方案**
   - 列出必要的测试项目
   - 说明测试方法和通过标准
   - 提供测试矩阵

4. **风险提示**
   - 识别潜在的设计风险
   - 提供风险缓解措施
   - 说明常见错误及避免方法

## 输出格式要求

请使用以下格式输出，确保清晰易读：

## 【标准合规性分析】
### ${standards.firstOrNull()?.code ?: "标准"}核心要求
- 要求1：[具体描述]
- 要求2：[具体描述]

## 【设计参数建议】
### 关键尺寸
- 尺寸1：[数值] ± [公差] (条款：[标准条款])
- 尺寸2：[数值] ± [公差] (条款：[标准条款])

### 材料规格
- 组件1：[材料规格]
- 组件2：[材料规格]

## 【测试方案】
| 测试项目 | 测试方法 | 通过标准 |
|----------|----------|----------|
| [项目1] | [方法] | [标准] |
| [项目2] | [方法] | [标准] |

## 【风险提示】
### 高风险项
- 风险1：[描述] → 缓解措施：[措施]
- 风险2：[描述] → 缓解措施：[措施]

### 常见错误
1. [错误描述]
2. [错误描述]

⚠️ **重要提示**：
- 本建议仅基于用户选择的标准：${standards.joinToString("、") { it.code }}
- 请勿引用其他标准（如ECE R129、FMVSS 213等，除非用户已选择）
- 所有参数必须标注来源标准的具体条款
        """.trimIndent()
    }

    /**
     * 生成标准查询的Prompt
     */
    fun generateStandardQueryPrompt(
        standardCode: String,
        queryType: String,
        keywords: List<String>
    ): String {
        return """
## 标准查询

### 查询参数
- 标准代码：$standardCode
- 查询类型：$queryType
- 关键词：${keywords.joinToString("、")}

### 任务要求
请查询标准 $standardCode 中与「${keywords.joinToString("、")}」相关的所有条款，并提供以下信息：

1. **相关条款列表**
   - 条款编号
   - 条款内容
   - 适用范围

2. **技术参数提取**
   - 提取所有数值要求（尺寸、重量、时间等）
   - 说明测试条件
   - 标注单位

3. **实施指南**
   - 如何在实际设计中应用
   - 常见错误及避免方法
   - 验证方法

## 输出格式

## 【条款查询结果】

### 标准概述
- 标准全称：[标准全称]
- 最新版本：[版本号]
- 发布日期：[日期]

### 相关条款
#### 条款1：[条款编号]
**内容**：[条款原文]
**适用范围**：[范围]
**技术参数**：
- 参数1：[数值] [单位] (条件：[条件])
- 参数2：[数值] [单位] (条件：[条件])

#### 条款2：[条款编号]
...

### 技术参数汇总
| 参数名称 | 要求值 | 单位 | 测试条件 | 条款来源 |
|----------|--------|------|----------|----------|
| [参数1] | [值] | [单位] | [条件] | [条款] |
| [参数2] | [值] | [单位] | [条件] | [条款] |

### 实施指南
1. [步骤1]
2. [步骤2]

### 常见错误
1. [错误1] → [正确做法]
2. [错误2] → [正确做法]

⚠️ **注意事项**：
- 仅提供 $standardCode 标准的内容
- 不要混用其他标准（如ECE R129、FMVSS 213等）
- 如果查询的关键词在标准中不存在，明确说明
        """.trimIndent()
    }

    /**
     * 生成问题诊断的Prompt
     */
    fun generateProblemDiagnosisPrompt(
        problemDescription: String,
        context: String? = null,
        productType: EnhancedProductType? = null,
        standards: List<InternationalStandard>? = null
    ): String {
        val standardContext = standards?.joinToString("、") { it.code } ?: "无指定标准"
        val productContext = productType?.displayName ?: "通用产品"

        return """
## 问题诊断分析

### 问题描述
$problemDescription

### 上下文信息
- 产品类型：$productContext
- 适用标准：$standardContext
${context?.let { "- 附加信息：$it" } ?: ""}

### 任务要求
请对以上问题进行深入分析，并提供诊断报告：

1. **问题定位**
   - 识别问题的根本原因
   - 分析问题影响范围
   - 判断严重程度

2. **根因分析**
   - 从技术角度分析原因
   - 识别设计缺陷
   - 识别流程问题

3. **解决方案**
   - 提供具体修复方案
   - 给出代码示例（如适用）
   - 说明验证方法

4. **预防措施**
   - 如何避免类似问题
   - 建议的改进措施
   - 长期优化方向

## 输出格式

## 【问题诊断报告】

### 1. 问题概述
- **问题描述**：[简明描述]
- **严重程度**：[高/中/低]
- **影响范围**：[描述]

### 2. 问题定位
#### 根本原因
[详细分析]

#### 影响分析
- 直接影响：[描述]
- 间接影响：[描述]

### 3. 根因分析
#### 技术原因
- 原因1：[描述]
- 原因2：[描述]

#### 设计原因
- 原因1：[描述]
- 原因2：[描述]

${standards?.isNotEmpty() == true ? """
#### 标准合规性
- 是否符合 $standardContext 标准：[是/否]
- 违反的条款（如有）：[条款编号] - [违规内容]
""" : ""}

### 4. 解决方案
#### 修复方案
**方案1**：[描述]
- 实施步骤：
  1. [步骤1]
  2. [步骤2]
- 预期效果：[描述]
- 验证方法：[方法]

**方案2**：[描述]（如有）

#### 代码示例（如适用）
```kotlin
// 代码示例
```

### 5. 预防措施
1. [措施1]
2. [措施2]

### 6. 长期优化
- [优化建议1]
- [优化建议2]

⚠️ **注意事项**：
${standards?.isNotEmpty() == true ? "- 本诊断仅基于用户选择的标准：$standardContext\n- 请勿引用其他标准的内容" : "- 未指定标准，仅提供通用技术建议"}
- 如需更精确的诊断，请提供更多上下文信息
        """.trimIndent()
    }

    /**
     * 获取系统提示词
     */
    fun getSystemPrompt(): String {
        return SYSTEM_PROMPT
    }
}

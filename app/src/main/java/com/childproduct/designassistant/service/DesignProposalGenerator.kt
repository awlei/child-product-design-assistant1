package com.childproduct.designassistant.service

import com.childproduct.designassistant.data.model.*
import com.childproduct.designassistant.database.EceR129Database
import com.childproduct.designassistant.network.llm.LLMClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * 设计方案生成器
 *
 * 使用LLM生成结构化的儿童安全座椅设计方案
 */
class DesignProposalGenerator(
    private val database: EceR129Database
) {

    private val llmClient = LLMClient()
    private val json = Json { ignoreUnknownKeys = true }
    private val standardDbService = StandardDatabaseService(database)

    /**
     * 生成设计方案
     *
     * @param request 设计方案生成请求
     * @return 设计方案
     */
    suspend fun generateProposal(request: DesignProposalRequest): Result<DesignProposal> =
        withContext(Dispatchers.IO) {
            try {
                // 查询选中的标准信息
                val standardSummaries = standardDbService.getStandardSummaries(
                    request.selectedStandards.values.flatten()
                )

                // 构建系统提示词
                val systemPrompt = buildSystemPrompt(request.productType)

                // 构建用户消息，包含标准信息
                val userMessage = buildUserMessage(request, standardSummaries)

                // 调用LLM
                val response = llmClient.chat(
                    systemPrompt = systemPrompt,
                    userMessage = userMessage
                )

                // 解析响应
                val proposal = parseResponse(response, request)
                Result.success(proposal)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * 构建系统提示词
     */
    private fun buildSystemPrompt(productType: String): String {
        return """
            你是一位专业的儿童安全座椅设计专家，精通各种国际标准（如R129、R44、FMVSS213、GB27887等）。

            你的任务是根据用户选择的标准，生成完整的${productType}设计方案。

            你必须严格遵循以下输出格式，以JSON格式返回：

            {
              "applicableStandards": ["标准1", "标准2", ...],
              "basicFitData": {
                "dummyInfo": {
                  "heightRange": "身高范围（mm）",
                  "weightRange": "体重范围（kg）",
                  "installationDirection": "安装方向",
                  "dummyType": "假人类型（可选）"
                }
              },
              "designParameters": {
                "headrestHeightRange": "头枕高度范围（mm）",
                "seatWidth": "座宽（mm）",
                "envelope": "盒子Envelope（如：ISOFIX SIZE CLASS B1）",
                "sideImpactProtectionArea": "侧防面积（mm²）",
                "additionalParameters": {}
              },
              "testRequirements": {
                "frontalImpact": "正面碰撞要求",
                "sideImpactChestCompression": "侧撞胸部压缩要求",
                "harnessStrength": "织带强度要求",
                "additionalRequirements": {}
              },
              "standardTestItems": {
                "dynamicFrontal": "动态碰撞：正碰要求",
                "dynamicRear": "动态碰撞：后碰要求",
                "dynamicSide": "动态碰撞：侧碰要求",
                "flammability": "阻燃要求",
                "additionalTests": {}
              }
            }

            ## 生成规则：

            1. **适用标准**：必须使用用户选择的标准，不得添加未选择的标准
            2. **基础适配数据**：根据标准中定义的假人要求生成
            3. **设计参数**：
               - 头枕高度、座宽、侧防面积等参考GPS数据库
               - Envelope根据标准要求生成（如ISOFIX SIZE CLASS B1）
            4. **测试要求**：严格遵循标准中的测试要求
            5. **标准测试项**：包含标准要求的所有测试项

            ## 注意事项：

            - 所有数值必须准确对应标准要求
            - 保持技术术语的专业性和准确性
            - 确保JSON格式正确，可以被解析
            - 如果某个标准中没有某项要求，标注"该标准无此要求"或"参见其他标准"
        """.trimIndent()
    }

    /**
     * 构建用户消息
     */
    private fun buildUserMessage(
        request: DesignProposalRequest,
        standardSummaries: List<StandardSummary>
    ): String {
        val standardsInfo = buildString {
            request.selectedStandards.forEach { (productType, standardIds) ->
                append("\n## $productType 适用标准：\n")
                standardIds.forEach { standardId ->
                    val summary = standardSummaries.find { it.id == standardId }
                    if (summary != null) {
                        append("- ${summary.name} (版本: ${summary.version}, 地区: ${summary.region})\n")
                        append("  标准ID: $standardId\n")
                    } else {
                        append("- $standardId\n")
                    }
                }
            }
        }

        val userInputInfo = buildString {
            request.userInputDummyInfo?.let { dummy ->
                append("\n## 用户输入信息：\n")
                dummy.targetHeightRange?.let { append("- 目标身高范围：$it\n") }
                dummy.targetWeightRange?.let { append("- 目标体重范围：$it\n") }
                dummy.targetInstallationDirection?.let { append("- 目标安装方向：$it\n") }
            }

            if (request.additionalRequirements.isNotEmpty()) {
                append("\n## 其他需求：\n")
                request.additionalRequirements.forEach { requirement ->
                    append("- $requirement\n")
                }
            }
        }

        return """
            请根据以下信息生成${request.productType}的设计方案：

            $standardsInfo

            $userInputInfo

            请严格按照要求的JSON格式返回设计方案。
        """.trimIndent()
    }

    /**
     * 解析LLM响应
     */
    private fun parseResponse(
        response: String,
        request: DesignProposalRequest
    ): DesignProposal {
        // 尝试从响应中提取JSON
        val jsonString = extractJson(response)

        // 解析为最终数据模型
        return json.decodeFromString<DesignProposal>(jsonString)
    }

    /**
     * 从响应中提取JSON
     */
    private fun extractJson(response: String): String {
        // 查找JSON的开始和结束
        val startIdx = response.indexOf("{")
        val endIdx = response.lastIndexOf("}")

        if (startIdx == -1 || endIdx == -1) {
            throw IllegalArgumentException("响应中未找到有效的JSON格式")
        }

        return response.substring(startIdx, endIdx + 1)
    }
}

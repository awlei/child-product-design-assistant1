package com.childproduct.designassistant.ai

import android.util.Log
import com.childproduct.designassistant.ai.PromptTemplate
import com.childproduct.designassistant.model.EnhancedProductType
import com.childproduct.designassistant.model.InternationalStandard
import com.childproduct.designassistant.network.llm.LLMClient
import com.childproduct.designassistant.network.llm.LLMException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 设计协助服务
 * 为工程师提供实时的设计建议、标准查询和问题诊断
 */
class DesignAssistantService private constructor() {

    companion object {
        private const val TAG = "DesignAssistantService"

        @Volatile
        private var instance: DesignAssistantService? = null

        /**
         * 获取单例实例
         */
        fun getInstance(): DesignAssistantService {
            return instance ?: synchronized(this) {
                instance ?: DesignAssistantService().also { instance = it }
            }
        }
    }

    private val llmClient = LLMClient.getInstance()

    /**
     * 生成设计建议
     *
     * @param productType 产品类型
     * @param standards 适用标准
     * @param heightRange 身高范围（可选）
     * @param customRequest 自定义需求（可选）
     * @return 设计建议
     */
    suspend fun generateDesignAdvice(
        productType: EnhancedProductType,
        standards: List<InternationalStandard>,
        heightRange: String? = null,
        customRequest: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "生成设计建议: productType=$productType, standards=$standards")

            val userPrompt = PromptTemplate.generateDesignAdvicePrompt(
                productType,
                standards,
                heightRange,
                customRequest
            )

            val response = llmClient.simpleChat(
                systemPrompt = PromptTemplate.getSystemPrompt(),
                userPrompt = userPrompt,
                temperature = 0.7f
            )

            Log.d(TAG, "设计建议生成成功")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "生成设计建议失败", e)
            Result.failure(e)
        }
    }

    /**
     * 查询标准信息
     *
     * @param standardCode 标准代码
     * @param queryType 查询类型（如：条款、参数、测试方法等）
     * @param keywords 关键词列表
     * @return 查询结果
     */
    suspend fun queryStandard(
        standardCode: String,
        queryType: String,
        keywords: List<String>
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "查询标准: standardCode=$standardCode, queryType=$queryType")

            val userPrompt = PromptTemplate.generateStandardQueryPrompt(
                standardCode,
                queryType,
                keywords
            )

            val response = llmClient.simpleChat(
                systemPrompt = PromptTemplate.getSystemPrompt(),
                userPrompt = userPrompt,
                temperature = 0.5f // 查询标准使用较低温度，确保准确性
            )

            Log.d(TAG, "标准查询成功")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "标准查询失败", e)
            Result.failure(e)
        }
    }

    /**
     * 诊断问题
     *
     * @param problemDescription 问题描述
     * @param context 上下文信息（可选）
     * @param productType 产品类型（可选）
     * @param standards 适用标准（可选）
     * @return 诊断报告
     */
    suspend fun diagnoseProblem(
        problemDescription: String,
        context: String? = null,
        productType: EnhancedProductType? = null,
        standards: List<InternationalStandard>? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "诊断问题: problemDescription=$problemDescription")

            val userPrompt = PromptTemplate.generateProblemDiagnosisPrompt(
                problemDescription,
                context,
                productType,
                standards
            )

            val response = llmClient.simpleChat(
                systemPrompt = PromptTemplate.getSystemPrompt(),
                userPrompt = userPrompt,
                temperature = 0.6f // 问题诊断使用中等温度
            )

            Log.d(TAG, "问题诊断成功")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "问题诊断失败", e)
            Result.failure(e)
        }
    }

    /**
     * 聊天对话（通用）
     *
     * @param userMessage 用户消息
     * @param conversationHistory 对话历史（可选）
     * @return AI回复
     */
    suspend fun chat(
        userMessage: String,
        conversationHistory: List<ChatMessage>? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "聊天: userMessage=$userMessage")

            val systemPrompt = PromptTemplate.getSystemPrompt()

            // 构建消息列表（如果提供了历史记录）
            val messages = mutableListOf<String>()
            messages.add(systemPrompt)

            conversationHistory?.forEach { msg ->
                when (msg.role) {
                    ChatRole.USER -> messages.add(msg.content)
                    ChatRole.ASSISTANT -> messages.add(msg.content)
                    ChatRole.SYSTEM -> { /* 系统消息已经单独处理，忽略 */ }
                }
            }

            messages.add(userMessage)

            val response = llmClient.simpleChat(
                systemPrompt = systemPrompt,
                userPrompt = messages.joinToString("\n\n"),
                temperature = 0.8f // 聊天使用较高温度，增加灵活性
            )

            Log.d(TAG, "聊天成功")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "聊天失败", e)
            Result.failure(e)
        }
    }

    /**
     * 检查服务是否可用
     */
    suspend fun isServiceAvailable(): Boolean {
        return try {
            val response = llmClient.simpleChat(
                systemPrompt = PromptTemplate.getSystemPrompt(),
                userPrompt = "你好，请回复'服务正常'",
                temperature = 0.5f
            )
            response.isNotEmpty()
        } catch (e: Exception) {
            Log.e(TAG, "服务不可用", e)
            false
        }
    }
}

/**
 * 聊天消息
 */
data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val role: ChatRole,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * 聊天角色
 */
enum class ChatRole {
    SYSTEM,
    USER,
    ASSISTANT
}

package com.childproduct.designassistant.network.llm

import com.google.gson.annotations.SerializedName

/**
 * 大模型API响应模型
 */
data class LLMResponse(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("object")
    val objectType: String? = null,

    @SerializedName("created")
    val created: Long? = null,

    @SerializedName("model")
    val model: String? = null,

    @SerializedName("choices")
    val choices: List<Choice>? = null,

    @SerializedName("usage")
    val usage: Usage? = null,

    @SerializedName("error")
    val error: Error? = null
)

/**
 * 选择结果
 */
data class Choice(
    @SerializedName("index")
    val index: Int? = null,

    @SerializedName("message")
    val message: Message? = null,

    @SerializedName("finish_reason")
    val finishReason: String? = null
)

/**
 * 使用情况
 */
data class Usage(
    @SerializedName("prompt_tokens")
    val promptTokens: Int? = null,

    @SerializedName("completion_tokens")
    val completionTokens: Int? = null,

    @SerializedName("total_tokens")
    val totalTokens: Int? = null
)

/**
 * 错误信息
 */
data class Error(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("code")
    val code: String? = null
)

/**
 * 响应是否成功
 */
fun LLMResponse.isSuccess(): Boolean {
    return error == null && choices != null && choices.isNotEmpty()
}

/**
 * 获取响应文本
 */
fun LLMResponse.getText(): String {
    return choices?.firstOrNull()?.message?.content?.let { content ->
        when (content) {
            is Content.Text -> content.text
            else -> content.toString()
        }
    } ?: ""
}

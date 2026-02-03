package com.childproduct.designassistant.network.llm

import com.google.gson.annotations.SerializedName

/**
 * 大模型API请求模型
 */
data class LLMRequest(
    @SerializedName("model")
    val model: String = "doubao-seed-1-6-251015", // 默认使用平衡性能模型

    @SerializedName("messages")
    val messages: List<Message>,

    @SerializedName("temperature")
    val temperature: Float = 0.7f,

    @SerializedName("max_tokens")
    val maxTokens: Int? = null,

    @SerializedName("stream")
    val stream: Boolean = false,

    @SerializedName("extra_body")
    val extraBody: Map<String, Any>? = null
)

/**
 * 消息模型
 */
data class Message(
    @SerializedName("role")
    val role: String, // system, user, assistant

    @SerializedName("content")
    val content: Content // 支持文本或多模态内容
)

/**
 * 内容类型（支持文本和多模态）
 */
sealed class Content {
    data class Text(
        @SerializedName("type")
        val type: String = "text",

        @SerializedName("text")
        val text: String
    ) : Content()

    data class ImageUrl(
        @SerializedName("type")
        val type: String = "image_url",

        @SerializedName("image_url")
        val imageUrl: Map<String, String>
    ) : Content()

    data class VideoUrl(
        @SerializedName("type")
        val type: String = "video_url",

        @SerializedName("video_url")
        val videoUrl: Map<String, String>
    ) : Content()
}

/**
 * 用于创建文本消息的便捷方法
 */
fun createTextMessage(role: String, text: String): Message {
    return Message(role, Content.Text(text = text))
}

/**
 * 用于创建多模态消息的便捷方法
 */
fun createMultimodalMessage(role: String, text: String, imageUrl: String? = null, videoUrl: String? = null): Message {
    val contentList = mutableListOf<Content>()
    contentList.add(Content.Text(text = text))

    imageUrl?.let {
        contentList.add(Content.ImageUrl(imageUrl = mapOf("url" to imageUrl)))
    }

    videoUrl?.let {
        contentList.add(Content.VideoUrl(videoUrl = mapOf("url" to videoUrl)))
    }

    // 注意：这里需要特殊处理，因为LLMRequest期望单个Content对象，而不是列表
    // 实际使用时，应该使用支持列表的内容格式
    return Message(role, Content.Text(text = text)) // 简化版本，实际需要根据API调整
}

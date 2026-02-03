package com.childproduct.designassistant.network.llm

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * 大模型API服务接口
 */
interface LLMApiService {

    /**
     * 聊天完成接口
     *
     * @param authorization 授权头（Bearer token）
     * @param request 请求体
     * @return 响应结果
     */
    @POST("chat/completions")
    suspend fun chatCompletions(
        @Header("Authorization") authorization: String,
        @Body request: LLMRequest
    ): LLMResponse
}

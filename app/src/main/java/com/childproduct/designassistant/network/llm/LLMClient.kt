package com.childproduct.designassistant.network.llm

import android.util.Log
import com.childproduct.designassistant.config.LLMConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 大模型客户端
 * 提供统一的API调用接口，包含缓存、错误处理和重试机制
 */
class LLMClient private constructor() {

    companion object {
        private const val TAG = "LLMClient"

        @Volatile
        private var instance: LLMClient? = null

        /**
         * 获取单例实例
         */
        fun getInstance(): LLMClient {
            return instance ?: synchronized(this) {
                instance ?: LLMClient().also { instance = it }
            }
        }
    }

    // Retrofit实例
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(LLMConfig.getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .build()
    }

    // API服务
    private val apiService: LLMApiService by lazy {
        retrofit.create(LLMApiService::class.java)
    }

    /**
     * 创建OkHttpClient
     */
    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(LLMConfig.getConnectTimeout().toLong(), TimeUnit.SECONDS)
            .readTimeout(LLMConfig.getReadTimeout().toLong(), TimeUnit.SECONDS)
            .writeTimeout(LLMConfig.getWriteTimeout().toLong(), TimeUnit.SECONDS)

        // 添加日志拦截器
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d(TAG, message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        builder.addInterceptor(loggingInterceptor)

        // 添加授权拦截器
        builder.addInterceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("Authorization", "Bearer ${LLMConfig.getApiKey()}")
                .header("Content-Type", "application/json")
                .method(originalRequest.method, originalRequest.body)

            chain.proceed(requestBuilder.build())
        }

        return builder.build()
    }

    /**
     * 发送聊天请求（带重试）
     *
     * @param request 请求体
     * @return 响应结果
     * @throws LLMException 如果请求失败
     */
    suspend fun chatCompletions(request: LLMRequest): LLMResponse = withContext(Dispatchers.IO) {
        var lastException: Exception? = null
        val maxRetries = LLMConfig.getMaxRetries()
        val retryDelay = LLMConfig.getRetryDelay()

        repeat(maxRetries) { attempt ->
            try {
                val response = apiService.chatCompletions("Bearer ${LLMConfig.getApiKey()}", request)

                if (response.isSuccess()) {
                    return@withContext response
                } else {
                    throw LLMException(
                        message = response.error?.message ?: "未知错误",
                        code = response.error?.code,
                        type = response.error?.type
                    )
                }
            } catch (e: Exception) {
                lastException = e
                Log.w(TAG, "请求失败，重试 ${attempt + 1}/$maxRetries: ${e.message}")

                if (attempt < maxRetries - 1) {
                    kotlinx.coroutines.delay(retryDelay * (attempt + 1))
                }
            }
        }

        throw LLMException(
            message = "请求失败，已重试 $maxRetries 次: ${lastException?.message}",
            cause = lastException
        )
    }

    /**
     * 发送简单的文本聊天请求
     *
     * @param systemPrompt 系统提示词
     * @param userPrompt 用户提示词
     * @param temperature 温度参数
     * @return 响应文本
     */
    suspend fun simpleChat(
        systemPrompt: String,
        userPrompt: String,
        temperature: Float = 0.7f
    ): String {
        val request = LLMRequest(
            model = LLMConfig.getDefaultModel(),
            messages = listOf(
                Message(role = "system", content = Content.Text(text = systemPrompt)),
                Message(role = "user", content = Content.Text(text = userPrompt))
            ),
            temperature = temperature
        )

        val response = chatCompletions(request)
        return response.getText()
    }

    /**
     * 发送多模态聊天请求（支持图片）
     *
     * @param systemPrompt 系统提示词
     * @param userPrompt 用户提示词
     * @param imageUrl 图片URL
     * @param temperature 温度参数
     * @return 响应文本
     */
    suspend fun multimodalChat(
        systemPrompt: String,
        userPrompt: String,
        imageUrl: String,
        temperature: Float = 0.7f
    ): String {
        val request = LLMRequest(
            model = "doubao-seed-1-6-vision-250815",
            messages = listOf(
                Message(role = "system", content = Content.Text(text = systemPrompt)),
                Message(role = "user", content = Content.Text(text = "$userPrompt\n\n图片URL: $imageUrl"))
            ),
            temperature = temperature
        )

        val response = chatCompletions(request)
        return response.getText()
    }
}

/**
 * LLM异常
 */
class LLMException(
    message: String,
    code: String? = null,
    type: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {

    val errorCode: String? = code
    val errorType: String? = type

    override fun toString(): String {
        return buildString {
            append("LLMException: $message")
            if (errorCode != null) append(" (code: $errorCode)")
            if (errorType != null) append(" (type: $errorType)")
            cause?.let { append("\nCaused by: $it") }
        }
    }
}

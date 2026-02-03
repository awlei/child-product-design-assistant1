package com.childproduct.designassistant.config

import android.content.Context
import android.util.Log
import java.io.InputStream
import java.util.Properties

/**
 * LLM配置管理器
 * 从assets目录读取配置文件
 */
object LLMConfig {

    private const val TAG = "LLMConfig"
    private const val CONFIG_FILE = "config/llm_config.properties"

    // 配置项
    private var properties: Properties? = null
    private var initialized = false

    /**
     * 初始化配置
     */
    fun init(context: Context) {
        if (initialized) return

        try {
            val inputStream: InputStream = context.assets.open(CONFIG_FILE)
            val props = Properties()
            props.load(inputStream)
            inputStream.close()

            properties = props
            initialized = true

            Log.i(TAG, "LLM配置加载成功")
        } catch (e: Exception) {
            Log.e(TAG, "加载LLM配置失败", e)
            // 提供默认配置
            properties = Properties().apply {
                setProperty("LLM_BASE_URL", "https://ark.cn-beijing.volces.com/api/v3/")
                setProperty("LLM_API_KEY", "")
                setProperty("LLM_DEFAULT_MODEL", "doubao-seed-1-6-251015")
            }
            initialized = true
        }
    }

    /**
     * 获取基础URL
     */
    fun getBaseUrl(): String {
        return properties?.getProperty("LLM_BASE_URL") ?: "https://ark.cn-beijing.volces.com/api/v3/"
    }

    /**
     * 获取API密钥
     */
    fun getApiKey(): String {
        return properties?.getProperty("LLM_API_KEY") ?: ""
    }

    /**
     * 获取默认模型
     */
    fun getDefaultModel(): String {
        return properties?.getProperty("LLM_DEFAULT_MODEL") ?: "doubao-seed-1-6-251015"
    }

    /**
     * 获取连接超时（秒）
     */
    fun getConnectTimeout(): Int {
        return properties?.getProperty("LLM_CONNECT_TIMEOUT")?.toIntOrNull() ?: 30
    }

    /**
     * 获取读取超时（秒）
     */
    fun getReadTimeout(): Int {
        return properties?.getProperty("LLM_READ_TIMEOUT")?.toIntOrNull() ?: 60
    }

    /**
     * 获取写入超时（秒）
     */
    fun getWriteTimeout(): Int {
        return properties?.getProperty("LLM_WRITE_TIMEOUT")?.toIntOrNull() ?: 60
    }

    /**
     * 获取最大重试次数
     */
    fun getMaxRetries(): Int {
        return properties?.getProperty("LLM_MAX_RETRIES")?.toIntOrNull() ?: 3
    }

    /**
     * 获取重试延迟（毫秒）
     */
    fun getRetryDelay(): Long {
        return properties?.getProperty("LLM_RETRY_DELAY_MS")?.toLongOrNull() ?: 1000L
    }

    /**
     * 检查配置是否有效
     */
    fun isValid(): Boolean {
        return initialized && !getApiKey().isNullOrEmpty()
    }
}

package com.childproduct.designassistant.common

/**
 * 统一的验证结果类
 * 用于避免在多个文件中重复定义
 */
data class ValidationResult(
    val isValid: Boolean,
    val errors: List<Any> = emptyList(),
    val warnings: List<Any> = emptyList(),
    val errorMessage: String? = null,
    val warningMessage: String? = null
) {
    companion object {
        /**
         * 创建成功的验证结果
         */
        fun success() = ValidationResult(isValid = true)

        /**
         * 创建失败的验证结果（使用字符串）
         */
        fun error(message: String) = ValidationResult(
            isValid = false,
            errorMessage = message
        )

        /**
         * 创建失败的验证结果（使用列表）
         */
        fun errorList(errors: List<Any>) = ValidationResult(
            isValid = false,
            errors = errors
        )

        /**
         * 创建带警告的验证结果（使用字符串）
         */
        fun warning(message: String) = ValidationResult(
            isValid = true,
            warningMessage = message
        )

        /**
         * 创建带警告的验证结果（使用列表）
         */
        fun warningList(warnings: List<Any>) = ValidationResult(
            isValid = true,
            warnings = warnings
        )

        /**
         * 创建失败的验证结果（使用错误和警告列表）
         */
        fun failure(errors: List<Any>, warnings: List<Any> = emptyList()) = ValidationResult(
            isValid = false,
            errors = errors,
            warnings = warnings
        )

        /**
         * 创建成功的验证结果（带警告）
         */
        fun success(warnings: List<Any>) = ValidationResult(
            isValid = true,
            warnings = warnings
        )
    }

    /**
     * 获取错误摘要（最多显示指定数量的错误）
     */
    fun getErrorSummary(maxCount: Int = 3): String {
        if (errorMessage != null) return errorMessage
        val errorList = errors.take(maxCount)
        return when {
            errorList.isEmpty() -> "无错误"
            errorList.size < errors.size -> errorList.joinToString("; ") { it.toString() } + "; ..."
            else -> errorList.joinToString("; ") { it.toString() }
        }
    }

    /**
     * 获取警告摘要（最多显示指定数量的警告）
     */
    fun getWarningSummary(maxCount: Int = 3): String {
        if (warningMessage != null) return warningMessage
        val warningList = warnings.take(maxCount)
        return when {
            warningList.isEmpty() -> "无警告"
            warningList.size < warnings.size -> warningList.joinToString("; ") { it.toString() } + "; ..."
            else -> warningList.joinToString("; ") { it.toString() }
        }
    }
}

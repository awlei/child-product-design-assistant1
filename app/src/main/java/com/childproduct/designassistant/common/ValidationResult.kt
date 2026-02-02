package com.childproduct.designassistant.common

/**
 * 统一的验证结果类
 * 用于避免在多个文件中重复定义
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null,
    val warningMessage: String? = null
) {
    companion object {
        /**
         * 创建成功的验证结果
         */
        fun success() = ValidationResult(isValid = true)

        /**
         * 创建失败的验证结果
         */
        fun error(message: String) = ValidationResult(
            isValid = false,
            errorMessage = message
        )

        /**
         * 创建带警告的验证结果
         */
        fun warning(message: String) = ValidationResult(
            isValid = true,
            warningMessage = message
        )
    }
}

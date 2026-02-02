package com.childproduct.designassistant.validator

import com.childproduct.designassistant.config.StandardConfig
import com.childproduct.designassistant.helper.SchemeOptimizer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * 统一验证接口（所有验证逻辑实现此接口）
 *
 * 优化目标：
 * - 统一验证接口，避免验证逻辑分散
 * - 支持多种验证器组合
 * - 使用不可变集合存储错误和警告信息
 * - 提供清晰的验证结果反馈
 */
interface Validator<T> {
    fun validate(target: T): com.childproduct.designassistant.common.ValidationResult
}

/**
 * 身高验证器（实现统一接口）
 */
class HeightValidator : Validator<String> {
    override fun validate(target: String): com.childproduct.designassistant.common.ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // 格式校验
        val heightPattern = Regex("""^\d{2,3}-\d{2,3}cm$""")
        if (!heightPattern.matches(target)) {
            errors.add("身高范围格式错误，正确格式如：40-150cm")
        } else {
            // 数值校验
            val (min, max) = target.replace("cm", "").split("-").map { it.toInt() }
            if (min >= max) {
                errors.add("最小身高$min cm必须小于最大身高$max cm")
            }
            if (min < 40) {
                warnings.add("最小身高$min cm低于${StandardConfig.STANDARD_VERSION}标准下限（40cm）")
            }
            if (max > 150) {
                warnings.add("最大身高$max cm超过${StandardConfig.STANDARD_VERSION}标准上限（150cm）")
            }

            // 标准合规性校验
            if (!StandardConfig.isValidHeightRange(target)) {
                warnings.add("身高范围${target}未在${StandardConfig.STANDARD_VERSION}标准映射表中，可能需要额外测试验证")
            }
        }

        return when {
            errors.isNotEmpty() -> com.childproduct.designassistant.common.ValidationResult.failure(errors, warnings)
            warnings.isNotEmpty() -> com.childproduct.designassistant.common.ValidationResult.success(warnings)
            else -> com.childproduct.designassistant.common.ValidationResult.success()
        }
    }
}

/**
 * 产品输入验证器（聚合多个验证器）
 */
class ProductInputValidator : Validator<SchemeOptimizer.UserInput> {
    private val heightValidator = HeightValidator()
    private val themeValidator = ThemeKeywordValidator()

    override fun validate(target: SchemeOptimizer.UserInput): com.childproduct.designassistant.common.ValidationResult {
        val allErrors = mutableListOf<String>()
        val allWarnings = mutableListOf<String>()

        // 基础非空校验
        if (target.productType.isBlank()) {
            allErrors.add("产品类型不能为空")
        }

        // 主题关键词校验
        val themeResult = themeValidator.validate(target.themeKeyword)
        allErrors.addAll(themeResult.errors.map { it.toString() })
        allWarnings.addAll(themeResult.warnings.map { it.toString() })

        // 身高校验
        val heightResult = heightValidator.validate(target.heightRange)
        allErrors.addAll(heightResult.errors.map { it.toString() })
        allWarnings.addAll(heightResult.warnings.map { it.toString() })

        return when {
            allErrors.isNotEmpty() -> com.childproduct.designassistant.common.ValidationResult.failure(allErrors, allWarnings)
            allWarnings.isNotEmpty() -> com.childproduct.designassistant.common.ValidationResult.success(allWarnings)
            else -> com.childproduct.designassistant.common.ValidationResult.success()
        }
    }
}

/**
 * 主题关键词验证器
 */
class ThemeKeywordValidator : Validator<String> {
    override fun validate(target: String): com.childproduct.designassistant.common.ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // 非空校验
        if (target.isBlank()) {
            errors.add("设计主题不能为空")
            return com.childproduct.designassistant.common.ValidationResult.failure(errors, warnings)
        }

        // 长度校验
        if (target.length > 50) {
            warnings.add("设计主题过长，建议不超过50个字符")
        }

        // 特殊字符校验
        val invalidChars = target.filter { !it.isLetterOrDigit() && !it.isWhitespace() }
        if (invalidChars.isNotEmpty() && invalidChars.length > target.length * 0.3) {
            warnings.add("设计主题包含过多特殊字符，建议使用简洁描述")
        }

        return when {
            errors.isNotEmpty() -> com.childproduct.designassistant.common.ValidationResult.failure(errors, warnings)
            warnings.isNotEmpty() -> com.childproduct.designassistant.common.ValidationResult.success(warnings)
            else -> com.childproduct.designassistant.common.ValidationResult.success()
        }
    }
}

/**
 * 设计方案验证器
 */
class DesignSchemeValidator : Validator<com.childproduct.designassistant.model.ChildProductDesignScheme> {
    override fun validate(target: com.childproduct.designassistant.model.ChildProductDesignScheme): com.childproduct.designassistant.common.ValidationResult {
        // 复用模型自带的验证逻辑
        val modelValidation = target.validate()

        val errors = modelValidation.errors.toMutableList()
        val warnings = mutableListOf<String>()

        // 额外的业务验证
        // 检查核心特点是否包含必需项
        val requiredFeatures = listOf("易安装性", "安全性", "舒适性", "材质环保")
        val missingFeatures = requiredFeatures.filter { required ->
            target.coreFeatures.none { feature -> feature.contains(required) }
        }
        if (missingFeatures.isNotEmpty()) {
            warnings.add("核心特点可能缺少以下内容：${missingFeatures.joinToString("、")}")
        }

        // 检查安全注意事项是否完整
        val requiredNotes = listOf("防吞咽", "材质安全", "边缘安全")
        val missingNotes = requiredNotes.filter { required ->
            target.safetyNotes.none { note -> note.contains(required) }
        }
        if (missingNotes.isNotEmpty()) {
            warnings.add("安全注意事项可能缺少以下内容：${missingNotes.joinToString("、")}")
        }

        return when {
            errors.isNotEmpty() -> com.childproduct.designassistant.common.ValidationResult.failure(errors, warnings)
            warnings.isNotEmpty() -> com.childproduct.designassistant.common.ValidationResult.success(warnings)
            else -> com.childproduct.designassistant.common.ValidationResult.success()
        }
    }
}

/**
 * 验证器工厂（创建不同类型的验证器）
 */
object ValidatorFactory {
    /**
     * 获取身高验证器
     */
    fun heightValidator(): HeightValidator = HeightValidator()

    /**
     * 获取产品输入验证器
     */
    fun productInputValidator(): ProductInputValidator = ProductInputValidator()

    /**
     * 获取主题关键词验证器
     */
    fun themeKeywordValidator(): ThemeKeywordValidator = ThemeKeywordValidator()

    /**
     * 获取设计方案验证器
     */
    fun designSchemeValidator(): DesignSchemeValidator = DesignSchemeValidator()
}

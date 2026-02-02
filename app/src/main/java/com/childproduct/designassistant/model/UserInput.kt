package com.childproduct.designassistant.model

import com.childproduct.designassistant.common.ValidationResult
import com.childproduct.designassistant.model.ProductType
import com.childproduct.designassistant.model.InternationalStandard.*
import com.childproduct.designassistant.model.UsageScenario

/**
 * 用户输入数据 - 儿童安全座椅
 * 支持极简输入：仅需身高或重量范围，系统自动匹配标准
 */
data class SafetySeatInput(
    // 基本信息（系统自动识别，用户可选）
    val standard: InternationalStandard? = null,
    val subtype: SafetySeatSubtype? = null,

    // 核心参数（仅需输入其一）
    val heightRange: String? = null,  // 身高范围，如 "60-105cm"（优先）
    val weightRange: String? = null,  // 重量范围，如 "9-18kg" 或 "22-40lb"
    val ageGroup: AgeGroup? = null,   // 年龄组（可选）

    // 特征选择（可选）
    val customFeatures: List<String> = emptyList(),

    // 专项需求（可选）
    val specificRequirements: List<String> = emptyList(),

    // 额外信息（可选）
    val customNotes: String? = null,

    // 极简输入标识
    val isSimplifiedInput: Boolean = true
)

/**
 * 用户输入数据 - 婴儿推车
 * 支持极简输入：仅需承重范围，系统自动匹配标准
 */
data class StrollerInput(
    // 基本信息（系统自动识别，用户可选）
    val standard: InternationalStandard? = null,
    val subtype: StrollerSubtype? = null,

    // 核心参数
    val weightCapacityRange: String? = null,  // 承重范围，如 "0-15kg"（必需）
    val foldedDimensions: String? = null,      // 折叠后尺寸限制，如 "50×30×20cm"（可选）
    val usageScenario: UsageScenario? = null,  // 使用场景（可选）

    // 特征选择（可选）
    val customFeatures: List<String> = emptyList(),

    // 专项需求（可选）
    val specificRequirements: List<String> = emptyList(),

    // 额外信息（可选）
    val customNotes: String? = null,

    // 极简输入标识
    val isSimplifiedInput: Boolean = true
)

/**
 * 用户输入数据 - 儿童家庭用品
 */
data class HouseholdGoodInput(
    // 基本信息
    val productCategory: String,
    
    // 核心参数
    val targetAgeGroup: AgeGroup,
    val usageScenario: String,
    
    // 特征选择
    val keyFeatures: List<String> = emptyList(),
    
    // 专项需求
    val specificRequirements: List<String> = emptyList(),
    
    // 额外信息
    val customNotes: String? = null
)

/**
 * 统一用户输入容器
 */
sealed class ProductInput {
    data class SafetySeat(val data: SafetySeatInput) : ProductInput()
    data class Stroller(val data: StrollerInput) : ProductInput()
    data class HouseholdGood(val data: HouseholdGoodInput) : ProductInput()
}

/**
 * 校验错误
 */
data class ValidationError(
    val field: String,
    val message: String,
    val severity: ErrorSeverity
)

/**
 * 错误严重程度
 */
enum class ErrorSeverity {
    CRITICAL,  // 严重错误，必须修复
    ERROR,     // 一般错误
    WARNING    // 警告
}

/**
 * 校验警告
 */
data class ValidationWarning(
    val field: String,
    val message: String,
    val suggestion: String?
)

/**
 * 输入历史记录
 */
data class InputHistory(
    val id: String,
    val timestamp: Long,
    val productType: ProductType,
    val standard: InternationalStandard?,
    val summary: String,
    val inputData: ProductInput
)

/**
 * 产品特征标签
 */
val COMMON_SAFETY_SEAT_FEATURES = listOf(
    "可折叠高景观推车",
    "后向安装安全座椅",
    "ISOFIX硬连接",
    "360度旋转",
    "侧面碰撞防护",
    "头托多档调节",
    "吸能底座",
    "SensorSafe智能监测"
)

val COMMON_STROLLER_FEATURES = listOf(
    "一键折叠",
    "四轮避震",
    "可调节座垫",
    "超大储物篮",
    "适配安全座椅",
    "可拆卸遮阳篷",
    "轻量化设计",
    "转向灵活"
)

val COMMON_HOUSEHOLD_GOODS_FEATURES = listOf(
    "安全圆角设计",
    "环保材质",
    "易清洁表面",
    "防滑底部",
    "可调节高度",
    "储物功能",
    "便携式设计",
    "防倾倒"
)

/**
 * 常见专项需求 - 儿童安全座椅
 */
val COMMON_SAFETY_SEAT_REQUIREMENTS = listOf(
    "优化头托侧面碰撞防护",
    "符合i-Size Envelope内部尺寸",
    "提升安装便捷性",
    "增强侧面支撑结构",
    "优化通风散热性能",
    "降低座椅重量",
    "提升座椅舒适度",
    "优化安全带调节系统"
)

/**
 * 常见专项需求 - 婴儿推车
 */

/**
 * 输入校验工具类
 */
object InputValidator {
    
    /**
     * 校验身高范围
     */
    fun validateHeightRange(input: String): ValidationResult {
        val errors = mutableListOf<ValidationError>()
        val warnings = mutableListOf<ValidationWarning>()
        
        try {
            val parts = input.split("-").map { it.replace("cm", "").trim() }
            if (parts.size != 2) {
                errors.add(ValidationError(
                    field = "heightRange",
                    message = "身高范围格式错误，应为：最小值-最大值（如：60-105cm）",
                    severity = ErrorSeverity.ERROR
                ))
                return ValidationResult(false, errors, warnings)
            }
            
            val minHeight = parts[0].toDouble()
            val maxHeight = parts[1].toDouble()
            
            if (minHeight <= 0 || maxHeight <= 0) {
                errors.add(ValidationError(
                    field = "heightRange",
                    message = "身高必须为正数",
                    severity = ErrorSeverity.ERROR
                ))
            }
            
            if (minHeight >= maxHeight) {
                errors.add(ValidationError(
                    field = "heightRange",
                    message = "最小身高必须小于最大身高",
                    severity = ErrorSeverity.ERROR
                ))
            }
            
            if (minHeight < 40 || maxHeight > 150) {
                warnings.add(ValidationWarning(
                    field = "heightRange",
                    message = "身高范围超出常规范围（40-150cm）",
                    suggestion = "请确认输入是否正确"
                ))
            }
            
            // ECE R129后向安装检查
            if (maxHeight > 75) {
                warnings.add(ValidationWarning(
                    field = "heightRange",
                    message = "身高超过75cm，建议使用前向安装模式",
                    suggestion = "ECE R129规定Group 0+建议后向安装至75cm/15个月"
                ))
            }
            
        } catch (e: NumberFormatException) {
            errors.add(ValidationError(
                field = "heightRange",
                message = "身高数值格式错误，请输入数字",
                severity = ErrorSeverity.ERROR
            ))
        }
        
        return ValidationResult(errors.isEmpty(), errors, warnings)
    }
    
    /**
     * 校验重量范围
     */
    fun validateWeightRange(input: String, unit: WeightUnit = WeightUnit.KG): ValidationResult {
        val errors = mutableListOf<ValidationError>()
        val warnings = mutableListOf<ValidationWarning>()
        
        try {
            val parts = input.split("-")
                .map { 
                    it.replace("kg", "", ignoreCase = true)
                     .replace("lb", "", ignoreCase = true)
                     .replace("磅", "", ignoreCase = true)
                     .trim() 
                }
            
            if (parts.size != 2) {
                errors.add(ValidationError(
                    field = "weightRange",
                    message = "重量范围格式错误，应为：最小值-最大值（如：9-18kg）",
                    severity = ErrorSeverity.ERROR
                ))
                return ValidationResult(false, errors, warnings)
            }
            
            val minWeight = parts[0].toDouble() * unit.toKgFactor
            val maxWeight = parts[1].toDouble() * unit.toKgFactor
            
            if (minWeight <= 0 || maxWeight <= 0) {
                errors.add(ValidationError(
                    field = "weightRange",
                    message = "重量必须为正数",
                    severity = ErrorSeverity.ERROR
                ))
            }
            
            if (minWeight >= maxWeight) {
                errors.add(ValidationError(
                    field = "weightRange",
                    message = "最小重量必须小于最大重量",
                    severity = ErrorSeverity.ERROR
                ))
            }
            
            if (maxWeight > 36) {
                warnings.add(ValidationWarning(
                    field = "weightRange",
                    message = "最大重量超过常规儿童座椅范围（36kg）",
                    suggestion = "请确认是否为增高垫产品"
                ))
            }
            
        } catch (e: NumberFormatException) {
            errors.add(ValidationError(
                field = "weightRange",
                message = "重量数值格式错误，请输入数字",
                severity = ErrorSeverity.ERROR
            ))
        }
        
        return ValidationResult(errors.isEmpty(), errors, warnings)
    }
    
    /**
     * 校验折叠尺寸
     */
    fun validateFoldedDimensions(input: String): ValidationResult {
        val errors = mutableListOf<ValidationError>()
        val warnings = mutableListOf<ValidationWarning>()
        
        try {
            val parts = input.split("×")
                .map { 
                    it.replace("cm", "", ignoreCase = true)
                     .trim() 
                }
            
            if (parts.size != 3) {
                errors.add(ValidationError(
                    field = "foldedDimensions",
                    message = "折叠尺寸格式错误，应为：长×宽×高（如：50×30×20cm）",
                    severity = ErrorSeverity.ERROR
                ))
                return ValidationResult(false, errors, warnings)
            }
            
            val width = parts[0].toDouble()
            val depth = parts[1].toDouble()
            val height = parts[2].toDouble()
            
            if (width <= 0 || depth <= 0 || height <= 0) {
                errors.add(ValidationError(
                    field = "foldedDimensions",
                    message = "尺寸必须为正数",
                    severity = ErrorSeverity.ERROR
                ))
            }
            
            // 车载收纳检查
            if (width > 50 || depth > 30 || height > 20) {
                warnings.add(ValidationWarning(
                    field = "foldedDimensions",
                    message = "折叠尺寸超过车载收纳推荐尺寸（50×30×20cm）",
                    suggestion = "可能无法放入普通轿车后备箱"
                ))
            }
            
        } catch (e: NumberFormatException) {
            errors.add(ValidationError(
                field = "foldedDimensions",
                message = "尺寸数值格式错误，请输入数字",
                severity = ErrorSeverity.ERROR
            ))
        }
        
        return ValidationResult(errors.isEmpty(), errors, warnings)
    }
}

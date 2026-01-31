package com.childproduct.designassistant.model

/**
 * 极简输入模型
 * 核心设计：仅需输入身高或重量范围，系统自动匹配对应法规
 */
data class SimplifiedInput(
    /**
     * 输入类型：身高或重量
     */
    val inputType: InputType,

    /**
     * 品类：儿童安全座椅或婴儿推车
     * 如果输入Type是HEIGHT_RANGE，系统自动识别为安全座椅
     * 如果输入Type是WEIGHT_RANGE，需要用户选择品类
     */
    val productType: ProductType? = null,

    /**
     * 身高范围（cm）
     * 用于安全座椅设计
     */
    val heightRange: HeightRange? = null,

    /**
     * 重量范围（kg或lb）
     * 可用于安全座椅或婴儿推车
     */
    val weightRange: WeightRange? = null,

    /**
     * 年龄范围（月）
     * 用于安全座椅或婴儿推车
     */
    val ageRange: AgeRange? = null,

    /**
     * 产品细分（可选）
     */
    val safetySeatSubtype: SafetySeatSubtype? = null,
    val strollerSubtype: StrollerSubtype? = null,

    /**
     * 使用场景（可选）
     */
    val usageScenario: UsageScenario? = null,

    /**
     * 专项设计需求（可选）
     * 如"优化头托侧撞防护"、"提升避震性能"等
     */
    val specialRequirements: List<String> = emptyList()
)

/**
 * 身高范围
 */
data class HeightRange(
    val minHeight: Double,  // cm
    val maxHeight: Double   // cm
) {
    /**
     * 校验输入范围是否有效
     */
    fun isValid(): Boolean {
        return minHeight > 0 && maxHeight > minHeight && maxHeight <= 150.0
    }

    /**
     * 获取提示信息
     */
    fun getValidationHint(): String? {
        return when {
            minHeight <= 0 -> "最小身高必须大于0"
            maxHeight <= minHeight -> "最小身高必须小于最大身高"
            maxHeight > 150 -> "身高范围超出常规范围（40-150cm）"
            minHeight < 40 -> "身高建议>=40cm（符合新生儿标准）"
            else -> null
        }
    }

    /**
     * 推荐标准
     */
    fun getRecommendedStandard(): InternationalStandard? {
        return when {
            minHeight < 75 -> InternationalStandard.ECE_R129  // Group 0+
            minHeight < 100 -> InternationalStandard.ECE_R129  // Group 0/1
            maxHeight > 100 -> InternationalStandard.ECE_R129  // Group 2/3
            else -> InternationalStandard.ECE_R129
        }
    }

    /**
     * 推荐分组
     */
    fun getRecommendedGroup(): String {
        return when {
            minHeight < 75 -> "Group 0+（后向强制）"
            maxHeight < 105 -> "Group 0/1"
            else -> "Group 2/3"
        }
    }
}

/**
 * 重量范围
 */
data class WeightRange(
    val minWeight: Double,  // kg
    val maxWeight: Double,  // kg
    val unit: WeightUnit = WeightUnit.KG
) {
    /**
     * 校验输入范围是否有效
     */
    fun isValid(productType: ProductType): Boolean {
        return minWeight > 0 &&
                maxWeight > minWeight &&
                maxWeight <= when (productType) {
                    ProductType.CHILD_SAFETY_SEAT -> 36.0
                    ProductType.BABY_STROLLER -> 25.0
                    else -> 36.0
                }
    }

    /**
     * 获取提示信息
     */
    fun getValidationHint(productType: ProductType): String? {
        val maxLimit = when (productType) {
            ProductType.CHILD_SAFETY_SEAT -> 36.0
            ProductType.BABY_STROLLER -> 25.0
            else -> 36.0
        }

        return when {
            minWeight <= 0 -> "最小重量必须大于0"
            maxWeight <= minWeight -> "最小重量必须小于最大重量"
            maxWeight > maxLimit -> "最大重量超过${productType.displayName}常规范围（${maxLimit}kg）"
            else -> null
        }
    }

    /**
     * 推荐标准
     */
    fun getRecommendedStandard(productType: ProductType): InternationalStandard? {
        return when (productType) {
            ProductType.CHILD_SAFETY_SEAT -> {
                when {
                    maxWeight <= 13 -> InternationalStandard.FMVSS_213  // Infant Seat
                    maxWeight <= 18 -> InternationalStandard.FMVSS_213  // Convertible
                    else -> InternationalStandard.FMVSS_213  // Forward-facing
                }
            }
            ProductType.BABY_STROLLER -> InternationalStandard.EN_1888
            else -> null
        }
    }

    /**
     * 推荐类型
     */
    fun getRecommendedType(productType: ProductType): String {
        return when (productType) {
            ProductType.CHILD_SAFETY_SEAT -> {
                when {
                    maxWeight <= 13 -> "Infant Seat（婴儿座椅）"
                    maxWeight <= 18 -> "Convertible（可转换座椅）"
                    else -> "Forward-facing（前向座椅）"
                }
            }
            ProductType.BABY_STROLLER -> {
                when {
                    maxWeight <= 15 -> "通用型推车"
                    else -> "重型推车"
                }
            }
            else -> ""
        }
    }

    /**
     * 转换为kg
     */
    fun toKg(): WeightRange {
        return if (unit == WeightUnit.KG) {
            this
        } else {
            copy(
                minWeight = minWeight * unit.toKgFactor,
                maxWeight = maxWeight * unit.toKgFactor,
                unit = WeightUnit.KG
            )
        }
    }
}

/**
 * 年龄范围
 */
data class AgeRange(
    val minAge: Int,  // 月
    val maxAge: Int   // 月
) {
    /**
     * 校验输入范围是否有效
     */
    fun isValid(productType: ProductType): Boolean {
        return minAge > 0 &&
                maxAge > minAge &&
                maxAge <= when (productType) {
                    ProductType.CHILD_SAFETY_SEAT -> 144  // 12岁
                    ProductType.BABY_STROLLER -> 48  // 4岁
                    else -> 144
                }
    }

    /**
     * 获取提示信息
     */
    fun getValidationHint(productType: ProductType): String? {
        val maxLimit = when (productType) {
            ProductType.CHILD_SAFETY_SEAT -> 144
            ProductType.BABY_STROLLER -> 48
            else -> 144
        }

        return when {
            minAge <= 0 -> "最小年龄必须大于0"
            maxAge <= minAge -> "最小年龄必须小于最大年龄"
            maxAge > maxLimit -> "最大年龄超过${productType.displayName}常规范围（${maxLimit / 12}岁）"
            minAge < 0 -> "年龄必须为正数"
            else -> null
        }
    }

    /**
     * 推荐标准
     */
    fun getRecommendedStandard(productType: ProductType): InternationalStandard? {
        return when (productType) {
            ProductType.CHILD_SAFETY_SEAT -> {
                when {
                    maxAge <= 9 -> InternationalStandard.ECE_R129  // Group 0+
                    maxAge <= 18 -> InternationalStandard.ECE_R129  // Group 0/1
                    else -> InternationalStandard.ECE_R129  // Group 2/3
                }
            }
            ProductType.BABY_STROLLER -> InternationalStandard.EN_1888
            else -> null
        }
    }

    /**
     * 推荐类型
     */
    fun getRecommendedType(productType: ProductType): String {
        return when (productType) {
            ProductType.CHILD_SAFETY_SEAT -> {
                when {
                    maxAge <= 9 -> "Group 0+（后向强制）"
                    maxAge <= 18 -> "Group 0/1"
                    else -> "Group 2/3"
                }
            }
            ProductType.BABY_STROLLER -> {
                when {
                    maxAge <= 6 -> "婴儿推车"
                    else -> "幼儿推车"
                }
            }
            else -> ""
        }
    }
}

/**
 * 输入匹配结果
 */
data class InputMatchingResult(
    val success: Boolean,
    val productType: ProductType,
    val standard: InternationalStandard?,
    val recommendedGroup: String?,
    val recommendedType: String?,
    val installDirection: InstallDirection?,
    val validationErrors: List<String> = emptyList()
)

/**
 * 输入匹配引擎
 */
object InputMatchingEngine {

    /**
     * 匹配输入并返回结果
     */
    fun match(input: SimplifiedInput): InputMatchingResult {
        val validationErrors = mutableListOf<String>()

        // 根据输入类型进行匹配
        when (input.inputType) {
            InputType.HEIGHT_RANGE -> {
                // 身高输入：自动识别为安全座椅
                val heightRange = input.heightRange
                if (heightRange == null) {
                    return InputMatchingResult(
                        success = false,
                        productType = ProductType.CHILD_SAFETY_SEAT,
                        standard = null,
                        recommendedGroup = null,
                        recommendedType = null,
                        installDirection = null,
                        validationErrors = listOf("请输入身高范围")
                    )
                }

                // 校验身高范围
                val hint = heightRange.getValidationHint()
                if (hint != null) {
                    validationErrors.add(hint)
                }

                if (!heightRange.isValid()) {
                    return InputMatchingResult(
                        success = false,
                        productType = ProductType.CHILD_SAFETY_SEAT,
                        standard = null,
                        recommendedGroup = null,
                        recommendedType = null,
                        installDirection = null,
                        validationErrors = validationErrors
                    )
                }

                // 匹配成功
                return InputMatchingResult(
                    success = true,
                    productType = ProductType.CHILD_SAFETY_SEAT,
                    standard = heightRange.getRecommendedStandard(),
                    recommendedGroup = heightRange.getRecommendedGroup(),
                    recommendedType = null,
                    installDirection = if (heightRange.minHeight < 75) {
                        InstallDirection.REARWARD
                    } else {
                        InstallDirection.FORWARD
                    },
                    validationErrors = emptyList()
                )
            }

            InputType.WEIGHT_RANGE -> {
                // 重量输入：需要用户选择品类
                val weightRange = input.weightRange
                if (weightRange == null) {
                    return InputMatchingResult(
                        success = false,
                        productType = input.productType ?: ProductType.CHILD_SAFETY_SEAT,
                        standard = null,
                        recommendedGroup = null,
                        recommendedType = null,
                        installDirection = null,
                        validationErrors = listOf("请输入重量范围")
                    )
                }

                val productType = input.productType
                if (productType == null) {
                    return InputMatchingResult(
                        success = false,
                        productType = ProductType.CHILD_SAFETY_SEAT,
                        standard = null,
                        recommendedGroup = null,
                        recommendedType = null,
                        installDirection = null,
                        validationErrors = listOf("请选择产品品类")
                    )
                }

                // 校验重量范围
                val hint = weightRange.getValidationHint(productType)
                if (hint != null) {
                    validationErrors.add(hint)
                }

                if (!weightRange.isValid(productType)) {
                    return InputMatchingResult(
                        success = false,
                        productType = productType,
                        standard = null,
                        recommendedGroup = null,
                        recommendedType = null,
                        installDirection = null,
                        validationErrors = validationErrors
                    )
                }

                // 匹配成功
                return InputMatchingResult(
                    success = true,
                    productType = productType,
                    standard = weightRange.getRecommendedStandard(productType),
                    recommendedGroup = if (productType == ProductType.CHILD_SAFETY_SEAT) {
                        weightRange.getRecommendedType(productType)
                    } else {
                        null
                    },
                    recommendedType = weightRange.getRecommendedType(productType),
                    installDirection = null,
                    validationErrors = emptyList()
                )
            }
            InputType.AGE_RANGE -> {
                // 年龄范围输入
                val ageRange = input.ageRange
                if (ageRange == null) {
                    return InputMatchingResult(
                        success = false,
                        productType = input.productType ?: ProductType.CHILD_SAFETY_SEAT,
                        standard = null,
                        recommendedGroup = null,
                        recommendedType = null,
                        installDirection = null,
                        validationErrors = listOf("请输入年龄范围")
                    )
                }

                val productType = input.productType
                if (productType == null) {
                    return InputMatchingResult(
                        success = false,
                        productType = ProductType.CHILD_SAFETY_SEAT,
                        standard = null,
                        recommendedGroup = null,
                        recommendedType = null,
                        installDirection = null,
                        validationErrors = listOf("请选择产品品类")
                    )
                }

                // 校验年龄范围
                val hint = ageRange.getValidationHint(productType)
                if (hint != null) {
                    validationErrors.add(hint)
                }

                if (!ageRange.isValid(productType)) {
                    return InputMatchingResult(
                        success = false,
                        productType = productType,
                        standard = null,
                        recommendedGroup = null,
                        recommendedType = null,
                        installDirection = null,
                        validationErrors = validationErrors
                    )
                }

                // 匹配成功
                return InputMatchingResult(
                    success = true,
                    productType = productType,
                    standard = ageRange.getRecommendedStandard(productType),
                    recommendedGroup = if (productType == ProductType.CHILD_SAFETY_SEAT) {
                        ageRange.getRecommendedType(productType)
                    } else {
                        null
                    },
                    recommendedType = ageRange.getRecommendedType(productType),
                    installDirection = null,
                    validationErrors = emptyList()
                )
            }
            InputType.CUSTOM -> {
                // 自定义参数输入
                if (input.productType == null) {
                    return InputMatchingResult(
                        success = false,
                        productType = ProductType.CHILD_SAFETY_SEAT,
                        standard = null,
                        recommendedGroup = null,
                        recommendedType = null,
                        installDirection = null,
                        validationErrors = listOf("请选择产品品类")
                    )
                }

                // 自定义参数暂时返回基本成功状态
                return InputMatchingResult(
                    success = true,
                    productType = input.productType!!,
                    standard = null,
                    recommendedGroup = null,
                    recommendedType = null,
                    installDirection = null,
                    validationErrors = emptyList()
                )
            }
        }
    }

    /**
     * 智能联想：根据输入值提示可能的参数
     */
    fun suggest(inputValue: String): List<Suggestion> {
        val suggestions = mutableListOf<Suggestion>()

        try {
            val value = inputValue.toDouble()

            // 身高联想
            if (value > 40 && value <= 150) {
                suggestions.add(Suggestion(
                    type = SuggestionType.HEIGHT,
                    value = value,
                    hint = "ECE R129 ${if (value < 75) "Group 0+标准，后向安装" else if (value < 100) "Group 0/1标准" else "Group 2/3标准"}"
                ))
            }

            // 重量联想
            if (value > 0 && value <= 36) {
                suggestions.add(Suggestion(
                    type = SuggestionType.WEIGHT,
                    value = value,
                    hint = "FMVSS 213 ${if (value <= 13) "Infant Seat" else if (value <= 18) "Convertible" else "Forward-facing"}"
                ))
            }

            // 超出常规范围
            if (value > 36) {
                suggestions.add(Suggestion(
                    type = SuggestionType.WARNING,
                    value = value,
                    hint = "超过安全座椅常规范围（36kg），请确认产品类型"
                ))
            }
        } catch (e: NumberFormatException) {
            // 忽略非数字输入
        }

        return suggestions
    }
}

/**
 * 建议类型
 */
enum class SuggestionType {
    HEIGHT,
    WEIGHT,
    WARNING
}

/**
 * 联想建议
 */
data class Suggestion(
    val type: SuggestionType,
    val value: Double,
    val hint: String
)

/**
 * 常用专项需求（安全座椅）
 */
val COMMON_SEAT_REQUIREMENTS = listOf(
    "优化头托侧面碰撞防护",
    "符合i-Size Envelope内部尺寸",
    "提升安装便捷性",
    "增强侧面支撑结构",
    "优化通风散热性能",
    "降低座椅重量",
    "提升座椅舒适度",
    "优化安全带调节系统",
    "集成SensorSafe智能监测",
    "支持360°旋转功能"
)

/**
 * 常用专项需求（婴儿推车）
 */
val COMMON_STROLLER_REQUIREMENTS = listOf(
    "提升避震性能适配崎岖路面",
    "优化折叠机构实现一键收纳",
    "提升折叠后的便携性",
    "增强储物篮容量",
    "优化转向灵活性",
    "提升座椅舒适性",
    "适配多种安全座椅",
    "优化遮阳篷防护性能",
    "增强制动系统可靠性",
    "优化车轮稳定性"
)

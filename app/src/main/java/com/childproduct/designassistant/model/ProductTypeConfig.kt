package com.childproduct.designassistant.model

/**
 * 输入项类型枚举
 */
enum class InputType {
    HEIGHT_RANGE,   // 身高范围
    WEIGHT_RANGE,   // 体重范围
    AGE_RANGE,      // 适用年龄
    CUSTOM          // 自定义
}

/**
 * 输入项配置
 */
data class InputItemConfig(
    val inputType: InputType,
    val inputLabel: String,
    val unit: String,
    val editable: Boolean,
    val placeholder: String
)

/**
 * 参数区间配置（用于匹配dummy/年龄段）
 */
data class ParamInterval(
    val start: Double,
    val end: Double,
    val desc: String,
    val dummyType: CrashTestDummy? = null  // 关联的假人类型（可选）
)

/**
 * 体重参数区间配置（同时支持lb和kg）
 */
data class WeightInterval(
    val startLb: Double,
    val endLb: Double,
    val startKg: Double,
    val endKg: Double,
    val desc: String
)

/**
 * 参数校验规则
 */
data class ParamRule(
    val minValue: Double? = null,           // 最小值（身高/年龄）
    val maxValue: Double? = null,           // 最大值（身高/年龄）
    val minValueLb: Double? = null,         // 最小值（磅）
    val maxValueLb: Double? = null,         // 最大值（磅）
    val minValueKg: Double? = null,         // 最小值（公斤）
    val maxValueKg: Double? = null,         // 最大值（公斤）
    val intervals: List<ParamInterval> = emptyList(),      // 参数区间（身高/年龄）
    val weightIntervals: List<WeightInterval> = emptyList(), // 体重区间
    val errorTip: String                    // 超范围提示语
)

/**
 * 标准配置
 */
data class StandardConfig(
    val standardId: String,
    val standardName: String,
    val inputItem: InputItemConfig,
    val paramRule: ParamRule,
    val coreRequirements: String
)

/**
 * 产品类型配置
 */
data class ProductTypeConfig(
    val productTypeId: String,
    val productTypeName: String,
    val standards: List<StandardConfig>
)

/**
 * 合规组合状态
 */
data class ComplianceCombination(
    val productType: ProductType,
    val standardName: String,
    val paramValue: String,
    val matchedDummy: CrashTestDummy? = null,
    val coreRequirements: String,
    val isValid: Boolean
) {
    fun getDisplayText(): String {
        val dummyText = matchedDummy?.let { "（${it.name} dummy）" } ?: ""
        return "$productType + $standardName → $paramValue$dummyText"
    }
}

/**
 * 参数输入结果
 */
data class ParamInputResult(
    val isValid: Boolean,
    val errorMessage: String? = null,
    val matchedDummy: CrashTestDummy? = null,
    val matchedInterval: String? = null
)

/**
 * 产品类型-标准-输入项配置管理器
 */
object ProductTypeConfigManager {

    /**
     * 所有产品类型配置
     */
    private val allConfigs = listOf(
        // 儿童安全座椅
        ProductTypeConfig(
            productTypeId = "CHILD_SAFETY_SEAT",
            productTypeName = "儿童安全座椅",
            standards = listOf(
                StandardConfig(
                    standardId = "ECE_R129_GB27887",
                    standardName = "ECE R129 / GB 27887-2024",
                    inputItem = InputItemConfig(
                        inputType = InputType.HEIGHT_RANGE,
                        inputLabel = "身高范围（cm）",
                        unit = "cm",
                        editable = true,
                        placeholder = "请输入40-150之间的范围，如40-105"
                    ),
                    paramRule = ParamRule(
                        minValue = 40.0,
                        maxValue = 150.0,
                        intervals = listOf(
                            ParamInterval(40.0, 60.0, "新生儿（0-1岁）", CrashTestDummy.Q0),
                            ParamInterval(60.0, 75.0, "1-2岁", CrashTestDummy.Q1),
                            ParamInterval(75.0, 87.0, "2-3岁", CrashTestDummy.Q1_5),
                            ParamInterval(87.0, 105.0, "3-4岁", CrashTestDummy.Q3),
                            ParamInterval(105.0, 125.0, "4-6岁", CrashTestDummy.Q3_S),
                            ParamInterval(125.0, 150.0, "6-10岁", CrashTestDummy.Q10)
                        ),
                        errorTip = "当前标准要求身高范围为40-150cm，请调整输入"
                    ),
                    coreRequirements = "适配对应dummy（Q0-Q10）、后向安装至4岁、HIC≤1000"
                ),
                StandardConfig(
                    standardId = "FMVSS_213",
                    standardName = "FMVSS 213（美标）",
                    inputItem = InputItemConfig(
                        inputType = InputType.WEIGHT_RANGE,
                        inputLabel = "体重范围（lb/kg）",
                        unit = "lb/kg",
                        editable = true,
                        placeholder = "请输入5-80lb（2.3-36kg）之间的范围，如5-36"
                    ),
                    paramRule = ParamRule(
                        minValueLb = 5.0,
                        maxValueLb = 80.0,
                        minValueKg = 2.3,
                        maxValueKg = 36.0,
                        weightIntervals = listOf(
                            WeightInterval(5.0, 20.0, 2.3, 9.0, "0-2岁"),
                            WeightInterval(20.0, 40.0, 9.0, 18.0, "2-4岁"),
                            WeightInterval(40.0, 80.0, 18.0, 36.0, "4-10岁")
                        ),
                        errorTip = "当前标准要求体重范围为5-80lb（2.3-36kg），请调整输入"
                    ),
                    coreRequirements = "正面碰撞HIC≤1000、织带断裂强度≥11000N、侧面碰撞胸部压缩量≤23mm"
                )
            )
        ),
        // 婴儿推车
        ProductTypeConfig(
            productTypeId = "BABY_STROLLER",
            productTypeName = "婴儿推车",
            standards = listOf(
                StandardConfig(
                    standardId = "EN_1888_GB14748",
                    standardName = "EN 1888 / GB 14748-2020",
                    inputItem = InputItemConfig(
                        inputType = InputType.HEIGHT_RANGE,
                        inputLabel = "身高范围（cm）",
                        unit = "cm",
                        editable = true,
                        placeholder = "请输入≤100的数值，如50-90"
                    ),
                    paramRule = ParamRule(
                        minValue = 0.0,
                        maxValue = 100.0,
                        errorTip = "当前标准要求婴儿推车适配身高≤100cm，请调整输入"
                    ),
                    coreRequirements = "制动距离≤50cm、折叠后无锐边、推把强度≥1000N"
                )
            )
        ),
        // 儿童家庭用品
        ProductTypeConfig(
            productTypeId = "CHILD_HOME_PRODUCTS",
            productTypeName = "儿童家庭用品",
            standards = listOf(
                StandardConfig(
                    standardId = "ISO_8124_GB28007",
                    standardName = "ISO 8124-3 / GB 28007-2011",
                    inputItem = InputItemConfig(
                        inputType = InputType.AGE_RANGE,
                        inputLabel = "适用年龄（岁）",
                        unit = "岁",
                        editable = true,
                        placeholder = "请输入1-12之间的范围，如1-6"
                    ),
                    paramRule = ParamRule(
                        minValue = 1.0,
                        maxValue = 12.0,
                        errorTip = "当前标准要求适用年龄为1-12岁，请调整输入"
                    ),
                    coreRequirements = "小零件直径≥31.75mm（防吞咽）、阻燃性能达标、无有害可迁移元素"
                )
            )
        ),
        // 儿童高脚椅
        ProductTypeConfig(
            productTypeId = "CHILD_HIGH_CHAIR",
            productTypeName = "儿童高脚椅",
            standards = listOf(
                StandardConfig(
                    standardId = "ISO_8124_GB28007",
                    standardName = "ISO 8124-3 / GB 28007-2011",
                    inputItem = InputItemConfig(
                        inputType = InputType.AGE_RANGE,
                        inputLabel = "适用年龄（岁）",
                        unit = "岁",
                        editable = true,
                        placeholder = "请输入1-6之间的范围，如1-3"
                    ),
                    paramRule = ParamRule(
                        minValue = 1.0,
                        maxValue = 6.0,
                        errorTip = "当前标准要求高脚椅适用年龄为1-6岁，请调整输入"
                    ),
                    coreRequirements = "稳定性测试无倾倒、束缚带强度≥150N、边缘圆角≥2mm"
                )
            )
        )
    )

    /**
     * 根据产品类型获取配置
     */
    fun getConfigByProductType(productType: ProductType): ProductTypeConfig? {
        return allConfigs.find { it.productTypeId == productType.name }
    }

    /**
     * 根据产品类型ID获取配置
     */
    fun getConfigByProductTypeId(productTypeId: String): ProductTypeConfig? {
        return allConfigs.find { it.productTypeId == productTypeId }
    }

    /**
     * 获取所有产品类型配置
     */
    fun getAllConfigs(): List<ProductTypeConfig> {
        return allConfigs
    }

    /**
     * 验证身高范围输入
     */
    fun validateHeightRange(
        minHeight: Double,
        maxHeight: Double,
        standardConfig: StandardConfig
    ): ParamInputResult {
        val rule = standardConfig.paramRule
        
        // 检查范围有效性
        if (minHeight >= maxHeight) {
            return ParamInputResult(
                isValid = false,
                errorMessage = "最小身高必须小于最大身高"
            )
        }

        // 检查是否在允许范围内
        if (rule.minValue != null && minHeight < rule.minValue) {
            return ParamInputResult(
                isValid = false,
                errorMessage = rule.errorTip
            )
        }
        if (rule.maxValue != null && maxHeight > rule.maxValue) {
            return ParamInputResult(
                isValid = false,
                errorMessage = rule.errorTip
            )
        }

        // 查找匹配的区间和假人类型
        val middleHeight = (minHeight + maxHeight) / 2
        val matchedInterval = rule.intervals.find { interval ->
            middleHeight >= interval.start && middleHeight < interval.end
        }

        return ParamInputResult(
            isValid = true,
            matchedDummy = matchedInterval?.dummyType,
            matchedInterval = matchedInterval?.desc
        )
    }

    /**
     * 验证体重范围输入
     */
    fun validateWeightRange(
        minWeight: Double,
        maxWeight: Double,
        isLb: Boolean,
        standardConfig: StandardConfig
    ): ParamInputResult {
        val rule = standardConfig.paramRule
        
        // 检查范围有效性
        if (minWeight >= maxWeight) {
            return ParamInputResult(
                isValid = false,
                errorMessage = "最小体重必须小于最大体重"
            )
        }

        // 检查是否在允许范围内（磅）
        if (isLb) {
            if (rule.minValueLb != null && minWeight < rule.minValueLb) {
                return ParamInputResult(
                    isValid = false,
                    errorMessage = rule.errorTip
                )
            }
            if (rule.maxValueLb != null && maxWeight > rule.maxValueLb) {
                return ParamInputResult(
                    isValid = false,
                    errorMessage = rule.errorTip
                )
            }
        } else {
            // 检查是否在允许范围内（公斤）
            if (rule.minValueKg != null && minWeight < rule.minValueKg) {
                return ParamInputResult(
                    isValid = false,
                    errorMessage = rule.errorTip
                )
            }
            if (rule.maxValueKg != null && maxWeight > rule.maxValueKg) {
                return ParamInputResult(
                    isValid = false,
                    errorMessage = rule.errorTip
                )
            }
        }

        // 查找匹配的区间
        val middleWeight = (minWeight + maxWeight) / 2
        val matchedInterval = if (isLb) {
            rule.weightIntervals.find { interval ->
                middleWeight >= interval.startLb && middleWeight < interval.endLb
            }?.desc
        } else {
            rule.weightIntervals.find { interval ->
                middleWeight >= interval.startKg && middleWeight < interval.endKg
            }?.desc
        }

        return ParamInputResult(
            isValid = true,
            matchedInterval = matchedInterval
        )
    }

    /**
     * 验证年龄范围输入
     */
    fun validateAgeRange(
        minAge: Double,
        maxAge: Double,
        standardConfig: StandardConfig
    ): ParamInputResult {
        val rule = standardConfig.paramRule
        
        // 检查范围有效性
        if (minAge >= maxAge) {
            return ParamInputResult(
                isValid = false,
                errorMessage = "最小年龄必须小于最大年龄"
            )
        }

        // 检查是否在允许范围内
        if (rule.minValue != null && minAge < rule.minValue) {
            return ParamInputResult(
                isValid = false,
                errorMessage = rule.errorTip
            )
        }
        if (rule.maxValue != null && maxAge > rule.maxValue) {
            return ParamInputResult(
                isValid = false,
                errorMessage = rule.errorTip
            )
        }

        return ParamInputResult(
            isValid = true,
            matchedInterval = "${minAge.toInt()}-${maxAge.toInt()}岁"
        )
    }
}

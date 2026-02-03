package com.childproduct.designassistant.model

import com.childproduct.designassistant.model.ProductType

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
    val region: String,  // 地区标识：DOMESTIC（国内）、INTERNATIONAL（国际）
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
                // 欧标+国标
                StandardConfig(
                    standardId = "ECE_R129_GB27887",
                    standardName = "ECE R129 / GB 27887-2024",
                    region = "INTERNATIONAL",
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
                    coreRequirements = "适配对应dummy（Q0-Q10）、后向安装至4岁、HIC≤1000、侧面碰撞胸部压缩量≤44mm"
                ),
                // 美标
                StandardConfig(
                    standardId = "FMVSS_213",
                    standardName = "FMVSS 213（美标）",
                    region = "INTERNATIONAL",
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
                            WeightInterval(40.0, 65.0, 18.0, 29.0, "4-8岁"),
                            WeightInterval(65.0, 80.0, 29.0, 36.0, "8-10岁")
                        ),
                        errorTip = "当前标准要求体重范围为5-80lb（2.3-36kg），请调整输入"
                    ),
                    coreRequirements = "正面碰撞HIC≤1000、织带断裂强度≥11000N、侧面碰撞胸部压缩量≤23mm、头部加速度≤50g"
                ),
                // 澳标
                StandardConfig(
                    standardId = "AS_NZS_1754",
                    standardName = "AS/NZS 1754（澳标）",
                    region = "INTERNATIONAL",
                    inputItem = InputItemConfig(
                        inputType = InputType.HEIGHT_RANGE,
                        inputLabel = "身高范围（cm）",
                        unit = "cm",
                        editable = true,
                        placeholder = "请输入40-145之间的范围，如40-100"
                    ),
                    paramRule = ParamRule(
                        minValue = 40.0,
                        maxValue = 145.0,
                        intervals = listOf(
                            ParamInterval(40.0, 70.0, "0-2岁", CrashTestDummy.Q0),
                            ParamInterval(70.0, 80.0, "2-3岁", CrashTestDummy.Q1),
                            ParamInterval(80.0, 95.0, "3-4岁", CrashTestDummy.Q3),
                            ParamInterval(95.0, 115.0, "4-6岁", CrashTestDummy.Q3_S),
                            ParamInterval(115.0, 145.0, "6-8岁", CrashTestDummy.Q6)
                        ),
                        errorTip = "当前标准要求身高范围为40-145cm，请调整输入"
                    ),
                    coreRequirements = "后向安装至6个月、侧面碰撞头部位移≤550mm、动态测试加速度≤60g、ISOFIX锚点强度≥8kN"
                ),
                // 日标
                StandardConfig(
                    standardId = "JIS_D_1601",
                    standardName = "JIS D 1601（日标）",
                    region = "INTERNATIONAL",
                    inputItem = InputItemConfig(
                        inputType = InputType.WEIGHT_RANGE,
                        inputLabel = "体重范围（kg）",
                        unit = "kg",
                        editable = true,
                        placeholder = "请输入9-36kg之间的范围，如9-18"
                    ),
                    paramRule = ParamRule(
                        minValueKg = 9.0,
                        maxValueKg = 36.0,
                        weightIntervals = listOf(
                            WeightInterval(18.0, 25.0, 8.2, 11.3, "1-3岁"),
                            WeightInterval(25.0, 30.0, 11.3, 13.6, "3-5岁"),
                            WeightInterval(30.0, 36.0, 13.6, 16.3, "5-8岁")
                        ),
                        errorTip = "当前标准要求体重范围为9-36kg，请调整输入"
                    ),
                    coreRequirements = "后向安装至2岁、正面碰撞胸部加速度≤60g、侧面碰撞胸部位移≤30mm、织带锁止≤15mm"
                )
            )
        ),
        // 婴儿推车
        ProductTypeConfig(
            productTypeId = "CHILD_STROLLER",
            productTypeName = "婴儿推车",
            standards = listOf(
                // 欧标
                StandardConfig(
                    standardId = "EN_1888",
                    standardName = "EN 1888（欧标）",
                    region = "INTERNATIONAL",
                    inputItem = InputItemConfig(
                        inputType = InputType.WEIGHT_RANGE,
                        inputLabel = "体重范围（kg）",
                        unit = "kg",
                        editable = true,
                        placeholder = "请输入0-22kg之间的范围，如0-15"
                    ),
                    paramRule = ParamRule(
                        minValueKg = 0.0,
                        maxValueKg = 22.0,
                        weightIntervals = listOf(
                            WeightInterval(0.0, 9.0, 0.0, 9.0, "0-9个月"),
                            WeightInterval(9.0, 13.0, 9.0, 13.0, "9-18个月"),
                            WeightInterval(13.0, 15.0, 13.0, 15.0, "1.5-3岁"),
                            WeightInterval(15.0, 22.0, 15.0, 22.0, "3-4岁")
                        ),
                        errorTip = "当前标准要求体重范围为0-22kg，请调整输入"
                    ),
                    coreRequirements = "制动距离≤100cm（10°斜坡）、推把强度≥600N、折叠机构防止意外触发、警告标识清晰可见"
                ),
                // 国标
                StandardConfig(
                    standardId = "GB_14748",
                    standardName = "GB 14748-2006（国标）",
                    region = "DOMESTIC",
                    inputItem = InputItemConfig(
                        inputType = InputType.WEIGHT_RANGE,
                        inputLabel = "体重范围（kg）",
                        unit = "kg",
                        editable = true,
                        placeholder = "请输入0-15kg之间的范围，如0-9"
                    ),
                    paramRule = ParamRule(
                        minValueKg = 0.0,
                        maxValueKg = 15.0,
                        weightIntervals = listOf(
                            WeightInterval(0.0, 9.0, 0.0, 9.0, "0-9个月"),
                            WeightInterval(9.0, 13.0, 9.0, 13.0, "9-18个月"),
                            WeightInterval(13.0, 15.0, 13.0, 15.0, "1.5-3岁")
                        ),
                        errorTip = "当前标准要求体重范围为0-15kg，请调整输入"
                    ),
                    coreRequirements = "制动距离≤50cm（5°斜坡）、稳定性测试无倾倒、束缚带宽度≥20mm、折叠后锁定牢固"
                ),
                // 美标
                StandardConfig(
                    standardId = "ASTM_F833",
                    standardName = "ASTM F833（美标）",
                    region = "INTERNATIONAL",
                    inputItem = InputItemConfig(
                        inputType = InputType.WEIGHT_RANGE,
                        inputLabel = "体重范围（lb/kg）",
                        unit = "lb/kg",
                        editable = true,
                        placeholder = "请输入0-50lb（0-22.7kg）之间的范围，如0-25"
                    ),
                    paramRule = ParamRule(
                        minValueLb = 0.0,
                        maxValueLb = 50.0,
                        minValueKg = 0.0,
                        maxValueKg = 22.7,
                        weightIntervals = listOf(
                            WeightInterval(0.0, 20.0, 0.0, 9.0, "0-9个月"),
                            WeightInterval(20.0, 30.0, 9.0, 13.6, "9-18个月"),
                            WeightInterval(30.0, 40.0, 13.6, 18.1, "1.5-3岁"),
                            WeightInterval(40.0, 50.0, 18.1, 22.7, "3-4岁")
                        ),
                        errorTip = "当前标准要求体重范围为0-50lb（0-22.7kg），请调整输入"
                    ),
                    coreRequirements = "制动距离≤36英寸（10°斜坡）、五点式安全带、扶手间距≤45cm、停车制动器可靠"
                ),
                // 澳标
                StandardConfig(
                    standardId = "AS_NZS_2088",
                    standardName = "AS/NZS 2088（澳标）",
                    region = "INTERNATIONAL",
                    inputItem = InputItemConfig(
                        inputType = InputType.WEIGHT_RANGE,
                        inputLabel = "体重范围（kg）",
                        unit = "kg",
                        editable = true,
                        placeholder = "请输入0-17kg之间的范围，如0-9"
                    ),
                    paramRule = ParamRule(
                        minValueKg = 0.0,
                        maxValueKg = 17.0,
                        weightIntervals = listOf(
                            WeightInterval(0.0, 9.0, 0.0, 9.0, "0-9个月"),
                            WeightInterval(9.0, 13.0, 9.0, 13.0, "9-18个月"),
                            WeightInterval(13.0, 17.0, 13.0, 17.0, "1.5-3岁")
                        ),
                        errorTip = "当前标准要求体重范围为0-17kg，请调整输入"
                    ),
                    coreRequirements = "制动距离≤120cm（12°斜坡）、稳定性测试通过、五点式安全带锁定装置、可调节靠背角度"
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
                    region = "INTERNATIONAL",
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
                // 国际标准+国标
                StandardConfig(
                    standardId = "ISO_8124_GB28007",
                    standardName = "ISO 8124-3 / GB 28007-2011",
                    region = "INTERNATIONAL",
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
                        intervals = listOf(
                            ParamInterval(1.0, 2.0, "1-2岁"),
                            ParamInterval(2.0, 3.0, "2-3岁"),
                            ParamInterval(3.0, 4.0, "3-4岁"),
                            ParamInterval(4.0, 6.0, "4-6岁")
                        ),
                        errorTip = "当前标准要求高脚椅适用年龄为1-6岁，请调整输入"
                    ),
                    coreRequirements = "稳定性测试无倾倒、束缚带强度≥150N、边缘圆角≥2mm、可调节高度锁定可靠"
                ),
                // 欧标
                StandardConfig(
                    standardId = "EN_14988",
                    standardName = "EN 14988（欧标）",
                    region = "INTERNATIONAL",
                    inputItem = InputItemConfig(
                        inputType = InputType.WEIGHT_RANGE,
                        inputLabel = "体重范围（kg）",
                        unit = "kg",
                        editable = true,
                        placeholder = "请输入9-15kg之间的范围，如9-12"
                    ),
                    paramRule = ParamRule(
                        minValueKg = 9.0,
                        maxValueKg = 15.0,
                        weightIntervals = listOf(
                            WeightInterval(9.0, 11.0, 9.0, 11.0, "9-11kg"),
                            WeightInterval(11.0, 13.0, 11.0, 13.0, "11-13kg"),
                            WeightInterval(13.0, 15.0, 13.0, 15.0, "13-15kg")
                        ),
                        errorTip = "当前标准要求体重范围为9-15kg，请调整输入"
                    ),
                    coreRequirements = "向前/向后倾斜30°不倾倒、托盘强度≥120N、五点式安全带、座垫抗撕裂≥25N"
                ),
                // 美标
                StandardConfig(
                    standardId = "ASTM_F404",
                    standardName = "ASTM F404（美标）",
                    region = "INTERNATIONAL",
                    inputItem = InputItemConfig(
                        inputType = InputType.WEIGHT_RANGE,
                        inputLabel = "体重范围（lb/kg）",
                        unit = "lb/kg",
                        editable = true,
                        placeholder = "请输入≤50lb（≤22.7kg），如15-30"
                    ),
                    paramRule = ParamRule(
                        maxValueLb = 50.0,
                        maxValueKg = 22.7,
                        weightIntervals = listOf(
                            WeightInterval(0.0, 20.0, 0.0, 9.0, "0-20lb"),
                            WeightInterval(20.0, 30.0, 9.0, 13.6, "20-30lb"),
                            WeightInterval(30.0, 40.0, 13.6, 18.1, "30-40lb"),
                            WeightInterval(40.0, 50.0, 18.1, 22.7, "40-50lb")
                        ),
                        errorTip = "当前标准要求体重不超过50lb（22.7kg），请调整输入"
                    ),
                    coreRequirements = "托盘锁定强度≥50lb、C型扣带强度≥133N、五点式安全带、稳定性测试通过"
                )
            )
        ),
        // 儿童床
        ProductTypeConfig(
            productTypeId = "CRIB",
            productTypeName = "儿童床",
            standards = listOf(
                // 欧标
                StandardConfig(
                    standardId = "EN_716",
                    standardName = "EN 716（欧标）",
                    region = "INTERNATIONAL",
                    inputItem = InputItemConfig(
                        inputType = InputType.AGE_RANGE,
                        inputLabel = "适用年龄（岁）",
                        unit = "岁",
                        editable = true,
                        placeholder = "请输入0-5之间的范围，如0-3"
                    ),
                    paramRule = ParamRule(
                        minValue = 0.0,
                        maxValue = 5.0,
                        intervals = listOf(
                            ParamInterval(0.0, 1.0, "0-1岁"),
                            ParamInterval(1.0, 2.0, "1-2岁"),
                            ParamInterval(2.0, 3.0, "2-3岁"),
                            ParamInterval(3.0, 5.0, "3-5岁")
                        ),
                        errorTip = "当前标准要求适用年龄为0-5岁，请调整输入"
                    ),
                    coreRequirements = "床垫支撑高度差≤12cm、床板条间距≤6cm、四边护栏高度≥60cm、无锐边"
                ),
                // 美标
                StandardConfig(
                    standardId = "ASTM_F1169",
                    standardName = "ASTM F1169（美标）",
                    region = "INTERNATIONAL",
                    inputItem = InputItemConfig(
                        inputType = InputType.AGE_RANGE,
                        inputLabel = "适用年龄（岁）",
                        unit = "岁",
                        editable = true,
                        placeholder = "请输入0-4之间的范围，如0-2"
                    ),
                    paramRule = ParamRule(
                        minValue = 0.0,
                        maxValue = 4.0,
                        intervals = listOf(
                            ParamInterval(0.0, 1.0, "0-1岁"),
                            ParamInterval(1.0, 2.0, "1-2岁"),
                            ParamInterval(2.0, 3.0, "2-3岁"),
                            ParamInterval(3.0, 4.0, "3-4岁")
                        ),
                        errorTip = "当前标准要求适用年龄为0-4岁，请调整输入"
                    ),
                    coreRequirements = "床垫厚度≤15cm、床板条间距≤6cm、全封闭床垫支撑、防跌落装置"
                ),
                // 国标
                StandardConfig(
                    standardId = "GB_28007",
                    standardName = "GB 28007-2011（国标）",
                    region = "DOMESTIC",
                    inputItem = InputItemConfig(
                        inputType = InputType.AGE_RANGE,
                        inputLabel = "适用年龄（岁）",
                        unit = "岁",
                        editable = true,
                        placeholder = "请输入0-5之间的范围，如0-3"
                    ),
                    paramRule = ParamRule(
                        minValue = 0.0,
                        maxValue = 5.0,
                        intervals = listOf(
                            ParamInterval(0.0, 1.0, "0-1岁"),
                            ParamInterval(1.0, 2.0, "1-2岁"),
                            ParamInterval(2.0, 3.0, "2-3岁"),
                            ParamInterval(3.0, 5.0, "3-5岁")
                        ),
                        errorTip = "当前标准要求适用年龄为0-5岁，请调整输入"
                    ),
                    coreRequirements = "床板条间距≤6cm、四边护栏高度≥60cm、防跌落条间距≤6cm、甲醛释放量≤0.1mg/L"
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

        // 特殊处理：如果范围覆盖整个标准范围（40-150cm），返回全假人
        if (minHeight <= 40.0 && maxHeight >= 150.0) {
            return ParamInputResult(
                isValid = true,
                matchedDummy = null,  // null表示全假人
                matchedInterval = "全年龄段（0-12岁）"
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

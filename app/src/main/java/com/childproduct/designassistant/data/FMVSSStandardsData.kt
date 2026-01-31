package com.childproduct.designassistant.data

/**
 * FMVSS 213/213a 标准数据模型
 * 
 * 美国联邦机动车安全标准
 * FMVSS 213: 儿童约束系统（正面碰撞）
 * FMVSS 213a: 儿童约束系统（侧面碰撞）
 * 
 * 参考标准：
 * - FMVSS 209: 座椅安全带总成
 * - FMVSS 225: 儿童约束系统固定装置
 * - FMVSS 302: 汽车内饰材料阻燃性
 * - ASTM B117: 盐雾测试
 * - ASTM D756: 热老化测试
 * - AATCC 30: 抗菌测试
 */

/**
 * FMVSS标准类型
 */
enum class FMVSSStandardType(val displayName: String, val code: String, val effectiveDate: String) {
    FMVSS_213("FMVSS 213", "213", "2023修订版"),
    FMVSS_213A("FMVSS 213a", "213a", "2025年6月30日生效"),
    FMVSS_209("FMVSS 209", "209", "引用标准"),
    FMVSS_225("FMVSS 225", "225", "引用标准"),
    FMVSS_302("FMVSS 302", "302", "引用标准");

    companion object {
        fun fromCode(code: String): FMVSSStandardType? = values().find { it.code == code }
    }
}

/**
 * 碰撞类型
 */
enum class ImpactTypeFMVSS(val displayName: String, val standard: FMVSSStandardType) {
    FRONTAL("正面碰撞", FMVSSStandardType.FMVSS_213),
    SIDE("侧面碰撞", FMVSSStandardType.FMVSS_213A)
}

/**
 * 安装方式
 */
enum class InstallationMethodFMVSS(val displayName: String, val requirement: String) {
    LATCH("LATCH系统", "下锚+顶部tether"),
    THREE_POINT_BELT("三点式安全带", "标准三点式安全带安装"),
    TWO_POINT_BELT("两点式安全带", "Lap belt安装"),
    AIRCRAFT("飞机安全带", "FAA认证安全带安装")
}

/**
 * 织带性能要求（FMVSS 213 S5.4.1）
 */
data class WebbingRequirements(
    val type: WebbingType,                  // 织带类型
    val minBreakStrength: Double,           // 最小断裂强度（N）
    val minWidthMm: Double,                // 最小宽度（mm）
    val tensionForWidth: Double,           // 测量宽度时的张力（N）
    val abrasionRetention: Double,          // 耐磨后强度保留率（%）
    val abrasionCycles: Int,               // 摩擦次数
    val lightRetention: Double,            // 耐光后强度保留率（%）
    val lightExposureHours: Int,           // 光照时长（小时）
    val antimicrobialRetention: Double,    // 抗菌后强度保留率（%）
    val antimicrobialDays: Int             // 抗菌测试天数
) {
    companion object {
        /**
         * 固定CRS至车辆的织带要求
         */
        val VEHICLE_ATTACHMENT = WebbingRequirements(
            type = WebbingType.VEHICLE_ATTACHMENT,
            minBreakStrength = 15000.0,
            minWidthMm = 38.0,
            tensionForWidth = 2.3,
            abrasionRetention = 75.0,
            abrasionCycles = 5000,
            lightRetention = 60.0,
            lightExposureHours = 100,
            antimicrobialRetention = 85.0,
            antimicrobialDays = 14
        )

        /**
         * 约束儿童的织带要求
         */
        val CHILD_RESTRAINT = WebbingRequirements(
            type = WebbingType.CHILD_RESTRAINT,
            minBreakStrength = 11000.0,
            minWidthMm = 38.0,
            tensionForWidth = 2.3,
            abrasionRetention = 75.0,
            abrasionCycles = 5000,
            lightRetention = 60.0,
            lightExposureHours = 100,
            antimicrobialRetention = 85.0,
            antimicrobialDays = 14
        )
    }
}

/**
 * 织带类型
 */
enum class WebbingType(val displayName: String) {
    VEHICLE_ATTACHMENT("固定CRS至车辆的织带"),
    CHILD_RESTRAINT("约束儿童的织带")
}

/**
 * 非金属材料阻燃要求（FMVSS 302 S4.3）
 */
data class FlammabilityRequirements(
    val maxBurnRate: Double,               // 最大燃烧速率（mm/分钟）
    val maxBurnDistance: Double,           // 60秒内最大燃烧距离（mm）
    val maxBurnTime: Int,                  // 最大燃烧时间（秒）
    val preTempMin: Double,                // 预处理最低温度（℃）
    val preTempMax: Double,                // 预处理最高温度（℃）
    val preHumidityMin: Double,            // 预处理最低湿度（%）
    val preHumidityMax: Double,            // 预处理最高湿度（%）
    val preDurationHours: Int              // 预处理时长（小时）
) {
    companion object {
        val STANDARD = FlammabilityRequirements(
            maxBurnRate = 102.0,
            maxBurnDistance = 51.0,
            maxBurnTime = 60,
            preTempMin = 16.0,
            preTempMax = 27.0,
            preHumidityMin = 40.0,
            preHumidityMax = 60.0,
            preDurationHours = 24
        )
    }

    /**
     * 验证燃烧性能是否符合要求
     */
    fun validate(burnRate: Double, burnDistance: Double, burnTime: Int): ValidationResult {
        val issues = mutableListOf<String>()
        var isCompliant = true

        if (burnRate > maxBurnRate) {
            issues.add("燃烧速率${burnRate}mm/min超过最大值${maxBurnRate}mm/min")
            isCompliant = false
        }

        if (burnDistance > maxBurnDistance) {
            issues.add("燃烧距离${burnDistance}mm超过最大值${maxBurnDistance}mm")
            isCompliant = false
        }

        if (burnTime > maxBurnTime) {
            issues.add("燃烧时间${burnTime}秒超过最大值${maxBurnTime}秒")
            isCompliant = false
        }

        return ValidationResult(
            isCompliant = isCompliant,
            issues = issues
        )
    }
}

/**
 * 硬件性能要求（FMVSS 213 S5.4.2）
 */
data class HardwareRequirements(
    val maxProtrusion: Double,             // 最大突起（mm）
    val minEdgeRadius: Double,             // 最小边缘半径（mm）
    val saltSprayHours: Int,               // 盐雾测试时长（小时）
    val dryTimeHours: Int,                 // 干燥时长（小时）
    val tempTestCelsius: Double,           // 温度测试温度（℃）
    val tempTestHours: Int,                // 温度测试时长（小时）
    val allowDeformation: Boolean,         // 是否允许变形
    val allowFunctionalIssue: Boolean      // 是否允许功能问题
) {
    companion object {
        val STANDARD = HardwareRequirements(
            maxProtrusion = 9.5,
            minEdgeRadius = 6.35,
            saltSprayHours = 24,
            dryTimeHours = 1,
            tempTestCelsius = 80.0,
            tempTestHours = 24,
            allowDeformation = false,
            allowFunctionalIssue = false
        )
    }
}

/**
 * 头部与躯干支撑要求（FMVSS 213 S5.2.1/S5.2.2）
 */
data class SupportRequirements(
    val weightCategory: WeightCategory,    // 体重分类
    val minBackHeight: Double,             // 最小靠背高度（mm）
    val minBackArea: Double,               // 最小背部支撑面积（cm²）
    val minSideArea: Double,               // 最小侧面支撑面积（cm²）
    val minFrontRadius: Double,            // 前向约束面最小纵向截面半径（mm）
    val shapeRequirement: ShapeRequirement // 形状要求
) {
    companion object {
        /**
         * ≤18kg儿童的支撑要求
         */
        val UP_TO_18KG = SupportRequirements(
            weightCategory = WeightCategory.UP_TO_18KG,
            minBackHeight = 500.0,
            minBackArea = 548.0,            // 85平方英寸
            minSideArea = 310.0,            // <9kg儿童
            minFrontRadius = 51.0,
            shapeRequirement = ShapeRequirement.FLAT_OR_CONCAVE
        )

        /**
         * >18kg儿童的支撑要求
         */
        val OVER_18KG = SupportRequirements(
            weightCategory = WeightCategory.OVER_18KG,
            minBackHeight = 560.0,
            minBackArea = 548.0,            // 85平方英寸
            minSideArea = 155.0,            // ≥9kg儿童
            minFrontRadius = 51.0,
            shapeRequirement = ShapeRequirement.FLAT_OR_CONCAVE
        )
    }
}

/**
 * 体重分类
 */
enum class WeightCategory(val displayName: String, val maxWeightKg: Double) {
    UP_TO_18KG("≤18kg", 18.0),
    OVER_18KG(">18kg", 36.0)
}

/**
 * 形状要求
 */
enum class ShapeRequirement(val displayName: String) {
    FLAT_OR_CONCAVE("平面或凹面"),
    RADIUS_51MM("纵向截面半径≥51mm")
}

/**
 * Buckle性能要求（FMVSS 213 S5.4.3）
 */
data class BuckleRequirements(
    val maxReleaseForcePreCrash: Double,   // 碰撞前最大释放力（N）
    val maxReleaseForcePostCrash: Double,  // 碰撞后最大释放力（N）
    val minReleaseForcePreCrash: Double,   // 碰撞前最小释放力（N）
    val maxAdjustmentForce: Double,        // 最大调节力（N）
    val minTiltLockAngle: Double,          // 最小倾斜锁定角度（°）
    val minReleaseArea: Double,            // 最小释放面面积（cm²）
    val releaseAreaSqInches: Double        // 释放面面积（平方英寸）
) {
    companion object {
        val STANDARD = BuckleRequirements(
            maxReleaseForcePreCrash = 62.0,
            maxReleaseForcePostCrash = 71.0,
            minReleaseForcePreCrash = 40.0,
            maxAdjustmentForce = 49.0,
            minTiltLockAngle = 30.0,
            minReleaseArea = 3.9,
            releaseAreaSqInches = 0.6
        )
    }
}

/**
 * 正面碰撞参数（FMVSS 213 S6）
 */
data class FrontalCrashParameters(
    val configuration: String,             // 配置类型
    val velocityKmh: Double,               // 速度（km/h）
    val velocityRange: String,             // 速度范围
    val accelerationProfile: AccelerationProfile, // 加速度走廊
    val headExcursionWithTether: Double,   // 头部位移（有tether，mm）
    val headExcursionNoTether: Double,     // 头部位移（无tether，mm）
    val kneeExcursion: Double,             // 膝盖位移（mm）
    val hic36Limit: Double,                // HIC36限值
    val chestAccelerationLimit: Double,    // 胸部加速度限值（g）
    val chestAccelerationDuration: Double  // 胸部加速度累积时间（ms）
) {
    companion object {
        val CONFIGURATION_I = FrontalCrashParameters(
            configuration = "Configuration I",
            velocityKmh = 48.0,
            velocityRange = "48±3.2 km/h",
            accelerationProfile = AccelerationProfile.FRONTAL,
            headExcursionWithTether = 720.0,
            headExcursionNoTether = 813.0,
            kneeExcursion = 915.0,
            hic36Limit = 1000.0,
            chestAccelerationLimit = 60.0,
            chestAccelerationDuration = 3.0
        )
    }
}

/**
 * 侧面碰撞参数（FMVSS 213a S6）
 */
data class SideCrashParameters(
    val relativeVelocityKmh: Double,       // 相对速度（km/h）
    val velocityRange: String,             // 速度范围
    val accelerationProfile: AccelerationProfile, // 加速度走廊
    val chestCompressionLimit: Double,     // 胸部压缩量限值（mm）
    val hic15Limit: Double,                // HIC15限值
    val doorDistance: Double,              // 与车门距离（mm）
    val doorDistanceRange: String,         // 距离范围
    val sisa2InchCompressionMin: Double,   // SISA 2英寸泡沫压缩载荷最小值（N）
    val sisa2InchCompressionMax: Double,   // SISA 2英寸泡沫压缩载荷最大值（N）
    val sisa4InchCompressionMin: Double,   // SISA 4英寸泡沫压缩载荷最小值（N）
    val sisa4InchCompressionMax: Double    // SISA 4英寸泡沫压缩载荷最大值（N）
) {
    companion object {
        val STANDARD = SideCrashParameters(
            relativeVelocityKmh = 31.3,
            velocityRange = "31.3±0.64 km/h",
            accelerationProfile = AccelerationProfile.SIDE,
            chestCompressionLimit = 23.0,
            hic15Limit = 570.0,
            doorDistance = 38.0,
            doorDistanceRange = "38±6 mm",
            sisa2InchCompressionMin = 255.0,
            sisa2InchCompressionMax = 345.0,
            sisa4InchCompressionMin = 374.0,
            sisa4InchCompressionMax = 506.0
        )
    }
}

/**
 * 加速度走廊
 */
data class AccelerationProfile(
    val type: ImpactTypeFMVSS,
    val profile: List<AccelerationPoint>
) {
    companion object {
        /**
         * 正面碰撞加速度走廊
         * 0ms→3g、10ms→25g、52ms→25g、90ms→0g
         */
        val FRONTAL = AccelerationProfile(
            type = ImpactTypeFMVSS.FRONTAL,
            profile = listOf(
                AccelerationPoint(0, 3.0),
                AccelerationPoint(10, 25.0),
                AccelerationPoint(52, 25.0),
                AccelerationPoint(90, 0.0)
            )
        )

        /**
         * 侧面碰撞加速度走廊
         * 0ms→0.5g、6ms→25.5g、44ms→25.5g、58ms→0g
         */
        val SIDE = AccelerationProfile(
            type = ImpactTypeFMVSS.SIDE,
            profile = listOf(
                AccelerationPoint(0, 0.5),
                AccelerationPoint(6, 25.5),
                AccelerationPoint(44, 25.5),
                AccelerationPoint(58, 0.0)
            )
        )
    }
}

/**
 * 加速度点
 */
data class AccelerationPoint(
    val timeMs: Int,                       // 时间（毫秒）
    val acceleration: Double               // 加速度（g）
)

/**
 * 标签要求（FMVSS 213 S5.5）
 */
data class LabelRequirements(
    val minFontSize: Int,                  // 最小字体大小
    val requiredInfo: List<String>,        // 必备信息
    val format: LabelFormat,               // 格式要求
    val aircraftMarking: AircraftMarking   // 飞机认证标记
) {
    companion object {
        val STANDARD = LabelRequirements(
            minFontSize = 10,
            requiredInfo = listOf(
                "型号/制造商",
                "生产日期/产地",
                "适配体重/身高（双语）",
                "符合联邦安全标准声明",
                "安全警告（如Rear-facing禁放前排气囊位）"
            ),
            format = LabelFormat.STANDARD,
            aircraftMarking = AircraftMarking.REQUIRED
        )
    }
}

/**
 * 标签格式
 */
enum class LabelFormat(val displayName: String) {
    STANDARD("白底黑字"),
    WARNING("警告区黄底黑字"),
    AIRCRAFT("飞机认证专用")
}

/**
 * 飞机认证标记
 */
enum class AircraftMarking(val displayName: String) {
    REQUIRED("适用于机动车与飞机"),
    NOT_APPLICABLE("不适用于飞机"),
    OPTIONAL("可选标注")
}

/**
 * 说明书要求（FMVSS 213 S5.6/S5.8）
 */
data class ManualRequirements(
    val includeInstallationDiagrams: Boolean,  // 包含分步安装图
    val includeWarningConsequences: Boolean,   // 包含警告后果说明
    val includeRecallRegistration: Boolean,   // 包含召回注册指引
    val installationMethods: List<InstallationMethodFMVSS>, // 安装方式
    val registrationCardFormat: String         // 注册卡格式
) {
    companion object {
        val STANDARD = ManualRequirements(
            includeInstallationDiagrams = true,
            includeWarningConsequences = true,
            includeRecallRegistration = true,
            installationMethods = listOf(
                InstallationMethodFMVSS.LATCH,
                InstallationMethodFMVSS.THREE_POINT_BELT,
                InstallationMethodFMVSS.AIRCRAFT
            ),
            registrationCardFormat = "符合图37格式"
        )
    }
}

/**
 * 验证结果
 */
data class ValidationResult(
    val isCompliant: Boolean,
    val issues: List<String>
) {
    fun getFormattedReport(): String {
        val report = StringBuilder()
        
        if (isCompliant) {
            report.appendLine("✅ 合规性验证通过")
        } else {
            report.appendLine("❌ 合规性验证失败")
            report.appendLine()
            report.appendLine("发现的问题：")
            issues.forEach { issue ->
                report.appendLine("  - $issue")
            }
        }
        
        return report.toString()
    }
}

/**
 * FMVSS 213/213a完整标准数据
 */
data class FMVSSStandardData(
    val standardType: FMVSSStandardType,
    val webbingRequirements: WebbingRequirements,
    val flammabilityRequirements: FlammabilityRequirements,
    val hardwareRequirements: HardwareRequirements,
    val supportRequirements: List<SupportRequirements>,
    val buckleRequirements: BuckleRequirements,
    val frontalCrashParameters: FrontalCrashParameters,
    val sideCrashParameters: SideCrashParameters,
    val labelRequirements: LabelRequirements,
    val manualRequirements: ManualRequirements
) {
    companion object {
        /**
         * 获取FMVSS 213标准数据
         */
        fun getFMVSS213(): FMVSSStandardData {
            return FMVSSStandardData(
                standardType = FMVSSStandardType.FMVSS_213,
                webbingRequirements = WebbingRequirements.VEHICLE_ATTACHMENT,
                flammabilityRequirements = FlammabilityRequirements.STANDARD,
                hardwareRequirements = HardwareRequirements.STANDARD,
                supportRequirements = listOf(
                    SupportRequirements.UP_TO_18KG,
                    SupportRequirements.OVER_18KG
                ),
                buckleRequirements = BuckleRequirements.STANDARD,
                frontalCrashParameters = FrontalCrashParameters.CONFIGURATION_I,
                sideCrashParameters = SideCrashParameters.STANDARD,
                labelRequirements = LabelRequirements.STANDARD,
                manualRequirements = ManualRequirements.STANDARD
            )
        }

        /**
         * 获取FMVSS 213a标准数据
         */
        fun getFMVSS213a(): FMVSSStandardData {
            return FMVSSStandardData(
                standardType = FMVSSStandardType.FMVSS_213A,
                webbingRequirements = WebbingRequirements.VEHICLE_ATTACHMENT,
                flammabilityRequirements = FlammabilityRequirements.STANDARD,
                hardwareRequirements = HardwareRequirements.STANDARD,
                supportRequirements = listOf(
                    SupportRequirements.UP_TO_18KG,
                    SupportRequirements.OVER_18KG
                ),
                buckleRequirements = BuckleRequirements.STANDARD,
                frontalCrashParameters = FrontalCrashParameters.CONFIGURATION_I,
                sideCrashParameters = SideCrashParameters.STANDARD,
                labelRequirements = LabelRequirements.STANDARD,
                manualRequirements = ManualRequirements.STANDARD
            )
        }
    }
}

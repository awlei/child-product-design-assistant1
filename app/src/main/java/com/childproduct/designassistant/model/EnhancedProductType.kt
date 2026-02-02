package com.childproduct.designassistant.model

/**
 * 增强的产品类型枚举
 * 包含产品类型的基本信息和输入需求
 */
enum class EnhancedProductType(
    val displayName: String,
    val code: String,
    val requiresHeightInput: Boolean,
    val requiresInstallMethod: Boolean
) {
    SAFETY_SEAT("儿童安全座椅", "SAFETY_SEAT", true, true),
    STROLLER("婴儿推车", "STROLLER", false, false),
    HIGH_CHAIR("儿童高脚椅", "HIGH_CHAIR", false, false),
    CRIB("婴儿床", "CRIB", false, false);

    fun toProductType(): ProductType {
        return when (this) {
            SAFETY_SEAT -> ProductType.CHILD_SAFETY_SEAT
            STROLLER -> ProductType.STROLLER
            HIGH_CHAIR -> ProductType.HIGH_CHAIR
            CRIB -> ProductType.CRIB
        }
    }
}

/**
 * 国际标准枚举
 * 包含标准的基本信息、适用地区和适用产品
 */
enum class InternationalStandard(
    val displayName: String,
    val code: String,
    val region: String,
    val applicableProducts: List<ProductType>
) {
    ECE_R129("ECE R129 (i-Size)", "ECE R129", "欧洲", listOf(ProductType.CHILD_SAFETY_SEAT)),
    FMVSS_213("FMVSS 213", "FMVSS 213", "美国", listOf(ProductType.CHILD_SAFETY_SEAT)),
    JIS("JIS D 0401", "JIS", "日本", listOf(ProductType.CHILD_SAFETY_SEAT, ProductType.STROLLER)),
    CANADIAN("CMVSS 213", "CMVSS 213", "加拿大", listOf(ProductType.CHILD_SAFETY_SEAT)),
    AUSTRALIAN("AS/NZS 1754", "AS/NZS 1754", "澳洲", listOf(ProductType.CHILD_SAFETY_SEAT)),
    GB_27887("GB 27887-2024", "GB 27887-2024", "中国", listOf(ProductType.CHILD_SAFETY_SEAT)),
    GB_14748("GB 14748", "GB 14748", "中国", listOf(ProductType.STROLLER)),
    GB_22793("GB 22793", "GB 22793", "中国", listOf(ProductType.HIGH_CHAIR)),
    EN_1888("EN 1888", "EN 1888", "欧洲", listOf(ProductType.CHILD_STROLLER))
}

/**
 * 安装方向枚举
 */
enum class InstallDirection(val displayName: String) {
    REARWARD("后向安装"),  // 后向安装
    FORWARD("前向安装")    // 前向安装
}

/**
 * 增强的安装方式枚举
 * 包含固定类型、防旋转类型和适用方向
 */
enum class EnhancedInstallMethod(
    val displayName: String,
    val fixedType: String,      // ISOFIX / Vehicle Seat Belt
    val antiRotationType: String, // Support Leg / Top Tether
    val applicableDirection: InstallDirection
) {
    ISOFIX_SUPPORT_LEG(
        "ISOFIX + 支撑腿",
        "ISOFIX 3 pts",
        "Support leg",
        InstallDirection.REARWARD  // 仅适用于后向安装
    ),
    ISOFIX_TOP_TETHER(
        "ISOFIX + Top-tether",
        "ISOFIX 3 pts",
        "Top Tether",
        InstallDirection.FORWARD   // 仅适用于前向安装
    ),
    SEAT_BELT_TOP_TETHER(
        "安全带 + Top-tether",
        "Vehicle Seat Belt",
        "Top Tether",
        InstallDirection.FORWARD
    ),
    SEAT_BELT_ONLY(
        "仅车辆安全带",
        "Vehicle Seat Belt",
        "Without",
        InstallDirection.FORWARD   // 不推荐，仅用于特定场景
    )
}

/**
 * 假人类型枚举
 */
enum class CrashTestDummy(
    val code: String,
    val displayName: String,
    val heightRange: String,
    val ageRange: String,
    val weight: String,
    val age: String,           // 添加 age 字段，与 ageRange 保持一致
    val hicLimit: Int         // 添加 hicLimit 字段，基于 UN R129 标准
) {
    Q0("Q0", "新生儿假人", "40-50cm", "0-6个月", "3.5kg", "0-6个月", 390),
    Q0_PLUS("Q0+", "大婴儿假人", "50-60cm", "6-12个月", "6kg", "6-12个月", 390),
    Q1("Q1", "幼儿假人", "60-75cm", "1-2岁", "9.5kg", "9-18个月", 390),
    Q1_5("Q1.5", "学步儿童假人", "75-87cm", "2-3岁", "11kg", "18-36个月", 570),
    Q3("Q3", "学前儿童假人", "87-105cm", "3-4岁", "15kg", "3-4岁", 1000),
    Q3_S("Q3s", "儿童假人", "105-125cm", "4-6岁", "21.5kg", "4-7岁", 1000),
    Q6("Q6", "大龄儿童假人", "125-145cm", "6-10岁", "30kg", "7-10岁", 1000),
    Q10("Q10", "青少年假人", "145-150cm", "10-12岁", "36kg", "10岁以上", 1000);

    companion object {
        /**
         * 根据年龄段获取推荐的假人类型（返回中间值）
         */
        fun getByAgeGroup(ageGroup: AgeGroup): CrashTestDummy {
            return when (ageGroup) {
                AgeGroup.INFANT -> Q1           // 9-18个月（新生儿到幼儿的中间值）
                AgeGroup.TODDLER -> Q1_5       // 18-36个月（学步儿童）
                AgeGroup.PRESCHOOL -> Q3       // 3-4岁（学前儿童）
                AgeGroup.SCHOOL_AGE -> Q3_S    // 4-7岁（学龄儿童）
                AgeGroup.TEEN -> Q10           // 10岁以上（青少年）
                AgeGroup.ALL -> Q3             // 全年龄段（0-12岁），返回中间值假人
            }
        }

        /**
         * 将 CrashTestDummy 转换为 ComplianceDummy
         */
        fun toComplianceDummy(crashTestDummy: CrashTestDummy): ComplianceDummy {
            return when (crashTestDummy) {
                Q0 -> ComplianceDummy.Q0
                Q0_PLUS -> ComplianceDummy.Q0_PLUS
                Q1 -> ComplianceDummy.Q1
                Q1_5 -> ComplianceDummy.Q1_5
                Q3 -> ComplianceDummy.Q3
                Q3_S -> ComplianceDummy.Q3_S
                Q6 -> ComplianceDummy.Q6
                Q10 -> ComplianceDummy.Q10
            }
        }
    }
}

/**
 * 设计输入数据模型
 */
data class DesignInput(
    val productType: EnhancedProductType,
    val standards: List<InternationalStandard>,
    val heightRange: String?,          // 例如："40-105cm"
    val installMethod: EnhancedInstallMethod?,
    val theme: String = ""
)

/**
 * 假人映射数据模型
 */
data class DummyMapping(
    val minHeight: Int,
    val maxHeight: Int,
    val dummyTypeCode: String,
    val dummyType: String,
    val ageRange: String
)

/**
 * ROADMATE 360测试项数据模型
 */
data class Roadmate360TestItem(
    val sample: String,
    val pulse: String,
    val impact: String,
    val dummy: String,
    val position: String,
    val installation: String,
    val specificInstallation: String,
    val productConfiguration: String,
    val isofixAnchors: String,
    val positionOfFloor: String,
    val harness: String,
    val topTetherSupportLeg: String,
    val dashboard: String,
    val comments: String,
    val buckle: String,
    val adjuster: String,
    val isofix: String,
    val tt: String,
    val quantity: String,
    val testNo: String
)

/**
 * 安全阈值数据模型
 */
data class SafetyThreshold(
    val testItem: String,
    val standardRequirement: String,
    val applicableDummy: String,
    val unit: String,
    val standardSource: String
)

/**
 * 安装方向配置
 */
typealias InstallDirectionsMap = Map<IntRange, InstallDirection>

/**
 * 生成的设计方案数据模型
 */
data class GeneratedDesignScheme(
    val productType: String,
    val heightRange: String,
    val ageRange: String,
    val designTheme: String,
    val standardMapping: String,
    val installMethodDesc: String,
    val isoFixEnvelopeDesc: String?,
    val testMatrix: String,
    val safetyThresholds: String,
    val complianceStatement: String,
    val safetyNotes: String
)

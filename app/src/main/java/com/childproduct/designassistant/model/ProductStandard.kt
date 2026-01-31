package com.childproduct.designassistant.model

/**
 * 国际标准枚举
 */
enum class InternationalStandard(
    val displayName: String,
    val englishName: String,
    val applicableProductType: ProductType
) {
    // 儿童安全座椅标准
    ECE_R129("ECE R129/i-Size", "ECE R129/i-Size", ProductType.CHILD_SAFETY_SEAT),
    FMVSS_213("FMVSS 213", "FMVSS 213", ProductType.CHILD_SAFETY_SEAT),
    GB_27887("GB 27887", "GB 27887", ProductType.CHILD_SAFETY_SEAT),
    
    // 婴儿推车标准
    EN_1888("EN 1888", "EN 1888", ProductType.BABY_STROLLER),
    ASTM_F833("ASTM F833", "ASTM F833", ProductType.BABY_STROLLER),
    
    // 通用标准
    ISO_13209("ISO 13209", "ISO 13209", ProductType.CHILD_HOUSEHOLD_GOODS)
}

/**
 * 产品细分类型 - 儿童安全座椅
 */
enum class SafetySeatSubtype(
    val displayName: String,
    val englishName: String,
    val description: String
) {
    GROUP_0_PLUS("Group 0+", "Group 0+", "新生儿专用，后向安装，适合出生-13kg"),
    GROUP_0_1("Group 0/1", "Group 0/1", "0-18kg，可转换安装方向"),
    GROUP_1("Group 1", "Group 1", "9-18kg，前向安装"),
    GROUP_2_3("Group 2/3", "Group 2/3", "15-36kg，高背增高"),
    ISOFIX_TYPE("ISOFIX接口型", "ISOFIX Type", "使用ISOFIX硬连接安装"),
    PORTABLE("便携型", "Portable", "可快速拆卸转移"),
    ROTATABLE("可旋转型", "Rotatable", "360度旋转，方便上下车"),
    BOOSTER("增高垫", "Booster", "无靠背增高垫")
}

/**
 * 产品细分类型 - 婴儿推车
 */
enum class StrollerSubtype(
    val displayName: String,
    val englishName: String,
    val description: String
) {
    HIGH_VIEW("高景观型", "High View", "座位高，视野好，远离尾气"),
    LIGHTWEIGHT_FOLD("轻便折叠型", "Lightweight Fold", "重量轻，一键折叠"),
    SPORT("运动型", "Sport", "大轮避震，适合户外"),
    TWIN("双胞胎专用型", "Twin", "双座设计，可并排/前后"),
    TRAVEL_SYSTEM("旅行系统", "Travel System", "适配安全座椅睡篮"),
    JOGGING("慢跑推车", "Jogging", "三轮设计，适合慢跑")
}

/**
 * 使用场景
 */
enum class UsageScenario(
    val displayName: String,
    val description: String
) {
    CITY_COMMUTE("城市通勤", "日常城市出行，地铁公交换乘"),
    OUTDOOR_OFFROAD("户外越野", "公园、草地等户外场景"),
    TRAVEL_PORTABLE("旅行便携", "飞机、高铁等长途旅行"),
    SHOPPING("购物", "商场超市购物场景"),
    DAILY_WALK("日常散步", "小区内日常散步")
}

/**
 * 标准要求核心参数
 */
data class StandardRequirements(
    val standard: InternationalStandard,
    val productType: ProductType,
    val heightRange: HeightRequirement?,
    val weightRange: WeightRequirement?,
    val dimensionalRequirements: DimensionalRequirements?,
    val performanceRequirements: PerformanceRequirements?,
    val safetyRequirements: SafetyRequirements?
)

/**
 * 身高要求
 */
data class HeightRequirement(
    val minHeight: Double,  // cm
    val maxHeight: Double,  // cm
    val recommendedDirection: InstallDirection,
    val forceRearwardUntil: Double? = null  // 强制后向安装高度
)

/**
 * 重量要求
 */
data class WeightRequirement(
    val minWeight: Double,  // kg
    val maxWeight: Double,  // kg
    val unit: WeightUnit
)

/**
 * 尺寸要求
 */
data class DimensionalRequirements(
    val envelopeDimensions: EnvelopeDimensions? = null,  // i-Size包络尺寸
    val seatDepth: DoubleRange?,  // 座椅深度
    val seatWidth: DoubleRange?,  // 座椅宽度
    val headrestWidth: DoubleRange?,  // 头托宽度
    val shoulderWidth: DoubleRange?  // 肩宽
)

/**
 * 包络尺寸（i-Size Envelope）
 */
data class EnvelopeDimensions(
    val width: Double,   // 最大宽度 44cm
    val length: Double,  // 长度 75cm
    val height: Double   // 高度 81cm
)

/**
 * 性能要求
 */
data class PerformanceRequirements(
    val impactTestGForce: DoubleRange?,  // 碰撞测试G-force阈值
    val brakePerformance: String? = null,  // 制动性能
    val foldLockReliability: String? = null,  // 折叠锁定可靠性
    val shockAbsorption: String? = null  // 避震性能
)

/**
 * 安全要求
 */
data class SafetyRequirements(
    val noSharpEdges: Boolean = true,  // 无尖锐边缘
    val harnessType: HarnessType?,  // 安全带类型
    val restraintSystem: String? = null,  // 约束系统
    val materialFlammability: Boolean = true,  // 材料阻燃性
    val chemicalSafety: Boolean = true  // 化学安全性
)

/**
 * 安装方向
 */
enum class InstallDirection(val displayName: String) {
    REARWARD("后向安装"),
    FORWARD("前向安装"),
    CONVERTIBLE("可转换"),
    UNIVERSAL("通用")
}

/**
 * 安全带类型
 */
enum class HarnessType(val displayName: String) {
    FIVE_POINT("五点式安全带"),
    THREE_POINT("三点式安全带"),
    SHIELD("护盾式")
}

/**
 * 双精度范围
 */
data class DoubleRange(
    val min: Double,
    val max: Double
)

/**
 * 重量单位
 */
enum class WeightUnit(val displayName: String, val symbol: String, val toKgFactor: Double) {
    KG("公斤", "kg", 1.0),
    LB("磅", "lb", 0.453592),
    G("克", "g", 0.001)
}

/**
 * 假人数据
 */
data class DummyData(
    val dummyName: String,
    val age: String,
    val weight: Double,
    val height: Double,
    val shoulderHeight: Double,
    val headCircumference: Double
)

/**
 * 测试假人集合
 */
val CRABI_DUMMY = DummyData(
    dummyName = "CRABI 12个月假人",
    age = "12个月",
    weight = 10.0,  // 22磅 ≈ 10kg
    height = 75.0,
    shoulderHeight = 38.0,
    headCircumference = 46.0
)

val HYBRID_III_3Y_DUMMY = DummyData(
    dummyName = "Hybrid III 3岁假人",
    age = "3岁",
    weight = 14.0,  // 31磅 ≈ 14kg
    height = 95.0,
    shoulderHeight = 48.0,
    headCircumference = 50.0
)

val Q3S_DUMMY = DummyData(
    dummyName = "Q3s 3岁假人",
    age = "3岁",
    weight = 14.5,
    height = 98.0,
    shoulderHeight = 50.0,
    headCircumference = 51.0
)

package com.childproduct.designassistant.data

/**
 * ECE R129 标准数据库对象
 * 
 * ECE R129 (i-Size) 欧洲儿童安全座椅标准
 * 
 * 包含内容：
 * - ECE R129 标准的详细信息和测试要求
 * - Q系列假人类型和规格
 * - 假人分组和重量/身高对应关系
 * - 测试条件和通过标准
 * - 安全阈值和尺寸要求
 * 
 * @see ECE R129 (i-Size Regulation)
 * @see ECE R44/04 替代标准
 */

/**
 * ECE R129 假人类型
 */
enum class EceR129DummyType(val code: String, val displayName: String, val weightKg: Double, val heightCm: Double) {
    Q0("Q0", "Q0假人", 1.5, 50.0),
    Q1("Q1", "Q1假人", 9.0, 75.0),
    Q1_5("Q1.5", "Q1.5假人", 11.0, 83.0),
    Q3("Q3", "Q3假人", 15.0, 100.0),
    Q6("Q6", "Q6假人", 22.0, 125.0),
    Q10("Q10", "Q10假人", 32.0, 150.0)
}

/**
 * ECE R129 假人分组
 */
enum class EceR129DummyGroup(val group: String, val displayName: String, val sizeCode: String) {
    GROUP_0("Group 0", "新生儿组", "40"),
    GROUP_0_PLUS("Group 0+", "婴儿组", "45"),
    GROUP_1("Group 1", "幼儿组", "55"),
    GROUP_2("Group 2", "儿童组", "85"),
    GROUP_3("Group 3", "大童组", "105")
}

/**
 * ECE R129 伤害指标
 */
data class EceR129InjuryCriteria(
    val criterionName: String,
    val limitValue: Double,
    val unit: String,
    val dummyTypes: List<EceR129DummyType>,
    val description: String
)

/**
 * ECE R129 假人规格
 */
data class EceR129DummySpec(
    val dummyType: EceR129DummyType,
    val group: EceR129DummyGroup,
    val weight: Double, // kg
    val height: Double, // cm
    const val headCircumference: Double, // cm
    val shoulderWidth: Double, // mm
    val shoulderHeight: Double, // mm
    val hipWidth: Double, // mm
    val headWidth: Double, // mm
    val headHeight: Double, // mm
    val适用车型: List<String>,
    val安装方向: List<String>
)

/**
 * ECE R129 测试矩阵
 */
data class EceR129TestMatrix(
    val matrixId: String,
    val dummyType: EceR129DummyType,
    val seatSize: String,
    const val 速度范围: String,
    val velocityMin: Int, // km/h
    val velocityMax: Int, // km/h
    const val 速度标准: String,
    const val 刹车距离: String, // mm
    val 刹车距离Range: String
)

/**
 * ECE R129 尺寸阈值
 */
data class EceR129DimensionThreshold(
    val thresholdId: String,
    val dummyType: EceR129DummyType,
    val dimension: String,
    val minValue: Double,
    val maxValue: Double,
    val unit: String,
    val 适用场景: String
)

/**
 * ECE R129 标准数据库对象
 */
object EceR129StandardDatabase {
    
    // ========== 标准基本信息 ==========
    
    val STANDARD_INFO = StandardBasicInfo(
        standardId = "ECE-R129",
        standardName = "Uniform Provisions for the Approval of ISOFIX Child Restraint Systems",
        standardType = StandardCategory.INTERNATIONAL,
        applicableRegion = "欧洲 (ECE)",
        applicableWeight = "0-36kg",
        applicableAge = "新生儿至12岁",
        coreScope = "规定基于身高、体重和ISOFIX系统的儿童安全座椅测试要求，使用Q系列假人，强调头部保护和侧面碰撞",
        effectiveDate = "2013年7月",
        standardStatus = "Current (逐步替代R44)",
        dataSource = "Economic Commission for Europe (UNECE)"
    )
    
    // ========== 假人规格 ==========
    
    val DUMMY_SPECS = listOf(
        EceR129DummySpec(
            dummyType = EceR129DummyType.Q0,
            group = EceR129DummyGroup.GROUP_0,
            weight = 1.5,
            height = 50.0,
            headCircumference = 32.5,
            shoulderWidth = 63.0,
            shoulderHeight = 170.0,
            hipWidth = 65.0,
            headWidth = 105.0,
            headHeight = 115.0,
            适用车型 = listOf("后排中间", "后排两侧"),
            安装方向 = listOf("后向安装")
        ),
        EceR129DummySpec(
            dummyType = EceR129DummyType.Q1,
            group = EceR129DummyGroup.GROUP_0_PLUS,
            weight = 9.0,
            height = 75.0,
            headCircumference = 44.5,
            shoulderWidth = 83.0,
            shoulderHeight = 185.0,
            hipWidth = 83.0,
            headWidth = 125.0,
            headHeight = 130.0,
            适用车型 = listOf("后排中间", "后排两侧"),
            安装方向 = listOf("后向安装")
        ),
        EceR129DummySpec(
            dummyType = EceR129DummyType.Q1_5,
            group = EceR129DummyGroup.GROUP_1,
            weight = 11.0,
            height = 83.0,
            headCircumference = 46.5,
            shoulderWidth = 85.0,
            shoulderHeight = 190.0,
            hipWidth = 85.0,
            headWidth = 127.0,
            headHeight = 132.0,
            适用车型 = listOf("后排中间", "后排两侧"),
            安装方向 = listOf("后向安装", "前向安装")
        ),
        EceR129DummySpec(
            dummyType = EceR129DummyType.Q3,
            group = EceR129DummyGroup.GROUP_1,
            weight = 15.0,
            height = 100.0,
            headCircumference = 49.0,
            shoulderWidth = 95.0,
            shoulderHeight = 200.0,
            hipWidth = 95.0,
            headWidth = 135.0,
            headHeight = 140.0,
            适用车型 = listOf("后排两侧"),
            安装方向 = listOf("前向安装")
        ),
        EceR129DummySpec(
            dummyType = EceR129DummyType.Q6,
            group = EceR129DummyGroup.GROUP_2,
            weight = 22.0,
            height = 125.0,
            headCircumference = 51.5,
            shoulderWidth = 105.0,
            shoulderHeight = 210.0,
            hipWidth = 105.0,
            headWidth = 145.0,
            headHeight = 145.0,
            适用车型 = listOf("后排两侧"),
            安装方向 = listOf("前向安装")
        ),
        EceR129DummySpec(
            dummyType = EceR129DummyType.Q10,
            group = EceR129DummyGroup.GROUP_3,
            weight = 32.0,
            height = 150.0,
            headCircumference = 53.5,
            shoulderWidth = 115.0,
            shoulderHeight = 220.0,
            hipWidth = 115.0,
            headWidth = 155.0,
            headHeight = 150.0,
            适用车型 = listOf("后排两侧"),
            安装方向 = listOf("前向安装")
        )
    )
    
    // ========== 测试矩阵 ==========
    
    val TEST_MATRICES = mapOf(
        "Q0" to listOf(
            EceR129TestMatrix(
                matrixId = "R129-Q0",
                dummyType = EceR129DummyType.Q0,
                seatSize = "40",
                速度范围 = "50-51 km/h",
                velocityMin = 50,
                velocityMax = 51,
                速度标准 = "50 km/h",
                刹车距离 = "650",
                刹车距离Range = "650±30 mm"
            )
        ),
        "Q1" to listOf(
            EceR129TestMatrix(
                matrixId = "R129-Q1",
                dummyType = EceR129DummyType.Q1,
                seatSize = "45",
                速度范围 = "50-51 km/h",
                velocityMin = 50,
                velocityMax = 51,
                速度标准 = "50 km/h",
                刹车距离 = "650",
                刹车距离Range = "650±30 mm"
            )
        ),
        "Q1.5" to listOf(
            EceR129TestMatrix(
                matrixId = "R129-Q1.5-RF",
                dummyType = EceR129DummyType.Q1_5,
                seatSize = "55",
                速度范围 = "50-51 km/h",
                velocityMin = 50,
                velocityMax = 51,
                速度标准 = "50 km/h",
                刹车距离 = "650",
                刹车距离Range = "650±30 mm"
            )
        ),
        "Q3" to listOf(
            EceR129TestMatrix(
                matrixId = "R129-Q3",
                dummyType = EceR129DummyType.Q3,
                seatSize = "55",
                速度范围 = "50-51 km/h",
                velocityMin = 50,
                velocityMax = 51,
                速度标准 = "50 km/h",
                刹车距离 = "650",
                刹车距离Range = "650±30 mm"
            )
        ),
        "Q6" to listOf(
            EceR129TestMatrix(
                matrixId = "R129-Q6",
                dummyType = EceR129DummyType.Q6,
                seatSize = "85",
                速度范围 = "50-51 km/h",
                velocityMin = 50,
                velocityMax = 51,
                速度标准 = "50 km/h",
                刹车距离 = "650",
                刹车距离Range = "650±30 mm"
            )
        ),
        "Q10" to listOf(
            EceR129TestMatrix(
                matrixId = "R129-Q10",
                dummyType = EceR129DummyType.Q10,
                seatSize = "105",
                速度范围 = "50-51 km/h",
                velocityMin = 50,
                velocityMax = 51,
                速度标准 = "50 km/h",
                刹车距离 = "650",
                刹车距离Range = "650±30 mm"
            )
        )
    )
    
    // ========== 伤害指标 ==========
    
    val INJURY_CRITERIA = listOf(
        EceR129InjuryCriteria(
            criterionName = "HIC36",
            limitValue = 1000.0,
            unit = "",
            dummyTypes = listOf(EceR129DummyType.Q0, EceR129DummyType.Q1, EceR129DummyType.Q1_5),
            description = "头部伤害指数，36ms时间窗"
        ),
        EceR129InjuryCriteria(
            criterionName = "HIC15",
            limitValue = 570.0,
            unit = "",
            dummyTypes = listOf(EceR129DummyType.Q3, EceR129DummyType.Q6, EceR129DummyType.Q10),
            description = "头部伤害指数，15ms时间窗"
        ),
        EceR129InjuryCriteria(
            criterionName = "胸部加速度3ms",
            limitValue = 55.0,
            unit = "g",
            dummyTypes = EceR129DummyType.values().toList(),
            description = "胸部3ms累积加速度"
        ),
        EceR129InjuryCriteria(
            criterionName = "颈部剪切力",
            limitValue = 1250.0,
            unit = "N",
            dummyTypes = listOf(EceR129DummyType.Q0, EceR129DummyType.Q1, EceR129DummyType.Q1_5),
            description = "颈部剪切力（后向安装）"
        ),
        EceR129InjuryCriteria(
            criterionName = "颈部拉伸力",
            limitValue = 830.0,
            unit = "N",
            dummyTypes = listOf(EceR129DummyType.Q0, EceR129DummyType.Q1, EceR129DummyType.Q1_5),
            description = "颈部拉伸力（后向安装）"
        ),
        EceR129InjuryCriteria(
            criterionName = "颈部弯矩",
            limitValue = 20.0,
            unit = "Nm",
            dummyTypes = listOf(EceR129DummyType.Q0, EceR129DummyType.Q1, EceR129DummyType.Q1_5),
            description = "颈部弯矩（后向安装）"
        ),
        EceR129InjuryCriteria(
            criterionName = "颈部弯矩",
            limitValue = 57.0,
            unit = "Nm",
            dummyTypes = listOf(EceR129DummyType.Q3, EceR129DummyType.Q6, EceR129DummyType.Q10),
            description = "颈部弯矩（前向安装）"
        )
    )
    
    // ========== 尺寸阈值 ==========
    
    val DIMENSION_THRESHOLDS = listOf(
        // Q0 尺寸阈值
        EceR129DimensionThreshold(
            thresholdId = "R129-Q0-SH",
            dummyType = EceR129DummyType.Q0,
            dimension = "肩部高度",
            minValue = 170.0,
            maxValue = 190.0,
            unit = "mm",
            适用场景 = "座椅高度设计"
        ),
        EceR129DimensionThreshold(
            thresholdId = "R129-Q0-SW",
            dummyType = EceR129DummyType.Q0,
            dimension = "肩部宽度",
            minValue = 63.0,
            maxValue = 73.0,
            unit = "mm",
            适用场景 = "靠背宽度设计"
        ),
        // Q1 尺寸阈值
        EceR129DimensionThreshold(
            thresholdId = "R129-Q1-SH",
            dummyType = EceR129DummyType.Q1,
            dimension = "肩部高度",
            minValue = 185.0,
            maxValue = 205.0,
            unit = "mm",
            适用场景 = "座椅高度设计"
        ),
        EceR129DimensionThreshold(
            thresholdId = "R129-Q1-SW",
            dummyType = EceR129DummyType.Q1,
            dimension = "肩部宽度",
            minValue = 83.0,
            maxValue = 93.0,
            unit = "mm",
            适用场景 = "靠背宽度设计"
        ),
        // Q1.5 尺寸阈值
        EceR129DimensionThreshold(
            thresholdId = "R129-Q1.5-SH",
            dummyType = EceR129DummyType.Q1_5,
            dimension = "肩部高度",
            minValue = 190.0,
            maxValue = 210.0,
            unit = "mm",
            适用场景 = "座椅高度设计"
        ),
        EceR129DimensionThreshold(
            thresholdId = "R129-Q1.5-SW",
            dummyType = EceR129DummyType.Q1_5,
            dimension = "肩部宽度",
            minValue = 85.0,
            maxValue = 95.0,
            unit = "mm",
            适用场景 = "靠背宽度设计"
        ),
        // Q3 尺寸阈值
        EceR129DimensionThreshold(
            thresholdId = "R129-Q3-SH",
            dummyType = EceR129DummyType.Q3,
            dimension = "肩部高度",
            minValue = 200.0,
            maxValue = 220.0,
            unit = "mm",
            适用场景 = "座椅高度设计"
        ),
        EceR129DimensionThreshold(
            thresholdId = "R129-Q3-SW",
            dummyType = EceR129DummyType.Q3,
            dimension = "肩部宽度",
            minValue = 95.0,
            maxValue = 105.0,
            unit = "mm",
            适用场景 = "靠背宽度设计"
        ),
        // Q6 尺寸阈值
        EceR129DimensionThreshold(
            thresholdId = "R129-Q6-SH",
            dummyType = EceR129DummyType.Q6,
            dimension = "肩部高度",
            minValue = 210.0,
            maxValue = 230.0,
            unit = "mm",
            适用场景 = "座椅高度设计"
        ),
        EceR129DimensionThreshold(
            thresholdId = "R129-Q6-SW",
            dummyType = EceR129DummyType.Q6,
            dimension = "肩部宽度",
            minValue = 105.0,
            maxValue = 115.0,
            unit = "mm",
            适用场景 = "靠背宽度设计"
        ),
        // Q10 尺寸阈值
        EceR129DimensionThreshold(
            thresholdId = "R129-Q10-SH",
            dummyType = EceR129DummyType.Q10,
            dimension = "肩部高度",
            minValue = 220.0,
            maxValue = 240.0,
            unit = "mm",
            适用场景 = "座椅高度设计"
        ),
        EceR129DimensionThreshold(
            thresholdId = "R129-Q10-SW",
            dummyType = EceR129DummyType.Q10,
            dimension = "肩部宽度",
            minValue = 115.0,
            maxValue = 125.0,
            unit = "mm",
            适用场景 = "靠背宽度设计"
        )
    )
    
    // ========== 假人重量映射 ==========
    
    val DUMMY_WEIGHT_MAPPING = mapOf(
        "0-5kg" to listOf(EceR129DummyType.Q0),
        "5-9kg" to listOf(EceR129DummyType.Q1),
        "9-11kg" to listOf(EceR129DummyType.Q1_5),
        "9-15kg" to listOf(EceR129DummyType.Q1_5, EceR129DummyType.Q3),
        "15-22kg" to listOf(EceR129DummyType.Q3, EceR129DummyType.Q6),
        "22-32kg" to listOf(EceR129DummyType.Q6, EceR129DummyType.Q10),
        "32-36kg" to listOf(EceR129DummyType.Q10)
    )
    
    // ========== 假人身高映射 ==========
    
    val DUMMY_HEIGHT_MAPPING = mapOf(
        "40-60cm" to listOf(EceR129DummyType.Q0),
        "60-75cm" to listOf(EceR129DummyType.Q1),
        "75-87cm" to listOf(EceR129DummyType.Q1_5),
        "87-105cm" to listOf(EceR129DummyType.Q3),
        "105-125cm" to listOf(EceR129DummyType.Q6),
        "125-150cm" to listOf(EceR129DummyType.Q10)
    )
    
    // ========== 查询方法 ==========
    
    /**
     * 获取标准信息
     */
    fun getStandardInfo(): StandardBasicInfo = STANDARD_INFO
    
    /**
     * 获取所有假人规格
     */
    fun getAllDummySpecs(): List<EceR129DummySpec> = DUMMY_SPECS
    
    /**
     * 根据假人类型获取规格
     */
    fun getDummySpecByType(dummyType: EceR129DummyType): EceR129DummySpec? {
        return DUMMY_SPECS.find { it.dummyType == dummyType }
    }
    
    /**
     * 根据重量范围获取假人
     */
    fun getDummiesByWeightRange(weightKg: Double): List<EceR129DummyType>? {
        return DUMMY_WEIGHT_MAPPING.entries.find { (range, _) ->
            val (min, max) = parseWeightRange(range)
            weightKg >= min && weightKg <= max
        }?.value
    }
    
    /**
     * 根据身高范围获取假人
     */
    fun getDummiesByHeightRange(heightCm: Double): List<EceR129DummyType>? {
        return DUMMY_HEIGHT_MAPPING.entries.find { (range, _) ->
            val (min, max) = parseHeightRange(range)
            heightCm >= min && heightCm <= max
        }?.value
    }
    
    /**
     * 获取假人的伤害指标
     */
    fun getInjuryCriteriaByDummy(dummyType: EceR129DummyType): List<EceR129InjuryCriteria> {
        return INJURY_CRITERIA.filter { it.dummyTypes.contains(dummyType) }
    }
    
    /**
     * 获取假人的尺寸阈值
     */
    fun getDimensionThresholdsByDummy(dummyType: EceR129DummyType): List<EceR129DimensionThreshold> {
        return DIMENSION_THRESHOLDS.filter { it.dummyType == dummyType }
    }
    
    /**
     * 获取假人的测试矩阵
     */
    fun getTestMatrixByDummy(dummyType: EceR129DummyType): List<EceR129TestMatrix>? {
        return TEST_MATRICES[dummyType.code.replace(".", "")]
    }
    
    // ========== 辅助方法 ==========
    
    private fun parseWeightRange(range: String): Pair<Double, Double> {
        val match = Regex("([\\d.]+)kg").findAll(range).map { it.groupValues[1].toDouble() }
        return if (match.size >= 2) match[0] to match[1] else 0.0 to 0.0
    }
    
    private fun parseHeightRange(range: String): Pair<Double, Double> {
        val match = Regex("([\\d.]+)cm").findAll(range).map { it.groupValues[1].toDouble() }
        return if (match.size >= 2) match[0] to match[1] else 0.0 to 0.0
    }
}

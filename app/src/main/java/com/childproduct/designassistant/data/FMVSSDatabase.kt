package com.childproduct.designassistant.data

/**
 * FMVSS 213/213a 标准数据库
 * 
 * 美国联邦机动车安全标准数据库
 * 
 * 包含内容：
 * - FMVSS 213/213a 标准的详细信息
 * - 假人类型和匹配规则
 * - 测试配置和阈值
 * - 安全要求和通过标准
 * 
 * @see FMVSS 213 Child Restraint Systems
 * @see FMVSS 213a Side Impact Protection
 */

/**
 * FMVSS 标准类型
 */
enum class FMVSSStandardType(val code: String, val displayName: String, val description: String) {
    FMVSS_213("FMVSS 213", "Child Restraint Systems", "儿童约束系统正面碰撞测试标准"),
    FMVSS_213A("FMVSS 213a", "Side Impact Protection", "儿童约束系统侧面碰撞测试标准")
}

/**
 * FMVSS 假人类型
 */
enum class FMVSSDummyType(val code: String, val displayName: String, val weightLbs: Double, val weightKg: Double) {
    CRABI_6("CRABI 6", "6个月假人", 22.0, 9.98),
    CRABI_12("CRABI 12", "12个月假人", 22.0, 9.98),
    CRABI_18("CRABI 18", "18个月假人", 33.0, 14.97),
    HYBRID_III_3Y("Hybrid III 3Y", "3岁假人", 51.0, 23.13),
    HYBRID_III_6Y("Hybrid III 6Y", "6岁假人", 68.0, 30.84),
    Q3S("Q3s", "3岁侧撞假人", 51.0, 23.13)
}

/**
 * 碰撞类型
 */
enum class FMVSSImpactType(val code: String, val displayName: String) {
    FRONTAL("FRONTAL", "正面碰撞"),
    SIDE("SIDE", "侧面碰撞"),
    REAR("REAR", "后部碰撞")
}

/**
 * 加速度曲线类型
 */
enum class FMVSSAccelerationProfile(val type: String, val description: String) {
    FRONTAL("Frontal", "正面碰撞加速度曲线"),
    SIDE("Side", "侧面碰撞加速度曲线"),
    REAR("Rear", "后部碰撞加速度曲线")
}

/**
 * FMVSS 测试配置
 */
data class FMVSSTestConfiguration(
    val configId: String,
    val testName: String,
    val standard: FMVSSStandardType,
    val impactType: FMVSSImpactType,
    val velocityKmh: Double,
    val velocityRange: String,
    val accelerationProfile: FMVSSAccelerationProfile,
    val dummyTypes: List<FMVSSDummyType>,
    val injuryCriteria: Map<String, Double>,
    val excursionLimits: Map<String, Double>,
    val specialRequirements: List<String> = emptyList()
)

/**
 * FMVSS 安全阈值
 */
data class FMVSSThreshold(
    val thresholdId: String,
    val standard: FMVSSStandardType,
    val dummyType: FMVSSDummyType,
    val criterion: String,
    val limitValue: Double,
    val unit: String,
    val testCondition: String
)

/**
 * FMVSS 标准分组
 */
data class FMVSSGroup(
    val groupCode: String,
    val displayName: String,
    val weightRange: String,
    val ageRange: String,
    val installationType: String,
    val applicableDummies: List<FMVSSDummyType>
)

/**
 * FMVSSDatabase 标准数据库对象
 */
object FMVSSDatabase {
    
    // ========== 标准基本信息 ==========
    
    val FMVSS_213_INFO = StandardBasicInfo(
        standardId = "FMVSS-213",
        standardName = "Child Restraint Systems",
        standardType = StandardCategory.NATIONAL,
        applicableRegion = "USA",
        applicableWeight = "0-36kg (0-80lb)",
        applicableAge = "新生儿至12岁",
        coreScope = "规定儿童约束系统的性能要求，包括正面碰撞测试、材料阻燃性、标识等",
        effectiveDate = "2012年12月",
        standardStatus = "Current",
        dataSource = "U.S. Department of Transportation, NHTSA"
    )
    
    val FMVSS_213A_INFO = StandardBasicInfo(
        standardId = "FMVSS-213a",
        standardName = "Side Impact Protection",
        standardType = StandardCategory.NATIONAL,
        applicableRegion = "USA",
        applicableWeight = "0-40lb (0-18.1kg)",
        applicableAge = "新生儿至约4岁",
        coreScope = "2025年重要更新：新增侧碰测试要求，适用于体重≤40lb的儿童安全座椅。测试速度设定为30mph（48km/h），接近美国常见城市限速，旨在加强儿童乘车安全防护。原定2025年6月30日生效，现延长至2026年12月5日合规",
        effectiveDate = "2014年2月 (2025年更新)",
        standardStatus = "Current (2025年重要更新，2026年12月5日全面合规)",
        dataSource = "U.S. Department of Transportation, NHTSA"
    )
    
    // ========== 标准分组 ==========
    
    val GROUPS = listOf(
        FMVSSGroup(
            groupCode = "Group 0",
            displayName = "新生儿组",
            weightRange = "0-5kg (0-11lb)",
            ageRange = "新生儿至6个月",
            installationType = "后向安装",
            applicableDummies = listOf(FMVSSDummyType.CRABI_6)
        ),
        FMVSSGroup(
            groupCode = "Group 0+",
            displayName = "婴儿组",
            weightRange = "0-13kg (0-29lb)",
            ageRange = "新生儿至18个月",
            installationType = "后向安装",
            applicableDummies = listOf(FMVSSDummyType.CRABI_6, FMVSSDummyType.CRABI_12, FMVSSDummyType.CRABI_18)
        ),
        FMVSSGroup(
            groupCode = "Group 1",
            displayName = "幼儿组",
            weightRange = "9-18kg (20-40lb)",
            ageRange = "9个月至4岁",
            installationType = "前向安装",
            applicableDummies = listOf(FMVSSDummyType.CRABI_18, FMVSSDummyType.HYBRID_III_3Y)
        ),
        FMVSSGroup(
            groupCode = "Group 2",
            displayName = "儿童组",
            weightRange = "15-25kg (33-55lb)",
            ageRange = "4岁至6岁",
            installationType = "前向安装",
            applicableDummies = listOf(FMVSSDummyType.HYBRID_III_3Y, FMVSSDummyType.HYBRID_III_6Y)
        ),
        FMVSSGroup(
            groupCode = "Group 3",
            displayName = "大童组",
            weightRange = "22-36kg (48-80lb)",
            ageRange = "6岁至12岁",
            installationType = "前向安装",
            applicableDummies = listOf(FMVSSDummyType.HYBRID_III_6Y)
        )
    )
    
    // ========== 测试配置 ==========
    
    val TEST_CONFIGURATIONS = mapOf(
        "FMVSS-213-FRONTAL" to FMVSSTestConfiguration(
            configId = "FMVSS-213-FRONTAL",
            testName = "FMVSS 213 正面碰撞测试",
            standard = FMVSSStandardType.FMVSS_213,
            impactType = FMVSSImpactType.FRONTAL,
            velocityKmh = 48.0,
            velocityRange = "48±3.2 km/h (30±2 mph)",
            accelerationProfile = FMVSSAccelerationProfile.FRONTAL,
            dummyTypes = listOf(
                FMVSSDummyType.CRABI_6,
                FMVSSDummyType.CRABI_12,
                FMVSSDummyType.CRABI_18,
                FMVSSDummyType.HYBRID_III_3Y,
                FMVSSDummyType.HYBRID_III_6Y
            ),
            injuryCriteria = mapOf(
                "HIC36" to 1000.0,
                "胸部加速度3ms" to 60.0,
                "头部加速度3ms" to 80.0
            ),
            excursionLimits = mapOf(
                "头部位移(有tether)" to 720.0,
                "头部位移(无tether)" to 813.0,
                "膝盖位移" to 915.0
            )
        ),
        "FMVSS-213A-SIDE" to FMVSSTestConfiguration(
            configId = "FMVSS-213A-SIDE",
            testName = "FMVSS 213a 侧面碰撞测试 (2025更新)",
            standard = FMVSSStandardType.FMVSS_213A,
            impactType = FMVSSImpactType.SIDE,
            velocityKmh = 48.0,
            velocityRange = "48±0 km/h (30±0 mph) - 2025年新标准",
            accelerationProfile = FMVSSAccelerationProfile.SIDE,
            dummyTypes = listOf(
                FMVSSDummyType.CRABI_12,
                FMVSSDummyType.Q3S
            ),
            injuryCriteria = mapOf(
                "HIC15" to 570.0,
                "胸部压缩量" to 23.0,
                "头部加速度3ms" to 75.0
            ),
            excursionLimits = mapOf(
                "与车门距离" to 38.0
            ),
            specialRequirements = listOf(
                "2025年更新：测试速度设定为30mph（48km/h）",
                "12个月假人头部无直接接触SISA或车门结构",
                "躯干完全被CRS包裹",
                "适用于体重≤40lb（约18.1kg）的儿童安全座椅",
                "合规截止日期：2026年12月5日"
            )
        )
    )
    
    // ========== 安全阈值 ==========
    
    val THRESHOLDS = listOf(
        // CRABI 6 假人阈值
        FMVSSThreshold(
            thresholdId = "FMVSS-CRABI6-HIC36",
            standard = FMVSSStandardType.FMVSS_213,
            dummyType = FMVSSDummyType.CRABI_6,
            criterion = "HIC36",
            limitValue = 1000.0,
            unit = "无",
            testCondition = "正面碰撞测试"
        ),
        FMVSSThreshold(
            thresholdId = "FMVSS-CRABI6-CHEST",
            standard = FMVSSStandardType.FMVSS_213,
            dummyType = FMVSSDummyType.CRABI_6,
            criterion = "胸部加速度3ms",
            limitValue = 60.0,
            unit = "g",
            testCondition = "正面碰撞测试"
        ),
        
        // CRABI 12 假人阈值
        FMVSSThreshold(
            thresholdId = "FMVSS-CRABI12-HIC15",
            standard = FMVSSStandardType.FMVSS_213A,
            dummyType = FMVSSDummyType.CRABI_12,
            criterion = "HIC15",
            limitValue = 570.0,
            unit = "无",
            testCondition = "侧面碰撞测试"
        ),
        FMVSSThreshold(
            thresholdId = "FMVSS-CRABI12-CHEST",
            standard = FMVSSStandardType.FMVSS_213A,
            dummyType = FMVSSDummyType.CRABI_12,
            criterion = "胸部压缩量",
            limitValue = 23.0,
            unit = "mm",
            testCondition = "侧面碰撞测试"
        ),
        
        // Q3s 假人阈值
        FMVSSThreshold(
            thresholdId = "FMVSS-Q3S-HIC15",
            standard = FMVSSStandardType.FMVSS_213A,
            dummyType = FMVSSDummyType.Q3S,
            criterion = "HIC15",
            limitValue = 570.0,
            unit = "无",
            testCondition = "侧面碰撞测试"
        ),
        
        // Hybrid III 3Y 假人阈值
        FMVSSThreshold(
            thresholdId = "FMVSS-3Y-HIC36",
            standard = FMVSSStandardType.FMVSS_213,
            dummyType = FMVSSDummyType.HYBRID_III_3Y,
            criterion = "HIC36",
            limitValue = 1000.0,
            unit = "无",
            testCondition = "正面碰撞测试"
        ),
        FMVSSThreshold(
            thresholdId = "FMVSS-3Y-CHEST",
            standard = FMVSSStandardType.FMVSS_213,
            dummyType = FMVSSDummyType.HYBRID_III_3Y,
            criterion = "胸部加速度3ms",
            limitValue = 60.0,
            unit = "g",
            testCondition = "正面碰撞测试"
        ),
        
        // Hybrid III 6Y 假人阈值
        FMVSSThreshold(
            thresholdId = "FMVSS-6Y-HIC36",
            standard = FMVSSStandardType.FMVSS_213,
            dummyType = FMVSSDummyType.HYBRID_III_6Y,
            criterion = "HIC36",
            limitValue = 1000.0,
            unit = "无",
            testCondition = "正面碰撞测试"
        ),
        FMVSSThreshold(
            thresholdId = "FMVSS-6Y-CHEST",
            standard = FMVSSStandardType.FMVSS_213,
            dummyType = FMVSSDummyType.HYBRID_III_6Y,
            criterion = "胸部加速度3ms",
            limitValue = 60.0,
            unit = "g",
            testCondition = "正面碰撞测试"
        )
    )
    
    // ========== 假人重量映射 ==========
    
    val DUMMY_WEIGHT_MAPPING = mapOf(
        "0-2.3kg (0-5lb)" to listOf(FMVSSDummyType.CRABI_6),
        "2.3-5kg (5-11lb)" to listOf(FMVSSDummyType.CRABI_6),
        "5-10kg (11-22lb)" to listOf(FMVSSDummyType.CRABI_12),
        "9-18kg (20-40lb)" to listOf(FMVSSDummyType.CRABI_18, FMVSSDummyType.HYBRID_III_3Y),
        "13.6-18kg (30-40lb)" to listOf(FMVSSDummyType.HYBRID_III_3Y),
        "18-36kg (40-80lb)" to listOf(FMVSSDummyType.HYBRID_III_6Y)
    )
    
    // ========== 假人年龄映射 ==========
    
    val DUMMY_AGE_MAPPING = mapOf(
        "新生儿 (0-6个月)" to listOf(FMVSSDummyType.CRABI_6),
        "6-12个月" to listOf(FMVSSDummyType.CRABI_12),
        "12-18个月" to listOf(FMVSSDummyType.CRABI_18),
        "18-36个月 (1.5-3岁)" to listOf(FMVSSDummyType.HYBRID_III_3Y),
        "36-72个月 (3-6岁)" to listOf(FMVSSDummyType.HYBRID_III_3Y, FMVSSDummyType.HYBRID_III_6Y),
        "72-120个月 (6-10岁)" to listOf(FMVSSDummyType.HYBRID_III_6Y)
    )
    
    // ========== 材料要求 ==========
    
    val MATERIAL_REQUIREMENTS = listOf(
        MaterialRequirement(
            materialId = 1,
            standardId = "FMVSS-213",
            materialType = "阻燃材料",
            performanceIndex = "燃烧速度",
            requirementValue = "≤102mm/min (水平燃烧测试)",
            testStandard = "FMVSS 302"
        ),
        MaterialRequirement(
            materialId = 2,
            standardId = "FMVSS-213",
            materialType = "金属部件",
            performanceIndex = "锐利边缘",
            requirementValue = "无锐利边缘和尖端",
            testStandard = "FMVSS 213 §S5.3"
        ),
        MaterialRequirement(
            materialId = 3,
            standardId = "FMVSS-213",
            materialType = "塑料部件",
            performanceIndex = "耐冲击性",
            requirementValue = "在-30°C温度下无脆性破裂",
            testStandard = "FMVSS 213 §S5.5"
        )
    )
    
    // ========== 查询方法 ==========
    
    /**
     * 获取所有标准信息
     */
    fun getAllStandards(): List<StandardBasicInfo> {
        return listOf(FMVSS_213_INFO, FMVSS_213A_INFO)
    }
    
    /**
     * 根据标准ID获取标准信息
     */
    fun getStandardById(standardId: String): StandardBasicInfo? {
        return when (standardId) {
            "FMVSS-213" -> FMVSS_213_INFO
            "FMVSS-213A" -> FMVSS_213A_INFO
            else -> null
        }
    }
    
    /**
     * 获取所有分组
     */
    fun getAllGroups(): List<FMVSSGroup> {
        return GROUPS
    }
    
    /**
     * 根据分组代码获取分组
     */
    fun getGroupByCode(groupCode: String): FMVSSGroup? {
        return GROUPS.find { it.groupCode == groupCode }
    }
    
    /**
     * 根据重量范围获取适用的假人
     */
    fun getDummiesByWeightRange(weightKg: Double): List<FMVSSDummyType>? {
        return DUMMY_WEIGHT_MAPPING.entries.find { (range, _) ->
            val (min, max) = parseWeightRange(range)
            weightKg >= min && weightKg <= max
        }?.value
    }
    
    /**
     * 根据年龄范围获取适用的假人
     */
    fun getDummiesByAgeRange(ageYears: Double): List<FMVSSDummyType>? {
        return DUMMY_AGE_MAPPING.entries.find { (range, _) ->
            val (min, max) = parseAgeRange(range)
            ageYears >= min && ageYears <= max
        }?.value
    }
    
    /**
     * 获取假人的安全阈值
     */
    fun getThresholdsByDummy(dummyType: FMVSSDummyType): List<FMVSSThreshold> {
        return THRESHOLDS.filter { it.dummyType == dummyType }
    }
    
    // ========== 辅助方法 ==========
    
    private fun parseWeightRange(range: String): Pair<Double, Double> {
        val match = Regex("([\\d.]+)kg").findAll(range).map { it.groupValues[1].toDouble() }
        return if (match.size >= 2) match[0] to match[1] else 0.0 to 0.0
    }
    
    private fun parseAgeRange(range: String): Pair<Double, Double> {
        val match = Regex("([\\d.]+)岁").findAll(range).map { it.groupValues[1].toDouble() }
        return if (match.size >= 2) match[0] to match[1] else 0.0 to 0.0
    }
}

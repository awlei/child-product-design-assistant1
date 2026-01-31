package com.childproduct.designassistant.data

/**
 * FMVSS 213/213a 测试标准数据库
 * 
 * 包含所有FMVSS 213/213a标准相关的测试配置、Dummy匹配规则、测试条件等信息
 * 用于指导CRS产品设计和测试验证
 */
object FMVSSTestStandardDatabase {

    /**
     * FMVSS 213/213a测试配置映射
     */
    val TEST_CONFIGURATIONS = mapOf(
        "FMVSS-213-FRONTAL" to TestConfiguration(
            testName = "FMVSS 213 正面碰撞测试",
            standard = FMVSSStandardType.FMVSS_213,
            impactType = ImpactTypeFMVSS.FRONTAL,
            velocityKmh = 48.0,
            velocityRange = "48±3.2 km/h",
            accelerationProfile = AccelerationProfile.FRONTAL,
            dummyTypes = listOf(
                DummyType.CRABI_6,
                DummyType.CRABI_12,
                DummyType.CRABI_18,
                DummyType.HYBRID_III_3Y,
                DummyType.HYBRID_III_6Y
            ),
            injuryCriteria = mapOf(
                "HIC36" to 1000.0,
                "胸部加速度3ms" to 60.0
            ),
            excursionLimits = mapOf(
                "头部位移(有tether)" to 720.0,
                "头部位移(无tether)" to 813.0,
                "膝盖位移" to 915.0
            )
        ),
        "FMVSS-213A-SIDE" to TestConfiguration(
            testName = "FMVSS 213a 侧面碰撞测试",
            standard = FMVSSStandardType.FMVSS_213A,
            impactType = ImpactTypeFMVSS.SIDE,
            velocityKmh = 31.3,
            velocityRange = "31.3±0.64 km/h",
            accelerationProfile = AccelerationProfile.SIDE,
            dummyTypes = listOf(
                DummyType.CRABI_12,
                DummyType.Q3S
            ),
            injuryCriteria = mapOf(
                "HIC15" to 570.0,
                "胸部压缩量" to 23.0
            ),
            excursionLimits = mapOf(
                "与车门距离" to 38.0
            ),
            specialRequirements = listOf(
                "12个月dummy头部无直接接触SISA或车门结构",
                "躯干完全被CRS包裹"
            )
        )
    )

    /**
     * Dummy体重范围匹配表
     * 根据FMVSS 213/213a标准，不同体重范围使用对应的Dummy
     */
    val DUMMY_WEIGHT_MAPPING = mapOf(
        "0-2.3kg (0-5lb)" to listOf(DummyType.Q0),
        "2.3-5kg (5-11lb)" to listOf(DummyType.CRABI_6, DummyType.Q0),
        "5-10kg (11-22lb)" to listOf(DummyType.CRABI_12, DummyType.Q1),
        "9-18kg (20-40lb)" to listOf(DummyType.CRABI_18, DummyType.Q1_5, DummyType.Q3S),
        "13.6-18kg (30-40lb)" to listOf(DummyType.Q3S, DummyType.HYBRID_III_3Y),
        "18-36kg (40-80lb)" to listOf(DummyType.Q6, DummyType.HYBRID_III_6Y)
    )

    /**
     * Dummy年龄匹配表
     */
    val DUMMY_AGE_MAPPING = mapOf(
        "新生儿 (0-6个月)" to listOf(DummyType.Q0, DummyType.CRABI_6),
        "6-12个月" to listOf(DummyType.Q1, DummyType.CRABI_6),
        "12-18个月" to listOf(DummyType.Q1_5, DummyType.CRABI_12, DummyType.CRABI_18),
        "18-36个月 (1.5-3岁)" to listOf(DummyType.Q3, DummyType.Q3S),
        "36-72个月 (3-6岁)" to listOf(DummyType.Q6, DummyType.HYBRID_III_3Y),
        "72-120个月 (6-10岁)" to listOf(DummyType.Q10, DummyType.HYBRID_III_6Y)
    )

    /**
     * 测试环境要求
     */
    val TEST_ENVIRONMENT_REQUIREMENTS = mapOf(
        "动态碰撞测试" to EnvironmentRequirement(
            temperatureMin = 20.6,
            temperatureMax = 22.2,
            humidityMin = 10.0,
            humidityMax = 70.0,
            description = "动态碰撞测试环境条件"
        ),
        "组件测试" to EnvironmentRequirement(
            temperatureMin = 21.0,
            temperatureMax = 25.0,
            humidityMin = 48.0,
            humidityMax = 67.0,
            description = "组件测试环境条件"
        ),
        "材料预处理（阻燃性）" to EnvironmentRequirement(
            temperatureMin = 16.0,
            temperatureMax = 27.0,
            humidityMin = 40.0,
            humidityMax = 60.0,
            description = "材料预处理环境条件（FMVSS 302）",
            durationHours = 24
        ),
        "盐雾测试" to EnvironmentRequirement(
            temperatureMin = 35.0,
            temperatureMax = 35.0,
            humidityMin = 0.0,
            humidityMax = 0.0,
            description = "盐雾测试环境条件（ASTM B117）",
            durationHours = 24
        ),
        "热老化测试" to EnvironmentRequirement(
            temperatureMin = 80.0,
            temperatureMax = 80.0,
            humidityMin = 0.0,
            humidityMax = 0.0,
            description = "热老化测试环境条件（ASTM D756）",
            durationHours = 24
        )
    )

    /**
     * 测试设备要求
     */
    val TEST_EQUIPMENT_REQUIREMENTS = mapOf(
        "FISA座椅" to EquipmentRequirement(
            name = "FISA座椅总成",
            specification = "用于正面碰撞测试的车辆座椅模拟器"
        ),
        "SISA座椅" to EquipmentRequirement(
            name = "SISA座椅总成",
            specification = "用于侧面碰撞测试的车辆座椅模拟器，配备50%压缩载荷泡沫"
        ),
        "高速摄像" to EquipmentRequirement(
            name = "高速摄像机",
            specification = "帧率≥1000fps，用于捕捉碰撞过程"
        ),
        "加速度传感器" to EquipmentRequirement(
            name = "加速度传感器",
            specification = "支持CFC 60/180/1000滤波通道"
        ),
        "力传感器" to EquipmentRequirement(
            name = "力传感器",
            specification = "用于测量颈部力和胸部压缩力"
        ),
        "位移传感器" to EquipmentRequirement(
            name = "位移传感器",
            specification = "用于测量头部位移和膝部位移"
        ),
        "摩擦测试仪" to EquipmentRequirement(
            name = "Taber摩擦测试仪",
            specification = "用于测试织带耐磨性，5000次摩擦循环"
        ),
        "碳弧灯老化箱" to EquipmentRequirement(
            name = "碳弧灯老化箱",
            specification = "用于测试织带耐光性，100小时照射"
        )
    )

    /**
     * 测试用例模板
     */
    val TEST_CASE_TEMPLATES = listOf(
        TestCaseTemplate(
            testCaseId = "TC-FMVSS-213-001",
            testName = "织带断裂强度测试",
            standard = "FMVSS 213 S5.4.1",
            testType = "组件测试",
            testMethod = "拉伸测试",
            acceptanceCriteria = mapOf(
                "固定CRS至车辆的织带" to "断裂强度≥15000N",
                "约束儿童的织带" to "断裂强度≥11000N"
            ),
            preconditions = listOf(
                "织带在20-27℃、40-60%湿度条件下放置24小时"
            )
        ),
        TestCaseTemplate(
            testCaseId = "TC-FMVSS-213-002",
            testName = "织带耐磨性测试",
            standard = "FMVSS 213 S5.4.1",
            testType = "组件测试",
            testMethod = "Taber摩擦测试",
            acceptanceCriteria = mapOf(
                "耐磨后强度保留率" to "≥75%"
            ),
            preconditions = listOf(
                "进行5000次摩擦循环"
            )
        ),
        TestCaseTemplate(
            testCaseId = "TC-FMVSS-213-003",
            testName = "织带耐光性测试",
            standard = "FMVSS 213 S5.4.1",
            testType = "组件测试",
            testMethod = "碳弧灯照射",
            acceptanceCriteria = mapOf(
                "耐光后强度保留率" to "≥60%"
            ),
            preconditions = listOf(
                "进行100小时碳弧灯照射"
            )
        ),
        TestCaseTemplate(
            testCaseId = "TC-FMVSS-213-004",
            testName = "材料阻燃性测试",
            standard = "FMVSS 302 S4.3",
            testType = "组件测试",
            testMethod = "水平燃烧测试",
            acceptanceCriteria = mapOf(
                "燃烧速率" to "≤102mm/分钟",
                "或" to "60秒内熄灭且燃烧距离≤51mm"
            ),
            preconditions = listOf(
                "材料在16-27℃、40-60%湿度条件下放置24小时"
            )
        ),
        TestCaseTemplate(
            testCaseId = "TC-FMVSS-213-005",
            testName = "硬件耐腐蚀测试",
            standard = "FMVSS 213 S5.4.2",
            testType = "组件测试",
            testMethod = "盐雾测试（ASTM B117）",
            acceptanceCriteria = mapOf(
                "腐蚀状态" to "24小时盐雾+1小时干燥后无可见腐蚀"
            ),
            preconditions = listOf(
                "35℃盐雾环境，24小时"
            )
        ),
        TestCaseTemplate(
            testCaseId = "TC-FMVSS-213-006",
            testName = "硬件热老化测试",
            standard = "FMVSS 213 S5.4.2",
            testType = "组件测试",
            testMethod = "热老化测试（ASTM D756）",
            acceptanceCriteria = mapOf(
                "变形" to "80℃干燥箱24小时后无变形",
                "功能" to "功能正常"
            ),
            preconditions = listOf(
                "80℃干燥箱环境，24小时"
            )
        ),
        TestCaseTemplate(
            testCaseId = "TC-FMVSS-213-007",
            testName = "Buckle性能测试",
            standard = "FMVSS 213 S5.4.3",
            testType = "组件测试",
            testMethod = "拉力测试",
            acceptanceCriteria = mapOf(
                "释放力（预碰撞）" to "40-62N",
                "释放力（碰撞后）" to "≤71N",
                "调节力" to "≤49N",
                "倾斜锁定角度" to "≥30°",
                "释放面面积" to "≥3.9cm² (0.6平方英寸)"
            ),
            preconditions = listOf(
                "Buckle在20-27℃、40-60%湿度条件下放置24小时"
            )
        ),
        TestCaseTemplate(
            testCaseId = "TC-FMVSS-213-008",
            testName = "正面碰撞测试 - Configuration I",
            standard = "FMVSS 213 S6",
            testType = "动态测试",
            testMethod = " sled测试",
            acceptanceCriteria = mapOf(
                "HIC36" to "≤1000",
                "胸部加速度3ms" to "≤60g",
                "头部位移（有tether）" to "≤720mm",
                "头部位移（无tether）" to "≤813mm",
                "膝盖位移" to "≤915mm"
            ),
            preconditions = listOf(
                "Dummy在20.6-22.2℃、10-70%湿度条件下放置4小时",
                "碰撞速度48±3.2 km/h",
                "加速度走廊符合标准"
            )
        ),
        TestCaseTemplate(
            testCaseId = "TC-FMVSS-213A-001",
            testName = "侧面碰撞测试",
            standard = "FMVSS 213a S6",
            testType = "动态测试",
            testMethod = "移动屏障测试",
            acceptanceCriteria = mapOf(
                "HIC15" to "≤570",
                "胸部压缩量" to "≤23mm (Q3s dummy)",
                "头部接触" to "12个月dummy头部无直接接触SISA或车门结构",
                "躯干包裹" to "躯干完全被CRS包裹"
            ),
            preconditions = listOf(
                "Dummy在20.6-22.2℃、10-70%湿度条件下放置4小时",
                "相对速度31.3±0.64 km/h",
                "与车门间距38±6mm"
            )
        ),
        TestCaseTemplate(
            testCaseId = "TC-FMVSS-213-009",
            testName = "飞机用CRS旋转测试",
            standard = "FMVSS 213 S8",
            testType = "特殊场景测试",
            testMethod = "旋转测试",
            acceptanceCriteria = mapOf(
                "脱离" to "180°倒置旋转（35-45°/秒）时CRS与dummy不脱离"
            ),
            preconditions = listOf(
                "使用FAA认证安全带安装",
                "旋转速度35-45°/秒"
            )
        )
    )

    /**
     * 获取指定标准类型的测试配置
     */
    fun getTestConfiguration(standardType: FMVSSStandardType): TestConfiguration? {
        return when (standardType) {
            FMVSSStandardType.FMVSS_213 -> TEST_CONFIGURATIONS["FMVSS-213-FRONTAL"]
            FMVSSStandardType.FMVSS_213A -> TEST_CONFIGURATIONS["FMVSS-213A-SIDE"]
            else -> null
        }
    }

    /**
     * 根据体重范围获取推荐的Dummy类型
     */
    fun getRecommendedDummyByWeight(weightKg: Double): List<DummyType> {
        return DUMMY_WEIGHT_MAPPING.entries
            .filter { (range, _) ->
                val parts = range.split("-")
                val min = parts[0].replace("kg", "").trim().toDoubleOrNull() ?: 0.0
                val max = parts[1].replace("kg", "").trim().toDoubleOrNull() ?: 999.0
                weightKg >= min && weightKg <= max
            }
            .flatMap { it.value }
            .distinct()
    }

    /**
     * 根据年龄范围获取推荐的Dummy类型
     */
    fun getRecommendedDummyByAge(ageMonths: Int): List<DummyType> {
        return DUMMY_AGE_MAPPING.entries
            .filter { (range, _) ->
                val parts = range.split("-")
                val min = parts[0].replace("个月", "").replace("(", "").trim().toIntOrNull() ?: 0
                val max = parts[1].replace("个月", "").replace(")", "").trim().toIntOrNull() ?: 999
                ageMonths >= min && ageMonths <= max
            }
            .flatMap { it.value }
            .distinct()
    }

    /**
     * 获取测试环境要求
     */
    fun getTestEnvironmentRequirement(testType: String): EnvironmentRequirement? {
        return TEST_ENVIRONMENT_REQUIREMENTS[testType]
    }

    /**
     * 获取所有测试用例模板
     */
    fun getAllTestCaseTemplates(): List<TestCaseTemplate> {
        return TEST_CASE_TEMPLATES
    }

    /**
     * 根据标准获取测试用例模板
     */
    fun getTestCaseTemplatesByStandard(standard: String): List<TestCaseTemplate> {
        return TEST_CASE_TEMPLATES.filter { it.standard.contains(standard) }
    }
}

/**
 * 测试配置
 */
data class TestConfiguration(
    val testName: String,
    val standard: FMVSSStandardType,
    val impactType: ImpactTypeFMVSS,
    val velocityKmh: Double,
    val velocityRange: String,
    val accelerationProfile: AccelerationProfile,
    val dummyTypes: List<DummyType>,
    val injuryCriteria: Map<String, Double>,
    val excursionLimits: Map<String, Double>,
    val specialRequirements: List<String> = emptyList()
)

/**
 * 环境要求
 */
data class EnvironmentRequirement(
    val temperatureMin: Double,           // 最低温度（℃）
    val temperatureMax: Double,           // 最高温度（℃）
    val humidityMin: Double,              // 最低湿度（%）
    val humidityMax: Double,              // 最高湿度（%）
    val description: String,              // 描述
    val durationHours: Int? = null        // 持续时长（小时）
)

/**
 * 设备要求
 */
data class EquipmentRequirement(
    val name: String,                     // 设备名称
    val specification: String             // 规格说明
)

/**
 * 测试用例模板
 */
data class TestCaseTemplate(
    val testCaseId: String,               // 测试用例ID
    val testName: String,                 // 测试名称
    val standard: String,                  // 标准
    val testType: String,                  // 测试类型
    val testMethod: String,                // 测试方法
    val acceptanceCriteria: Map<String, String>, // 合格标准
    val preconditions: List<String>       // 前置条件
)

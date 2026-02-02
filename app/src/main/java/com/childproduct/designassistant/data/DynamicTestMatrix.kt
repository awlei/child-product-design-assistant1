package com.childproduct.designassistant.data

import com.childproduct.designassistant.model.*

/**
 * 动态测试矩阵数据模型
 * 参考：ROADMATE 360 dynamic test matrix
 */

/**
 * 测试输入参数
 */
data class TestInputParameter(
    val sample: String,                    // 样本标识（如R129）
    val pulse: PulseType,                  // 脉冲类型
    val impact: ImpactType,                // 撞击类型
    val dummy: DummyType,                  // 假人类型
    val position: SeatPosition,            // 座椅方向
    val installation: InstallationType,    // 安装方式
    val specificInstallation: String?,     // 特殊安装要求
    val productConfiguration: SeatConfiguration,  // 产品配置
    val isofixAnchors: Boolean,            // ISOFIX锚点
    val floorPosition: FloorPosition,      // 地板位置
    val harness: Boolean,                  // 安全带
    val topTetherOrSupportLeg: Boolean,    // 顶部系带/支撑腿
    val dashboard: Boolean,                // 仪表盘
    val comments: String?                  // 备注
)

/**
 * 测试输出参数
 */
data class TestOutputParameter(
    val buckle: Boolean?,                  // Buckle状态
    val adjuster: Boolean?,                // Adjuster状态
    val isofix: Boolean?,                  // ISOFIX状态
    val tt: Boolean?,                      // Top Tether状态
    val quantity: Int?,                    // 数量
    val testNumber: String?,               // 测试编号
    val speedKmh: Double?,                 // 速度 km/h
    val maxPulseG: Double?,                // 最大脉冲 g
    val stoppingDistanceMm: Double?,       // 停止距离 mm
    val headExcursionMm: Double?,          // 头部偏移 mm
    val verticalLimitMm: Double?,          // 垂直限制 mm
    val timeForHeadExcursionMs: Double?,   // 头部偏移时间 ms
    val minVerticalG: Double?,             // 最小垂直 g
    val tMinus30gMs: Double?,              // T(-30g) ms
    val y3msG: Double?,                    // Y(3ms) g
    val maxResultantG: Double?,            // 最大合加速度 g
    val t55gMs: Double?,                   // T(55g) ms
    val y3msG_2: Double?,                  // Y(3ms) g (第二次)
    val accHeadRes3ms: Double?,            // 头部合加速度 3ms
    val hic36ForADAC: Double?,             // HIC36 for ADAC
    val hic36ForR44: Double?,              // HIC36 for R44
    val hpc15ForIsize: Double?,            // HPC15 for i-Size
    val frForADAC: Double?,                // Fr for ADAC
    val fzForIsize: Double?,               // Fz for i-Size
    val fzForPLUS: Double?,                // Fz for PLUS N
    val upperNeckForce: Double?,           // 上颈部力 N
    val upperNeckMomentMx: Double?,        // 上颈部力矩 Mx (i-Size Lateral)
    val upperNeckMomentMy: Double?,        // 上颈部力矩 My (i-Size Front & Rear)
    val upperNeckMomentMr: Double?,        // 上颈部力矩 Mr (ADAC)
    val chestDeflectionMm: Double?,        // 胸部变形 mm
    val observationAfterCrash: String?,    // 撞击后观察
    val status: TestStatus                 // 测试状态（PASS/FAIL）
)

/**
 * 完整的测试用例
 */
data class DynamicTestCase(
    val id: String,
    val input: TestInputParameter,
    val output: TestOutputParameter?
)

/**
 * 动态测试矩阵
 */
data class DynamicTestMatrix(
    val productName: String,               // 产品名称
    val productId: String,                 // 产品ID
    val version: String,                   // 版本号
    val testCases: List<DynamicTestCase>   // 测试用例列表
)

// ===== 枚举定义 =====

/**
 * 脉冲类型
 */
enum class PulseType {
    FRONTAL,       // 正面
    LATERAL,       // 侧面
    REAR,          // 后面
    ROLL_OVER      // 翻滚
}

/**
 * 撞击类型
 */
enum class ImpactType {
    FRONTAL_RIGID,        // 正面撞击刚性障碍物
    FRONTAL_DEFORMABLE,   // 正面撞击可变形障碍物
    LATERAL_POLE,         // 侧面撞击杆
    LATERAL_DEFORMABLE,   // 侧面撞击可变形障碍物
    REAR,                 // 后向
    ROLL_OVER             // 翻滚
}

/**
 * 座椅方向
 */
enum class SeatPosition {
    REARWARD_FACING,    // 后向
    FORWARD_FACING,     // 前向
    ROTARY              // 旋转
}

/**
 * 安装方式
 */
enum class InstallationType {
    ISOFIX_3_PTS,       // ISOFIX 3点
    ISOFIX_2_PTS,       // ISOFIX 2点
    VEHICLE_BELT,       // 车辆安全带
    ISOFIX_TOP_TETHER,  // ISOFIX + 顶部系带
    ISOFIX_SUPPORT_LEG  // ISOFIX + 支撑腿
}

/**
 * 座椅配置
 */
enum class SeatConfiguration {
    UPRIGHT,            // 直立
    RECLINED,           // 倾斜
    ADJUSTABLE          // 可调节
}

/**
 * 地板位置
 */
enum class FloorPosition {
    LOW,                // 低
    HIGH,               // 高
    ADJUSTABLE          // 可调节
}

/**
 * 测试状态
 */
enum class TestStatus {
    PASS,
    FAIL,
    NOT_STARTED,
    IN_PROGRESS
}

/**
 * 测试矩阵生成服务
 */
class DynamicTestMatrixGenerator {

    companion object {
        /**
         * 生成ECE R129测试矩阵数据（参考ROADMATE 360）
         */
        private fun generateECE_R129TestMatrix(): DynamicTestMatrix {
            // 这里可以预定义完整的测试矩阵
            return DynamicTestMatrix(
                productName = "ECE R129 Standard Test Matrix",
                productId = "ECE_R129",
                version = "1.0",
                testCases = emptyList()
            )
        }

        /**
         * 生成GB 27887测试矩阵数据
         */
        private fun generateGB_27887TestMatrix(): DynamicTestMatrix {
            return DynamicTestMatrix(
                productName = "GB 27887 Standard Test Matrix",
                productId = "GB_27887",
                version = "1.0",
                testCases = emptyList()
            )
        }

        // ECE R129 标准测试矩阵
        private val ECE_R129_TEST_MATRIX = generateECE_R129TestMatrix()
        
        // GB 27887 标准测试矩阵
        private val GB_27887_TEST_MATRIX = generateGB_27887TestMatrix()
    }

    /**
     * 根据标准生成测试矩阵
     */
    fun generateTestMatrix(
        productName: String,
        productId: String,
        standard: String,
        heightRange: String,
        productType: String
    ): DynamicTestMatrix {
        val mapper = HeightAgeGroupMapper()
        val heightMatch = mapper.matchHeightRange(heightRange, 
            com.childproduct.designassistant.model.ProductType.CHILD_SAFETY_SEAT)
        
        val testCases = when (standard) {
            "ECE R129", "ECE R129/GB 27887" -> {
                generateECE_R129TestCases(heightMatch.matchedGroups.map { it.name })
            }
            "GB 27887" -> {
                generateGB_27887TestCases()
            }
            "EN 1888" -> {
                generateEN_1888TestCases()
            }
            "ISO 8124-3" -> {
                generateISO_8124_3TestCases()
            }
            else -> {
                emptyList()
            }
        }

        return DynamicTestMatrix(
            productName = productName,
            productId = productId,
            version = "1.0",
            testCases = testCases
        )
    }

    /**
     * 生成ECE R129测试用例
     */
    private fun generateECE_R129TestCases(groups: List<String>): List<DynamicTestCase> {
        val testCases = mutableListOf<DynamicTestCase>()
        var testId = 1

        // Q0 新生儿测试
        if (groups.contains("GROUP_0") || groups.contains("GROUP_0_PLUS") || groups.contains("GROUP_0_1_2_3")) {
            // 正面撞击 - 后向 - 直立
            testCases.add(createTestCase(
                id = "ECE_R129_${testId++}",
                pulse = PulseType.FRONTAL,
                impact = ImpactType.FRONTAL_RIGID,
                dummy = DummyType.Q0,
                position = SeatPosition.REARWARD_FACING,
                installation = InstallationType.ISOFIX_3_PTS,
                productConfig = SeatConfiguration.UPRIGHT,
                speedKmh = 50.0
            ))
            
            // 正面撞击 - 后向 - 倾斜
            testCases.add(createTestCase(
                id = "ECE_R129_${testId++}",
                pulse = PulseType.FRONTAL,
                impact = ImpactType.FRONTAL_RIGID,
                dummy = DummyType.Q0,
                position = SeatPosition.REARWARD_FACING,
                installation = InstallationType.ISOFIX_3_PTS,
                productConfig = SeatConfiguration.RECLINED,
                speedKmh = 50.0
            ))

            // 后向翻滚 - 后向 - 直立
            testCases.add(createTestCase(
                id = "ECE_R129_${testId++}",
                pulse = PulseType.REAR,
                impact = ImpactType.REAR,
                dummy = DummyType.Q0,
                position = SeatPosition.REARWARD_FACING,
                installation = InstallationType.ISOFIX_3_PTS,
                productConfig = SeatConfiguration.UPRIGHT,
                speedKmh = 30.0
            ))
        }

        // Q1.5 18个月测试
        if (groups.contains("GROUP_1") || groups.contains("GROUP_0_1_2_3")) {
            // 正面撞击 - 前向 - 直立
            testCases.add(createTestCase(
                id = "ECE_R129_${testId++}",
                pulse = PulseType.FRONTAL,
                impact = ImpactType.FRONTAL_RIGID,
                dummy = DummyType.Q1_5,
                position = SeatPosition.FORWARD_FACING,
                installation = InstallationType.ISOFIX_3_PTS,
                productConfig = SeatConfiguration.UPRIGHT,
                speedKmh = 50.0
            ))
            
            // 侧面撞击 - 前向
            testCases.add(createTestCase(
                id = "ECE_R129_${testId++}",
                pulse = PulseType.LATERAL,
                impact = ImpactType.LATERAL_DEFORMABLE,
                dummy = DummyType.Q1_5,
                position = SeatPosition.FORWARD_FACING,
                installation = InstallationType.ISOFIX_3_PTS,
                productConfig = SeatConfiguration.UPRIGHT,
                speedKmh = 50.0
            ))
        }

        // Q3 3岁测试
        if (groups.contains("GROUP_2") || groups.contains("GROUP_1") || groups.contains("GROUP_0_1_2_3")) {
            // 正面撞击 - 前向 - 直立
            testCases.add(createTestCase(
                id = "ECE_R129_${testId++}",
                pulse = PulseType.FRONTAL,
                impact = ImpactType.FRONTAL_RIGID,
                dummy = DummyType.Q3,
                position = SeatPosition.FORWARD_FACING,
                installation = InstallationType.ISOFIX_3_PTS,
                productConfig = SeatConfiguration.UPRIGHT,
                speedKmh = 50.0
            ))
            
            // 正面撞击 - 前向 - 倾斜
            testCases.add(createTestCase(
                id = "ECE_R129_${testId++}",
                pulse = PulseType.FRONTAL,
                impact = ImpactType.FRONTAL_RIGID,
                dummy = DummyType.Q3,
                position = SeatPosition.FORWARD_FACING,
                installation = InstallationType.ISOFIX_3_PTS,
                productConfig = SeatConfiguration.RECLINED,
                speedKmh = 50.0
            ))

            // 侧面撞击 - 前向
            testCases.add(createTestCase(
                id = "ECE_R129_${testId++}",
                pulse = PulseType.LATERAL,
                impact = ImpactType.LATERAL_DEFORMABLE,
                dummy = DummyType.Q3,
                position = SeatPosition.FORWARD_FACING,
                installation = InstallationType.ISOFIX_3_PTS,
                productConfig = SeatConfiguration.UPRIGHT,
                speedKmh = 50.0
            ))

            // 正面撞击 - 后向 - 直立
            testCases.add(createTestCase(
                id = "ECE_R129_${testId++}",
                pulse = PulseType.FRONTAL,
                impact = ImpactType.FRONTAL_RIGID,
                dummy = DummyType.Q3,
                position = SeatPosition.REARWARD_FACING,
                installation = InstallationType.ISOFIX_3_PTS,
                productConfig = SeatConfiguration.UPRIGHT,
                speedKmh = 50.0
            ))
        }

        // Q6 6岁测试
        if (groups.contains("GROUP_2") || groups.contains("GROUP_3") || groups.contains("GROUP_0_1_2_3")) {
            // 正面撞击 - 前向 - 直立
            testCases.add(createTestCase(
                id = "ECE_R129_${testId++}",
                pulse = PulseType.FRONTAL,
                impact = ImpactType.FRONTAL_RIGID,
                dummy = DummyType.Q6,
                position = SeatPosition.FORWARD_FACING,
                installation = InstallationType.ISOFIX_3_PTS,
                productConfig = SeatConfiguration.UPRIGHT,
                speedKmh = 50.0
            ))
            
            // 侧面撞击 - 前向
            testCases.add(createTestCase(
                id = "ECE_R129_${testId++}",
                pulse = PulseType.LATERAL,
                impact = ImpactType.LATERAL_DEFORMABLE,
                dummy = DummyType.Q6,
                position = SeatPosition.FORWARD_FACING,
                installation = InstallationType.ISOFIX_3_PTS,
                productConfig = SeatConfiguration.UPRIGHT,
                speedKmh = 50.0
            ))
        }

        // Q10 10岁测试
        if (groups.contains("GROUP_3") || groups.contains("GROUP_0_1_2_3")) {
            // 正面撞击 - 前向 - 直立
            testCases.add(createTestCase(
                id = "ECE_R129_${testId++}",
                pulse = PulseType.FRONTAL,
                impact = ImpactType.FRONTAL_RIGID,
                dummy = DummyType.Q10,
                position = SeatPosition.FORWARD_FACING,
                installation = InstallationType.ISOFIX_3_PTS,
                productConfig = SeatConfiguration.UPRIGHT,
                speedKmh = 50.0
            ))
        }

        return testCases
    }

    /**
     * 生成GB 27887测试用例
     */
    private fun generateGB_27887TestCases(): List<DynamicTestCase> {
        // GB 27887 与 ECE R129 测试矩阵类似
        return generateECE_R129TestCases(listOf("GROUP_0_1_2_3"))
    }

    /**
     * 生成EN 1888测试用例（婴儿推车）
     */
    private fun generateEN_1888TestCases(): List<DynamicTestCase> {
        val testCases = mutableListOf<DynamicTestCase>()
        // 婴儿推车测试用例
        return testCases
    }

    /**
     * 生成ISO 8124-3测试用例（儿童高脚椅）
     */
    private fun generateISO_8124_3TestCases(): List<DynamicTestCase> {
        val testCases = mutableListOf<DynamicTestCase>()
        // 儿童高脚椅测试用例
        return testCases
    }

    /**
     * 创建测试用例
     */
    private fun createTestCase(
        id: String,
        pulse: PulseType,
        impact: ImpactType,
        dummy: DummyType,
        position: SeatPosition,
        installation: InstallationType,
        productConfig: SeatConfiguration,
        speedKmh: Double
    ): DynamicTestCase {
        val input = TestInputParameter(
            sample = "ECE_R129",
            pulse = pulse,
            impact = impact,
            dummy = dummy,
            position = position,
            installation = installation,
            specificInstallation = null,
            productConfiguration = productConfig,
            isofixAnchors = true,
            floorPosition = FloorPosition.LOW,
            harness = true,
            topTetherOrSupportLeg = true,
            dashboard = true,
            comments = null
        )

        val output = TestOutputParameter(
            buckle = null,
            adjuster = null,
            isofix = null,
            tt = null,
            quantity = null,
            testNumber = null,
            speedKmh = speedKmh,
            maxPulseG = null,
            stoppingDistanceMm = null,
            headExcursionMm = null,
            verticalLimitMm = null,
            timeForHeadExcursionMs = null,
            minVerticalG = null,
            tMinus30gMs = null,
            y3msG = null,
            maxResultantG = null,
            t55gMs = null,
            y3msG_2 = null,
            accHeadRes3ms = null,
            hic36ForADAC = null,
            hic36ForR44 = null,
            hpc15ForIsize = null,
            frForADAC = null,
            fzForIsize = null,
            fzForPLUS = null,
            upperNeckForce = null,
            upperNeckMomentMx = null,
            upperNeckMomentMy = null,
            upperNeckMomentMr = null,
            chestDeflectionMm = null,
            observationAfterCrash = null,
            status = TestStatus.NOT_STARTED
        )

        return DynamicTestCase(
            id = id,
            input = input,
            output = output
        )
    }
}

/**
 * 测试矩阵显示数据（用于UI）
 */
data class TestMatrixDisplayItem(
    val testId: String,
    val pulse: String,
    val impact: String,
    val dummy: String,
    val position: String,
    val installation: String,
    val productConfig: String,
    val speed: String,
    val acceptanceCriteria: String
)

/**
 * 将测试用例转换为显示格式
 */
fun DynamicTestCase.toDisplayItem(): TestMatrixDisplayItem {
    return TestMatrixDisplayItem(
        testId = id,
        pulse = input.pulse.name,
        impact = input.impact.name,
        dummy = input.dummy.name,
        position = input.position.name,
        installation = input.installation.name,
        productConfig = input.productConfiguration.name,
        speed = output?.speedKmh?.let { "${it} km/h" } ?: "N/A",
        acceptanceCriteria = generateAcceptanceCriteria(input)
    )
}

/**
 * 生成合格标准
 */
fun generateAcceptanceCriteria(input: TestInputParameter): String {
    return when {
        input.impact == ImpactType.FRONTAL_RIGID -> {
            "HIC36 ≤ 324, 胸部加速度 ≤ 55g"
        }
        input.impact == ImpactType.LATERAL_DEFORMABLE -> {
            "头部偏移 ≤ 150mm, 颈部力 ≤ 2.5kN"
        }
        input.impact == ImpactType.REAR -> {
            "假人加速度 ≤ 25g, 座椅无结构性变形"
        }
        else -> {
            "符合ECE R129标准要求"
        }
    }
}

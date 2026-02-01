package com.childproduct.designassistant.utils

import com.childproduct.designassistant.model.TestMatrixItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

/**
 * Tether 类型枚举（根据 ECE R129 §6.1.2）
 */
enum class TetherType(val displayName: String, val description: String) {
    SUPPORT_LEG("Support leg", "支撑腿（适用于40-105cm后向安装）"),
    TOP_TETHER("Top Tether", "上拉带（适用于105-150cm前向安装）"),
    BOTH("Support leg + Top Tether", "双模式（根据身高范围自动切换）")
}

/**
 * ROADMATE 360测试矩阵输出生成器（ECE R129单一标准 · 40-150cm全范围）
 * 严格遵循ECE R129 Annex 19标准，不混用GB 27887-2024/FMVSS 213
 * 假人映射：Q0(40-50cm) → Q0+(50-60cm) → Q1(60-75cm) → Q1.5(75-87cm) → Q3(87-105cm) → Q3s(105-125cm) → Q6(125-145cm) → Q10(145-150cm)
 */
object Roadmate360OutputGenerator {

    // ========== 标准映射表（ECE R129 Annex 19）==========
    private val HEIGHT_TO_DUMMY_MAPPING = listOf(
        HeightDummyMapping(40, 50, "Q0", "Group 0+", "新生儿", "Rearward facing", "Support leg"),
        HeightDummyMapping(50, 60, "Q0+", "Group 0+", "大婴儿", "Rearward facing", "Support leg"),
        HeightDummyMapping(60, 75, "Q1", "Group 0+/1", "幼儿", "Rearward facing", "Support leg"),
        HeightDummyMapping(75, 87, "Q1.5", "Group 1", "学步儿童", "Rearward facing", "Support leg"),
        HeightDummyMapping(87, 105, "Q3", "Group 2", "学前儿童", "Rearward facing", "Support leg"),
        HeightDummyMapping(105, 125, "Q3s", "Group 2/3", "儿童", "Forward facing", "Top Tether"),
        HeightDummyMapping(125, 145, "Q6", "Group 3", "大龄儿童", "Forward facing", "Top Tether"),
        HeightDummyMapping(145, 150, "Q10", "Group 3", "青少年", "Forward facing", "Top Tether")
    )

    data class HeightDummyMapping(
        val minHeightCm: Int,
        val maxHeightCm: Int,
        val dummyType: String,
        val productGroup: String,
        val description: String,
        val position: String,          // Rearward/Forward facing
        val tetherType: String         // Support leg/Top Tether
    )

    // ========== 安全阈值（ECE R129 §7.1 · 分段精确）==========
    private val SAFETY_THRESHOLDS = listOf(
        SafetyThreshold("HIC15", "≤390", "Q0-Q1.5", "-", "ECE R129 §7.1.2"),
        SafetyThreshold("HIC36", "≤570", "Q1.5", "-", "ECE R129 §7.1.2"),
        SafetyThreshold("HIC36", "≤1000", "Q3-Q10", "-", "ECE R129 §7.1.2"),
        SafetyThreshold("胸部合成加速度(3ms)", "≤55g", "Q0-Q1.5", "g", "ECE R129 §7.1.3"),
        SafetyThreshold("胸部合成加速度(3ms)", "≤60g", "Q3-Q10", "g", "ECE R129 §7.1.3"),
        SafetyThreshold("颈部张力", "≤1800N", "Q0-Q1.5", "N", "ECE R129 §7.1.4"),
        SafetyThreshold("颈部张力", "≤2000N", "Q3-Q10", "N", "ECE R129 §7.1.4"),
        SafetyThreshold("颈部压缩", "≤2200N", "Q0-Q1.5", "N", "ECE R129 §7.1.4"),
        SafetyThreshold("颈部压缩", "≤2500N", "Q3-Q10", "N", "ECE R129 §7.1.4"),
        SafetyThreshold("头部位移", "≤550mm", "Q0-Q10", "mm", "ECE R129 §7.1.5"),
        SafetyThreshold("膝部位移", "≤650mm", "Q0-Q10", "mm", "ECE R129 §7.1.5"),
        SafetyThreshold("胸部位移", "≤45mm", "Q0-Q1.5", "mm", "ECE R129 §7.1.6"),
        SafetyThreshold("胸部位移", "≤52mm", "Q3-Q10", "mm", "ECE R129 §7.1.6")
    )

    data class SafetyThreshold(
        val testItem: String,
        val standardRequirement: String,
        val applicableDummy: String,
        val unit: String,
        val standardSource: String
    )

    // ========== ROADMATE 360测试项模型 ==========
    data class Roadmate360TestItem(
        val sample: String = "R129",
        val pulse: String,              // Frontal/Rear/Lateral
        val impact: String,             // 假人类型：Q0/Q1/Q3/Q3s/Q6/Q10
        val dummy: String,              // 假人类型（同impact，ROADMATE格式要求）
        val position: String,           // Rearward/Forward facing
        val installation: String,       // Isofix 3 pts / Isofix 2 pts
        val specificInstallation: String = "-",
        val productConfiguration: String, // Upright/Reclined
        val isofixAnchors: String = "yes",
        val positionOfFloor: String = "Low",
        val harness: String = "With",
        val topTetherSupportLeg: String,  // With/Without/no
        val dashboard: String = "With",
        val comments: String = "-",
        val buckle: String = "no",
        val adjuster: String = "no",
        val isofix: String = "yes",
        val tt: String,                   // yes/no
        val quantity: String = "n/a",
        val testNo: String = "-"
    )

    // ========== 核心方法：生成完整输出 ==========
    /**
     * 生成完整输出
     * @param minHeightCm 最小身高
     * @param maxHeightCm 最大身高
     * @param productType 产品类型
     * @param designTheme 设计主题
     * @param tetherType Tether类型（Support leg / Top Tether / Both）
     */
    fun generateOutput(
        minHeightCm: Int,
        maxHeightCm: Int,
        productType: String = "儿童安全座椅",
        designTheme: String = "全阶段成长型",
        tetherType: TetherType = TetherType.BOTH
    ): String {
        require(minHeightCm in 40..150 && maxHeightCm in 40..150 && minHeightCm < maxHeightCm) {
            "身高范围必须在40-150cm之间，且最小值小于最大值"
        }

        // 根据 tetherType 生成安装方式描述
        val installMethodDesc = when (tetherType) {
            TetherType.SUPPORT_LEG -> "- 40-105cm：ISOFIX 3点连接 + 支撑腿（后向安装，ECE R129 §5.1.3强制要求）"
            TetherType.TOP_TETHER -> "- 105-150cm：ISOFIX 3点连接 + Top-tether上拉带（前向安装，ECE R129 §6.1.2强制要求）"
            TetherType.BOTH -> """
                |- 40-105cm：ISOFIX 3点连接 + 支撑腿（后向安装，ECE R129 §5.1.3强制要求）
                |- 105-150cm：ISOFIX 3点连接 + Top-tether上拉带（前向安装，ECE R129 §6.1.2强制要求）
            """.trimMargin()
        }

        return buildString {
            // ========== 基本信息 ==========
            appendLine("【设计方案】")
            appendLine("产品类型：$productType")
            appendLine("身高范围：${minHeightCm}-${maxHeightCm}cm")
            appendLine("适配年龄段：${getAgeRange(maxHeightCm)}")
            appendLine("设计主题：${designTheme}安全座椅")
            appendLine("Tether类型：${tetherType.displayName} - ${tetherType.description}")
            appendLine("安装方式：")
            installMethodDesc.lines().forEach { appendLine(it) }
            appendLine()
            appendLine("【标准说明】")
            appendLine("本方案严格遵循单一标准ECE R129 (i-Size)，不混用GB 27887-2024或FMVSS 213")
            appendLine("身高40-150cm按ECE R129 Annex 19完整映射8个假人区间：")
            appendLine("- 40-50cm → Q0（新生儿）")
            appendLine("- 50-60cm → Q0+（大婴儿）")
            appendLine("- 60-75cm → Q1（幼儿）")
            appendLine("- 75-87cm → Q1.5（学步儿童）")
            appendLine("- 87-105cm → Q3（学前儿童）")
            appendLine("- 105-125cm → Q3s（儿童）")
            appendLine("- 125-145cm → Q6（大龄儿童）")
            appendLine("- 145-150cm → Q10（青少年）")
            appendLine()
            appendLine("安装方向规则（ECE R129 §5.1.3）：")
            appendLine("- 40-105cm：强制后向安装（Rearward facing）")
            appendLine("- 105-150cm：允许前向安装（Forward facing），必须使用Top-tether防旋转")
            appendLine()

            // ========== 测试矩阵 ==========
            appendLine("【测试矩阵】（ROADMATE 360格式 · ECE R129 Annex 19）")
            appendLine()
            appendLine(generateRoadmate360Table(minHeightCm, maxHeightCm, tetherType))
            appendLine()

            // ========== 安全阈值 ==========
            appendLine("【安全阈值】（ECE R129 §7.1 · 单一标准分段）")
            appendLine()
            appendLine(generateSafetyThresholdsTable())
            appendLine()

            // ========== 合规声明 ==========
            appendLine("【合规声明】")
            appendLine("本设计方案仅适配ECE R129 (i-Size) 标准，符合以下要求：")
            appendLine("- 假人覆盖：Q0→Q10全假人序列（40-150cm），符合ECE R129 §5.2")
            appendLine("- 安装方向：")
            appendLine("  • 40-105cm：强制后向安装（ECE R129 §5.1.3）")
            appendLine("  • 105-150cm：前向安装+Top-tether强制（ECE R129 §6.1.2）")
            appendLine("- 动态测试：正面/后向/侧向碰撞全覆盖（ECE R129 §7）")
            appendLine("- ISOFIX连接：3点式+支撑腿/上拉带双重防旋转（ECE R129 §6.1.2）")
            appendLine("- 身高范围：40-150cm（ECE R129 §5.1.2）")
            appendLine()
            appendLine("【安全注意事项】（ECE R129 §8）")
            appendLine("- 安装警示：")
            appendLine("  • 40-105cm：必须后向安装，禁止前向（ECE R129 §8.1）")
            appendLine("  • 105-150cm：前向安装必须使用Top-tether，禁止仅用ISOFIX（ECE R129 §8.1）")
            appendLine("- 产品标识：座椅侧面必须永久标注：")
            appendLine("  • \"40-105cm: Rearward facing only\"")
            appendLine("  • \"105-150cm: Forward facing with Top-tether\"")
            appendLine("- 阻燃性能：面料燃烧速度≤4英寸/分钟（FMVSS 302，ECE R129引用要求）")
            appendLine("- 成长调节：头枕/肩带7档高度调节，适配Q0-Q10全假人肩高范围")
        }
    }

    // ========== 私有方法：生成ROADMATE 360表格 ==========
    private fun generateRoadmate360Table(minHeightCm: Int, maxHeightCm: Int, tetherType: TetherType): String {
        val testItems = generateTestMatrix(minHeightCm, maxHeightCm, tetherType)

        return buildString {
            // 表头（20列）
            appendLine("| # Sample | Pulse | Impact | Dummy | Position | Installation | Specific Installation | Product Configuration | Isofix anchors | Position of floor | Harness | Top Tether / Support leg | Dashboard | Comments | Buckle | Adjuster | Isofix | TT | QUANTITY | Testn° |")
            appendLine("|----------|-------|--------|-------|----------|--------------|----------------------|----------------------|----------------|-------------------|---------|--------------------------|-----------|----------|--------|----------|--------|----|----------|--------|")

            // 表格内容
            testItems.forEach { item ->
                appendLine(
                    "| ${item.sample} | ${item.pulse} | ${item.impact} | ${item.dummy} | ${item.position} | " +
                            "${item.installation} | ${item.specificInstallation} | ${item.productConfiguration} | " +
                            "${item.isofixAnchors} | ${item.positionOfFloor} | ${item.harness} | " +
                            "${item.topTetherSupportLeg} | ${item.dashboard} | ${item.comments} | " +
                            "${item.buckle} | ${item.adjuster} | ${item.isofix} | ${item.tt} | " +
                            "${item.quantity} | ${item.testNo} |"
                )
            }
        }
    }

    // ========== 私有方法：生成测试矩阵项 ==========
    private fun generateTestMatrix(minHeightCm: Int, maxHeightCm: Int, tetherType: TetherType): List<Roadmate360TestItem> {
        val items = mutableListOf<Roadmate360TestItem>()
        
        // 根据 tetherType 确定 topTetherSupportLeg 的值
        val rearwardTetherValue = when (tetherType) {
            TetherType.SUPPORT_LEG, TetherType.BOTH -> "With"  // 后向安装使用 Support leg
            TetherType.TOP_TETHER -> "no"                    // 不使用 Support leg
        }
        
        val forwardTetherValue = when (tetherType) {
            TetherType.TOP_TETHER, TetherType.BOTH -> "With"  // 前向安装使用 Top Tether
            TetherType.SUPPORT_LEG -> "With"                  // 前向安装也显示 With（可能是双模式）
        }

        // 后向安装测试（40-105cm）：Q0, Q0+, Q1, Q1.5, Q3
        if (minHeightCm < 105) {
            // Q0假人（40-50cm）
            if (maxHeightCm > 40) {
                items.addAll(listOf(
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q0", dummy = "Q0", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With",
                        comments = "if contact repeat the test without dashboard"
                    ),
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q0", dummy = "Q0", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Reclined",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With",
                        comments = "if contact repeat the test without dashboard"
                    ),
                    Roadmate360TestItem(
                        pulse = "Rear", impact = "Q0", dummy = "Q0", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With"
                    ),
                    Roadmate360TestItem(
                        pulse = "Lateral", impact = "Q0", dummy = "Q0", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "no"
                    )
                ))
            }

            // Q0+假人（50-60cm）
            if (maxHeightCm > 50) {
                items.addAll(listOf(
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q0+", dummy = "Q0+", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With",
                        comments = "if contact repeat the test without dashboard"
                    ),
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q0+", dummy = "Q0+", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Reclined",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With",
                        comments = "if contact repeat the test without dashboard"
                    ),
                    Roadmate360TestItem(
                        pulse = "Rear", impact = "Q0+", dummy = "Q0+", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With"
                    )
                ))
            }

            // Q1假人（60-75cm）
            if (maxHeightCm > 60) {
                items.addAll(listOf(
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q1", dummy = "Q1", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With",
                        comments = "if contact repeat the test without dashboard"
                    ),
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q1", dummy = "Q1", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Reclined",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With",
                        comments = "if contact repeat the test without dashboard"
                    ),
                    Roadmate360TestItem(
                        pulse = "Rear", impact = "Q1", dummy = "Q1", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With"
                    )
                ))
            }

            // Q1.5假人（75-87cm）
            if (maxHeightCm > 75) {
                items.addAll(listOf(
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q1.5", dummy = "Q1.5", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With",
                        comments = "if contact repeat the test without dashboard"
                    ),
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q1.5", dummy = "Q1.5", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Reclined",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With",
                        comments = "if contact repeat the test without dashboard"
                    ),
                    Roadmate360TestItem(
                        pulse = "Rear", impact = "Q1.5", dummy = "Q1.5", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With"
                    )
                ))
            }

            // Q3假人（87-105cm）
            if (maxHeightCm > 87) {
                items.addAll(listOf(
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q3", dummy = "Q3", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With",
                        comments = "if contact repeat the test without dashboard"
                    ),
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q3", dummy = "Q3", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Reclined",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With",
                        comments = "if contact repeat the test without dashboard"
                    ),
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q3", dummy = "Q3", position = "Rearward facing",
                        installation = "Isofix 2 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = "no", tt = "no", dashboard = "Without"
                    ),
                    Roadmate360TestItem(
                        pulse = "Rear", impact = "Q3", dummy = "Q3", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "With"
                    ),
                    Roadmate360TestItem(
                        pulse = "Lateral", impact = "Q3", dummy = "Q3", position = "Rearward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = rearwardTetherValue, tt = "no", dashboard = "no"
                    )
                ))
            }
        }

        // 前向安装测试（105-150cm）：Q3s, Q6, Q10
        if (maxHeightCm >= 105) {
            // Q3s假人（105-125cm）
            if (maxHeightCm > 105) {
                items.addAll(listOf(
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q3s", dummy = "Q3s", position = "Forward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = forwardTetherValue, tt = "yes", dashboard = "With"
                    ),
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q3s", dummy = "Q3s", position = "Forward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Reclined",
                        topTetherSupportLeg = forwardTetherValue, tt = "yes", dashboard = "With"
                    ),
                    Roadmate360TestItem(
                        pulse = "Lateral", impact = "Q3s", dummy = "Q3s", position = "Forward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = "With", tt = "no", dashboard = "no"
                    )
                ))
            }

            // Q6假人（125-145cm）
            if (maxHeightCm > 125) {
                items.addAll(listOf(
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q6", dummy = "Q6", position = "Forward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = forwardTetherValue, tt = "yes", dashboard = "With"
                    ),
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q6", dummy = "Q6", position = "Forward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Reclined",
                        topTetherSupportLeg = forwardTetherValue, tt = "yes", dashboard = "With"
                    ),
                    Roadmate360TestItem(
                        pulse = "Rear", impact = "Q6", dummy = "Q6", position = "Forward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = forwardTetherValue, tt = "yes", dashboard = "With"
                    ),
                    Roadmate360TestItem(
                        pulse = "Lateral", impact = "Q6", dummy = "Q6", position = "Forward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = "With", tt = "no", dashboard = "no"
                    )
                ))
            }

            // Q10假人（145-150cm）
            if (maxHeightCm >= 145) {
                items.addAll(listOf(
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q10", dummy = "Q10", position = "Forward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = forwardTetherValue, tt = "yes", dashboard = "With"
                    ),
                    Roadmate360TestItem(
                        pulse = "Frontal", impact = "Q10", dummy = "Q10", position = "Forward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Reclined",
                        topTetherSupportLeg = forwardTetherValue, tt = "yes", dashboard = "With"
                    ),
                    Roadmate360TestItem(
                        pulse = "Rear", impact = "Q10", dummy = "Q10", position = "Forward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = forwardTetherValue, tt = "yes", dashboard = "With"
                    ),
                    Roadmate360TestItem(
                        pulse = "Lateral", impact = "Q10", dummy = "Q10", position = "Forward facing",
                        installation = "Isofix 3 pts", productConfiguration = "Upright",
                        topTetherSupportLeg = "With", tt = "no", dashboard = "no"
                    )
                ))
            }
        }

        return items
    }

    // ========== 私有方法：生成安全阈值表格 ==========
    private fun generateSafetyThresholdsTable(): String {
        return buildString {
            appendLine("| 测试项目 | 标准要求 | 适用假人 | 单位 | 标准条款 |")
            appendLine("|----------|----------|----------|------|----------|")
            SAFETY_THRESHOLDS.forEach { threshold ->
                appendLine(
                    "| ${threshold.testItem} | ${threshold.standardRequirement} | " +
                            "${threshold.applicableDummy} | ${threshold.unit} | ${threshold.standardSource} |"
                )
            }
        }
    }

    // ========== 与现有架构集成 ==========
    /**
     * 生成标准化方案（兼容ChildProductDesignScheme）
     * @param minHeightCm 最小身高
     * @param maxHeightCm 最大身高
     * @param productType 产品类型
     * @param designTheme 设计主题
     * @param tetherType Tether类型（Support leg / Top Tether / Both）
     */
    fun generateStandardizedScheme(
        minHeightCm: Int,
        maxHeightCm: Int,
        productType: String = "儿童安全座椅",
        designTheme: String = "全阶段成长型",
        tetherType: TetherType = TetherType.BOTH
    ): com.childproduct.designassistant.model.ChildProductDesignScheme {
        // 根据 tetherType 生成安装方式描述
        val installMethodDesc = when (tetherType) {
            TetherType.SUPPORT_LEG -> "40-105cm：ISOFIX+支撑腿（后向）"
            TetherType.TOP_TETHER -> "105-150cm：ISOFIX+Top-tether（前向）"
            TetherType.BOTH -> "40-105cm：ISOFIX+支撑腿（后向）；105-150cm：ISOFIX+Top-tether（前向）"
        }

        // 构建核心特点（根据 tetherType 动态生成）
        val coreFeatures = buildList {
            add("易安装性：ISOFIX 3点连接 + ${tetherType.displayName}双重防旋转")
            add("安全性：符合ECE R129标准（Q0-Q10全假人覆盖，分段安全阈值）")
            add("舒适性：高回弹海绵填充（密度30kg/m³，适配0-12岁儿童体型变化）")
            add("材质环保：食品级PP塑料（无甲醛/重金属，符合EN 71-3有害元素标准）")
            
            // 根据 tetherType 添加额外特点
            when (tetherType) {
                TetherType.SUPPORT_LEG -> add("稳固性：支撑腿触地设计，防止前倾（符合ECE R129 §6.1.2）")
                TetherType.TOP_TETHER -> add("防翻滚：上拉带锚固设计，防止后翻（符合ECE R129 §6.1.3）")
                TetherType.BOTH -> add("双模式：支撑腿+上拉带智能切换，全阶段防护")
            }
        }.toImmutableList()

        // 构建推荐材料
        val recommendMaterials = listOf(
            "主体框架：食品级PP塑料（耐温-30℃~80℃，抗冲击强度≥20kJ/m²）",
            "填充层：高回弹海绵（压缩回弹率≥90%，无异味）",
            "约束部件：高强度安全带织带（断裂强度≥11000N，耐磨后强度保留率≥75%）",
            "支撑结构：铝合金支架（盐雾测试50小时无腐蚀，抗拉强度≥300MPa）",
            "面料：阻燃聚酯纤维（符合FMVSS 302，燃烧速度≤4英寸/分钟）"
        ).toImmutableList()

        // 构建安全注意事项（根据 tetherType 动态生成）
        val safetyNotes = buildList {
            add("防吞咽风险：所有可拆卸部件尺寸≥3.5cm（ISO 8124-2要求）")
            add("材质安全：使用食品级PP塑料，无甲醛/重金属残留（EN 71-3）")
            add("边缘安全：产品边缘做圆角处理（R≥2mm），无尖锐突出物")
            add("防火阻燃：面料通过FMVSS 302认证（燃烧速度≤4英寸/分钟）")
            
            // 根据 tetherType 添加安装警示
            when (tetherType) {
                TetherType.SUPPORT_LEG -> add("安装警示：支撑腿必须与地面紧密接触，确保最大防护（ECE R129 §8.1）")
                TetherType.TOP_TETHER -> add("安装警示：上拉带必须正确锚固至车辆锚点，拉力≥400N（ECE R129 §8.1）")
                TetherType.BOTH -> add("安装警示：40-105cm必须后向安装；105-150cm前向安装必须使用Top-tether（ECE R129 §8.1）")
            }
        }.toImmutableList()

        // 构建测试矩阵项（添加 tetherType 信息）
        val testMatrixItems = SAFETY_THRESHOLDS.map { threshold ->
            com.childproduct.designassistant.model.TestMatrixItem(
                testItem = threshold.testItem,
                standardRequirement = threshold.standardRequirement,
                applicableDummy = threshold.applicableDummy,
                unit = threshold.unit,
                standardSource = threshold.standardSource + " [${tetherType.displayName}]" // 在标准来源中标注 tether 类型
            )
        }.toImmutableList()

        return com.childproduct.designassistant.model.ChildProductDesignScheme(
            productType = productType,
            heightRange = "${minHeightCm}-${maxHeightCm}cm",
            ageRange = getAgeRange(maxHeightCm),
            designTheme = "${designTheme}安全座椅",
            installMethodDesc = installMethodDesc,
            coreFeatures = coreFeatures,
            recommendMaterials = recommendMaterials,
            complianceStandards = listOf("ECE R129 (i-Size)").toImmutableList(),
            dummyType = "Q0→Q10全假人序列（40-150cm）",
            safetyThresholds = mapOf(
                "HIC" to "HIC15≤390(Q0-Q1.5)/HIC36≤1000(Q3-Q10)",
                "胸部加速度" to "≤55g(Q0-Q1.5)/≤60g(Q3-Q10)",
                "颈部张力" to "≤1800N(Q0-Q1.5)/≤2000N(Q3-Q10)",
                "头部位移" to "≤550mm(全假人)",
                "膝部位移" to "≤650mm(全假人)",
                "胸部位移" to "≤45mm(Q0-Q1.5)/≤52mm(Q3-Q10)"
            ).toImmutableMap(),
            testMatrix = testMatrixItems,
            safetyNotes = safetyNotes
        )
    }

    /**
     * 格式化方案用于UI展示（纯文本，无代码字段）
     */
    fun formatForDisplay(scheme: com.childproduct.designassistant.model.ChildProductDesignScheme): String {
        // 从scheme中提取身高范围
        val heightPattern = Regex("(\\d+)-(\\d+)cm")
        val match = heightPattern.find(scheme.heightRange)
        val (minHeight, maxHeight) = if (match != null) {
            Pair(match.groupValues[1].toInt(), match.groupValues[2].toInt())
        } else {
            Pair(40, 150)
        }

        return generateOutput(
            minHeightCm = minHeight,
            maxHeightCm = maxHeight,
            productType = scheme.productType,
            designTheme = scheme.designTheme.replace("安全座椅", "").trim()
        )
    }

    // ========== 辅助方法：根据身高范围获取年龄段 ==========
    private fun getAgeRange(maxHeightCm: Int): String {
        return when {
            maxHeightCm <= 50 -> "0-6个月"
            maxHeightCm <= 60 -> "0-12个月"
            maxHeightCm <= 75 -> "0-2岁"
            maxHeightCm <= 87 -> "0-3岁"
            maxHeightCm <= 105 -> "0-4岁"
            maxHeightCm <= 125 -> "0-6岁"
            maxHeightCm <= 145 -> "0-10岁"
            else -> "0-12岁"
        }
    }
}

package com.childproduct.designassistant.utils

import com.childproduct.designassistant.model.ChildProductDesignScheme
import com.childproduct.designassistant.model.TestMatrixItem
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

/**
 * ROADMATE 360测试矩阵专用输出生成器
 * 严格遵循单一标准ECE R129，不混用GB 27887-2024/FMVSS 213
 * 修正假人映射：105-150cm → Q6(105-145cm) + Q10(145-150cm)
 */
object Roadmate360OutputGenerator {

    // ========== 标准映射表（ECE R129单一标准）==========
    private val HEIGHT_TO_DUMMY_MAPPING = mapOf(
        "105-145" to "Q6",
        "105-145cm" to "Q6",
        "145-150" to "Q10",
        "145-150cm" to "Q10",
        "105-150" to "Q6+Q10",
        "105-150cm" to "Q6+Q10"
    )

    // ========== 安全阈值（ECE R129 §7.1 单一标准）==========
    private val ECE_R129_SAFETY_THRESHOLDS = listOf(
        SafetyThreshold("HIC36", "≤1000", "Q6/Q10", "-", "ECE R129 §7.1.2"),
        SafetyThreshold("胸部合成加速度(3ms)", "≤60g", "Q6/Q10", "g", "ECE R129 §7.1.3"),
        SafetyThreshold("颈部张力", "≤2000N", "Q6/Q10", "N", "ECE R129 §7.1.4"),
        SafetyThreshold("颈部压缩", "≤2500N", "Q6/Q10", "N", "ECE R129 §7.1.4"),
        SafetyThreshold("头部位移", "≤550mm", "Q6/Q10", "mm", "ECE R129 §7.1.5"),
        SafetyThreshold("膝部位移", "≤650mm", "Q6/Q10", "mm", "ECE R129 §7.1.5"),
        SafetyThreshold("胸部位移", "≤52mm", "Q6/Q10", "mm", "ECE R129 §7.1.6")
    )

    // ========== 数据模型 ==========
    data class SafetyThreshold(
        val testItem: String,
        val standardRequirement: String,
        val applicableDummy: String,
        val unit: String,
        val standardSource: String
    )

    data class Roadmate360TestItem(
        val sample: String = "R129",
        val pulse: String,
        val impact: String,
        val dummy: String,
        val position: String,
        val installation: String,
        val specificInstallation: String = "-",
        val productConfiguration: String,
        val isofixAnchors: String = "yes",
        val positionOfFloor: String = "Low",
        val harness: String = "With",
        val topTetherSupportLeg: String,
        val dashboard: String,
        val comments: String = "-",
        val buckle: String = "no",
        val adjuster: String = "no",
        val isofix: String = "yes",
        val tt: String,
        val quantity: String = "n/a",
        val testNo: String = "-"
    )

    // ========== 核心方法：生成完整输出 ==========
    fun generateOutput(
        minHeightCm: Int,
        maxHeightCm: Int,
        productType: String = "儿童安全座椅",
        designTheme: String = "社交元素集成式安全座椅"
    ): String {
        // 1. 验证输入范围
        require(minHeightCm in 40..150 && maxHeightCm in 40..150 && minHeightCm < maxHeightCm) {
            "身高范围必须在40-150cm之间，且最小值小于最大值"
        }

        // 2. 生成测试矩阵（仅ECE R129）
        val testMatrix = generateTestMatrix(minHeightCm, maxHeightCm)

        // 3. 生成安全阈值表格
        val safetyThresholdsTable = generateSafetyThresholdsTable()

        // 4. 组装完整输出
        return buildString {
            appendLine("【设计方案】")
            appendLine("产品类型：$productType")
            appendLine("身高范围：${minHeightCm}-${maxHeightCm}cm")
            appendLine("适配年龄段：${getAgeRange(minHeightCm, maxHeightCm)}")
            appendLine("设计主题：$designTheme")
            appendLine("安装方式：ISOFIX 3点连接 + Top-tether上拉带（ECE R129 §5.2强制要求）")
            appendLine()
            appendLine("> ⚠️ 标准说明：本方案严格遵循单一标准ECE R129，不混用GB 27887-2024或FMVSS 213")
            appendLine("> 身高${minHeightCm}-${maxHeightCm}cm按ECE R129 Annex 19分段适配：")
            appendLine("> - ${getDummyRange(minHeightCm, maxHeightCm)}")
            appendLine()
            appendLine("【测试矩阵】（ROADMATE 360格式 · ECE R129 Annex 19）")
            appendLine()
            appendLine(generateRoadmate360Table(testMatrix))
            appendLine()
            appendLine("【安全阈值】（ECE R129 §7.1 · 单一标准）")
            appendLine()
            appendLine(safetyThresholdsTable)
            appendLine()
            appendLine("【合规声明】")
            appendLine("本设计方案仅适配ECE R129 (i-Size) 标准，符合以下要求：")
            appendLine("- 假人分类：${getDummyRange(minHeightCm, maxHeightCm)}（ECE R129 §5.2）")
            appendLine("- 动态测试：正面/后向/侧向碰撞（ECE R129 §7）")
            appendLine("- ISOFIX连接：3点式+Top-tether（ECE R129 §6.1.2）")
            appendLine("- 身高范围：${minHeightCm}-${maxHeightCm}cm（ECE R129 §5.1.2）")
            appendLine()
            appendLine("> ⚠️ 重要提示：如需同时满足GB 27887-2024或FMVSS 213，必须在设计输入阶段明确选择\"多标准适配\"")
        }
    }

    // ========== 私有辅助方法 ==========
    private fun generateTestMatrix(minHeightCm: Int, maxHeightCm: Int): List<Roadmate360TestItem> {
        val items = mutableListOf<Roadmate360TestItem>()

        // Q6假人测试（105-145cm）
        if (maxHeightCm > 105 && minHeightCm < 145) {
            items.addAll(listOf(
                Roadmate360TestItem(
                    pulse = "Frontal", impact = "Q6", position = "Forward facing",
                    installation = "Isofix 3 pts", productConfiguration = "Upright",
                    topTetherSupportLeg = "With", dashboard = "With", tt = "yes"
                ),
                Roadmate360TestItem(
                    pulse = "Frontal", impact = "Q6", position = "Forward facing",
                    installation = "Isofix 3 pts", productConfiguration = "Reclined",
                    topTetherSupportLeg = "With", dashboard = "With", tt = "yes"
                ),
                Roadmate360TestItem(
                    pulse = "Rear", impact = "Q6", position = "Forward facing",
                    installation = "Isofix 3 pts", productConfiguration = "Upright",
                    topTetherSupportLeg = "With", dashboard = "With", tt = "yes"
                ),
                Roadmate360TestItem(
                    pulse = "Lateral", impact = "Q6", position = "Forward facing",
                    installation = "Isofix 3 pts", productConfiguration = "Upright",
                    topTetherSupportLeg = "no", dashboard = "With", tt = "no"
                )
            ))
        }

        // Q10假人测试（145-150cm）
        if (maxHeightCm >= 145) {
            items.addAll(listOf(
                Roadmate360TestItem(
                    pulse = "Frontal", impact = "Q10", position = "Forward facing",
                    installation = "Isofix 3 pts", productConfiguration = "Upright",
                    topTetherSupportLeg = "With", dashboard = "With", tt = "yes"
                ),
                Roadmate360TestItem(
                    pulse = "Frontal", impact = "Q10", position = "Forward facing",
                    installation = "Isofix 3 pts", productConfiguration = "Reclined",
                    topTetherSupportLeg = "With", dashboard = "With", tt = "yes"
                ),
                Roadmate360TestItem(
                    pulse = "Rear", impact = "Q10", position = "Forward facing",
                    installation = "Isofix 3 pts", productConfiguration = "Upright",
                    topTetherSupportLeg = "With", dashboard = "With", tt = "yes"
                ),
                Roadmate360TestItem(
                    pulse = "Lateral", impact = "Q10", position = "Forward facing",
                    installation = "Isofix 3 pts", productConfiguration = "Upright",
                    topTetherSupportLeg = "no", dashboard = "With", tt = "no"
                )
            ))
        }

        return items
    }

    private fun generateRoadmate360Table(items: List<Roadmate360TestItem>): String {
        return buildString {
            // 表头
            appendLine("| # Sample | Pulse | Impact | Dummy | Position | Installation | Specific Installation | Product Configuration | Isofix anchors | Position of floor | Harness | Top Tether / Support leg | Dashboard | Comments | Buckle | Adjuster | Isofix | TT | QUANTITY | Testn° |")
            appendLine("|----------|-------|--------|-------|----------|--------------|----------------------|----------------------|----------------|-------------------|---------|--------------------------|-----------|----------|--------|----------|--------|----|----------|--------|")

            // 表格内容
            items.forEach { item ->
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

    private fun generateSafetyThresholdsTable(): String {
        return buildString {
            appendLine("| 测试项目 | 标准要求 | 适用假人 | 单位 | 标准条款 |")
            appendLine("|----------|----------|----------|------|----------|")
            ECE_R129_SAFETY_THRESHOLDS.forEach { threshold ->
                appendLine(
                    "| ${threshold.testItem} | ${threshold.standardRequirement} | " +
                            "${threshold.applicableDummy} | ${threshold.unit} | ${threshold.standardSource} |"
                )
            }
        }
    }

    private fun getAgeRange(minHeightCm: Int, maxHeightCm: Int): String {
        return when {
            minHeightCm >= 105 && maxHeightCm <= 125 -> "4-6岁"
            minHeightCm >= 125 && maxHeightCm <= 145 -> "6-10岁"
            minHeightCm >= 145 && maxHeightCm <= 150 -> "10-12岁"
            minHeightCm >= 105 && maxHeightCm <= 150 -> "4-12岁"
            else -> "4-12岁（分段适配）"
        }
    }

    private fun getDummyRange(minHeightCm: Int, maxHeightCm: Int): String {
        return when {
            minHeightCm >= 105 && maxHeightCm <= 145 -> "Q6假人（Group 2/3）"
            minHeightCm >= 145 && maxHeightCm <= 150 -> "Q10假人（Group 3）"
            minHeightCm >= 105 && maxHeightCm <= 150 -> "Q6假人(105-145cm) + Q10假人(145-150cm)"
            else -> "Q6+Q10（分段适配）"
        }
    }

    // ========== 与现有架构集成 ==========
    /**
     * 生成标准化设计方案（兼容ChildProductDesignScheme）
     */
    fun generateStandardizedScheme(
        minHeightCm: Int,
        maxHeightCm: Int,
        productType: String = "儿童安全座椅",
        designTheme: String = "社交元素"
    ): ChildProductDesignScheme {
        val dummyRange = getDummyRange(minHeightCm, maxHeightCm)
        val ageRange = getAgeRange(minHeightCm, maxHeightCm)

        return ChildProductDesignScheme(
            productType = productType,
            heightRange = "${minHeightCm}-${maxHeightCm}cm",
            ageRange = ageRange,
            designTheme = "$productType - $designTheme",
            installMethodDesc = "ISOFIX 3点连接 + Top-tether上拉带（ECE R129 §5.2）",
            coreFeatures = listOf(
                "易安装性：ISOFIX快速连接 + Top-tether防旋转（上拉带），30秒内完成安装",
                "安全性：符合ECE R129标准（全假人覆盖）",
                "舒适性：高回弹海绵填充（密度30kg/m³），适配4-12岁儿童体型变化",
                "材质环保：食品级PP塑料（无甲醛/重金属，符合EN 71-3有害元素标准）"
            ).toImmutableList(),
            recommendMaterials = listOf(
                "主体框架：食品级PP塑料（耐温-30℃~80℃，抗冲击强度≥20kJ/m²）",
                "填充层：高回弹海绵（压缩回弹率≥90%，无异味）",
                "约束部件：高强度安全带织带（断裂强度≥11000N）",
                "支撑结构：铝合金支架（盐雾测试50小时无腐蚀）",
                "面料：阻燃聚酯纤维（符合FMVSS 302，燃烧速度≤4英寸/分钟）"
            ).toImmutableList(),
            complianceStandards = listOf("ECE R129 (i-Size)").toImmutableList(),
            dummyType = dummyRange,
            safetyThresholds = ECE_R129_SAFETY_THRESHOLDS.associate {
                it.testItem to "${it.standardRequirement} (${it.applicableDummy})"
            }.toImmutableMap(),
            testMatrix = ECE_R129_SAFETY_THRESHOLDS.map {
                TestMatrixItem(
                    testItem = it.testItem,
                    standardRequirement = it.standardRequirement,
                    applicableDummy = it.applicableDummy,
                    unit = it.unit,
                    standardSource = it.standardSource
                )
            }.toImmutableList(),
            safetyNotes = listOf(
                "防吞咽风险：所有可拆卸部件尺寸≥3.5cm（ISO 8124-2）",
                "材质安全：使用食品级PP塑料，无甲醛/重金属残留（EN 71-3）",
                "边缘安全：产品边缘做圆角处理（R≥2mm），无尖锐突出物",
                "防火阻燃：面料通过FMVSS 302认证（燃烧速度≤4英寸/分钟）",
                "安装警示：必须使用ISOFIX+Top-tether双重固定（ECE R129 §8.1）"
            ).toImmutableList()
        )
    }

    /**
     * 格式化方案用于UI展示（纯文本，无代码字段）
     */
    fun formatForDisplay(scheme: ChildProductDesignScheme): String {
        return generateOutput(
            minHeightCm = scheme.heightRange.replace(Regex("\\D+"), "").chunked(3).first().toInt(),
            maxHeightCm = scheme.heightRange.replace(Regex("\\D+"), "").chunked(3).last().toInt(),
            productType = scheme.productType,
            designTheme = scheme.designTheme.replace("${scheme.productType} - ", "")
        )
    }
}

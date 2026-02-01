package com.childproduct.designassistant.model.engineering

/**
 * ISOFIX Envelope - 刚性约束尺寸要求
 * 基于ECE R129 Annex 17标准
 */
data class IsofixEnvelope(
    val installDirection: InstallDirection,
    val fixtureType: FixtureType,
    val anchorSpacing: DimensionConstraint,
    val foreAftTravel: DimensionConstraint,
    val lateralTravel: DimensionConstraint,
    val supportLegLength: DimensionConstraint?,
    val topTetherPosition: TopTetherPosition?,
    val staticStrength: StaticStrengthRequirement,
    val notes: List<String>
) {
    companion object {
        /**
         * 根据安装方向生成ISOFIX Envelope要求
         * @param installDirection 安装方向
         * @return ISOFIX Envelope要求
         */
        fun fromInstallDirection(installDirection: InstallDirection): IsofixEnvelope {
            return when (installDirection) {
                InstallDirection.REARWARD -> createRearwardEnvelope()
                InstallDirection.FORWARD -> createForwardEnvelope()
            }
        }

        /**
         * 后向安装ISOFIX Envelope
         */
        private fun createRearwardEnvelope(): IsofixEnvelope {
            return IsofixEnvelope(
                installDirection = InstallDirection.REARWARD,
                fixtureType = FixtureType.ISO_R2,
                anchorSpacing = DimensionConstraint(
                    nominalValue = 280.0,
                    tolerance = "±10mm",
                    minValue = 270.0,
                    maxValue = 290.0,
                    unit = "mm",
                    clause = "ECE R129 Annex 17 §4.1"
                ),
                foreAftTravel = DimensionConstraint(
                    nominalValue = 80.0,
                    tolerance = "±1mm",
                    minValue = 79.0,
                    maxValue = 81.0,
                    unit = "mm",
                    clause = "ECE R129 Annex 17 §4.2"
                ),
                lateralTravel = DimensionConstraint(
                    nominalValue = 200.0,
                    tolerance = "≥",
                    minValue = 200.0,
                    maxValue = null,
                    unit = "mm (单侧)",
                    clause = "ECE R129 Annex 17 §4.3"
                ),
                supportLegLength = DimensionConstraint(
                    nominalValue = null,
                    tolerance = "285-540mm",
                    minValue = 285.0,
                    maxValue = 540.0,
                    unit = "mm",
                    clause = "ECE R129 §6.1.2.4"
                ),
                topTetherPosition = null,
                staticStrength = StaticStrengthRequirement(
                    value = 8.0,
                    unit = "kN",
                    clause = "ECE R129 Annex 17 §5",
                    description = "静态强度要求"
                ),
                notes = listOf(
                    "后向安装必须使用支撑腿（Support leg）作为防旋转装置",
                    "支撑腿长度范围：285-540mm",
                    "侧向滑动量≥200mm（单侧），确保横向稳定性",
                    "ISO/R2治具用于后向安装的ISOFIX连接"
                )
            )
        }

        /**
         * 前向安装ISOFIX Envelope
         */
        private fun createForwardEnvelope(): IsofixEnvelope {
            return IsofixEnvelope(
                installDirection = InstallDirection.FORWARD,
                fixtureType = FixtureType.ISO_F2X,
                anchorSpacing = DimensionConstraint(
                    nominalValue = 280.0,
                    tolerance = "±10mm",
                    minValue = 270.0,
                    maxValue = 290.0,
                    unit = "mm",
                    clause = "ECE R129 Annex 17 §4.1"
                ),
                foreAftTravel = DimensionConstraint(
                    nominalValue = 80.0,
                    tolerance = "±1mm",
                    minValue = 79.0,
                    maxValue = 81.0,
                    unit = "mm",
                    clause = "ECE R129 Annex 17 §4.2"
                ),
                lateralTravel = DimensionConstraint(
                    nominalValue = 200.0,
                    tolerance = "≥",
                    minValue = 200.0,
                    maxValue = null,
                    unit = "mm (单侧)",
                    clause = "ECE R129 Annex 17 §4.3"
                ),
                supportLegLength = null,
                topTetherPosition = TopTetherPosition(
                    distanceFromSeat = 500.0,
                    distanceRange = "500-700mm",
                    minHeightRequirement = "≥座椅顶部",
                    clause = "ECE R129 §6.1.2.3"
                ),
                staticStrength = StaticStrengthRequirement(
                    value = 8.0,
                    unit = "kN",
                    clause = "ECE R129 Annex 17 §5",
                    description = "静态强度要求"
                ),
                notes = listOf(
                    "前向安装必须使用Top-tether作为防旋转装置（ECE R129 §6.1.2）",
                    "Top-tether锚点位置：座椅后方500-700mm",
                    "Top-tether锚点高度：≥座椅顶部",
                    "侧向滑动量≥200mm（单侧），确保横向稳定性",
                    "ISO/F2X治具用于前向安装的ISOFIX连接"
                )
            )
        }
    }

    /**
     * 生成Markdown格式的ISOFIX Envelope文档
     */
    fun toMarkdown(): String {
        return buildString {
            appendLine("## 【ISOFIX Envelope 尺寸要求】（ECE R129 Annex 17）")
            appendLine()
            appendLine("| 安装方向 | 治具类型 | 锚点间距 | 前后滑动量 | 侧向滑动量 | 刚性要求 |")
            appendLine("|----------|----------|----------|------------|------------|----------|")
            appendLine("| ${installDirection.displayName} | ${fixtureType.code} | ")
            appendLine("${anchorSpacing.nominalValue}${anchorSpacing.tolerance} | ")
            appendLine("${foreAftTravel.nominalValue}${foreAftTravel.tolerance} | ")
            appendLine("${lateralTravel.nominalValue}${lateralTravel.tolerance} | ")
            appendLine("静态强度≥${staticStrength.value}${staticStrength.unit} |")
            appendLine()
            
            if (supportLegLength != null) {
                appendLine("### 支撑腿要求")
                appendLine("- 长度范围：${supportLegLength.tolerance}（${supportLegLength.clause}）")
                appendLine()
            }
            
            if (topTetherPosition != null) {
                appendLine("### Top-tether要求")
                appendLine("- 锚点距离：${topTetherPosition.distanceRange}（${topTetherPosition.clause}）")
                appendLine("- 高度要求：${topTetherPosition.minHeightRequirement}")
                appendLine()
            }
            
            appendLine("### 关键尺寸")
            appendLine("- 锚点间距：${anchorSpacing.nominalValue}${anchorSpacing.tolerance}（${anchorSpacing.clause}）")
            appendLine("- 前后滑动量：${foreAftTravel.nominalValue}${foreAftTravel.tolerance}（${foreAftTravel.clause}）")
            appendLine("- 侧向滑动量：${lateralTravel.nominalValue}${lateralTravel.tolerance}（${lateralTravel.clause}）")
            appendLine("- 静态强度：≥${staticStrength.value}${staticStrength.unit}（${staticStrength.clause}）")
            appendLine()
            
            appendLine("### 设计注意事项")
            notes.forEach { note ->
                appendLine("- $note")
            }
        }
    }
}

/**
 * 尺寸约束
 */
data class DimensionConstraint(
    val nominalValue: Double?,
    val tolerance: String,
    val minValue: Double,
    val maxValue: Double?,
    val unit: String,
    val clause: String
)

/**
 * 治具类型
 */
enum class FixtureType(val code: String, val displayName: String) {
    ISO_R2("ISO/R2", "ISOFIX Rearward Fixture"),
    ISO_F2X("ISO/F2X", "ISOFIX Forward Fixture")
}

/**
 * Top-tether位置
 */
data class TopTetherPosition(
    val distanceFromSeat: Double,
    val distanceRange: String,
    val minHeightRequirement: String,
    val clause: String
)

/**
 * 静态强度要求
 */
data class StaticStrengthRequirement(
    val value: Double,
    val unit: String,
    val clause: String,
    val description: String
)

/**
 * 旋转角度限制（ECE R129 §6.1.2）
 */
data class RotationConstraint(
    val maxRotationAngle: Double,
    val unit: String = "°",
    val clause: String = "ECE R129 §6.1.2",
    val description: String = "防旋转装置生效后，座椅旋转角度≤30°"
) {
    companion object {
        val DEFAULT = RotationConstraint(
            maxRotationAngle = 30.0,
            unit = "°",
            clause = "ECE R129 §6.1.2",
            description = "防旋转装置生效后，座椅旋转角度≤30°"
        )
    }
}

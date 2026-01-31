package com.childproduct.designassistant.utils

import com.childproduct.designassistant.model.*

/**
 * 设计方案格式化工具
 * 将CreativeIdea对象转换为结构化、易读的文本格式
 */
object DesignSchemeFormatter {

    /**
     * 格式化设计方案为结构化文本
     */
    fun formatCreativeIdea(idea: CreativeIdea): String {
        return buildString {
            // 【基本信息】
            appendLine("【基本信息】")
            appendLine("- 产品类型：${idea.productType.displayName}")
            appendLine("- 适用年龄段：${idea.ageGroup.displayName}")
            appendLine("- 设计主题：${idea.theme}")
            appendLine()

            // 【核心设计特点】
            appendLine("【核心设计特点】")
            idea.features.forEach { feature ->
                appendLine("- $feature")
            }
            appendLine()

            // 【推荐材料】
            appendLine("【推荐材料】")
            idea.materials.forEach { material ->
                appendLine("- $material")
            }
            appendLine()

            // 【合规参数】
            appendLine("【合规参数】")
            idea.complianceParameters?.let { params ->
                val dummyType = params.dummyType
                appendLine("- 对应标准：${formatStandards(idea.standardsReference)}")
                appendLine("- 适配假人：${dummyType.displayName}")
                appendLine("- 安全阈值：")
                appendLine("  - HIC极限值：≤${params.hicLimit}")
                appendLine("  - 胸部加速度：≤${params.chestAccelerationLimit}g")
                appendLine("  - 颈部张力：≤${params.neckTensionLimit}N")
                appendLine("  - 头部位移：≤${params.headExcursionLimit}mm")
                if (params.neckCompressionLimit > 0) {
                    appendLine("  - 颈部压缩：≤${params.neckCompressionLimit}N")
                }
                if (params.kneeExcursionLimit > 0) {
                    appendLine("  - 膝部位移：≤${params.kneeExcursionLimit}mm")
                }
                if (params.chestDeflectionLimit > 0) {
                    appendLine("  - 胸部位移：≤${params.chestDeflectionLimit}mm")
                }
            }
            appendLine()

            // 【安全注意事项】
            appendLine("【安全注意事项】")
            idea.safetyNotes.forEach { note ->
                appendLine("- $note")
            }
        }
    }

    /**
     * 格式化标准信息
     */
    private fun formatStandards(standardsRef: StandardsReference?): String {
        if (standardsRef == null) return "暂无"

        return buildString {
            // 简化标准条款描述
            when {
                standardsRef.mainStandard.contains("ECE R129") && standardsRef.mainStandard.contains("GB 27887") -> {
                    append("符合UN R129 i-Size动态测试要求")
                    if (standardsRef.mainStandard.contains("FMVSS 213")) {
                        append(" + FMVSS 213动态测试要求")
                    }
                }
                standardsRef.mainStandard.contains("EN 1888") -> {
                    append("符合EN 1888婴儿推车稳定性与制动要求")
                }
                standardsRef.mainStandard.contains("ISO 8124") -> {
                    append("符合ISO 8124-3儿童用品安全要求")
                }
                else -> {
                    append(standardsRef.mainStandard)
                }
            }
        }
    }

    /**
     * 格式化设计方案为卡片展示格式（用于UI）
     * 返回结构化的Map，方便Compose UI展示
     */
    fun formatToCardData(idea: CreativeIdea): SchemeCardData {
        return SchemeCardData(
            basicInfo = listOf(
                "产品类型" to idea.productType.displayName,
                "适用年龄段" to idea.ageGroup.displayName,
                "设计主题" to idea.theme
            ),
            coreFeatures = idea.features,
            recommendedMaterials = idea.materials,
            complianceParams = idea.complianceParameters?.let { params ->
                ComplianceParamsData(
                    standards = formatStandards(idea.standardsReference),
                    dummyType = params.dummyType.displayName,
                    thresholds = listOf(
                        "HIC极限值" to "≤${params.hicLimit}",
                        "胸部加速度" to "≤${params.chestAccelerationLimit}g",
                        "颈部张力" to "≤${params.neckTensionLimit}N",
                        "头部位移" to "≤${params.headExcursionLimit}mm"
                    ).let { thresholds ->
                        if (params.neckCompressionLimit > 0) {
                            thresholds + listOf("颈部压缩" to "≤${params.neckCompressionLimit}N")
                        } else {
                            thresholds
                        }
                    }
                )
            },
            safetyNotes = idea.safetyNotes
        )
    }

    /**
     * 根据身高范围生成格式化方案
     */
    fun formatCreativeIdeaByHeight(
        minHeightCm: Int,
        maxHeightCm: Int,
        idea: CreativeIdea
    ): String {
        val baseContent = formatCreativeIdea(idea)

        // 添加身高映射信息
        val heightMapping = buildString {
            appendLine()
            appendLine("【身高映射说明】")
            appendLine("- 输入身高范围：${minHeightCm}-${maxHeightCm}cm")
            
            // 获取映射的假人类型
            val intervals = HeightAgeMappingConfig.getIntervalsByHeightRange(
                minHeightCm.toDouble(),
                maxHeightCm.toDouble()
            )
            
            if (intervals.isNotEmpty()) {
                val uniqueDummies = intervals.map { it.dummyType }.distinct()
                appendLine("- 涉及假人类型：${uniqueDummies.joinToString(", ") { it.displayName }}")
                
                val ageRanges = intervals.map { it.ageRange }.distinct()
                appendLine("- 对应年龄范围：${ageRanges.joinToString(", ")}")
            }
        }

        return baseContent + heightMapping
    }
}

/**
 * 方案卡片数据（用于UI展示）
 */
data class SchemeCardData(
    val basicInfo: List<Pair<String, String>>,
    val coreFeatures: List<String>,
    val recommendedMaterials: List<String>,
    val complianceParams: ComplianceParamsData?,
    val safetyNotes: List<String>
)

/**
 * 合规参数数据（用于UI展示）
 */
data class ComplianceParamsData(
    val standards: String,
    val dummyType: String,
    val thresholds: List<Pair<String, String>>
)

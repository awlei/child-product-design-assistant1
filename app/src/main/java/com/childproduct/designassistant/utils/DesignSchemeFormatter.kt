package com.childproduct.designassistant.utils

import com.childproduct.designassistant.model.*
import com.childproduct.designassistant.model.ProductType

/**
 * 设计方案格式化工具
 * 将CreativeIdea对象转换为符合输出模板规范的结构化文本格式
 *
 * 模板版本：1.0
 * 字符编码：UTF-8
 * 输出格式：结构化列表
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
            appendLine("- 设计主题：${simplifyTheme(idea.theme)}")
            appendLine()

            // 【核心设计特点】
            appendLine("【核心设计特点】")
            val structuredFeatures = structureFeatures(idea.features, idea.productType)
            structuredFeatures.forEach { feature ->
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
                appendLine("- 对应标准：${formatStandards(idea.standardsReference)}")
                appendLine("- 适配假人：${params.dummyType.displayName}")
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
            val structuredSafetyNotes = structureSafetyNotes(idea.safetyNotes)
            structuredSafetyNotes.forEach { note ->
                appendLine("- $note")
            }
        }
    }

    /**
     * 简化设计主题（去除冗余修饰）
     */
    private fun simplifyTheme(theme: String): String {
        // 移除重复的词汇，如"儿童产品"中的"儿童"
        val redundantWords = listOf("儿童", "婴幼儿", "婴幼儿专用")
        var simplified = theme
        redundantWords.forEach { word ->
            if (simplified.contains(word) && simplified.count { it == word[0] } > 1) {
                // 保留第一次出现的
                val firstIndex = simplified.indexOf(word)
                simplified = simplified.substring(0, firstIndex + word.length) +
                    simplified.substring(firstIndex + word.length).replace(word, "")
            }
        }
        return simplified
    }

    /**
     * 结构化核心设计特点
     * 按照易安装性、舒适性、材质环保、安全性的顺序组织
     */
    private fun structureFeatures(
        features: List<String>,
        productType: ProductType
    ): List<String> {
        // 根据关键词分类特征
        val installFeature = features.find { it.contains("安装") || it.contains("连接") || it.contains("便携") }
            ?: getInstallFeature(productType)

        val comfortFeature = features.find { it.contains("舒适") || it.contains("靠背") || it.contains("填充") }
            ?: getComfortFeature(productType)

        val materialFeature = features.find { it.contains("材质") || it.contains("材料") || it.contains("环保") }
            ?: getMaterialFeature(productType)

        val safetyFeature = features.find { it.contains("安全") || it.contains("标准") || it.contains("符合") }
            ?: getSafetyFeature(productType)

        return listOfNotNull(
            installFeature?.let { "易安装性：${formatFeature(it)}" },
            comfortFeature?.let { "舒适性：${formatFeature(it)}" },
            materialFeature?.let { "材质环保：${formatFeature(it)}" },
            safetyFeature?.let { "安全性：${formatFeature(it)}" }
        )
    }

    /**
     * 格式化特征描述
     */
    private fun formatFeature(feature: String): String {
        // 移除冒号及前面的分类词
        val colonIndex = feature.indexOf("：")
        return if (colonIndex != -1) {
            feature.substring(colonIndex + 1).trim()
        } else {
            feature
        }
    }

    /**
     * 获取易安装性特征（默认值）
     */
    private fun getInstallFeature(productType: ProductType): String {
        return when (productType) {
            ProductType.SAFETY_SEAT -> "支持ISOFIX连接，实现快速安装"
            ProductType.STROLLER -> "支持一键折叠，收纳便捷"
            ProductType.HIGH_CHAIR -> "支持快速拆装，便于清洁"
            ProductType.CRIB -> "结构简单，易于组装"
        }
    }

    /**
     * 获取舒适性特征（默认值）
     */
    private fun getComfortFeature(productType: ProductType): String {
        return when (productType) {
            ProductType.SAFETY_SEAT -> "采用高回弹海绵填充，贴合儿童体型"
            ProductType.STROLLER -> "配备悬挂避震系统，乘坐平稳舒适"
            ProductType.HIGH_CHAIR -> "座椅角度多档可调，适应不同坐姿需求"
            ProductType.CRIB -> "人体工学设计，使用舒适"
        }
    }

    /**
     * 获取材质环保特征（默认值）
     */
    private fun getMaterialFeature(productType: ProductType): String {
        return when (productType) {
            ProductType.SAFETY_SEAT -> "选用食品级安全材料，无甲醛/重金属"
            ProductType.STROLLER -> "选用无毒环保材料，符合欧盟安全标准"
            ProductType.HIGH_CHAIR -> "食品级材质，可直接接触食品"
            ProductType.CRIB -> "环保材质，可回收利用"
        }
    }

    /**
     * 获取安全性特征（默认值）
     */
    private fun getSafetyFeature(productType: ProductType): String {
        return when (productType) {
            ProductType.SAFETY_SEAT -> "符合UN R129 i-Size+GB 27887-2024安全要求"
            ProductType.STROLLER -> "符合EN 1888婴儿推车安全标准"
            ProductType.HIGH_CHAIR -> "符合GB 22793-2008儿童高椅安全标准"
            ProductType.CRIB -> "符合ISO 8124儿童用品安全要求"
        }
    }

    /**
     * 结构化安全注意事项
     * 按照防吞咽、材质安全、边缘处理的顺序组织
     */
    private fun structureSafetyNotes(notes: List<String>): List<String> {
        val swallowRiskNote = notes.find { it.contains("吞咽") || it.contains("尺寸") }
            ?: "所有部件尺寸大于3.5cm，避免儿童吞咽风险"

        val materialSafetyNote = notes.find { it.contains("甲醛") || it.contains("重金属") || it.contains("食品级") }
            ?: "使用食品级安全材料，无甲醛、无重金属残留"

        val edgeSafetyNote = notes.find { it.contains("边缘") || it.contains("圆角") || it.contains("尖锐") }
            ?: "产品边缘做圆角处理（半径≥2mm），无尖锐结构"

        return listOf(swallowRiskNote, materialSafetyNote, edgeSafetyNote)
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
                    append("UN R129 i-Size + GB 27887-2024")
                    if (standardsRef.mainStandard.contains("FMVSS 213")) {
                        append(" + FMVSS 213")
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
     * 按照模板规范输出，包含精准的年龄段和假人信息
     */
    fun formatCreativeIdeaByHeight(
        minHeightCm: Int,
        maxHeightCm: Int,
        idea: CreativeIdea
    ): String {
        return buildString {
            // 【基本信息】
            appendLine("【基本信息】")
            appendLine("- 产品类型：${idea.productType.displayName}")
            
            // 获取身高映射的年龄段信息
            val intervals = HeightAgeMappingConfig.getIntervalsByHeightRange(
                minHeightCm.toDouble(),
                maxHeightCm.toDouble()
            )

            // 格式化年龄段：0-12岁（40-150cm）
            val ageRange = if (intervals.isNotEmpty()) {
                val ageRanges = intervals.map { it.ageRange }.distinct()
                ageRanges.joinToString("、")
            } else {
                idea.ageGroup.displayName
            }
            appendLine("- 适用年龄段：$ageRange（${minHeightCm}-${maxHeightCm}cm）")
            appendLine("- 设计主题：${simplifyTheme(idea.theme)}")
            appendLine()

            // 【核心设计特点】
            appendLine("【核心设计特点】")
            val structuredFeatures = structureFeatures(idea.features, idea.productType)
            structuredFeatures.forEach { feature ->
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
                appendLine("- 对应标准：${formatStandards(idea.standardsReference)}")
                
                // 格式化适配假人：Q0-Q10（0-12岁，40-150cm）
                val dummyInfo = if (intervals.isNotEmpty()) {
                    val uniqueDummies = intervals.map { it.dummyType }.distinct()
                    val dummyStr = uniqueDummies.joinToString("-") { it.displayName }
                    "$dummyStr（$ageRange，${minHeightCm}-${maxHeightCm}cm）"
                } else {
                    "${params.dummyType.displayName}（$ageRange）"
                }
                appendLine("- 适配假人：$dummyInfo")
                
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
            val structuredSafetyNotes = structureSafetyNotes(idea.safetyNotes)
            structuredSafetyNotes.forEach { note ->
                appendLine("- $note")
            }
        }
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

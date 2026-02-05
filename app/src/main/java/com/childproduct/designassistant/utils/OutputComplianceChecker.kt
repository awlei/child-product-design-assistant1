package com.childproduct.designassistant.utils

import com.childproduct.designassistant.model.ChildProductDesignScheme
import com.childproduct.designassistant.model.TestMatrixItem
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

/**
 * 核心优化工具类：清理乱码+修正参数+结构化输出
 *
 * 解决的问题：
 * 1. 参数匹配错误（违反UN R129/GB 27887-2024标准）
 * 2. 输出内容含大量乱码/冗余代码字段
 * 3. 信息重复堆砌
 * 4. 界面信息自相矛盾
 * 5. 输出排版无结构化
 */
object OutputComplianceChecker {

    // ========== 标准映射表 ==========

    /**
     * 标准映射表：身高→(正确年龄段, 正确假人类型)
     * 符合 UN R129 / GB 27887-2024 标准
     */
    private val STANDARD_MAPPING = mapOf(
        "40-150" to Pair("0-12岁", "Q0-Q10（全年龄段假人）"),
        "40-150cm" to Pair("0-12岁", "Q0-Q10（全年龄段假人）"),
        "87-105" to Pair("3-4岁", "Q3假人"),
        "87-105cm" to Pair("3-4岁", "Q3假人"),
        "125-150" to Pair("6-12岁", "Q10假人"),
        "125-150cm" to Pair("6-12岁", "Q10假人"),
        "40-60" to Pair("0-1岁", "Q0假人"),
        "40-60cm" to Pair("0-1岁", "Q0假人"),
        "60-75" to Pair("1-2岁", "Q1假人"),
        "60-75cm" to Pair("1-2岁", "Q1假人"),
        "75-87" to Pair("2-3岁", "Q1.5假人"),
        "75-87cm" to Pair("2-3岁", "Q1.5假人"),
        "105-125" to Pair("4-6岁", "Q6假人"),
        "105-125cm" to Pair("4-6岁", "Q6假人")
    )

    /**
     * 固定安全阈值（按标准定义，无重复）
     */
    private val STANDARD_SAFETY_THRESHOLDS = mapOf(
        "HIC极限值" to "≤390（Q0-Q1.5）/≤1000（Q3-Q10）",
        "胸部加速度" to "≤55g（Q0-Q1.5）/≤60g（Q3-Q10）",
        "颈部张力极限" to "≤1800N（Q3-Q10）",
        "头部位移极限" to "≤550mm（全假人）",
        "阻燃性能" to "符合FMVSS 302（燃烧速度≤4英寸/分钟）"
    ).toImmutableMap()

    /**
     * 标准合规标准（去重后）
     */
    private val STANDARD_COMPLIANCE_STANDARDS = listOf(
        "ECE R129 / GB 27887-2024",
        "FMVSS 213（美标）"
    )

    // ========== 核心方法 ==========

    /**
     * 核心入口：输入原始杂乱内容，输出标准化结构化方案
     *
     * @param rawContent 原始乱码/冗余内容（界面生成的原始输出）
     * @param inputHeight 输入的身高范围（如：40-150cm）
     * @param productType 产品类型（默认：儿童安全座椅）
     * @return 无乱码/无重复/参数正确的结构化字符串
     */
    fun optimizeScheme(
        rawContent: String,
        inputHeight: String,
        productType: String = "儿童安全座椅"
    ): String {
        val cleanedContent = cleanRawContent(rawContent)  // 步骤1：清理乱码/冗余
        val standardScheme = parseToStandardScheme(
            cleanedContent,
            inputHeight,
            productType
        )  // 步骤2：修正参数
        return generateStructuredOutput(standardScheme)  // 步骤3：结构化输出
    }

    /**
     * 兼容旧接口的方法（向后兼容）
     */
    fun checkAndFixOutput(
        rawOutput: String,
        inputHeightRange: String = "40-150",
        productType: String = "儿童安全座椅"
    ): String {
        return optimizeScheme(rawOutput, inputHeightRange, productType)
    }

    // ========== 私有方法 ==========

    /**
     * 步骤1：清理原始内容（去乱码、去代码字段、去重复）
     */
    private fun cleanRawContent(rawContent: String): String {
        var cleaned = rawContent

        // 1.1 移除所有代码式对象字段（增强版，支持嵌套和多行）
        try {
            // 移除 CreativeIdea 和 Creativeldea（处理拼写错误）
            cleaned = Regex("""CreativeIdea\([^)]*\)|Creativeldea\([^)]*\)""", RegexOption.DOT_MATCHES_ALL)
                .replace(cleaned, "")

            // 移除 complianceParameters=ComplianceParameters(...) 整个对象
            cleaned = Regex("""complianceParameters=ComplianceParameters\([^)]*\)""", RegexOption.DOT_MATCHES_ALL)
                .replace(cleaned, "")

            // 移除 standardsReference=StandardsReference(...) 整个对象
            cleaned = Regex("""standardsReference=StandardsReference\([^)]*\)""", RegexOption.DOT_MATCHES_ALL)
                .replace(cleaned, "")

            // 移除 materialSpecs=MaterialSpecs(...) 整个对象
            cleaned = Regex("""materialSpecs=MaterialSpecs\([^)]*\)""", RegexOption.DOT_MATCHES_ALL)
                .replace(cleaned, "")

            // 移除 dummyTypes=[...] 数组
            cleaned = Regex("""dummyTypes=\[[^\]]*\]""", RegexOption.DOT_MATCHES_ALL)
                .replace(cleaned, "")

            // 移除 complianceRequirements=[...] 数组
            cleaned = Regex("""complianceRequirements=\[[^\]]*\]""", RegexOption.DOT_MATCHES_ALL)
                .replace(cleaned, "")

            // 移除 additionalSpecs=[...] 数组
            cleaned = Regex("""additionalSpecs=\[[^\]]*\]""", RegexOption.DOT_MATCHES_ALL)
                .replace(cleaned, "")

            // 移除其他单行键值对字段
            cleaned = Regex("""\b(?:id|title|description|ageGroup|productType|theme|dummyType|hicLimit|chestAccelerationLimit|neckTensionLimit|neckCompressionLimit|headExcursionLimit|kneeExcursionLimit|chestDeflectionLimit|flameRetardantFabric|isoFixComponents|impactAbsorber)=[\w\s#\[\],\(\).:;\\\-≤>=°]+""")
                .replace(cleaned, "")

            // 移除 features=[...] 数组
            cleaned = Regex("""features=\[[^\]]*\]""", RegexOption.DOT_MATCHES_ALL)
                .replace(cleaned, "")

            // 移除 materials=[...] 数组
            cleaned = Regex("""materials=\[[^\]]*\]""", RegexOption.DOT_MATCHES_ALL)
                .replace(cleaned, "")

            // 移除 colorPalette=[...] 数组
            cleaned = Regex("""colorPalette=\[[^\]]*\]""", RegexOption.DOT_MATCHES_ALL)
                .replace(cleaned, "")

            // 移除 safetyNotes=[...] 数组
            cleaned = Regex("""safetyNotes=\[[^\]]*\]""", RegexOption.DOT_MATCHES_ALL)
                .replace(cleaned, "")

            // 1.2 移除UUID
            cleaned = Regex("""[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}""")
                .replace(cleaned, "")

            // 1.3 过滤乱码字符（更严格，仅保留中文、英文、数字、常用标点）
            cleaned = Regex("""[^\u4e00-\u9fa5a-zA-Z0-9\s\-+≤>=（）【】：;,.，。！？、·/]""")
                .replace(cleaned, "")

            // 1.4 移除空白行和多余空格
            cleaned = cleaned.replace(Regex("""\s+"""), " ").trim()

            // 1.5 移除重复的短语（如"HIC≤1000 HIC≤1000"）
            val words = cleaned.split(" ")
            val uniqueWords = mutableListOf<String>()
            words.forEach { word ->
                if (word.isNotBlank() && !uniqueWords.contains(word)) {
                    uniqueWords.add(word)
                }
            }
            cleaned = uniqueWords.joinToString(" ")

        } catch (e: Exception) {
            // 如果清理过程中出现异常，返回清理过的基本版本
            cleaned = rawContent
                .replace(Regex("""[^\u4e00-\u9fa5a-zA-Z0-9\s\-+≤>=（）【】：;,.，。！？、·/]"""), "")
                .replace(Regex("""\s+"""), " ")
                .trim()
        }

        return cleaned
    }

    /**
     * 步骤2：解析为标准化模型（修正身高-年龄段-假人匹配错误）
     */
    private fun parseToStandardScheme(
        cleanedContent: String,
        inputHeight: String,
        productType: String
    ): ChildProductDesignScheme {
        // 提取核心信息（兼容模糊匹配）
        val designTheme = extractDesignTheme(cleanedContent)

        // 核心特点（去重）
        val coreFeatures = extractCoreFeatures(cleanedContent).distinct()

        // 推荐材料（去重）
        val recommendMaterials = extractRecommendMaterials(cleanedContent).distinct()

        // 合规标准（去重）
        val complianceStandards = extractComplianceStandards(cleanedContent).distinct()

        // 安全提示（去重）
        val safetyNotes = extractSafetyNotes(cleanedContent).distinct()

        // 强制修正身高对应的年龄段和假人（解决核心匹配错误）
        val (correctAge, correctDummy) = STANDARD_MAPPING[inputHeight]
            ?: STANDARD_MAPPING["40-150cm"]!!

        return ChildProductDesignScheme(
            productType = productType,
            heightRange = inputHeight,
            ageRange = correctAge,
            designTheme = designTheme,
            installMethodDesc = "ISOFIX快速连接（兼容多种安装方式）",
            coreFeatures = coreFeatures.toImmutableList(),
            recommendMaterials = recommendMaterials.toImmutableList(),
            complianceStandards = complianceStandards.toImmutableList(),
            dummyType = correctDummy,
            safetyThresholds = STANDARD_SAFETY_THRESHOLDS,
            testMatrix = emptyList<TestMatrixItem>().toImmutableList(),  // 旧版本兼容，实际使用 SchemeOptimizer
            safetyNotes = safetyNotes.toImmutableList()
        )
    }

    /**
     * 提取设计主题
     */
    private fun extractDesignTheme(content: String): String {
        val themePattern = Regex("设计主题[:：](.+?)|创意主题[:：](.+?)|theme[:：](.+?)")
        return themePattern.find(content)?.groupValues?.let {
            it[1].ifEmpty { it[2].ifEmpty { it[3] } }
        } ?: "创意儿童安全座椅"
    }

    /**
     * 提取核心特点
     */
    private fun extractCoreFeatures(content: String): List<String> {
        val features = mutableListOf<String>()

        if (content.contains("易安装性")) {
            features.add("易安装性（ISOFIX快速连接）")
        }
        if (content.contains("安全性")) {
            features.add("安全性（符合ECE R129/GB 27887-2024标准）")
        }
        if (content.contains("舒适性")) {
            features.add("舒适性（高回弹海绵填充）")
        }
        if (content.contains("材质环保")) {
            features.add("材质环保（食品级PP塑料）")
        }

        return features
    }

    /**
     * 提取推荐材料
     */
    private fun extractRecommendMaterials(content: String): List<String> {
        val materials = mutableListOf<String>()

        if (content.contains("食品级PP塑料")) {
            materials.add("食品级PP塑料（主体框架）")
        }
        if (content.contains("高回弹海绵")) {
            materials.add("高回弹海绵（填充层）")
        }
        if (content.contains("安全带织带")) {
            materials.add("安全带织带（抗拉强度≥2000N）")
        }
        if (content.contains("铝合金支架")) {
            materials.add("铝合金支架（支撑结构）")
        }

        return materials
    }

    /**
     * 提取合规标准
     */
    private fun extractComplianceStandards(content: String): List<String> {
        val standards = mutableListOf<String>()

        if (content.contains("ECE R129") || content.contains("UN R129")) {
            standards.add("ECE R129 / GB 27887-2024")
        }
        if (content.contains("FMVSS") || content.contains("美标")) {
            standards.add("FMVSS 213（美标）")
        }

        return if (standards.isEmpty()) STANDARD_COMPLIANCE_STANDARDS else standards
    }

    /**
     * 提取安全注意事项
     */
    private fun extractSafetyNotes(content: String): List<String> {
        val notes = mutableListOf<String>()

        if (content.contains("细小零件") || content.contains("吞咽")) {
            notes.add("避免细小零件脱落，防止儿童吞咽风险")
        }
        if (content.contains("EN71")) {
            notes.add("材料需通过欧盟EN71安全认证")
        }
        if (content.contains("结构稳固") || content.contains("倾倒")) {
            notes.add("结构稳固，安装后无倾倒风险")
        }

        return notes
    }

    /**
     * 步骤3：生成结构化输出（按照指定格式）
     */
    private fun generateStructuredOutput(scheme: ChildProductDesignScheme): String {
        return buildString {
            // 1. 设计方案模块
            appendLine("【设计方案】")
            appendLine("产品类型：${scheme.productType}")
            appendLine("身高范围：${scheme.heightRange}")
            appendLine("年龄段：${scheme.ageRange}")
            appendLine("设计主题：${scheme.designTheme}")
            appendLine()

            // 2. 安装方式模块
            appendLine("【安装方式】")
            appendLine(scheme.installMethodDesc)
            appendLine()

            // 3. 核心特点模块
            appendLine("【核心特点】")
            scheme.coreFeatures.forEach { feature -> appendLine("- $feature") }
            appendLine()

            // 4. 推荐材料模块
            appendLine("【推荐材料】")
            scheme.recommendMaterials.forEach { material -> appendLine("- $material") }
            appendLine()

            // 5. 合规标准模块
            appendLine("【合规标准】")
            scheme.complianceStandards.forEach { standard -> appendLine("- $standard") }
            appendLine()

            // 6. 适配假人模块
            appendLine("【适配假人】")
            appendLine(scheme.dummyType)
            appendLine()

            // 7. 安全阈值模块
            appendLine("【安全阈值】")
            scheme.safetyThresholds.forEach { (key, value) -> appendLine("- $key：$value") }
            appendLine()

            // 8. 安全注意事项模块
            appendLine("【安全注意事项】")
            scheme.safetyNotes.forEach { note -> appendLine("- $note") }
        }
    }

    // ========== 新增：标准混用检查方法（解决标准混用问题）==========

    /**
     * 校验输出内容是否与选中的标准一致
     *
     * @param content 输出内容
     * @param selectedStandard 选中的标准类型："ECE_R129", "FMVSS_213", "GB_27887_2024"
     * @return true表示合规，false表示不合规
     */
    fun checkStandardCompliance(content: String, selectedStandard: String): Boolean {
        // 规则1：内容中是否包含选中标准的标识
        val standardKeywords = when (selectedStandard) {
            "ECE_R129" -> listOf("ECE R129", "i-Size", "UN R129")
            "FMVSS_213" -> listOf("FMVSS 213", "FMVSS", "NHTSA")
            "GB_27887_2024" -> listOf("GB 27887", "GB 27887-2024", "GB")
            else -> emptyList()
        }

        val containsStandardTag = standardKeywords.any { content.contains(it, ignoreCase = true) }

        // 规则2：内容中是否包含其他标准的标识（混用检测）
        val otherStandards = when (selectedStandard) {
            "ECE_R129" -> listOf("FMVSS", "NHTSA")
            "FMVSS_213" -> listOf("ECE R129", "i-Size", "UN R129")
            "GB_27887_2024" -> listOf("ECE R129", "i-Size", "FMVSS")
            else -> emptyList()
        }

        val containsOtherStandard = otherStandards.any {
            content.contains(it, ignoreCase = true)
        }

        // 规则3：检查假人类型是否匹配标准
        val dummyMismatch = checkDummyMismatch(content, selectedStandard)

        return containsStandardTag && !containsOtherStandard && !dummyMismatch
    }

    /**
     * 检查假人类型是否与标准匹配
     */
    private fun checkDummyMismatch(content: String, standard: String): Boolean {
        return when (standard) {
            "ECE_R129" -> {
                // ECE R129应该使用Q系列假人
                content.contains("HIII", ignoreCase = true) ||
                content.contains("Hybrid", ignoreCase = true) ||
                content.contains("CRABI", ignoreCase = true)
            }
            "FMVSS_213" -> {
                // FMVSS 213应该使用HIII系列假人（侧碰可使用Q3s）
                content.contains("Q0", ignoreCase = true) ||
                content.contains("Q1", ignoreCase = true) ||
                content.contains("Q1.5", ignoreCase = true) ||
                content.contains("Q6", ignoreCase = true) ||
                content.contains("Q10", ignoreCase = true)
            }
            "GB_27887_2024" -> {
                // GB 27887应该使用Q系列假人
                content.contains("HIII", ignoreCase = true) ||
                content.contains("Hybrid", ignoreCase = true) ||
                content.contains("CRABI", ignoreCase = true)
            }
            else -> false
        }
    }

    /**
     * 强制校验输出合规性，若不合规则抛出异常
     *
     * @param content 输出内容
     * @param selectedStandard 选中的标准类型
     * @throws IllegalStateException 当内容与标准不匹配时抛出
     */
    fun enforceStandardCompliance(content: String, selectedStandard: String) {
        if (!checkStandardCompliance(content, selectedStandard)) {
            val standardName = when (selectedStandard) {
                "ECE_R129" -> "ECE R129 (i-Size)"
                "FMVSS_213" -> "FMVSS 213"
                "GB_27887_2024" -> "GB 27887-2024"
                else -> selectedStandard
            }

            val errorDetails = buildString {
                appendLine("❌ 输出内容与选中标准不匹配！")
                appendLine("选中的标准：$standardName")
                appendLine("问题分析：")

                // 检查是否缺少标准标识
                val standardKeywords = when (selectedStandard) {
                    "ECE_R129" -> listOf("ECE R129", "i-Size", "UN R129")
                    "FMVSS_213" -> listOf("FMVSS 213", "FMVSS", "NHTSA")
                    "GB_27887_2024" -> listOf("GB 27887", "GB 27887-2024", "GB")
                    else -> emptyList()
                }

                val hasStandardTag = standardKeywords.any { content.contains(it, ignoreCase = true) }
                if (!hasStandardTag) {
                    appendLine("❓ 内容中缺少标准标识：${standardKeywords.joinToString(" / ")}")
                }

                // 检查是否混用其他标准
                val otherStandards = when (selectedStandard) {
                    "ECE_R129" -> listOf("FMVSS", "NHTSA")
                    "FMVSS_213" -> listOf("ECE R129", "i-Size", "UN R129")
                    "GB_27887_2024" -> listOf("ECE R129", "i-Size", "FMVSS")
                    else -> emptyList()
                }

                val foundOtherStandards = otherStandards.filter {
                    content.contains(it, ignoreCase = true)
                }
                if (foundOtherStandards.isNotEmpty()) {
                    appendLine("❓ 内容中包含其他标准标识：${foundOtherStandards.joinToString(" / ")}")
                }

                // 检查假人类型是否匹配
                if (checkDummyMismatch(content, selectedStandard)) {
                    appendLine("❓ 假人类型与标准不匹配")
                }
            }

            throw IllegalStateException(errorDetails.toString())
        }
    }

    /**
     * 生成标准合规性报告
     *
     * @param content 输出内容
     * @param selectedStandard 选中的标准类型
     * @return 合规性报告文本
     */
    fun generateComplianceReport(content: String, selectedStandard: String): String {
        val standardName = when (selectedStandard) {
            "ECE_R129" -> "ECE R129 (i-Size)"
            "FMVSS_213" -> "FMVSS 213"
            "GB_27887_2024" -> "GB 27887-2024"
            else -> selectedStandard
        }

        val report = buildString {
            appendLine("📋 标准合规性报告")
            appendLine("=")
            appendLine("选中标准：$standardName")
            appendLine()

            appendLine("✅ 合规检查：")
            appendLine("- [${if (checkStandardCompliance(content, selectedStandard)) "✓" else "✗"}] 整体合规")

            // 标准标识检查
            val standardKeywords = when (selectedStandard) {
                "ECE_R129" -> listOf("ECE R129", "i-Size", "UN R129")
                "FMVSS_213" -> listOf("FMVSS 213", "FMVSS", "NHTSA")
                "GB_27887_2024" -> listOf("GB 27887", "GB 27887-2024", "GB")
                else -> emptyList()
            }

            val hasStandardTag = standardKeywords.any { content.contains(it, ignoreCase = true) }
            appendLine("- [${if (hasStandardTag) "✓" else "✗"}] 包含标准标识")

            // 混用检查
            val otherStandards = when (selectedStandard) {
                "ECE_R129" -> listOf("FMVSS", "NHTSA")
                "FMVSS_213" -> listOf("ECE R129", "i-Size", "UN R129")
                "GB_27887_2024" -> listOf("ECE R129", "i-Size", "FMVSS")
                else -> emptyList()
            }

            val foundOtherStandards = otherStandards.filter {
                content.contains(it, ignoreCase = true)
            }
            appendLine("- [${if (foundOtherStandards.isEmpty()) "✓" else "✗"}] 无其他标准混用")

            // 假人类型检查
            val hasDummyMismatch = checkDummyMismatch(content, selectedStandard)
            appendLine("- [${if (!hasDummyMismatch) "✓" else "✗"}] 假人类型匹配")

            appendLine()

            if (!hasStandardTag) {
                appendLine("⚠️ 建议：在输出中添加标准标识：${standardKeywords.joinToString(" / ")}")
            }

            if (foundOtherStandards.isNotEmpty()) {
                appendLine("⚠️ 发现混用标准：${foundOtherStandards.joinToString(" / ")}")
                appendLine("   建议：删除或替换为 $standardName")
            }

            if (hasDummyMismatch) {
                appendLine("⚠️ 假人类型与标准不匹配")
                appendLine("   建议：使用符合 $standardName 的假人类型")
            }

            if (checkStandardCompliance(content, selectedStandard)) {
                appendLine()
                appendLine("✅ 合规：输出内容完全符合 $standardName 标准")
            }
        }

        return report
    }
}

// ====================== 调用示例 ======================
/**
 * 在APK的方案生成模块中调用示例
 */
fun applyOptimizationExample() {
    // 模拟界面生成的原始杂乱内容（含乱码/重复/参数错误）
    val rawBadContent = """
        CreativeIdea(id=6ade8c48-4e33-46f5-a5c7-241347746403, title=6-9岁儿童安全座椅 - 拼图游戏, description=专为6-9岁儿童设计的儿童安全座椅，融入拼图游戏设计理念。主要特点包括：易安装性、安全性、舒适性、材质环保。符合UN R129 i-Size儿童标准（Q3假人），HIC极限值≤1000，满足FMVSS 302燃烧性能要求，通过ISOFIX连接实现快速安装。
        ageGroup=PRESCHOOL, productType=CHILD_SAFETY_SEAT, theme=拼图游戏, features=[易安装性, 安全性, 舒适性, 材质环保], materials=[食品级PP塑料, 高回弹海绵, 安全带织带, 铝合金支架], colorPalette=[#FFA500, #00CED1, #FF69B4, #9370DB], safetyNotes=[避免细小零件脱落风险, 材料需通过欧盟EN71安全认证, 结构稳固，不易倒塌]
        complianceParameters=ComplianceParameters(dummyType=Q3, hicLimit=1000, chestAccelerationLimit=60, neckTensionLimit=2000, neckCompressionLimit=2500, headExcursionLimit=550, kneeExcursionLimit=650, chestDeflectionLimit=52)
        standardsReference=StandardsReference(mainStandard=ECE R129 + GB 27887-2024 + FMVSS 213, keyClauses=[ECE R129 §5.2: 假人分类（Q0-Q10）])
        HIC极限值≤1000 HIC极限值≤1000 符合ECE R129 适配假人：Q3
    """.trimIndent()

    // 调用优化工具（输入身高40-150cm，修正所有问题）
    val optimizedResult = OutputComplianceChecker.optimizeScheme(
        rawContent = rawBadContent,
        inputHeight = "40-150cm",
        productType = "儿童安全座椅"
    )

    // 将optimizedResult展示在APK界面，替代原有杂乱输出
    println(optimizedResult)
}

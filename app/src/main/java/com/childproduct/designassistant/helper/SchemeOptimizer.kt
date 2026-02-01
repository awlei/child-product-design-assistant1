package com.childproduct.designassistant.helper

import com.childproduct.designassistant.model.ChildProductDesignScheme
import com.childproduct.designassistant.model.InstallMethod
import com.childproduct.designassistant.model.TestMatrixItem

/**
 * 终极修复：针对"12岁以上儿童安全座椅"场景的零乱码方案
 *
 * 核心优化：
 * - 彻底清理代码字段（CreativeIdea、title=、description=等）
 * - 强制修正年龄段（按标准映射，40-150cm → 0-12岁）
 * - 补全不完整描述（基于主题关键词）
 * - 白名单过滤，阻断乱码来源
 */
object SchemeOptimizer {
    data class UserInput(
        val productType: String,
        val heightRange: String,
        val installMethod: InstallMethod,
        val themeKeyword: String,
        val rawOutput: String = "" // 接收原始输出（用于提取主题，忽略乱码）
    )

    /**
     * 核心入口：彻底清理+修正+补全
     */
    fun generateOptimizedScheme(userInput: UserInput): ChildProductDesignScheme {
        // 步骤1：彻底清理原始输出中的代码字段和乱码
        val cleanedRaw = cleanCodeAndGarbled(userInput.rawOutput)
        // 步骤2：提取并修正主题（从清理后的原始输出中获取）
        val safeTheme = extractTheme(userInput.themeKeyword, cleanedRaw)
        // 步骤3：修正年龄段（强制符合标准）
        val (correctAge, ageHint) = SceneAdapter.correctAgeRange(userInput.heightRange, cleanedRaw)
        // 步骤4：补全描述
        val completeDesc = SceneAdapter.completeDescription(safeTheme) + ageHint

        // 步骤5：固定规范数据（无拼接，零乱码）
        val coreFeatures = listOf(
            "材质环保：食品级PP塑料，无甲醛/重金属，符合EN 71-3标准",
            "易安装性：${userInput.installMethod.description}，安装耗时≤5分钟",
            "舒适性：高回弹海绵填充，头枕多档位调节，适配不同身高儿童"
        ) + if (safeTheme.contains("社交元素")) {
            listOf("社交互动：支持个性化装饰定制、互动贴纸搭配（社交元素主题专属）")
        } else if (safeTheme.contains("个性化设计")) {
            listOf("个性化定制：支持颜色、图案自定义，适配不同儿童审美偏好")
        } else {
            listOf("安全性能：符合ECE R129/GB 27887-2024标准，通过动态测试")
        }

        val recommendMaterials = listOf(
            "主体框架：食品级PP塑料（耐温-30℃~80℃，抗冲击强度≥20kJ/m²）",
            "填充层：高回弹海绵（密度30kg/m³，压缩回弹率≥90%）",
            "约束部件：高强度安全带织带（断裂强度≥11000N，耐磨后强度保留率≥75%）"
        ) + if (safeTheme.contains("社交元素") || safeTheme.contains("个性化设计")) {
            listOf("装饰材料：可移除环保贴纸（无荧光剂，符合儿童安全标准）")
        } else {
            listOf()
        }

        val safetyThresholds = mapOf(
            "HIC极限值" to "≤390（Q0-Q1.5）/≤1000（Q3-Q10）",
            "胸部加速度" to "≤55g（Q0-Q1.5）/≤60g（Q3-Q10）",
            "颈部张力极限" to "≤1800N（Q0-Q1.5）/≤2000N（Q3+）",
            "颈部压缩极限" to "≤2500N",
            "头部位移极限" to "≤550mm（全假人）",
            "膝部位移极限" to "≤650mm（全假人）",
            "胸部位移极限" to "≤52mm（全假人）",
            "阻燃性能" to "符合FMVSS 302（燃烧速度≤4英寸/分钟）"
        )

        val testMatrix = listOf(
            TestMatrixItem(
                testItem = "HIC极限值",
                standardRequirement = "≤390（小龄）/≤1000（大龄）",
                applicableDummy = "Q0-Q10",
                unit = "-",
                standardSource = "ECE R129 §7.1.2"
            ),
            TestMatrixItem(
                testItem = "胸部加速度",
                standardRequirement = "≤55g（小龄）/≤60g（大龄）",
                applicableDummy = "Q0-Q10",
                unit = "g",
                standardSource = "ECE R129 §7.1.3"
            ),
            TestMatrixItem(
                testItem = "颈部张力极限",
                standardRequirement = "≤1800N（小龄）/≤2000N（大龄）",
                applicableDummy = "Q0-Q10",
                unit = "N",
                standardSource = "ECE R129 §7.1.4"
            ),
            TestMatrixItem(
                testItem = "颈部压缩极限",
                standardRequirement = "≤2500N",
                applicableDummy = "Q0-Q10",
                unit = "N",
                standardSource = "ECE R129 §7.1.4"
            ),
            TestMatrixItem(
                testItem = "头部位移极限",
                standardRequirement = "≤550mm",
                applicableDummy = "Q0-Q10",
                unit = "mm",
                standardSource = "ECE R129 §7.1.5"
            ),
            TestMatrixItem(
                testItem = "膝部位移极限",
                standardRequirement = "≤650mm",
                applicableDummy = "Q0-Q10",
                unit = "mm",
                standardSource = "ECE R129 §7.1.5"
            ),
            TestMatrixItem(
                testItem = "胸部位移极限",
                standardRequirement = "≤52mm",
                applicableDummy = "Q0-Q10",
                unit = "mm",
                standardSource = "ECE R129 §7.1.6"
            ),
            TestMatrixItem(
                testItem = "阻燃性能",
                standardRequirement = "燃烧速度≤4英寸/分钟",
                applicableDummy = "全年龄段",
                unit = "-",
                standardSource = "FMVSS 302"
            )
        )

        val safetyNotes = mutableListOf(
            "符合ECE R129/GB 27887-2024+FMVSS 213标准",
            "安装后需确认防旋转装置（Top-tether/支撑腿）锁止到位"
        )
        if (safeTheme.contains("社交元素") || safeTheme.contains("个性化设计")) {
            safetyNotes.add(0, "装饰贴纸需定期检查，避免脱落导致儿童吞咽风险")
            safetyNotes.add(1, "个性化定制时不可修改座椅结构和安全部件")
        }
        safetyNotes.add("定期检查安全带和卡扣，确保无磨损、无断裂")

        // 构建最终方案
        return ChildProductDesignScheme.builder(
            productType = userInput.productType,
            heightRange = userInput.heightRange
        )
            .ageRange(correctAge)
            .designTheme("儿童安全座椅 - $safeTheme")
            .installMethodDesc(userInput.installMethod.description)
            .coreFeatures(coreFeatures)
            .recommendMaterials(recommendMaterials)
            .complianceStandards(listOf("ECE R129 i-Size", "GB 27887-2024", "FMVSS 213"))
            .dummyType("Q0-Q10全假人")
            .safetyThresholds(safetyThresholds)
            .testMatrix(testMatrix)
            .safetyNotes(safetyNotes)
            .build()
    }

    /**
     * 彻底清理代码字段和乱码（针对该场景强化）
     */
    private fun cleanCodeAndGarbled(rawOutput: String): String {
        var cleaned = rawOutput

        // 1. 移除所有嵌套对象字段（增强版）
        // 移除 CreativeIdea 和 Creativeldea（处理拼写错误）
        cleaned = Regex("""CreativeIdea\([^)]+\)|Creativeldea\([^)]+\)""", RegexOption.DOT_MATCHES_ALL)
            .replace(cleaned, "")

        // 移除 complianceParameters=ComplianceParameters(...)
        cleaned = Regex("""complianceParameters=ComplianceParameters\([^)]+\)""", RegexOption.DOT_MATCHES_ALL)
            .replace(cleaned, "")

        // 移除 standardsReference=StandardsReference(...)
        cleaned = Regex("""standardsReference=StandardsReference\([^)]+\)""", RegexOption.DOT_MATCHES_ALL)
            .replace(cleaned, "")

        // 移除 materialSpecs=MaterialSpecs(...)
        cleaned = Regex("""materialSpecs=MaterialSpecs\([^)]+\)""", RegexOption.DOT_MATCHES_ALL)
            .replace(cleaned, "")

        // 移除所有数组字段
        cleaned = Regex("""\b(?:features|materials|colorPalette|safetyNotes|dummyTypes|complianceRequirements|additionalSpecs)=\[[^\]]+\]""", RegexOption.DOT_MATCHES_ALL)
            .replace(cleaned, "")

        // 移除所有单行键值对
        cleaned = Regex("""\b(?:id|title|description|ageGroup|productType|theme|dummyType|hicLimit|chestAccelerationLimit|neckTensionLimit|neckCompressionLimit|headExcursionLimit|kneeExcursionLimit|chestDeflectionLimit|flameRetardantFabric|isoFixComponents|impactAbsorber)=[^\s,)]+""")
            .replace(cleaned, "")

        // 2. 移除UUID
        cleaned = Regex("""[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}""")
            .replace(cleaned, "")

        // 3. 过滤乱码字符（更严格）
        cleaned = Regex("""[^\u4e00-\u9fa5a-zA-Z0-9\s\-+≤>=（）【】：；,.，。！？、·/℃%gNmm英寸第§]""")
            .replace(cleaned, "")

        // 4. 清理多余空格
        cleaned = cleaned.replace(Regex("""\s+"""), " ").trim()

        return cleaned
    }

    /**
     * 提取主题（优先用户输入，其次从原始输出提取）
     */
    private fun extractTheme(userTheme: String, cleanedRaw: String): String {
        val safeUserTheme = TextWhiteListFilter.filter(userTheme)
        return if (safeUserTheme.isNotBlank()) {
            safeUserTheme
        } else {
            // 从清理后的原始输出中提取主题关键词
            val themePattern = Regex("社交元素|个性化设计|拼图游戏|卡通图案|科技元素")
            themePattern.find(cleanedRaw)?.value ?: "通用款"
        }
    }

    /**
     * 格式化方案用于UI展示（模块+列表，极致可读）
     */
    fun formatSchemeForDisplay(scheme: ChildProductDesignScheme): String {
        return buildString {
            appendLine("🎯 设计方案")
            appendLine("├─ 产品类型：${scheme.productType}")
            appendLine("├─ 身高范围：${scheme.heightRange}")
            appendLine("├─ 适用年龄段：${scheme.ageRange}")
            appendLine("├─ 设计主题：${scheme.designTheme}")
            appendLine("├─ 适配假人：${scheme.dummyType}")
            appendLine("└─ 安装方式：${scheme.installMethodDesc}")
            appendLine()

            appendLine("📝 方案描述")
            val themeName = scheme.designTheme.split(" - ").getOrNull(1) ?: "通用款"
            val description = SceneAdapter.completeDescription(themeName)
            if (scheme.ageRange == "0-12岁" &&
                (themeName.contains("12岁以上") ||
                 themeName.contains("13岁") ||
                 themeName.contains("14岁"))) {
                appendLine("$description（注：按ECE R129/GB 27887-2024标准，40-150cm身高仅适配0-12岁，已自动修正）")
            } else {
                appendLine(description)
            }
            appendLine()

            appendLine("✨ 核心特点")
            scheme.coreFeatures.forEachIndexed { i, feat ->
                appendLine("${i + 1}. $feat")
            }
            appendLine()

            appendLine("📦 推荐材料")
            scheme.recommendMaterials.forEach { mat ->
                appendLine("- $mat")
            }
            appendLine()

            appendLine("🎨 颜色搭配")
            appendLine("- #000000（黑色）")
            appendLine("- #808080（灰色）")
            appendLine("- #FFFFFF（白色）")
            appendLine("- #FF1493（深粉色）")
            appendLine()

            appendLine("📋 合规参数")
            appendLine("├─ 遵循标准：${scheme.complianceStandards.joinToString(" + ")}")
            appendLine("├─ 安全阈值：")
            scheme.safetyThresholds.forEach { (key, value) ->
                appendLine("│  └─ $key：$value")
            }
            appendLine("└─ 核心条款：")
            scheme.safetyNotes.filter { it.startsWith("ECE") || it.startsWith("GB") }.forEach { clause ->
                appendLine("   └─ $clause")
            }
            appendLine()

            appendLine("⚠️  安全注意事项")
            scheme.safetyNotes.filterNot { it.startsWith("ECE") || it.startsWith("GB") }.forEach { note ->
                appendLine("- $note")
            }
        }
    }

    /**
     * 快速验证输入
     */
    fun validateInput(userInput: UserInput): Pair<Boolean, String> {
        // 产品类型非空校验
        if (userInput.productType.isBlank()) {
            return Pair(false, "产品类型不能为空")
        }

        // 身高范围校验
        if (userInput.heightRange.isBlank()) {
            return Pair(false, "身高范围不能为空")
        }

        // 主题关键词非空校验
        if (userInput.themeKeyword.isBlank()) {
            return Pair(false, "设计主题不能为空")
        }

        return Pair(true, "")
    }
}

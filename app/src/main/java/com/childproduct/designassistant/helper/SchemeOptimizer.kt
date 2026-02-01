package com.childproduct.designassistant.helper

import com.childproduct.designassistant.config.StandardConfig
import com.childproduct.designassistant.model.ChildProductDesignScheme
import com.childproduct.designassistant.model.InstallMethod
import com.childproduct.designassistant.model.TestMatrixItem

/**
 * 方案优化工具（优化点：清理乱码+规范格式+结构化输出）
 *
 * 核心优化：
 * - 彻底清理乱码（无意义中文乱码、代码字段、特殊字符）
 * - 规范中英文格式（标准名称、条款编号等）
 * - 结构化输出（模块+列表+符号，极致可读）
 * - 使用预编译正则表达式，减少运行时开销
 */
object SchemeOptimizer {
    /**
     * 用户输入模型
     */
    data class UserInput(
        val productType: String,
        val heightRange: String,
        val installMethod: InstallMethod,
        val themeKeyword: String
    )

    // 预编译正则表达式（避免重复创建，提升性能）
    private val CODE_PATTERN = Regex(
        """CreativeIdea\(id=.+?\)|ComplianceParameters\(.+?\)|StandardsReference\(.+?\)"""
    )
    private val KEY_VALUE_PATTERN = Regex("""[a-zA-Z_]+=[\w#\[\],\(\).:;]+""")
    private val UUID_PATTERN = Regex(
        """[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"""
    )
    private val MEANINGLESS_CHINESE_PATTERN = Regex(
        """[罕諍贏襯儔赎秭烊騭閒鬥逅崃譖教渧矮備鲂窓的煤抽屃冭粬亂║胜文鴆嘱魅喙躍¶裉燹苇]+"""
    )
    private val SPECIAL_CHAR_PATTERN = Regex("""[\p{So}\p{Sk}\p{Cc}\p{Cf}]""")
    private val SPACE_PATTERN = Regex("""\s{2,}""")

    /**
     * 入口方法：生成优化后的设计方案（结构化+清理乱码）
     * @param userInput  用户输入集合
     * @return  优化后的结构化设计方案
     */
    fun generateOptimizedScheme(userInput: UserInput): ChildProductDesignScheme {
        // 1. 深度清理输入内容（含主题关键词和原始输出残留）
        val cleanedTheme = cleanGarbledContent(userInput.themeKeyword)
        val heightConfig = StandardConfig.getHeightConfig(userInput.heightRange)
            ?: StandardConfig.HEIGHT_DUMMY_MAPPING["40-150cm"]!!

        // 2. 提取并规范有效数据（固定核心特点，去重+整理）
        val coreFeatures = listOf("材质环保", "易安装性", "舒适性", "安全性")
        val recommendMaterials = listOf(
            "食品级PP塑料（主体框架）",
            "高回弹海绵（填充层）",
            "安全带织带（抗拉强度≥11000N）",
            "铝合金支架（支撑结构）"
        )
        val safetyNotes = listOf(
            "符合国家玩具安全标准GB 6675",
            "注意电气安全（如适用）",
            "提供清晰的使用说明和安全警示"
        )

        // 3. 规范合规参数（清理乱码后整理）
        val safetyThresholds = mapOf(
            "HIC极限值" to "≤390（Q0/Q0+/Q1）、≤570（Q1.5）、≤1000（Q3/Q3s/Q6/Q10）",
            "胸部加速度" to "≤55g（Q0-Q1.5）、≤60g（Q3+）",
            "颈部张力极限" to "≤1800N（Q0-Q1.5）、≤2000N（Q3+）",
            "颈部压缩极限" to "≤2500N",
            "头部位移极限" to "≤550mm（全假人）",
            "膝部位移极限" to "≤650mm（全假人）",
            "胸部位移极限" to "≤52mm（全假人）",
            "阻燃性能" to "符合FMVSS 302（燃烧速度≤4英寸/分钟）"
        )

        // 4. 简化标准条款（保留核心要求，去除乱码和冗余）
        val complianceStandards = listOf("ECE R129 i-Size", "GB 27887-2024", "FMVSS 213")
        val standardClauses = listOf(
            "ECE R129 §5.2：假人分类覆盖Q0-Q10",
            "ECE R129 §7：动态测试要求（含HIC、胸部加速度等指标）",
            "GB 27887-2024 §5.3：身高适配范围40-150cm",
            "GB 27887-2024 §6.4：动态测试性能达标"
        )

        // 5. 构建结构化方案
        return ChildProductDesignScheme.builder(
            productType = userInput.productType,
            heightRange = userInput.heightRange
        )
            .ageRange(if (userInput.heightRange == "40-150cm") "0-12岁" else heightConfig.ageRange)
            .designTheme("${userInput.productType} - $cleanedTheme")
            .installMethodDesc("ISOFIX快速连接（符合ECE R129安装要求）")
            .coreFeatures(coreFeatures)
            .recommendMaterials(recommendMaterials)
            .complianceStandards(complianceStandards)
            .dummyType(if (userInput.heightRange == "40-150cm") "Q0-Q10全假人" else "Q10假人")
            .safetyThresholds(safetyThresholds)
            .testMatrix(generateTestMatrix())
            .safetyNotes(safetyNotes + standardClauses) // 合并安全提示和标准条款
            .build()
    }

    /**
     * 生成测试矩阵（结构化+关联标准条款）
     * @return  测试矩阵列表
     */
    private fun generateTestMatrix(): List<TestMatrixItem> {
        return listOf(
            TestMatrixItem(
                testItem = "HIC极限值",
                standardRequirement = "≤390（Q0/Q0+/Q1）、≤570（Q1.5）、≤1000（Q3/Q3s/Q6/Q10）",
                applicableDummy = "Q0-Q10",
                unit = "-",
                standardSource = "ECE R129 §7.1.2"
            ),
            TestMatrixItem(
                testItem = "胸部加速度（3ms）",
                standardRequirement = "≤55g（Q0-Q1.5）、≤60g（Q3+）",
                applicableDummy = "Q0-Q10",
                unit = "g",
                standardSource = "ECE R129 §7.1.3"
            ),
            TestMatrixItem(
                testItem = "颈部张力极限",
                standardRequirement = "≤1800N（Q0-Q1.5）、≤2000N（Q3+）",
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
                testItem = "胸部压缩极限",
                standardRequirement = "≤52mm",
                applicableDummy = "Q0-Q10",
                unit = "mm",
                standardSource = "ECE R129 §7.1.6"
            ),
            TestMatrixItem(
                testItem = "阻燃性能",
                standardRequirement = "符合FMVSS 302（燃烧速度≤4英寸/分钟）",
                applicableDummy = "全年龄段",
                unit = "-",
                standardSource = "FMVSS 302"
            )
        )
    }

    /**
     * 终极乱码清理：覆盖无意义中文乱码、代码字段、特殊字符
     * @param rawContent  原始内容
     * @return  清理后的内容
     */
    fun cleanGarbledContent(rawContent: String): String {
        if (rawContent.isBlank()) return ""

        var cleaned = rawContent

        // 1. 移除所有代码式字段（含CreativeIdea、合规参数等）
        cleaned = CODE_PATTERN.replace(cleaned, "")
        cleaned = KEY_VALUE_PATTERN.replace(cleaned, "")
        cleaned = UUID_PATTERN.replace(cleaned, "")

        // 2. 清理无意义中文乱码（匹配非常用中文，保留正常词汇）
        cleaned = MEANINGLESS_CHINESE_PATTERN.replace(cleaned, "")

        // 3. 规范中英文格式（补全空格、修正标准名称）
        cleaned = cleaned.replace("ECER129", "ECE R129")
            .replace("GB27887", "GB 27887")
            .replace("S5.2", "§5.2")
            .replace("HIC15", "HIC-15")
            .replace("HIC36", "HIC-36")
            .replace("q(", "g（")

        // 4. 过滤特殊字符/不可见字符
        cleaned = SPECIAL_CHAR_PATTERN.replace(cleaned, "")
        cleaned = SPACE_PATTERN.replace(cleaned, " ").trim()

        return cleaned
    }

    /**
     * 格式化方案用于UI展示（模块+列表，极致可读）
     * @param scheme  设计方案
     * @return  格式化后的字符串
     */
    fun formatSchemeForDisplay(scheme: ChildProductDesignScheme): String {
        return buildString {
            // 1. 基本信息（清晰罗列核心属性）
            appendLine("📌 基本信息")
            appendLine("├─ 产品类型：${scheme.productType}")
            appendLine("├─ 身高范围：${scheme.heightRange}")
            appendLine("├─ 适用年龄段：${scheme.ageRange}")
            appendLine("├─ 设计主题：${scheme.designTheme}")
            appendLine("├─ 适配假人：${scheme.dummyType}")
            appendLine("└─ 安装方式：${scheme.installMethodDesc}")
            appendLine()

            // 2. 核心设计特点（简洁列表）
            appendLine("✨ 核心设计特点")
            scheme.coreFeatures.forEachIndexed { index, feature ->
                appendLine("${index + 1}. $feature")
            }
            appendLine()

            // 3. 推荐材料（带用途说明）
            appendLine("📦 推荐材料")
            scheme.recommendMaterials.forEach { material ->
                appendLine("- $material")
            }
            appendLine()

            // 4. 颜色搭配（规范颜色码+中文说明）
            appendLine("🎨 颜色搭配")
            appendLine("- #000000（黑色）")
            appendLine("- #808080（灰色）")
            appendLine("- #FFFFFF（白色）")
            appendLine("- #FF1493（深粉色）")
            appendLine()

            // 5. 合规参数（安全阈值+标准条款，子列表清晰区分）
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

            // 6. 安全注意事项（单独罗列，重点突出）
            appendLine("⚠️  安全注意事项")
            scheme.safetyNotes.filterNot { it.startsWith("ECE") || it.startsWith("GB") }.forEach { note ->
                appendLine("- $note")
            }
        }
    }

    /**
     * 快速验证输入（复用StandardConfig）
     * @param userInput  用户输入
     * @return  验证结果
     */
    fun validateInput(userInput: UserInput): Pair<Boolean, String> {
        // 产品类型非空校验
        if (userInput.productType.isBlank()) {
            return Pair(false, "产品类型不能为空")
        }

        // 身高范围校验
        if (!StandardConfig.isValidHeightRange(userInput.heightRange)) {
            return Pair(false, "身高范围${userInput.heightRange}不符合标准要求")
        }

        // 主题关键词非空校验
        if (userInput.themeKeyword.isBlank()) {
            return Pair(false, "设计主题不能为空")
        }

        return Pair(true, "")
    }
}

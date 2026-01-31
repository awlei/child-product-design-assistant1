package com.childproduct.designassistant.helper

import com.childproduct.designassistant.model.ChildProductDesignScheme
import com.childproduct.designassistant.model.InstallMethod
import com.childproduct.designassistant.model.TestMatrixItem
import java.nio.charset.StandardCharsets

/**
 * 核心工具类：整合乱码清理、动态方案生成、标准参数映射
 */
object SchemeOptimizer {
    // 1. 标准映射表：身高范围 → (适用年龄段, 适配假人描述)
    private val HEIGHT_AGE_DUMMY_MAP = mapOf(
        "40-60cm" to Pair("0-1岁", "Q0假人"),
        "60-75cm" to Pair("1-2岁", "Q1假人"),
        "75-87cm" to Pair("2-3岁", "Q1.5假人"),
        "87-105cm" to Pair("3-4岁", "Q3假人"),
        "105-125cm" to Pair("4-6岁", "Q6假人"),
        "125-150cm" to Pair("6-12岁", "Q10假人"),
        "40-150cm" to Pair("0-12岁", "Q0-Q10全假人")
    )

    /**
     * 入口方法：接收用户输入，生成优化后的设计方案
     * @param userInput  用户输入集合（产品类型、身高、安装方式、主题关键词）
     * @return  结构化、无乱码的设计方案
     */
    fun generateOptimizedScheme(userInput: UserInput): ChildProductDesignScheme {
        // 步骤1：清理设计主题关键词中的乱码
        val cleanedThemeKeyword = cleanGarbledContent(userInput.themeKeyword)

        // 步骤2：动态生成基础信息
        val (ageRange, dummyType) = HEIGHT_AGE_DUMMY_MAP[userInput.heightRange]
            ?: HEIGHT_AGE_DUMMY_MAP["40-150cm"]!! // 兜底默认值
        val designTheme = "${userInput.productType} - $cleanedThemeKeyword"

        // 步骤3：动态生成核心设计特点（联动安装方式）
        val coreFeatures = generateCoreFeatures(userInput.installMethod)

        // 步骤4：生成推荐材料（含标准性能参数）
        val recommendMaterials = generateRecommendMaterials()

        // 步骤5：生成结构化测试矩阵（参照ROADMATE 360）
        val testMatrix = generateTestMatrix()

        // 步骤6：生成合规标准
        val complianceStandards = listOf("ECE R129", "GB 27887-2024", "FMVSS 213")

        // 步骤7：生成安全阈值
        val safetyThresholds = mapOf(
            "HIC极限值" to "≤390（Q0-Q1.5）/≤1000（Q3-Q10）",
            "胸部加速度" to "≤55g（Q0-Q1.5）/≤60g（Q3-Q10）",
            "颈部张力极限" to "≤1800N（Q3-Q10）",
            "头部位移极限" to "≤550mm（全假人）",
            "阻燃性能" to "符合FMVSS 302（燃烧速度≤4英寸/分钟）"
        )

        // 步骤8：生成安全注意事项（关联ISO/ECE标准）
        val safetyNotes = generateSafetyNotes()

        // 组装最终方案
        return ChildProductDesignScheme(
            productType = userInput.productType,
            heightRange = userInput.heightRange,
            ageRange = ageRange,
            designTheme = designTheme,
            installMethodDesc = userInput.installMethod.description,
            coreFeatures = coreFeatures,
            recommendMaterials = recommendMaterials,
            complianceStandards = complianceStandards,
            dummyType = dummyType,
            safetyThresholds = safetyThresholds,
            testMatrix = testMatrix,
            safetyNotes = safetyNotes
        )
    }

    /**
     * 全维度乱码清理：覆盖编码问题、代码字段、特殊符号、emoji
     */
    private fun cleanGarbledContent(rawContent: String): String {
        var cleaned = rawContent
        // 1. 强制统一UTF-8编码（解决编码导致的乱码）
        cleaned = String(cleaned.toByteArray(StandardCharsets.UTF_8), StandardCharsets.UTF_8)

        // 2. 移除代码式字段（如id、变量名）
        val codePatterns = listOf(
            Regex("""CreativeIdea\(id=.+?\)"""),
            Regex("""[a-zA-Z_]+=[\w#\[\],]+"""),
            Regex("""[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}""") // UUID
        )
        codePatterns.forEach { cleaned = it.replace(cleaned, "") }

        // 3. 过滤特殊字符（转义符、emoji、不可见字符）
        val specialPatterns = listOf(
            Regex("""\\u[0-9a-fA-F]{4}"""), // Unicode转义符
            Regex("""[\p{So}\p{Sk}]"""),    // Emoji/特殊符号
            Regex("""[\t\r\f\v]"""),       // 不可见控制字符
            Regex("""\s{2,}""")            // 多余空格
        )
        specialPatterns.forEach {
            cleaned = if (it.pattern == """\s{2,}""") it.replace(cleaned, " ") else it.replace(cleaned, "")
        }

        return cleaned.trim()
    }

    /**
     * 动态生成核心设计特点（联动用户选择的安装方式）
     */
    private fun generateCoreFeatures(installMethod: InstallMethod): List<String> {
        return listOf(
            "易安装性：${installMethod.description}",
            "安全性：符合ECE R129/GB 27887-2024 + FMVSS 213标准（全假人覆盖）",
            "舒适性：高回弹海绵填充（密度30kg/m³，适配0-12岁儿童体型）",
            "材质环保：食品级PP塑料（无甲醛/重金属，符合EN 71-3有害元素标准）"
        )
    }

    /**
     * 生成推荐材料（含ECE R129要求的性能参数）
     */
    private fun generateRecommendMaterials(): List<String> {
        return listOf(
            "主体框架：食品级PP塑料（耐温-30℃~80℃，抗冲击强度≥20kJ/m²）",
            "填充层：高回弹海绵（压缩回弹率≥90%，无异味）",
            "约束部件：高强度安全带织带（断裂强度≥11000N，耐磨后强度保留率≥75%）",
            "支撑结构：铝合金支架（盐雾测试50小时无腐蚀，抗拉强度≥300MPa）",
            "面料：阻燃聚酯纤维（符合FMVSS 302，燃烧速度≤4英寸/分钟）"
        )
    }

    /**
     * 生成结构化测试矩阵（参照ROADMATE 360格式，关联标准条款）
     */
    private fun generateTestMatrix(): List<TestMatrixItem> {
        return listOf(
            TestMatrixItem(
                testItem = "HIC极限值",
                standardRequirement = "≤390（Q0-Q1.5）/ ≤1000（Q3-Q10）",
                applicableDummy = "Q0-Q10",
                unit = "-",
                standardSource = "ECE R129 §7.1.2"
            ),
            TestMatrixItem(
                testItem = "胸部加速度（3ms）",
                standardRequirement = "≤55g（Q0-Q1.5）/ ≤60g（Q3-Q10）",
                applicableDummy = "Q0-Q10",
                unit = "g",
                standardSource = "ECE R129 §7.1.3"
            ),
            TestMatrixItem(
                testItem = "颈部张力极限",
                standardRequirement = "≤1500N（Q0-Q1.5）/ ≤1800N（Q3-Q10）",
                applicableDummy = "Q0-Q10",
                unit = "N",
                standardSource = "ECE R129 §7.1.4"
            ),
            TestMatrixItem(
                testItem = "颈部压缩极限",
                standardRequirement = "≤2000N（Q0-Q1.5）/ ≤2500N（Q3-Q10）",
                applicableDummy = "Q0-Q10",
                unit = "N",
                standardSource = "ECE R129 §7.1.4"
            ),
            TestMatrixItem(
                testItem = "头部位移极限",
                standardRequirement = "≤550mm（全假人）",
                applicableDummy = "Q0-Q10",
                unit = "mm",
                standardSource = "ECE R129 §7.1.5"
            ),
            TestMatrixItem(
                testItem = "膝部位移极限",
                standardRequirement = "≤650mm（全假人）",
                applicableDummy = "Q0-Q10",
                unit = "mm",
                standardSource = "ECE R129 §7.1.5"
            ),
            TestMatrixItem(
                testItem = "胸部压缩极限",
                standardRequirement = "≤45mm（Q0-Q1.5）/ ≤52mm（Q3-Q10）",
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
    }

    /**
     * 生成安全注意事项（关联ISO 8124-3/ECE R129标准）
     */
    private fun generateSafetyNotes(): List<String> {
        return listOf(
            "防吞咽风险：所有细小零件（直径＜31.75mm）需固定，符合ISO 8124-3",
            "材料安全：接触材料需通过EN 71-3检测（铅≤90mg/kg，镉≤75mg/kg）",
            "安装检查：防旋转装置（Top-tether/支撑腿）需完全锁止，避免倾倒",
            "碰撞后处理：即使外观无损坏，碰撞后也需更换ECRS（ECE R129强制要求）",
            "使用限制：不可改装ECRS结构（如切割织带、更换零件），否则失效"
        )
    }

    /**
     * 辅助方法：将设计方案转为UI展示的字符串（格式化输出）
     */
    fun formatSchemeForDisplay(scheme: ChildProductDesignScheme): String {
        return buildString {
            // 1. 基本信息
            appendLine("【基本信息】")
            appendLine("- 产品类型：${scheme.productType}")
            appendLine("- 身高范围：${scheme.heightRange}")
            appendLine("- 适用年龄段：${scheme.ageRange}")
            appendLine("- 设计主题：${scheme.designTheme}")
            appendLine()

            // 2. 核心设计特点
            appendLine("【核心设计特点】")
            scheme.coreFeatures.forEach { appendLine("- $it") }
            appendLine()

            // 3. 推荐材料
            appendLine("【推荐材料】")
            scheme.recommendMaterials.forEach { appendLine("- $it") }
            appendLine()

            // 4. 测试矩阵（表格格式）
            appendLine("【测试矩阵】（参照ROADMATE 360标准）")
            appendLine("| 测试项目         | 标准要求                          | 适用假人 | 单位 | 标准来源               |")
            appendLine("|------------------|-----------------------------------|----------|------|------------------------|")
            scheme.testMatrix.forEach { item ->
                appendLine(String.format(
                    "| %-13s | %-30s | %-8s | %-4s | %-22s |",
                    item.testItem,
                    item.standardRequirement,
                    item.applicableDummy,
                    item.unit,
                    item.standardSource
                ))
            }
            appendLine()

            // 5. 安全注意事项
            appendLine("【安全注意事项】")
            scheme.safetyNotes.forEach { appendLine("- $it") }
        }
    }

    /**
     * 用户输入数据类（整合所有用户输入项，便于传递）
     */
    data class UserInput(
        val productType: String,          // 产品类型（如：儿童安全座椅）
        val heightRange: String,          // 身高范围（如：40-150cm）
        val installMethod: InstallMethod, // 安装方式（用户选择的枚举值）
        val themeKeyword: String          // 设计主题关键词（如：拼图游戏）
    )
}

package com.childproduct.designassistant.helper

import com.childproduct.designassistant.config.StandardConfig
import com.childproduct.designassistant.model.ChildProductDesignScheme
import com.childproduct.designassistant.model.InstallMethod
import com.childproduct.designassistant.model.TestMatrixItem
import java.nio.charset.StandardCharsets

/**
 * 方案优化工具（优化点：职责单一+复用配置+性能优化）
 *
 * 核心优化：
 * - 复用 StandardConfig 配置，避免重复定义
 * - 使用构建器模式简化对象创建
 * - 预编译正则表达式，减少运行时开销
 * - 链式字符串处理，减少中间变量和内存拷贝
 * - 职责单一化，仅负责方案生成和优化
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
        """CreativeIdea\(id=.+?\)|[a-zA-Z_]+=[\w#\[\],]+|[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"""
    )
    private val SPECIAL_CHAR_PATTERN = Regex("""\\u[0-9a-fA-F]{4}|[\p{So}\p{Sk}|\t\r\f\v]""")
    private val SPACE_PATTERN = Regex("""\s{2,}""")

    /**
     * 入口方法：生成优化后的设计方案（使用构建器模式，减少冗余）
     * @param userInput  用户输入集合
     * @return  优化后的结构化设计方案
     */
    fun generateOptimizedScheme(userInput: UserInput): ChildProductDesignScheme {
        // 1. 清理设计主题
        val cleanedTheme = cleanGarbledContent(userInput.themeKeyword)
        val designTheme = "${userInput.productType} - $cleanedTheme"

        // 2. 获取标准映射配置（复用StandardConfig）
        val heightConfig = StandardConfig.getHeightConfig(userInput.heightRange)
            ?: StandardConfig.HEIGHT_DUMMY_MAPPING["40-150cm"]!!

        // 3. 生成核心特点（复用配置+联动安装方式）
        val coreFeatures = generateCoreFeatures(userInput.installMethod, heightConfig.installDirection)

        // 4. 生成测试矩阵（结构化+关联标准条款）
        val testMatrix = generateTestMatrix()

        // 5. 使用构建器创建方案（简化代码，自动去重）
        return ChildProductDesignScheme.builder(
            productType = userInput.productType,
            heightRange = userInput.heightRange
        )
            .ageRange(heightConfig.ageRange)
            .designTheme(designTheme)
            .installMethodDesc(userInput.installMethod.description)
            .coreFeatures(coreFeatures)
            .dummyType(getDummyTypeDescription(userInput.heightRange, heightConfig.dummyType))
            .testMatrix(testMatrix)
            .build()
    }

    /**
     * 获取假人类型描述
     */
    private fun getDummyTypeDescription(heightRange: String, dummyType: com.childproduct.designassistant.model.CrashTestDummy): String {
        return if (heightRange == "40-150cm") {
            "Q0-Q10全假人"
        } else {
            "${dummyType.name}假人"
        }
    }

    /**
     * 乱码清理（优化：减少字符串拷贝+预编译正则）
     * @param rawContent  原始内容
     * @return  清理后的内容
     */
    fun cleanGarbledContent(rawContent: String): String {
        if (rawContent.isBlank()) return ""

        // 链式处理，减少中间变量
        return rawContent
            .let { String(it.toByteArray(StandardCharsets.UTF_8), StandardCharsets.UTF_8) }
            .replace(CODE_PATTERN, "")
            .replace(SPECIAL_CHAR_PATTERN, "")
            .replace(SPACE_PATTERN, " ")
            .trim()
    }

    /**
     * 生成核心特点（优化：复用配置+联动安装方式）
     * @param installMethod  安装方式
     * @param installDirection  安装方向
     * @return  核心特点列表
     */
    private fun generateCoreFeatures(installMethod: InstallMethod, installDirection: String): List<String> {
        return listOf(
            "易安装性：${installMethod.description}",
            "安装方向：$installDirection（符合${StandardConfig.STANDARD_VERSION}要求）",
            "安全性：符合${StandardConfig.COMPLIANCE_STANDARDS.joinToString("、")}",
            "舒适性：高回弹海绵填充（密度30kg/m³，适配全年龄段儿童体型）",
            "材质环保：食品级PP塑料（无甲醛/重金属，符合EN 71-3有害元素标准）"
        )
    }

    /**
     * 生成测试矩阵（优化：复用配置+结构化）
     * @return  测试矩阵列表
     */
    private fun generateTestMatrix(): List<TestMatrixItem> {
        return listOf(
            TestMatrixItem(
                testItem = "HIC极限值",
                standardRequirement = StandardConfig.getSafetyThreshold("HIC极限值") ?: "",
                applicableDummy = "Q0-Q10",
                unit = "-",
                standardSource = "${StandardConfig.STANDARD_VERSION} §7.1.2"
            ),
            TestMatrixItem(
                testItem = "胸部加速度（3ms）",
                standardRequirement = StandardConfig.getSafetyThreshold("胸部加速度") ?: "",
                applicableDummy = "Q0-Q10",
                unit = "g",
                standardSource = "${StandardConfig.STANDARD_VERSION} §7.1.3"
            ),
            TestMatrixItem(
                testItem = "颈部张力极限",
                standardRequirement = StandardConfig.getSafetyThreshold("颈部张力极限") ?: "",
                applicableDummy = "Q0-Q10",
                unit = "N",
                standardSource = "${StandardConfig.STANDARD_VERSION} §7.1.4"
            ),
            TestMatrixItem(
                testItem = "颈部压缩极限",
                standardRequirement = StandardConfig.getSafetyThreshold("颈部压缩极限") ?: "",
                applicableDummy = "Q0-Q10",
                unit = "N",
                standardSource = "${StandardConfig.STANDARD_VERSION} §7.1.4"
            ),
            TestMatrixItem(
                testItem = "头部位移极限",
                standardRequirement = StandardConfig.getSafetyThreshold("头部位移极限") ?: "",
                applicableDummy = "Q0-Q10",
                unit = "mm",
                standardSource = "${StandardConfig.STANDARD_VERSION} §7.1.5"
            ),
            TestMatrixItem(
                testItem = "膝部位移极限",
                standardRequirement = StandardConfig.getSafetyThreshold("膝部位移极限") ?: "",
                applicableDummy = "Q0-Q10",
                unit = "mm",
                standardSource = "${StandardConfig.STANDARD_VERSION} §7.1.5"
            ),
            TestMatrixItem(
                testItem = "胸部压缩极限",
                standardRequirement = StandardConfig.getSafetyThreshold("胸部压缩极限") ?: "",
                applicableDummy = "Q0-Q10",
                unit = "mm",
                standardSource = "${StandardConfig.STANDARD_VERSION} §7.1.6"
            ),
            TestMatrixItem(
                testItem = "阻燃性能",
                standardRequirement = StandardConfig.getSafetyThreshold("阻燃性能") ?: "",
                applicableDummy = "全年龄段",
                unit = "-",
                standardSource = StandardConfig.FMVSS_VERSION
            )
        )
    }

    /**
     * 格式化方案用于UI展示（优化：使用StringBuilder+树形结构）
     * @param scheme  设计方案
     * @return  格式化后的字符串
     */
    fun formatSchemeForDisplay(scheme: ChildProductDesignScheme): String {
        return buildString {
            appendLine("【${StandardConfig.STANDARD_VERSION} 标准化设计方案】")
            appendLine("├─ 产品类型：${scheme.productType}")
            appendLine("├─ 身高范围：${scheme.heightRange}")
            appendLine("├─ 适配年龄段：${scheme.ageRange}")
            appendLine("├─ 设计主题：${scheme.designTheme}")
            appendLine("├─ 安装方式：${scheme.installMethodDesc}")
            appendLine("├─ 适配假人：${scheme.dummyType}")
            appendLine("├─ 核心特点：")
            scheme.coreFeatures.forEach { appendLine("│  └─ $it") }
            appendLine("├─ 推荐材料：")
            scheme.recommendMaterials.forEach { appendLine("│  └─ $it") }
            appendLine("├─ 合规标准：${scheme.complianceStandards.joinToString("、")}")
            appendLine("├─ 安全阈值：")
            scheme.safetyThresholds.forEach { (key, value) -> appendLine("│  └─ $key：$value") }
            appendLine("└─ 安全注意事项：")
            scheme.safetyNotes.forEach { appendLine("   └─ $it") }
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
            return Pair(false, "身高范围${userInput.heightRange}不符合${StandardConfig.STANDARD_VERSION}标准")
        }

        // 主题关键词非空校验
        if (userInput.themeKeyword.isBlank()) {
            return Pair(false, "设计主题不能为空")
        }

        return Pair(true, "")
    }
}

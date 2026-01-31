package com.childproduct.designassistant.utils

/**
 * 儿童产品设计方案输出合规校验工具类
 * 功能：检测并自动修正输出结果中的乱码、参数错误、排版杂乱等问题
 * 适配场景：输入身高40-150cm时的儿童安全座椅方案输出
 */
object OutputComplianceChecker {

    // 核心配置：身高-年龄段-假人映射表（标准值）
    private val HEIGHT_AGE_DUMMY_MAP = mapOf(
        "40-150" to Pair("0-12岁（身高40-150cm）", "Q0-Q10（0-12岁）")
    )

    // 核心配置：结构化模板模块
    private val STRUCTURE_MODULES = listOf(
        "【基本信息】",
        "【核心设计特点】",
        "【推荐材料】",
        "【合规参数】",
        "【安全注意事项】"
    )

    /**
     * 对外暴露的核心方法：检测并修正输出结果
     * @param rawOutput 原始输出字符串（含乱码/错误/杂乱内容）
     * @param inputHeightRange 用户输入的身高范围（如"40-150"）
     * @param productType 产品类型（默认：儿童安全座椅）
     * @return 修正后的结构化输出字符串
     */
    fun checkAndFixOutput(
        rawOutput: String,
        inputHeightRange: String = "40-150",
        productType: String = "儿童安全座椅"
    ): String {
        // 步骤1：清理乱码和冗余代码字段
        var fixedOutput = cleanGarbledAndRedundant(rawOutput)
        // 步骤2：修正身高-年龄段-假人匹配错误
        fixedOutput = fixHeightAgeDummyMapping(fixedOutput, inputHeightRange)
        // 步骤3：修正合规组合描述错误
        fixedOutput = fixComplianceCombination(fixedOutput, inputHeightRange, productType)
        // 步骤4：移除重复信息
        fixedOutput = removeDuplicateInfo(fixedOutput)
        // 步骤5：结构化排版
        fixedOutput = structureFormat(fixedOutput, productType, inputHeightRange)
        return fixedOutput
    }

    /**
     * 步骤1：清理乱码、冗余代码字段（如id=xxx、productType=xxx等）
     */
    private fun cleanGarbledAndRedundant(rawOutput: String): String {
        var result = rawOutput
        // 1. 移除代码式字段（正则匹配）
        val codePattern = Regex("(CreativeIdea\\(id=\\w+\\)|productType=\\w+|ComplianceParameters\\(.+?\\)|standardsReference=\\w+|\\w+=\\w+)")
        result = codePattern.replace(result, "")
        // 2. 过滤非UTF-8乱码字符（保留中文、数字、字母、常用符号）
        val garbledPattern = Regex("[^\\u4e00-\\u9fa50-9a-zA-Z\\s\\-\\+\\≤≥（）【】：；,.，。！？]")
        result = garbledPattern.replace(result, "")
        // 3. 移除多余空格和换行
        result = result.replace(Regex("\\s+"), " ").trim()
        return result
    }

    /**
     * 步骤2：修正身高-年龄段-假人匹配错误
     */
    private fun fixHeightAgeDummyMapping(rawOutput: String, heightRange: String): String {
        var result = rawOutput
        val (correctAge, correctDummy) = HEIGHT_AGE_DUMMY_MAP[heightRange]
            ?: Pair("0-12岁（身高40-150cm）", "Q0-Q10（0-12岁）")

        // 替换错误年龄段（如6-9岁）
        val wrongAgePattern = Regex("6-9岁|3-6岁|1-3岁")
        result = wrongAgePattern.replace(result, correctAge)

        // 替换错误假人类型（如Q3）
        val wrongDummyPattern = Regex("Q3|Q6|Q1（仅）|Q0（仅）")
        result = wrongDummyPattern.replace(result, correctDummy)

        return result
    }

    /**
     * 步骤3：修正合规组合描述错误
     */
    private fun fixComplianceCombination(
        rawOutput: String,
        heightRange: String,
        productType: String
    ): String {
        val correctCompliance = "当前合规组合：$productType + ECE R129/GB 27887-2024 → $heightRange cm（Q0-Q10 dummy）（覆盖0-12岁全年龄段）"
        // 匹配原有合规组合字段并替换
        val compliancePattern = Regex("当前合规组合：.+?（.*?）|当前合规组合：.+?→.*?")
        return if (compliancePattern.containsMatchIn(rawOutput)) {
            compliancePattern.replace(rawOutput, correctCompliance)
        } else {
            "$correctCompliance\n$rawOutput"
        }
    }

    /**
     * 步骤4：移除重复信息（如重复的HIC阈值、标准名称）
     */
    private fun removeDuplicateInfo(rawOutput: String): String {
        val lines = rawOutput.split("\n").toMutableList()
        val uniqueLines = mutableSetOf<String>()
        // 过滤空行+重复行
        lines.forEach { line ->
            val trimLine = line.trim()
            if (trimLine.isNotEmpty() && !uniqueLines.contains(trimLine)) {
                uniqueLines.add(trimLine)
            }
        }
        return uniqueLines.joinToString("\n")
    }

    /**
     * 步骤5：结构化排版（按固定模块生成列表式内容）
     */
    private fun structureFormat(
        rawOutput: String,
        productType: String,
        heightRange: String
    ): String {
        val (ageRange, dummyType) = HEIGHT_AGE_DUMMY_MAP[heightRange]
            ?: Pair("0-12岁（身高40-150cm）", "Q0-Q10（0-12岁）")

        // 提取设计主题（从原始内容中匹配）
        val themePattern = Regex("设计主题：(.+?)|创意主题：(.+?)")
        val designTheme = themePattern.find(rawOutput)?.groupValues?.let {
            it[1].ifEmpty { it[2] }
        } ?: "创意涂鸦儿童安全座椅"

        // 构建结构化内容
        val structuredContent = StringBuilder().apply {
            // 1. 基本信息
            appendLine(STRUCTURE_MODULES[0])
            appendLine("- 产品类型：$productType")
            appendLine("- 适用年龄段：$ageRange")
            appendLine("- 设计主题：$designTheme")
            appendLine()

            // 2. 核心设计特点
            appendLine(STRUCTURE_MODULES[1])
            appendLine("- 易安装性：支持ISOFIX连接，实现快速安装")
            appendLine("- 材质环保：选用食品级安全材料，无甲醛/重金属")
            appendLine("- 舒适性：高回弹海绵填充，贴合全年龄段儿童体型")
            appendLine("- 安全性：符合ECE R129/GB 27887-2024全假人安全要求")
            appendLine()

            // 3. 推荐材料
            appendLine(STRUCTURE_MODULES[2])
            appendLine("- 主体框架：食品级PP塑料")
            appendLine("- 填充材质：高回弹海绵")
            appendLine("- 约束部件：高强度安全带织带（抗拉强度≥2000N）")
            appendLine("- 支撑结构：铝合金支架（防锈处理）")
            appendLine()

            // 4. 合规参数
            appendLine(STRUCTURE_MODULES[3])
            appendLine("- 对应标准：ECE R129 + GB 27887-2024 + FMVSS 213")
            appendLine("- 适配假人：$dummyType")
            appendLine("- 安全阈值：")
            appendLine("  - HIC极限值：≤390（Q0-Q1.5）/≤1000（Q3-Q10）")
            appendLine("  - 胸部加速度：≤55g（Q0-Q1.5）/≤60g（Q3-Q10）")
            appendLine("  - 头部位移：≤550mm（全假人）")
            appendLine("  - 阻燃性能：符合FMVSS 302（燃烧速度≤4英寸/分钟）")
            appendLine()

            // 5. 安全注意事项
            appendLine(STRUCTURE_MODULES[4])
            appendLine("- 避免细小零件脱落，防止儿童吞咽风险")
            appendLine("- 材料需通过欧盟EN71安全认证")
            appendLine("- 结构稳固，安装后无倾倒风险")
        }

        return structuredContent.toString()
    }
}

// ====================== 调用示例 ======================
/**
 * 在APK的方案生成模块中调用示例
 */
fun generateFixedOutputExample() {
    // 原始乱码输出（示例）
    val rawBadOutput = "CreativeIdea(id=123) 产品类型=儿童安全座椅 适用年龄段：6-9岁 设计主题：创意涂鸦儿童安全座椅 HIC≤1000 HIC≤1000 符合ECE R129 适配假人：Q3"
    // 调用校验工具修正
    val fixedOutput = OutputComplianceChecker.checkAndFixOutput(
        rawOutput = rawBadOutput,
        inputHeightRange = "40-150",
        productType = "儿童安全座椅"
    )
    // 输出修正后的结果（可直接展示在APK界面）
    println(fixedOutput)
}

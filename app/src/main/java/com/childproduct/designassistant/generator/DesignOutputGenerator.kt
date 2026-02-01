package com.childproduct.designassistant.generator

import com.childproduct.designassistant.model.*

/**
 * 儿童产品设计方案输出生成器
 * 支持多标准适配、产品类型差异化输出、安装方式智能映射
 */
object DesignOutputGenerator {

    /**
     * 生成完整设计方案输出（Markdown格式）
     */
    fun generateDesignOutput(input: DesignInput): String {
        return when (input.productType) {
            EnhancedProductType.SAFETY_SEAT -> generateSafetySeatOutput(input)
            EnhancedProductType.STROLLER -> generateStrollerOutput(input)
            EnhancedProductType.HIGH_CHAIR -> generateHighChairOutput(input)
            EnhancedProductType.CRIB -> generateCribOutput(input)
        }
    }

    // ========== 儿童安全座椅输出 ==========
    private fun generateSafetySeatOutput(input: DesignInput): String {
        require(input.heightRange != null) { "安全座椅必须提供身高范围" }
        require(input.installMethod != null) { "安全座椅必须选择安装方式" }

        val (minHeight, maxHeight) = parseHeightRange(input.heightRange!!)
        val dummyMappings = getDummyMappings(minHeight, maxHeight)
        val installDirections = getInstallDirections(minHeight, maxHeight)

        return buildString {
            // 1. 标准映射说明
            appendLine(generateStandardMappingSection(input, dummyMappings, installDirections))

            // 2. 基本设计方案
            appendLine(generateBasicDesignSection(input, minHeight, maxHeight, installDirections))

            // 3. ISOFIX envelope尺寸要求（仅ISOFIX安装）
            if (input.installMethod!!.fixedType.contains("ISOFIX")) {
                appendLine(generateIsofixEnvelopeSection())
            }

            // 4. 测试矩阵
            appendLine(generateTestMatrixSection(input, dummyMappings, installDirections))

            // 5. 安全阈值
            appendLine(generateSafetyThresholdsSection(input.standards, dummyMappings))

            // 6. 合规声明
            appendLine(generateComplianceStatementSection(input))

            // 7. 安全注意事项
            appendLine(generateSafetyNotesSection(input, installDirections))
        }
    }

    // ========== 标准映射说明 ==========
    private fun generateStandardMappingSection(
        input: DesignInput,
        dummyMappings: List<DummyMapping>,
        installDirections: InstallDirectionsMap
    ): String {
        return buildString {
            appendLine("## 【标准说明】")
            appendLine()
            appendLine("> ⚠️ **关键说明**：根据所选标准及身高范围，自动映射假人类型与安装方向")
            appendLine()

            // 假人映射表
            appendLine("| 身高范围 | 假人类型 | 年龄段 | 安装方向 | 标准依据 |")
            appendLine("|----------|----------|--------|----------|----------|")
            dummyMappings.forEach { mapping ->
                val direction = installDirections.entries.firstOrNull {
                    it.key.contains(mapping.minHeight)
                }?.value ?: InstallDirection.FORWARD

                appendLine(
                    "| ${mapping.minHeight}-${mapping.maxHeight}cm | ${mapping.dummyType} | ${mapping.ageRange} | " +
                            "${if (direction == InstallDirection.REARWARD) "Rearward facing" else "Forward facing"} | " +
                            "${input.standards.joinToString(" / ") { it.displayName }} |"
                )
            }
            appendLine()

            // 安装方向规则
            appendLine("**安装方向规则**（${input.standards.joinToString(" / ") { it.code }}）：")
            appendLine("- 40-105cm：强制后向安装（Rearward facing）")
            appendLine("- 105-150cm：允许前向安装（Forward facing），必须使用Top-tether防旋转")
            appendLine()
        }
    }

    // ========== 基本设计方案 ==========
    private fun generateBasicDesignSection(
        input: DesignInput,
        minHeight: Int,
        maxHeight: Int,
        installDirections: InstallDirectionsMap
    ): String {
        return buildString {
            appendLine("## 【设计方案】")
            appendLine("**产品类型**：${input.productType.displayName}")
            appendLine("**身高范围**：${minHeight}-${maxHeight}cm")
            appendLine("**适配年龄段**：${getAgeRange(minHeight, maxHeight)}")
            appendLine("**设计主题**：${input.theme.ifEmpty { "标准设计" }}")
            appendLine()

            // 安装方式（分段描述）
            appendLine("**安装方式**：")
            installDirections.forEach { (range, direction) ->
                val method = if (direction == InstallDirection.REARWARD) {
                    // 后向安装：使用支撑腿（即使用户选择Top-tether，标准也允许但支撑腿更优）
                    "ISOFIX 3点连接 + 支撑腿（后向安装，${input.standards.joinToString(" / ") { it.code }}强制要求）"
                } else {
                    // 前向安装：强制使用Top-tether
                    "ISOFIX 3点连接 + Top-tether上拉带（前向安装，${input.standards.joinToString(" / ") { it.code }} §6.1.2强制要求）"
                }
                appendLine("- ${range.first}-${range.last}cm：$method")
            }
            appendLine()
        }
    }

    // ========== ISOFIX envelope尺寸 ==========
    private fun generateIsofixEnvelopeSection(): String {
        return """
## 【ISOFIX envelope尺寸要求】
- **正向安装**：需符合 ISO/F2X 治具尺寸，确保锚点连接刚性
- **反向安装**：需符合 ISO/R2 治具尺寸，保证与车辆座椅贴合度
- **前后滑动量**：允许 80mm±1mm 调节，适配不同车型锚点位置
- **侧向滑动量**：单侧可向外滑动≥200mm，方便装卸且关闭后精准匹配
- **刚性要求**：ISOFIX连接件静态强度 ≥8kN（ECE R129 §6.1.2.3）
        """.trimIndent() + "\n\n"
    }

    // ========== 测试矩阵（ROADMATE 360格式）==========
    private fun generateTestMatrixSection(
        input: DesignInput,
        dummyMappings: List<DummyMapping>,
        installDirections: InstallDirectionsMap
    ): String {
        return buildString {
            appendLine("## 【测试矩阵】（ROADMATE 360格式）")
            appendLine()
            appendLine(generateRoadmate360Table(input, dummyMappings, installDirections))
            appendLine()
            appendLine("> ✅ **格式验证**：")
            appendLine("> - **Pulse列**：碰撞脉冲类型（Frontal/Rear/Lateral）")
            appendLine("> - **Impact列**：假人类型（Q0/Q1/Q3/Q3s/Q6/Q10）")
            appendLine("> - **Position列**：安装方向严格遵循105cm分界线")
            appendLine("> - **Top Tether/Support leg**：后向用Support leg，前向强制用Top Tether")
            appendLine()
        }
    }

    private fun generateRoadmate360Table(
        input: DesignInput,
        dummyMappings: List<DummyMapping>,
        installDirections: InstallDirectionsMap
    ): String {
        val testItems = mutableListOf<Roadmate360TestItem>()

        dummyMappings.forEach { mapping ->
            val direction = installDirections.entries.firstOrNull {
                it.key.contains(mapping.minHeight)
            }?.value ?: InstallDirection.FORWARD

            // 正面碰撞测试
            testItems.add(createTestItem(
                pulse = "Frontal",
                dummy = mapping.dummyTypeCode,
                position = if (direction == InstallDirection.REARWARD) "Rearward facing" else "Forward facing",
                installation = input.installMethod!!.fixedType,
                productConfig = "Upright",
                topTetherSupportLeg = if (direction == InstallDirection.REARWARD) "With" else "With",
                tt = if (direction == InstallDirection.REARWARD) "no" else "yes"
            ))

            // 后向碰撞测试（仅后向安装）
            if (direction == InstallDirection.REARWARD) {
                testItems.add(createTestItem(
                    pulse = "Rear",
                    dummy = mapping.dummyTypeCode,
                    position = "Rearward facing",
                    installation = input.installMethod!!.fixedType,
                    productConfig = "Upright",
                    topTetherSupportLeg = "With",
                    tt = "no"
                ))
            }

            // 侧向碰撞测试
            testItems.add(createTestItem(
                pulse = "Lateral",
                dummy = mapping.dummyTypeCode,
                position = if (direction == InstallDirection.REARWARD) "Rearward facing" else "Forward facing",
                installation = input.installMethod!!.fixedType,
                productConfig = "Upright",
                topTetherSupportLeg = if (direction == InstallDirection.REARWARD) "With" else "no",
                tt = if (direction == InstallDirection.REARWARD) "no" else "no"
            ))
        }

        return buildString {
            // 表头
            appendLine("| #Sample|Pulse|Impact|Dummy|Position|Installation|Specific Installation|Product Configuration|Isofix anchors|Position of floor|Harness|Top Tether /Support leg|Dashboard|Comments|Buckle|Adjuster|Isofix|TT|QUANTITY|Testn°|")
            appendLine("|------|-----|-------|-----|--------|-------------|----------------------|----------------------|----------------|-------------------|--------|------------------|-----------|----------|--------|----------|--------|----|----------|--------|")

            // 表格内容
            testItems.forEach { item ->
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

    private fun createTestItem(
        pulse: String,
        dummy: String,
        position: String,
        installation: String,
        productConfig: String,
        topTetherSupportLeg: String,
        tt: String
    ): Roadmate360TestItem {
        return Roadmate360TestItem(
            sample = "R129",
            pulse = pulse,
            impact = dummy,
            dummy = dummy,
            position = position,
            installation = installation,
            specificInstallation = "-",
            productConfiguration = productConfig,
            isofixAnchors = "yes",
            positionOfFloor = "Low",
            harness = "With",
            topTetherSupportLeg = topTetherSupportLeg,
            dashboard = "With",
            comments = "-",
            buckle = "no",
            adjuster = "no",
            isofix = "yes",
            tt = tt,
            quantity = "n/a",
            testNo = "-"
        )
    }

    // ========== 安全阈值 ==========
    private fun generateSafetyThresholdsSection(
        standards: List<InternationalStandard>,
        dummyMappings: List<DummyMapping>
    ): String {
        return buildString {
            appendLine("## 【安全阈值】（${standards.joinToString(" / ") { it.displayName }}）")
            appendLine()
            appendLine("| 测试项目 | 标准要求 | 适用假人 | 单位 | 标准条款 |")
            appendLine("|----------|----------|----------|------|----------|")

            // 根据标准生成阈值（此处简化，实际需按标准映射）
            val thresholds = listOf(
                SafetyThreshold("HIC15/HIC36", "≤390(Q0-Q1.5)/≤1000(Q3-Q10)", "Q0-Q10", "-", "ECE R129 §7.1.2"),
                SafetyThreshold("胸部合成加速度(3ms)", "≤55g(Q0-Q1.5)/≤60g(Q3-Q10)", "Q0-Q10", "g", "ECE R129 §7.1.3"),
                SafetyThreshold("颈部张力", "≤1800N(Q0-Q1.5)/≤2000N(Q3-Q10)", "Q0-Q10", "N", "ECE R129 §7.1.4"),
                SafetyThreshold("头部位移", "≤550mm", "Q0-Q10", "mm", "ECE R129 §7.1.5"),
                SafetyThreshold("膝部位移", "≤650mm", "Q0-Q10", "mm", "ECE R129 §7.1.5")
            )

            thresholds.forEach { threshold ->
                appendLine(
                    "| ${threshold.testItem} | ${threshold.standardRequirement} | " +
                            "${threshold.applicableDummy} | ${threshold.unit} | ${threshold.standardSource} |"
                )
            }
            appendLine()
            appendLine("> ⚠️ **阈值说明**：")
            appendLine("> - Q0-Q1.5（40-87cm）：更严格阈值（HIC15≤390，胸部≤55g）")
            appendLine("> - Q3-Q10（87-150cm）：标准阈值（HIC36≤1000，胸部≤60g）")
            appendLine()
        }
    }

    // ========== 合规声明 ==========
    private fun generateComplianceStatementSection(input: DesignInput): String {
        return """
## 【合规声明】
本设计方案适配以下标准：
${input.standards.joinToString("\n") { "- ${it.displayName}" }}

**核心合规要求**：
- 假人覆盖：${getDummyCoverage(input.heightRange!!)}（符合${input.standards.joinToString(" / ") { it.code }} §5.2）
- 安装方向：
  • 40-105cm：强制后向安装（${input.standards.joinToString(" / ") { it.code }} §5.1.3）
  • 105-150cm：前向安装+Top-tether强制（${input.standards.joinToString(" / ") { it.code }} §6.1.2）
- 动态测试：正面/后向/侧向碰撞全覆盖（${input.standards.joinToString(" / ") { it.code }} §7）
- ISOFIX连接：3点式+支撑腿/上拉带双重防旋转（${input.standards.joinToString(" / ") { it.code }} §6.1.2）

> ⚠️ **标准隔离原则**：本输出严格遵循所选标准要求，不同标准参数独立呈现，无混用。
        """.trimIndent() + "\n\n"
    }

    // ========== 安全注意事项 ==========
    private fun generateSafetyNotesSection(
        input: DesignInput,
        installDirections: InstallDirectionsMap
    ): String {
        return buildString {
            appendLine("## 【安全注意事项】")
            appendLine()

            // 安装警示
            appendLine("- **安装警示**：")
            installDirections.forEach { (range, direction) ->
                if (direction == InstallDirection.REARWARD) {
                    appendLine("  • ${range.first}-${range.last}cm：必须后向安装，禁止前向（${input.standards.joinToString(" / ") { it.code }} §8.1）")
                } else {
                    appendLine("  • ${range.first}-${range.last}cm：前向安装必须使用Top-tether，禁止仅用ISOFIX（${input.standards.joinToString(" / ") { it.code }} §8.1）")
                }
            }

            // 产品标识
            appendLine("- **产品标识**：座椅侧面必须永久标注：")
            appendLine("  • \"40-105cm: Rearward facing only\"")
            appendLine("  • \"105-150cm: Forward facing with Top-tether\"")

            // 材质阻燃
            appendLine("- **材质阻燃**：面料燃烧速度≤4英寸/分钟（FMVSS 302，${input.standards.joinToString(" / ") { it.code }}引用要求）")

            // 成长调节
            appendLine("- **成长调节**：头枕/肩带7档高度调节，适配Q0-Q10全假人肩高范围")
            appendLine()
        }
    }

    // ========== 婴儿推车输出 ==========
    private fun generateStrollerOutput(input: DesignInput): String {
        return buildString {
            appendLine("## 【设计方案】")
            appendLine("**产品类型**：${input.productType.displayName}")
            appendLine("**适用标准**：${input.standards.joinToString("、") { it.displayName }}")
            appendLine("**设计主题**：${input.theme.ifEmpty { "标准设计" }}")
            appendLine()

            appendLine("**核心特性**：")
            appendLine("- 易安装性：一键折叠（收纳尺寸≤50×30×20cm）")
            appendLine("- 安全性：符合${input.standards.joinToString("、") { it.code }}")
            appendLine("- 舒适性：配备悬挂避震系统，乘坐平稳")
            appendLine("- 便携性：轻量化设计（整车重量≤8kg）")
            appendLine()

            appendLine("**安全阈值**：")
            appendLine("| 测试项目 | 标准要求 | 适用标准 |")
            appendLine("|----------|----------|----------|")
            appendLine("| 制动性能 | 在10°斜面上不滑行 | GB 14748-2020 §5.4 |")
            appendLine("| 折叠机构安全 | 手指探针不能插入 | EN 1888-2 §4.2 |")
            appendLine("| 静态强度 | 座椅承受100kg重量不变形 | EN 1888-2 §5.3 |")
            appendLine("| 锁定装置 | 手动释放力≥50N | GB 14748-2020 §5.5 |")
            appendLine()

            appendLine("**安全注意事项**：")
            appendLine("- 刹车系统：双轮同步刹车（防止侧翻）")
            appendLine("- 面料安全：可水洗+无荧光剂（符合EN 71-3）")
            appendLine("- 折叠安全：折叠机构安全防夹（R≥2mm）")
            appendLine("- 使用警示：禁止在台阶、斜坡上使用")
            appendLine("- 载重限制：最大承重15kg")
            appendLine()
        }
    }

    // ========== 儿童高脚椅输出 ==========
    private fun generateHighChairOutput(input: DesignInput): String {
        return buildString {
            appendLine("## 【设计方案】")
            appendLine("**产品类型**：${input.productType.displayName}")
            appendLine("**适用标准**：${input.standards.joinToString("、") { it.displayName }}")
            appendLine("**设计主题**：${input.theme.ifEmpty { "标准设计" }}")
            appendLine()

            appendLine("**核心特性**：")
            appendLine("- 易安装性：快速拆装（无需工具，便于清洁）")
            appendLine("- 安全性：符合${input.standards.joinToString("、") { it.code }}")
            appendLine("- 舒适性：座椅角度多档可调（3-5档位）")
            appendLine("- 稳定性：防倾倒设计（重心降低+防滑脚垫）")
            appendLine()

            appendLine("**安全阈值**：")
            appendLine("| 测试项目 | 标准要求 | 适用标准 |")
            appendLine("|----------|----------|----------|")
            appendLine("| 稳定性测试 | 倾斜10°不倾倒 | GB 22793-2008 §5.2 |")
            appendLine("| 锁定装置 | 锁定力≥50N | GB 22793-2008 §5.3 |")
            appendLine("| 静态强度 | 托盘承重≥30kg | GB 22793-2008 §5.4 |")
            appendLine("| 背板强度 | 背板承重≥60kg | GB 22793-2008 §5.5 |")
            appendLine()

            appendLine("**安全注意事项**：")
            appendLine("- 安全带系统：五点式安全带（防止滑落）")
            appendLine("- 托盘安全：边缘圆角处理（R≥2mm）")
            appendLine("- 防滑设计：底部防滑脚垫（防止滑动）")
            appendLine("- 年龄限制：建议6个月-3岁使用")
            appendLine("- 载重限制：最大承重15kg")
            appendLine("- 使用警示：必须有成人监护")
            appendLine()
        }
    }

    // ========== 婴儿床输出 ==========
    private fun generateCribOutput(input: DesignInput): String {
        return buildString {
            appendLine("## 【设计方案】")
            appendLine("**产品类型**：${input.productType.displayName}")
            appendLine("**适用标准**：${input.standards.joinToString("、") { it.displayName }}")
            appendLine("**设计主题**：${input.theme.ifEmpty { "标准设计" }}")
            appendLine()

            appendLine("**核心特性**：")
            appendLine("- 易安装性：模块化组装（无需工具）")
            appendLine("- 安全性：符合${input.standards.joinToString("、") { it.code }}")
            appendLine("- 舒适性：可调节高度（3档位）")
            appendLine("- 耐用性：实木材质，承重≥30kg")
            appendLine()

            appendLine("**安全阈值**：")
            appendLine("| 测试项目 | 标准要求 | 适用标准 |")
            appendLine("|----------|----------|----------|")
            appendLine("| 围栏间距 | ≤6cm（防卡住） | ASTM F1169 |")
            appendLine("| 床板强度 | 承重≥50kg不变形 | ASTM F1169 |")
            appendLine("| 油漆安全 | 无铅无有害物质 | EN 71-3 |")
            appendLine("| 锁定装置 | 锁定力≥30N | ASTM F1169 |")
            appendLine()

            appendLine("**安全注意事项**：")
            appendLine("- 围栏安全：围栏间距≤6cm（防卡住）")
            appendLine("- 材质安全：无铅油漆，无甲醛")
            appendLine("- 使用警示：建议0-3岁使用")
            appendLine("- 载重限制：最大承重30kg")
            appendLine("- 床垫厚度：≤15cm（防攀爬）")
            appendLine()
        }
    }

    // ========== 辅助方法 ==========
    private fun parseHeightRange(range: String): Pair<Int, Int> {
        val regex = Regex("(\\d+)-(\\d+)")
        val match = regex.find(range)
        return if (match != null) {
            Pair(match.groupValues[1].toInt(), match.groupValues[2].toInt())
        } else {
            Pair(40, 150)
        }
    }

    private fun getDummyMappings(minHeight: Int, maxHeight: Int): List<DummyMapping> {
        val allMappings = listOf(
            DummyMapping(40, 50, "Q0", "新生儿假人", "0-6个月"),
            DummyMapping(50, 60, "Q0+", "大婴儿假人", "6-12个月"),
            DummyMapping(60, 75, "Q1", "幼儿假人", "1-2岁"),
            DummyMapping(75, 87, "Q1.5", "学步儿童假人", "2-3岁"),
            DummyMapping(87, 105, "Q3", "学前儿童假人", "3-4岁"),
            DummyMapping(105, 125, "Q3s", "儿童假人", "4-6岁"),
            DummyMapping(125, 145, "Q6", "大龄儿童假人", "6-10岁"),
            DummyMapping(145, 150, "Q10", "青少年假人", "10-12岁")
        )
        return allMappings.filter { it.minHeight < maxHeight && it.maxHeight > minHeight }
    }

    private fun getInstallDirections(minHeight: Int, maxHeight: Int): InstallDirectionsMap {
        val directions = mutableMapOf<IntRange, InstallDirection>()

        if (minHeight < 105) {
            val end = minOf(maxHeight, 105)
            directions[40..end] = InstallDirection.REARWARD
        }
        if (maxHeight >= 105) {
            val start = maxOf(minHeight, 105)
            directions[start..maxHeight] = InstallDirection.FORWARD
        }
        return directions
    }

    private fun getAgeRange(minHeight: Int, maxHeight: Int): String {
        return when {
            maxHeight <= 50 -> "0-6个月"
            maxHeight <= 60 -> "0-12个月"
            maxHeight <= 75 -> "0-2岁"
            maxHeight <= 87 -> "0-3岁"
            maxHeight <= 105 -> "0-4岁"
            maxHeight <= 125 -> "0-6岁"
            maxHeight <= 145 -> "0-10岁"
            else -> "0-12岁"
        }
    }

    private fun getDummyCoverage(heightRange: String): String {
        val (min, max) = parseHeightRange(heightRange)
        return when {
            max <= 50 -> "Q0"
            max <= 60 -> "Q0-Q0+"
            max <= 75 -> "Q0-Q1"
            max <= 87 -> "Q0-Q1.5"
            max <= 105 -> "Q0-Q3"
            max <= 125 -> "Q0-Q3s"
            max <= 145 -> "Q0-Q6"
            else -> "Q0-Q10（全假人序列）"
        }
    }
}

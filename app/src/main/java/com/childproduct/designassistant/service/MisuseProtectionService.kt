package com.childproduct.designassistant.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 用户误用防护设计指导服务
 * 
 * 提供防止用户（特别是儿童）误用CRS产品的设计指导
 * 符合FMVSS 213隐含要求及NHTSA测试模拟误用场景
 */
class MisuseProtectionService {

    /**
     * 误用防护设计要素
     */
    data class MisuseProtectionElement(
        val elementId: String,
        val name: String,
        val category: MisuseCategory,
        val description: String,
        val designRequirements: List<DesignRequirement>,
        val standardReference: String,
        val riskLevel: RiskLevel,
        val testMethod: String
    )

    /**
     * 误用类别
     */
    enum class MisuseCategory(val displayName: String) {
        INSTALLATION_ERROR("安装错误"),
        OPERATION_MISUSE("操作误用"),
        CHILD_ACCESS("儿童接触"),
        ENVIRONMENTAL_MISUSE("环境误用"),
        MAINTENANCE_NEGLECT("维护缺失")
    }

    /**
     * 设计要求
     */
    data class DesignRequirement(
        val requirementId: String,
        val name: String,
        val description: String,
        val parameter: String?,
        val targetValue: String?,
        val verificationMethod: String,
        val critical: Boolean
    )

    /**
     * 风险等级
     */
    enum class RiskLevel(val displayName: String, val value: Int) {
        LOW("低", 1),
        MEDIUM("中", 2),
        HIGH("高", 3),
        CRITICAL("关键", 4)
    }

    /**
     * 误用场景评估
     */
    data class MisuseScenario(
        val scenarioId: String,
        val name: String,
        val description: String,
        val frequency: Frequency,
        val severity: Severity,
        val mitigations: List<String>,
        val recommendedTests: List<String>
    )

    /**
     * 频率
     */
    enum class Frequency(val displayName: String, val score: Int) {
        RARE("罕见", 1),
        OCCASIONAL("偶尔", 2),
        PROBABLE("可能", 3),
        FREQUENT("频繁", 4)
    }

    /**
     * 严重性
     */
    enum class Severity(val displayName: String, val score: Int) {
        MINOR("轻微", 1),
        MODERATE("中等", 2),
        SERIOUS("严重", 3),
        CATASTROPHIC("灾难性", 4)
    }

    /**
     * 安装防错设计要素
     */
    data class InstallationErrorProofing(
        val buckleAntiHalfClick: BuckleDesign,
        val latchInterface: LatchInterface,
        val tetherAnchor: TetherAnchorDesign,
        val visualIndicators: List<VisualIndicator>,
        val instructionDesign: InstructionDesign
    )

    /**
     * 卡扣设计
     */
    data class BuckleDesign(
        val requiresFullClick: Boolean,
        val audibleFeedback: Boolean,
        val visualFeedback: Boolean,
        val releaseForceMinN: Double,
        val releaseForceMaxN: Double,
        val standardReference: String
    )

    /**
     * LATCH接口设计
     */
    data class LatchInterface(
        val oneClickLock: Boolean,
        val colorCoding: ColorCoding,
        val lockingIndicator: String,
        val releaseForceMinN: Double,
        val releaseForceMaxN: Double
    )

    /**
     * 颜色编码
     */
    data class ColorCoding(
        val unlockedColor: String,
        val lockedColor: String,
        val description: String
    )

    /**
     * Tether锚点设计
     */
    data class TetherAnchorDesign(
        val easyToAttach: Boolean,
        val visualLocking: Boolean,
        val lengthAdjustable: Boolean,
        val maximumLengthMm: Double,
        val minimumLengthMm: Double
    )

    /**
     * 视觉指示器
     */
    data class VisualIndicator(
        val indicatorId: String,
        val location: String,
        val type: IndicatorType,
        val colors: ColorScheme,
        val meaning: String
    )

    /**
     * 指示器类型
     */
    enum class IndicatorType(val displayName: String) {
        COLOR_INDICATOR("颜色指示"),
        POSITION_INDICATOR("位置指示"),
        ICON_INDICATOR("图标指示"),
        TEXT_INDICATOR("文字指示")
    }

    /**
     * 颜色方案
     */
    data class ColorScheme(
        val safeColor: String,
        val warningColor: String,
        val dangerColor: String
    )

    /**
     * 说明书设计
     */
    data class InstructionDesign(
        val threePartPrompts: Boolean,
        val pictorialInstructions: Boolean,
        val warningPlacement: WarningPlacement,
        val stepByStepGuide: Boolean
    )

    /**
     * 警告放置
     */
    enum class WarningPlacement(val displayName: String) {
        FRONT_COVER("封面警告"),
        SECTION_START("章节开始"),
        CRITICAL_POINTS("关键点"),
        QUICK_REFERENCE("快速参考卡")
    }

    /**
     * 儿童操作限制设计
     */
    data class ChildOperationRestriction(
        val restraintAdjustment: AdjustmentDesign,
        val removableComponents: List<RemovableComponent>,
        val accessibility: AccessibilityDesign,
        val physicalBarriers: List<PhysicalBarrier>
    )

    /**
     * 调节设计
     */
    data class AdjustmentDesign(
        val buttonDiameterMinMm: Double,
        val operatingForceMinN: Double,
        val locationHeightMm: Double,
        val requiresTwoHand: Boolean,
        val description: String
    )

    /**
     * 可拆卸组件
     */
    data class RemovableComponent(
        val componentId: String,
        val name: String,
        val requiresTool: Boolean,
        val toolType: String?,
        val removalSteps: Int,
        val criticalForSafety: Boolean
    )

    /**
     * 可达性设计
     */
    data class AccessibilityDesign(
        val adultReachable: Boolean,
        val childUnreachable: Boolean,
        val reachHeightMm: Double,
        val description: String
    )

    /**
     * 物理屏障
     */
    data class PhysicalBarrier(
        val barrierId: String,
        val type: BarrierType,
        val location: String,
        val material: String,
        val effectiveness: Effectiveness
    )

    /**
     * 屏障类型
     */
    enum class BarrierType(val displayName: String) {
        COVER("盖板"),
        FLAP("挡板"),
        GUARD("护板"),
        ENCLOSURE("封闭")
    }

    /**
     * 有效性
     */
    enum class Effectiveness(val displayName: String, val score: Int) {
        LOW("低", 1),
        MEDIUM("中", 2),
        HIGH("高", 3),
        VERY_HIGH("很高", 4)
    }

    /**
     * NHTSA模拟误用测试场景
     */
    private val misuseScenarios = listOf(
        MisuseScenario(
            scenarioId = "SCEN-001",
            name = "Tether未固定",
            description = "前向安装时顶部系带（tether）未正确固定到车辆锚点",
            frequency = Frequency.PROBABLE,
            severity = Severity.SERIOUS,
            mitigations = listOf(
                "设计一键锁定视觉指示（绿色=锁定，红色=未锁定）",
                "在tether钩附近标注明显的固定警告图示",
                "在说明书中强调tether的重要性"
            ),
            recommendedTests = listOf(
                "FMVSS 213 S5.4.3.5(d) 卡扣完整性测试",
                "用户误用模拟测试：未固定tether的碰撞测试"
            )
        ),
        MisuseScenario(
            scenarioId = "SCEN-002",
            name = "安全带未扣紧",
            description = "车辆安全带或CRS带子未完全扣紧，存在松弛",
            frequency = Frequency.PROBABLE,
            severity = Severity.SERIOUS,
            mitigations = listOf(
                "卡扣采用防半扣设计，未完全扣合时发出声光提示",
                "设计明显的锁定位置指示器",
                "在说明书中提供清晰的扣紧图示和步骤"
            ),
            recommendedTests = listOf(
                "FMVSS 213 S5.4.3.5(d) 卡扣完整性测试",
                "用户误用模拟测试：松弛带子的碰撞测试"
            )
        ),
        MisuseScenario(
            scenarioId = "SCEN-003",
            name = "Dummy放置不当",
            description = "儿童放置位置不正确，如肩带位置错误、头枕高度不适",
            frequency = Frequency.OCCASIONAL,
            severity = Severity.MODERATE,
            mitigations = listOf(
                "肩带设计明显的正确位置标记（颜色或符号）",
                "头枕设计高度标尺，避免儿童调节",
                "在说明书中提供详细的放置指南"
            ),
            recommendedTests = listOf(
                "用户误用模拟测试：不当dummy放置的碰撞测试"
            )
        ),
        MisuseScenario(
            scenarioId = "SCEN-004",
            name = "Rear-facing禁放前排气囊位",
            description = "后向安装的CRS放置在前排有安全气囊的位置",
            frequency = Frequency.OCCASIONAL,
            severity = Severity.CATASTROPHIC,
            mitigations = listOf(
                "产品标签和说明书显著警告：'NEVER place rear-facing in front with airbag'",
                "在包装盒上添加醒目的警告图示",
                "在说明书中提供详细的安装位置指导"
            ),
            recommendedTests = listOf(
                "FMVSS 213 S5.8 标签要求测试",
                "用户可读性测试：警告信息理解度评估"
            )
        ),
        MisuseScenario(
            scenarioId = "SCEN-005",
            name = "儿童自行调节约束带",
            description = "儿童自行解开或调节约束带，导致保护失效",
            frequency = Frequency.OCCASIONAL,
            severity = Severity.SERIOUS,
            mitigations = listOf(
                "调节按钮直径≥10mm，操作力≥10N，儿童难以操作",
                "按钮位置设计在成人可触及区域（高度≥500mm）",
                "增加双层锁定机制（长按+旋转）"
            ),
            recommendedTests = listOf(
                "儿童操作力测试：确保儿童无法操作",
                "成人操作测试：确保成人可正常操作"
            )
        ),
        MisuseScenario(
            scenarioId = "SCEN-006",
            name = "儿童自行拆卸组件",
            description = "儿童自行拆卸头枕、坐垫等组件，导致结构失效",
            frequency = Frequency.RARE,
            severity = Severity.SERIOUS,
            mitigations = listOf(
                "可拆卸组件需工具拆卸（如内六角扳手）",
                "设计隐蔽的拆卸点，不易发现",
                "重要结构组件（如头枕）禁止拆卸"
            ),
            recommendedTests = listOf(
                "儿童拆卸测试：儿童无法拆卸安全组件",
                "成人拆卸测试：成人可正常拆卸维护"
            )
        ),
        MisuseScenario(
            scenarioId = "SCEN-007",
            name = "LATCH安装不到位",
            description = "LATCH连接器未完全插入车辆锚点，或未锁定",
            frequency = Frequency.PROBABLE,
            severity = Severity.SERIOUS,
            mitigations = listOf(
                "设计一键锁定+视觉指示（红色=未锁定，绿色=锁定）",
                "LATCH连接器设计明显的插入位置标记",
                "在说明书中提供详细的LATCH安装图示"
            ),
            recommendedTests = listOf(
                "FMVSS 225 LATCH系统测试",
                "用户误用模拟测试：LATCH未锁定的碰撞测试"
            )
        )
    )

    /**
     * 误用防护设计要素库
     */
    private val protectionElements = listOf(
        MisuseProtectionElement(
            elementId = "MIS-001",
            name = "卡扣防半扣设计",
            category = MisuseCategory.INSTALLATION_ERROR,
            description = "确保卡扣必须完全扣合才能工作，未完全扣合时发出声光提示",
            designRequirements = listOf(
                DesignRequirement(
                    requirementId = "REQ-001-1",
                    name = "完全扣合要求",
                    description = "卡扣必须完全扣合才能锁定，半扣状态无法锁定",
                    parameter = null,
                    targetValue = null,
                    verificationMethod = "功能测试",
                    critical = true
                ),
                DesignRequirement(
                    requirementId = "REQ-001-2",
                    name = "声觉反馈",
                    description = "卡扣完全扣合时发出清晰的'咔嗒'声",
                    parameter = "声压级",
                    targetValue = "≥60 dB",
                    verificationMethod = "声压级测试",
                    critical = true
                ),
                DesignRequirement(
                    requirementId = "REQ-001-3",
                    name = "视觉指示",
                    description = "卡扣锁定后显示绿色，未锁定显示红色",
                    parameter = "颜色对比度",
                    targetValue = "≥4.5:1",
                    verificationMethod = "视觉对比度测试",
                    critical = true
                ),
                DesignRequirement(
                    requirementId = "REQ-001-4",
                    name = "释放力范围",
                    description = "卡扣释放力需在规定范围内，确保成人可操作，儿童难操作",
                    parameter = "释放力",
                    targetValue = "40-90 N",
                    verificationMethod = "力测试",
                    critical = true
                )
            ),
            standardReference = "FMVSS 213 S5.4.3.5(d)",
            riskLevel = RiskLevel.CRITICAL,
            testMethod = "FMVSS 213 S5.4.3.5(d) 卡扣完整性测试"
        ),
        MisuseProtectionElement(
            elementId = "MIS-002",
            name = "LATCH一键锁定设计",
            category = MisuseCategory.INSTALLATION_ERROR,
            description = "LATCH接口设计为一键锁定，并配备视觉指示器",
            designRequirements = listOf(
                DesignRequirement(
                    requirementId = "REQ-002-1",
                    name = "一键锁定",
                    description = "LATCH连接器插入车辆锚点时自动锁定",
                    parameter = "锁定力",
                    targetValue = "≤30 N",
                    verificationMethod = "锁定力测试",
                    critical = true
                ),
                DesignRequirement(
                    requirementId = "REQ-002-2",
                    name = "颜色编码",
                    description = "红色表示未锁定，绿色表示已锁定",
                    parameter = "颜色识别度",
                    targetValue = "≥90%",
                    verificationMethod = "用户识别度测试",
                    critical = true
                ),
                DesignRequirement(
                    requirementId = "REQ-002-3",
                    name = "释放力范围",
                    description = "LATCH释放力需在规定范围内",
                    parameter = "释放力",
                    targetValue = "40-160 N",
                    verificationMethod = "力测试",
                    critical = true
                )
            ),
            standardReference = "FMVSS 225",
            riskLevel = RiskLevel.CRITICAL,
            testMethod = "FMVSS 225 LATCH系统测试"
        ),
        MisuseProtectionElement(
            elementId = "MIS-003",
            name = "约束带调节按钮设计",
            category = MisuseCategory.CHILD_ACCESS,
            description = "调节按钮设计为成人可操作、儿童无法误触",
            designRequirements = listOf(
                DesignRequirement(
                    requirementId = "REQ-003-1",
                    name = "按钮直径",
                    description = "调节按钮直径需足够大，便于成人操作",
                    parameter = "直径",
                    targetValue = "≥10 mm",
                    verificationMethod = "尺寸测量",
                    critical = true
                ),
                DesignRequirement(
                    requirementId = "REQ-003-2",
                    name = "操作力",
                    description = "调节按钮操作力需足够大，儿童难以操作",
                    parameter = "操作力",
                    targetValue = "≥10 N",
                    verificationMethod = "力测试",
                    critical = true
                ),
                DesignRequirement(
                    requirementId = "REQ-003-3",
                    name = "位置高度",
                    description = "调节按钮位置需在成人可达但儿童难以触及的区域",
                    parameter = "高度",
                    targetValue = "≥500 mm",
                    verificationMethod = "尺寸测量",
                    critical = false
                )
            ),
            standardReference = "FMVSS 213 S5.1.1",
            riskLevel = RiskLevel.HIGH,
            testMethod = "儿童操作力测试"
        ),
        MisuseProtectionElement(
            elementId = "MIS-004",
            name = "可拆卸组件工具限制",
            category = MisuseCategory.CHILD_ACCESS,
            description = "可拆卸组件需工具拆卸，避免儿童自行拆卸",
            designRequirements = listOf(
                DesignRequirement(
                    requirementId = "REQ-004-1",
                    name = "工具要求",
                    description = "可拆卸组件必须使用工具拆卸",
                    parameter = "工具类型",
                    targetValue = "内六角扳手/螺丝刀",
                    verificationMethod = "拆卸测试",
                    critical = true
                ),
                DesignRequirement(
                    requirementId = "REQ-004-2",
                    name = "拆卸点隐蔽",
                    description = "拆卸点设计在不易发现的位置",
                    parameter = "可见度",
                    targetValue = "≤30%",
                    verificationMethod = "用户可见度测试",
                    critical = false
                ),
                DesignRequirement(
                    requirementId = "REQ-004-3",
                    name = "重要组件禁止拆卸",
                    description = "关键安全组件（如头枕）禁止拆卸",
                    parameter = null,
                    targetValue = null,
                    verificationMethod = "结构审查",
                    critical = true
                )
            ),
            standardReference = "FMVSS 213 S5.1.1",
            riskLevel = RiskLevel.HIGH,
            testMethod = "儿童拆卸测试"
        ),
        MisuseProtectionElement(
            elementId = "MIS-005",
            name = "说明书三重提示",
            category = MisuseCategory.INSTALLATION_ERROR,
            description = "高风险误用场景需使用步骤+图示+警告三重提示",
            designRequirements = listOf(
                DesignRequirement(
                    requirementId = "REQ-005-1",
                    name = "分步骤指导",
                    description = "每个安装步骤编号清晰，便于跟随",
                    parameter = "步骤清晰度",
                    targetValue = "100%",
                    verificationMethod = "用户测试",
                    critical = true
                ),
                DesignRequirement(
                    requirementId = "REQ-005-2",
                    name = "图示说明",
                    description = "关键步骤配备清晰的图示",
                    parameter = "图示比例",
                    targetValue = "≥80%",
                    verificationMethod = "视觉审查",
                    critical = true
                ),
                DesignRequirement(
                    requirementId = "REQ-005-3",
                    name = "警告标记",
                    description = "高风险场景使用醒目的警告（红色背景+感叹号）",
                    parameter = "警告可见度",
                    targetValue = "≥95%",
                    verificationMethod = "可见度测试",
                    critical = true
                )
            ),
            standardReference = "FMVSS 213 S5.8",
            riskLevel = RiskLevel.HIGH,
            testMethod = "用户可读性测试"
        )
    )

    /**
     * 获取所有误用场景
     */
    fun getMisuseScenarios(): List<MisuseScenario> {
        return misuseScenarios
    }

    /**
     * 根据频率筛选误用场景
     */
    fun getMisuseScenariosByFrequency(minFrequency: Frequency): List<MisuseScenario> {
        return misuseScenarios.filter { it.frequency.score >= minFrequency.score }
    }

    /**
     * 根据严重性筛选误用场景
     */
    fun getMisuseScenariosBySeverity(minSeverity: Severity): List<MisuseScenario> {
        return misuseScenarios.filter { it.severity.score >= minSeverity.score }
    }

    /**
     * 获取风险矩阵
     */
    fun getRiskMatrix(): List<RiskItem> {
        val riskItems = mutableListOf<RiskItem>()
        
        misuseScenarios.forEach { scenario ->
            val riskScore = scenario.frequency.score * scenario.severity.score
            val riskLevel = when (riskScore) {
                1 -> RiskLevel.LOW
                2 -> RiskLevel.LOW
                3, 4 -> RiskLevel.MEDIUM
                6, 8 -> RiskLevel.HIGH
                9, 12, 16 -> RiskLevel.CRITICAL
                else -> RiskLevel.LOW
            }
            
            riskItems.add(
                RiskItem(
                    scenarioId = scenario.scenarioId,
                    scenarioName = scenario.name,
                    frequency = scenario.frequency,
                    severity = scenario.severity,
                    riskScore = riskScore,
                    riskLevel = riskLevel
                )
            )
        }
        
        return riskItems.sortedByDescending { it.riskScore }
    }

    /**
     * 风险项
     */
    data class RiskItem(
        val scenarioId: String,
        val scenarioName: String,
        val frequency: Frequency,
        val severity: Severity,
        val riskScore: Int,
        val riskLevel: RiskLevel
    )

    /**
     * 获取所有防护设计要素
     */
    fun getProtectionElements(): List<MisuseProtectionElement> {
        return protectionElements
    }

    /**
     * 根据类别获取防护设计要素
     */
    fun getProtectionElementsByCategory(category: MisuseCategory): List<MisuseProtectionElement> {
        return protectionElements.filter { it.category == category }
    }

    /**
     * 根据风险等级获取防护设计要素
     */
    fun getProtectionElementsByRiskLevel(riskLevel: RiskLevel): List<MisuseProtectionElement> {
        return protectionElements.filter { it.riskLevel == riskLevel }
    }

    /**
     * 生成误用防护设计报告
     */
    suspend fun generateMisuseProtectionReport(
        selectedElements: List<MisuseProtectionElement>
    ): String = withContext(Dispatchers.Default) {
        val report = StringBuilder()
        
        report.appendLine("=" .repeat(70))
        report.appendLine("用户误用防护设计报告")
        report.appendLine("=" .repeat(70))
        report.appendLine()
        
        // 风险矩阵
        report.appendLine("-" .repeat(70))
        report.appendLine("误用场景风险矩阵")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        report.appendLine("场景ID | 场景名称 | 频率 | 严重性 | 风险分数 | 风险等级")
        report.appendLine("-" .repeat(70))
        
        getRiskMatrix().forEach { item ->
            report.appendLine("${item.scenarioId} | ${item.scenarioName} | ${item.frequency.displayName} | ${item.severity.displayName} | ${item.riskScore} | ${item.riskLevel.displayName}")
        }
        report.appendLine()
        
        // 防护设计要素
        report.appendLine("-" .repeat(70))
        report.appendLine("已选择的防护设计要素")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        selectedElements.forEach { element ->
            report.appendLine("要素ID：${element.elementId}")
            report.appendLine("名称：${element.name}")
            report.appendLine("类别：${element.category.displayName}")
            report.appendLine("描述：${element.description}")
            report.appendLine("标准参考：${element.standardReference}")
            report.appendLine("风险等级：${element.riskLevel.displayName}")
            report.appendLine("测试方法：${element.testMethod}")
            report.appendLine()
            report.appendLine("设计要求：")
            element.designRequirements.forEach { requirement ->
                val criticalMark = if (requirement.critical) "[关键]" else ""
                report.appendLine("  - ${requirement.name} $criticalMark")
                report.appendLine("    描述：${requirement.description}")
                if (requirement.parameter != null && requirement.targetValue != null) {
                    report.appendLine("    参数：${requirement.parameter} = ${requirement.targetValue}")
                }
                report.appendLine("    验证方法：${requirement.verificationMethod}")
            }
            report.appendLine()
        }
        
        // 优先级建议
        report.appendLine("-" .repeat(70))
        report.appendLine("设计优先级建议")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        val criticalElements = selectedElements.filter { it.riskLevel == RiskLevel.CRITICAL }
        val highElements = selectedElements.filter { it.riskLevel == RiskLevel.HIGH }
        
        if (criticalElements.isNotEmpty()) {
            report.appendLine("【关键优先级】以下要素必须优先实施：")
            criticalElements.forEach { element ->
                report.appendLine("  - ${element.name} (${element.elementId})")
            }
            report.appendLine()
        }
        
        if (highElements.isNotEmpty()) {
            report.appendLine("【高优先级】以下要素建议在关键要素完成后实施：")
            highElements.forEach { element ->
                report.appendLine("  - ${element.name} (${element.elementId})")
            }
            report.appendLine()
        }
        
        // 合规建议
        report.appendLine("-" .repeat(70))
        report.appendLine("合规建议")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        report.appendLine("1. 所有关键风险等级的防护要素必须实施")
        report.appendLine("2. 对高频高严重性场景（风险分数≥6）必须提供防护措施")
        report.appendLine("3. 进行用户测试验证防护措施的有效性")
        report.appendLine("4. 在说明书中突出标注高风险误用场景的警告")
        report.appendLine("5. 定期更新防护设计，基于实际误用数据")
        
        report.toString()
    }

    /**
     * 生成安装防错设计指南
     */
    fun generateInstallationErrorProofingGuide(): String {
        val guide = StringBuilder()
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("安装防错设计指南")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        
        guide.appendLine("【卡扣防半扣设计】")
        guide.appendLine("1. 卡扣必须完全扣合才能锁定，半扣状态无法锁定")
        guide.appendLine("2. 卡扣完全扣合时发出清晰的'咔嗒'声（声压级≥60 dB）")
        guide.appendLine("3. 卡扣锁定后显示绿色，未锁定显示红色（颜色对比度≥4.5:1）")
        guide.appendLine("4. 卡扣释放力范围：40-90 N（成人可操作，儿童难操作）")
        guide.appendLine("5. 符合标准：FMVSS 213 S5.4.3.5(d)")
        guide.appendLine()
        
        guide.appendLine("【LATCH一键锁定设计】")
        guide.appendLine("1. LATCH连接器插入车辆锚点时自动锁定")
        guide.appendLine("2. 锁定力≤30 N，确保轻松锁定")
        guide.appendLine("3. 颜色编码：红色=未锁定，绿色=已锁定")
        guide.appendLine("4. 颜色识别度≥90%，确保用户可识别")
        guide.appendLine("5. 释放力范围：40-160 N，成人可操作")
        guide.appendLine("6. 符合标准：FMVSS 225")
        guide.appendLine()
        
        guide.appendLine("【Tether锚点设计】")
        guide.appendLine("1. Tether钩设计为易于固定到车辆锚点")
        guide.appendLine("2. 设计视觉锁定指示，确认固定到位")
        guide.appendLine("3. Tether长度可调节，范围根据标准要求")
        guide.appendLine("4. 最大长度限制，防止过长导致松弛")
        guide.appendLine()
        
        guide.appendLine("【视觉指示器设计】")
        guide.appendLine("1. 使用颜色编码：绿色=安全，红色=警告/危险")
        guide.appendLine("2. 位置标注清晰，易于用户观察")
        guide.appendLine("3. 符号国际通用，避免歧义")
        guide.appendLine()
        
        guide.appendLine("【说明书设计】")
        guide.appendLine("1. 每个安装步骤编号清晰，便于跟随")
        guide.appendLine("2. 关键步骤配备清晰的图示（图示比例≥80%）")
        guide.appendLine("3. 高风险场景使用醒目的警告（红色背景+感叹号）")
        guide.appendLine("4. 警告可见度≥95%，确保用户注意")
        guide.appendLine("5. 封面放置最重要的警告（如Rear-facing禁放前排气囊位）")
        guide.appendLine("6. 符合标准：FMVSS 213 S5.8")
        guide.appendLine()
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("验证测试")
        guide.appendLine("=" .repeat(70))
        guide.appendLine("1. FMVSS 213 S5.4.3.5(d) 卡扣完整性测试")
        guide.appendLine("2. FMVSS 225 LATCH系统测试")
        guide.appendLine("3. 用户误用模拟测试：未固定tether的碰撞测试")
        guide.appendLine("4. 用户误用模拟测试：松弛带子的碰撞测试")
        guide.appendLine("5. 用户可读性测试：警告信息理解度评估")
        
        return guide.toString()
    }

    /**
     * 生成儿童操作限制设计指南
     */
    fun generateChildOperationRestrictionGuide(): String {
        val guide = StringBuilder()
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("儿童操作限制设计指南")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        
        guide.appendLine("【约束带调节按钮设计】")
        guide.appendLine("1. 调节按钮直径≥10 mm，便于成人操作")
        guide.appendLine("2. 调节按钮操作力≥10 N，儿童难以操作")
        guide.appendLine("3. 按钮位置高度≥500 mm，成人可达儿童难触")
        guide.appendLine("4. 可增加双层锁定机制（长按+旋转）")
        guide.appendLine("5. 符合标准：FMVSS 213 S5.1.1")
        guide.appendLine()
        
        guide.appendLine("【可拆卸组件工具限制】")
        guide.appendLine("1. 可拆卸组件必须使用工具拆卸（内六角扳手/螺丝刀）")
        guide.appendLine("2. 拆卸点设计在不易发现的位置（可见度≤30%）")
        guide.appendLine("3. 关键安全组件（如头枕）禁止拆卸")
        guide.appendLine("4. 重要结构组件禁止儿童拆卸")
        guide.appendLine("5. 符合标准：FMVSS 213 S5.1.1")
        guide.appendLine()
        
        guide.appendLine("【可达性设计】")
        guide.appendLine("1. 调节和操作部件设计在成人可达区域")
        guide.appendLine("2. 避免儿童能够触及到关键操作部件")
        guide.appendLine("3. 使用物理屏障防止儿童接触")
        guide.appendLine()
        
        guide.appendLine("【物理屏障设计】")
        guide.appendLine("1. 使用盖板/挡板/护板覆盖关键部件")
        guide.appendLine("2. 屏障材料坚固，儿童难以破坏")
        guide.appendLine("3. 屏障不影响成人正常操作")
        guide.appendLine()
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("验证测试")
        guide.appendLine("=" .repeat(70))
        guide.appendLine("1. 儿童操作力测试：确保儿童无法操作")
        guide.appendLine("2. 成人操作测试：确保成人可正常操作")
        guide.appendLine("3. 儿童拆卸测试：儿童无法拆卸安全组件")
        guide.appendLine("4. 成人拆卸测试：成人可正常拆卸维护")
        
        return guide.toString()
    }
}

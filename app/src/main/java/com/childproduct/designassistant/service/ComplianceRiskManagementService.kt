package com.childproduct.designassistant.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * 合规风险管理系统
 * 
 * 提供全面的风险评估和预警机制，确保产品合规性
 */
class ComplianceRiskManagementService {

    /**
     * 风险评估
     */
    data class RiskAssessment(
        val assessmentId: String,
        val productId: String,
        val assessmentDate: Date,
        val overallRiskScore: Double,  // 0-100
        val riskLevel: RiskLevel,
        val riskCategories: List<CategoryRisk>,
        val topRisks: List<RiskItem>,
        val recommendations: List<String>
    )

    /**
     * 风险等级
     */
    enum class RiskLevel(val displayName: String, val score: Int, val color: String) {
        LOW("低", 1, "绿色"),
        MEDIUM("中", 2, "黄色"),
        HIGH("高", 3, "橙色"),
        CRITICAL("关键", 4, "红色")
    }

    /**
     * 类别风险
     */
    data class CategoryRisk(
        val category: RiskCategory,
        val riskScore: Double,  // 0-100
        val riskLevel: RiskLevel,
        val riskCount: Int,
        val description: String
    )

    /**
     * 风险类别
     */
    enum class RiskCategory(val displayName: String) {
        STANDARD_COMPLIANCE("标准合规"),
        MATERIAL("材料合规"),
        TESTING("测试合规"),
        DOCUMENTATION("文档合规"),
        SUPPLY_CHAIN("供应链合规"),
        AFTER_SALES("售后合规"),
        PATENT("专利风险"),
        ENVIRONMENTAL("环境合规")
    }

    /**
     * 风险项
     */
    data class RiskItem(
        val riskId: String,
        val category: RiskCategory,
        val name: String,
        val description: String,
        val probability: Double,  // 0-1
        val impact: Double,  // 0-1
        val riskScore: Double,  // probability * impact * 100
        val riskLevel: RiskLevel,
        val mitigationStatus: MitigationStatus,
        val lastReviewDate: Date
    )

    /**
     * 缓解状态
     */
    enum class MitigationStatus(val displayName: String) {
        NOT_STARTED("未开始"),
        IN_PROGRESS("进行中"),
        MITIGATED("已缓解"),
        ACCEPTED("已接受"),
        CLOSED("已关闭")
    }

    /**
     * 风险预警
     */
    data class RiskAlert(
        val alertId: String,
        val alertType: AlertType,
        val severity: AlertSeverity,
        val title: String,
        val description: String,
        val riskItems: List<RiskItem>,
        val affectedProducts: List<String>,
        val alertDate: Date,
        val actionRequired: String,
        val dueDate: Date,
        val status: AlertStatus
    )

    /**
     * 预警类型
     */
    enum class AlertType(val displayName: String) {
        STANDARD_UPDATE("标准更新"),
        COMPLIANCE_VIOLATION("合规违规"),
        SUPPLIER_ISSUE("供应商问题"),
        MATERIAL_DEFECT("材料缺陷"),
        DOCUMENT_EXPIRY("文档过期"),
        CERTIFICATION_EXPIRY("认证过期"),
        RECALL_RISK("召回风险"),
        PATENT_ISSUE("专利问题")
    }

    /**
     * 预警严重性
     */
    enum class AlertSeverity(val displayName: String, val score: Int) {
        INFO("信息", 1),
        WARNING("警告", 2),
        ERROR("错误", 3),
        CRITICAL("关键", 4)
    }

    /**
     * 预警状态
     */
    enum class AlertStatus(val displayName: String) {
        OPEN("打开"),
        IN_PROGRESS("进行中"),
        RESOLVED("已解决"),
        CLOSED("已关闭")
    }

    /**
     * 风险矩阵
     */
    data class RiskMatrix(
        val matrixId: String,
        val assessmentId: String,
        val date: Date,
        val items: List<MatrixItem>
    )

    /**
     * 矩阵项
     */
    data class MatrixItem(
        val probability: Probability,
        val impact: Impact,
        val risks: List<RiskItem>
    )

    /**
     * 概率
     */
    enum class Probability(val displayName: String, val score: Int) {
        RARE("罕见", 1),
        UNLIKELY("不太可能", 2),
        POSSIBLE("可能", 3),
        LIKELY("很可能", 4),
        CERTAIN("肯定", 5)
    }

    /**
     * 影响
     */
    enum class Impact(val displayName: String, val score: Int) {
        NEGLIGIBLE("可忽略", 1),
        MINOR("轻微", 2),
        MODERATE("中等", 3),
        MAJOR("重大", 4),
        SEVERE("严重", 5)
    }

    /**
     * 风险缓解计划
     */
    data class RiskMitigationPlan(
        val planId: String,
        val assessmentId: String,
        val mitigationActions: List<MitigationAction>,
        val priorityActions: List<MitigationAction>,
        val timeline: List<TimelineMilestone>,
        val estimatedCost: Double,
        val responsibleParty: String,
        val status: PlanStatus
    )

    /**
     * 缓解行动
     */
    data class MitigationAction(
        val actionId: String,
        val riskId: String,
        val actionType: ActionType,
        val description: String,
        val priority: Priority,
        val responsible: String,
        val startDate: Date,
        val targetDate: Date,
        val completionDate: Date?,
        val status: ActionStatus,
        val cost: Double
    )

    /**
     * 行动类型
     */
    enum class ActionType(val displayName: String) {
        PREVENTIVE("预防性"),
        CORRECTIVE("纠正性"),
        CONTINGENCY("应急性")
    }

    /**
     * 优先级
     */
    enum class Priority(val displayName: String, val score: Int) {
        LOW("低", 1),
        MEDIUM("中", 2),
        HIGH("高", 3),
        URGENT("紧急", 4)
    }

    /**
     * 行动状态
     */
    enum class ActionStatus(val displayName: String) {
        NOT_STARTED("未开始"),
        IN_PROGRESS("进行中"),
        COMPLETED("已完成"),
        ON_HOLD("暂停"),
        CANCELLED("已取消")
    }

    /**
     * 时间线里程碑
     */
    data class TimelineMilestone(
        val milestoneId: String,
        val name: String,
        val targetDate: Date,
        val actualDate: Date?,
        val status: MilestoneStatus
    )

    /**
     * 里程碑状态
     */
    enum class MilestoneStatus(val displayName: String) {
        NOT_STARTED("未开始"),
        IN_PROGRESS("进行中"),
        COMPLETED("已完成"),
        DELAYED("延期")
    }

    /**
     * 计划状态
     */
    enum class PlanStatus(val displayName: String) {
        DRAFT("草稿"),
        ACTIVE("活跃"),
        COMPLETED("已完成"),
        ON_HOLD("暂停")
    }

    /**
     * 风险监控仪表板
     */
    data class RiskMonitoringDashboard(
        val dashboardId: String,
        val date: Date,
        val summary: RiskSummary,
        val topRisks: List<RiskItem>,
        val recentAlerts: List<RiskAlert>,
        val mitigationProgress: MitigationProgress,
        val trendAnalysis: TrendAnalysis
    )

    /**
     * 风险汇总
     */
    data class RiskSummary(
        val totalRisks: Int,
        val criticalRisks: Int,
        val highRisks: Int,
        val mediumRisks: Int,
        val lowRisks: Int,
        val averageRiskScore: Double,
        val highestRiskScore: Double,
        val lowestRiskScore: Double
    )

    /**
     * 缓解进度
     */
    data class MitigationProgress(
        val totalActions: Int,
        val completedActions: Int,
        val inProgressActions: Int,
        val overdueActions: Int,
        val completionRate: Double,
        val onSchedule: Boolean
    )

    /**
     * 趋势分析
     */
    data class TrendAnalysis(
        val period: String,
        val trendDirection: TrendDirection,
        val changePercentage: Double,
        val description: String
    )

    /**
     * 趋势方向
     */
    enum class TrendDirection(val displayName: String) {
        IMPROVING("改善"),
        STABLE("稳定"),
        WORSENING("恶化")
    }

    /**
     * 默认风险项库
     */
    private val defaultRisks = listOf(
        RiskItem(
            riskId = "RISK-001",
            category = RiskCategory.STANDARD_COMPLIANCE,
            name = "FMVSS标准更新未跟踪",
            description = "未及时跟踪FMVSS标准更新，导致产品不符合最新要求",
            probability = 0.6,
            impact = 0.8,
            riskScore = 48.0,
            riskLevel = RiskLevel.HIGH,
            mitigationStatus = MitigationStatus.NOT_STARTED,
            lastReviewDate = Date()
        ),
        RiskItem(
            riskId = "RISK-002",
            category = RiskCategory.MATERIAL,
            name = "材料阻燃性不符合要求",
            description = "材料不符合FMVSS 302阻燃要求",
            probability = 0.3,
            impact = 0.9,
            riskScore = 27.0,
            riskLevel = RiskLevel.MEDIUM,
            mitigationStatus = MitigationStatus.NOT_STARTED,
            lastReviewDate = Date()
        ),
        RiskItem(
            riskId = "RISK-003",
            category = RiskCategory.TESTING,
            name = "测试样品与量产不一致",
            description = "测试样品与量产产品存在差异",
            probability = 0.4,
            impact = 0.9,
            riskScore = 36.0,
            riskLevel = RiskLevel.MEDIUM,
            mitigationStatus = MitigationStatus.NOT_STARTED,
            lastReviewDate = Date()
        ),
        RiskItem(
            riskId = "RISK-004",
            category = RiskCategory.DOCUMENTATION,
            name = "产品标签不符合要求",
            description = "产品标签不符合FMVSS 213 S5.8要求",
            probability = 0.5,
            impact = 0.6,
            riskScore = 30.0,
            riskLevel = RiskLevel.MEDIUM,
            mitigationStatus = MitigationStatus.NOT_STARTED,
            lastReviewDate = Date()
        ),
        RiskItem(
            riskId = "RISK-005",
            category = RiskCategory.SUPPLY_CHAIN,
            name = "供应商材料不符合标准",
            description = "供应商提供的材料不符合美标要求",
            probability = 0.4,
            impact = 0.8,
            riskScore = 32.0,
            riskLevel = RiskLevel.MEDIUM,
            mitigationStatus = MitigationStatus.NOT_STARTED,
            lastReviewDate = Date()
        ),
        RiskItem(
            riskId = "RISK-006",
            category = RiskCategory.PATENT,
            name = "设计侵犯竞品专利",
            description = "产品设计可能与竞品专利冲突",
            probability = 0.3,
            impact = 0.8,
            riskScore = 24.0,
            riskLevel = RiskLevel.MEDIUM,
            mitigationStatus = MitigationStatus.NOT_STARTED,
            lastReviewDate = Date()
        ),
        RiskItem(
            riskId = "RISK-007",
            category = RiskCategory.AFTER_SALES,
            name = "召回通知不到位",
            description = "召回通知未能覆盖所有受影响用户",
            probability = 0.3,
            impact = 0.7,
            riskScore = 21.0,
            riskLevel = RiskLevel.LOW,
            mitigationStatus = MitigationStatus.NOT_STARTED,
            lastReviewDate = Date()
        ),
        RiskItem(
            riskId = "RISK-008",
            category = RiskCategory.ENVIRONMENTAL,
            name = "产品不适应极端环境",
            description = "产品在极端气候条件下性能下降",
            probability = 0.4,
            impact = 0.6,
            riskScore = 24.0,
            riskLevel = RiskLevel.LOW,
            mitigationStatus = MitigationStatus.NOT_STARTED,
            lastReviewDate = Date()
        )
    )

    /**
     * 执行风险评估
     */
    suspend fun performRiskAssessment(
        productId: String,
        customRisks: List<RiskItem> = emptyList()
    ): RiskAssessment = withContext(Dispatchers.Default) {
        val allRisks = defaultRisks + customRisks
        val categoryRisks = calculateCategoryRisks(allRisks)
        val overallRiskScore = calculateOverallRiskScore(categoryRisks)
        val riskLevel = determineRiskLevel(overallRiskScore)
        val topRisks = allRisks.sortedByDescending { it.riskScore }.take(5)
        
        val recommendations = generateRecommendations(categoryRisks, topRisks)
        
        RiskAssessment(
            assessmentId = "RA-${System.currentTimeMillis()}",
            productId = productId,
            assessmentDate = Date(),
            overallRiskScore = overallRiskScore,
            riskLevel = riskLevel,
            riskCategories = categoryRisks,
            topRisks = topRisks,
            recommendations = recommendations
        )
    }

    /**
     * 计算类别风险
     */
    private fun calculateCategoryRisks(risks: List<RiskItem>): List<CategoryRisk> {
        return RiskCategory.values().map { category ->
            val categoryRisks = risks.filter { it.category == category }
            val avgScore = if (categoryRisks.isNotEmpty()) {
                categoryRisks.map { it.riskScore }.average()
            } else {
                0.0
            }
            val riskLevel = determineRiskLevel(avgScore)
            
            CategoryRisk(
                category = category,
                riskScore = avgScore,
                riskLevel = riskLevel,
                riskCount = categoryRisks.size,
                description = getCategoryDescription(category)
            )
        }
    }

    /**
     * 获取类别描述
     */
    private fun getCategoryDescription(category: RiskCategory): String {
        return when (category) {
            RiskCategory.STANDARD_COMPLIANCE -> "产品是否符合FMVSS等标准要求"
            RiskCategory.MATERIAL -> "材料是否满足阻燃、强度等要求"
            RiskCategory.TESTING -> "测试是否充分、样品是否一致"
            RiskCategory.DOCUMENTATION -> "文档是否完整、准确"
            RiskCategory.SUPPLY_CHAIN -> "供应商是否合规、追溯是否完整"
            RiskCategory.AFTER_SALES -> "注册、召回等售后服务是否完善"
            RiskCategory.PATENT -> "是否存在专利侵权风险"
            RiskCategory.ENVIRONMENTAL -> "产品是否适应各种环境条件"
        }
    }

    /**
     * 计算总体风险分数
     */
    private fun calculateOverallRiskScore(categoryRisks: List<CategoryRisk>): Double {
        return categoryRisks.map { it.riskScore }.average()
    }

    /**
     * 确定风险等级
     */
    private fun determineRiskLevel(score: Double): RiskLevel {
        return when {
            score >= 75 -> RiskLevel.CRITICAL
            score >= 50 -> RiskLevel.HIGH
            score >= 25 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
    }

    /**
     * 生成建议
     */
    private fun generateRecommendations(
        categoryRisks: List<CategoryRisk>,
        topRisks: List<RiskItem>
    ): List<String> {
        val recommendations = mutableListOf<String>()
        
        // 基于高类别风险的建议
        categoryRisks.filter { it.riskLevel == RiskLevel.CRITICAL || it.riskLevel == RiskLevel.HIGH }
            .forEach { categoryRisk ->
                recommendations.add("优先处理${categoryRisk.category.displayName}风险")
            }
        
        // 基于前5风险的建议
        topRisks.filter { it.riskLevel == RiskLevel.CRITICAL || it.riskLevel == RiskLevel.HIGH }
            .forEach { risk ->
                recommendations.add("关注高风险项：${risk.name}")
            }
        
        // 通用建议
        recommendations.add("建立风险监控机制")
        recommendations.add("定期更新风险评估")
        recommendations.add("制定风险缓解计划")
        
        return recommendations
    }

    /**
     * 创建风险预警
     */
    fun createRiskAlert(
        alertType: AlertType,
        severity: AlertSeverity,
        title: String,
        description: String,
        riskItems: List<RiskItem>,
        affectedProducts: List<String>,
        actionRequired: String
    ): RiskAlert {
        val dueDate = Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000L)  // 30天后
        
        return RiskAlert(
            alertId = "ALERT-${System.currentTimeMillis()}",
            alertType = alertType,
            severity = severity,
            title = title,
            description = description,
            riskItems = riskItems,
            affectedProducts = affectedProducts,
            alertDate = Date(),
            actionRequired = actionRequired,
            dueDate = dueDate,
            status = AlertStatus.OPEN
        )
    }

    /**
     * 创建风险缓解计划
     */
    fun createMitigationPlan(
        assessmentId: String,
        riskItems: List<RiskItem>
    ): RiskMitigationPlan {
        val actions = riskItems.mapIndexed { index, risk ->
            MitigationAction(
                actionId = "ACT-${System.currentTimeMillis()}-$index",
                riskId = risk.riskId,
                actionType = ActionType.CORRECTIVE,
                description = "缓解${risk.name}风险",
                priority = when (risk.riskLevel) {
                    RiskLevel.CRITICAL -> Priority.URGENT
                    RiskLevel.HIGH -> Priority.HIGH
                    RiskLevel.MEDIUM -> Priority.MEDIUM
                    RiskLevel.LOW -> Priority.LOW
                },
                responsible = "合规团队",
                startDate = Date(),
                targetDate = Date(System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000L),
                completionDate = null,
                status = ActionStatus.NOT_STARTED,
                cost = when (risk.riskLevel) {
                    RiskLevel.CRITICAL -> 100000.0
                    RiskLevel.HIGH -> 50000.0
                    RiskLevel.MEDIUM -> 20000.0
                    RiskLevel.LOW -> 5000.0
                }
            )
        }
        
        return RiskMitigationPlan(
            planId = "MP-${System.currentTimeMillis()}",
            assessmentId = assessmentId,
            mitigationActions = actions,
            priorityActions = actions.filter { it.priority == Priority.URGENT || it.priority == Priority.HIGH },
            timeline = createInitialTimeline(),
            estimatedCost = actions.sumOf { it.cost },
            responsibleParty = "合规团队",
            status = PlanStatus.ACTIVE
        )
    }

    /**
     * 创建初始时间线
     */
    private fun createInitialTimeline(): List<TimelineMilestone> {
        return listOf(
            TimelineMilestone(
                milestoneId = "M1",
                name = "风险识别完成",
                targetDate = Date(),
                actualDate = Date(),
                status = MilestoneStatus.COMPLETED
            ),
            TimelineMilestone(
                milestoneId = "M2",
                name = "缓解措施制定",
                targetDate = Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000L),
                actualDate = null,
                status = MilestoneStatus.NOT_STARTED
            ),
            TimelineMilestone(
                milestoneId = "M3",
                name = "缓解措施实施",
                targetDate = Date(System.currentTimeMillis() + 60L * 24 * 60 * 60 * 1000L),
                actualDate = null,
                status = MilestoneStatus.NOT_STARTED
            ),
            TimelineMilestone(
                milestoneId = "M4",
                name = "风险缓解验证",
                targetDate = Date(System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000L),
                actualDate = null,
                status = MilestoneStatus.NOT_STARTED
            )
        )
    }

    /**
     * 生成风险监控仪表板
     */
    suspend fun generateMonitoringDashboard(
        assessment: RiskAssessment,
        alerts: List<RiskAlert>
    ): RiskMonitoringDashboard = withContext(Dispatchers.Default) {
        val summary = calculateRiskSummary(assessment.riskCategories)
        val topRisks = assessment.topRisks
        val recentAlerts = alerts.sortedByDescending { it.alertDate }.take(5)
        val mitigationProgress = MitigationProgress(
            totalActions = 0,
            completedActions = 0,
            inProgressActions = 0,
            overdueActions = 0,
            completionRate = 0.0,
            onSchedule = true
        )
        val trendAnalysis = TrendAnalysis(
            period = "过去30天",
            trendDirection = TrendDirection.STABLE,
            changePercentage = 0.0,
            description = "风险状况稳定"
        )
        
        RiskMonitoringDashboard(
            dashboardId = "DASH-${System.currentTimeMillis()}",
            date = Date(),
            summary = summary,
            topRisks = topRisks,
            recentAlerts = recentAlerts,
            mitigationProgress = mitigationProgress,
            trendAnalysis = trendAnalysis
        )
    }

    /**
     * 计算风险汇总
     */
    private fun calculateRiskSummary(categoryRisks: List<CategoryRisk>): RiskSummary {
        val allRisks = categoryRisks.flatMap { category ->
            generateRiskItemsForCategory(category.category, category.riskCount)
        }
        
        return RiskSummary(
            totalRisks = allRisks.size,
            criticalRisks = allRisks.count { it.riskLevel == RiskLevel.CRITICAL },
            highRisks = allRisks.count { it.riskLevel == RiskLevel.HIGH },
            mediumRisks = allRisks.count { it.riskLevel == RiskLevel.MEDIUM },
            lowRisks = allRisks.count { it.riskLevel == RiskLevel.LOW },
            averageRiskScore = allRisks.map { it.riskScore }.average(),
            highestRiskScore = allRisks.maxOfOrNull { it.riskScore } ?: 0.0,
            lowestRiskScore = allRisks.minOfOrNull { it.riskScore } ?: 0.0
        )
    }

    /**
     * 为类别生成风险项
     */
    private fun generateRiskItemsForCategory(category: RiskCategory, count: Int): List<RiskItem> {
        return (1..count).map { index ->
            RiskItem(
                riskId = "${category.name}-$index",
                category = category,
                name = "${category.displayName}风险-$index",
                description = "描述",
                probability = 0.5,
                impact = 0.5,
                riskScore = 25.0,
                riskLevel = RiskLevel.MEDIUM,
                mitigationStatus = MitigationStatus.NOT_STARTED,
                lastReviewDate = Date()
            )
        }
    }

    /**
     * 生成风险评估报告
     */
    suspend fun generateRiskAssessmentReport(
        assessment: RiskAssessment,
        mitigationPlan: RiskMitigationPlan?
    ): String = withContext(Dispatchers.Default) {
        val report = StringBuilder()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        
        report.appendLine("=" .repeat(70))
        report.appendLine("合规风险评估报告")
        report.appendLine("=" .repeat(70))
        report.appendLine()
        
        report.appendLine("评估ID：${assessment.assessmentId}")
        report.appendLine("产品ID：${assessment.productId}")
        report.appendLine("评估日期：${dateFormat.format(assessment.assessmentDate)}")
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("总体风险评估")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        report.appendLine("总体风险分数：${String.format("%.2f", assessment.overallRiskScore)}/100")
        report.appendLine("风险等级：${assessment.riskLevel.displayName}")
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("类别风险评估")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        assessment.riskCategories.forEach { category ->
            report.appendLine("${category.category.displayName}：")
            report.appendLine("  风险分数：${String.format("%.2f", category.riskScore)}/100")
            report.appendLine("  风险等级：${category.riskLevel.displayName}")
            report.appendLine("  风险数量：${category.riskCount}")
            report.appendLine("  描述：${category.description}")
            report.appendLine()
        }
        
        report.appendLine("-" .repeat(70))
        report.appendLine("前5高风险")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        assessment.topRisks.forEachIndexed { index, risk ->
            report.appendLine("${index + 1}. ${risk.name}")
            report.appendLine("   类别：${risk.category.displayName}")
            report.appendLine("   描述：${risk.description}")
            report.appendLine("   概率：${String.format("%.0f", risk.probability * 100)}%")
            report.appendLine("   影响：${String.format("%.0f", risk.impact * 100)}%")
            report.appendLine("   风险分数：${String.format("%.2f", risk.riskScore)}")
            report.appendLine("   风险等级：${risk.riskLevel.displayName}")
            report.appendLine("   缓解状态：${risk.mitigationStatus.displayName}")
            report.appendLine()
        }
        
        if (mitigationPlan != null) {
            report.appendLine("-" .repeat(70))
            report.appendLine("缓解计划")
            report.appendLine("-" .repeat(70))
            report.appendLine()
            report.appendLine("计划ID：${mitigationPlan.planId}")
            report.appendLine("负责人：${mitigationPlan.responsibleParty}")
            report.appendLine("估计成本：$${String.format("%.0f", mitigationPlan.estimatedCost)}")
            report.appendLine("状态：${mitigationPlan.status.displayName}")
            report.appendLine()
            report.appendLine("优先行动：")
            mitigationPlan.priorityActions.take(5).forEach { action ->
                report.appendLine("  - ${action.description} (优先级：${action.priority.displayName})")
            }
            report.appendLine()
        }
        
        report.appendLine("-" .repeat(70))
        report.appendLine("建议")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        assessment.recommendations.forEach { rec ->
            report.appendLine("- $rec")
        }
        report.appendLine()
        
        report.appendLine("=" .repeat(70))
        report.appendLine("后续步骤")
        report.appendLine("=" .repeat(70))
        report.appendLine()
        report.appendLine("1. 优先处理关键和高风险项")
        report.appendLine("2. 实施缓解计划")
        report.appendLine("3. 监控风险状态")
        report.appendLine("4. 定期更新风险评估")
        report.appendLine("5. 向管理层报告风险状况")
        
        report.toString()
    }

    /**
     * 生成风险管理指南
     */
    fun generateRiskManagementGuide(): String {
        val guide = StringBuilder()
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("合规风险管理指南")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        
        guide.appendLine("【风险评估流程】")
        guide.appendLine("1. 风险识别：识别所有潜在风险")
        guide.appendLine("2. 风险分析：评估概率和影响")
        guide.appendLine("3. 风险评估：确定风险等级")
        guide.appendLine("4. 风险优先级：确定缓解优先级")
        guide.appendLine("5. 风险监控：持续监控风险状态")
        guide.appendLine()
        
        guide.appendLine("【风险缓解策略】")
        guide.appendLine("1. 避免：通过设计避免风险")
        guide.appendLine("2. 降低：通过措施降低概率或影响")
        guide.appendLine("3. 转移：通过保险等方式转移风险")
        guide.appendLine("4. 接受：接受低风险")
        guide.appendLine()
        
        guide.appendLine("【风险预警机制】")
        guide.appendLine("1. 设置风险阈值")
        guide.appendLine("2. 建立预警系统")
        guide.appendLine("3. 定义预警等级")
        guide.appendLine("4. 制定响应流程")
        guide.appendLine()
        
        guide.appendLine("【常见风险】")
        guide.appendLine("1. 标准合规风险：不符合FMVSS等标准")
        guide.appendLine("2. 材料合规风险：材料不符合要求")
        guide.appendLine("3. 测试合规风险：测试不充分或样品不一致")
        guide.appendLine("4. 文档合规风险：文档不完整或不准确")
        guide.appendLine("5. 供应链风险：供应商不合规或追溯不完整")
        guide.appendLine("6. 售后风险：注册、召回等不完善")
        guide.appendLine("7. 专利风险：专利侵权")
        guide.appendLine("8. 环境风险：产品不适应极端环境")
        guide.appendLine()
        
        guide.appendLine("【风险管理最佳实践】")
        guide.appendLine("1. 建立风险管理框架")
        guide.appendLine("2. 定期进行风险评估")
        guide.appendLine("3. 实施风险缓解计划")
        guide.appendLine("4. 监控风险预警")
        guide.appendLine("5. 持续改进风险管理")
        guide.appendLine()
        
        guide.appendLine("【建议】")
        guide.appendLine("1. 建立风险管理文化")
        guide.appendLine("2. 培训员工风险意识")
        guide.appendLine("3. 使用风险管理工具")
        guide.appendLine("4. 定期报告风险状况")
        guide.appendLine("5. 持续改进风险管理流程")
        
        return guide.toString()
    }
}

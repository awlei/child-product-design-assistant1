package com.childproduct.designassistant.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * 标准动态适配服务
 * 
 * 跟踪FMVSS标准修订、州级额外要求、进口与报关合规，确保产品持续符合最新要求
 */
class StandardDynamicAdaptationService {

    /**
     * 标准版本
     */
    data class StandardVersion(
        val standardId: String,
        val standardName: String,
        val version: String,
        val effectiveDate: Date,
        val expiryDate: Date?,
        val status: StandardStatus,
        val changes: List<StandardChange>
    )

    /**
     * 标准状态
     */
    enum class StandardStatus(val displayName: String) {
        CURRENT("当前有效"),
        UPCOMING("即将生效"),
        PROPOSED("提议中"),
        WITHDRAWN("已撤销"),
        SUPERSEDED("已替代")
    }

    /**
     * 标准变更
     */
    data class StandardChange(
        val changeId: String,
        val changeType: ChangeType,
        val description: String,
        val affectedSections: List<String>,
        val impactLevel: ImpactLevel,
        val actionRequired: String
    )

    /**
     * 变更类型
     */
    enum class ChangeType(val displayName: String) {
        NEW_REQUIREMENT("新要求"),
        MODIFICATION("修改"),
        DELETION("删除"),
        CLARIFICATION("澄清"),
        TECHNICAL_CORRECTION("技术修正")
    }

    /**
     * 影响等级
     */
    enum class ImpactLevel(val displayName: String, val score: Int) {
        MINOR("轻微", 1),
        MODERATE("中等", 2),
        SIGNIFICANT("显著", 3),
        MAJOR("重大", 4)
    }

    /**
     * 州级额外要求
     */
    data class StateRequirement(
        val requirementId: String,
        val stateCode: String,
        val stateName: String,
        val category: RequirementCategory,
        val description: String,
        val requirementDetails: String,
        val effectiveDate: Date,
        val complianceMethod: String
    )

    /**
     * 要求类别
     */
    enum class RequirementCategory(val displayName: String) {
        ENVIRONMENTAL("环保要求"),
        INSTALLATION("安装培训"),
        LABELING("标签要求"),
        MATERIAL("材料要求"),
        TESTING("测试要求"),
        REGISTRATION("注册要求")
    }

    /**
     * 进口与报关合规
     */
    data class ImportCompliance(
        val complianceId: String,
        val productName: String,
        val destinationCountry: String,
        val requirements: List<ImportRequirement>,
        val documentation: List<RequiredDocument>,
        val inspectionPoints: List<InspectionPoint>
    )

    /**
     * 进口要求
     */
    data class ImportRequirement(
        val requirementId: String,
        val category: ImportCategory,
        val description: String,
        val regulation: String,
        val isMandatory: Boolean
    )

    /**
     * 进口类别
     */
    enum class ImportCategory(val displayName: String) {
        CERTIFICATION("认证"),
        LABELING("标签"),
        SAFETY("安全"),
        ENVIRONMENT("环境"),
        ORIGIN("原产地"),
        RESPONSIBLE_PARTY("责任人")
    }

    /**
     * 所需文件
     */
    data class RequiredDocument(
        val documentId: String,
        val name: String,
        val issuingAuthority: String,
        val validityPeriod: Int,  // 月
        val originalRequired: Boolean,
        val copyRequired: Boolean,
        val translationRequired: Boolean
    )

    /**
     * 检验点
     */
    data class InspectionPoint(
        val pointId: String,
        val pointName: String,
        val description: String,
        val inspectionMethod: String,
        val passCriteria: String
    )

    /**
     * 标准更新订阅
     */
    data class StandardUpdateSubscription(
        val subscriptionId: String,
        val subscriber: String,
        val email: String,
        val subscribedStandards: List<String>,
        val notificationFrequency: NotificationFrequency,
        val startDate: Date,
        val status: SubscriptionStatus
    )

    /**
     * 通知频率
     */
    enum class NotificationFrequency(val displayName: String) {
        IMMEDIATE("立即"),
        DAILY("每日"),
        WEEKLY("每周"),
        MONTHLY("每月")
    }

    /**
     * 订阅状态
     */
    enum class SubscriptionStatus(val displayName: String) {
        ACTIVE("活跃"),
        PAUSED("暂停"),
        CANCELLED("已取消"),
        EXPIRED("已过期")
    }

    /**
     * 标准更新通知
     */
    data class StandardUpdateNotification(
        val notificationId: String,
        val standardId: String,
        val standardName: String,
        val version: String,
        val effectiveDate: Date,
        val changes: List<StandardChange>,
        val notificationDate: Date,
        val readStatus: ReadStatus
    )

    /**
     * 阅读状态
     */
    enum class ReadStatus(val displayName: String) {
        UNREAD("未读"),
        READ("已读"),
        ACKNOWLEDGED("已确认")
    }

    /**
     * 合规影响评估
     */
    data class ComplianceImpactAssessment(
        val assessmentId: String,
        val standardId: String,
        val standardName: String,
        val version: String,
        val impactLevel: ImpactLevel,
        val affectedProducts: List<String>,
        val requiredActions: List<RequiredAction>,
        val estimatedCost: Double,
        val estimatedTimeframe: Int,  // 月
        val recommendations: List<String>
    )

    /**
     * 所需行动
     */
    data class RequiredAction(
        val actionId: String,
        val actionType: ActionType,
        val description: String,
        val priority: Priority,
        val responsible: String,
        val targetDate: Date,
        val status: ActionStatus
    )

    /**
     * 行动类型
     */
    enum class ActionType(val displayName: String) {
        DESIGN_CHANGE("设计变更"),
        MATERIAL_CHANGE("材料变更"),
        PROCESS_CHANGE("工艺变更"),
        TESTING("重新测试"),
        DOCUMENT_UPDATE("文档更新"),
        CERTIFICATION_UPDATE("认证更新")
    }

    /**
     * 优先级
     */
    enum class Priority(val displayName: String, val value: Int) {
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
        BLOCKED("已阻塞"),
        CANCELLED("已取消")
    }

    /**
     * FMVSS标准版本库
     */
    private val fmvssVersions = listOf(
        StandardVersion(
            standardId = "FMVSS-213a",
            standardName = "FMVSS 213a - Child Restraint Systems",
            version = "1.0",
            effectiveDate = Date(System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000L),  // 1年前生效
            expiryDate = null,
            status = StandardStatus.CURRENT,
            changes = listOf(
                StandardChange(
                    changeId = "CHANGE-001",
                    changeType = ChangeType.NEW_REQUIREMENT,
                    description = "新增侧面碰撞测试要求",
                    affectedSections = listOf("S5.3", "S5.4"),
                    impactLevel = ImpactLevel.MAJOR,
                    actionRequired = "进行侧面碰撞测试，更新产品设计"
                ),
                StandardChange(
                    changeId = "CHANGE-002",
                    changeType = ChangeType.MODIFICATION,
                    description = "修改头部伤害判据",
                    affectedSections = listOf("S5.4.1"),
                    impactLevel = ImpactLevel.SIGNIFICANT,
                    actionRequired = "验证产品符合新判据"
                )
            )
        ),
        StandardVersion(
            standardId = "FMVSS-213",
            standardName = "FMVSS 213 - Child Restraint Systems",
            version = "2.0",
            effectiveDate = Date(System.currentTimeMillis() - 730L * 24 * 60 * 60 * 1000L),  // 2年前
            expiryDate = null,
            status = StandardStatus.CURRENT,
            changes = emptyList()
        ),
        StandardVersion(
            standardId = "FMVSS-225",
            standardName = "FMVSS 225 - LATCH System",
            version = "1.0",
            effectiveDate = Date(System.currentTimeMillis() - 1825L * 24 * 60 * 60 * 1000L),  // 5年前
            expiryDate = null,
            status = StandardStatus.CURRENT,
            changes = emptyList()
        ),
        StandardVersion(
            standardId = "FMVSS-302",
            standardName = "FMVSS 302 - Flammability",
            version = "1.0",
            effectiveDate = Date(System.currentTimeMillis() - 3650L * 24 * 60 * 60 * 1000L),  // 10年前
            expiryDate = null,
            status = StandardStatus.CURRENT,
            changes = emptyList()
        )
    )

    /**
     * 州级额外要求库
     */
    private val stateRequirements = listOf(
        StateRequirement(
            requirementId = "STATE-CA-001",
            stateCode = "CA",
            stateName = "California",
            category = RequirementCategory.ENVIRONMENTAL,
            description = "加州环保要求",
            requirementDetails = "产品需符合加州65号法案（Proposition 65），不含有害物质",
            effectiveDate = Date(System.currentTimeMillis() - 3650L * 24 * 60 * 60 * 1000L),
            complianceMethod = "材料测试报告，标签警示"
        ),
        StateRequirement(
            requirementId = "STATE-NY-001",
            stateCode = "NY",
            stateName = "New York",
            category = RequirementCategory.INSTALLATION,
            description = "纽约州安装培训要求",
            requirementDetails = "儿童座椅需提供安装培训指导",
            effectiveDate = Date(System.currentTimeMillis() - 1825L * 24 * 60 * 60 * 1000L),
            complianceMethod = "提供安装培训材料和视频"
        ),
        StateRequirement(
            requirementId = "STATE-CA-002",
            stateCode = "CA",
            stateName = "California",
            category = RequirementCategory.MATERIAL,
            description = "加州材料要求",
            requirementDetails = "塑料件需符合加州材料要求",
            effectiveDate = Date(System.currentTimeMillis() - 3650L * 24 * 60 * 60 * 1000L),
            complianceMethod = "材料符合性证书"
        ),
        StateRequirement(
            requirementId = "STATE-MA-001",
            stateCode = "MA",
            stateName = "Massachusetts",
            category = RequirementCategory.REGISTRATION,
            description = "马萨诸塞州注册要求",
            requirementDetails = "需在州政府注册产品",
            effectiveDate = Date(System.currentTimeMillis() - 730L * 24 * 60 * 60 * 1000L),
            complianceMethod = "完成州政府注册"
        ),
        StateRequirement(
            requirementId = "STATE-TX-001",
            stateCode = "TX",
            stateName = "Texas",
            category = RequirementCategory.LABELING,
            description = "德克萨斯州标签要求",
            requirementDetails = "标签需包含德州特定信息",
            effectiveDate = Date(System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000L),
            complianceMethod = "更新产品标签"
        )
    )

    /**
     * 美国进口合规要求
     */
    private val importComplianceRequirements = ImportCompliance(
        complianceId = "US-IMPORT-001",
        productName = "CRS Product",
        destinationCountry = "United States",
        requirements = listOf(
            ImportRequirement(
                requirementId = "IMP-001",
                category = ImportCategory.CERTIFICATION,
                description = "DOT认证",
                regulation = "49 CFR Part 567",
                isMandatory = true
            ),
            ImportRequirement(
                requirementId = "IMP-002",
                category = ImportCategory.CERTIFICATION,
                description = "FMVSS 213合规性声明",
                regulation = "49 CFR Part 571",
                isMandatory = true
            ),
            ImportRequirement(
                requirementId = "IMP-003",
                category = ImportCategory.LABELING,
                description = "产品标签包含责任人信息",
                regulation = "49 CFR Part 567",
                isMandatory = true
            ),
            ImportRequirement(
                requirementId = "IMP-004",
                category = ImportCategory.LABELING,
                description = "召回信息标签",
                regulation = "49 CFR Part 573",
                isMandatory = true
            ),
            ImportRequirement(
                requirementId = "IMP-005",
                category = ImportCategory.RESPONSIBLE_PARTY,
                description = "美国境内责任人",
                regulation = "49 CFR Part 567",
                isMandatory = true
            ),
            ImportRequirement(
                requirementId = "IMP-006",
                category = ImportCategory.ORIGIN,
                description = "原产地标记",
                regulation = "19 U.S.C. 1304",
                isMandatory = true
            )
        ),
        documentation = listOf(
            RequiredDocument(
                documentId = "DOC-001",
                name = "DOT认证证书",
                issuingAuthority = "NHTSA",
                validityPeriod = 60,
                originalRequired = true,
                copyRequired = false,
                translationRequired = false
            ),
            RequiredDocument(
                documentId = "DOC-002",
                name = "FMVSS合规声明",
                issuingAuthority = "Manufacturer",
                validityPeriod = 60,
                originalRequired = true,
                copyRequired = true,
                translationRequired = true
            ),
            RequiredDocument(
                documentId = "DOC-003",
                name = "测试报告",
                issuingAuthority = "NHTSA认证实验室",
                validityPeriod = 60,
                originalRequired = false,
                copyRequired = true,
                translationRequired = true
            ),
            RequiredDocument(
                documentId = "DOC-004",
                name = "商业发票",
                issuingAuthority = "Exporter",
                validityPeriod = 6,
                originalRequired = true,
                copyRequired = false,
                translationRequired = true
            ),
            RequiredDocument(
                documentId = "DOC-005",
                name = "装箱单",
                issuingAuthority = "Exporter",
                validityPeriod = 6,
                originalRequired = true,
                copyRequired = false,
                translationRequired = true
            ),
            RequiredDocument(
                documentId = "DOC-006",
                name = "原产地证书",
                issuingAuthority = "Exporter",
                validityPeriod = 12,
                originalRequired = true,
                copyRequired = false,
                translationRequired = true
            )
        ),
        inspectionPoints = listOf(
            InspectionPoint(
                pointId = "INS-001",
                pointName = "DOT认证核查",
                description = "核查DOT认证有效性",
                inspectionMethod = "系统查询",
                passCriteria = "认证有效且未过期"
            ),
            InspectionPoint(
                pointId = "INS-002",
                pointName = "产品标签检查",
                description = "检查产品标签是否符合要求",
                inspectionMethod = "目视检查",
                passCriteria = "标签完整、清晰、包含所有必需信息"
            ),
            InspectionPoint(
                pointId = "INS-003",
                pointName = "责任人信息检查",
                description = "检查产品上的美国境内责任人信息",
                inspectionMethod = "目视检查",
                passCriteria = "责任人信息完整、准确"
            ),
            InspectionPoint(
                pointId = "INS-004",
                pointName = "原产地标记检查",
                description = "检查产品上的原产地标记",
                inspectionMethod = "目视检查",
                passCriteria = "原产地标记清晰、准确"
            )
        )
    )

    /**
     * 获取当前有效的标准版本
     */
    fun getCurrentStandards(): List<StandardVersion> {
        return fmvssVersions.filter { it.status == StandardStatus.CURRENT }
    }

    /**
     * 获取即将生效的标准
     */
    fun getUpcomingStandards(): List<StandardVersion> {
        return fmvssVersions.filter { it.status == StandardStatus.UPCOMING }
    }

    /**
     * 获取提议中的标准
     */
    fun getProposedStandards(): List<StandardVersion> {
        return fmvssVersions.filter { it.status == StandardStatus.PROPOSED }
    }

    /**
     * 根据州获取要求
     */
    fun getStateRequirements(stateCode: String): List<StateRequirement> {
        return stateRequirements.filter { it.stateCode == stateCode }
    }

    /**
     * 根据类别获取州级要求
     */
    fun getStateRequirementsByCategory(category: RequirementCategory): List<StateRequirement> {
        return stateRequirements.filter { it.category == category }
    }

    /**
     * 获取所有州级要求
     */
    fun getAllStateRequirements(): List<StateRequirement> {
        return stateRequirements
    }

    /**
     * 获取进口合规要求
     */
    fun getImportComplianceRequirements(): ImportCompliance {
        return importComplianceRequirements
    }

    /**
     * 创建标准更新订阅
     */
    fun createStandardUpdateSubscription(
        subscriber: String,
        email: String,
        subscribedStandards: List<String>,
        frequency: NotificationFrequency
    ): StandardUpdateSubscription {
        return StandardUpdateSubscription(
            subscriptionId = "SUB-${SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())}",
            subscriber = subscriber,
            email = email,
            subscribedStandards = subscribedStandards,
            notificationFrequency = frequency,
            startDate = Date(),
            status = SubscriptionStatus.ACTIVE
        )
    }

    /**
     * 评估标准变更影响
     */
    suspend fun assessComplianceImpact(
        standardVersion: StandardVersion,
        affectedProducts: List<String>
    ): ComplianceImpactAssessment = withContext(Dispatchers.Default) {
        val maxImpactLevel = standardVersion.changes.maxOfOrNull { it.impactLevel } ?: ImpactLevel.MINOR
        
        val requiredActions = standardVersion.changes.mapIndexed { index, change ->
            RequiredAction(
                actionId = "ACT-${SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())}-$index",
                actionType = when (change.changeType) {
                    ChangeType.NEW_REQUIREMENT -> ActionType.DESIGN_CHANGE
                    ChangeType.MODIFICATION -> ActionType.DOCUMENT_UPDATE
                    ChangeType.DELETION -> ActionType.DOCUMENT_UPDATE
                    ChangeType.CLARIFICATION -> ActionType.DOCUMENT_UPDATE
                    ChangeType.TECHNICAL_CORRECTION -> ActionType.DOCUMENT_UPDATE
                },
                description = change.actionRequired,
                priority = when (change.impactLevel) {
                    ImpactLevel.MAJOR -> Priority.URGENT
                    ImpactLevel.SIGNIFICANT -> Priority.HIGH
                    ImpactLevel.MODERATE -> Priority.MEDIUM
                    ImpactLevel.MINOR -> Priority.LOW
                },
                responsible = "合规团队",
                targetDate = Date(System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000L),
                status = ActionStatus.NOT_STARTED
            )
        }

        ComplianceImpactAssessment(
            assessmentId = "CIA-${SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())}",
            standardId = standardVersion.standardId,
            standardName = standardVersion.standardName,
            version = standardVersion.version,
            impactLevel = maxImpactLevel,
            affectedProducts = affectedProducts,
            requiredActions = requiredActions,
            estimatedCost = when (maxImpactLevel) {
                ImpactLevel.MAJOR -> 500000.0
                ImpactLevel.SIGNIFICANT -> 200000.0
                ImpactLevel.MODERATE -> 50000.0
                ImpactLevel.MINOR -> 10000.0
            },
            estimatedTimeframe = when (maxImpactLevel) {
                ImpactLevel.MAJOR -> 12
                ImpactLevel.SIGNIFICANT -> 6
                ImpactLevel.MODERATE -> 3
                ImpactLevel.MINOR -> 1
            },
            recommendations = listOf(
                "立即评估标准变更对产品的影响",
                "制定详细实施计划",
                "预留足够的资源和时间",
                "考虑提前测试以验证合规性",
                "更新相关文档和培训材料"
            )
        )
    }

    /**
     * 生成标准动态适配报告
     */
    suspend fun generateStandardAdaptationReport(
        assessment: ComplianceImpactAssessment
    ): String = withContext(Dispatchers.Default) {
        val report = StringBuilder()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        
        report.appendLine("=" .repeat(70))
        report.appendLine("标准动态适配报告")
        report.appendLine("=" .repeat(70))
        report.appendLine()
        
        report.appendLine("评估ID：${assessment.assessmentId}")
        report.appendLine("标准：${assessment.standardName} (${assessment.standardId})")
        report.appendLine("版本：${assessment.version}")
        report.appendLine("影响等级：${assessment.impactLevel.displayName}")
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("受影响产品")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        assessment.affectedProducts.forEach { product ->
            report.appendLine("- $product")
        }
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("所需行动")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        assessment.requiredActions.forEach { action ->
            report.appendLine("行动ID：${action.actionId}")
            report.appendLine("类型：${action.actionType.displayName}")
            report.appendLine("描述：${action.description}")
            report.appendLine("优先级：${action.priority.displayName}")
            report.appendLine("负责人：${action.responsible}")
            report.appendLine("目标日期：${dateFormat.format(action.targetDate)}")
            report.appendLine("状态：${action.status.displayName}")
            report.appendLine()
        }
        
        report.appendLine("-" .repeat(70))
        report.appendLine("影响评估")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        report.appendLine("估计成本：$${String.format("%.0f", assessment.estimatedCost)}")
        report.appendLine("估计时间：${assessment.estimatedTimeframe}个月")
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("建议")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        assessment.recommendations.forEach { rec ->
            report.appendLine("- $rec")
        }
        report.appendLine()
        
        report.toString()
    }

    /**
     * 生成州级要求合规指南
     */
    fun generateStateComplianceGuide(stateCode: String): String {
        val guide = StringBuilder()
        val stateReqs = getStateRequirements(stateCode)
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("州级要求合规指南 - $stateCode")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        
        if (stateReqs.isEmpty()) {
            guide.appendLine("未找到" + stateCode + "的额外要求")
            guide.appendLine("仅需满足联邦标准（FMVSS）")
        } else {
            guide.appendLine("发现${stateReqs.size}项额外要求：")
            guide.appendLine()
            
            stateReqs.forEach { req ->
                guide.appendLine("要求ID：${req.requirementId}")
                guide.appendLine("类别：${req.category.displayName}")
                guide.appendLine("描述：${req.description}")
                guide.appendLine("详情：${req.requirementDetails}")
                guide.appendLine("生效日期：${SimpleDateFormat("yyyy-MM-dd").format(req.effectiveDate)}")
                guide.appendLine("合规方法：${req.complianceMethod}")
                guide.appendLine()
            }
        }
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("合规建议")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        guide.appendLine("1. 提前了解目标州市场的额外要求")
        guide.appendLine("2. 联系州政府机构获取最新要求")
        guide.appendLine("3. 在产品设计阶段考虑州级要求")
        guide.appendLine("4. 保留相关合规文件备查")
        guide.appendLine("5. 定期检查州级要求更新")
        
        return guide.toString()
    }

    /**
     * 生成进口合规指南
     */
    fun generateImportComplianceGuide(): String {
        val guide = StringBuilder()
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("美国进口合规指南")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        
        guide.appendLine("【认证要求】")
        guide.appendLine("1. DOT认证（49 CFR Part 567）")
        guide.appendLine("   - 制造商必须申请DOT认证")
        guide.appendLine("   - 指定美国境内责任人")
        guide.appendLine("   - 保留认证文件")
        guide.appendLine()
        guide.appendLine("2. FMVSS合规性声明（49 CFR Part 571）")
        guide.appendLine("   - 提交合规声明")
        guide.appendLine("   - 包含测试报告")
        guide.appendLine("   - 符合所有适用标准")
        guide.appendLine()
        
        guide.appendLine("【标签要求】")
        guide.appendLine("1. 产品标签包含责任人信息")
        guide.appendLine("   - 制造商或授权经销商名称")
        guide.appendLine("   - 地址")
        guide.appendLine("   - 电话")
        guide.appendLine("   - 网站")
        guide.appendLine()
        guide.appendLine("2. 召回信息标签")
        guide.appendLine("   - 召回热线")
        guide.appendLine("   - 召回邮箱")
        guide.appendLine("   - 召回网站")
        guide.appendLine()
        guide.appendLine("3. 原产地标记")
        guide.appendLine("   - 清晰标注'Made in [Country]'")
        guide.appendLine("   - 符合19 U.S.C. 1304要求")
        guide.appendLine()
        
        guide.appendLine("【所需文件】")
        guide.appendLine("1. DOT认证证书（原件）")
        guide.appendLine("2. FMVSS合规声明（原件+翻译）")
        guide.appendLine("3. 测试报告（复印件+翻译）")
        guide.appendLine("4. 商业发票（原件+翻译）")
        guide.appendLine("5. 装箱单（原件+翻译）")
        guide.appendLine("6. 原产地证书（原件+翻译）")
        guide.appendLine()
        
        guide.appendLine("【海关检验】")
        guide.appendLine("1. DOT认证核查")
        guide.appendLine("2. 产品标签检查")
        guide.appendLine("3. 责任人信息检查")
        guide.appendLine("4. 原产地标记检查")
        guide.appendLine()
        
        guide.appendLine("【建议】")
        guide.appendLine("1. 提前准备所有所需文件")
        guide.appendLine("2. 确保文件翻译准确")
        guide.appendLine("3. 指定美国境内责任人")
        guide.appendLine("4. 确保产品标签完整准确")
        guide.appendLine("5. 保留所有文件备查")
        
        return guide.toString()
    }

    /**
     * 生成标准更新跟踪指南
     */
    fun generateStandardTrackingGuide(): String {
        val guide = StringBuilder()
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("标准更新跟踪指南")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        
        guide.appendLine("【订阅NHTSA通知】")
        guide.appendLine("1. 加入NHTSA标准更新通知列表")
        guide.appendLine("2. 订阅NHTSA电子邮件通知")
        guide.appendLine("3. 关注NHTSA网站更新")
        guide.appendLine("4. 参加NHTSA行业会议")
        guide.appendLine()
        
        guide.appendLine("【跟踪要点】")
        guide.appendLine("1. FMVSS标准修订通知")
        guide.appendLine("2. 新标准提议")
        guide.appendLine("3. 标准解释澄清")
        guide.appendLine("4. 测试方法更新")
        guide.appendLine("5. 强制生效日期")
        guide.appendLine()
        
        guide.appendLine("【关键时间节点】")
        guide.appendLine("1. 提议发布：关注新要求提议")
        guide.appendLine("2. 公众评论期：参与评论和反馈")
        guide.appendLine("3. 最终发布：了解最终要求")
        guide.appendLine("4. 生效前准备：准备实施")
        guide.appendLine("5. 强制生效：确保合规")
        guide.appendLine()
        
        guide.appendLine("【应对策略】")
        guide.appendLine("1. 建立标准更新跟踪机制")
        guide.appendLine("2. 定期评估标准变更影响")
        guide.appendLine("3. 制定标准更新应对计划")
        guide.appendLine("4. 预留足够实施时间")
        guide.appendLine("5. 与NHTSA保持沟通")
        guide.appendLine()
        
        guide.appendLine("【重要标准】")
        guide.appendLine("1. FMVSS 213：儿童约束系统标准")
        guide.appendLine("   - 当前版本：FMVSS 213a")
        guide.appendLine("   - 生效日期：2025年6月30日")
        guide.appendLine("   - 主要变更：侧面碰撞要求")
        guide.appendLine()
        guide.appendLine("2. FMVSS 225：LATCH系统")
        guide.appendLine("3. FMVSS 302：阻燃性要求")
        guide.appendLine("4. 49 CFR Part 567：认证要求")
        guide.appendLine("5. 49 CFR Part 573：召回要求")
        
        return guide.toString()
    }
}

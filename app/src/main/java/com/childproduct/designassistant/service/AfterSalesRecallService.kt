package com.childproduct.designassistant.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * 售后与召回机制管理服务
 * 
 * 管理产品注册、使用寿命、追溯及召回机制，符合FMVSS 213 S5.8和49 CFR Part 573要求
 */
class AfterSalesRecallService {

    /**
     * 产品注册信息
     */
    data class ProductRegistration(
        val registrationId: String,
        val productSerialNumber: String,
        val productName: String,
        val modelNumber: String,
        val purchaseDate: Date,
        val owner: OwnerInfo,
        val retailer: RetailerInfo,
        val registrationDate: Date,
        val registrationMethod: RegistrationMethod
    )

    /**
     * 所有者信息
     */
    data class OwnerInfo(
        val firstName: String,
        val lastName: String,
        val email: String,
        val phone: String,
        val address: Address
    )

    /**
     * 地址
     */
    data class Address(
        val street: String,
        val city: String,
        val state: String,
        val zipCode: String,
        val country: String = "USA"
    )

    /**
     * 零售商信息
     */
    data class RetailerInfo(
        val retailerName: String,
        val retailerId: String,
        val location: String
    )

    /**
     * 注册方式
     */
    enum class RegistrationMethod(val displayName: String) {
        MAIL("邮寄注册"),
        ONLINE("在线注册"),
        RETAILER("零售商注册"),
        PHONE("电话注册")
    }

    /**
     * 产品使用寿命管理
     */
    data class ProductLifeCycle(
        val serialNumber: String,
        val productionDate: Date,
        val recommendedLifeYears: Int,
        val expiryDate: Date,
        val materialAgingStatus: AgingStatus,
        val inspectionHistory: List<InspectionRecord>,
        val recallHistory: List<RecallRecord>
    )

    /**
     * 老化状态
     */
    enum class AgingStatus(val displayName: String, val score: Int) {
        EXCELLENT("优秀", 5),
        GOOD("良好", 4),
        ACCEPTABLE("可接受", 3),
        DETERIORATED("老化", 2),
        EXPIRED("已过期", 1)
    }

    /**
     * 检验记录
     */
    data class InspectionRecord(
        val inspectionId: String,
        val inspectionDate: Date,
        val inspector: String,
        val findings: List<Finding>,
        val overallStatus: InspectionStatus,
        val recommendations: List<String>
    )

    /**
     * 检查发现
     */
    data class Finding(
        val findingId: String,
        val category: FindingCategory,
        val description: String,
        val severity: Severity,
        val requiresAttention: Boolean
    )

    /**
     * 发现类别
     */
    enum class FindingCategory(val displayName: String) {
        MATERIAL("材料老化"),
        STRUCTURAL("结构完整性"),
        MECHANICAL("机械性能"),
        SAFETY("安全风险")
    }

    /**
     * 严重性
     */
    enum class Severity(val displayName: String, val score: Int) {
        MINOR("轻微", 1),
        MODERATE("中等", 2),
        SERIOUS("严重", 3),
        CRITICAL("关键", 4)
    }

    /**
     * 检验状态
     */
    enum class InspectionStatus(val displayName: String) {
        EXCELLENT("优秀"),
        GOOD("良好"),
        ACCEPTABLE("可接受"),
        REQUIRES_REPLACEMENT("需要更换"),
        REQUIRES_IMMEDIATE_ACTION("需要立即行动")
    }

    /**
     * 召回记录
     */
    data class RecallRecord(
        val recallId: String,
        val recallNumber: String,
        val recallDate: Date,
        val reason: String,
        val description: String,
        val riskLevel: RecallRiskLevel,
        val affectedSerialNumbers: List<String>,
        val remedyType: RemedyType,
        val remedyDescription: String,
        val contactInformation: ContactInfo,
        val status: RecallStatus,
        val completionDate: Date?
    )

    /**
     * 召回风险等级
     */
    enum class RecallRiskLevel(val displayName: String, val score: Int) {
        LOW("低", 1),
        MEDIUM("中", 2),
        HIGH("高", 3),
        SEVERE("严重", 4)
    }

    /**
     * 补救类型
     */
    enum class RemedyType(val displayName: String) {
        REPAIR("维修"),
        REPLACEMENT("更换"),
        REFUND("退款"),
        INSPECTION("检查")
    }

    /**
     * 联系信息
     */
    data class ContactInfo(
        val phone: String,
        val email: String,
        val website: String,
        val tollFree: String
    )

    /**
     * 召回状态
     */
    enum class RecallStatus(val displayName: String) {
        ANNOUNCED("已公告"),
        IN_PROGRESS("进行中"),
        COMPLETED("已完成"),
        CANCELLED("已取消")
    }

    /**
     * 召回管理
     */
    data class RecallManagement(
        val recallId: String,
        val productName: String,
        val affectedUnits: Int,
        val notifiedUnits: Int,
        val remediedUnits: Int,
        val completionRate: Double,
        val timeline: List<TimelineEvent>,
        val communications: List<Communication>
    )

    /**
     * 时间线事件
     */
    data class TimelineEvent(
        val eventId: String,
        val eventType: EventType,
        val eventDate: Date,
        val description: String,
        val responsible: String
    )

    /**
     * 事件类型
     */
    enum class EventType(val displayName: String) {
        ISSUE_DETECTED("问题检测"),
        INVESTIGATION_STARTED("调查开始"),
        NHTSA_NOTIFICATION("通知NHTSA"),
        PUBLIC_ANNOUNCEMENT("公众公告"),
        REMEDY_IMPLEMENTATION("补救实施"),
        COMPLETION("完成")
    }

    /**
     * 通讯
     */
    data class Communication(
        val communicationId: String,
        val communicationType: CommunicationType,
        val sentDate: Date,
        val recipient: String,
        val content: String,
        val deliveryStatus: DeliveryStatus
    )

    /**
     * 通讯类型
     */
    enum class CommunicationType(val displayName: String) {
        MAIL("邮件"),
        EMAIL("电子邮件"),
        PHONE("电话"),
        SMS("短信"),
        WEBSITE("网站公告")
    }

    /**
     * 投递状态
     */
    enum class DeliveryStatus(val displayName: String) {
        PENDING("待发送"),
        SENT("已发送"),
        DELIVERED("已送达"),
        FAILED("失败"),
        READ("已读")
    }

    /**
     * 产品标签要求（符合FMVSS 213 S5.8和49 CFR Part 573）
     */
    data class ProductLabel(
        val labelId: String,
        val productName: String,
        val modelNumber: String,
        val serialNumber: String,
        val productionDate: Date,
        val expiryDate: Date,
        val manufacturerInfo: ManufacturerInfo,
        val certificationInfo: CertificationInfo,
        val recallContact: RecallContactInfo,
        val warnings: List<Warning>
    )

    /**
     * 制造商信息
     */
    data class ManufacturerInfo(
        val name: String,
        val address: Address,
        val phone: String,
        val website: String
    )

    /**
     * 认证信息
     */
    data class CertificationInfo(
        val fmvssStandard: String,
        val certificationDate: Date,
        val certificationNumber: String
    )

    /**
     * 召回联系信息
     */
    data class RecallContactInfo(
        val recallHotline: String,
        val recallEmail: String,
        val recallWebsite: String,
        val complianceInformation: String
    )

    /**
     * 警告
     */
    data class Warning(
        val warningId: String,
        val type: WarningType,
        val text: String,
        val location: String,
        val prominence: Prominence
    )

    /**
     * 警告类型
     */
    enum class WarningType(val displayName: String) {
        REAR_FACING_AIRBAG("后向安装禁放前排气囊位"),
        WEIGHT_LIMIT("重量限制"),
        HEIGHT_LIMIT("高度限制"),
        INSTALLATION("安装要求"),
        RECALL_INFORMATION("召回信息")
    }

    /**
     * 显著性
     */
    enum class Prominence(val displayName: String, val score: Int) {
        LOW("低", 1),
        MEDIUM("中", 2),
        HIGH("高", 3),
        VERY_HIGH("极高", 4)
    }

    /**
     * 推荐使用寿命（年）
     */
    private val recommendedLifeYears = 7

    /**
     * 召回联系信息模板
     */
    private val defaultRecallContact = ContactInfo(
        phone = "+1-800-XXX-XXXX",
        email = "recall@example.com",
        website = "https://www.example.com/recall",
        tollFree = "1-800-XXX-XXXX"
    )

    /**
     * 注册产品
     */
    suspend fun registerProduct(
        serialNumber: String,
        productName: String,
        modelNumber: String,
        purchaseDate: Date,
        owner: OwnerInfo,
        retailer: RetailerInfo,
        method: RegistrationMethod
    ): ProductRegistration = withContext(Dispatchers.Default) {
        ProductRegistration(
            registrationId = "REG-${SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())}",
            productSerialNumber = serialNumber,
            productName = productName,
            modelNumber = modelNumber,
            purchaseDate = purchaseDate,
            owner = owner,
            retailer = retailer,
            registrationDate = Date(),
            registrationMethod = method
        )
    }

    /**
     * 创建产品生命周期记录
     */
    fun createProductLifeCycle(
        serialNumber: String,
        productionDate: Date
    ): ProductLifeCycle {
        val calendar = Calendar.getInstance()
        calendar.time = productionDate
        calendar.add(Calendar.YEAR, recommendedLifeYears)
        val expiryDate = calendar.time

        return ProductLifeCycle(
            serialNumber = serialNumber,
            productionDate = productionDate,
            recommendedLifeYears = recommendedLifeYears,
            expiryDate = expiryDate,
            materialAgingStatus = AgingStatus.EXCELLENT,
            inspectionHistory = emptyList(),
            recallHistory = emptyList()
        )
    }

    /**
     * 检查产品老化状态
     */
    fun checkAgingStatus(lifeCycle: ProductLifeCycle): Pair<AgingStatus, List<Finding>> {
        val currentDate = Date()
        val yearsInUse = (currentDate.time - lifeCycle.productionDate.time) / (365L * 24 * 60 * 60 * 1000)
        val findings = mutableListOf<Finding>()
        
        val status = when {
            yearsInUse > recommendedLifeYears -> {
                findings.add(Finding(
                    findingId = "FIND-001",
                    category = FindingCategory.MATERIAL,
                    description = "产品已超过推荐使用寿命${(yearsInUse - recommendedLifeYears).toInt()}年",
                    severity = Severity.CRITICAL,
                    requiresAttention = true
                ))
                AgingStatus.EXPIRED
            }
            yearsInUse > recommendedLifeYears * 0.8 -> {
                findings.add(Finding(
                    findingId = "FIND-002",
                    category = FindingCategory.MATERIAL,
                    description = "产品接近使用寿命，建议更换",
                    severity = Severity.MODERATE,
                    requiresAttention = true
                ))
                AgingStatus.DETERIORATED
            }
            yearsInUse > recommendedLifeYears * 0.5 -> {
                findings.add(Finding(
                    findingId = "FIND-003",
                    category = FindingCategory.MATERIAL,
                    description = "产品使用超过一半推荐寿命，建议定期检查",
                    severity = Severity.MINOR,
                    requiresAttention = false
                ))
                AgingStatus.ACCEPTABLE
            }
            yearsInUse > recommendedLifeYears * 0.25 -> {
                AgingStatus.GOOD
            }
            else -> AgingStatus.EXCELLENT
        }

        return Pair(status, findings)
    }

    /**
     * 创建召回记录
     */
    fun createRecallRecord(
        recallNumber: String,
        productName: String,
        reason: String,
        description: String,
        riskLevel: RecallRiskLevel,
        affectedSerialNumbers: List<String>,
        remedyType: RemedyType,
        remedyDescription: String,
        contactInfo: ContactInfo
    ): RecallRecord {
        return RecallRecord(
            recallId = "RC-${SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())}",
            recallNumber = recallNumber,
            recallDate = Date(),
            reason = reason,
            description = description,
            riskLevel = riskLevel,
            affectedSerialNumbers = affectedSerialNumbers,
            remedyType = remedyType,
            remedyDescription = remedyDescription,
            contactInformation = contactInfo,
            status = RecallStatus.ANNOUNCED,
            completionDate = null
        )
    }

    /**
     * 创建召回管理
     */
    fun createRecallManagement(
        recallId: String,
        productName: String,
        affectedUnits: Int
    ): RecallManagement {
        return RecallManagement(
            recallId = recallId,
            productName = productName,
            affectedUnits = affectedUnits,
            notifiedUnits = 0,
            remediedUnits = 0,
            completionRate = 0.0,
            timeline = createInitialTimeline(recallId),
            communications = emptyList()
        )
    }

    /**
     * 创建初始时间线
     */
    private fun createInitialTimeline(recallId: String): List<TimelineEvent> {
        return listOf(
            TimelineEvent(
                eventId = "TL-001",
                eventType = EventType.ISSUE_DETECTED,
                eventDate = Date(),
                description = "问题检测",
                responsible = "质量控制"
            ),
            TimelineEvent(
                eventId = "TL-002",
                eventType = EventType.INVESTIGATION_STARTED,
                eventDate = Date(),
                description = "调查开始",
                responsible = "工程团队"
            )
        )
    }

    /**
     * 生成产品标签信息
     */
    fun generateProductLabel(
        productName: String,
        modelNumber: String,
        serialNumber: String,
        productionDate: Date,
        manufacturerInfo: ManufacturerInfo,
        certificationInfo: CertificationInfo,
        recallContact: RecallContactInfo
    ): ProductLabel {
        // 计算到期日期
        val calendar = Calendar.getInstance()
        calendar.time = productionDate
        calendar.add(Calendar.YEAR, recommendedLifeYears)
        val expiryDate = calendar.time

        return ProductLabel(
            labelId = "LBL-${SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())}",
            productName = productName,
            modelNumber = modelNumber,
            serialNumber = serialNumber,
            productionDate = productionDate,
            expiryDate = expiryDate,
            manufacturerInfo = manufacturerInfo,
            certificationInfo = certificationInfo,
            recallContact = recallContact,
            warnings = generateDefaultWarnings()
        )
    }

    /**
     * 生成默认警告
     */
    private fun generateDefaultWarnings(): List<Warning> {
        return listOf(
            Warning(
                warningId = "WARN-001",
                type = WarningType.REAR_FACING_AIRBAG,
                text = "NEVER place a rear-facing CRS in front seat with airbag",
                location = "Front Cover",
                prominence = Prominence.VERY_HIGH
            ),
            Warning(
                warningId = "WARN-002",
                type = WarningType.WEIGHT_LIMIT,
                text = "Maximum weight: XX lbs (XX kg)",
                location = "Side Label",
                prominence = Prominence.HIGH
            ),
            Warning(
                warningId = "WARN-003",
                type = WarningType.HEIGHT_LIMIT,
                text = "Maximum height: XX in (XX cm)",
                location = "Side Label",
                prominence = Prominence.HIGH
            ),
            Warning(
                warningId = "WARN-004",
                type = WarningType.RECALL_INFORMATION,
                text = "For recall information: 1-800-XXX-XXXX or visit www.example.com/recall",
                location = "Permanent Label",
                prominence = Prominence.HIGH
            )
        )
    }

    /**
     * 生成产品注册报告
     */
    suspend fun generateProductRegistrationReport(
        registrations: List<ProductRegistration>
    ): String = withContext(Dispatchers.Default) {
        val report = StringBuilder()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        
        report.appendLine("=" .repeat(70))
        report.appendLine("产品注册报告")
        report.appendLine("=" .repeat(70))
        report.appendLine()
        
        report.appendLine("注册总数：${registrations.size}")
        report.appendLine()
        
        // 注册方式统计
        report.appendLine("-" .repeat(70))
        report.appendLine("注册方式统计")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        registrations.groupBy { it.registrationMethod }.forEach { (method, regs) ->
            report.appendLine("${method.displayName}: ${regs.size} (${regs.size * 100 / registrations.size}%)")
        }
        report.appendLine()
        
        // 地区分布
        report.appendLine("-" .repeat(70))
        report.appendLine("地区分布")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        registrations.groupBy { it.owner.address.state }.forEach { (state, regs) ->
            report.appendLine("$state: ${regs.size} (${regs.size * 100 / registrations.size}%)")
        }
        report.appendLine()
        
        // 近期注册
        report.appendLine("-" .repeat(70))
        report.appendLine("近期注册（最近10条）")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        registrations.takeLast(10).forEach { reg ->
            report.appendLine("注册ID：${reg.registrationId}")
            report.appendLine("产品名称：${reg.productName}")
            report.appendLine("型号：${reg.modelNumber}")
            report.appendLine("序列号：${reg.productSerialNumber}")
            report.appendLine("所有者：${reg.owner.firstName} ${reg.owner.lastName}")
            report.appendLine("注册方式：${reg.registrationMethod.displayName}")
            report.appendLine("注册日期：${dateFormat.format(reg.registrationDate)}")
            report.appendLine()
        }
        
        report.toString()
    }

    /**
     * 生成召回管理报告
     */
    suspend fun generateRecallManagementReport(
        recallManagement: RecallManagement
    ): String = withContext(Dispatchers.Default) {
        val report = StringBuilder()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        
        report.appendLine("=" .repeat(70))
        report.appendLine("召回管理报告")
        report.appendLine("=" .repeat(70))
        report.appendLine()
        
        report.appendLine("召回ID：${recallManagement.recallId}")
        report.appendLine("产品名称：${recallManagement.productName}")
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("召回进度")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        report.appendLine("受影响单位：${recallManagement.affectedUnits}")
        report.appendLine("已通知单位：${recallManagement.notifiedUnits}")
        report.appendLine("已补救单位：${recallManagement.remediedUnits}")
        report.appendLine("完成率：${String.format("%.1f", recallManagement.completionRate)}%")
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("时间线")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        recallManagement.timeline.forEach { event ->
            report.appendLine("${dateFormat.format(event.eventDate)} - ${event.eventType.displayName}")
            report.appendLine("  描述：${event.description}")
            report.appendLine("  负责人：${event.responsible}")
            report.appendLine()
        }
        
        report.appendLine("-" .repeat(70))
        report.appendLine("通讯记录")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        if (recallManagement.communications.isEmpty()) {
            report.appendLine("暂无通讯记录")
        } else {
            recallManagement.communications.takeLast(10).forEach { comm ->
                report.appendLine("${dateFormat.format(comm.sentDate)} - ${comm.communicationType.displayName}")
                report.appendLine("  收件人：${comm.recipient}")
                report.appendLine("  状态：${comm.deliveryStatus.displayName}")
                report.appendLine()
            }
        }
        
        report.appendLine("-" .repeat(70))
        report.appendLine("建议")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        report.appendLine("1. 确保召回通知覆盖所有受影响单位")
        report.appendLine("2. 使用多种通讯方式（邮件、电话、网站）提高通知率")
        report.appendLine("3. 跟踪补救进度，定期更新召回状态")
        report.appendLine("4. 向NHTSA提交召回进展报告")
        report.appendLine("5. 确保召回完成后通知相关单位")
        
        report.toString()
    }

    /**
     * 生成产品标签要求指南
     */
    fun generateProductLabelGuide(): String {
        val guide = StringBuilder()
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("产品标签要求指南")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        guide.appendLine("符合标准：FMVSS 213 S5.8 和 49 CFR Part 573")
        guide.appendLine()
        
        guide.appendLine("【永久标签信息】")
        guide.appendLine("1. 制造商名称和地址（或授权经销商）")
        guide.appendLine("2. 产品名称和型号")
        guide.appendLine("3. 生产日期（月/年格式）")
        guide.appendLine("4. 产品序列号（唯一标识）")
        guide.appendLine("5. 使用截止日期（生产日期+7年）")
        guide.appendLine("6. FMVSS 213符合性声明")
        guide.appendLine("7. 召回联系信息（电话、邮箱、网站）")
        guide.appendLine()
        
        guide.appendLine("【警告要求】")
        guide.appendLine("1. 'NEVER place a rear-facing CRS in front seat with airbag'（最高显著位置）")
        guide.appendLine("2. 重量限制警告")
        guide.appendLine("3. 高度限制警告")
        guide.appendLine("4. 召回信息通知渠道")
        guide.appendLine("5. 安装要求警告")
        guide.appendLine()
        
        guide.appendLine("【召回联系信息】")
        guide.appendLine("1. 美国境内免费电话（Toll-free）")
        guide.appendLine("2. 召回专用邮箱")
        guide.appendLine("3. 召回专用网站")
        guide.appendLine("4. 符合NHTSA的召回管理要求（49 CFR Part 573）")
        guide.appendLine()
        
        guide.appendLine("【标签位置】")
        guide.appendLine("1. 封面：最高显著警告（后向安装禁放前排气囊位）")
        guide.appendLine("2. 侧面标签：产品信息、生产日期、使用截止日期")
        guide.appendLine("3. 永久标签：制造商信息、召回联系信息、认证信息")
        guide.appendLine()
        
        guide.appendLine("【语言要求】")
        guide.appendLine("1. 英语（必须）")
        guide.appendLine("2. 西班牙语（推荐）")
        guide.appendLine("3. 文字清晰可读，字体大小符合要求")
        
        return guide.toString()
    }

    /**
     * 生成召回流程指南
     */
    fun generateRecallProcessGuide(): String {
        val guide = StringBuilder()
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("召回流程指南")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        guide.appendLine("符合标准：49 CFR Part 573")
        guide.appendLine()
        
        guide.appendLine("【召回触发条件】")
        guide.appendLine("1. 发现产品存在安全风险")
        guide.appendLine("2. 产品不符合FMVSS标准")
        guide.appendLine("3. 接到NHTSA召回要求")
        guide.appendLine()
        
        guide.appendLine("【召回流程】")
        guide.appendLine("1. 问题检测与调查")
        guide.appendLine("   - 检测产品问题")
        guide.appendLine("   - 调查问题原因和影响范围")
        guide.appendLine("   - 评估风险等级")
        guide.appendLine()
        guide.appendLine("2. 通知NHTSA")
        guide.appendLine("   - 向NHTSA提交召回通知")
        guide.appendLine("   - 提供召回计划和补救措施")
        guide.appendLine("   - 获取NHTSA批准")
        guide.appendLine()
        guide.appendLine("3. 公众公告")
        guide.appendLine("   - 发布召回公告")
        guide.appendLine("   - 通过多种渠道通知用户")
        guide.appendLine("   - 提供召回热线和网站")
        guide.appendLine()
        guide.appendLine("4. 补救措施实施")
        guide.appendLine("   - 维修、更换或退款")
        guide.appendLine("   - 跟踪补救进度")
        guide.appendLine("   - 记录所有补救活动")
        guide.appendLine()
        guide.appendLine("5. 召回完成")
        guide.appendLine("   - 向NHTSA提交召回完成报告")
        guide.appendLine("   - 更新产品注册信息")
        guide.appendLine("   - 关闭召回记录")
        guide.appendLine()
        
        guide.appendLine("【召回通知要求】")
        guide.appendLine("1. 通知方式：邮件、电话、网站、社交媒体")
        guide.appendLine("2. 通知内容：召回原因、风险描述、补救措施、联系方式")
        guide.appendLine("3. 通知时间：检测到问题后5个工作日内通知NHTSA")
        guide.appendLine("4. 通知覆盖：确保所有受影响用户都能收到通知")
        guide.appendLine()
        
        guide.appendLine("【补救措施类型】")
        guide.appendLine("1. 维修：修复缺陷部件")
        guide.appendLine("2. 更换：更换缺陷产品或部件")
        guide.appendLine("3. 退款：全额退款")
        guide.appendLine("4. 检查：确认产品无安全风险")
        guide.appendLine()
        
        guide.appendLine("【记录保持】")
        guide.appendLine("1. 保留所有召回记录至少10年")
        guide.appendLine("2. 保留NHTSA通信记录")
        guide.appendLine("3. 保留用户通知记录")
        guide.appendLine("4. 保留补救措施记录")
        
        return guide.toString()
    }

    /**
     * 生成使用寿命管理指南
     */
    fun generateLifeCycleManagementGuide(): String {
        val guide = StringBuilder()
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("产品使用寿命管理指南")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        
        guide.appendLine("【推荐使用寿命】")
        guide.appendLine("1. 推荐使用寿命：7年")
        guide.appendLine("2. 使用截止日期：生产日期+7年")
        guide.appendLine("3. 超期使用风险：材料老化、性能衰减、安全风险增加")
        guide.appendLine()
        
        guide.appendLine("【材料老化测试】")
        guide.appendLine("1. 织带：1000小时耐光测试（ASTM G154）")
        guide.appendLine("2. 塑料件：500小时高温老化测试（ASTM D4329）")
        guide.appendLine("3. 金属件：盐雾试验（ASTM B117）")
        guide.appendLine("4. 泡沫：压缩永久变形测试（ASTM D3574）")
        guide.appendLine()
        
        guide.appendLine("【老化状态评估】")
        guide.appendLine("1. 优秀（0-25%寿命）：无老化迹象")
        guide.appendLine("2. 良好（25-50%寿命）：轻微老化")
        guide.appendLine("3. 可接受（50-80%寿命）：中度老化，需定期检查")
        guide.appendLine("4. 老化（80-100%寿命）：显著老化，建议更换")
        guide.appendLine("5. 已过期（>100%寿命）：超过使用寿命，必须更换")
        guide.appendLine()
        
        guide.appendLine("【检查频率】")
        guide.appendLine("1. 正常使用：每年检查一次")
        guide.appendLine("2. 接近使用寿命：每季度检查一次")
        guide.appendLine("3. 使用超过5年：每月检查一次")
        guide.appendLine()
        
        guide.appendLine("【检查项目】")
        guide.appendLine("1. 织带：检查是否有磨损、断裂、脱色")
        guide.appendLine("2. 塑料件：检查是否有裂纹、变形")
        guide.appendLine("3. 金属件：检查是否有锈蚀、变形")
        guide.appendLine("4. 泡沫：检查是否有塌陷、硬化")
        guide.appendLine("5. 卡扣：检查是否能正常锁定和释放")
        guide.appendLine()
        
        guide.appendLine("【更换建议】")
        guide.appendLine("1. 超过使用寿命：必须更换")
        guide.appendLine("2. 发生碰撞事故：必须更换")
        guide.appendLine("3. 发现严重缺陷：必须更换")
        guide.appendLine("4. 无法通过安全检查：必须更换")
        
        return guide.toString()
    }
}

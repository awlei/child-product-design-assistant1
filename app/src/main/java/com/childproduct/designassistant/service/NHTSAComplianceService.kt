package com.childproduct.designassistant.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * NHTSA认证流程管理服务
 * 
 * 管理CRS产品通过NHTSA（美国国家公路交通安全管理局）认证的全流程
 * 包含：认证申请、实验室资质、样品一致性、测试周期管理等
 */
class NHTSAComplianceService {

    /**
     * 认证流程状态
     */
    data class CertificationProcess(
        val processId: String,
        val productName: String,
        val manufacturer: String,
        val currentPhase: CertificationPhase,
        val phases: List<PhaseStatus>,
        val selectedLab: Laboratory?,
        val testSampleStatus: TestSampleStatus,
        val documentStatus: DocumentStatus,
        val startDate: Date,
        val estimatedCompletionDate: Date,
        val notes: List<String>
    )

    /**
     * 认证阶段
     */
    enum class CertificationPhase(val displayName: String, val durationWeeks: Int) {
        PREPARATION("准备阶段", 4),
        LAB_SELECTION("实验室选择", 2),
        DOCUMENT_SUBMISSION("文档提交", 2),
        TESTING("测试执行", 12),
        REPORT_REVIEW("报告审核", 4),
        CERTIFICATION_ISSUANCE("认证颁发", 2),
        POST_CERTIFICATION("认证后维护", 26)
    }

    /**
     * 阶段状态
     */
    data class PhaseStatus(
        val phase: CertificationPhase,
        val status: PhaseStatusType,
        val progress: Double,
        val startDate: Date?,
        val completionDate: Date?,
        val tasks: List<Task>
    )

    /**
     * 阶段状态类型
     */
    enum class PhaseStatusType(val displayName: String) {
        NOT_STARTED("未开始"),
        IN_PROGRESS("进行中"),
        COMPLETED("已完成"),
        BLOCKED("已阻塞"),
        REQUIRES_RETEST("需要重新测试")
    }

    /**
     * 任务
     */
    data class Task(
        val taskId: String,
        val name: String,
        val description: String,
        val status: TaskStatus,
        val assignedTo: String?,
        val dueDate: Date?,
        val priority: Priority
    )

    /**
     * 任务状态
     */
    enum class TaskStatus(val displayName: String) {
        NOT_STARTED("未开始"),
        IN_PROGRESS("进行中"),
        COMPLETED("已完成"),
        FAILED("失败")
    }

    /**
     * 优先级
     */
    enum class Priority(val displayName: String, val value: Int) {
        LOW("低", 1),
        MEDIUM("中", 2),
        HIGH("高", 3),
        CRITICAL("关键", 4)
    }

    /**
     * 实验室资质
     */
    data class Laboratory(
        val labId: String,
        val name: String,
        val isNHTSAApproved: Boolean,
        val certificationNumber: String,
        val location: String,
        val specialties: List<String>,
        val testCapacity: TestCapacity,
        val contactInfo: ContactInfo,
        val averageLeadTime: Int,  // 平均等待时间（周）
        val reputationScore: Double  // 信誉评分（0-5）
    )

    /**
     * 测试容量
     */
    data class TestCapacity(
        val frontalCrash: Boolean,
        val sideCrash: Boolean,
        val componentTests: Boolean,
        val environmentalTests: Boolean,
        val maxConcurrentTests: Int
    )

    /**
     * 联系信息
     */
    data class ContactInfo(
        val phone: String,
        val email: String,
        val address: String,
        val website: String
    )

    /**
     * 测试样品状态
     */
    data class TestSampleStatus(
        val sampleId: String,
        val productName: String,
        val serialNumber: String,
        val productionDate: Date,
        val materialBatch: String,
        val componentSuppliers: Map<String, String>,
        val assemblyLocation: String,
        val isConsistentWithProduction: Boolean,
        val technicalStatusFrozen: Boolean,
        val testHistory: List<TestRecord>
    )

    /**
     * 测试记录
     */
    data class TestRecord(
        val testId: String,
        val testName: String,
        val testDate: Date,
        val result: TestResult,
        val passed: Boolean,
        val findings: String,
        val requiredRetest: Boolean
    )

    /**
     * 测试结果
     */
    enum class TestResult(val displayName: String) {
        PASS("通过"),
        FAIL("未通过"),
        CONDITIONAL_PASS("有条件通过"),
        INCONCLUSIVE("不确定")
    }

    /**
     * 文档状态
     */
    data class DocumentStatus(
        val certificateOfCompliance: DocumentItem,
        val technicalDocumentation: DocumentItem,
        val testPlan: DocumentItem,
        val materialSpecifications: DocumentItem,
        val structuralDrawings: DocumentItem
    )

    /**
     * 文档项
     */
    data class DocumentItem(
        val documentId: String,
        val name: String,
        val status: DocumentStatusType,
        val version: String,
        val lastUpdated: Date,
        val requiredBy: String,
        val submittedDate: Date?
    )

    /**
     * 文档状态类型
     */
    enum class DocumentStatusType(val displayName: String) {
        NOT_STARTED("未开始"),
        IN_PREPARATION("准备中"),
        SUBMITTED("已提交"),
        UNDER_REVIEW("审核中"),
        APPROVED("已批准"),
        REJECTED("已拒绝")
    }

    /**
     * NHTSA认可的实验室列表
     */
    private val approvedLaboratories = listOf(
        Laboratory(
            labId = "LAB-UTAC",
            name = "UTAC",
            isNHTSAApproved = true,
            certificationNumber = "NHTSA-LAB-001",
            location = "美国密歇根州",
            specialties = listOf("正面碰撞", "侧面碰撞", "组件测试"),
            testCapacity = TestCapacity(
                frontalCrash = true,
                sideCrash = true,
                componentTests = true,
                environmentalTests = true,
                maxConcurrentTests = 3
            ),
            contactInfo = ContactInfo(
                phone = "+1-248-555-0101",
                email = "info@utac-usa.com",
                address = "1234 Test Road, Detroit, MI 48201",
                website = "https://www.utac-usa.com"
            ),
            averageLeadTime = 8,
            reputationScore = 4.8
        ),
        Laboratory(
            labId = "LAB-INTERTEK",
            name = "Intertek",
            isNHTSAApproved = true,
            certificationNumber = "NHTSA-LAB-002",
            location = "美国加利福尼亚州",
            specialties = listOf("正面碰撞", "侧面碰撞", "环境测试", "阻燃测试"),
            testCapacity = TestCapacity(
                frontalCrash = true,
                sideCrash = true,
                componentTests = true,
                environmentalTests = true,
                maxConcurrentTests = 4
            ),
            contactInfo = ContactInfo(
                phone = "+1-510-555-0202",
                email = "info@intertek-usa.com",
                address = "5678 Certification Way, San Francisco, CA 94105",
                website = "https://www.intertek-usa.com"
            ),
            averageLeadTime = 6,
            reputationScore = 4.9
        ),
        Laboratory(
            labId = "LAB-EXOVA",
            name = "Exova",
            isNHTSAApproved = true,
            certificationNumber = "NHTSA-LAB-003",
            location = "美国德克萨斯州",
            specialties = listOf("正面碰撞", "组件测试", "材料测试"),
            testCapacity = TestCapacity(
                frontalCrash = true,
                sideCrash = false,
                componentTests = true,
                environmentalTests = true,
                maxConcurrentTests = 2
            ),
            contactInfo = ContactInfo(
                phone = "+1-512-555-0303",
                email = "info@exova-usa.com",
                address = "9012 Testing Boulevard, Austin, TX 78701",
                website = "https://www.exova-usa.com"
            ),
            averageLeadTime = 10,
            reputationScore = 4.5
        )
    )

    /**
     * 初始化NHTSA认证流程
     */
    suspend fun initializeCertificationProcess(
        productName: String,
        manufacturer: String
    ): CertificationProcess = withContext(Dispatchers.Default) {
        val processId = "NHTSA-${SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())}"
        val startDate = Date()
        
        // 计算预计完成日期（约6个月）
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        calendar.add(Calendar.WEEK_OF_YEAR, 24)
        val estimatedCompletionDate = calendar.time

        CertificationProcess(
            processId = processId,
            productName = productName,
            manufacturer = manufacturer,
            currentPhase = CertificationPhase.PREPARATION,
            phases = initializePhases(),
            selectedLab = null,
            testSampleStatus = TestSampleStatus(
                sampleId = "",
                productName = productName,
                serialNumber = "",
                productionDate = Date(),
                materialBatch = "",
                componentSuppliers = emptyMap(),
                assemblyLocation = "",
                isConsistentWithProduction = false,
                technicalStatusFrozen = false,
                testHistory = emptyList()
            ),
            documentStatus = DocumentStatus(
                certificateOfCompliance = DocumentItem(
                    documentId = "DOC-COC-001",
                    name = "合规声明（Certificate of Compliance）",
                    status = DocumentStatusType.NOT_STARTED,
                    version = "1.0",
                    lastUpdated = Date(),
                    requiredBy = "FMVSS 213",
                    submittedDate = null
                ),
                technicalDocumentation = DocumentItem(
                    documentId = "DOC-TECH-001",
                    name = "产品技术文档",
                    status = DocumentStatusType.NOT_STARTED,
                    version = "1.0",
                    lastUpdated = Date(),
                    requiredBy = "FMVSS 213",
                    submittedDate = null
                ),
                testPlan = DocumentItem(
                    documentId = "DOC-PLAN-001",
                    name = "测试方案",
                    status = DocumentStatusType.NOT_STARTED,
                    version = "1.0",
                    lastUpdated = Date(),
                    requiredBy = "NHTSA",
                    submittedDate = null
                ),
                materialSpecifications = DocumentItem(
                    documentId = "DOC-MAT-001",
                    name = "材料规格书",
                    status = DocumentStatusType.NOT_STARTED,
                    version = "1.0",
                    lastUpdated = Date(),
                    requiredBy = "FMVSS 213",
                    submittedDate = null
                ),
                structuralDrawings = DocumentItem(
                    documentId = "DOC-DRAW-001",
                    name = "结构图纸",
                    status = DocumentStatusType.NOT_STARTED,
                    version = "1.0",
                    lastUpdated = Date(),
                    requiredBy = "FMVSS 213",
                    submittedDate = null
                )
            ),
            startDate = startDate,
            estimatedCompletionDate = estimatedCompletionDate,
            notes = listOf(
                "NHTSA认证流程已初始化",
                "预计测试周期：3-6个月",
                "需选择NHTSA认可的实验室进行测试"
            )
        )
    }

    /**
     * 初始化认证阶段
     */
    private fun initializePhases(): List<PhaseStatus> {
        return CertificationPhase.values().map { phase ->
            PhaseStatus(
                phase = phase,
                status = if (phase == CertificationPhase.PREPARATION) {
                    PhaseStatusType.IN_PROGRESS
                } else {
                    PhaseStatusType.NOT_STARTED
                },
                progress = if (phase == CertificationPhase.PREPARATION) 0.0 else 0.0,
                startDate = if (phase == CertificationPhase.PREPARATION) Date() else null,
                completionDate = null,
                tasks = getPhaseTasks(phase)
            )
        }
    }

    /**
     * 获取阶段任务
     */
    private fun getPhaseTasks(phase: CertificationPhase): List<Task> {
        return when (phase) {
            CertificationPhase.PREPARATION -> listOf(
                Task(
                    taskId = "PREP-001",
                    name = "准备产品技术文档",
                    description = "准备材料规格、结构图纸、测试方案",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "设计工程师",
                    dueDate = null,
                    priority = Priority.HIGH
                ),
                Task(
                    taskId = "PREP-002",
                    name = "准备测试样品",
                    description = "确保测试样品与量产产品完全一致",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "生产工程师",
                    dueDate = null,
                    priority = Priority.CRITICAL
                ),
                Task(
                    taskId = "PREP-003",
                    name = "冻结技术状态",
                    description = "记录材料批次、组件供应商、装配工艺",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "质量工程师",
                    dueDate = null,
                    priority = Priority.CRITICAL
                )
            )
            CertificationPhase.LAB_SELECTION -> listOf(
                Task(
                    taskId = "LAB-001",
                    name = "评估NHTSA认可实验室",
                    description = "比较实验室能力、等待时间、信誉",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "认证专员",
                    dueDate = null,
                    priority = Priority.HIGH
                ),
                Task(
                    taskId = "LAB-002",
                    name = "选择并预约实验室",
                    description = "选择最适合的实验室并预约测试",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "认证专员",
                    dueDate = null,
                    priority = Priority.HIGH
                )
            )
            CertificationPhase.DOCUMENT_SUBMISSION -> listOf(
                Task(
                    taskId = "DOC-001",
                    name = "提交合规声明",
                    description = "提交Certificate of Compliance",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "认证专员",
                    dueDate = null,
                    priority = Priority.CRITICAL
                ),
                Task(
                    taskId = "DOC-002",
                    name = "提交技术文档",
                    description = "提交材料规格、结构图纸、测试方案",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "设计工程师",
                    dueDate = null,
                    priority = Priority.HIGH
                )
            )
            CertificationPhase.TESTING -> listOf(
                Task(
                    taskId = "TEST-001",
                    name = "组件测试",
                    description = "织带强度、材料阻燃、硬件性能测试",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "实验室",
                    dueDate = null,
                    priority = Priority.HIGH
                ),
                Task(
                    taskId = "TEST-002",
                    name = "正面碰撞测试",
                    description = "FMVSS 213正面碰撞动态测试",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "实验室",
                    dueDate = null,
                    priority = Priority.CRITICAL
                ),
                Task(
                    taskId = "TEST-003",
                    name = "侧面碰撞测试",
                    description = "FMVSS 213a侧面碰撞动态测试",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "实验室",
                    dueDate = null,
                    priority = Priority.CRITICAL
                )
            )
            CertificationPhase.REPORT_REVIEW -> listOf(
                Task(
                    taskId = "REVIEW-001",
                    name = "审核测试报告",
                    description = "审核实验室提交的测试报告",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "认证专员",
                    dueDate = null,
                    priority = Priority.HIGH
                ),
                Task(
                    taskId = "REVIEW-002",
                    name = "处理测试发现",
                    description = "处理测试中发现的问题",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "设计工程师",
                    dueDate = null,
                    priority = Priority.CRITICAL
                )
            )
            CertificationPhase.CERTIFICATION_ISSUANCE -> listOf(
                Task(
                    taskId = "CERT-001",
                    name = "申请认证证书",
                    description = "向NHTSA申请认证证书",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "认证专员",
                    dueDate = null,
                    priority = Priority.CRITICAL
                ),
                Task(
                    taskId = "CERT-002",
                    name = "更新产品标签",
                    description = "在产品标签上添加认证信息",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "生产工程师",
                    dueDate = null,
                    priority = Priority.HIGH
                )
            )
            CertificationPhase.POST_CERTIFICATION -> listOf(
                Task(
                    taskId = "POST-001",
                    name = "维护样品一致性",
                    description = "确保量产产品与测试样品一致",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "质量工程师",
                    dueDate = null,
                    priority = Priority.HIGH
                ),
                Task(
                    taskId = "POST-002",
                    name = "跟踪标准更新",
                    description = "跟踪FMVSS标准更新",
                    status = TaskStatus.NOT_STARTED,
                    assignedTo = "合规专员",
                    dueDate = null,
                    priority = Priority.MEDIUM
                )
            )
        }
    }

    /**
     * 获取NHTSA认可的实验室列表
     */
    fun getApprovedLaboratories(): List<Laboratory> {
        return approvedLaboratories
    }

    /**
     * 根据测试需求推荐实验室
     */
    fun recommendLaboratory(
        requiresFrontalCrash: Boolean,
        requiresSideCrash: Boolean,
        requiresEnvironmentalTests: Boolean,
        maxLeadTimeWeeks: Int
    ): List<Laboratory> {
        return approvedLaboratories.filter { lab ->
            (!requiresFrontalCrash || lab.testCapacity.frontalCrash) &&
            (!requiresSideCrash || lab.testCapacity.sideCrash) &&
            (!requiresEnvironmentalTests || lab.testCapacity.environmentalTests) &&
            lab.averageLeadTime <= maxLeadTimeWeeks
        }.sortedByDescending { it.reputationScore }
    }

    /**
     * 验证测试样品一致性
     */
    suspend fun verifyTestSampleConsistency(
        testSample: TestSampleStatus,
        productionSample: TestSampleStatus
    ): Pair<Boolean, List<String>> = withContext(Dispatchers.Default) {
        val issues = mutableListOf<String>()
        var isConsistent = true

        // 检查材料批次
        if (testSample.materialBatch != productionSample.materialBatch) {
            issues.add("材料批次不一致：测试样品${testSample.materialBatch}，量产样品${productionSample.materialBatch}")
            isConsistent = false
        }

        // 检查组件供应商
        val testSuppliers = testSample.componentSuppliers
        val productionSuppliers = productionSample.componentSuppliers
        
        testSuppliers.forEach { (component, supplier) ->
            if (productionSuppliers[component] != supplier) {
                issues.add("组件" + component + "供应商不一致：测试样品" + supplier + "，量产样品" + (productionSuppliers[component] ?: ""))
                isConsistent = false
            }
        }

        // 检查装配位置
        if (testSample.assemblyLocation != productionSample.assemblyLocation) {
            issues.add("装配位置不一致：测试样品${testSample.assemblyLocation}，量产样品${productionSample.assemblyLocation}")
            isConsistent = false
        }

        Pair(isConsistent, issues)
    }

    /**
     * 生成认证流程报告
     */
    suspend fun generateCertificationReport(
        process: CertificationProcess
    ): String = withContext(Dispatchers.Default) {
        val report = StringBuilder()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        
        report.appendLine("=" .repeat(70))
        report.appendLine("NHTSA认证流程报告")
        report.appendLine("=" .repeat(70))
        report.appendLine()
        report.appendLine("流程ID：${process.processId}")
        report.appendLine("产品名称：${process.productName}")
        report.appendLine("制造商：${process.manufacturer}")
        report.appendLine("当前阶段：${process.currentPhase.displayName}")
        report.appendLine("开始日期：${dateFormat.format(process.startDate)}")
        report.appendLine("预计完成：${dateFormat.format(process.estimatedCompletionDate)}")
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("实验室信息")
        report.appendLine("-" .repeat(70))
        if (process.selectedLab != null) {
            report.appendLine("已选择实验室：${process.selectedLab.name}")
            report.appendLine("认证编号：${process.selectedLab.certificationNumber}")
            report.appendLine("位置：${process.selectedLab.location}")
            report.appendLine("平均等待时间：${process.selectedLab.averageLeadTime}周")
            report.appendLine("信誉评分：${process.selectedLab.reputationScore}/5.0")
        } else {
            report.appendLine("尚未选择实验室")
        }
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("测试样品状态")
        report.appendLine("-" .repeat(70))
        report.appendLine("样品ID：${process.testSampleStatus.sampleId}")
        report.appendLine("序列号：${process.testSampleStatus.serialNumber}")
        report.appendLine("生产日期：${dateFormat.format(process.testSampleStatus.productionDate)}")
        report.appendLine("材料批次：${process.testSampleStatus.materialBatch}")
        report.appendLine("与量产一致：${if (process.testSampleStatus.isConsistentWithProduction) "是" else "否"}")
        report.appendLine("技术状态冻结：${if (process.testSampleStatus.technicalStatusFrozen) "是" else "否"}")
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("认证阶段进度")
        report.appendLine("-" .repeat(70))
        process.phases.forEach { phase ->
            val statusIcon = when (phase.status) {
                PhaseStatusType.NOT_STARTED -> "○"
                PhaseStatusType.IN_PROGRESS -> "◐"
                PhaseStatusType.COMPLETED -> "●"
                PhaseStatusType.BLOCKED -> "⊘"
                PhaseStatusType.REQUIRES_RETEST -> "⊝"
            }
            report.appendLine("$statusIcon ${phase.phase.displayName} - ${String.format("%.1f", phase.progress)}%")
            
            if (phase.status == PhaseStatusType.IN_PROGRESS || phase.status == PhaseStatusType.BLOCKED) {
                phase.tasks.forEach { task ->
                    val taskIcon = when (task.status) {
                        TaskStatus.NOT_STARTED -> "  ○"
                        TaskStatus.IN_PROGRESS -> "  ◐"
                        TaskStatus.COMPLETED -> "  ●"
                        TaskStatus.FAILED -> "  ✗"
                    }
                    report.appendLine("$taskIcon ${task.name}")
                }
            }
        }
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("文档状态")
        report.appendLine("-" .repeat(70))
        report.appendLine("合规声明：${process.documentStatus.certificateOfCompliance.status.displayName}")
        report.appendLine("技术文档：${process.documentStatus.technicalDocumentation.status.displayName}")
        report.appendLine("测试方案：${process.documentStatus.testPlan.status.displayName}")
        report.appendLine("材料规格：${process.documentStatus.materialSpecifications.status.displayName}")
        report.appendLine("结构图纸：${process.documentStatus.structuralDrawings.status.displayName}")
        report.appendLine()
        
        if (process.notes.isNotEmpty()) {
            report.appendLine("-" .repeat(70))
            report.appendLine("备注")
            report.appendLine("-" .repeat(70))
            process.notes.forEach { note ->
                report.appendLine("- $note")
            }
            report.appendLine()
        }
        
        report.appendLine("=" .repeat(70))
        report.appendLine("建议")
        report.appendLine("=" .repeat(70))
        report.appendLine("1. 确保测试样品与量产产品完全一致")
        report.appendLine("2. 选择NHTSA认可的实验室进行测试")
        report.appendLine("3. 预留3-6个月的测试周期")
        report.appendLine("4. 冻结技术状态，避免后续变更导致合规失效")
        report.appendLine("5. 保留所有测试样品和文档以备NHTSA抽查")
        
        report.toString()
    }

    /**
     * 生成实验室推荐报告
     */
    fun generateLaboratoryRecommendation(
        recommendations: List<Laboratory>
    ): String {
        val report = StringBuilder()
        
        report.appendLine("=" .repeat(70))
        report.appendLine("NHTSA认可实验室推荐")
        report.appendLine("=" .repeat(70))
        report.appendLine()
        
        if (recommendations.isEmpty()) {
            report.appendLine("未找到符合要求的实验室")
            return report.toString()
        }
        
        recommendations.forEachIndexed { index, lab ->
            report.appendLine("推荐${index + 1}：${lab.name}")
            report.appendLine("  - 认证编号：${lab.certificationNumber}")
            report.appendLine("  - 位置：${lab.location}")
            report.appendLine("  - 信誉评分：${lab.reputationScore}/5.0")
            report.appendLine("  - 平均等待时间：${lab.averageLeadTime}周")
            report.appendLine("  - 专长：${lab.specialties.joinToString(", ")}")
            report.appendLine("  - 联系电话：${lab.contactInfo.phone}")
            report.appendLine("  - 电子邮件：${lab.contactInfo.email}")
            report.appendLine("  - 网站：${lab.contactInfo.website}")
            report.appendLine()
        }
        
        report.appendLine("=" .repeat(70))
        report.appendLine("选择建议")
        report.appendLine("=" .repeat(70))
        report.appendLine("1. 优先选择信誉评分高的实验室（≥4.5分）")
        report.appendLine("2. 考虑等待时间，避免影响产品上市计划")
        report.appendLine("3. 确认实验室具备所需的测试能力")
        report.appendLine("4. 提前预约，避免排队等待")
        
        return report.toString()
    }
}

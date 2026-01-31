package com.childproduct.designassistant.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * NPD (New Product Development) 流程管理服务
 * 
 * 管理儿童安全座椅新产品开发的完整流程
 * 包含：需求分析、概念设计、原型开发、测试验证、合规认证等阶段
 */
class NPDProcessService {

    companion object {
        // 流程阶段定义
        private val PHASES = listOf(
            "1. 需求分析",
            "2. 市场调研",
            "3. 概念设计",
            "4. 详细设计",
            "5. 原型开发",
            "6. 测试验证",
            "7. 合规认证",
            "8. 量产准备"
        )
    }

    /**
     * NPD流程状态
     */
    data class NPDProcessStatus(
        val processId: String,
        val productName: String,
        val currentPhase: Int,              // 当前阶段索引（0-based）
        val phases: List<PhaseStatus>,
        val progress: Double,               // 总进度（0-100%）
        val startDate: Date,
        val estimatedCompletionDate: Date?,
        val deliverables: List<Deliverable>,
        val status: ProcessStatus,
        val notes: List<String>
    )

    /**
     * 阶段状态
     */
    data class PhaseStatus(
        val phaseName: String,
        val phaseIndex: Int,
        val status: PhaseStatusType,
        val progress: Double,               // 阶段进度（0-100%）
        val startDate: Date?,
        val completionDate: Date?,
        val deliverables: List<Deliverable>,
        val blockers: List<String>          // 阻塞问题
    )

    /**
     * 阶段状态类型
     */
    enum class PhaseStatusType(val displayName: String, val color: String) {
        NOT_STARTED("未开始", "#CCCCCC"),
        IN_PROGRESS("进行中", "#2196F3"),
        COMPLETED("已完成", "#4CAF50"),
        BLOCKED("已阻塞", "#F44336"),
        SKIPPED("已跳过", "#9E9E9E")
    }

    /**
     * 交付物
     */
    data class Deliverable(
        val id: String,
        val name: String,
        val type: DeliverableType,
        val status: DeliverableStatus,
        val priority: Priority,
        val assignedTo: String?,
        val dueDate: Date?,
        val completedDate: Date?,
        val description: String,
        val attachments: List<String>      // 附件路径列表
    )

    /**
     * 交付物类型
     */
    enum class DeliverableType(val displayName: String) {
        DOCUMENT("文档"),
        DESIGN_FILE("设计文件"),
        TEST_REPORT("测试报告"),
        CERTIFICATION("认证证书"),
        PROTOTYPE("原型"),
        BILL_OF_MATERIALS("物料清单"),
        TOOLING("工装夹具"),
        SPECIFICATION("规格书")
    }

    /**
     * 交付物状态
     */
    enum class DeliverableStatus(val displayName: String) {
        NOT_STARTED("未开始"),
        IN_PROGRESS("进行中"),
        REVIEW("审核中"),
        APPROVED("已批准"),
        REJECTED("已拒绝"),
        COMPLETED("已完成")
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
     * 流程状态
     */
    enum class ProcessStatus(val displayName: String) {
        PLANNING("规划中"),
        ACTIVE("进行中"),
        ON_HOLD("暂停"),
        COMPLETED("已完成"),
        CANCELLED("已取消")
    }

    /**
     * GPS输入到NPD流程映射
     * 
     * @param childData 儿童数据
     * @param seatType 座椅类型
     * @param marketTarget 目标市场
     * @return NPD流程初始化状态
     */
    suspend fun initializeNPDFromGPS(
        childData: com.childproduct.designassistant.data.ChildAnthropometricData,
        seatType: GPSAnthroTool.SeatType,
        marketTarget: String = "全球"
    ): NPDProcessStatus = withContext(Dispatchers.Default) {
        val processId = "NPD-${SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())}"
        val productName = "${seatType.displayName} - ${marketTarget} - ${childData.region.code}"

        // 初始化所有阶段
        val phases = initializePhases(childData, seatType, marketTarget)

        // 初始化交付物
        val deliverables = initializeDeliverables(processId, childData, seatType)

        NPDProcessStatus(
            processId = processId,
            productName = productName,
            currentPhase = 0,
            phases = phases,
            progress = 0.0,
            startDate = Date(),
            estimatedCompletionDate = calculateEstimatedCompletionDate(),
            deliverables = deliverables,
            status = ProcessStatus.PLANNING,
            notes = listOf(
                "基于GPS Anthro Tool分析结果初始化",
                "目标市场：$marketTarget",
                "目标年龄段：${childData.ageMonths}个月（${childData.getAgeGroupLabel()}）"
            )
        )
    }

    /**
     * 初始化阶段
     */
    private fun initializePhases(
        childData: com.childproduct.designassistant.data.ChildAnthropometricData,
        seatType: GPSAnthroTool.SeatType,
        marketTarget: String
    ): List<PhaseStatus> {
        return PHASES.mapIndexed { index, phaseName ->
            val blockers = mutableListOf<String>()
            val estimatedDays = when (index) {
                0 -> 7  // 需求分析
                1 -> 14 // 市场调研
                2 -> 21 // 概念设计
                3 -> 28 // 详细设计
                4 -> 35 // 原型开发
                5 -> 21 // 测试验证
                6 -> 42 // 合规认证
                7 -> 14 // 量产准备
                else -> 14
            }

            // 添加阶段特定的注意事项
            when (index) {
                2 -> blockers.add("需基于R129r4e标准进行设计")
                4 -> blockers.add("需制作符合${childData.region.code}标准的原型")
                5 -> blockers.add("需进行R129 Annex 18规定的测试")
                6 -> blockers.add("需获取${marketTarget}市场认证")
                else -> {}
            }

            PhaseStatus(
                phaseName = phaseName,
                phaseIndex = index,
                status = if (index == 0) PhaseStatusType.IN_PROGRESS else PhaseStatusType.NOT_STARTED,
                progress = if (index == 0) 10.0 else 0.0,
                startDate = if (index == 0) Date() else null,
                completionDate = null,
                deliverables = getPhaseDeliverables(index, childData, seatType),
                blockers = blockers
            )
        }
    }

    /**
     * 获取阶段交付物
     */
    private fun getPhaseDeliverables(
        phaseIndex: Int,
        childData: com.childproduct.designassistant.data.ChildAnthropometricData,
        seatType: GPSAnthroTool.SeatType
    ): List<Deliverable> {
        return when (phaseIndex) {
            0 -> listOf(  // 需求分析
                Deliverable(
                    id = "REQ-001",
                    name = "产品需求文档（PRD）",
                    type = DeliverableType.DOCUMENT,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.HIGH,
                    assignedTo = "产品经理",
                    dueDate = null,
                    completedDate = null,
                    description = "基于GPS分析结果的产品需求定义",
                    attachments = emptyList()
                )
            )
            1 -> listOf(  // 市场调研
                Deliverable(
                    id = "MKT-001",
                    name = "市场调研报告",
                    type = DeliverableType.DOCUMENT,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.HIGH,
                    assignedTo = "市场分析师",
                    dueDate = null,
                    completedDate = null,
                    description = "目标市场竞品分析",
                    attachments = emptyList()
                )
            )
            2 -> listOf(  // 概念设计
                Deliverable(
                    id = "DES-001",
                    name = "概念设计方案",
                    type = DeliverableType.DESIGN_FILE,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.CRITICAL,
                    assignedTo = "设计师",
                    dueDate = null,
                    completedDate = null,
                    description = "基于儿童人体测量的概念设计",
                    attachments = emptyList()
                ),
                Deliverable(
                    id = "DES-002",
                    name = "R129r4e合规性评估",
                    type = DeliverableType.DOCUMENT,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.CRITICAL,
                    assignedTo = "合规专家",
                    dueDate = null,
                    completedDate = null,
                    description = "ECE R129 Rev.4标准合规性分析",
                    attachments = emptyList()
                )
            )
            3 -> listOf(  // 详细设计
                Deliverable(
                    id = "DES-003",
                    name = "详细设计图纸",
                    type = DeliverableType.DESIGN_FILE,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.CRITICAL,
                    assignedTo = "设计工程师",
                    dueDate = null,
                    completedDate = null,
                    description = "3D模型和2D工程图",
                    attachments = emptyList()
                ),
                Deliverable(
                    id = "BOM-001",
                    name = "物料清单（BOM）",
                    type = DeliverableType.BILL_OF_MATERIALS,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.HIGH,
                    assignedTo = "采购工程师",
                    dueDate = null,
                    completedDate = null,
                    description = "所有零部件清单",
                    attachments = emptyList()
                )
            )
            4 -> listOf(  // 原型开发
                Deliverable(
                    id = "PROT-001",
                    name = "功能原型",
                    type = DeliverableType.PROTOTYPE,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.CRITICAL,
                    assignedTo = "原型工程师",
                    dueDate = null,
                    completedDate = null,
                    description = "功能验证原型",
                    attachments = emptyList()
                ),
                Deliverable(
                    id = "TOOL-001",
                    name = "工装夹具",
                    type = DeliverableType.TOOLING,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.HIGH,
                    assignedTo = "工艺工程师",
                    dueDate = null,
                    completedDate = null,
                    description = "生产工装设计",
                    attachments = emptyList()
                )
            )
            5 -> listOf(  // 测试验证
                Deliverable(
                    id = "TEST-001",
                    name = "R129动态测试报告",
                    type = DeliverableType.TEST_REPORT,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.CRITICAL,
                    assignedTo = "测试工程师",
                    dueDate = null,
                    completedDate = null,
                    description = "正面、后向、侧面撞击测试",
                    attachments = emptyList()
                ),
                Deliverable(
                    id = "TEST-002",
                    name = "静态测试报告",
                    type = DeliverableType.TEST_REPORT,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.HIGH,
                    assignedTo = "测试工程师",
                    dueDate = null,
                    completedDate = null,
                    description = "强度、耐久性测试",
                    attachments = emptyList()
                )
            )
            6 -> listOf(  // 合规认证
                Deliverable(
                    id = "CERT-001",
                    name = "ECE R129认证证书",
                    type = DeliverableType.CERTIFICATION,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.CRITICAL,
                    assignedTo = "合规专员",
                    dueDate = null,
                    completedDate = null,
                    description = "ECE R129 Rev.4认证",
                    attachments = emptyList()
                ),
                Deliverable(
                    id = "CERT-002",
                    name = "CFR认证证书",
                    type = DeliverableType.CERTIFICATION,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.HIGH,
                    assignedTo = "合规专员",
                    dueDate = null,
                    completedDate = null,
                    description = "美国联邦法规认证",
                    attachments = emptyList()
                )
            )
            7 -> listOf(  // 量产准备
                Deliverable(
                    id = "PROD-001",
                    name = "生产作业指导书",
                    type = DeliverableType.DOCUMENT,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.HIGH,
                    assignedTo = "工艺工程师",
                    dueDate = null,
                    completedDate = null,
                    description = "SOP和质量控制标准",
                    attachments = emptyList()
                ),
                Deliverable(
                    id = "PROD-002",
                    name = "产品规格书",
                    type = DeliverableType.SPECIFICATION,
                    status = DeliverableStatus.NOT_STARTED,
                    priority = Priority.HIGH,
                    assignedTo = "产品经理",
                    dueDate = null,
                    completedDate = null,
                    description = "最终产品规格",
                    attachments = emptyList()
                )
            )
            else -> emptyList()
        }
    }

    /**
     * 初始化交付物
     */
    private fun initializeDeliverables(
        processId: String,
        childData: com.childproduct.designassistant.data.ChildAnthropometricData,
        seatType: GPSAnthroTool.SeatType
    ): List<Deliverable> {
        val allDeliverables = mutableListOf<Deliverable>()

        // 添加GPS相关交付物
        allDeliverables.add(
            Deliverable(
                id = "GPS-001",
                name = "GPS Anthro分析报告",
                type = DeliverableType.DOCUMENT,
                status = DeliverableStatus.COMPLETED,
                priority = Priority.HIGH,
                assignedTo = "GPS工具",
                dueDate = null,
                completedDate = Date(),
                description = "基于儿童人体测量数据的分析结果",
                attachments = listOf("gps_analysis_report.pdf")
            )
        )

        return allDeliverables
    }

    /**
     * 计算预计完成日期
     */
    private fun calculateEstimatedCompletionDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 182)  // 约6个月
        return calendar.time
    }

    /**
     * 更新阶段进度
     * 
     * @param processStatus 当前流程状态
     * @param phaseIndex 阶段索引
     * @param progress 进度（0-100%）
     * @return 更新后的流程状态
     */
    fun updatePhaseProgress(
        processStatus: NPDProcessStatus,
        phaseIndex: Int,
        progress: Double
    ): NPDProcessStatus {
        val updatedPhases = processStatus.phases.mapIndexed { index, phase ->
            if (index == phaseIndex) {
                phase.copy(
                    progress = progress,
                    status = when {
                        progress >= 100.0 -> PhaseStatusType.COMPLETED
                        progress > 0.0 -> PhaseStatusType.IN_PROGRESS
                        else -> PhaseStatusType.NOT_STARTED
                    },
                    completionDate = if (progress >= 100.0) Date() else null
                )
            } else {
                phase
            }
        }

        // 更新总进度
        val totalProgress = updatedPhases.map { it.progress }.average()

        // 更新当前阶段
        val newCurrentPhase = updatedPhases.indexOfFirst {
            it.status == PhaseStatusType.IN_PROGRESS || it.status == PhaseStatusType.NOT_STARTED
        }

        // 确定流程状态
        val processStatusType = when {
            totalProgress >= 100.0 -> ProcessStatus.COMPLETED
            newCurrentPhase >= updatedPhases.size -> ProcessStatus.COMPLETED
            updatedPhases.any { it.status == PhaseStatusType.BLOCKED } -> ProcessStatus.ON_HOLD
            updatedPhases.any { it.status == PhaseStatusType.IN_PROGRESS } -> ProcessStatus.ACTIVE
            else -> ProcessStatus.PLANNING
        }

        return processStatus.copy(
            phases = updatedPhases,
            progress = totalProgress,
            currentPhase = newCurrentPhase,
            status = processStatusType
        )
    }

    /**
     * 添加交付物
     * 
     * @param processStatus 当前流程状态
     * @param deliverable 新交付物
     * @return 更新后的流程状态
     */
    fun addDeliverable(
        processStatus: NPDProcessStatus,
        deliverable: Deliverable
    ): NPDProcessStatus {
        val updatedDeliverables = processStatus.deliverables + deliverable
        return processStatus.copy(deliverables = updatedDeliverables)
    }

    /**
     * 更新交付物状态
     * 
     * @param processStatus 当前流程状态
     * @param deliverableId 交付物ID
     * @param status 新状态
     * @return 更新后的流程状态
     */
    fun updateDeliverableStatus(
        processStatus: NPDProcessStatus,
        deliverableId: String,
        status: DeliverableStatus
    ): NPDProcessStatus {
        val updatedDeliverables = processStatus.deliverables.map { deliverable ->
            if (deliverable.id == deliverableId) {
                deliverable.copy(
                    status = status,
                    completedDate = if (status == DeliverableStatus.COMPLETED) Date() else null
                )
            } else {
                deliverable
            }
        }
        return processStatus.copy(deliverables = updatedDeliverables)
    }

    /**
     * 生成流程报告
     * 
     * @param processStatus 流程状态
     * @return 报告文本
     */
    fun generateProcessReport(processStatus: NPDProcessStatus): String {
        val report = StringBuilder()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        report.appendLine("NPD流程报告")
        report.appendLine("=" .repeat(50))
        report.appendLine()
        report.appendLine("流程ID：${processStatus.processId}")
        report.appendLine("产品名称：${processStatus.productName}")
        report.appendLine("状态：${processStatus.status.displayName}")
        report.appendLine("总进度：${String.format("%.1f", processStatus.progress)}%")
        report.appendLine("开始日期：${dateFormat.format(processStatus.startDate)}")
        report.appendLine("预计完成：${processStatus.estimatedCompletionDate?.let { dateFormat.format(it) } ?: "未确定"}")
        report.appendLine()

        report.appendLine("阶段详情")
        report.appendLine("-" .repeat(50))
        processStatus.phases.forEach { phase ->
            val statusIcon = when (phase.status) {
                PhaseStatusType.NOT_STARTED -> "○"
                PhaseStatusType.IN_PROGRESS -> "◐"
                PhaseStatusType.COMPLETED -> "●"
                PhaseStatusType.BLOCKED -> "⊘"
                PhaseStatusType.SKIPPED -> "⊝"
            }
            report.appendLine("$statusIcon ${phase.phaseName} - ${String.format("%.1f", phase.progress)}%")
            if (phase.blockers.isNotEmpty()) {
                report.appendLine("  阻塞：${phase.blockers.joinToString(", ")}")
            }
        }
        report.appendLine()

        report.appendLine("交付物统计")
        report.appendLine("-" .repeat(50))
        val totalDeliverables = processStatus.deliverables.size
        val completedDeliverables = processStatus.deliverables.count { it.status == DeliverableStatus.COMPLETED }
        val inProgressDeliverables = processStatus.deliverables.count { it.status == DeliverableStatus.IN_PROGRESS }
        report.appendLine("总数：$totalDeliverables")
        report.appendLine("已完成：$completedDeliverables")
        report.appendLine("进行中：$inProgressDeliverables")
        report.appendLine("未开始：${totalDeliverables - completedDeliverables - inProgressDeliverables}")

        return report.toString()
    }
}

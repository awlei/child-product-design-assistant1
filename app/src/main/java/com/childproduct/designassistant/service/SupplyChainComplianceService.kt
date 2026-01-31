package com.childproduct.designassistant.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * 供应链与量产合规控制服务
 * 
 * 管理供应链溯源性、量产一致性控制，确保产品持续符合FMVSS标准
 */
class SupplyChainComplianceService {

    /**
     * 供应商资质
     */
    data class SupplierQualification(
        val supplierId: String,
        val name: String,
        val type: SupplierType,
        val location: String,
        val certifications: List<Certification>,
        val complianceScore: Double,  // 0-100
        val lastAuditDate: Date,
        val nextAuditDate: Date,
        val status: SupplierStatus
    )

    /**
     * 供应商类型
     */
    enum class SupplierType(val displayName: String) {
        WEBBING("织带供应商"),
        BUCKLE("卡扣供应商"),
        METAL_HARDWARE("金属件供应商"),
        PLASTIC("塑料件供应商"),
        FOAM("泡沫供应商"),
        TEXTILE("纺织品供应商"),
        ELECTRONICS("电子元件供应商")
    }

    /**
     * 认证
     */
    data class Certification(
        val certificationId: String,
        val name: String,
        val issuingBody: String,
        val issueDate: Date,
        val expiryDate: Date,
        val status: CertificationStatus
    )

    /**
     * 认证状态
     */
    enum class CertificationStatus(val displayName: String) {
        ACTIVE("有效"),
        EXPIRED("已过期"),
        SUSPENDED("暂停"),
        UNDER_REVIEW("审核中")
    }

    /**
     * 供应商状态
     */
    enum class SupplierStatus(val displayName: String) {
        APPROVED("已批准"),
        CONDITIONAL("有条件批准"),
        UNDER_REVIEW("审核中"),
        SUSPENDED("暂停"),
        TERMINATED("终止")
    }

    /**
     * 材料批次追溯
     */
    data class MaterialBatch(
        val batchId: String,
        val materialName: String,
        val materialType: MaterialType,
        val supplierId: String,
        val supplierBatchCode: String,
        val productionDate: Date,
        val expiryDate: Date?,
        val specifications: MaterialSpecification,
        val testResults: List<TestResult>,
        val qualityRating: QualityRating
    )

    /**
     * 材料类型
     */
    enum class MaterialType(val displayName: String) {
        WEBBING("织带"),
        PLASTIC("塑料"),
        METAL("金属"),
        FOAM("泡沫"),
        TEXTILE("纺织品"),
        ADHESIVE("胶粘剂"),
        ELASTIC("弹性材料")
    }

    /**
     * 材料规格
     */
    data class MaterialSpecification(
        val specId: String,
        val version: String,
        val parameters: Map<String, String>,
        val tolerance: Map<String, String>
    )

    /**
     * 测试结果
     */
    data class TestResult(
        val testId: String,
        val testName: String,
        val testDate: Date,
        val laboratory: String,
        val parameters: Map<String, Double>,
        val passed: Boolean,
        val certificateNumber: String?
    )

    /**
     * 质量评级
     */
    enum class QualityRating(val displayName: String, val score: Int) {
        EXCELLENT("优秀", 5),
        GOOD("良好", 4),
        ACCEPTABLE("可接受", 3),
        MARGINAL("边缘", 2),
        POOR("差", 1)
    }

    /**
     * 组件批次追溯
     */
    data class ComponentBatch(
        val componentId: String,
        val componentName: String,
        val componentType: ComponentType,
        val supplierId: String,
        val materialBatchIds: List<String>,
        val productionDate: Date,
        val specifications: ComponentSpecification,
        val testResults: List<TestResult>,
        val qualityRating: QualityRating
    )

    /**
     * 组件类型
     */
    enum class ComponentType(val displayName: String) {
        BUCKLE("卡扣"),
        ADJUSTER("调节器"),
        LATCH_CONNECTOR("LATCH连接器"),
        TETHER_HOOK("Tether钩"),
        METAL_FRAME("金属框架"),
        PLASTIC_SHELL("塑料外壳"),
        FOAM_PADDING("泡沫垫"),
        HEADREST("头枕"),
        HARNESS("安全带")
    }

    /**
     * 组件规格
     */
    data class ComponentSpecification(
        val specId: String,
        val version: String,
        val criticalDimensions: Map<String, Dimension>,
        val performanceRequirements: List<PerformanceRequirement>
    )

    /**
     * 尺寸
     */
    data class Dimension(
        val name: String,
        val nominalValue: Double,
        val unit: String,
        val tolerance: String
    )

    /**
     * 性能要求
     */
    data class PerformanceRequirement(
        val requirementId: String,
        val name: String,
        val parameter: String,
        val minValue: Double?,
        val maxValue: Double?,
        val unit: String,
        val testStandard: String
    )

    /**
     * 产品批次追溯
     */
    data class ProductBatch(
        val batchId: String,
        val productName: String,
        val productionDate: Date,
        val productionLocation: String,
        val quantity: Int,
        val componentBatches: Map<String, String>,
        val serialNumbers: List<String>,
        val inspectionResults: List<InspectionResult>,
        val batchStatus: BatchStatus
    )

    /**
     * 检验结果
     */
    data class InspectionResult(
        val inspectionId: String,
        val inspectionType: InspectionType,
        val inspectionDate: Date,
        val inspector: String,
        val results: Map<String, InspectionResultItem>,
        val overallPassed: Boolean,
        val notes: String
    )

    /**
     * 检验类型
     */
    enum class InspectionType(val displayName: String) {
        INCOMING("进货检验"),
        IN_PROCESS("过程检验"),
        FINAL("最终检验"),
        QUALITY_AUDIT("质量审核")
    }

    /**
     * 检验结果项
     */
    data class InspectionResultItem(
        val itemName: String,
        val measuredValue: Double,
        val unit: String,
        val tolerance: String,
        val passed: Boolean,
        val deviation: Double
    )

    /**
     * 批次状态
     */
    enum class BatchStatus(val displayName: String) {
        IN_PRODUCTION("生产中"),
        QC_PENDING("待检验"),
        QC_PASSED("检验合格"),
        QC_FAILED("检验不合格"),
        QUARANTINED("隔离"),
        RELEASED("放行"),
        SHIPPED("已发货")
    }

    /**
     * 量产一致性控制
     */
    data class ProductionConsistencyControl(
        val controlId: String,
        val productName: String,
        val testSampleRef: TestSampleReference,
        val controlPlan: ControlPlan,
        val deviationTolerance: Map<String, Double>,  // 关键参数允许偏差百分比
        val samplingPlan: SamplingPlan,
        val records: List<ConsistencyRecord>
    )

    /**
     * 测试样品参考
     */
    data class TestSampleReference(
        val sampleId: String,
        val serialNumber: String,
        val certificationDate: Date,
        val specifications: ProductSpecification,
        val testReports: List<String>
    )

    /**
     * 产品规格
     */
    data class ProductSpecification(
        val specId: String,
        val version: String,
        val keyDimensions: Map<String, Dimension>,
        val keyMaterials: Map<String, MaterialReference>,
        val performanceRequirements: List<PerformanceRequirement>
    )

    /**
     * 材料参考
     */
    data class MaterialReference(
        val materialId: String,
        val materialName: String,
        val materialType: MaterialType,
        val specification: String
    )

    /**
     * 控制计划
     */
    data class ControlPlan(
        val planId: String,
        val planVersion: String,
        val controlPoints: List<ControlPoint>
    )

    /**
     * 控制点
     */
    data class ControlPoint(
        val pointId: String,
        val name: String,
        val processStage: ProcessStage,
        val parameter: String,
        val targetValue: Double,
        val tolerance: String,
        val measurementMethod: String,
        val frequency: InspectionFrequency,
        val responsible: String
    )

    /**
     * 工艺阶段
     */
    enum class ProcessStage(val displayName: String) {
        RAW_MATERIAL("原材料"),
        COMPONENT_ASSEMBLY("组件组装"),
        FINAL_ASSEMBLY("总装"),
        FINISHING("精加工"),
        PACKAGING("包装")
    }

    /**
     * 检验频率
     */
    enum class InspectionFrequency(val displayName: String) {
        CONTINUOUS("连续"),
        EVERY_HOUR("每小时"),
        EVERY_SHIFT("每班"),
        DAILY("每日"),
        WEEKLY("每周"),
        LOT_SAMPLING("批次抽样")
    }

    /**
     * 抽样计划
     */
    data class SamplingPlan(
        val planType: SamplingPlanType,
        val lotSize: Int,
        val sampleSize: Int,
        val acceptanceNumber: Int,
        val rejectionNumber: Int,
        val inspectionLevel: InspectionLevel
    )

    /**
     * 抽样计划类型
     */
    enum class SamplingPlanType(val displayName: String) {
        AQL("AQL抽样"),
        FIXED_PERCENT("固定百分比"),
        FIXED_QUANTITY("固定数量"),
        ZERO_DEFECT("零缺陷")
    }

    /**
     * 检验水平
     */
    enum class InspectionLevel(val displayName: String) {
        NORMAL("正常"),
        TIGHTENED("加严"),
        REDUCED("放宽")
    }

    /**
     * 一致性记录
     */
    data class ConsistencyRecord(
        val recordId: String,
        val recordDate: Date,
        val batchId: String,
        val measurements: Map<String, Double>,
        val deviations: Map<String, Double>,
        val passed: Boolean,
        val deviationsFound: List<String>,
        val correctiveActions: List<CorrectiveAction>
    )

    /**
     * 纠正措施
     */
    data class CorrectiveAction(
        val actionId: String,
        val description: String,
        val responsible: String,
        val targetDate: Date,
        val status: CorrectiveActionStatus
    )

    /**
     * 纠正措施状态
     */
    enum class CorrectiveActionStatus(val displayName: String) {
        OPEN("打开"),
        IN_PROGRESS("进行中"),
        COMPLETED("已完成"),
        VERIFIED("已验证"),
        CLOSED("已关闭")
    }

    /**
     * 供应商资质要求
     */
    private val supplierRequirements = mapOf(
        SupplierType.WEBBING to listOf(
            "ISO 9001质量管理体系认证",
            "FMVSS 213织带测试报告（断裂强度≥13,000 N）",
            "耐光色牢度测试报告",
            "阻燃测试报告（符合FMVSS 302）"
        ),
        SupplierType.BUCKLE to listOf(
            "ISO 9001质量管理体系认证",
            "FMVSS 213卡扣完整性测试报告",
            "腐蚀测试报告（盐雾试验）",
            "释放力测试报告（40-90 N）"
        ),
        SupplierType.METAL_HARDWARE to listOf(
            "ISO 9001质量管理体系认证",
            "材料测试报告（符合标准）",
            "抗拉强度测试报告",
            "腐蚀测试报告（盐雾试验）"
        ),
        SupplierType.PLASTIC to listOf(
            "ISO 9001质量管理体系认证",
            "材料测试报告（符合标准）",
            "阻燃测试报告（符合FMVSS 302）",
            "老化测试报告（UV/高温）"
        ),
        SupplierType.FOAM to listOf(
            "ISO 9001质量管理体系认证",
            "材料测试报告（符合标准）",
            "阻燃测试报告（符合FMVSS 302）",
            "压缩永久变形测试报告"
        ),
        SupplierType.TEXTILE to listOf(
            "ISO 9001质量管理体系认证",
            "材料测试报告（符合标准）",
            "耐洗色牢度测试报告",
            "阻燃测试报告（符合FMVSS 302）"
        ),
        SupplierType.ELECTRONICS to listOf(
            "ISO 9001质量管理体系认证",
            "功能安全认证（如适用）",
            "EMC测试报告",
            "环境适应性测试报告"
        )
    )

    /**
     * 关键参数偏差容忍度（美标要求）
     */
    private val criticalParameterTolerances = mapOf(
        "backrest_height" to 3.0,  // 靠背高度，±3%
        "webbing_width" to 3.0,     // 织带宽度，±3%
        "protrusion_height" to 3.0,  // 突起高度，±3%
        "buckle_release_force" to 10.0,  // 卡扣释放力，±10%
        "latch_release_force" to 10.0,   // LATCH释放力，±10%
        "adjustment_button_force" to 10.0,  // 调节按钮操作力，±10%
        "harness_length" to 3.0,       // 安全带长度，±3%
        "tether_length" to 3.0         // Tether长度，±3%
    )

    /**
     * 初始化量产一致性控制
     */
    suspend fun initializeProductionConsistencyControl(
        productName: String,
        testSampleRef: TestSampleReference
    ): ProductionConsistencyControl = withContext(Dispatchers.Default) {
        val controlId = "PCC-${SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())}"
        
        ProductionConsistencyControl(
            controlId = controlId,
            productName = productName,
            testSampleRef = testSampleRef,
            controlPlan = createDefaultControlPlan(controlId),
            deviationTolerance = criticalParameterTolerances,
            samplingPlan = createDefaultSamplingPlan(),
            records = emptyList()
        )
    }

    /**
     * 创建默认控制计划
     */
    private fun createDefaultControlPlan(controlId: String): ControlPlan {
        return ControlPlan(
            planId = "CP-$controlId",
            planVersion = "1.0",
            controlPoints = listOf(
                ControlPoint(
                    pointId = "CP-001",
                    name = "织带宽度检查",
                    processStage = ProcessStage.RAW_MATERIAL,
                    parameter = "webbing_width",
                    targetValue = 50.0,
                    tolerance = "±1.5 mm (±3%)",
                    measurementMethod = "卡尺测量",
                    frequency = InspectionFrequency.LOT_SAMPLING,
                    responsible = "质量检验员"
                ),
                ControlPoint(
                    pointId = "CP-002",
                    name = "卡扣释放力检查",
                    processStage = ProcessStage.COMPONENT_ASSEMBLY,
                    parameter = "buckle_release_force",
                    targetValue = 65.0,
                    tolerance = "±6.5 N (±10%)",
                    measurementMethod = "力计测量",
                    frequency = InspectionFrequency.LOT_SAMPLING,
                    responsible = "质量检验员"
                ),
                ControlPoint(
                    pointId = "CP-003",
                    name = "靠背高度检查",
                    processStage = ProcessStage.FINAL_ASSEMBLY,
                    parameter = "backrest_height",
                    targetValue = 600.0,
                    tolerance = "±18 mm (±3%)",
                    measurementMethod = "高度尺测量",
                    frequency = InspectionFrequency.LOT_SAMPLING,
                    responsible = "质量检验员"
                ),
                ControlPoint(
                    pointId = "CP-004",
                    name = "LATCH释放力检查",
                    processStage = ProcessStage.COMPONENT_ASSEMBLY,
                    parameter = "latch_release_force",
                    targetValue = 100.0,
                    tolerance = "±10 N (±10%)",
                    measurementMethod = "力计测量",
                    frequency = InspectionFrequency.LOT_SAMPLING,
                    responsible = "质量检验员"
                ),
                ControlPoint(
                    pointId = "CP-005",
                    name = "安全带长度检查",
                    processStage = ProcessStage.FINAL_ASSEMBLY,
                    parameter = "harness_length",
                    targetValue = 800.0,
                    tolerance = "±24 mm (±3%)",
                    measurementMethod = "卷尺测量",
                    frequency = InspectionFrequency.LOT_SAMPLING,
                    responsible = "质量检验员"
                ),
                ControlPoint(
                    pointId = "CP-006",
                    name = "调节按钮操作力检查",
                    processStage = ProcessStage.FINAL_ASSEMBLY,
                    parameter = "adjustment_button_force",
                    targetValue = 15.0,
                    tolerance = "±1.5 N (±10%)",
                    measurementMethod = "力计测量",
                    frequency = InspectionFrequency.LOT_SAMPLING,
                    responsible = "质量检验员"
                ),
                ControlPoint(
                    pointId = "CP-007",
                    name = "突起高度检查",
                    processStage = ProcessStage.FINISHING,
                    parameter = "protrusion_height",
                    targetValue = 50.0,
                    tolerance = "±1.5 mm (±3%)",
                    measurementMethod = "高度尺测量",
                    frequency = InspectionFrequency.LOT_SAMPLING,
                    responsible = "质量检验员"
                )
            )
        )
    }

    /**
     * 创建默认抽样计划
     */
    private fun createDefaultSamplingPlan(): SamplingPlan {
        return SamplingPlan(
            planType = SamplingPlanType.AQL,
            lotSize = 1000,
            sampleSize = 50,
            acceptanceNumber = 3,
            rejectionNumber = 4,
            inspectionLevel = InspectionLevel.NORMAL
        )
    }

    /**
     * 验证组件供应商资质
     */
    fun verifySupplierQualification(
        supplierType: SupplierType,
        certifications: List<Certification>
    ): Pair<Boolean, List<String>> {
        val requiredCerts = supplierRequirements[supplierType] ?: emptyList()
        val issues = mutableListOf<String>()
        var isQualified = true

        requiredCerts.forEach { requiredCert ->
            val hasCert = certifications.any { it.name.contains(requiredCert, ignoreCase = true) }
            if (!hasCert) {
                issues.add("缺少认证：$requiredCert")
                isQualified = false
            }
        }

        // 检查认证是否过期
        certifications.forEach { cert ->
            if (cert.expiryDate.before(Date())) {
                issues.add("认证已过期：${cert.name}（过期日期：${SimpleDateFormat("yyyy-MM-dd").format(cert.expiryDate)}）")
                isQualified = false
            }
        }

        return Pair(isQualified, issues)
    }

    /**
     * 创建材料批次记录
     */
    fun createMaterialBatchRecord(
        materialName: String,
        materialType: MaterialType,
        supplierId: String,
        specifications: MaterialSpecification
    ): MaterialBatch {
        return MaterialBatch(
            batchId = "MAT-${SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())}",
            materialName = materialName,
            materialType = materialType,
            supplierId = supplierId,
            supplierBatchCode = "",
            productionDate = Date(),
            expiryDate = null,
            specifications = specifications,
            testResults = emptyList(),
            qualityRating = QualityRating.ACCEPTABLE
        )
    }

    /**
     * 创建产品批次记录
     */
    fun createProductBatchRecord(
        productName: String,
        productionLocation: String,
        quantity: Int,
        componentBatches: Map<String, String>
    ): ProductBatch {
        return ProductBatch(
            batchId = "PROD-${SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())}",
            productName = productName,
            productionDate = Date(),
            productionLocation = productionLocation,
            quantity = quantity,
            componentBatches = componentBatches,
            serialNumbers = generateSerialNumbers(quantity),
            inspectionResults = emptyList(),
            batchStatus = BatchStatus.IN_PRODUCTION
        )
    }

    /**
     * 生成序列号
     */
    private fun generateSerialNumbers(quantity: Int): List<String> {
        val serialNumbers = mutableListOf<String>()
        val prefix = SimpleDateFormat("yyyyMMdd").format(Date())
        repeat(quantity) { index ->
            serialNumbers.add("SN-$prefix-${String.format("%05d", index + 1)}")
        }
        return serialNumbers
    }

    /**
     * 验证量产一致性
     */
    suspend fun verifyProductionConsistency(
        control: ProductionConsistencyControl,
        batch: ProductBatch
    ): Pair<Boolean, ConsistencyRecord> = withContext(Dispatchers.Default) {
        val deviations = mutableMapOf<String, Double>()
        val deviationsFound = mutableListOf<String>()
        var passed = true

        // 模拟检验结果（实际应用中从检验记录获取）
        val measurements = mapOf(
            "webbing_width" to 51.0,
            "buckle_release_force" to 68.0,
            "backrest_height" to 605.0,
            "latch_release_force" to 105.0,
            "harness_length" to 805.0,
            "adjustment_button_force" to 16.0,
            "protrusion_height" to 51.0
        )

        // 计算偏差
        measurements.forEach { (parameter, measuredValue) ->
            val targetValue = control.controlPlan.controlPoints
                .find { it.parameter == parameter }?.targetValue ?: 0.0
            val tolerance = control.deviationTolerance[parameter] ?: 0.0
            val deviation = ((measuredValue - targetValue) / targetValue) * 100
            
            if (Math.abs(deviation) > tolerance) {
                deviations[parameter] = deviation
                deviationsFound.add("$parameter 偏差 ${String.format("%.2f", deviation)}% (允许±$tolerance%)")
                passed = false
            }
        }

        val record = ConsistencyRecord(
            recordId = "CR-${SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())}",
            recordDate = Date(),
            batchId = batch.batchId,
            measurements = measurements,
            deviations = deviations,
            passed = passed,
            deviationsFound = deviationsFound,
            correctiveActions = if (!passed) {
                listOf(
                    CorrectiveAction(
                        actionId = "CA-001",
                        description = "调查偏差原因并调整生产工艺",
                        responsible = "质量工程师",
                        targetDate = Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L),
                        status = CorrectiveActionStatus.OPEN
                    )
                )
            } else {
                emptyList()
            }
        )

        Pair(passed, record)
    }

    /**
     * 生成供应链合规报告
     */
    suspend fun generateSupplyChainComplianceReport(
        suppliers: List<SupplierQualification>,
        materialBatches: List<MaterialBatch>,
        productBatches: List<ProductBatch>,
        consistencyControl: ProductionConsistencyControl
    ): String = withContext(Dispatchers.Default) {
        val report = StringBuilder()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        
        report.appendLine("=" .repeat(70))
        report.appendLine("供应链与量产合规报告")
        report.appendLine("=" .repeat(70))
        report.appendLine()
        
        // 供应商资质
        report.appendLine("-" .repeat(70))
        report.appendLine("供应商资质状态")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        suppliers.forEach { supplier ->
            report.appendLine("供应商ID：${supplier.supplierId}")
            report.appendLine("名称：${supplier.name}")
            report.appendLine("类型：${supplier.type.displayName}")
            report.appendLine("位置：${supplier.location}")
            report.appendLine("状态：${supplier.status.displayName}")
            report.appendLine("合规评分：${String.format("%.1f", supplier.complianceScore)}/100")
            report.appendLine("上次审核：${dateFormat.format(supplier.lastAuditDate)}")
            report.appendLine("下次审核：${dateFormat.format(supplier.nextAuditDate)}")
            report.appendLine()
            report.appendLine("认证：")
            supplier.certifications.forEach { cert ->
                report.appendLine("  - ${cert.name} (${cert.status.displayName}, 有效期至${dateFormat.format(cert.expiryDate)})")
            }
            report.appendLine()
        }
        
        // 材料批次
        report.appendLine("-" .repeat(70))
        report.appendLine("材料批次追溯")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        materialBatches.take(10).forEach { batch ->
            report.appendLine("批次ID：${batch.batchId}")
            report.appendLine("材料名称：${batch.materialName}")
            report.appendLine("材料类型：${batch.materialType.displayName}")
            report.appendLine("供应商：${batch.supplierId}")
            report.appendLine("生产日期：${dateFormat.format(batch.productionDate)}")
            report.appendLine("质量评级：${batch.qualityRating.displayName}")
            report.appendLine("测试结果：${batch.testResults.size}项")
            report.appendLine()
        }
        
        // 量产一致性
        report.appendLine("-" .repeat(70))
        report.appendLine("量产一致性控制")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        report.appendLine("控制ID：${consistencyControl.controlId}")
        report.appendLine("产品名称：${consistencyControl.productName}")
        report.appendLine("测试样品参考：${consistencyControl.testSampleRef.sampleId}")
        report.appendLine()
        
        report.appendLine("关键参数偏差容忍度：")
        consistencyControl.deviationTolerance.forEach { (parameter, tolerance) ->
            report.appendLine("  - $parameter: ±$tolerance%")
        }
        report.appendLine()
        
        report.appendLine("抽样计划：")
        report.appendLine("  - 计划类型：${consistencyControl.samplingPlan.planType.displayName}")
        report.appendLine("  - 批量大小：${consistencyControl.samplingPlan.lotSize}")
        report.appendLine("  - 样本大小：${consistencyControl.samplingPlan.sampleSize}")
        report.appendLine("  - 接收数：${consistencyControl.samplingPlan.acceptanceNumber}")
        report.appendLine("  - 拒收数：${consistencyControl.samplingPlan.rejectionNumber}")
        report.appendLine()
        
        // 产品批次
        report.appendLine("-" .repeat(70))
        report.appendLine("产品批次状态")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        
        productBatches.take(10).forEach { batch ->
            report.appendLine("批次ID：${batch.batchId}")
            report.appendLine("产品名称：${batch.productName}")
            report.appendLine("生产日期：${dateFormat.format(batch.productionDate)}")
            report.appendLine("生产位置：${batch.productionLocation}")
            report.appendLine("数量：${batch.quantity}")
            report.appendLine("状态：${batch.batchStatus.displayName}")
            report.appendLine("序列号数量：${batch.serialNumbers.size}")
            report.appendLine()
        }
        
        // 合规建议
        report.appendLine("-" .repeat(70))
        report.appendLine("合规建议")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        report.appendLine("1. 所有关键组件供应商必须提供符合美标的测试报告")
        report.appendLine("2. 建立批次追溯系统，每个产品标注唯一序列号")
        report.appendLine("3. 量产产品关键尺寸偏差需控制在±3%以内")
        report.appendLine("4. 每批次量产产品需抽取1-3件进行关键性能抽检")
        report.appendLine("5. 定期审核供应商资质，确保认证持续有效")
        report.appendLine("6. 保留测试样品的技术状态冻结，避免后续变更")
        report.appendLine("7. 建立纠正措施机制，及时处理偏差和不合格")
        
        report.toString()
    }

    /**
     * 生成供应商资质要求指南
     */
    fun generateSupplierRequirementGuide(): String {
        val guide = StringBuilder()
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("供应商资质要求指南")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        
        supplierRequirements.forEach { (supplierType, requirements) ->
            guide.appendLine("【${supplierType.displayName}】")
            guide.appendLine("必需认证/测试报告：")
            requirements.forEachIndexed { index, req ->
                guide.appendLine("${index + 1}. $req")
            }
            guide.appendLine()
        }
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("合规要求")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        guide.appendLine("1. 所有关键组件供应商需提供'符合美标'的测试报告")
        guide.appendLine("2. 供应商需持有ISO 9001质量管理体系认证")
        guide.appendLine("3. 认证必须持续有效，过期认证需及时更新")
        guide.appendLine("4. 定期审核供应商，确保持续符合资质要求")
        guide.appendLine("5. 建立供应商评分机制，监控质量表现")
        
        return guide.toString()
    }

    /**
     * 生成量产一致性控制指南
     */
    fun generateProductionConsistencyGuide(): String {
        val guide = StringBuilder()
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("量产一致性控制指南")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        
        guide.appendLine("【关键尺寸控制】")
        guide.appendLine("以下关键尺寸的量产偏差需控制在±3%以内：")
        guide.appendLine("1. 靠背高度（backrest_height）")
        guide.appendLine("2. 织带宽度（webbing_width）")
        guide.appendLine("3. 突起高度（protrusion_height）")
        guide.appendLine("4. 安全带长度（harness_length）")
        guide.appendLine("5. Tether长度（tether_length）")
        guide.appendLine()
        
        guide.appendLine("【性能参数控制】")
        guide.appendLine("以下性能参数的量产偏差需控制在±10%以内：")
        guide.appendLine("1. 卡扣释放力（buckle_release_force）：40-90 N")
        guide.appendLine("2. LATCH释放力（latch_release_force）：40-160 N")
        guide.appendLine("3. 调节按钮操作力（adjustment_button_force）：≥10 N")
        guide.appendLine()
        
        guide.appendLine("【抽样检验】")
        guide.appendLine("1. 每批次量产产品需抽取1-3件进行关键性能抽检")
        guide.appendLine("2. 抽样计划：AQL抽样，批量1000，样本50，接收数3，拒收数4")
        guide.appendLine("3. 检验水平：正常检验")
        guide.appendLine()
        
        guide.appendLine("【偏差处理】")
        guide.appendLine("1. 发现偏差立即隔离相关批次")
        guide.appendLine("2. 调查偏差原因（材料、工艺、设备）")
        guide.appendLine("3. 制定纠正措施并实施")
        guide.appendLine("4. 验证纠正措施有效性")
        guide.appendLine("5. 更新控制计划，预防再发生")
        guide.appendLine()
        
        guide.appendLine("【记录保持】")
        guide.appendLine("1. 保留所有检验记录至少5年")
        guide.appendLine("2. 保留测试样品的技术状态冻结")
        guide.appendLine("3. 保留供应商测试报告和认证证书")
        guide.appendLine("4. 保留批次追溯信息（序列号、材料批次、生产日期）")
        
        return guide.toString()
    }
}

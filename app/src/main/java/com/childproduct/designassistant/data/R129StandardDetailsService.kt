package com.childproduct.designassistant.data

import com.childproduct.designassistant.data.model.*

/**
 * R129r4e标准详细解析服务
 * 提供ECE R129标准的详细解析功能,包括假人规格、伤害判据、防旋转装置、材料要求等
 */
class R129StandardDetailsService {

    private val R129r4eStandardDatabase = R129r4eStandardDatabase()

    /**
     * 获取标准概述
     */
    fun getStandardOverview(): StandardOverview {
        return R129r4eStandardDatabase.getStandardOverview()
    }

    /**
     * 获取所有假人规格
     */
    fun getAllDummySpecs(): List<DummySpec> {
        return R129r4eStandardDatabase.getDummySpecsList()
    }

    /**
     * 根据假人类型获取详细规格
     */
    fun getDummySpecDetail(dummyType: String): DummySpec? {
        return R129r4eStandardDatabase.getDummySpec(dummyType)
    }

    /**
     * 根据身高范围获取适用假人
     */
    fun getApplicableDummiesByHeight(heightRange: String): List<DummySpec> {
        return R129r4eStandardDatabase.getApplicableDummies(heightRange)
    }

    /**
     * 获取假人伤害判据对比表
     */
    fun getInjuryCriteriaComparison(): Map<String, Map<String, Any>> {
        val comparison = mutableMapOf<String, Map<String, Any>>()

        R129r4eStandardDatabase.getDummySpecsList().forEach { dummy ->
            val criteriaMap: Map<String, Any> = mapOf(
                "dummyType" to dummy.dummyType,
                "headAcceleration3ms_low" to dummy.injuryCriteria.headAcceleration3ms.lowThreshold,
                "headAcceleration3ms_high" to dummy.injuryCriteria.headAcceleration3ms.highThreshold,
                "hpc_low" to dummy.injuryCriteria.hpc.lowThreshold,
                "hpc_high" to dummy.injuryCriteria.hpc.highThreshold,
                "chestAcceleration3ms" to dummy.injuryCriteria.chestAcceleration3ms,
                "abdominalPressure" to (dummy.injuryCriteria.abdominalPressure?.let {
                    "${it.q1_5Threshold}/${it.q3_q6Threshold}/${it.q10Threshold}"
                } ?: "N/A"),
                "neckTension" to (dummy.injuryCriteria.neckForces?.tensionLimit ?: 0.0),
                "neckCompression" to (dummy.injuryCriteria.neckForces?.compressionLimit ?: 0.0)
            )
            comparison[dummy.dummyType] = criteriaMap
        }

        return comparison
    }

    /**
     * 获取防旋转装置要求
     */
    fun getAntiRotationDeviceSpec(deviceType: AntiRotationDeviceType): AntiRotationDeviceSpec? {
        return R129r4eStandardDatabase.getAntiRotationDeviceSpec(deviceType)
    }

    /**
     * 获取所有防旋转装置类型及规格
     */
    fun getAllAntiRotationDeviceSpecs(): Map<AntiRotationDeviceType, AntiRotationDeviceSpec> {
        return R129r4eStandardDatabase.getAntiRotationDevicesMap()
    }

    /**
     * 获取碰撞测试曲线
     */
    fun getImpactTestCurve(testType: ImpactTestType): ImpactTestCurve? {
        return R129r4eStandardDatabase.getImpactTestCurve(testType)
    }

    /**
     * 获取所有碰撞测试曲线
     */
    fun getAllImpactTestCurves(): List<ImpactTestCurve> {
        return R129r4eStandardDatabase.getImpactTestCurvesList()
    }

    /**
     * 获取材料标准要求
     */
    fun getMaterialStandards(): List<MaterialStandardRequirement> {
        return R129r4eStandardDatabase.getMaterialStandardsList()
    }

    /**
     * 根据材料类型获取标准要求
     */
    fun getMaterialStandardByType(materialType: MaterialType): List<MaterialStandardRequirement> {
        return R129r4eStandardDatabase.getMaterialStandardsList().filter { it.materialType == materialType }
    }

    /**
     * 获取卡扣要求
     */
    fun getBuckleRequirement(): BuckleRequirement {
        return R129r4eStandardDatabase.getBuckleRequirement()
    }

    /**
     * 获取卷收器要求
     */
    fun getRetractorRequirement(retractorType: RetractorType): RetractorRequirement {
        return when (retractorType) {
            RetractorType.AUTO_LOCKING -> R129r4eStandardDatabase.getAutoLockingRetractor()
            RetractorType.EMERGENCY_LOCKING -> R129r4eStandardDatabase.getEmergencyLockingRetractor()
        }
    }

    /**
     * 获取认证申请材料清单
     */
    fun getApplicationDocuments(): ApplicationDocuments {
        return R129r4eStandardDatabase.getApplicationDocuments()
    }

    /**
     * 获取标识要求
     */
    fun getMarkingRequirements(): List<MarkingRequirement> {
        return R129r4eStandardDatabase.getMarkingRequirementsList()
    }

    /**
     * 获取生产一致性控制要求
     */
    fun getProductionConformity(): ProductionConformityControl {
        return R129r4eStandardDatabase.getProductionConformity()
    }

    /**
     * 获取用户说明书要求
     */
    fun getUserManualRequirements(): UserManualRequirements {
        return R129r4eStandardDatabase.getUserManualRequirements()
    }

    /**
     * 获取测试台车规格
     */
    fun getTestTrolleySpec(): TestTrolleySpec {
        return R129r4eStandardDatabase.getTestTrolleySpec()
    }

    /**
     * 获取假人安装垫片高度
     */
    fun getSpacerHeight(dummyType: String): Int? {
        return R129r4eStandardDatabase.getSpacerHeight(dummyType)
    }

    /**
     * 获取测试安装预紧力要求
     */
    fun getInstallationPreload(): Map<String, Int> {
        return R129r4eStandardDatabase.getInstallationPreloadMap()
    }

    /**
     * 获取外部尺寸ISO包络
     */
    fun getExternalEnvelopes(): Map<String, EnvelopeDimensions> {
        return R129r4eStandardDatabase.getExternalEnvelopesMap()
    }

    /**
     * 根据ECRS类型获取外部尺寸包络
     */
    fun getExternalEnvelopeByEcrsType(ecrsType: String): EnvelopeDimensions? {
        val envelopeKey = when (ecrsType) {
            "iSizeForwardFacing" -> "iSizeForwardFacing"
            "iSizeRearwardFacing" -> "iSizeRearwardFacing"
            "iSizeBooster" -> "iSizeBooster"
            else -> null
        }
        return envelopeKey?.let { R129r4eStandardDatabase.getExternalEnvelopesMap()[it] }
    }

    /**
     * 获取关键术语定义
     */
    fun getKeyTerms(): List<Pair<String, String>> {
        return R129r4eStandardDatabase.getKeyTermsList()
    }

    /**
     * 搜索术语定义
     */
    fun searchTerm(keyword: String): Pair<String, String>? {
        return R129r4eStandardDatabase.getKeyTermsList().find { it.first.contains(keyword, ignoreCase = true) }
    }

    /**
     * 获取ECRS分类信息
     */
    fun getEcrsClassifications(): List<ECRSClassification> {
        return R129r4eStandardDatabase.getECRSClassificationsList()
    }

    /**
     * 获取R129r4e关键阈值
     */
    fun getR129r4eThresholds(): R129r4eThresholds {
        return R129r4eStandardDatabase.getThresholds()
    }

    /**
     * 根据身高范围判断朝向要求
     */
    fun determineOrientationRequirement(heightRange: String): String {
        val (minHeight, maxHeight) = parseHeightRange(heightRange)
        val thresholds = R129r4eStandardDatabase.getThresholds()

        return when {
            maxHeight <= thresholds.maxRearwardHeight -> "强制后向(≤${thresholds.mandatoryRearwardMonths}个月)"
            minHeight >= thresholds.minForwardHeight -> "可前向安装"
            else -> "双向支持(后向前向可转换)"
        }
    }

    /**
     * 验证假人伤害判据合规性
     */
    fun validateInjuryCriteria(
        dummyType: String,
        headAcceleration3ms: Double,
        hpc: Int,
        chestAcceleration3ms: Double
    ): ComplianceResult {
        val dummy = R129r4eStandardDatabase.getDummySpec(dummyType) ?: return ComplianceResult(
            isCompliant = false,
            details = "未找到假人类型: $dummyType"
        )

        val violations = mutableListOf<String>()

        // 检查头部加速度
        val headThreshold = if (dummyType in listOf("Q0", "Q1", "Q1.5", "Q3")) {
            dummy.injuryCriteria.headAcceleration3ms.lowThreshold
        } else {
            dummy.injuryCriteria.headAcceleration3ms.highThreshold
        }
        if (headAcceleration3ms > headThreshold) {
            violations.add("头部3ms加速度超标: $headAcceleration3ms > $headThreshold g")
        }

        // 检查HPC
        val hpcThreshold = if (dummyType in listOf("Q0", "Q1", "Q1.5", "Q3")) {
            dummy.injuryCriteria.hpc.lowThreshold
        } else {
            dummy.injuryCriteria.hpc.highThreshold
        }
        if (hpc > hpcThreshold) {
            violations.add("HPC超标: $hpc > $hpcThreshold")
        }

        // 检查胸部加速度
        if (chestAcceleration3ms > dummy.injuryCriteria.chestAcceleration3ms) {
            violations.add("胸部3ms加速度超标: $chestAcceleration3ms > ${dummy.injuryCriteria.chestAcceleration3ms} g")
        }

        return ComplianceResult(
            isCompliant = violations.isEmpty(),
            details = if (violations.isEmpty()) "所有伤害指标均符合标准" else violations.joinToString("; ")
        )
    }

    /**
     * 生成测试报告摘要
     */
    fun generateTestReportSummary(
        heightRange: String,
        testType: String,
        results: Map<String, Any>
    ): String {
        val applicableDummies = R129r4eStandardDatabase.getApplicableDummies(heightRange)
        val dummyNames = applicableDummies.joinToString(", ") { it.dummyType }

        return """
            |ECE R129r4e 测试报告摘要
            |==========================
            |
            |适用身高范围: $heightRange
            |适用假人类型: $dummyNames
            |测试类型: $testType
            |
            |测试结果:
            |${results.entries.joinToString("\n") { "| ${it.key}: ${it.value}" }}
            |
            |标准依据: ECE R129 Rev.4
            |生效日期: 2018-12-29
            |
        """.trimMargin()
    }

    /**
     * 解析身高范围
     */
    private fun parseHeightRange(range: String): Pair<Double, Double> {
        val parts = range.split("-").map { it.trim().replace("cm", "") }
        return Pair(parts[0].toDouble(), parts[1].toDouble())
    }
}

/**
 * 合规结果
 */
data class ComplianceResult(
    val isCompliant: Boolean,
    val details: String
)

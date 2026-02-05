package com.childproduct.designassistant.service

import android.util.Log
import com.childproduct.designassistant.constants.StandardConstants
import com.childproduct.designassistant.database.entity.EceCrashTestDummy
import com.childproduct.designassistant.database.entity.FmvssCrashTestDummy
import com.childproduct.designassistant.repository.StandardRepositoryRefactored

/**
 * 标准输出生成器（重构版）
 * 物理隔离：ECE和FMVSS数据完全分离
 * 全链路标准拦截：确保输出内容与用户选择的标准一致
 */
class StandardOutputGenerator(private val standardRepository: StandardRepositoryRefactored) {

    companion object {
        private const val TAG = "StandardOutputGenerator"
    }

    /**
     * 设计方案输出
     */
    data class DesignProposal(
        val applicableStandards: List<String>,
        val dummyData: DummyDataSection,
        val designParameters: DesignParametersSection,
        val testRequirements: TestRequirementsSection,
        val standardTestItems: StandardTestItemsSection,
        val standardType: String  // 新增：标准类型标识
    )

    /**
     * 假人数据部分
     */
    data class DummyDataSection(
        val heightRange: String,
        val weightRange: String,
        val installationDirection: String,
        val dummyCode: String?,     // 新增：假人代码
        val dummyName: String?,     // 新增：假人名称
        val ageRange: String?       // 新增：年龄范围
    )

    /**
     * 设计参数部分
     */
    data class DesignParametersSection(
        val headRestHeight: String?,
        val seatWidth: String?,
        val envelope: String?,
        val sideImpactArea: String?
    )

    /**
     * 测试要求部分
     */
    data class TestRequirementsSection(
        val frontal: String?,
        val sideChestCompression: String?,
        val webbingStrength: String?,
        val sideImpactVelocity: String?  // 新增：侧碰速度
    )

    /**
     * 标准测试项部分
     */
    data class StandardTestItemsSection(
        val dynamicFrontal: String?,
        val dynamicRear: String?,
        val dynamicSide: String?,
        val flammability: String?
    )

    /**
     * 生成设计方案（全链路标准拦截）
     *
     * @param standardType 标准类型（必须通过StandardConstants验证）
     * @param heightCm 用户输入的身高（cm）
     * @param weightKg 用户输入的体重（kg）
     * @return 设计方案
     */
    suspend fun generateDesignProposal(
        standardType: String,
        heightCm: Double = 0.0,
        weightKg: Double = 0.0
    ): DesignProposal {
        // 全链路标准验证
        val validStandardType = StandardConstants.getStandardConstant(standardType)
        if (validStandardType == null) {
            Log.e(TAG, "[StandardFlow] 无效的标准类型: ${standardType}")
            return createErrorProposal(standardType, "无效的标准类型")
        }

        Log.d(TAG, "[StandardFlow] 生成设计方案，标准类型: ${validStandardType}, 身高: ${heightCm}cm, 体重: ${weightKg}kg")

        // 根据标准类型调用对应的Repository
        return when (validStandardType) {
            StandardConstants.ECE_R129 -> generateEceProposal(heightCm, weightKg)
            StandardConstants.FMVSS_213 -> generateFmvssProposal(heightCm, weightKg)
            else -> createErrorProposal(standardType, "未支持的标准类型: ${validStandardType}")
        }
    }

    /**
     * 生成ECE R129设计方案
     * 物理隔离：仅查询ECE数据
     */
    private suspend fun generateEceProposal(
        heightCm: Double,
        weightKg: Double
    ): DesignProposal {
        Log.d(TAG, "[ECE] 生成ECE R129设计方案")

        // 查询ECE假人数据
        val dummy = if (heightCm > 0) {
            standardRepository.getEceDummyByHeight(heightCm.toInt())
        } else {
            null
        }

        // 生成假人数据
        val dummyData = generateEceDummyData(dummy, heightCm, weightKg)

        // 生成设计参数
        val designParameters = generateEceDesignParameters(dummy)

        // 生成测试要求
        val testRequirements = generateEceTestRequirements(dummy)

        // 生成标准测试项
        val standardTestItems = generateEceStandardTestItems()

        return DesignProposal(
            applicableStandards = listOf(StandardConstants.getDisplayName(StandardConstants.ECE_R129)),
            dummyData = dummyData,
            designParameters = designParameters,
            testRequirements = testRequirements,
            standardTestItems = standardTestItems,
            standardType = StandardConstants.ECE_R129
        )
    }

    /**
     * 生成FMVSS 213设计方案
     * 物理隔离：仅查询FMVSS数据
     */
    private suspend fun generateFmvssProposal(
        heightCm: Double,
        weightKg: Double
    ): DesignProposal {
        Log.d(TAG, "[FMVSS] 生成FMVSS 213设计方案")

        // 查询FMVSS假人数据
        val dummy = if (heightCm > 0) {
            standardRepository.getFmvssDummyByHeight(heightCm.toInt())
        } else {
            null
        }

        // 生成假人数据
        val dummyData = generateFmvssDummyData(dummy, heightCm, weightKg)

        // 生成设计参数
        val designParameters = generateFmvssDesignParameters(dummy)

        // 生成测试要求
        val testRequirements = generateFmvssTestRequirements(dummy)

        // 生成标准测试项
        val standardTestItems = generateFmvssStandardTestItems()

        return DesignProposal(
            applicableStandards = listOf(StandardConstants.getDisplayName(StandardConstants.FMVSS_213)),
            dummyData = dummyData,
            designParameters = designParameters,
            testRequirements = testRequirements,
            standardTestItems = standardTestItems,
            standardType = StandardConstants.FMVSS_213
        )
    }

    /**
     * 生成ECE假人数据
     */
    private fun generateEceDummyData(
        dummy: EceCrashTestDummy?,
        heightCm: Double,
        weightKg: Double
    ): DummyDataSection {
        val heightRange = if (dummy != null) {
            "${dummy.minHeightCm} - ${dummy.maxHeightCm} cm"
        } else if (heightCm > 0) {
            "${heightCm.toInt()} cm"
        } else {
            "40 - 145 cm (Q0-Q10)"
        }

        val weightRange = if (dummy != null) {
            "${(dummy.minHeightCm / 10)} - ${(dummy.maxHeightCm / 10)} kg"
        } else if (weightKg > 0) {
            "${weightKg.toInt()} kg"
        } else {
            "0 - 36 kg"
        }

        val direction = if (dummy != null) {
            when (dummy.installDirection) {
                com.childproduct.designassistant.model.InstallDirection.REARWARD -> "后向 (Rearward)"
                com.childproduct.designassistant.model.InstallDirection.FORWARD -> "前向 (Forward)"
            }
        } else {
            "后向 / 前向"
        }

        return DummyDataSection(
            heightRange = heightRange,
            weightRange = weightRange,
            installationDirection = direction,
            dummyCode = dummy?.dummyCode,
            dummyName = dummy?.dummyName,
            ageRange = dummy?.ageRange
        )
    }

    /**
     * 生成FMVSS假人数据
     */
    private fun generateFmvssDummyData(
        dummy: FmvssCrashTestDummy?,
        heightCm: Double,
        weightKg: Double
    ): DummyDataSection {
        val heightRange = if (dummy != null) {
            val (minCm, maxCm) = dummy.toHeightCm()
            "${minCm} - ${maxCm} cm (${dummy.minHeightIn}\" - ${dummy.maxHeightIn}\")"
        } else if (heightCm > 0) {
            "${heightCm.toInt()} cm"
        } else {
            "86 - 168 cm (34\" - 66\")"
        }

        val weightRange = if (dummy != null) {
            "${(dummy.weightLb * 0.453592).toInt()} kg (${dummy.weightLb.toInt()} lbs)"
        } else if (weightKg > 0) {
            "${weightKg.toInt()} kg"
        } else {
            "0 - 45 kg (0 - 100 lbs)"
        }

        val direction = if (dummy != null) {
            when (dummy.installDirection) {
                com.childproduct.designassistant.model.InstallDirection.REARWARD -> "后向 (Rearward)"
                com.childproduct.designassistant.model.InstallDirection.FORWARD -> "前向 (Forward)"
            }
        } else {
            "后向 / 前向 / 腰带模式 / 增高垫"
        }

        return DummyDataSection(
            heightRange = heightRange,
            weightRange = weightRange,
            installationDirection = direction,
            dummyCode = dummy?.dummyCode,
            dummyName = dummy?.dummyName,
            ageRange = dummy?.ageRange
        )
    }

    /**
     * 生成ECE设计参数
     */
    private fun generateEceDesignParameters(dummy: EceCrashTestDummy?): DesignParametersSection {
        return DesignParametersSection(
            headRestHeight = "ECE R129: 参考GPS-028 Q系列假人数据，支持头枕调节",
            seatWidth = "ECE R129: ISOFIX SIZE CLASS (B1, B2, D, E)",
            envelope = "ECE R129: External Envelope (基于ISO-FIX)",
            sideImpactArea = "ECE R129: 侧面碰撞防护区域（Q系列假人HIC≤1000）"
        )
    }

    /**
     * 生成FMVSS设计参数
     */
    private fun generateFmvssDesignParameters(dummy: FmvssCrashTestDummy?): DesignParametersSection {
        return DesignParametersSection(
            headRestHeight = "FMVSS 213: 参考GPS-028 Q系列和CRABI系列假人",
            seatWidth = "FMVSS 213: 380-460mm（根据体重分组）",
            envelope = "FMVSS 213: Vehicle Envelope Requirements",
            sideImpactArea = "FMVSS 213a: 侧碰测试区域（30mph侧碰）"
        )
    }

    /**
     * 生成ECE测试要求
     */
    private fun generateEceTestRequirements(dummy: EceCrashTestDummy?): TestRequirementsSection {
        return TestRequirementsSection(
            frontal = "ECE R129: 50km/h ±2km/h 正碰，HIC≤1000",
            sideChestCompression = "ECE R129: 侧碰胸部压缩量 ≤ 35mm (Q1.5), ≤ 44mm (Q3)",
            webbingStrength = "ECE R129: 织带最小断裂强度 4.5kN",
            sideImpactVelocity = "ECE R129: 侧碰 50km/h"
        )
    }

    /**
     * 生成FMVSS测试要求
     */
    private fun generateFmvssTestRequirements(dummy: FmvssCrashTestDummy?): TestRequirementsSection {
        return TestRequirementsSection(
            frontal = "FMVSS 213: 30mph (48km/h) 正碰，固定滑台",
            sideChestCompression = "FMVSS 213a: 侧碰胸部加速度限制（Q3s假人）",
            webbingStrength = "FMVSS 213: 织带最小断裂强度 11kN (约束儿童) / 15kN (固定车辆)",
            sideImpactVelocity = "FMVSS 213a: 侧碰 30mph (2026年强制实施)"
        )
    }

    /**
     * 生成ECE标准测试项
     */
    private fun generateEceStandardTestItems(): StandardTestItemsSection {
        return StandardTestItemsSection(
            dynamicFrontal = "ECE R129: 50km/h 正碰（Q系列假人）",
            dynamicRear = "ECE R129: 后碰测试（如需要）",
            dynamicSide = "ECE R129: 50km/h 侧碰（Q系列假人）",
            flammability = "ECE R129: 参考ECE R118阻燃要求"
        )
    }

    /**
     * 生成FMVSS标准测试项
     */
    private fun generateFmvssStandardTestItems(): StandardTestItemsSection {
        return StandardTestItemsSection(
            dynamicFrontal = "FMVSS 213: 30mph 正碰（Hybrid III假人）",
            dynamicRear = "FMVSS 213: 后碰测试（如需要）",
            dynamicSide = "FMVSS 213a: 30mph 侧碰（Q3s假人，2026年强制）",
            flammability = "FMVSS 302: 燃烧测试要求"
        )
    }

    /**
     * 创建错误方案
     */
    private fun createErrorProposal(standardType: String, errorMessage: String): DesignProposal {
        return DesignProposal(
            applicableStandards = emptyList(),
            dummyData = DummyDataSection(errorMessage, errorMessage, errorMessage, null, null, null),
            designParameters = DesignParametersSection(null, null, null, null),
            testRequirements = TestRequirementsSection(null, null, null, null),
            standardTestItems = StandardTestItemsSection(null, null, null, null),
            standardType = standardType
        )
    }
}

package com.childproduct.designassistant.service.engineering

import com.childproduct.designassistant.model.CrashTestMapping
import com.childproduct.designassistant.model.InstallDirection
import com.childproduct.designassistant.model.InstallMethod
import com.childproduct.designassistant.model.ProductType
import com.childproduct.designassistant.model.engineering.AntiRotationType
import com.childproduct.designassistant.model.engineering.DummyType
import com.childproduct.designassistant.model.engineering.EngineeringInput
import com.childproduct.designassistant.model.engineering.EngineeringOutput
import com.childproduct.designassistant.model.engineering.IsofixEnvelope
import com.childproduct.designassistant.model.engineering.RoadmateTestMatrix
import com.childproduct.designassistant.model.engineering.SafetyParameter
import com.childproduct.designassistant.model.engineering.Standard
import com.childproduct.designassistant.model.engineering.TestMatrixMetadata
import com.childproduct.designassistant.model.engineering.TestMatrixRow
import java.text.SimpleDateFormat
import java.util.*

/**
 * 工程输出生成器 - 工程师专用
 * 实施标准隔离机制，确保输出严格符合工程规范
 * 
 * 核心功能：
 * 1. 标准隔离：不同标准的参数完全独立，无交叉混用
 * 2. 假人映射：精确映射身高范围到假人类型，包含安装方向规则
 * 3. 测试矩阵：生成ROADMATE 360格式的20列测试矩阵
 * 4. 合规验证：自动验证输入是否符合标准要求
 */
class EngineeringOutputGenerator {
    
    companion object {
        private const val APP_VERSION = "2.0.0"
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }
    
    /**
     * 生成工程输出
     * 
     * @param input 工程输入
     * @param primaryStandard 主标准（用于输出安全阈值）
     * @return 工程输出
     */
    fun generate(
        input: EngineeringInput,
        primaryStandard: Standard
    ): Result<EngineeringOutput> {
        try {
            // 1. 验证输入
            val validationResult = input.validate()
            if (!validationResult.isValid) {
                return Result.failure(
                    IllegalStateException(
                        "输入验证失败：\n${validationResult.errors.joinToString("\n")}"
                    )
                )
            }
            
            // 2. 获取适用的假人类型
            val dummyTypes = input.getApplicableDummies()
            
            // 3. 确定安装方向
            val installDirections = determineInstallDirections(input, dummyTypes)
            
            // 4. 获取ISOFIX Envelope
            val isofixEnvelope = getIsofixEnvelope(input.installMethod)
            
            // 5. 生成测试矩阵
            val testMatrix = generateTestMatrix(
                input.standards,
                dummyTypes,
                installDirections
            )
            
            // 6. 构建输出
            val output = EngineeringOutput(
                productName = "儿童安全座椅",
                productType = input.productType.displayName,
                standardVersion = primaryStandard.code,
                metadata = mapOf(
                    "generatedAt" to System.currentTimeMillis(),
                    "appVersion" to APP_VERSION,
                    "standards" to input.standards.map { it.code },
                    "dummyCoverage" to buildDummyCoverageString(dummyTypes)
                ),
                basicInfo = mapOf(
                    "productType" to input.productType.displayName,
                    "heightRange" to "${input.heightRange.minCm}-${input.heightRange.maxCm}cm",
                    "dummyCoverage" to buildDummyCoverageString(dummyTypes),
                    "installMethod" to (input.installMethod?.displayName ?: "N/A")
                ),
                standardMapping = mapOf(
                    "dummyMappings" to dummyTypes,
                    "installDirections" to installDirections
                ),
                isofixEnvelope = isofixEnvelope,
                testMatrix = testMatrix,
                safetyThresholds = mapOf(
                    "standard" to primaryStandard,
                    "dummyTypes" to dummyTypes
                ),
                complianceStatement = mapOf(
                    "standards" to input.standards,
                    "dummyTypes" to dummyTypes
                ),
                engineeringNotes = mapOf(
                    "input" to input,
                    "dummyTypes" to dummyTypes
                )
            )
            
            return Result.success(output)
            
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
    
    /**
     * 确定安装方向
     * 
     * 严格遵循 ECE R129 §5.1.3：
     * - 40-105cm：强制后向安装
     * - 105-150cm：允许前向安装，必须使用Top-tether
     */
    private fun determineInstallDirections(
        input: EngineeringInput,
        dummyTypes: List<DummyType>
    ): Map<DummyType, InstallDirection> {
        val directions = mutableMapOf<DummyType, InstallDirection>()
        
        dummyTypes.forEach { dummy ->
            // 规则：40-105cm强制后向安装
            if (dummy.heightRangeCm.start < 105) {
                directions[dummy] = InstallDirection.REARWARD
            } 
            // 规则：105cm以上可以使用前向或后向安装
            else if (dummy.heightRangeCm.start >= 105) {
                val userDirection = input.installMethod?.getDirection()
                if (userDirection != null) {
                    directions[dummy] = userDirection
                } else {
                    // 默认：105cm以上使用前向安装
                    directions[dummy] = InstallDirection.FORWARD
                }
            }
        }
        
        return directions
    }
    
    /**
     * 获取ISOFIX Envelope
     */
    private fun getIsofixEnvelope(
        installMethod: InstallMethod?
    ): IsofixEnvelope? {
        // 检查是否是 ISOFIX 相关的安装方式
        if (installMethod == null || 
            (installMethod != InstallMethod.ISOFIX && 
             installMethod != InstallMethod.ISOFIX_TOP_TETHER && 
             installMethod != InstallMethod.ISOFIX_SUPPORT_LEG)) {
            return null
        }
        
        // 根据安装方式确定对应的Envelope
        val direction = installMethod.getDirection() ?: InstallDirection.REARWARD
        return IsofixEnvelope.getEnvelopeByDirection(direction)
    }
    
    /**
     * 生成测试矩阵
     * 
     * ROADMATE 360格式（20列）：
     * Column 1: Test ID
     * Column 2: Standard
     * Column 3: Vehicle Seat Position
     * Column 4: Dummy Height (cm)
     * Column 5: Dummy Type
     * Column 6: Impact Type
     * Column 7: Impact Speed (km/h)
     * Column 8: Installation Method
     * Column 9: Vehicle Type
     * Column 10: ISOFIX Type
     * Column 11: Installation Direction
     * Column 12: Support Leg
     * Column 13: Anti-rotation Device
     * Column 14: Harness Adjustment
     * Column 15: Recline Position
     * Column 16: Measurement Points
     * Column 17: Criteria
     * Column 18: Top Tether
     * Column 19: Notes
     * Column 20: Test Date
     */
    private fun generateTestMatrix(
        standards: Set<Standard>,
        dummyTypes: List<DummyType>,
        installDirections: Map<DummyType, InstallDirection>
    ): RoadmateTestMatrix {
        val rows = mutableListOf<TestMatrixRow>()
        
        standards.forEach { standard ->
            dummyTypes.forEach { dummy ->
                val direction = installDirections[dummy] ?: InstallDirection.REARWARD
                val testCase = buildTestCase(standard, dummy, direction)
                rows.add(testCase)
            }
        }
        
        return RoadmateTestMatrix(
            rows = rows,
            metadata = TestMatrixMetadata(
                generatedAt = System.currentTimeMillis(),
                standard = standards.firstOrNull()?.code ?: "UNKNOWN",
                installMethod = "ISOFIX",
                rowCount = rows.size,
                dummyCoverage = buildDummyCoverageString(dummyTypes)
            )
        )
    }
    
    /**
     * 构建单个测试用例
     */
    private fun buildTestCase(
        standard: Standard,
        dummy: DummyType,
        direction: InstallDirection
    ): TestMatrixRow {
        // 规则：根据安装方向和假人类型确定测试要求
        val pulseType = when (direction) {
            InstallDirection.REARWARD -> "Frontal"
            InstallDirection.FORWARD -> "Frontal"
        }
        
        val impactType = dummy.code
        
        val antiRotation = when {
            direction == InstallDirection.REARWARD -> AntiRotationType.SUPPORT_LEG
            dummy.heightRangeCm.start >= 105 -> AntiRotationType.TOP_TETHER
            else -> AntiRotationType.SUPPORT_LEG
        }
        
        val topTether = when (direction) {
            InstallDirection.REARWARD -> "no"
            InstallDirection.FORWARD -> if (dummy.heightRangeCm.start >= 105) "yes" else "no"
            else -> "no"
        }
        
        return TestMatrixRow(
            sample = standard.code,
            pulse = pulseType,
            impact = impactType,
            dummyType = impactType,
            position = direction.displayName,
            installation = "ISOFIX",
            specificInstallation = "-",
            productConfiguration = "Upright",
            isofixAnchors = "yes",
            positionOfFloor = "Low",
            harness = "With",
            topTetherOrSupportLeg = antiRotation.displayName,
            dashboard = "With",
            comments = "",
            buckle = "no",
            adjuster = "no",
            isofix = "yes",
            topTether = topTether,
            quantity = "n/a",
            testNumber = "-"
        )
    }
    
    /**
     * 构建假人覆盖字符串
     */
    private fun buildDummyCoverageString(dummyTypes: List<DummyType>): String {
        return dummyTypes.joinToString(" → ") { "${it.code} (${it.heightRangeCm.start.toInt()}-${it.heightRangeCm.endInclusive.toInt()}cm)" }
    }
}

package com.childproduct.designassistant.model

import com.childproduct.designassistant.common.ValidationResult

/**
 * 碰撞测试映射系统
 * 提供准确的身高-假人类型-年龄段映射，基于UN R129/GB 27887-2024标准
 */
object CrashTestMapping {

    /**
     * 身高段到假人类型的映射表
     * 格式：身高范围(cm) -> 假人类型
     */
    private val heightToDummyMap = listOf(
        HeightToDummyMapping(40, 50, CrashTestDummy.Q0, "Group 0+", "新生儿"),
        HeightToDummyMapping(50, 60, CrashTestDummy.Q0_PLUS, "Group 0+", "大婴儿"),
        HeightToDummyMapping(60, 75, CrashTestDummy.Q1, "Group 0+/1", "幼儿"),
        HeightToDummyMapping(75, 87, CrashTestDummy.Q1_5, "Group 1", "学步儿童"),
        HeightToDummyMapping(87, 105, CrashTestDummy.Q3, "Group 2", "学前儿童"),
        HeightToDummyMapping(105, 125, CrashTestDummy.Q3_S, "Group 2/3", "儿童"),
        HeightToDummyMapping(125, 145, CrashTestDummy.Q6, "Group 3", "大龄儿童"),
        HeightToDummyMapping(145, 150, CrashTestDummy.Q10, "Group 3", "青少年")
    )

    /**
     * 身高段数据类
     */
    data class HeightToDummyMapping(
        val minHeightCm: Int,
        val maxHeightCm: Int,
        val dummyType: CrashTestDummy,
        val productGroup: String,
        val description: String
    )

    /**
     * 根据身高获取对应的假人类型
     * @param heightCm 儿童身高（厘米）
     * @return 匹配的假人类型，如果超出范围则返回最接近的假人类型
     */
    fun getDummyByHeight(heightCm: Int): CrashTestDummy {
        return heightToDummyMap.find { mapping ->
            heightCm >= mapping.minHeightCm && heightCm < mapping.maxHeightCm
        }?.dummyType ?: when {
            heightCm < 40 -> CrashTestDummy.Q0
            heightCm >= 150 -> CrashTestDummy.Q10
            else -> CrashTestDummy.Q3
        }
    }

    /**
     * 根据身高范围获取所有适用的假人类型
     * @param minHeightCm 最小身高（厘米）
     * @param maxHeightCm 最大身高（厘米）
     * @return 适用的假人类型列表
     */
    fun getDummiesByHeightRange(minHeightCm: Int, maxHeightCm: Int): List<CrashTestDummy> {
        return heightToDummyMap
            .filter { mapping ->
                // 检查身高范围是否有重叠
                !(mapping.maxHeightCm <= minHeightCm || mapping.minHeightCm >= maxHeightCm)
            }
            .map { it.dummyType }
            .distinct()
    }

    /**
     * 获取假人类型的详细信息
     * @param dummyType 假人类型
     * @return 假人详细信息
     */
    fun getDummyDetails(dummyType: CrashTestDummy): DummyDetails {
        val mapping = heightToDummyMap.find { it.dummyType == dummyType }
        return DummyDetails(
            dummyType = dummyType,
            displayName = dummyType.displayName,
            heightRange = dummyType.heightRange,
            weight = dummyType.weight.toFloatOrNull() ?: 0f,
            age = dummyType.age,
            productGroup = mapping?.productGroup ?: "Unknown",
            description = mapping?.description ?: "Unknown",
            hicLimit = dummyType.hicLimit,
            complianceParams = ComplianceParameters.getByDummy(CrashTestDummy.toComplianceDummy(dummyType))
        )
    }

    /**
     * 验证身高范围是否符合UN R129标准
     * @param minHeightCm 最小身高
     * @param maxHeightCm 最大身高
     * @return 验证结果
     */
    fun validateHeightRange(minHeightCm: Int, maxHeightCm: Int): ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // 检查范围是否有效
        if (minHeightCm >= maxHeightCm) {
            errors.add("最小身高必须小于最大身高")
        }

        // 检查是否在UN R129标准范围内（40-150cm）
        if (minHeightCm < 40) {
            warnings.add("最小身高${minHeightCm}cm低于UN R129标准范围（40cm）")
        }
        if (maxHeightCm > 150) {
            warnings.add("最大身高${maxHeightCm}cm超过UN R129标准范围（150cm）")
        }

        // 检查是否跨越多个假人类型
        val dummies = getDummiesByHeightRange(minHeightCm, maxHeightCm)
        if (dummies.size > 2) {
            warnings.add("身高范围跨越${dummies.size}种假人类型，可能需要多个测试场景")
        }

        // 检查是否涵盖了临界点
        val criticalPoints = listOf(50, 60, 75, 87, 105, 125, 145)
        val coveredCriticalPoints = criticalPoints.filter { point ->
            point >= minHeightCm && point < maxHeightCm
        }
        if (coveredCriticalPoints.isNotEmpty()) {
            warnings.add("身高范围包含临界点：${coveredCriticalPoints.joinToString(", ")}cm，需要特别关注")
        }

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors,
            warnings = warnings
        )
    }

    /**
     * 假人详细信息数据类
     */
    data class DummyDetails(
        val dummyType: CrashTestDummy,
        val displayName: String,
        val heightRange: String,
        val weight: Float,
        val age: String,
        val productGroup: String,
        val description: String,
        val hicLimit: Int,
        val complianceParams: ComplianceParameters
    )

    /**
     * 获取所有假人类型的映射信息
     * @return 所有假人类型的详细信息列表
     */
    fun getAllDummyMappings(): List<DummyDetails> {
        return CrashTestDummy.values().map { getDummyDetails(it) }
    }

    /**
     * 根据假人类型获取推荐的产品分组
     * @param dummyType 假人类型
     * @return 产品分组名称
     */
    fun getProductGroupByDummy(dummyType: CrashTestDummy): String {
        return heightToDummyMap.find { it.dummyType == dummyType }?.productGroup ?: "Unknown"
    }

    /**
     * 检查是否需要向后/向前兼容性测试
     * @param minHeightCm 最小身高
     * @param maxHeightCm 最大身高
     * @return 是否需要兼容性测试
     */
    fun needsCompatibilityTest(minHeightCm: Int, maxHeightCm: Int): Boolean {
        val dummies = getDummiesByHeightRange(minHeightCm, maxHeightCm)
        // 如果跨越多个假人类型，需要兼容性测试
        return dummies.size > 1
    }

    /**
     * 生成测试矩阵建议
     * @param minHeightCm 最小身高
     * @param maxHeightCm 最大身高
     * @return 测试矩阵建议列表
     */
    fun generateTestMatrixSuggestions(minHeightCm: Int, maxHeightCm: Int): List<TestScenario> {
        val dummies = getDummiesByHeightRange(minHeightCm, maxHeightCm)
        val scenarios = mutableListOf<TestScenario>()

        dummies.forEach { dummy ->
            val details = getDummyDetails(dummy)
            scenarios.add(
                TestScenario(
                    scenarioName = "Scenario ${scenarios.size + 1}: ${dummy.displayName}",
                    dummyType = dummy,
                    heightCm = extractMiddleHeight(details.heightRange),
                    testType = "FRONTAL_RIGID",
                    seatOrientation = if (dummy in listOf(CrashTestDummy.Q0, CrashTestDummy.Q0_PLUS, CrashTestDummy.Q1)) {
                        "REARWARD_FACING"
                    } else {
                        "FORWARD_FACING"
                    },
                    hicLimit = dummy.hicLimit,
                    chestAccelLimit = details.complianceParams.chestAccelerationLimit,
                    notes = listOf(
                        "假人类型: ${dummy.name}",
                        "产品分组: ${details.productGroup}",
                        "体重: ${details.weight}kg",
                        "HIC极限值: ${dummy.hicLimit}"
                    )
                )
            )
        }

        return scenarios
    }

    /**
     * 从身高范围字符串中提取中间身高
     * @param heightRange 身高范围字符串（如"60-75cm"）
     * @return 中间身高值
     */
    private fun extractMiddleHeight(heightRange: String): Int {
        val parts = heightRange.replace("cm", "").split("-")
        return if (parts.size == 2) {
            ((parts[0].toIntOrNull() ?: 0) + (parts[1].toIntOrNull() ?: 0)) / 2
        } else {
            75 // 默认值
        }
    }

    /**
     * 测试场景数据类
     */
    data class TestScenario(
        val scenarioName: String,
        val dummyType: CrashTestDummy,
        val heightCm: Int,
        val testType: String,
        val seatOrientation: String,
        val hicLimit: Int,
        val chestAccelLimit: Int,
        val notes: List<String>
    )
}

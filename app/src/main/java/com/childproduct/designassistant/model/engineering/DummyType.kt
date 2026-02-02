package com.childproduct.designassistant.model.engineering

import com.childproduct.designassistant.common.ValidationResult
import com.childproduct.designassistant.model.InstallDirection

/**
 * 假人类型 - 基于ECE R129 Rev.4标准
 * 修正映射：40-150cm → Q0/Q0+/Q1/Q1.5/Q3/Q3s/Q6/Q10（8个区间）
 */
enum class DummyType(
    val code: String,
    val displayName: String,
    val heightRangeCm: ClosedFloatingPointRange<Double>,
    val weightKg: Float,
    val ageRange: String,
    val productGroup: String,
    val installDirection: InstallDirection,
    val requiresAntiRotation: Boolean,
    val antiRotationType: AntiRotationType?
) {
    /**
     * Q0 - 新生儿假人（40-50cm）
     * 对应年龄：0-6个月
     * 强制后向安装
     */
    Q0(
        code = "Q0",
        displayName = "Q0",
        heightRangeCm = 40.0..50.0,
        weightKg = 3.5f,
        ageRange = "0-6个月",
        productGroup = "Group 0+",
        installDirection = InstallDirection.REARWARD,
        requiresAntiRotation = true,
        antiRotationType = AntiRotationType.SUPPORT_LEG
    ),

    /**
     * Q0+ - 大婴儿假人（50-60cm）
     * 对应年龄：6-12个月
     * 强制后向安装
     */
    Q0_PLUS(
        code = "Q0+",
        displayName = "Q0+",
        heightRangeCm = 50.0..60.0,
        weightKg = 5.0f,
        ageRange = "6-12个月",
        productGroup = "Group 0+",
        installDirection = InstallDirection.REARWARD,
        requiresAntiRotation = true,
        antiRotationType = AntiRotationType.SUPPORT_LEG
    ),

    /**
     * Q1 - 幼儿假人（60-75cm）
     * 对应年龄：1-2岁
     * 强制后向安装
     */
    Q1(
        code = "Q1",
        displayName = "Q1",
        heightRangeCm = 60.0..75.0,
        weightKg = 7.5f,
        ageRange = "1-2岁",
        productGroup = "Group 0+/1",
        installDirection = InstallDirection.REARWARD,
        requiresAntiRotation = true,
        antiRotationType = AntiRotationType.SUPPORT_LEG
    ),

    /**
     * Q1.5 - 学步儿童假人（75-87cm）
     * 对应年龄：2-3岁
     * 强制后向安装
     */
    Q1_5(
        code = "Q1.5",
        displayName = "Q1.5",
        heightRangeCm = 75.0..87.0,
        weightKg = 9.5f,
        ageRange = "2-3岁",
        productGroup = "Group 1",
        installDirection = InstallDirection.REARWARD,
        requiresAntiRotation = true,
        antiRotationType = AntiRotationType.SUPPORT_LEG
    ),

    /**
     * Q3 - 学前儿童假人（87-105cm）
     * 对应年龄：3-4岁
     * 强制后向安装（105cm以下）
     */
    Q3(
        code = "Q3",
        displayName = "Q3",
        heightRangeCm = 87.0..105.0,
        weightKg = 15.0f,
        ageRange = "3-4岁",
        productGroup = "Group 2",
        installDirection = InstallDirection.REARWARD,
        requiresAntiRotation = true,
        antiRotationType = AntiRotationType.SUPPORT_LEG
    ),

    /**
     * Q3s - 大龄儿童假人（105-125cm）
     * 对应年龄：4-6岁
     * 允许前向安装，强制使用Top-tether
     * ⚠️ 关键修正：原实现中遗漏此假人
     */
    Q3s(
        code = "Q3s",
        displayName = "Q3s",
        heightRangeCm = 105.0..125.0,
        weightKg = 21.0f,
        ageRange = "4-6岁",
        productGroup = "Group 2/3",
        installDirection = InstallDirection.FORWARD,
        requiresAntiRotation = true,
        antiRotationType = AntiRotationType.TOP_TETHER
    ),

    /**
     * Q6 - 大龄儿童假人（125-145cm）
     * 对应年龄：6-10岁
     * 允许前向安装，强制使用Top-tether
     */
    Q6(
        code = "Q6",
        displayName = "Q6",
        heightRangeCm = 125.0..145.0,
        weightKg = 29.0f,
        ageRange = "6-10岁",
        productGroup = "Group 3",
        installDirection = InstallDirection.FORWARD,
        requiresAntiRotation = true,
        antiRotationType = AntiRotationType.TOP_TETHER
    ),

    /**
     * Q10 - 青少年假人（145-150cm）
     * 对应年龄：10-12岁
     * 允许前向安装，强制使用Top-tether
     */
    Q10(
        code = "Q10",
        displayName = "Q10",
        heightRangeCm = 145.0..150.0,
        weightKg = 36.0f,
        ageRange = "10-12岁",
        productGroup = "Group 3",
        installDirection = InstallDirection.FORWARD,
        requiresAntiRotation = true,
        antiRotationType = AntiRotationType.TOP_TETHER
    );

    companion object {
        /**
         * 根据身高获取假人类型
         * @param heightCm 儿童身高（厘米）
         * @return 匹配的假人类型
         */
        fun fromHeight(heightCm: Double): DummyType {
            return values().find { heightCm in it.heightRangeCm }
                ?: when {
                    heightCm < 40.0 -> Q0
                    heightCm > 150.0 -> Q10
                    else -> Q3
                }
        }

        /**
         * 根据身高范围获取所有适用的假人类型
         * @param minHeightCm 最小身高（厘米）
         * @param maxHeightCm 最大身高（厘米）
         * @return 适用的假人类型列表（按身高排序）
         */
        fun fromHeightRange(minHeightCm: Double, maxHeightCm: Double): List<DummyType> {
            return values()
                .filter { dummy ->
                    // 检查身高范围是否有重叠
                    !(dummy.heightRangeCm.endInclusive <= minHeightCm ||
                      dummy.heightRangeCm.start >= maxHeightCm)
                }
                .sortedBy { it.heightRangeCm.start }
        }

        /**
         * 验证安装方向规则（ECE R129 §5.1.3）
         * @param heightCm 儿童身高
         * @return 验证结果
         */
        fun validateInstallDirection(heightCm: Double): ValidationResult {
            val errors = mutableListOf<String>()
            val warnings = mutableListOf<String>()

            // 规则1: 40-105cm强制后向安装
            if (heightCm in 40.0..105.0) {
                warnings.add(
                    "ECE R129 §5.1.3: ${heightCm.toInt()}cm身高范围" +
                    "强制要求后向安装（Rearward facing）"
                )
            }

            // 规则2: 105cm以上允许前向安装，但必须使用Top-tether
            if (heightCm >= 105.0) {
                warnings.add(
                    "ECE R129 §6.1.2: ${heightCm.toInt()}cm以上允许前向安装" +
                    "，但强制要求使用Top-tether防旋转装置"
                )
            }

            return ValidationResult(
                isValid = errors.isEmpty(),
                errors = errors,
                warnings = warnings
            )
        }

        /**
         * 获取所有假人类型的详细信息列表
         */
        fun getAllDummies(): List<DummyDetails> {
            return values().map { dummy ->
                DummyDetails(
                    code = dummy.code,
                    displayName = dummy.displayName,
                    heightRange = "${dummy.heightRangeCm.start.toInt()}-${dummy.heightRangeCm.endInclusive.toInt()}cm",
                    weight = dummy.weightKg,
                    ageRange = dummy.ageRange,
                    productGroup = dummy.productGroup,
                    installDirection = dummy.installDirection,
                    requiresAntiRotation = dummy.requiresAntiRotation,
                    antiRotationType = dummy.antiRotationType
                )
            }
        }
    }
}

/**
 * 防旋转装置类型
 */
enum class AntiRotationType(val displayName: String) {
    SUPPORT_LEG("支撑腿"),
    TOP_TETHER("Top-tether上拉带")
}

/**
 * 假人详细信息
 */
data class DummyDetails(
    val code: String,
    val displayName: String,
    val heightRange: String,
    val weight: Float,
    val ageRange: String,
    val productGroup: String,
    val installDirection: InstallDirection,
    val requiresAntiRotation: Boolean,
    val antiRotationType: AntiRotationType?
)

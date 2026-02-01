package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * 安全阈值实体
 * 基于UN R129 §7.1动态测试要求
 */
@Entity(
    tableName = "safety_threshold",
    foreignKeys = [
        ForeignKey(
            entity = CrashTestDummy::class,
            parentColumns = ["dummyId"],
            childColumns = ["dummyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["dummyId"]),
        Index(value = ["testItem", "dummyId"], unique = true)
    ]
)
data class SafetyThreshold(
    @PrimaryKey val thresholdId: String,      // THRESHOLD_Q0_HIC
    val dummyId: String,                      // DUMMY_Q0
    val testItem: String,                     // 测试项目: HIC, ChestAcceleration...
    val parameterName: String,                // 参数名称: HIC15, HIC36, ChestAcc3ms...
    val minValue: Double?,                    // 最小值（如适用）
    val maxValue: Double,                     // 最大值（阈值上限）
    val unit: String,                         // 单位: g, N, mm, -
    val testDurationMs: Int?,                 // 测试持续时间(ms): 15, 36, 3ms
    val applicableDummies: String,            // 适用假人范围: "Q0-Q1.5", "Q3-Q10"
    val standardSource: String,               // 标准来源: "UN R129 §7.1.2"
    val notes: String? = null,                // 备注说明
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        // 标准安全阈值配置（基于UN R129 Rev.4 §7.1）
        val STANDARD_THRESHOLDS = listOf(
            // HIC阈值
            SafetyThreshold(
                thresholdId = "THRESHOLD_HIC_Q0_Q15",
                dummyId = "DUMMY_Q0",
                testItem = "头部伤害准则",
                parameterName = "HIC15",
                minValue = null,
                maxValue = 390.0,
                unit = "-",
                testDurationMs = 15,
                applicableDummies = "Q0-Q1.5",
                standardSource = "UN R129 §7.1.2",
                notes = "Q0-Q1.5假人使用HIC15，Q3+假人使用HIC36"
            ),
            SafetyThreshold(
                thresholdId = "THRESHOLD_HIC_Q1_5",
                dummyId = "DUMMY_Q1_5",
                testItem = "头部伤害准则",
                parameterName = "HIC36",
                minValue = null,
                maxValue = 570.0,
                unit = "-",
                testDurationMs = 36,
                applicableDummies = "Q1.5",
                standardSource = "UN R129 §7.1.2",
                notes = "Q1.5假人特殊HIC36阈值"
            ),
            SafetyThreshold(
                thresholdId = "THRESHOLD_HIC_Q3_Q10",
                dummyId = "DUMMY_Q3",
                testItem = "头部伤害准则",
                parameterName = "HIC36",
                minValue = null,
                maxValue = 1000.0,
                unit = "-",
                testDurationMs = 36,
                applicableDummies = "Q3-Q10",
                standardSource = "UN R129 §7.1.2"
            ),
            // 胸部加速度
            SafetyThreshold(
                thresholdId = "THRESHOLD_CHEST_ACC_Q0_Q15",
                dummyId = "DUMMY_Q0",
                testItem = "胸部合成加速度",
                parameterName = "ChestAcc3ms",
                minValue = null,
                maxValue = 55.0,
                unit = "g",
                testDurationMs = 3,
                applicableDummies = "Q0-Q1.5",
                standardSource = "UN R129 §7.1.3"
            ),
            SafetyThreshold(
                thresholdId = "THRESHOLD_CHEST_ACC_Q3_Q10",
                dummyId = "DUMMY_Q3",
                testItem = "胸部合成加速度",
                parameterName = "ChestAcc3ms",
                minValue = null,
                maxValue = 60.0,
                unit = "g",
                testDurationMs = 3,
                applicableDummies = "Q3-Q10",
                standardSource = "UN R129 §7.1.3"
            ),
            // 颈部张力
            SafetyThreshold(
                thresholdId = "THRESHOLD_NECK_TENSION_Q0_Q15",
                dummyId = "DUMMY_Q0",
                testItem = "颈部张力",
                parameterName = "NeckTension",
                minValue = null,
                maxValue = 1800.0,
                unit = "N",
                testDurationMs = null,
                applicableDummies = "Q0-Q1.5",
                standardSource = "UN R129 §7.1.4"
            ),
            SafetyThreshold(
                thresholdId = "THRESHOLD_NECK_TENSION_Q3_Q10",
                dummyId = "DUMMY_Q3",
                testItem = "颈部张力",
                parameterName = "NeckTension",
                minValue = null,
                maxValue = 2000.0,
                unit = "N",
                testDurationMs = null,
                applicableDummies = "Q3-Q10",
                standardSource = "UN R129 §7.1.4"
            ),
            // 颈部压力
            SafetyThreshold(
                thresholdId = "THRESHOLD_NECK_COMPRESSION_Q0_Q15",
                dummyId = "DUMMY_Q0",
                testItem = "颈部压力",
                parameterName = "NeckCompression",
                minValue = null,
                maxValue = 2200.0,
                unit = "N",
                testDurationMs = null,
                applicableDummies = "Q0-Q1.5",
                standardSource = "UN R129 §7.1.4"
            ),
            SafetyThreshold(
                thresholdId = "THRESHOLD_NECK_COMPRESSION_Q3_Q10",
                dummyId = "DUMMY_Q3",
                testItem = "颈部压力",
                parameterName = "NeckCompression",
                minValue = null,
                maxValue = 2500.0,
                unit = "N",
                testDurationMs = null,
                applicableDummies = "Q3-Q10",
                standardSource = "UN R129 §7.1.4"
            ),
            // 头部位移
            SafetyThreshold(
                thresholdId = "THRESHOLD_HEAD_EXCURSION_ALL",
                dummyId = "DUMMY_Q0",
                testItem = "头部位移",
                parameterName = "HeadExcursion",
                minValue = null,
                maxValue = 550.0,
                unit = "mm",
                testDurationMs = null,
                applicableDummies = "Q0-Q10",
                standardSource = "UN R129 §7.1.5"
            ),
            // 膝部位移
            SafetyThreshold(
                thresholdId = "THRESHOLD_KNEE_EXCURSION_ALL",
                dummyId = "DUMMY_Q0",
                testItem = "膝部位移",
                parameterName = "KneeExcursion",
                minValue = null,
                maxValue = 650.0,
                unit = "mm",
                testDurationMs = null,
                applicableDummies = "Q0-Q10",
                standardSource = "UN R129 §7.1.6"
            ),
            // 胸部位移
            SafetyThreshold(
                thresholdId = "THRESHOLD_CHEST_DEFLECTION_Q0_Q15",
                dummyId = "DUMMY_Q0",
                testItem = "胸部位移",
                parameterName = "ChestDeflection",
                minValue = null,
                maxValue = 45.0,
                unit = "mm",
                testDurationMs = null,
                applicableDummies = "Q0-Q1.5",
                standardSource = "UN R129 §7.1.7"
            ),
            SafetyThreshold(
                thresholdId = "THRESHOLD_CHEST_DEFLECTION_Q3_Q10",
                dummyId = "DUMMY_Q3",
                testItem = "胸部位移",
                parameterName = "ChestDeflection",
                minValue = null,
                maxValue = 52.0,
                unit = "mm",
                testDurationMs = null,
                applicableDummies = "Q3-Q10",
                standardSource = "UN R129 §7.1.7"
            )
        )
    }
}

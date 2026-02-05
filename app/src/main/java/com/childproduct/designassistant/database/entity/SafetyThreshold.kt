package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * 安全阈值实体
 * 基于UN R129 §7.1动态测试要求（Rev.5，2022版）
 * 修复：添加standardType字段以支持按标准类型过滤数据
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
        Index(value = ["testItem", "dummyId"], unique = true),
        Index(value = ["standardType"])  // 新增：支持按标准类型查询
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
    val standardType: String = "ECE_R129",    // 新增：标准类型标识 "ECE_R129", "FMVSS_213", "GB_27887_2024"
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        // 标准安全阈值配置（基于UN R129 Rev.5 §7.1，2022版）
        val STANDARD_THRESHOLDS = listOf(
            // HIC阈值 - Q0/Q0+ (15ms)
            SafetyThreshold(
                thresholdId = "THRESHOLD_HIC_Q0_Q0PLUS",
                dummyId = "DUMMY_Q0",
                testItem = "头部伤害准则",
                parameterName = "HIC15",
                minValue = null,
                maxValue = 300.0,
                unit = "-",
                testDurationMs = 15,
                applicableDummies = "Q0/Q0+",
                standardSource = "UN R129 §7.1.2",
                notes = "Q0/Q0+假人使用HIC15"
            ),
            // HIC阈值 - Q1/Q1.5 (15ms)
            SafetyThreshold(
                thresholdId = "THRESHOLD_HIC_Q1_Q15",
                dummyId = "DUMMY_Q1",
                testItem = "头部伤害准则",
                parameterName = "HIC15",
                minValue = null,
                maxValue = 400.0,
                unit = "-",
                testDurationMs = 15,
                applicableDummies = "Q1/Q1.5",
                standardSource = "UN R129 §7.1.2",
                notes = "Q1/Q1.5假人使用HIC15"
            ),
            // HIC阈值 - Q3-Q10 (36ms)
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
            // 胸部加速度 - Q0-Q1.5
            SafetyThreshold(
                thresholdId = "THRESHOLD_CHEST_ACC_Q0_Q15",
                dummyId = "DUMMY_Q0",
                testItem = "胸部合成加速度（峰值）",
                parameterName = "ChestAcc3ms",
                minValue = null,
                maxValue = 55.0,
                unit = "g",
                testDurationMs = 3,
                applicableDummies = "Q0-Q1.5",
                standardSource = "UN R129 §7.1.3"
            ),
            // 胸部加速度 - Q3-Q10
            SafetyThreshold(
                thresholdId = "THRESHOLD_CHEST_ACC_Q3_Q10",
                dummyId = "DUMMY_Q3",
                testItem = "胸部合成加速度（峰值）",
                parameterName = "ChestAcc3ms",
                minValue = null,
                maxValue = 60.0,
                unit = "g",
                testDurationMs = 3,
                applicableDummies = "Q3-Q10",
                standardSource = "UN R129 §7.1.3"
            ),
            // 颈部张力 - Q0
            SafetyThreshold(
                thresholdId = "THRESHOLD_NECK_TENSION_Q0",
                dummyId = "DUMMY_Q0",
                testItem = "颈部张力",
                parameterName = "NeckTension",
                minValue = null,
                maxValue = 1500.0,
                unit = "N",
                testDurationMs = null,
                applicableDummies = "Q0",
                standardSource = "UN R129 §7.1.4"
            ),
            // 颈部张力 - Q1/Q1.5
            SafetyThreshold(
                thresholdId = "THRESHOLD_NECK_TENSION_Q1_Q15",
                dummyId = "DUMMY_Q1",
                testItem = "颈部张力",
                parameterName = "NeckTension",
                minValue = null,
                maxValue = 1800.0,
                unit = "N",
                testDurationMs = null,
                applicableDummies = "Q1/Q1.5",
                standardSource = "UN R129 §7.1.4"
            ),
            // 颈部张力 - Q3-Q10
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
            // 颈部压力 - Q0
            SafetyThreshold(
                thresholdId = "THRESHOLD_NECK_COMPRESSION_Q0",
                dummyId = "DUMMY_Q0",
                testItem = "颈部压力",
                parameterName = "NeckCompression",
                minValue = null,
                maxValue = 2000.0,
                unit = "N",
                testDurationMs = null,
                applicableDummies = "Q0",
                standardSource = "UN R129 §7.1.4"
            ),
            // 颈部压力 - Q1/Q1.5
            SafetyThreshold(
                thresholdId = "THRESHOLD_NECK_COMPRESSION_Q1_Q15",
                dummyId = "DUMMY_Q1",
                testItem = "颈部压力",
                parameterName = "NeckCompression",
                minValue = null,
                maxValue = 2200.0,
                unit = "N",
                testDurationMs = null,
                applicableDummies = "Q1/Q1.5",
                standardSource = "UN R129 §7.1.4"
            ),
            // 颈部压力 - Q3-Q10
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
            // 头部位移 - 后向安装
            SafetyThreshold(
                thresholdId = "THRESHOLD_HEAD_EXCURSION_REARWARD",
                dummyId = "DUMMY_Q0",
                testItem = "头部位移（后向）",
                parameterName = "HeadExcursion",
                minValue = null,
                maxValue = 500.0,
                unit = "mm",
                testDurationMs = null,
                applicableDummies = "Q0-Q3",
                standardSource = "UN R129 §7.1.5",
                notes = "后向安装假人(Q0-Q3)"
            ),
            // 头部位移 - 前向安装
            SafetyThreshold(
                thresholdId = "THRESHOLD_HEAD_EXCURSION_FORWARD",
                dummyId = "DUMMY_Q6",
                testItem = "头部位移（前向）",
                parameterName = "HeadExcursion",
                minValue = null,
                maxValue = 550.0,
                unit = "mm",
                testDurationMs = null,
                applicableDummies = "Q6-Q10",
                standardSource = "UN R129 §7.1.5",
                notes = "前向安装假人(Q6-Q10)"
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
            // 胸部压缩量 - Q0-Q1.5
            SafetyThreshold(
                thresholdId = "THRESHOLD_CHEST_DEFLECTION_Q0_Q15",
                dummyId = "DUMMY_Q0",
                testItem = "胸部压缩量",
                parameterName = "ChestDeflection",
                minValue = null,
                maxValue = 40.0,
                unit = "mm",
                testDurationMs = null,
                applicableDummies = "Q0-Q1.5",
                standardSource = "UN R129 §7.1.7"
            ),
            // 胸部压缩量 - Q3-Q10
            SafetyThreshold(
                thresholdId = "THRESHOLD_CHEST_DEFLECTION_Q3_Q10",
                dummyId = "DUMMY_Q3",
                testItem = "胸部压缩量",
                parameterName = "ChestDeflection",
                minValue = null,
                maxValue = 48.0,
                unit = "mm",
                testDurationMs = null,
                applicableDummies = "Q3-Q10",
                standardSource = "UN R129 §7.1.7"
            )
        )
    }
}

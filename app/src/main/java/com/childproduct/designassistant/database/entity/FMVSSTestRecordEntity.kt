package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * FMVSS测试记录实体
 * 记录FMVSS 213标准测试的结果数据
 */
@Entity(
    tableName = "fmvss_test_records",
    foreignKeys = [
        ForeignKey(
            entity = FMVSSTestConfigEntity::class,
            parentColumns = ["configId"],
            childColumns = ["configId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["configId"]),
        Index(value = ["testDate"])
    ]
)
data class FMVSSTestRecordEntity(
    @PrimaryKey val recordId: String,        // TEST_R001
    val configId: String,                    // CONFIG_Q3s
    val testDate: Long,                      // 测试日期
    val testResult: String,                  // PASS, FAIL
    val hicValue: Double?,                   // HIC值
    val chestAccelerationG: Double?,         // 胸部加速度(g)
    val chestDeflectionMm: Double?,          // 胸部压缩量(mm)
    val headDisplacementMm: Double?,         // 头部位移(mm)
    val neckTensionN: Double?,               // 颈部张力(N)
    val comments: String?,                   // 备注
    val lastUpdated: Long = System.currentTimeMillis()
)

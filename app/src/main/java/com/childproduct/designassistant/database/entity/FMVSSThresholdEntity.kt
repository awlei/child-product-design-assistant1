package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * FMVSS安全阈值实体
 * 专用于FMVSS 213标准的安全伤害评估阈值
 */
@Entity(tableName = "fmvss_thresholds")
data class FMVSSThresholdEntity(
    @PrimaryKey val thresholdId: String,      // THRESH_Q3s, THRESH_HIII_3Y
    val dummyCode: String,                   // Q3s, HIII, 3y, 6y, 10y
    val hicLimit: Int,                       // HIC限值
    val chestAccelerationGLimit: Int,        // 胸部加速度限值(g)
    val chestDeflectionLimitMm: Int?,        // 胸部压缩量限值(mm)
    val headDisplacementLimitMm: Int?,       // 头部位移限值(mm)
    val neckTensionLimitN: Int?,             // 颈部张力限值(N)
    val lastUpdated: Long = System.currentTimeMillis()
)

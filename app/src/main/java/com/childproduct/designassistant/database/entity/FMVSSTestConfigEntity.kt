package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * FMVSS测试配置实体
 * 专用于FMVSS 213标准的测试配置
 */
@Entity(tableName = "fmvss_test_configurations")
data class FMVSSTestConfigEntity(
    @PrimaryKey val configId: String,         // CONFIG_Q3s, CONFIG_HIII
    val standardId: String,                  // FMVSS_213, FMVSS_213a
    val dummyCode: String,                   // Q3s, HIII, 3y, 6y, 10y
    val testSpeedKmph: Int,                  // 测试速度(km/h): 48 (30mph) for side impact
    val testType: String,                    // Side Impact, Frontal Impact
    val installDirection: String,            // FORWARD, REARWARD
    val description: String?,               // 测试描述
    val lastUpdated: Long = System.currentTimeMillis()
)

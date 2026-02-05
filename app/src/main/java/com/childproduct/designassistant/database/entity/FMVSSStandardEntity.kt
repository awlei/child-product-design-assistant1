package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * FMVSS标准实体
 * 
 * 用于持久化FMVSS标准的基本信息
 */
@Entity(tableName = "fmvss_standards")
data class FMVSSStandardEntity(
    @PrimaryKey
    val standardId: String,
    val standardName: String,
    val standardType: String,
    val applicableRegion: String,
    val applicableWeight: String,
    val applicableAge: String,
    val coreScope: String,
    val effectiveDate: String,
    val standardStatus: String,
    val dataSource: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * FMVSS假人实体
 */
@Entity(tableName = "fmvss_dummies")
data class FMVSSDummyEntity(
    @PrimaryKey
    val dummyCode: String,
    val displayName: String,
    val weightLbs: Double,
    val weightKg: Double,
    val ageRange: String,
    val applicableStandards: String, // JSON string
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * FMVSS测试配置实体
 */
@Entity(tableName = "fmvss_test_configurations")
data class FMVSSTestConfigEntity(
    @PrimaryKey
    val configId: String,
    val standardId: String,
    val dummyCode: String,
    val testSpeedKmph: Int,
    val testType: String,
    val installDirection: String,
    val description: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * FMVSS安全阈值实体
 */
@Entity(tableName = "fmvss_thresholds")
data class FMVSSThresholdEntity(
    @PrimaryKey
    val thresholdId: String,
    val standardId: String,
    val dummyCode: String,
    val hicLimit: Int,
    val chestAccelerationGLimit: Int,
    val chestDeflectionLimitMm: Int?,
    val headDisplacementLimitMm: Int?,
    val neckTensionLimitN: Int?,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * FMVSS测试记录实体
 */
@Entity(tableName = "fmvss_test_records")
data class FMVSSTestRecordEntity(
    @PrimaryKey
    val testId: String,
    val configId: String,
    val testDate: Long,
    val passed: Boolean = false,
    val testResult: String,
    val hicValue: Double?,
    val chestAccelerationG: Double?,
    val chestDeflectionMm: Double?,
    val headDisplacementMm: Double?,
    val neckTensionN: Double?,
    val comments: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * FMVSS产品测试实体
 */
@Entity(tableName = "fmvss_product_tests")
data class FMVSSProductTestEntity(
    @PrimaryKey
    val testId: String,
    val productId: String,
    val manufacturer: String?,
    val testDate: Long,
    val testType: String,
    val testSpeedKmph: Int,
    val testResult: String,
    val certificationNumber: String?,
    val testLab: String?,
    val comments: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

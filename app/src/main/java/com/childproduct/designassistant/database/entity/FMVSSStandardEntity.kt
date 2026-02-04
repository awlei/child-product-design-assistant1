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
    val testName: String,
    val standardId: String,
    val impactType: String,
    val velocityKmh: Double,
    val velocityRange: String,
    val accelerationProfile: String,
    val dummyTypes: String, // JSON string
    val injuryCriteria: String, // JSON string
    val excursionLimits: String, // JSON string
    val specialRequirements: String?, // JSON string
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
    val criterion: String,
    val limitValue: Double,
    val unit: String,
    val testCondition: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * FMVSS测试记录实体
 */
@Entity(tableName = "fmvss_test_records")
data class FMVSSTestRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val recordId: Long = 0,
    val testId: String,
    val configId: String,
    val dummyCode: String,
    val testDate: String,
    val velocityActual: Double?,
    val temperature: Double?,
    val injuryResults: String, // JSON string
    val excursionResults: String, // JSON string
    val passed: Boolean,
    val remarks: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * FMVSS产品测试实体
 */
@Entity(tableName = "fmvss_product_tests")
data class FMVSSProductTestEntity(
    @PrimaryKey(autoGenerate = true)
    val productId: Long = 0,
    val productName: String,
    val productModel: String,
    val manufacturer: String,
    val testStandards: String, // JSON string
    val testResults: String, // JSON string
    val certificationDate: String?,
    val certificationNumber: String?,
    val expiresDate: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

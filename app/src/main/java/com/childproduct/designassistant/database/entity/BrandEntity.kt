package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 品牌产品实体
 * 
 * 存储品牌产品的基本信息和参数
 */
@Entity(tableName = "brand_products")
data class BrandProductEntity(
    @PrimaryKey
    val productId: String,
    val brandName: String,
    val productName: String,
    val model: String,
    val productType: String,
    val applicableWeight: String,
    val applicableAge: String,
    val installationType: String,
    val features: String, // JSON string
    val priceRange: String?,
    val availability: String?,
    val imageUrl: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * 品牌参数实体
 * 
 * 存储品牌产品的详细技术参数
 */
@Entity(tableName = "brand_parameters")
data class BrandParameterEntity(
    @PrimaryKey
    val parameterId: String,
    val productId: String,
    val parameterName: String,
    val parameterValue: String,
    val unit: String?,
    val minValue: String?,
    val maxValue: String?,
    val standardReference: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * 品牌功能实体
 * 
 * 存储品牌产品的特色功能
 */
@Entity(tableName = "brand_features")
data class BrandFeatureEntity(
    @PrimaryKey
    val featureId: String,
    val productId: String,
    val featureName: String,
    val featureDescription: String,
    val isStandard: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * 品牌合规性实体
 * 
 * 存储品牌产品的合规性信息
 */
@Entity(tableName = "brand_compliance")
data class BrandComplianceEntity(
    @PrimaryKey
    val complianceId: String,
    val productId: String,
    val standardId: String,
    val certificationStatus: String,
    val certificationDate: String?,
    val certificationNumber: String?,
    val expiresDate: String?,
    val testResults: String?, // JSON string
    val remarks: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * 品牌评价实体
 * 
 * 存储品牌产品的用户评价
 */
@Entity(tableName = "brand_reviews")
data class BrandReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val reviewId: Long = 0,
    val productId: String,
    val rating: Double,
    val reviewText: String,
    val reviewerName: String?,
    val reviewDate: String,
    val source: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * 品牌对比实体
 * 
 * 存储品牌产品的对比数据
 */
@Entity(tableName = "brand_comparisons")
data class BrandComparisonEntity(
    @PrimaryKey(autoGenerate = true)
    val comparisonId: Long = 0,
    val comparisonName: String,
    val productIds: String, // JSON string array
    val comparisonCriteria: String, // JSON string
    val comparisonResult: String, // JSON string
    val createdAt: Long = System.currentTimeMillis()
)

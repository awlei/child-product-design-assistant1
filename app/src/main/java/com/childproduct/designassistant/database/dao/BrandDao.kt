package com.childproduct.designassistant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.childproduct.designassistant.database.entity.*

/**
 * 品牌数据库数据访问对象
 */
@Dao
interface BrandDao {
    
    // ========== 产品相关 ==========
    
    /**
     * 获取所有品牌产品
     */
    @Query("SELECT * FROM brand_products ORDER BY brandName, productName")
    suspend fun getAllProducts(): List<BrandProductEntity>
    
    /**
     * 根据品牌获取产品
     */
    @Query("SELECT * FROM brand_products WHERE brandName = :brandName")
    suspend fun getProductsByBrand(brandName: String): List<BrandProductEntity>
    
    /**
     * 根据产品ID获取产品
     */
    @Query("SELECT * FROM brand_products WHERE productId = :productId")
    suspend fun getProductById(productId: String): BrandProductEntity?
    
    /**
     * 根据产品类型获取产品
     */
    @Query("SELECT * FROM brand_products WHERE productType = :productType")
    suspend fun getProductsByType(productType: String): List<BrandProductEntity>
    
    /**
     * 根据重量范围获取产品
     */
    @Query("SELECT * FROM brand_products WHERE applicableWeight LIKE :weightRange")
    suspend fun getProductsByWeightRange(weightRange: String): List<BrandProductEntity>
    
    /**
     * 插入或更新产品
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProduct(product: BrandProductEntity)
    
    /**
     * 批量插入产品
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<BrandProductEntity>)
    
    // ========== 参数相关 ==========
    
    /**
     * 获取产品的所有参数
     */
    @Query("SELECT * FROM brand_parameters WHERE productId = :productId")
    suspend fun getParametersByProduct(productId: String): List<BrandParameterEntity>
    
    /**
     * 根据参数名获取参数
     */
    @Query("SELECT * FROM brand_parameters WHERE productId = :productId AND parameterName = :parameterName")
    suspend fun getParameterByName(productId: String, parameterName: String): BrandParameterEntity?
    
    /**
     * 插入或更新参数
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateParameter(parameter: BrandParameterEntity)
    
    /**
     * 批量插入参数
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParameters(parameters: List<BrandParameterEntity>)
    
    // ========== 功能相关 ==========
    
    /**
     * 获取产品的所有功能
     */
    @Query("SELECT * FROM brand_features WHERE productId = :productId")
    suspend fun getFeaturesByProduct(productId: String): List<BrandFeatureEntity>
    
    /**
     * 获取产品的标准功能
     */
    @Query("SELECT * FROM brand_features WHERE productId = :productId AND isStandard = 1")
    suspend fun getStandardFeatures(productId: String): List<BrandFeatureEntity>
    
    /**
     * 插入或更新功能
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateFeature(feature: BrandFeatureEntity)
    
    /**
     * 批量插入功能
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeatures(features: List<BrandFeatureEntity>)
    
    // ========== 合规性相关 ==========
    
    /**
     * 获取产品的所有合规性信息
     */
    @Query("SELECT * FROM brand_compliance WHERE productId = :productId")
    suspend fun getComplianceByProduct(productId: String): List<BrandComplianceEntity>
    
    /**
     * 根据标准获取合规性信息
     */
    @Query("SELECT * FROM brand_compliance WHERE productId = :productId AND standardId = :standardId")
    suspend fun getComplianceByStandard(productId: String, standardId: String): BrandComplianceEntity?
    
    /**
     * 获取有效的认证
     */
    @Query("SELECT * FROM brand_compliance WHERE certificationStatus = 'Certified' AND (expiresDate IS NULL OR expiresDate > datetime('now'))")
    suspend fun getValidCertifications(): List<BrandComplianceEntity>
    
    /**
     * 插入或更新合规性信息
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCompliance(compliance: BrandComplianceEntity)
    
    /**
     * 更新合规性状态
     */
    @Update
    suspend fun updateCompliance(compliance: BrandComplianceEntity)
    
    // ========== 评价相关 ==========
    
    /**
     * 获取产品的所有评价
     */
    @Query("SELECT * FROM brand_reviews WHERE productId = :productId ORDER BY reviewDate DESC")
    suspend fun getReviewsByProduct(productId: String): List<BrandReviewEntity>
    
    /**
     * 获取产品的平均评分
     */
    @Query("SELECT AVG(rating) FROM brand_reviews WHERE productId = :productId")
    suspend fun getAverageRating(productId: String): Double?
    
    /**
     * 插入评价
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: BrandReviewEntity)
    
    // ========== 对比相关 ==========
    
    /**
     * 获取所有对比
     */
    @Query("SELECT * FROM brand_comparisons ORDER BY createdAt DESC")
    suspend fun getAllComparisons(): List<BrandComparisonEntity>
    
    /**
     * 根据对比ID获取对比
     */
    @Query("SELECT * FROM brand_comparisons WHERE comparisonId = :comparisonId")
    suspend fun getComparisonById(comparisonId: Long): BrandComparisonEntity?
    
    /**
     * 插入对比
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComparison(comparison: BrandComparisonEntity)
    
    // ========== 删除操作 ==========
    
    /**
     * 删除产品及其相关数据
     */
    @Query("DELETE FROM brand_products WHERE productId = :productId")
    suspend fun deleteProduct(productId: String)
    
    /**
     * 删除产品的参数
     */
    @Query("DELETE FROM brand_parameters WHERE productId = :productId")
    suspend fun deleteProductParameters(productId: String)
    
    /**
     * 删除产品的功能
     */
    @Query("DELETE FROM brand_features WHERE productId = :productId")
    suspend fun deleteProductFeatures(productId: String)
    
    /**
     * 删除产品的合规性信息
     */
    @Query("DELETE FROM brand_compliance WHERE productId = :productId")
    suspend fun deleteProductCompliance(productId: String)
    
    /**
     * 删除产品的评价
     */
    @Query("DELETE FROM brand_reviews WHERE productId = :productId")
    suspend fun deleteProductReviews(productId: String)
    
    // ========== 搜索功能 ==========
    
    /**
     * 搜索产品
     */
    @Query("SELECT * FROM brand_products WHERE brandName LIKE '%' || :query || '%' OR productName LIKE '%' || :query || '%' OR model LIKE '%' || :query || '%'")
    suspend fun searchProducts(query: String): List<BrandProductEntity>
    
    /**
     * 根据功能搜索产品
     */
    @Query("SELECT DISTINCT bp.* FROM brand_products bp INNER JOIN brand_features bf ON bp.productId = bf.productId WHERE bf.featureName LIKE '%' || :featureName || '%'")
    suspend fun searchProductsByFeature(featureName: String): List<BrandProductEntity>
}

package com.childproduct.designassistant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.childproduct.designassistant.database.entity.*

/**
 * FMVSS标准数据访问对象
 */
@Dao
interface FMVSSDao {
    
    // ========== 标准相关 ==========
    
    /**
     * 获取所有FMVSS标准
     */
    @Query("SELECT * FROM fmvss_standards ORDER BY standardId")
    suspend fun getAllStandards(): List<FMVSSStandardEntity>
    
    /**
     * 根据ID获取标准
     */
    @Query("SELECT * FROM fmvss_standards WHERE standardId = :standardId")
    suspend fun getStandardById(standardId: String): FMVSSStandardEntity?
    
    /**
     * 根据类型获取标准列表
     */
    @Query("SELECT * FROM fmvss_standards WHERE standardType = :type")
    suspend fun getStandardsByType(type: String): List<FMVSSStandardEntity>
    
    /**
     * 插入或更新标准
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStandard(standard: FMVSSStandardEntity)
    
    /**
     * 批量插入标准
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandards(standards: List<FMVSSStandardEntity>)
    
    // ========== 假人相关 ==========
    
    /**
     * 获取所有假人
     */
    @Query("SELECT * FROM fmvss_dummies ORDER BY weightKg")
    suspend fun getAllDummies(): List<FMVSSDummyEntity>
    
    /**
     * 根据代码获取假人
     */
    @Query("SELECT * FROM fmvss_dummies WHERE dummyCode = :dummyCode")
    suspend fun getDummyByCode(dummyCode: String): FMVSSDummyEntity?
    
    /**
     * 根据重量范围获取假人
     */
    @Query("SELECT * FROM fmvss_dummies WHERE weightKg BETWEEN :minWeight AND :maxWeight")
    suspend fun getDummiesByWeightRange(minWeight: Double, maxWeight: Double): List<FMVSSDummyEntity>
    
    /**
     * 插入或更新假人
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDummy(dummy: FMVSSDummyEntity)
    
    // ========== 测试配置相关 ==========
    
    /**
     * 获取所有测试配置
     */
    @Query("SELECT * FROM fmvss_test_configurations ORDER BY standardId, configId")
    suspend fun getAllTestConfigurations(): List<FMVSSTestConfigEntity>
    
    /**
     * 根据配置ID获取测试配置
     */
    @Query("SELECT * FROM fmvss_test_configurations WHERE configId = :configId")
    suspend fun getTestConfigurationById(configId: String): FMVSSTestConfigEntity?
    
    /**
     * 根据标准ID获取测试配置
     */
    @Query("SELECT * FROM fmvss_test_configurations WHERE standardId = :standardId")
    suspend fun getTestConfigurationsByStandard(standardId: String): List<FMVSSTestConfigEntity>
    
    /**
     * 插入或更新测试配置
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateTestConfiguration(config: FMVSSTestConfigEntity)
    
    // ========== 安全阈值相关 ==========
    
    /**
     * 获取所有安全阈值
     */
    @Query("SELECT * FROM fmvss_thresholds ORDER BY standardId, dummyCode")
    suspend fun getAllThresholds(): List<FMVSSThresholdEntity>
    
    /**
     * 根据假人代码获取阈值
     */
    @Query("SELECT * FROM fmvss_thresholds WHERE dummyCode = :dummyCode")
    suspend fun getThresholdsByDummy(dummyCode: String): List<FMVSSThresholdEntity>
    
    /**
     * 根据标准ID获取阈值
     */
    @Query("SELECT * FROM fmvss_thresholds WHERE standardId = :standardId")
    suspend fun getThresholdsByStandard(standardId: String): List<FMVSSThresholdEntity>
    
    /**
     * 插入或更新阈值
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateThreshold(threshold: FMVSSThresholdEntity)
    
    // ========== 测试记录相关 ==========
    
    /**
     * 获取所有测试记录
     */
    @Query("SELECT * FROM fmvss_test_records ORDER BY testDate DESC")
    suspend fun getAllTestRecords(): List<FMVSSTestRecordEntity>
    
    /**
     * 根据测试ID获取记录
     */
    @Query("SELECT * FROM fmvss_test_records WHERE testId = :testId")
    suspend fun getTestRecordById(testId: String): FMVSSTestRecordEntity?
    
    /**
     * 根据配置ID获取测试记录
     */
    @Query("SELECT * FROM fmvss_test_records WHERE configId = :configId")
    suspend fun getTestRecordsByConfig(configId: String): List<FMVSSTestRecordEntity>
    
    /**
     * 获取通过的测试记录
     */
    @Query("SELECT * FROM fmvss_test_records WHERE passed = 1 ORDER BY testDate DESC")
    suspend fun getPassedTestRecords(): List<FMVSSTestRecordEntity>
    
    /**
     * 插入测试记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestRecord(record: FMVSSTestRecordEntity)
    
    // ========== 产品测试相关 ==========
    
    /**
     * 获取所有产品测试
     */
    @Query("SELECT * FROM fmvss_product_tests ORDER BY lastUpdated DESC")
    suspend fun getAllProductTests(): List<FMVSSProductTestEntity>
    
    /**
     * 根据产品ID获取测试信息
     */
    @Query("SELECT * FROM fmvss_product_tests WHERE productId = :productId")
    suspend fun getProductTestById(productId: Long): FMVSSProductTestEntity?
    
    /**
     * 根据制造商获取产品测试
     */
    @Query("SELECT * FROM fmvss_product_tests WHERE manufacturer = :manufacturer")
    suspend fun getProductTestsByManufacturer(manufacturer: String): List<FMVSSProductTestEntity>
    
    /**
     * 获取有效的认证产品（未过期）
     */
    @Query("SELECT * FROM fmvss_product_tests WHERE expiresDate IS NULL OR expiresDate > datetime('now') ORDER BY certificationDate DESC")
    suspend fun getValidCertifications(): List<FMVSSProductTestEntity>
    
    /**
     * 插入或更新产品测试
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProductTest(productTest: FMVSSProductTestEntity)
    
    /**
     * 更新产品测试结果
     */
    @Update
    suspend fun updateProductTest(productTest: FMVSSProductTestEntity)
    
    // ========== 删除操作 ==========
    
    /**
     * 删除标准
     */
    @Query("DELETE FROM fmvss_standards WHERE standardId = :standardId")
    suspend fun deleteStandard(standardId: String)
    
    /**
     * 删除测试记录
     */
    @Query("DELETE FROM fmvss_test_records WHERE recordId = :recordId")
    suspend fun deleteTestRecord(recordId: Long)
    
    /**
     * 删除产品测试
     */
    @Query("DELETE FROM fmvss_product_tests WHERE productId = :productId")
    suspend fun deleteProductTest(productId: Long)
}

package com.childproduct.designassistant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.childproduct.designassistant.database.entity.*

/**
 * FMVSS标准数据访问对象（重构版 - 支持标准隔离）
 * 
 * 修复说明：
 * - 所有假人相关查询都强制过滤standardType = 'FMVSS_213'
 * - 新增getDummyByHeight方法，支持按身高查询FMVSS假人
 * - 新增批量插入/删除方法，支持数据库初始化
 * 
 * 使用原则：
 * - FMVSSDao只返回FMVSS标准的假人，绝不返回ECE标准假人
 * - 所有假人查询都自动过滤standardType = 'FMVSS_213'
 */
@Dao
interface FMVSSDao {
    
    // ========== 假人相关（核心修复 - 强制过滤standardType） ==========
    
    /**
     * 获取所有FMVSS假人（仅FMVSS_213标准）
     * 
     * 强制过滤：WHERE standardType = 'FMVSS_213'
     */
    @Query("SELECT * FROM crash_test_dummy WHERE standardType = 'FMVSS_213' ORDER BY minHeightCm ASC")
    suspend fun getAllDummies(): List<CrashTestDummy>

    /**
     * 根据身高获取FMVSS假人（核心方法）
     * 
     * 强制过滤：WHERE standardType = 'FMVSS_213' AND 身高匹配
     * 
     * @param heightCm 身高（cm）
     * @return 匹配的FMVSS假人
     */
    @Query("""
        SELECT * FROM crash_test_dummy 
        WHERE standardType = 'FMVSS_213' 
        AND :heightCm >= minHeightCm 
        AND :heightCm <= maxHeightCm
        LIMIT 1
    """)
    suspend fun getDummyByHeight(heightCm: Int): CrashTestDummy?

    /**
     * 根据代码获取FMVSS假人
     * 
     * 强制过滤：WHERE standardType = 'FMVSS_213' AND dummyCode = :dummyCode
     */
    @Query("SELECT * FROM crash_test_dummy WHERE dummyCode = :dummyCode AND standardType = 'FMVSS_213'")
    suspend fun getDummyByCode(dummyCode: String): CrashTestDummy?

    /**
     * 根据重量范围获取FMVSS假人
     * 
     * 强制过滤：WHERE standardType = 'FMVSS_213'
     */
    @Query("SELECT * FROM crash_test_dummy WHERE standardType = 'FMVSS_213' AND weightKg BETWEEN :minWeight AND :maxWeight ORDER BY weightKg ASC")
    suspend fun getDummiesByWeightRange(minWeight: Double, maxWeight: Double): List<CrashTestDummy>

    /**
     * 批量插入FMVSS假人（用于初始化）
     * 
     * 使用说明：插入的假人必须设置standardType = 'FMVSS_213'
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDummies(dummies: List<CrashTestDummy>)

    /**
     * 删除所有FMVSS假人（用于重新初始化）
     * 
     * 强制删除：DELETE FROM crash_test_dummy WHERE standardType = 'FMVSS_213'
     * 不会删除ECE标准假人（它们在EceR129Database中）
     */
    @Query("DELETE FROM crash_test_dummy WHERE standardType = 'FMVSS_213'")
    suspend fun deleteAllDummies()

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
    @Query("SELECT * FROM fmvss_standards WHERE standardType = :type ORDER BY standardId")
    suspend fun getStandardsByType(type: String): List<FMVSSStandardEntity>
    
    /**
     * 插入单个标准
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandard(standard: FMVSSStandardEntity)
    
    /**
     * 批量插入标准
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandards(standards: List<FMVSSStandardEntity>)

    /**
     * 更新标准
     */
    @Update
    suspend fun updateStandard(standard: FMVSSStandardEntity)

    /**
     * 删除标准
     */
    @Query("DELETE FROM fmvss_standards WHERE standardId = :standardId")
    suspend fun deleteStandard(standardId: String)
    
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
    @Query("SELECT * FROM fmvss_test_configurations WHERE standardId = :standardId ORDER BY configId")
    suspend fun getTestConfigurationsByStandard(standardId: String): List<FMVSSTestConfigEntity>

    /**
     * 根据假人代码获取测试配置
     */
    @Query("SELECT * FROM fmvss_test_configurations WHERE dummyCode = :dummyCode ORDER BY configId")
    suspend fun getTestConfigurationsByDummy(dummyCode: String): List<FMVSSTestConfigEntity>
    
    /**
     * 插入单个测试配置
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestConfig(config: FMVSSTestConfigEntity)

    /**
     * 批量插入测试配置
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestConfigs(configs: List<FMVSSTestConfigEntity>)

    /**
     * 更新测试配置
     */
    @Update
    suspend fun updateTestConfiguration(config: FMVSSTestConfigEntity)

    /**
     * 删除测试配置
     */
    @Query("DELETE FROM fmvss_test_configurations WHERE configId = :configId")
    suspend fun deleteTestConfiguration(configId: String)
    
    // ========== 安全阈值相关 ==========
    
    /**
     * 获取所有安全阈值
     */
    @Query("SELECT * FROM fmvss_thresholds ORDER BY standardId, dummyCode")
    suspend fun getAllThresholds(): List<FMVSSThresholdEntity>
    
    /**
     * 根据假人代码获取阈值（FMVSS 213b）
     * 
     * @param dummyCode 假人代码（如Q3s、HIII）
     * @return 该假人的阈值列表
     */
    @Query("SELECT * FROM fmvss_thresholds WHERE dummyCode = :dummyCode")
    suspend fun getThresholdsByDummy(dummyCode: String): List<FMVSSThresholdEntity>
    
    /**
     * 根据标准ID获取阈值
     */
    @Query("SELECT * FROM fmvss_thresholds WHERE standardId = :standardId ORDER BY dummyCode")
    suspend fun getThresholdsByStandard(standardId: String): List<FMVSSThresholdEntity>
    
    /**
     * 插入单个阈值
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThreshold(threshold: FMVSSThresholdEntity)

    /**
     * 批量插入阈值
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThresholds(thresholds: List<FMVSSThresholdEntity>)

    /**
     * 更新阈值
     */
    @Update
    suspend fun updateThreshold(threshold: FMVSSThresholdEntity)

    /**
     * 删除阈值
     */
    @Query("DELETE FROM fmvss_thresholds WHERE thresholdId = :thresholdId")
    suspend fun deleteThreshold(thresholdId: String)
    
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
    @Query("SELECT * FROM fmvss_test_records WHERE configId = :configId ORDER BY testDate DESC")
    suspend fun getTestRecordsByConfig(configId: String): List<FMVSSTestRecordEntity>
    
    /**
     * 获取通过的测试记录
     */
    @Query("SELECT * FROM fmvss_test_records WHERE passed = 1 ORDER BY testDate DESC")
    suspend fun getPassedTestRecords(): List<FMVSSTestRecordEntity>

    /**
     * 获取失败的测试记录
     */
    @Query("SELECT * FROM fmvss_test_records WHERE passed = 0 ORDER BY testDate DESC")
    suspend fun getFailedTestRecords(): List<FMVSSTestRecordEntity>
    
    /**
     * 插入测试记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestRecord(record: FMVSSTestRecordEntity)

    /**
     * 批量插入测试记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestRecords(records: List<FMVSSTestRecordEntity>)

    /**
     * 更新测试记录
     */
    @Update
    suspend fun updateTestRecord(record: FMVSSTestRecordEntity)

    /**
     * 删除测试记录
     */
    @Query("DELETE FROM fmvss_test_records WHERE testId = :testId")
    suspend fun deleteTestRecord(testId: String)
    
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
    @Query("SELECT * FROM fmvss_product_tests WHERE manufacturer = :manufacturer ORDER BY lastUpdated DESC")
    suspend fun getProductTestsByManufacturer(manufacturer: String): List<FMVSSProductTestEntity>
    
    /**
     * 获取有效的认证产品（未过期）
     */
    @Query("SELECT * FROM fmvss_product_tests WHERE expiresDate IS NULL OR expiresDate > datetime('now') ORDER BY certificationDate DESC")
    suspend fun getValidCertifications(): List<FMVSSProductTestEntity>

    /**
     * 获取已过期的认证产品
     */
    @Query("SELECT * FROM fmvss_product_tests WHERE expiresDate IS NOT NULL AND expiresDate <= datetime('now') ORDER BY expiresDate DESC")
    suspend fun getExpiredCertifications(): List<FMVSSProductTestEntity>
    
    /**
     * 插入或更新产品测试
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProductTest(productTest: FMVSSProductTestEntity)

    /**
     * 批量插入产品测试
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductTests(productTests: List<FMVSSProductTestEntity>)
    
    /**
     * 更新产品测试结果
     */
    @Update
    suspend fun updateProductTest(productTest: FMVSSProductTestEntity)

    /**
     * 删除产品测试
     */
    @Query("DELETE FROM fmvss_product_tests WHERE productId = :productId")
    suspend fun deleteProductTest(productId: Long)

    /**
     * 清空所有产品测试数据
     */
    @Query("DELETE FROM fmvss_product_tests")
    suspend fun clearAllProductTests()
}

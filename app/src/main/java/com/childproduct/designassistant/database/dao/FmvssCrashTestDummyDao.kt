package com.childproduct.designassistant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.childproduct.designassistant.database.entity.FmvssCrashTestDummy
import com.childproduct.designassistant.model.InstallDirection
import kotlinx.coroutines.flow.Flow

/**
 * FMVSS 213 专属假人数据访问对象
 * 物理隔离：仅查询fmvss_crash_test_dummy表，不涉及ECE数据
 */
@Dao
interface FmvssCrashTestDummyDao {
    /**
     * 插入FMVSS假人数据
     * 避免冲突：如果已存在则替换
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dummy: FmvssCrashTestDummy)

    /**
     * 批量插入FMVSS假人数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dummies: List<FmvssCrashTestDummy>)

    /**
     * 根据假人代码查询FMVSS假人
     * 纯FMVSS查询：仅查询fmvss_crash_test_dummy表
     */
    @Query("SELECT * FROM fmvss_crash_test_dummy WHERE dummyCode = :dummyCode LIMIT 1")
    suspend fun getByDummyCode(dummyCode: String): FmvssCrashTestDummy?

    /**
     * 根据FMVSS测试标准查询假人
     */
    @Query("SELECT * FROM fmvss_crash_test_dummy WHERE fmvssTestStandard = :standard ORDER BY minHeightIn")
    suspend fun getByStandard(standard: String): List<FmvssCrashTestDummy>

    /**
     * 根据安装方向查询FMVSS假人
     */
    @Query("SELECT * FROM fmvss_crash_test_dummy WHERE installDirection = :installDirection ORDER BY minHeightIn")
    suspend fun getByInstallDirection(installDirection: InstallDirection): List<FmvssCrashTestDummy>

    /**
     * 根据身高范围查询FMVSS假人（英寸）
     * 返回所有匹配该身高范围的FMVSS假人
     */
    @Query("SELECT * FROM fmvss_crash_test_dummy WHERE :heightIn >= minHeightIn AND :heightIn <= maxHeightIn ORDER BY minHeightIn")
    suspend fun getByHeightInches(heightIn: Int): List<FmvssCrashTestDummy>

    /**
     * 查询所有FMVSS假人
     */
    @Query("SELECT * FROM fmvss_crash_test_dummy ORDER BY minHeightIn")
    suspend fun getAll(): List<FmvssCrashTestDummy>

    /**
     * Flow方式查询所有FMVSS假人（响应式）
     */
    @Query("SELECT * FROM fmvss_crash_test_dummy ORDER BY minHeightIn")
    fun getAllFlow(): Flow<List<FmvssCrashTestDummy>>

    /**
     * 更新FMVSS假人数据
     */
    @Update
    suspend fun update(dummy: FmvssCrashTestDummy)

    /**
     * 删除指定假人
     */
    @Query("DELETE FROM fmvss_crash_test_dummy WHERE dummyId = :dummyId")
    suspend fun deleteById(dummyId: String)

    /**
     * 清空所有FMVSS假人数据
     */
    @Query("DELETE FROM fmvss_crash_test_dummy")
    suspend fun deleteAll()

    /**
     * 获取FMVSS假人数量
     */
    @Query("SELECT COUNT(*) FROM fmvss_crash_test_dummy")
    suspend fun getCount(): Int

    /**
     * 验证数据完整性：检查是否有不符合FMVSS标准的假人
     */
    @Query("SELECT dummyCode FROM fmvss_crash_test_dummy WHERE dummyCode NOT IN ('Q3s', '3y', '6y', '10y')")
    suspend fun getInvalidFmvssDummies(): List<String>

    /**
     * 查询最近更新的FMVSS假人
     */
    @Query("SELECT * FROM fmvss_crash_test_dummy ORDER BY updatedAt DESC LIMIT :limit")
    suspend fun getRecentlyUpdated(limit: Int): List<FmvssCrashTestDummy>

    /**
     * 根据年龄范围查询FMVSS假人
     */
    @Query("SELECT * FROM fmvss_crash_test_dummy WHERE ageRange = :ageRange")
    suspend fun getByAgeRange(ageRange: String): List<FmvssCrashTestDummy>

    /**
     * 根据侧碰测试要求查询假人
     */
    @Query("SELECT * FROM fmvss_crash_test_dummy WHERE fmvssTestStandard LIKE '%213a%'")
    suspend fun getBySideImpact(): List<FmvssCrashTestDummy>

    /**
     * 根据正碰测试要求查询假人
     */
    @Query("SELECT * FROM fmvss_crash_test_dummy WHERE fmvssTestStandard = 'FMVSS 213'")
    suspend fun getByFrontalImpact(): List<FmvssCrashTestDummy>

    /**
     * 初始化FMVSS标准假人数据
     */
    suspend fun initializeFmvssDummies() {
        insertAll(listOf(
            FmvssCrashTestDummy.createQ3s(),
            FmvssCrashTestDummy.create3yrHybridIII(),
            FmvssCrashTestDummy.create6yrHybridIII(),
            FmvssCrashTestDummy.create10yrHybridIII()
        ))
    }

    /**
     * 获取侧碰测试假人（Q3s）
     */
    @Query("SELECT * FROM fmvss_crash_test_dummy WHERE dummyCode = 'Q3s' LIMIT 1")
    suspend fun getSideImpactDummy(): FmvssCrashTestDummy?

    /**
     * 获取指定碰撞速度的假人
     */
    @Query("SELECT * FROM fmvss_crash_test_dummy WHERE fmvssImpactVelocityMph = :velocityMph ORDER BY minHeightIn")
    suspend fun getByImpactVelocity(velocityMph: Double): List<FmvssCrashTestDummy>
}

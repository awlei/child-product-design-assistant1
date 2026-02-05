package com.childproduct.designassistant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.childproduct.designassistant.database.entity.EceCrashTestDummy
import com.childproduct.designassistant.model.InstallDirection
import kotlinx.coroutines.flow.Flow

/**
 * ECE R129 专属假人数据访问对象
 * 物理隔离：仅查询ece_crash_test_dummy表，不涉及FMVSS数据
 */
@Dao
interface EceCrashTestDummyDao {
    /**
     * 插入ECE假人数据
     * 避免冲突：如果已存在则替换
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dummy: EceCrashTestDummy)

    /**
     * 批量插入ECE假人数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dummies: List<EceCrashTestDummy>)

    /**
     * 根据假人代码查询ECE假人
     * 纯ECE查询：仅查询ece_crash_test_dummy表
     */
    @Query("SELECT * FROM ece_crash_test_dummy WHERE dummyCode = :dummyCode LIMIT 1")
    suspend fun getByDummyCode(dummyCode: String): EceCrashTestDummy?

    /**
     * 根据产品组查询ECE假人
     */
    @Query("SELECT * FROM ece_crash_test_dummy WHERE productGroup = :productGroup ORDER BY minHeightCm")
    suspend fun getByProductGroup(productGroup: String): List<EceCrashTestDummy>

    /**
     * 根据安装方向查询ECE假人
     */
    @Query("SELECT * FROM ece_crash_test_dummy WHERE installDirection = :installDirection ORDER BY minHeightCm")
    suspend fun getByInstallDirection(installDirection: InstallDirection): List<EceCrashTestDummy>

    /**
     * 根据身高范围查询ECE假人
     * 返回所有匹配该身高范围的ECE假人
     */
    @Query("SELECT * FROM ece_crash_test_dummy WHERE :heightCm >= minHeightCm AND :heightCm <= maxHeightCm ORDER BY minHeightCm")
    suspend fun getByHeight(heightCm: Int): List<EceCrashTestDummy>

    /**
     * 查询所有ECE假人
     */
    @Query("SELECT * FROM ece_crash_test_dummy ORDER BY minHeightCm")
    suspend fun getAll(): List<EceCrashTestDummy>

    /**
     * Flow方式查询所有ECE假人（响应式）
     */
    @Query("SELECT * FROM ece_crash_test_dummy ORDER BY minHeightCm")
    fun getAllFlow(): Flow<List<EceCrashTestDummy>>

    /**
     * 更新ECE假人数据
     */
    @Update
    suspend fun update(dummy: EceCrashTestDummy)

    /**
     * 删除指定假人
     */
    @Query("DELETE FROM ece_crash_test_dummy WHERE dummyId = :dummyId")
    suspend fun deleteById(dummyId: String)

    /**
     * 清空所有ECE假人数据
     */
    @Query("DELETE FROM ece_crash_test_dummy")
    suspend fun deleteAll()

    /**
     * 获取ECE假人数量
     */
    @Query("SELECT COUNT(*) FROM ece_crash_test_dummy")
    suspend fun getCount(): Int

    /**
     * 验证数据完整性：检查是否有不符合UN R129标准的假人
     */
    @Query("SELECT dummyCode FROM ece_crash_test_dummy WHERE dummyCode NOT IN ('Q0', 'Q0+', 'Q1', 'Q1.5', 'Q3', 'Q6', 'Q10')")
    suspend fun getInvalidEceDummies(): List<String>

    /**
     * 查询最近更新的ECE假人
     */
    @Query("SELECT * FROM ece_crash_test_dummy ORDER BY updatedAt DESC LIMIT :limit")
    suspend fun getRecentlyUpdated(limit: Int): List<EceCrashTestDummy>

    /**
     * 根据年龄范围查询ECE假人
     */
    @Query("SELECT * FROM ece_crash_test_dummy WHERE ageRange = :ageRange")
    suspend fun getByAgeRange(ageRange: String): List<EceCrashTestDummy>

    /**
     * 初始化ECE标准假人数据
     */
    suspend fun initializeEceDummies() {
        insertAll(listOf(
            EceCrashTestDummy.createQ0(),
            EceCrashTestDummy.createQ1(),
            EceCrashTestDummy.createQ3()
        ))
    }
}

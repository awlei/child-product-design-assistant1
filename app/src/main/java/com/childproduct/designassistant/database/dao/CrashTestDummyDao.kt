package com.childproduct.designassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.childproduct.designassistant.database.entity.CrashTestDummy

/**
 * 假人类型DAO（重构版 - 支持标准隔离）
 * 
 * 修复说明：
 * - 所有查询方法必须按standardType过滤，杜绝标准混用
 * - getDummyByHeightRange已废弃，使用getDummyByStandardAndHeight替代
 * - 新增getDummiesByStandard方法，强制按标准查询
 * 
 * 使用建议：
 * - 优先使用带standardType参数的方法（如getDummyByStandardAndHeight）
 * - 避免使用已废弃的方法（如getDummyByHeightRange）
 */
@Dao
interface CrashTestDummyDao {

    // ========== 核心查询方法（推荐使用） ==========

    /**
     * 按「标准+身高」查询假人（核心方法 - 彻底隔离）
     * 
     * @param standardType 标准类型（必须）：ECE_R129 / FMVSS_213 / GB_27887_2024
     * @param heightCm 身高（cm）
     * @return 匹配的假人，如果没有匹配返回null
     */
    @Query("""
        SELECT * FROM crash_test_dummy 
        WHERE standardType = :standardType 
        AND :heightCm >= minHeightCm 
        AND :heightCm <= maxHeightCm
        LIMIT 1
    """)
    suspend fun getDummyByStandardAndHeight(
        standardType: String,
        heightCm: Int
    ): CrashTestDummy?

    /**
     * 按标准类型查询所有假人
     * 
     * @param standardType 标准类型：ECE_R129 / FMVSS_213 / GB_27887_2024
     * @return 该标准的所有假人列表
     */
    @Query("SELECT * FROM crash_test_dummy WHERE standardType = :standardType ORDER BY minHeightCm ASC")
    suspend fun getDummiesByStandard(standardType: String): List<CrashTestDummy>

    /**
     * 按标准类型查询所有假人（LiveData版本）
     * 
     * @param standardType 标准类型：ECE_R129 / FMVSS_213 / GB_27887_2024
     * @return 该标准的所有假人列表（LiveData）
     */
    @Query("SELECT * FROM crash_test_dummy WHERE standardType = :standardType ORDER BY minHeightCm ASC")
    fun getDummiesByStandardLiveData(standardType: String): LiveData<List<CrashTestDummy>>

    /**
     * 按标准+安装方向查询假人
     * 
     * @param standardType 标准类型
     * @param direction 安装方向：REARWARD / FORWARD
     * @return 匹配的假人列表
     */
    @Query("SELECT * FROM crash_test_dummy WHERE standardType = :standardType AND installDirection = :direction ORDER BY minHeightCm ASC")
    suspend fun getDummiesByStandardAndDirection(standardType: String, direction: String): List<CrashTestDummy>

    // ========== 基础查询方法（通用，但返回所有标准的数据，谨慎使用） ==========

    /**
     * 获取所有假人（包含所有标准）
     * 
     * ⚠️ 警告：此方法返回所有标准的假人，可能导致标准混用
     * 💡 推荐：使用getDummiesByStandard(standardType)替代
     * 
     * @return 所有假人列表（包含ECE、FMVSS等所有标准）
     */
    @Deprecated("建议使用getDummiesByStandard(standardType)以避免标准混用")
    @Query("SELECT * FROM crash_test_dummy ORDER BY minHeightCm ASC")
    fun getAllDummies(): LiveData<List<CrashTestDummy>>

    @Deprecated("建议使用getDummiesByStandard(standardType)以避免标准混用")
    @Query("SELECT * FROM crash_test_dummy ORDER BY minHeightCm ASC")
    suspend fun getAllDummiesList(): List<CrashTestDummy>

    /**
     * 按ID查询假人
     * 
     * @param dummyId 假人ID
     * @return 假人对象
     */
    @Query("SELECT * FROM crash_test_dummy WHERE dummyId = :dummyId")
    fun getDummyById(dummyId: String): LiveData<CrashTestDummy?>

    /**
     * 按假人代码查询（跨标准查询）
     * 
     * ⚠️ 警告：不同标准可能有相同的假人代码（如Q3），此方法可能返回多个结果
     * 💡 推荐：使用getDummiesByStandard(standardType) + 代码过滤
     * 
     * @param dummyCode 假人代码（如Q0, Q1, Q3）
     * @return 假人对象（可能有多个标准版本）
     */
    @Deprecated("建议使用getDummiesByStandard(standardType)以避免标准混用")
    @Query("SELECT * FROM crash_test_dummy WHERE dummyCode = :dummyCode")
    fun getDummyByCode(dummyCode: String): LiveData<CrashTestDummy?>

    /**
     * 按身高查询假人（跨标准查询 - 已废弃）
     * 
     * ❌ 废弃原因：此方法未按standardType过滤，会返回所有标准的匹配假人
     * ✅ 替代方法：getDummyByStandardAndHeight(standardType, heightCm)
     * 
     * @param heightCm 身高（cm）
     * @return 匹配的假人（可能来自不同标准）
     */
    @Deprecated("请使用getDummyByStandardAndHeight(standardType, heightCm)以避免标准混用")
    @Query("""
        SELECT * FROM crash_test_dummy 
        WHERE :heightCm >= minHeightCm 
        AND :heightCm <= maxHeightCm
        LIMIT 1
    """)
    suspend fun getDummyByHeightRange(heightCm: Int): CrashTestDummy?

    // ========== 数据操作方法 ==========

    /**
     * 插入单个假人
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dummy: CrashTestDummy)

    /**
     * 批量插入假人
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dummies: List<CrashTestDummy>)

    /**
     * 更新假人
     */
    @Update
    suspend fun update(dummy: CrashTestDummy)

    /**
     * 删除单个假人
     */
    @Delete
    suspend fun delete(dummy: CrashTestDummy)

    /**
     * 删除所有假人
     */
    @Query("DELETE FROM crash_test_dummy")
    suspend fun deleteAll()

    // ========== 辅助查询方法 ==========

    /**
     * 按标准查询假人总数
     * 
     * @param standardType 标准类型
     * @return 假人数量
     */
    @Query("SELECT COUNT(*) FROM crash_test_dummy WHERE standardType = :standardType")
    suspend fun getCountByStandard(standardType: String): Int

    /**
     * 按标准+代码查询假人（精确匹配）
     * 
     * @param standardType 标准类型
     * @param dummyCode 假人代码
     * @return 匹配的假人
     */
    @Query("SELECT * FROM crash_test_dummy WHERE standardType = :standardType AND dummyCode = :dummyCode LIMIT 1")
    suspend fun getDummyByStandardAndCode(standardType: String, dummyCode: String): CrashTestDummy?

    /**
     * 按标准+身高范围查询所有匹配的假人
     * 
     * @param standardType 标准类型
     * @param minHeightCm 最小身高
     * @param maxHeightCm 最大身高
     * @return 匹配的假人列表
     */
    @Query("""
        SELECT * FROM crash_test_dummy 
        WHERE standardType = :standardType 
        AND minHeightCm >= :minHeightCm 
        AND maxHeightCm <= :maxHeightCm
        ORDER BY minHeightCm ASC
    """)
    suspend fun getDummiesByStandardAndHeightRange(
        standardType: String,
        minHeightCm: Int,
        maxHeightCm: Int
    ): List<CrashTestDummy>

    // ========== 向后兼容方法（仅用于测试，生产环境不推荐使用） ==========

    /**
     * 按安装方向查询假人（跨标准）
     * 
     * ⚠️ 警告：此方法返回所有标准的假人，可能导致标准混用
     * 💡 推荐：使用getDummiesByStandardAndDirection(standardType, direction)
     * 
     * @param direction 安装方向
     * @return 匹配的假人列表
     */
    @Deprecated("建议使用getDummiesByStandardAndDirection(standardType, direction)")
    @Query("SELECT * FROM crash_test_dummy WHERE installDirection = :direction ORDER BY minHeightCm ASC")
    fun getDummiesByInstallDirection(direction: String): LiveData<List<CrashTestDummy>>
}

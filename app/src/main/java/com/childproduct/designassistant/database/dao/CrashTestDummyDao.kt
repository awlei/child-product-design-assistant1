package com.childproduct.designassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.childproduct.designassistant.database.entity.CrashTestDummy

/**
 * 假人类型DAO
 */
@Dao
interface CrashTestDummyDao {

    @Query("SELECT * FROM crash_test_dummy ORDER BY minHeightCm ASC")
    fun getAllDummies(): LiveData<List<CrashTestDummy>>

    @Query("SELECT * FROM crash_test_dummy ORDER BY minHeightCm ASC")
    suspend fun getAllDummiesList(): List<CrashTestDummy>

    @Query("SELECT * FROM crash_test_dummy WHERE dummyId = :dummyId")
    fun getDummyById(dummyId: String): LiveData<CrashTestDummy?>

    @Query("SELECT * FROM crash_test_dummy WHERE dummyCode = :dummyCode")
    fun getDummyByCode(dummyCode: String): LiveData<CrashTestDummy?>

    @Query("""
        SELECT * FROM crash_test_dummy 
        WHERE :heightCm >= minHeightCm AND :heightCm <= maxHeightCm
        LIMIT 1
    """)
    suspend fun getDummyByHeightRange(heightCm: Int): CrashTestDummy?

    @Query("SELECT * FROM crash_test_dummy WHERE installDirection = :direction ORDER BY minHeightCm ASC")
    fun getDummiesByInstallDirection(direction: String): LiveData<List<CrashTestDummy>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dummy: CrashTestDummy)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dummies: List<CrashTestDummy>)

    @Update
    suspend fun update(dummy: CrashTestDummy)

    @Delete
    suspend fun delete(dummy: CrashTestDummy)

    @Query("DELETE FROM crash_test_dummy")
    suspend fun deleteAll()
}

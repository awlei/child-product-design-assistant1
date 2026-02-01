package com.childproduct.designassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.childproduct.designassistant.database.entity.SafetyThreshold

/**
 * 安全阈值DAO
 */
@Dao
interface SafetyThresholdDao {

    @Query("SELECT * FROM safety_threshold ORDER BY testItem, parameterName")
    fun getAllThresholds(): LiveData<List<SafetyThreshold>>

    @Query("SELECT * FROM safety_threshold ORDER BY testItem, parameterName")
    suspend fun getAllThresholdsList(): List<SafetyThreshold>

    @Query("SELECT * FROM safety_threshold WHERE thresholdId = :thresholdId")
    fun getThresholdById(thresholdId: String): LiveData<SafetyThreshold?>

    @Query("SELECT * FROM safety_threshold WHERE dummyId = :dummyId ORDER BY testItem")
    fun getThresholdsByDummy(dummyId: String): LiveData<List<SafetyThreshold>>

    @Query("SELECT * FROM safety_threshold WHERE testItem = :testItem ORDER BY parameterName")
    fun getThresholdsByTestItem(testItem: String): LiveData<List<SafetyThreshold>>

    @Query("SELECT * FROM safety_threshold WHERE applicableDummies LIKE '%' || :dummyCode || '%'")
    suspend fun getThresholdsApplicableToDummy(dummyCode: String): List<SafetyThreshold>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(threshold: SafetyThreshold)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(thresholds: List<SafetyThreshold>)

    @Update
    suspend fun update(threshold: SafetyThreshold)

    @Delete
    suspend fun delete(threshold: SafetyThreshold)

    @Query("DELETE FROM safety_threshold")
    suspend fun deleteAll()
}

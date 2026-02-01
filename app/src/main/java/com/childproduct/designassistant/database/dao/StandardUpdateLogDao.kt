package com.childproduct.designassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.childproduct.designassistant.database.entity.StandardUpdateLog

/**
 * 标准更新日志DAO
 */
@Dao
interface StandardUpdateLogDao {

    @Query("SELECT * FROM standard_update_log ORDER BY timestamp DESC LIMIT 20")
    fun getRecentLogs(): LiveData<List<StandardUpdateLog>>

    @Query("SELECT * FROM standard_update_log ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastLog(): StandardUpdateLog?

    @Query("SELECT timestamp FROM standard_update_log WHERE regulationNumber = :regulationNumber AND status = 'SUCCESS' ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastSyncTime(regulationNumber: String = "UN R129"): Long?

    @Query("SELECT * FROM standard_update_log WHERE regulationNumber = :regulationNumber ORDER BY timestamp DESC LIMIT 10")
    fun getLogsByRegulation(regulationNumber: String): LiveData<List<StandardUpdateLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: StandardUpdateLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<StandardUpdateLog>)

    @Delete
    suspend fun delete(log: StandardUpdateLog)

    @Query("DELETE FROM standard_update_log")
    suspend fun deleteAll()
}

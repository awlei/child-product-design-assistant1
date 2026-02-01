package com.childproduct.designassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.childproduct.designassistant.database.entity.StandardReference

/**
 * 标准引用DAO
 */
@Dao
interface StandardReferenceDao {

    @Query("SELECT * FROM standard_reference ORDER BY regulationNumber")
    fun getAllStandards(): LiveData<List<StandardReference>>

    @Query("SELECT * FROM standard_reference ORDER BY regulationNumber")
    suspend fun getAllStandardsList(): List<StandardReference>

    @Query("SELECT * FROM standard_reference WHERE referenceId = :referenceId")
    fun getStandardById(referenceId: String): LiveData<StandardReference?>

    @Query("SELECT * FROM standard_reference WHERE regulationNumber = :regulationNumber")
    fun getStandardByNumber(regulationNumber: String): LiveData<StandardReference?>

    @Query("SELECT currentVersion FROM standard_reference WHERE regulationNumber = :regulationNumber LIMIT 1")
    suspend fun getCurrentVersion(regulationNumber: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reference: StandardReference)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(references: List<StandardReference>)

    @Update
    suspend fun update(reference: StandardReference)

    @Delete
    suspend fun delete(reference: StandardReference)

    @Query("DELETE FROM standard_reference")
    suspend fun deleteAll()
}

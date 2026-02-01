package com.childproduct.designassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.childproduct.designassistant.database.entity.InstallationMethod

/**
 * 安装方式DAO
 */
@Dao
interface InstallationMethodDao {

    @Query("SELECT * FROM installation_method ORDER BY methodName")
    fun getAllMethods(): LiveData<List<InstallationMethod>>

    @Query("SELECT * FROM installation_method ORDER BY methodName")
    suspend fun getAllMethodsList(): List<InstallationMethod>

    @Query("SELECT * FROM installation_method WHERE methodId = :methodId")
    fun getMethodById(methodId: String): LiveData<InstallationMethod?>

    @Query("SELECT * FROM installation_method WHERE methodCode = :methodCode")
    fun getMethodByCode(methodCode: String): LiveData<InstallationMethod?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(method: InstallationMethod)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(methods: List<InstallationMethod>)

    @Update
    suspend fun update(method: InstallationMethod)

    @Delete
    suspend fun delete(method: InstallationMethod)

    @Query("DELETE FROM installation_method")
    suspend fun deleteAll()
}

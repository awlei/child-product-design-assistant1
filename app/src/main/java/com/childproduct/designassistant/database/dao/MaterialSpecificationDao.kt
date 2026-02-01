package com.childproduct.designassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.childproduct.designassistant.database.entity.MaterialSpecification

/**
 * 材料规格DAO
 */
@Dao
interface MaterialSpecificationDao {

    @Query("SELECT * FROM material_specification ORDER BY specName")
    fun getAllSpecifications(): LiveData<List<MaterialSpecification>>

    @Query("SELECT * FROM material_specification ORDER BY specName")
    suspend fun getAllSpecificationsList(): List<MaterialSpecification>

    @Query("SELECT * FROM material_specification WHERE specId = :specId")
    fun getSpecificationById(specId: String): LiveData<MaterialSpecification?>

    @Query("SELECT * FROM material_specification WHERE specCode = :specCode")
    fun getSpecificationByCode(specCode: String): LiveData<MaterialSpecification?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(spec: MaterialSpecification)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(specs: List<MaterialSpecification>)

    @Update
    suspend fun update(spec: MaterialSpecification)

    @Delete
    suspend fun delete(spec: MaterialSpecification)

    @Query("DELETE FROM material_specification")
    suspend fun deleteAll()
}

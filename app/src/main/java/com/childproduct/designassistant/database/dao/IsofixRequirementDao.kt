package com.childproduct.designassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.childproduct.designassistant.database.entity.IsofixRequirement

/**
 * ISOFIX要求DAO
 */
@Dao
interface IsofixRequirementDao {

    @Query("SELECT * FROM isofix_requirement ORDER BY sizeCategory")
    fun getAllRequirements(): LiveData<List<IsofixRequirement>>

    @Query("SELECT * FROM isofix_requirement ORDER BY sizeCategory")
    suspend fun getAllRequirementsList(): List<IsofixRequirement>

    @Query("SELECT * FROM isofix_requirement WHERE requirementId = :requirementId")
    fun getRequirementById(requirementId: String): LiveData<IsofixRequirement?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(requirement: IsofixRequirement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(requirements: List<IsofixRequirement>)

    @Update
    suspend fun update(requirement: IsofixRequirement)

    @Delete
    suspend fun delete(requirement: IsofixRequirement)

    @Query("DELETE FROM isofix_requirement")
    suspend fun deleteAll()
}

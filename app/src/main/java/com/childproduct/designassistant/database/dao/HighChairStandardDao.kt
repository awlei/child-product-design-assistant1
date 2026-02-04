package com.childproduct.designassistant.database.dao

import androidx.room.*
import com.childproduct.designassistant.database.entity.*
import kotlinx.coroutines.flow.Flow

/**
 * 儿童高脚椅标准 DAO
 */
@Dao
interface HighChairStandardDao {
    
    @Query("SELECT * FROM high_chair_standard WHERE status = 'ACTIVE'")
    fun getAllActiveStandards(): Flow<List<HighChairStandard>>

    @Query("SELECT * FROM high_chair_standard WHERE standardId = :standardId")
    suspend fun getStandardById(standardId: String): HighChairStandard?

    @Query("SELECT * FROM high_chair_standard WHERE region = :region AND status = 'ACTIVE'")
    fun getStandardsByRegion(region: String): Flow<List<HighChairStandard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandard(standard: HighChairStandard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandards(standards: List<HighChairStandard>)

    @Update
    suspend fun updateStandard(standard: HighChairStandard)

    @Delete
    suspend fun deleteStandard(standard: HighChairStandard)
}

/**
 * 儿童高脚椅年龄段 DAO
 */
@Dao
interface HighChairAgeGroupDao {
    
    @Query("SELECT * FROM high_chair_age_group WHERE standardId = :standardId")
    fun getAgeGroupsByStandard(standardId: String): Flow<List<HighChairAgeGroup>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgeGroup(ageGroup: HighChairAgeGroup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgeGroups(ageGroups: List<HighChairAgeGroup>)

    @Delete
    suspend fun deleteAgeGroup(ageGroup: HighChairAgeGroup)
}

/**
 * 儿童高脚椅安全要求 DAO
 */
@Dao
interface HighChairSafetyRequirementDao {
    
    @Query("SELECT * FROM high_chair_safety_requirement WHERE standardId = :standardId")
    fun getRequirementsByStandard(standardId: String): Flow<List<HighChairSafetyRequirement>>

    @Query("SELECT * FROM high_chair_safety_requirement WHERE standardId = :standardId AND category = :category")
    fun getRequirementsByCategory(standardId: String, category: String): Flow<List<HighChairSafetyRequirement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequirement(requirement: HighChairSafetyRequirement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequirements(requirements: List<HighChairSafetyRequirement>)
}

/**
 * 儿童高脚椅稳定性 DAO
 */
@Dao
interface HighChairStabilityDao {
    
    @Query("SELECT * FROM high_chair_stability WHERE standardId = :standardId")
    fun getStabilityByStandard(standardId: String): Flow<List<HighChairStability>>

    @Query("SELECT * FROM high_chair_stability WHERE stabilityId = :stabilityId")
    suspend fun getStabilityById(stabilityId: String): HighChairStability?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStability(stability: HighChairStability)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStabilities(stabilities: List<HighChairStability>)
}

/**
 * 儿童高脚椅约束系统 DAO
 */
@Dao
interface HighChairRestraintDao {
    
    @Query("SELECT * FROM high_chair_restraint WHERE standardId = :standardId")
    fun getRestraintsByStandard(standardId: String): Flow<List<HighChairRestraint>>

    @Query("SELECT * FROM high_chair_restraint WHERE restraintType = :type")
    fun getRestraintsByType(type: String): Flow<List<HighChairRestraint>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestraint(restraint: HighChairRestraint)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestraints(restraints: List<HighChairRestraint>)
}

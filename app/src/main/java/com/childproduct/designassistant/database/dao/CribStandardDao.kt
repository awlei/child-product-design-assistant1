package com.childproduct.designassistant.database.dao

import androidx.room.*
import com.childproduct.designassistant.database.entity.*
import kotlinx.coroutines.flow.Flow

/**
 * 儿童床标准 DAO
 */
@Dao
interface CribStandardDao {
    
    @Query("SELECT * FROM crib_standard WHERE status = 'ACTIVE'")
    fun getAllActiveStandards(): Flow<List<CribStandard>>

    @Query("SELECT * FROM crib_standard WHERE standardId = :standardId")
    suspend fun getStandardById(standardId: String): CribStandard?

    @Query("SELECT * FROM crib_standard WHERE region = :region AND status = 'ACTIVE'")
    fun getStandardsByRegion(region: String): Flow<List<CribStandard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandard(standard: CribStandard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandards(standards: List<CribStandard>)

    @Update
    suspend fun updateStandard(standard: CribStandard)

    @Delete
    suspend fun deleteStandard(standard: CribStandard)
}

/**
 * 儿童床尺寸要求 DAO
 */
@Dao
interface CribDimensionDao {
    
    @Query("SELECT * FROM crib_dimension WHERE standardId = :standardId")
    fun getDimensionsByStandard(standardId: String): Flow<List<CribDimension>>

    @Query("SELECT * FROM crib_dimension WHERE standardId = :standardId AND dimensionType = :type")
    fun getDimensionsByType(standardId: String, type: String): Flow<List<CribDimension>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDimension(dimension: CribDimension)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDimensions(dimensions: List<CribDimension>)
}

/**
 * 儿童床床垫间隙 DAO
 */
@Dao
interface CribMattressGapDao {
    
    @Query("SELECT * FROM crib_mattress_gap WHERE standardId = :standardId")
    fun getGapsByStandard(standardId: String): Flow<List<CribMattressGap>>

    @Query("SELECT * FROM crib_mattress_gap WHERE standardId = :standardId AND location = :location")
    fun getGapsByLocation(standardId: String, location: String): Flow<List<CribMattressGap>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGap(gap: CribMattressGap)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGaps(gaps: List<CribMattressGap>)
}

/**
 * 儿童床栏杆 DAO
 */
@Dao
interface CribRailingDao {
    
    @Query("SELECT * FROM crib_railing WHERE standardId = :standardId")
    fun getRailingsByStandard(standardId: String): Flow<List<CribRailing>>

    @Query("SELECT * FROM crib_railing WHERE standardId = :standardId AND railingType = :type")
    fun getRailingsByType(standardId: String, type: String): Flow<List<CribRailing>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRailing(railing: CribRailing)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRailings(railings: List<CribRailing>)
}

/**
 * 儿童床安全要求 DAO
 */
@Dao
interface CribSafetyRequirementDao {
    
    @Query("SELECT * FROM crib_safety_requirement WHERE standardId = :standardId")
    fun getRequirementsByStandard(standardId: String): Flow<List<CribSafetyRequirement>>

    @Query("SELECT * FROM crib_safety_requirement WHERE standardId = :standardId AND category = :category")
    fun getRequirementsByCategory(standardId: String, category: String): Flow<List<CribSafetyRequirement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequirement(requirement: CribSafetyRequirement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequirements(requirements: List<CribSafetyRequirement>)
}

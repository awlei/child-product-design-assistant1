package com.childproduct.designassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.childproduct.designassistant.database.entity.HeightRangeMapping

/**
 * 身高范围映射DAO
 */
@Dao
interface HeightRangeMappingDao {

    @Query("SELECT * FROM height_range_mapping ORDER BY heightRangeStartCm ASC")
    fun getAllMappings(): LiveData<List<HeightRangeMapping>>

    @Query("SELECT * FROM height_range_mapping ORDER BY heightRangeStartCm ASC")
    suspend fun getAllMappingsList(): List<HeightRangeMapping>

    @Query("SELECT * FROM height_range_mapping WHERE mappingId = :mappingId")
    fun getMappingById(mappingId: String): LiveData<HeightRangeMapping?>

    @Query("""
        SELECT * FROM height_range_mapping 
        WHERE :heightCm >= heightRangeStartCm AND :heightCm <= heightRangeEndCm
        LIMIT 1
    """)
    suspend fun getMappingByHeight(heightCm: Int): HeightRangeMapping?

    @Query("SELECT * FROM height_range_mapping WHERE dummyId = :dummyId")
    fun getMappingsByDummyId(dummyId: String): LiveData<List<HeightRangeMapping>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mapping: HeightRangeMapping)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mappings: List<HeightRangeMapping>)

    @Update
    suspend fun update(mapping: HeightRangeMapping)

    @Delete
    suspend fun delete(mapping: HeightRangeMapping)

    @Query("DELETE FROM height_range_mapping")
    suspend fun deleteAll()
}

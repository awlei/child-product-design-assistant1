package com.childproduct.designassistant.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.childproduct.designassistant.database.entity.TestConfiguration

/**
 * 测试配置DAO
 */
@Dao
interface TestConfigurationDao {

    @Query("SELECT * FROM test_configuration ORDER BY dummyId, pulseType, installDirection")
    fun getAllConfigurations(): LiveData<List<TestConfiguration>>

    @Query("SELECT * FROM test_configuration ORDER BY dummyId, pulseType, installDirection")
    suspend fun getAllConfigurationsList(): List<TestConfiguration>

    @Query("SELECT * FROM test_configuration WHERE configId = :configId")
    fun getConfigurationById(configId: String): LiveData<TestConfiguration?>

    @Query("SELECT * FROM test_configuration WHERE dummyId = :dummyId ORDER BY pulseType, installDirection")
    fun getConfigurationsByDummyId(dummyId: String): LiveData<List<TestConfiguration>>

    @Query("""
        SELECT tc.* FROM test_configuration tc
        INNER JOIN crash_test_dummy ctd ON tc.dummyId = ctd.dummyId
        WHERE ctd.dummyCode = :dummyCode 
        AND tc.installDirection = :installDirection
        ORDER BY tc.pulseType
    """)
    fun getConfigurationsByDummyAndDirection(dummyCode: String, installDirection: String): LiveData<List<TestConfiguration>>

    @Query("SELECT * FROM test_configuration WHERE pulseType = :pulseType ORDER BY dummyId")
    fun getConfigurationsByPulseType(pulseType: String): LiveData<List<TestConfiguration>>

    @Query("SELECT * FROM test_configuration WHERE installDirection = :installDirection ORDER BY dummyId, pulseType")
    fun getConfigurationsByInstallDirection(installDirection: String): LiveData<List<TestConfiguration>>

    @Query("SELECT * FROM test_configuration WHERE standardReference = :standardReference ORDER BY dummyId")
    fun getConfigurationsByStandard(standardReference: String): LiveData<List<TestConfiguration>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(configuration: TestConfiguration)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(configurations: List<TestConfiguration>)

    @Update
    suspend fun update(configuration: TestConfiguration)

    @Delete
    suspend fun delete(configuration: TestConfiguration)

    @Query("DELETE FROM test_configuration")
    suspend fun deleteAll()
}

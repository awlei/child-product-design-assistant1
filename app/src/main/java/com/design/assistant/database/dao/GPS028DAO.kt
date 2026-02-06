package com.design.assistant.database.dao

import androidx.room.*
import com.design.assistant.database.entities.GPS028Entity
import com.design.assistant.model.GPS028Group
import kotlinx.coroutines.flow.Flow

/**
 * GPS028数据访问对象
 * 用于查询和管理GB 27887-2011标准的设计参数数据
 */
@Dao
interface GPS028DAO {

    /**
     * 根据组别和百分位查询参数
     */
    @Query("""
        SELECT * FROM gps028_params
        WHERE groupName = :groupName AND percentile = :percentile
        LIMIT 1
    """)
    suspend fun getByGroupAndPercentile(groupName: String, percentile: String): GPS028Entity?

    /**
     * 根据组别查询所有参数
     */
    @Query("""
        SELECT * FROM gps028_params
        WHERE groupName = :groupName
        ORDER BY percentile
    """)
    suspend fun getAllByGroup(groupName: String): List<GPS028Entity>

    /**
     * 根据组别查询所有参数（Flow）
     */
    @Query("""
        SELECT * FROM gps028_params
        WHERE groupName = :groupName
        ORDER BY percentile
    """)
    fun getAllByGroupFlow(groupName: String): Flow<List<GPS028Entity>>

    /**
     * 根据百分位查询所有组别的参数
     */
    @Query("""
        SELECT * FROM gps028_params
        WHERE percentile = :percentile
        ORDER BY groupName
    """)
    suspend fun getAllByPercentile(percentile: String): List<GPS028Entity>

    /**
     * 查询所有参数
     */
    @Query("SELECT * FROM gps028_params ORDER BY groupName, percentile")
    suspend fun getAll(): List<GPS028Entity>

    /**
     * 查询所有参数（Flow）
     */
    @Query("SELECT * FROM gps028_params ORDER BY groupName, percentile")
    fun getAllFlow(): Flow<List<GPS028Entity>>

    /**
     * 插入参数
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GPS028Entity): Long

    /**
     * 批量插入参数
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<GPS028Entity>)

    /**
     * 更新参数
     */
    @Update
    suspend fun update(entity: GPS028Entity)

    /**
     * 删除参数
     */
    @Delete
    suspend fun delete(entity: GPS028Entity)

    /**
     * 清空所有数据
     */
    @Query("DELETE FROM gps028_params")
    suspend fun deleteAll()

    /**
     * 获取数据数量
     */
    @Query("SELECT COUNT(*) FROM gps028_params")
    suspend fun getCount(): Int

    /**
     * 检查特定组别和百分位的数据是否存在
     */
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM gps028_params
            WHERE groupName = :groupName AND percentile = :percentile
        )
    """)
    suspend fun exists(groupName: String, percentile: String): Boolean

    /**
     * 根据体重范围查询参数
     */
    @Query("""
        SELECT * FROM gps028_params
        WHERE weight >= :minWeight AND weight <= :maxWeight
        ORDER BY weight
    """)
    suspend fun getByWeightRange(minWeight: Double, maxWeight: Double): List<GPS028Entity>

    /**
     * 根据身高范围查询参数
     */
    @Query("""
        SELECT * FROM gps028_params
        WHERE height >= :minHeight AND height <= :maxHeight
        ORDER BY height
    """)
    suspend fun getByHeightRange(minHeight: Double, maxHeight: Double): List<GPS028Entity>
}

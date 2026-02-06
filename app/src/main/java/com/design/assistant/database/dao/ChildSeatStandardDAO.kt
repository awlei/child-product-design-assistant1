package com.design.assistant.database.dao

import androidx.room.*
import com.design.assistant.database.entities.ChildSeatStandardEntity
import kotlinx.coroutines.flow.Flow

/**
 * 儿童安全座椅标准数据访问对象
 * 用于查询和管理ECE R129、CMVSS213、FMVSS213等标准的设计参数数据
 */
@Dao
interface ChildSeatStandardDAO {

    /**
     * 根据标准类型和假人类型查询参数
     */
    @Query("""
        SELECT * FROM child_seat_standard_params
        WHERE standardType = :standardType AND dummyType = :dummyType
        LIMIT 1
    """)
    suspend fun getByStandardAndDummy(standardType: String, dummyType: String): ChildSeatStandardEntity?

    /**
     * 根据标准类型查询所有参数
     */
    @Query("""
        SELECT * FROM child_seat_standard_params
        WHERE standardType = :standardType
        ORDER BY weight
    """)
    suspend fun getAllByStandard(standardType: String): List<ChildSeatStandardEntity>

    /**
     * 根据标准类型查询所有参数（Flow）
     */
    @Query("""
        SELECT * FROM child_seat_standard_params
        WHERE standardType = :standardType
        ORDER BY weight
    """)
    fun getAllByStandardFlow(standardType: String): Flow<List<ChildSeatStandardEntity>>

    /**
     * 根据地区查询所有参数
     */
    @Query("""
        SELECT * FROM child_seat_standard_params
        WHERE region = :region
        ORDER BY standardType, weight
    """)
    suspend fun getAllByRegion(region: String): List<ChildSeatStandardEntity>

    /**
     * 查询所有参数
     */
    @Query("SELECT * FROM child_seat_standard_params ORDER BY standardType, weight")
    suspend fun getAll(): List<ChildSeatStandardEntity>

    /**
     * 查询所有参数（Flow）
     */
    @Query("SELECT * FROM child_seat_standard_params ORDER BY standardType, weight")
    fun getAllFlow(): Flow<List<ChildSeatStandardEntity>>

    /**
     * 插入参数
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ChildSeatStandardEntity): Long

    /**
     * 批量插入参数
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ChildSeatStandardEntity>)

    /**
     * 更新参数
     */
    @Update
    suspend fun update(entity: ChildSeatStandardEntity)

    /**
     * 删除参数
     */
    @Delete
    suspend fun delete(entity: ChildSeatStandardEntity)

    /**
     * 清空所有数据
     */
    @Query("DELETE FROM child_seat_standard_params")
    suspend fun deleteAll()

    /**
     * 获取数据数量
     */
    @Query("SELECT COUNT(*) FROM child_seat_standard_params")
    suspend fun getCount(): Int

    /**
     * 检查特定标准和假人类型的数据是否存在
     */
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM child_seat_standard_params
            WHERE standardType = :standardType AND dummyType = :dummyType
        )
    """)
    suspend fun exists(standardType: String, dummyType: String): Boolean

    /**
     * 根据体重范围查询参数
     */
    @Query("""
        SELECT * FROM child_seat_standard_params
        WHERE weight >= :minWeight AND weight <= :maxWeight
        ORDER BY weight
    """)
    suspend fun getByWeightRange(minWeight: Double, maxWeight: Double): List<ChildSeatStandardEntity>

    /**
     * 根据身高范围查询参数
     */
    @Query("""
        SELECT * FROM child_seat_standard_params
        WHERE height >= :minHeight AND height <= :maxHeight
        ORDER BY height
    """)
    suspend fun getByHeightRange(minHeight: Double, maxHeight: Double): List<ChildSeatStandardEntity>

    /**
     * 查询支持ISOFIX的标准
     */
    @Query("""
        SELECT * FROM child_seat_standard_params
        WHERE hasISOFIXSupport = 1
        ORDER BY standardType, weight
    """)
    suspend fun getISOFIXSupported(): List<ChildSeatStandardEntity>

    /**
     * 查询支持LATCH的标准
     */
    @Query("""
        SELECT * FROM child_seat_standard_params
        WHERE hasLATCHSupport = 1
        ORDER BY standardType, weight
    """)
    suspend fun getLATCHSupported(): List<ChildSeatStandardEntity>
}

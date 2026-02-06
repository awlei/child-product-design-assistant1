package com.design.assistant.database.dao

import androidx.room.*
import com.design.assistant.database.entities.ProductStandardEntity
import kotlinx.coroutines.flow.Flow

/**
 * 产品标准数据访问对象
 * 用于查询和管理婴儿推车、高脚椅、儿童床等产品的标准设计参数数据
 */
@Dao
interface ProductStandardDAO {

    /**
     * 根据产品类型和标准类型查询参数
     */
    @Query("""
        SELECT * FROM product_standard_params
        WHERE productType = :productType AND standardType = :standardType
        LIMIT 1
    """)
    suspend fun getByProductAndStandard(productType: String, standardType: String): ProductStandardEntity?

    /**
     * 根据产品类型查询所有参数
     */
    @Query("""
        SELECT * FROM product_standard_params
        WHERE productType = :productType
        ORDER BY standardType
    """)
    suspend fun getAllByProduct(productType: String): List<ProductStandardEntity>

    /**
     * 根据产品类型查询所有参数（Flow）
     */
    @Query("""
        SELECT * FROM product_standard_params
        WHERE productType = :productType
        ORDER BY standardType
    """)
    fun getAllByProductFlow(productType: String): Flow<List<ProductStandardEntity>>

    /**
     * 根据地区查询所有参数
     */
    @Query("""
        SELECT * FROM product_standard_params
        WHERE region = :region
        ORDER BY productType, standardType
    """)
    suspend fun getAllByRegion(region: String): List<ProductStandardEntity>

    /**
     * 查询所有参数
     */
    @Query("SELECT * FROM product_standard_params ORDER BY productType, standardType")
    suspend fun getAll(): List<ProductStandardEntity>

    /**
     * 查询所有参数（Flow）
     */
    @Query("SELECT * FROM product_standard_params ORDER BY productType, standardType")
    fun getAllFlow(): Flow<List<ProductStandardEntity>>

    /**
     * 插入参数
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ProductStandardEntity): Long

    /**
     * 批量插入参数
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ProductStandardEntity>)

    /**
     * 更新参数
     */
    @Update
    suspend fun update(entity: ProductStandardEntity)

    /**
     * 删除参数
     */
    @Delete
    suspend fun delete(entity: ProductStandardEntity)

    /**
     * 清空所有数据
     */
    @Query("DELETE FROM product_standard_params")
    suspend fun deleteAll()

    /**
     * 获取数据数量
     */
    @Query("SELECT COUNT(*) FROM product_standard_params")
    suspend fun getCount(): Int

    /**
     * 检查特定产品和标准的数据是否存在
     */
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM product_standard_params
            WHERE productType = :productType AND standardType = :standardType
        )
    """)
    suspend fun exists(productType: String, standardType: String): Boolean

    /**
     * 根据承重范围查询参数
     */
    @Query("""
        SELECT * FROM product_standard_params
        WHERE maxWeightCapacity >= :minWeight AND maxWeightCapacity <= :maxWeight
        ORDER BY maxWeightCapacity
    """)
    suspend fun getByWeightCapacityRange(minWeight: Double, maxWeight: Double): List<ProductStandardEntity>

    /**
     * 查询支持折叠功能的产品
     */
    @Query("""
        SELECT * FROM product_standard_params
        WHERE hasFoldableFeature = 1
        ORDER BY productType, standardType
    """)
    suspend fun getFoldableProducts(): List<ProductStandardEntity>

    /**
     * 查询支持高度调节功能的产品
     */
    @Query("""
        SELECT * FROM product_standard_params
        WHERE hasAdjustableHeight = 1
        ORDER BY productType, standardType
    """)
    suspend fun getAdjustableHeightProducts(): List<ProductStandardEntity>
}

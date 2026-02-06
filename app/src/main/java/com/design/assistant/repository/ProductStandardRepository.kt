package com.design.assistant.repository

import com.design.assistant.database.databases.ProductStandardDatabase
import com.design.assistant.database.entities.ProductStandardEntity
import kotlinx.coroutines.flow.Flow

/**
 * 产品标准Repository
 * 封装产品标准数据库的访问逻辑
 */
class ProductStandardRepository(private val database: ProductStandardDatabase) {

    private val dao = database.productStandardDAO()

    /**
     * 根据产品类型和标准类型查询参数
     */
    suspend fun getByProductAndStandard(productType: String, standardType: String): ProductStandardEntity? {
        return dao.getByProductAndStandard(productType, standardType)
    }

    /**
     * 根据产品类型查询所有参数
     */
    suspend fun getAllByProduct(productType: String): List<ProductStandardEntity> {
        return dao.getAllByProduct(productType)
    }

    /**
     * 根据产品类型查询所有参数（Flow）
     */
    fun getAllByProductFlow(productType: String): Flow<List<ProductStandardEntity>> {
        return dao.getAllByProductFlow(productType)
    }

    /**
     * 根据地区查询所有参数
     */
    suspend fun getAllByRegion(region: String): List<ProductStandardEntity> {
        return dao.getAllByRegion(region)
    }

    /**
     * 查询所有参数
     */
    suspend fun getAll(): List<ProductStandardEntity> {
        return dao.getAll()
    }

    /**
     * 查询所有参数（Flow）
     */
    fun getAllFlow(): Flow<List<ProductStandardEntity>> {
        return dao.getAllFlow()
    }

    /**
     * 初始化产品标准数据
     */
    suspend fun initializeData() {
        database.initializeProductStandardData()
    }

    /**
     * 获取数据数量
     */
    suspend fun getCount(): Int {
        return dao.getCount()
    }
}

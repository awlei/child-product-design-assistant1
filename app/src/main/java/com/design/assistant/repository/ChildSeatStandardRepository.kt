package com.design.assistant.repository

import com.design.assistant.database.databases.ChildSeatStandardDatabase
import com.design.assistant.database.entities.ChildSeatStandardEntity
import kotlinx.coroutines.flow.Flow

/**
 * 儿童安全座椅标准Repository
 * 封装儿童安全座椅标准数据库的访问逻辑
 */
class ChildSeatStandardRepository(private val database: ChildSeatStandardDatabase) {

    private val dao = database.childSeatStandardDAO()

    /**
     * 根据标准类型和假人类型查询参数
     */
    suspend fun getByStandardAndDummy(standardType: String, dummyType: String): ChildSeatStandardEntity? {
        return dao.getByStandardAndDummy(standardType, dummyType)
    }

    /**
     * 根据标准类型查询所有参数
     */
    suspend fun getAllByStandard(standardType: String): List<ChildSeatStandardEntity> {
        return dao.getAllByStandard(standardType)
    }

    /**
     * 根据标准类型查询所有参数（Flow）
     */
    fun getAllByStandardFlow(standardType: String): Flow<List<ChildSeatStandardEntity>> {
        return dao.getAllByStandardFlow(standardType)
    }

    /**
     * 根据地区查询所有参数
     */
    suspend fun getAllByRegion(region: String): List<ChildSeatStandardEntity> {
        return dao.getAllByRegion(region)
    }

    /**
     * 查询所有参数
     */
    suspend fun getAll(): List<ChildSeatStandardEntity> {
        return dao.getAll()
    }

    /**
     * 查询所有参数（Flow）
     */
    fun getAllFlow(): Flow<List<ChildSeatStandardEntity>> {
        return dao.getAllFlow()
    }

    /**
     * 初始化儿童安全座椅标准数据
     */
    suspend fun initializeData() {
        database.initializeChildSeatStandardData()
    }

    /**
     * 获取数据数量
     */
    suspend fun getCount(): Int {
        return dao.getCount()
    }
}

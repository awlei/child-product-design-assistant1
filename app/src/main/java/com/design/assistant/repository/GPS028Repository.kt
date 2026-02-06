package com.design.assistant.repository

import com.design.assistant.database.databases.GPS028Database
import com.design.assistant.database.entities.GPS028Entity
import com.design.assistant.model.GPS028Group
import kotlinx.coroutines.flow.Flow

/**
 * GPS028 Repository
 * 封装GPS028数据库的访问逻辑
 */
class GPS028Repository(private val database: GPS028Database) {

    private val dao = database.gps028DAO()

    /**
     * 根据组别和百分位查询参数
     */
    suspend fun getByGroupAndPercentile(groupName: String, percentile: String): com.design.assistant.model.GPS028Params? {
        val entity = dao.getByGroupAndPercentile(groupName, percentile)
        return entity?.toGPS028Params()
    }

    /**
     * 根据组别查询所有参数
     */
    suspend fun getAllByGroup(groupName: String): List<com.design.assistant.model.GPS028Params> {
        val entities = dao.getAllByGroup(groupName)
        return entities.map { it.toGPS028Params() }
    }

    /**
     * 根据组别查询所有参数（Flow）
     */
    fun getAllByGroupFlow(groupName: String): Flow<List<com.design.assistant.model.GPS028Params>> {
        return dao.getAllByGroupFlow(groupName).map { entities ->
            entities.map { it.toGPS028Params() }
        }
    }

    /**
     * 查询所有参数
     */
    suspend fun getAll(): List<com.design.assistant.model.GPS028Params> {
        val entities = dao.getAll()
        return entities.map { it.toGPS028Params() }
    }

    /**
     * 查询所有参数（Flow）
     */
    fun getAllFlow(): Flow<List<com.design.assistant.model.GPS028Params>> {
        return dao.getAllFlow().map { entities ->
            entities.map { it.toGPS028Params() }
        }
    }

    /**
     * 插入参数
     */
    suspend fun insert(params: com.design.assistant.model.GPS028Params): Long {
        val entity = GPS028Entity.fromGPS028Params(params)
        return dao.insert(entity)
    }

    /**
     * 初始化GPS028数据
     */
    suspend fun initializeData() {
        database.initializeGPS028Data()
    }

    /**
     * 获取数据数量
     */
    suspend fun getCount(): Int {
        return dao.getCount()
    }
}

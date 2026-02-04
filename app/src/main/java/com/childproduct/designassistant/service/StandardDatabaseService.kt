package com.childproduct.designassistant.service

import com.childproduct.designassistant.database.AppDatabase
import com.childproduct.designassistant.database.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 标准数据库查询服务
 *
 * 提供标准数据库的查询功能，支持根据标准ID获取详细的标准信息
 */
class StandardDatabaseService(
    private val database: AppDatabase
) {

    /**
     * 根据标准ID获取儿童安全座椅标准
     */
    suspend fun getCarSeatStandard(standardId: String): CarSeatStandard? =
        withContext(Dispatchers.IO) {
            database.carSeatStandardDao().getById(standardId)
        }

    /**
     * 根据标准ID获取婴儿推车标准
     */
    suspend fun getStrollerStandard(standardId: String): StrollerStandard? =
        withContext(Dispatchers.IO) {
            database.strollerStandardDao().getById(standardId)
        }

    /**
     * 获取所有儿童安全座椅标准
     */
    suspend fun getAllCarSeatStandards(): List<CarSeatStandard> =
        withContext(Dispatchers.IO) {
            database.carSeatStandardDao().getAll()
        }

    /**
     * 获取所有婴儿推车标准
     */
    suspend fun getAllStrollerStandards(): List<StrollerStandard> =
        withContext(Dispatchers.IO) {
            database.strollerStandardDao().getAll()
        }

    /**
     * 根据标准名称模糊搜索儿童安全座椅标准
     */
    suspend fun searchCarSeatStandards(query: String): List<CarSeatStandard> =
        withContext(Dispatchers.IO) {
            database.carSeatStandardDao().searchByName("%$query%")
        }

    /**
     * 根据标准名称模糊搜索婴儿推车标准
     */
    suspend fun searchStrollerStandards(query: String): List<StrollerStandard> =
        withContext(Dispatchers.IO) {
            database.strollerStandardDao().searchByName("%$query%")
        }

    /**
     * 获取标准的摘要信息
     */
    suspend fun getStandardSummary(standardId: String): StandardSummary? =
        withContext(Dispatchers.IO) {
            val carSeatStandard = database.carSeatStandardDao().getById(standardId)
            if (carSeatStandard != null) {
                return@withContext StandardSummary(
                    id = carSeatStandard.standardId,
                    name = carSeatStandard.standardName,
                    region = carSeatStandard.region,
                    version = carSeatStandard.version,
                    type = "儿童安全座椅"
                )
            }

            val strollerStandard = database.strollerStandardDao().getById(standardId)
            if (strollerStandard != null) {
                return@withContext StandardSummary(
                    id = strollerStandard.standardId,
                    name = strollerStandard.standardName,
                    region = strollerStandard.region,
                    version = strollerStandard.version,
                    type = "婴儿推车"
                )
            }

            null
        }

    /**
     * 获取多个标准的摘要信息
     */
    suspend fun getStandardSummaries(standardIds: List<String>): List<StandardSummary> =
        withContext(Dispatchers.IO) {
            val summaries = mutableListOf<StandardSummary>()

            // 查询儿童安全座椅标准
            val carSeatStandards = database.carSeatStandardDao().getByIds(standardIds)
            carSeatStandards.forEach { standard: CarSeatStandard ->
                summaries.add(
                    StandardSummary(
                        id = standard.standardId,
                        name = standard.standardName,
                        region = standard.region,
                        version = standard.version,
                        type = "儿童安全座椅"
                    )
                )
            }

            // 查询婴儿推车标准
            val strollerStandards = database.strollerStandardDao().getByIds(standardIds)
            strollerStandards.forEach { standard: StrollerStandard ->
                summaries.add(
                    StandardSummary(
                        id = standard.standardId,
                        name = standard.standardName,
                        region = standard.region,
                        version = standard.version,
                        type = "婴儿推车"
                    )
                )
            }

            summaries
        }
}

/**
 * 标准摘要信息
 */
data class StandardSummary(
    val id: String,
    val name: String,
    val region: String,
    val version: String,
    val type: String
)

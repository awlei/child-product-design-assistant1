package com.childproduct.designassistant.service

import com.childproduct.designassistant.database.CribDatabase
import com.childproduct.designassistant.database.EceR129Database
import com.childproduct.designassistant.database.HighChairDatabase
import com.childproduct.designassistant.database.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * 标准数据库查询服务
 *
 * 提供标准数据库的查询功能，支持根据标准ID获取详细的标准信息
 */
class StandardDatabaseService(
    private val eceR129Database: EceR129Database,
    private val highChairDatabase: HighChairDatabase,
    private val cribDatabase: CribDatabase
) {

    /**
     * 根据标准ID获取儿童高脚椅标准
     */
    suspend fun getHighChairStandard(standardId: String): HighChairStandard? =
        withContext(Dispatchers.IO) {
            highChairDatabase.highChairStandardDao().getStandardById(standardId)
        }

    /**
     * 根据标准ID获取儿童床标准
     */
    suspend fun getCribStandard(standardId: String): CribStandard? =
        withContext(Dispatchers.IO) {
            cribDatabase.cribStandardDao().getStandardById(standardId)
        }

    /**
     * 获取所有儿童高脚椅标准
     */
    suspend fun getAllHighChairStandards(): List<HighChairStandard> =
        withContext(Dispatchers.IO) {
            highChairDatabase.highChairStandardDao().getAllActiveStandards().first()
        }

    /**
     * 获取所有儿童床标准
     */
    suspend fun getAllCribStandards(): List<CribStandard> =
        withContext(Dispatchers.IO) {
            cribDatabase.cribStandardDao().getAllActiveStandards().first()
        }

    /**
     * 获取标准的摘要信息
     */
    suspend fun getStandardSummary(standardId: String): StandardSummary? =
        withContext(Dispatchers.IO) {
            val highChairStandard = highChairDatabase.highChairStandardDao().getStandardById(standardId)
            if (highChairStandard != null) {
                return@withContext StandardSummary(
                    id = highChairStandard.standardId,
                    name = highChairStandard.standardName,
                    region = highChairStandard.region,
                    version = highChairStandard.version,
                    type = "儿童高脚椅"
                )
            }

            val cribStandard = cribDatabase.cribStandardDao().getStandardById(standardId)
            if (cribStandard != null) {
                return@withContext StandardSummary(
                    id = cribStandard.standardId,
                    name = cribStandard.standardName,
                    region = cribStandard.region,
                    version = cribStandard.version,
                    type = "儿童床"
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

            // 查询儿童高脚椅标准
            val highChairStandards = highChairDatabase.highChairStandardDao().getAllActiveStandards().first()
            highChairStandards.forEach { standard: HighChairStandard ->
                if (standardIds.contains(standard.standardId)) {
                    summaries.add(
                        StandardSummary(
                            id = standard.standardId,
                            name = standard.standardName,
                            region = standard.region,
                            version = standard.version,
                            type = "儿童高脚椅"
                        )
                    )
                }
            }

            // 查询儿童床标准
            val cribStandards = cribDatabase.cribStandardDao().getAllActiveStandards().first()
            cribStandards.forEach { standard: CribStandard ->
                if (standardIds.contains(standard.standardId)) {
                    summaries.add(
                        StandardSummary(
                            id = standard.standardId,
                            name = standard.standardName,
                            region = standard.region,
                            version = standard.version,
                            type = "儿童床"
                        )
                    )
                }
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

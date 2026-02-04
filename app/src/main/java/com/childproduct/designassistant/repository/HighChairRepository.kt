package com.childproduct.designassistant.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.childproduct.designassistant.database.HighChairDatabase
import com.childproduct.designassistant.database.dao.*
import com.childproduct.designassistant.database.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * 儿童高脚椅标准数据仓库
 * 提供对儿童高脚椅标准数据的统一访问接口
 */
class HighChairRepository private constructor(
    private val context: Context,
    private val database: HighChairDatabase
) {

    // ========== 标准相关操作 ==========

    /**
     * 获取所有活跃的儿童高脚椅标准
     */
    fun getAllActiveStandards() = database.highChairStandardDao().getAllActiveStandards()

    /**
     * 根据标准ID获取儿童高脚椅标准
     */
    suspend fun getStandardById(standardId: String) =
        withContext(Dispatchers.IO) { database.highChairStandardDao().getStandardById(standardId) }

    /**
     * 根据地区获取儿童高脚椅标准
     */
    fun getStandardsByRegion(region: String) =
        database.highChairStandardDao().getStandardsByRegion(region)

    // ========== 年龄组相关操作 ==========

    /**
     * 获取标准下的年龄组
     */
    fun getAgeGroups(standardId: String) =
        database.highChairAgeGroupDao().getAgeGroupsByStandard(standardId)

    // ========== 安全要求相关操作 ==========

    /**
     * 获取标准下的安全要求
     */
    fun getSafetyRequirements(standardId: String) =
        database.highChairSafetyRequirementDao().getRequirementsByStandard(standardId)

    /**
     * 根据类别获取安全要求
     */
    fun getRequirementsByCategory(standardId: String, category: String) =
        database.highChairSafetyRequirementDao().getRequirementsByCategory(standardId, category)

    // ========== 稳定性相关操作 ==========

    /**
     * 获取标准下的稳定性数据
     */
    fun getStability(standardId: String) =
        database.highChairStabilityDao().getStabilityByStandard(standardId)

    // ========== 约束系统相关操作 ==========

    /**
     * 获取标准下的约束系统数据
     */
    fun getRestraints(standardId: String) =
        database.highChairRestraintDao().getRestraintsByStandard(standardId)

    /**
     * 根据类型获取约束系统数据
     */
    fun getRestraintsByType(type: String) =
        database.highChairRestraintDao().getRestraintsByType(type)

    // ========== 数据初始化 ==========

    /**
     * 初始化儿童高脚椅标准数据
     */
    suspend fun initializeStandards() = withContext(Dispatchers.IO) {
        // 插入标准
        database.highChairStandardDao().insertStandards(
            listOf(
                HighChairStandardsData.EN_14988_STANDARD,
                HighChairStandardsData.GB_29281_STANDARD
            )
        )

        // 插入年龄组
        database.highChairAgeGroupDao().insertAgeGroups(HighChairStandardsData.AGE_GROUPS)

        // 插入安全要求
        database.highChairSafetyRequirementDao().insertRequirements(HighChairStandardsData.SAFETY_REQUIREMENTS)

        // 插入稳定性数据
        database.highChairStabilityDao().insertStabilities(HighChairStandardsData.STABILITY_DATA)

        // 插入约束系统数据
        database.highChairRestraintDao().insertRestraints(HighChairStandardsData.RESTRAINT_DATA)
    }

    /**
     * 获取所有标准（用于下拉列表等）
     */
    suspend fun getAllStandardsForSelection(): List<StandardSelection> = withContext(Dispatchers.IO) {
        database.highChairStandardDao().getAllActiveStandards().first().map { standard ->
            StandardSelection(
                id = standard.standardId,
                name = standard.standardName,
                region = standard.region
            )
        }
    }

    companion object {
        @Volatile private var INSTANCE: HighChairRepository? = null

        fun getInstance(context: Context): HighChairRepository {
            return INSTANCE ?: synchronized(this) {
                val database = HighChairDatabase.getDatabase(context)
                val instance = HighChairRepository(context, database)
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * 标准选择项
 */
data class StandardSelection(
    val id: String,
    val name: String,
    val region: String
)

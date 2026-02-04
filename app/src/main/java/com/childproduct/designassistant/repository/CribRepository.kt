package com.childproduct.designassistant.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.childproduct.designassistant.database.CribDatabase
import com.childproduct.designassistant.database.dao.*
import com.childproduct.designassistant.database.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * 儿童床标准数据仓库
 * 提供对儿童床标准数据的统一访问接口
 */
class CribRepository private constructor(
    private val context: Context,
    private val database: CribDatabase
) {

    // ========== 标准相关操作 ==========

    /**
     * 获取所有活跃的儿童床标准
     */
    fun getAllActiveStandards() = database.cribStandardDao().getAllActiveStandards()

    /**
     * 根据标准ID获取儿童床标准
     */
    suspend fun getStandardById(standardId: String) =
        withContext(Dispatchers.IO) { database.cribStandardDao().getStandardById(standardId) }

    /**
     * 根据地区获取儿童床标准
     */
    fun getStandardsByRegion(region: String) =
        database.cribStandardDao().getStandardsByRegion(region)

    // ========== 尺寸要求相关操作 ==========

    /**
     * 获取标准下的尺寸要求
     */
    fun getDimensions(standardId: String) =
        database.cribDimensionDao().getDimensionsByStandard(standardId)

    /**
     * 根据类型获取尺寸要求
     */
    fun getDimensionsByType(standardId: String, type: String) =
        database.cribDimensionDao().getDimensionsByType(standardId, type)

    // ========== 床垫间隙相关操作 ==========

    /**
     * 获取标准下的床垫间隙数据
     */
    fun getMattressGaps(standardId: String) =
        database.cribMattressGapDao().getGapsByStandard(standardId)

    /**
     * 根据位置获取床垫间隙数据
     */
    fun getGapsByLocation(standardId: String, location: String) =
        database.cribMattressGapDao().getGapsByLocation(standardId, location)

    // ========== 栏杆相关操作 ==========

    /**
     * 获取标准下的栏杆数据
     */
    fun getRailings(standardId: String) =
        database.cribRailingDao().getRailingsByStandard(standardId)

    /**
     * 根据类型获取栏杆数据
     */
    fun getRailingsByType(standardId: String, type: String) =
        database.cribRailingDao().getRailingsByType(standardId, type)

    // ========== 安全要求相关操作 ==========

    /**
     * 获取标准下的安全要求
     */
    fun getSafetyRequirements(standardId: String) =
        database.cribSafetyRequirementDao().getRequirementsByStandard(standardId)

    /**
     * 根据类别获取安全要求
     */
    fun getRequirementsByCategory(standardId: String, category: String) =
        database.cribSafetyRequirementDao().getRequirementsByCategory(standardId, category)

    // ========== 数据初始化 ==========

    /**
     * 初始化儿童床标准数据
     */
    suspend fun initializeStandards() = withContext(Dispatchers.IO) {
        // 插入标准
        database.cribStandardDao().insertStandards(
            listOf(
                CribStandardsData.EN_716_STANDARD,
                CribStandardsData.GB_28007_STANDARD
            )
        )

        // 插入尺寸要求
        database.cribDimensionDao().insertDimensions(CribStandardsData.DIMENSIONS)

        // 插入床垫间隙数据
        database.cribMattressGapDao().insertGaps(CribStandardsData.MATTRESS_GAPS)

        // 插入栏杆数据
        database.cribRailingDao().insertRailings(CribStandardsData.RAILINGS)

        // 插入安全要求数据
        database.cribSafetyRequirementDao().insertRequirements(CribStandardsData.SAFETY_REQUIREMENTS)
    }

    /**
     * 获取所有标准（用于下拉列表等）
     */
    suspend fun getAllStandardsForSelection(): List<StandardSelection> = withContext(Dispatchers.IO) {
        database.cribStandardDao().getAllActiveStandards().first().map { standard ->
            StandardSelection(
                id = standard.standardId,
                name = standard.standardName,
                region = standard.region
            )
        }
    }

    companion object {
        @Volatile private var INSTANCE: CribRepository? = null

        fun getInstance(context: Context): CribRepository {
            return INSTANCE ?: synchronized(this) {
                val database = CribDatabase.getDatabase(context)
                val instance = CribRepository(context, database)
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

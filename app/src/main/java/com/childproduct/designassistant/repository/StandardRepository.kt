package com.childproduct.designassistant.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.childproduct.designassistant.database.CribDatabase
import com.childproduct.designassistant.database.EceR129Database
import com.childproduct.designassistant.database.HighChairDatabase
import com.childproduct.designassistant.database.dao.*
import com.childproduct.designassistant.database.entity.*
import com.childproduct.designassistant.model.InstallDirection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 标准数据仓库
 * 提供对ECE R129标准数据的统一访问接口
 */
class StandardRepository private constructor(
    private val context: Context,
    private val eceR129Database: EceR129Database,
    private val highChairDatabase: HighChairDatabase,
    private val cribDatabase: CribDatabase
) {

    // ========== 假人相关操作 ==========

    /**
     * 获取所有假人类型
     */
    fun getAllDummies(): LiveData<List<CrashTestDummy>> {
        return eceR129Database.crashTestDummyDao().getAllDummies()
    }

    /**
     * 根据身高获取适用假人
     */
    suspend fun getDummyByHeight(heightCm: Int): CrashTestDummy? {
        return withContext(Dispatchers.IO) {
            eceR129Database.crashTestDummyDao().getDummyByHeightRange(heightCm)
        }
    }

    /**
     * 根据身高范围获取所有适用假人
     */
    suspend fun getDummiesByHeightRange(minHeight: Int, maxHeight: Int): List<CrashTestDummy> {
        return withContext(Dispatchers.IO) {
            val allDummies = eceR129Database.crashTestDummyDao().getAllDummiesList()
            allDummies.filter { dummy ->
                dummy.minHeightCm >= minHeight && dummy.maxHeightCm <= maxHeight
            }
        }
    }

    // ========== 安全阈值相关操作 ==========

    /**
     * 获取所有安全阈值
     */
    fun getAllSafetyThresholds(): LiveData<List<SafetyThreshold>> {
        return eceR129Database.safetyThresholdDao().getAllThresholds()
    }

    /**
     * 根据假人获取安全阈值
     */
    fun getSafetyThresholdsByDummy(dummyId: String): LiveData<List<SafetyThreshold>> {
        return eceR129Database.safetyThresholdDao().getThresholdsByDummy(dummyId)
    }

    /**
     * 根据假人代码获取适用的安全阈值
     */
    suspend fun getThresholdsByDummyCode(dummyCode: String): List<SafetyThreshold> {
        return withContext(Dispatchers.IO) {
            eceR129Database.safetyThresholdDao().getThresholdsApplicableToDummy(dummyCode)
        }
    }

    // ========== 测试配置相关操作 ==========

    /**
     * 获取所有测试配置
     */
    fun getAllTestConfigurations(): LiveData<List<TestConfiguration>> {
        return eceR129Database.testConfigurationDao().getAllConfigurations()
    }

    /**
     * 根据假人和安装方向获取测试配置
     */
    fun getTestConfigurations(
        dummyCode: String,
        installDirection: String
    ): LiveData<List<TestConfiguration>> {
        return eceR129Database.testConfigurationDao().getConfigurationsByDummyAndDirection(
            dummyCode, installDirection
        )
    }

    /**
     * 根据身高范围获取测试配置
     */
    suspend fun getTestConfigurationsByHeightRange(
        minHeight: Int,
        maxHeight: Int
    ): List<TestConfiguration> {
        return withContext(Dispatchers.IO) {
            val dummies = getDummiesByHeightRange(minHeight, maxHeight)
            val configs = mutableListOf<TestConfiguration>()
            
            dummies.forEach { dummy ->
                val direction = if (dummy.minHeightCm < 105) {
                    "REARWARD"
                } else {
                    "FORWARD"
                }
                val dummyConfigs = eceR129Database.testConfigurationDao()
                    .getConfigurationsByDummyAndDirection(dummy.dummyCode, direction).value ?: emptyList()
                configs.addAll(dummyConfigs)
            }
            
            configs.distinctBy { it.configId }
        }
    }

    // ========== 标准引用相关操作 ==========

    /**
     * 获取当前标准版本
     */
    suspend fun getCurrentStandardVersion(): String? {
        return withContext(Dispatchers.IO) {
            eceR129Database.standardReferenceDao().getCurrentVersion("UN R129")
        }
    }

    /**
     * 获取所有标准引用
     */
    fun getAllStandards(): LiveData<List<StandardReference>> {
        return eceR129Database.standardReferenceDao().getAllStandards()
    }

    // ========== 身高范围映射相关操作 ==========

    /**
     * 根据身高获取映射信息
     */
    suspend fun getHeightMapping(heightCm: Int): HeightRangeMapping? {
        return withContext(Dispatchers.IO) {
            eceR129Database.heightRangeMappingDao().getMappingByHeight(heightCm)
        }
    }

    /**
     * 获取所有身高映射
     */
    fun getAllHeightMappings(): LiveData<List<HeightRangeMapping>> {
        return eceR129Database.heightRangeMappingDao().getAllMappings()
    }

    // ========== 安装方式相关操作 ==========

    /**
     * 获取所有安装方式
     */
    fun getAllInstallationMethods(): LiveData<List<InstallationMethod>> {
        return eceR129Database.installationMethodDao().getAllMethods()
    }

    // ========== 材料规格相关操作 ==========

    /**
     * 获取所有材料规格
     */
    fun getAllMaterialSpecifications(): LiveData<List<MaterialSpecification>> {
        return eceR129Database.materialSpecificationDao().getAllSpecifications()
    }

    // ========== ISOFIX要求相关操作 ==========

    /**
     * 获取所有ISOFIX要求
     */
    fun getAllIsofixRequirements(): LiveData<List<IsofixRequirement>> {
        return eceR129Database.isofixRequirementDao().getAllRequirements()
    }

    // ========== 同步日志相关操作 ==========

    /**
     * 获取最近的同步日志
     */
    fun getRecentSyncLogs(): LiveData<List<StandardUpdateLog>> {
        return eceR129Database.standardUpdateLogDao().getRecentLogs()
    }

    /**
     * 获取最后同步时间
     */
    suspend fun getLastSyncTime(): Long? {
        return withContext(Dispatchers.IO) {
            eceR129Database.standardUpdateLogDao().getLastSyncTime()
        }
    }

    // ========== 儿童高脚椅相关操作 ==========

    /**
     * 获取所有活跃的儿童高脚椅标准
     */
    fun getAllActiveHighChairStandards() = highChairDatabase.highChairStandardDao().getAllActiveStandards()

    /**
     * 根据标准ID获取儿童高脚椅标准
     */
    suspend fun getHighChairStandardById(standardId: String) = 
        withContext(Dispatchers.IO) { highChairDatabase.highChairStandardDao().getStandardById(standardId) }

    /**
     * 根据地区获取儿童高脚椅标准
     */
    fun getHighChairStandardsByRegion(region: String) = 
        highChairDatabase.highChairStandardDao().getStandardsByRegion(region)

    /**
     * 获取标准下的年龄组
     */
    fun getHighChairAgeGroups(standardId: String) = 
        highChairDatabase.highChairAgeGroupDao().getAgeGroupsByStandard(standardId)

    /**
     * 获取标准下的安全要求
     */
    fun getHighChairSafetyRequirements(standardId: String) = 
        highChairDatabase.highChairSafetyRequirementDao().getRequirementsByStandard(standardId)

    /**
     * 根据类别获取安全要求
     */
    fun getHighChairRequirementsByCategory(standardId: String, category: String) = 
        highChairDatabase.highChairSafetyRequirementDao().getRequirementsByCategory(standardId, category)

    /**
     * 获取标准下的稳定性数据
     */
    fun getHighChairStability(standardId: String) = 
        highChairDatabase.highChairStabilityDao().getStabilityByStandard(standardId)

    /**
     * 获取标准下的约束系统数据
     */
    fun getHighChairRestraints(standardId: String) = 
        highChairDatabase.highChairRestraintDao().getRestraintsByStandard(standardId)

    /**
     * 初始化儿童高脚椅标准数据
     */
    suspend fun initializeHighChairStandards() = withContext(Dispatchers.IO) {
        // 插入标准
        highChairDatabase.highChairStandardDao().insertStandards(
            listOf(
                HighChairStandardsData.EN_14988_STANDARD,
                HighChairStandardsData.GB_29281_STANDARD
            )
        )
        
        // 插入年龄组
        highChairDatabase.highChairAgeGroupDao().insertAgeGroups(HighChairStandardsData.AGE_GROUPS)
        
        // 插入安全要求
        highChairDatabase.highChairSafetyRequirementDao().insertRequirements(HighChairStandardsData.SAFETY_REQUIREMENTS)
        
        // 插入稳定性数据
        highChairDatabase.highChairStabilityDao().insertStabilities(HighChairStandardsData.STABILITY_DATA)
        
        // 插入约束系统数据
        highChairDatabase.highChairRestraintDao().insertRestraints(HighChairStandardsData.RESTRAINT_DATA)
    }

    // ========== 儿童床相关操作 ==========

    /**
     * 获取所有活跃的儿童床标准
     */
    fun getAllActiveCribStandards() = cribDatabase.cribStandardDao().getAllActiveStandards()

    /**
     * 根据标准ID获取儿童床标准
     */
    suspend fun getCribStandardById(standardId: String) = 
        withContext(Dispatchers.IO) { cribDatabase.cribStandardDao().getStandardById(standardId) }

    /**
     * 根据地区获取儿童床标准
     */
    fun getCribStandardsByRegion(region: String) = 
        cribDatabase.cribStandardDao().getStandardsByRegion(region)

    /**
     * 获取标准下的尺寸要求
     */
    fun getCribDimensions(standardId: String) = 
        cribDatabase.cribDimensionDao().getDimensionsByStandard(standardId)

    /**
     * 根据类型获取尺寸要求
     */
    fun getCribDimensionsByType(standardId: String, type: String) = 
        cribDatabase.cribDimensionDao().getDimensionsByType(standardId, type)

    /**
     * 获取标准下的床垫间隙要求
     */
    fun getCribMattressGaps(standardId: String) = 
        cribDatabase.cribMattressGapDao().getGapsByStandard(standardId)

    /**
     * 获取标准下的栏杆要求
     */
    fun getCribRailings(standardId: String) = 
        cribDatabase.cribRailingDao().getRailingsByStandard(standardId)

    /**
     * 根据类型获取栏杆要求
     */
    fun getCribRailingsByType(standardId: String, type: String) = 
        cribDatabase.cribRailingDao().getRailingsByType(standardId, type)

    /**
     * 获取标准下的安全要求
     */
    fun getCribSafetyRequirements(standardId: String) = 
        cribDatabase.cribSafetyRequirementDao().getRequirementsByStandard(standardId)

    /**
     * 根据类别获取安全要求
     */
    fun getCribRequirementsByCategory(standardId: String, category: String) = 
        cribDatabase.cribSafetyRequirementDao().getRequirementsByCategory(standardId, category)

    /**
     * 初始化儿童床标准数据
     */
    suspend fun initializeCribStandards() = withContext(Dispatchers.IO) {
        // 插入标准
        cribDatabase.cribStandardDao().insertStandards(
            listOf(
                CribStandardsData.EN_716_STANDARD,
                CribStandardsData.GB_28007_STANDARD
            )
        )
        
        // 插入尺寸要求
        cribDatabase.cribDimensionDao().insertDimensions(CribStandardsData.DIMENSIONS)
        
        // 插入床垫间隙要求
        cribDatabase.cribMattressGapDao().insertGaps(CribStandardsData.MATTRESS_GAPS)
        
        // 插入栏杆要求
        cribDatabase.cribRailingDao().insertRailings(CribStandardsData.RAILINGS)
        
        // 插入安全要求
        cribDatabase.cribSafetyRequirementDao().insertRequirements(CribStandardsData.SAFETY_REQUIREMENTS)
    }

    companion object {
        @Volatile private var instance: StandardRepository? = null

        fun getInstance(context: Context): StandardRepository {
            return instance ?: synchronized(this) {
                instance ?: StandardRepository(
                    context,
                    EceR129Database.getDatabase(context),
                    HighChairDatabase.getDatabase(context),
                    CribDatabase.getDatabase(context)
                ).also { instance = it }
            }
        }

        /**
         * 清除实例（用于测试）
         */
        fun clearInstance() {
            instance = null
        }
    }
}

package com.childproduct.designassistant.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.childproduct.designassistant.database.CribDatabase
import com.childproduct.designassistant.database.EceR129Database
import com.childproduct.designassistant.database.FMVSSDatabase
import com.childproduct.designassistant.database.HighChairDatabase
import com.childproduct.designassistant.database.dao.*
import com.childproduct.designassistant.database.entity.*
import com.childproduct.designassistant.model.InstallDirection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

/**
 * 标准数据仓库（重构版 - 支持标准路由）
 * 
 * 修复说明：
 * - 新增FMVSSDatabase支持，实现ECE/FMVSS双标准路由
 * - 核心方法getDummyByStandardAndHeight按选中标准路由到对应数据库
 * - 彻底隔离：查ECE只走ECE库，查FMVSS只走FMVSS库
 * 
 * 使用原则：
 * - 必须传入选中标准（ECE_R129 / FMVSS_213 / GB_27887_2024）
 * - Repository层自动路由到对应数据库
 * - 杜绝跨标准的通用查询
 */
class StandardRepository private constructor(
    private val context: Context,
    private val eceR129Database: EceR129Database,
    private val fmvssDatabase: FMVSSDatabase,
    private val highChairDatabase: HighChairDatabase,
    private val cribDatabase: CribDatabase
) {

    // ========== 核心方法：按标准路由查询假人 ==========

    /**
     * 按「选中标准+身高」查询假人（彻底隔离）
     * 
     * 修复说明：
     * - 根据selectedStandard参数路由到对应数据库
     * - ECE标准：查EceR129Database，强制过滤standardType = 'ECE_R129'
     * - FMVSS标准：查FMVSSDatabase，强制过滤standardType = 'FMVSS_213'
     * - 非法标准：返回null
     * 
     * @param selectedStandard 选中的标准（ECE_R129/FMVSS_213/GB_27887_2024）
     * @param heightCm 身高（cm）
     * @return 匹配的假人，如果标准无效或无匹配则返回null
     */
    suspend fun getDummyByStandardAndHeight(
        selectedStandard: String,
        heightCm: Int
    ): CrashTestDummy? {
        return withContext(Dispatchers.IO) {
            when (selectedStandard) {
                // ECE R129标准：查ECE数据库，且强制过滤standardType
                "ECE_R129", "GB_27887_2024" -> {
                    android.util.Log.d("StandardRouting", "查询ECE数据库 - 标准: $selectedStandard, 身高: $heightCm")
                    eceR129Database.crashTestDummyDao()
                        .getDummyByStandardAndHeight("ECE_R129", heightCm)
                }
                
                // FMVSS 213标准：查FMVSS数据库，且强制过滤standardType
                "FMVSS_213" -> {
                    android.util.Log.d("StandardRouting", "查询FMVSS数据库 - 标准: $selectedStandard, 身高: $heightCm")
                    fmvssDatabase.fmvssDao()
                        .getDummyByHeight(heightCm)
                }
                
                // 非法标准直接返回null
                else -> {
                    android.util.Log.w("StandardRouting", "无效的标准类型: $selectedStandard")
                    null
                }
            }
        }
    }

    /**
     * 按标准查询所有假人（彻底隔离）
     * 
     * @param selectedStandard 选中的标准
     * @return 该标准的所有假人列表
     */
    suspend fun getAllDummiesByStandard(selectedStandard: String): List<CrashTestDummy> {
        return withContext(Dispatchers.IO) {
            when (selectedStandard) {
                "ECE_R129", "GB_27887_2024" -> {
                    eceR129Database.crashTestDummyDao()
                        .getDummiesByStandard("ECE_R129")
                }
                
                "FMVSS_213" -> {
                    fmvssDatabase.fmvssDao()
                        .getAllDummies()
                }
                
                else -> {
                    android.util.Log.w("StandardRouting", "无效的标准类型: $selectedStandard")
                    emptyList()
                }
            }
        }
    }

    /**
     * 按标准+安装方向查询假人（彻底隔离）
     * 
     * @param selectedStandard 选中的标准
     * @param direction 安装方向（REARWARD/FORWARD）
     * @return 匹配的假人列表
     */
    suspend fun getDummiesByStandardAndDirection(
        selectedStandard: String,
        direction: String
    ): List<CrashTestDummy> {
        return withContext(Dispatchers.IO) {
            when (selectedStandard) {
                "ECE_R129", "GB_27887_2024" -> {
                    eceR129Database.crashTestDummyDao()
                        .getDummiesByStandardAndDirection("ECE_R129", direction)
                }
                
                "FMVSS_213" -> {
                    // FMVSS只支持前向安装
                    if (direction == "FORWARD") {
                        fmvssDatabase.fmvssDao()
                            .getAllDummies()
                    } else {
                        emptyList()
                    }
                }
                
                else -> emptyList()
            }
        }
    }

    // ========== ECE R129专属操作（已废弃，推荐使用带标准参数的方法） ==========

    /**
     * 获取所有假人类型（已废弃 - 请使用getAllDummiesByStandard）
     * 
     * ⚠️ 警告：此方法返回所有标准的假人，可能导致标准混用
     * 💡 推荐：使用getAllDummiesByStandard(selectedStandard)
     */
    @Deprecated("请使用getAllDummiesByStandard(selectedStandard)以避免标准混用")
    fun getAllDummies(): LiveData<List<CrashTestDummy>> {
        return eceR129Database.crashTestDummyDao().getAllDummies()
    }

    /**
     * 根据身高获取适用假人（已废弃 - 请使用getDummyByStandardAndHeight）
     * 
     * ⚠️ 警告：此方法未按标准过滤，会返回所有标准的匹配假人
     * 💡 推荐：使用getDummyByStandardAndHeight(selectedStandard, heightCm)
     */
    @Deprecated("请使用getDummyByStandardAndHeight(selectedStandard, heightCm)以避免标准混用")
    suspend fun getDummyByHeight(heightCm: Int): CrashTestDummy? {
        return withContext(Dispatchers.IO) {
            eceR129Database.crashTestDummyDao().getDummyByHeightRange(heightCm)
        }
    }

    // ========== ECE R129安全阈值相关操作 ==========

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

    // ========== FMVSS安全阈值相关操作 ==========

    /**
     * 获取FMVSS阈值（按假人代码）
     * 
     * @param dummyCode 假人代码（如Q3s、HIII）
     * @return FMVSS阈值列表
     */
    suspend fun getFmvssThresholdsByDummy(dummyCode: String): List<FMVSSThresholdEntity> {
        return withContext(Dispatchers.IO) {
            fmvssDatabase.fmvssDao().getThresholdsByDummy(dummyCode)
        }
    }

    /**
     * 获取所有FMVSS阈值
     */
    suspend fun getAllFmvssThresholds(): List<FMVSSThresholdEntity> {
        return withContext(Dispatchers.IO) {
            fmvssDatabase.fmvssDao().getAllThresholds()
        }
    }

    // ========== ECE R129测试配置相关操作 ==========

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
            val allDummies = eceR129Database.crashTestDummyDao().getAllDummiesList()
            val dummies = allDummies.filter { dummy ->
                dummy.minHeightCm >= minHeight && dummy.maxHeightCm <= maxHeight
            }
            
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

    // ========== FMVSS测试配置相关操作 ==========

    /**
     * 获取FMVSS测试配置（按假人代码）
     * 
     * @param dummyCode 假人代码（如Q3s、HIII）
     * @return FMVSS测试配置列表
     */
    suspend fun getFmvssTestConfigsByDummy(dummyCode: String): List<FMVSSTestConfigEntity> {
        return withContext(Dispatchers.IO) {
            fmvssDatabase.fmvssDao().getTestConfigurationsByDummy(dummyCode)
        }
    }

    /**
     * 获取所有FMVSS测试配置
     */
    suspend fun getAllFmvssTestConfigs(): List<FMVSSTestConfigEntity> {
        return withContext(Dispatchers.IO) {
            fmvssDatabase.fmvssDao().getAllTestConfigurations()
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

    /**
     * 获取FMVSS标准信息
     */
    suspend fun getFmvssStandardInfo(): FMVSSStandardEntity? {
        return withContext(Dispatchers.IO) {
            fmvssDatabase.fmvssDao().getStandardById("FMVSS_213")
        }
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
     * 根据标准ID获取儿童床年龄组
     * 注意：cribAgeGroupDao() 方法在 CribDatabase 中不存在，暂时返回空列表
     * TODO: 需要实现实际的年龄组查询
     */
    fun getCribAgeGroups(standardId: String) = 
        flowOf<List<String>>(emptyList())  // 临时返回空列表，类型待定义

    /**
     * 获取标准下的儿童床安全要求
     */
    fun getCribSafetyRequirements(standardId: String) = 
        cribDatabase.cribSafetyRequirementDao().getRequirementsByStandard(standardId)

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
        
        // 年龄组和安全要求的插入需要根据实际情况调整
        // 暂时注释掉，因为这些字段在 CribStandardsData 中不存在
        // cribDatabase.cribAgeGroupDao().insertAgeGroups(CribStandardsData.AGE_GROUPS)
        
        // 插入安全要求
        cribDatabase.cribSafetyRequirementDao().insertRequirements(CribStandardsData.SAFETY_REQUIREMENTS)
        
        // 插入尺寸数据
        cribDatabase.cribDimensionDao().insertDimensions(CribStandardsData.DIMENSIONS)
    }

    // ========== 标准类型验证 ==========

    /**
     * 验证标准类型是否有效
     * 
     * @param standardType 标准类型
     * @return 是否有效
     */
    fun isValidStandardType(standardType: String): Boolean {
        return standardType in listOf("ECE_R129", "FMVSS_213", "GB_27887_2024")
    }

    // ========== 标准路由日志 ==========

    /**
     * 记录标准路由日志（用于调试）
     * 
     * @param standardType 标准类型
     * @param operation 操作类型
     * @param result 查询结果
     */
    private fun logStandardRouting(standardType: String, operation: String, result: Any?) {
        android.util.Log.d(
            "StandardRouting",
            "标准路由 - 类型: $standardType, 操作: $operation, 结果: ${result?.javaClass?.simpleName}"
        )
    }

    companion object {
        @Volatile private var INSTANCE: StandardRepository? = null

        fun getInstance(
            context: Context,
            eceR129Database: EceR129Database,
            fmvssDatabase: FMVSSDatabase,
            highChairDatabase: HighChairDatabase,
            cribDatabase: CribDatabase
        ): StandardRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = StandardRepository(
                    context,
                    eceR129Database,
                    fmvssDatabase,
                    highChairDatabase,
                    cribDatabase
                )
                INSTANCE = instance
                instance
            }
        }
    }
}

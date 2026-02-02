package com.childproduct.designassistant.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.childproduct.designassistant.database.EceR129Database
import com.childproduct.designassistant.database.dao.*
import com.childproduct.designassistant.database.entity.*
import com.childproduct.designassistant.model.EnhancedProductType.InstallDirection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 标准数据仓库
 * 提供对ECE R129标准数据的统一访问接口
 */
class StandardRepository private constructor(
    private val context: Context,
    private val database: EceR129Database
) {

    // ========== 假人相关操作 ==========

    /**
     * 获取所有假人类型
     */
    fun getAllDummies(): LiveData<List<CrashTestDummy>> {
        return database.crashTestDummyDao().getAllDummies()
    }

    /**
     * 根据身高获取适用假人
     */
    suspend fun getDummyByHeight(heightCm: Int): CrashTestDummy? {
        return withContext(Dispatchers.IO) {
            database.crashTestDummyDao().getDummyByHeightRange(heightCm)
        }
    }

    /**
     * 根据身高范围获取所有适用假人
     */
    suspend fun getDummiesByHeightRange(minHeight: Int, maxHeight: Int): List<CrashTestDummy> {
        return withContext(Dispatchers.IO) {
            val allDummies = database.crashTestDummyDao().getAllDummiesList()
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
        return database.safetyThresholdDao().getAllThresholds()
    }

    /**
     * 根据假人获取安全阈值
     */
    fun getSafetyThresholdsByDummy(dummyId: String): LiveData<List<SafetyThreshold>> {
        return database.safetyThresholdDao().getThresholdsByDummy(dummyId)
    }

    /**
     * 根据假人代码获取适用的安全阈值
     */
    suspend fun getThresholdsByDummyCode(dummyCode: String): List<SafetyThreshold> {
        return withContext(Dispatchers.IO) {
            database.safetyThresholdDao().getThresholdsApplicableToDummy(dummyCode)
        }
    }

    // ========== 测试配置相关操作 ==========

    /**
     * 获取所有测试配置
     */
    fun getAllTestConfigurations(): LiveData<List<TestConfiguration>> {
        return database.testConfigurationDao().getAllConfigurations()
    }

    /**
     * 根据假人和安装方向获取测试配置
     */
    fun getTestConfigurations(
        dummyCode: String,
        installDirection: String
    ): LiveData<List<TestConfiguration>> {
        return database.testConfigurationDao().getConfigurationsByDummyAndDirection(
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
                val dummyConfigs = database.testConfigurationDao()
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
            database.standardReferenceDao().getCurrentVersion("UN R129")
        }
    }

    /**
     * 获取所有标准引用
     */
    fun getAllStandards(): LiveData<List<StandardReference>> {
        return database.standardReferenceDao().getAllStandards()
    }

    // ========== 身高范围映射相关操作 ==========

    /**
     * 根据身高获取映射信息
     */
    suspend fun getHeightMapping(heightCm: Int): HeightRangeMapping? {
        return withContext(Dispatchers.IO) {
            database.heightRangeMappingDao().getMappingByHeight(heightCm)
        }
    }

    /**
     * 获取所有身高映射
     */
    fun getAllHeightMappings(): LiveData<List<HeightRangeMapping>> {
        return database.heightRangeMappingDao().getAllMappings()
    }

    // ========== 安装方式相关操作 ==========

    /**
     * 获取所有安装方式
     */
    fun getAllInstallationMethods(): LiveData<List<InstallationMethod>> {
        return database.installationMethodDao().getAllMethods()
    }

    // ========== 材料规格相关操作 ==========

    /**
     * 获取所有材料规格
     */
    fun getAllMaterialSpecifications(): LiveData<List<MaterialSpecification>> {
        return database.materialSpecificationDao().getAllSpecifications()
    }

    // ========== ISOFIX要求相关操作 ==========

    /**
     * 获取所有ISOFIX要求
     */
    fun getAllIsofixRequirements(): LiveData<List<IsofixRequirement>> {
        return database.isofixRequirementDao().getAllRequirements()
    }

    // ========== 同步日志相关操作 ==========

    /**
     * 获取最近的同步日志
     */
    fun getRecentSyncLogs(): LiveData<List<StandardUpdateLog>> {
        return database.standardUpdateLogDao().getRecentLogs()
    }

    /**
     * 获取最后同步时间
     */
    suspend fun getLastSyncTime(): Long? {
        return withContext(Dispatchers.IO) {
            database.standardUpdateLogDao().getLastSyncTime()
        }
    }

    companion object {
        @Volatile private var instance: StandardRepository? = null

        fun getInstance(context: Context): StandardRepository {
            return instance ?: synchronized(this) {
                instance ?: StandardRepository(
                    context,
                    EceR129Database.getDatabase(context)
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

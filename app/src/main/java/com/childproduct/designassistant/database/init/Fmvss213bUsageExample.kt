package com.childproduct.designassistant.database.init

import android.content.Context
import android.util.Log
import com.childproduct.designassistant.database.FMVSSDatabase
import com.childproduct.designassistant.database.dao.FMVSSDao
import com.childproduct.designassistant.database.entity.FMVSSTestConfigEntity
import com.childproduct.designassistant.database.entity.FMVSSDummyEntity
import com.childproduct.designassistant.database.entity.FMVSSThresholdEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * FMVSS 213b数据使用示例
 * 
 * 演示如何在业务代码中查询和使用FMVSS 213b数据
 */
class Fmvss213bUsageExample(private val context: Context) {
    
    companion object {
        private const val TAG = "FMVSS213bExample"
        private const val STANDARD_ID_213b = "FMVSS-213b-2026"
    }
    
    private val database: FMVSSDatabase = FMVSSDatabase.getInstance(context)
    private val dao: FMVSSDao = database.fmvssDao()
    
    /**
     * 示例1：初始化FMVSS 213b数据
     * 
     * 应在Application首次启动时调用
     */
    fun exampleInitializeData() {
        CoroutineScope(Dispatchers.IO).launch {
            val initializer = Fmvss213bDataInitializer(context, dao)
            initializer.initialize()
            Log.d(TAG, "FMVSS 213b数据初始化完成")
        }
    }
    
    /**
     * 示例2：查询FMVSS 213b所有假人
     */
    fun exampleGetAllDummies() {
        CoroutineScope(Dispatchers.IO).launch {
            val dummies = dao.getFmvss213bDummies()
            dummies.forEach { dummy ->
                Log.d(TAG, "假人: ${dummy.displayName}, 体重: ${dummy.weightKg}kg, 年龄: ${dummy.ageRange}")
            }
        }
    }
    
    /**
     * 示例3：查询正碰测试假人
     */
    fun exampleGetFrontImpactDummies() {
        CoroutineScope(Dispatchers.IO).launch {
            val dummies = dao.getFmvss213bDummiesByScenario("正碰")
            dummies.forEach { dummy ->
                Log.d(TAG, "正碰假人: ${dummy.displayName} (${dummy.dummyCode})")
                Log.d(TAG, "  - 体重: ${dummy.weightKg}kg")
                Log.d(TAG, "  - 适用标准: ${dummy.applicableStandards}")
            }
        }
    }
    
    /**
     * 示例4：查询特定假人的安全阈值
     */
    fun exampleGetDummyThresholds(dummyCode: String = "HIII-6YO") {
        CoroutineScope(Dispatchers.IO).launch {
            val thresholds = dao.getFmvss213bThresholdsByDummy(dummyCode)
            Log.d(TAG, "$dummyCode 的安全阈值:")
            thresholds.forEach { threshold ->
                Log.d(TAG, "  - ${threshold.criterion}: ${threshold.limitValue} ${threshold.unit}")
                Log.d(TAG, "    测试条件: ${threshold.testCondition}")
            }
        }
    }
    
    /**
     * 示例5：查询Type 2测试配置（FMVSS 213b新增）
     */
    fun exampleGetType2TestConfig() {
        CoroutineScope(Dispatchers.IO).launch {
            val config = dao.getFmvss213bType2TestConfig()
            if (config != null) {
                Log.d(TAG, "Type 2测试配置:")
                Log.d(TAG, "  - 测试名称: ${config.testName}")
                Log.d(TAG, "  - 速度要求: ${config.velocityRange}")
                Log.d(TAG, "  - 加速度曲线: ${config.accelerationProfile}")
                Log.d(TAG, "  - 伤害指标: ${config.injuryCriteria}")
                Log.d(TAG, "  - 特殊要求: ${config.specialRequirements}")
            } else {
                Log.w(TAG, "未找到Type 2测试配置")
            }
        }
    }
    
    /**
     * 示例6：查询侧碰测试配置
     */
    fun exampleGetSideImpactConfig() {
        CoroutineScope(Dispatchers.IO).launch {
            val config = dao.getFmvss213bSideImpactConfig()
            if (config != null) {
                Log.d(TAG, "侧碰测试配置:")
                Log.d(TAG, "  - 测试名称: ${config.testName}")
                Log.d(TAG, "  - 速度要求: ${config.velocityRange}")
                Log.d(TAG, "  - 适用假人: ${config.dummyTypes}")
                Log.d(TAG, "  - 伤害指标: ${config.injuryCriteria}")
                
                // 解析伤害指标JSON
                val injuryCriteria = config.injuryCriteria
                Log.d(TAG, "  - 详细伤害指标:")
                Log.d(TAG, "    * HIC限值: 查看JSON配置")
                Log.d(TAG, "    * 胸部压缩量限值: 查看JSON配置")
                Log.d(TAG, "    * 头部接触要求: 禁碰门板")
            }
        }
    }
    
    /**
     * 示例7：查询所有正碰测试配置
     */
    fun exampleGetFrontImpactConfigs() {
        CoroutineScope(Dispatchers.IO).launch {
            val configs = dao.getFmvss213bFrontImpactConfigs()
            Log.d(TAG, "FMVSS 213b正碰测试配置:")
            configs.forEach { config ->
                Log.d(TAG, "  - ${config.testName}")
                Log.d(TAG, "    配置ID: ${config.configId}")
                Log.d(TAG, "    速度: ${config.velocityRange}")
            }
        }
    }
    
    /**
     * 示例8：获取FMVSS 213b标准信息
     */
    fun exampleGetStandardInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val standard = dao.getFmvss213bStandard()
            if (standard != null) {
                Log.d(TAG, "FMVSS 213b标准信息:")
                Log.d(TAG, "  - 标准ID: ${standard.standardId}")
                Log.d(TAG, "  - 标准名称: ${standard.standardName}")
                Log.d(TAG, "  - 适用地区: ${standard.applicableRegion}")
                Log.d(TAG, "  - 适用体重: ${standard.applicableWeight}")
                Log.d(TAG, "  - 核心范围: ${standard.coreScope}")
                Log.d(TAG, "  - 生效日期: ${standard.effectiveDate}")
                Log.d(TAG, "  - 标准状态: ${standard.standardStatus}")
            }
        }
    }
    
    /**
     * 示例9：综合查询 - 检查产品是否符合FMVSS 213b要求
     */
    fun exampleCheckCompliance(dummyCode: String = "HIII-6YO") {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "========== FMVSS 213b合规性检查 ==========")
            
            // 1. 获取标准信息
            val standard = dao.getFmvss213bStandard()
            Log.d(TAG, "标准: ${standard?.standardName}")
            
            // 2. 获取假人信息
            val dummy = dao.getDummyByCode(dummyCode)
            if (dummy != null) {
                Log.d(TAG, "测试假人: ${dummy.displayName}")
                Log.d(TAG, "  - 体重: ${dummy.weightKg}kg")
                Log.d(TAG, "  - 年龄: ${dummy.ageRange}")
            }
            
            // 3. 获取安全阈值
            val thresholds = dao.getFmvss213bThresholdsByDummy(dummyCode)
            Log.d(TAG, "安全阈值要求:")
            thresholds.forEach { threshold ->
                Log.d(TAG, "  - ${threshold.criterion}: ${threshold.limitValue} ${threshold.unit}")
            }
            
            // 4. 获取测试配置
            val configs = dao.getFmvss213bFrontImpactConfigs()
            Log.d(TAG, "测试配置:")
            configs.forEach { config ->
                Log.d(TAG, "  - ${config.testName}")
                Log.d(TAG, "    速度: ${config.velocityRange}")
            }
            
            Log.d(TAG, "==========================================")
        }
    }
    
    /**
     * 示例10：产品设计建议查询
     * 
     * 根据用户输入的产品信息，查询FMVSS 213b相关数据
     */
    fun exampleQueryForDesign(targetWeightKg: Double, targetHeightCm: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "========== FMVSS 213b设计建议 ==========")
            Log.d(TAG, "目标体重: ${targetWeightKg}kg, 目标身高: ${targetHeightCm}cm")
            
            // 1. 查找适用的假人
            val dummies = dao.getFmvss213bDummies()
            val suitableDummies = dummies.filter { 
                it.weightKg in (targetWeightKg - 5)..(targetWeightKg + 5)
            }
            
            if (suitableDummies.isEmpty()) {
                Log.w(TAG, "未找到完全匹配的假人，建议使用最接近的假人")
            } else {
                Log.d(TAG, "推荐测试假人:")
                suitableDummies.forEach { dummy ->
                    Log.d(TAG, "  - ${dummy.displayName} (${dummy.weightKg}kg)")
                    
                    // 查询该假人的阈值
                    val thresholds = dao.getFmvss213bThresholdsByDummy(dummy.dummyCode)
                    Log.d(TAG, "    安全阈值:")
                    thresholds.forEach { threshold ->
                        Log.d(TAG, "      ${threshold.criterion}: ${threshold.limitValue} ${threshold.unit}")
                    }
                }
            }
            
            // 2. 查询测试要求
            val type2Config = dao.getFmvss213bType2TestConfig()
            if (type2Config != null) {
                Log.d(TAG, "\nType 2测试要求:")
                Log.d(TAG, "  - 速度: ${type2Config.velocityRange}")
                Log.d(TAG, "  - 加速度曲线: ${type2Config.accelerationProfile}")
            }
            
            Log.d(TAG, "==========================================")
        }
    }
}

package com.childproduct.designassistant.database.init

import android.content.Context
import com.childproduct.designassistant.database.dao.FMVSSDao
import com.childproduct.designassistant.database.entity.*

/**
 * FMVSS 213b（2026版）数据初始化器
 * 
 * 功能：
 * - 初始化FMVSS 213b标准数据
 * - 插入假人参数、安全阈值、测试配置等核心数据
 * - 支持美标最新要求（禁用Hybrid II 6YO、新增Type 2测试等）
 * 
 * 参考标准：
 * - FMVSS 213b (2026版)
 * - 假人类型：HIII-3YO、HIII-6YO、HIII-10YO、Q3S
 * - 测试类型：Type 1（腰带）、Type 2（腰带+肩带）
 */
class Fmvss213bDataInitializer(
    private val context: Context,
    private val dao: FMVSSDao
) {

    companion object {
        private const val STANDARD_ID_213b = "FMVSS-213b-2026"
        private const val STANDARD_NAME = "FMVSS 213b (2026版)"
        
        // FMVSS 213b适用场景
        private const val SCENARIO_FRONT_IMPACT = "正碰"
        private const val SCENARIO_SIDE_IMPACT = "侧碰"
        
        // 测试类型
        private const val TEST_TYPE_1 = "Type 1"
        private const val TEST_TYPE_2 = "Type 2"
    }

    /**
     * 初始化FMVSS 213b数据
     * 
     * 执行流程：
     * 1. 检查是否已初始化
     * 2. 插入标准信息
     * 3. 插入假人数据
     * 4. 插入安全阈值数据
     * 5. 插入测试配置数据
     */
    suspend fun initialize() {
        // 检查标准是否已存在
        val existingStandard = dao.getStandardById(STANDARD_ID_213b)
        if (existingStandard != null) {
            return // 已初始化，跳过
        }

        // 1. 插入标准信息
        insertStandard()

        // 2. 插入假人数据
        insertDummies()

        // 3. 插入安全阈值数据
        insertSafetyThresholds()

        // 4. 插入测试配置数据
        insertTestConfigurations()
    }

    /**
     * 插入FMVSS 213b标准信息
     */
    private suspend fun insertStandard() {
        val standard = FMVSSStandardEntity(
            standardId = STANDARD_ID_213b,
            standardName = STANDARD_NAME,
            standardType = "CRS安全测试",
            applicableRegion = "美国",
            applicableWeight = "10kg+（提篮至增高座）",
            applicableAge = "新生儿至12岁",
            coreScope = "儿童约束系统正面碰撞、侧面碰撞安全性能测试",
            effectiveDate = "2026-01-01",
            standardStatus = "即将生效",
            dataSource = "NHTSA FMVSS 213b Final Rule",
            lastUpdated = System.currentTimeMillis()
        )
        dao.insertOrUpdateStandard(standard)
    }

    /**
     * 插入假人数据
     * 
     * FMVSS 213b支持的假人：
     * - HIII-3YO: 正碰测试
     * - HIII-6YO: 正碰测试（替代Hybrid II 6YO）
     * - HIII-10YO: 正碰测试
     * - Q3S: 侧碰测试（Q3.5的简化版）
     */
    private suspend fun insertDummies() {
        val dummies = listOf(
            // HIII-3YO（3岁儿童假人）
            FMVSSDummyEntity(
                dummyCode = "HIII-3YO",
                displayName = "Hybrid III 3岁儿童假人",
                weightLbs = 33.0,
                weightKg = 15.0,
                ageRange = "3岁",
                applicableStandards = """
                    {
                        "standards": ["FMVSS-213b-2026"],
                        "scenarios": ["正碰"],
                        "replaces": [],
                        "notes": "用于正碰测试"
                    }
                """.trimIndent()
            ),
            
            // HIII-6YO（6岁儿童假人）- 替代Hybrid II 6YO
            FMVSSDummyEntity(
                dummyCode = "HIII-6YO",
                displayName = "Hybrid III 6岁儿童假人",
                weightLbs = 45.0,
                weightKg = 20.4,
                ageRange = "6岁",
                applicableStandards = """
                    {
                        "standards": ["FMVSS-213b-2026"],
                        "scenarios": ["正碰"],
                        "replaces": ["Hybrid II 6YO"],
                        "notes": "FMVSS 213b新增，替代Hybrid II 6YO假人"
                    }
                """.trimIndent()
            ),
            
            // HIII-10YO（10岁儿童假人）
            FMVSSDummyEntity(
                dummyCode = "HIII-10YO",
                displayName = "Hybrid III 10岁儿童假人",
                weightLbs = 66.0,
                weightKg = 30.0,
                ageRange = "10岁",
                applicableStandards = """
                    {
                        "standards": ["FMVSS-213b-2026"],
                        "scenarios": ["正碰"],
                        "replaces": [],
                        "notes": "用于正碰测试"
                    }
                """.trimIndent()
            ),
            
            // Q3S（侧碰假人）
            FMVSSDummyEntity(
                dummyCode = "Q3S",
                displayName = "Q3S侧碰假人",
                weightLbs = 33.0,
                weightKg = 15.0,
                ageRange = "3岁",
                applicableStandards = """
                    {
                        "standards": ["FMVSS-213b-2026"],
                        "scenarios": ["侧碰"],
                        "replaces": [],
                        "notes": "FMVSS 213b专用侧碰假人"
                    }
                """.trimIndent()
            )
        )
        
        dummies.forEach { dummy ->
            dao.insertOrUpdateDummy(dummy)
        }
    }

    /**
     * 插入安全阈值数据
     * 
     * 包含：
     * - HIC限值（不同假人有不同窗口）
     * - 胸部加速度限值
     * - 胸部压缩量限值（侧碰）
     * - 头部位移限值
     */
    private suspend fun insertSafetyThresholds() {
        val thresholds = listOf(
            // HIII-3YO 正碰阈值
            FMVSSThresholdEntity(
                thresholdId = "${STANDARD_ID_213b}-HIII-3YO-HIC",
                standardId = STANDARD_ID_213b,
                dummyCode = "HIII-3YO",
                criterion = "HIC（头部损伤指标）",
                limitValue = 1000.0,
                unit = "HIC",
                testCondition = "正碰，0-175ms窗口"
            ),
            FMVSSThresholdEntity(
                thresholdId = "${STANDARD_ID_213b}-HIII-3YO-CHEST-ACCEL",
                standardId = STANDARD_ID_213b,
                dummyCode = "HIII-3YO",
                criterion = "胸部加速度",
                limitValue = 60.0,
                unit = "g",
                testCondition = "正碰，3ms持续时间"
            ),
            FMVSSThresholdEntity(
                thresholdId = "${STANDARD_ID_213b}-HIII-3YO-HEAD-EXCURSION",
                standardId = STANDARD_ID_213b,
                dummyCode = "HIII-3YO",
                criterion = "头部位移",
                limitValue = 813.0,
                unit = "mm",
                testCondition = "正碰"
            ),
            
            // HIII-6YO 正碰阈值
            FMVSSThresholdEntity(
                thresholdId = "${STANDARD_ID_213b}-HIII-6YO-HIC",
                standardId = STANDARD_ID_213b,
                dummyCode = "HIII-6YO",
                criterion = "HIC（头部损伤指标）",
                limitValue = 1000.0,
                unit = "HIC",
                testCondition = "正碰，0-175ms窗口"
            ),
            FMVSSThresholdEntity(
                thresholdId = "${STANDARD_ID_213b}-HIII-6YO-CHEST-ACCEL",
                standardId = STANDARD_ID_213b,
                dummyCode = "HIII-6YO",
                criterion = "胸部加速度",
                limitValue = 60.0,
                unit = "g",
                testCondition = "正碰，3ms持续时间"
            ),
            FMVSSThresholdEntity(
                thresholdId = "${STANDARD_ID_213b}-HIII-6YO-HEAD-EXCURSION",
                standardId = STANDARD_ID_213b,
                dummyCode = "HIII-6YO",
                criterion = "头部位移",
                limitValue = 813.0,
                unit = "mm",
                testCondition = "正碰"
            ),
            
            // HIII-10YO 正碰阈值
            FMVSSThresholdEntity(
                thresholdId = "${STANDARD_ID_213b}-HIII-10YO-HIC",
                standardId = STANDARD_ID_213b,
                dummyCode = "HIII-10YO",
                criterion = "HIC（头部损伤指标）",
                limitValue = 1000.0,
                unit = "HIC",
                testCondition = "正碰，0-175ms窗口"
            ),
            FMVSSThresholdEntity(
                thresholdId = "${STANDARD_ID_213b}-HIII-10YO-CHEST-ACCEL",
                standardId = STANDARD_ID_213b,
                dummyCode = "HIII-10YO",
                criterion = "胸部加速度",
                limitValue = 60.0,
                unit = "g",
                testCondition = "正碰，3ms持续时间"
            ),
            FMVSSThresholdEntity(
                thresholdId = "${STANDARD_ID_213b}-HIII-10YO-HEAD-EXCURSION",
                standardId = STANDARD_ID_213b,
                dummyCode = "HIII-10YO",
                criterion = "头部位移",
                limitValue = 813.0,
                unit = "mm",
                testCondition = "正碰"
            ),
            
            // Q3S 侧碰阈值
            FMVSSThresholdEntity(
                thresholdId = "${STANDARD_ID_213b}-Q3S-HIC",
                standardId = STANDARD_ID_213b,
                dummyCode = "Q3S",
                criterion = "HIC（头部损伤指标）",
                limitValue = 570.0,
                unit = "HIC",
                testCondition = "侧碰"
            ),
            FMVSSThresholdEntity(
                thresholdId = "${STANDARD_ID_213b}-Q3S-CHEST-COMPRESSION",
                standardId = STANDARD_ID_213b,
                dummyCode = "Q3S",
                criterion = "胸部压缩量",
                limitValue = 23.0,
                unit = "mm",
                testCondition = "侧碰"
            ),
            FMVSSThresholdEntity(
                thresholdId = "${STANDARD_ID_213b}-Q3S-HEAD-CONTACT",
                standardId = STANDARD_ID_213b,
                dummyCode = "Q3S",
                criterion = "头部接触",
                limitValue = 0.0,
                unit = "binary",
                testCondition = "侧碰，禁碰门板"
            )
        )
        
        thresholds.forEach { threshold ->
            dao.insertOrUpdateThreshold(threshold)
        }
    }

    /**
     * 插入测试配置数据
     * 
     * FMVSS 213b测试配置：
     * - Type 1: 腰带测试
     * - Type 2: 腰带+肩带测试（213b新增）
     * - 侧碰测试配置
     */
    private suspend fun insertTestConfigurations() {
        val configs = listOf(
            // Type 1 正碰测试（仅腰带）
            FMVSSTestConfigEntity(
                configId = "${STANDARD_ID_213b}-TYPE1-FRONT",
                testName = "Type 1 正碰测试（仅腰带）",
                standardId = STANDARD_ID_213b,
                impactType = "正面碰撞",
                velocityKmh = 48.0,
                velocityRange = "48±3.2 km/h (30±2 mph)",
                accelerationProfile = """
                    {
                        "time_points": [0, 10, 52, 90],
                        "accelerations": [3, 25, 25, 0],
                        "unit": "g"
                    }
                """.trimIndent(),
                dummyTypes = """
                    {
                        "available_dummies": ["HIII-3YO", "HIII-6YO", "HIII-10YO"],
                        "default_dummy": "HIII-6YO"
                    }
                """.trimIndent(),
                injuryCriteria = """
                    {
                        "HIC": {"limit": 1000, "window": "0-175ms"},
                        "chest_accel": {"limit": 60, "window": "3ms"},
                        "head_excursion": {"limit": 813, "unit": "mm"}
                    }
                """.trimIndent(),
                excursionLimits = """
                    {
                        "head": {"limit": 813, "unit": "mm"},
                        "knee": {"limit": 915, "unit": "mm"}
                    }
                """.trimIndent(),
                specialRequirements = """
                    {
                        "installation": {
                            "method": "安全带",
                            "belt_tension": "53.5-67N",
                            "shoulder_belt": "不使用"
                        },
                        "test_bench": {
                            "type": "FISA",
                            "foam_hardness": "20.4-24.9kg",
                            "foam_thickness": "51mm"
                        }
                    }
                """.trimIndent()
            ),
            
            // Type 2 正碰测试（腰带+肩带，213b新增）
            FMVSSTestConfigEntity(
                configId = "${STANDARD_ID_213b}-TYPE2-FRONT",
                testName = "Type 2 正碰测试（腰带+肩带）",
                standardId = STANDARD_ID_213b,
                impactType = "正面碰撞",
                velocityKmh = 48.0,
                velocityRange = "48±3.2 km/h (30±2 mph)",
                accelerationProfile = """
                    {
                        "time_points": [0, 10, 52, 90],
                        "accelerations": [3, 25, 25, 0],
                        "unit": "g"
                    }
                """.trimIndent(),
                dummyTypes = """
                    {
                        "available_dummies": ["HIII-3YO", "HIII-6YO", "HIII-10YO"],
                        "default_dummy": "HIII-6YO",
                        "notes": "Type 2锚点兼容性要求"
                    }
                """.trimIndent(),
                injuryCriteria = """
                    {
                        "HIC": {"limit": 1000, "window": "0-175ms"},
                        "chest_accel": {"limit": 60, "window": "3ms"},
                        "head_excursion": {"limit": 813, "unit": "mm"}
                    }
                """.trimIndent(),
                excursionLimits = """
                    {
                        "head": {"limit": 813, "unit": "mm"},
                        "knee": {"limit": 915, "unit": "mm"}
                    }
                """.trimIndent(),
                specialRequirements = """
                    {
                        "installation": {
                            "method": "安全带",
                            "belt_tension": "53.5-67N",
                            "shoulder_belt_tension": "9-18N",
                            "type2_anchors": "必需"
                        },
                        "test_bench": {
                            "type": "FISA",
                            "foam_hardness": "20.4-24.9kg",
                            "foam_thickness": "51mm",
                            "type2_compatible": true
                        },
                        "fmvss213b_specific": {
                            "test_type": "Type 2",
                            "requires_shoulder_belt": true,
                            "replaces": "Type 1 for some configurations"
                        }
                    }
                """.trimIndent()
            ),
            
            // 侧碰测试配置
            FMVSSTestConfigEntity(
                configId = "${STANDARD_ID_213b}-SIDE-IMPACT",
                testName = "FMVSS 213b 侧碰测试",
                standardId = STANDARD_ID_213b,
                impactType = "侧面碰撞",
                velocityKmh = 32.0,
                velocityRange = "32±2 km/h (20±1.2 mph)",
                accelerationProfile = """
                    {
                        "time_points": [0, 15, 30, 45],
                        "accelerations": [5, 30, 30, 0],
                        "unit": "g"
                    }
                """.trimIndent(),
                dummyTypes = """
                    {
                        "available_dummies": ["Q3S"],
                        "default_dummy": "Q3S",
                        "notes": "Q3S为FMVSS 213b专用侧碰假人"
                    }
                """.trimIndent(),
                injuryCriteria = """
                    {
                        "HIC": {"limit": 570, "window": "full"},
                        "chest_compression": {"limit": 23, "unit": "mm"},
                        "head_contact": {"limit": "prohibited", "target": "door_panel"}
                    }
                """.trimIndent(),
                excursionLimits = """
                    {
                        "head": {"limit": "no_contact", "notes": "禁碰门板"},
                        "chest": {"limit": 23, "unit": "mm", "type": "compression"}
                    }
                """.trimIndent(),
                specialRequirements = """
                    {
                        "installation": {
                            "method": ["LATCH", "安全带", "ISOFIX"],
                            "belt_tension": "53.5-67N"
                        },
                        "test_bench": {
                            "type": "SISA",
                            "door_panel": "标准配置",
                            "foam_hardness": "20.4-24.9kg"
                        },
                        "fmvss213b_specific": {
                            "dummy_type": "Q3S",
                            "impact_direction": "侧向",
                            "door_panel_contact": "prohibited"
                        }
                    }
                """.trimIndent()
            )
        )
        
        configs.forEach { config ->
            dao.insertOrUpdateTestConfiguration(config)
        }
    }
}

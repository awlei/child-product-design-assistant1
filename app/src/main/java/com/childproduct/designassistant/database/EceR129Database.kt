package com.childproduct.designassistant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.childproduct.designassistant.database.converter.Converters
import com.childproduct.designassistant.database.dao.*
import com.childproduct.designassistant.database.entity.*
// 移除通配符导入，只导入实际需要的类

/**
 * ECE R129标准数据库
 * 基于UN R129 Rev.4标准
 */
@Database(
    entities = [
        StandardUpdateLog::class  // 只保留最简单的实体
    ],
    version = 6,
    exportSchema = true
)
// @TypeConverters(Converters::class)  // 临时注释以测试
abstract class EceR129Database : RoomDatabase() {

    // ECE R129 相关 DAO
    // abstract fun crashTestDummyDao(): CrashTestDummyDao
    // abstract fun heightRangeMappingDao(): HeightRangeMappingDao
    // abstract fun safetyThresholdDao(): SafetyThresholdDao
    // abstract fun testConfigurationDao(): TestConfigurationDao
    // abstract fun standardReferenceDao(): StandardReferenceDao
    // abstract fun installationMethodDao(): InstallationMethodDao
    // abstract fun materialSpecificationDao(): MaterialSpecificationDao
    // abstract fun isofixRequirementDao(): IsofixRequirementDao
    abstract fun standardUpdateLogDao(): StandardUpdateLogDao

    companion object {
        @Volatile private var INSTANCE: EceR129Database? = null

        fun getDatabase(context: Context): EceR129Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EceR129Database::class.java,
                    "ece_r129_database"
                )
                    .fallbackToDestructiveMigration()  // 开发阶段允许破坏性迁移
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * 关闭数据库连接
         */
        fun closeDatabase() {
            INSTANCE?.close()
            INSTANCE = null
        }

        // ===== 静态标准数据（从 EceR129StandardDatabase 合并而来） =====

        /**
         * 核心设计参数（关联标准条款）
         */
        data class CoreDesignParameter(
            val parameterName: String,
            val value: String,
            val unit: String,
            val standardClause: String,
            val description: String
        )

        /**
         * 材料标准标注
         */
        data class MaterialStandard(
            val materialName: String,
            val standard: String,
            val requirement: String,
            val application: String
        )

        /**
         * 测试矩阵项
         */
        data class TestMatrixItem(
            val testName: String,
            val standardClause: String,
            val testMethod: String,
            val dummyType: String,
            val acceptanceCriteria: String
        )

        /**
         * 尺寸阈值
         */
        data class DimensionalThreshold(
            val dimensionType: String,
            val limit: String,
            val unit: String,
            val standardClause: String,
            val description: String
        )

        /**
         * 产品配置信息
         */
        data class ProductConfigurationInfo(
            val configType: String,
            val description: String,
            val standardClause: String?,
            val installationRequirement: String
        )

        /**
         * 标准概述
         */
        data class StandardOverview(
            val standardName: String,
            val revision: String,
            val effectiveDate: String,
            val scope: String,
            val productTypes: Int,
            val heightRange: String,
            val weightRange: String,
            val ageRange: String,
            val keyFeatures: List<String>
        )

        // ===== EceR129StandardDatabase 原有数据 =====

        // 核心设计参数
        val CORE_PARAMETERS = listOf(
            CoreDesignParameter(
                parameterName = "头托调节范围",
                value = "10-30",
                unit = "cm",
                standardClause = "ECE R129 §5.4.2",
                description = "头托可调节范围，需覆盖40-150cm身高区间"
            ),
            CoreDesignParameter(
                parameterName = "ISOFIX接口间距",
                value = "280",
                unit = "mm",
                standardClause = "GB 27887-2024 §5.5",
                description = "ISOFIX锚点中心距，允许偏差±5mm"
            ),
            CoreDesignParameter(
                parameterName = "ISOFIX上拉带挂钩高度",
                value = "500-1200",
                unit = "mm",
                standardClause = "ECE R129 §5.5.1",
                description = "上拉带挂钩相对于锚点的高度范围"
            ),
            CoreDesignParameter(
                parameterName = "支撑腿长度",
                value = "280-450",
                unit = "mm",
                standardClause = "ECE R129 §5.5.3",
                description = "支撑腿可调节长度范围"
            ),
            CoreDesignParameter(
                parameterName = "侧翼内宽",
                value = "≥380",
                unit = "mm",
                standardClause = "ECE R129 §5.3.3",
                description = "侧面碰撞防护结构最小宽度"
            ),
            CoreDesignParameter(
                parameterName = "座椅靠背角度（后向）",
                value = "135-150",
                unit = "度",
                standardClause = "ECE R129 §5.4.1",
                description = "后向安装时座椅靠背角度范围"
            ),
            CoreDesignParameter(
                parameterName = "座椅靠背角度（前向）",
                value = "15-30",
                unit = "度",
                standardClause = "ECE R129 §5.4.1",
                description = "前向安装时座椅靠背角度范围（相对于垂直线）"
            ),
            CoreDesignParameter(
                parameterName = "安全带织带宽度",
                value = "25-30",
                unit = "mm",
                standardClause = "GB 6095-2021 §4.2",
                description = "安全带织带标准宽度"
            ),
            CoreDesignParameter(
                parameterName = "安全带锁止强度",
                value = "≥5",
                unit = "kN",
                standardClause = "GB 6095-2021 §4.3",
                description = "安全带锁止机构最小承受力"
            )
        )

        // 材料标准（使用 R129r4eStandardDatabase 的 MaterialStandardRequirement）
        val MATERIAL_STANDARDS = listOf(
            MaterialStandard(
                materialName = "PP塑料（座椅主体）",
                standard = "GB 6675.4-2014",
                requirement = "可迁移元素限值：铅≤90mg/kg，镉≤75mg/kg",
                application = "座椅主体、头托、底座"
            ),
            MaterialStandard(
                materialName = "ABS塑料（结构部件）",
                standard = "GB 6675.4-2014",
                requirement = "可迁移元素限值：符合GB 6675所有要求",
                application = "底座骨架、连接件"
            ),
            MaterialStandard(
                materialName = "EPS吸能材料",
                standard = "GB/T 10801.1-2021",
                requirement = "密度≥30kg/m³，抗压强度≥150kPa",
                application = "侧面防护、头部保护"
            ),
            MaterialStandard(
                materialName = "EPP吸能材料",
                standard = "GB/T 10801.2-2021",
                requirement = "密度≥45kg/m³，能量吸收≥30%",
                application = "侧翼、后背板"
            ),
            MaterialStandard(
                materialName = "安全带织带",
                standard = "GB 6095-2021",
                requirement = "断裂强度≥5kN，延伸率≤10%（在5kN力作用下）",
                application = "五点式安全带、肩带"
            ),
            MaterialStandard(
                materialName = "安全带插扣",
                standard = "GB 6675.3-2014",
                requirement = "锁止力≥5kN，解锁力≤300N",
                application = "安全带卡扣"
            ),
            MaterialStandard(
                materialName = "座椅面料",
                standard = "GB 18401 B类",
                requirement = "甲醛含量≤75mg/kg，pH值4.0-7.5",
                application = "座椅表面、头枕套"
            ),
            MaterialStandard(
                materialName = "阻燃面料",
                standard = "GB 6675.3-2014",
                requirement = "垂直燃烧速度≤100mm/min",
                application = "易燃区域面料"
            )
        )

        // ===== R129r4eStandardDatabase 的数据 =====

        // R129r4e 关键阈值
        val THRESHOLDS = R129r4eThresholds(
            maxRearwardHeight = 83.0,
            minForwardHeight = 76.0,
            minBoosterHeight = 100.0,
            minBoosterUpperLimit = 105.0,
            boosterHeadProtection = 135.0,
            mandatoryRearwardMonths = 15
        )

        // ECRS分类定义
        val ECRS_CLASSIFICATIONS = listOf(
            ECRSClassification(
                classificationType = ECRSType.INTEGRAL_ISOFIX_UNIVERSAL,
                description = "整体式通用ISOFIX型(i-Size)",
                heightRange = "40-150cm",
                weightRange = "3.47-35.58kg",
                installationMethod = "ISOFIX + 防旋转装置",
                features = listOf(
                    "适配所有i-Size车辆座椅位置",
                    "儿童仅通过ECRS自身组件约束",
                    "五点式安全带或冲击盾"
                )
            ),
            ECRSClassification(
                classificationType = ECRSType.INTEGRAL_ISOFIX_SPECIFIC,
                description = "整体式特定车型ISOFIX型",
                heightRange = "40-150cm",
                weightRange = "3.47-35.58kg",
                installationMethod = "ISOFIX + 防旋转装置",
                features = listOf(
                    "仅适配指定车辆列表",
                    "车辆结构需满足安装要求",
                    "需提供车辆适配证明"
                )
            ),
            ECRSClassification(
                classificationType = ECRSType.NON_INTEGRAL_UNIVERSAL,
                description = "非整体式通用型(i-Size增高座)",
                heightRange = "100-150cm",
                weightRange = "15-36kg",
                installationMethod = "车辆安全带",
                features = listOf(
                    "儿童约束依赖车辆安全带",
                    "提供辅助支撑和定位",
                    "头部保护至135cm"
                )
            ),
            ECRSClassification(
                classificationType = ECRSType.INTEGRAL_BELT_UNIVERSAL,
                description = "整体式通用安全带固定型",
                heightRange = "40-150cm",
                weightRange = "3.47-35.58kg",
                installationMethod = "车辆安全带",
                features = listOf(
                    "通过车辆安全带固定",
                    "内置儿童约束系统",
                    "无需ISOFIX接口"
                )
            )
        )

        // 假人规格数据(Annex 8)
        val DUMMY_SPECS = listOf(
            DummySpec(
                dummyType = "Q0",
                statureRange = "≤60cm",
                mass = 3.47,
                massTolerance = 0.21,
                keyDimensions = DummyKeyDimensions(
                    sittingHeight = 355.0,
                    sittingHeightTolerance = 9.0,
                    shoulderBreadth = 145.0,
                    shoulderBreadthTolerance = 5.0,
                    hipBreadth = 142.0,
                    hipBreadthTolerance = 5.0,
                    abdomenDepth = null,
                    thighThickness = null
                ),
                injuryCriteria = InjuryCriteria(
                    headAcceleration3ms = HeadAccelerationThreshold(75.0, 80.0),
                    hpc = HPThreshold(600, 800),
                    chestAcceleration3ms = 55.0,
                    abdominalPressure = null,
                    neckForces = NeckForceThreshold(750.0, 445.0),
                    neckMoments = NeckMomentThreshold(14.3, 9.3)
                ),
                applicableTests = listOf("正面碰撞", "后向碰撞", "侧面碰撞")
            ),
            DummySpec(
                dummyType = "Q1",
                statureRange = "60-75cm",
                mass = 9.6,
                massTolerance = 0.80,
                keyDimensions = DummyKeyDimensions(
                    sittingHeight = 479.0,
                    sittingHeightTolerance = 9.0,
                    shoulderBreadth = 227.0,
                    shoulderBreadthTolerance = 7.0,
                    hipBreadth = 191.0,
                    hipBreadthTolerance = 7.0,
                    abdomenDepth = null,
                    thighThickness = null
                ),
                injuryCriteria = InjuryCriteria(
                    headAcceleration3ms = HeadAccelerationThreshold(75.0, 80.0),
                    hpc = HPThreshold(600, 800),
                    chestAcceleration3ms = 55.0,
                    abdominalPressure = null,
                    neckForces = NeckForceThreshold(750.0, 445.0),
                    neckMoments = NeckMomentThreshold(14.3, 9.3)
                ),
                applicableTests = listOf("正面碰撞", "后向碰撞", "侧面碰撞")
            ),
            DummySpec(
                dummyType = "Q1.5",
                statureRange = "75-87cm",
                mass = 11.43,
                massTolerance = 0.70,
                keyDimensions = DummyKeyDimensions(
                    sittingHeight = 498.0,
                    sittingHeightTolerance = 9.0,
                    shoulderBreadth = 259.0,
                    shoulderBreadthTolerance = 7.0,
                    hipBreadth = 200.0,
                    hipBreadthTolerance = 7.0,
                    abdomenDepth = 52.0,
                    thighThickness = 58.0
                ),
                injuryCriteria = InjuryCriteria(
                    headAcceleration3ms = HeadAccelerationThreshold(75.0, 80.0),
                    hpc = HPThreshold(600, 800),
                    chestAcceleration3ms = 55.0,
                    abdominalPressure = AbdomenPressureThreshold(1.2, 1.0, 1.2),
                    neckForces = NeckForceThreshold(750.0, 445.0),
                    neckMoments = NeckMomentThreshold(14.3, 9.3)
                ),
                applicableTests = listOf("正面碰撞", "侧面碰撞")
            ),
            DummySpec(
                dummyType = "Q3",
                statureRange = "87-105cm",
                mass = 14.59,
                massTolerance = 0.70,
                keyDimensions = DummyKeyDimensions(
                    sittingHeight = 544.0,
                    sittingHeightTolerance = 9.0,
                    shoulderBreadth = 259.0,
                    shoulderBreadthTolerance = 7.0,
                    hipBreadth = 200.0,
                    hipBreadthTolerance = 7.0,
                    abdomenDepth = 52.0,
                    thighThickness = 58.0
                ),
                injuryCriteria = InjuryCriteria(
                    headAcceleration3ms = HeadAccelerationThreshold(75.0, 80.0),
                    hpc = HPThreshold(600, 800),
                    chestAcceleration3ms = 55.0,
                    abdominalPressure = AbdomenPressureThreshold(1.2, 1.0, 1.2),
                    neckForces = NeckForceThreshold(750.0, 445.0),
                    neckMoments = NeckMomentThreshold(14.3, 9.3)
                ),
                applicableTests = listOf("正面碰撞", "侧面碰撞")
            ),
            DummySpec(
                dummyType = "Q6",
                statureRange = "105-125cm",
                mass = 22.93,
                massTolerance = 1.00,
                keyDimensions = DummyKeyDimensions(
                    sittingHeight = 632.0,
                    sittingHeightTolerance = 9.0,
                    shoulderBreadth = 294.0,
                    shoulderBreadthTolerance = 7.0,
                    hipBreadth = 232.0,
                    hipBreadthTolerance = 7.0,
                    abdomenDepth = 52.0,
                    thighThickness = 58.0
                ),
                injuryCriteria = InjuryCriteria(
                    headAcceleration3ms = HeadAccelerationThreshold(75.0, 80.0),
                    hpc = HPThreshold(600, 800),
                    chestAcceleration3ms = 55.0,
                    abdominalPressure = AbdomenPressureThreshold(1.2, 1.0, 1.2),
                    neckForces = NeckForceThreshold(750.0, 445.0),
                    neckMoments = NeckMomentThreshold(14.3, 9.3)
                ),
                applicableTests = listOf("正面碰撞", "侧面碰撞")
            ),
            DummySpec(
                dummyType = "Q10",
                statureRange = "125-150cm",
                mass = 35.58,
                massTolerance = 1.39,
                keyDimensions = DummyKeyDimensions(
                    sittingHeight = 733.7,
                    sittingHeightTolerance = 9.0,
                    shoulderBreadth = 334.8,
                    shoulderBreadthTolerance = 7.0,
                    hipBreadth = 270.0,
                    hipBreadthTolerance = 7.0,
                    abdomenDepth = 52.0,
                    thighThickness = 58.0
                ),
                injuryCriteria = InjuryCriteria(
                    headAcceleration3ms = HeadAccelerationThreshold(75.0, 80.0),
                    hpc = HPThreshold(600, 800),
                    chestAcceleration3ms = 55.0,
                    abdominalPressure = AbdomenPressureThreshold(1.2, 1.0, 1.2),
                    neckForces = NeckForceThreshold(750.0, 445.0),
                    neckMoments = NeckMomentThreshold(14.3, 9.3)
                ),
                applicableTests = listOf("正面碰撞", "侧面碰撞")
            )
        )

        // 防旋转装置要求(Annex 19)
        val ANTI_ROTATION_DEVICES = mapOf(
            AntiRotationDeviceType.SUPPORT_LEG to AntiRotationDeviceSpec(
                deviceType = AntiRotationDeviceType.SUPPORT_LEG,
                supportLegSpec = SupportLegSpec(
                    geometryRequirements = SupportLegGeometry(
                        widthRangeYAxis = DoubleRange(-100.0, 100.0),
                        lengthRangeXAxis = DoubleRange(585.0, 695.0),
                        heightUpperLimitZ = 70.0,
                        heightLowerLimitZ = -285.0,
                        adjustmentStep = 20.0
                    ),
                    footRequirements = SupportLegFootSpec(
                        minContactArea = 2500.0,
                        minDimensions = 30.0,
                        minEdgeRadius = 3.2
                    ),
                    strengthRequirement = 2.5
                ),
                topTetherSpec = null
            ),
            AntiRotationDeviceType.TOP_TETHER to AntiRotationDeviceSpec(
                deviceType = AntiRotationDeviceType.TOP_TETHER,
                supportLegSpec = null,
                topTetherSpec = TopTetherSpec(
                    minLength = 2000.0,
                    tension = 50.0,
                    tensionTolerance = 5.0,
                    hasNoSlackIndicator = true
                )
            ),
            AntiRotationDeviceType.BOTH to AntiRotationDeviceSpec(
                deviceType = AntiRotationDeviceType.BOTH,
                supportLegSpec = SupportLegSpec(
                    geometryRequirements = SupportLegGeometry(
                        widthRangeYAxis = DoubleRange(-100.0, 100.0),
                        lengthRangeXAxis = DoubleRange(585.0, 695.0),
                        heightUpperLimitZ = 70.0,
                        heightLowerLimitZ = -285.0,
                        adjustmentStep = 20.0
                    ),
                    footRequirements = SupportLegFootSpec(
                        minContactArea = 2500.0,
                        minDimensions = 30.0,
                        minEdgeRadius = 3.2
                    ),
                    strengthRequirement = 2.5
                ),
                topTetherSpec = TopTetherSpec(
                    minLength = 2000.0,
                    tension = 50.0,
                    tensionTolerance = 5.0,
                    hasNoSlackIndicator = true
                )
            )
        )

        // 碰撞测试曲线(Annex 7)
        val IMPACT_TEST_CURVES = listOf(
            ImpactTestCurve(
                testType = ImpactTestType.FRONTAL_IMPACT,
                curvePoints = listOf(
                    CurvePoint(0.0, 10.0),
                    CurvePoint(20.0, 0.0),
                    CurvePoint(50.0, 20.0),
                    CurvePoint(50.0, 28.0),
                    CurvePoint(65.0, 20.0),
                    CurvePoint(80.0, 28.0),
                    CurvePoint(100.0, 0.0),
                    CurvePoint(120.0, 0.0)
                ),
                minSegment = listOf(
                    CurvePoint(10.0, 5.0),
                    CurvePoint(20.0, 9.0)
                ),
                velocityRange = "50±2km/h"
            ),
            ImpactTestCurve(
                testType = ImpactTestType.REAR_IMPACT,
                curvePoints = listOf(
                    CurvePoint(0.0, 0.0),
                    CurvePoint(30.0, 15.0),
                    CurvePoint(60.0, 25.0),
                    CurvePoint(90.0, 15.0),
                    CurvePoint(120.0, 0.0)
                ),
                minSegment = null,
                velocityRange = "30±2km/h"
            ),
            ImpactTestCurve(
                testType = ImpactTestType.SIDE_IMPACT,
                curvePoints = listOf(
                    CurvePoint(0.0, 6.375),
                    CurvePoint(15.0, 5.5),
                    CurvePoint(18.0, 6.2),
                    CurvePoint(60.0, 0.0),
                    CurvePoint(70.0, 0.0)
                ),
                minSegment = null,
                velocityRange = "6.375-7.25m/s"
            )
        )

        // 材料标准要求（R129r4e 格式）
        val MATERIAL_STANDARDS_REQUIREMENTS = listOf(
            MaterialStandardRequirement(
                materialType = MaterialType.TOXICITY,
                standard = "EN 71-3:2013+A1:2014(III类)",
                requirement = "可迁移元素限值符合EN 71-3 III类标准",
                application = "儿童可接触材料(非整体式ECRS≥100cm除外)",
                testMethod = "ICP-MS检测"
            ),
            MaterialStandardRequirement(
                materialType = MaterialType.FLAMMABILITY,
                standard = "EN 71-2:2011+A1:2014",
                requirement = "非内置式ECRS火焰蔓延速率≤30mm/s",
                application = "非内置式ECRS织物材料",
                testMethod = "垂直燃烧测试"
            ),
            MaterialStandardRequirement(
                materialType = MaterialType.FLAMMABILITY,
                standard = "Annex 22",
                requirement = "内置式ECRS火焰蔓延速率≤100mm/min;或60秒内熄灭且燃烧距离≤51mm",
                application = "内置式ECRS织物材料",
                testMethod = "Annex 22测试方法"
            ),
            MaterialStandardRequirement(
                materialType = MaterialType.WEBBING,
                standard = "i-Size系统要求",
                requirement = WebbingRequirement(
                    minWidth = 25.0,
                    minBreakingStrength = 3600.0,
                    abrasionResistance = 75.0,
                    lightResistance = 60.0,
                    lowTempResistance = "-30℃无断裂",
                    application = "儿童接触部位织带"
                ).toString(),
                application = "安全带织带",
                testMethod = "拉伸强度测试"
            ),
            MaterialStandardRequirement(
                materialType = MaterialType.METAL_PARTS,
                standard = "盐雾腐蚀测试",
                requirement = "50小时连续喷雾,无明显腐蚀",
                application = "金属连接件、结构部件",
                testMethod = "中性盐雾测试"
            ),
            MaterialStandardRequirement(
                materialType = MaterialType.PLASTIC_PARTS,
                standard = "高温变形测试",
                requirement = "80℃高温24小时无变形、功能正常",
                application = "塑料件",
                testMethod = "高温老化测试"
            )
        )

        // 卡扣要求
        val BUCKLE_REQUIREMENT = BuckleRequirement(
            releaseForceNoLoad = DoubleRange(40.0, 80.0),
            releaseForceUnderLoad = 80.0,
            strengthMassBelow13kg = 4000.0,
            strengthMassAbove13kg = 10000.0,
            buttonAreaClosed = 4.5,
            buttonAreaOpen = 2.5,
            cycles = 5000
        )

        // 卷收器要求
        val AUTO_LOCKING_RETRACTOR = RetractorRequirement(
            retractorType = RetractorType.AUTO_LOCKING,
            lockingGap = 30.0,
            retractionForce = RetractionForce(
                lapBelt = 7.0,
                shoulderBelt = DoubleRange(2.0, 7.0)
            ),
            lockingAcceleration = 0.8,
            lockingTiltAngle = 27.0,
            cycles = 10000
        )

        val EMERGENCY_LOCKING_RETRACTOR = RetractorRequirement(
            retractorType = RetractorType.EMERGENCY_LOCKING,
            lockingGap = 30.0,
            retractionForce = RetractionForce(
                lapBelt = 7.0,
                shoulderBelt = DoubleRange(2.0, 7.0)
            ),
            lockingAcceleration = 0.45,
            lockingTiltAngle = 27.0,
            cycles = 40000
        )

        // 认证申请材料清单(Annex 20)
        val APPLICATION_DOCUMENTS = ApplicationDocuments(
            generalDocs = listOf(
                DocumentItem("申请表", true, "DOC_APPLICATION_FORM"),
                DocumentItem("ECRS技术描述(材料/结构/载荷限制)", true, "DOC_TECH_DESCRIPTION"),
                DocumentItem("毒性/燃烧声明", true, "DOC_TOXICITY_FLAMMABILITY"),
                DocumentItem("安装说明书", true, "DOC_INSTALL_GUIDE"),
                DocumentItem("生产一致性控制文件", true, "DOC_PRODUCTION_CONFORMITY"),
                DocumentItem("产品图纸", true, "DOC_DRAWINGS"),
                DocumentItem("标签标识说明", true, "DOC_MARKINGS")
            ),
            specificVehicleDocs = listOf(
                DocumentItem("适配车辆清单", true, "DOC_VEHICLE_LIST"),
                DocumentItem("车辆结构图纸", true, "DOC_VEHICLE_DRAWING"),
                DocumentItem("车辆安装示意图", true, "DOC_INSTALLATION_SCHEMATIC")
            ),
            samples = listOf(
                SampleItem("ECRS样品", "按技术服务机构要求", "SAMPLE_ECRS"),
                SampleItem("织带样品", "每种类型10米", "SAMPLE_WEBBING")
            )
        )

        // 标识要求
        val MARKING_REQUIREMENTS = listOf(
            MarkingRequirement(
                markType = "基础信息标识",
                content = listOf(
                    "制造商名称",
                    "生产日期",
                    "朝向",
                    "身高范围",
                    "最大体重"
                ),
                position = "产品可见位置",
                durability = "清晰耐久",
                size = null,
                attachment = null,
                contrast = null,
                codeTag = "MARK_BASIC_INFO"
            ),
            MarkingRequirement(
                markType = "后向约束警告标签",
                content = listOf(
                    "后向使用提示",
                    "0-15个月标识",
                    "禁用前排气囊警示"
                ),
                position = "儿童头部区域内侧",
                durability = "清晰耐久",
                size = "最小60×120mm",
                attachment = "全周长缝制/全背面粘接",
                contrast = null,
                codeTag = "MARK_REARWARD_WARNING"
            ),
            MarkingRequirement(
                markType = "i-Size标识",
                content = listOf("i-Size logo"),
                position = "安装时可见",
                durability = "清晰耐久",
                size = "最小25×25mm",
                attachment = null,
                contrast = "背景对比明显(颜色/浮雕)",
                codeTag = "MARK_ISIZE_LOGO"
            ),
            MarkingRequirement(
                markType = "织带路径标识",
                content = listOf("腰带路径", "肩带路径"),
                position = "安全带导向处/锁止装置",
                durability = "清晰耐久",
                size = "至少与织带同宽",
                attachment = null,
                contrast = "绿色标识",
                codeTag = "MARK_WEBBING_PATH"
            )
        )

        // 生产一致性控制(Annex 12)
        val PRODUCTION_CONFORMITY = ProductionConformityControl(
            testItems = listOf(
                TestItem(
                    item = "紧急锁止卷收器锁定阈值与耐久性",
                    frequency = "至少每年1次",
                    codeTag = "PROD_TEST_RETRACTOR"
                ),
                TestItem(
                    item = "自动锁止卷收器耐久性",
                    frequency = "至少每年1次",
                    codeTag = "PROD_TEST_AUTO_RETRACTOR"
                ),
                TestItem(
                    item = "织带强度(含环境预处理)",
                    frequency = "至少每年1次",
                    codeTag = "PROD_TEST_WEBBING_STRENGTH"
                ),
                TestItem(
                    item = "微滑移测试",
                    frequency = "至少每年1次",
                    codeTag = "PROD_TEST_MICROSLIP"
                ),
                TestItem(
                    item = "能量吸收测试",
                    frequency = "至少每年1次",
                    codeTag = "PROD_TEST_ENERGY_ABSORPTION"
                ),
                TestItem(
                    item = "动态碰撞测试",
                    frequency = "按批次抽样",
                    codeTag = "PROD_TEST_DYNAMIC"
                )
            ),
            batchSampling = BatchSampling(
                batchSizeSmall = SizeSampling(1, 499, 1),
                batchSizeLarge = SizeSampling(500, 5000, 2),
                acceptanceCriteria = "头部位移≤1.05倍限值;均值+标准差≤限值"
            ),
            continuousControl = ContinuousControl(
                normalControlRate = 0.0002,
                strengthenedControlRate = 0.0005
            )
        )

        // 用户说明书要求
        val USER_MANUAL_REQUIREMENTS = UserManualRequirements(
            mandatoryContent = listOf(
                ManualContent("安装步骤(图文)", "MANUAL_INSTALL_STEPS"),
                ManualContent("适配车型清单", "MANUAL_VEHICLE_LIST"),
                ManualContent("身高/体重范围", "MANUAL_SIZE_RANGE"),
                ManualContent("调节方法", "MANUAL_ADJUSTMENT"),
                ManualContent("清洁说明", "MANUAL_CLEANING"),
                ManualContent("碰撞后更换提示", "MANUAL_REPLACEMENT_AFTER_CRASH"),
                ManualContent("禁止改装警告", "MANUAL_NO_MODIFICATION"),
                ManualContent("后向约束禁用前排气囊提示", "MANUAL_AIRBAG_WARNING"),
                ManualContent("特殊需求约束系统需咨询医生提示", "MANUAL_SPECIAL_NEEDS")
            ),
            language = "销售国官方语言(支持多语言)",
            retention = "需保留至ECRS使用寿命结束"
        )

        // 测试台车要求(Annex 6)
        val TEST_TROLLEY_SPEC = TestTrolleySpec(
            massRequirements = TrolleyMass(
                basicTrolley = 380.0,
                withVehicleStructure = 800.0
            ),
            stoppingDevice = StoppingDevice(
                absorberType = "聚氨酯能量吸收管",
                materialSpecs = AbsorberMaterial(
                    shoreHardnessA = 88,
                    breakingStrength = 300,
                    elongation = 400
                )
            ),
            sideImpactDoor = SideImpactDoor(
                dimensions = DoorDimensions(
                    width = 600,
                    height = 800,
                    groundClearance = 175,
                    groundClearanceTolerance = 25,
                    angleWithVertical = 25
                ),
                padding = listOf(
                    DoorPadding(
                        layer = 1,
                        material = "Polychloropren CR4271",
                        thickness = 35
                    ),
                    DoorPadding(
                        layer = 2,
                        material = "Styrodur C2500",
                        thickness = 20
                    )
                ),
                velocityCorridor = listOf(
                    VelocityPoint(0, 6.375, 7.25),
                    VelocityPoint(15, 5.5, null),
                    VelocityPoint(18, null, 6.2),
                    VelocityPoint(60, 0.0, null),
                    VelocityPoint(70, null, 0.0)
                )
            )
        )

        // 假人安装垫片高度(Annex 7 Table 7)
        val SPACER_HEIGHTS = mapOf(
            "Q0" to 173,
            "Q1" to 229,
            "Q1.5" to 237,
            "Q3" to 250,
            "Q6" to 270,
            "Q10" to 359
        )

        // 测试安装预紧力要求
        val INSTALLATION_PRELOAD = mapOf(
            "isofix_preload" to 135,
            "isofix_preload_height" to 100,
            "top_tether_tension" to 50,
            "webbing_tension" to 250,
            "webbing_deflection" to 45,
            "test_window" to 10
        )

        // 外部尺寸ISO包络
        val EXTERNAL_ENVELOPES = mapOf(
            "iSizeForwardFacing" to EnvelopeDimensions(
                width = 600.0,
                height = 800.0,
                depth = 650.0
            ),
            "iSizeRearwardFacing" to EnvelopeDimensions(
                width = 600.0,
                height = 900.0,
                depth = 700.0
            ),
            "iSizeBooster" to EnvelopeDimensions(
                width = 550.0,
                height = 750.0,
                depth = 500.0
            )
        )

        // 关键术语定义
        val KEY_TERMS = listOf(
            "i-Size" to "整体式通用ISOFIX系统,适配所有i-Size车辆座椅位置(按UN R14/R145/R16认证)",
            "ISOFIX" to "含2个车辆下锚点+2个ECRS连接件+防旋转装置(上拉带/支撑腿)的连接系统",
            "整体式" to "儿童仅通过ECRS自身组件约束(如五点式安全带、冲击盾),不直接使用车辆安全带",
            "非整体式" to "儿童约束依赖车辆安全带,ECRS仅提供辅助支撑(如增高座)",
            "防旋转装置" to "限制ECRS碰撞时俯仰旋转的组件,包括上拉带(Top-tether)或支撑腿"
        )

        // ===== EceR129StandardDatabase 原有的测试矩阵、尺寸阈值和产品配置 =====

        // 测试矩阵（简化版，用于显示，基于R129r4e §6.6.4.3.1 Table 4）
        val TEST_MATRIX = listOf(
            TestMatrixItem(
                testName = "正面撞击测试（Q0 + 后向 + 直立）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点，带支撑腿",
                dummyType = "Q0（新生儿，≤60cm）",
                acceptanceCriteria = "HPC≤600，头部加速度3ms≤75g，胸部加速度3ms≤55g"
            ),
            TestMatrixItem(
                testName = "正面撞击测试（Q0 + 后向 + 倾斜）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点，带支撑腿，靠背倾斜",
                dummyType = "Q0（新生儿，≤60cm）",
                acceptanceCriteria = "HPC≤600，头部加速度3ms≤75g，胸部加速度3ms≤55g"
            ),
            TestMatrixItem(
                testName = "后向翻滚测试（Q0）",
                standardClause = "ECE R129 Rev.4 §7.1.3.2",
                testMethod = "30km/h 后向撞击，ΔV=32km/h，ISOFIX 3点，带支撑腿",
                dummyType = "Q0（新生儿，≤60cm）",
                acceptanceCriteria = "假人加速度≤25g，座椅无结构性变形"
            ),
            TestMatrixItem(
                testName = "正面撞击测试（Q1.5 + 前向）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点，带支撑腿",
                dummyType = "Q1.5（18个月，75-87cm）",
                acceptanceCriteria = "HPC≤600，头部加速度3ms≤75g，胸部加速度3ms≤55g，腹部压力≤1.2bar"
            ),
            TestMatrixItem(
                testName = "侧面碰撞测试（Q1.5）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.5.1",
                testMethod = "32km/h 侧面撞击可变形障碍物，门侵入深度，ISOFIX 3点",
                dummyType = "Q1.5（18个月，75-87cm）",
                acceptanceCriteria = "头部偏移量≤150mm，头部加速度3ms≤75g，头部无接触车门"
            ),
            TestMatrixItem(
                testName = "正面撞击测试（Q3 + 前向 + 直立）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点，带支撑腿",
                dummyType = "Q3（3岁，87-105cm）",
                acceptanceCriteria = "HPC≤800，头部加速度3ms≤80g，胸部加速度3ms≤55g，腹部压力≤1.0bar"
            ),
            TestMatrixItem(
                testName = "正面撞击测试（Q3 + 前向 + 倾斜）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点，带支撑腿，靠背倾斜",
                dummyType = "Q3（3岁，87-105cm）",
                acceptanceCriteria = "HPC≤800，头部加速度3ms≤80g，胸部加速度3ms≤55g，腹部压力≤1.0bar"
            ),
            TestMatrixItem(
                testName = "侧面碰撞测试（Q3）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.5.1",
                testMethod = "32km/h 侧面撞击可变形障碍物，门侵入深度，ISOFIX 3点",
                dummyType = "Q3（3岁，87-105cm）",
                acceptanceCriteria = "头部偏移量≤150mm，头部加速度3ms≤80g，头部无接触车门"
            ),
            TestMatrixItem(
                testName = "正面撞击测试（Q6 + 前向）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点，带支撑腿",
                dummyType = "Q6（6岁，105-125cm）",
                acceptanceCriteria = "HPC≤800，头部加速度3ms≤80g，胸部加速度3ms≤55g，腹部压力≤1.0bar"
            ),
            TestMatrixItem(
                testName = "侧面碰撞测试（Q6）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.5.1",
                testMethod = "32km/h 侧面撞击可变形障碍物，门侵入深度，ISOFIX 3点",
                dummyType = "Q6（6岁，105-125cm）",
                acceptanceCriteria = "头部偏移量≤150mm，头部加速度3ms≤80g，头部无接触车门"
            ),
            TestMatrixItem(
                testName = "正面撞击测试（Q10 + 前向）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点",
                dummyType = "Q10（10岁，125-150cm）",
                acceptanceCriteria = "HPC≤800，头部加速度3ms≤80g，胸部加速度3ms≤55g，腹部压力≤1.2bar"
            ),
            TestMatrixItem(
                testName = "ISOFIX强度测试",
                standardClause = "GB 27887-2024 §6.2",
                testMethod = "施加10kN拉力至ISOFIX锚点",
                dummyType = "无",
                acceptanceCriteria = "ISOFIX连接件无变形，位移 ≤ 15mm"
            ),
            TestMatrixItem(
                testName = "支撑腿强度测试",
                standardClause = "ECE R129 §5.5.3",
                testMethod = "施加2.5kN垂直载荷至支撑腿",
                dummyType = "无",
                acceptanceCriteria = "支撑腿无断裂，偏转 ≤ 10mm"
            ),
            TestMatrixItem(
                testName = "安全带锁止测试",
                standardClause = "GB 6095-2021 §5.2",
                testMethod = "施加5kN拉力至安全带",
                dummyType = "无",
                acceptanceCriteria = "锁止机构无解锁，延伸率 ≤ 10%"
            ),
            TestMatrixItem(
                testName = "头托调节耐久性测试",
                standardClause = "ECE R129 §5.4.2",
                testMethod = "反复调节头托10000次",
                dummyType = "无",
                acceptanceCriteria = "调节机构无卡滞，档位准确"
            ),
            TestMatrixItem(
                testName = "材料燃烧性测试",
                standardClause = "GB 6675.3-2014 §5.3",
                testMethod = "垂直燃烧测试",
                dummyType = "无",
                acceptanceCriteria = "燃烧速率 ≤ 100mm/min"
            ),
            TestMatrixItem(
                testName = "可迁移元素测试",
                standardClause = "GB 6675.4-2014 §4",
                testMethod = "ICP-MS 检测",
                dummyType = "无",
                acceptanceCriteria = "符合GB 6675限值要求"
            )
        )

        // 尺寸阈值
        val DIMENSIONAL_THRESHOLDS = listOf(
            DimensionalThreshold(
                dimensionType = "外宽度上限",
                limit = "≤50",
                unit = "cm",
                standardClause = "ECE R129 Envelope要求",
                description = "i-Size 包络宽度限制，确保车辆安装兼容性"
            ),
            DimensionalThreshold(
                dimensionType = "外长度上限",
                limit = "≤75",
                unit = "cm",
                standardClause = "ECE R129 Envelope要求",
                description = "i-Size 包络长度限制，确保车辆安装兼容性"
            ),
            DimensionalThreshold(
                dimensionType = "外高度上限",
                limit = "≤85",
                unit = "cm",
                standardClause = "ECE R129 Envelope要求",
                description = "i-Size 包络高度限制，确保车辆安装兼容性"
            ),
            DimensionalThreshold(
                dimensionType = "ISOFIX接口间距",
                limit = "280±5",
                unit = "mm",
                standardClause = "GB 27887-2024 §5.5",
                description = "ISOFIX锚点标准间距"
            ),
            DimensionalThreshold(
                dimensionType = "座椅内宽",
                limit = "≥38",
                unit = "cm",
                standardClause = "ECE R129 §5.2",
                description = "座椅内部最小宽度，确保儿童舒适性"
            )
        )

        // 产品配置
        val PRODUCT_CONFIGURATIONS = listOf(
            ProductConfigurationInfo(
                configType = "ISOFIX接口",
                description = "ISOFIX硬连接接口，提供稳固的车辆固定",
                standardClause = "ECE R129 §5.5.1 / §5.5.3",
                installationRequirement = "适配带ISOFIX锚点的乘用车（2006年后欧盟车辆，2012年后中国车辆）"
            ),
            ProductConfigurationInfo(
                configType = "可伸缩支撑腿",
                description = "地面支撑结构，提高座椅稳定性",
                standardClause = "ECE R129 §5.5.3",
                installationRequirement = "适配地板间隙≥5cm的车辆，支撑腿长度可调280-450mm"
            ),
            ProductConfigurationInfo(
                configType = "顶部系带挂钩",
                description = "上拉带连接车辆顶部系带点",
                standardClause = "ECE R129 §5.5.1",
                installationRequirement = "适配车辆后排座椅顶部有系带锚点的车型"
            ),
            ProductConfigurationInfo(
                configType = "侧撞防护装置（SIP）",
                description = "可调节的侧面碰撞保护块",
                standardClause = "ECE R129 §5.3.3",
                installationRequirement = "需根据车门间隙调整侧翼展开角度，避免影响车门关闭"
            ),
            ProductConfigurationInfo(
                configType = "360°旋转机构",
                description = "座椅可360度旋转，方便抱娃进出",
                standardClause = "ECE R129 §5.2（需通过动态测试）",
                installationRequirement = "需确保旋转机构的锁止强度≥5kN"
            ),
            ProductConfigurationInfo(
                configType = "头托高度调节",
                description = "多档位头托高度调节，适应不同身高儿童",
                standardClause = "ECE R129 §5.4.2",
                installationRequirement = "调节范围10-30cm，建议分12-15档"
            ),
            ProductConfigurationInfo(
                configType = "靠背角度调节",
                description = "座椅靠背角度可调（后向135-150°，前向15-30°）",
                standardClause = "ECE R129 §5.4.1",
                installationRequirement = "角度锁止机构需通过耐久性测试10000次"
            )
        )

        // ===== 静态访问方法 =====

        fun getStandardOverview(): StandardOverview {
            return StandardOverview(
                standardName = "ECE R129 / i-Size",
                revision = "Revision 4 + 02/03 Series Amendments",
                effectiveDate = "2018-12-29",
                scope = "机动车儿童约束系统(ECRS)统一技术规范",
                productTypes = 6,
                heightRange = "40-150cm",
                weightRange = "3.47-35.58kg",
                ageRange = "新生儿-10岁",
                keyFeatures = listOf(
                    "基于身高的i-Size分类",
                    "强制15个月以下后向约束",
                    "侧面碰撞测试要求",
                    "防旋转装置强制要求",
                    "ISO尺寸包络兼容性"
                )
            )
        }

        fun getDummySpecsList(): List<DummySpec> = DUMMY_SPECS

        fun getDummySpec(dummyType: String): DummySpec? {
            return DUMMY_SPECS.find { it.dummyType == dummyType }
        }

        fun getApplicableDummies(heightRange: String): List<DummySpec> {
            val (minHeight, maxHeight) = parseHeightRange(heightRange)
            return DUMMY_SPECS.filter { spec ->
                val (specMin, specMax) = parseStatureRange(spec.statureRange)
                !(maxHeight < specMin || minHeight > specMax)
            }
        }

        fun getAntiRotationDeviceSpec(deviceType: AntiRotationDeviceType): AntiRotationDeviceSpec? {
            return ANTI_ROTATION_DEVICES[deviceType]
        }

        fun getAntiRotationDevicesMap(): Map<AntiRotationDeviceType, AntiRotationDeviceSpec> = ANTI_ROTATION_DEVICES

        fun getImpactTestCurve(testType: ImpactTestType): ImpactTestCurve? {
            return IMPACT_TEST_CURVES.find { it.testType == testType }
        }

        fun getImpactTestCurvesList(): List<ImpactTestCurve> = IMPACT_TEST_CURVES

        fun getMaterialStandardsList(): List<MaterialStandardRequirement> = MATERIAL_STANDARDS_REQUIREMENTS

        fun getMaterialStandardsByType(materialType: MaterialType): List<MaterialStandardRequirement> {
            return MATERIAL_STANDARDS_REQUIREMENTS.filter { it.materialType == materialType }
        }

        fun getBuckleRequirement(): BuckleRequirement = BUCKLE_REQUIREMENT

        fun getAutoLockingRetractor(): RetractorRequirement = AUTO_LOCKING_RETRACTOR

        fun getEmergencyLockingRetractor(): RetractorRequirement = EMERGENCY_LOCKING_RETRACTOR

        fun getApplicationDocuments(): ApplicationDocuments = APPLICATION_DOCUMENTS

        fun getApplicationDocumentsList(): List<Any> =
            APPLICATION_DOCUMENTS.generalDocs +
            (APPLICATION_DOCUMENTS.specificVehicleDocs ?: emptyList()) +
            APPLICATION_DOCUMENTS.samples.map { it as Any }

        fun getMarkingRequirementsList(): List<MarkingRequirement> = MARKING_REQUIREMENTS

        fun getProductionConformity(): ProductionConformityControl = PRODUCTION_CONFORMITY

        fun getUserManualRequirements(): UserManualRequirements = USER_MANUAL_REQUIREMENTS

        fun getTestTrolleySpec(): TestTrolleySpec = TEST_TROLLEY_SPEC

        fun getSpacerHeight(dummyType: String): Int? {
            return SPACER_HEIGHTS[dummyType]
        }

        fun getInstallationPreloadMap(): Map<String, Int> = INSTALLATION_PRELOAD

        fun getExternalEnvelopesMap(): Map<String, EnvelopeDimensions> = EXTERNAL_ENVELOPES

        fun getKeyTermsList(): List<Pair<String, String>> = KEY_TERMS

        fun getECRSClassificationsList(): List<ECRSClassification> = ECRS_CLASSIFICATIONS

        fun getThresholds(): R129r4eThresholds = THRESHOLDS

        // ===== 辅助方法 =====

        private fun parseHeightRange(range: String): Pair<Double, Double> {
            val parts = range.split("-").map { it.trim().replace("cm", "") }
            return Pair(parts[0].toDouble(), parts[1].toDouble())
        }

        private fun parseStatureRange(range: String): Pair<Double, Double> {
            return when {
                range.contains("≤") -> {
                    val value = range.replace("≤", "").replace("cm", "").toDouble()
                    Pair(0.0, value)
                }
                range.contains(">") -> {
                    val value = range.replace(">", "").replace("cm", "").toDouble()
                    Pair(value, 200.0)
                }
                else -> parseHeightRange(range)
            }
        }

        // ===== EceR129StandardDatabase 原有方法 =====

        /**
         * 根据产品类型获取核心参数
         */
        fun getCoreParameters(productType: String): List<CoreDesignParameter> {
            return when (productType) {
                "儿童安全座椅" -> CORE_PARAMETERS
                "婴儿推车" -> getStrollerParameters()
                "儿童高脚椅" -> getHighChairParameters()
                else -> emptyList()
            }
        }

        /**
         * 根据产品类型获取材料标准
         */
        fun getMaterialStandards(productType: String): List<MaterialStandard> {
            return when (productType) {
                "儿童安全座椅" -> MATERIAL_STANDARDS
                "婴儿推车" -> getStrollerMaterialStandards()
                "儿童高脚椅" -> getHighChairMaterialStandards()
                else -> emptyList()
            }
        }

        /**
         * 根据产品类型获取测试矩阵
         */
        fun getTestMatrix(productType: String): List<TestMatrixItem> {
            return when (productType) {
                "儿童安全座椅" -> TEST_MATRIX
                "婴儿推车" -> getStrollerTestMatrix()
                "儿童高脚椅" -> getHighChairTestMatrix()
                else -> emptyList()
            }
        }

        /**
         * 根据产品类型获取尺寸阈值
         */
        fun getDimensionalThresholds(productType: String): List<DimensionalThreshold> {
            return when (productType) {
                "儿童安全座椅" -> DIMENSIONAL_THRESHOLDS
                "婴儿推车" -> getStrollerDimensionalThresholds()
                "儿童高脚椅" -> getHighChairDimensionalThresholds()
                else -> emptyList()
            }
        }

        /**
         * 根据产品类型获取产品配置
         */
        fun getProductConfigurationInfos(productType: String): List<ProductConfigurationInfo> {
            return when (productType) {
                "儿童安全座椅" -> PRODUCT_CONFIGURATIONS
                "婴儿推车" -> getStrollerConfigurations()
                "儿童高脚椅" -> getHighChairConfigurations()
                else -> emptyList()
            }
        }

        // ===== 婴儿推车专用数据 =====

        private fun getStrollerParameters(): List<CoreDesignParameter> = listOf(
            CoreDesignParameter("推车宽度", "≤50", "cm", "EN 1888 §5.2", "确保通过标准门洞"),
            CoreDesignParameter("折叠尺寸", "≤50×30×80", "cm", "EN 1888 §5.2", "便于存放和携带"),
            CoreDesignParameter("靠背角度范围", "110-175", "度", "EN 1888 §5.3", "坐姿、半躺、全躺三种状态"),
            CoreDesignParameter("制动强度", "≥1.5", "g", "EN 1888 §7.3", "15°斜坡制动性能要求")
        )

        private fun getStrollerMaterialStandards(): List<MaterialStandard> = listOf(
            MaterialStandard("铝合金车架", "EN 71-3", "可迁移元素限值符合EN 71-3", "车架结构"),
            MaterialStandard("织物面料", "GB 18401 B类", "甲醛≤75mg/kg", "座椅面料、遮阳篷")
        )

        private fun getStrollerTestMatrix(): List<TestMatrixItem> = listOf(
            TestMatrixItem("稳定性测试", "EN 1888 §7.3", "15°斜坡制动", "无", "无滑动、无倾倒"),
            TestMatrixItem("折叠锁定测试", "ASTM F833 §5.7", "折叠-展开500次", "无", "无卡滞、无意外解锁")
        )

        private fun getStrollerDimensionalThresholds(): List<DimensionalThreshold> = listOf(
            DimensionalThreshold("推车宽度", "≤50", "cm", "EN 1888", "通过标准门洞（80cm）"),
            DimensionalThreshold("折叠尺寸", "≤50×30×80", "cm", "EN 1888", "便于携带存放")
        )

        private fun getStrollerConfigurations(): List<ProductConfigurationInfo> = listOf(
            ProductConfigurationInfo("四轮避震", "四轮独立避震系统", null, "减震行程≥20mm"),
            ProductConfigurationInfo("一键折叠", "单手折叠机构", null, "折叠操作力≤50N")
        )

        // ===== 儿童高脚椅专用数据 =====

        private fun getHighChairParameters(): List<CoreDesignParameter> = listOf(
            CoreDesignParameter("座面高度", "45-65", "cm", "GB 28007-2011 §4.1", "适配标准餐桌高度"),
            CoreDesignParameter("安全带间距", "15-20", "cm", "ISO 8124-3 §4.2", "防止儿童滑落"),
            CoreDesignParameter("脚踏板承重", "≥50", "kg", "ISO 8124-3 §4.3", "确保结构强度")
        )

        private fun getHighChairMaterialStandards(): List<MaterialStandard> = listOf(
            MaterialStandard("食品级PP塑料", "GB 4806.7", "符合食品接触材料标准", "托盘、餐盘"),
            MaterialStandard("ABS塑料", "GB 6675.4-2014", "可迁移元素限值符合", "椅腿、底座")
        )

        private fun getHighChairTestMatrix(): List<TestMatrixItem> = listOf(
            TestMatrixItem("稳定性测试", "ISO 8124-3", "倾斜测试", "无", "倾斜角度<10°"),
            TestMatrixItem("强度测试", "GB 28007", "静载测试", "无", "无明显变形")
        )

        private fun getHighChairDimensionalThresholds(): List<DimensionalThreshold> = listOf(
            DimensionalThreshold("座面高度", "45-65", "cm", "GB 28007-2011", "适配标准餐桌高度75cm"),
            DimensionalThreshold("整体高度", "≤95", "cm", "ISO 8124-3", "避免重心过高")
        )

        private fun getHighChairConfigurations(): List<ProductConfigurationInfo> = listOf(
            ProductConfigurationInfo("五点式安全带", "防止儿童滑落", "ISO 8124-3", "肩带、腰带、跨带五点固定"),
            ProductConfigurationInfo("高度调节", "多档位高度调节", null, "调节范围20cm，至少5档")
        )
    }
}

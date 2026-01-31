package com.childproduct.designassistant.data.model

/**
 * ECE R129 / i-Size 标准详细数据模型
 * 基于R129r4e修订版(含02/03系列修正案,2018年12月29日生效)
 */

/**
 * 假人规格参数(Annex 8)
 */
data class DummySpec(
    val dummyType: String,              // 假人类型: Q0, Q1, Q1.5, Q3, Q6, Q10
    val statureRange: String,           // 身高范围
    val mass: Double,                   // 质量(kg)
    val massTolerance: Double,          // 质量公差(kg)
    val keyDimensions: DummyKeyDimensions, // 关键尺寸
    val injuryCriteria: InjuryCriteria,  // 伤害判据
    val applicableTests: List<String>   // 适用测试
)

/**
 * 假人关键尺寸
 */
data class DummyKeyDimensions(
    val sittingHeight: Double,          // 坐姿高度(mm)
    val sittingHeightTolerance: Double, // 坐姿高度公差(mm)
    val shoulderBreadth: Double,        // 肩宽(mm)
    val shoulderBreadthTolerance: Double, // 肩宽公差(mm)
    val hipBreadth: Double,             // 髋宽(mm)
    val hipBreadthTolerance: Double,    // 髋宽公差(mm)
    val abdomenDepth: Double?,          // 腹部深度(mm, Q1.5+适用)
    val thighThickness: Double?,        // 大腿厚度(mm, Q1.5+适用)
)

/**
 * 假人伤害判据(Annex 8 Table 4-5)
 */
data class InjuryCriteria(
    val headAcceleration3ms: HeadAccelerationThreshold, // 头部3ms累计加速度(g)
    val hpc: HPThreshold,                               // 头部伤害准则
    val chestAcceleration3ms: Double,                   // 胸部3ms累计加速度(g)
    val abdominalPressure: AbdomenPressureThreshold?,   // 腹部压力(Bar)
    val neckForces: NeckForceThreshold?,                // 颈部力
    val neckMoments: NeckMomentThreshold?               // 颈部力矩
)

/**
 * 头部加速度阈值
 */
data class HeadAccelerationThreshold(
    val lowThreshold: Double,  // 低阈值(Q0-Q1.5)
    val highThreshold: Double  // 高阈值(Q3-Q10)
)

/**
 * HPC阈值
 */
data class HPThreshold(
    val lowThreshold: Int,    // 低阈值(Q0-Q1.5)
    val highThreshold: Int    // 高阈值(Q3-Q10)
)

/**
 * 腹部压力阈值
 */
data class AbdomenPressureThreshold(
    val q1_5Threshold: Double,  // Q1.5阈值
    val q3_q6Threshold: Double, // Q3, Q6阈值
    val q10Threshold: Double    // Q10阈值
)

/**
 * 颈部力阈值
 */
data class NeckForceThreshold(
    val tensionLimit: Double?,  // 张力限值(N)
    val compressionLimit: Double?, // 压缩限值(N)
)

/**
 * 颈部力矩阈值
 */
data class NeckMomentThreshold(
    val flexionLimit: Double?,  // 屈曲限值(Nm)
    val extensionLimit: Double? // 伸展限值(Nm)
)

/**
 * 防旋转装置规格(Annex 19)
 */
data class AntiRotationDeviceSpec(
    val deviceType: AntiRotationDeviceType, // 装置类型
    val supportLegSpec: SupportLegSpec?,    // 支撑腿规格(如果有)
    val topTetherSpec: TopTetherSpec?       // 上拉带规格(如果有)
)

/**
 * 防旋转装置类型
 */
enum class AntiRotationDeviceType {
    SUPPORT_LEG,       // 支撑腿
    TOP_TETHER,        // 上拉带
    BOTH              // 支撑腿+上拉带
}

/**
 * 支撑腿规格
 */
data class SupportLegSpec(
    val geometryRequirements: SupportLegGeometry,
    val footRequirements: SupportLegFootSpec,
    val strengthRequirement: Double  // 强度要求(kN)
)

/**
 * 支撑腿几何要求(Annex 19)
 */
data class SupportLegGeometry(
    val widthRangeYAxis: DoubleRange,  // Y'轴方向范围(mm)
    val lengthRangeXAxis: DoubleRange, // X'轴方向范围(mm)
    val heightUpperLimitZ: Double,     // Z'轴上方限制(mm)
    val heightLowerLimitZ: Double,     // Z'轴下方限制(mm)
    val adjustmentStep: Double         // 调节步距(mm)
)

/**
 * 双精度范围
 */
data class DoubleRange(
    val min: Double,
    val max: Double
)

/**
 * 支撑腿底座要求
 */
data class SupportLegFootSpec(
    val minContactArea: Double,   // 最小接触面积(mm²)
    val minDimensions: Double,    // X'/Y'方向最小尺寸(mm)
    val minEdgeRadius: Double     // 边缘最小半径(mm)
)

/**
 * 上拉带规格
 */
data class TopTetherSpec(
    val minLength: Double,        // 最小长度(mm)
    val tension: Double,          // 张力(N)
    val tensionTolerance: Double, // 张力公差(N)
    val hasNoSlackIndicator: Boolean // 无松弛指示器
)

/**
 * 碰撞测试曲线(Annex 7)
 */
data class ImpactTestCurve(
    val testType: ImpactTestType,  // 测试类型
    val curvePoints: List<CurvePoint>, // 曲线坐标点
    val minSegment: List<CurvePoint>?, // 最小线段(如有)
    val velocityRange: String      // 速度范围
)

/**
 * 碰撞测试类型
 */
enum class ImpactTestType {
    FRONTAL_IMPACT,     // 正面碰撞
    REAR_IMPACT,        // 后向碰撞
    SIDE_IMPACT         // 侧面碰撞
}

/**
 * 曲线坐标点
 */
data class CurvePoint(
    val timeMs: Double,   // 时间(ms)
    val accelerationG: Double // 加速度(g)或速度(m/s)
)

/**
 * 内部几何尺寸要求(Annex 18)
 */
data class InternalGeometryRequirement(
    val measurementDevice: MeasurementDeviceSpec,
    val dimensionTolerances: Map<String, DimensionTolerance>
)

/**
 * 测量装置规格
 */
data class MeasurementDeviceSpec(
    val mass: Double,         // 质量(kg)
    val massTolerance: Double, // 质量公差(kg)
    val contactForce: Double,  // 接触力(N)
    val shoulderCylinderDia: Double, // 肩部圆柱直径(mm)
    val abdomenSphereDia: Double     // 腹部球体直径(mm)
)

/**
 * 尺寸公差
 */
data class DimensionTolerance(
    val toleranceType: String,   // 公差类型
    val below87Cm: Double,       // 身高≤87cm时公差(%)或(mm)
    val above87Cm: Double        // 身高>87cm时公差(%)或(mm)
)

/**
 * 外部尺寸包络要求(ISO尺寸)
 */
data class ExternalEnvelopeRequirement(
    val ecrsType: String,        // ECRS类型
    val envelopeType: String,    // 包络类型(ISO/F2x, ISO/R2, ISO/B2等)
    val dimensions: EnvelopeDimensions,
    val vehicleFixture: String   // 车辆夹具
)

/**
 * 包络尺寸
 */
data class EnvelopeDimensions(
    val width: Double,   // 宽度(mm)
    val height: Double,  // 高度(mm)
    val depth: Double    // 深度(mm)
)

/**
 * 材料标准要求
 */
data class MaterialStandardRequirement(
    val materialType: MaterialType,
    val standard: String,           // 符合标准
    val requirement: String,        // 标准要求
    val application: String,        // 应用部位
    val testMethod: String?         // 测试方法
)

/**
 * 材料类型
 */
enum class MaterialType {
    TOXICITY,              // 毒性
    FLAMMABILITY,          // 燃烧性
    WEBBING,               // 织带
    METAL_PARTS,           // 金属件
    PLASTIC_PARTS,         // 塑料件
    FOAM                   // 泡沫材料
}

/**
 * 织带要求
 */
data class WebbingRequirement(
    val minWidth: Double,             // 最小宽度(mm)
    val minBreakingStrength: Double,  // 最小断裂强度(N)
    val abrasionResistance: Double,   // 耐磨后强度保留率(%)
    val lightResistance: Double,      // 耐光后强度保留率(%)
    val lowTempResistance: String,    // 低温要求
    val application: String           // 应用部位
)

/**
 * 卡扣要求
 */
data class BuckleRequirement(
    val releaseForceNoLoad: DoubleRange, // 无载时释放力(N)
    val releaseForceUnderLoad: Double,   // 加载后释放力上限(N)
    val strengthMassBelow13kg: Double,   // 适配≤13kg儿童的强度(N)
    val strengthMassAbove13kg: Double,   // 适配>13kg儿童的强度(N)
    val buttonAreaClosed: Double,        // 封闭型按钮面积(cm²)
    val buttonAreaOpen: Double,          // 非封闭型按钮面积(cm²)
    val cycles: Int                      // 循环测试次数
)

/**
 * 卷收器要求
 */
data class RetractorRequirement(
    val retractorType: RetractorType,   // 卷收器类型
    val lockingGap: Double,              // 锁定间隙(mm)
    val retractionForce: RetractionForce, // 卷收力(N)
    val lockingAcceleration: Double,     // 锁定加速度(g)
    val lockingTiltAngle: Double,        // 锁定倾斜角度(°)
    val cycles: Int                      // 循环次数
)

/**
 * 卷收器类型
 */
enum class RetractorType {
    AUTO_LOCKING,      // 自动锁定型
    EMERGENCY_LOCKING  // 紧急锁定型
}

/**
 * 卷收力
 */
data class RetractionForce(
    val lapBelt: Double,   // 腰带卷收力(N)
    val shoulderBelt: DoubleRange // 胸带卷收力(N)
)

/**
 * 认证申请材料清单(Annex 20)
 */
data class ApplicationDocuments(
    val generalDocs: List<DocumentItem>, // 通用文件
    val specificVehicleDocs: List<DocumentItem>?, // 特定车型文件
    val samples: List<SampleItem>       // 样品
)

/**
 * 文档项
 */
data class DocumentItem(
    val name: String,
    val required: Boolean,
    val codeTag: String
)

/**
 * 样品项
 */
data class SampleItem(
    val name: String,
    val quantity: String,
    val codeTag: String
)

/**
 * 标识要求
 */
data class MarkingRequirement(
    val markType: String,          // 标识类型
    val content: List<String>,     // 内容
    val position: String,          // 位置
    val durability: String,        // 耐久性
    val size: String?,             // 尺寸要求
    val attachment: String?,       // 固定方式
    val contrast: String?,         // 对比度要求
    val codeTag: String            // 代码标签
)

/**
 * 生产一致性控制要求(Annex 12)
 */
data class ProductionConformityControl(
    val testItems: List<TestItem>,
    val batchSampling: BatchSampling,
    val continuousControl: ContinuousControl
)

/**
 * 测试项
 */
data class TestItem(
    val item: String,
    val frequency: String,
    val codeTag: String
)

/**
 * 批次抽样
 */
data class BatchSampling(
    val batchSizeSmall: SizeSampling,    // 小批次
    val batchSizeLarge: SizeSampling,    // 大批次
    val acceptanceCriteria: String       // 合格标准
)

/**
 * 尺寸抽样
 */
data class SizeSampling(
    val minBatchSize: Int,
    val maxBatchSize: Int,
    val sampleSize: Int
)

/**
 * 连续控制
 */
data class ContinuousControl(
    val normalControlRate: Double,  // 正常控制率
    val strengthenedControlRate: Double // 加强控制率
)

/**
 * 测试台车要求(Annex 6)
 */
data class TestTrolleySpec(
    val massRequirements: TrolleyMass,    // 质量要求
    val stoppingDevice: StoppingDevice,   // 停止装置
    val sideImpactDoor: SideImpactDoor?   // 侧面碰撞门
)

/**
 * 台车质量
 */
data class TrolleyMass(
    val basicTrolley: Double,      // 仅含座椅(kg)
    val withVehicleStructure: Double // 含车辆结构(kg)
)

/**
 * 停止装置
 */
data class StoppingDevice(
    val absorberType: String,      // 吸能器类型
    val materialSpecs: AbsorberMaterial // 材料规格
)

/**
 * 吸能器材料
 */
data class AbsorberMaterial(
    val shoreHardnessA: Int,       // 邵氏硬度A
    val breakingStrength: Int,     // 断裂强度(kg/cm²)
    val elongation: Int            // 延伸率(%)
)

/**
 * 侧面碰撞门
 */
data class SideImpactDoor(
    val dimensions: DoorDimensions,
    val padding: List<DoorPadding>,
    val velocityCorridor: List<VelocityPoint>
)

/**
 * 门尺寸
 */
data class DoorDimensions(
    val width: Int,            // 宽度(mm)
    val height: Int,           // 高度(mm)
    val groundClearance: Int,  // 离地间隙(mm)
    val groundClearanceTolerance: Int, // 公差
    val angleWithVertical: Int // 与垂直面角度(°)
)

/**
 * 门缓冲层
 */
data class DoorPadding(
    val layer: Int,            // 层数
    val material: String,      // 材料
    val thickness: Int         // 厚度(mm)
)

/**
 * 速度点
 */
data class VelocityPoint(
    val timePoint: Int,        // 时间点(ms)
    val lowerVelocity: Double?, // 下限速度(m/s)
    val upperVelocity: Double?  // 上限速度(m/s)
)

/**
 * 用户说明书要求
 */
data class UserManualRequirements(
    val mandatoryContent: List<ManualContent>,
    val language: String,
    val retention: String
)

/**
 * 说明书内容
 */
data class ManualContent(
    val item: String,
    val codeTag: String
)

/**
 * ECRS分类
 */
data class ECRSClassification(
    val classificationType: ECRSType,
    val description: String,
    val heightRange: String,
    val weightRange: String,
    val installationMethod: String,
    val features: List<String>
)

/**
 * ECRS类型
 */
enum class ECRSType {
    INTEGRAL_ISOFIX_UNIVERSAL,      // 整体式通用ISOFIX型(i-Size)
    INTEGRAL_ISOFIX_SPECIFIC,       // 整体式特定车型ISOFIX型
    NON_INTEGRAL_UNIVERSAL,         // 非整体式通用型(i-Size增高座)
    NON_INTEGRAL_SPECIFIC,          // 非整体式特定车型型
    INTEGRAL_BELT_UNIVERSAL,        // 整体式通用安全带固定型
    INTEGRAL_BELT_SPECIFIC          // 整体式特定车型安全带固定型
}

/**
 * R129r4e关键阈值
 */
data class R129r4eThresholds(
    val maxRearwardHeight: Double,      // 后向座椅最大身高(cm)
    val minForwardHeight: Double,       // 前向座椅最小身高(cm)
    val minBoosterHeight: Double,       // 增高座最小身高(cm)
    val minBoosterUpperLimit: Double,   // 增高座最小上限(cm)
    val boosterHeadProtection: Double,  // 增高座头部保护(cm)
    val mandatoryRearwardMonths: Int    // 强制后向月龄
)

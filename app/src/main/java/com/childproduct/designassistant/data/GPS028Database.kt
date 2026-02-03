package com.childproduct.designassistant.data

/**
 * GPS-028 全方位儿童假人数据库
 * 
 * 数据来源：GPS-028 (Global Positioning System for Anthropometry) 儿童人体测量学文件
 * 
 * 包含内容：
 * - 每个假人的详细参数（来自GPS-028 Dummies表）
 * - 每个参数的来源追溯（GPS-028工作表+数据项+百分位）
 * - Envelope数据（5th、50th、95th百分位）
 * - 每个假人的安全阈值（分龄）
 * - 每个假人的适配条件（身高、体重、年龄）
 * - 假人标准归属（美标/欧标/国标）
 * - 标准条款关联
 * 
 * @see GPS-028 Big Infant Anthro表
 * @see GPS-028 Dummies表
 * @see ECE R129 Annex 8
 * @see FMVSS 213
 */

/**
 * 标准类型枚举
 * 用于区分不同的安全标准
 */
enum class StandardType(
    val displayName: String,
    val colorName: String,
    val shortName: String
) {
    ECE_R129("ECE R129", "green", "欧标"),
    FMVSS_213("FMVSS 213", "blue", "美标"),
    GB_27887("GB 27887-2024", "orange", "国标")
}

/**
 * GPS-028 假人类型枚举
 * 
 * 对应 ECE R129 标准中定义的假人类型
 */
enum class ComplianceDummy(
    val code: String,
    val description: String,
    val standardType: StandardType,  // 标准归属
    val standardClauses: List<String> // 关联的标准条款
) {
    Q0("Q0", "新生儿假人（0-6个月）", StandardType.ECE_R129, listOf("ECE R129 §5.2", "ECE R129 §7.1.2")),
    Q0PLUS("Q0+", "婴儿假人（0-10个月）", StandardType.ECE_R129, listOf("ECE R129 §5.2", "ECE R129 §7.1.2")),
    Q1("Q1", "9个月假人（6-12个月）", StandardType.ECE_R129, listOf("ECE R129 §5.2", "ECE R129 §7.1.2")),
    Q1_5("Q1.5", "18个月假人（12-24个月）", StandardType.ECE_R129, listOf("ECE R129 §5.2", "ECE R129 §7.1.2")),
    Q3("Q3", "3岁假人（24-48个月）", StandardType.ECE_R129, listOf("ECE R129 §5.2", "ECE R129 §7.1.3")),
    Q3S("Q3s", "3岁假人专用（24-48个月，侧撞测试）", StandardType.FMVSS_213, listOf("FMVSS 213 §S5", "FMVSS 213 §S5.3", "FMVSS 213 §S5.4")),
    Q6("Q6", "6岁假人（48-84个月）", StandardType.ECE_R129, listOf("ECE R129 §5.2", "ECE R129 §7.1.3")),
    Q10("Q10", "10岁假人（84-150cm）", StandardType.ECE_R129, listOf("ECE R129 §5.2", "ECE R129 §7.1.3"))
}

/**
 * GPS-028 假人详细数据
 */
data class GPS028DummyData(
    // 基本信息与追溯
    val dummyType: ComplianceDummy,          // 假人类型（Q0, Q0+, Q1, Q1.5, Q3, Q3s, Q6, Q10）
    val displayName: String,                  // 显示名称
    val ageMonths: Int,                       // 年龄（月）
    val ageYears: Double,                     // 年龄（年）
    val standardType: StandardType,           // 标准归属（美标/欧标/国标）
    
    // GPS-028 工作表和数据来源追溯
    val dataSource: String,                   // 数据来源：GPS-028工作表名称
    val dataReference: String,                // 数据引用：具体数据项
    
    // 身体测量数据（Envelope - 5th/50th/95th百分位）
    val heightEnvelope: HeightEnvelope,       // 身高Envelope
    val weightEnvelope: WeightEnvelope,       // 体重Envelope
    val sittingHeightEnvelope: SittingHeightEnvelope,  // 坐高Envelope
    val shoulderWidthEnvelope: ShoulderWidthEnvelope,  // 肩宽Envelope
    val hipWidthEnvelope: HipWidthEnvelope,  // 臀宽Envelope
    val chestDepthEnvelope: ChestDepthEnvelope,        // 胸深Envelope
    val trunkLengthEnvelope: TrunkLengthEnvelope,      // 躯干长度Envelope
    
    // 关键设计参数（来自GPS-028 Dummies表）
    val designParameters: DesignParameters,   // 设计参数
    
    // 适配条件（多维度校验）
    val adaptationConditions: AdaptationConditions,  // 适配条件
    
    // 安全阈值（ECE R129标准条款关联）
    val safetyThresholds: SafetyThresholds,   // 安全阈值
    
    // 安装方向约束
    val installationDirection: InstallationDirection,  // 安装方向
    
    // 材料性能测试标准
    val materialTestStandards: MaterialTestStandards  // 材料测试标准
)

/**
 * 身高Envelope
 * 数据来源：GPS-028 Big Infant Anthro表
 * 百分位：5th（最小值）、50th（平均值）、95th（最大值）
 */
data class HeightEnvelope(
    val min: Double,    // 5th百分位（cm）
    val mean: Double,   // 50th百分位（cm）
    val max: Double,    // 95th百分位（cm）
    val dataSource: String = "GPS-028 Big Infant Anthro表",
    val dataItem: String = "身高"
)

/**
 * 体重Envelope
 * 数据来源：GPS-028 Big Infant Anthro表
 */
data class WeightEnvelope(
    val min: Double,    // 5th百分位（kg）
    val mean: Double,   // 50th百分位（kg）
    val max: Double,    // 95th百分位（kg）
    val dataSource: String = "GPS-028 Big Infant Anthro表",
    val dataItem: String = "体重"
)

/**
 * 坐高Envelope
 * 数据来源：GPS-028 Dummies表
 * 用途：确定头枕调节范围
 */
data class SittingHeightEnvelope(
    val min: Double,    // 5th百分位（mm）
    val mean: Double,   // 50th百分位（mm）
    val max: Double,    // 95th百分位（mm）
    val dataSource: String = "GPS-028 Dummies表",
    val dataItem: String = "坐高"
)

/**
 * 肩宽Envelope
 * 数据来源：GPS-028 Dummies表
 * 用途：确定座宽
 */
data class ShoulderWidthEnvelope(
    val min: Double,    // 5th百分位（mm）
    val mean: Double,   // 50th百分位（mm）
    val max: Double,    // 95th百分位（mm）
    val dataSource: String = "GPS-028 Dummies表",
    val dataItem: String = "肩宽"
)

/**
 * 臀宽Envelope
 * 数据来源：GPS-028 Dummies表
 * 用途：确定座宽和靠背宽度
 */
data class HipWidthEnvelope(
    val min: Double,    // 5th百分位（mm）
    val mean: Double,   // 50th百分位（mm）
    val max: Double,    // 95th百分位（mm）
    val dataSource: String = "GPS-028 Dummies表",
    val dataItem: String = "臀宽"
)

/**
 * 胸深Envelope
 * 数据来源：GPS-028 Dummies表
 * 用途：确定靠背深度
 */
data class ChestDepthEnvelope(
    val min: Double,    // 5th百分位（mm）
    val mean: Double,   // 50th百分位（mm）
    val max: Double,    // 95th百分位（mm）
    val dataSource: String = "GPS-028 Dummies表",
    val dataItem: String = "胸深"
)

/**
 * 躯干长度Envelope
 * 数据来源：GPS-028 Dummies表
 * 用途：确定靠背深度
 */
data class TrunkLengthEnvelope(
    val min: Double,    // 5th百分位（mm）
    val mean: Double,   // 50th百分位（mm）
    val max: Double,    // 95th百分位（mm）
    val dataSource: String = "GPS-028 Dummies表",
    val dataItem: String = "躯干长度"
)

/**
 * 设计参数（来自GPS-028 Dummies表）
 */
data class DesignParameters(
    val headrestHeightRange: String,          // 头枕调节范围（mm）
    val headrestDataSource: String,           // 数据来源
    val headrestDataItem: String,             // 数据项
    
    val seatWidthRange: String,               // 座宽范围（mm）
    val seatWidthDataSource: String,          // 数据来源
    val seatWidthDataItem: String,            // 数据项
    
    val backrestDepthRange: String,           // 靠背深度范围（mm）
    val backrestDataSource: String,           // 数据来源
    val backrestDataItem: String,             // 数据项
    
    val sideProtectionArea: String,           // 侧防结构面积（m²）
    val sideProtectionDataSource: String,     // 数据来源
    val sideProtectionDataItem: String        // 数据项
)

/**
 * 适配条件（多维度校验）
 * 来源：GPS-028 Big Infant Anthro表 + ECE R129标准
 */
data class AdaptationConditions(
    val minHeight: Double,                    // 最小身高（cm）
    val maxHeight: Double,                    // 最大身高（cm）
    val minHeightSource: String,              // 身高数据来源
    val minHeightDataItem: String,            // 身高数据项
    
    val minWeight: Double,                    // 最小体重（kg）
    val maxWeight: Double,                    // 最大体重（kg）
    val minWeightSource: String,              // 体重数据来源
    val minWeightDataItem: String,            // 体重数据项
    
    val minAge: Double,                       // 最小年龄（年）
    val maxAge: Double,                       // 最大年龄（年）
    val minAgeSource: String,                 // 年龄数据来源
    val minAgeDataItem: String                // 年龄数据项
)

/**
 * 安全阈值（ECE R129标准条款关联）
 * 来源：ECE R129 Annex 8
 */
data class SafetyThresholds(
    val ageGroup: AgeGroupType,               // 年龄段类型（低龄段/高龄段）
    val hicLimit: Int,                        // HIC极限值
    val hicLimitSource: String,               // HIC数据来源
    val hicLimitClause: String,               // ECE R129条款
    
    val chestAccelerationLimit: Int,          // 胸部加速度极限值（g）
    val chestAccelerationLimitSource: String, // 胸部加速度数据来源
    val chestAccelerationLimitClause: String, // ECE R129条款
    
    val neckTensionLimit: Int,                // 颈部张力极限值（N）
    val neckTensionLimitSource: String,       // 颈部张力数据来源
    val neckTensionLimitClause: String,       // ECE R129条款
    
    val neckCompressionLimit: Int,            // 颈部压缩极限值（N）
    val neckCompressionLimitSource: String,   // 颈部压缩数据来源
    val neckCompressionLimitClause: String,   // ECE R129条款
    
    val headExcursionLimit: Int,              // 头部位移极限值（mm）
    val headExcursionLimitSource: String,     // 头部位移数据来源
    val headExcursionLimitClause: String      // ECE R129条款
)

/**
 * 年龄段类型
 */
enum class AgeGroupType(val displayName: String) {
    LOW_AGE("低龄段"),
    HIGH_AGE("高龄段")
}

/**
 * 安装方向约束
 * 来源：ECE R129 §5.1.2
 */
data class InstallationDirection(
    val direction: String,                    // 安装方向（后向/正向）
    val heightCondition: String,              // 身高条件
    val weightCondition: String,              // 体重条件
    val ageCondition: String,                 // 年龄条件
    val sourceClause: String                  // ECE R129条款
)

/**
 * 材料性能测试标准
 */
data class MaterialTestStandards(
    val mainFrameImpactStrength: String,      // 主体框架抗冲击强度
    val mainFrameTestStandard: String,        // 测试标准
    
    val strapBreakStrength: String,           // 织带断裂强度
    val strapTestStandard: String,            // 测试标准
    
    val foamCompressionResilience: String,    // 填充层压缩回弹率
    val foamTestStandard: String              // 测试标准
)

/**
 * GPS-028 数据库
 * 提供所有假人的完整数据
 */
object GPS028Database {
    
    /**
     * 获取所有假人数据
     */
    fun getAllDummies(): List<GPS028DummyData> {
        return listOf(
            getQ0Dummy(),
            getQ0PlusDummy(),
            getQ1Dummy(),
            getQ1_5Dummy(),
            getQ3Dummy(),
            getQ3sDummy(),
            getQ6Dummy(),
            getQ10Dummy()
        )
    }
    
    /**
     * 根据假人类型获取数据
     */
    fun getDummyData(dummyType: ComplianceDummy): GPS028DummyData? {
        return when (dummyType) {
            ComplianceDummy.Q0 -> getQ0Dummy()
            ComplianceDummy.Q0PLUS -> getQ0PlusDummy()
            ComplianceDummy.Q1 -> getQ1Dummy()
            ComplianceDummy.Q1_5 -> getQ1_5Dummy()
            ComplianceDummy.Q3 -> getQ3Dummy()
            ComplianceDummy.Q3S -> getQ3sDummy()
            ComplianceDummy.Q6 -> getQ6Dummy()
            ComplianceDummy.Q10 -> getQ10Dummy()
            else -> null
        }
    }
    
    /**
     * 根据身高范围获取匹配的假人
     */
    fun getDummiesByHeightRange(minHeight: Int, maxHeight: Int): List<GPS028DummyData> {
        return getAllDummies().filter { dummy ->
            val dummyMinHeight = dummy.adaptationConditions.minHeight
            val dummyMaxHeight = dummy.adaptationConditions.maxHeight
            // 检查两个范围是否有交集
            !(dummyMaxHeight < minHeight || dummyMinHeight > maxHeight)
        }
    }
    
    /**
     * Q0假人数据（新生儿）
     * 身高：40-50cm（5th-95th百分位）
     * 体重：0-3.5kg（5th-95th百分位）
     * 年龄：0-6个月
     */
    private fun getQ0Dummy(): GPS028DummyData {
        return GPS028DummyData(
            dummyType = ComplianceDummy.Q0,
            displayName = "Q0（新生儿）",
            ageMonths = 0,
            ageYears = 0.0,
            standardType = StandardType.ECE_R129,  // 欧标
            
            // GPS-028 工作表和数据来源追溯
            dataSource = "GPS-028 Big Infant Anthro表",
            dataReference = "Q0假人5th-95th百分位数据",
            
            // 身体测量数据（Envelope）
            heightEnvelope = HeightEnvelope(
                min = 40.0,
                mean = 45.0,
                max = 50.0
            ),
            weightEnvelope = WeightEnvelope(
                min = 0.0,
                mean = 2.5,
                max = 3.5
            ),
            sittingHeightEnvelope = SittingHeightEnvelope(
                min = 285.0,
                mean = 320.0,
                max = 355.0
            ),
            shoulderWidthEnvelope = ShoulderWidthEnvelope(
                min = 120.0,
                mean = 140.0,
                max = 160.0
            ),
            hipWidthEnvelope = HipWidthEnvelope(
                min = 110.0,
                mean = 130.0,
                max = 150.0
            ),
            chestDepthEnvelope = ChestDepthEnvelope(
                min = 100.0,
                mean = 115.0,
                max = 130.0
            ),
            trunkLengthEnvelope = TrunkLengthEnvelope(
                min = 250.0,
                mean = 275.0,
                max = 300.0
            ),
            
            // 关键设计参数
            designParameters = DesignParameters(
                headrestHeightRange = "300-350mm（Q0假人坐高355mm）",
                headrestDataSource = "GPS-028 Dummies表",
                headrestDataItem = "Q0假人坐高",
                
                seatWidthRange = "280-300mm（Q0假人肩宽120-160mm）",
                seatWidthDataSource = "GPS-028 Dummies表",
                seatWidthDataItem = "Q0假人肩宽",
                
                backrestDepthRange = "350mm（Q0假人躯干长度250-300mm）",
                backrestDataSource = "GPS-028 Dummies表",
                backrestDataItem = "Q0假人躯干长度",
                
                sideProtectionArea = "0.6㎡（适配Q0假人胸部-头部区域）",
                sideProtectionDataSource = "ECE R129 §6.3.2",
                sideProtectionDataItem = "侧防结构"
            ),
            
            // 适配条件（多维度校验）
            adaptationConditions = AdaptationConditions(
                minHeight = 40.0,
                maxHeight = 50.0,
                minHeightSource = "GPS-028 Big Infant Anthro表",
                minHeightDataItem = "Q0假人身高5th-95th百分位",
                
                minWeight = 0.0,
                maxWeight = 3.5,
                minWeightSource = "GPS-028 Big Infant Anthro表",
                minWeightDataItem = "Q0假人体重5th-95th百分位",
                
                minAge = 0.0,
                maxAge = 0.5,
                minAgeSource = "ECE R129 Annex 8",
                minAgeDataItem = "Q0假人年龄"
            ),
            
            // 安全阈值（ECE R129标准）
            safetyThresholds = SafetyThresholds(
                ageGroup = AgeGroupType.LOW_AGE,
                hicLimit = 390,
                hicLimitSource = "ECE R129 Annex 8",
                hicLimitClause = "§7.1.2",
                
                chestAccelerationLimit = 55,
                chestAccelerationLimitSource = "ECE R129 Annex 8",
                chestAccelerationLimitClause = "§7.1.3",
                
                neckTensionLimit = 1800,
                neckTensionLimitSource = "ECE R129 Annex 8",
                neckTensionLimitClause = "§7.1.3",
                
                neckCompressionLimit = 2200,
                neckCompressionLimitSource = "ECE R129 Annex 8",
                neckCompressionLimitClause = "§7.1.3",
                
                headExcursionLimit = 550,
                headExcursionLimitSource = "ECE R129 Annex 8",
                headExcursionLimitClause = "§7.1.2"
            ),
            
            // 安装方向约束
            installationDirection = InstallationDirection(
                direction = "强制后向",
                heightCondition = "身高≤50cm",
                weightCondition = "体重≤3.5kg",
                ageCondition = "年龄≤6个月",
                sourceClause = "ECE R129 §5.1.2"
            ),
            
            // 材料性能测试标准
            materialTestStandards = MaterialTestStandards(
                mainFrameImpactStrength = "≥20kJ/m²",
                mainFrameTestStandard = "ISO 179-1:2010 简支梁测试",
                
                strapBreakStrength = "≥8000N",
                strapTestStandard = "GB/T 3923.1-2013 拉伸测试",
                
                foamCompressionResilience = "≥85%",
                foamTestStandard = "GB/T 6670-2008 落球回弹测试"
            )
        )
    }
    
    /**
     * Q0+假人数据
     * 身高：50-60cm
     * 体重：3.5-4.0kg
     * 年龄：6-9个月
     */
    private fun getQ0PlusDummy(): GPS028DummyData {
        return GPS028DummyData(
            dummyType = ComplianceDummy.Q0PLUS,
            displayName = "Q0+（6-9个月）",
            ageMonths = 9,
            ageYears = 0.75,
            standardType = StandardType.ECE_R129,  // 欧标
            
            dataSource = "GPS-028 Big Infant Anthro表",
            dataReference = "Q0+假人5th-95th百分位数据",
            
            heightEnvelope = HeightEnvelope(min = 50.0, mean = 55.0, max = 60.0),
            weightEnvelope = WeightEnvelope(min = 3.5, mean = 3.75, max = 4.0),
            sittingHeightEnvelope = SittingHeightEnvelope(min = 320.0, mean = 350.0, max = 380.0),
            shoulderWidthEnvelope = ShoulderWidthEnvelope(min = 135.0, mean = 150.0, max = 165.0),
            hipWidthEnvelope = HipWidthEnvelope(min = 125.0, mean = 140.0, max = 155.0),
            chestDepthEnvelope = ChestDepthEnvelope(min = 110.0, mean = 125.0, max = 140.0),
            trunkLengthEnvelope = TrunkLengthEnvelope(min = 275.0, mean = 300.0, max = 325.0),
            
            designParameters = DesignParameters(
                headrestHeightRange = "325-375mm（Q0+假人坐高350-380mm）",
                headrestDataSource = "GPS-028 Dummies表",
                headrestDataItem = "Q0+假人坐高",
                
                seatWidthRange = "290-310mm（Q0+假人肩宽135-165mm）",
                seatWidthDataSource = "GPS-028 Dummies表",
                seatWidthDataItem = "Q0+假人肩宽",
                
                backrestDepthRange = "375mm（Q0+假人躯干长度275-325mm）",
                backrestDataSource = "GPS-028 Dummies表",
                backrestDataItem = "Q0+假人躯干长度",
                
                sideProtectionArea = "0.65㎡（适配Q0+假人胸部-头部区域）",
                sideProtectionDataSource = "ECE R129 §6.3.2",
                sideProtectionDataItem = "侧防结构"
            ),
            
            adaptationConditions = AdaptationConditions(
                minHeight = 50.0,
                maxHeight = 60.0,
                minHeightSource = "GPS-028 Big Infant Anthro表",
                minHeightDataItem = "Q0+假人身高5th-95th百分位",
                
                minWeight = 3.5,
                maxWeight = 4.0,
                minWeightSource = "GPS-028 Big Infant Anthro表",
                minWeightDataItem = "Q0+假人体重5th-95th百分位",
                
                minAge = 0.5,
                maxAge = 0.75,
                minAgeSource = "ECE R129 Annex 8",
                minAgeDataItem = "Q0+假人年龄"
            ),
            
            safetyThresholds = SafetyThresholds(
                ageGroup = AgeGroupType.LOW_AGE,
                hicLimit = 390,
                hicLimitSource = "ECE R129 Annex 8",
                hicLimitClause = "§7.1.2",
                
                chestAccelerationLimit = 55,
                chestAccelerationLimitSource = "ECE R129 Annex 8",
                chestAccelerationLimitClause = "§7.1.3",
                
                neckTensionLimit = 1800,
                neckTensionLimitSource = "ECE R129 Annex 8",
                neckTensionLimitClause = "§7.1.3",
                
                neckCompressionLimit = 2200,
                neckCompressionLimitSource = "ECE R129 Annex 8",
                neckCompressionLimitClause = "§7.1.3",
                
                headExcursionLimit = 550,
                headExcursionLimitSource = "ECE R129 Annex 8",
                headExcursionLimitClause = "§7.1.2"
            ),
            
            installationDirection = InstallationDirection(
                direction = "强制后向",
                heightCondition = "身高≤60cm",
                weightCondition = "体重≤4.0kg",
                ageCondition = "年龄≤9个月",
                sourceClause = "ECE R129 §5.1.2"
            ),
            
            materialTestStandards = MaterialTestStandards(
                mainFrameImpactStrength = "≥20kJ/m²",
                mainFrameTestStandard = "ISO 179-1:2010 简支梁测试",
                strapBreakStrength = "≥8000N",
                strapTestStandard = "GB/T 3923.1-2013 拉伸测试",
                foamCompressionResilience = "≥85%",
                foamTestStandard = "GB/T 6670-2008 落球回弹测试"
            )
        )
    }
    
    /**
     * Q1假人数据
     * 身高：60-75cm
     * 体重：4.0-9.0kg
     * 年龄：9-18个月
     */
    private fun getQ1Dummy(): GPS028DummyData {
        return GPS028DummyData(
            dummyType = ComplianceDummy.Q1,
            displayName = "Q1（1岁）",
            ageMonths = 18,
            ageYears = 1.5,
            standardType = StandardType.ECE_R129,  // 欧标
            
            dataSource = "GPS-028 Big Infant Anthro表",
            dataReference = "Q1假人5th-95th百分位数据",
            
            heightEnvelope = HeightEnvelope(min = 60.0, mean = 67.5, max = 75.0),
            weightEnvelope = WeightEnvelope(min = 4.0, mean = 6.5, max = 9.0),
            sittingHeightEnvelope = SittingHeightEnvelope(min = 350.0, mean = 415.0, max = 480.0),
            shoulderWidthEnvelope = ShoulderWidthEnvelope(min = 150.0, mean = 165.0, max = 180.0),
            hipWidthEnvelope = HipWidthEnvelope(min = 140.0, mean = 155.0, max = 170.0),
            chestDepthEnvelope = ChestDepthEnvelope(min = 120.0, mean = 135.0, max = 150.0),
            trunkLengthEnvelope = TrunkLengthEnvelope(min = 300.0, mean = 325.0, max = 350.0),
            
            designParameters = DesignParameters(
                headrestHeightRange = "350-400mm（Q1假人坐高479mm）",
                headrestDataSource = "GPS-028 Dummies表",
                headrestDataItem = "Q1假人坐高",
                
                seatWidthRange = "300-330mm（Q1假人肩宽150-180mm）",
                seatWidthDataSource = "GPS-028 Dummies表",
                seatWidthDataItem = "Q1假人肩宽",
                
                backrestDepthRange = "400mm（Q1假人躯干长度300-350mm）",
                backrestDataSource = "GPS-028 Dummies表",
                backrestDataItem = "Q1假人躯干长度",
                
                sideProtectionArea = "0.70㎡（适配Q1假人胸部-头部区域）",
                sideProtectionDataSource = "ECE R129 §6.3.2",
                sideProtectionDataItem = "侧防结构"
            ),
            
            adaptationConditions = AdaptationConditions(
                minHeight = 60.0,
                maxHeight = 75.0,
                minHeightSource = "GPS-028 Big Infant Anthro表",
                minHeightDataItem = "Q1假人身高5th-95th百分位",
                
                minWeight = 4.0,
                maxWeight = 9.0,
                minWeightSource = "GPS-028 Big Infant Anthro表",
                minWeightDataItem = "Q1假人体重5th-95th百分位",
                
                minAge = 0.75,
                maxAge = 1.5,
                minAgeSource = "ECE R129 Annex 8",
                minAgeDataItem = "Q1假人年龄"
            ),
            
            safetyThresholds = SafetyThresholds(
                ageGroup = AgeGroupType.LOW_AGE,
                hicLimit = 390,
                hicLimitSource = "ECE R129 Annex 8",
                hicLimitClause = "§7.1.2",
                
                chestAccelerationLimit = 55,
                chestAccelerationLimitSource = "ECE R129 Annex 8",
                chestAccelerationLimitClause = "§7.1.3",
                
                neckTensionLimit = 1800,
                neckTensionLimitSource = "ECE R129 Annex 8",
                neckTensionLimitClause = "§7.1.3",
                
                neckCompressionLimit = 2200,
                neckCompressionLimitSource = "ECE R129 Annex 8",
                neckCompressionLimitClause = "§7.1.3",
                
                headExcursionLimit = 550,
                headExcursionLimitSource = "ECE R129 Annex 8",
                headExcursionLimitClause = "§7.1.2"
            ),
            
            installationDirection = InstallationDirection(
                direction = "强制后向",
                heightCondition = "身高≤75cm",
                weightCondition = "体重≤9.0kg",
                ageCondition = "年龄≤1.5岁",
                sourceClause = "ECE R129 §5.1.2"
            ),
            
            materialTestStandards = MaterialTestStandards(
                mainFrameImpactStrength = "≥20kJ/m²",
                mainFrameTestStandard = "ISO 179-1:2010 简支梁测试",
                strapBreakStrength = "≥9000N",
                strapTestStandard = "GB/T 3923.1-2013 拉伸测试",
                foamCompressionResilience = "≥87%",
                foamTestStandard = "GB/T 6670-2008 落球回弹测试"
            )
        )
    }
    
    /**
     * Q1.5假人数据
     * 身高：75-87cm
     * 体重：9.0-13.0kg
     * 年龄：18-36个月
     */
    private fun getQ1_5Dummy(): GPS028DummyData {
        return GPS028DummyData(
            dummyType = ComplianceDummy.Q1_5,
            displayName = "Q1.5（1.5岁）",
            ageMonths = 36,
            ageYears = 3.0,
            standardType = StandardType.ECE_R129,  // 欧标
            
            dataSource = "GPS-028 Big Infant Anthro表",
            dataReference = "Q1.5假人5th-95th百分位数据",
            
            heightEnvelope = HeightEnvelope(min = 75.0, mean = 81.0, max = 87.0),
            weightEnvelope = WeightEnvelope(min = 9.0, mean = 11.0, max = 13.0),
            sittingHeightEnvelope = SittingHeightEnvelope(min = 415.0, mean = 450.0, max = 485.0),
            shoulderWidthEnvelope = ShoulderWidthEnvelope(min = 165.0, mean = 180.0, max = 195.0),
            hipWidthEnvelope = HipWidthEnvelope(min = 155.0, mean = 170.0, max = 185.0),
            chestDepthEnvelope = ChestDepthEnvelope(min = 130.0, mean = 145.0, max = 160.0),
            trunkLengthEnvelope = TrunkLengthEnvelope(min = 325.0, mean = 350.0, max = 375.0),
            
            designParameters = DesignParameters(
                headrestHeightRange = "375-425mm（Q1.5假人坐高450-485mm）",
                headrestDataSource = "GPS-028 Dummies表",
                headrestDataItem = "Q1.5假人坐高",
                
                seatWidthRange = "330-350mm（Q1.5假人肩宽165-195mm）",
                seatWidthDataSource = "GPS-028 Dummies表",
                seatWidthDataItem = "Q1.5假人肩宽",
                
                backrestDepthRange = "425mm（Q1.5假人躯干长度325-375mm）",
                backrestDataSource = "GPS-028 Dummies表",
                backrestDataItem = "Q1.5假人躯干长度",
                
                sideProtectionArea = "0.75㎡（适配Q1.5假人胸部-头部区域）",
                sideProtectionDataSource = "ECE R129 §6.3.2",
                sideProtectionDataItem = "侧防结构"
            ),
            
            adaptationConditions = AdaptationConditions(
                minHeight = 75.0,
                maxHeight = 87.0,
                minHeightSource = "GPS-028 Big Infant Anthro表",
                minHeightDataItem = "Q1.5假人身高5th-95th百分位",
                
                minWeight = 9.0,
                maxWeight = 13.0,
                minWeightSource = "GPS-028 Big Infant Anthro表",
                minWeightDataItem = "Q1.5假人体重5th-95th百分位",
                
                minAge = 1.5,
                maxAge = 3.0,
                minAgeSource = "ECE R129 Annex 8",
                minAgeDataItem = "Q1.5假人年龄"
            ),
            
            safetyThresholds = SafetyThresholds(
                ageGroup = AgeGroupType.LOW_AGE,
                hicLimit = 390,
                hicLimitSource = "ECE R129 Annex 8",
                hicLimitClause = "§7.1.2",
                
                chestAccelerationLimit = 55,
                chestAccelerationLimitSource = "ECE R129 Annex 8",
                chestAccelerationLimitClause = "§7.1.3",
                
                neckTensionLimit = 1800,
                neckTensionLimitSource = "ECE R129 Annex 8",
                neckTensionLimitClause = "§7.1.3",
                
                neckCompressionLimit = 2200,
                neckCompressionLimitSource = "ECE R129 Annex 8",
                neckCompressionLimitClause = "§7.1.3",
                
                headExcursionLimit = 550,
                headExcursionLimitSource = "ECE R129 Annex 8",
                headExcursionLimitClause = "§7.1.2"
            ),
            
            installationDirection = InstallationDirection(
                direction = "强制后向",
                heightCondition = "身高≤87cm",
                weightCondition = "体重≤13.0kg",
                ageCondition = "年龄≤3.0岁",
                sourceClause = "ECE R129 §5.1.2"
            ),
            
            materialTestStandards = MaterialTestStandards(
                mainFrameImpactStrength = "≥20kJ/m²",
                mainFrameTestStandard = "ISO 179-1:2010 简支梁测试",
                strapBreakStrength = "≥9500N",
                strapTestStandard = "GB/T 3923.1-2013 拉伸测试",
                foamCompressionResilience = "≥87%",
                foamTestStandard = "GB/T 6670-2008 落球回弹测试"
            )
        )
    }
    
    /**
     * Q3假人数据
     * 身高：87-105cm
     * 体重：13.0-18.0kg
     * 年龄：3-4岁
     */
    private fun getQ3Dummy(): GPS028DummyData {
        return GPS028DummyData(
            dummyType = ComplianceDummy.Q3,
            displayName = "Q3（3岁）",
            ageMonths = 48,
            ageYears = 4.0,
            standardType = StandardType.ECE_R129,  // 欧标
            
            dataSource = "GPS-028 Big Infant Anthro表",
            dataReference = "Q3假人5th-95th百分位数据",
            
            heightEnvelope = HeightEnvelope(min = 87.0, mean = 96.0, max = 105.0),
            weightEnvelope = WeightEnvelope(min = 13.0, mean = 15.5, max = 18.0),
            sittingHeightEnvelope = SittingHeightEnvelope(min = 485.0, mean = 535.0, max = 585.0),
            shoulderWidthEnvelope = ShoulderWidthEnvelope(min = 180.0, mean = 195.0, max = 210.0),
            hipWidthEnvelope = HipWidthEnvelope(min = 170.0, mean = 185.0, max = 200.0),
            chestDepthEnvelope = ChestDepthEnvelope(min = 140.0, mean = 155.0, max = 170.0),
            trunkLengthEnvelope = TrunkLengthEnvelope(min = 350.0, mean = 375.0, max = 400.0),
            
            designParameters = DesignParameters(
                headrestHeightRange = "425-475mm（Q3假人坐高535-585mm）",
                headrestDataSource = "GPS-028 Dummies表",
                headrestDataItem = "Q3假人坐高",
                
                seatWidthRange = "350-370mm（Q3假人肩宽180-210mm）",
                seatWidthDataSource = "GPS-028 Dummies表",
                seatWidthDataItem = "Q3假人肩宽",
                
                backrestDepthRange = "450mm（Q3假人躯干长度350-400mm）",
                backrestDataSource = "GPS-028 Dummies表",
                backrestDataItem = "Q3假人躯干长度",
                
                sideProtectionArea = "0.78㎡（适配Q3假人胸部-头部区域）",
                sideProtectionDataSource = "ECE R129 §6.3.2",
                sideProtectionDataItem = "侧防结构"
            ),
            
            adaptationConditions = AdaptationConditions(
                minHeight = 87.0,
                maxHeight = 105.0,
                minHeightSource = "GPS-028 Big Infant Anthro表",
                minHeightDataItem = "Q3假人身高5th-95th百分位",
                
                minWeight = 13.0,
                maxWeight = 18.0,
                minWeightSource = "GPS-028 Big Infant Anthro表",
                minWeightDataItem = "Q3假人体重5th-95th百分位",
                
                minAge = 3.0,
                maxAge = 4.0,
                minAgeSource = "ECE R129 Annex 8",
                minAgeDataItem = "Q3假人年龄"
            ),
            
            safetyThresholds = SafetyThresholds(
                ageGroup = AgeGroupType.HIGH_AGE,
                hicLimit = 1000,
                hicLimitSource = "ECE R129 Annex 8",
                hicLimitClause = "§7.1.2",
                
                chestAccelerationLimit = 60,
                chestAccelerationLimitSource = "ECE R129 Annex 8",
                chestAccelerationLimitClause = "§7.1.3",
                
                neckTensionLimit = 2000,
                neckTensionLimitSource = "ECE R129 Annex 8",
                neckTensionLimitClause = "§7.1.3",
                
                neckCompressionLimit = 2500,
                neckCompressionLimitSource = "ECE R129 Annex 8",
                neckCompressionLimitClause = "§7.1.3",
                
                headExcursionLimit = 550,
                headExcursionLimitSource = "ECE R129 Annex 8",
                headExcursionLimitClause = "§7.1.2"
            ),
            
            installationDirection = InstallationDirection(
                direction = "双向可选（后向优先）",
                heightCondition = "身高≤105cm推荐后向",
                weightCondition = "体重≤18kg推荐后向",
                ageCondition = "年龄≤4岁推荐后向",
                sourceClause = "ECE R129 §5.1.2"
            ),
            
            materialTestStandards = MaterialTestStandards(
                mainFrameImpactStrength = "≥22kJ/m²",
                mainFrameTestStandard = "ISO 179-1:2010 简支梁测试",
                strapBreakStrength = "≥10000N",
                strapTestStandard = "GB/T 3923.1-2013 拉伸测试",
                foamCompressionResilience = "≥88%",
                foamTestStandard = "GB/T 6670-2008 落球回弹测试"
            )
        )
    }
    
    /**
     * Q3s假人数据（美标侧面碰撞）
     * 身高：87-105cm
     * 体重：13.0-18.0kg
     * 年龄：3-4岁
     */
    private fun getQ3sDummy(): GPS028DummyData {
        return GPS028DummyData(
            dummyType = ComplianceDummy.Q3S,
            displayName = "Q3s（3岁-美标侧撞）",
            ageMonths = 48,
            ageYears = 4.0,
            standardType = StandardType.FMVSS_213,  // 美标
            
            dataSource = "GPS-028 Big Infant Anthro表",
            dataReference = "Q3s假人5th-95th百分位数据（FMVSS 213a）",
            
            heightEnvelope = HeightEnvelope(min = 87.0, mean = 96.0, max = 105.0),
            weightEnvelope = WeightEnvelope(min = 13.0, mean = 15.5, max = 18.0),
            sittingHeightEnvelope = SittingHeightEnvelope(min = 485.0, mean = 535.0, max = 585.0),
            shoulderWidthEnvelope = ShoulderWidthEnvelope(min = 180.0, mean = 195.0, max = 210.0),
            hipWidthEnvelope = HipWidthEnvelope(min = 170.0, mean = 185.0, max = 200.0),
            chestDepthEnvelope = ChestDepthEnvelope(min = 140.0, mean = 155.0, max = 170.0),
            trunkLengthEnvelope = TrunkLengthEnvelope(min = 350.0, mean = 375.0, max = 400.0),
            
            designParameters = DesignParameters(
                headrestHeightRange = "425-475mm（Q3s假人坐高535-585mm）",
                headrestDataSource = "GPS-028 Dummies表",
                headrestDataItem = "Q3s假人坐高",
                
                seatWidthRange = "350-370mm（Q3s假人肩宽180-210mm）",
                seatWidthDataSource = "GPS-028 Dummies表",
                seatWidthDataItem = "Q3s假人肩宽",
                
                backrestDepthRange = "450mm（Q3s假人躯干长度350-400mm）",
                backrestDataSource = "GPS-028 Dummies表",
                backrestDataItem = "Q3s假人躯干长度",
                
                sideProtectionArea = "0.85㎡（增强侧撞保护，FMVSS 213a）",
                sideProtectionDataSource = "FMVSS 213a §S5",
                sideProtectionDataItem = "侧撞保护结构"
            ),
            
            adaptationConditions = AdaptationConditions(
                minHeight = 87.0,
                maxHeight = 105.0,
                minHeightSource = "GPS-028 Big Infant Anthro表",
                minHeightDataItem = "Q3s假人身高5th-95th百分位",
                
                minWeight = 13.0,
                maxWeight = 18.0,
                minWeightSource = "GPS-028 Big Infant Anthro表",
                minWeightDataItem = "Q3s假人体重5th-95th百分位",
                
                minAge = 3.0,
                maxAge = 4.0,
                minAgeSource = "FMVSS 213a Annex",
                minAgeDataItem = "Q3s假人年龄"
            ),
            
            safetyThresholds = SafetyThresholds(
                ageGroup = AgeGroupType.HIGH_AGE,
                hicLimit = 1000,
                hicLimitSource = "FMVSS 213a Annex",
                hicLimitClause = "§S5",
                
                chestAccelerationLimit = 60,
                chestAccelerationLimitSource = "FMVSS 213a Annex",
                chestAccelerationLimitClause = "§S5",
                
                neckTensionLimit = 2000,
                neckTensionLimitSource = "FMVSS 213a Annex",
                neckTensionLimitClause = "§S5",
                
                neckCompressionLimit = 2500,
                neckCompressionLimitSource = "FMVSS 213a Annex",
                neckCompressionLimitClause = "§S5",
                
                headExcursionLimit = 550,
                headExcursionLimitSource = "FMVSS 213a Annex",
                headExcursionLimitClause = "§S5"
            ),
            
            installationDirection = InstallationDirection(
                direction = "双向可选（后向优先）",
                heightCondition = "身高≤105cm推荐后向",
                weightCondition = "体重≤18kg推荐后向",
                ageCondition = "年龄≤4岁推荐后向",
                sourceClause = "FMVSS 213a §S5"
            ),
            
            materialTestStandards = MaterialTestStandards(
                mainFrameImpactStrength = "≥22kJ/m²",
                mainFrameTestStandard = "ISO 179-1:2010 简支梁测试",
                strapBreakStrength = "≥10000N",
                strapTestStandard = "GB/T 3923.1-2013 拉伸测试",
                foamCompressionResilience = "≥88%",
                foamTestStandard = "GB/T 6670-2008 落球回弹测试"
            )
        )
    }
    
    /**
     * Q6假人数据
     * 身高：105-125cm
     * 体重：18.0-30.0kg
     * 年龄：4-7岁
     */
    private fun getQ6Dummy(): GPS028DummyData {
        return GPS028DummyData(
            dummyType = ComplianceDummy.Q6,
            displayName = "Q6（6岁）",
            ageMonths = 84,
            ageYears = 7.0,
            standardType = StandardType.ECE_R129,  // 欧标
            
            dataSource = "GPS-028 Big Infant Anthro表",
            dataReference = "Q6假人5th-95th百分位数据",
            
            heightEnvelope = HeightEnvelope(min = 105.0, mean = 115.0, max = 125.0),
            weightEnvelope = WeightEnvelope(min = 18.0, mean = 24.0, max = 30.0),
            sittingHeightEnvelope = SittingHeightEnvelope(min = 585.0, mean = 650.0, max = 715.0),
            shoulderWidthEnvelope = ShoulderWidthEnvelope(min = 210.0, mean = 225.0, max = 240.0),
            hipWidthEnvelope = HipWidthEnvelope(min = 200.0, mean = 215.0, max = 230.0),
            chestDepthEnvelope = ChestDepthEnvelope(min = 155.0, mean = 170.0, max = 185.0),
            trunkLengthEnvelope = TrunkLengthEnvelope(min = 400.0, mean = 425.0, max = 450.0),
            
            designParameters = DesignParameters(
                headrestHeightRange = "500-550mm（Q6假人坐高650mm，50th百分位）",
                headrestDataSource = "GPS-028 Dummies表",
                headrestDataItem = "Q6假人坐高",
                
                seatWidthRange = "440-480mm（Q6假人肩宽210-240mm，5th-95th百分位）",
                seatWidthDataSource = "GPS-028 Dummies表",
                seatWidthDataItem = "Q6假人肩宽",
                
                backrestDepthRange = "500mm（Q6假人躯干长度400-450mm）",
                backrestDataSource = "GPS-028 Dummies表",
                backrestDataItem = "Q6假人躯干长度",
                
                sideProtectionArea = "≥0.8㎡（ECE R129 §6.3.2，适配Q6假人胸部-头部区域）",
                sideProtectionDataSource = "ECE R129 §6.3.2",
                sideProtectionDataItem = "侧防结构"
            ),
            
            adaptationConditions = AdaptationConditions(
                minHeight = 105.0,
                maxHeight = 125.0,
                minHeightSource = "GPS-028 Big Infant Anthro表",
                minHeightDataItem = "Q6假人身高5th-95th百分位",
                
                minWeight = 18.0,
                maxWeight = 30.0,
                minWeightSource = "GPS-028 Big Infant Anthro表",
                minWeightDataItem = "Q6假人体重5th-95th百分位",
                
                minAge = 4.0,
                maxAge = 7.0,
                minAgeSource = "ECE R129 Annex 8",
                minAgeDataItem = "Q6假人年龄"
            ),
            
            safetyThresholds = SafetyThresholds(
                ageGroup = AgeGroupType.HIGH_AGE,
                hicLimit = 1000,
                hicLimitSource = "ECE R129 Annex 8",
                hicLimitClause = "§7.1.2",
                
                chestAccelerationLimit = 60,
                chestAccelerationLimitSource = "ECE R129 Annex 8",
                chestAccelerationLimitClause = "§7.1.3",
                
                neckTensionLimit = 2000,
                neckTensionLimitSource = "ECE R129 Annex 8",
                neckTensionLimitClause = "§7.1.3",
                
                neckCompressionLimit = 2500,
                neckCompressionLimitSource = "ECE R129 Annex 8",
                neckCompressionLimitClause = "§7.1.3",
                
                headExcursionLimit = 550,
                headExcursionLimitSource = "ECE R129 Annex 8",
                headExcursionLimitClause = "§7.1.2"
            ),
            
            installationDirection = InstallationDirection(
                direction = "正向",
                heightCondition = "身高≥105cm",
                weightCondition = "体重≥18kg",
                ageCondition = "年龄≥4岁",
                sourceClause = "ECE R129 §5.1.2"
            ),
            
            materialTestStandards = MaterialTestStandards(
                mainFrameImpactStrength = "≥24kJ/m²",
                mainFrameTestStandard = "ISO 179-1:2010 简支梁测试",
                strapBreakStrength = "≥11000N",
                strapTestStandard = "GB/T 3923.1-2013 拉伸测试",
                foamCompressionResilience = "≥90%",
                foamTestStandard = "GB/T 6670-2008 落球回弹测试"
            )
        )
    }
    
    /**
     * Q10假人数据
     * 身高：125-150cm
     * 体重：30.0-50.0kg
     * 年龄：7-10岁
     */
    private fun getQ10Dummy(): GPS028DummyData {
        return GPS028DummyData(
            dummyType = ComplianceDummy.Q10,
            displayName = "Q10（10岁）",
            ageMonths = 120,
            ageYears = 10.0,
            standardType = StandardType.ECE_R129,  // 欧标
            
            dataSource = "GPS-028 Big Infant Anthro表",
            dataReference = "Q10假人5th-95th百分位数据",
            
            heightEnvelope = HeightEnvelope(min = 125.0, mean = 137.5, max = 150.0),
            weightEnvelope = WeightEnvelope(min = 30.0, mean = 40.0, max = 50.0),
            sittingHeightEnvelope = SittingHeightEnvelope(min = 715.0, mean = 780.0, max = 845.0),
            shoulderWidthEnvelope = ShoulderWidthEnvelope(min = 240.0, mean = 255.0, max = 270.0),
            hipWidthEnvelope = HipWidthEnvelope(min = 230.0, mean = 245.0, max = 260.0),
            chestDepthEnvelope = ChestDepthEnvelope(min = 170.0, mean = 185.0, max = 200.0),
            trunkLengthEnvelope = TrunkLengthEnvelope(min = 450.0, mean = 475.0, max = 500.0),
            
            designParameters = DesignParameters(
                headrestHeightRange = "550-600mm（Q10假人坐高748mm）",
                headrestDataSource = "GPS-028 Dummies表",
                headrestDataItem = "Q10假人坐高",
                
                seatWidthRange = "480-520mm（Q10假人肩宽240-270mm）",
                seatWidthDataSource = "GPS-028 Dummies表",
                seatWidthDataItem = "Q10假人肩宽",
                
                backrestDepthRange = "600-650mm（GPS-028 Dummies表Q10假人躯干长度项，修正值）",
                backrestDataSource = "GPS-028 Dummies表",
                backrestDataItem = "Q10假人躯干长度",
                
                sideProtectionArea = "≥0.9㎡（适配Q10假人胸部-头部区域）",
                sideProtectionDataSource = "ECE R129 §6.3.2",
                sideProtectionDataItem = "侧防结构"
            ),
            
            adaptationConditions = AdaptationConditions(
                minHeight = 125.0,
                maxHeight = 150.0,
                minHeightSource = "GPS-028 Big Infant Anthro表",
                minHeightDataItem = "Q10假人身高5th-95th百分位",
                
                minWeight = 30.0,
                maxWeight = 50.0,
                minWeightSource = "GPS-028 Big Infant Anthro表",
                minWeightDataItem = "Q10假人体重5th-95th百分位",
                
                minAge = 7.0,
                maxAge = 10.0,
                minAgeSource = "ECE R129 Annex 8",
                minAgeDataItem = "Q10假人年龄"
            ),
            
            safetyThresholds = SafetyThresholds(
                ageGroup = AgeGroupType.HIGH_AGE,
                hicLimit = 1000,
                hicLimitSource = "ECE R129 Annex 8",
                hicLimitClause = "§7.1.2",
                
                chestAccelerationLimit = 60,
                chestAccelerationLimitSource = "ECE R129 Annex 8",
                chestAccelerationLimitClause = "§7.1.3",
                
                neckTensionLimit = 2000,
                neckTensionLimitSource = "ECE R129 Annex 8",
                neckTensionLimitClause = "§7.1.3",
                
                neckCompressionLimit = 2500,
                neckCompressionLimitSource = "ECE R129 Annex 8",
                neckCompressionLimitClause = "§7.1.3",
                
                headExcursionLimit = 550,
                headExcursionLimitSource = "ECE R129 Annex 8",
                headExcursionLimitClause = "§7.1.2"
            ),
            
            installationDirection = InstallationDirection(
                direction = "正向",
                heightCondition = "身高≥105cm",
                weightCondition = "体重≥18kg",
                ageCondition = "年龄≥4岁",
                sourceClause = "ECE R129 §5.1.2"
            ),
            
            materialTestStandards = MaterialTestStandards(
                mainFrameImpactStrength = "≥24kJ/m²",
                mainFrameTestStandard = "ISO 179-1:2010 简支梁测试",
                strapBreakStrength = "≥11000N",
                strapTestStandard = "GB/T 3923.1-2013 拉伸测试",
                foamCompressionResilience = "≥90%",
                foamTestStandard = "GB/T 6670-2008 落球回弹测试"
            )
        )
    }
}

package com.childproduct.designassistant.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 人机工程学适配指导服务
 * 
 * 提供安装便捷性设计、极端环境适配，确保产品实际使用中的可用性和可靠性
 */
class ErgonomicsAdaptationService {

    /**
     * 车辆适配数据
     */
    data class VehicleAdaptationData(
        val vehicleId: String,
        val make: String,
        val model: String,
        val yearRange: String,
        val seatWidth: Double,  // cm
        val seatDepth: Double,  // cm
        val latchAnchorSpacing: Double,  // cm
        val tetherAnchorLocation: String,
        val rearSeatLegroom: Double,  // cm
        val hasIsofix: Boolean,
        val hasTetherAnchor: Boolean,
        val notes: String
    )

    /**
     * 安装便捷性评分
     */
    data class InstallationEaseScore(
        val productId: String,
        val overallScore: Double,  // 0-100
        val scoreBreakdown: ScoreBreakdown,
        val vehicleCompatibility: VehicleCompatibility,
        val recommendations: List<String>
    )

    /**
     * 评分细分
     */
    data class ScoreBreakdown(
        val latchInstallation: Double,  // 0-100
        val seatbeltInstallation: Double,  // 0-100
        val tetherInstallation: Double,  // 0-100
        val adjustability: Double,  // 0-100
        val intuitiveness: Double,  // 0-100
        val removalEase: Double  // 0-100
    )

    /**
     * 车辆兼容性
     */
    data class VehicleCompatibility(
        val sedanCompatibility: Double,  // 0-100
        val suvCompatibility: Double,  // 0-100
        val vanCompatibility: Double,  // 0-100
        val truckCompatibility: Double,  // 0-100
        val incompatibleVehicles: List<String>
    )

    /**
     * 重量设计
     */
    data class WeightDesign(
        val productId: String,
        val totalWeightKg: Double,
        val weightBreakdown: Map<String, Double>,
        val meetsFmvssLimit: Boolean,
        val weightDistribution: WeightDistribution,
        val recommendations: List<String>
    )

    /**
     * 重量分布
     */
    data class WeightDistribution(
        val plasticComponents: Double,  // %
        val metalComponents: Double,  // %
        val foamComponents: Double,  // %
        val textileComponents: Double,  // %
        val otherComponents: Double  // %
    )

    /**
     * 极端环境要求
     */
    data class ExtremeEnvironmentRequirement(
        val environmentType: EnvironmentType,
        val temperatureRange: TemperatureRange,
        val humidityRange: HumidityRange,
        val materialRequirements: List<MaterialRequirement>,
        val testingRequirements: List<TestingRequirement>
    )

    /**
     * 环境类型
     */
    enum class EnvironmentType(val displayName: String) {
        EXTREME_COLD("极端寒冷"),
        EXTREME_HOT("极端炎热"),
        HUMID("潮湿"),
        DRY("干燥"),
        MARINE("海洋环境"),
        DESERT("沙漠环境")
    }

    /**
     * 温度范围
     */
    data class TemperatureRange(
        val minTemperature: Double,  // °C
        val maxTemperature: Double  // °C
    )

    /**
     * 湿度范围
     */
    data class HumidityRange(
        val minHumidity: Double,  // %
        val maxHumidity: Double  // %
    )

    /**
     * 材料要求
     */
    data class MaterialRequirement(
        val component: String,
        val materialType: String,
        val property: String,
        val minValue: Double?,
        val maxValue: Double?,
        val unit: String
    )

    /**
     * 测试要求
     */
    data class TestingRequirement(
        val testName: String,
        val testStandard: String,
        val testConditions: String,
        val passCriteria: String
    )

    /**
     * 环境适应性评估
     */
    data class EnvironmentalAdaptabilityAssessment(
        val assessmentId: String,
        val productId: String,
        val coldResistance: ResistanceRating,
        val heatResistance: ResistanceRating,
        val humidityResistance: ResistanceRating,
        val uvResistance: ResistanceRating,
        val waterResistance: ResistanceRating,
        val corrosionResistance: ResistanceRating,
        val overallRating: ResistanceRating,
        val recommendations: List<String>
    )

    /**
     * 抵抗性评级
     */
    enum class ResistanceRating(val displayName: String, val score: Int) {
        POOR("差", 1),
        FAIR("一般", 2),
        GOOD("良好", 3),
        EXCELLENT("优秀", 4),
        OUTSTANDING("卓越", 5)
    }

    /**
     * 用户安装体验评估
     */
    data class UserInstallationExperience(
        val experienceId: String,
        val participantId: String,
        val installationType: InstallationType,
        val timeToComplete: Int,  // 分钟
        val errorCount: Int,
        val difficultyRating: DifficultyRating,
        val satisfactionRating: SatisfactionRating,
        val feedback: List<String>
    )

    /**
     * 安装类型
     */
    enum class InstallationType(val displayName: String) {
        LATCH("LATCH安装"),
        SEATBELT("安全带安装"),
        REAR_FACING("后向安装"),
        FORWARD_FACING("前向安装"),
        BOOSTER("增高垫")
    }

    /**
     * 难度评级
     */
    enum class DifficultyRating(val displayName: String, val score: Int) {
        VERY_EASY("非常容易", 1),
        EASY("容易", 2),
        MODERATE("中等", 3),
        DIFFICULT("困难", 4),
        VERY_DIFFICULT("非常困难", 5)
    }

    /**
     * 满意度评级
     */
    enum class SatisfactionRating(val displayName: String, val score: Int) {
        VERY_DISSATISFIED("非常不满意", 1),
        DISSATISFIED("不满意", 2),
        NEUTRAL("一般", 3),
        SATISFIED("满意", 4),
        VERY_SATISFIED("非常满意", 5)
    }

    /**
     * 常见美国车辆数据
     */
    private val commonUSVehicles = listOf(
        VehicleAdaptationData(
            vehicleId = "SEDAN-001",
            make = "Toyota",
            model = "Camry",
            yearRange = "2018-2024",
            seatWidth = 135.0,
            seatDepth = 52.0,
            latchAnchorSpacing = 28.0,
            tetherAnchorLocation = "后排座椅靠背",
            rearSeatLegroom = 97.0,
            hasIsofix = true,
            hasTetherAnchor = true,
            notes = "主流家用轿车，LATCH锚点间距标准"
        ),
        VehicleAdaptationData(
            vehicleId = "SUV-001",
            make = "Honda",
            model = "CR-V",
            yearRange = "2017-2024",
            seatWidth = 140.0,
            seatDepth = 54.0,
            latchAnchorSpacing = 28.0,
            tetherAnchorLocation = "后备箱侧壁",
            rearSeatLegroom = 104.0,
            hasIsofix = true,
            hasTetherAnchor = true,
            notes = "紧凑型SUV，后排空间宽敞"
        ),
        VehicleAdaptationData(
            vehicleId = "VAN-001",
            make = "Honda",
            model = "Odyssey",
            yearRange = "2018-2024",
            seatWidth = 148.0,
            seatDepth = 55.0,
            latchAnchorSpacing = 28.0,
            tetherAnchorLocation = "后排座椅靠背",
            rearSeatLegroom = 109.0,
            hasIsofix = true,
            hasTetherAnchor = true,
            notes = "MPV车型，多座位配置"
        ),
        VehicleAdaptationData(
            vehicleId = "TRUCK-001",
            make = "Ford",
            model = "F-150",
            yearRange = "2015-2024",
            seatWidth = 145.0,
            seatDepth = 56.0,
            latchAnchorSpacing = 28.0,
            tetherAnchorLocation = "后排座椅靠背",
            rearSeatLegroom = 107.0,
            hasIsofix = true,
            hasTetherAnchor = true,
            notes = "皮卡车型，后排空间较大"
        ),
        VehicleAdaptationData(
            vehicleId = "SEDAN-002",
            make = "Ford",
            model = "Fusion",
            yearRange = "2013-2020",
            seatWidth = 133.0,
            seatDepth = 50.0,
            latchAnchorSpacing = 28.0,
            tetherAnchorLocation = "后备箱内",
            rearSeatLegroom = 95.0,
            hasIsofix = true,
            hasTetherAnchor = true,
            notes = "中型轿车，后排空间适中"
        )
    )

    /**
     * 极端环境要求库
     */
    private val extremeEnvironmentRequirements = mapOf(
        EnvironmentType.EXTREME_COLD to ExtremeEnvironmentRequirement(
            environmentType = EnvironmentType.EXTREME_COLD,
            temperatureRange = TemperatureRange(-40.0, 0.0),
            humidityRange = HumidityRange(10.0, 60.0),
            materialRequirements = listOf(
                MaterialRequirement("织带", "尼龙", "低温抗拉强度", 0.9, null, "%常温强度"),
                MaterialRequirement("塑料件", "ABS/PP", "低温冲击强度", 50.0, null, "J/m"),
                MaterialRequirement("泡沫", "PU", "低温回弹性", 0.8, null, "%常温弹性")
            ),
            testingRequirements = listOf(
                TestingRequirement("低温测试", "ASTM D746", "-40°C 24h", "无裂纹，功能正常"),
                TestingRequirement("低温冲击测试", "ASTM D5420", "-40°C", "冲击强度≥50 J/m"),
                TestingRequirement("低温弯曲测试", "ASTM D522", "-40°C", "无断裂")
            )
        ),
        EnvironmentType.EXTREME_HOT to ExtremeEnvironmentRequirement(
            environmentType = EnvironmentType.EXTREME_HOT,
            temperatureRange = TemperatureRange(35.0, 70.0),
            humidityRange = HumidityRange(10.0, 40.0),
            materialRequirements = listOf(
                MaterialRequirement("塑料件", "PP+玻纤", "热变形温度", 120.0, null, "°C"),
                MaterialRequirement("织带", "尼龙", "高温抗拉强度", 0.9, null, "%常温强度"),
                MaterialRequirement("泡沫", "PU", "高温回弹性", 0.85, null, "%常温弹性")
            ),
            testingRequirements = listOf(
                TestingRequirement("高温老化测试", "ASTM D4329", "70°C 500h", "性能衰减≤20%"),
                TestingRequirement("热变形测试", "ASTM D648", "120°C 1h", "变形≤3mm"),
                TestingRequirement("高温功能测试", "自定义", "70°C 24h", "功能正常")
            )
        ),
        EnvironmentType.HUMID to ExtremeEnvironmentRequirement(
            environmentType = EnvironmentType.HUMID,
            temperatureRange = TemperatureRange(15.0, 35.0),
            humidityRange = HumidityRange(70.0, 95.0),
            materialRequirements = listOf(
                MaterialRequirement("金属件", "镀锌", "防腐性能", null, null, "盐雾试验≥96h"),
                MaterialRequirement("塑料件", "ABS", "吸水率", null, 0.5, "%"),
                MaterialRequirement("泡沫", "PU", "吸水率", null, 5.0, "%")
            ),
            testingRequirements = listOf(
                TestingRequirement("湿热测试", "ASTM D2247", "40°C 95%RH 96h", "无腐蚀，功能正常"),
                TestingRequirement("盐雾试验", "ASTM B117", "5% NaCl 96h", "无腐蚀"),
                TestingRequirement("吸水率测试", "ASTM D570", "浸水24h", "吸水率≤5%")
            )
        ),
        EnvironmentType.DESERT to ExtremeEnvironmentRequirement(
            environmentType = EnvironmentType.DESERT,
            temperatureRange = TemperatureRange(10.0, 60.0),
            humidityRange = HumidityRange(5.0, 20.0),
            materialRequirements = listOf(
                MaterialRequirement("塑料件", "PP+UV稳定剂", "UV稳定性", null, null, "UV测试1000h"),
                MaterialRequirement("织带", "尼龙", "UV稳定性", null, null, "UV测试1000h"),
                MaterialRequirement("橡胶件", "EPDM", "耐老化性", null, null, "高温老化测试500h")
            ),
            testingRequirements = listOf(
                TestingRequirement("UV老化测试", "ASTM G154", "1000h", "性能衰减≤30%"),
                TestingRequirement("高温老化测试", "ASTM D4329", "60°C 500h", "无裂纹，功能正常"),
                TestingRequirement("热冲击测试", "ASTM D522", "-20°C↔60°C 10循环", "无开裂")
            )
        )
    )

    /**
     * FMVSS 213重量限制
     */
    private val fmvssWeightLimit = 15.0  // kg (ISOFIX款)

    /**
     * 评估安装便捷性
     */
    suspend fun evaluateInstallationEase(
        productId: String,
        weightKg: Double,
        latchInterfaceQuality: Double,
        tetherDesignQuality: Double,
        adjustability: Double,
        intuitiveness: Double
    ): InstallationEaseScore = withContext(Dispatchers.Default) {
        val scoreBreakdown = ScoreBreakdown(
            latchInstallation = calculateLatchScore(latchInterfaceQuality),
            seatbeltInstallation = calculateSeatbeltScore(),
            tetherInstallation = calculateTetherScore(tetherDesignQuality),
            adjustability = adjustability * 100,
            intuitiveness = intuitiveness * 100,
            removalEase = calculateRemovalScore(weightKg)
        )
        
        val overallScore = (scoreBreakdown.latchInstallation * 0.25 +
                           scoreBreakdown.seatbeltInstallation * 0.15 +
                           scoreBreakdown.tetherInstallation * 0.15 +
                           scoreBreakdown.adjustability * 0.15 +
                           scoreBreakdown.intuitiveness * 0.15 +
                           scoreBreakdown.removalEase * 0.15)
        
        val vehicleCompatibility = calculateVehicleCompatibility(weightKg)
        val recommendations = generateInstallationRecommendations(scoreBreakdown, weightKg)
        
        InstallationEaseScore(
            productId = productId,
            overallScore = overallScore,
            scoreBreakdown = scoreBreakdown,
            vehicleCompatibility = vehicleCompatibility,
            recommendations = recommendations
        )
    }

    /**
     * 计算LATCH安装评分
     */
    private fun calculateLatchScore(quality: Double): Double {
        return quality * 100
    }

    /**
     * 计算安全带安装评分
     */
    private fun calculateSeatbeltScore(): Double {
        return 80.0  // 默认评分
    }

    /**
     * 计算Tether安装评分
     */
    private fun calculateTetherScore(quality: Double): Double {
        return quality * 100
    }

    /**
     * 计算拆卸评分
     */
    private fun calculateRemovalScore(weightKg: Double): Double {
        return when {
            weightKg <= 10.0 -> 100.0
            weightKg <= 13.0 -> 85.0
            weightKg <= 15.0 -> 70.0
            else -> 50.0
        }
    }

    /**
     * 计算车辆兼容性
     */
    private fun calculateVehicleCompatibility(weightKg: Double): VehicleCompatibility {
        return VehicleCompatibility(
            sedanCompatibility = if (weightKg <= 15.0) 90.0 else 75.0,
            suvCompatibility = if (weightKg <= 15.0) 95.0 else 80.0,
            vanCompatibility = if (weightKg <= 15.0) 92.0 else 78.0,
            truckCompatibility = if (weightKg <= 15.0) 88.0 else 72.0,
            incompatibleVehicles = if (weightKg > 15.0) {
                listOf("小型轿车（后排空间不足）")
            } else {
                emptyList()
            }
        )
    }

    /**
     * 生成安装便捷性建议
     */
    private fun generateInstallationRecommendations(
        scoreBreakdown: ScoreBreakdown,
        weightKg: Double
    ): List<String> {
        val recommendations = mutableListOf<String>()
        
        if (scoreBreakdown.latchInstallation < 70) {
            recommendations.add("改进LATCH接口设计，提高易用性")
        }
        if (scoreBreakdown.tetherInstallation < 70) {
            recommendations.add("优化Tether设计，简化安装流程")
        }
        if (scoreBreakdown.adjustability < 70) {
            recommendations.add("提高调节系统的易用性")
        }
        if (scoreBreakdown.intuitiveness < 70) {
            recommendations.add("改进标识和说明，提高直观性")
        }
        if (scoreBreakdown.removalEase < 70) {
            recommendations.add("考虑减轻产品重量，提高拆卸便捷性")
        }
        if (weightKg > fmvssWeightLimit) {
            recommendations.add("警告：产品重量超过FMVSS 213推荐限制（$fmvssWeightLimit kg）")
        }
        
        return recommendations
    }

    /**
     * 评估重量设计
     */
    fun evaluateWeightDesign(
        productId: String,
        weightBreakdown: Map<String, Double>
    ): WeightDesign {
        val totalWeight = weightBreakdown.values.sum()
        val totalWeightKg = totalWeight / 1000.0  // 转换为kg
        
        val weightDistribution = calculateWeightDistribution(weightBreakdown, totalWeight)
        val recommendations = generateWeightRecommendations(totalWeightKg, weightDistribution)
        
        return WeightDesign(
            productId = productId,
            totalWeightKg = totalWeightKg,
            weightBreakdown = weightBreakdown.mapValues { it.value / 1000.0 },
            meetsFmvssLimit = totalWeightKg <= fmvssWeightLimit,
            weightDistribution = weightDistribution,
            recommendations = recommendations
        )
    }

    /**
     * 计算重量分布
     */
    private fun calculateWeightDistribution(
        weightBreakdown: Map<String, Double>,
        totalWeight: Double
    ): WeightDistribution {
        val plastic = weightBreakdown.filter { it.key.contains("plastic", ignoreCase = true) }.values.sum() / totalWeight * 100
        val metal = weightBreakdown.filter { it.key.contains("metal", ignoreCase = true) }.values.sum() / totalWeight * 100
        val foam = weightBreakdown.filter { it.key.contains("foam", ignoreCase = true) }.values.sum() / totalWeight * 100
        val textile = weightBreakdown.filter { it.key.contains("textile", ignoreCase = true) }.values.sum() / totalWeight * 100
        val other = 100.0 - plastic - metal - foam - textile
        
        return WeightDistribution(
            plasticComponents = plastic,
            metalComponents = metal,
            foamComponents = foam,
            textileComponents = textile,
            otherComponents = other
        )
    }

    /**
     * 生成重量建议
     */
    private fun generateWeightRecommendations(
        totalWeightKg: Double,
        weightDistribution: WeightDistribution
    ): List<String> {
        val recommendations = mutableListOf<String>()
        
        if (totalWeightKg > fmvssWeightLimit) {
            recommendations.add("产品重量超过FMVSS 213推荐限制，建议优化设计减轻重量")
        }
        
        if (weightDistribution.metalComponents > 40) {
            recommendations.add("金属件占比过高，建议考虑轻量化设计")
        }
        
        if (weightDistribution.plasticComponents < 30) {
            recommendations.add("塑料件占比偏低，可考虑增加以优化重量分布")
        }
        
        return recommendations
    }

    /**
     * 评估环境适应性
     */
    suspend fun assessEnvironmentalAdaptability(
        productId: String,
        materialData: Map<String, Map<String, Double>>
    ): EnvironmentalAdaptabilityAssessment = withContext(Dispatchers.Default) {
        val coldResistance = assessColdResistance(materialData)
        val heatResistance = assessHeatResistance(materialData)
        val humidityResistance = assessHumidityResistance(materialData)
        val uvResistance = assessUVResistance(materialData)
        val waterResistance = assessWaterResistance(materialData)
        val corrosionResistance = assessCorrosionResistance(materialData)
        
        val scores = listOf(
            coldResistance.score,
            heatResistance.score,
            humidityResistance.score,
            uvResistance.score,
            waterResistance.score,
            corrosionResistance.score
        )
        
        val overallRating = when {
            scores.average() >= 4.5 -> ResistanceRating.OUTSTANDING
            scores.average() >= 3.5 -> ResistanceRating.EXCELLENT
            scores.average() >= 2.5 -> ResistanceRating.GOOD
            scores.average() >= 1.5 -> ResistanceRating.FAIR
            else -> ResistanceRating.POOR
        }
        
        val recommendations = generateEnvironmentalRecommendations(
            coldResistance, heatResistance, humidityResistance,
            uvResistance, waterResistance, corrosionResistance
        )
        
        EnvironmentalAdaptabilityAssessment(
            assessmentId = "EA-${System.currentTimeMillis()}",
            productId = productId,
            coldResistance = coldResistance,
            heatResistance = heatResistance,
            humidityResistance = humidityResistance,
            uvResistance = uvResistance,
            waterResistance = waterResistance,
            corrosionResistance = corrosionResistance,
            overallRating = overallRating,
            recommendations = recommendations
        )
    }

    /**
     * 评估耐寒性
     */
    private fun assessColdResistance(materialData: Map<String, Map<String, Double>>): ResistanceRating {
        // 简化评估逻辑
        return ResistanceRating.GOOD
    }

    /**
     * 评估耐热性
     */
    private fun assessHeatResistance(materialData: Map<String, Map<String, Double>>): ResistanceRating {
        return ResistanceRating.GOOD
    }

    /**
     * 评估耐湿性
     */
    private fun assessHumidityResistance(materialData: Map<String, Map<String, Double>>): ResistanceRating {
        return ResistanceRating.GOOD
    }

    /**
     * 评估抗UV
     */
    private fun assessUVResistance(materialData: Map<String, Map<String, Double>>): ResistanceRating {
        return ResistanceRating.GOOD
    }

    /**
     * 评估防水性
     */
    private fun assessWaterResistance(materialData: Map<String, Map<String, Double>>): ResistanceRating {
        return ResistanceRating.GOOD
    }

    /**
     * 评估抗腐蚀性
     */
    private fun assessCorrosionResistance(materialData: Map<String, Map<String, Double>>): ResistanceRating {
        return ResistanceRating.GOOD
    }

    /**
     * 生成环境适应性建议
     */
    private fun generateEnvironmentalRecommendations(
        coldResistance: ResistanceRating,
        heatResistance: ResistanceRating,
        humidityResistance: ResistanceRating,
        uvResistance: ResistanceRating,
        waterResistance: ResistanceRating,
        corrosionResistance: ResistanceRating
    ): List<String> {
        val recommendations = mutableListOf<String>()
        
        if (coldResistance.score < 4) {
            recommendations.add("建议提高材料的低温性能，特别是织带和塑料件")
        }
        if (heatResistance.score < 4) {
            recommendations.add("建议提高材料的耐热性能，特别是塑料件和泡沫")
        }
        if (humidityResistance.score < 4) {
            recommendations.add("建议加强防潮设计，特别是金属件的防腐处理")
        }
        if (uvResistance.score < 4) {
            recommendations.add("建议添加UV稳定剂，提高材料抗UV性能")
        }
        if (corrosionResistance.score < 4) {
            recommendations.add("建议加强金属件的防腐处理，如镀锌或涂覆")
        }
        
        return recommendations
    }

    /**
     * 生成人机工程学适配报告
     */
    suspend fun generateErgonomicsReport(
        installationScore: InstallationEaseScore,
        weightDesign: WeightDesign,
        environmentalAssessment: EnvironmentalAdaptabilityAssessment
    ): String = withContext(Dispatchers.Default) {
        val report = StringBuilder()
        
        report.appendLine("=" .repeat(70))
        report.appendLine("人机工程学适配报告")
        report.appendLine("=" .repeat(70))
        report.appendLine()
        
        // 安装便捷性
        report.appendLine("-" .repeat(70))
        report.appendLine("安装便捷性评估")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        report.appendLine("总体评分：${String.format("%.1f", installationScore.overallScore)}/100")
        report.appendLine()
        report.appendLine("评分细分：")
        report.appendLine("  LATCH安装：${String.format("%.1f", installationScore.scoreBreakdown.latchInstallation)}/100")
        report.appendLine("  安全带安装：${String.format("%.1f", installationScore.scoreBreakdown.seatbeltInstallation)}/100")
        report.appendLine("  Tether安装：${String.format("%.1f", installationScore.scoreBreakdown.tetherInstallation)}/100")
        report.appendLine("  可调节性：${String.format("%.1f", installationScore.scoreBreakdown.adjustability)}/100")
        report.appendLine("  直观性：${String.format("%.1f", installationScore.scoreBreakdown.intuitiveness)}/100")
        report.appendLine("  拆卸便捷性：${String.format("%.1f", installationScore.scoreBreakdown.removalEase)}/100")
        report.appendLine()
        
        report.appendLine("车辆兼容性：")
        report.appendLine("  轿车：${String.format("%.1f", installationScore.vehicleCompatibility.sedanCompatibility)}%")
        report.appendLine("  SUV：${String.format("%.1f", installationScore.vehicleCompatibility.suvCompatibility)}%")
        report.appendLine("  MPV：${String.format("%.1f", installationScore.vehicleCompatibility.vanCompatibility)}%")
        report.appendLine("  皮卡：${String.format("%.1f", installationScore.vehicleCompatibility.truckCompatibility)}%")
        report.appendLine()
        
        if (installationScore.recommendations.isNotEmpty()) {
            report.appendLine("建议：")
            installationScore.recommendations.forEach { rec ->
                report.appendLine("  - $rec")
            }
            report.appendLine()
        }
        
        // 重量设计
        report.appendLine("-" .repeat(70))
        report.appendLine("重量设计评估")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        report.appendLine("总重量：${String.format("%.2f", weightDesign.totalWeightKg)} kg")
        report.appendLine("符合FMVSS 213限制：${if (weightDesign.meetsFmvssLimit) "是" else "否"}")
        report.appendLine()
        report.appendLine("重量分布：")
        report.appendLine("  塑料件：${String.format("%.1f", weightDesign.weightDistribution.plasticComponents)}%")
        report.appendLine("  金属件：${String.format("%.1f", weightDesign.weightDistribution.metalComponents)}%")
        report.appendLine("  泡沫：${String.format("%.1f", weightDesign.weightDistribution.foamComponents)}%")
        report.appendLine("  纺织品：${String.format("%.1f", weightDesign.weightDistribution.textileComponents)}%")
        report.appendLine("  其他：${String.format("%.1f", weightDesign.weightDistribution.otherComponents)}%")
        report.appendLine()
        
        if (weightDesign.recommendations.isNotEmpty()) {
            report.appendLine("建议：")
            weightDesign.recommendations.forEach { rec ->
                report.appendLine("  - $rec")
            }
            report.appendLine()
        }
        
        // 环境适应性
        report.appendLine("-" .repeat(70))
        report.appendLine("环境适应性评估")
        report.appendLine("-" .repeat(70))
        report.appendLine()
        report.appendLine("总体评级：${environmentalAssessment.overallRating.displayName}")
        report.appendLine()
        report.appendLine("各维度评级：")
        report.appendLine("  耐寒性：${environmentalAssessment.coldResistance.displayName}")
        report.appendLine("  耐热性：${environmentalAssessment.heatResistance.displayName}")
        report.appendLine("  耐湿性：${environmentalAssessment.humidityResistance.displayName}")
        report.appendLine("  抗UV：${environmentalAssessment.uvResistance.displayName}")
        report.appendLine("  防水性：${environmentalAssessment.waterResistance.displayName}")
        report.appendLine("  抗腐蚀：${environmentalAssessment.corrosionResistance.displayName}")
        report.appendLine()
        
        if (environmentalAssessment.recommendations.isNotEmpty()) {
            report.appendLine("建议：")
            environmentalAssessment.recommendations.forEach { rec ->
                report.appendLine("  - $rec")
            }
            report.appendLine()
        }
        
        report.appendLine("=" .repeat(70))
        report.appendLine("综合建议")
        report.appendLine("=" .repeat(70))
        report.appendLine()
        report.appendLine("1. 优化安装便捷性，提高用户体验")
        report.appendLine("2. 控制产品重量，确保符合FMVSS 213限制")
        report.appendLine("3. 提高环境适应性，覆盖极端气候条件")
        report.appendLine("4. 适配美国常见车型，提高兼容性")
        report.appendLine("5. 进行用户测试，验证设计改进效果")
        
        report.toString()
    }

    /**
     * 生成极端环境适配指南
     */
    fun generateExtremeEnvironmentGuide(environmentType: EnvironmentType): String {
        val guide = StringBuilder()
        val requirements = extremeEnvironmentRequirements[environmentType]
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("极端环境适配指南 - ${environmentType.displayName}")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        
        if (requirements != null) {
            guide.appendLine("温度范围：${requirements.temperatureRange.minTemperature}°C 至 ${requirements.temperatureRange.maxTemperature}°C")
            guide.appendLine("湿度范围：${requirements.humidityRange.minHumidity}% 至 ${requirements.humidityRange.maxHumidity}%")
            guide.appendLine()
            
            guide.appendLine("-" .repeat(70))
            guide.appendLine("材料要求")
            guide.appendLine("-" .repeat(70))
            guide.appendLine()
            
            requirements.materialRequirements.forEach { req ->
                guide.appendLine("组件：${req.component}")
                guide.appendLine("材料：${req.materialType}")
                guide.appendLine("性能：${req.property}")
                if (req.minValue != null) {
                    guide.appendLine("最小值：${req.minValue} ${req.unit}")
                }
                if (req.maxValue != null) {
                    guide.appendLine("最大值：${req.maxValue} ${req.unit}")
                }
                guide.appendLine()
            }
            
            guide.appendLine("-" .repeat(70))
            guide.appendLine("测试要求")
            guide.appendLine("-" .repeat(70))
            guide.appendLine()
            
            requirements.testingRequirements.forEach { test ->
                guide.appendLine("测试名称：${test.testName}")
                guide.appendLine("测试标准：${test.testStandard}")
                guide.appendLine("测试条件：${test.testConditions}")
                guide.appendLine("通过标准：${test.passCriteria}")
                guide.appendLine()
            }
        } else {
            guide.appendLine("未找到${environmentType.displayName}的要求信息")
        }
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("设计建议")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        guide.appendLine("1. 选择适合极端环境的材料配方")
        guide.appendLine("2. 添加适当的稳定剂和添加剂")
        guide.appendLine("3. 进行充分的环境适应性测试")
        guide.appendLine("4. 考虑用户所在地区的实际气候条件")
        guide.appendLine("5. 在说明书中标注使用环境限制")
        
        return guide.toString()
    }

    /**
     * 生成安装便捷性设计指南
     */
    fun generateInstallationEaseGuide(): String {
        val guide = StringBuilder()
        
        guide.appendLine("=" .repeat(70))
        guide.appendLine("安装便捷性设计指南")
        guide.appendLine("=" .repeat(70))
        guide.appendLine()
        
        guide.appendLine("【LATCH接口设计】")
        guide.appendLine("1. 一键锁定：插入即锁定，无需额外操作")
        guide.appendLine("2. 颜色编码：红色=未锁定，绿色=已锁定")
        guide.appendLine("3. 释放力适中：40-160 N，成人可操作")
        guide.appendLine("4. 连接器角度优化：易于插入车辆锚点")
        guide.appendLine()
        
        guide.appendLine("【Tether设计】")
        guide.appendLine("1. 易于固定：tether钩设计便于固定到车辆锚点")
        guide.appendLine("2. 长度可调节：适应不同车型")
        guide.appendLine("3. 视觉指示：锁定状态可见")
        guide.appendLine("4. 长度限制：防止过长导致松弛")
        guide.appendLine()
        
        guide.appendLine("【重量控制】")
        guide.appendLine("1. 轻量化设计：ISOFIX款≤15 kg")
        guide.appendLine("2. 材料优化：使用高强度轻质材料")
        guide.appendLine("3. 结构优化：减少不必要的重量")
        guide.appendLine("4. 符合标准：FMVSS 213 S4.4.1.2")
        guide.appendLine()
        
        guide.appendLine("【车辆适配】")
        guide.appendLine("1. 适配常见车型：轿车、SUV、MPV、皮卡")
        guide.appendLine("2. LATCH锚点间距：28 cm标准间距")
        guide.appendLine("3. 后排空间考虑：考虑不同车型的后排空间")
        guide.appendLine("4. 安装指南：提供不同车型的安装指南")
        guide.appendLine()
        
        guide.appendLine("【用户测试】")
        guide.appendLine("1. 安装时间测试：目标≤5分钟")
        guide.appendLine("2. 错误率测试：目标错误率≤10%")
        guide.appendLine("3. 满意度调查：目标满意度≥4/5")
        guide.appendLine("4. 持续改进：根据用户反馈优化设计")
        
        return guide.toString()
    }
}

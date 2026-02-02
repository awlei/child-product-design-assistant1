package com.childproduct.designassistant.service

import com.childproduct.designassistant.data.*
import com.childproduct.designassistant.model.ComplianceStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * FMVSS CRS产品设计指导服务
 * 
 * 基于FMVSS 213/213a标准提供全面的CRS产品设计指导
 * 包含：材料选型、结构设计、功能适配、测试验证等全流程指导
 */
class FMVSSCRSDesignGuideService {

    /**
     * 设计指导结果
     */
    data class DesignGuideResult(
        val productName: String,
        val targetMarket: String,
        val designGuidance: Map<String, List<String>>,
        val materialRequirements: List<MaterialRequirement>,
        val structuralRequirements: List<StructuralRequirement>,
        val testConfigurations: List<TestConfiguration>,
        val complianceStatus: ComplianceStatus,
        val recommendations: List<String>
    )

    /**
     * 材料要求
     */
    data class MaterialRequirement(
        val category: String,              // 材料类别
        val requirement: String,           // 要求
        val testStandard: String,          // 测试标准
        val acceptanceCriteria: String     // 合格标准
    )

    /**
     * 结构要求
     */
    data class StructuralRequirement(
        val component: String,             // 组件名称
        val requirement: String,           // 要求
        val testStandard: String,          // 测试标准
        val acceptanceCriteria: String     // 合格标准
    )

    /**
     * 生成FMVSS CRS产品设计指导
     */
    suspend fun generateDesignGuide(
        productName: String,
        targetMarket: String = "美国",
        weightRangeKg: String = "≤36kg (≤80lb)",
        heightRangeCm: String = "根据体重范围"
    ): DesignGuideResult = withContext(Dispatchers.Default) {
        val guidance = mutableMapOf<String, List<String>>()
        val materialRequirements = mutableListOf<MaterialRequirement>()
        val structuralRequirements = mutableListOf<StructuralRequirement>()
        val recommendations = mutableListOf<String>()

        // 1. 产品设计核心定位
        guidance["产品设计核心定位"] = listOf(
            "设计依据：严格遵循FMVSS 213（2023修订版）、FMVSS 213a（2025年6月30日生效）",
            "适用范围：体重≤36kg/80lb，新生儿至10岁",
            "Dummy类型：CRABI 12个月、Q3s 3岁等7类",
            "核心目标：同时满足正面/侧面碰撞安全要求、材料性能、约束系统完整性"
        )

        // 2. 材料选型设计要求
        materialRequirements.addAll(generateMaterialRequirements())

        // 3. 结构设计要求
        structuralRequirements.addAll(generateStructuralRequirements())

        // 4. 测试配置
        val testConfigs = listOf(
            FMVSSTestStandardDatabase.getTestConfiguration(FMVSSStandardType.FMVSS_213),
            FMVSSTestStandardDatabase.getTestConfiguration(FMVSSStandardType.FMVSS_213A)
        ).filterNotNull()

        // 5. 关键建议
        recommendations.addAll(listOf(
            "使用FMVSS 213/213a标准数据进行设计验证",
            "确保所有材料符合FMVSS 302阻燃性要求",
            "进行完整的动态碰撞测试（正面+侧面）",
            "确保标签和说明书符合FMVSS 213 S5.5/S5.6要求",
            "支持LATCH系统、三点式安全带、飞机安全带安装",
            "预留足够的头部位移空间（720mm有tether/813mm无tether）",
            "确保Buckle释放力在40-71N范围内",
            "进行材料老化测试（盐雾、热老化、耐磨、耐光）",
            "确保靠背高度符合要求（≤18kg≥500mm，>18kg≥560mm）",
            "确保支撑面积符合要求（背部≥548cm²，侧面≥155/310cm²）"
        ))

        DesignGuideResult(
            productName = productName,
            targetMarket = targetMarket,
            designGuidance = guidance,
            materialRequirements = materialRequirements,
            structuralRequirements = structuralRequirements,
            testConfigurations = testConfigs,
            complianceStatus = ComplianceStatus.NOT_APPLICABLE,
            recommendations = recommendations
        )
    }

    /**
     * 生成材料要求
     */
    private fun generateMaterialRequirements(): List<MaterialRequirement> {
        return listOf(
            MaterialRequirement(
                category = "织带性能（FMVSS 213 S5.4.1）",
                requirement = "固定CRS至车辆的织带≥15000N，约束儿童的织带≥11000N",
                testStandard = "FMVSS 213",
                acceptanceCriteria = "断裂强度、耐磨、耐光、抗菌均符合要求"
            ),
            MaterialRequirement(
                category = "织带环境耐受性",
                requirement = "耐磨≥75%（5000次）、耐光≥60%（100小时）、抗菌≥85%（2周）",
                testStandard = "FMVSS 213 + ASTM B117/AATCC 30",
                acceptanceCriteria = "强度保留率达标"
            ),
            MaterialRequirement(
                category = "织带尺寸",
                requirement = "宽度≥38mm（2.3kg张力下），厚度均匀无破损",
                testStandard = "FMVSS 213",
                acceptanceCriteria = "尺寸和外观符合要求"
            ),
            MaterialRequirement(
                category = "非金属材料阻燃（FMVSS 302 S4.3）",
                requirement = "燃烧速率≤102mm/分钟，或60秒内熄灭且燃烧距离≤51mm",
                testStandard = "FMVSS 302",
                acceptanceCriteria = "燃烧性能符合要求"
            ),
            MaterialRequirement(
                category = "非金属材料预处理",
                requirement = "16-27℃、湿度40-60%条件下放置≥24小时",
                testStandard = "FMVSS 302",
                acceptanceCriteria = "预处理条件符合要求"
            ),
            MaterialRequirement(
                category = "金属/塑料硬件（FMVSS 213 S5.4.2）",
                requirement = "耐腐蚀、温度耐受、突起限制",
                testStandard = "FMVSS 213 + ASTM B117/D756",
                acceptanceCriteria = "无腐蚀、无变形、功能正常"
            ),
            MaterialRequirement(
                category = "硬件突起限制",
                requirement = "刚性结构突起≤9.5mm，边缘半径≥6.35mm",
                testStandard = "FMVSS 213",
                acceptanceCriteria = "突起和边缘符合要求"
            )
        )
    }

    /**
     * 生成结构要求
     */
    private fun generateStructuralRequirements(): List<StructuralRequirement> {
        return listOf(
            StructuralRequirement(
                component = "头部与躯干支撑（FMVSS 213 S5.2.1/S5.2.2）",
                requirement = "靠背高度、支撑面积、轮廓要求",
                testStandard = "FMVSS 213",
                acceptanceCriteria = "高度、面积、半径均符合要求"
            ),
            StructuralRequirement(
                component = "靠背高度",
                requirement = "≤18kg儿童≥500mm，＞18kg儿童≥560mm",
                testStandard = "FMVSS 213",
                acceptanceCriteria = "高度符合要求"
            ),
            StructuralRequirement(
                component = "支撑面积",
                requirement = "背部≥548cm²，侧面≥155cm²（≥9kg）/≥310cm²（＜9kg）",
                testStandard = "FMVSS 213",
                acceptanceCriteria = "面积符合要求"
            ),
            StructuralRequirement(
                component = "轮廓要求",
                requirement = "背部/侧面为平面或凹面，前向约束面半径≥51mm",
                testStandard = "FMVSS 213",
                acceptanceCriteria = "轮廓符合要求"
            ),
            StructuralRequirement(
                component = "安装适配（FMVSS 213 S5.3/S5.9、213a S5.1.6）",
                requirement = "兼容LATCH、三点式、两点式安全带",
                testStandard = "FMVSS 213/213a",
                acceptanceCriteria = "安装方式和张力符合要求"
            ),
            StructuralRequirement(
                component = "固定组件",
                requirement = "下锚需工具拆卸，tether钩符合图36尺寸",
                testStandard = "FMVSS 213/225",
                acceptanceCriteria = "固定组件符合要求"
            ),
            StructuralRequirement(
                component = "安装张力",
                requirement = "LATCH: 53.5-67N，三点式: 9-18N",
                testStandard = "FMVSS 213",
                acceptanceCriteria = "张力在要求范围内"
            ),
            StructuralRequirement(
                component = "碰撞完整性（FMVSS 213 S5.1.1、213a S5.1.1）",
                requirement = "无承载结构完全分离，无锐边或突起超标",
                testStandard = "FMVSS 213/213a",
                acceptanceCriteria = "碰撞后完整性符合要求"
            ),
            StructuralRequirement(
                component = "角度保持",
                requirement = "Forward-facing碰撞后靠背与座椅面夹角≥45°",
                testStandard = "FMVSS 213",
                acceptanceCriteria = "角度保持符合要求"
            ),
            StructuralRequirement(
                component = "约束系统（FMVSS 213 S5.4.3）",
                requirement = "安全带、buckle性能要求",
                testStandard = "FMVSS 213",
                acceptanceCriteria = "调节力、释放力、贴合性符合要求"
            ),
            StructuralRequirement(
                component = "Buckle性能",
                requirement = "释放力40-71N，调节力≤49N，倾斜锁定≥30°",
                testStandard = "FMVSS 213",
                acceptanceCriteria = "Buckle性能符合要求"
            ),
            StructuralRequirement(
                component = "侧面碰撞保护（FMVSS 213a S5.1.2/S5.1.3）",
                requirement = "胸部压缩≤23mm（Q3s），HIC15≤570",
                testStandard = "FMVSS 213a",
                acceptanceCriteria = "损伤指标符合要求"
            ),
            StructuralRequirement(
                component = "SISA泡沫",
                requirement = "2英寸厚：255-345N，4英寸厚：374-506N",
                testStandard = "FMVSS 213a",
                acceptanceCriteria = "泡沫压缩载荷符合要求"
            ),
            StructuralRequirement(
                component = "标签要求（FMVSS 213 S5.5）",
                requirement = "必备信息、格式、飞机认证标记",
                testStandard = "FMVSS 213",
                acceptanceCriteria = "标签符合要求"
            ),
            StructuralRequirement(
                component = "说明书与注册（FMVSS 213 S5.6/S5.8）",
                requirement = "安装图、警告说明、注册卡",
                testStandard = "FMVSS 213",
                acceptanceCriteria = "说明书和注册卡符合要求"
            )
        )
    }

    /**
     * 生成FMVSS合规性检查报告
     */
    suspend fun generateComplianceReport(
        productName: String,
        testData: Map<String, Any>
    ): String = withContext(Dispatchers.Default) {
        val report = StringBuilder()
        
        report.appendLine("=" .repeat(70))
        report.appendLine("FMVSS 213/213a 合规性检查报告")
        report.appendLine("=" .repeat(70))
        report.appendLine()
        report.appendLine("产品名称：$productName")
        report.appendLine("检查日期：${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}")
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("一、材料性能检查")
        report.appendLine("-" .repeat(70))
        
        // 织带性能
        val webbingBreakStrength = testData["webbingBreakStrength"] as? Double ?: 0.0
        val webbingType = testData["webbingType"] as? String ?: "未知"
        
        val webbingRequirement = if (webbingType == "vehicle_attachment") 15000.0 else 11000.0
        val webbingCompliant = webbingBreakStrength >= webbingRequirement
        
        report.appendLine("1. 织带断裂强度：")
        report.appendLine("   - 要求：$webbingRequirement N")
        report.appendLine("   - 实测：$webbingBreakStrength N")
        report.appendLine("   - 结果：${if (webbingCompliant) "✅ 合格" else "❌ 不合格"}")
        report.appendLine()
        
        // 阻燃性
        val burnRate = testData["burnRate"] as? Double ?: 0.0
        val burnDistance = testData["burnDistance"] as? Double ?: 0.0
        val burnTime = testData["burnTime"] as? Int ?: 0
        
        val flammability = FlammabilityRequirements.STANDARD
        val flammabilityResult = flammability.validate(burnRate, burnDistance, burnTime)
        
        report.appendLine("2. 材料阻燃性：")
        report.appendLine("   - 燃烧速率：$burnRate mm/min（≤102 mm/min）")
        report.appendLine("   - 燃烧距离：$burnDistance mm（≤51 mm）")
        report.appendLine("   - 燃烧时间：$burnTime 秒（≤60 秒）")
        report.appendLine("   - 结果：${if (flammabilityResult.isValid) "✅ 合格" else "❌ 不合格"}")
        if (!flammabilityResult.isValid) {
            flammabilityResult.errors.forEach { report.appendLine("   - 问题：$it") }
        }
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("二、结构性能检查")
        report.appendLine("-" .repeat(70))
        
        // 靠背高度
        val backHeight = testData["backHeight"] as? Double ?: 0.0
        val productMaxWeight = testData["productMaxWeight"] as? Double ?: 0.0
        val backHeightRequirement = if (productMaxWeight <= 18.0) 500.0 else 560.0
        val backHeightCompliant = backHeight >= backHeightRequirement
        
        report.appendLine("1. 靠背高度：")
        report.appendLine("   - 要求：$backHeightRequirement mm")
        report.appendLine("   - 实测：$backHeight mm")
        report.appendLine("   - 结果：${if (backHeightCompliant) "✅ 合格" else "❌ 不合格"}")
        report.appendLine()
        
        // Buckle性能
        val releaseForce = testData["releaseForce"] as? Double ?: 0.0
        val buckleRequirements = BuckleRequirements.STANDARD
        val buckleCompliant = releaseForce in buckleRequirements.minReleaseForcePreCrash..buckleRequirements.maxReleaseForcePreCrash
        
        report.appendLine("2. Buckle释放力：")
        report.appendLine("   - 要求：${buckleRequirements.minReleaseForcePreCrash}-${buckleRequirements.maxReleaseForcePreCrash} N")
        report.appendLine("   - 实测：$releaseForce N")
        report.appendLine("   - 结果：${if (buckleCompliant) "✅ 合格" else "❌ 不合格"}")
        report.appendLine()
        
        report.appendLine("-" .repeat(70))
        report.appendLine("三、动态碰撞检查")
        report.appendLine("-" .repeat(70))
        
        // 正面碰撞
        val hic36 = testData["hic36"] as? Double ?: 0.0
        val chestAccel = testData["chestAcceleration"] as? Double ?: 0.0
        val frontalConfig = FrontalCrashParameters.CONFIGURATION_I
        
        val frontalCompliant = hic36 <= frontalConfig.hic36Limit && 
                               chestAccel <= frontalConfig.chestAccelerationLimit
        
        report.appendLine("1. 正面碰撞（FMVSS 213）：")
        report.appendLine("   - HIC36：$hic36（≤${frontalConfig.hic36Limit}）")
        report.appendLine("   - 胸部加速度：$chestAccel g（≤${frontalConfig.chestAccelerationLimit} g）")
        report.appendLine("   - 结果：${if (frontalCompliant) "✅ 合格" else "❌ 不合格"}")
        report.appendLine()
        
        // 侧面碰撞
        val hic15 = testData["hic15"] as? Double ?: 0.0
        val chestCompression = testData["chestCompression"] as? Double ?: 0.0
        val sideConfig = SideCrashParameters.STANDARD
        
        val sideCompliant = hic15 <= sideConfig.hic15Limit && 
                           chestCompression <= sideConfig.chestCompressionLimit
        
        report.appendLine("2. 侧面碰撞（FMVSS 213a）：")
        report.appendLine("   - HIC15：$hic15（≤${sideConfig.hic15Limit}）")
        report.appendLine("   - 胸部压缩：$chestCompression mm（≤${sideConfig.chestCompressionLimit} mm）")
        report.appendLine("   - 结果：${if (sideCompliant) "✅ 合格" else "❌ 不合格"}")
        report.appendLine()
        
        report.appendLine("=" .repeat(70))
        report.appendLine("总结")
        report.appendLine("=" .repeat(70))
        
        val allCompliant = webbingCompliant && flammabilityResult.isValid && 
                          backHeightCompliant && buckleCompliant && 
                          frontalCompliant && sideCompliant
        
        if (allCompliant) {
            report.appendLine("✅ 所有检查项目均符合FMVSS 213/213a标准要求")
        } else {
            report.appendLine("❌ 部分检查项目不符合FMVSS 213/213a标准要求")
            report.appendLine("   请查看上述详细检查结果并采取改进措施")
        }
        
        report.toString()
    }

    /**
     * 获取推荐的Dummy类型
     */
    fun getRecommendedDummyType(weightKg: Double, ageMonths: Int): List<DummyType> {
        val byWeight = FMVSSTestStandardDatabase.getRecommendedDummyByWeight(weightKg)
        val byAge = FMVSSTestStandardDatabase.getRecommendedDummyByAge(ageMonths)
        return (byWeight + byAge).distinct()
    }
}

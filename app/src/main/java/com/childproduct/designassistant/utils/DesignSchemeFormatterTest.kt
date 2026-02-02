package com.childproduct.designassistant.utils

import com.childproduct.designassistant.model.*
import com.childproduct.designassistant.model.ProductType

/**
 * 设计方案格式化工具测试
 * 验证格式化输出是否符合输出模板规范（模板版本：1.0）
 */
object DesignSchemeFormatterTest {

    /**
     * 测试基础格式化输出
     */
    fun testFormatCreativeIdea(): String {
        // 创建测试数据
        val complianceParams = ComplianceParameters(
            dummyType = CrashTestDummy.toComplianceDummy(CrashTestDummy.Q1),
            hicLimit = 390,
            chestAccelerationLimit = 55,
            neckTensionLimit = 1800,
            neckCompressionLimit = 2200,
            headExcursionLimit = 550,
            kneeExcursionLimit = 650,
            chestDeflectionLimit = 52
        )

        val standardsRef = StandardsReference(
            mainStandard = "ECE R129 + GB 27887-2024 + FMVSS 213",
            keyClauses = emptyList(),
            complianceRequirements = emptyList()
        )

        val materialSpecs = MaterialSpecs(
            flameRetardantFabric = "通过FMVSS 302认证的阻燃面料",
            isoFixComponents = "高强度钢材ISOFIX连接件",
            impactAbsorber = "EPP/EPS吸能材料"
        )

        val testIdea = CreativeIdea(
            id = "test-001",
            title = "婴幼儿专用儿童安全座椅 - 柔和色彩",
            description = "专为0-3岁儿童设计的儿童安全座椅",
            ageGroup = AgeGroup.INFANT,
            productType = ProductType.SAFETY_SEAT,
            theme = "婴幼儿专用儿童安全座椅（柔和色彩）",
            features = listOf(
                "易安装性：支持ISOFIX快速连接",
                "舒适性：高回弹海绵填充靠背",
                "材质环保：食品级安全材料",
                "安全性：符合多国标安全要求"
            ),
            materials = listOf(
                "主体框架：食品级PP塑料",
                "填充材质：高回弹海绵",
                "约束部件：高强度安全带织带（断裂强度≥11000N）",
                "支撑结构：铝合金支架"
            ),
            colorPalette = listOf("#FFB6C1", "#E0FFFF"),
            safetyNotes = listOf(
                "部件尺寸＞3.5cm，避免吞咽风险",
                "无甲醛/重金属，符合食品级标准",
                "边缘圆角处理，无尖锐结构"
            ),
            complianceParameters = complianceParams,
            standardsReference = standardsRef,
            materialSpecs = materialSpecs
        )

        // 格式化输出
        val formattedText = DesignSchemeFormatter.formatCreativeIdea(testIdea)
        
        // 返回格式化文本
        return formattedText
    }

    /**
     * 测试按身高范围格式化输出（符合模板规范）
     */
    fun testFormatCreativeIdeaByHeight(): String {
        // 创建测试数据
        val complianceParams = ComplianceParameters(
            dummyType = CrashTestDummy.toComplianceDummy(CrashTestDummy.Q0),
            hicLimit = 750,
            chestAccelerationLimit = 55,
            neckTensionLimit = 1500,
            neckCompressionLimit = 2200,
            headExcursionLimit = 550,
            kneeExcursionLimit = 0,
            chestDeflectionLimit = 0
        )

        val standardsRef = StandardsReference(
            mainStandard = "ECE R129 + GB 27887-2024",
            keyClauses = emptyList(),
            complianceRequirements = emptyList()
        )

        val materialSpecs = MaterialSpecs()

        val testIdea = CreativeIdea(
            id = "test-height-001",
            title = "0-12岁儿童安全座椅",
            description = "覆盖全年龄段的儿童安全座椅",
            ageGroup = AgeGroup.TODDLER,
            productType = ProductType.SAFETY_SEAT,
            theme = "全年龄段儿童安全座椅（时尚设计）",
            features = listOf(
                "易安装性：支持ISOFIX快速连接",
                "舒适性：多档位调节，适应不同成长阶段",
                "材质环保：食品级安全材料",
                "安全性：符合多国标安全要求"
            ),
            materials = listOf(
                "主体框架：食品级PP塑料",
                "填充材质：高回弹海绵",
                "约束部件：高强度安全带织带（断裂强度≥11000N）",
                "支撑结构：铝合金支架"
            ),
            colorPalette = emptyList(),
            safetyNotes = listOf(
                "所有部件尺寸大于3.5cm，避免儿童吞咽风险",
                "使用食品级安全材料，无甲醛、无重金属残留",
                "产品边缘做圆角处理（半径≥2mm），无尖锐结构"
            ),
            complianceParameters = complianceParams,
            standardsReference = standardsRef,
            materialSpecs = materialSpecs
        )

        // 格式化输出（身高范围 40-150cm，对应 0-12岁）
        val formattedText = DesignSchemeFormatter.formatCreativeIdeaByHeight(
            minHeightCm = 40,
            maxHeightCm = 150,
            idea = testIdea
        )
        
        return formattedText
    }

    /**
     * 测试卡片数据格式化
     */
    fun testFormatToCardData(): SchemeCardData? {
        val complianceParams = ComplianceParameters(
            dummyType = CrashTestDummy.toComplianceDummy(CrashTestDummy.Q1),
            hicLimit = 390,
            chestAccelerationLimit = 55,
            neckTensionLimit = 1800,
            neckCompressionLimit = 2200,
            headExcursionLimit = 550,
            kneeExcursionLimit = 650,
            chestDeflectionLimit = 52
        )

        val standardsRef = StandardsReference(
            mainStandard = "ECE R129 + GB 27887-2024 + FMVSS 213",
            keyClauses = emptyList(),
            complianceRequirements = emptyList()
        )

        val materialSpecs = MaterialSpecs()

        val testIdea = CreativeIdea(
            id = "test-002",
            title = "测试标题",
            description = "测试描述",
            ageGroup = AgeGroup.INFANT,
            productType = ProductType.SAFETY_SEAT,
            theme = "测试主题",
            features = listOf("特性1", "特性2", "特性3", "特性4"),
            materials = listOf("材料1", "材料2", "材料3", "材料4"),
            colorPalette = emptyList(),
            safetyNotes = listOf("注意1", "注意2", "注意3"),
            complianceParameters = complianceParams,
            standardsReference = standardsRef,
            materialSpecs = materialSpecs
        )

        return DesignSchemeFormatter.formatToCardData(testIdea)
    }

    /**
     * 验证格式化输出是否符合输出模板规范
     */
    fun validateFormattedOutput(): Boolean {
        val formattedText = testFormatCreativeIdea()
        
        // 验证是否包含所有必需的模块标题
        val requiredSections = listOf(
            "【基本信息】",
            "【核心设计特点】",
            "【推荐材料】",
            "【合规参数】",
            "【安全注意事项】"
        )
        
        return requiredSections.all { section -> formattedText.contains(section) }
    }

    /**
     * 验证按身高范围格式化的输出是否符合模板规范
     */
    fun validateHeightFormattedOutput(): Boolean {
        val formattedText = testFormatCreativeIdeaByHeight()
        
        // 验证必需的模块标题
        val requiredSections = listOf(
            "【基本信息】",
            "【核心设计特点】",
            "【推荐材料】",
            "【合规参数】",
            "【安全注意事项】"
        )
        
        // 验证特定格式要求
        val hasAgeRangeFormat = formattedText.contains("（40-150cm）")
        val hasDummyInfo = formattedText.contains("适配假人：")
        val hasSubThresholds = formattedText.contains("  - HIC极限值：")
        
        return requiredSections.all { section -> formattedText.contains(section) } &&
               hasAgeRangeFormat &&
               hasDummyInfo &&
               hasSubThresholds
    }

    /**
     * 获取期望的示例输出（用于对比）
     */
    fun getExpectedExampleOutput(): String {
        return """【基本信息】
- 产品类型：儿童安全座椅
- 适用年龄段：0-12岁（40-150cm）
- 设计主题：婴幼儿专用儿童安全座椅（柔和色彩）

【核心设计特点】
- 易安装性：支持ISOFIX连接，实现快速安装
- 舒适性：采用高回弹海绵填充，贴合儿童体型
- 材质环保：选用食品级安全材料，无甲醛/重金属
- 安全性：符合UN R129 i-Size+GB 27887-2024安全要求

【推荐材料】
- 主体框架：食品级PP塑料
- 填充材质：高回弹海绵
- 约束部件：高强度安全带织带（断裂强度≥11000N）
- 支撑结构：铝合金支架

【合规参数】
- 对应标准：UN R129 i-Size + GB 27887-2024 + FMVSS 213
- 适配假人：Q0-Q10（0-12岁，40-150cm）
- 安全阈值：
  - HIC极限值：≤1000
  - 胸部加速度：≤55g
  - 颈部张力：≤1800N
  - 头部位移：≤550mm

【安全注意事项】
- 所有部件尺寸大于3.5cm，避免儿童吞咽风险
- 使用食品级安全材料，无甲醛、无重金属残留
- 产品边缘做圆角处理（半径≥2mm），无尖锐结构"""
    }
}

package com.childproduct.designassistant.utils

import com.childproduct.designassistant.model.*

/**
 * 设计方案格式化工具测试
 * 验证格式化输出是否符合要求
 */
object DesignSchemeFormatterTest {

    /**
     * 测试格式化输出
     */
    fun testFormatCreativeIdea(): String {
        // 创建测试数据
        val complianceParams = ComplianceParameters(
            dummyType = CrashTestDummy.Q1,
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
            productType = ProductType.CHILD_SAFETY_SEAT,
            theme = "婴幼儿专用儿童安全座椅（柔和色彩）",
            features = listOf(
                "易安装性：支持ISOFIX快速连接",
                "舒适性：高回弹海绵填充靠背",
                "材质环保：食品级安全材料",
                "安全性：符合多国标安全要求"
            ),
            materials = listOf(
                "主体框架：食品级PP塑料",
                "填充层：高回弹海绵",
                "约束部件：高强度安全带织带",
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
     * 测试卡片数据格式化
     */
    fun testFormatToCardData(): SchemeCardData? {
        val complianceParams = ComplianceParameters(
            dummyType = CrashTestDummy.Q1,
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
            productType = ProductType.CHILD_SAFETY_SEAT,
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
     * 验证格式化输出是否符合要求
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
}

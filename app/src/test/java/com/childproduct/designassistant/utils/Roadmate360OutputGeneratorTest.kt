package com.childproduct.designassistant.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Roadmate360OutputGenerator 单元测试
 */
class Roadmate360OutputGeneratorTest {

    @Test
    fun testGenerateOutput_105to150cm() {
        val output = Roadmate360OutputGenerator.generateOutput(
            minHeightCm = 105,
            maxHeightCm = 150,
            productType = "儿童安全座椅",
            designTheme = "社交元素集成式安全座椅"
        )

        // 验证输出包含所有必要模块
        assertTrue("【设计方案】", output.contains("【设计方案】"))
        assertTrue("【测试矩阵】", output.contains("【测试矩阵】"))
        assertTrue("【安全阈值】", output.contains("【安全阈值】"))
        assertTrue("【合规声明】", output.contains("【合规声明】"))

        // 验证假人映射
        assertTrue("Q6+Q10", output.contains("Q6假人(105-145cm) + Q10假人(145-150cm)"))

        // 验证标准引用
        assertTrue("ECE R129", output.contains("ECE R129"))

        println("✅ testGenerateOutput_105to150cm 通过")
    }

    @Test
    fun testGenerateStandardizedScheme() {
        val scheme = Roadmate360OutputGenerator.generateStandardizedScheme(
            minHeightCm = 105,
            maxHeightCm = 150,
            productType = "儿童安全座椅",
            designTheme = "社交元素"
        )

        // 验证基本字段
        assertEquals("儿童安全座椅", scheme.productType)
        assertEquals("105-150cm", scheme.heightRange)
        assertEquals("4-12岁", scheme.ageRange)

        // 验证假人类型
        assertTrue(scheme.dummyType.contains("Q6"))
        assertTrue(scheme.dummyType.contains("Q10"))

        // 验证核心特点不为空
        assertTrue(scheme.coreFeatures.isNotEmpty())
        assertTrue(scheme.coreFeatures.size >= 4)

        // 验证推荐材料不为空
        assertTrue(scheme.recommendMaterials.isNotEmpty())
        assertTrue(scheme.recommendMaterials.size >= 4)

        // 验证合规标准
        assertEquals("ECE R129 (i-Size)", scheme.complianceStandards.first())

        // 验证安全阈值
        assertTrue(scheme.safetyThresholds.containsKey("HIC36"))
        assertTrue(scheme.safetyThresholds.containsKey("胸部合成加速度(3ms)"))

        // 验证测试矩阵
        assertTrue(scheme.testMatrix.isNotEmpty())

        // 验证安全注意事项
        assertTrue(scheme.safetyNotes.isNotEmpty())

        // 验证通过
        assertTrue(scheme.validationResult.isValid)

        println("✅ testGenerateStandardizedScheme 通过")
    }

    @Test
    fun testFormatForDisplay() {
        val scheme = Roadmate360OutputGenerator.generateStandardizedScheme(
            minHeightCm = 105,
            maxHeightCm = 150
        )
        val displayText = Roadmate360OutputGenerator.formatForDisplay(scheme)

        // 验证输出为纯文本
        assertNotNull(displayText)
        assertTrue(displayText.isNotEmpty())
        assertTrue(displayText.contains("【设计方案】"))
        assertTrue(displayText.contains("【测试矩阵】"))
        assertTrue(displayText.contains("【安全阈值】"))
        assertTrue(displayText.contains("【合规声明】"))

        // 验证不包含代码字段（如UUID、id等）
        assertTrue("不包含id字段", !displayText.contains("id="))
        assertTrue("不包含UUID", !displayText.contains("UUID"))

        println("✅ testFormatForDisplay 通过")
    }

    @Test
    fun testGenerateTestMatrix() {
        val testItems = Roadmate360OutputGenerator.generateTestMatrix(105, 150)

        // 验证Q6和Q10假人都包含测试项
        val q6Items = testItems.filter { it.dummy == "Q6" }
        val q10Items = testItems.filter { it.dummy == "Q10" }

        assertTrue("Q6假人测试项", q6Items.isNotEmpty())
        assertTrue("Q10假人测试项", q10Items.isNotEmpty())
        assertTrue("Q6假人测试项数量", q6Items.size >= 4)
        assertTrue("Q10假人测试项数量", q10Items.size >= 4)

        println("✅ testGenerateTestMatrix 通过 (Q6: ${q6Items.size}项, Q10: ${q10Items.size}项)")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testGenerateOutput_InvalidRange() {
        // 测试无效范围（最小值大于最大值）
        Roadmate360OutputGenerator.generateOutput(
            minHeightCm = 150,
            maxHeightCm = 100
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testGenerateOutput_OutOfRange() {
        // 测试超出范围（>150cm）
        Roadmate360OutputGenerator.generateOutput(
            minHeightCm = 100,
            maxHeightCm = 200
        )
    }

    @Test
    fun testGetAgeRange() {
        // 测试年龄段映射
        // 由于getAgeRange是私有方法，我们通过generateOutput间接测试
        val output1 = Roadmate360OutputGenerator.generateOutput(105, 125)
        assertTrue(output1.contains("4-6岁"))

        val output2 = Roadmate360OutputGenerator.generateOutput(125, 145)
        assertTrue(output2.contains("6-10岁"))

        val output3 = Roadmate360OutputGenerator.generateOutput(145, 150)
        assertTrue(output3.contains("10-12岁"))

        println("✅ testGetAgeRange 通过")
    }
}

package com.childproduct.designassistant.utils

import org.junit.Test
import org.junit.Assert.*

class OutputComplianceCheckerTest {

    @Test
    fun testCheckAndFixOutput_withGarbledText() {
        // 原始乱码输出
        val rawBadOutput = "CreativeIdea(id=123) 产品类型=儿童安全座椅 适用年龄段：6-9岁 设计主题：创意涂鸦儿童安全座椅 HIC≤1000 HIC≤1000 符合ECE R129 适配假人：Q3"

        // 调用校验工具修正
        val fixedOutput = OutputComplianceChecker.checkAndFixOutput(
            rawOutput = rawBadOutput,
            inputHeightRange = "40-150",
            productType = "儿童安全座椅"
        )

        // 验证修正结果
        assertTrue("修正后的输出应包含【基本信息】", fixedOutput.contains("【基本信息】"))
        assertTrue("修正后的输出应包含正确的年龄段", fixedOutput.contains("0-12岁（身高40-150cm）"))
        assertTrue("修正后的输出应包含正确的假人类型", fixedOutput.contains("Q0-Q10（0-12岁）"))
        assertFalse("修正后的输出不应包含乱码字段", fixedOutput.contains("CreativeIdea(id=123)"))
        assertFalse("修正后的输出不应包含重复的HIC阈值", fixedOutput.contains("HIC≤1000 HIC≤1000"))
    }

    @Test
    fun testCheckAndFixOutput_withWrongAgeRange() {
        // 包含错误年龄段的输出
        val rawOutput = "适用年龄段：3-6岁 适配假人：Q6"

        val fixedOutput = OutputComplianceChecker.checkAndFixOutput(
            rawOutput = rawOutput,
            inputHeightRange = "40-150",
            productType = "儿童安全座椅"
        )

        // 验证年龄段被修正
        assertTrue("应修正为正确的年龄段", fixedOutput.contains("0-12岁（身高40-150cm）"))
        assertFalse("不应包含错误的年龄段", fixedOutput.contains("3-6岁"))
    }

    @Test
    fun testCheckAndFixOutput_withWrongDummyType() {
        // 包含错误假人类型的输出
        val rawOutput = "适配假人：Q3 Q6"

        val fixedOutput = OutputComplianceChecker.checkAndFixOutput(
            rawOutput = rawOutput,
            inputHeightRange = "40-150",
            productType = "儿童安全座椅"
        )

        // 验证假人类型被修正
        assertTrue("应修正为正确的假人类型", fixedOutput.contains("Q0-Q10（0-12岁）"))
        assertFalse("不应包含错误的假人类型", fixedOutput.contains("Q3"))
    }

    @Test
    fun testCheckAndFixOutput_structureModules() {
        // 简单的原始输出
        val rawOutput = "设计主题：测试主题"

        val fixedOutput = OutputComplianceChecker.checkAndFixOutput(
            rawOutput = rawOutput,
            inputHeightRange = "40-150",
            productType = "儿童安全座椅"
        )

        // 验证所有结构化模块都存在
        assertTrue("应包含【基本信息】", fixedOutput.contains("【基本信息】"))
        assertTrue("应包含【核心设计特点】", fixedOutput.contains("【核心设计特点】"))
        assertTrue("应包含【推荐材料】", fixedOutput.contains("【推荐材料】"))
        assertTrue("应包含【合规参数】", fixedOutput.contains("【合规参数】"))
        assertTrue("应包含【安全注意事项】", fixedOutput.contains("【安全注意事项】"))
    }

    @Test
    fun testCheckAndFixOutput_withSpecialCharacters() {
        // 包含特殊字符的输出
        val rawOutput = "设计主题：测试@#\$%^&*()主题"

        val fixedOutput = OutputComplianceChecker.checkAndFixOutput(
            rawOutput = rawOutput,
            inputHeightRange = "40-150",
            productType = "儿童安全座椅"
        )

        // 验证特殊字符被清理
        assertFalse("不应包含特殊字符", fixedOutput.contains("@#\$%^&*()"))
    }

    @Test
    fun testCheckAndFixOutput_withEmptyInput() {
        // 空输入
        val rawOutput = ""

        val fixedOutput = OutputComplianceChecker.checkAndFixOutput(
            rawOutput = rawOutput,
            inputHeightRange = "40-150",
            productType = "儿童安全座椅"
        )

        // 验证输出不为空且包含标准结构
        assertFalse("输出不应为空", fixedOutput.isEmpty())
        assertTrue("应包含【基本信息】", fixedOutput.contains("【基本信息】"))
    }

    @Test
    fun testCheckAndFixOutput_withDifferentProductType() {
        // 不同的产品类型
        val rawOutput = "设计主题：婴儿推车主题"

        val fixedOutput = OutputComplianceChecker.checkAndFixOutput(
            rawOutput = rawOutput,
            inputHeightRange = "40-150",
            productType = "婴儿推车"
        )

        // 验证产品类型被正确设置
        assertTrue("应包含正确的产品类型", fixedOutput.contains("- 产品类型：婴儿推车"))
    }

    @Test
    fun testCheckAndFixOutput_withDifferentHeightRange() {
        // 不同的身高范围
        val rawOutput = "设计主题：测试主题"

        val fixedOutput = OutputComplianceChecker.checkAndFixOutput(
            rawOutput = rawOutput,
            inputHeightRange = "60-75",
            productType = "儿童安全座椅"
        )

        // 验证使用默认值（因为映射表中没有60-75）
        assertTrue("应使用默认年龄段", fixedOutput.contains("0-12岁（身高40-150cm）"))
    }

    @Test
    fun testCheckAndFixOutput_duplicateRemoval() {
        // 包含重复信息的输出
        val rawOutput = "HIC≤1000 HIC≤1000 符合ECE R129 符合ECE R129"

        val fixedOutput = OutputComplianceChecker.checkAndFixOutput(
            rawOutput = rawOutput,
            inputHeightRange = "40-150",
            productType = "儿童安全座椅"
        )

        // 验证重复信息被移除
        val hicCount = "HIC≤1000".toRegex().findAll(fixedOutput).count()
        assertEquals("HIC阈值不应重复", 1, hicCount)
    }

    @Test
    fun testStructureFormat_outputFormat() {
        // 验证结构化输出的格式
        val rawOutput = "设计主题：测试主题"

        val fixedOutput = OutputComplianceChecker.checkAndFixOutput(
            rawOutput = rawOutput,
            inputHeightRange = "40-150",
            productType = "儿童安全座椅"
        )

        // 验证关键信息存在
        assertTrue("应包含产品类型", fixedOutput.contains("产品类型：儿童安全座椅"))
        assertTrue("应包含适用年龄段", fixedOutput.contains("适用年龄段："))
        assertTrue("应包含设计主题", fixedOutput.contains("设计主题："))
        assertTrue("应包含对应标准", fixedOutput.contains("对应标准："))
        assertTrue("应包含适配假人", fixedOutput.contains("适配假人："))
        assertTrue("应包含安全阈值", fixedOutput.contains("安全阈值："))
    }
}

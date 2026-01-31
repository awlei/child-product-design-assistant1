package com.childproduct.designassistant.helper

import com.childproduct.designassistant.model.InstallMethod
import com.childproduct.designassistant.validator.ValidatorFactory
import org.junit.Assert.*
import org.junit.Test

/**
 * SchemeOptimizer 单元测试
 *
 * 测试优化后的功能：
 * - 乱码清理
 * - 方案生成
 * - 输入验证
 * - 格式化输出
 */
class SchemeOptimizerTest {

    @Test
    fun `test cleanGarbledContent with normal text`() {
        val input = "拼图游戏"
        val output = SchemeOptimizer.cleanGarbledContent(input)
        assertEquals(input, output)
    }

    @Test
    fun `test cleanGarbledContent with code patterns`() {
        val input = "拼图游戏 CreativeIdea(id=abc-123) [a=b, c=d]"
        val output = SchemeOptimizer.cleanGarbledContent(input)
        assertFalse(output.contains("CreativeIdea"))
        assertFalse(output.contains("id="))
        assertFalse(output.contains("[a=b"))
        assertTrue(output.contains("拼图游戏"))
    }

    @Test
    fun `test cleanGarbledContent with special characters`() {
        val input = "拼图游戏😊\t测试"
        val output = SchemeOptimizer.cleanGarbledContent(input)
        assertFalse(output.contains("😊"))
        assertFalse(output.contains("\t"))
        assertTrue(output.contains("拼图游戏"))
        assertTrue(output.contains("测试"))
    }

    @Test
    fun `test cleanGarbledContent with empty string`() {
        val input = ""
        val output = SchemeOptimizer.cleanGarbledContent(input)
        assertEquals("", output)
    }

    @Test
    fun `test cleanGarbledContent with blank string`() {
        val input = "   "
        val output = SchemeOptimizer.cleanGarbledContent(input)
        assertEquals("", output)
    }

    @Test
    fun `test generateOptimizedScheme with valid input`() {
        val userInput = SchemeOptimizer.UserInput(
            productType = "儿童安全座椅",
            heightRange = "40-150cm",
            installMethod = InstallMethod.ISOFIX,
            themeKeyword = "拼图游戏"
        )

        val scheme = SchemeOptimizer.generateOptimizedScheme(userInput)

        // 验证基本信息
        assertEquals("儿童安全座椅", scheme.productType)
        assertEquals("40-150cm", scheme.heightRange)
        assertEquals("0-12岁", scheme.ageRange)
        assertEquals("儿童安全座椅 - 拼图游戏", scheme.designTheme)

        // 验证核心特点不为空
        assertTrue(scheme.coreFeatures.isNotEmpty())
        assertTrue(scheme.coreFeatures.any { it.contains("易安装性") })
        assertTrue(scheme.coreFeatures.any { it.contains("安全性") })

        // 验证验证通过
        assertTrue(scheme.validationResult.isValid)
    }

    @Test
    fun `test generateOptimizedScheme with different height range`() {
        val userInput = SchemeOptimizer.UserInput(
            productType = "儿童安全座椅",
            heightRange = "87-105cm",
            installMethod = InstallMethod.ISOFIX_TOP_TETHER,
            themeKeyword = "卡通图案"
        )

        val scheme = SchemeOptimizer.generateOptimizedScheme(userInput)

        assertEquals("87-105cm", scheme.heightRange)
        assertEquals("3-4岁", scheme.ageRange)
        assertTrue(scheme.validationResult.isValid)
    }

    @Test
    fun `test validateInput with valid input`() {
        val userInput = SchemeOptimizer.UserInput(
            productType = "儿童安全座椅",
            heightRange = "40-150cm",
            installMethod = InstallMethod.ISOFIX,
            themeKeyword = "拼图游戏"
        )

        val (isValid, message) = SchemeOptimizer.validateInput(userInput)

        assertTrue(isValid)
        assertTrue(message.isEmpty())
    }

    @Test
    fun `test validateInput with empty product type`() {
        val userInput = SchemeOptimizer.UserInput(
            productType = "",
            heightRange = "40-150cm",
            installMethod = InstallMethod.ISOFIX,
            themeKeyword = "拼图游戏"
        )

        val (isValid, message) = SchemeOptimizer.validateInput(userInput)

        assertFalse(isValid)
        assertTrue(message.contains("产品类型"))
    }

    @Test
    fun `test validateInput with invalid height range`() {
        val userInput = SchemeOptimizer.UserInput(
            productType = "儿童安全座椅",
            heightRange = "40-160cm",
            installMethod = InstallMethod.ISOFIX,
            themeKeyword = "拼图游戏"
        )

        val (isValid, message) = SchemeOptimizer.validateInput(userInput)

        assertFalse(isValid)
        assertTrue(message.contains("身高范围"))
    }

    @Test
    fun `test validateInput with empty theme keyword`() {
        val userInput = SchemeOptimizer.UserInput(
            productType = "儿童安全座椅",
            heightRange = "40-150cm",
            installMethod = InstallMethod.ISOFIX,
            themeKeyword = ""
        )

        val (isValid, message) = SchemeOptimizer.validateInput(userInput)

        assertFalse(isValid)
        assertTrue(message.contains("设计主题"))
    }

    @Test
    fun `test formatSchemeForDisplay`() {
        val userInput = SchemeOptimizer.UserInput(
            productType = "儿童安全座椅",
            heightRange = "40-150cm",
            installMethod = InstallMethod.ISOFIX,
            themeKeyword = "拼图游戏"
        )

        val scheme = SchemeOptimizer.generateOptimizedScheme(userInput)
        val formatted = SchemeOptimizer.formatSchemeForDisplay(scheme)

        // 验证格式化输出包含关键信息
        assertTrue(formatted.contains("UN R129:2024 / GB 27887-2024"))
        assertTrue(formatted.contains("产品类型"))
        assertTrue(formatted.contains("儿童安全座椅"))
        assertTrue(formatted.contains("身高范围"))
        assertTrue(formatted.contains("40-150cm"))
        assertTrue(formatted.contains("核心特点"))
        assertTrue(formatted.contains("推荐材料"))
        assertTrue(formatted.contains("安全注意事项"))
    }

    @Test
    fun `test validator integration`() {
        val userInput = SchemeOptimizer.UserInput(
            productType = "儿童安全座椅",
            heightRange = "40-150cm",
            installMethod = InstallMethod.ISOFIX,
            themeKeyword = "拼图游戏"
        )

        val validator = ValidatorFactory.productInputValidator()
        val validationResult = validator.validate(userInput)

        assertTrue(validationResult.isValid)
        assertTrue(validationResult.errors.isEmpty())
    }

    @Test
    fun `test validator with warnings`() {
        val userInput = SchemeOptimizer.UserInput(
            productType = "儿童安全座椅",
            heightRange = "30-160cm",
            installMethod = InstallMethod.ISOFIX,
            themeKeyword = "拼图游戏"
        )

        val validator = ValidatorFactory.productInputValidator()
        val validationResult = validator.validate(userInput)

        // 身高范围不符合标准，应该有警告
        assertFalse(validationResult.isValid)
        assertTrue(validationResult.errors.isNotEmpty())
    }
}

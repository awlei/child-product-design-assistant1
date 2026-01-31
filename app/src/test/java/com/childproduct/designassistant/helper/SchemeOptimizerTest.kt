package com.childproduct.designassistant.helper

import com.childproduct.designassistant.model.InstallMethod
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * SchemeOptimizer 单元测试
 */
class SchemeOptimizerTest {

    @Test
    fun testGenerateOptimizedScheme() {
        // 准备测试数据
        val userInput = SchemeOptimizer.UserInput(
            productType = "儿童安全座椅",
            heightRange = "40-150cm",
            installMethod = InstallMethod.ISOFIX_TOP_TETHER,
            themeKeyword = "拼图游戏"
        )

        // 生成方案
        val scheme = SchemeOptimizer.generateOptimizedScheme(userInput)

        // 验证基本信息
        assertEquals("儿童安全座椅", scheme.productType)
        assertEquals("40-150cm", scheme.heightRange)
        assertEquals("0-12岁", scheme.ageRange)
        assertEquals("儿童安全座椅 - 拼图游戏", scheme.designTheme)

        // 验证安装方式
        assertEquals("ISOFIX快速连接 + Top-tether防旋转（上拉带）", scheme.installMethodDesc)

        // 验证核心特点包含安装方式
        val installFeature = scheme.coreFeatures.find { it.contains("ISOFIX快速连接 + Top-tether防旋转") }
        assertNotNull("核心特点应包含安装方式描述", installFeature)

        // 验证测试矩阵不为空
        assertNotNull("测试矩阵不应为空", scheme.testMatrix)
        assertEquals("测试矩阵应有8个测试项", 8, scheme.testMatrix.size)

        // 验证假人类型
        assertEquals("Q0-Q10全假人", scheme.dummyType)

        // 验证安全注意事项不为空
        assertNotNull("安全注意事项不应为空", scheme.safetyNotes)
        assertEquals("安全注意事项应有5项", 5, scheme.safetyNotes.size)

        println("✅ 测试通过：方案生成正确")
        println("生成的方案：")
        println(SchemeOptimizer.formatSchemeForDisplay(scheme))
    }

    @Test
    fun testHeightMapping() {
        // 测试不同身高范围的映射
        val testCases = mapOf(
            "40-60cm" to "0-1岁",
            "60-75cm" to "1-2岁",
            "75-87cm" to "2-3岁",
            "87-105cm" to "3-4岁",
            "105-125cm" to "4-6岁",
            "125-150cm" to "6-12岁",
            "40-150cm" to "0-12岁"
        )

        testCases.forEach { (height, expectedAge) ->
            val userInput = SchemeOptimizer.UserInput(
                productType = "儿童安全座椅",
                heightRange = height,
                installMethod = InstallMethod.ISOFIX,
                themeKeyword = "测试"
            )

            val scheme = SchemeOptimizer.generateOptimizedScheme(userInput)
            assertEquals("身高 $height 应映射到年龄段 $expectedAge", expectedAge, scheme.ageRange)
            println("✅ $height → $expectedAge (实际：${scheme.ageRange})")
        }
    }

    @Test
    fun testTestMatrixStructure() {
        val userInput = SchemeOptimizer.UserInput(
            productType = "儿童安全座椅",
            heightRange = "40-150cm",
            installMethod = InstallMethod.ISOFIX,
            themeKeyword = "测试"
        )

        val scheme = SchemeOptimizer.generateOptimizedScheme(userInput)

        // 验证测试矩阵结构
        scheme.testMatrix.forEach { item ->
            assertNotNull("测试项名称不应为空", item.testItem)
            assertNotNull("标准要求不应为空", item.standardRequirement)
            assertNotNull("适用假人不应为空", item.applicableDummy)
            assertNotNull("单位不应为空", item.unit)
            assertNotNull("标准来源不应为空", item.standardSource)
        }

        println("✅ 测试通过：测试矩阵结构正确")
    }

    @Test
    fun testInstallMethodVariations() {
        val installMethods = InstallMethod.values()

        installMethods.forEach { method ->
            val userInput = SchemeOptimizer.UserInput(
                productType = "儿童安全座椅",
                heightRange = "40-150cm",
                installMethod = method,
                themeKeyword = "测试"
            )

            val scheme = SchemeOptimizer.generateOptimizedScheme(userInput)

            // 验证安装方式描述正确
            assertEquals(method.description, scheme.installMethodDesc)

            // 验证核心特点包含安装方式
            val hasInstallFeature = scheme.coreFeatures.any { it.contains(method.description) }
            assert(hasInstallFeature) { "核心特点应包含安装方式：${method.description}" }

            println("✅ 安装方式 ${method.displayName} → ${scheme.installMethodDesc}")
        }
    }
}

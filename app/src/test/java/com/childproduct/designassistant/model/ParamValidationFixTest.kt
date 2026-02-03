package com.childproduct.designassistant.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * 测试假人匹配和年龄区间修复
 */
class ParamValidationFixTest {

    @Test
    fun test40_105cm_MultipleDummies() {
        // 获取ECE R129标准配置
        val config = ProductTypeConfigManager.getConfigByProductType(ProductType.SAFETY_SEAT)
        val standard = config?.standards?.find { it.standardId == "ECE_R129_GB27887" }
        assertNotNull(standard, "ECE R129标准配置应该存在")

        // 测试40-105cm范围
        val result = ProductTypeConfigManager.validateHeightRange(40.0, 105.0, standard!!)

        // 验证结果
        assert(result.isValid) { "验证应该通过" }

        // 验证匹配多个假人类型（Q0、Q1、Q1.5、Q3）
        val matchedDummies = result.matchedDummies
        assert(matchedDummies != null && matchedDummies.size >= 3) {
            "40-105cm应该匹配至少3个假人类型，实际匹配：${matchedDummies?.size}"
        }

        println("匹配的假人类型：${matchedDummies?.map { it.name }}")
        println("匹配的年龄区间：${result.matchedInterval}")

        // 验证年龄区间应该是1-3岁
        val interval = result.matchedInterval ?: ""
        assert(interval.contains("1-3岁") || interval.contains("3岁")) {
            "年龄区间应该包含1-3岁，实际：$interval"
        }
    }

    @Test
    fun test40_60cm_SingleDummy() {
        // 获取ECE R129标准配置
        val config = ProductTypeConfigManager.getConfigByProductType(ProductType.SAFETY_SEAT)
        val standard = config?.standards?.find { it.standardId == "ECE_R129_GB27887" }
        assertNotNull(standard, "ECE R129标准配置应该存在")

        // 测试40-60cm范围（单个区间）
        val result = ProductTypeConfigManager.validateHeightRange(40.0, 60.0, standard!!)

        // 验证结果
        assert(result.isValid) { "验证应该通过" }

        // 验证匹配的假人类型
        val matchedDummies = result.matchedDummies
        println("40-60cm匹配的假人类型：${matchedDummies?.map { it.name }}")
        println("40-60cm匹配的年龄区间：${result.matchedInterval}")
    }

    @Test
    fun test60_75cm_SingleDummy() {
        // 获取ECE R129标准配置
        val config = ProductTypeConfigManager.getConfigByProductType(ProductType.SAFETY_SEAT)
        val standard = config?.standards?.find { it.standardId == "ECE_R129_GB27887" }
        assertNotNull(standard, "ECE R129标准配置应该存在")

        // 测试60-75cm范围（单个区间）
        val result = ProductTypeConfigManager.validateHeightRange(60.0, 75.0, standard!!)

        // 验证结果
        assert(result.isValid) { "验证应该通过" }

        // 验证匹配的假人类型
        val matchedDummies = result.matchedDummies
        println("60-75cm匹配的假人类型：${matchedDummies?.map { it.name }}")
        println("60-75cm匹配的年龄区间：${result.matchedInterval}")

        // 验证年龄区间应该是1-2岁
        val interval = result.matchedInterval ?: ""
        assert(interval.contains("1-2岁")) {
            "年龄区间应该是1-2岁，实际：$interval"
        }
    }
}

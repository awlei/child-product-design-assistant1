package com.childproduct.designassistant.utils

import org.junit.Test
import org.junit.Assert.*

/**
 * 输出优化效果验证测试
 *
 * 验证所有核心问题的解决方案：
 * 1. 参数匹配错误
 * 2. 乱码/冗余代码字段
 * 3. 信息重复堆砌
 * 4. 界面信息自相矛盾
 * 5. 输出排版无结构化
 */
class OutputOptimizationVerificationTest {

    @Test
    fun verifyProblem1_FixedParameterMatching() {
        // 问题1：参数匹配错误（违反UN R129/GB 27887-2024标准）
        // 输入：40-150cm，错误匹配：Q3、3-4岁、6-9岁
        val rawBadContent = """
            CreativeIdea(id=123) 适配假人：Q3学前儿童假人（87-105cm）
            匹配区间：3-4岁
            生成方案年龄段标注：6-9岁
            身高范围：40-150cm
        """.trimIndent()

        val optimizedResult = OutputComplianceChecker.optimizeScheme(
            rawContent = rawBadContent,
            inputHeight = "40-150cm",
            productType = "儿童安全座椅"
        )

        // 验证修正结果
        assertTrue("应修正为正确的年龄段（0-12岁）", optimizedResult.contains("0-12岁"))
        assertTrue("应修正为正确的假人类型（Q0-Q10）", optimizedResult.contains("Q0-Q10（全年龄段假人）"))
        assertFalse("不应包含错误的年龄段（6-9岁）", optimizedResult.contains("6-9岁"))
        assertFalse("不应包含错误的假人类型（Q3）", optimizedResult.contains("Q3学前儿童假人"))
    }

    @Test
    fun verifyProblem2_CleanGarbledCodeFields() {
        // 问题2：输出内容含大量乱码/冗余代码字段
        val rawBadContent = """
            CreativeIdea(id=6ade8c48-4e33-46f5-a5c7-241347746403)
            productType=CHILD_SAFETY_SEAT
            ageGroup=PRESCHOOL
            complianceParameters=ComplianceParameters(dummyType=Q3, hicLimit=1000)
            standardsReference=StandardsReference(mainStandard=ECE R129 + GB 27887-2024 + FMVSS 213)
            designTheme=拼图游戏
        """.trimIndent()

        val optimizedResult = OutputComplianceChecker.optimizeScheme(
            rawContent = rawBadContent,
            inputHeight = "40-150cm"
        )

        // 验证清理结果
        assertFalse("不应包含代码字段（CreativeIdea(id=...)）", optimizedResult.contains("CreativeIdea(id="))
        assertFalse("不应包含代码字段（productType=...）", optimizedResult.contains("productType="))
        assertFalse("不应包含代码字段（ageGroup=...）", optimizedResult.contains("ageGroup="))
        assertFalse("不应包含代码字段（complianceParameters=...）", optimizedResult.contains("complianceParameters="))
        assertFalse("不应包含代码字段（standardsReference=...）", optimizedResult.contains("standardsReference="))
        assertFalse("不应包含代码字段（designTheme=...）", optimizedResult.contains("designTheme="))
    }

    @Test
    fun verifyProblem3_RemoveDuplicateInfo() {
        // 问题3：信息重复堆砌
        val rawBadContent = """
            HIC极限值≤1000 HIC极限值≤1000 符合ECE R129 符合ECE R129
            安全性（符合ECE R129/GB 27887-2024标准）
            材质环保（食品级PP塑料）
            材质环保（食品级PP塑料）
        """.trimIndent()

        val optimizedResult = OutputComplianceChecker.optimizeScheme(
            rawContent = rawBadContent,
            inputHeight = "40-150cm"
        )

        // 验证去重结果
        val hicCount = "HIC极限值".toRegex().findAll(optimizedResult).count()
        assertEquals("HIC极限值不应重复", 1, hicCount)

        val eceCount = "符合ECE R129".toRegex().findAll(optimizedResult).count()
        assertEquals("符合ECE R129不应重复", 1, eceCount)
    }

    @Test
    fun verifyProblem4_FixSelfContradiction() {
        // 问题4：界面信息自相矛盾
        // "当前合规组合"标注"适配对应dummy（Q0-Q10）"，但下方仅显示"Q3"
        val rawBadContent = """
            当前合规组合：适配对应dummy（Q0-Q10）
            适配假人：Q3学前儿童假人
            身高范围：40-150cm
        """.trimIndent()

        val optimizedResult = OutputComplianceChecker.optimizeScheme(
            rawContent = rawBadContent,
            inputHeight = "40-150cm"
        )

        // 验证修正结果
        assertTrue("应包含正确的假人类型（Q0-Q10）", optimizedResult.contains("Q0-Q10（全年龄段假人）"))
        assertFalse("不应包含错误的假人类型（Q3）", optimizedResult.contains("Q3学前儿童假人"))

        // 验证界面一致性
        val dummyCount = "适配假人：".toRegex().findAll(optimizedResult).count()
        assertTrue("假人类型应只出现一次（确保一致性）", dummyCount == 1)
    }

    @Test
    fun verifyProblem5_StructuredOutput() {
        // 问题5：输出排版无结构化
        val rawBadContent = """
            CreativeIdea(id=123) 设计主题：拼图游戏 易安装性 安全性 舒适性 材质环保
            食品级PP塑料 高回弹海绵 安全带织带 铝合金支架
        """.trimIndent()

        val optimizedResult = OutputComplianceChecker.optimizeScheme(
            rawContent = rawBadContent,
            inputHeight = "40-150cm"
        )

        // 验证结构化输出
        assertTrue("应包含【基本信息】模块", optimizedResult.contains("【基本信息】"))
        assertTrue("应包含【核心设计特点】模块", optimizedResult.contains("【核心设计特点】"))
        assertTrue("应包含【推荐材料】模块", optimizedResult.contains("【推荐材料】"))
        assertTrue("应包含【合规参数】模块", optimizedResult.contains("【合规参数】"))
        assertTrue("应包含【安全注意事项】模块", optimizedResult.contains("【安全注意事项】"))
    }

    @Test
    fun verifyCompleteOptimizationExample() {
        // 完整优化示例：原始杂乱内容 → 标准化结构化输出
        val rawBadContent = """
            CreativeIdea(id=6ade8c48-4e33-46f5-a5c7-241347746403, title=6-9岁儿童安全座椅 - 拼图游戏, description=专为6-9岁儿童设计的儿童安全座椅，融入拼图游戏设计理念。主要特点包括：易安装性、安全性、舒适性、材质环保。符合UN R129 i-Size儿童标准（Q3假人），HIC极限值≤1000，满足FMVSS 302燃烧性能要求，通过ISOFIX连接实现快速安装。
            ageGroup=PRESCHOOL, productType=CHILD_SAFETY_SEAT, theme=拼图游戏, features=[易安装性, 安全性, 舒适性, 材质环保], materials=[食品级PP塑料, 高回弹海绵, 安全带织带, 铝合金支架], colorPalette=[#FFA500, #00CED1, #FF69B4, #9370DB], safetyNotes=[避免细小零件脱落风险, 材料需通过欧盟EN71安全认证, 结构稳固，不易倒塌]
            complianceParameters=ComplianceParameters(dummyType=Q3, hicLimit=1000, chestAccelerationLimit=60, neckTensionLimit=2000, neckCompressionLimit=2500, headExcursionLimit=550, kneeExcursionLimit=650, chestDeflectionLimit=52)
            standardsReference=StandardsReference(mainStandard=ECE R129 + GB 27887-2024 + FMVSS 213, keyClauses=[ECE R129 §5.2: 假人分类（Q0-Q10）])
            HIC极限值≤1000 HIC极限值≤1000 符合ECE R129 适配假人：Q3
        """.trimIndent()

        val optimizedResult = OutputComplianceChecker.optimizeScheme(
            rawContent = rawBadContent,
            inputHeight = "40-150cm",
            productType = "儿童安全座椅"
        )

        // 验证所有问题都被解决
        // 1. 参数匹配正确
        assertTrue("年龄段应为0-12岁", optimizedResult.contains("0-12岁"))
        assertTrue("假人类型应为Q0-Q10", optimizedResult.contains("Q0-Q10（全年龄段假人）"))

        // 2. 无代码字段
        assertFalse("不应包含CreativeIdea", optimizedResult.contains("CreativeIdea"))
        assertFalse("不应包含productType=", optimizedResult.contains("productType="))

        // 3. 无重复信息
        val hicCount = "HIC极限值".toRegex().findAll(optimizedResult).count()
        assertEquals("HIC极限值应只出现1次", 1, hicCount)

        // 4. 信息一致
        val dummyMatches = "Q0-Q10".toRegex().findAll(optimizedResult).count()
        assertTrue("假人类型应保持一致（都为Q0-Q10）", dummyMatches >= 1)
        assertFalse("不应包含不一致的假人类型（Q3）", optimizedResult.contains("Q3假人"))

        // 5. 结构化输出
        assertTrue("应包含所有5个模块", optimizedResult.contains("【基本信息】"))
        assertTrue("应包含所有5个模块", optimizedResult.contains("【核心设计特点】"))
        assertTrue("应包含所有5个模块", optimizedResult.contains("【推荐材料】"))
        assertTrue("应包含所有5个模块", optimizedResult.contains("【合规参数】"))
        assertTrue("应包含所有5个模块", optimizedResult.contains("【安全注意事项】"))

        // 验证标准安全阈值
        assertTrue("应包含标准HIC极限值", optimizedResult.contains("HIC极限值：≤390（Q0-Q1.5）/≤1000（Q3-Q10）"))
        assertTrue("应包含标准胸部加速度", optimizedResult.contains("胸部加速度：≤55g（Q0-Q1.5）/≤60g（Q3-Q10）"))
        assertTrue("应包含标准颈部张力极限", optimizedResult.contains("颈部张力极限：≤1800N（Q3-Q10）"))
        assertTrue("应包含标准头部位移极限", optimizedResult.contains("头部位移极限：≤550mm（全假人）"))
        assertTrue("应包含标准阻燃性能", optimizedResult.contains("阻燃性能：符合FMVSS 302"))
    }

    @Test
    fun verifyDifferentHeightRanges() {
        // 验证不同身高范围的正确映射
        val testCases = listOf(
            "40-60" to Pair("0-1岁", "Q0假人"),
            "60-75" to Pair("1-2岁", "Q1假人"),
            "75-87" to Pair("2-3岁", "Q1.5假人"),
            "87-105" to Pair("3-4岁", "Q3假人"),
            "105-125" to Pair("4-6岁", "Q6假人"),
            "125-150" to Pair("6-12岁", "Q10假人"),
            "40-150" to Pair("0-12岁", "Q0-Q10（全年龄段假人）")
        )

        testCases.forEach { (heightRange, expectedPair) ->
            val (expectedAge, expectedDummy) = expectedPair
            val result = OutputComplianceChecker.optimizeScheme(
                rawContent = "设计主题：测试主题",
                inputHeight = heightRange
            )
            assertTrue("身高范围 $heightRange 应匹配年龄段 $expectedAge", result.contains(expectedAge))
            assertTrue("身高范围 $heightRange 应匹配假人 $expectedDummy", result.contains(expectedDummy))
        }
    }
}

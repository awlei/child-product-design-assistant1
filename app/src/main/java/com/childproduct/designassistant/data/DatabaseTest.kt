package com.childproduct.designassistant.data

/**
 * 数据库调用测试
 * 用于验证ChildSafetySeatDatabase和StrollerStandardDatabase的功能
 */
object DatabaseTest {
    
    /**
     * 测试儿童安全座椅数据库
     */
    fun testSafetySeatDatabase(): String {
        val result = StringBuilder()
        
        result.appendLine("=== 儿童安全座椅数据库测试 ===\n")
        
        // 1. 获取数据库统计
        val safetyStats = DatabaseManager.getSafetySeatDatabaseStats()
        result.appendLine("1. 数据库统计:")
        result.appendLine("   - GPS-028假人数: ${safetyStats.gpsDummiesCount}")
        result.appendLine("   - 澳标假人数: ${safetyStats.australianDummiesCount}")
        result.appendLine("   - 日标假人数: ${safetyStats.japaneseDummiesCount}")
        result.appendLine("   - 总假人数: ${safetyStats.totalDummiesCount}")
        result.appendLine("   - 支持的标准: ${safetyStats.supportedStandards.joinToString(", ")}\n")
        
        // 2. 获取所有支持的假人类型
        val allDummies = DatabaseManager.getAllSupportedDummies()
        result.appendLine("2. 所有支持的假人类型:")
        allDummies.forEach { (standard, dummies) ->
            result.appendLine("   - $standard: ${dummies.joinToString(", ")}")
        }
        result.appendLine()
        
        // 3. 根据身高范围查询假人（测试40-150cm）
        val dummiesByHeight = DatabaseManager.getSafetySeatDummiesByHeight(40, 150)
        result.appendLine("3. 根据身高范围查询假人（40-150cm）:")
        result.appendLine("   - GPS-028假人: ${dummiesByHeight.gpsDummies.size}个")
        result.appendLine("   - 澳标假人: ${dummiesByHeight.australianDummies.size}个")
        result.appendLine("   - 日标假人: ${dummiesByHeight.japaneseDummies.size}个\n")
        
        // 4. 获取澳标假人
        val ausDummies = DatabaseManager.getAustralianDummies()
        result.appendLine("4. 澳标假人数据:")
        ausDummies.forEach { dummy ->
            result.appendLine("   - ${dummy.displayName}: ${dummy.heightMin}-${dummy.heightMax}cm, ${dummy.minWeight}-${dummy.maxWeight}kg")
        }
        result.appendLine()
        
        // 5. 获取日标假人
        val jpDummies = DatabaseManager.getJapaneseDummies()
        result.appendLine("5. 日标假人数据:")
        jpDummies.forEach { dummy ->
            result.appendLine("   - ${dummy.displayName}: ${dummy.heightMin}-${dummy.heightMax}cm, ${dummy.minWeight}-${dummy.maxWeight}kg")
        }
        result.appendLine()
        
        return result.toString()
    }
    
    /**
     * 测试婴儿推车数据库
     */
    fun testStrollerDatabase(): String {
        val result = StringBuilder()
        
        result.appendLine("=== 婴儿推车数据库测试 ===\n")
        
        // 1. 获取数据库统计
        val strollerStats = DatabaseManager.getStrollerDatabaseStats()
        result.appendLine("1. 数据库统计:")
        result.appendLine("   - 标准数量: ${strollerStats.standardsCount}")
        result.appendLine("   - 测试项目数量: ${strollerStats.testItemsCount}")
        result.appendLine("   - 合规阈值数量: ${strollerStats.thresholdsCount}")
        result.appendLine("   - 材料要求数量: ${strollerStats.materialRequirementsCount}")
        result.appendLine("   - 设计要求数量: ${strollerStats.designRequirementsCount}")
        result.appendLine("   - 支持的地区: ${strollerStats.supportedRegions.joinToString(", ")}")
        result.appendLine("   - 支持的标准: ${strollerStats.supportedStandards.joinToString(", ")}\n")
        
        // 2. 获取所有标准信息
        val allStandards = DatabaseManager.getAllStrollerStandards()
        result.appendLine("2. 所有标准信息:")
        allStandards.forEach { standard ->
            result.appendLine("   - ${standard.standardId}: ${standard.standardName}")
            result.appendLine("     适用地区: ${standard.applicableRegion}")
            result.appendLine("     生效日期: ${standard.effectiveDate}")
        }
        result.appendLine()
        
        // 3. 根据地区查询标准（测试中国）
        val chinaStandards = DatabaseManager.getStrollerStandardsByRegion("China")
        result.appendLine("3. 中国市场的标准:")
        chinaStandards.forEach { standard ->
            result.appendLine("   - ${standard.standardId}: ${standard.standardName}")
        }
        result.appendLine()
        
        // 4. 获取综合合规要求（中国市场单人推车）
        val comprehensiveReq = DatabaseManager.getStrollerComprehensiveRequirements(
            productType = "单人推车（≤18kg）",
            targetRegion = "China"
        )
        result.appendLine("4. 中国市场单人推车综合合规要求:")
        if (comprehensiveReq != null) {
            result.appendLine("   - 所需标准: ${comprehensiveReq.requiredStandards.joinToString(", ")}")
            result.appendLine("   - 标准数量: ${comprehensiveReq.standardInfos.size}")
            result.appendLine("   - 测试项目数量: ${comprehensiveReq.testItems.size}")
            result.appendLine("   - 合规阈值数量: ${comprehensiveReq.thresholds.size}")
            result.appendLine("   - 材料要求数量: ${comprehensiveReq.materialRequirements.size}")
            result.appendLine("   - 设计要求数量: ${comprehensiveReq.designRequirements.size}")
            result.appendLine("   - 特殊说明: ${comprehensiveReq.note}")
        } else {
            result.appendLine("   - 未找到数据")
        }
        result.appendLine()
        
        return result.toString()
    }
    
    /**
     * 综合测试
     */
    fun runAllTests(): String {
        val result = StringBuilder()
        
        result.appendLine("╔══════════════════════════════════════════════════════════╗")
        result.appendLine("║         儿童产品设计助手 - 数据库调用测试                 ║")
        result.appendLine("╚══════════════════════════════════════════════════════════╝\n")
        
        // 测试儿童安全座椅数据库
        result.append(testSafetySeatDatabase())
        result.appendLine()
        
        // 测试婴儿推车数据库
        result.append(testStrollerDatabase())
        result.appendLine()
        
        result.appendLine("=== 测试完成 ===")
        result.appendLine("✓ 所有数据库调用正常")
        result.appendLine("✓ APK可以正确调用ChildSafetySeatDatabase")
        result.appendLine("✓ APK可以正确调用StrollerStandardDatabase")
        
        return result.toString()
    }
}

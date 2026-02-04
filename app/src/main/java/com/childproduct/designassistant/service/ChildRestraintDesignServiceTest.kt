package com.childproduct.designassistant.service

/**
 * 儿童安全座椅设计服务测试示例
 */
class ChildRestraintDesignServiceTest {

    private val service = ChildRestraintDesignService()

    /**
     * 测试1：仅选择 ECE R129 (欧标)
     */
    fun testEceR129Only() {
        println("=== 测试1：仅选择 ECE R129 ===")
        
        val selection = ChildRestraintDesignService.StandardSelection(
            eceR129 = true,
            gb27887 = false,
            fmvss213 = false,
            asNzs1754 = false,
            jisD1601 = false
        )
        
        val proposal = service.generateDesignProposal(
            selection = selection,
            heightCm = 100.0,  // 3岁儿童身高
            weightKg = 15.0
        )
        
        println(service.formatAsMarkdown(proposal))
        println("\n" + "=".repeat(80) + "\n")
    }

    /**
     * 测试2：仅选择 GB 28007-2024 (国标)
     */
    fun testGB27887Only() {
        println("=== 测试2：仅选择 GB 28007-2024 ===")
        
        val selection = ChildRestraintDesignService.StandardSelection(
            eceR129 = false,
            gb27887 = true,
            fmvss213 = false,
            asNzs1754 = false,
            jisD1601 = false
        )
        
        val proposal = service.generateDesignProposal(
            selection = selection,
            heightCm = 75.0,
            weightKg = 9.0
        )
        
        println(service.formatAsMarkdown(proposal))
        println("\n" + "=".repeat(80) + "\n")
    }

    /**
     * 测试3：仅选择 FMVSS 213 (美标)
     */
    fun testFmvss213Only() {
        println("=== 测试3：仅选择 FMVSS 213 ===")
        
        val selection = ChildRestraintDesignService.StandardSelection(
            eceR129 = false,
            gb27887 = false,
            fmvss213 = true,
            asNzs1754 = false,
            jisD1601 = false
        )
        
        val proposal = service.generateDesignProposal(
            selection = selection,
            heightCm = 125.0,
            weightKg = 22.0
        )
        
        println(service.formatAsMarkdown(proposal))
        println("\n" + "=".repeat(80) + "\n")
    }

    /**
     * 测试4：多标准选择 (ECE R129 + GB 28007-2024)
     */
    fun testMultiStandards() {
        println("=== 测试4：多标准选择 (ECE R129 + GB 28007-2024) ===")
        
        val selection = ChildRestraintDesignService.StandardSelection(
            eceR129 = true,
            gb27887 = true,
            fmvss213 = false,
            asNzs1754 = false,
            jisD1601 = false
        )
        
        val proposal = service.generateDesignProposal(
            selection = selection,
            heightCm = 83.0,
            weightKg = 11.0
        )
        
        println(service.formatAsMarkdown(proposal))
        println("\n" + "=".repeat(80) + "\n")
    }

    /**
     * 测试5：全选所有标准
     */
    fun testAllStandards() {
        println("=== 测试5：全选所有标准 ===")
        
        val selection = ChildRestraintDesignService.StandardSelection(
            eceR129 = true,
            gb27887 = true,
            fmvss213 = true,
            asNzs1754 = true,
            jisD1601 = true
        )
        
        val proposal = service.generateDesignProposal(
            selection = selection,
            heightCm = 50.0,
            weightKg = 1.5
        )
        
        println(service.formatAsMarkdown(proposal))
        println("\n" + "=".repeat(80) + "\n")
    }

    /**
     * 测试6：未选择任何标准
     */
    fun testNoSelection() {
        println("=== 测试6：未选择任何标准 ===")
        
        val selection = ChildRestraintDesignService.StandardSelection(
            eceR129 = false,
            gb27887 = false,
            fmvss213 = false,
            asNzs1754 = false,
            jisD1601 = false
        )
        
        val proposal = service.generateDesignProposal(
            selection = selection,
            heightCm = 100.0,
            weightKg = 15.0
        )
        
        println(service.formatAsMarkdown(proposal))
        println("\n" + "=".repeat(80) + "\n")
    }

    /**
     * 运行所有测试
     */
    fun runAllTests() {
        println("\n" + "#".repeat(80))
        println("# 儿童安全座椅设计服务测试")
        println("#".repeat(80) + "\n")
        
        testEceR129Only()
        testGB27887Only()
        testFmvss213Only()
        testMultiStandards()
        testAllStandards()
        testNoSelection()
        
        println("#".repeat(80))
        println("# 所有测试完成")
        println("#".repeat(80) + "\n")
    }
}

/**
 * 主函数 - 运行测试
 */
fun main() {
    val test = ChildRestraintDesignServiceTest()
    test.runAllTests()
}

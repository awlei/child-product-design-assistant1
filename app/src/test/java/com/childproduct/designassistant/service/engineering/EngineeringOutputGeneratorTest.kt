package com.childproduct.designassistant.service.engineering

import com.childproduct.designassistant.model.engineering.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat

/**
 * 工程输出生成器测试
 * 验证工程输出的完整性和准确性
 */
class EngineeringOutputGeneratorTest {
    
    private lateinit var generator: EngineeringOutputGenerator
    
    @Before
    fun setup() {
        generator = EngineeringOutputGenerator()
    }
    
    /**
     * 测试场景1：40-105cm身高范围，后向安装
     * 预期：Q0, Q0+, Q1, Q1.5，强制后向安装，使用Support Leg
     */
    @Test
    fun testScenario1_RearwardFacing_40to105cm() {
        // 1. 准备输入
        val input = EngineeringInput(
            productType = ProductType.CHILD_SEAT,
            standards = setOf(Standard.ECE_R129),
            heightRange = HeightRange(minCm = 40, maxCm = 105),
            installMethod = InstallMethod(
                type = InstallType.ISOFIX,
                direction = InstallDirection.REARWARD,
                antiRotation = AntiRotationType.SUPPORT_LEG
            ),
            designConstraints = null
        )
        
        // 2. 验证输入
        val validationResult = input.validate()
        assertTrue("输入应该有效", validationResult.isValid)
        assertEquals("应该有0个错误", 0, validationResult.errors.size)
        
        // 3. 生成输出
        val result = generator.generate(input, Standard.ECE_R129)
        assertTrue("生成应该成功", result.isSuccess)
        
        val output = result.getOrThrow()
        
        // 4. 验证假人类型
        val dummyTypes = input.getApplicableDummies()
        assertEquals("应该有4种假人类型", 4, dummyTypes.size)
        assertEquals("第1个假人应该是Q0", DummyType.Q0, dummyTypes[0])
        assertEquals("第2个假人应该是Q0+", DummyType.Q0_PLUS, dummyTypes[1])
        assertEquals("第3个假人应该是Q1", DummyType.Q1, dummyTypes[2])
        assertEquals("第4个假人应该是Q1.5", DummyType.Q1_5, dummyTypes[3])
        
        // 5. 验证安装方向
        assertEquals("Q0应该是后向安装", InstallDirection.REARWARD, 
            output.standardMapping.installDirections[DummyType.Q0])
        assertEquals("Q1.5应该是后向安装", InstallDirection.REARWARD, 
            output.standardMapping.installDirections[DummyType.Q1_5])
        
        // 6. 验证测试矩阵
        assertEquals("应该有4个测试用例", 4, output.testMatrix.testCases.size)
        
        val q0TestCase = output.testMatrix.testCases.find { it.dummyType == "Q0" }
        assertNotNull("应该有Q0的测试用例", q0TestCase)
        assertEquals("Q0应该标记为Frontal碰撞", "Frontal", q0TestCase?.impactType)
        assertEquals("Q0的假人类型应该是Q0", "Q0", q0TestCase?.dummyType) // 修正：Impact列填假人类型
        assertEquals("Q0不应该使用Top Tether", "NO", q0TestCase?.topTether)
        
        // 7. 验证ISOFIX Envelope
        assertNotNull("应该有ISOFIX Envelope", output.isofixEnvelope)
        assertEquals("Envelope类型应该是ISO/R2", "ISO/R2", 
            output.testMatrix.testCases.firstOrNull()?.isofixType)
        
        // 8. 验证安全阈值
        val thresholds = output.safetyThresholds.standard.getSafetyParameters()
        assertFalse("应该有头部伤害准则阈值", thresholds.headInjuryCriteria.isEmpty())
        assertFalse("应该有胸部加速度阈值", thresholds.chestAcceleration.isEmpty())
    }
    
    /**
     * 测试场景2：105-150cm身高范围，前向安装
     * 预期：Q3, Q3s, Q6, Q10，前向安装，必须使用Top-tether
     */
    @Test
    fun testScenario2_ForwardFacing_105to150cm() {
        // 1. 准备输入
        val input = EngineeringInput(
            productType = ProductType.CHILD_SEAT,
            standards = setOf(Standard.ECE_R129),
            heightRange = HeightRange(minCm = 105, maxCm = 150),
            installMethod = InstallMethod(
                type = InstallType.ISOFIX,
                direction = InstallDirection.FORWARD,
                antiRotation = AntiRotationType.TOP_TETHER
            ),
            designConstraints = null
        )
        
        // 2. 验证输入
        val validationResult = input.validate()
        assertTrue("输入应该有效", validationResult.isValid)
        
        // 3. 生成输出
        val result = generator.generate(input, Standard.ECE_R129)
        assertTrue("生成应该成功", result.isSuccess)
        
        val output = result.getOrThrow()
        
        // 4. 验证假人类型
        val dummyTypes = input.getApplicableDummies()
        assertEquals("应该有4种假人类型", 4, dummyTypes.size)
        assertEquals("第1个假人应该是Q3", DummyType.Q3, dummyTypes[0])
        assertEquals("第2个假人应该是Q3s", DummyType.Q3s, dummyTypes[1])
        assertEquals("第3个假人应该是Q6", DummyType.Q6, dummyTypes[2])
        assertEquals("第4个假人应该是Q10", DummyType.Q10, dummyTypes[3])
        
        // 5. 验证安装方向
        assertEquals("Q3应该是前向安装", InstallDirection.FORWARD, 
            output.standardMapping.installDirections[DummyType.Q3])
        assertEquals("Q10应该是前向安装", InstallDirection.FORWARD, 
            output.standardMapping.installDirections[DummyType.Q10])
        
        // 6. 验证测试矩阵
        val q6TestCase = output.testMatrix.testCases.find { it.dummyType == "Q6" }
        assertNotNull("应该有Q6的测试用例", q6TestCase)
        assertEquals("Q6应该使用Top Tether", "YES", q6TestCase?.topTether) // Column 18: 标记Top Tether测试
        
        // 7. 验证ISOFIX Envelope
        val f2xTestCase = output.testMatrix.testCases.find { it.dummyType == "Q10" }
        assertEquals("Q10的ISOFIX类型应该是ISO/F2X", "ISO/F2X", f2xTestCase?.isofixType)
    }
    
    /**
     * 测试场景3：40-150cm全范围，混合安装
     * 预期：8种假人类型，40-105cm后向，105-150cm前向
     */
    @Test
    fun testScenario3_MixedDirection_40to150cm() {
        // 1. 准备输入
        val input = EngineeringInput(
            productType = ProductType.CHILD_SEAT,
            standards = setOf(Standard.ECE_R129),
            heightRange = HeightRange(minCm = 40, maxCm = 150),
            installMethod = InstallMethod(
                type = InstallType.ISOFIX,
                direction = InstallDirection.FORWARD, // 用户选择前向安装
                antiRotation = AntiRotationType.TOP_TETHER
            ),
            designConstraints = null
        )
        
        // 2. 验证输入
        val validationResult = input.validate()
        assertTrue("输入应该有效", validationResult.isValid)
        
        // 3. 生成输出
        val result = generator.generate(input, Standard.ECE_R129)
        assertTrue("生成应该成功", result.isSuccess)
        
        val output = result.getOrThrow()
        
        // 4. 验证假人类型（应该有8种）
        val dummyTypes = input.getApplicableDummies()
        assertEquals("应该有8种假人类型", 8, dummyTypes.size)
        
        // 5. 验证安装方向（强制规则：40-105cm后向，105-150cm前向）
        assertEquals("Q0应该是后向安装（强制规则）", InstallDirection.REARWARD, 
            output.standardMapping.installDirections[DummyType.Q0])
        assertEquals("Q1.5应该是后向安装（强制规则）", InstallDirection.REARWARD, 
            output.standardMapping.installDirections[DummyType.Q1_5])
        assertEquals("Q3应该是前向安装（用户选择+强制规则）", InstallDirection.FORWARD, 
            output.standardMapping.installDirections[DummyType.Q3])
        assertEquals("Q10应该是前向安装（用户选择+强制规则）", InstallDirection.FORWARD, 
            output.standardMapping.installDirections[DummyType.Q10])
        
        // 6. 验证测试矩阵（应该有8个测试用例）
        assertEquals("应该有8个测试用例", 8, output.testMatrix.testCases.size)
        
        // 7. 验证警告（跨越多个假人类型）
        assertTrue("应该有警告信息", validationResult.warnings.isNotEmpty())
    }
    
    /**
     * 测试场景4：非法输入 - 40-105cm尝试前向安装
     * 预期：验证失败，提示强制后向安装规则
     */
    @Test
    fun testScenario4_InvalidInput_40to105cm_ForwardFacing() {
        // 1. 准备非法输入
        val input = EngineeringInput(
            productType = ProductType.CHILD_SEAT,
            standards = setOf(Standard.ECE_R129),
            heightRange = HeightRange(minCm = 40, maxCm = 105),
            installMethod = InstallMethod(
                type = InstallType.ISOFIX,
                direction = InstallDirection.FORWARD, // ❌ 非法：40-105cm禁止前向安装
                antiRotation = AntiRotationType.SUPPORT_LEG
            ),
            designConstraints = null
        )
        
        // 2. 验证输入
        val validationResult = input.validate()
        
        // 3. 预期验证失败
        assertFalse("输入应该无效", validationResult.isValid)
        assertEquals("应该有1个错误", 1, validationResult.errors.size)
        assertTrue("错误信息应该包含ECE R129 §5.1.3", 
            validationResult.errors[0].contains("ECE R129 §5.1.3"))
    }
    
    /**
     * 测试场景5：非法输入 - 105cm以上前向安装未使用Top-tether
     * 预期：验证失败，提示必须使用Top-tether
     */
    @Test
    fun testScenario5_InvalidInput_105plus_NoTopTether() {
        // 1. 准备非法输入
        val input = EngineeringInput(
            productType = ProductType.CHILD_SEAT,
            standards = setOf(Standard.ECE_R129),
            heightRange = HeightRange(minCm = 105, maxCm = 130),
            installMethod = InstallMethod(
                type = InstallType.ISOFIX,
                direction = InstallDirection.FORWARD,
                antiRotation = AntiRotationType.SUPPORT_LEG // ❌ 非法：必须使用Top-tether
            ),
            designConstraints = null
        )
        
        // 2. 验证输入
        val validationResult = input.validate()
        
        // 3. 预期验证失败
        assertFalse("输入应该无效", validationResult.isValid)
        assertEquals("应该有1个错误", 1, validationResult.errors.size)
        assertTrue("错误信息应该包含ECE R129 §6.1.2", 
            validationResult.errors[0].contains("ECE R129 §6.1.2"))
    }
    
    /**
     * 测试场景6：多标准支持
     * 预期：支持ECE R129, GB 27887, FMVSS 213多个标准
     */
    @Test
    fun testScenario6_MultipleStandards() {
        // 1. 准备输入
        val input = EngineeringInput(
            productType = ProductType.CHILD_SEAT,
            standards = setOf(
                Standard.ECE_R129,
                Standard.GB_27887_2024,
                Standard.FMVSS_213
            ),
            heightRange = HeightRange(minCm = 40, maxCm = 105),
            installMethod = InstallMethod(
                type = InstallType.ISOFIX,
                direction = InstallDirection.REARWARD,
                antiRotation = AntiRotationType.SUPPORT_LEG
            ),
            designConstraints = null
        )
        
        // 2. 验证输入
        val validationResult = input.validate()
        assertTrue("输入应该有效", validationResult.isValid)
        
        // 3. 生成输出
        val result = generator.generate(input, Standard.ECE_R129)
        assertTrue("生成应该成功", result.isSuccess)
        
        val output = result.getOrThrow()
        
        // 4. 验证元数据中的标准
        assertEquals("元数据应该包含3个标准", 3, output.metadata.standards.size)
        assertTrue("应该包含ECE_R129", output.metadata.standards.contains("ECE_R129"))
        assertTrue("应该包含GB_27887_2024", output.metadata.standards.contains("GB_27887_2024"))
        assertTrue("应该包含FMVSS_213", output.metadata.standards.contains("FMVSS_213"))
        
        // 5. 验证合规声明
        assertEquals("合规声明应该包含3个标准", 3, output.complianceStatement.standards.size)
    }
    
    /**
     * 测试场景7：输出格式验证
     * 预期：Markdown和CSV格式输出应该正确
     */
    @Test
    fun testScenario7_OutputFormats() {
        // 1. 准备输入
        val input = EngineeringInput(
            productType = ProductType.CHILD_SEAT,
            standards = setOf(Standard.ECE_R129),
            heightRange = HeightRange(minCm = 40, maxCm = 105),
            installMethod = InstallMethod(
                type = InstallType.ISOFIX,
                direction = InstallDirection.REARWARD,
                antiRotation = AntiRotationType.SUPPORT_LEG
            ),
            designConstraints = null
        )
        
        // 2. 生成输出
        val result = generator.generate(input, Standard.ECE_R129)
        assertTrue("生成应该成功", result.isSuccess)
        
        val output = result.getOrThrow()
        
        // 3. 验证Markdown格式
        val markdown = output.toMarkdown()
        assertNotNull("Markdown输出不应该为空", markdown)
        assertTrue("Markdown应该包含元数据", markdown.contains("## 📋 元数据"))
        assertTrue("Markdown应该包含基本信息", markdown.contains("## 【基本信息】"))
        assertTrue("Markdown应该包含标准映射", markdown.contains("## 【标准映射】"))
        assertTrue("Markdown应该包含测试矩阵", markdown.contains("## 【测试矩阵】"))
        assertTrue("Markdown应该包含安全阈值", markdown.contains("## 【安全阈值】"))
        assertTrue("Markdown应该包含版本信息", markdown.contains("## 标准版本信息"))
        
        // 4. 验证CSV格式
        val csv = output.toCsv()
        assertNotNull("CSV输出不应该为空", csv)
        assertTrue("CSV应该包含20列", csv.split("\n")[0].split(",").size == 20)
        assertTrue("CSV应该包含Test ID列", csv.contains("Test ID"))
        assertTrue("CSV应该包含Dummy Type列", csv.contains("Dummy Type"))
        assertTrue("CSV应该包含Top Tether列", csv.contains("Top Tether"))
    }
}

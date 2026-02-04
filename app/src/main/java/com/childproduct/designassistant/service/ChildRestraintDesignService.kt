package com.childproduct.designassistant.service

import com.childproduct.designassistant.data.*
import com.childproduct.designassistant.data.GPS028Database
import com.childproduct.designassistant.data.model.StandardBasicInfo

/**
 * 儿童安全座椅标准适配设计服务
 * 
 * 功能：
 * 1. 根据用户选择的标准调用相应的数据库
 * 2. 严格按照用户选择生成输出
 * 3. 支持多标准选择
 */
class ChildRestraintDesignService {

    /**
     * 标准选择数据类
     */
    data class StandardSelection(
        val eceR129: Boolean = false,      // ECE R129 (欧盟i-Size)
        val gb27887: Boolean = false,     // GB 28007-2024 (中国新标)
        val fmvss213: Boolean = false,    // FMVSS 213 (美国标准)
        val asNzs1754: Boolean = false    // AS/NZS 1754 (澳洲标准)
    ) {
        fun hasAnySelection(): Boolean = eceR129 || gb27887 || fmvss213 || asNzs1754
        
        fun getSelectedStandards(): List<String> {
            val list = mutableListOf<String>()
            if (eceR129) list.add("ECE R129")
            if (gb27887) list.add("GB 28007-2024")
            if (fmvss213) list.add("FMVSS 213")
            if (asNzs1754) list.add("AS/NZS 1754")
            return list
        }
    }

    /**
     * 设计方案输出
     */
    data class DesignProposal(
        val applicableStandards: List<String>,
        val dummyData: DummyDataSection,
        val designParameters: DesignParametersSection,
        val testRequirements: TestRequirementsSection,
        val standardTestItems: StandardTestItemsSection
    )

    /**
     * 假人数据部分
     */
    data class DummyDataSection(
        val heightRange: String,
        val weightRange: String,
        val installationDirection: String
    )

    /**
     * 设计参数部分
     */
    data class DesignParametersSection(
        val headRestHeight: String?,
        val seatWidth: String?,
        val envelope: String?,
        val sideImpactArea: String?
    )

    /**
     * 测试要求部分
     */
    data class TestRequirementsSection(
        val frontal: String?,
        val sideChestCompression: String?,
        val webbingStrength: String?
    )

    /**
     * 标准测试项部分
     */
    data class StandardTestItemsSection(
        val dynamicFrontal: String?,
        val dynamicRear: String?,
        val dynamicSide: String?,
        val flammability: String?
    )

    /**
     * 生成设计方案
     * 
     * @param selection 用户选择的标准
     * @param heightCm 用户输入的身高（cm）
     * @param weightKg 用户输入的体重（kg）
     * @return 设计方案
     */
    fun generateDesignProposal(
        selection: StandardSelection,
        heightCm: Double = 0.0,
        weightKg: Double = 0.0
    ): DesignProposal {
        if (!selection.hasAnySelection()) {
            return DesignProposal(
                applicableStandards = emptyList(),
                dummyData = DummyDataSection("未选择标准", "未选择标准", "未选择标准"),
                designParameters = DesignParametersSection(null, null, null, null),
                testRequirements = TestRequirementsSection(null, null, null),
                standardTestItems = StandardTestItemsSection(null, null, null, null)
            )
        }

        // 获取适用的标准标签
        val applicableStandards = selection.getSelectedStandards()

        // 生成假人数据
        val dummyData = generateDummyData(selection, heightCm, weightKg)

        // 生成设计参数
        val designParameters = generateDesignParameters(selection)

        // 生成测试要求
        val testRequirements = generateTestRequirements(selection)

        // 生成标准测试项
        val standardTestItems = generateStandardTestItems(selection)

        return DesignProposal(
            applicableStandards = applicableStandards,
            dummyData = dummyData,
            designParameters = designParameters,
            testRequirements = testRequirements,
            standardTestItems = standardTestItems
        )
    }

    /**
     * 生成假人数据
     */
    private fun generateDummyData(
        selection: StandardSelection,
        heightCm: Double,
        weightKg: Double
    ): DummyDataSection {
        val heightRanges = mutableListOf<String>()
        val weightRanges = mutableListOf<String>()
        val directions = mutableListOf<String>()

        // ECE R129 (欧标)
        if (selection.eceR129) {
            val eceInfo = EceR129StandardDatabase.getStandardInfo()
            heightRanges.add("ECE R129: ${eceInfo.applicableAge}（基于身高分组）")
            weightRanges.add("ECE R129: ${eceInfo.applicableWeight}")
            
            // 根据输入的身高/体重查找适用的假人
            val applicableDummies = if (heightCm > 0) {
                EceR129StandardDatabase.getDummiesByHeightRange(heightCm)?.map { it.displayName }
            } else if (weightKg > 0) {
                EceR129StandardDatabase.getDummiesByWeightRange(weightKg)?.map { it.displayName }
            } else {
                null
            }
            
            val dummySpecs = EceR129StandardDatabase.getAllDummySpecs()
            val eceDirections = dummySpecs.flatMap { it.installationDirection }.distinct()
            directions.add("ECE R129: ${eceDirections.joinToString(" / ")}")
        }

        // GB 28007-2024 (国标) - 使用相同的欧标数据库
        if (selection.gb27887) {
            heightRanges.add("GB 28007-2024: 新生儿-36kg（12岁以下）")
            weightRanges.add("GB 28007-2024: 0-36kg")
            directions.add("GB 28007-2024: 反向、前向、增高垫")
        }

        // FMVSS 213 (美标)
        if (selection.fmvss213) {
            heightRanges.add("FMVSS 213: 基于体重分组")
            weightRanges.add("FMVSS 213: 0-45kg")
            directions.add("FMVSS 213: 反向、前向、腰带模式、增高垫")
        }

        // AS/NZS 1754 (澳标)
        if (selection.asNzs1754) {
            heightRanges.add("AS/NZS 1754: 基于体重分组")
            weightRanges.add("AS/NZS 1754: 0-32kg")
            directions.add("AS/NZS 1754: 反向、前向、增高垫")
        }

        return DummyDataSection(
            heightRange = heightRanges.joinToString("\n"),
            weightRange = weightRanges.joinToString("\n"),
            installationDirection = directions.joinToString("\n")
        )
    }

    /**
     * 生成设计参数（从GPS数据库）
     */
    private fun generateDesignParameters(selection: StandardSelection): DesignParametersSection {
        val headRestHeight = mutableListOf<String>()
        val seatWidth = mutableListOf<String>()
        val envelope = mutableListOf<String>()
        val sideImpactArea = mutableListOf<String>()

        // ECE R129 (欧标)
        if (selection.eceR129) {
            headRestHeight.add("ECE R129: 参考GPS-028 Q系列假人数据")
            seatWidth.add("ECE R129: ISOFIX SIZE CLASS (B1, B2, D, E)")
            envelope.add("ECE R129: External Envelope (基于ISO-FIX)")
            sideImpactArea.add("ECE R129: 侧面碰撞防护区域")
        }

        // GB 28007-2024 (国标)
        if (selection.gb27887) {
            headRestHeight.add("GB 28007-2024: 参考欧标要求")
            seatWidth.add("GB 28007-2024: 400-500mm（根据年龄分组）")
            envelope.add("GB 28007-2024: 外形尺寸限制")
            sideImpactArea.add("GB 28007-2024: 侧碰参考欧标")
        }

        // FMVSS 213 (美标)
        if (selection.fmvss213) {
            headRestHeight.add("FMVSS 213: 参考GPS-028 Q系列和CRABI系列假人")
            seatWidth.add("FMVSS 213: 380-460mm（根据体重分组）")
            envelope.add("FMVSS 213: Vehicle Envelope Requirements")
            sideImpactArea.add("FMVSS 213: 213a侧碰测试区域")
        }

        // AS/NZS 1754 (澳标)
        if (selection.asNzs1754) {
            headRestHeight.add("AS/NZS 1754: 参考欧标要求")
            seatWidth.add("AS/NZS 1754: 380-480mm")
            envelope.add("AS/NZS 1754: 外形尺寸要求")
            sideImpactArea.add("AS/NZS 1754: 侧碰要求")
        }

        return DesignParametersSection(
            headRestHeight = if (headRestHeight.isNotEmpty()) headRestHeight.joinToString("\n") else null,
            seatWidth = if (seatWidth.isNotEmpty()) seatWidth.joinToString("\n") else null,
            envelope = if (envelope.isNotEmpty()) envelope.joinToString("\n") else null,
            sideImpactArea = if (sideImpactArea.isNotEmpty()) sideImpactArea.joinToString("\n") else null
        )
    }

    /**
     * 生成测试要求
     */
    private fun generateTestRequirements(selection: StandardSelection): TestRequirementsSection {
        val frontal = mutableListOf<String>()
        val sideChestCompression = mutableListOf<String>()
        val webbingStrength = mutableListOf<String>()

        // ECE R129 (欧标)
        if (selection.eceR129) {
            frontal.add("ECE R129: 50km/h ±2km/h 正碰")
            sideChestCompression.add("ECE R129: 侧碰胸部压缩量 ≤ 35mm")
            webbingStrength.add("ECE R129: 织带最小断裂强度 4.5kN")
        }

        // GB 28007-2024 (国标)
        if (selection.gb27887) {
            frontal.add("GB 28007-2024: 50km/h 正碰")
            sideChestCompression.add("GB 28007-2024: 参考欧标侧碰要求")
            webbingStrength.add("GB 28007-2024: 织带最小断裂强度 4.5kN")
        }

        // FMVSS 213 (美标)
        if (selection.fmvss213) {
            frontal.add("FMVSS 213: 30mph (48km/h) 正碰")
            sideChestCompression.add("FMVSS 213a: 侧碰胸部加速度限制")
            webbingStrength.add("FMVSS 213: 织带最小断裂强度 11kN (约束儿童) / 15kN (固定车辆)")
        }

        // AS/NZS 1754 (澳标)
        if (selection.asNzs1754) {
            frontal.add("AS/NZS 1754: 48km/h 正碰")
            sideChestCompression.add("AS/NZS 1754: 侧碰胸部压缩限制")
            webbingStrength.add("AS/NZS 1754: 织带最小断裂强度 4.5kN")
        }

        return TestRequirementsSection(
            frontal = if (frontal.isNotEmpty()) frontal.joinToString("\n") else null,
            sideChestCompression = if (sideChestCompression.isNotEmpty()) sideChestCompression.joinToString("\n") else null,
            webbingStrength = if (webbingStrength.isNotEmpty()) webbingStrength.joinToString("\n") else null
        )
    }

    /**
     * 生成标准测试项
     */
    private fun generateStandardTestItems(selection: StandardSelection): StandardTestItemsSection {
        val dynamicFrontal = mutableListOf<String>()
        val dynamicRear = mutableListOf<String>()
        val dynamicSide = mutableListOf<String>()
        val flammability = mutableListOf<String>()

        // ECE R129 (欧标)
        if (selection.eceR129) {
            dynamicFrontal.add("ECE R129: 50km/h 正碰 + 脉冲波形")
            dynamicRear.add("ECE R129: 无强制后碰测试要求")
            dynamicSide.add("ECE R129: 24km/h 侧碰 + Q系列假人")
            flammability.add("ECE R129: UN R118.03 阻燃要求（水平燃烧速度 ≤ 100mm/min）")
        }

        // GB 28007-2024 (国标)
        if (selection.gb27887) {
            dynamicFrontal.add("GB 28007-2024: 50km/h 正碰测试")
            dynamicRear.add("GB 28007-2024: 无强制后碰测试要求")
            dynamicSide.add("GB 28007-2024: 侧碰测试（参考欧标）")
            flammability.add("GB 28007-2024: GB 8410 阻燃要求")
        }

        // FMVSS 213 (美标)
        if (selection.fmvss213) {
            dynamicFrontal.add("FMVSS 213: 30mph (48km/h) 正碰 + Hyge 滑台")
            dynamicRear.add("FMVSS 213: 30mph 后碰测试")
            dynamicSide.add("FMVSS 213a: 侧碰测试 + Q3s假人")
            flammability.add("FMVSS 213: FMVSS 302 阻燃要求（燃烧速度 ≤ 4英寸/分钟）")
        }

        // AS/NZS 1754 (澳标)
        if (selection.asNzs1754) {
            dynamicFrontal.add("AS/NZS 1754: 48km/h 正碰测试")
            dynamicRear.add("AS/NZS 1754: 无强制后碰测试要求")
            dynamicSide.add("AS/NZS 1754: 侧碰测试")
            flammability.add("AS/NZS 1754: AS 1530.3 阻燃要求")
        }

        return StandardTestItemsSection(
            dynamicFrontal = if (dynamicFrontal.isNotEmpty()) dynamicFrontal.joinToString("\n") else null,
            dynamicRear = if (dynamicRear.isNotEmpty()) dynamicRear.joinToString("\n") else null,
            dynamicSide = if (dynamicSide.isNotEmpty()) dynamicSide.joinToString("\n") else null,
            flammability = if (flammability.isNotEmpty()) flammability.joinToString("\n") else null
        )
    }

    /**
     * 格式化输出为Markdown
     */
    fun formatAsMarkdown(proposal: DesignProposal): String {
        return buildString {
            appendLine("## 📦 儿童安全座椅设计方案")
            appendLine()
            
            // 适用标准
            appendLine("### 【适用标准】")
            proposal.applicableStandards.forEach { standard ->
                appendLine("🔵 $standard")
            }
            appendLine()
            
            // 基础适配数据
            appendLine("### 📊 基础适配数据")
            appendLine()
            appendLine("#### 🔽 假人")
            appendLine("- **身高范围**：\n${proposal.dummyData.heightRange}")
            appendLine("- **体重范围**：\n${proposal.dummyData.weightRange}")
            appendLine("- **安装方向**：\n${proposal.dummyData.installationDirection}")
            appendLine()
            
            // 设计参数
            appendLine("### 📏 设计参数")
            proposal.designParameters.headRestHeight?.let {
                appendLine("- **头枕高度**：\n$it")
            }
            proposal.designParameters.seatWidth?.let {
                appendLine("- **座宽**：\n$it")
            }
            proposal.designParameters.envelope?.let {
                appendLine("- **盒子 Envelope**：\n$it")
            }
            proposal.designParameters.sideImpactArea?.let {
                appendLine("- **侧防面积**：\n$it")
            }
            appendLine()
            
            // 测试要求
            appendLine("### ⚖️ 测试要求")
            proposal.testRequirements.frontal?.let {
                appendLine("- **正面**：\n$it")
            }
            proposal.testRequirements.sideChestCompression?.let {
                appendLine("- **侧撞胸部压缩**：\n$it")
            }
            proposal.testRequirements.webbingStrength?.let {
                appendLine("- **织带强度**：\n$it")
            }
            appendLine()
            
            // 标准测试项
            appendLine("### 🧪 标准测试项")
            proposal.standardTestItems.dynamicFrontal?.let {
                appendLine("- **动态碰撞：正碰**：\n$it")
            }
            proposal.standardTestItems.dynamicRear?.let {
                appendLine("- **动态碰撞：后碰**：\n$it")
            }
            proposal.standardTestItems.dynamicSide?.let {
                appendLine("- **动态碰撞：侧碰**：\n$it")
            }
            proposal.standardTestItems.flammability?.let {
                appendLine("- **阻燃**：\n$it")
            }
        }
    }
}

/**
 * 简单测试函数 - 用于快速验证服务功能
 */
fun testService() {
    val service = ChildRestraintDesignService()
    
    println("\n" + "=".repeat(80))
    println("测试1：仅选择 ECE R129")
    println("=".repeat(80) + "\n")
    
    val selection1 = ChildRestraintDesignService.StandardSelection(
        eceR129 = true,
        gb27887 = false,
        fmvss213 = false,
        asNzs1754 = false
    )
    
    val proposal1 = service.generateDesignProposal(
        selection = selection1,
        heightCm = 100.0,
        weightKg = 15.0
    )
    
    println(service.formatAsMarkdown(proposal1))
    
    println("\n" + "=".repeat(80))
    println("测试2：选择 ECE R129 + GB 28007")
    println("=".repeat(80) + "\n")
    
    val selection2 = ChildRestraintDesignService.StandardSelection(
        eceR129 = true,
        gb27887 = true,
        fmvss213 = false,
        asNzs1754 = false
    )
    
    val proposal2 = service.generateDesignProposal(
        selection = selection2,
        heightCm = 83.0,
        weightKg = 11.0
    )
    
    println(service.formatAsMarkdown(proposal2))
    
    println("\n" + "=".repeat(80))
    println("测试3：选择 FMVSS 213")
    println("=".repeat(80) + "\n")
    
    val selection3 = ChildRestraintDesignService.StandardSelection(
        eceR129 = false,
        gb27887 = false,
        fmvss213 = true,
        asNzs1754 = false
    )
    
    val proposal3 = service.generateDesignProposal(
        selection = selection3,
        heightCm = 125.0,
        weightKg = 22.0
    )
    
    println(service.formatAsMarkdown(proposal3))
    
    println("\n" + "=".repeat(80))
    println("测试完成")
    println("=".repeat(80) + "\n")
}

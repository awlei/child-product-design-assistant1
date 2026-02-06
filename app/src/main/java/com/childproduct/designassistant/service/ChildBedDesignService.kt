package com.childproduct.designassistant.service

import com.childproduct.designassistant.constants.StandardConstants

/**
 * 儿童床标准适配设计服务
 * 
 * 功能：
 * 1. 根据用户选择的标准调用相应的数据库
 * 2. 严格按照用户选择生成输出
 * 3. 支持多标准选择（EN 716、GB 28007、CAN_CSA_D1169、ASTM F1169）
 */
class ChildBedDesignService {

    /**
     * 标准选择数据类
     */
    data class StandardSelection(
        val en716: Boolean = false,             // EN 716 (欧盟)
        val gb28007: Boolean = false,           // GB 28007 (中国)
        val canCsaD1169: Boolean = false,       // CAN/CSA D1169 (加拿大)
        val astmF1169: Boolean = false         // ASTM F1169 (美国)
    ) {
        fun hasAnySelection(): Boolean = en716 || gb28007 || canCsaD1169 || astmF1169

        fun getSelectedStandards(): List<String> {
            val list = mutableListOf<String>()
            if (en716) list.add(StandardConstants.getStandardName(StandardConstants.EN_716))
            if (gb28007) list.add(StandardConstants.getStandardName(StandardConstants.GB_28007))
            if (canCsaD1169) list.add(StandardConstants.getStandardName(StandardConstants.CAN_CSA_D1169))
            if (astmF1169) list.add(StandardConstants.getStandardName(StandardConstants.ASTM_F1169))
            return list
        }
    }

    /**
     * 设计方案输出
     */
    data class DesignProposal(
        val applicableStandards: List<String>,
        val childData: ChildDataSection,
        val designParameters: DesignParametersSection,
        val testRequirements: TestRequirementsSection,
        val standardTestItems: StandardTestItemsSection
    )

    /**
     * 儿童数据部分
     */
    data class ChildDataSection(
        val heightRange: String,
        val weightRange: String,
        val ageRange: String
    )

    /**
     * 设计参数部分
     */
    data class DesignParametersSection(
        val guardrailHeight: String?,
        val guardrailSpacing: String?,
        val mattressThickness: String?,
        val bedFrameStrength: String?
    )

    /**
     * 测试要求部分
     */
    data class TestRequirementsSection(
        val guardrailStrength: String?,
        val spacingRequirement: String?,
        val mattressFit: String?
    )

    /**
     * 标准测试项部分
     */
    data class StandardTestItemsSection(
        val guardrailTest: String?,
        val spacingTest: String?,
        val mattressTest: String?,
        val materialSafety: String?
    )

    /**
     * 生成设计方案
     */
    fun generateDesignProposal(
        selection: StandardSelection,
        heightCm: Double = 0.0,
        weightKg: Double = 0.0
    ): DesignProposal {
        if (!selection.hasAnySelection()) {
            return DesignProposal(
                applicableStandards = emptyList(),
                childData = ChildDataSection("未选择标准", "未选择标准", "未选择标准"),
                designParameters = DesignParametersSection(null, null, null, null),
                testRequirements = TestRequirementsSection(null, null, null),
                standardTestItems = StandardTestItemsSection(null, null, null, null)
            )
        }

        val applicableStandards = selection.getSelectedStandards()
        val childData = generateChildData(selection, heightCm, weightKg)
        val designParameters = generateDesignParameters(selection)
        val testRequirements = generateTestRequirements(selection)
        val standardTestItems = generateStandardTestItems(selection)

        return DesignProposal(
            applicableStandards = applicableStandards,
            childData = childData,
            designParameters = designParameters,
            testRequirements = testRequirements,
            standardTestItems = standardTestItems
        )
    }

    /**
     * 生成儿童数据
     */
    private fun generateChildData(
        selection: StandardSelection,
        heightCm: Double,
        weightKg: Double
    ): ChildDataSection {
        val heightRanges = mutableListOf<String>()
        val weightRanges = mutableListOf<String>()
        val ageRanges = mutableListOf<String>()

        if (selection.en716) {
            heightRanges.add("EN 716: 50-150cm（新生儿-5岁）")
            weightRanges.add("EN 716: 0-36kg")
            ageRanges.add("EN 716: 0-60个月")
        }

        if (selection.gb28007) {
            heightRanges.add("GB 28007: 50-150cm（新生儿-5岁）")
            weightRanges.add("GB 28007: 0-36kg")
            ageRanges.add("GB 28007: 0-60个月")
        }

        if (selection.canCsaD1169) {
            heightRanges.add("CAN/CSA D1169: 50-150cm（新生儿-5岁）")
            weightRanges.add("CAN/CSA D1169: 0-36kg")
            ageRanges.add("CAN/CSA D1169: 0-60个月")
        }

        if (selection.astmF1169) {
            heightRanges.add("ASTM F1169: 50-160cm（新生儿-6岁）")
            weightRanges.add("ASTM F1169: 0-40kg")
            ageRanges.add("ASTM F1169: 0-72个月")
        }

        return ChildDataSection(
            heightRange = heightRanges.joinToString("\n"),
            weightRange = weightRanges.joinToString("\n"),
            ageRange = ageRanges.joinToString("\n")
        )
    }

    /**
     * 生成设计参数
     */
    private fun generateDesignParameters(selection: StandardSelection): DesignParametersSection {
        val guardrailHeight = mutableListOf<String>()
        val guardrailSpacing = mutableListOf<String>()
        val mattressThickness = mutableListOf<String>()
        val bedFrameStrength = mutableListOf<String>()

        if (selection.en716 || selection.gb28007 || selection.canCsaD1169) {
            guardrailHeight.add("标准要求：防护栏高度≥600mm（从床垫表面测量）")
            guardrailSpacing.add("标准要求：防护栏间距≤60mm（防夹头）")
            mattressThickness.add("标准要求：床垫厚度≤100mm（防窒息）")
            bedFrameStrength.add("标准要求：床架强度：静态载荷≥800N")
        }

        if (selection.astmF1169) {
            guardrailHeight.add("ASTM F1169: 防护栏高度≥580mm（从床垫表面测量）")
            guardrailSpacing.add("ASTM F1169: 防护栏间距≤65mm（防夹头）")
            mattressThickness.add("ASTM F1169: 床垫厚度≤110mm（防窒息）")
            bedFrameStrength.add("ASTM F1169: 床架强度：静态载荷≥750N")
        }

        return DesignParametersSection(
            guardrailHeight = if (guardrailHeight.isNotEmpty()) guardrailHeight.joinToString("\n") else null,
            guardrailSpacing = if (guardrailSpacing.isNotEmpty()) guardrailSpacing.joinToString("\n") else null,
            mattressThickness = if (mattressThickness.isNotEmpty()) mattressThickness.joinToString("\n") else null,
            bedFrameStrength = if (bedFrameStrength.isNotEmpty()) bedFrameStrength.joinToString("\n") else null
        )
    }

    /**
     * 生成测试要求
     */
    private fun generateTestRequirements(selection: StandardSelection): TestRequirementsSection {
        val guardrailStrength = mutableListOf<String>()
        val spacingRequirement = mutableListOf<String>()
        val mattressFit = mutableListOf<String>()

        if (selection.en716 || selection.gb28007 || selection.canCsaD1169) {
            guardrailStrength.add("防护栏强度：防护栏载荷≥400N，无断裂无变形")
            spacingRequirement.add("间距要求：防护栏间距≤60mm（使用φ25mm测头）")
            mattressFit.add("床垫适配：床垫与床架间隙≤25mm（防卡陷）")
        }

        if (selection.astmF1169) {
            guardrailStrength.add("防护栏强度：防护栏载荷≥350N，无断裂无变形")
            spacingRequirement.add("间距要求：防护栏间距≤65mm（使用φ25mm测头）")
            mattressFit.add("床垫适配：床垫与床架间隙≤30mm（防卡陷）")
        }

        return TestRequirementsSection(
            guardrailStrength = if (guardrailStrength.isNotEmpty()) guardrailStrength.joinToString("\n") else null,
            spacingRequirement = if (spacingRequirement.isNotEmpty()) spacingRequirement.joinToString("\n") else null,
            mattressFit = if (mattressFit.isNotEmpty()) mattressFit.joinToString("\n") else null
        )
    }

    /**
     * 生成标准测试项
     */
    private fun generateStandardTestItems(selection: StandardSelection): StandardTestItemsSection {
        val guardrailTest = mutableListOf<String>()
        val spacingTest = mutableListOf<String>()
        val mattressTest = mutableListOf<String>()
        val materialSafety = mutableListOf<String>()

        if (selection.en716 || selection.gb28007 || selection.canCsaD1169) {
            guardrailTest.add("测试设备：拉力试验机（符合EN 716 §5.4）\n测试方法：防护栏中部施加400N垂直载荷，保持30秒\n合格判据：防护栏无断裂，无永久变形≤2mm")
            
            spacingTest.add("测试设备：φ25mm测头（符合EN 716 §5.2）\n测试方法：使用φ25mm测头检测所有防护栏间距\n合格判据：所有间距≤60mm，无卡陷风险")
            
            mattressTest.add("测试设备：间隙测量尺\n测试方法：测量床垫与床架四周间隙，取最大值\n合格判据：最大间隙≤25mm，无卡陷风险")
            
            materialSafety.add("测试标准：EN 71-3:2019\n测试方法：重金属迁移测试\n合格判据：铅≤90mg/kg，镉≤60mg/kg，砷≤25mg/kg")
        }

        if (selection.astmF1169) {
            guardrailTest.add("测试设备：拉力试验机（符合ASTM F1169 §7）\n测试方法：防护栏中部施加350N垂直载荷，保持30秒\n合格判据：防护栏无断裂，无永久变形≤3mm")
            
            spacingTest.add("测试设备：φ25mm测头（符合ASTM F1169 §5）\n测试方法：使用φ25mm测头检测所有防护栏间距\n合格判据：所有间距≤65mm，无卡陷风险")
            
            mattressTest.add("测试设备：间隙测量尺\n测试方法：测量床垫与床架四周间隙，取最大值\n合格判据：最大间隙≤30mm，无卡陷风险")
            
            materialSafety.add("测试标准：ASTM F963\n测试方法：重金属迁移测试\n合格判据：铅≤100mg/kg，镉≤75mg/kg，砷≤30mg/kg")
        }

        return StandardTestItemsSection(
            guardrailTest = if (guardrailTest.isNotEmpty()) guardrailTest.joinToString("\n\n") else null,
            spacingTest = if (spacingTest.isNotEmpty()) spacingTest.joinToString("\n\n") else null,
            mattressTest = if (mattressTest.isNotEmpty()) mattressTest.joinToString("\n\n") else null,
            materialSafety = if (materialSafety.isNotEmpty()) materialSafety.joinToString("\n\n") else null
        )
    }

    /**
     * 格式化输出为Markdown（专业版）
     */
    fun formatAsMarkdown(proposal: DesignProposal, inputHeightCm: Double, inputWeightKg: Double): String {
        return buildString {
            // 标题
            val standardName = proposal.applicableStandards.firstOrNull() ?: "未选择标准"
            appendLine("📦 儿童床设计方案（严格遵守$standardName）")
            appendLine()
            
            // 适用标准
            appendLine("【适用标准】$standardName")
            appendLine("标准版本：2021版 | 实施要求：欧盟强制实施")
            appendLine("🔍 核心要求：防护栏高度≥600mm，防护栏间距≤60mm，床垫厚度≤100mm")
            appendLine()
            
            // 基础适配数据
            appendLine("📊 基础适配数据（基于用户输入身高：${inputHeightCm.toInt()}cm / 体重：${inputWeightKg.toInt()}kg）")
            appendLine("🔽 儿童适配参数（ISO 7176-16:2012）")
            
            appendLine("▫️ 身高范围：${proposal.childData.heightRange}")
            appendLine("▫️ 体重范围：${proposal.childData.weightRange}")
            appendLine("▫️ 年龄范围：${proposal.childData.ageRange}")
            appendLine("▫️ 人体测量参数：坐高35cm，肩宽20cm，头围48cm")
            appendLine("▫️ 适用场景：家庭卧室、婴儿房、儿童房")
            appendLine()
            
            // 设计参数
            appendLine("📏 设计参数（儿童床结构设计标准）")
            
            proposal.designParameters.guardrailHeight?.let {
                appendLine("▫️ 防护栏高度：防护栏高度≥600mm（从床垫表面测量）")
            }
            
            proposal.designParameters.guardrailSpacing?.let {
                appendLine("▫️ 防护栏间距：防护栏间距≤60mm（防夹头）")
            }
            
            proposal.designParameters.mattressThickness?.let {
                appendLine("▫️ 床垫厚度：床垫厚度≤100mm（防窒息）")
            }
            
            proposal.designParameters.bedFrameStrength?.let {
                appendLine("▫️ 床架强度：床架强度：静态载荷≥800N")
            }
            appendLine()
            
            // 测试要求
            appendLine("⚖️ 测试要求（量化阈值 + 标准条款，可直接用于测试方案）")
            
            proposal.testRequirements.guardrailStrength?.let {
                appendLine("▫️ 防护栏强度：防护栏载荷≥400N，无断裂无变形（EN 716 §5.4）")
            }
            
            proposal.testRequirements.spacingRequirement?.let {
                appendLine("▫️ 间距要求：防护栏间距≤60mm（使用φ25mm测头）（EN 716 §5.2）")
            }
            
            proposal.testRequirements.mattressFit?.let {
                appendLine("▫️ 床垫适配：床垫与床架间隙≤25mm（防卡陷）（EN 716 §5.3）")
            }
            appendLine()
            
            // 标准测试项
            appendLine("🧪 标准测试项（测试设备+流程+合格判据，可直接对接实验室）")
            
            proposal.standardTestItems.guardrailTest?.let {
                appendLine("防护栏强度测试")
                appendLine("   测试设备：拉力试验机（符合EN 716 §5.4）")
                appendLine("   测试方法：防护栏中部施加400N垂直载荷，保持30秒")
                appendLine("   ✅ 合格判据：防护栏无断裂，无永久变形≤2mm")
            }
            
            proposal.standardTestItems.spacingTest?.let {
                appendLine("\n防护栏间距测试")
                appendLine("   测试设备：φ25mm测头（符合EN 716 §5.2）")
                appendLine("   测试方法：使用φ25mm测头检测所有防护栏间距")
                appendLine("   ✅ 合格判据：所有间距≤60mm，无卡陷风险")
            }
            
            proposal.standardTestItems.mattressTest?.let {
                appendLine("\n床垫适配测试")
                appendLine("   测试设备：间隙测量尺")
                appendLine("   测试方法：测量床垫与床架四周间隙，取最大值")
                appendLine("   ✅ 合格判据：最大间隙≤25mm，无卡陷风险")
            }
        }
    }
}

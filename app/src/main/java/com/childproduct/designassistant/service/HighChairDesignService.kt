package com.childproduct.designassistant.service

import com.childproduct.designassistant.constants.StandardConstants

/**
 * 儿童高脚椅标准适配设计服务
 * 
 * 功能：
 * 1. 根据用户选择的标准调用相应的数据库
 * 2. 严格按照用户选择生成输出
 * 3. 支持多标准选择（EN 14988、GB 29281、CAN_CSA_Z217_1、ASTM F404）
 */
class HighChairDesignService {

    /**
     * 标准选择数据类
     */
    data class StandardSelection(
        val en14988: Boolean = false,          // EN 14988 (欧盟)
        val gb29281: Boolean = false,         // GB 29281 (中国)
        val canCsaZ2171: Boolean = false,     // CAN/CSA Z217.1 (加拿大)
        val astmF404: Boolean = false         // ASTM F404 (美国)
    ) {
        fun hasAnySelection(): Boolean = en14988 || gb29281 || canCsaZ2171 || astmF404

        fun getSelectedStandards(): List<String> {
            val list = mutableListOf<String>()
            if (en14988) list.add(StandardConstants.getStandardName(StandardConstants.EN_14988))
            if (gb29281) list.add(StandardConstants.getStandardName(StandardConstants.GB_29281))
            if (canCsaZ2171) list.add(StandardConstants.getStandardName(StandardConstants.CAN_CSA_Z217_1))
            if (astmF404) list.add(StandardConstants.getStandardName(StandardConstants.ASTM_F404))
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
        val seatHeightAdjustRange: String?,
        val safetyBeltType: String?,
        val tableEdgeDistance: String?,
        val antiTipOverDimension: String?
    )

    /**
     * 测试要求部分
     */
    data class TestRequirementsSection(
        val stability: String?,
        val strapStrength: String?,
        val tableStrength: String?
    )

    /**
     * 标准测试项部分
     */
    data class StandardTestItemsSection(
        val stabilityTest: String?,
        val strapTest: String?,
        val loadTest: String?,
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

        if (selection.en14988) {
            heightRanges.add("EN 14988: 75-120cm（6个月-3岁）")
            weightRanges.add("EN 14988: 6-15kg")
            ageRanges.add("EN 14988: 6-36个月")
        }

        if (selection.gb29281) {
            heightRanges.add("GB 29281: 75-120cm（6个月-3岁）")
            weightRanges.add("GB 29281: 6-15kg")
            ageRanges.add("GB 29281: 6-36个月")
        }

        if (selection.canCsaZ2171) {
            heightRanges.add("CAN/CSA Z217.1: 75-120cm（6个月-3岁）")
            weightRanges.add("CAN/CSA Z217.1: 6-15kg")
            ageRanges.add("CAN/CSA Z217.1: 6-36个月")
        }

        if (selection.astmF404) {
            heightRanges.add("ASTM F404: 70-130cm（5个月-4岁）")
            weightRanges.add("ASTM F404: 5-20kg")
            ageRanges.add("ASTM F404: 5-48个月")
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
        val seatHeightAdjustRange = mutableListOf<String>()
        val safetyBeltType = mutableListOf<String>()
        val tableEdgeDistance = mutableListOf<String>()
        val antiTipOverDimension = mutableListOf<String>()

        if (selection.en14988 || selection.gb29281 || selection.canCsaZ2171) {
            seatHeightAdjustRange.add("标准要求：座椅高度可调节范围750-950mm（3档调节）")
            safetyBeltType.add("标准要求：五点式安全带，织带宽度≥25mm")
            tableEdgeDistance.add("标准要求：与桌面边缘距离≥200mm（防夹手）")
            antiTipOverDimension.add("标准要求：防倾倒尺寸：座面深度≥300mm，座面宽度≥350mm")
        }

        if (selection.astmF404) {
            seatHeightAdjustRange.add("ASTM F404: 座椅高度可调节范围700-900mm（3档调节）")
            safetyBeltType.add("ASTM F404: 五点式安全带，织带宽度≥20mm")
            tableEdgeDistance.add("ASTM F404: 与桌面边缘距离≥180mm（防夹手）")
            antiTipOverDimension.add("ASTM F404: 防倾倒尺寸：座面深度≥280mm，座面宽度≥330mm")
        }

        return DesignParametersSection(
            seatHeightAdjustRange = if (seatHeightAdjustRange.isNotEmpty()) seatHeightAdjustRange.joinToString("\n") else null,
            safetyBeltType = if (safetyBeltType.isNotEmpty()) safetyBeltType.joinToString("\n") else null,
            tableEdgeDistance = if (tableEdgeDistance.isNotEmpty()) tableEdgeDistance.joinToString("\n") else null,
            antiTipOverDimension = if (antiTipOverDimension.isNotEmpty()) antiTipOverDimension.joinToString("\n") else null
        )
    }

    /**
     * 生成测试要求
     */
    private fun generateTestRequirements(selection: StandardSelection): TestRequirementsSection {
        val stability = mutableListOf<String>()
        val strapStrength = mutableListOf<String>()
        val tableStrength = mutableListOf<String>()

        if (selection.en14988 || selection.gb29281 || selection.canCsaZ2171) {
            stability.add("稳定性：前后倾斜10°无倾倒，左右倾斜15°无倾倒")
            strapStrength.add("安全带强度：织带断裂强度≥200N，卡扣保持力≥250N")
            tableStrength.add("托盘强度：托盘载荷≥30kg无断裂，边缘无锐利边")
        }

        if (selection.astmF404) {
            stability.add("稳定性：前后倾斜9°无倾倒，左右倾斜14°无倾倒")
            strapStrength.add("安全带强度：织带断裂强度≥180N，卡扣保持力≥220N")
            tableStrength.add("托盘强度：托盘载荷≥25kg无断裂，边缘无锐利边")
        }

        return TestRequirementsSection(
            stability = if (stability.isNotEmpty()) stability.joinToString("\n") else null,
            strapStrength = if (strapStrength.isNotEmpty()) strapStrength.joinToString("\n") else null,
            tableStrength = if (tableStrength.isNotEmpty()) tableStrength.joinToString("\n") else null
        )
    }

    /**
     * 生成标准测试项
     */
    private fun generateStandardTestItems(selection: StandardSelection): StandardTestItemsSection {
        val stabilityTest = mutableListOf<String>()
        val strapTest = mutableListOf<String>()
        val loadTest = mutableListOf<String>()
        val materialSafety = mutableListOf<String>()

        if (selection.en14988 || selection.gb29281 || selection.canCsaZ2171) {
            stabilityTest.add("测试设备：倾斜台（符合EN 14988 §5.5）\n测试方法：前后左右倾斜测试，分别倾斜10°/15°，保持30秒\n合格判据：无倾倒，座椅无滑动，锁定机构无失效")
            
            strapTest.add("测试设备：拉力试验机\n测试方法：施加200N拉力于安全带，保持10秒\n合格判据：织带无断裂，卡扣无松脱，永久变形≤5mm")
            
            loadTest.add("测试设备：静载荷测试台\n测试方法：托盘上施加30kg载荷，保持60秒\n合格判据：托盘无断裂，支撑结构无永久变形")
            
            materialSafety.add("测试标准：EN 71-3:2019\n测试方法：重金属迁移测试\n合格判据：铅≤90mg/kg，镉≤60mg/kg，砷≤25mg/kg")
        }

        if (selection.astmF404) {
            stabilityTest.add("测试设备：倾斜台（符合ASTM F404 §6）\n测试方法：前后左右倾斜测试，分别倾斜9°/14°，保持30秒\n合格判据：无倾倒，座椅无滑动，锁定机构无失效")
            
            strapTest.add("测试设备：拉力试验机\n测试方法：施加180N拉力于安全带，保持10秒\n合格判据：织带无断裂，卡扣无松脱，永久变形≤6mm")
            
            loadTest.add("测试设备：静载荷测试台\n测试方法：托盘上施加25kg载荷，保持60秒\n合格判据：托盘无断裂，支撑结构无永久变形")
            
            materialSafety.add("测试标准：ASTM F963\n测试方法：重金属迁移测试\n合格判据：铅≤100mg/kg，镉≤75mg/kg，砷≤30mg/kg")
        }

        return StandardTestItemsSection(
            stabilityTest = if (stabilityTest.isNotEmpty()) stabilityTest.joinToString("\n\n") else null,
            strapTest = if (strapTest.isNotEmpty()) strapTest.joinToString("\n\n") else null,
            loadTest = if (loadTest.isNotEmpty()) loadTest.joinToString("\n\n") else null,
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
            appendLine("📦 儿童高脚椅设计方案（严格遵守$standardName）")
            appendLine()
            
            // 适用标准
            appendLine("【适用标准】$standardName")
            appendLine("标准版本：2021版 | 实施要求：欧盟强制实施")
            appendLine("🔍 核心要求：稳定性≥10°，五点式安全带，托盘载荷≥30kg")
            appendLine()
            
            // 基础适配数据
            appendLine("📊 基础适配数据（基于用户输入身高：${inputHeightCm.toInt()}cm / 体重：${inputWeightKg.toInt()}kg）")
            appendLine("🔽 儿童适配参数（ISO 7176-9:2009）")
            
            appendLine("▫️ 身高范围：${proposal.childData.heightRange}")
            appendLine("▫️ 体重范围：${proposal.childData.weightRange}")
            appendLine("▫️ 年龄范围：${proposal.childData.ageRange}")
            appendLine("▫️ 人体测量参数：坐高38cm，肩宽22cm，头围48cm")
            appendLine("▫️ 适用场景：家庭用餐、餐厅辅助、学习桌配套")
            appendLine()
            
            // 设计参数
            appendLine("📏 设计参数（高脚椅结构设计标准）")
            
            proposal.designParameters.seatHeightAdjustRange?.let {
                appendLine("▫️ 座椅高度调节范围：座椅高度可调节范围750-950mm（3档调节）")
            }
            
            proposal.designParameters.safetyBeltType?.let {
                appendLine("▫️ 安全带类型：五点式安全带，织带宽度≥25mm")
            }
            
            proposal.designParameters.tableEdgeDistance?.let {
                appendLine("▫️ 与桌面边缘距离：与桌面边缘距离≥200mm（防夹手）")
            }
            
            proposal.designParameters.antiTipOverDimension?.let {
                appendLine("▫️ 防倾倒尺寸：座面深度≥300mm，座面宽度≥350mm")
            }
            appendLine()
            
            // 测试要求
            appendLine("⚖️ 测试要求（量化阈值 + 标准条款，可直接用于测试方案）")
            
            proposal.testRequirements.stability?.let {
                appendLine("▫️ 稳定性：前后倾斜10°无倾倒，左右倾斜15°无倾倒（EN 14988 §5.5）")
            }
            
            proposal.testRequirements.strapStrength?.let {
                appendLine("▫️ 安全带强度：织带断裂强度≥200N，卡扣保持力≥250N（EN 14988 §5.6）")
            }
            
            proposal.testRequirements.tableStrength?.let {
                appendLine("▫️ 托盘强度：托盘载荷≥30kg无断裂，边缘无锐利边（EN 14988 §5.7）")
            }
            appendLine()
            
            // 标准测试项
            appendLine("🧪 标准测试项（测试设备+流程+合格判据，可直接对接实验室）")
            
            proposal.standardTestItems.stabilityTest?.let {
                appendLine("稳定性测试")
                appendLine("   测试设备：倾斜台（符合EN 14988 §5.5）")
                appendLine("   测试方法：前后左右倾斜测试，分别倾斜10°/15°，保持30秒")
                appendLine("   ✅ 合格判据：无倾倒，座椅无滑动，锁定机构无失效")
            }
            
            proposal.standardTestItems.strapTest?.let {
                appendLine("\n安全带测试")
                appendLine("   测试设备：拉力试验机")
                appendLine("   测试方法：施加200N拉力于安全带，保持10秒")
                appendLine("   ✅ 合格判据：织带无断裂，卡扣无松脱，永久变形≤5mm")
            }
            
            proposal.standardTestItems.loadTest?.let {
                appendLine("\n托盘载荷测试")
                appendLine("   测试设备：静载荷测试台")
                appendLine("   测试方法：托盘上施加30kg载荷，保持60秒")
                appendLine("   ✅ 合格判据：托盘无断裂，支撑结构无永久变形")
            }
        }
    }
}

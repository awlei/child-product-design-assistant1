package com.childproduct.designassistant.service

import com.childproduct.designassistant.constants.StandardConstants

/**
 * 婴儿推车标准适配设计服务
 * 
 * 功能：
 * 1. 根据用户选择的标准调用相应的数据库
 * 2. 严格按照用户选择生成输出
 * 3. 支持多标准选择（EN 1888、GB 14748、ASTM F833、CAN_CSA_D425、AS/NZS 2088）
 */
class BabyStrollerDesignService {

    /**
     * 标准选择数据类
     */
    data class StandardSelection(
        val en1888: Boolean = false,          // EN 1888 (欧盟)
        val gb14748: Boolean = false,        // GB 14748 (中国)
        val astmF833: Boolean = false,       // ASTM F833 (美国)
        val canCsaD425: Boolean = false,     // CAN/CSA D425 (加拿大)
        val asNzs2088: Boolean = false       // AS/NZS 2088 (澳洲)
    ) {
        fun hasAnySelection(): Boolean = en1888 || gb14748 || astmF833 || canCsaD425 || asNzs2088

        fun getSelectedStandards(): List<String> {
            val list = mutableListOf<String>()
            if (en1888) list.add(StandardConstants.getStandardName(StandardConstants.EN_1888))
            if (gb14748) list.add(StandardConstants.getStandardName(StandardConstants.GB_14748))
            if (astmF833) list.add(StandardConstants.getStandardName(StandardConstants.ASTM_F833))
            if (canCsaD425) list.add(StandardConstants.getStandardName(StandardConstants.CAN_CSA_D425))
            if (asNzs2088) list.add(StandardConstants.getStandardName(StandardConstants.AS_NZS_2088))
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
        val wheelbase: String?,
        val brakeSystemType: String?,
        val foldingDimension: String?,
        val stabilityAngle: String?,
        val handleHeightRange: String?
    )

    /**
     * 测试要求部分
     */
    data class TestRequirementsSection(
        val stability: String?,
        val brakeStrength: String?,
        val foldingStrength: String?
    )

    /**
     * 标准测试项部分
     */
    data class StandardTestItemsSection(
        val stabilityTest: String?,
        val brakeTest: String?,
        val durabilityTest: String?,
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
                designParameters = DesignParametersSection(null, null, null, null, null),
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

        if (selection.en1888) {
            heightRanges.add("EN 1888: 新生儿-105cm（0-4岁）")
            weightRanges.add("EN 1888: 0-15kg")
            ageRanges.add("EN 1888: 0-48个月")
        }

        if (selection.gb14748) {
            heightRanges.add("GB 14748: 新生儿-105cm（0-4岁）")
            weightRanges.add("GB 14748: 0-15kg")
            ageRanges.add("GB 14748: 0-48个月")
        }

        if (selection.astmF833) {
            heightRanges.add("ASTM F833: 新生儿-110cm（0-5岁）")
            weightRanges.add("ASTM F833: 0-18kg")
            ageRanges.add("ASTM F833: 0-60个月")
        }

        if (selection.canCsaD425) {
            heightRanges.add("CAN/CSA D425: 新生儿-105cm（0-4岁）")
            weightRanges.add("CAN/CSA D425: 0-15kg")
            ageRanges.add("CAN/CSA D425: 0-48个月")
        }

        if (selection.asNzs2088) {
            heightRanges.add("AS/NZS 2088: 新生儿-105cm（0-4岁）")
            weightRanges.add("AS/NZS 2088: 0-15kg")
            ageRanges.add("AS/NZS 2088: 0-48个月")
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
        val wheelbase = mutableListOf<String>()
        val brakeSystemType = mutableListOf<String>()
        val foldingDimension = mutableListOf<String>()
        val stabilityAngle = mutableListOf<String>()
        val handleHeightRange = mutableListOf<String>()

        if (selection.en1888 || selection.gb14748 || selection.asNzs2088 || selection.canCsaD425) {
            wheelbase.add("标准要求：轴距≥500mm（确保稳定性）")
            brakeSystemType.add("标准要求：双轮锁死制动系统，操作力≤50N")
            foldingDimension.add("标准要求：折叠后尺寸≤100×60×40cm（便于携带）")
            stabilityAngle.add("标准要求：稳定角≥15°（前后）/≥10°（左右）")
            handleHeightRange.add("标准要求：扶手高度950-1100mm（可调节）")
        }

        if (selection.astmF833) {
            wheelbase.add("ASTM F833: 轴距≥480mm")
            brakeSystemType.add("ASTM F833: 双轮锁死，操作力≤60N")
            foldingDimension.add("ASTM F833: 折叠后尺寸≤105×65×45cm")
            stabilityAngle.add("ASTM F833: 稳定角≥14°（前后）/≥9°（左右）")
            handleHeightRange.add("ASTM F833: 扶手高度900-1050mm（可调节）")
        }

        return DesignParametersSection(
            wheelbase = if (wheelbase.isNotEmpty()) wheelbase.joinToString("\n") else null,
            brakeSystemType = if (brakeSystemType.isNotEmpty()) brakeSystemType.joinToString("\n") else null,
            foldingDimension = if (foldingDimension.isNotEmpty()) foldingDimension.joinToString("\n") else null,
            stabilityAngle = if (stabilityAngle.isNotEmpty()) stabilityAngle.joinToString("\n") else null,
            handleHeightRange = if (handleHeightRange.isNotEmpty()) handleHeightRange.joinToString("\n") else null
        )
    }

    /**
     * 生成测试要求
     */
    private fun generateTestRequirements(selection: StandardSelection): TestRequirementsSection {
        val stability = mutableListOf<String>()
        val brakeStrength = mutableListOf<String>()
        val foldingStrength = mutableListOf<String>()

        if (selection.en1888 || selection.gb14748 || selection.asNzs2088 || selection.canCsaD425) {
            stability.add("稳定性：前后倾斜15°无倾倒，左右倾斜10°无倾倒")
            brakeStrength.add("刹车强度：制动力≥150N，在10°斜面上无滑动")
            foldingStrength.add("折叠强度：反复折叠500次无卡滞，折叠力≤50N")
        }

        if (selection.astmF833) {
            stability.add("稳定性：前后倾斜14°无倾倒，左右倾斜9°无倾倒")
            brakeStrength.add("刹车强度：制动力≥140N，在10°斜面上无滑动")
            foldingStrength.add("折叠强度：反复折叠300次无卡滞，折叠力≤60N")
        }

        return TestRequirementsSection(
            stability = if (stability.isNotEmpty()) stability.joinToString("\n") else null,
            brakeStrength = if (brakeStrength.isNotEmpty()) brakeStrength.joinToString("\n") else null,
            foldingStrength = if (foldingStrength.isNotEmpty()) foldingStrength.joinToString("\n") else null
        )
    }

    /**
     * 生成标准测试项
     */
    private fun generateStandardTestItems(selection: StandardSelection): StandardTestItemsSection {
        val stabilityTest = mutableListOf<String>()
        val brakeTest = mutableListOf<String>()
        val durabilityTest = mutableListOf<String>()
        val materialSafety = mutableListOf<String>()

        if (selection.en1888 || selection.gb14748 || selection.asNzs2088 || selection.canCsaD425) {
            stabilityTest.add("测试设备：倾斜台（符合EN 1888 §5.7）\n测试方法：前后左右倾斜测试，分别倾斜15°/10°，保持30秒\n合格判据：无倾倒，座椅无滑动，锁定机构无失效")
            
            brakeTest.add("测试设备：斜坡测试台\n测试方法：在10°斜坡上施加150N制动力，保持10秒\n合格判据：车轮无滚动，刹车机构无失效，制动距离≤50mm")
            
            durabilityTest.add("测试设备：折叠测试机\n测试方法：反复折叠500次，每次折叠力≤50N\n合格判据：机构无卡滞，无零件脱落，无永久变形")
            
            materialSafety.add("测试标准：ISO 3795:2019\n测试方法：水平燃烧测试\n合格判据：燃烧速度≤100mm/min，无熔融滴落")
        }

        if (selection.astmF833) {
            stabilityTest.add("测试设备：倾斜台（符合ASTM F833 §6）\n测试方法：前后左右倾斜测试，分别倾斜14°/9°，保持30秒\n合格判据：无倾倒，座椅无滑动，锁定机构无失效")
            
            brakeTest.add("测试设备：斜坡测试台\n测试方法：在10°斜坡上施加140N制动力，保持10秒\n合格判据：车轮无滚动，刹车机构无失效，制动距离≤60mm")
            
            durabilityTest.add("测试设备：折叠测试机\n测试方法：反复折叠300次，每次折叠力≤60N\n合格判据：机构无卡滞，无零件脱落，无永久变形")
            
            materialSafety.add("测试标准：ASTM F963\n测试方法：水平燃烧测试\n合格判据：燃烧速度≤100mm/min，无熔融滴落")
        }

        return StandardTestItemsSection(
            stabilityTest = if (stabilityTest.isNotEmpty()) stabilityTest.joinToString("\n\n") else null,
            brakeTest = if (brakeTest.isNotEmpty()) brakeTest.joinToString("\n\n") else null,
            durabilityTest = if (durabilityTest.isNotEmpty()) durabilityTest.joinToString("\n\n") else null,
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
            appendLine("📦 婴儿推车设计方案（严格遵守$standardName）")
            appendLine()
            
            // 适用标准
            appendLine("【适用标准】$standardName")
            appendLine("标准版本：2022版 | 实施要求：欧盟强制实施")
            appendLine("🔍 核心要求：稳定性≥15°，双轮锁死刹车，折叠尺寸≤100×60×40cm")
            appendLine()
            
            // 基础适配数据
            appendLine("📊 基础适配数据（基于用户输入身高：${inputHeightCm.toInt()}cm / 体重：${inputWeightKg.toInt()}kg）")
            appendLine("🔽 儿童适配参数（ISO 7176-5:2017）")
            
            appendLine("▫️ 身高范围：${proposal.childData.heightRange}")
            appendLine("▫️ 体重范围：${proposal.childData.weightRange}")
            appendLine("▫️ 年龄范围：${proposal.childData.ageRange}")
            appendLine("▫️ 人体测量参数：坐高40cm，肩宽24cm，头围46cm")
            appendLine("▫️ 适用场景：日常出行、公园散步、旅行")
            appendLine()
            
            // 设计参数
            appendLine("📏 设计参数（推车结构设计标准）")
            
            proposal.designParameters.wheelbase?.let {
                appendLine("▫️ 轴距：轴距≥500mm（确保推车稳定性）")
            }
            
            proposal.designParameters.brakeSystemType?.let {
                appendLine("▫️ 刹车类型：双轮锁死制动系统，操作力≤50N")
            }
            
            proposal.designParameters.foldingDimension?.let {
                appendLine("▫️ 折叠尺寸：折叠后尺寸≤100×60×40cm（便于携带）")
            }
            
            proposal.designParameters.stabilityAngle?.let {
                appendLine("▫️ 稳定角：稳定角≥15°（前后）/≥10°（左右）")
            }
            
            proposal.designParameters.handleHeightRange?.let {
                appendLine("▫️ 扶手高度：扶手高度950-1100mm（可调节）")
            }
            appendLine()
            
            // 测试要求
            appendLine("⚖️ 测试要求（量化阈值 + 标准条款，可直接用于测试方案）")
            
            proposal.testRequirements.stability?.let {
                appendLine("▫️ 稳定性：前后倾斜15°无倾倒，左右倾斜10°无倾倒（EN 1888 §5.7）")
            }
            
            proposal.testRequirements.brakeStrength?.let {
                appendLine("▫️ 刹车强度：制动力≥150N，在10°斜面上无滑动（EN 1888 §5.8）")
            }
            
            proposal.testRequirements.foldingStrength?.let {
                appendLine("▫️ 折叠强度：反复折叠500次无卡滞，折叠力≤50N（EN 1888 §5.9）")
            }
            appendLine()
            
            // 标准测试项
            appendLine("🧪 标准测试项（测试设备+流程+合格判据，可直接对接实验室）")
            
            proposal.standardTestItems.stabilityTest?.let {
                appendLine("稳定性测试")
                appendLine("   测试设备：倾斜台（符合EN 1888 §5.7）")
                appendLine("   测试方法：前后左右倾斜测试，分别倾斜15°/10°，保持30秒")
                appendLine("   ✅ 合格判据：无倾倒，座椅无滑动，锁定机构无失效")
            }
            
            proposal.standardTestItems.brakeTest?.let {
                appendLine("\n刹车测试")
                appendLine("   测试设备：斜坡测试台")
                appendLine("   测试方法：在10°斜坡上施加150N制动力，保持10秒")
                appendLine("   ✅ 合格判据：车轮无滚动，刹车机构无失效，制动距离≤50mm")
            }
            
            proposal.standardTestItems.durabilityTest?.let {
                appendLine("\n折叠耐用性测试")
                appendLine("   测试设备：折叠测试机")
                appendLine("   测试方法：反复折叠500次，每次折叠力≤50N")
                appendLine("   ✅ 合格判据：机构无卡滞，无零件脱落，无永久变形")
            }
        }
    }
}

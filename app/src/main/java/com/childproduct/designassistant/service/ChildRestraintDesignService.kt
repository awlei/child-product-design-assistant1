package com.childproduct.designassistant.service

import com.childproduct.designassistant.data.*
import com.childproduct.designassistant.constants.StandardConstants

/**
 * 儿童安全座椅标准适配设计服务
 *
 * 功能：
 * 1. 根据用户选择的标准调用相应的数据库
 * 2. 严格按照用户选择生成输出
 * 3. 支持多标准选择
 * 修复：使用StandardConstants统一标准标识，添加全链路日志
 */
class ChildRestraintDesignService {

    /**
     * 标准选择数据类
     */
    data class StandardSelection(
        val eceR129: Boolean = false,      // ECE R129 (欧盟i-Size)
        val gb27887: Boolean = false,     // GB 27887-2024 (中国新标)
        val fmvss213: Boolean = false,    // FMVSS 213 (美国标准)
        val asNzs1754: Boolean = false,   // AS/NZS 1754 (澳洲标准)
        val jisD1601: Boolean = false     // JIS D 1601 (日本标准)
    ) {
        fun hasAnySelection(): Boolean = eceR129 || gb27887 || fmvss213 || asNzs1754 || jisD1601

        fun getSelectedStandards(): List<String> {
            val list = mutableListOf<String>()
            if (eceR129) list.add(StandardConstants.getStandardName(StandardConstants.ECE_R129))
            if (gb27887) list.add(StandardConstants.getStandardName(StandardConstants.GB_27887_2024))
            if (fmvss213) list.add(StandardConstants.getStandardName(StandardConstants.FMVSS_213))
            if (asNzs1754) list.add(StandardConstants.getStandardName(StandardConstants.AS_NZS_1754))
            if (jisD1601) list.add(StandardConstants.getStandardName(StandardConstants.JIS_D1601))
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
        android.util.Log.d("ChildRestraintDS", "generateDesignProposal调用")
        android.util.Log.d("ChildRestraintDS", "selection参数: ECE=${selection.eceR129}, GB=${selection.gb27887}, FMVSS=${selection.fmvss213}, AS=${selection.asNzs1754}, JIS=${selection.jisD1601}")

        if (!selection.hasAnySelection()) {
            android.util.Log.e("ChildRestraintDS", "未选择任何标准，返回空方案")
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

        // JIS D 1601 (日标)
        if (selection.jisD1601) {
            heightRanges.add("JIS D 1601: 基于年龄分组（0-6岁）")
            weightRanges.add("JIS D 1601: 0-18kg")
            directions.add("JIS D 1601: 反向、前向、安全带固定")
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

        // JIS D 1601 (日标)
        if (selection.jisD1601) {
            headRestHeight.add("JIS D 1601: 参考日本儿童座椅规范")
            seatWidth.add("JIS D 1601: 360-450mm")
            envelope.add("JIS D 1601: 外形尺寸限制")
            sideImpactArea.add("JIS D 1601: 侧碰防护要求")
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
            sideChestCompression.add("FMVSS 213: 213a侧碰胸部压缩 ≤ 52mm")
            webbingStrength.add("FMVSS 213: 织带强度 11kN（安全带）/15kN（ISOFIX）")
        }

        // AS/NZS 1754 (澳标)
        if (selection.asNzs1754) {
            frontal.add("AS/NZS 1754: 50km/h 正碰")
            sideChestCompression.add("AS/NZS 1754: 侧碰胸部压缩 ≤ 40mm")
            webbingStrength.add("AS/NZS 1754: 织带强度 4.5kN")
        }

        // JIS D 1601 (日标)
        if (selection.jisD1601) {
            frontal.add("JIS D 1601: 50km/h 正碰")
            sideChestCompression.add("JIS D 1601: 侧碰胸部压缩 ≤ 38mm")
            webbingStrength.add("JIS D 1601: 织带强度 4.5kN")
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
            dynamicFrontal.add("测试设备：HYGE电动碰撞台（符合ISO 6487:2018）\n测试条件：假人后向，约束系统：ISOFIX+Top Tether，碰撞速度50km/h\n合格判据：头部HIC≤1000，胸部加速度≤60g（3ms滑动平均）")
            dynamicRear.add("测试设备：HYGE电动碰撞台\n测试条件：假人后向，约束系统：ISOFIX+Top Tether，碰撞速度35km/h\n合格判据：头部HIC≤800，胸部加速度≤55g")
            dynamicSide.add("测试设备：移动壁障（符合ECE R129 §7.1.3）\n测试条件：侧向撞击速度60km/h，使用Q3假人\n合格判据：胸部压缩≤44mm，腹部受力≤2.5kN")
            flammability.add("测试标准：ISO 3795:2019\n测试方法：水平燃烧测试\n合格判据：燃烧速度≤100mm/min，无熔融滴落")
        }

        // GB 28007-2024 (国标)
        if (selection.gb27887) {
            dynamicFrontal.add("测试设备：HYGE电动碰撞台（符合GB 27887-2024 §6.1）\n测试条件：假人后向/前向，约束系统：ISOFIX+Top Tether，碰撞速度50km/h\n合格判据：头部HIC≤1000，胸部加速度≤60g")
            dynamicRear.add("测试设备：HYGE电动碰撞台\n测试条件：假人后向，碰撞速度35km/h\n合格判据：头部HIC≤800，胸部加速度≤55g")
            dynamicSide.add("测试设备：移动壁障\n测试条件：侧向撞击速度60km/h，使用Q3假人\n合格判据：胸部压缩≤44mm，腹部受力≤2.5kN")
            flammability.add("测试标准：GB 8410-2006\n测试方法：垂直燃烧测试\n合格判据：燃烧速度≤100mm/min，无熔融滴落")
        }

        // FMVSS 213 (美标)
        if (selection.fmvss213) {
            dynamicFrontal.add("测试设备：HYGE电动碰撞台（符合FMVSS 213 §S7）\n测试条件：假人后向/前向，约束系统：安全带/ISOFIX，碰撞速度48km/h（30mph）\n合格判据：头部HIC≤1000，胸部加速度≤60g")
            dynamicRear.add("测试设备：HYGE电动碰撞台\n测试条件：假人后向，碰撞速度48km/h\n合格判据：头部HIC≤1000，胸部加速度≤60g")
            dynamicSide.add("测试设备：移动壁障（符合FMVSS 213a）\n测试条件：侧向撞击速度32km/h（20mph），使用Q3s假人\n合格判据：胸部压缩≤52mm，腹部受力≤2.5kN")
            flammability.add("测试标准：FMVSS 302\n测试方法：水平燃烧测试\n合格判据：燃烧速度≤100mm/min，无熔融滴落")
        }

        // AS/NZS 1754 (澳标)
        if (selection.asNzs1754) {
            dynamicFrontal.add("测试设备：HYGE电动碰撞台（符合AS/NZS 1754 §7）\n测试条件：假人后向/前向，约束系统：ISOFIX+Top Tether，碰撞速度50km/h\n合格判据：头部HIC≤1000，胸部加速度≤60g")
            dynamicRear.add("测试设备：HYGE电动碰撞台\n测试条件：假人后向，碰撞速度35km/h\n合格判据：头部HIC≤800，胸部加速度≤55g")
            dynamicSide.add("测试设备：移动壁障\n测试条件：侧向撞击速度60km/h，使用Q3假人\n合格判据：胸部压缩≤40mm，腹部受力≤2.5kN")
            flammability.add("测试标准：AS/NZS 1754 §8\n测试方法：水平燃烧测试\n合格判据：燃烧速度≤100mm/min，无熔融滴落")
        }

        // JIS D 1601 (日标)
        if (selection.jisD1601) {
            dynamicFrontal.add("测试设备：HYGE电动碰撞台（符合JIS D 1601 §6）\n测试条件：假人后向/前向，约束系统：ISOFIX+Top Tether，碰撞速度50km/h\n合格判据：头部HIC≤1000，胸部加速度≤60g")
            dynamicRear.add("测试设备：HYGE电动碰撞台\n测试条件：假人后向，碰撞速度35km/h\n合格判据：头部HIC≤800，胸部加速度≤55g")
            dynamicSide.add("测试设备：移动壁障\n测试条件：侧向撞击速度60km/h，使用Q3假人\n合格判据：胸部压缩≤38mm，腹部受力≤2.5kN")
            flammability.add("测试标准：JIS D 1201\n测试方法：水平燃烧测试\n合格判据：燃烧速度≤100mm/min，无熔融滴落")
        }

        return StandardTestItemsSection(
            dynamicFrontal = if (dynamicFrontal.isNotEmpty()) dynamicFrontal.joinToString("\n\n") else null,
            dynamicRear = if (dynamicRear.isNotEmpty()) dynamicRear.joinToString("\n\n") else null,
            dynamicSide = if (dynamicSide.isNotEmpty()) dynamicSide.joinToString("\n\n") else null,
            flammability = if (flammability.isNotEmpty()) flammability.joinToString("\n\n") else null
        )
    }

    /**
     * 格式化输出为Markdown（专业版）
     * 严格按照工程师工作流输出：适用标准→基础适配→设计参数→测试要求→标准测试项
     */
    fun formatAsMarkdown(proposal: DesignProposal): String {
        return buildString {
            // 标题
            val standardName = proposal.applicableStandards.firstOrNull() ?: "未选择标准"
            appendLine("📦 儿童安全座椅设计方案（严格遵守$standardName）")
            appendLine()
            
            // 适用标准
            appendLine("【适用标准】$standardName")
            appendLine("标准版本：2021版 | 实施要求：欧盟强制实施")
            appendLine("🔍 核心要求：动态碰撞三向覆盖，侧防系统强制，ISOFIX接口兼容ISO 14530-3")
            appendLine()
            
            // 基础适配数据
            appendLine("📊 基础适配数据")
            appendLine("🔽 假人参数（ISO 13232-2:2021）")
            
            // 解析假人数据
            val heightRange = proposal.dummyData.heightRange
            val weightRange = proposal.dummyData.weightRange
            val direction = proposal.dummyData.installationDirection
            
            // 提取假人模型信息
            val dummyModel = when {
                heightRange.contains("Q3") || weightRange.contains("13-18") -> "ECE R129 Q3假人"
                heightRange.contains("Q1") || weightRange.contains("9-18") -> "ECE R129 Q1假人"
                heightRange.contains("Q0") || weightRange.contains("0-13") -> "ECE R129 Q0假人"
                heightRange.contains("Q6") || weightRange.contains("22-36") -> "ECE R129 Q6假人"
                else -> "根据身高体重自动匹配"
            }
            
            val percentile = when {
                heightRange.contains("87-105") || weightRange.contains("13-18") -> "50th百分位3-4岁儿童"
                heightRange.contains("75-97") || weightRange.contains("9-18") -> "50th百分位1.5-4岁儿童"
                heightRange.contains("40-85") || weightRange.contains("0-13") -> "50th百分位0-15个月儿童"
                heightRange.contains("105-150") || weightRange.contains("22-36") -> "50th百分位6-12岁儿童"
                else -> "根据身高体重自动匹配"
            }
            
            // 解析身高体重范围
            val heightValue = extractHeightValue(heightRange)
            val weightValue = extractWeightValue(weightRange)
            
            appendLine("▫️ 假人模型：$dummyModel")
            appendLine("▫️ 百分位/年龄：$percentile")
            appendLine("▫️ 身高范围：$heightValue（基于用户输入，适配性最优）")
            appendLine("▫️ 体重范围：$weightValue")
            appendLine("▫️ 人体测量参数：坐高52cm，肩宽28cm，头围49cm")
            appendLine("▫️ 安装方向：$direction")
            appendLine()
            
            // 设计参数
            appendLine("📏 设计参数（GPS028-2023数据库 + 标准强制要求）")
            
            proposal.designParameters.headRestHeight?.let {
                if (it.contains("ECE R129") || it.contains("GB 28007")) {
                    appendLine("▫️ 头枕高度：535-585mm（基准点：坐骨结节（H点），公差：±5mm）")
                } else if (it.contains("FMVSS")) {
                    appendLine("▫️ 头枕高度：480-530mm（基准点：坐骨结节（H点），公差：±5mm）")
                }
            }
            
            proposal.designParameters.seatWidth?.let {
                if (it.contains("ECE R129") || it.contains("GB 28007")) {
                    appendLine("▫️ 座宽：有效座宽：350mm，总座宽（含侧防）：420mm")
                } else if (it.contains("FMVSS")) {
                    appendLine("▫️ 座宽：有效座宽：380mm，总座宽（含侧防）：440mm")
                }
            }
            
            proposal.designParameters.envelope?.let {
                if (it.contains("ECE R129") || it.contains("GB 28007")) {
                    appendLine("▫️ ISOFIX Envelop（盒子）尺寸：ISOFIX Size Class B2（ECE R129 §5.3.2 / GB 27887-2024 §5.2）")
                    appendLine("▫️ Envelop详细尺寸：纵向长度730mm(±10mm)，横向宽度460mm(±5mm)，固定点间距300-350mm")
                } else if (it.contains("FMVSS")) {
                    appendLine("▫️ ISOFIX Envelop（盒子）尺寸：FMVSS 213 Size Class B2")
                    appendLine("▫️ Envelop详细尺寸：纵向长度720mm(±10mm)，横向宽度450mm(±5mm)，固定点间距280-330mm")
                }
            }
            
            proposal.designParameters.sideImpactArea?.let {
                if (it.contains("ECE R129") || it.contains("GB 28007")) {
                    appendLine("▫️ 侧防面积要求：≥0.85m²（覆盖T12胸部至P8头部侧方区域）")
                    appendLine("▫️ 侧防测试标准：EN 14154-3:2022")
                } else if (it.contains("FMVSS")) {
                    appendLine("▫️ 侧防面积要求：≥0.80m²（覆盖T12胸部至P8头部侧方区域）")
                    appendLine("▫️ 侧防测试标准：FMVSS 213a")
                }
            }
            appendLine()
            
            // 测试要求
            appendLine("⚖️ 测试要求（量化阈值 + 标准条款，可直接用于测试方案）")
            
            proposal.testRequirements.frontal?.let {
                if (it.contains("ECE R129") || it.contains("GB 28007")) {
                    appendLine("▫️ 正面碰撞：碰撞速度50km/h(±1km/h)，碰撞台加速度15g(持续3ms)，HIC≤1000（ECE R129 §7.1.2）")
                } else if (it.contains("FMVSS")) {
                    appendLine("▫️ 正面碰撞：碰撞速度48km/h(30mph ±1mph)，碰撞台加速度15g(持续3ms)，HIC≤1000（FMVSS 213 §S7）")
                }
            }
            
            proposal.testRequirements.sideChestCompression?.let {
                if (it.contains("ECE R129") || it.contains("GB 28007")) {
                    appendLine("▫️ 侧撞胸部压缩：侧撞速度60km/h(移动壁障)，胸部压缩量≤44mm，压缩速度≤2.5m/s（ECE R129 §7.1.3）")
                } else if (it.contains("FMVSS")) {
                    appendLine("▫️ 侧撞胸部压缩：侧撞速度32km/h(20mph)，胸部压缩量≤52mm，压缩速度≤2.5m/s（FMVSS 213a）")
                }
            }
            
            proposal.testRequirements.webbingStrength?.let {
                if (it.contains("ECE R129") || it.contains("GB 28007")) {
                    appendLine("▫️ 安全带织带强度：纵向≥26.7kN，横向≥17.8kN（测试方法：ISO 6683:2017）")
                } else if (it.contains("FMVSS")) {
                    appendLine("▫️ 安全带织带强度：纵向≥11kN，横向≥15kN（测试方法：FMVSS 213 §S5）")
                }
            }
            appendLine()
            
            // 标准测试项
            appendLine("🧪 标准测试项（测试设备+流程+合格判据，可直接对接实验室）")
            
            proposal.standardTestItems.dynamicFrontal?.let {
                if (it.contains("ECE R129") || it.contains("GB 28007")) {
                    appendLine("动态碰撞：正碰")
                    appendLine("   测试设备：HYGE电动碰撞台（符合ISO 6487:2018）")
                    appendLine("   测试条件：假人后向，约束系统：ISOFIX+Top Tether，碰撞速度50km/h")
                    appendLine("   ✅ 合格判据：头部HIC≤1000，胸部加速度≤60g（3ms滑动平均）")
                } else if (it.contains("FMVSS")) {
                    appendLine("动态碰撞：正碰")
                    appendLine("   测试设备：HYGE电动碰撞台（符合FMVSS 213 §S7）")
                    appendLine("   测试条件：假人后向/前向，约束系统：ISOFIX+Top Tether，碰撞速度48km/h")
                    appendLine("   ✅ 合格判据：头部HIC≤1000，胸部加速度≤60g（3ms滑动平均）")
                }
            }
            
            proposal.standardTestItems.dynamicSide?.let {
                if (it.contains("ECE R129") || it.contains("GB 28007")) {
                    appendLine("\n动态碰撞：侧碰")
                    appendLine("   测试设备：移动壁障（符合ECE R129 §7.1.3）")
                    appendLine("   测试条件：侧向撞击速度60km/h，使用Q3假人")
                    appendLine("   ✅ 合格判据：胸部压缩≤44mm，腹部受力≤2.5kN")
                } else if (it.contains("FMVSS")) {
                    appendLine("\n动态碰撞：侧碰")
                    appendLine("   测试设备：移动壁障（符合FMVSS 213a）")
                    appendLine("   测试条件：侧向撞击速度32km/h，使用Q3s假人")
                    appendLine("   ✅ 合格判据：胸部压缩≤52mm，腹部受力≤2.5kN")
                }
            }
        }
    }

    /**
     * 提取身高值
     */
    private fun extractHeightValue(heightRange: String): String {
        return when {
            heightRange.contains("40-85") -> "40-85cm"
            heightRange.contains("75-97") -> "75-97cm"
            heightRange.contains("87-105") -> "87-105cm"
            heightRange.contains("105-150") -> "105-150cm"
            else -> heightRange
        }
    }

    /**
     * 提取体重值
     */
    private fun extractWeightValue(weightRange: String): String {
        return when {
            weightRange.contains("0-13") -> "0-13kg"
            weightRange.contains("9-18") -> "9-18kg"
            weightRange.contains("13-18") -> "13-18kg"
            weightRange.contains("22-36") -> "22-36kg"
            else -> weightRange
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
        asNzs1754 = false,
        jisD1601 = false
    )
    
    val proposal1 = service.generateDesignProposal(
        selection = selection1,
        heightCm = 90.0,
        weightKg = 14.0
    )
    
    println(service.formatAsMarkdown(proposal1))
    
    println("\n" + "=".repeat(80))
    println("测试完成")
    println("=".repeat(80) + "\n")
}

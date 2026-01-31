package com.childproduct.designassistant.service

import com.childproduct.designassistant.model.*
import com.childproduct.designassistant.utils.ComplianceParamsData
import com.childproduct.designassistant.utils.DesignSchemeFormatter
import com.childproduct.designassistant.utils.SchemeCardData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class CreativeService {

    private val ageGroupThemes = mapOf(
        AgeGroup.INFANT to listOf("柔和色彩", "圆形设计", "安全材料", "触感刺激"),
        AgeGroup.TODDLER to listOf("鲜艳色彩", "简单互动", "大尺寸", "耐摔材料"),
        AgeGroup.PRESCHOOL to listOf("教育主题", "创意涂鸦", "角色扮演", "拼图游戏"),
        AgeGroup.SCHOOL_AGE to listOf("科技元素", "团队合作", "探索发现", "竞技挑战"),
        AgeGroup.TEEN to listOf("个性化设计", "社交元素", "技能培养", "潮流风格")
    )

    private val productTypeFeatures = mapOf(
        ProductType.CHILD_SAFETY_SEAT to listOf("安全性", "舒适性", "易安装性", "材质环保"),
        ProductType.BABY_STROLLER to listOf("折叠便携", "避震系统", "稳固性", "储物功能"),
        ProductType.CHILD_HOUSEHOLD_GOODS to listOf("安全设计", "易清洁", "耐久性", "适龄性"),
        ProductType.CHILD_HIGH_CHAIR to listOf("稳定性", "可调节高度", "安全带系统", "易清洁")
    )

    private val colorPalettes = mapOf(
        AgeGroup.INFANT to listOf("#FFB6C1", "#E0FFFF", "#F0E68C", "#E6E6FA"),
        AgeGroup.TODDLER to listOf("#FF6347", "#4169E1", "#32CD32", "#FFD700"),
        AgeGroup.PRESCHOOL to listOf("#FF69B4", "#00CED1", "#FFA500", "#9370DB"),
        AgeGroup.SCHOOL_AGE to listOf("#1E90FF", "#00FA9A", "#FF4500", "#9932CC"),
        AgeGroup.TEEN to listOf("#000000", "#FFFFFF", "#808080", "#C0C0C0", "#FF1493")
    )

    private val materialSuggestions = mapOf(
        ProductType.CHILD_SAFETY_SEAT to listOf("食品级PP塑料", "高回弹海绵", "安全带织带", "铝合金支架"),
        ProductType.BABY_STROLLER to listOf("铝合金框架", "耐磨牛津布", "EVA发泡轮子", "不锈钢车轴"),
        ProductType.CHILD_HOUSEHOLD_GOODS to listOf("ABS环保塑料", "实木（榉木/桦木）", "食品级硅胶", "不锈钢配件"),
        ProductType.CHILD_HIGH_CHAIR to listOf("实木（榉木）", "食品级PP塑料", "不锈钢螺丝", "环保涂层")
    )

    suspend fun generateCreativeIdea(
        ageGroup: AgeGroup,
        productType: ProductType,
        customTheme: String = ""
    ): CreativeIdea = withContext(Dispatchers.IO) {
        val themes = ageGroupThemes[ageGroup] ?: emptyList()
        val features = productTypeFeatures[productType] ?: emptyList()
        val colors = colorPalettes[ageGroup] ?: emptyList()
        val materials = materialSuggestions[productType] ?: listOf("塑料", "木质", "金属", "布料")

        val finalTheme = if (customTheme.isNotEmpty()) customTheme else themes.random()
        val selectedFeatures = features.shuffled().take(4)
        val selectedColors = colors.shuffled().take(4)

        val title = generateTitle(ageGroup, productType, finalTheme)
        val description = generateDescription(ageGroup, productType, finalTheme, selectedFeatures)
        val safetyNotes = generateSafetyNotes(ageGroup, productType)

        // 获取对应的假人类型
        val dummyType = CrashTestDummy.getByAgeGroup(ageGroup)

        // 生成专业合规参数（基于假人类型）
        val complianceParameters = ComplianceParameters.getByDummy(dummyType)

        // 生成标准关联（根据产品类型）
        val standardsReference = StandardsReference.getDefaultForProductType(productType)

        // 生成材质规格（根据产品类型）
        val materialSpecs = MaterialSpecs.getDefaultForProductType(productType)

        CreativeIdea(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            ageGroup = ageGroup,
            productType = productType,
            theme = finalTheme,
            features = selectedFeatures,
            materials = materials,
            colorPalette = selectedColors,
            safetyNotes = safetyNotes,
            complianceParameters = complianceParameters,
            standardsReference = standardsReference,
            materialSpecs = materialSpecs
        )
    }

    /**
     * 基于身高范围生成创意（新增）
     */
    suspend fun generateCreativeIdeaByHeight(
        minHeightCm: Int,
        maxHeightCm: Int,
        productType: ProductType,
        customTheme: String = ""
    ): CreativeIdeaResult = withContext(Dispatchers.IO) {
        // 强制规则：身高40-150cm → 0-12岁（AgeGroup.ALL）
        val ageGroup = if (minHeightCm >= 40 && maxHeightCm <= 150) {
            AgeGroup.ALL  // 强制映射到0-12岁
        } else {
            // 如果范围超出，根据中间高度映射
            val middleHeight = (minHeightCm + maxHeightCm) / 2
            val dummyType = CrashTestMapping.getDummyByHeight(middleHeight)
            when {
                dummyType == CrashTestDummy.Q0 || dummyType == CrashTestDummy.Q0_PLUS -> AgeGroup.INFANT
                dummyType == CrashTestDummy.Q1 || dummyType == CrashTestDummy.Q1_5 -> AgeGroup.TODDLER
                dummyType == CrashTestDummy.Q3 -> AgeGroup.PRESCHOOL
                dummyType == CrashTestDummy.Q3_S -> AgeGroup.SCHOOL_AGE
                else -> AgeGroup.TEEN
            }
        }

        // 验证身高范围
        val validation = CrashTestMapping.validateHeightRange(minHeightCm, maxHeightCm)

        if (!validation.isValid) {
            return@withContext CreativeIdeaResult(
                success = false,
                error = validation.errors.joinToString("; "),
                idea = null,
                validation = validation
            )
        }

        // 获取所有涉及的假人类型
        val dummyTypes = HeightAgeMappingConfig.getIntervalsByHeightRange(
            minHeightCm.toDouble(),
            maxHeightCm.toDouble()
        ).map { it.dummyType }.distinct()

        // 使用中间假人类型作为主要参考
        val mainDummyType = CrashTestMapping.getDummyByHeight((minHeightCm + maxHeightCm) / 2)
        val dummyType = mainDummyType  // 用于后续生成

        val themes = ageGroupThemes[ageGroup] ?: emptyList()
        val features = productTypeFeatures[productType] ?: emptyList()
        val colors = colorPalettes[ageGroup] ?: emptyList()
        val materials = materialSuggestions[productType] ?: listOf("塑料", "木质", "金属", "布料")

        val finalTheme = if (customTheme.isNotEmpty()) customTheme else themes.random()
        val selectedFeatures = features.shuffled().take(4)
        val selectedColors = colors.shuffled().take(4)

        val title = generateTitle(ageGroup, productType, finalTheme)
        val description = generateDescription(ageGroup, productType, finalTheme, selectedFeatures)
        val safetyNotes = generateSafetyNotes(ageGroup, productType)

        // 生成专业合规参数（基于假人类型）
        val complianceParameters = ComplianceParameters.getByDummy(dummyType)

        // 生成标准关联（根据产品类型）
        val standardsReference = StandardsReference.getDefaultForProductType(productType)

        // 生成材质规格（根据产品类型）
        val materialSpecs = MaterialSpecs.getDefaultForProductType(productType)

        // 生成年龄修正提示（如果需要）
        val ageCorrectionHint = if (ageGroup == AgeGroup.ALL) {
            HeightAgeMappingConfig.getCorrectionHint()
        } else {
            null
        }

        // 生成标准合规性说明
        val standardComplianceText = HeightAgeMappingConfig.getStandardComplianceText()

        val idea = CreativeIdea(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            ageGroup = ageGroup,
            productType = productType,
            theme = finalTheme,
            features = selectedFeatures,
            materials = materials,
            colorPalette = selectedColors,
            safetyNotes = safetyNotes,
            complianceParameters = complianceParameters,
            standardsReference = standardsReference,
            materialSpecs = materialSpecs
        )

        CreativeIdeaResult(
            success = true,
            error = null,
            idea = idea,
            validation = validation,
            ageCorrectionHint = ageCorrectionHint,
            standardComplianceText = standardComplianceText
        )
    }

    private fun generateTitle(ageGroup: AgeGroup, productType: ProductType, theme: String): String {
        return when {
            ageGroup == AgeGroup.INFANT && productType == ProductType.CHILD_SAFETY_SEAT ->
                "婴幼儿专用${productType.displayName} - $theme"
            ageGroup == AgeGroup.INFANT && productType == ProductType.BABY_STROLLER ->
                "婴幼儿${productType.displayName} - $theme"
            else ->
                "${ageGroup.displayName}${productType.displayName} - $theme"
        }
    }

    private fun generateDescription(
        ageGroup: AgeGroup,
        productType: ProductType,
        theme: String,
        features: List<String>
    ): String {
        val baseDescription = "专为${ageGroup.displayName}儿童设计的${productType.displayName}，" +
                "融入${theme}设计理念。主要特点包括：${features.joinToString("、")}。"

        // 根据产品类型添加专业性描述
        val professionalDescription = when (productType) {
            ProductType.CHILD_SAFETY_SEAT -> {
                val dummyType = CrashTestDummy.getByAgeGroup(ageGroup)
                val complianceText = when (dummyType) {
                    CrashTestDummy.Q0, CrashTestDummy.Q0_PLUS, CrashTestDummy.Q1 ->
                        "符合UN R129 i-Size婴儿标准（${dummyType.name}假人），HIC极限值≤ ${dummyType.hicLimit}，"
                    CrashTestDummy.Q1_5 ->
                        "符合UN R129 i-Size幼儿标准（${dummyType.name}假人），HIC极限值≤ ${dummyType.hicLimit}，"
                    else ->
                        "符合UN R129 i-Size儿童标准（${dummyType.name}假人），HIC极限值≤ ${dummyType.hicLimit}，"
                }
                "$baseDescription $complianceText 满足FMVSS 302燃烧性能要求，通过ISOFIX连接实现快速安装。"
            }
            ProductType.BABY_STROLLER -> {
                "$baseDescription 符合EN 1888 + GB 14748-2020标准，制动系统可靠，折叠机构安全防夹，危险点圆角处理R≥ 2.5mm。"
            }
            ProductType.CHILD_HIGH_CHAIR -> {
                "$baseDescription 符合EN 14988 + GB 22793.1-2008标准，五点式安全带，防滑脚垫，稳固结构设计。"
            }
            else -> {
                "$baseDescription 产品设计充分考虑儿童发展特点，注重安全性、教育性和趣味性。"
            }
        }

        return professionalDescription
    }

    private fun generateSafetyNotes(ageGroup: AgeGroup, productType: ProductType): List<String> {
        val notes = mutableListOf<String>()

        when (ageGroup) {
            AgeGroup.INFANT, AgeGroup.TODDLER -> {
                notes.add("确保所有部件尺寸大于3.5cm，防止吞咽风险")
                notes.add("使用食品级安全材料，无甲醛、无重金属")
                notes.add("所有边缘需圆角处理，无尖锐部位")
            }
            AgeGroup.PRESCHOOL -> {
                notes.add("避免细小零件脱落风险")
                notes.add("材料需通过欧盟EN71安全认证")
                notes.add("结构稳固，不易倒塌")
            }
            else -> {
                notes.add("符合国家玩具安全标准GB 6675")
                notes.add("注意电气安全（如适用）")
                notes.add("提供清晰的使用说明和安全警示")
            }
        }

        return notes
    }
}

/**
 * 创意生成结果（带校验信息）
 */
data class CreativeIdeaResult(
    val success: Boolean,
    val error: String?,
    val idea: CreativeIdea?,
    val validation: CrashTestMapping.ValidationResult,
    val ageCorrectionHint: String? = null,  // 年龄修正提示
    val standardComplianceText: String? = null  // 标准合规性说明
) {
    /**
     * 获取格式化的设计方案文本
     * 返回结构化、无冗余、易读的文本格式
     */
    fun getFormattedScheme(): String {
        if (idea == null) return ""

        return DesignSchemeFormatter.formatCreativeIdea(idea)
    }

    /**
     * 获取格式化的设计方案文本（带身高范围）
     */
    fun getFormattedSchemeWithHeight(minHeightCm: Int, maxHeightCm: Int): String {
        if (idea == null) return ""

        return DesignSchemeFormatter.formatCreativeIdeaByHeight(minHeightCm, maxHeightCm, idea)
    }

    /**
     * 获取卡片数据（用于UI展示）
     */
    fun getCardData(): SchemeCardData? {
        return idea?.let { DesignSchemeFormatter.formatToCardData(it) }
    }
}


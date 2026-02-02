package com.childproduct.designassistant.model

import com.childproduct.designassistant.model.engineering.ProductType

/**
 * 合规参数模型
 * 存储儿童安全座椅/儿童产品的关键合规指标
 */
data class ComplianceParameters(
    val dummyType: ComplianceDummy,    // 假人类型（新增，作为主要判断依据）
    val hicLimit: Int = 1000,          // HIC (头部伤害准则) 极限值，默认1000 (FMVSS 213标准)
    val chestAccelerationLimit: Int = 55, // 胸部加速度极限，默认55g
    val neckTensionLimit: Int = 1800,  // 颈部张力极限，默认1800N
    val neckCompressionLimit: Int = 2200, // 颈部压缩极限，默认2200N
    val headExcursionLimit: Int = 550, // 头部位移极限，默认550mm
    val kneeExcursionLimit: Int = 650, // 膝部位移极限，默认650mm
    val chestDeflectionLimit: Int = 52 // 胸部位移极限，默认52mm
) {
    companion object {
        /**
         * 根据假人类型返回精确参数（基于UN R129标准）
         */
        fun getByDummy(dummyType: ComplianceDummy): ComplianceParameters {
            return when (dummyType) {
                // Q0/Q0+/Q1假人（0-18个月）：最严格
                ComplianceDummy.Q0, ComplianceDummy.Q0_PLUS, ComplianceDummy.Q1 -> ComplianceParameters(
                    dummyType = dummyType,
                    hicLimit = 390,  // 最严格
                    chestAccelerationLimit = 55,
                    neckTensionLimit = 1800,
                    neckCompressionLimit = 2200,
                    headExcursionLimit = 550,
                    kneeExcursionLimit = 650,
                    chestDeflectionLimit = 52
                )
                // Q1.5假人（18-36个月）：中等严格
                ComplianceDummy.Q1_5 -> ComplianceParameters(
                    dummyType = dummyType,
                    hicLimit = 570,  // Q1.5专用标准
                    chestAccelerationLimit = 55,
                    neckTensionLimit = 1800,
                    neckCompressionLimit = 2200,
                    headExcursionLimit = 550,
                    kneeExcursionLimit = 650,
                    chestDeflectionLimit = 52
                )
                // Q3/Q3s假人（3-12岁）：标准严格度
                ComplianceDummy.Q3, ComplianceDummy.Q3_S -> ComplianceParameters(
                    dummyType = dummyType,
                    hicLimit = 1000,  // Q3标准
                    chestAccelerationLimit = 60,  // 胸部加速度放宽到60g
                    neckTensionLimit = 2000,  // 颈部张力放宽
                    neckCompressionLimit = 2500,  // 颈部压缩放宽
                    headExcursionLimit = 550,
                    kneeExcursionLimit = 650,
                    chestDeflectionLimit = 52
                )
                // Q6/Q10假人（7岁以上）：严格度适中
                ComplianceDummy.Q6, ComplianceDummy.Q10 -> ComplianceParameters(
                    dummyType = dummyType,
                    hicLimit = 1000,
                    chestAccelerationLimit = 60,
                    neckTensionLimit = 2000,
                    neckCompressionLimit = 2500,
                    headExcursionLimit = 550,
                    kneeExcursionLimit = 650,
                    chestDeflectionLimit = 52
                )
            }
        }

        /**
         * 根据年龄段返回默认参数（兼容旧版本，内部转换为假人类型）
         * 注意：此方法已废弃，建议使用getByDummy()以获得更精确的参数
         */
        @Deprecated("Use getByDummy instead for more accurate parameters")
        fun getDefaultForAgeGroup(ageGroup: AgeGroup): ComplianceParameters {
            val dummyType = ComplianceDummy.getByAgeGroup(ageGroup)
            return getByDummy(dummyType)
        }
    }
}

/**
 * 标准关联模型
 * 记录设计方案对应的标准条款
 */
data class StandardsReference(
    val mainStandard: String,        // 主标准名称
    val keyClauses: List<String>,    // 关键条款
    val complianceRequirements: List<String>, // 合规要求
    val dummyTypes: List<String> = emptyList() // 适用的假人类型
) {
    companion object {
        /**
         * 根据产品类型生成标准关联
         */
        fun getDefaultForProductType(productType: ProductType): StandardsReference {
            return when (productType) {
                ProductType.CHILD_SAFETY_SEAT -> StandardsReference(
                    mainStandard = "ECE R129 + GB 27887-2024 + FMVSS 213",
                    keyClauses = listOf(
                        // ECE R129标准
                        "ECE R129 §5.2: 假人分类（Q0-Q10）",
                        "ECE R129 §7: 动态测试要求",
                        "ECE R129 §7.1.2: HIC15 ≤ 390 (Q0/Q0+/Q1), HIC36 ≤ 570 (Q1.5), HIC36 ≤ 1000 (Q3/Q3s/Q6/Q10)",
                        "ECE R129 §7.1.3: 胸部加速度 ≤ 55g (Q0-Q1.5), ≤ 60g (Q3+)",
                        "ECE R129 §7.1.4: 颈部张力 ≤ 1800N (Q0-Q1.5), ≤ 2000N (Q3+)",
                        "ECE R129 §7.1.5: 头部位移 ≤ 550mm",
                        "ECE R129 §7.1.6: 膝部位移 ≤ 650mm",
                        "ECE R129 §7.1.7: 胸部位移 ≤ 52mm",
                        // GB 27887-2024标准
                        "GB 27887-2024 §5.3: 身高范围要求（40-150cm）",
                        "GB 27887-2024 §6.4: 动态测试性能要求",
                        "GB 27887-2024 §6.4.1: 头部伤害准则HIC15 ≤ 324",
                        "GB 27887-2024 §6.4.2: 胸部合成加速度3ms ≤ 55g",
                        "GB 27887-2024 §6.4.3: 颈部伤害指标Nij ≤ 1.0",
                        // FMVSS标准
                        "FMVSS 302: 燃烧性能",
                        "FMVSS 213 §S5: 动态测试要求",
                        "FMVSS 213 §S5.2: HIC15 ≤ 324",
                        "FMVSS 213 §S5.3: 胸部加速度 ≤ 55g",
                        "FMVSS 213 §S5.4: 膝部位移 ≤ 915mm"
                    ),
                    complianceRequirements = listOf(
                        // 身高-假人映射要求
                        "身高 < 50cm 使用 Q0 假人（新生儿）",
                        "身高 50-60cm 使用 Q0+ 假人（大婴儿）",
                        "身高 60-75cm 使用 Q1 假人（幼儿）",
                        "身高 75-87cm 使用 Q1.5 假人（学步儿童）",
                        "身高 87-105cm 使用 Q3 假人（学前儿童）",
                        "身高 105-125cm 使用 Q3s 假人（儿童）",
                        "身高 125-145cm 使用 Q6 假人（大龄儿童）",
                        "身高 145-150cm 使用 Q10 假人（青少年）",
                        // 动态测试要求
                        "HIC15 ≤ 324 (Q0-Q1.5) 或 HIC36 ≤ 1000 (Q3+)",
                        "胸部加速度 ≤ 55g (Q0-Q1.5) 或 ≤ 60g (Q3+)",
                        "颈部张力 ≤ 1800N (Q0-Q1.5) 或 ≤ 2000N (Q3+)",
                        "颈部压缩 ≤ 2200N (Q0-Q1.5) 或 ≤ 2500N (Q3+)",
                        "头部位移 ≤ 550mm",
                        "膝部位移 ≤ 650mm",
                        "胸部位移 ≤ 52mm",
                        // 材料要求
                        "阻燃面料燃烧速度 < 4英寸/分钟 (FMVSS 302)",
                        "ISOFIX连接件静态强度 >= 8kN (ECE R129)",
                        "五点式安全带抗拉强度 >= 2000N"
                    ),
                    dummyTypes = listOf(
                        "Q0 (新生儿, 0-50cm)",
                        "Q0+ (大婴儿, 50-60cm)",
                        "Q1 (幼儿, 60-75cm)",
                        "Q1.5 (学步儿童, 75-87cm)",
                        "Q3 (学前儿童, 87-105cm)",
                        "Q3s (儿童, 105-125cm)",
                        "Q6 (大龄儿童, 125-145cm)",
                        "Q10 (青少年, 145-150cm)"
                    )
                )
                ProductType.STROLLER -> StandardsReference(
                    mainStandard = "EN 1888-2:2018 + GB 14748-2020",
                    keyClauses = listOf(
                        "GB 14748-2020 §5.3: 稳定性要求",
                        "GB 14748-2020 §5.4: 制动系统",
                        "GB 14748-2020 §5.5: 锁定装置",
                        "EN 1888-2 §4.2: 折叠机构安全",
                        "EN 1888-2 §4.3: 危险点要求",
                        "EN 1888-2 §5.1: 动态耐久性测试"
                    ),
                    complianceRequirements = listOf(
                        "制动系统锁定可靠性：在10°斜面上不滑行",
                        "折叠机构安全防夹：手指探针不能插入",
                        "危险点圆角R >= 2.5mm",
                        "动态耐久性：通过36,000次循环测试",
                        "静态强度：座椅承受100kg重量不变形",
                        "锁定装置：手动释放力 >= 50N"
                    )
                )
                else -> StandardsReference(
                    mainStandard = productType.mainStandards,
                    keyClauses = emptyList(),
                    complianceRequirements = emptyList()
                )
            }
        }
    }
}

/**
 * 材质规格模型
 * 存储详细的材质要求和规格
 */
data class MaterialSpecs(
    val flameRetardantFabric: String = "通过FMVSS 302认证的阻燃面料",
    val isoFixComponents: String = "高强度钢材ISOFIX连接件，抗拉强度>= 450MPa",
    val impactAbsorber: String = "EPP/EPS吸能材料",
    val additionalSpecs: List<String> = emptyList()
) {
    companion object {
        // 根据产品类型生成材质规格
        fun getDefaultForProductType(productType: ProductType): MaterialSpecs {
            return when (productType) {
                ProductType.CHILD_SAFETY_SEAT -> MaterialSpecs(
                    flameRetardantFabric = "通过FMVSS 302认证的阻燃面料，燃烧速度< 4英寸/分钟",
                    isoFixComponents = "高强度钢材ISOFIX连接件，抗拉强度>= 450MPa",
                    impactAbsorber = "三层复合EPP/EPS吸能材料，密度30-50kg/m³",
                    additionalSpecs = listOf(
                        "五点式安全带：聚酯纤维，抗拉强度>= 2000N",
                        "调节机构：锌合金材质，防锈处理",
                        "靠背骨架：高强度PP+玻璃纤维"
                    )
                )
                else -> MaterialSpecs()
            }
        }
    }
}

/**
 * 创意设计方案模型
 * 扩展版本，包含合规参数、标准关联和材质规格
 */
data class CreativeIdea(
    val id: String,
    val title: String,
    val description: String,
    val ageGroup: AgeGroup,
    val productType: ProductType,
    val theme: String,
    val features: List<String>,
    val materials: List<String>,
    val colorPalette: List<String>,
    val safetyNotes: List<String>,
    val complianceParameters: ComplianceParameters? = null,
    val standardsReference: StandardsReference? = null,
    val materialSpecs: MaterialSpecs? = null
) {
    /**
     * 自定义toString方法，返回格式化的摘要而不是完整的对象表示
     * 这样可以避免在日志或调试时输出大量冗余信息
     */
    override fun toString(): String {
        return buildString {
            append("CreativeIdea(id=$id, title=$title, ageGroup=${ageGroup.displayName}, productType=${productType.displayName})")
        }
    }
}

/**
 * 合规假人类型枚举（基于ECE R129标准，用于合规性检查）
 */
enum class ComplianceDummy(val displayName: String, val heightRange: String, val weight: Float, val age: String, val hicLimit: Int) {
    Q0("Q0新生儿假人", "出生-50cm", 2.5f, "0-6个月", 390),
    Q0_PLUS("Q0+大婴儿假人", "50-60cm", 4.0f, "6-9个月", 390),
    Q1("Q1幼儿假人", "60-75cm", 9.0f, "9-18个月", 390),
    Q1_5("Q1.5学步儿童假人", "75-87cm", 11.0f, "18-36个月", 570),
    Q3("Q3学前儿童假人", "87-105cm", 15.0f, "3-4岁", 1000),
    Q3_S("Q3s儿童假人", "105-125cm", 21.0f, "4-7岁", 1000),
    Q6("Q6大龄儿童假人", "125-145cm", 33.0f, "7-10岁", 1000),
    Q10("Q10青少年假人", "145-150cm", 38.0f, "10岁以上", 1000);

    companion object {
        /**
         * 根据身高获取对应的假人类型
         */
        fun getByHeight(heightCm: Int): ComplianceDummy {
            return when {
                heightCm < 50 -> Q0
                heightCm < 60 -> Q0_PLUS
                heightCm < 75 -> Q1
                heightCm < 87 -> Q1_5
                heightCm < 105 -> Q3
                heightCm < 125 -> Q3_S
                heightCm < 145 -> Q6
                else -> Q10
            }
        }

        /**
         * 根据年龄段获取推荐的假人类型（返回中间值）
         */
        fun getByAgeGroup(ageGroup: AgeGroup): ComplianceDummy {
            return when (ageGroup) {
                AgeGroup.INFANT -> Q1  // 0-3岁中间值
                AgeGroup.TODDLER -> Q1_5  // 3-6岁中间值
                AgeGroup.PRESCHOOL -> Q3  // 6-9岁中间值（取较小值保守）
                AgeGroup.SCHOOL_AGE -> Q6  // 9-12岁中间值
                AgeGroup.TEEN -> Q10
                AgeGroup.ALL -> Q3  // 默认值
            }
        }

        /**
         * 获取假人类型对应的产品分组
         */
        fun getProductGroup(dummy: ComplianceDummy): String {
            return when (dummy) {
                Q0, Q0_PLUS, Q1 -> "Group 0+ (0-13kg)"
                Q1_5 -> "Group 1 (9-18kg)"
                Q3, Q3_S -> "Group 2/3 (15-36kg)"
                Q6, Q10 -> "Group 2/3 (15-36kg)"
            }
        }
    }
}

enum class AgeGroup(val displayName: String, val heightRange: String, val weightRange: String, val ageRangeMonths: String) {
    INFANT("0-3岁", "50-87cm", "2.5-11kg", "0-36个月"),
    TODDLER("3-6岁", "87-105cm", "11-15kg", "36-72个月"),
    PRESCHOOL("6-9岁", "105-125cm", "15-21kg", "72-108个月"),
    SCHOOL_AGE("9-12岁", "125-145cm", "21-33kg", "108-144个月"),
    /**
     * 注意：根据ECE R129/GB 27887-2024标准，儿童安全座椅的适用年龄上限为12岁
     * 145-150cm对应Q10假人，仍属于10-12岁年龄段，而非12岁以上
     */
    TEEN("10-12岁", "145-150cm", "33-38kg", "120-144个月"),
    /**
     * 全年龄段（强制映射：身高40-150cm → 0-12岁）
     */
    ALL("0-12岁", "40-150cm", "2.5-38kg", "0-144个月")
}

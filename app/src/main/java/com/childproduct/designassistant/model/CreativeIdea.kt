package com.childproduct.designassistant.model

/**
 * 合规参数模型
 * 存储儿童安全座椅/儿童产品的关键合规指标
 */
data class ComplianceParameters(
    val hicLimit: Int = 1000,          // HIC (头部伤害准则) 极限值，默认1000 (FMVSS 213标准)
    val chestAccelerationLimit: Int = 55, // 胸部加速度极限，默认55g
    val neckTensionLimit: Int = 1800,  // 颈部张力极限，默认1800N
    val neckCompressionLimit: Int = 2200 // 颈部压缩极限，默认2200N
) {
    companion object {
        // 根据年龄段返回默认参数
        fun getDefaultForAgeGroup(ageGroup: AgeGroup): ComplianceParameters {
            return when (ageGroup) {
                AgeGroup.INFANT -> ComplianceParameters(
                    hicLimit = 390,  // 婴儿假人更严格
                    chestAccelerationLimit = 55,
                    neckTensionLimit = 1800,
                    neckCompressionLimit = 2200
                )
                AgeGroup.TODDLER -> ComplianceParameters(
                    hicLimit = 570,  // Q1.5假人
                    chestAccelerationLimit = 55,
                    neckTensionLimit = 1800,
                    neckCompressionLimit = 2200
                )
                AgeGroup.PRESCHOOL -> ComplianceParameters(
                    hicLimit = 1000, // Q3/Q3s假人
                    chestAccelerationLimit = 60,
                    neckTensionLimit = 2000,
                    neckCompressionLimit = 2500
                )
                else -> ComplianceParameters() // 使用默认值
            }
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
    val complianceRequirements: List<String> // 合规要求
) {
    companion object {
        // 根据产品类型生成标准关联
        fun getDefaultForProductType(productType: ProductType): StandardsReference {
            return when (productType) {
                ProductType.CHILD_SAFETY_SEAT -> StandardsReference(
                    mainStandard = "ECE R129 + FMVSS 213",
                    keyClauses = listOf(
                        "FMVSS 302: 燃烧性能",
                        "FMVSS 213: 儿童约束系统",
                        "UN R129: i-Size认证"
                    ),
                    complianceRequirements = listOf(
                        "阻燃面料燃烧速度< 4英寸/分钟",
                        "ISOFIX连接件静态强度>= 8kN",
                        "HIC值<= 1000 (Q3s假人)",
                        "胸部加速度<= 55g"
                    )
                )
                ProductType.BABY_STROLLER -> StandardsReference(
                    mainStandard = "EN 1888 + GB 14748-2020",
                    keyClauses = listOf(
                        "GB 14748-2020: 婴儿推车安全",
                        "EN 1888-1: 推车稳定性"
                    ),
                    complianceRequirements = listOf(
                        "制动系统锁定可靠性",
                        "折叠机构安全防夹",
                        "危险点圆角R>= 2.5mm"
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
)

enum class AgeGroup(val displayName: String) {
    ALL("全年龄段"),
    INFANT("0-3岁"),
    TODDLER("3-6岁"),
    PRESCHOOL("6-9岁"),
    SCHOOL_AGE("9-12岁"),
    TEEN("12岁以上")
}

enum class ProductType(val displayName: String, val standardAbbr: String, val mainStandards: String) {
    CHILD_SAFETY_SEAT("儿童安全座椅", "ECE/GB", "主标准：ECE R129、GB 27887-2024"),
    BABY_STROLLER("婴儿推车", "EN/GB", "主标准：EN 1888、GB 14748-2020"),
    CHILD_HOUSEHOLD_GOODS("儿童家庭用品", "ISO/GB", "主标准：ISO 8124-3、GB 28007-2011"),
    CHILD_HIGH_CHAIR("儿童高脚椅", "ISO/GB", "主标准：ISO 8124-3、GB 28007-2011")
}

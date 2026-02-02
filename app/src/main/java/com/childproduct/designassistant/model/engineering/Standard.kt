package com.childproduct.designassistant.model.engineering

/**
 * 标准模型 - 工程级标准定义
 * 严格遵循标准隔离原则，每个标准独立定义其参数，防止混用
 */
enum class Standard(
    val code: String,
    val displayName: String,
    val region: String,
    val currentVersion: String,
    val effectiveDate: String,
    val nextAmendment: AmendmentInfo?,
    val supportedProducts: List<ProductType>
) {
    /**
     * ECE R129 (i-Size)
     * 联合国欧洲经济委员会法规，适用于欧洲、中国、澳大利亚等
     */
    ECE_R129(
        code = "UN R129",
        displayName = "ECE R129 (i-Size)",
        region = "ECE",
        currentVersion = "Rev.4",
        effectiveDate = "2018-12-29",
        nextAmendment = AmendmentInfo(
            amendmentId = "03 series",
            expectedDate = "2026-Q3",
            keyChanges = listOf(
                "新增侧面碰撞测试要求",
                "扩展Q3s假人使用范围",
                "强化防旋转装置要求"
            )
        ),
        supportedProducts = listOf(
            ProductType.CHILD_SAFETY_SEAT,
            ProductType.CRIB
        )
    ),

    /**
     * GB 27887-2024
     * 中国强制性国家标准，等同采用ECE R129
     */
    GB_27887_2024(
        code = "GB 27887-2024",
        displayName = "GB 27887-2024 (等同i-Size)",
        region = "CN",
        currentVersion = "2024版",
        effectiveDate = "2024-05-28",
        nextAmendment = null,
        supportedProducts = listOf(
            ProductType.CHILD_SAFETY_SEAT,
            ProductType.CRIB
        )
    ),

    /**
     * FMVSS 213
     * 美国联邦机动车安全标准
     */
    FMVSS_213(
        code = "FMVSS 213",
        displayName = "FMVSS 213",
        region = "USA",
        currentVersion = "Rev.8",
        effectiveDate = "2023-03-15",
        nextAmendment = AmendmentInfo(
            amendmentId = "Rev.9",
            expectedDate = "2026-Q2",
            keyChanges = listOf(
                "更新侧面碰撞测试要求",
                "强化织带强度标准"
            )
        ),
        supportedProducts = listOf(
            ProductType.CHILD_SAFETY_SEAT,
            ProductType.CRIB
        )
    ),

    /**
     * EN 1888
     * 欧洲婴儿推车标准
     */
    EN_1888(
        code = "EN 1888:2012+A1:2018",
        displayName = "EN 1888 (推车)",
        region = "EU",
        currentVersion = "A1:2018",
        effectiveDate = "2018-08-28",
        nextAmendment = null,
        supportedProducts = listOf(
            ProductType.STROLLER
        )
    ),

    /**
     * GB 14748-2020
     * 中国婴儿推车标准
     */
    GB_14748_2020(
        code = "GB 14748-2020",
        displayName = "GB 14748-2020 (推车)",
        region = "CN",
        currentVersion = "2020版",
        effectiveDate = "2020-11-19",
        nextAmendment = null,
        supportedProducts = listOf(
            ProductType.STROLLER
        )
    ),

    /**
     * EN 14988
     * 欧洲儿童高脚椅标准
     */
    EN_14988(
        code = "EN 14988:2017",
        displayName = "EN 14988 (高脚椅)",
        region = "EU",
        currentVersion = "2017版",
        effectiveDate = "2017-08-16",
        nextAmendment = null,
        supportedProducts = listOf(
            ProductType.HIGH_CHAIR
        )
    ),

    /**
     * GB 22793-2022
     * 中国儿童高脚椅标准
     */
    GB_22793_2022(
        code = "GB 22793-2022",
        displayName = "GB 22793-2022 (高脚椅)",
        region = "CN",
        currentVersion = "2022版",
        effectiveDate = "2022-07-11",
        nextAmendment = null,
        supportedProducts = listOf(
            ProductType.HIGH_CHAIR
        )
    );

    /**
     * 检查标准是否支持指定产品类型
     */
    fun supportsProduct(productType: ProductType): Boolean {
        return supportedProducts.contains(productType)
    }

    /**
     * 获取标准安全参数（标准隔离，无混用）
     */
    fun getSafetyParameters(): SafetyParameters {
        return when (this) {
            ECE_R129, GB_27887_2024 -> SafetyParameters(
                headInjuryCriteria = listOf(
                    SafetyParameter(
                        name = "HIC15",
                        value = "≤390",
                        unit = "",
                        appliesToDummies = listOf("Q0", "Q0+", "Q1", "Q1.5", "Q3"),
                        clause = "ECE R129 §7.1.2"
                    ),
                    SafetyParameter(
                        name = "HIC36",
                        value = "≤1000",
                        unit = "",
                        appliesToDummies = listOf("Q3s", "Q6", "Q10"),
                        clause = "ECE R129 §7.1.2"
                    )
                ),
                chestAcceleration = listOf(
                    SafetyParameter(
                        name = "3ms",
                        value = "≤55g",
                        unit = "g",
                        appliesToDummies = listOf("Q0", "Q0+", "Q1", "Q1.5"),
                        clause = "ECE R129 §7.1.3"
                    ),
                    SafetyParameter(
                        name = "3ms",
                        value = "≤60g",
                        unit = "g",
                        appliesToDummies = listOf("Q3", "Q3s", "Q6", "Q10"),
                        clause = "ECE R129 §7.1.3"
                    )
                ),
                neckTension = listOf(
                    SafetyParameter(
                        name = "Peak",
                        value = "≤1800N",
                        unit = "N",
                        appliesToDummies = listOf("Q0", "Q0+", "Q1", "Q1.5"),
                        clause = "ECE R129 §7.1.4"
                    ),
                    SafetyParameter(
                        name = "Peak",
                        value = "≤2000N",
                        unit = "N",
                        appliesToDummies = listOf("Q3", "Q3s", "Q6", "Q10"),
                        clause = "ECE R129 §7.1.4"
                    )
                ),
                neckCompression = listOf(
                    SafetyParameter(
                        name = "Peak",
                        value = "≤2200N",
                        unit = "N",
                        appliesToDummies = listOf("Q0", "Q0+", "Q1", "Q1.5"),
                        clause = "ECE R129 §7.1.4"
                    ),
                    SafetyParameter(
                        name = "Peak",
                        value = "≤2500N",
                        unit = "N",
                        appliesToDummies = listOf("Q3", "Q3s", "Q6", "Q10"),
                        clause = "ECE R129 §7.1.4"
                    )
                )
            )
            FMVSS_213 -> SafetyParameters(
                headInjuryCriteria = listOf(
                    SafetyParameter(
                        name = "HIC36",
                        value = "≤1000",
                        unit = "",
                        appliesToDummies = listOf("CRABI", "Hybrid III 3yr", "Hybrid III 6yr"),
                        clause = "FMVSS 213 S5.3.2"
                    )
                ),
                chestAcceleration = listOf(
                    SafetyParameter(
                        name = "3ms",
                        value = "≤60g",
                        unit = "g",
                        appliesToDummies = listOf("CRABI", "Hybrid III 3yr", "Hybrid III 6yr"),
                        clause = "FMVSS 213 S5.3.2"
                    )
                ),
                neckTension = emptyList(),
                neckCompression = emptyList()
            )
            EN_1888, GB_14748_2020 -> SafetyParameters(
                headInjuryCriteria = emptyList(),
                chestAcceleration = emptyList(),
                neckTension = emptyList(),
                neckCompression = emptyList()
            )
            EN_14988, GB_22793_2022 -> SafetyParameters(
                headInjuryCriteria = emptyList(),
                chestAcceleration = emptyList(),
                neckTension = emptyList(),
                neckCompression = emptyList()
            )
        }
    }

    /**
     * 获取测试要求
     */
    fun getTestRequirements(): TestRequirements {
        return when (this) {
            ECE_R129, GB_27887_2024 -> TestRequirements(
                frontalImpact = listOf(
                    TestCondition(
                        velocity = "50 km/h",
                        deceleration = "28-32 g",
                        description = "正面碰撞测试"
                    )
                ),
                sideImpact = listOf(
                    TestCondition(
                        velocity = "24 km/h",
                        deceleration = "N/A",
                        description = "侧面碰撞测试（如适用）"
                    )
                ),
                rearImpact = listOf(
                    TestCondition(
                        velocity = "30 km/h",
                        deceleration = "20-25 g",
                        description = "后向碰撞测试"
                    )
                )
            )
            FMVSS_213 -> TestRequirements(
                frontalImpact = listOf(
                    TestCondition(
                        velocity = "48 km/h",
                        deceleration = "28-32 g",
                        description = "正面碰撞测试"
                    )
                ),
                sideImpact = emptyList(),
                rearImpact = emptyList()
            )
            else -> TestRequirements(
                frontalImpact = emptyList(),
                sideImpact = emptyList(),
                rearImpact = emptyList()
            )
        }
    }

    companion object {
        /**
         * 根据代码查找标准
         */
        fun fromCode(code: String): Standard? {
            return values().find { it.code == code }
        }

        /**
         * 获取所有适用于指定产品的标准
         */
        fun getStandardsForProduct(productType: ProductType): List<Standard> {
            return values().filter { it.supportsProduct(productType) }
        }
    }
}

/**
 * 修订信息
 */
data class AmendmentInfo(
    val amendmentId: String,
    val expectedDate: String,
    val keyChanges: List<String>
)

/**
 * 安全参数（标准隔离）
 */
data class SafetyParameter(
    val name: String,
    val value: String,
    val unit: String,
    val appliesToDummies: List<String>,
    val clause: String
)

/**
 * 安全参数集合
 */
data class SafetyParameters(
    val headInjuryCriteria: List<SafetyParameter>,
    val chestAcceleration: List<SafetyParameter>,
    val neckTension: List<SafetyParameter>,
    val neckCompression: List<SafetyParameter>
)

/**
 * 测试条件
 */
data class TestCondition(
    val velocity: String,
    val deceleration: String,
    val description: String
)

/**
 * 测试要求
 */
data class TestRequirements(
    val frontalImpact: List<TestCondition>,
    val sideImpact: List<TestCondition>,
    val rearImpact: List<TestCondition>
)

/**
 * 产品类型
 */
enum class ProductType(val displayName: String) {
    CHILD_SAFETY_SEAT("儿童安全座椅"),
    CHILD_STROLLER("婴儿推车"),
    CHILD_HOUSEHOLD_GOODS("儿童家庭用品"),
    CHILD_HIGH_CHAIR("儿童高脚椅"),
    CRIB("儿童床"),
    
    // 兼容旧代码
    SAFETY_SEAT("安全座椅"),
    STROLLER("婴儿推车"),
    HIGH_CHAIR("儿童高脚椅")
}

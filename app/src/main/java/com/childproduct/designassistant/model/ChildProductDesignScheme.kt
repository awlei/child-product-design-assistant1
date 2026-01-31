package com.childproduct.designassistant.model

import com.childproduct.designassistant.config.StandardConfig
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

/**
 * 标准化设计方案数据模型（优化点：不可变集合+封装+懒加载+构建器模式）
 *
 * 核心优化：
 * - 使用不可变集合（ImmutableList/ImmutableMap）防止外部修改
 * - 懒加载验证结果，避免重复计算
 * - 构建器模式简化对象创建，减少冗余代码
 * - 增强标准合规性校验
 * - 所有集合字段自动去重
 */
data class ChildProductDesignScheme(
    // ========== 基本信息 ==========
    val productType: String,                           // 产品类型（如：儿童安全座椅）
    val heightRange: String,                          // 身高范围（如：40-150cm）
    val ageRange: String,                             // 适配年龄段（标准值）
    val designTheme: String,                          // 设计主题（如：拼图游戏）

    // ========== 核心设计 ==========
    val installMethodDesc: String,                    // 安装方式描述（联动InstallMethod）
    val coreFeatures: ImmutableList<String>,          // 核心特点（不可变列表，已去重）
    val recommendMaterials: ImmutableList<String>,    // 推荐材料（不可变列表，已去重）

    // ========== 合规参数 ==========
    val complianceStandards: ImmutableList<String>,   // 合规标准（不可变列表，已去重）
    val dummyType: String,                            // 适配假人类型（标准值）
    val safetyThresholds: ImmutableMap<String, String>, // 安全阈值（不可变Map）
    val testMatrix: ImmutableList<TestMatrixItem>,    // 测试矩阵（不可变列表，已去重）

    // ========== 安全提示 ==========
    val safetyNotes: ImmutableList<String>            // 安全注意事项（不可变列表，已去重）
) {
    // 懒加载验证结果（避免重复计算）
    val validationResult: ValidationResult by lazy { validate() }

    /**
     * 验证数据完整性（优化：增加标准合规性校验）
     */
    fun validate(): ValidationResult {
        val errors = mutableListOf<String>()

        // 基础非空校验
        if (productType.isBlank()) errors.add("产品类型不能为空")
        if (heightRange.isBlank()) errors.add("身高范围不能为空")
        if (ageRange.isBlank()) errors.add("年龄段不能为空")
        if (coreFeatures.isEmpty()) errors.add("核心特点不能为空")
        if (recommendMaterials.isEmpty()) errors.add("推荐材料不能为空")
        if (complianceStandards.isEmpty()) errors.add("合规标准不能为空")
        if (dummyType.isBlank()) errors.add("假人类型不能为空")
        if (safetyThresholds.isEmpty()) errors.add("安全阈值不能为空")
        if (safetyNotes.isEmpty()) errors.add("安全注意事项不能为空")

        // 标准合规性校验
        val heightConfig = StandardConfig.getHeightConfig(heightRange)
        if (heightConfig == null && heightRange != "40-150cm") {
            errors.add("身高范围${heightRange}不符合${StandardConfig.STANDARD_VERSION}标准")
        }

        // 假人类型合规性校验
        if (dummyType.isNotBlank() && !dummyType.contains(Regex("Q[0-9](\\.|_)?[0-9]?"))) {
            errors.add("假人类型${dummyType}不符合标准格式（如Q0、Q1.5、Q10）")
        }

        // 安全阈值完整性校验
        val requiredThresholds = listOf(
            "HIC极限值", "胸部加速度", "颈部张力极限",
            "颈部压缩极限", "头部位移极限", "膝部位移极限"
        )
        requiredThresholds.forEach { threshold ->
            if (!safetyThresholds.containsKey(threshold)) {
                errors.add("缺少安全阈值：$threshold")
            }
        }

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors.toImmutableList() // 转为不可变集合
        )
    }

    /**
     * 验证结果（使用不可变集合）
     */
    data class ValidationResult(
        val isValid: Boolean,
        val errors: ImmutableList<String>
    ) {
        companion object {
            /**
             * 创建成功的验证结果
             */
            fun success() = ValidationResult(isValid = true, errors = emptyList())

            /**
             * 创建失败的验证结果
             */
            fun failure(errors: List<String>) = ValidationResult(
                isValid = false,
                errors = errors.toImmutableList()
            )
        }
    }

    // 构建器模式（简化对象创建）
    companion object {
        fun builder(productType: String, heightRange: String): Builder {
            return Builder(productType, heightRange)
        }
    }

    /**
     * 构建器类（优化对象创建体验，减少冗余代码）
     */
    class Builder(
        private val productType: String,
        private val heightRange: String
    ) {
        private var ageRange: String = ""
        private var designTheme: String = ""
        private var installMethodDesc: String = ""
        private var coreFeatures: List<String> = emptyList()
        private var recommendMaterials: List<String> = StandardConfig.RECOMMENDED_MATERIALS
        private var complianceStandards: List<String> = StandardConfig.COMPLIANCE_STANDARDS
        private var dummyType: String = ""
        private var safetyThresholds: Map<String, String> = StandardConfig.SAFETY_THRESHOLDS
        private var testMatrix: List<TestMatrixItem> = emptyList()
        private var safetyNotes: List<String> = StandardConfig.SAFETY_NOTES

        fun ageRange(ageRange: String) = apply { this.ageRange = ageRange }
        fun designTheme(designTheme: String) = apply { this.designTheme = designTheme }
        fun installMethodDesc(installMethodDesc: String) = apply { this.installMethodDesc = installMethodDesc }
        fun coreFeatures(coreFeatures: List<String>) = apply { this.coreFeatures = coreFeatures }
        fun recommendMaterials(recommendMaterials: List<String>) = apply { this.recommendMaterials = recommendMaterials }
        fun complianceStandards(complianceStandards: List<String>) = apply { this.complianceStandards = complianceStandards }
        fun dummyType(dummyType: String) = apply { this.dummyType = dummyType }
        fun safetyThresholds(safetyThresholds: Map<String, String>) = apply { this.safetyThresholds = safetyThresholds }
        fun testMatrix(testMatrix: List<TestMatrixItem>) = apply { this.testMatrix = testMatrix }
        fun safetyNotes(safetyNotes: List<String>) = apply { this.safetyNotes = safetyNotes }

        /**
         * 构建对象（自动去重+转为不可变集合）
         */
        fun build(): ChildProductDesignScheme {
            return ChildProductDesignScheme(
                productType = productType,
                heightRange = heightRange,
                ageRange = ageRange,
                designTheme = designTheme,
                installMethodDesc = installMethodDesc,
                coreFeatures = coreFeatures.distinct().toImmutableList(),
                recommendMaterials = recommendMaterials.distinct().toImmutableList(),
                complianceStandards = complianceStandards.distinct().toImmutableList(),
                dummyType = dummyType,
                safetyThresholds = safetyThresholds.toImmutableMap(),
                testMatrix = testMatrix.distinct().toImmutableList(),
                safetyNotes = safetyNotes.distinct().toImmutableList()
            )
        }
    }
}

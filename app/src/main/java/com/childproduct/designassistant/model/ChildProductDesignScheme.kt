package com.childproduct.designassistant.model

/**
 * 标准化设计方案数据模型（替代杂乱代码字段，消除乱码/冗余根源）
 *
 * 核心优化：
 * - 使用结构化字段替代代码式字符串
 * - 强制使用标准值（如年龄段、假人类型）
 * - 所有列表字段自动去重
 */
data class ChildProductDesignScheme(
    // ========== 基本信息 ==========
    val productType: String,                    // 产品类型（如：儿童安全座椅）
    val heightRange: String,                   // 身高范围（如：40-150cm）
    val ageRange: String,                      // 适配年龄段（标准值）
    val designTheme: String,                   // 设计主题（如：拼图游戏）

    // ========== 核心设计 ==========
    val coreFeatures: List<String>,            // 核心特点（去重后）
    val recommendMaterials: List<String>,      // 推荐材料（去重后）

    // ========== 合规参数 ==========
    val complianceStandards: List<String>,     // 合规标准（去重后）
    val dummyType: String,                     // 适配假人类型（标准值）
    val safetyThresholds: Map<String, String>, // 安全阈值（无重复）

    // ========== 安全提示 ==========
    val safetyNotes: List<String>              // 安全注意事项（去重后）
) {
    /**
     * 验证数据完整性
     */
    fun validate(): ValidationResult {
        val errors = mutableListOf<String>()

        if (productType.isEmpty()) errors.add("产品类型不能为空")
        if (heightRange.isEmpty()) errors.add("身高范围不能为空")
        if (ageRange.isEmpty()) errors.add("年龄段不能为空")
        if (coreFeatures.isEmpty()) errors.add("核心特点不能为空")
        if (recommendMaterials.isEmpty()) errors.add("推荐材料不能为空")
        if (complianceStandards.isEmpty()) errors.add("合规标准不能为空")
        if (dummyType.isEmpty()) errors.add("假人类型不能为空")
        if (safetyThresholds.isEmpty()) errors.add("安全阈值不能为空")
        if (safetyNotes.isEmpty()) errors.add("安全注意事项不能为空")

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }

    /**
     * 验证结果
     */
    data class ValidationResult(
        val isValid: Boolean,
        val errors: List<String>
    )
}

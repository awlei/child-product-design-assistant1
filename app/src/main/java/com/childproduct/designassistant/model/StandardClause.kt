package com.childproduct.designassistant.model

import com.childproduct.designassistant.model.engineering.ProductType

/**
 * 标准条款数据模型
 * 用于支持条款溯源和直达功能
 */
data class StandardClause(
    val standardName: String,        // 标准名称，如 "ECE R129:2013"
    val clauseId: String,            // 条款标识，如 "§5.5.1"
    val clauseTitle: String,         // 条款标题
    val clauseContent: String,       // 条款内容（原文片段）
    val clauseType: ClauseType,      // 条款类型
    val relatedSections: List<String> = emptyList()  // 相关章节
) {
    /**
     * 获取完整条款引用
     */
    fun getFullReference(): String = "$standardName $clauseId"
}

/**
 * 条款类型枚举
 */
enum class ClauseType {
    REQUIREMENT,           // 强制要求
    RECOMMENDATION,        // 推荐要求
    TESTING_METHOD,        // 测试方法
    ACCEPTANCE_CRITERIA,   // 验收标准
    DIMENSIONAL_SPEC,      // 尺寸规格
    MATERIAL_SPEC,         // 材料标准
    QUALITY_CONTROL,       // 质量控制
    USER_INFORMATION,      // 用户信息
    CERTIFICATION,         // 认证要求
    INSTALLATION           // 安装要求
}

/**
 * 标准匹配结果
 */
data class StandardMatchResult(
    val standardName: String,              // 标准名称
    val productClassification: String,     // 产品分类
    val ageRange: String,                  // 覆盖年龄区间
    val heightRange: String,               // 覆盖身高范围
    val weightRange: String,               // 覆盖重量范围
    val relevantClauses: List<StandardClause>,  // 相关条款
    val configurationRequirements: List<String> // 配置要求
) {
    /**
     * 获取配置合规性描述
     */
    fun getComplianceDescription(): String {
        return if (configurationRequirements.isEmpty()) {
            "无特殊配置要求"
        } else {
            configurationRequirements.joinToString("; ")
        }
    }
}

/**
 * 设计参数项（带条款溯源）
 */
data class DesignParameter(
    val parameterName: String,             // 设计项名称
    val specificParameter: String,        // 具体参数值
    val tolerance: String? = null,         // 公差范围
    val relatedClause: StandardClause?,    // 对应标准条款
    val remarks: String? = null            // 备注
)

/**
 * 合规测试项（关联标准测试要求）
 */
data class ComplianceTestItem(
    val testName: String,                  // 测试项名称
    val testDummy: String,                 // 测试假人
    val testConditions: String,            // 测试条件
    val acceptanceCriteria: String,        // 合格阈值
    val relatedClause: StandardClause?,    // 对应标准条款
    val testStandard: String               // 测试标准
)

/**
 * 产品配置选项
 */
data class ProductConfiguration(
    val configId: String,                  // 配置ID
    val configName: String,                // 配置名称（如 "ISOFIX接口"）
    val applicableProductTypes: List<ProductType>,  // 适用产品类型
    val isRequired: Boolean,               // 是否强制要求
    val relatedClauses: List<StandardClause>,       // 关联条款
    val description: String                // 配置描述
)

/**
 * 身高范围输入
 */
data class HeightRangeInput(
    val minHeight: String,                 // 最小身高（cm）
    val maxHeight: String                  // 最大身高（cm）
) {
    fun isValid(): Boolean {
        val min = minHeight.toIntOrNull()
        val max = maxHeight.toIntOrNull()
        return min != null && max != null && min <= max
    }

    fun getRangeString(): String = "$minHeight-$maxHeight cm"

    fun getPoundsRange(): String {
        val min = minHeight.toIntOrNull()?.let { (it * 2.20462).toInt() } ?: 0
        val max = maxHeight.toIntOrNull()?.let { (it * 2.20462).toInt() } ?: 0
        return "$min-$max lbs"
    }
}

/**
 * 重量范围输入（支持美标）
 */
data class WeightRangeInput(
    val minWeight: String,                 // 最小重量
    val maxWeight: String,                 // 最大重量
    val unit: WeightUnit = WeightUnit.KG   // 单位
) {
    fun isValid(): Boolean {
        val min = minWeight.toDoubleOrNull()
        val max = maxWeight.toDoubleOrNull()
        return min != null && max != null && min <= max
    }

    fun getRangeString(): String = "$minWeight-$maxWeight ${unit.symbol}"

    fun getKgRange(): String {
        if (unit == WeightUnit.KG) {
            return "$minWeight-$maxWeight kg"
        } else {
            val min = minWeight.toDoubleOrNull()?.div(2.20462)?.let { "%.1f".format(it) } ?: "0"
            val max = maxWeight.toDoubleOrNull()?.div(2.20462)?.let { "%.1f".format(it) } ?: "0"
            return "$min-$max kg"
        }
    }
}



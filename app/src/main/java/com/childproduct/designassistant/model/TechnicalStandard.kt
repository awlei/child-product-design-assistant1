package com.childproduct.designassistant.model

/**
 * 国际技术标准数据模型
 */
data class TechnicalStandard(
    val id: String,
    val code: String,           // 标准代码，如 ECE R129, FMVSS213
    val name: String,           // 标准名称
    val region: StandardRegion, // 适用地区
    val category: StandardCategory, // 标准类别
    val groups: List<StandardGroup>, // 适用分组
    val requirements: List<Requirement>, // 具体要求
    val lastUpdated: String,    // 最后更新日期
    val sourceUrl: String? = null // 标准源文档链接
)

enum class StandardRegion(val displayName: String) {
    EUROPE("欧洲"),
    USA("美国"),
    CHINA("中国"),
    JAPAN("日本"),
    INTERNATIONAL("国际")
}

enum class StandardCategory(val displayName: String) {
    SAFETY_SEAT("儿童安全座椅"),
    STROLLER("婴儿推车"),
    CAR_BED("车载睡床"),
    BOOSTER("增高垫"),
    CRIB("婴儿床"),
    CARRIER("婴儿提篮")
}

data class StandardGroup(
    val code: String,           // 分组代码，如 Group 0+/1/2/3
    val weightRange: String,    // 重量范围
    val heightRange: String,    // 身高范围
    val ageRange: String? = null, // 年龄范围（可选）
    val envelopeClass: String? = null // i-Size Envelope 分类（如 A/B/C）
)

data class Requirement(
    val id: String,
    val category: RequirementCategory,
    val description: String,
    val testMethod: String?,
    val passCriteria: String?
)

enum class RequirementCategory {
    IMPACT_TEST,        // 碰撞测试
    MATERIAL_SAFETY,    // 材料安全
    STRUCTURAL_INTEGRITY, // 结构完整性
    INSTALLATION,       // 安装要求
    LABELING,          // 标识要求
    FLAMMABILITY,      // 阻燃性
    CHEMICAL_SAFETY    // 化学安全
}

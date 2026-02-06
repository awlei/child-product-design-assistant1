package com.childproduct.designassistant.model

/**
 * 产品类型枚举（出行/家居分类，专业工程师易识别）
 * 新增产品仅需加枚举项+指定分类，无其他修改
 */
enum class ProductType(val typeName: String, val category: ProductCategory) {
    // 出行类
    CHILD_SEAT("儿童安全座椅", ProductCategory.TRAVEL),
    BABY_STROLLER("婴儿推车", ProductCategory.TRAVEL),
    // 家居类
    HIGH_CHAIR("儿童高脚椅", ProductCategory.HOME),
    CHILD_BED("儿童床", ProductCategory.HOME),

    // 兼容旧代码（保持向后兼容）
    SAFETY_SEAT("儿童安全座椅", ProductCategory.TRAVEL),
    STROLLER("婴儿推车", ProductCategory.TRAVEL),
    CRIB("儿童床", ProductCategory.HOME),
    CHILD_SAFETY_SEAT("儿童安全座椅", ProductCategory.TRAVEL),
    CHILD_STROLLER("婴儿推车", ProductCategory.TRAVEL),
    CHILD_HIGH_CHAIR("儿童高脚椅", ProductCategory.HOME),
    CHILD_HOUSEHOLD_GOODS("儿童床", ProductCategory.HOME);

    /** 产品大品类（出行/家居），UI分类展示用 */
    enum class ProductCategory(val categoryName: String) {
        TRAVEL("出行类"),
        HOME("家居类")
    }

    /** 兼容旧代码：获取显示名称 */
    val displayName: String
        get() = typeName

    /** 获取对应的标准枚举值（映射旧枚举到新枚举） */
    val standard: ProductType
        get() = when (this) {
            SAFETY_SEAT, CHILD_SAFETY_SEAT -> CHILD_SEAT
            STROLLER, CHILD_STROLLER -> BABY_STROLLER
            CRIB, CHILD_HOUSEHOLD_GOODS -> CHILD_BED
            CHILD_HIGH_CHAIR -> HIGH_CHAIR
            else -> this
        }

    /** 获取主要标准（用于显示和引用） */
    val mainStandards: String
        get() = when (standard) {
            CHILD_SEAT -> "ECE R129 / GB 27887-2024"
            BABY_STROLLER -> "EN 1888 / GB 14748"
            HIGH_CHAIR -> "EN 14988 / GB 29281"
            CHILD_BED -> "EN 716 / GB 28007"
        }
}

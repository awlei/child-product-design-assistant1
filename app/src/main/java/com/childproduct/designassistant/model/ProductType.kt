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
    CHILD_BED("儿童床", ProductCategory.HOME);

    /** 产品大品类（出行/家居），UI分类展示用 */
    enum class ProductCategory(val categoryName: String) {
        TRAVEL("出行类"),
        HOME("家居类")
    }

    /** 兼容旧代码：获取显示名称 */
    val displayName: String
        get() = typeName
}

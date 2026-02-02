package com.childproduct.designassistant.model

/**
 * 产品类型枚举
 * 定义所有支持的儿童产品类型
 */
enum class ProductType(val displayName: String, val mainStandards: String) {
    CHILD_SAFETY_SEAT("儿童安全座椅", "ECE R129 / FMVSS 213 / GB 27887"),
    CHILD_STROLLER("婴儿推车", "EN 1888 / GB 14748"),
    CHILD_HOUSEHOLD_GOODS("儿童家庭用品", "GB 6675 / EN 71"),
    CHILD_HIGH_CHAIR("儿童高脚椅", "GB 22793"),
    CRIB("儿童床", "ASTM F1169 / EN 716"),
    
    // 兼容旧代码
    SAFETY_SEAT("安全座椅", "ECE R129 / FMVSS 213 / GB 27887"),
    STROLLER("婴儿推车", "EN 1888 / GB 14748"),
    HIGH_CHAIR("儿童高脚椅", "GB 22793")
}

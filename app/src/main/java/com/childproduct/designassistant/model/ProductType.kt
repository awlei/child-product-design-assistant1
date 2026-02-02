package com.childproduct.designassistant.model

/**
 * 产品类型枚举
 * 定义所有支持的儿童产品类型
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

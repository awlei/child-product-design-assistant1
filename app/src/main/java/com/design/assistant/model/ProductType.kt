package com.design.assistant.model

import kotlinx.serialization.Serializable

/**
 * 产品类型枚举
 * 支持的儿童产品类型
 */
@Serializable
enum class ProductType {
    CHILD_SEAT,           // 儿童安全座椅
    STROLLER,             // 婴儿推车
    HIGH_CHAIR,           // 儿童高脚椅
    CRIB                  // 儿童床
}

/**
 * 获取产品类型的中文名称
 */
fun ProductType.getDisplayName(): String {
    return when (this) {
        ProductType.CHILD_SEAT -> "儿童安全座椅"
        ProductType.STROLLER -> "婴儿推车"
        ProductType.HIGH_CHAIR -> "儿童高脚椅"
        ProductType.CRIB -> "儿童床"
    }
}

/**
 * 获取产品类型的图标（Compose Icon 名称）
 */
fun ProductType.getIconName(): String {
    return when (this) {
        ProductType.CHILD_SEAT -> "chair"
        ProductType.STROLLER -> "stroller"
        ProductType.HIGH_CHAIR -> "table_restaurant"
        ProductType.CRIB -> "bed"
    }
}

/**
 * 获取产品类型支持的标准列表
 */
fun ProductType.getSupportedStandards(): List<StandardType> {
    return when (this) {
        ProductType.CHILD_SEAT -> listOf(
            StandardType.GPS028,
            StandardType.ECE_R129,
            StandardType.CMVSS213,
            StandardType.FMVSS213,
            StandardType.AS_NZS1754,
            StandardType.ISOFIX_Standard
        )
        ProductType.STROLLER -> listOf(
            StandardType.EN1888,
            StandardType.ASTM_F833,
            StandardType.CSA_B311,
            StandardType.SOR_85_379
        )
        ProductType.HIGH_CHAIR -> listOf(
            StandardType.EN14988,
            StandardType.ASTM_F404,
            StandardType.CSA_B229,
            StandardType.SOR_85_379
        )
        ProductType.CRIB -> listOf(
            StandardType.ASTM_F1169,
            StandardType.CSA_B113,
            StandardType.SOR_86_332,
            StandardType.EN716
        )
    }
}

/**
 * 获取产品类型的设计参数模板
 */
fun ProductType.getDefaultDesignParams(): Map<String, Any> {
    return when (this) {
        ProductType.CHILD_SEAT -> mapOf(
            "age_range" to listOf("0-6m", "6-12m", "12-18m", "18-36m"),
            "weight_range" to listOf("0-13kg", "9-18kg", "15-36kg"),
            "percentile" to listOf("50%", "75%", "95%"),
            "mounting_type" to listOf("ISOFIX", "Vehicle Belt"),
            "recline_positions" to listOf(3, 4, 5)
        )
        ProductType.STROLLER -> mapOf(
            "age_range" to listOf("0-6m", "6-36m"),
            "weight_capacity" to listOf("15kg", "22kg"),
            "fold_type" to listOf("Umbrella", "3D Fold", "Hand Fold"),
            "wheel_size" to listOf("6in", "8in", "10in", "12in")
        )
        ProductType.HIGH_CHAIR -> mapOf(
            "age_range" to listOf("6-36m", "6-48m"),
            "weight_capacity" to listOf("15kg", "23kg"),
            "adjustable_height" to listOf(true),
            "tray_size" to listOf("Standard", "XL")
        )
        ProductType.CRIB -> mapOf(
            "age_range" to listOf("0-12m", "0-24m", "0-36m"),
            "mattress_size" to listOf("120x60cm", "140x70cm"),
            "convertible" to listOf(true, false),
            "height_adjustable" to listOf(true)
        )
    }
}

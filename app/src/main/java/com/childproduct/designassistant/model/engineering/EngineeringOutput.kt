package com.childproduct.designassistant.model.engineering

/**
 * 工程输出数据 - 工程师专用
 */
data class EngineeringOutput(
    val productName: String = "Default Product",
    val productType: String = "Unknown",
    val standardVersion: String = "UN R129 Rev.4"
)

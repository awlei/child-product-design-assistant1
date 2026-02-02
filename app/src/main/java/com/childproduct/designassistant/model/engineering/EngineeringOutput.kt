package com.childproduct.designassistant.model.engineering

/**
 * 工程输出数据 - 工程师专用
 */
data class EngineeringOutput(
    val productName: String = "Default Product",
    val productType: String = "Unknown",
    val standardVersion: String = "UN R129 Rev.4",
    val metadata: Map<String, Any>? = null,
    val basicInfo: Map<String, Any>? = null,
    val standardMapping: Map<String, Any>? = null,
    val isofixEnvelope: IsofixEnvelope? = null,
    val testMatrix: TestMatrix? = null,
    val safetyThresholds: Map<String, Any>? = null,
    val complianceStatement: Map<String, Any>? = null,
    val engineeringNotes: Map<String, Any>? = null
)

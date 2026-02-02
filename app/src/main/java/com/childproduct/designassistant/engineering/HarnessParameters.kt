package com.childproduct.designassistant.engineering

/**
 * 安全带参数
 * 基于 GPS-028 Harness Segment Length
 */
data class HarnessParameters(
    val shoulderBeltMinLength: Double,  // 肩带最小长度 (mm)
    val shoulderBeltMaxLength: Double,  // 肩带最大长度 (mm)
    val crotchBeltLength: Double,       // 裆带长度 (mm)
    val bucklePositionRange: ClosedRange<Double>  // 卡扣位置范围 (mm)
)

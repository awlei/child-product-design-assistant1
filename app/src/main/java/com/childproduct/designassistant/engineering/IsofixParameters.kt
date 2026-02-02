package com.childproduct.designassistant.engineering

/**
 * ISOFIX 参数
 * 基于 UN R129 Annex 17
 */
data class IsofixParameters(
    val anchorSpacingMm: Double,              // 锚点间距 (mm)
    val anchorToleranceMm: Double,            // 锚点容差 (mm)
    val foreAftAdjustmentMm: Double,          // 前后调节范围 (mm)
    val lateralAdjustmentMm: Double,          // 侧向调节范围 (mm)
    val staticStrengthKN: Double,             // 静态强度 (kN)
    val supportLegLengthRange: ClosedRange<Double>?,  // 支撑腿长度范围 (mm) - 可选
    val topTetherLengthRange: ClosedRange<Double>?    // 上拉带长度范围 (mm) - 可选
)

package com.childproduct.designassistant.engineering

/**
 * 座椅尺寸参数
 * 基于 GPS-028 设计规则
 */
data class SeatDimensions(
    val minWidth: Double,           // 最小宽度 (mm)
    val idealWidth: Double,         // 理想宽度 (mm)
    val maxWidth: Double,           // 最大宽度 (mm)
    val minDepth: Double,           // 最小深度 (mm)
    val idealDepth: Double,         // 理想深度 (mm)
    val maxDepth: Double,           // 最大深度 (mm)
    val headRestMinHeight: Double,  // 头枕最小高度 (mm)
    val headRestIdealHeight: Double,// 头枕理想高度 (mm)
    val headRestMaxHeight: Double   // 头枕最大高度 (mm)
)

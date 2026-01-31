package com.childproduct.designassistant.model

/**
 * 测试矩阵单项模型（参照ROADMATE 360格式，含标准关联）
 */
data class TestMatrixItem(
    val testItem: String,        // 测试项目（如：HIC极限值）
    val standardRequirement: String, // 标准要求（如：≤390/≤1000）
    val applicableDummy: String, // 适用假人（如：Q0-Q1.5/Q3-Q10）
    val unit: String,            // 单位（如：g、N、mm）
    val standardSource: String   // 标准来源（如：ECE R129 §7.1.2）
)

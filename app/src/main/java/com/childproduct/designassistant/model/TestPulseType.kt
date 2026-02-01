package com.childproduct.designassistant.model

/**
 * 测试脉冲类型
 * 基于ECE R129标准的碰撞测试类型
 */
enum class TestPulseType(val displayName: String) {
    FRONTAL("正面碰撞"),
    REAR("后方碰撞"),
    LATERAL("侧面碰撞")
}

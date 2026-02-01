package com.childproduct.designassistant.model

/**
 * 碰撞方向/假人类型
 * 基于ECE R129标准的碰撞方向
 */
enum class ImpactDirection(val displayName: String) {
    Q0("Q0 假人"),
    Q1("Q1 假人"),
    Q3("Q3 假人"),
    Q6("Q6 假人")
}

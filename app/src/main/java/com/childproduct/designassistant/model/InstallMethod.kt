package com.childproduct.designassistant.model

/**
 * 安装方式枚举（用户可选择的选项，与UI单选按钮对应）
 */
enum class InstallMethod(
    val displayName: String,  // UI显示名称
    val description: String   // 核心特点中的描述文本
) {
    ISOFIX(
        displayName = "ISOFIX快速连接",
        description = "ISOFIX快速连接（无防旋转装置）"
    ),
    ISOFIX_TOP_TETHER(
        displayName = "ISOFIX + Top-tether",
        description = "ISOFIX快速连接 + Top-tether防旋转（上拉带）"
    ),
    ISOFIX_SUPPORT_LEG(
        displayName = "ISOFIX + 支撑腿",
        description = "ISOFIX快速连接 + 支撑腿防旋转（底部支撑）"
    ),
    SEAT_BELT(
        displayName = "车辆安全带固定",
        description = "车辆三点式安全带固定（无防旋转装置）"
    ),
    SEAT_BELT_TOP_TETHER(
        displayName = "安全带 + Top-tether",
        description = "车辆安全带固定 + Top-tether防旋转（上拉带）"
    )
}

/**
 * InstallMethod 扩展函数 - 获取安装方向
 */
fun InstallMethod.getDirection(): InstallDirection? {
    return when (this) {
        InstallMethod.ISOFIX_SUPPORT_LEG -> InstallDirection.REARWARD
        InstallMethod.SEAT_BELT, InstallMethod.SEAT_BELT_TOP_TETHER -> InstallDirection.FORWARD
        InstallMethod.ISOFIX -> InstallDirection.REARWARD
        InstallMethod.ISOFIX_TOP_TETHER -> InstallDirection.FORWARD
    }
}

/**
 * InstallMethod 扩展函数 - 获取防旋转类型
 */
fun InstallMethod.getAntiRotation(): String? {
    return when (this) {
        InstallMethod.ISOFIX_SUPPORT_LEG -> "Support Leg"
        InstallMethod.ISOFIX_TOP_TETHER, InstallMethod.SEAT_BELT_TOP_TETHER -> "Top Tether"
        InstallMethod.ISOFIX, InstallMethod.SEAT_BELT -> null
    }
}

package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 安装方式实体
 * 基于UN R129 §6.1安装要求
 */
@Entity(tableName = "installation_method")
data class InstallationMethod(
    @PrimaryKey val methodId: String,            // METHOD_ISOFIX_3PTS
    val methodName: String,                     // "ISOFIX 3点安装"
    val methodCode: String,                     // "ISOFIX_3PTS"
    val description: String,                    // 详细描述
    val applicableHeightCm: String,             // "40-105cm", "105-150cm"
    val requiresTopTether: Boolean,             // 是否需要Top Tether
    val requiresSupportLeg: Boolean,            // 是否需要支撑腿
    val standardClause: String,                 // "UN R129 §6.1.2"
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        val STANDARD_METHODS = listOf(
            InstallationMethod(
                methodId = "METHOD_ISOFIX_3PTS_REARWARD",
                methodName = "ISOFIX 3点后向安装",
                methodCode = "ISOFIX_3PTS_REARWARD",
                description = "使用ISOFIX 3点连接+支撑腿进行后向安装",
                applicableHeightCm = "40-105cm",
                requiresTopTether = false,
                requiresSupportLeg = true,
                standardClause = "UN R129 §6.1.2"
            ),
            InstallationMethod(
                methodId = "METHOD_ISOFIX_2PTS_REARWARD",
                methodName = "ISOFIX 2点后向安装",
                methodCode = "ISOFIX_2PTS_REARWARD",
                description = "使用ISOFIX 2点连接进行后向安装（仅Q3）",
                applicableHeightCm = "87-105cm",
                requiresTopTether = false,
                requiresSupportLeg = false,
                standardClause = "UN R129 §6.1.2"
            ),
            InstallationMethod(
                methodId = "METHOD_ISOFIX_3PTS_FORWARD",
                methodName = "ISOFIX 3点前向安装",
                methodCode = "ISOFIX_3PTS_FORWARD",
                description = "使用ISOFIX 3点连接+Top Tether进行前向安装",
                applicableHeightCm = "105-150cm",
                requiresTopTether = true,
                requiresSupportLeg = false,
                standardClause = "UN R129 §6.1.3"
            ),
            InstallationMethod(
                methodId = "METHOD_VEHICLE_BELT",
                methodName = "车辆安全带安装",
                methodCode = "VEHICLE_BELT",
                description = "使用车辆安全带安装",
                applicableHeightCm = "40-150cm",
                requiresTopTether = false,
                requiresSupportLeg = false,
                standardClause = "UN R129 §6.2"
            )
        )
    }
}

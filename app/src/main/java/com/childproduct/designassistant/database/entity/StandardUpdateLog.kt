package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 标准更新日志实体
 * 记录数据同步和更新历史
 */
@Entity(tableName = "standard_update_log")
data class StandardUpdateLog(
    @PrimaryKey val logId: String,              // SYNC_20240101_120000
    val regulationNumber: String,               // "UN R129"
    val version: String,                        // "Rev.4"
    val syncType: String,                       // "INITIAL", "AUTO_SYNC", "FORCE_SYNC", "CHECK_ONLY"
    val status: String,                         // "SUCCESS", "FAILED", "NO_UPDATE"
    val timestamp: Long,                        // 同步时间戳
    val details: String? = null                 // 详细信息
)

package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 标准引用实体
 * 存储标准的基本信息和版本（Rev.5，2022版）
 */
@Entity(tableName = "standard_reference")
data class StandardReference(
    @PrimaryKey val referenceId: String,          // REF_UN_R129
    val regulationNumber: String,                // "UN R129"
    val regulationName: String,                  // "Enhanced Child Restraint Systems"
    val currentVersion: String,                  // "Rev.4"
    val entryIntoForceDate: String,              // "29 December 2018"
    val officialUrl: String,                     // 官方URL
    val status: String,                          // "ACTIVE", "DEPRECATED"
    val relatedRegulations: List<String>,        // 相关标准: ["UN R16", "UN R14", "UN R145"]
    val lastUpdated: Long = System.currentTimeMillis()
)

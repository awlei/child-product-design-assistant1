package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

/**
 * FMVSS产品测试实体
 * 记录产品通过FMVSS 213标准认证的测试信息
 */
@Entity(
    tableName = "fmvss_product_tests",
    indices = [
        Index(value = ["productId"]),
        Index(value = ["testDate"])
    ]
)
data class FMVSSProductTestEntity(
    @PrimaryKey val testId: String,          // TEST_P001
    val productId: String,                  // 产品ID
    val testDate: Long,                      // 测试日期
    val testType: String,                    // Frontal Impact, Side Impact
    val testSpeedKmph: Int,                  // 测试速度
    val testResult: String,                  // PASS, FAIL
    val certificationNumber: String?,        // 认证编号
    val testLab: String?,                    // 测试实验室
    val comments: String?,                   // 备注
    val lastUpdated: Long = System.currentTimeMillis()
)

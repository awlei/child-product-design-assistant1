package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ISOFIX要求实体
 * 基于UN R16和UN R145 ISOFIX标准
 */
@Entity(tableName = "isofix_requirement")
data class IsofixRequirement(
    @PrimaryKey val requirementId: String,        // REQUIREMENT_ISOFIX_SIZE
    val sizeCategory: String,                   // "ISOFIX SIZE", "ISOFIX SIZE+", "UNIVERSAL"
    val maxWeightKg: Double,                    // 最大重量
    val seatWidthMm: Int,                       // 座椅宽度
    val supportLegMinLengthMm: Int,             // 支撑腿最小长度
    val supportLegMaxLengthMm: Int,             // 支撑腿最大长度
    val standardClause: String,                 // "UN R16", "UN R145"
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        val STANDARD_REQUIREMENTS = listOf(
            IsofixRequirement(
                requirementId = "REQUIREMENT_ISOFIX_SIZE",
                sizeCategory = "ISOFIX SIZE",
                maxWeightKg = 18.0,
                seatWidthMm = 360,
                supportLegMinLengthMm = 285,
                supportLegMaxLengthMm = 540,
                standardClause = "UN R16, UN R145"
            ),
            IsofixRequirement(
                requirementId = "REQUIREMENT_ISOFIX_SIZE_PLUS",
                sizeCategory = "ISOFIX SIZE+",
                maxWeightKg = 36.0,
                seatWidthMm = 440,
                supportLegMinLengthMm = 285,
                supportLegMaxLengthMm = 540,
                standardClause = "UN R16, UN R145"
            ),
            IsofixRequirement(
                requirementId = "REQUIREMENT_UNIVERSAL",
                sizeCategory = "UNIVERSAL",
                maxWeightKg = 36.0,
                seatWidthMm = 550,
                supportLegMinLengthMm = 285,
                supportLegMaxLengthMm = 540,
                standardClause = "UN R16, UN R145"
            )
        )
    }
}

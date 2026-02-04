package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ISOFIX要求实体
 * 基于UN R16、UN R145和UN R129（Rev.5，2022版）
 * 支持i-Size分类：A/B1/B2/C/D/E
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
            // 基础i-Size分类（UN R129 Rev.5）
            IsofixRequirement(
                requirementId = "REQUIREMENT_ISOFIX_SIZE_A",
                sizeCategory = "ISOFIX SIZE A",
                maxWeightKg = 18.0,
                seatWidthMm = 360,
                supportLegMinLengthMm = 285,
                supportLegMaxLengthMm = 540,
                standardClause = "UN R16, UN R145, UN R129 Annex 17"
            ),
            IsofixRequirement(
                requirementId = "REQUIREMENT_ISOFIX_SIZE_B1",
                sizeCategory = "ISOFIX SIZE B1",
                maxWeightKg = 18.0,
                seatWidthMm = 320,
                supportLegMinLengthMm = 285,
                supportLegMaxLengthMm = 540,
                standardClause = "UN R16, UN R145, UN R129 Annex 17"
            ),
            IsofixRequirement(
                requirementId = "REQUIREMENT_ISOFIX_SIZE_B2",
                sizeCategory = "ISOFIX SIZE B2",
                maxWeightKg = 18.0,
                seatWidthMm = 320,
                supportLegMinLengthMm = 285,
                supportLegMaxLengthMm = 540,
                standardClause = "UN R16, UN R145, UN R129 Annex 17"
            ),
            IsofixRequirement(
                requirementId = "REQUIREMENT_ISOFIX_SIZE_C",
                sizeCategory = "ISOFIX SIZE C",
                maxWeightKg = 13.0,
                seatWidthMm = 300,
                supportLegMinLengthMm = 285,
                supportLegMaxLengthMm = 540,
                standardClause = "UN R16, UN R145, UN R129 Annex 17"
            ),
            IsofixRequirement(
                requirementId = "REQUIREMENT_ISOFIX_SIZE_D",
                sizeCategory = "ISOFIX SIZE D",
                maxWeightKg = 13.0,
                seatWidthMm = 300,
                supportLegMinLengthMm = 285,
                supportLegMaxLengthMm = 540,
                standardClause = "UN R16, UN R145, UN R129 Annex 17"
            ),
            IsofixRequirement(
                requirementId = "REQUIREMENT_ISOFIX_SIZE_E",
                sizeCategory = "ISOFIX SIZE E",
                maxWeightKg = 18.0,
                seatWidthMm = 280,
                supportLegMinLengthMm = 285,
                supportLegMaxLengthMm = 540,
                standardClause = "UN R16, UN R145, UN R129 Annex 17"
            ),
            // 传统分类（保留兼容性）
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

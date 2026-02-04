package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

/**
 * 身高范围映射实体
 * 将身高范围映射到假人类型和产品分组
 * 基于UN R129 Annex 19（Rev.5，2022版）
 */
@Entity(
    tableName = "height_range_mapping",
    indices = [Index(value = ["heightRangeStartCm", "heightRangeEndCm"], unique = true)]
)
data class HeightRangeMapping(
    @PrimaryKey val mappingId: String,          // MAPPING_40_50
    val heightRangeStartCm: Int,                // 起始身高
    val heightRangeEndCm: Int,                  // 结束身高
    val dummyId: String,                        // 关联假人ID
    val productGroup: String,                   // 产品分组: Group 0+, Group 1, etc.
    val installDirection: String,               // 安装方向: REARWARD, FORWARD
    val standardClause: String,                 // 标准条款
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        val STANDARD_MAPPINGS = listOf(
            HeightRangeMapping(
                mappingId = "MAPPING_40_50",
                heightRangeStartCm = 40,
                heightRangeEndCm = 50,
                dummyId = "DUMMY_Q0",
                productGroup = "Group 0+",
                installDirection = "REARWARD",
                standardClause = "UN R129 Annex 19"
            ),
            HeightRangeMapping(
                mappingId = "MAPPING_50_60",
                heightRangeStartCm = 50,
                heightRangeEndCm = 60,
                dummyId = "DUMMY_Q0_PLUS",
                productGroup = "Group 0+",
                installDirection = "REARWARD",
                standardClause = "UN R129 Annex 19"
            ),
            HeightRangeMapping(
                mappingId = "MAPPING_60_75",
                heightRangeStartCm = 60,
                heightRangeEndCm = 75,
                dummyId = "DUMMY_Q1",
                productGroup = "Group I",
                installDirection = "REARWARD",
                standardClause = "UN R129 Annex 19"
            ),
            HeightRangeMapping(
                mappingId = "MAPPING_75_87",
                heightRangeStartCm = 75,
                heightRangeEndCm = 87,
                dummyId = "DUMMY_Q1_5",
                productGroup = "Group I",
                installDirection = "REARWARD",
                standardClause = "UN R129 Annex 19"
            ),
            HeightRangeMapping(
                mappingId = "MAPPING_87_105",
                heightRangeStartCm = 87,
                heightRangeEndCm = 105,
                dummyId = "DUMMY_Q3",
                productGroup = "Group I/II",
                installDirection = "REARWARD_FORWARD",
                standardClause = "UN R129 Annex 19"
            ),
            HeightRangeMapping(
                mappingId = "MAPPING_105_125",
                heightRangeStartCm = 105,
                heightRangeEndCm = 125,
                dummyId = "DUMMY_Q6",
                productGroup = "Group II",
                installDirection = "FORWARD",
                standardClause = "UN R129 Annex 19"
            ),
            HeightRangeMapping(
                mappingId = "MAPPING_125_145",
                heightRangeStartCm = 125,
                heightRangeEndCm = 145,
                dummyId = "DUMMY_Q10",
                productGroup = "Group III",
                installDirection = "FORWARD",
                standardClause = "UN R129 Annex 19"
            )
        )
    }
}

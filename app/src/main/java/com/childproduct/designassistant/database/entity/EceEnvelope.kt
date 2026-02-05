package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

/**
 * ECE R129 Envelope实体
 * 存储儿童安全座椅的外形尺寸和空间占用数据
 * 基于ISOFIX尺寸标准和UN R129要求
 */
@Entity(
    tableName = "ece_envelope",
    indices = [
        Index(value = ["sizeClass"], unique = true),
        Index(value = ["applicableGroup"])
    ]
)
data class EceEnvelope(
    @PrimaryKey val envelopeId: String,          // 唯一ID: ENV_B1, ENV_B2, ENV_D, ENV_E
    val sizeClass: String,                       // ISOFIX尺寸等级: B1, B2, D, E
    val applicableGroup: String,                 // 适用产品组: Group 0+, Group I, Group II, Group III
    val maxLengthMm: Int,                        // 最大长度(mm)
    val maxWidthMm: Int,                         // 最大宽度(mm)
    val maxHeightMm: Int,                        // 最大高度(mm)
    val minCockpitLengthMm: Int?,               // 最小座舱长度(mm) - 假人腿部空间
    val minCockpitWidthMm: Int?,                // 最小座舱宽度(mm)
    val minHeadRestHeightMm: Int?,              // 最小头枕高度(mm)
    val maxHeadRestHeightMm: Int?,              // 最大头枕高度(mm)
    val isofixWidthMm: Int,                     // ISOFIX宽度(mm)
    val topTetherDistanceMm: Int?,              // Top tether锚点距离(mm)
    val legFootprintMm: Int?,                   // 支撑腿占地面积(mm)
    val sideImpactWidthMm: Int?,                // 侧碰防护宽度(mm)
    val description: String,                    // 描述
    val standardClause: String,                  // 标准条款: "UN R129 Annex 18", "ISO/FDIS 13216"
    val vehicleRequirements: String,            // 车辆要求
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * 验证尺寸是否符合ISOFIX标准
     */
    fun validateIsSizeClass(): Boolean {
        return when (sizeClass) {
            "B1" -> maxLengthMm <= 690 && maxWidthMm <= 460
            "B2" -> maxLengthMm <= 730 && maxWidthMm <= 460
            "D" -> maxLengthMm <= 690 && maxWidthMm <= 460
            "E" -> maxLengthMm <= 820 && maxWidthMm <= 460
            else -> false
        }
    }

    /**
     * 获取适用的假人代码列表
     */
    fun getCompatibleDummyCodes(): List<String> {
        return when (applicableGroup) {
            "Group 0+" -> listOf("Q0", "Q0+")
            "Group I" -> listOf("Q1", "Q1.5", "Q3")
            "Group II" -> listOf("Q6")
            "Group III" -> listOf("Q10")
            "Group I/II" -> listOf("Q3", "Q6")
            else -> emptyList()
        }
    }

    companion object {
        /**
         * ECE R129标准Envelope数据（基于ISOFIX Size Class）
         */
        val STANDARD_ENVELOPES = listOf(
            // ISOFIX Size Class B1 - 适用于Group 0+
            EceEnvelope(
                envelopeId = "ENV_B1",
                sizeClass = "B1",
                applicableGroup = "Group 0+",
                maxLengthMm = 690,
                maxWidthMm = 460,
                maxHeightMm = 550,
                minCockpitLengthMm = 520,
                minCockpitWidthMm = 240,
                minHeadRestHeightMm = 380,
                maxHeadRestHeightMm = 500,
                isofixWidthMm = 280,
                topTetherDistanceMm = null,
                legFootprintMm = 200,
                sideImpactWidthMm = 450,
                description = "ISOFIX Size Class B1，适用于Group 0+（40-75cm），强制后向安装",
                standardClause = "UN R129 Annex 18, ISO/FDIS 13216 Size Class B1",
                vehicleRequirements = "ISOFIX下固定点间距280mm，支撑腿区域无障碍"
            ),
            // ISOFIX Size Class B2 - 适用于Group I
            EceEnvelope(
                envelopeId = "ENV_B2",
                sizeClass = "B2",
                applicableGroup = "Group I",
                maxLengthMm = 730,
                maxWidthMm = 460,
                maxHeightMm = 580,
                minCockpitLengthMm = 580,
                minCockpitWidthMm = 260,
                minHeadRestHeightMm = 450,
                maxHeadRestHeightMm = 650,
                isofixWidthMm = 280,
                topTetherDistanceMm = 1050,
                legFootprintMm = null,
                sideImpactWidthMm = 480,
                description = "ISOFIX Size Class B2，适用于Group I（60-105cm），可后向或前向安装",
                standardClause = "UN R129 Annex 18, ISO/FDIS 13216 Size Class B2",
                vehicleRequirements = "ISOFIX下固定点间距280mm，Top tether锚点距离1050mm"
            ),
            // ISOFIX Size Class D - 适用于Group 0+（特殊配置）
            EceEnvelope(
                envelopeId = "ENV_D",
                sizeClass = "D",
                applicableGroup = "Group 0+",
                maxLengthMm = 690,
                maxWidthMm = 460,
                maxHeightMm = 550,
                minCockpitLengthMm = 520,
                minCockpitWidthMm = 240,
                minHeadRestHeightMm = 380,
                maxHeadRestHeightMm = 500,
                isofixWidthMm = 280,
                topTetherDistanceMm = null,
                legFootprintMm = 200,
                sideImpactWidthMm = 450,
                description = "ISOFIX Size Class D，适用于Group 0+，特殊配置的婴儿提篮",
                standardClause = "UN R129 Annex 18, ISO/FDIS 13216 Size Class D",
                vehicleRequirements = "ISOFIX下固定点间距280mm，支撑腿区域无障碍"
            ),
            // ISOFIX Size Class E - 适用于Group II/III
            EceEnvelope(
                envelopeId = "ENV_E",
                sizeClass = "E",
                applicableGroup = "Group II/III",
                maxLengthMm = 820,
                maxWidthMm = 460,
                maxHeightMm = 620,
                minCockpitLengthMm = 650,
                minCockpitWidthMm = 300,
                minHeadRestHeightMm = 500,
                maxHeadRestHeightMm = 750,
                isofixWidthMm = 280,
                topTetherDistanceMm = 1250,
                legFootprintMm = null,
                sideImpactWidthMm = 520,
                description = "ISOFIX Size Class E，适用于Group II/III（105-145cm），前向安装",
                standardClause = "UN R129 Annex 18, ISO/FDIS 13216 Size Class E",
                vehicleRequirements = "ISOFIX下固定点间距280mm，Top tether锚点距离1250mm"
            )
        )

        /**
         * 根据产品组获取Envelope
         */
        fun getByProductGroup(productGroup: String): EceEnvelope? {
            return STANDARD_ENVELOPES.find { it.applicableGroup == productGroup }
        }

        /**
         * 根据假人代码获取Envelope
         */
        fun getByDummyCode(dummyCode: String): EceEnvelope? {
            val group = when (dummyCode) {
                "Q0", "Q0+" -> "Group 0+"
                "Q1", "Q1.5" -> "Group I"
                "Q3" -> "Group I/II"
                "Q6" -> "Group II"
                "Q10" -> "Group III"
                else -> null
            }
            return group?.let { getByProductGroup(it) }
        }

        /**
         * 根据身高获取Envelope
         */
        fun getByHeight(heightCm: Int): EceEnvelope? {
            return when (heightCm) {
                in 40..60 -> getByProductGroup("Group 0+")
                in 60..87 -> getByProductGroup("Group I")
                in 87..105 -> getByProductGroup("Group I/II")
                in 105..125 -> getByProductGroup("Group II")
                in 125..145 -> getByProductGroup("Group III")
                else -> null
            }
        }
    }
}

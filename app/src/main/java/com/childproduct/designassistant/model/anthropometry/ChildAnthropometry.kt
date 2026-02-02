package com.childproduct.designassistant.model.anthropometry

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * GPS-028人体测量学数据实体
 * 数据来源：GPS-028 Anthropometry 11-28-2018
 * 标准参考：Dorel人体测量学数据库
 */
@Entity(tableName = "child_anthropometry")
data class ChildAnthropometry(
    @PrimaryKey val id: String,                 // AGE_0M_5TH, AGE_1YR_50TH...
    val ageMonths: Int,                         // 年龄（月）
    val statureCm: Double,                      // 身高（cm）
    val weightKg: Double,                       // 体重（kg）
    val percentile: Percentile,                 // 百分位：5TH/50TH/95TH
    
    // 头部尺寸
    val headCircumferenceMm: Double,            // 头围(mm)
    val headDepthMm: Double,                    // 头深(mm)
    val headWidthMm: Double,                    // 头宽(mm)
    
    // 躯干尺寸
    val shoulderWidthMm: Double,                // 肩宽(mm) - GPS-028关键参数
    val chestDepthMm: Double,                   // 胸深(mm)
    val chestWidthMm: Double,                   // 胸宽(mm)
    val sittingHeightMm: Double,                // 坐高(mm)
    
    // 臀部尺寸
    val hipWidthMm: Double,                     // 臀宽(mm) - 安全座椅宽度设计依据
    val hipDepthMm: Double,                     // 臀深(mm)
    
    // 大腿尺寸
    val thighWidthMm: Double,                   // 大腿宽(mm)
    val thighHeightMm: Double,                  // 大腿高(mm)
    
    // 五点式安全带关键点坐标（GPS-028 Harness Segment Length）
    val shoulderPointX: Double,                 // 肩点X坐标
    val shoulderPointY: Double,                 // 肩点Y坐标
    val crotchPointX: Double,                   // 裆部X坐标
    val crotchPointY: Double,                   // 裆部Y坐标
    val harnessLengthMm: Double,                // 安全带总长度(mm)
    
    // 标准来源
    val dataSource: String = "GPS-028 Anthropometry 11-28-2018",
    val lastUpdated: Long = System.currentTimeMillis()
) {
    /**
     * 百分位枚举
     */
    enum class Percentile {
        FIFTH,      // 5th百分位（小体型儿童）
        FIFTIETH,   // 50th百分位（平均体型）
        NINETY_FIFTH // 95th百分位（大体型儿童）
    }
    
    /**
     * 从GPS-028数据生成产品设计参数
     * 基于人体测量学数据计算座椅尺寸
     */
    fun generateSeatDimensions(): AnthropometrySeatDimensions {
        return AnthropometrySeatDimensions(
            minWidth = hipWidthMm * 1.1,            // 臀宽×1.1（最小间隙）
            idealWidth = hipWidthMm * 1.2,          // 臀宽×1.2（舒适间隙）
            maxWidth = hipWidthMm * 1.3,            // 臀宽×1.3（最大间隙）
            minDepth = sittingHeightMm * 0.35,      // 坐高×35%（最小座深）
            idealDepth = sittingHeightMm * 0.4,     // 坐高×40%（理想座深）
            maxDepth = sittingHeightMm * 0.45,      // 坐高×45%（最大座深）
            minHeight = headRestMinHeight(),        // 头枕最小高度
            idealHeight = headRestIdealHeight(),    // 头枕理想高度
            maxHeight = headRestMaxHeight(),        // 头枕最大高度
            shoulderBeltHeight = shoulderPointY     // 肩带高度（直接映射）
        )
    }
    
    /**
     * 生成安全带长度要求
     * 基于GPS-028 Harness Segment Length数据
     */
    fun generateHarnessRequirements(): AnthropometryHarnessRequirements {
        return AnthropometryHarnessRequirements(
            minShoulderBeltLength = harnessLengthMm * 0.9,  // 90%最小长度
            idealShoulderBeltLength = harnessLengthMm,       // 100%理想长度
            maxShoulderBeltLength = harnessLengthMm * 1.2,  // 120%最大长度
            crotchBeltLength = thighWidthMm * 1.5           // 大腿宽×1.5
        )
    }
    
    /**
     * 头枕最小高度
     * 基于身高范围计算
     */
    private fun headRestMinHeight(): Double {
        return when {
            statureCm < 60 -> 200.0
            statureCm < 75 -> 240.0
            statureCm < 87 -> 280.0
            statureCm < 105 -> 320.0
            statureCm < 125 -> 360.0
            statureCm < 145 -> 400.0
            else -> 440.0
        }
    }
    
    /**
     * 头枕理想高度
     */
    private fun headRestIdealHeight(): Double {
        return headRestMinHeight() + 60.0  // +60mm理想高度
    }
    
    /**
     * 头枕最大高度
     */
    private fun headRestMaxHeight(): Double {
        return headRestMinHeight() + 120.0  // +120mm调节范围
    }
    
    /**
     * 获取显示名称
     */
    fun getDisplayName(): String {
        val ageText = when {
            ageMonths < 12 -> "${ageMonths}个月"
            else -> "${ageMonths / 12}岁"
        }
        return "$ageText (${percentile.displayName})"
    }
    
    /**
     * 百分位显示名称
     */
    val Percentile.displayName: String
        get() = when (this) {
            Percentile.FIFTH -> "5%"
            Percentile.FIFTIETH -> "50%"
            Percentile.NINETY_FIFTH -> "95%"
        }
}

/**
 * 座椅尺寸参数（基于人体测量学数据）
 * 基于GPS-028人体测量学数据生成
 */
data class AnthropometrySeatDimensions(
    val minWidth: Double,           // 最小宽度(mm)
    val idealWidth: Double,         // 理想宽度(mm)
    val maxWidth: Double,           // 最大宽度(mm)
    val minDepth: Double,           // 最小深度(mm)
    val idealDepth: Double,         // 理想深度(mm)
    val maxDepth: Double,           // 最大深度(mm)
    val minHeight: Double,          // 最小高度(mm)
    val idealHeight: Double,        // 理想高度(mm)
    val maxHeight: Double,          // 最大高度(mm)
    val shoulderBeltHeight: Double  // 肩带高度(mm)
)

/**
 * 安全带要求（基于人体测量学数据）
 * 基于GPS-028 Harness Segment Length数据
 */
data class AnthropometryHarnessRequirements(
    val minShoulderBeltLength: Double,   // 最小肩带长度(mm)
    val idealShoulderBeltLength: Double, // 理想肩带长度(mm)
    val maxShoulderBeltLength: Double,   // 最大肩带长度(mm)
    val crotchBeltLength: Double         // 裆带长度(mm)
)

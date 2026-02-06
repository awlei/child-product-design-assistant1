package com.design.assistant.model

import kotlinx.serialization.Serializable

/**
 * GPS028设计参数数据类
 * 用于生成符合GB 27887-2011标准的专业设计参数
 */
@Serializable
data class GPS028Params(
    // 基础信息
    val groupName: String,                    // 组别（0、0+、I、II、III）
    val percentile: String,                   // 百分位（50%、75%、95%）
    val weight: Double,                       // 体重（kg）
    val height: Double,                       // 身高（cm）
    val age: String,                          // 适用年龄

    // 头部参数
    val headWidth: Double,                    // 头宽（mm）
    val headDepth: Double,                    // 头深（mm）
    val headHeight: Double,                   // 头高（mm）
    val headCircumference: Double,            // 头围（mm）

    // 颈部参数
    val neckWidth: Double,                    // 颈宽（mm）
    val neckLength: Double,                   // 颈长（mm）

    // 肩部参数
    val shoulderWidth: Double,                // 肩宽（mm）
    val shoulderHeight: Double,               // 肩高（mm）

    // 躯干参数
    val chestWidth: Double,                   // 胸宽（mm）
    val chestDepth: Double,                   // 胸深（mm）
    val chestCircumference: Double,           // 胸围（mm）
    val waistWidth: Double,                   // 腰宽（mm）
    val waistDepth: Double,                   // 腰深（mm）
    val waistCircumference: Double,           // 腰围（mm）
    val hipWidth: Double,                     // 臀宽（mm）
    val hipDepth: Double,                     // 臀深（mm）
    val hipCircumference: Double,             // 臀围（mm）

    // 上肢参数
    val armLength: Double,                    // 臂长（mm）
    val upperArmLength: Double,               // 上臂长（mm）
    val forearmLength: Double,                // 前臂长（mm）
    val handLength: Double,                   // 手长（mm）

    // 下肢参数
    val legLength: Double,                    // 腿长（mm）
    val thighLength: Double,                  // 大腿长（mm）
    val calfLength: Double,                   // 小腿长（mm）
    val footLength: Double,                   // 足长（mm）
    val footWidth: Double,                    // 足宽（mm）

    // 设计参考点（基准点）
    val hPoint: Point,                        // H点（髋关节中心点）
    val headReferencePoint: Point,            // 头部参考点
    val shoulderReferencePoint: Point,        // 肩部参考点
    val kneeReferencePoint: Point,            // 膝盖参考点

    // 安全性能参数
    val maxHeadInjuryCriterion: Double,       // 最大头部伤害指标HIC
    val maxChestAcceleration: Double,         // 最大胸部加速度（g）
    val maxNeckMoment: Double,                // 最大颈部力矩（Nm）
    val maxChestDeflection: Double,           // 最大胸部变形（mm）

    // 位移限制
    val maxHeadExcursion: Double,             // 最大头部位移（mm）
    val maxKneeExcursion: Double,             // 最大膝盖位移（mm）
    val maxHeadRotation: Double,              // 最大头部旋转角度（°）
    val maxTorsoRotation: Double,             // 最大躯干旋转角度（°）

    // 带宽要求
    val lapBeltWidth: Double,                 // 腰带宽度（mm）
    val shoulderBeltWidth: Double,            // 肩带宽度（mm）
    val lapBeltAngle: Double,                 // 腰带角度（°）
    val shoulderBeltAngle: Double,            // 肩带角度（°）

    // 其他设计参数
    val minHeadSupportHeight: Double,         // 最小头部支撑高度（mm）
    val minSideWingDepth: Double,             // 最小侧翼深度（mm）
    val minSideWingWidth: Double,             // 最小侧翼宽度（mm）
    val minHarnessWidth: Double,              // 最小安全带间距（mm）
    val minCrotchBuckleDistance: Double,      // 最小胯部扣距（mm）
) {
    /**
     * 生成专业设计报告文本
     */
    fun generateDesignReport(): String {
        return buildString {
            appendLine("=== GPS028设计参数报告 ===")
            appendLine()
            appendLine("【基本信息】")
            appendLine("标准：GB 27887-2011 (GPS028)")
            appendLine("组别：$groupName")
            appendLine("百分位：$percentile")
            appendLine("适用年龄：$age")
            appendLine("体重：${weight}kg")
            appendLine("身高：${height}cm")
            appendLine()
            appendLine("【头部参数】")
            appendLine("头宽：${headWidth}mm")
            appendLine("头深：${headDepth}mm")
            appendLine("头高：${headHeight}mm")
            appendLine("头围：${headCircumference}mm")
            appendLine()
            appendLine("【躯干参数】")
            appendLine("肩宽：${shoulderWidth}mm")
            appendLine("胸围：${chestCircumference}mm")
            appendLine("腰围：${waistCircumference}mm")
            appendLine("臀围：${hipCircumference}mm")
            appendLine()
            appendLine("【设计参考点（基准点）】")
            appendLine("H点（髋关节中心）：(${hPoint.x}, ${hPoint.y})")
            appendLine("头部参考点：(${headReferencePoint.x}, ${headReferencePoint.y})")
            appendLine("肩部参考点：(${shoulderReferencePoint.x}, ${shoulderReferencePoint.y})")
            appendLine("膝盖参考点：(${kneeReferencePoint.x}, ${kneeReferencePoint.y})")
            appendLine()
            appendLine("【安全性能参数】")
            appendLine("最大头部伤害指标（HIC）：$maxHeadInjuryCriterion")
            appendLine("最大胸部加速度：${maxChestAcceleration}g")
            appendLine("最大颈部力矩：${maxNeckMoment}Nm")
            appendLine()
            appendLine("【位移限制】")
            appendLine("最大头部位移：${maxHeadExcursion}mm")
            appendLine("最大膝盖位移：${maxKneeExcursion}mm")
            appendLine("最大头部旋转角度：${maxHeadRotation}°")
            appendLine("最大躯干旋转角度：${maxTorsoRotation}°")
            appendLine()
            appendLine("【带宽要求】")
            appendLine("腰带宽：${lapBeltWidth}mm")
            appendLine("肩带宽：${shoulderBeltWidth}mm")
            appendLine("腰带角度：${lapBeltAngle}°")
            appendLine("肩带角度：${shoulderBeltAngle}°")
            appendLine()
            appendLine("【标准条款引用】")
            appendLine("- 5.3.1.1 头部伤害指标HIC限值：${maxHeadInjuryCriterion}")
            appendLine("- 5.3.1.2 胸部加速度限值：${maxChestAcceleration}g")
            appendLine("- 5.3.1.3 颈部力矩限值：${maxNeckMoment}Nm")
            appendLine("- 5.4.1.1 假人位移限值：${maxHeadExcursion}mm")
        }
    }

    /**
     * 生成JSON格式的参数数据（用于API输出）
     */
    fun toJson(): Map<String, Any> {
        return mapOf(
            "group" to groupName,
            "percentile" to percentile,
            "weight" to weight,
            "height" to height,
            "age" to age,
            "head" to mapOf(
                "width" to headWidth,
                "depth" to headDepth,
                "height" to headHeight,
                "circumference" to headCircumference
            ),
            "torso" to mapOf(
                "shoulderWidth" to shoulderWidth,
                "chestCircumference" to chestCircumference,
                "waistCircumference" to waistCircumference,
                "hipCircumference" to hipCircumference
            ),
            "referencePoints" to mapOf(
                "hPoint" to hPoint.toMap(),
                "headReference" to headReferencePoint.toMap(),
                "shoulderReference" to shoulderReferencePoint.toMap(),
                "kneeReference" to kneeReferencePoint.toMap()
            ),
            "safety" to mapOf(
                "maxHeadInjuryCriterion" to maxHeadInjuryCriterion,
                "maxChestAcceleration" to maxChestAcceleration,
                "maxNeckMoment" to maxNeckMoment
            ),
            "limits" to mapOf(
                "maxHeadExcursion" to maxHeadExcursion,
                "maxKneeExcursion" to maxKneeExcursion,
                "maxHeadRotation" to maxHeadRotation,
                "maxTorsoRotation" to maxTorsoRotation
            )
        )
    }
}

/**
 * 坐标点数据类
 */
@Serializable
data class Point(
    val x: Double,
    val y: Double
) {
    fun toMap(): Map<String, Double> = mapOf("x" to x, "y" to y)
}

/**
 * GPS028组别定义
 */
enum class GPS028Group(val displayName: String, val weightRange: String, val ageRange: String) {
    GROUP_0("0组", "0-10kg", "0-9个月"),
    GROUP_0P("0+组", "0-13kg", "0-15个月"),
    GROUP_I("I组", "9-18kg", "9个月-4岁"),
    GROUP_II("II组", "15-25kg", "3-6岁"),
    GROUP_III("III组", "22-36kg", "6-12岁")
}

package com.design.assistant.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * GPS028数据库实体
 * 用于存储GB 27887-2011标准的设计参数数据
 */
@Entity(tableName = "gps028_params")
data class GPS028Entity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

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
    val hPointX: Double,                      // H点X坐标
    val hPointY: Double,                      // H点Y坐标
    val headRefPointX: Double,                // 头部参考点X坐标
    val headRefPointY: Double,                // 头部参考点Y坐标
    val shoulderRefPointX: Double,            // 肩部参考点X坐标
    val shoulderRefPointY: Double,            // 肩部参考点Y坐标
    val kneeRefPointX: Double,                // 膝盖参考点X坐标
    val kneeRefPointY: Double,                // 膝盖参考点Y坐标

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
     * 转换为GPS028Params对象
     */
    fun toGPS028Params(): com.design.assistant.model.GPS028Params {
        return com.design.assistant.model.GPS028Params(
            groupName = groupName,
            percentile = percentile,
            weight = weight,
            height = height,
            age = age,
            headWidth = headWidth,
            headDepth = headDepth,
            headHeight = headHeight,
            headCircumference = headCircumference,
            neckWidth = neckWidth,
            neckLength = neckLength,
            shoulderWidth = shoulderWidth,
            shoulderHeight = shoulderHeight,
            chestWidth = chestWidth,
            chestDepth = chestDepth,
            chestCircumference = chestCircumference,
            waistWidth = waistWidth,
            waistDepth = waistDepth,
            waistCircumference = waistCircumference,
            hipWidth = hipWidth,
            hipDepth = hipDepth,
            hipCircumference = hipCircumference,
            armLength = armLength,
            upperArmLength = upperArmLength,
            forearmLength = forearmLength,
            handLength = handLength,
            legLength = legLength,
            thighLength = thighLength,
            calfLength = calfLength,
            footLength = footLength,
            footWidth = footWidth,
            hPoint = com.design.assistant.model.Point(hPointX, hPointY),
            headReferencePoint = com.design.assistant.model.Point(headRefPointX, headRefPointY),
            shoulderReferencePoint = com.design.assistant.model.Point(shoulderRefPointX, shoulderRefPointY),
            kneeReferencePoint = com.design.assistant.model.Point(kneeRefPointX, kneeRefPointY),
            maxHeadInjuryCriterion = maxHeadInjuryCriterion,
            maxChestAcceleration = maxChestAcceleration,
            maxNeckMoment = maxNeckMoment,
            maxChestDeflection = maxChestDeflection,
            maxHeadExcursion = maxHeadExcursion,
            maxKneeExcursion = maxKneeExcursion,
            maxHeadRotation = maxHeadRotation,
            maxTorsoRotation = maxTorsoRotation,
            lapBeltWidth = lapBeltWidth,
            shoulderBeltWidth = shoulderBeltWidth,
            lapBeltAngle = lapBeltAngle,
            shoulderBeltAngle = shoulderBeltAngle,
            minHeadSupportHeight = minHeadSupportHeight,
            minSideWingDepth = minSideWingDepth,
            minSideWingWidth = minSideWingWidth,
            minHarnessWidth = minHarnessWidth,
            minCrotchBuckleDistance = minCrotchBuckleDistance
        )
    }

    companion object {
        /**
         * 从GPS028Params对象创建Entity
         */
        fun fromGPS028Params(params: com.design.assistant.model.GPS028Params): GPS028Entity {
            return GPS028Entity(
                groupName = params.groupName,
                percentile = params.percentile,
                weight = params.weight,
                height = params.height,
                age = params.age,
                headWidth = params.headWidth,
                headDepth = params.headDepth,
                headHeight = params.headHeight,
                headCircumference = params.headCircumference,
                neckWidth = params.neckWidth,
                neckLength = params.neckLength,
                shoulderWidth = params.shoulderWidth,
                shoulderHeight = params.shoulderHeight,
                chestWidth = params.chestWidth,
                chestDepth = params.chestDepth,
                chestCircumference = params.chestCircumference,
                waistWidth = params.waistWidth,
                waistDepth = params.waistDepth,
                waistCircumference = params.waistCircumference,
                hipWidth = params.hipWidth,
                hipDepth = params.hipDepth,
                hipCircumference = params.hipCircumference,
                armLength = params.armLength,
                upperArmLength = params.upperArmLength,
                forearmLength = params.forearmLength,
                handLength = params.handLength,
                legLength = params.legLength,
                thighLength = params.thighLength,
                calfLength = params.calfLength,
                footLength = params.footLength,
                footWidth = params.footWidth,
                hPointX = params.hPoint.x,
                hPointY = params.hPoint.y,
                headRefPointX = params.headReferencePoint.x,
                headRefPointY = params.headReferencePoint.y,
                shoulderRefPointX = params.shoulderReferencePoint.x,
                shoulderRefPointY = params.shoulderReferencePoint.y,
                kneeRefPointX = params.kneeReferencePoint.x,
                kneeRefPointY = params.kneeReferencePoint.y,
                maxHeadInjuryCriterion = params.maxHeadInjuryCriterion,
                maxChestAcceleration = params.maxChestAcceleration,
                maxNeckMoment = params.maxNeckMoment,
                maxChestDeflection = params.maxChestDeflection,
                maxHeadExcursion = params.maxHeadExcursion,
                maxKneeExcursion = params.maxKneeExcursion,
                maxHeadRotation = params.maxHeadRotation,
                maxTorsoRotation = params.maxTorsoRotation,
                lapBeltWidth = params.lapBeltWidth,
                shoulderBeltWidth = params.shoulderBeltWidth,
                lapBeltAngle = params.lapBeltAngle,
                shoulderBeltAngle = params.shoulderBeltAngle,
                minHeadSupportHeight = params.minHeadSupportHeight,
                minSideWingDepth = params.minSideWingDepth,
                minSideWingWidth = params.minSideWingWidth,
                minHarnessWidth = params.minHarnessWidth,
                minCrotchBuckleDistance = params.minCrotchBuckleDistance
            )
        }
    }
}

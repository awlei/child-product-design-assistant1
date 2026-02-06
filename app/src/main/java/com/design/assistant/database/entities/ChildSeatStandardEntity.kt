package com.design.assistant.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 儿童安全座椅标准数据库实体
 * 用于存储ECE R129、CMVSS213、FMVSS213等标准的设计参数数据
 * 采用物理隔离策略，与GPS028数据库分开
 */
@Entity(tableName = "child_seat_standard_params")
data class ChildSeatStandardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // 标准信息
    val standardType: String,                  // 标准类型（ECE_R129、CMVSS213、FMVSS213等）
    val standardName: String,                  // 标准名称

    // 假人类型
    val dummyType: String,                     // 假人类型（Q1、Q1.5、Q3、Q6、Q10等）
    val weight: Double,                        // 体重（kg）
    val height: Double,                        // 身高（cm）

    // 安全性能参数
    val maxHeadInjuryCriterion: Double,        // 最大头部伤害指标HIC
    val maxChestAcceleration: Double,          // 最大胸部加速度（g）
    val maxNeckMoment: Double,                 // 最大颈部力矩（Nm）
    val maxChestDeflection: Double,            // 最大胸部变形（mm）

    // 位移限制
    val maxHeadExcursion: Double,              // 最大头部位移（mm）
    val maxKneeExcursion: Double,              // 最大膝盖位移（mm）
    val maxHeadRotation: Double,               // 最大头部旋转角度（°）
    val maxTorsoRotation: Double,              // 最大躯干旋转角度（°）

    // 设计参考点
    val hPointX: Double,                       // H点X坐标
    val hPointY: Double,                       // H点Y坐标

    // 其他设计参数
    val minSideWingDepth: Double,              // 最小侧翼深度（mm）
    val minSideWingWidth: Double,              // 最小侧翼宽度（mm）
    val minHarnessWidth: Double,               // 最小安全带间距（mm）

    // ISOFIX/LATCH参数（如适用）
    val hasISOFIXSupport: Boolean = false,     // 是否支持ISOFIX
    val hasLATCHSupport: Boolean = false,      // 是否支持LATCH
    val isofixBarWidth: Double = 280.0,        // ISOFIX杆宽（mm）
    val latchStrapCount: Int = 2,              // LATCH带数量

    // 地区特定参数
    val region: String,                        // 地区（欧洲、美国、加拿大等）
    val extraInfo: String = "",                // 额外信息（JSON格式）
) {
    companion object {
        /**
         * 创建ECE R129 Q1假人参数
         */
        fun createECE_R129_Q1(): ChildSeatStandardEntity {
            return ChildSeatStandardEntity(
                standardType = "ECE_R129",
                standardName = "ECE R129 i-Size",
                dummyType = "Q1",
                weight = 9.0,
                height = 72.0,
                maxHeadInjuryCriterion = com.design.assistant.model.StandardConstants.ECE_R129.MAX_HIC_Q1,
                maxChestAcceleration = com.design.assistant.model.StandardConstants.ECE_R129.MAX_CHEST_ACCEL,
                maxNeckMoment = com.design.assistant.model.StandardConstants.ECE_R129.MAX_NECK_MOMENT_Q1,
                maxChestDeflection = 30.0,
                maxHeadExcursion = com.design.assistant.model.StandardConstants.ECE_R129.MAX_HEAD_EXCURSION,
                maxKneeExcursion = com.design.assistant.model.StandardConstants.ECE_R129.MAX_HEAD_EXCURSION,
                maxHeadRotation = 30.0,
                maxTorsoRotation = 30.0,
                hPointX = 0.0,
                hPointY = 0.0,
                minSideWingDepth = 60.0,
                minSideWingWidth = 120.0,
                minHarnessWidth = 90.0,
                hasISOFIXSupport = true,
                hasLATCHSupport = false,
                isofixBarWidth = com.design.assistant.model.StandardConstants.ECE_R129.ISOFIX_BAR_WIDTH,
                latchStrapCount = 0,
                region = "欧洲"
            )
        }

        /**
         * 创建ECE R129 Q3假人参数
         */
        fun createECE_R129_Q3(): ChildSeatStandardEntity {
            return ChildSeatStandardEntity(
                standardType = "ECE_R129",
                standardName = "ECE R129 i-Size",
                dummyType = "Q3",
                weight = 15.0,
                height = 83.0,
                maxHeadInjuryCriterion = com.design.assistant.model.StandardConstants.ECE_R129.MAX_HIC_Q3,
                maxChestAcceleration = com.design.assistant.model.StandardConstants.ECE_R129.MAX_CHEST_ACCEL,
                maxNeckMoment = com.design.assistant.model.StandardConstants.ECE_R129.MAX_NECK_MOMENT_Q3,
                maxChestDeflection = 30.0,
                maxHeadExcursion = com.design.assistant.model.StandardConstants.ECE_R129.MAX_HEAD_EXCURSION,
                maxKneeExcursion = com.design.assistant.model.StandardConstants.ECE_R129.MAX_HEAD_EXCURSION,
                maxHeadRotation = 30.0,
                maxTorsoRotation = 30.0,
                hPointX = 0.0,
                hPointY = 0.0,
                minSideWingDepth = 70.0,
                minSideWingWidth = 140.0,
                minHarnessWidth = 100.0,
                hasISOFIXSupport = true,
                hasLATCHSupport = false,
                isofixBarWidth = com.design.assistant.model.StandardConstants.ECE_R129.ISOFIX_BAR_WIDTH,
                latchStrapCount = 0,
                region = "欧洲"
            )
        }

        /**
         * 创建ECE R129 Q6假人参数
         */
        fun createECE_R129_Q6(): ChildSeatStandardEntity {
            return ChildSeatStandardEntity(
                standardType = "ECE_R129",
                standardName = "ECE R129 i-Size",
                dummyType = "Q6",
                weight = 21.8,
                height = 105.0,
                maxHeadInjuryCriterion = com.design.assistant.model.StandardConstants.ECE_R129.MAX_HIC_Q6,
                maxChestAcceleration = com.design.assistant.model.StandardConstants.ECE_R129.MAX_CHEST_ACCEL,
                maxNeckMoment = com.design.assistant.model.StandardConstants.ECE_R129.MAX_NECK_MOMENT_Q6,
                maxChestDeflection = 35.0,
                maxHeadExcursion = com.design.assistant.model.StandardConstants.ECE_R129.MAX_HEAD_EXCURSION,
                maxKneeExcursion = com.design.assistant.model.StandardConstants.ECE_R129.MAX_HEAD_EXCURSION,
                maxHeadRotation = 35.0,
                maxTorsoRotation = 35.0,
                hPointX = 0.0,
                hPointY = 0.0,
                minSideWingDepth = 80.0,
                minSideWingWidth = 160.0,
                minHarnessWidth = 110.0,
                hasISOFIXSupport = true,
                hasLATCHSupport = false,
                isofixBarWidth = com.design.assistant.model.StandardConstants.ECE_R129.ISOFIX_BAR_WIDTH,
                latchStrapCount = 0,
                region = "欧洲"
            )
        }

        /**
         * 创建CMVSS213假人参数（加拿大）
         */
        fun createCMVSS213_Toddler(): ChildSeatStandardEntity {
            return ChildSeatStandardEntity(
                standardType = "CMVSS213",
                standardName = "CMVSS 213",
                dummyType = "Toddler",
                weight = 15.0,
                height = 83.0,
                maxHeadInjuryCriterion = com.design.assistant.model.StandardConstants.CMVSS213.MAX_HIC_TODDLER,
                maxChestAcceleration = com.design.assistant.model.StandardConstants.CMVSS213.MAX_CHEST_ACCEL,
                maxNeckMoment = 30.0,
                maxChestDeflection = 30.0,
                maxHeadExcursion = com.design.assistant.model.StandardConstants.CMVSS213.MAX_HEAD_EXCURSION,
                maxKneeExcursion = com.design.assistant.model.StandardConstants.CMVSS213.MAX_KNEE_EXCURSION,
                maxHeadRotation = 30.0,
                maxTorsoRotation = 30.0,
                hPointX = 0.0,
                hPointY = 0.0,
                minSideWingDepth = 70.0,
                minSideWingWidth = 140.0,
                minHarnessWidth = 100.0,
                hasISOFIXSupport = false,
                hasLATCHSupport = true,
                isofixBarWidth = 0.0,
                latchStrapCount = com.design.assistant.model.StandardConstants.CMVSS213.MIN_UAS_STRAPS,
                region = "加拿大"
            )
        }

        /**
         * 创建FMVSS213假人参数（美国）
         */
        fun createFMVSS213_Toddler(): ChildSeatStandardEntity {
            return ChildSeatStandardEntity(
                standardType = "FMVSS213",
                standardName = "FMVSS 213",
                dummyType = "Toddler",
                weight = 15.0,
                height = 83.0,
                maxHeadInjuryCriterion = com.design.assistant.model.StandardConstants.FMVSS213.MAX_HIC_TODDLER,
                maxChestAcceleration = com.design.assistant.model.StandardConstants.FMVSS213.MAX_CHEST_ACCEL,
                maxNeckMoment = 30.0,
                maxChestDeflection = 30.0,
                maxHeadExcursion = com.design.assistant.model.StandardConstants.FMVSS213.MAX_HEAD_EXCURSION,
                maxKneeExcursion = com.design.assistant.model.StandardConstants.FMVSS213.MAX_KNEE_EXCURSION,
                maxHeadRotation = 30.0,
                maxTorsoRotation = 30.0,
                hPointX = 0.0,
                hPointY = 0.0,
                minSideWingDepth = 70.0,
                minSideWingWidth = 140.0,
                minHarnessWidth = 100.0,
                hasISOFIXSupport = false,
                hasLATCHSupport = true,
                isofixBarWidth = 0.0,
                latchStrapCount = com.design.assistant.model.StandardConstants.FMVSS213.MIN_LATCH_STRAPS,
                region = "美国"
            )
        }

        /**
         * 创建AS/NZS1754假人参数（澳大利亚/新西兰）
         */
        fun createAS_NZS1754_I(): ChildSeatStandardEntity {
            return ChildSeatStandardEntity(
                standardType = "AS_NZS1754",
                standardName = "AS/NZS 1754",
                dummyType = "I-Group",
                weight = 15.0,
                height = 83.0,
                maxHeadInjuryCriterion = com.design.assistant.model.StandardConstants.AS_NZS1754.MAX_HIC_I,
                maxChestAcceleration = com.design.assistant.model.StandardConstants.AS_NZS1754.MAX_CHEST_ACCEL,
                maxNeckMoment = 30.0,
                maxChestDeflection = 30.0,
                maxHeadExcursion = com.design.assistant.model.StandardConstants.AS_NZS1754.MAX_HEAD_EXCURSION,
                maxKneeExcursion = com.design.assistant.model.StandardConstants.AS_NZS1754.MAX_HEAD_EXCURSION,
                maxHeadRotation = 30.0,
                maxTorsoRotation = 30.0,
                hPointX = 0.0,
                hPointY = 0.0,
                minSideWingDepth = 70.0,
                minSideWingWidth = 140.0,
                minHarnessWidth = 100.0,
                hasISOFIXSupport = true,
                hasLATCHSupport = false,
                isofixBarWidth = 280.0,
                latchStrapCount = 0,
                region = "澳大利亚/新西兰"
            )
        }
    }
}

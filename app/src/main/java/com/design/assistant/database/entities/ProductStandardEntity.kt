package com.design.assistant.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 产品标准数据库实体
 * 用于存储婴儿推车、高脚椅、儿童床等产品的标准设计参数数据
 */
@Entity(tableName = "product_standard_params")
data class ProductStandardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // 产品信息
    val productType: String,                   // 产品类型（STROLLER、HIGH_CHAIR、CRIB）
    val productName: String,                   // 产品名称

    // 标准信息
    val standardType: String,                  // 标准类型（EN1888、ASTM_F833等）
    val standardName: String,                  // 标准名称
    val region: String,                        // 地区（欧洲、美国、加拿大等）

    // 尺寸参数
    val minSeatHeight: Double = 0.0,           // 最小座椅高度（mm）
    val maxStrollerWidth: Double = 0.0,        // 最大推车宽度（mm）
    val minWheelDiameter: Double = 0.0,        // 最小轮径（mm）
    val minHandleHeight: Double = 0.0,         // 最小手柄高度（mm）

    // 结构参数
    val minTrayDepth: Double = 0.0,            // 最小托盘深度（mm）
    val maxTrayGap: Double = 0.0,              // 最大托盘间隙（mm）
    val minLegSpread: Double = 0.0,            // 最小腿部间距（mm）
    val minStabilityAngle: Double = 0.0,       // 最小稳定角度（°）

    // 材质参数
    val minMattressThickness: Double = 0.0,    // 最小床垫厚度（mm）
    val maxBarSpacing: Double = 0.0,           // 最大栏杆间距（mm）
    val minRailHeight: Double = 0.0,           // 最小护栏高度（mm）

    // 安全参数
    val minBrakeForce: Double = 0.0,           // 最小刹车力（N）
    val minLockingMechanismStrength: Double = 0.0,  // 最小锁定机构强度（N）

    // 功能参数
    val hasFoldableFeature: Boolean = false,   // 是否可折叠
    val hasAdjustableHeight: Boolean = false,  // 是否可调高度
    val hasReclineFeature: Boolean = false,    // 是否可后仰

    // 重量参数
    val maxWeightCapacity: Double = 0.0,       // 最大承重（kg）
    val minProductWeight: Double = 0.0,        // 最小产品自重（kg）

    // 额外信息（JSON格式）
    val extraInfo: String = ""
) {
    companion object {
        /**
         * 创建EN1888婴儿推车参数（欧洲）
         */
        fun createEN1888_Stroller(): ProductStandardEntity {
            return ProductStandardEntity(
                productType = "STROLLER",
                productName = "婴儿推车",
                standardType = "EN1888",
                standardName = "EN 1888",
                region = "欧洲",
                minSeatHeight = 0.0,
                maxStrollerWidth = 600.0,
                minWheelDiameter = com.design.assistant.model.StandardConstants.StrollerStandards.EN1888_MIN_WHEEL_DIA,
                minHandleHeight = com.design.assistant.model.StandardConstants.StrollerStandards.EN1888_MIN_HANDLE_HEIGHT,
                minTrayDepth = 0.0,
                maxTrayGap = 0.0,
                minLegSpread = 0.0,
                minStabilityAngle = 0.0,
                minMattressThickness = 0.0,
                maxBarSpacing = 0.0,
                minRailHeight = 0.0,
                minBrakeForce = com.design.assistant.model.StandardConstants.StrollerStandards.EN1888_MIN_BRAKE_FORCE,
                minLockingMechanismStrength = 100.0,
                hasFoldableFeature = true,
                hasAdjustableHeight = true,
                hasReclineFeature = true,
                maxWeightCapacity = 22.0,
                minProductWeight = 8.0
            )
        }

        /**
         * 创建ASTM_F833婴儿推车参数（美国）
         */
        fun createASTM_F833_Stroller(): ProductStandardEntity {
            return ProductStandardEntity(
                productType = "STROLLER",
                productName = "婴儿推车",
                standardType = "ASTM_F833",
                standardName = "ASTM F833",
                region = "美国",
                minSeatHeight = 0.0,
                maxStrollerWidth = com.design.assistant.model.StandardConstants.StrollerStandards.ASTM_F833_MAX_STROLLER_WIDTH,
                minWheelDiameter = com.design.assistant.model.StandardConstants.StrollerStandards.ASTM_F833_MIN_WHEEL_DIA,
                minHandleHeight = 800.0,
                minTrayDepth = 0.0,
                maxTrayGap = 0.0,
                minLegSpread = 0.0,
                minStabilityAngle = 0.0,
                minMattressThickness = 0.0,
                maxBarSpacing = 0.0,
                minRailHeight = 0.0,
                minBrakeForce = 50.0,
                minLockingMechanismStrength = 100.0,
                hasFoldableFeature = true,
                hasAdjustableHeight = true,
                hasReclineFeature = true,
                maxWeightCapacity = 22.0,
                minProductWeight = 8.0
            )
        }

        /**
         * 创建CSA_B311婴儿推车参数（加拿大）
         */
        fun createCSA_B311_Stroller(): ProductStandardEntity {
            return ProductStandardEntity(
                productType = "STROLLER",
                productName = "婴儿推车",
                standardType = "CSA_B311",
                standardName = "CSA B311",
                region = "加拿大",
                minSeatHeight = 0.0,
                maxStrollerWidth = 600.0,
                minWheelDiameter = com.design.assistant.model.StandardConstants.StrollerStandards.CSA_B311_MIN_WHEEL_DIA,
                minHandleHeight = 800.0,
                minTrayDepth = 0.0,
                maxTrayGap = 0.0,
                minLegSpread = 0.0,
                minStabilityAngle = 0.0,
                minMattressThickness = 0.0,
                maxBarSpacing = 0.0,
                minRailHeight = 0.0,
                minBrakeForce = com.design.assistant.model.StandardConstants.StrollerStandards.CSA_B311_MIN_BRAKE_FORCE,
                minLockingMechanismStrength = 100.0,
                hasFoldableFeature = true,
                hasAdjustableHeight = true,
                hasReclineFeature = true,
                maxWeightCapacity = 22.0,
                minProductWeight = 8.0
            )
        }

        /**
         * 创建EN14988高脚椅参数（欧洲）
         */
        fun createEN14988_HighChair(): ProductStandardEntity {
            return ProductStandardEntity(
                productType = "HIGH_CHAIR",
                productName = "儿童高脚椅",
                standardType = "EN14988",
                standardName = "EN 14988",
                region = "欧洲",
                minSeatHeight = com.design.assistant.model.StandardConstants.HighChairStandards.EN14988_MIN_SEAT_HEIGHT,
                maxStrollerWidth = 0.0,
                minWheelDiameter = 0.0,
                minHandleHeight = 0.0,
                minTrayDepth = com.design.assistant.model.StandardConstants.HighChairStandards.EN14988_MIN_TRAY_DEPTH,
                maxTrayGap = com.design.assistant.model.StandardConstants.HighChairStandards.EN14988_MAX_TRAY_GAP,
                minLegSpread = 0.0,
                minStabilityAngle = 0.0,
                minMattressThickness = 0.0,
                maxBarSpacing = 0.0,
                minRailHeight = 0.0,
                minBrakeForce = 0.0,
                minLockingMechanismStrength = 200.0,
                hasFoldableFeature = true,
                hasAdjustableHeight = true,
                hasReclineFeature = false,
                maxWeightCapacity = 23.0,
                minProductWeight = 5.0
            )
        }

        /**
         * 创建ASTM_F404高脚椅参数（美国）
         */
        fun createASTM_F404_HighChair(): ProductStandardEntity {
            return ProductStandardEntity(
                productType = "HIGH_CHAIR",
                productName = "儿童高脚椅",
                standardType = "ASTM_F404",
                standardName = "ASTM F404",
                region = "美国",
                minSeatHeight = com.design.assistant.model.StandardConstants.HighChairStandards.ASTM_F404_MIN_SEAT_HEIGHT,
                maxStrollerWidth = 0.0,
                minWheelDiameter = 0.0,
                minHandleHeight = 0.0,
                minTrayDepth = 50.0,
                maxTrayGap = 30.0,
                minLegSpread = com.design.assistant.model.StandardConstants.HighChairStandards.ASTM_F404_MIN_LEG_SPREAD,
                minStabilityAngle = 0.0,
                minMattressThickness = 0.0,
                maxBarSpacing = 0.0,
                minRailHeight = 0.0,
                minBrakeForce = 0.0,
                minLockingMechanismStrength = 200.0,
                hasFoldableFeature = true,
                hasAdjustableHeight = true,
                hasReclineFeature = false,
                maxWeightCapacity = 23.0,
                minProductWeight = 5.0
            )
        }

        /**
         * 创建CSA_B229高脚椅参数（加拿大）
         */
        fun createCSA_B229_HighChair(): ProductStandardEntity {
            return ProductStandardEntity(
                productType = "HIGH_CHAIR",
                productName = "儿童高脚椅",
                standardType = "CSA_B229",
                standardName = "CSA B229",
                region = "加拿大",
                minSeatHeight = com.design.assistant.model.StandardConstants.HighChairStandards.CSA_B229_MIN_SEAT_HEIGHT,
                maxStrollerWidth = 0.0,
                minWheelDiameter = 0.0,
                minHandleHeight = 0.0,
                minTrayDepth = 50.0,
                maxTrayGap = 30.0,
                minLegSpread = 400.0,
                minStabilityAngle = com.design.assistant.model.StandardConstants.HighChairStandards.CSA_B229_MIN_STABILITY_ANGLE,
                minMattressThickness = 0.0,
                maxBarSpacing = 0.0,
                minRailHeight = 0.0,
                minBrakeForce = 0.0,
                minLockingMechanismStrength = 200.0,
                hasFoldableFeature = true,
                hasAdjustableHeight = true,
                hasReclineFeature = false,
                maxWeightCapacity = 23.0,
                minProductWeight = 5.0
            )
        }

        /**
         * 创建EN716儿童床参数（欧洲）
         */
        fun createEN716_Crib(): ProductStandardEntity {
            return ProductStandardEntity(
                productType = "CRIB",
                productName = "儿童床",
                standardType = "EN716",
                standardName = "EN 716",
                region = "欧洲",
                minSeatHeight = 0.0,
                maxStrollerWidth = 0.0,
                minWheelDiameter = 0.0,
                minHandleHeight = 0.0,
                minTrayDepth = 0.0,
                maxTrayGap = 0.0,
                minLegSpread = 0.0,
                minStabilityAngle = 0.0,
                minMattressThickness = com.design.assistant.model.StandardConstants.CribStandards.EN716_MIN_MATTRESS_THICKNESS,
                maxBarSpacing = com.design.assistant.model.StandardConstants.CribStandards.EN716_MAX_BAR_SPACING,
                minRailHeight = com.design.assistant.model.StandardConstants.CribStandards.EN716_MIN_RAIL_HEIGHT,
                minBrakeForce = 0.0,
                minLockingMechanismStrength = 300.0,
                hasFoldableFeature = false,
                hasAdjustableHeight = true,
                hasReclineFeature = false,
                maxWeightCapacity = 15.0,
                minProductWeight = 15.0
            )
        }

        /**
         * 创建ASTM_F1169儿童床参数（美国）
         */
        fun createASTM_F1169_Crib(): ProductStandardEntity {
            return ProductStandardEntity(
                productType = "CRIB",
                productName = "儿童床",
                standardType = "ASTM_F1169",
                standardName = "ASTM F1169",
                region = "美国",
                minSeatHeight = 0.0,
                maxStrollerWidth = 0.0,
                minWheelDiameter = 0.0,
                minHandleHeight = 0.0,
                minTrayDepth = 0.0,
                maxTrayGap = 0.0,
                minLegSpread = 0.0,
                minStabilityAngle = 0.0,
                minMattressThickness = com.design.assistant.model.StandardConstants.CribStandards.ASTM_F1169_MIN_MATTRESS_THICKNESS,
                maxBarSpacing = com.design.assistant.model.StandardConstants.CribStandards.ASTM_F1169_MAX_BAR_SPACING,
                minRailHeight = com.design.assistant.model.StandardConstants.CribStandards.ASTM_F1169_MIN_RAIL_HEIGHT,
                minBrakeForce = 0.0,
                minLockingMechanismStrength = 300.0,
                hasFoldableFeature = false,
                hasAdjustableHeight = true,
                hasReclineFeature = false,
                maxWeightCapacity = 15.0,
                minProductWeight = 15.0
            )
        }

        /**
         * 创建CSA_B113儿童床参数（加拿大）
         */
        fun createCSA_B113_Crib(): ProductStandardEntity {
            return ProductStandardEntity(
                productType = "CRIB",
                productName = "儿童床",
                standardType = "CSA_B113",
                standardName = "CSA B113",
                region = "加拿大",
                minSeatHeight = 0.0,
                maxStrollerWidth = 0.0,
                minWheelDiameter = 0.0,
                minHandleHeight = 0.0,
                minTrayDepth = 0.0,
                maxTrayGap = 0.0,
                minLegSpread = 0.0,
                minStabilityAngle = 0.0,
                minMattressThickness = com.design.assistant.model.StandardConstants.CribStandards.CSA_B113_MIN_MATTRESS_THICKNESS,
                maxBarSpacing = com.design.assistant.model.StandardConstants.CribStandards.CSA_B113_MAX_BAR_SPACING,
                minRailHeight = 300.0,
                minBrakeForce = 0.0,
                minLockingMechanismStrength = 300.0,
                hasFoldableFeature = false,
                hasAdjustableHeight = true,
                hasReclineFeature = false,
                maxWeightCapacity = 15.0,
                minProductWeight = 15.0
            )
        }
    }
}

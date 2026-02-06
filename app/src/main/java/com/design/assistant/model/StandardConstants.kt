package com.design.assistant.model

/**
 * 标准常量定义
 * 包含各标准的关键参数和限值
 */
object StandardConstants {

    // ========== GPS028 (GB 27887-2011) 常量 ==========
    object GPS028 {
        // 头部伤害指标HIC限值
        const val MAX_HIC_0_GROUP = 570          // 0组假人HIC限值
        const val MAX_HIC_0_PLUS_GROUP = 570     // 0+组假人HIC限值
        const val MAX_HIC_I_GROUP = 650          // I组假人HIC限值
        const val MAX_HIC_II_GROUP = 650         // II组假人HIC限值
        const val MAX_HIC_III_GROUP = 650        // III组假人HIC限值

        // 胸部加速度限值（g）
        const val MAX_CHEST_ACCEL_0_GROUP = 55.0
        const val MAX_CHEST_ACCEL_0_PLUS_GROUP = 55.0
        const val MAX_CHEST_ACCEL_I_GROUP = 55.0
        const val MAX_CHEST_ACCEL_II_GROUP = 55.0
        const val MAX_CHEST_ACCEL_III_GROUP = 55.0

        // 颈部力矩限值（Nm）
        const val MAX_NECK_MOMENT_0_GROUP = 30.0
        const val MAX_NECK_MOMENT_0_PLUS_GROUP = 30.0
        const val MAX_NECK_MOMENT_I_GROUP = 30.0
        const val MAX_NECK_MOMENT_II_GROUP = 40.0
        const val MAX_NECK_MOMENT_III_GROUP = 40.0

        // 假人位移限值（mm）
        const val MAX_HEAD_EXCURSION = 550.0
        const val MAX_KNEE_EXCURSION = 550.0

        // 带宽要求（mm）
        const val MIN_LAP_BELT_WIDTH = 25.0
        const val MIN_SHOULDER_BELT_WIDTH = 20.0

        // H点公差（mm）
        const val H_POINT_TOLERANCE = 10.0

        // 百分位定义
        const val PERCENTILE_50 = 50
        const val PERCENTILE_75 = 75
        const val PERCENTILE_95 = 95
    }

    // ========== ECE R129 (i-Size) 常量 ==========
    object ECE_R129 {
        // 头部伤害指标HIC限值
        const val MAX_HIC_Q1 = 570
        const val MAX_HIC_Q1_5 = 570
        const val MAX_HIC_Q3 = 650
        const val MAX_HIC_Q6 = 650
        const val MAX_HIC_Q10 = 650

        // 胸部加速度限值（g）
        const val MAX_CHEST_ACCEL = 55.0

        // 颈部力矩限值（Nm）
        const val MAX_NECK_MOMENT_Q1 = 30.0
        const val MAX_NECK_MOMENT_Q1_5 = 30.0
        const val MAX_NECK_MOMENT_Q3 = 30.0
        const val MAX_NECK_MOMENT_Q6 = 40.0
        const val MAX_NECK_MOMENT_Q10 = 40.0

        // 假人位移限值（mm）
        const val MAX_HEAD_EXCURSION = 550.0

        // ISOFIX接口尺寸（mm）
        const val ISOFIX_BAR_WIDTH = 280.0
        const val ISOFIX_BAR_DEPTH = 25.0
        const val ISOFIX_BAR_TOLERANCE = 2.0
    }

    // ========== CMVSS213 (加拿大) 常量 ==========
    object CMVSS213 {
        // 头部伤害指标HIC限值
        const val MAX_HIC_INFANT = 570
        const val MAX_HIC_TODDLER = 650
        const val MAX_HIC_CHILD = 650

        // 胸部加速度限值（g）
        const val MAX_CHEST_ACCEL = 55.0

        // 假人位移限值（mm）
        const val MAX_HEAD_EXCURSION = 720.0
        const val MAX_KNEE_EXCURSION = 720.0

        // UAS系统要求
        const val MIN_UAS_STRAPS = 2
        const val MAX_UAS_SPACING = 280.0
    }

    // ========== FMVSS213 (美国) 常量 ==========
    object FMVSS213 {
        // 头部伤害指标HIC限值
        const val MAX_HIC_INFANT = 570
        const val MAX_HIC_TODDLER = 650
        const val MAX_HIC_CHILD = 1000

        // 胸部加速度限值（g）
        const val MAX_CHEST_ACCEL = 60.0

        // 假人位移限值（mm）
        const val MAX_HEAD_EXCURSION = 720.0
        const val MAX_KNEE_EXCURSION = 720.0

        // LATCH系统要求
        const val MIN_LATCH_STRAPS = 2
        const val MAX_LATCH_SPACING = 280.0
    }

    // ========== AS/NZS1754 (澳大利亚/新西兰) 常量 ==========
    object AS_NZS1754 {
        // 头部伤害指标HIC限值
        const val MAX_HIC_0_0 = 570
        const val MAX_HIC_I = 650
        const val MAX_HIC_II = 650
        const val MAX_HIC_III = 650

        // 胸部加速度限值（g）
        const val MAX_CHEST_ACCEL = 55.0

        // 假人位移限值（mm）
        const val MAX_HEAD_EXCURSION = 550.0
    }

    // ========== 婴儿推车标准常量 ==========
    object StrollerStandards {
        // EN1888 (欧洲)
        const val EN1888_MIN_WHEEL_DIA = 50.0      // 最小轮径（mm）
        const val EN1888_MIN_HANDLE_HEIGHT = 800.0 // 最小手柄高度（mm）
        const val EN1888_MIN_BRAKE_FORCE = 50.0    // 最小刹车力（N）

        // ASTM F833 (美国)
        const val ASTM_F833_MIN_WHEEL_DIA = 50.0
        const val ASTM_F833_MAX_STROLLER_WIDTH = 600.0

        // CSA B311 (加拿大)
        const val CSA_B311_MIN_WHEEL_DIA = 50.0
        const val CSA_B311_MIN_BRAKE_FORCE = 50.0
    }

    // ========== 高脚椅标准常量 ==========
    object HighChairStandards {
        // EN14988 (欧洲)
        const val EN14988_MIN_SEAT_HEIGHT = 450.0      // 最小座椅高度（mm）
        const val EN14988_MIN_TRAY_DEPTH = 50.0        // 最小托盘深度（mm）
        const val EN14988_MAX_TRAY_GAP = 30.0          // 最大托盘间隙（mm）

        // ASTM F404 (美国)
        const val ASTM_F404_MIN_SEAT_HEIGHT = 450.0
        const val ASTM_F404_MIN_LEG_SPREAD = 400.0     // 最小腿部间距（mm）

        // CSA B229 (加拿大)
        const val CSA_B229_MIN_SEAT_HEIGHT = 450.0
        const val CSA_B229_MIN_STABILITY_ANGLE = 10.0  // 最小稳定角度（°）
    }

    // ========== 儿童床标准常量 ==========
    object CribStandards {
        // EN716 (欧洲)
        const val EN716_MIN_MATTRESS_THICKNESS = 80.0   // 最小床垫厚度（mm）
        const val EN716_MAX_BAR_SPACING = 60.0          // 最大栏杆间距（mm）
        const val EN716_MIN_RAIL_HEIGHT = 300.0         // 最小护栏高度（mm）

        // ASTM F1169 (美国)
        const val ASTM_F1169_MIN_MATTRESS_THICKNESS = 80.0
        const val ASTM_F1169_MAX_BAR_SPACING = 60.0
        const val ASTM_F1169_MIN_RAIL_HEIGHT = 300.0

        // CSA B113 (加拿大)
        const val CSA_B113_MIN_MATTRESS_THICKNESS = 80.0
        const val CSA_B113_MAX_BAR_SPACING = 60.0
    }

    // ========== 通用公差常量 ==========
    object Tolerances {
        const val POSITION_TOLERANCE = 2.0           // 位置公差（mm）
        const val DIMENSION_TOLERANCE = 5.0          // 尺寸公差（mm）
        const val ANGLE_TOLERANCE = 2.0              // 角度公差（°）
        const val WEIGHT_TOLERANCE = 0.5             // 重量公差（kg）
    }

    // ========== 百分位参数映射 ==========
    fun getGPS028PercentileParams(percentile: Int, group: GPS028Group): GPS028Params {
        return when (group) {
            GPS028Group.GROUP_0 -> getGroup0Params(percentile)
            GPS028Group.GROUP_0P -> getGroup0PlusParams(percentile)
            GPS028Group.GROUP_I -> getGroupIParams(percentile)
            GPS028Group.GROUP_II -> getGroupIIParams(percentile)
            GPS028Group.GROUP_III -> getGroupIIIParams(percentile)
        }
    }

    private fun getGroup0Params(percentile: Int): GPS028Params {
        return GPS028Params(
            groupName = "0组",
            percentile = "${percentile}%",
            weight = if (percentile == 50) 6.0 else if (percentile == 75) 8.0 else 10.0,
            height = if (percentile == 50) 62.0 else if (percentile == 75) 66.0 else 70.0,
            age = "0-9个月",
            headWidth = if (percentile == 50) 95.0 else if (percentile == 75) 100.0 else 105.0,
            headDepth = if (percentile == 50) 110.0 else if (percentile == 75) 115.0 else 120.0,
            headHeight = if (percentile == 50) 105.0 else if (percentile == 75) 110.0 else 115.0,
            headCircumference = if (percentile == 50) 420.0 else if (percentile == 75) 440.0 else 460.0,
            neckWidth = 60.0,
            neckLength = 40.0,
            shoulderWidth = if (percentile == 50) 170.0 else if (percentile == 75) 180.0 else 190.0,
            shoulderHeight = if (percentile == 50) 300.0 else if (percentile == 75) 310.0 else 320.0,
            chestWidth = if (percentile == 50) 130.0 else if (percentile == 75) 140.0 else 150.0,
            chestDepth = if (percentile == 50) 100.0 else if (percentile == 75) 110.0 else 120.0,
            chestCircumference = if (percentile == 50) 380.0 else if (percentile == 75) 410.0 else 440.0,
            waistWidth = if (percentile == 50) 120.0 else if (percentile == 75) 130.0 else 140.0,
            waistDepth = if (percentile == 50) 90.0 else if (percentile == 75) 100.0 else 110.0,
            waistCircumference = if (percentile == 50) 370.0 else if (percentile == 75) 400.0 else 430.0,
            hipWidth = if (percentile == 50) 110.0 else if (percentile == 75) 120.0 else 130.0,
            hipDepth = if (percentile == 50) 85.0 else if (percentile == 75) 95.0 else 105.0,
            hipCircumference = if (percentile == 50) 350.0 else if (percentile == 75) 380.0 else 410.0,
            armLength = 130.0,
            upperArmLength = 80.0,
            forearmLength = 50.0,
            handLength = 80.0,
            legLength = 180.0,
            thighLength = 100.0,
            calfLength = 80.0,
            footLength = if (percentile == 50) 95.0 else if (percentile == 75) 100.0 else 105.0,
            footWidth = if (percentile == 50) 45.0 else if (percentile == 75) 48.0 else 51.0,
            hPoint = Point(0.0, 0.0),
            headReferencePoint = Point(0.0, 150.0),
            shoulderReferencePoint = Point(0.0, 300.0),
            kneeReferencePoint = Point(200.0, -100.0),
            maxHeadInjuryCriterion = GPS028.MAX_HIC_0_GROUP,
            maxChestAcceleration = GPS028.MAX_CHEST_ACCEL_0_GROUP,
            maxNeckMoment = GPS028.MAX_NECK_MOMENT_0_GROUP,
            maxChestDeflection = 30.0,
            maxHeadExcursion = GPS028.MAX_HEAD_EXCURSION,
            maxKneeExcursion = GPS028.MAX_KNEE_EXCURSION,
            maxHeadRotation = 30.0,
            maxTorsoRotation = 30.0,
            lapBeltWidth = GPS028.MIN_LAP_BELT_WIDTH,
            shoulderBeltWidth = GPS028.MIN_SHOULDER_BELT_WIDTH,
            lapBeltAngle = 45.0,
            shoulderBeltAngle = 30.0,
            minHeadSupportHeight = 200.0,
            minSideWingDepth = 50.0,
            minSideWingWidth = 100.0,
            minHarnessWidth = 80.0,
            minCrotchBuckleDistance = 120.0
        )
    }

    private fun getGroup0PlusParams(percentile: Int): GPS028Params {
        // 类似0组，但体重和身高范围更大
        return getGroup0Params(percentile).copy(
            groupName = "0+组",
            age = "0-15个月",
            weight = if (percentile == 50) 7.0 else if (percentile == 75) 10.0 else 13.0,
            height = if (percentile == 50) 65.0 else if (percentile == 75) 70.0 else 75.0
        )
    }

    private fun getGroupIParams(percentile: Int): GPS028Params {
        return GPS028Params(
            groupName = "I组",
            percentile = "${percentile}%",
            weight = if (percentile == 50) 12.0 else if (percentile == 75) 15.0 else 18.0,
            height = if (percentile == 50) 80.0 else if (percentile == 75) 85.0 else 90.0,
            age = "9个月-4岁",
            headWidth = if (percentile == 50) 105.0 else if (percentile == 75) 110.0 else 115.0,
            headDepth = if (percentile == 50) 120.0 else if (percentile == 75) 125.0 else 130.0,
            headHeight = if (percentile == 50) 115.0 else if (percentile == 75) 120.0 else 125.0,
            headCircumference = if (percentile == 50) 460.0 else if (percentile == 75) 480.0 else 500.0,
            neckWidth = 65.0,
            neckLength = 45.0,
            shoulderWidth = if (percentile == 50) 190.0 else if (percentile == 75) 200.0 else 210.0,
            shoulderHeight = if (percentile == 50) 320.0 else if (percentile == 75) 330.0 else 340.0,
            chestWidth = if (percentile == 50) 150.0 else if (percentile == 75) 160.0 else 170.0,
            chestDepth = if (percentile == 50) 110.0 else if (percentile == 75) 120.0 else 130.0,
            chestCircumference = if (percentile == 50) 440.0 else if (percentile == 75) 470.0 else 500.0,
            waistWidth = if (percentile == 50) 140.0 else if (percentile == 75) 150.0 else 160.0,
            waistDepth = if (percentile == 50) 100.0 else if (percentile == 75) 110.0 else 120.0,
            waistCircumference = if (percentile == 50) 430.0 else if (percentile == 75) 460.0 else 490.0,
            hipWidth = if (percentile == 50) 130.0 else if (percentile == 75) 140.0 else 150.0,
            hipDepth = if (percentile == 50) 95.0 else if (percentile == 75) 105.0 else 115.0,
            hipCircumference = if (percentile == 50) 410.0 else if (percentile == 75) 440.0 else 470.0,
            armLength = 160.0,
            upperArmLength = 100.0,
            forearmLength = 60.0,
            handLength = 100.0,
            legLength = 250.0,
            thighLength = 140.0,
            calfLength = 110.0,
            footLength = if (percentile == 50) 120.0 else if (percentile == 75) 130.0 else 140.0,
            footWidth = if (percentile == 50) 55.0 else if (percentile == 75) 60.0 else 65.0,
            hPoint = Point(0.0, 0.0),
            headReferencePoint = Point(0.0, 170.0),
            shoulderReferencePoint = Point(0.0, 320.0),
            kneeReferencePoint = Point(220.0, -120.0),
            maxHeadInjuryCriterion = GPS028.MAX_HIC_I_GROUP,
            maxChestAcceleration = GPS028.MAX_CHEST_ACCEL_I_GROUP,
            maxNeckMoment = GPS028.MAX_NECK_MOMENT_I_GROUP,
            maxChestDeflection = 30.0,
            maxHeadExcursion = GPS028.MAX_HEAD_EXCURSION,
            maxKneeExcursion = GPS028.MAX_KNEE_EXCURSION,
            maxHeadRotation = 30.0,
            maxTorsoRotation = 30.0,
            lapBeltWidth = GPS028.MIN_LAP_BELT_WIDTH,
            shoulderBeltWidth = GPS028.MIN_SHOULDER_BELT_WIDTH,
            lapBeltAngle = 45.0,
            shoulderBeltAngle = 30.0,
            minHeadSupportHeight = 220.0,
            minSideWingDepth = 60.0,
            minSideWingWidth = 120.0,
            minHarnessWidth = 90.0,
            minCrotchBuckleDistance = 140.0
        )
    }

    private fun getGroupIIParams(percentile: Int): GPS028Params {
        return GPS028Params(
            groupName = "II组",
            percentile = "${percentile}%",
            weight = if (percentile == 50) 18.0 else if (percentile == 75) 22.0 else 25.0,
            height = if (percentile == 50) 100.0 else if (percentile == 75) 105.0 else 110.0,
            age = "3-6岁",
            headWidth = if (percentile == 50) 110.0 else if (percentile == 75) 115.0 else 120.0,
            headDepth = if (percentile == 50) 125.0 else if (percentile == 75) 130.0 else 135.0,
            headHeight = if (percentile == 50) 120.0 else if (percentile == 75) 125.0 else 130.0,
            headCircumference = if (percentile == 50) 500.0 else if (percentile == 75) 520.0 else 540.0,
            neckWidth = 70.0,
            neckLength = 50.0,
            shoulderWidth = if (percentile == 50) 210.0 else if (percentile == 75) 220.0 else 230.0,
            shoulderHeight = if (percentile == 50) 340.0 else if (percentile == 75) 350.0 else 360.0,
            chestWidth = if (percentile == 50) 170.0 else if (percentile == 75) 180.0 else 190.0,
            chestDepth = if (percentile == 50) 120.0 else if (percentile == 75) 130.0 else 140.0,
            chestCircumference = if (percentile == 50) 500.0 else if (percentile == 75) 530.0 else 560.0,
            waistWidth = if (percentile == 50) 160.0 else if (percentile == 75) 170.0 else 180.0,
            waistDepth = if (percentile == 50) 110.0 else if (percentile == 75) 120.0 else 130.0,
            waistCircumference = if (percentile == 50) 490.0 else if (percentile == 75) 520.0 else 550.0,
            hipWidth = if (percentile == 50) 150.0 else if (percentile == 75) 160.0 else 170.0,
            hipDepth = if (percentile == 50) 105.0 else if (percentile == 75) 115.0 else 125.0,
            hipCircumference = if (percentile == 50) 470.0 else if (percentile == 75) 500.0 else 530.0,
            armLength = 190.0,
            upperArmLength = 120.0,
            forearmLength = 70.0,
            handLength = 110.0,
            legLength = 320.0,
            thighLength = 180.0,
            calfLength = 140.0,
            footLength = if (percentile == 50) 140.0 else if (percentile == 75) 150.0 else 160.0,
            footWidth = if (percentile == 50) 65.0 else if (percentile == 75) 70.0 else 75.0,
            hPoint = Point(0.0, 0.0),
            headReferencePoint = Point(0.0, 190.0),
            shoulderReferencePoint = Point(0.0, 340.0),
            kneeReferencePoint = Point(250.0, -140.0),
            maxHeadInjuryCriterion = GPS028.MAX_HIC_II_GROUP,
            maxChestAcceleration = GPS028.MAX_CHEST_ACCEL_II_GROUP,
            maxNeckMoment = GPS028.MAX_NECK_MOMENT_II_GROUP,
            maxChestDeflection = 35.0,
            maxHeadExcursion = GPS028.MAX_HEAD_EXCURSION,
            maxKneeExcursion = GPS028.MAX_KNEE_EXCURSION,
            maxHeadRotation = 35.0,
            maxTorsoRotation = 35.0,
            lapBeltWidth = GPS028.MIN_LAP_BELT_WIDTH,
            shoulderBeltWidth = GPS028.MIN_SHOULDER_BELT_WIDTH,
            lapBeltAngle = 45.0,
            shoulderBeltAngle = 30.0,
            minHeadSupportHeight = 240.0,
            minSideWingDepth = 70.0,
            minSideWingWidth = 140.0,
            minHarnessWidth = 100.0,
            minCrotchBuckleDistance = 160.0
        )
    }

    private fun getGroupIIIParams(percentile: Int): GPS028Params {
        return GPS028Params(
            groupName = "III组",
            percentile = "${percentile}%",
            weight = if (percentile == 50) 25.0 else if (percentile == 75) 30.0 else 36.0,
            height = if (percentile == 50) 120.0 else if (percentile == 75) 130.0 else 140.0,
            age = "6-12岁",
            headWidth = if (percentile == 50) 115.0 else if (percentile == 75) 120.0 else 125.0,
            headDepth = if (percentile == 50) 130.0 else if (percentile == 75) 135.0 else 140.0,
            headHeight = if (percentile == 50) 125.0 else if (percentile == 75) 130.0 else 135.0,
            headCircumference = if (percentile == 50) 520.0 else if (percentile == 75) 540.0 else 560.0,
            neckWidth = 75.0,
            neckLength = 55.0,
            shoulderWidth = if (percentile == 50) 230.0 else if (percentile == 75) 240.0 else 250.0,
            shoulderHeight = if (percentile == 50) 360.0 else if (percentile == 75) 370.0 else 380.0,
            chestWidth = if (percentile == 50) 190.0 else if (percentile == 75) 200.0 else 210.0,
            chestDepth = if (percentile == 50) 130.0 else if (percentile == 75) 140.0 else 150.0,
            chestCircumference = if (percentile == 50) 560.0 else if (percentile == 75) 590.0 else 620.0,
            waistWidth = if (percentile == 50) 180.0 else if (percentile == 75) 190.0 else 200.0,
            waistDepth = if (percentile == 50) 120.0 else if (percentile == 75) 130.0 else 140.0,
            waistCircumference = if (percentile == 50) 550.0 else if (percentile == 75) 580.0 else 610.0,
            hipWidth = if (percentile == 50) 170.0 else if (percentile == 75) 180.0 else 190.0,
            hipDepth = if (percentile == 50) 115.0 else if (percentile == 75) 125.0 else 135.0,
            hipCircumference = if (percentile == 50) 530.0 else if (percentile == 75) 560.0 else 590.0,
            armLength = 220.0,
            upperArmLength = 140.0,
            forearmLength = 80.0,
            handLength = 130.0,
            legLength = 400.0,
            thighLength = 220.0,
            calfLength = 180.0,
            footLength = if (percentile == 50) 170.0 else if (percentile == 75) 180.0 else 190.0,
            footWidth = if (percentile == 50) 75.0 else if (percentile == 75) 80.0 else 85.0,
            hPoint = Point(0.0, 0.0),
            headReferencePoint = Point(0.0, 210.0),
            shoulderReferencePoint = Point(0.0, 360.0),
            kneeReferencePoint = Point(280.0, -160.0),
            maxHeadInjuryCriterion = GPS028.MAX_HIC_III_GROUP,
            maxChestAcceleration = GPS028.MAX_CHEST_ACCEL_III_GROUP,
            maxNeckMoment = GPS028.MAX_NECK_MOMENT_III_GROUP,
            maxChestDeflection = 40.0,
            maxHeadExcursion = GPS028.MAX_HEAD_EXCURSION,
            maxKneeExcursion = GPS028.MAX_KNEE_EXCURSION,
            maxHeadRotation = 40.0,
            maxTorsoRotation = 40.0,
            lapBeltWidth = GPS028.MIN_LAP_BELT_WIDTH,
            shoulderBeltWidth = GPS028.MIN_SHOULDER_BELT_WIDTH,
            lapBeltAngle = 45.0,
            shoulderBeltAngle = 30.0,
            minHeadSupportHeight = 260.0,
            minSideWingDepth = 80.0,
            minSideWingWidth = 160.0,
            minHarnessWidth = 110.0,
            minCrotchBuckleDistance = 180.0
        )
    }
}

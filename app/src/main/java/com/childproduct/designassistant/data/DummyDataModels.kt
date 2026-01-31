package com.childproduct.designassistant.data

/**
 * Dummy人体模型数据
 * 参考：R129 Annex 8, CFR Part 572等标准
 * 包含14种不同年龄段的人体模型参数
 */

/**
 * Dummy类型枚举
 * 包含从新生儿到12岁的14种Dummy类型
 */
enum class DummyType(
    val displayName: String,
    val code: String,
    val ageMonths: Int,
    val heightRangeCm: String,
    val weightRangeKg: String,
    val standard: String
) {
    // Q系列假人（ECE R129标准）
    Q0(
        displayName = "Q0 (新生儿)",
        code = "Q0",
        ageMonths = 0,
        heightRangeCm = "≤60",
        weightRangeKg = "0-3.5",
        standard = "ECE R129"
    ),
    Q1(
        displayName = "Q1 (6个月)",
        code = "Q1",
        ageMonths = 6,
        heightRangeCm = "60-75",
        weightRangeKg = "3.5-9",
        standard = "ECE R129"
    ),
    Q1_5(
        displayName = "Q1.5 (18个月)",
        code = "Q1.5",
        ageMonths = 18,
        heightRangeCm = "75-87",
        weightRangeKg = "9-13",
        standard = "ECE R129"
    ),
    Q3(
        displayName = "Q3 (3岁)",
        code = "Q3",
        ageMonths = 36,
        heightRangeCm = "87-105",
        weightRangeKg = "13-18",
        standard = "ECE R129"
    ),
    Q3S(
        displayName = "Q3s (3岁-美标侧面碰撞)",
        code = "Q3s",
        ageMonths = 36,
        heightRangeCm = "87-105",
        weightRangeKg = "13-18",
        standard = "FMVSS 213a (侧面碰撞)"
    ),
    Q6(
        displayName = "Q6 (6岁)",
        code = "Q6",
        ageMonths = 72,
        heightRangeCm = "105-125",
        weightRangeKg = "18-30",
        standard = "ECE R129"
    ),
    Q10(
        displayName = "Q10 (10岁)",
        code = "Q10",
        ageMonths = 120,
        heightRangeCm = "125-150",
        weightRangeKg = "30-50",
        standard = "ECE R129"
    ),

    // Hybrid III系列假人（美国CFR标准）
    CRABI_6(
        displayName = "CRABI 6个月",
        code = "CRABI-6",
        ageMonths = 6,
        heightRangeCm = "65",
        weightRangeKg = "7.7",
        standard = "CFR 572.211"
    ),
    CRABI_12(
        displayName = "CRABI 12个月",
        code = "CRABI-12",
        ageMonths = 12,
        heightRangeCm = "74",
        weightRangeKg = "10",
        standard = "CFR 572.211"
    ),
    CRABI_18(
        displayName = "CRABI 18个月",
        code = "CRABI-18",
        ageMonths = 18,
        heightRangeCm = "82",
        weightRangeKg = "11.2",
        standard = "CFR 572.211"
    ),
    HYBRID_III_3Y(
        displayName = "Hybrid III 3岁",
        code = "H3-3Y",
        ageMonths = 36,
        heightRangeCm = "90",
        weightRangeKg = "15.1",
        standard = "CFR 572.212"
    ),
    HYBRID_III_6Y(
        displayName = "Hybrid III 6岁",
        code = "H3-6Y",
        ageMonths = 72,
        heightRangeCm = "116",
        weightRangeKg = "21.5",
        standard = "CFR 572.212"
    ),

    // 其他专用假人
    PART_575_210(
        displayName = "Part 575.210 6个月",
        code = "P575-210",
        ageMonths = 6,
        heightRangeCm = "67",
        weightRangeKg = "8",
        standard = "ECE R44"
    ),
    SID_IIs_3Y(
        displayName = "SID IIs 3岁",
        code = "SID-IIs-3Y",
        ageMonths = 36,
        heightRangeCm = "88",
        weightRangeKg = "15",
        standard = "ECE R129 (Lateral)"
    ),
    SID_IIs_6Y(
        displayName = "SID IIs 6岁",
        code = "SID-IIs-6Y",
        ageMonths = 72,
        heightRangeCm = "117",
        weightRangeKg = "22",
        standard = "ECE R129 (Lateral)"
    );

    companion object {
        /**
         * 根据年龄获取对应的Dummy类型
         */
        fun getByAge(ageMonths: Int): DummyType? {
            return values().find { it.ageMonths == ageMonths }
        }

        /**
         * 根据身高范围获取Dummy类型
         */
        fun getByHeight(heightCm: Double): List<DummyType> {
            return values().filter { dummyType ->
                val range = dummyType.heightRangeCm
                when {
                    range.contains("≤") -> {
                        val max = range.replace("≤", "").toDouble()
                        heightCm <= max
                    }
                    range.contains("-") -> {
                        val parts = range.split("-")
                        val min = parts[0].toDouble()
                        val max = parts[1].toDouble()
                        heightCm >= min && heightCm <= max
                    }
                    else -> false
                }
            }
        }

        /**
         * 获取所有Q系列假人
         */
        fun getQSeries(): List<DummyType> {
            return values().filter { it.code.startsWith("Q") }
        }

        /**
         * 获取FMVSS 213a专用的Q3s假人
         */
        fun getQ3s(): DummyType {
            return Q3S
        }

        /**
         * 获取所有Hybrid III假人
         */
        fun getHybridIIISeries(): List<DummyType> {
            return values().filter { it.code.startsWith("H3") || it.code.startsWith("CRABI") }
        }
    }
}

/**
 * Dummy人体测量学参数
 * 包含头部、颈部、胸部、腹部、四肢等关键参数
 */
data class DummyAnthropometry(
    // 基本信息
    val dummyType: DummyType,
    val totalWeight: Double,                // 总重量（kg）
    val totalHeight: Double,                // 总高度（cm）
    val sittingHeight: Double,              // 坐高（cm）
    val shoulderHeight: Double,             // 肩高（cm）
    val hipHeight: Double,                  // 臀高（cm）
    val kneeHeight: Double,                 // 膝高（cm）

    // 头部参数
    val headLength: Double,                 // 头长（cm）
    val headBreadth: Double,                // 头宽（cm）
    val headCircumference: Double,          // 头围（cm）
    val headWeight: Double,                 // 头部重量（kg）
    val headCenterOfGravity: Double,        // 头部重心位置（cm）

    // 颈部参数
    val neckLength: Double,                 // 颈长（cm）
    val neckCircumference: Double,          // 颈围（cm）
    val neckWeight: Double,                 // 颈部重量（kg）

    // 胸部参数
    val chestDepth: Double,                 // 胸深（cm）
    val chestWidth: Double,                 // 胸宽（cm）
    val chestCircumference: Double,         // 胸围（cm）
    val chestWeight: Double,                // 胸部重量（kg）
    val chestDeflectionMax: Double,         // 胸部最大变形（mm）

    // 腹部参数
    val abdominalDepth: Double,             // 腹深（cm）
    val abdominalWidth: Double,             // 腹宽（cm）
    val abdominalCircumference: Double,     // 腹围（cm）
    val abdominalWeight: Double,            // 腹部重量（kg）
    val abdominalPressureMax: Double,       // 腹部最大压力（bar）

    // 肩部参数
    val shoulderWidth: Double,              // 肩宽（cm）
    val shoulderCircumference: Double,      // 肩围（cm）
    val acromionHeight: Double,             // 颧间距离（cm）

    // 臀部参数
    val hipWidth: Double,                   // 臀宽（cm）
    val hipCircumference: Double,           // 臀围（cm）
    val hipWeight: Double,                  // 臀部重量（kg）

    // 上肢参数
    val upperArmLength: Double,             // 上臂长（cm）
    val upperArmCircumference: Double,      // 上臂围（cm）
    val forearmLength: Double,              // 前臂长（cm）
    val forearmCircumference: Double,       // 前臂围（cm）
    val handLength: Double,                 // 手长（cm）
    val handWidth: Double,                  // 手宽（cm）
    val armWeight: Double,                  // 手臂总重量（kg）

    // 下肢参数
    val thighLength: Double,                // 大腿长（cm）
    val thighCircumference: Double,         // 大腿围（cm）
    val lowerLegLength: Double,             // 小腿长（cm）
    val lowerLegCircumference: Double,      // 小腿围（cm）
    val footLength: Double,                 // 足长（cm）
    val footWidth: Double,                  // 足宽（cm）
    val legWeight: Double,                  // 腿部总重量（kg）

    // 损伤评估参数
    val hpcLimit: Double,                   // HPC限值
    val headAcceleration3msLimit: Double,   // 头部加速度3ms限值（g）
    val chestAcceleration3msLimit: Double,  // 胸部加速度3ms限值（g）
    val neckForceLimit: Double,             // 颈部力限值（N）
    val neckMomentLimit: Double,            // 颈部力矩限值（Nm）
    val chestDeflectionLimit: Double,       // 胸部变形限值（mm）
) {
    /**
     * 计算BMI
     */
    fun calculateBMI(): Double {
        val heightInMeters = totalHeight / 100.0
        return totalWeight / (heightInMeters * heightInMeters)
    }

    /**
     * 获取关键参数摘要
     */
    fun getKeyParameters(): Map<String, Double> {
        return mapOf(
            "身高" to totalHeight,
            "体重" to totalWeight,
            "肩宽" to shoulderWidth,
            "臀宽" to hipWidth,
            "坐高" to sittingHeight,
            "头围" to headCircumference
        )
    }

    /**
     * 获取损伤评估限值
     */
    fun getInjuryCriteria(): Map<String, Double> {
        return mapOf(
            "HPC限值" to hpcLimit,
            "头部加速度3ms限值" to headAcceleration3msLimit,
            "胸部加速度3ms限值" to chestAcceleration3msLimit,
            "颈部力限值" to neckForceLimit,
            "颈部力矩限值" to neckMomentLimit,
            "胸部变形限值" to chestDeflectionLimit
        )
    }
}

/**
 * Dummy数据库
 * 包含14种Dummy类型的详细参数
 */
object DummyDatabase {
    /**
     * 获取指定Dummy类型的人体测量学数据
     */
    fun getDummyData(dummyType: DummyType): DummyAnthropometry {
        return when (dummyType) {
            DummyType.Q0 -> createQ0Data()
            DummyType.Q1 -> createQ1Data()
            DummyType.Q1_5 -> createQ1_5Data()
            DummyType.Q3 -> createQ3Data()
            DummyType.Q3S -> createQ3sData()
            DummyType.Q6 -> createQ6Data()
            DummyType.Q10 -> createQ10Data()
            DummyType.CRABI_6 -> createCRABI6Data()
            DummyType.CRABI_12 -> createCRABI12Data()
            DummyType.CRABI_18 -> createCRABI18Data()
            DummyType.HYBRID_III_3Y -> createHybridIII3YData()
            DummyType.HYBRID_III_6Y -> createHybridIII6YData()
            DummyType.PART_575_210 -> createPart575210Data()
            DummyType.SID_IIs_3Y -> createSIDIIs3YData()
            DummyType.SID_IIs_6Y -> createSIDIIs6YData()
        }
    }

    /**
     * 获取所有Dummy类型的数据
     */
    fun getAllDummyData(): Map<DummyType, DummyAnthropometry> {
        return DummyType.values().associateWith { getDummyData(it) }
    }

    // Q0数据（新生儿）
    private fun createQ0Data() = DummyAnthropometry(
        dummyType = DummyType.Q0,
        totalWeight = 3.5, totalHeight = 50.0, sittingHeight = 35.0,
        shoulderHeight = 30.0, hipHeight = 18.0, kneeHeight = 25.0,
        headLength = 12.0, headBreadth = 9.5, headCircumference = 34.0,
        headWeight = 0.8, headCenterOfGravity = 3.5,
        neckLength = 3.0, neckCircumference = 15.0, neckWeight = 0.2,
        chestDepth = 10.0, chestWidth = 14.0, chestCircumference = 40.0,
        chestWeight = 0.6, chestDeflectionMax = 35.0,
        abdominalDepth = 9.0, abdominalWidth = 13.0, abdominalCircumference = 38.0,
        abdominalWeight = 0.4, abdominalPressureMax = 1.2,
        shoulderWidth = 18.0, shoulderCircumference = 35.0, acromionHeight = 8.0,
        hipWidth = 16.0, hipCircumference = 42.0, hipWeight = 0.5,
        upperArmLength = 8.0, upperArmCircumference = 12.0,
        forearmLength = 7.0, forearmCircumference = 10.0,
        handLength = 5.0, handWidth = 4.0, armWeight = 0.4,
        thighLength = 10.0, thighCircumference = 14.0,
        lowerLegLength = 9.0, lowerLegCircumference = 11.0,
        footLength = 7.0, footWidth = 5.0, legWeight = 0.6,
        hpcLimit = 600.0, headAcceleration3msLimit = 75.0,
        chestAcceleration3msLimit = 55.0, neckForceLimit = 800.0,
        neckMomentLimit = 15.0, chestDeflectionLimit = 30.0
    )

    // Q1数据（6个月）
    private fun createQ1Data() = DummyAnthropometry(
        dummyType = DummyType.Q1,
        totalWeight = 7.5, totalHeight = 67.5, sittingHeight = 45.0,
        shoulderHeight = 38.0, hipHeight = 23.0, kneeHeight = 32.0,
        headLength = 14.5, headBreadth = 11.5, headCircumference = 42.0,
        headWeight = 1.2, headCenterOfGravity = 4.0,
        neckLength = 4.0, neckCircumference = 18.0, neckWeight = 0.3,
        chestDepth = 12.0, chestWidth = 16.0, chestCircumference = 48.0,
        chestWeight = 1.2, chestDeflectionMax = 40.0,
        abdominalDepth = 10.5, abdominalWidth = 15.0, abdominalCircumference = 45.0,
        abdominalWeight = 0.8, abdominalPressureMax = 1.2,
        shoulderWidth = 20.0, shoulderCircumference = 40.0, acromionHeight = 9.0,
        hipWidth = 18.0, hipCircumference = 50.0, hipWeight = 1.0,
        upperArmLength = 10.0, upperArmCircumference = 14.0,
        forearmLength = 9.0, forearmCircumference = 12.0,
        handLength = 6.5, handWidth = 5.0, armWeight = 0.7,
        thighLength = 13.0, thighCircumference = 17.0,
        lowerLegLength = 12.0, lowerLegCircumference = 14.0,
        footLength = 9.0, footWidth = 6.0, legWeight = 1.1,
        hpcLimit = 600.0, headAcceleration3msLimit = 75.0,
        chestAcceleration3msLimit = 55.0, neckForceLimit = 1000.0,
        neckMomentLimit = 20.0, chestDeflectionLimit = 35.0
    )

    // Q1.5数据（18个月）
    private fun createQ1_5Data() = DummyAnthropometry(
        dummyType = DummyType.Q1_5,
        totalWeight = 11.0, totalHeight = 81.0, sittingHeight = 52.0,
        shoulderHeight = 44.0, hipHeight = 27.0, kneeHeight = 38.0,
        headLength = 16.0, headBreadth = 12.5, headCircumference = 46.0,
        headWeight = 1.5, headCenterOfGravity = 4.5,
        neckLength = 4.5, neckCircumference = 20.0, neckWeight = 0.4,
        chestDepth = 13.5, chestWidth = 18.0, chestCircumference = 52.0,
        chestWeight = 1.8, chestDeflectionMax = 45.0,
        abdominalDepth = 12.0, abdominalWidth = 17.0, abdominalCircumference = 50.0,
        abdominalWeight = 1.2, abdominalPressureMax = 1.2,
        shoulderWidth = 22.0, shoulderCircumference = 45.0, acromionHeight = 10.0,
        hipWidth = 20.0, hipCircumference = 55.0, hipWeight = 1.5,
        upperArmLength = 12.0, upperArmCircumference = 16.0,
        forearmLength = 10.5, forearmCircumference = 14.0,
        handLength = 7.5, handWidth = 5.5, armWeight = 1.0,
        thighLength = 15.0, thighCircumference = 19.0,
        lowerLegLength = 14.0, lowerLegCircumference = 16.0,
        footLength = 10.5, footWidth = 7.0, legWeight = 1.6,
        hpcLimit = 600.0, headAcceleration3msLimit = 75.0,
        chestAcceleration3msLimit = 55.0, neckForceLimit = 1300.0,
        neckMomentLimit = 25.0, chestDeflectionLimit = 40.0
    )

    // Q3数据（3岁）
    private fun createQ3Data() = DummyAnthropometry(
        dummyType = DummyType.Q3,
        totalWeight = 15.5, totalHeight = 96.0, sittingHeight = 60.0,
        shoulderHeight = 52.0, hipHeight = 32.0, kneeHeight = 45.0,
        headLength = 17.5, headBreadth = 13.5, headCircumference = 50.0,
        headWeight = 2.0, headCenterOfGravity = 5.0,
        neckLength = 5.5, neckCircumference = 22.0, neckWeight = 0.5,
        chestDepth = 15.0, chestWidth = 20.0, chestCircumference = 56.0,
        chestWeight = 2.5, chestDeflectionMax = 50.0,
        abdominalDepth = 13.5, abdominalWidth = 19.0, abdominalCircumference = 55.0,
        abdominalWeight = 1.8, abdominalPressureMax = 1.0,
        shoulderWidth = 24.0, shoulderCircumference = 50.0, acromionHeight = 11.0,
        hipWidth = 22.0, hipCircumference = 60.0, hipWeight = 2.0,
        upperArmLength = 14.0, upperArmCircumference = 18.0,
        forearmLength = 12.0, forearmCircumference = 15.0,
        handLength = 8.5, handWidth = 6.0, armWeight = 1.4,
        thighLength = 18.0, thighCircumference = 22.0,
        lowerLegLength = 16.0, lowerLegCircumference = 18.0,
        footLength = 12.0, footWidth = 8.0, legWeight = 2.2,
        hpcLimit = 800.0, headAcceleration3msLimit = 80.0,
        chestAcceleration3msLimit = 55.0, neckForceLimit = 1800.0,
        neckMomentLimit = 30.0, chestDeflectionLimit = 45.0
    )

    // Q3s数据（3岁-美标侧面碰撞专用）
    // Q3s与Q3基本类似，但损伤评估限值符合FMVSS 213a标准
    private fun createQ3sData() = DummyAnthropometry(
        dummyType = DummyType.Q3S,
        totalWeight = 15.5, totalHeight = 96.0, sittingHeight = 60.0,
        shoulderHeight = 52.0, hipHeight = 32.0, kneeHeight = 45.0,
        headLength = 17.5, headBreadth = 13.5, headCircumference = 50.0,
        headWeight = 2.0, headCenterOfGravity = 5.0,
        neckLength = 5.5, neckCircumference = 22.0, neckWeight = 0.5,
        chestDepth = 15.0, chestWidth = 20.0, chestCircumference = 56.0,
        chestWeight = 2.5, chestDeflectionMax = 50.0,
        abdominalDepth = 13.5, abdominalWidth = 19.0, abdominalCircumference = 55.0,
        abdominalWeight = 1.8, abdominalPressureMax = 1.0,
        shoulderWidth = 24.0, shoulderCircumference = 50.0, acromionHeight = 11.0,
        hipWidth = 22.0, hipCircumference = 60.0, hipWeight = 2.0,
        upperArmLength = 14.0, upperArmCircumference = 18.0,
        forearmLength = 12.0, forearmCircumference = 15.0,
        handLength = 8.5, handWidth = 6.0, armWeight = 1.4,
        thighLength = 18.0, thighCircumference = 22.0,
        lowerLegLength = 16.0, lowerLegCircumference = 18.0,
        footLength = 12.0, footWidth = 8.0, legWeight = 2.2,
        // FMVSS 213a侧面碰撞专用限值
        hpcLimit = 800.0,
        headAcceleration3msLimit = 80.0,
        chestAcceleration3msLimit = 55.0,
        neckForceLimit = 1800.0,
        neckMomentLimit = 30.0,
        chestDeflectionLimit = 45.0
    )

    // Q6数据（6岁）
    private fun createQ6Data() = DummyAnthropometry(
        dummyType = DummyType.Q6,
        totalWeight = 24.0, totalHeight = 115.0, sittingHeight = 70.0,
        shoulderHeight = 62.0, hipHeight = 38.0, kneeHeight = 55.0,
        headLength = 19.0, headBreadth = 14.5, headCircumference = 53.0,
        headWeight = 2.5, headCenterOfGravity = 5.5,
        neckLength = 6.5, neckCircumference = 25.0, neckWeight = 0.6,
        chestDepth = 17.0, chestWidth = 23.0, chestCircumference = 62.0,
        chestWeight = 4.0, chestDeflectionMax = 55.0,
        abdominalDepth = 15.5, abdominalWidth = 21.0, abdominalCircumference = 62.0,
        abdominalWeight = 3.0, abdominalPressureMax = 1.0,
        shoulderWidth = 28.0, shoulderCircumference = 58.0, acromionHeight = 12.0,
        hipWidth = 26.0, hipCircumference = 68.0, hipWeight = 3.0,
        upperArmLength = 17.0, upperArmCircumference = 21.0,
        forearmLength = 15.0, forearmCircumference = 17.0,
        handLength = 10.5, handWidth = 7.0, armWeight = 2.0,
        thighLength = 22.0, thighCircumference = 26.0,
        lowerLegLength = 20.0, lowerLegCircumference = 21.0,
        footLength = 15.0, footWidth = 9.0, legWeight = 3.2,
        hpcLimit = 800.0, headAcceleration3msLimit = 80.0,
        chestAcceleration3msLimit = 55.0, neckForceLimit = 2500.0,
        neckMomentLimit = 40.0, chestDeflectionLimit = 52.0
    )

    // Q10数据（10岁）
    private fun createQ10Data() = DummyAnthropometry(
        dummyType = DummyType.Q10,
        totalWeight = 38.0, totalHeight = 137.5, sittingHeight = 82.0,
        shoulderHeight = 72.0, hipHeight = 45.0, kneeHeight = 65.0,
        headLength = 21.0, headBreadth = 15.5, headCircumference = 55.0,
        headWeight = 3.0, headCenterOfGravity = 6.0,
        neckLength = 7.5, neckCircumference = 28.0, neckWeight = 0.8,
        chestDepth = 19.5, chestWidth = 26.0, chestCircumference = 70.0,
        chestWeight = 6.0, chestDeflectionMax = 65.0,
        abdominalDepth = 18.0, abdominalWidth = 24.0, abdominalCircumference = 70.0,
        abdominalWeight = 5.0, abdominalPressureMax = 1.2,
        shoulderWidth = 32.0, shoulderCircumference = 65.0, acromionHeight = 13.0,
        hipWidth = 30.0, hipCircumference = 78.0, hipWeight = 5.0,
        upperArmLength = 20.0, upperArmCircumference = 24.0,
        forearmLength = 18.0, forearmCircumference = 20.0,
        handLength = 12.5, handWidth = 8.0, armWeight = 2.8,
        thighLength = 26.0, thighCircumference = 30.0,
        lowerLegLength = 24.0, lowerLegCircumference = 24.0,
        footLength = 18.0, footWidth = 10.0, legWeight = 4.5,
        hpcLimit = 800.0, headAcceleration3msLimit = 80.0,
        chestAcceleration3msLimit = 55.0, neckForceLimit = 3500.0,
        neckMomentLimit = 50.0, chestDeflectionLimit = 60.0
    )

    // CRABI 6个月数据
    private fun createCRABI6Data() = createQ1Data().copy(
        dummyType = DummyType.CRABI_6,
        totalWeight = 7.7, totalHeight = 65.0
    )

    // CRABI 12个月数据
    private fun createCRABI12Data() = createQ1_5Data().copy(
        dummyType = DummyType.CRABI_12,
        totalWeight = 10.0, totalHeight = 74.0
    )

    // CRABI 18个月数据
    private fun createCRABI18Data() = createQ1_5Data().copy(
        dummyType = DummyType.CRABI_18,
        totalWeight = 11.2, totalHeight = 82.0
    )

    // Hybrid III 3岁数据
    private fun createHybridIII3YData() = createQ3Data().copy(
        dummyType = DummyType.HYBRID_III_3Y,
        totalWeight = 15.1, totalHeight = 90.0
    )

    // Hybrid III 6岁数据
    private fun createHybridIII6YData() = createQ6Data().copy(
        dummyType = DummyType.HYBRID_III_6Y,
        totalWeight = 21.5, totalHeight = 116.0
    )

    // Part 575.210 6个月数据
    private fun createPart575210Data() = createQ1Data().copy(
        dummyType = DummyType.PART_575_210,
        totalWeight = 8.0, totalHeight = 67.0
    )

    // SID IIs 3岁数据
    private fun createSIDIIs3YData() = createQ3Data().copy(
        dummyType = DummyType.SID_IIs_3Y,
        totalWeight = 15.0, totalHeight = 88.0
    )

    // SID IIs 6岁数据
    private fun createSIDIIs6YData() = createQ6Data().copy(
        dummyType = DummyType.SID_IIs_6Y,
        totalWeight = 22.0, totalHeight = 117.0
    )
}

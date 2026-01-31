package com.childproduct.designassistant.service

import com.childproduct.designassistant.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * GPS Anthro Tool - 儿童安全座椅设计与适配性计算工具
 * 
 * 核心功能：
 * 1. 安全带长度计算（harness length）
 * 2. 颧间距离转换（Biacromal Conversion）
 * 3. 座椅适配性评估
 * 4. 身高-体重匹配分析
 * 5. 多标准对比（US/EU/CN）
 * 6. 损伤评估准则计算
 */
class GPSAnthroTool {

    companion object {
        // 常量定义
        private const val SAFETY_MARGIN_CM = 2.0          // 安全余量（cm）
        private const val HARNESS_SLOT_SPACING_CM = 3.5   // 安全带卡槽间距（cm）
        private const val MIN_HARNESS_LENGTH_CM = 15.0    // 最小安全带长度（cm）
        private const val MAX_HARNESS_LENGTH_CM = 70.0    // 最大安全带长度（cm）
    }

    /**
     * GPS工具计算结果
     */
    data class GPSCalculationResult(
        val childData: ChildAnthropometricData,
        val dummyData: DummyAnthropometry?,
        val seatType: SeatType,
        val calculationType: CalculationType,
        val results: Map<String, Any>,
        val recommendations: List<String>,
        val complianceStatus: ComplianceStatus,
        val calculationTime: Long
    )

    /**
     * 座椅类型
     */
    enum class SeatType(val displayName: String) {
        REARWARD_INFANT_CARRIER("后向婴儿提篮"),
        CONVERTIBLE_FF_RF("双向可转换座椅"),
        FORWARD_BOOSTER("前向增高座椅"),
        COMBINATION("组合式座椅"),
        BACKLESS_BOOSTER("无背增高垫"),
        ALL_IN_ONE("一体化座椅")
    }

    /**
     * 计算类型
     */
    enum class CalculationType(val displayName: String) {
        HARNESS_LENGTH("安全带长度计算"),
        BIACROMAL_CONVERSION("颧间距离转换"),
        SEAT_FIT("座椅适配性评估"),
        HEIGHT_WEIGHT_MATCH("身高-体重匹配"),
        INJURY_CRITERIA("损伤评估准则")
    }

    /**
     * 合规状态
     */
    enum class ComplianceStatus(val displayName: String, val score: Double) {
        COMPLIANT("完全合规", 100.0),
        PARTIALLY_COMPLIANT("部分合规", 75.0),
        NON_COMPLIANT("不合规", 0.0),
        NOT_APPLICABLE("不适用", 0.0)
    }

    /**
     * 计算安全带长度（harness length）
     * 
     * 参数：
     * - childData: 儿童人体测量数据
     * - seatType: 座椅类型
     * - adjustmentAllowance: 调节余量（cm）
     * 
     * 返回：安全带长度计算结果
     */
    suspend fun calculateHarnessLength(
        childData: ChildAnthropometricData,
        seatType: SeatType,
        adjustmentAllowance: Double = 5.0
    ): GPSCalculationResult = withContext(Dispatchers.Default) {
        val startTime = System.currentTimeMillis()

        // 计算关键参数
        val shoulderHeight = childData.shoulderHeight
        val chestDepth = childData.chestDepth
        val abdominalDepth = childData.abdominalDepth
        val backRestAngle = when (seatType) {
            SeatType.REARWARD_INFANT_CARRIER, SeatType.CONVERTIBLE_FF_RF -> 45.0  // 后向45度
            SeatType.FORWARD_BOOSTER, SeatType.COMBINATION -> 85.0  // 前向85度
            SeatType.BACKLESS_BOOSTER -> 90.0
            SeatType.ALL_IN_ONE -> 45.0
        }

        // 计算安全带长度
        val effectiveShoulderHeight = shoulderHeight * kotlin.math.sin(Math.toRadians(backRestAngle.toDouble()))
        val requiredLength = effectiveShoulderHeight + chestDepth + abdominalDepth + SAFETY_MARGIN_CM + adjustmentAllowance

        // 计算合适的卡槽数量
        val slotCount = ((requiredLength - MIN_HARNESS_LENGTH_CM) / HARNESS_SLOT_SPACING_CM).toInt() + 1

        // 验证是否在合理范围内
        val isWithinRange = requiredLength in MIN_HARNESS_LENGTH_CM..MAX_HARNESS_LENGTH_CM

        // 生成建议
        val recommendations = mutableListOf<String>()
        if (isWithinRange) {
            recommendations.add("安全带长度计算结果：${String.format("%.1f", requiredLength)} cm")
            recommendations.add("建议使用卡槽位置：第${slotCount}槽")
            recommendations.add("可调节余量：${String.format("%.1f", adjustmentAllowance)} cm")
        } else {
            if (requiredLength < MIN_HARNESS_LENGTH_CM) {
                recommendations.add("警告：计算长度低于最小要求（${MIN_HARNESS_LENGTH_CM}cm）")
                recommendations.add("建议：检查座椅类型选择，可能需要更小的座椅")
            }
            if (requiredLength > MAX_HARNESS_LENGTH_CM) {
                recommendations.add("警告：计算长度超过最大限制（${MAX_HARNESS_LENGTH_CM}cm）")
                recommendations.add("建议：考虑使用更大的座椅或增高座椅")
            }
        }

        // 合规性评估
        val complianceStatus = if (isWithinRange) ComplianceStatus.COMPLIANT else ComplianceStatus.NON_COMPLIANT

        val results = mapOf(
            "requiredLength" to String.format("%.2f", requiredLength),
            "slotCount" to slotCount,
            "effectiveShoulderHeight" to String.format("%.2f", effectiveShoulderHeight),
            "backRestAngle" to backRestAngle,
            "isWithinRange" to isWithinRange
        )

        GPSCalculationResult(
            childData = childData,
            dummyData = null,
            seatType = seatType,
            calculationType = CalculationType.HARNESS_LENGTH,
            results = results,
            recommendations = recommendations,
            complianceStatus = complianceStatus,
            calculationTime = System.currentTimeMillis() - startTime
        )
    }

    /**
     * 颧间距离转换（Biacromal Conversion）
     * 
     * 用于计算座椅肩部宽度的适配性
     * 
     * 参数：
     * - childData: 儿童人体测量数据
     * - dummyData: Dummy数据（可选）
     * 
     * 返回：颧间距离转换结果
     */
    suspend fun calculateBiacromalConversion(
        childData: ChildAnthropometricData,
        dummyData: DummyAnthropometry? = null
    ): GPSCalculationResult = withContext(Dispatchers.Default) {
        val startTime = System.currentTimeMillis()

        // 获取肩宽数据
        val shoulderWidth = childData.shoulderWidth
        val acromionHeight = dummyData?.acromionHeight ?: childData.shoulderWidth * 0.4

        // 计算颧间距离（简化公式）
        val biacromalDistance = shoulderWidth * 0.95  // 颧间距离约为肩宽的95%
        val effectiveBiacromalDistance = biacromalDistance * 0.9  // 有效距离

        // 座椅肩部宽度要求（标准值）
        val minSeatShoulderWidth = effectiveBiacromalDistance + 3.0  // 最小宽度
        val recommendedSeatShoulderWidth = effectiveBiacromalDistance + 5.0  // 推荐宽度

        // 舒适度评估
        val comfortRating = when {
            shoulderWidth < effectiveBiacromalDistance -> "过紧"
            shoulderWidth < recommendedSeatShoulderWidth -> "适中"
            else -> "宽松"
        }

        // 生成建议
        val recommendations = mutableListOf<String>()
        recommendations.add("儿童肩宽：${String.format("%.1f", shoulderWidth)} cm")
        recommendations.add("颧间距离：${String.format("%.1f", biacromalDistance)} cm")
        recommendations.add("有效颧间距离：${String.format("%.1f", effectiveBiacromalDistance)} cm")
        recommendations.add("最小座椅肩宽要求：${String.format("%.1f", minSeatShoulderWidth)} cm")
        recommendations.add("推荐座椅肩宽：${String.format("%.1f", recommendedSeatShoulderWidth)} cm")
        recommendations.add("舒适度评估：$comfortRating")

        // 合规性评估
        val complianceStatus = when (comfortRating) {
            "过紧" -> ComplianceStatus.PARTIALLY_COMPLIANT
            "适中" -> ComplianceStatus.COMPLIANT
            "宽松" -> ComplianceStatus.COMPLIANT
            else -> ComplianceStatus.NON_COMPLIANT
        }

        val results = mapOf(
            "biacromalDistance" to String.format("%.2f", biacromalDistance),
            "effectiveBiacromalDistance" to String.format("%.2f", effectiveBiacromalDistance),
            "minSeatShoulderWidth" to String.format("%.2f", minSeatShoulderWidth),
            "recommendedSeatShoulderWidth" to String.format("%.2f", recommendedSeatShoulderWidth),
            "comfortRating" to comfortRating
        )

        GPSCalculationResult(
            childData = childData,
            dummyData = dummyData,
            seatType = SeatType.ALL_IN_ONE,
            calculationType = CalculationType.BIACROMAL_CONVERSION,
            results = results,
            recommendations = recommendations,
            complianceStatus = complianceStatus,
            calculationTime = System.currentTimeMillis() - startTime
        )
    }

    /**
     * 座椅适配性评估
     * 
     * 评估儿童与安全座椅的适配性
     * 
     * 参数：
     * - childData: 儿童人体测量数据
     * - seatType: 座椅类型
     * - seatSpecs: 座椅规格（可选）
     * 
     * 返回：座椅适配性评估结果
     */
    suspend fun evaluateSeatFit(
        childData: ChildAnthropometricData,
        seatType: SeatType,
        seatSpecs: Map<String, Double>? = null
    ): GPSCalculationResult = withContext(Dispatchers.Default) {
        val startTime = System.currentTimeMillis()

        val recommendations = mutableListOf<String>()
        val results = mutableMapOf<String, Any>()

        // 基于身高范围的初步评估
        val height = childData.stature
        val weight = childData.weight

        // 根据座椅类型进行适配性评估
        val isHeightCompatible = when (seatType) {
            SeatType.REARWARD_INFANT_CARRIER -> height <= 83.0
            SeatType.CONVERTIBLE_FF_RF -> height in 40.0..105.0
            SeatType.FORWARD_BOOSTER -> height >= 100.0
            SeatType.COMBINATION -> height in 76.0..135.0
            SeatType.BACKLESS_BOOSTER -> height >= 125.0
            SeatType.ALL_IN_ONE -> height in 40.0..135.0
        }

        val isWeightCompatible = when (seatType) {
            SeatType.REARWARD_INFANT_CARRIER -> weight <= 13.0
            SeatType.CONVERTIBLE_FF_RF -> weight <= 18.0
            SeatType.FORWARD_BOOSTER -> weight >= 15.0
            SeatType.COMBINATION -> weight in 15.0..36.0
            SeatType.BACKLESS_BOOSTER -> weight >= 22.0
            SeatType.ALL_IN_ONE -> weight <= 36.0
        }

        // 生成评估结果
        recommendations.add("身高：${String.format("%.1f", height)} cm")
        recommendations.add("体重：${String.format("%.1f", weight)} kg")
        recommendations.add("座椅类型：${seatType.displayName}")
        
        if (isHeightCompatible) {
            recommendations.add("✓ 身高适配")
        } else {
            recommendations.add("✗ 身高不兼容")
        }
        
        if (isWeightCompatible) {
            recommendations.add("✓ 体重适配")
        } else {
            recommendations.add("✗ 体重不兼容")
        }

        // 特殊检查
        when (seatType) {
            SeatType.REARWARD_INFANT_CARRIER -> {
                if (height > 75.0) {
                    recommendations.add("提示：接近后向座椅上限，建议考虑可转换座椅")
                }
            }
            SeatType.FORWARD_BOOSTER -> {
                if (height < 100.0) {
                    recommendations.add("警告：身高低于增高座椅最低要求（100cm）")
                }
            }
            SeatType.BACKLESS_BOOSTER -> {
                if (childData.headCircumference > 55.0) {
                    recommendations.add("提示：头围较大，建议使用带靠背的增高座椅")
                }
            }
            else -> {}
        }

        results["isHeightCompatible"] = isHeightCompatible
        results["isWeightCompatible"] = isWeightCompatible
        results["overallCompatible"] = isHeightCompatible && isWeightCompatible

        // 合规性评估
        val complianceStatus = when {
            isHeightCompatible && isWeightCompatible -> ComplianceStatus.COMPLIANT
            isHeightCompatible xor isWeightCompatible -> ComplianceStatus.PARTIALLY_COMPLIANT
            else -> ComplianceStatus.NON_COMPLIANT
        }

        GPSCalculationResult(
            childData = childData,
            dummyData = null,
            seatType = seatType,
            calculationType = CalculationType.SEAT_FIT,
            results = results,
            recommendations = recommendations,
            complianceStatus = complianceStatus,
            calculationTime = System.currentTimeMillis() - startTime
        )
    }

    /**
     * 身高-体重匹配分析
     * 
     * 分析儿童的身高-体重比例是否健康
     * 
     * 参数：
     * - childData: 儿童人体测量数据
     * 
     * 返回：身高-体重匹配分析结果
     */
    suspend fun analyzeHeightWeightMatch(
        childData: ChildAnthropometricData
    ): GPSCalculationResult = withContext(Dispatchers.Default) {
        val startTime = System.currentTimeMillis()

        val recommendations = mutableListOf<String>()
        val results = mutableMapOf<String, Any>()

        // 计算BMI
        val bmi = childData.calculateBMI()
        
        // 根据年龄评估BMI范围（WHO标准）
        val ageMonths = childData.ageMonths
        val (minBMI, maxBMI) = when {
            ageMonths < 12 -> Pair(12.0, 18.0)
            ageMonths < 36 -> Pair(13.5, 18.5)
            ageMonths < 60 -> Pair(13.5, 19.0)
            ageMonths < 84 -> Pair(13.5, 20.0)
            ageMonths < 120 -> Pair(14.0, 22.0)
            else -> Pair(16.0, 25.0)
        }

        // BMI评估
        val bmiStatus = when {
            bmi < minBMI -> "偏瘦"
            bmi in minBMI..maxBMI -> "正常"
            bmi <= maxBMI + 3.0 -> "偏胖"
            else -> "肥胖"
        }

        // 生成建议
        recommendations.add("BMI：${String.format("%.1f", bmi)}")
        recommendations.add("年龄：${childData.getAgeGroupLabel()}")
        recommendations.add("BMI正常范围：${String.format("%.1f", minBMI)} - ${String.format("%.1f", maxBMI)}")
        recommendations.add("评估结果：$bmiStatus")

        // 座椅选择建议
        when (bmiStatus) {
            "偏瘦" -> {
                recommendations.add("提示：BMI偏低，建议选择可调节性更好的座椅")
            }
            "偏胖", "肥胖" -> {
                recommendations.add("提示：BMI偏高，建议选择宽度更大的座椅")
                recommendations.add("提示：注意安全带长度是否充足")
            }
            else -> {}
        }

        results["bmi"] to String.format("%.2f", bmi)
        results["minBMI"] = minBMI
        results["maxBMI"] = maxBMI
        results["bmiStatus"] = bmiStatus

        // 合规性评估
        val complianceStatus = when (bmiStatus) {
            "正常" -> ComplianceStatus.COMPLIANT
            "偏瘦", "偏胖" -> ComplianceStatus.PARTIALLY_COMPLIANT
            "肥胖" -> ComplianceStatus.NON_COMPLIANT
            else -> ComplianceStatus.NOT_APPLICABLE
        }

        GPSCalculationResult(
            childData = childData,
            dummyData = null,
            seatType = SeatType.ALL_IN_ONE,
            calculationType = CalculationType.HEIGHT_WEIGHT_MATCH,
            results = results,
            recommendations = recommendations,
            complianceStatus = complianceStatus,
            calculationTime = System.currentTimeMillis() - startTime
        )
    }

    /**
     * 损伤评估准则计算
     * 
     * 基于Dummy数据计算损伤评估准则
     * 
     * 参数：
     * - childData: 儿童人体测量数据
     * - dummyData: Dummy数据
     * 
     * 返回：损伤评估准则计算结果
     */
    suspend fun calculateInjuryCriteria(
        childData: ChildAnthropometricData,
        dummyData: DummyAnthropometry
    ): GPSCalculationResult = withContext(Dispatchers.Default) {
        val startTime = System.currentTimeMillis()

        val recommendations = mutableListOf<String>()
        val results = mutableMapOf<String, Any>()

        // 获取损伤评估限值
        val injuryCriteria = dummyData.getInjuryCriteria()

        // 生成建议
        recommendations.add("Dummy类型：${dummyData.dummyType.displayName}")
        recommendations.add("适用标准：${dummyData.dummyType.standard}")
        recommendations.add("")
        recommendations.add("损伤评估限值：")
        injuryCriteria.forEach { (key, value) ->
            recommendations.add("  - $key: $value")
        }

        results["injuryCriteria"] = injuryCriteria
        results["dummyType"] = dummyData.dummyType.displayName
        results["standard"] = dummyData.dummyType.standard

        // 合规性评估
        val complianceStatus = ComplianceStatus.NOT_APPLICABLE  // 仅为限值展示，无需评估

        GPSCalculationResult(
            childData = childData,
            dummyData = dummyData,
            seatType = SeatType.ALL_IN_ONE,
            calculationType = CalculationType.INJURY_CRITERIA,
            results = results,
            recommendations = recommendations,
            complianceStatus = complianceStatus,
            calculationTime = System.currentTimeMillis() - startTime
        )
    }

    /**
     * 综合计算（Easy Buttons）
     * 
     * 一次性执行多个计算，提供快捷结果
     * 
     * 参数：
     * - childData: 儿童人体测量数据
     * - seatType: 座椅类型
     * - dummyData: Dummy数据（可选）
     * 
     * 返回：综合计算结果列表
     */
    suspend fun comprehensiveCalculation(
        childData: ChildAnthropometricData,
        seatType: SeatType,
        dummyData: DummyAnthropometry? = null
    ): List<GPSCalculationResult> = withContext(Dispatchers.Default) {
        val results = mutableListOf<GPSCalculationResult>()

        // 执行所有计算
        results.add(calculateHarnessLength(childData, seatType))
        results.add(calculateBiacromalConversion(childData, dummyData))
        results.add(evaluateSeatFit(childData, seatType))
        results.add(analyzeHeightWeightMatch(childData))
        
        if (dummyData != null) {
            results.add(calculateInjuryCriteria(childData, dummyData))
        }

        results
    }

    /**
     * 多地区对比分析
     * 
     * 对比美国、欧盟、中国儿童数据的差异
     * 
     * 参数：
     * - ageMonths: 年龄（月）
     * - percentile: 百分位
     * 
     * 返回：地区对比数据
     */
    suspend fun compareRegions(
        ageMonths: Int,
        percentile: Percentile = Percentile.MEAN
    ): RegionComparisonData = withContext(Dispatchers.Default) {
        // 这里应该从数据库获取真实数据
        // 暂时使用模拟数据
        
        val usData = mockChildData(Region.UNITED_STATES, ageMonths, percentile)
        val euData = mockChildData(Region.EUROPEAN_UNION, ageMonths, percentile)
        val cnData = mockChildData(Region.CHINA, ageMonths, percentile)

        RegionComparisonData(
            ageMonths = ageMonths,
            usData = usData,
            euData = euData,
            cnData = cnData,
            percentile = percentile
        )
    }

    /**
     * 模拟儿童数据（仅用于演示）
     */
    private fun mockChildData(
        region: Region,
        ageMonths: Int,
        percentile: Percentile
    ): ChildAnthropometricData {
        // 基于年龄和地区生成模拟数据
        val baseHeight = when {
            ageMonths < 12 -> 50.0 + ageMonths * 2.5
            ageMonths < 36 -> 80.0 + (ageMonths - 12) * 0.7
            ageMonths < 84 -> 90.0 + (ageMonths - 36) * 0.6
            else -> 110.0 + (ageMonths - 84) * 0.5
        }

        val baseWeight = when {
            ageMonths < 12 -> 3.5 + ageMonths * 0.6
            ageMonths < 36 -> 10.0 + (ageMonths - 12) * 0.3
            ageMonths < 84 -> 15.0 + (ageMonths - 36) * 0.25
            else -> 25.0 + (ageMonths - 84) * 0.2
        }

        // 地区差异调整
        val heightAdjustment = when (region) {
            Region.UNITED_STATES -> 1.02  // 美国儿童偏高
            Region.EUROPEAN_UNION -> 1.0
            Region.CHINA -> 0.98
        }

        val weightAdjustment = when (region) {
            Region.UNITED_STATES -> 1.05  // 美国儿童偏重
            Region.EUROPEAN_UNION -> 1.0
            Region.CHINA -> 0.95
        }

        // 百分位调整
        val percentileAdjustment = when (percentile) {
            Percentile.FIFTH -> 0.9
            Percentile.MEAN -> 1.0
            Percentile.NINETY_FIFTH -> 1.1
        }

        val height = baseHeight * heightAdjustment * percentileAdjustment
        val weight = baseWeight * weightAdjustment * percentileAdjustment

        return ChildAnthropometricData(
            region = region,
            ageMonths = ageMonths,
            ageYears = ageMonths / 12.0,
            gender = Gender.ALL,
            stature = height,
            weight = weight,
            sittingHeight = height * 0.55,
            shoulderHeight = height * 0.65,
            elbowHeight = height * 0.45,
            hipHeight = height * 0.35,
            shoulderWidth = height * 0.22,
            hipWidth = height * 0.18,
            chestDepth = height * 0.15,
            chestWidth = height * 0.20,
            abdominalDepth = height * 0.12,
            headLength = height * 0.16,
            headBreadth = height * 0.13,
            headCircumference = height * 0.40,
            neckCircumference = height * 0.25,
            upperArmLength = height * 0.18,
            forearmLength = height * 0.16,
            handLength = height * 0.11,
            thighLength = height * 0.22,
            lowerLegLength = height * 0.20,
            footLength = height * 0.14,
            percentile = percentile,
            percentileValue = percentile.value,
            dataSource = "CDC/WHO/中国标准",
            sampleSize = 1000,
            measurementYear = 2020
        )
    }
}

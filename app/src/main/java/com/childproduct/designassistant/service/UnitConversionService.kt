package com.childproduct.designassistant.service

import com.childproduct.designassistant.data.*
import kotlin.math.pow
import kotlin.math.round

/**
 * 单位转换服务
 * 
 * 支持双向单位转换：
 * - 长度：厘米 ↔ 英寸（cm ↔ in）
 * - 重量：千克 ↔ 磅（kg ↔ lb）
 * - 温度：摄氏度 ↔ 华氏度（°C ↔ °F）
 * 
 * 确保转换精度 < 0.1%
 */
class UnitConversionService {

    companion object {
        // 转换常量
        private const val CM_TO_INCH = 0.3937007874
        private const val INCH_TO_CM = 2.54
        private const val KG_TO_LB = 2.20462262185
        private const val LB_TO_KG = 0.45359237
    }

    /**
     * 转换结果
     */
    data class ConversionResult(
        val originalValue: Double,
        val originalUnit: String,
        val convertedValue: Double,
        val convertedUnit: String,
        val conversionRate: Double,
        val precision: Double
    )

    /**
     * 长度转换：厘米 → 英寸
     * 
     * @param cm 厘米值
     * @return 英寸值（保留2位小数）
     */
    fun cmToInch(cm: Double): ConversionResult {
        val inch = cm * CM_TO_INCH
        return ConversionResult(
            originalValue = cm,
            originalUnit = "cm",
            convertedValue = roundToDecimal(inch, 2),
            convertedUnit = "in",
            conversionRate = CM_TO_INCH,
            precision = 0.01
        )
    }

    /**
     * 长度转换：英寸 → 厘米
     * 
     * @param inch 英寸值
     * @return 厘米值（保留2位小数）
     */
    fun inchToCm(inch: Double): ConversionResult {
        val cm = inch * INCH_TO_CM
        return ConversionResult(
            originalValue = inch,
            originalUnit = "in",
            convertedValue = roundToDecimal(cm, 2),
            convertedUnit = "cm",
            conversionRate = INCH_TO_CM,
            precision = 0.01
        )
    }

    /**
     * 重量转换：千克 → 磅
     * 
     * @param kg 千克值
     * @return 磅值（保留1位小数）
     */
    fun kgToLb(kg: Double): ConversionResult {
        val lb = kg * KG_TO_LB
        return ConversionResult(
            originalValue = kg,
            originalUnit = "kg",
            convertedValue = roundToDecimal(lb, 1),
            convertedUnit = "lb",
            conversionRate = KG_TO_LB,
            precision = 0.1
        )
    }

    /**
     * 重量转换：磅 → 千克
     * 
     * @param lb 磅值
     * @return 千克值（保留2位小数）
     */
    fun lbToKg(lb: Double): ConversionResult {
        val kg = lb * LB_TO_KG
        return ConversionResult(
            originalValue = lb,
            originalUnit = "lb",
            convertedValue = roundToDecimal(kg, 2),
            convertedUnit = "kg",
            conversionRate = LB_TO_KG,
            precision = 0.01
        )
    }

    /**
     * 温度转换：摄氏度 → 华氏度
     * 
     * @param celsius 摄氏度值
     * @return 华氏度值（保留1位小数）
     */
    fun celsiusToFahrenheit(celsius: Double): ConversionResult {
        val fahrenheit = (celsius * 9.0 / 5.0) + 32.0
        return ConversionResult(
            originalValue = celsius,
            originalUnit = "°C",
            convertedValue = roundToDecimal(fahrenheit, 1),
            convertedUnit = "°F",
            conversionRate = 9.0 / 5.0,
            precision = 0.1
        )
    }

    /**
     * 温度转换：华氏度 → 摄氏度
     * 
     * @param fahrenheit 华氏度值
     * @return 摄氏度值（保留1位小数）
     */
    fun fahrenheitToCelsius(fahrenheit: Double): ConversionResult {
        val celsius = (fahrenheit - 32.0) * 5.0 / 9.0
        return ConversionResult(
            originalValue = fahrenheit,
            originalUnit = "°F",
            convertedValue = roundToDecimal(celsius, 1),
            convertedUnit = "°C",
            conversionRate = 5.0 / 9.0,
            precision = 0.1
        )
    }

    /**
     * 转换儿童人体测量数据的单位系统
     * 
     * @param data 原始数据（公制）
     * @param targetUnitSystem 目标单位系统
     * @return 转换后的数据
     */
    fun convertChildData(
        data: ChildAnthropometricData,
        targetUnitSystem: UnitSystem
    ): ChildAnthropometricData {
        return when (targetUnitSystem) {
            UnitSystem.METRIC -> data  // 已经是公制，无需转换
            UnitSystem.IMPERIAL -> data.toImperial()  // 转换为英制
        }
    }

    /**
     * 转换Dummy数据的单位系统
     * 
     * @param data 原始数据（公制）
     * @param targetUnitSystem 目标单位系统
     * @return 转换后的数据
     */
    fun convertDummyData(
        data: DummyAnthropometry,
        targetUnitSystem: UnitSystem
    ): DummyAnthropometry {
        if (targetUnitSystem == UnitSystem.METRIC) {
            return data  // 已经是公制，无需转换
        }

        // 转换为英制
        return data.copy(
            totalHeight = data.totalHeight * CM_TO_INCH,
            sittingHeight = data.sittingHeight * CM_TO_INCH,
            shoulderHeight = data.shoulderHeight * CM_TO_INCH,
            hipHeight = data.hipHeight * CM_TO_INCH,
            kneeHeight = data.kneeHeight * CM_TO_INCH,
            headLength = data.headLength * CM_TO_INCH,
            headBreadth = data.headBreadth * CM_TO_INCH,
            headCircumference = data.headCircumference * CM_TO_INCH,
            neckLength = data.neckLength * CM_TO_INCH,
            neckCircumference = data.neckCircumference * CM_TO_INCH,
            chestDepth = data.chestDepth * CM_TO_INCH,
            chestWidth = data.chestWidth * CM_TO_INCH,
            chestCircumference = data.chestCircumference * CM_TO_INCH,
            abdominalDepth = data.abdominalDepth * CM_TO_INCH,
            abdominalWidth = data.abdominalWidth * CM_TO_INCH,
            abdominalCircumference = data.abdominalCircumference * CM_TO_INCH,
            shoulderWidth = data.shoulderWidth * CM_TO_INCH,
            shoulderCircumference = data.shoulderCircumference * CM_TO_INCH,
            acromionHeight = data.acromionHeight * CM_TO_INCH,
            hipWidth = data.hipWidth * CM_TO_INCH,
            hipCircumference = data.hipCircumference * CM_TO_INCH,
            upperArmLength = data.upperArmLength * CM_TO_INCH,
            upperArmCircumference = data.upperArmCircumference * CM_TO_INCH,
            forearmLength = data.forearmLength * CM_TO_INCH,
            forearmCircumference = data.forearmCircumference * CM_TO_INCH,
            handLength = data.handLength * CM_TO_INCH,
            handWidth = data.handWidth * CM_TO_INCH,
            thighLength = data.thighLength * CM_TO_INCH,
            thighCircumference = data.thighCircumference * CM_TO_INCH,
            lowerLegLength = data.lowerLegLength * CM_TO_INCH,
            lowerLegCircumference = data.lowerLegCircumference * CM_TO_INCH,
            footLength = data.footLength * CM_TO_INCH,
            footWidth = data.footWidth * CM_TO_INCH
        )
    }

    /**
     * 批量转换长度值
     * 
     * @param values 长度值列表（cm）
     * @return 转换后的长度值列表（in）
     */
    fun batchCmToInch(values: List<Double>): List<ConversionResult> {
        return values.map { cmToInch(it) }
    }

    /**
     * 批量转换重量值
     * 
     * @param values 重量值列表（kg）
     * @return 转换后的重量值列表（lb）
     */
    fun batchKgToLb(values: List<Double>): List<ConversionResult> {
        return values.map { kgToLb(it) }
    }

    /**
     * 格式化转换结果为可读字符串
     * 
     * @param result 转换结果
     * @return 格式化字符串
     */
    fun formatConversionResult(result: ConversionResult): String {
        return "${result.originalValue} ${result.originalUnit} = ${result.convertedValue} ${result.convertedUnit}"
    }

    /**
     * 四舍五入到指定小数位数
     * 
     * @param value 原始值
     * @param decimals 小数位数
     * @return 四舍五入后的值
     */
    private fun roundToDecimal(value: Double, decimals: Int): Double {
        val factor = 10.0.pow(decimals.toDouble())
        return kotlin.math.round(value * factor) / factor
    }

    /**
     * 验证转换精度
     * 
     * @param result 转换结果
     * @param maxError 最大允许误差（相对误差）
     * @return 是否满足精度要求
     */
    fun verifyPrecision(result: ConversionResult, maxError: Double = 0.001): Boolean {
        // 反向转换验证
        val reversed = when (result.originalUnit) {
            "cm" -> inchToCm(result.convertedValue)
            "in" -> cmToInch(result.convertedValue)
            "kg" -> lbToKg(result.convertedValue)
            "lb" -> kgToLb(result.convertedValue)
            "°C" -> fahrenheitToCelsius(result.convertedValue)
            "°F" -> celsiusToFahrenheit(result.convertedValue)
            else -> return false
        }

        // 计算相对误差
        val relativeError = kotlin.math.abs(reversed.convertedValue - result.originalValue) / result.originalValue

        return relativeError <= maxError
    }
}

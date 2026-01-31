package com.childproduct.designassistant.service

import com.childproduct.designassistant.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 数据百分位分析服务
 * 
 * 支持：
 * - 5th百分位计算
 * - 平均值计算（50th百分位）
 * - 95th百分位计算
 * - 自定义百分位计算
 * - 标准差计算
 * - 正态分布拟合
 * 
 * 确保计算精度 < 0.1%
 */
class PercentileAnalysisService {

    /**
     * 百分位计算结果
     */
    data class PercentileResult(
        val percentile: Percentile,
        val value: Double,
        val parameter: String,
        val ageMonths: Int,
        val standardDeviation: Double,
        val confidenceInterval95: Pair<Double, Double>,
        val sampleSize: Int
    )

    /**
     * 参数百分位分布
     */
    data class ParameterPercentileDistribution(
        val parameter: String,
        val ageMonths: Int,
        val fifthPercentile: Double,      // 5th百分位
        val mean: Double,                  // 平均值（50th百分位）
        val ninetyFifthPercentile: Double, // 95th百分位
        val standardDeviation: Double,
        val coefficientOfVariation: Double,  // 变异系数
        val distributionType: DistributionType
    )

    /**
     * 分布类型
     */
    enum class DistributionType(val displayName: String) {
        NORMAL("正态分布"),
        LOG_NORMAL("对数正态分布"),
        SKEWED("偏态分布")
    }

    /**
     * 计算指定百分位的值
     * 
     * 基于正态分布假设，使用Z分数计算
     * 
     * @param mean 平均值
     * @param standardDeviation 标准差
     * @param percentile 目标百分位
     * @return 百分位对应的值
     */
    fun calculatePercentileValue(
        mean: Double,
        standardDeviation: Double,
        percentile: Percentile
    ): Double {
        val zScore = getZScore(percentile.value)
        return mean + zScore * standardDeviation
    }

    /**
     * 计算数据集的百分位分布
     * 
     * @param parameterName 参数名称
     * @param ageMonths 年龄（月）
     * @param fifthValue 5th百分位值
     * @param meanValue 平均值
     * @param ninetyFifthValue 95th百分位值
     * @return 百分位分布结果
     */
    fun analyzePercentileDistribution(
        parameterName: String,
        ageMonths: Int,
        fifthValue: Double,
        meanValue: Double,
        ninetyFifthValue: Double
    ): ParameterPercentileDistribution {
        // 计算标准差（基于95th百分位）
        // 95th百分位的Z分数约为1.645
        val standardDeviation = (ninetyFifthValue - meanValue) / 1.645

        // 计算变异系数
        val coefficientOfVariation = (standardDeviation / meanValue) * 100.0

        // 判断分布类型
        val distributionType = determineDistributionType(fifthValue, meanValue, ninetyFifthValue)

        return ParameterPercentileDistribution(
            parameter = parameterName,
            ageMonths = ageMonths,
            fifthPercentile = fifthValue,
            mean = meanValue,
            ninetyFifthPercentile = ninetyFifthValue,
            standardDeviation = standardDeviation,
            coefficientOfVariation = coefficientOfVariation,
            distributionType = distributionType
        )
    }

    /**
     * 从原始数据集计算百分位分布
     * 
     * @param parameterName 参数名称
     * @param ageMonths 年龄（月）
     * @param values 原始数据值列表
     * @return 百分位分布结果
     */
    fun calculatePercentileFromDataset(
        parameterName: String,
        ageMonths: Int,
        values: List<Double>
    ): ParameterPercentileDistribution {
        // 排序数据
        val sortedValues = values.sorted()

        // 计算百分位值
        val fifthPercentile = calculatePercentileFromSorted(sortedValues, 5.0)
        val mean = sortedValues.average()
        val ninetyFifthPercentile = calculatePercentileFromSorted(sortedValues, 95.0)

        // 计算标准差
        val variance = sortedValues.map { (it - mean).pow(2) }.average()
        val standardDeviation = sqrt(variance)

        // 计算变异系数
        val coefficientOfVariation = if (mean != 0.0) {
            (standardDeviation / mean) * 100.0
        } else {
            0.0
        }

        // 判断分布类型
        val distributionType = determineDistributionType(fifthPercentile, mean, ninetyFifthPercentile)

        return ParameterPercentileDistribution(
            parameter = parameterName,
            ageMonths = ageMonths,
            fifthPercentile = fifthPercentile,
            mean = mean,
            ninetyFifthPercentile = ninetyFifthPercentile,
            standardDeviation = standardDeviation,
            coefficientOfVariation = coefficientOfVariation,
            distributionType = distributionType
        )
    }

    /**
     * 计算儿童数据的百分位分布
     * 
     * @param childDataList 儿童数据列表
     * @param ageMonths 年龄（月）
     * @param parameterSelector 参数选择器函数
     * @return 百分位分布结果
     */
    fun analyzeChildDataPercentile(
        childDataList: List<ChildAnthropometricData>,
        ageMonths: Int,
        parameterSelector: (ChildAnthropometricData) -> Double
    ): ParameterPercentileDistribution {
        // 筛选指定年龄的数据
        val filteredData = childDataList.filter {
            abs(it.ageMonths - ageMonths) <= 3  // ±3个月范围内
        }

        // 提取参数值
        val values = filteredData.map(parameterSelector)

        return calculatePercentileFromDataset(
            parameterName = "ChildParameter",
            ageMonths = ageMonths,
            values = values
        )
    }

    /**
     * 计算多参数的百分位分布
     * 
     * @param childDataList 儿童数据列表
     * @param ageMonths 年龄（月）
     * @return 多参数百分位分布Map
     */
    fun analyzeMultipleParameters(
        childDataList: List<ChildAnthropometricData>,
        ageMonths: Int
    ): Map<String, ParameterPercentileDistribution> {
        val results = mutableMapOf<String, ParameterPercentileDistribution>()

        // 分析多个关键参数
        results["身高"] = analyzeChildDataPercentile(childDataList, ageMonths) { it.stature }
        results["体重"] = analyzeChildDataPercentile(childDataList, ageMonths) { it.weight }
        results["肩宽"] = analyzeChildDataPercentile(childDataList, ageMonths) { it.shoulderWidth }
        results["臀宽"] = analyzeChildDataPercentile(childDataList, ageMonths) { it.hipWidth }
        results["坐高"] = analyzeChildDataPercentile(childDataList, ageMonths) { it.sittingHeight }
        results["头围"] = analyzeChildDataPercentile(childDataList, ageMonths) { it.headCircumference }

        return results
    }

    /**
     * 评估儿童数据的百分位位置
     * 
     * @param value 测量值
     * @param mean 平均值
     * @param standardDeviation 标准差
     * @return 百分位位置（0-100）
     */
    fun evaluatePercentilePosition(
        value: Double,
        mean: Double,
        standardDeviation: Double
    ): Double {
        // 计算Z分数
        val zScore = (value - mean) / standardDeviation

        // 将Z分数转换为百分位
        return normalCDF(zScore) * 100.0
    }

    /**
     * 生成百分位报告
     * 
     * @param distribution 百分位分布
     * @param childValue 儿童测量值
     * @return 报告文本
     */
    fun generatePercentileReport(
        distribution: ParameterPercentileDistribution,
        childValue: Double
    ): String {
        val percentilePosition = evaluatePercentilePosition(
            childValue,
            distribution.mean,
            distribution.standardDeviation
        )

        val positionDescription = when {
            percentilePosition < 5.0 -> "低于5th百分位（偏小）"
            percentilePosition < 25.0 -> "5th-25th百分位（偏小）"
            percentilePosition < 75.0 -> "25th-75th百分位（正常）"
            percentilePosition < 95.0 -> "75th-95th百分位（偏大）"
            else -> "高于95th百分位（偏大）"
        }

        val report = StringBuilder()
        report.appendLine("参数：${distribution.parameter}")
        report.appendLine("年龄：${distribution.ageMonths}个月")
        report.appendLine()
        report.appendLine("百分位分布：")
        report.appendLine("  - 5th百分位：${String.format("%.2f", distribution.fifthPercentile)}")
        report.appendLine("  - 平均值：${String.format("%.2f", distribution.mean)}")
        report.appendLine("  - 95th百分位：${String.format("%.2f", distribution.ninetyFifthPercentile)}")
        report.appendLine("  - 标准差：${String.format("%.2f", distribution.standardDeviation)}")
        report.appendLine("  - 变异系数：${String.format("%.2f", distribution.coefficientOfVariation)}%")
        report.appendLine()
        report.appendLine("儿童测量值：${String.format("%.2f", childValue)}")
        report.appendLine("百分位位置：${String.format("%.1f", percentilePosition)}th")
        report.appendLine("评估：$positionDescription")

        return report.toString()
    }

    /**
     * 从已排序列表计算百分位
     * 
     * @param sortedValues 已排序的值列表
     * @param percentile 目标百分位（0-100）
     * @return 百分位对应的值
     */
    private fun calculatePercentileFromSorted(
        sortedValues: List<Double>,
        percentile: Double
    ): Double {
        if (sortedValues.isEmpty()) return 0.0

        val n = sortedValues.size
        val rank = (percentile / 100.0) * (n - 1)
        val lowerIndex = rank.toInt()
        val upperIndex = minOf(lowerIndex + 1, n - 1)
        val fraction = rank - lowerIndex

        return sortedValues[lowerIndex] * (1 - fraction) + sortedValues[upperIndex] * fraction
    }

    /**
     * 获取Z分数（正态分布）
     * 
     * @param percentile 百分位（0-100）
     * @return Z分数
     */
    private fun getZScore(percentile: Double): Double {
        return inverseNormalCDF(percentile / 100.0)
    }

    /**
     * 正态分布累积分布函数（CDF）
     * 
     * @param x Z分数
     * @return 累积概率
     */
    private fun normalCDF(x: Double): Double {
        return 0.5 * (1.0 + erf(x / sqrt(2.0)))
    }

    /**
     * 正态分布逆累积分布函数（Inverse CDF）
     * 
     * 使用近似算法（Beasley-Springer-Moro算法）
     * 
     * @param p 概率（0-1）
     * @return Z分数
     */
    private fun inverseNormalCDF(p: Double): Double {
        require(p > 0.0 && p < 1.0) { "Probability must be between 0 and 1" }

        val a = listOf(-3.969683028665376e+01, 2.209460984245205e+02,
            -2.759285104469687e+02, 1.383577518672690e+02,
            -3.066479806614716e+01, 2.506628277459239e+00)

        val b = listOf(-5.447609879822406e+01, 1.615858368580409e+02,
            -1.556989798598866e+02, 6.680131188771972e+01,
            -1.328068155288572e+01)

        val c = listOf(-7.784894002430293e-03, -3.223964580411365e-01,
            -2.400758277161838e+00, -2.549732539343734e+00,
            4.374664141464968e+00, 2.938163982698783e+00)

        val d = listOf(7.784695709041462e-03, 3.224671290700398e-01,
            2.445134137142996e+00, 3.754408661907416e+00)

        val pLow = 0.02425
        val pHigh = 1.0 - pLow
        val q = p - 0.5
        val r: Double

        if (p < pLow) {
            r = sqrt(-2.0 * ln(p))
            return (((((c[0] * r + c[1]) * r + c[2]) * r + c[3]) * r + c[4]) * r + c[5]) /
                ((((d[0] * r + d[1]) * r + d[2]) * r + d[3]) * r + 1.0)
        } else if (p <= pHigh) {
            r = q * q
            return q * (((((a[0] * r + a[1]) * r + a[2]) * r + a[3]) * r + a[4]) * r + a[5]) /
                (((((b[0] * r + b[1]) * r + b[2]) * r + b[3]) * r + b[4]) * r + 1.0)
        } else {
            r = sqrt(-2.0 * ln(1.0 - p))
            return -(((((c[0] * r + c[1]) * r + c[2]) * r + c[3]) * r + c[4]) * r + c[5]) /
                ((((d[0] * r + d[1]) * r + d[2]) * r + d[3]) * r + 1.0)
        }
    }

    /**
     * 误差函数（erf）
     * 
     * @param x 输入值
     * @return 误差函数值
     */
    private fun erf(x: Double): Double {
        val a1 = 0.254829592
        val a2 = -0.284496736
        val a3 = 1.421413741
        val a4 = -1.453152027
        val a5 = 1.061405429
        val p = 0.3275911

        val sign = if (x < 0) -1 else 1
        val absX = abs(x)

        val t = 1.0 / (1.0 + p * absX)
        val y = 1.0 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * exp(-x * x)

        return sign * y
    }

    /**
     * 判断分布类型
     * 
     * @param fifthValue 5th百分位
     * @param mean 平均值
     * @param ninetyFifthValue 95th百分位
     * @return 分布类型
     */
    private fun determineDistributionType(
        fifthValue: Double,
        mean: Double,
        ninetyFifthValue: Double
    ): DistributionType {
        val lowerDeviation = mean - fifthValue
        val upperDeviation = ninetyFifthValue - mean

        // 计算偏度
        val skewness = (upperDeviation - lowerDeviation) / (upperDeviation + lowerDeviation)

        return when {
            abs(skewness) < 0.1 -> DistributionType.NORMAL
            skewness > 0.2 -> DistributionType.SKEWED
            else -> DistributionType.LOG_NORMAL
        }
    }

    /**
     * 计算生长曲线拟合
     * 
     * @param ageData 年龄与测量值的映射
     * @return 拟合结果（年龄 -> 预测值）
     */
    fun fitGrowthCurve(ageData: Map<Int, Double>): Map<Int, Double> {
        if (ageData.size < 2) return ageData

        // 使用三次样条插值（简化版）
        val sortedAges = ageData.keys.sorted()
        val result = mutableMapOf<Int, Double>()

        for (age in sortedAges) {
            result[age] = ageData[age] ?: 0.0
        }

        // 插值中间值（每隔6个月）
        val minAge = sortedAges.first()
        val maxAge = sortedAges.last()

        for (age in minAge..maxAge step 6) {
            if (age !in sortedAges) {
                // 线性插值
                val lowerAge = sortedAges.filter { it < age }.maxOrNull()
                val upperAge = sortedAges.filter { it > age }.minOrNull()

                if (lowerAge != null && upperAge != null) {
                    val lowerValue = ageData[lowerAge] ?: 0.0
                    val upperValue = ageData[upperAge] ?: 0.0
                    val ratio = (age - lowerAge).toDouble() / (upperAge - lowerAge)
                    result[age] = lowerValue + ratio * (upperValue - lowerValue)
                }
            }
        }

        return result
    }
}

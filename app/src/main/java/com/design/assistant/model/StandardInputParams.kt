package com.design.assistant.model

import kotlinx.serialization.Serializable

/**
 * 标准输入参数类型
 * 根据不同标准，需要输入不同的参数
 */
@Serializable
sealed class StandardInputParams {
    /**
     * ECE R129 (欧洲i-Size标准)
     * 输入：身高范围（cm）
     */
    @Serializable
    data class EceR129Params(
        val minHeightCm: Int,      // 最小身高 (cm)
        val maxHeightCm: Int       // 最大身高 (cm)
    ) : StandardInputParams()

    /**
     * FMVSS 213 (美国标准)
     * 输入：体重范围（磅）
     */
    @Serializable
    data class Fmvss213Params(
        val minWeightLb: Int,      // 最小体重 (磅)
        val maxWeightLb: Int       // 最大体重 (磅)
    ) : StandardInputParams()

    /**
     * GPS028 (中国GB 27887-2011标准)
     * 输入：身高和体重（cm和kg）
     */
    @Serializable
    data class Gps028Params(
        val heightCm: Int,         // 身高 (cm)
        val weightKg: Int          // 体重 (kg)
    ) : StandardInputParams()

    /**
     * CMVSS 213 (加拿大标准)
     * 输入：体重范围（kg）
     */
    @Serializable
    data class Cmvss213Params(
        val minWeightKg: Int,      // 最小体重 (kg)
        val maxWeightKg: Int       // 最大体重 (kg)
    ) : StandardInputParams()

    /**
     * 通用参数（其他标准）
     * 输入：身高和体重（cm和kg）
     */
    @Serializable
    data class GenericParams(
        val heightCm: Int = 0,     // 身高 (cm)，可选
        val weightKg: Int = 0      // 体重 (kg)，可选
    ) : StandardInputParams()
}

/**
 * 根据标准类型获取默认输入参数
 */
fun getStandardInputParams(standardType: StandardType): StandardInputParams {
    return when (standardType) {
        StandardType.ECE_R129 -> StandardInputParams.EceR129Params(
            minHeightCm = 87,
            maxHeightCm = 105
        )
        StandardType.FMVSS213 -> StandardInputParams.Fmvss213Params(
            minWeightLb = 20,
            maxWeightLb = 65
        )
        StandardType.GPS028 -> StandardInputParams.Gps028Params(
            heightCm = 95,
            weightKg = 15
        )
        StandardType.CMVSS213 -> StandardInputParams.Cmvss213Params(
            minWeightKg = 9,
            maxWeightKg = 30
        )
        else -> StandardInputParams.GenericParams(
            heightCm = 95,
            weightKg = 15
        )
    }
}

/**
 * 获取标准类型的输入参数类型说明
 */
fun getStandardInputDescription(standardType: StandardType): String {
    return when (standardType) {
        StandardType.ECE_R129 -> "请输入儿童身高范围（cm）"
        StandardType.FMVSS213 -> "请输入儿童体重范围（磅）"
        StandardType.GPS028 -> "请输入儿童身高和体重"
        StandardType.CMVSS213 -> "请输入儿童体重范围（kg）"
        else -> "请输入儿童身高和体重"
    }
}

/**
 * 获取标准类型的输入单位
 */
fun getStandardInputUnit(standardType: StandardType): String {
    return when (standardType) {
        StandardType.ECE_R129 -> "cm"
        StandardType.FMVSS213 -> "磅 (lb)"
        StandardType.GPS028 -> "cm / kg"
        StandardType.CMVSS213 -> "kg"
        else -> "cm / kg"
    }
}

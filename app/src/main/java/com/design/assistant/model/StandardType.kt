package com.design.assistant.model

import kotlinx.serialization.Serializable

/**
 * 标准类型枚举
 * 支持的国际标准
 */
@Serializable
enum class StandardType {
    // 中国标准
    GPS028,                 // GB 27887-2011 儿童安全座椅标准（GPS028参数）
    GB_8408,               // 游乐设施安全标准

    // 欧洲标准
    ECE_R129,              // 儿童安全座椅i-Size标准
    ECE_R44_04,            // 儿童安全座椅旧标准
    EN1888,                // 婴儿推车标准
    EN14988,               // 高脚椅标准
    EN716,                 // 儿童床标准

    // 美国标准
    FMVSS213,              // 儿童安全座椅标准
    ASTM_F833,             // 婴儿推车标准
    ASTM_F404,             // 高脚椅标准
    ASTM_F1169,            // 儿童床标准

    // 加拿大标准
    CMVSS213,              // 儿童安全座椅标准
    CSA_B311,              // 婴儿推车标准
    CSA_B229,              // 高脚椅标准
    CSA_B113,              // 儿童床标准

    // 澳大利亚/新西兰标准
    AS_NZS1754,            // 儿童安全座椅标准

    // 其他标准
    ISOFIX_Standard,       // ISOFIX国际标准
    SOR_85_379,            // 加拿大推车/高脚椅标准
    SOR_86_332,            // 加拿大儿童床标准
}

/**
 * 获取标准类型的中文名称
 */
fun StandardType.getDisplayName(): String {
    return when (this) {
        StandardType.GPS028 -> "GB 27887-2011 (GPS028)"
        StandardType.GB_8408 -> "GB 8408-2018"
        StandardType.ECE_R129 -> "ECE R129 i-Size"
        StandardType.ECE_R44_04 -> "ECE R44/04"
        StandardType.EN1888 -> "EN 1888"
        StandardType.EN14988 -> "EN 14988"
        StandardType.EN716 -> "EN 716"
        StandardType.FMVSS213 -> "FMVSS 213"
        StandardType.ASTM_F833 -> "ASTM F833"
        StandardType.ASTM_F404 -> "ASTM F404"
        StandardType.ASTM_F1169 -> "ASTM F1169"
        StandardType.CMVSS213 -> "CMVSS 213"
        StandardType.CSA_B311 -> "CSA B311"
        StandardType.CSA_B229 -> "CSA B229"
        StandardType.CSA_B113 -> "CSA B113"
        StandardType.AS_NZS1754 -> "AS/NZS 1754"
        StandardType.ISOFIX_Standard -> "ISOFIX 标准"
        StandardType.SOR_85_379 -> "SOR/85-379"
        StandardType.SOR_86_332 -> "SOR/86-332"
    }
}

/**
 * 获取标准类型的地区标识
 */
fun StandardType.getRegion(): String {
    return when (this) {
        StandardType.GPS028, StandardType.GB_8408 -> "中国"
        StandardType.ECE_R129, StandardType.ECE_R44_04, StandardType.EN1888,
        StandardType.EN14988, StandardType.EN716, StandardType.ISOFIX_Standard -> "欧洲"
        StandardType.FMVSS213, StandardType.ASTM_F833, StandardType.ASTM_F404,
        StandardType.ASTM_F1169 -> "美国"
        StandardType.CMVSS213, StandardType.CSA_B311, StandardType.CSA_B229,
        StandardType.CSA_B113, StandardType.SOR_85_379, StandardType.SOR_86_332 -> "加拿大"
        StandardType.AS_NZS1754 -> "澳大利亚/新西兰"
    }
}

/**
 * 获取标准类型的关键条款编号
 */
fun StandardType.getKeyClauses(): List<String> {
    return when (this) {
        StandardType.GPS028 -> listOf(
            "5.3.1.1 头部伤害指标HIC",
            "5.3.1.2 胸部加速度",
            "5.3.1.3 颈部力矩",
            "5.4.1.1 假人位移",
            "5.4.1.2 假人角度",
            "5.5.1.1 带载荷测试"
        )
        StandardType.ECE_R129 -> listOf(
            "5.1.1 头部伤害指标HIC",
            "5.1.2 胸部加速度",
            "5.1.3 颈部力矩",
            "6.1.1 假人位移",
            "6.1.2 假人角度"
        )
        StandardType.CMVSS213 -> listOf(
            "S5.1 头部伤害指标HIC",
            "S5.2 胸部加速度",
            "S5.3 颈部力矩",
            "S6.1 假人位移"
        )
        StandardType.FMVSS213 -> listOf(
            "S5.1 头部伤害指标HIC",
            "S5.2 胸部加速度",
            "S6.1 假人位移"
        )
        StandardType.EN1888 -> listOf(
            "4.2.1 结构强度",
            "4.3.1 锁定机构",
            "4.4.1 稳定性"
        )
        StandardType.ASTM_F833 -> listOf(
            "4.1 结构强度",
            "5.1 稳定性"
        )
        StandardType.CSA_B311 -> listOf(
            "5.1 结构强度",
            "6.1 稳定性"
        )
        StandardType.EN14988 -> listOf(
            "4.1.1 结构强度",
            "4.2.1 稳定性"
        )
        StandardType.ASTM_F404 -> listOf(
            "4.1.1 结构强度",
            "5.1 稳定性"
        )
        StandardType.CSA_B229 -> listOf(
            "4.1.1 结构强度",
            "5.1 稳定性"
        )
        StandardType.EN716 -> listOf(
            "4.1.1 结构强度",
            "4.2.1 间隙安全"
        )
        StandardType.ASTM_F1169 -> listOf(
            "4.1.1 结构强度",
            "4.2.1 间隙安全"
        )
        StandardType.CSA_B113 -> listOf(
            "4.1.1 结构强度",
            "4.2.1 间隙安全"
        )
        StandardType.AS_NZS1754 -> listOf(
            "5.1 头部伤害指标HIC",
            "5.2 胸部加速度"
        )
        StandardType.ISOFIX_Standard -> listOf(
            "4.1 ISOFIX接口尺寸",
            "4.2 强度要求"
        )
        StandardType.SOR_85_379 -> listOf(
            "5.1 结构强度",
            "6.1 稳定性"
        )
        StandardType.SOR_86_332 -> listOf(
            "4.1 结构强度",
            "4.2 间隙安全"
        )
        else -> listOf()
    }
}

/**
 * 获取标准类型的优先级（数值越大优先级越高）
 */
fun StandardType.getPriority(): Int {
    return when (this) {
        StandardType.GPS028 -> 100  // 最高优先级
        StandardType.ECE_R129 -> 90
        StandardType.CMVSS213 -> 85
        StandardType.FMVSS213 -> 80
        StandardType.AS_NZS1754 -> 75
        StandardType.ECE_R44_04 -> 70
        StandardType.EN1888 -> 60
        StandardType.ASTM_F833 -> 55
        StandardType.CSA_B311 -> 50
        StandardType.EN14988 -> 40
        StandardType.ASTM_F404 -> 35
        StandardType.CSA_B229 -> 30
        StandardType.EN716 -> 20
        StandardType.ASTM_F1169 -> 15
        StandardType.CSA_B113 -> 10
        else -> 0
    }
}

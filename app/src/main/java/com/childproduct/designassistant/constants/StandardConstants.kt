package com.childproduct.designassistant.constants

import com.childproduct.designassistant.model.ProductType

/**
 * 全局标准常量：代码定义+显示名称+产品-标准映射
 * 面向专业工程师：含标准编号/地区/实施属性，新增标准仅修改此类
 * 
 * 修复说明：
 * - 统一使用下划线命名（无空格/大小写差异）
 * - 提供标准ID到常量的映射
 * - 确保全链路使用统一标识
 * - 包含四大品类（安全座椅/婴儿推车/高脚椅/儿童床）的全球标准
 * 
 * 使用原则：
 * - 所有代码中必须使用这些常量，禁止硬编码字符串
 * - 数据库、Repository、ViewModel、UI统一使用这些常量
 * - 确保不会因空格/大小写差异导致匹配失败
 */
object StandardConstants {

    // ============= 儿童安全座椅（出行类） =============
    const val ECE_R129 = "ECE_R129"          // 欧盟i-Size 2021
    const val GB_27887_2024 = "GB_27887_2024"// 中国新标 2025强制
    const val FMVSS_213 = "FMVSS_213"        // 美国 2022版
    const val AS_NZS_1754 = "AS_NZS_1754"    // 澳洲 2020版
    const val CMVSS_213 = "CMVSS_213"        // 加拿大 第5版
    const val JIS_D1601 = "JIS_D1601"        // 日本标准

    // ============= 婴儿推车（出行类） =============
    const val EN_1888 = "EN_1888"            // 欧盟 2022版
    const val GB_14748 = "GB_14748"          // 中国 2020版
    const val ASTM_F833 = "ASTM_F833"        // 美国 2023版
    const val CAN_CSA_D425 = "CAN_CSA_D425"  // 加拿大 2021版
    const val AS_NZS_2088 = "AS_NZS_2088"    // 澳洲 2020版

    // ============= 儿童高脚椅（家居类） =============
    const val EN_14988 = "EN_14988"          // 欧盟 2021版
    const val GB_29281 = "GB_29281"          // 中国 2012版
    const val CAN_CSA_Z217_1 = "CAN_CSA_Z217_1"// 加拿大 2022版
    const val ASTM_F404 = "ASTM_F404"        // 美国 2023版

    // ============= 儿童床（家居类） =============
    const val EN_716 = "EN_716"              // 欧盟 2021版
    const val GB_28007 = "GB_28007"          // 中国 2011版
    const val CAN_CSA_D1169 = "CAN_CSA_D1169"// 加拿大 2020版
    const val ASTM_F1169 = "ASTM_F1169"      // 美国 2022版

    // ========== 标准ID映射（UI标识 → 常量） ==========

    /**
     * 标准ID映射表
     * 用于将UI中的标准ID转换为统一常量
     */
    private val STANDARD_ID_MAP = mapOf(
        // ECE R129相关
        "ece_r129" to ECE_R129,
        "ECE R129" to ECE_R129,
        "ECE-R129" to ECE_R129,
        "i-Size" to ECE_R129,

        // FMVSS 213相关
        "fmvss_213" to FMVSS_213,
        "FMVSS 213" to FMVSS_213,
        "FMVSS-213" to FMVSS_213,
        "FMVSS 213a" to FMVSS_213,

        // GB 27887-2024相关
        "gb_27887_2024" to GB_27887_2024,
        "GB 27887-2024" to GB_27887_2024,
        "GB-27887-2024" to GB_27887_2024,
        "gb_27887" to GB_27887_2024,

        // CMVSS 213相关
        "cmvss_213" to CMVSS_213,
        "CMVSS 213" to CMVSS_213,
        "CMVSS-213" to CMVSS_213,

        // JIS D1601相关
        "jis_d1601" to JIS_D1601,
        "JIS D1601" to JIS_D1601,
        "JIS-D1601" to JIS_D1601,

        // AS/NZS 1754相关
        "as_nzs_1754" to AS_NZS_1754,
        "AS/NZS 1754" to AS_NZS_1754,
        "AS_NZS_1754" to AS_NZS_1754,

        // EN 1888相关
        "en_1888" to EN_1888,
        "EN 1888" to EN_1888,
        "EN-1888" to EN_1888,

        // GB 14748相关
        "gb_14748" to GB_14748,
        "GB 14748" to GB_14748,
        "GB-14748" to GB_14748,

        // ASTM F833相关
        "astm_f833" to ASTM_F833,
        "ASTM F833" to ASTM_F833,
        "ASTM-F833" to ASTM_F833,

        // CAN/CSA D425相关
        "can_csa_d425" to CAN_CSA_D425,
        "CAN/CSA D425" to CAN_CSA_D425,
        "CAN_CSA_D425" to CAN_CSA_D425,

        // AS/NZS 2088相关
        "as_nzs_2088" to AS_NZS_2088,
        "AS/NZS 2088" to AS_NZS_2088,
        "AS_NZS_2088" to AS_NZS_2088,

        // EN 14988相关
        "en_14988" to EN_14988,
        "EN 14988" to EN_14988,
        "EN-14988" to EN_14988,

        // GB 29281相关
        "gb_29281" to GB_29281,
        "GB 29281" to GB_29281,
        "GB-29281" to GB_29281,

        // CAN/CSA Z217.1相关
        "can_csa_z217_1" to CAN_CSA_Z217_1,
        "CAN/CSA Z217.1" to CAN_CSA_Z217_1,
        "CAN_CSA_Z217_1" to CAN_CSA_Z217_1,

        // ASTM F404相关
        "astm_f404" to ASTM_F404,
        "ASTM F404" to ASTM_F404,
        "ASTM-F404" to ASTM_F404,

        // EN 716相关
        "en_716" to EN_716,
        "EN 716" to EN_716,
        "EN-716" to EN_716,

        // GB 28007相关
        "gb_28007" to GB_28007,
        "GB 28007" to GB_28007,
        "GB-28007" to GB_28007,

        // CAN/CSA D1169相关
        "can_csa_d1169" to CAN_CSA_D1169,
        "CAN/CSA D1169" to CAN_CSA_D1169,
        "CAN_CSA_D1169" to CAN_CSA_D1169,

        // ASTM F1169相关
        "astm_f1169" to ASTM_F1169,
        "ASTM F1169" to ASTM_F1169,
        "ASTM-F1169" to ASTM_F1169
    )

    /**
     * 根据标准ID获取常量
     * 
     * @param standardId 标准ID（可以是各种格式，如"ece_r129"、"ECE R129"等）
     * @return 统一常量，如果未找到则返回原ID
     */
    fun getStandardConstant(standardId: String): String {
        return STANDARD_ID_MAP[standardId] ?: standardId
    }

    /**
     * 标准代码 → 专业显示名称（含版本/地区）
     */
    fun getStandardName(standardCode: String): String {
        return when (standardCode) {
            // 儿童安全座椅
            ECE_R129 -> "ECE R129:2021 (欧盟i-Size)"
            GB_27887_2024 -> "GB 27887-2024 (中国新标，2025强制)"
            FMVSS_213 -> "FMVSS 213 (美国2022版，联邦强制)"
            AS_NZS_1754 -> "AS/NZS 1754:2020 (澳洲强制)"
            CMVSS_213 -> "CMVSS 213 (加拿大第5版，强制)"
            JIS_D1601 -> "JIS D1601 (日本标准)"
            // 婴儿推车
            EN_1888 -> "EN 1888:2022 (欧盟强制)"
            GB_14748 -> "GB 14748-2020 (中国强制)"
            ASTM_F833 -> "ASTM F833-23 (美国材料协会)"
            CAN_CSA_D425 -> "CAN/CSA D425-21 (加拿大强制)"
            AS_NZS_2088 -> "AS/NZS 2088:2020 (澳洲强制)"
            // 高脚椅
            EN_14988 -> "EN 14988:2021 (欧盟强制)"
            GB_29281 -> "GB 29281-2012 (中国强制)"
            CAN_CSA_Z217_1 -> "CAN/CSA Z217.1-22 (加拿大强制)"
            ASTM_F404 -> "ASTM F404-23 (美国材料协会)"
            // 儿童床
            EN_716 -> "EN 716:2021 (欧盟强制)"
            GB_28007 -> "GB 28007-2011 (中国强制)"
            CAN_CSA_D1169 -> "CAN/CSA D1169-20 (加拿大强制)"
            ASTM_F1169 -> "ASTM F1169-22 (美国材料协会)"
            else -> "未知标准"
        }
    }

    /**
     * 产品类型 → 对应支持的标准列表（自动关联，工程师无需手动匹配）
     */
    fun getStandardsByProduct(productType: ProductType): List<String> {
        return when (productType.standard) {
            ProductType.CHILD_SEAT -> listOf(ECE_R129, GB_27887_2024, FMVSS_213, AS_NZS_1754, CMVSS_213, JIS_D1601)
            ProductType.BABY_STROLLER -> listOf(EN_1888, GB_14748, ASTM_F833, CAN_CSA_D425, AS_NZS_2088)
            ProductType.HIGH_CHAIR -> listOf(EN_14988, GB_29281, CAN_CSA_Z217_1, ASTM_F404)
            ProductType.CHILD_BED -> listOf(EN_716, GB_28007, CAN_CSA_D1169, ASTM_F1169)
            else -> emptyList()
        }
    }

    /**
     * 获取所有有效标准列表
     */
    fun getAllStandards(): List<String> {
        return listOf(
            ECE_R129, GB_27887_2024, FMVSS_213, AS_NZS_1754, CMVSS_213, JIS_D1601,
            EN_1888, GB_14748, ASTM_F833, CAN_CSA_D425, AS_NZS_2088,
            EN_14988, GB_29281, CAN_CSA_Z217_1, ASTM_F404,
            EN_716, GB_28007, CAN_CSA_D1169, ASTM_F1169
        )
    }

    /**
     * 检查是否为有效的标准类型
     */
    fun isValidStandardType(standardType: String): Boolean {
        val normalizedType = getStandardConstant(standardType)
        return normalizedType in getAllStandards()
    }

    /**
     * 检查是否为ECE标准
     */
    fun isEceStandard(standardType: String): Boolean {
        val normalizedType = getStandardConstant(standardType)
        return normalizedType == ECE_R129 || normalizedType == GB_27887_2024
    }

    /**
     * 检查是否为FMVSS标准
     */
    fun isFmvssStandard(standardType: String): Boolean {
        val normalizedType = getStandardConstant(standardType)
        return normalizedType == FMVSS_213
    }

    /**
     * 获取标准数据库引用
     */
    fun getDbNameByStandard(standardCode: String): String {
        return getStandardName(standardCode).split("(")[0].trim() + "_db"
    }
}

/**
 * 标准代码转专业名称扩展方法，仓库层用
 */
fun String.toProStdName(): String = StandardConstants.getStandardName(this)

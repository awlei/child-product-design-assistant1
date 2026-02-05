package com.childproduct.designassistant.constants

/**
 * 标准常量
 *
 * 统一标识各种标准类型，避免字符串硬编码导致的匹配问题
 */
object StandardConstants {

    /** ECE R129 (欧盟i-Size) */
    const val ECE_R129 = "ECE_R129"

    /** GB 27887-2024 (中国新标) */
    const val GB_27887_2024 = "GB_27887_2024"

    /** FMVSS 213 (美国标准) */
    const val FMVSS_213 = "FMVSS_213"

    /** AS/NZS 1754 (澳洲标准) */
    const val AS_NZS_1754 = "AS_NZS_1754"

    /** JIS D 1601 (日本标准) */
    const val JIS_D1601 = "JIS_D1601"

    /**
     * 根据标准ID获取标准常量
     *
     * @param standardId 标准ID（如"ece_r129"、"fmvss_213"等）
     * @return 标准常量字符串，如果未匹配则返回null
     */
    fun getStandardConstant(standardId: String?): String? {
        return when (standardId?.lowercase()) {
            "ece_r129", "ece r129" -> ECE_R129
            "gb_27887_2024", "gb 27887", "gb 28007" -> GB_27887_2024
            "fmvss_213", "fmvss 213" -> FMVSS_213
            "as_nzs_1754", "as/nzs 1754" -> AS_NZS_1754
            "jis_d1601", "jis d 1601" -> JIS_D1601
            else -> null
        }
    }

    /**
     * 获取所有支持的标准列表
     */
    fun getAllStandards(): List<String> {
        return listOf(
            ECE_R129,
            GB_27887_2024,
            FMVSS_213,
            AS_NZS_1754,
            JIS_D1601
        )
    }

    /**
     * 获取标准显示名称
     *
     * @param standardConstant 标准常量
     * @return 显示名称
     */
    fun getDisplayName(standardConstant: String): String {
        return when (standardConstant) {
            ECE_R129 -> "ECE R129 (欧盟i-Size)"
            GB_27887_2024 -> "GB 27887-2024 (中国新标)"
            FMVSS_213 -> "FMVSS 213 (美国标准)"
            AS_NZS_1754 -> "AS/NZS 1754 (澳洲标准)"
            JIS_D1601 -> "JIS D 1601 (日本标准)"
            else -> standardConstant
        }
    }
}

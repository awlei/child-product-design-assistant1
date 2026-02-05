package com.childproduct.designassistant.constants

/**
 * 标准常量定义（统一标识，避免匹配失败）
 * 
 * 修复说明：
 * - 统一使用下划线命名（无空格/大小写差异）
 * - 提供标准ID到常量的映射
 * - 确保全链路使用统一标识
 * 
 * 使用原则：
 * - 所有代码中必须使用这些常量，禁止硬编码字符串
 * - 数据库、Repository、ViewModel、UI统一使用这些常量
 * - 确保不会因空格/大小写差异导致匹配失败
 */
object StandardConstants {

    // ========== 标准类型标识（统一格式） ==========

    /**
     * ECE R129标准（欧盟i-Size）
     */
    const val ECE_R129 = "ECE_R129"

    /**
     * FMVSS 213标准（美国）
     */
    const val FMVSS_213 = "FMVSS_213"

    /**
     * GB 27887-2024标准（中国新标）
     */
    const val GB_27887_2024 = "GB_27887_2024"

    /**
     * EN 14988标准（欧盟高脚椅）
     */
    const val EN_14988 = "EN_14988"

    /**
     * GB 29281标准（中国高脚椅）
     */
    const val GB_29281 = "GB_29281"

    /**
     * EN 716标准（欧盟儿童床）
     */
    const val EN_716 = "EN_716"

    /**
     * GB 28007标准（中国儿童床）
     */
    const val GB_28007 = "GB_28007"

    /**
     * AS/NZS 1754标准（澳洲）
     */
    const val AS_NZS_1754 = "AS_NZS_1754"

    /**
     * JIS D 1601标准（日本）
     */
    const val JIS_D1601 = "JIS_D1601"

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

        // EN 14988相关
        "en_14988" to EN_14988,
        "EN 14988" to EN_14988,
        "EN-14988" to EN_14988,

        // GB 29281相关
        "gb_29281" to GB_29281,
        "GB 29281" to GB_29281,
        "GB-29281" to GB_29281,

        // EN 716相关
        "en_716" to EN_716,
        "EN 716" to EN_716,
        "EN-716" to EN_716,

        // GB 28007相关
        "gb_28007" to GB_28007,
        "GB 28007" to GB_28007,
        "GB-28007" to GB_28007,

        // AS/NZS 1754相关
        "as_nzs_1754" to AS_NZS_1754,
        "AS/NZS 1754" to AS_NZS_1754,
        "AS_NZS_1754" to AS_NZS_1754,

        // JIS D 1601相关
        "jis_d1601" to JIS_D1601,
        "JIS D 1601" to JIS_D1601,
        "JIS-D1601" to JIS_D1601
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
     * 检查是否为有效的标准类型
     * 
     * @param standardType 标准类型
     * @return 是否有效
     */
    fun isValidStandardType(standardType: String): Boolean {
        val normalizedType = getStandardConstant(standardType)
        return normalizedType in listOf(
            ECE_R129,
            FMVSS_213,
            GB_27887_2024,
            EN_14988,
            GB_29281,
            EN_716,
            GB_28007,
            AS_NZS_1754,
            JIS_D1601
        )
    }

    /**
     * 检查是否为ECE标准
     * 
     * @param standardType 标准类型
     * @return 是否为ECE标准
     */
    fun isEceStandard(standardType: String): Boolean {
        val normalizedType = getStandardConstant(standardType)
        return normalizedType == ECE_R129 || normalizedType == GB_27887_2024
    }

    /**
     * 检查是否为FMVSS标准
     * 
     * @param standardType 标准类型
     * @return 是否为FMVSS标准
     */
    fun isFmvssStandard(standardType: String): Boolean {
        val normalizedType = getStandardConstant(standardType)
        return normalizedType == FMVSS_213
    }

    /**
     * 获取标准名称（用于显示）
     * 
     * @param standardType 标准类型
     * @return 标准名称
     */
    fun getStandardName(standardType: String): String {
        val normalizedType = getStandardConstant(standardType)
        return when (normalizedType) {
            ECE_R129 -> "ECE R129 (欧盟i-Size)"
            FMVSS_213 -> "FMVSS 213 (美国)"
            GB_27887_2024 -> "GB 27887-2024 (中国)"
            EN_14988 -> "EN 14988 (欧盟高脚椅)"
            GB_29281 -> "GB 29281 (中国高脚椅)"
            EN_716 -> "EN 716 (欧盟儿童床)"
            GB_28007 -> "GB 28007 (中国儿童床)"
            AS_NZS_1754 -> "AS/NZS 1754 (澳洲)"
            JIS_D1601 -> "JIS D 1601 (日本)"
            else -> standardType
        }
    }

    /**
     * 获取标准数据库引用
     * 
     * @param standardType 标准类型
     * @return 数据库引用名称
     */
    fun getDatabaseRef(standardType: String): String {
        val normalizedType = getStandardConstant(standardType)
        return when (normalizedType) {
            ECE_R129, GB_27887_2024 -> "EceR129Database"
            FMVSS_213 -> "FMVSSDatabase"
            EN_14988, GB_29281 -> "HighChairDatabase"
            EN_716, GB_28007 -> "CribDatabase"
            else -> "UnknownDatabase"
        }
    }

    /**
     * 获取所有支持的儿童安全座椅标准
     * 
     * @return 标准列表
     */
    fun getChildSafetySeatStandards(): List<String> {
        return listOf(ECE_R129, FMVSS_213, GB_27887_2024)
    }

    /**
     * 获取所有支持的标准
     * 
     * @return 标准列表
     */
    fun getAllStandards(): List<String> {
        return listOf(
            ECE_R129,
            FMVSS_213,
            GB_27887_2024,
            EN_14988,
            GB_29281,
            EN_716,
            GB_28007,
            AS_NZS_1754,
            JIS_D1601
        )
    }

    /**
     * 获取标准描述
     * 
     * @param standardType 标准类型
     * @return 标准描述
     */
    fun getStandardDescription(standardType: String): String {
        val normalizedType = getStandardConstant(standardType)
        return when (normalizedType) {
            ECE_R129 -> "联合国欧洲经济委员会儿童约束系统法规，基于身高分类，使用Q系列假人"
            FMVSS_213 -> "美国联邦机动车儿童约束系统安全标准，使用HIII和Q3s假人"
            GB_27887_2024 -> "中国机动车儿童乘员用约束系统国家标准，等效采用ECE R129"
            EN_14988 -> "欧盟儿童高脚椅安全要求标准"
            GB_29281 -> "中国儿童高脚椅安全要求标准"
            EN_716 -> "欧盟儿童家具-床安全要求标准"
            GB_28007 -> "中国儿童家具通用技术条件标准"
            AS_NZS_1754 -> "澳大利亚/新西兰儿童约束系统标准"
            JIS_D1601 -> "日本儿童约束系统安全标准"
            else -> "未知标准"
        }
    }

    /**
     * 获取标准显示名称（用于UI展示）
     * 
     * @param standardType 标准类型
     * @return 显示名称
     */
    fun getDisplayName(standardType: String): String {
        return getStandardName(standardType)
    }
}

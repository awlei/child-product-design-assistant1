package com.childproduct.designassistant.helper

/**
 * 白名单过滤：仅保留有效字符，彻底阻断乱码
 */
object TextWhiteListFilter {
    private val WHITE_LIST_PATTERN = Regex("""[^\u4e00-\u9fa5a-zA-Z0-9\s，。！？；：""''（）【】、·-+=≤≥/§]""")

    /**
     * 过滤文本，仅保留白名单中的字符
     * @param text 原始文本
     * @return 过滤后的文本
     */
    fun filter(text: String): String {
        return WHITE_LIST_PATTERN.replace(text, "").trim()
    }

    /**
     * 深度过滤，移除所有非标准字符（更严格）
     * @param text 原始文本
     * @return 过滤后的文本
     */
    fun deepFilter(text: String): String {
        return text
            .replace(Regex("""[\p{So}\p{Sk}\p{Cc}\p{Cf}]"""), "")
            .replace(Regex("""\s{2,}"""), " ")
            .trim()
    }
}

package com.childproduct.designassistant.helper

/**
 * 场景适配工具：处理年龄段冲突、补全描述
 */
object SceneAdapter {
    /**
     * 修正年龄段（核心：按标准强制映射）
     */
    fun correctAgeRange(heightRange: String, inputAge: String): Pair<String, String> {
        // 标准规则：40-150cm → 0-12岁（不可突破）
        return if (heightRange.contains("40-150") ||
                    heightRange.contains("40") && heightRange.contains("150")) {
            val hint = if (inputAge.contains("12岁以上") ||
                             inputAge.contains("13岁") ||
                             inputAge.contains("14岁") ||
                             inputAge.contains("15岁")) {
                "（注：按ECE R129/GB 27887-2024标准，40-150cm身高仅适配0-12岁，已自动修正）"
            } else {
                ""
            }
            Pair("0-12岁", hint)
        } else {
            Pair(inputAge, "")
        }
    }

    /**
     * 补全不完整描述（基于主题关键词）
     */
    fun completeDescription(theme: String): String {
        return when {
            theme.contains("社交元素") ->
                "专为0-12岁儿童设计的儿童安全座椅，融入社交元素设计理念。支持座椅装饰个性化定制、好友互动贴纸搭配，同时兼顾安全与趣味性，符合ECE R129 i-Size儿童标准，通过ISOFIX连接实现快速安装。"
            theme.contains("个性化设计") ->
                "专为0-12岁儿童设计的儿童安全座椅，融入个性化设计理念。支持颜色、图案自定义，适配不同儿童审美偏好，材质环保无异味，安装便捷，安全性能达标。"
            theme.contains("拼图游戏") ->
                "专为0-12岁儿童设计的儿童安全座椅，融入拼图游戏元素。采用趣味拼图装饰面板，激发儿童想象力，同时保持座椅结构完整性和安全性能，符合国际儿童安全标准。"
            theme.contains("卡通图案") ->
                "专为0-12岁儿童设计的儿童安全座椅，采用卡通图案设计。精选儿童喜爱的卡通形象，色彩鲜明但不刺眼，材质安全无毒，符合儿童产品安全要求。"
            theme.contains("科技元素") ->
                "专为0-12岁儿童设计的儿童安全座椅，融入科技元素设计。采用流线型外观和科技感配色，搭配智能调节功能，兼顾安全性与科技感，符合ECE R129标准。"
            else ->
                "专为0-12岁儿童设计的儿童安全座椅，聚焦安全性、舒适性、易安装性和材质环保四大核心特点，符合国际安全标准，适配全年龄段儿童使用。"
        }
    }
}

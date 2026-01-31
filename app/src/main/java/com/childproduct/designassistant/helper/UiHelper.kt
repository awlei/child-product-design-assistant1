package com.childproduct.designassistant.helper

import android.content.Context
import android.widget.RadioButton
import android.widget.RadioGroup
import com.childproduct.designassistant.model.InstallMethod

/**
 * UI交互辅助类：帮助快速实现输入项的UI适配（如安装方式单选按钮）
 */
object UiHelper {
    /**
     * 为安装方式单选组设置选项（动态生成单选按钮，无需XML硬编码）
     * @param context  上下文
     * @param radioGroup  目标单选组
     * @param onSelect  选择回调（返回用户选中的InstallMethod）
     */
    fun setupInstallMethodRadioGroup(
        context: Context,
        radioGroup: RadioGroup,
        onSelect: (InstallMethod) -> Unit
    ) {
        // 清空原有选项
        radioGroup.removeAllViews()

        // 遍历InstallMethod枚举，生成单选按钮
        InstallMethod.values().forEachIndexed { index, method ->
            val radioButton = RadioButton(context).apply {
                text = method.displayName // 显示名称（如：ISOFIX + Top-tether）
                tag = method // 绑定枚举值，便于后续获取
                // 设置默认选中第一个选项
                isChecked = index == 0
            }
            radioGroup.addView(radioButton)
        }

        // 设置选择监听
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadio = radioGroup.findViewById<RadioButton>(checkedId)
            val selectedMethod = selectedRadio.tag as InstallMethod
            onSelect(selectedMethod)
        }
    }

    /**
     * 验证用户输入是否合法（如身高范围格式）
     * @param userInput  用户输入
     * @return  合法返回null，不合法返回错误提示
     */
    fun validateUserInput(userInput: SchemeOptimizer.UserInput): String? {
        // 1. 验证产品类型非空
        if (userInput.productType.isBlank()) {
            return "请选择产品类型（如：儿童安全座椅）"
        }

        // 2. 验证身高范围格式（如：40-150cm）
        val heightPattern = Regex("""^\d{2,3}-\d{2,3}cm$""")
        if (!heightPattern.matches(userInput.heightRange)) {
            return "身高范围格式错误，请输入如：40-150cm"
        }

        // 3. 验证设计主题关键词非空
        if (userInput.themeKeyword.isBlank()) {
            return "请输入设计主题关键词（如：拼图游戏、卡通图案）"
        }

        // 输入合法
        return null
    }
}

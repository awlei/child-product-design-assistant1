package com.childproduct.designassistant.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.childproduct.designassistant.R
import com.childproduct.designassistant.helper.SchemeOptimizer
import com.childproduct.designassistant.helper.UiHelper
import com.childproduct.designassistant.model.InstallMethod

class DesignActivity : AppCompatActivity() {
    // UI控件
    private lateinit var rgInstallMethod: RadioGroup
    private lateinit var etProductType: EditText // 实际项目建议用Spinner选择产品类型
    private lateinit var etHeightRange: EditText
    private lateinit var etThemeKeyword: EditText
    private lateinit var btnGenerate: Button
    private lateinit var tvResult: TextView

    // 选中的安装方式（默认第一个）
    private var selectedInstallMethod: InstallMethod = InstallMethod.ISOFIX

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_design) // 需自行创建对应的XML布局

        // 初始化UI控件
        initViews()

        // 初始化安装方式单选组
        initInstallMethodRadioGroup()

        // 生成按钮点击事件
        setupGenerateButtonListener()
    }

    private fun initViews() {
        rgInstallMethod = findViewById(R.id.rg_install_method)
        etProductType = findViewById(R.id.et_product_type)
        etHeightRange = findViewById(R.id.et_height_range)
        etThemeKeyword = findViewById(R.id.et_theme_keyword)
        btnGenerate = findViewById(R.id.btn_generate)
        tvResult = findViewById(R.id.tv_result)

        // 预设示例输入（提升用户体验）
        etProductType.hint = "如：儿童安全座椅"
        etHeightRange.hint = "如：40-150cm"
        etThemeKeyword.hint = "如：拼图游戏、卡通图案"
    }

    private fun initInstallMethodRadioGroup() {
        // 调用UI辅助类，动态生成安装方式单选按钮
        UiHelper.setupInstallMethodRadioGroup(this, rgInstallMethod) { method ->
            selectedInstallMethod = method
        }
    }

    private fun setupGenerateButtonListener() {
        btnGenerate.setOnClickListener {
            // 1. 获取用户输入
            val userInput = SchemeOptimizer.UserInput(
                productType = etProductType.text.toString().trim(),
                heightRange = etHeightRange.text.toString().trim(),
                installMethod = selectedInstallMethod,
                themeKeyword = etThemeKeyword.text.toString().trim()
            )

            // 2. 验证输入合法性
            val validateError = UiHelper.validateUserInput(userInput)
            if (validateError != null) {
                Toast.makeText(this, validateError, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. 生成优化后的设计方案
            val optimizedScheme = SchemeOptimizer.generateOptimizedScheme(userInput)

            // 4. 格式化方案并展示到UI
            val displayText = SchemeOptimizer.formatSchemeForDisplay(optimizedScheme)
            tvResult.text = displayText
        }
    }
}

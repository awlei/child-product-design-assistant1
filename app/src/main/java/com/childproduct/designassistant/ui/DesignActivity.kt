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
import com.childproduct.designassistant.model.ProductFactory
import com.childproduct.designassistant.validator.ValidatorFactory

/**
 * 设计方案生成页面（优化版）
 *
 * 优化点：
 * - 使用统一验证器进行输入校验
 * - 使用产品工厂创建产品实例
 * - 集成优化后的SchemeOptimizer
 */
class DesignActivity : AppCompatActivity() {
    // UI控件
    private lateinit var rgInstallMethod: RadioGroup
    private lateinit var etProductType: EditText
    private lateinit var etHeightRange: EditText
    private lateinit var etThemeKeyword: EditText
    private lateinit var btnGenerate: Button
    private lateinit var tvResult: TextView

    // 选中的安装方式（默认第一个）
    private var selectedInstallMethod: InstallMethod = InstallMethod.ISOFIX

    // 验证器
    private val productInputValidator = ValidatorFactory.productInputValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_design)

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

        // 支持的产品类型提示
        val supportedProducts = ProductFactory.getSupportedProductTypes()
        etProductType.setOnClickListener {
            Toast.makeText(
                this,
                "支持的产品类型：${supportedProducts.joinToString("、")}",
                Toast.LENGTH_SHORT
            ).show()
        }
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

            // 2. 使用统一验证器验证输入
            val validationResult = productInputValidator.validate(userInput)

            if (!validationResult.isValid) {
                // 验证失败，显示错误信息
                Toast.makeText(
                    this,
                    validationResult.getErrorSummary(3),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // 显示警告信息（如果有）
            if (validationResult.warnings.isNotEmpty()) {
                Toast.makeText(
                    this,
                    validationResult.getWarningSummary(3),
                    Toast.LENGTH_LONG
                ).show()
            }

            try {
                // 3. 创建产品实例
                val product = ProductFactory.createProduct(userInput.productType)

                // 4. 生成优化后的设计方案
                val optimizedScheme = product.generateScheme(userInput)

                // 5. 验证生成的方案
                val schemeValidator = ValidatorFactory.designSchemeValidator()
                val schemeValidation = schemeValidator.validate(optimizedScheme)

                if (!schemeValidation.isValid) {
                    Toast.makeText(
                        this,
                        "方案生成失败：${schemeValidation.getErrorSummary()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                // 6. 格式化方案并展示到UI
                val displayText = SchemeOptimizer.formatSchemeForDisplay(optimizedScheme)
                tvResult.text = displayText

                Toast.makeText(
                    this,
                    "设计方案生成成功！",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: IllegalArgumentException) {
                // 产品类型不支持
                Toast.makeText(
                    this,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                // 其他异常
                Toast.makeText(
                    this,
                    "生成失败：${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

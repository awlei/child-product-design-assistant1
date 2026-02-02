package com.childproduct.designassistant.model.engineering

import com.childproduct.designassistant.common.ValidationResult
import com.childproduct.designassistant.model.InstallDirection
import com.childproduct.designassistant.model.InstallMethod
import com.childproduct.designassistant.model.ProductType
import com.childproduct.designassistant.model.getAntiRotation
import com.childproduct.designassistant.model.getDirection

/**
 * 工程输入数据 - 工程师专用
 * 精准参数捕获，自动验证合规性
 */
data class EngineeringInput(
    // 基本信息
    val productType: ProductType,
    val standards: Set<Standard>,
    
    // 核心参数
    val heightRange: HeightRange,
    val installMethod: InstallMethod?,
    
    // 设计约束（可选）
    val designConstraints: DesignConstraints?
) {
    /**
     * 验证输入合规性
     * @return 验证结果
     */
    fun validate(): ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        
        // 规则1: 40-105cm必须后向安装（ECE R129 §5.1.3）
        if (heightRange.minCm < 105 && installMethod?.getDirection() == InstallDirection.FORWARD) {
            errors.add(
                "ECE R129 §5.1.3: 40-105cm身高范围" +
                "强制要求后向安装（Rearward facing），禁止前向安装"
            )
        }
        
        // 规则2: 105cm以上前向安装必须使用Top-tether（ECE R129 §6.1.2）
        if (heightRange.maxCm >= 105 && 
            installMethod?.getDirection() == InstallDirection.FORWARD &&
            installMethod?.getAntiRotation() != "Top Tether") {
            errors.add(
                "ECE R129 §6.1.2: 105cm以上前向安装" +
                "强制要求使用Top-tether防旋转装置"
            )
        }
        
        // 规则3: 标准兼容性检查
        standards.forEach { standard ->
            if (!standard.supportsProduct(productType)) {
                errors.add(
                    "标准${standard.code}不支持产品类型${productType.displayName}"
                )
            }
        }
        
        // 规则4: 身高范围验证
        if (heightRange.minCm >= heightRange.maxCm) {
            errors.add("最小身高必须小于最大身高")
        }
        
        // 规则5: 身高范围是否在标准范围内
        if (heightRange.minCm < 40) {
            warnings.add("最小身高${heightRange.minCm}cm低于ECE R129标准范围（40cm）")
        }
        if (heightRange.maxCm > 150) {
            warnings.add("最大身高${heightRange.maxCm}cm超过ECE R129标准范围（150cm）")
        }
        
        // 规则6: 检查是否跨越多个假人类型
        val dummyTypes = DummyType.fromHeightRange(
            heightRange.minCm.toDouble(),
            heightRange.maxCm.toDouble()
        )
        if (dummyTypes.size > 2) {
            warnings.add(
                "身高范围跨越${dummyTypes.size}种假人类型" +
                "(${dummyTypes.joinToString(" → ") { it.code }})，可能需要多个测试场景"
            )
        }
        
        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors,
            warnings = warnings
        )
    }

    /**
     * 获取适用的假人类型
     */
    fun getApplicableDummies(): List<DummyType> {
        return DummyType.fromHeightRange(
            heightRange.minCm.toDouble(),
            heightRange.maxCm.toDouble()
        )
    }

    /**
     * 获取安装方向
     */
    fun getInstallDirection(): InstallDirection? {
        return installMethod?.getDirection()
    }
}

/**
 * 身高范围
 */
data class HeightRange(
    val minCm: Int,
    val maxCm: Int
) {
    init {
        require(minCm < maxCm) { "最小身高必须小于最大身高" }
    }

    override fun toString(): String {
        return "${minCm}-${maxCm}cm"
    }
}

/**
 * 设计约束
 */
data class DesignConstraints(
    val envelopeConstraints: EnvelopeConstraints? = null,
    val materialConstraints: MaterialConstraints? = null,
    val weightConstraints: WeightConstraints? = null,
    val customConstraints: Map<String, String> = emptyMap()
)

/**
 * Envelope尺寸约束
 */
data class EnvelopeConstraints(
    val maxWidth: Double?,
    val maxHeight: Double?,
    val maxDepth: Double?,
    val unit: String = "mm"
)

/**
 * 材料约束
 */
data class MaterialConstraints(
    val flameRetardantRequired: Boolean = true,
    val heavyMetalLimits: Map<String, String> = mapOf(
        "Lead" to "≤90 mg/kg",
        "Cadmium" to "≤25 mg/kg"
    ),
    val phthalateLimits: Map<String, String> = mapOf(
        "DEHP" to "≤0.1%",
        "DBP" to "≤0.1%",
        "BBP" to "≤0.1%"
    )
)

/**
 * 重量约束
 */
data class WeightConstraints(
    val maxWeightKg: Double?,
    val minWeightKg: Double?,
    val unit: String = "kg"
)


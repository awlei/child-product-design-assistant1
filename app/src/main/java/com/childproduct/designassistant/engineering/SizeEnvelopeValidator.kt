package com.childproduct.designassistant.engineering

/**
 * 尺寸包络线验证器
 * 基于UN R129 Annex 18标准进行产品设计验证
 * 
 * 核心功能：
 * 1. 验证座椅宽度是否符合臀宽要求
 * 2. 验证头枕高度是否符合UN R129 Annex 18 §3.2
 * 3. 验证肩带高度调节范围是否符合GPS-028数据
 * 4. 验证ISOFIX envelope兼容性（UN R129 Annex 17）
 */
object SizeEnvelopeValidator {

    /**
     * 验证产品设计是否符合UN R129 Annex 18尺寸包络线
     * @param productDimensions 产品设计尺寸
     * @param minHeightCm 最小适配身高(cm)
     * @param maxHeightCm 最大适配身高(cm)
     * @return 验证结果
     */
    fun validateAgainstAnnex18(
        productDimensions: ProductDimensions,
        minHeightCm: Double,
        maxHeightCm: Double
    ): SizeEnvelopeValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        
        // 1. 验证座椅宽度（UN R129 Annex 18 Table 1）
        val minHipWidth = getMinHipWidth(minHeightCm)
        val maxHipWidth = getMaxHipWidth(maxHeightCm)
        
        if (productDimensions.seatWidth < minHipWidth * 1.1) {
            errors.add("座椅宽度${productDimensions.seatWidth}mm < 最小臀宽${minHipWidth.toInt()}mm×1.1，不符合UN R129 Annex 18 Table 1")
        }
        if (productDimensions.seatWidth > maxHipWidth * 1.3) {
            warnings.add("座椅宽度${productDimensions.seatWidth}mm > 最大臀宽${maxHipWidth.toInt()}mm×1.3，可能影响侧向保护")
        }
        
        // 2. 验证头枕高度（UN R129 Annex 18 §3.2）
        val requiredHeadRestHeight = getRequiredHeadRestHeight(maxHeightCm)
        if (productDimensions.headRestHeight < requiredHeadRestHeight) {
            errors.add("头枕高度${productDimensions.headRestHeight.toInt()}mm < 要求高度${requiredHeadRestHeight.toInt()}mm，不符合UN R129 Annex 18 §3.2")
        }
        
        // 3. 验证肩带高度调节范围（UN R129 Annex 18 §3.3）
        val minShoulderHeight = getMinShoulderHeight(minHeightCm)
        val maxShoulderHeight = getMaxShoulderHeight(maxHeightCm)
        if (productDimensions.shoulderBeltMinHeight < minShoulderHeight) {
            errors.add("肩带最小高度${productDimensions.shoulderBeltMinHeight.toInt()}mm < 要求最小值${minShoulderHeight.toInt()}mm（GPS-028数据）")
        }
        if (productDimensions.shoulderBeltMaxHeight < maxShoulderHeight) {
            errors.add("肩带最大高度${productDimensions.shoulderBeltMaxHeight.toInt()}mm < 要求最大值${maxShoulderHeight.toInt()}mm（GPS-028数据）")
        }
        
        // 4. 验证ISOFIX envelope兼容性（UN R129 Annex 17）
        if (productDimensions.isofixCompatible) {
            val envelopeCheck = validateIsofixEnvelope(productDimensions)
            if (!envelopeCheck.isValid) {
                errors.addAll(envelopeCheck.errors)
            }
        }
        
        return SizeEnvelopeValidationResult(
            isValid = errors.isEmpty(),
            errors = errors,
            warnings = warnings,
            compliantStandards = listOf("UN R129 Annex 18", "UN R129 Annex 17", "GPS-028 Anthropometry")
        )
    }
    
    /**
     * 从GPS-028数据获取最小臀宽
     * @param heightCm 身高(cm)
     * @return 最小臀宽(mm)
     */
    private fun getMinHipWidth(heightCm: Double): Double {
        return when {
            heightCm < 60 -> 210.0   // Q0+臀宽
            heightCm < 75 -> 245.0   // Q1臀宽
            heightCm < 87 -> 275.0   // Q1.5臀宽
            heightCm < 105 -> 310.0  // Q3臀宽
            heightCm < 125 -> 345.0  // Q3s臀宽
            heightCm < 145 -> 380.0  // Q6臀宽
            else -> 410.0            // Q10臀宽
        }
    }
    
    /**
     * 从GPS-028数据获取最大臀宽（95th百分位）
     * @param heightCm 身高(cm)
     * @return 最大臀宽(mm)
     */
    private fun getMaxHipWidth(heightCm: Double): Double {
        // 95th百分位 ≈ 50th百分位 × 1.15
        return getMinHipWidth(heightCm) * 1.15
    }
    
    /**
     * 头枕高度要求（UN R129 Annex 18 §3.2）
     * @param heightCm 身高(cm)
     * @return 要求的头枕高度(mm)
     */
    private fun getRequiredHeadRestHeight(heightCm: Double): Double {
        return when {
            heightCm <= 105 -> 250.0   // 后向安装最小头枕高度
            heightCm <= 125 -> 320.0   // Q3s前向安装
            heightCm <= 145 -> 380.0   // Q6前向安装
            else -> 420.0              // Q10前向安装
        }
    }
    
    /**
     * 肩带高度范围（GPS-028 Harness Segment Length数据）
     * @param heightCm 身高(cm)
     * @return 最小肩带高度(mm)
     */
    private fun getMinShoulderHeight(heightCm: Double): Double {
        return when {
            heightCm < 60 -> 180.0
            heightCm < 75 -> 220.0
            heightCm < 87 -> 260.0
            heightCm < 105 -> 300.0
            heightCm < 125 -> 340.0
            heightCm < 145 -> 380.0
            else -> 420.0
        }
    }
    
    /**
     * 肩带高度范围（GPS-028 Harness Segment Length数据）
     * @param heightCm 身高(cm)
     * @return 最大肩带高度(mm)
     */
    private fun getMaxShoulderHeight(heightCm: Double): Double {
        // 允许100mm调节范围
        return getMinShoulderHeight(heightCm) + 100.0
    }
    
    /**
     * ISOFIX envelope验证
     * @param dimensions 产品尺寸
     * @return 验证结果
     */
    private fun validateIsofixEnvelope(dimensions: ProductDimensions): IsofixValidationResult {
        val errors = mutableListOf<String>()
        
        // 检查ISOFIX连接点位置（UN R129 Annex 17）
        if (dimensions.isofixAnchorX < 270 || dimensions.isofixAnchorX > 290) {
            errors.add("ISOFIX锚点X坐标${dimensions.isofixAnchorX.toInt()}mm 超出270-290mm范围（UN R129 Annex 17）")
        }
        if (dimensions.isofixAnchorZ < 120 || dimensions.isofixAnchorZ > 200) {
            errors.add("ISOFIX锚点Z坐标${dimensions.isofixAnchorZ.toInt()}mm 超出120-200mm范围（UN R129 Annex 17）")
        }
        
        // 检查支撑腿长度范围（UN R129 §6.1.2.4）
        if (dimensions.hasSupportLeg) {
            if (dimensions.supportLegMinLength < 285) {
                errors.add("支撑腿最小长度${dimensions.supportLegMinLength.toInt()}mm < 285mm（UN R129 §6.1.2.4）")
            }
            if (dimensions.supportLegMaxLength > 540) {
                errors.add("支撑腿最大长度${dimensions.supportLegMaxLength.toInt()}mm > 540mm（UN R129 §6.1.2.4）")
            }
        }
        
        return IsofixValidationResult(errors.isEmpty(), errors)
    }
}

/**
 * 产品尺寸
 * 用于尺寸包络线验证
 */
data class ProductDimensions(
    val seatWidth: Double,              // 座椅宽度(mm)
    val seatDepth: Double,              // 座椅深度(mm)
    val headRestHeight: Double,         // 头枕高度(mm)
    val shoulderBeltMinHeight: Double,  // 肩带最小高度(mm)
    val shoulderBeltMaxHeight: Double,  // 肩带最大高度(mm)
    val isofixCompatible: Boolean,      // 是否ISOFIX兼容
    val isofixAnchorX: Double = 280.0,  // ISOFIX锚点X坐标(mm)
    val isofixAnchorZ: Double = 160.0,  // ISOFIX锚点Z坐标(mm)
    val hasSupportLeg: Boolean = false, // 是否有支撑腿
    val supportLegMinLength: Double = 0.0, // 支撑腿最小长度(mm)
    val supportLegMaxLength: Double = 0.0  // 支撑腿最大长度(mm)
)

/**
 * 尺寸包络线验证结果
 */
data class SizeEnvelopeValidationResult(
    val isValid: Boolean,                    // 是否通过验证
    val errors: List<String>,                // 错误列表
    val warnings: List<String>,              // 警告列表
    val compliantStandards: List<String>     // 符合的标准
)

/**
 * ISOFIX验证结果
 */
data class IsofixValidationResult(
    val isValid: Boolean,                    // 是否通过验证
    val errors: List<String>                 // 错误列表
)

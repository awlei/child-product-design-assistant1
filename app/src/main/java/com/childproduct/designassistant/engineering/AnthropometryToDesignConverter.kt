package com.childproduct.designassistant.engineering

/**
 * 座椅尺寸参数
 * 基于 GPS-028 设计规则
 */
data class SeatDimensions(
    val minWidth: Double,           // 最小宽度 (mm)
    val idealWidth: Double,         // 理想宽度 (mm)
    val maxWidth: Double,           // 最大宽度 (mm)
    val minDepth: Double,           // 最小深度 (mm)
    val idealDepth: Double,         // 理想深度 (mm)
    val maxDepth: Double,           // 最大深度 (mm)
    val headRestMinHeight: Double,  // 头枕最小高度 (mm)
    val headRestIdealHeight: Double,// 头枕理想高度 (mm)
    val headRestMaxHeight: Double   // 头枕最大高度 (mm)
)

/**
 * 安全带参数
 * 基于 GPS-028 Harness Segment Length
 */
data class HarnessParameters(
    val shoulderBeltMinLength: Double,  // 肩带最小长度 (mm)
    val shoulderBeltMaxLength: Double,  // 肩带最大长度 (mm)
    val crotchBeltLength: Double,       // 裆带长度 (mm)
    val bucklePositionRange: ClosedRange<Double>  // 卡扣位置范围 (mm)
)

/**
 * ISOFIX 参数
 * 基于 UN R129 Annex 17
 */
data class IsofixParameters(
    val anchorSpacingMm: Double,              // 锚点间距 (mm)
    val anchorToleranceMm: Double,            // 锚点容差 (mm)
    val foreAftAdjustmentMm: Double,          // 前后调节范围 (mm)
    val lateralAdjustmentMm: Double,          // 侧向调节范围 (mm)
    val staticStrengthKN: Double,             // 静态强度 (kN)
    val supportLegLengthRange: ClosedRange<Double>?,  // 支撑腿长度范围 (mm)
    val topTetherLengthRange: ClosedRange<Double>?    // 上拉带长度范围 (mm)
)

/**
 * 材料规格
 * 基于 Dorel GPS 规范
 */
data class MaterialSpecifications(
    val shellMaterial: String,        // 外壳材料
    val foamDensity: String,          // 泡沫密度
    val harnessWebbing: String,       // 安全带织带
    val isofixHardware: String       // ISOFIX 硬件
)

/**
 * 人体数据到产品参数转换器
 * 基于GPS-028人体测量学数据生成工程设计参数
 * 
 * 核心功能：
 * 1. 从人体测量学数据提取关键尺寸极值（5th-95th百分位）
 * 2. 生成座椅尺寸参数（基于GPS-028设计规则）
 * 3. 生成安全带参数（基于GPS-028 Harness Segment Length）
 * 4. 生成ISOFIX参数（UN R129 Annex 17）
 * 5. 生成材料规格（基于Dorel GPS规范）
 */
object AnthropometryToDesignConverter {

    /**
     * 基于GPS-028人体测量学数据生成工程设计参数
     * @param minHeightCm 最小身高(cm)
     * @param maxHeightCm 最大身高(cm)
     * @param applicableDummies 适用的假人列表
     * @return 工程设计参数集
     */
    fun convertToEngineeringParameters(
        minHeightCm: Double,
        maxHeightCm: Double,
        applicableDummies: List<String>
    ): EngineeringDesignParameters {
        // 1. 计算关键尺寸极值（基于GPS-028数据）
        val minHipWidth = getHipWidth(minHeightCm)
        val maxHipWidth = getHipWidth(maxHeightCm) * 1.15  // 95th百分位
        val minShoulderWidth = getShoulderWidth(minHeightCm)
        val maxShoulderWidth = getShoulderWidth(maxHeightCm) * 1.15
        val minSittingHeight = getSittingHeight(minHeightCm)
        val maxSittingHeight = getSittingHeight(maxHeightCm)
        
        // 2. 生成座椅尺寸参数（基于GPS-028设计规则）
        val seatDimensions = SeatDimensions(
            minWidth = minHipWidth * 1.1,
            idealWidth = (minHipWidth + maxHipWidth) / 2 * 1.15,
            maxWidth = maxHipWidth * 1.25,
            minDepth = minSittingHeight * 0.35,
            idealDepth = (minSittingHeight + maxSittingHeight) / 2 * 0.4,
            maxDepth = maxSittingHeight * 0.45,
            headRestMinHeight = getHeadRestMinHeight(minHeightCm),
            headRestIdealHeight = getHeadRestMinHeight(minHeightCm) + 60.0,
            headRestMaxHeight = getHeadRestMaxHeight(maxHeightCm)
        )
        
        // 3. 生成安全带参数（基于GPS-028 Harness Segment Length）
        val harnessParams = HarnessParameters(
            shoulderBeltMinLength = getHarnessLength(minHeightCm) * 0.9,
            shoulderBeltMaxLength = getHarnessLength(maxHeightCm) * 1.2,
            crotchBeltLength = getThighWidth(maxHeightCm) * 1.5,
            bucklePositionRange = 80.0..150.0  // GPS-028标准范围
        )
        
        // 4. 生成ISOFIX参数（UN R129 Annex 17）
        val isofixParams = IsofixParameters(
            anchorSpacingMm = 280.0,
            anchorToleranceMm = 10.0,
            foreAftAdjustmentMm = 80.0,
            lateralAdjustmentMm = 200.0,
            staticStrengthKN = 8.0,
            supportLegLengthRange = if (minHeightCm < 105) 285.0..540.0 else null,
            topTetherLengthRange = if (maxHeightCm >= 105) 500.0..700.0 else null
        )
        
        // 5. 生成材料规格（基于Dorel GPS规范）
        val materialSpecs = MaterialSpecifications(
            shellMaterial = "PP+GF30% (抗冲击强度≥20kJ/m²)",
            foamDensity = "30-50kg/m³ (高回弹海绵)",
            harnessWebbing = "聚酯纤维 (断裂强度≥11000N)",
            isofixHardware = "高强度钢 (抗拉强度≥450MPa)"
        )
        
        return EngineeringDesignParameters(
            seatDimensions = seatDimensions,
            harnessParameters = harnessParams,
            isofixParameters = isofixParams,
            materialSpecifications = materialSpecs,
            applicableDummies = applicableDummies,
            standardReferences = listOf(
                "UN R129 Annex 18",
                "UN R129 Annex 17",
                "GPS-028 Anthropometry 11-28-2018"
            )
        )
    }
    
    /**
     * 获取臀宽（GPS-028数据）
     */
    private fun getHipWidth(heightCm: Double): Double {
        return when {
            heightCm < 60 -> 210.0
            heightCm < 75 -> 245.0
            heightCm < 87 -> 275.0
            heightCm < 105 -> 310.0
            heightCm < 125 -> 345.0
            heightCm < 145 -> 380.0
            else -> 410.0
        }
    }
    
    /**
     * 获取肩宽（GPS-028数据）
     */
    private fun getShoulderWidth(heightCm: Double): Double {
        return when {
            heightCm < 60 -> 145.0
            heightCm < 75 -> 165.0
            heightCm < 87 -> 195.0
            heightCm < 105 -> 225.0
            heightCm < 125 -> 260.0
            heightCm < 145 -> 295.0
            else -> 335.0
        }
    }
    
    /**
     * 获取坐高（GPS-028数据）
     */
    private fun getSittingHeight(heightCm: Double): Double {
        return when {
            heightCm < 60 -> 295.0
            heightCm < 75 -> 345.0
            heightCm < 87 -> 385.0
            heightCm < 105 -> 430.0
            heightCm < 125 -> 480.0
            heightCm < 145 -> 520.0
            else -> 560.0
        }
    }
    
    /**
     * 获取安全带长度（GPS-028 Harness Segment Length）
     */
    private fun getHarnessLength(heightCm: Double): Double {
        return when {
            heightCm < 60 -> 350.0
            heightCm < 75 -> 390.0
            heightCm < 87 -> 430.0
            heightCm < 105 -> 470.0
            heightCm < 125 -> 510.0
            heightCm < 145 -> 550.0
            else -> 590.0
        }
    }
    
    /**
     * 获取大腿宽度（GPS-028数据）
     */
    private fun getThighWidth(heightCm: Double): Double {
        return when {
            heightCm < 60 -> 90.0
            heightCm < 75 -> 100.0
            heightCm < 87 -> 110.0
            heightCm < 105 -> 120.0
            heightCm < 125 -> 130.0
            heightCm < 145 -> 140.0
            else -> 150.0
        }
    }
    
    /**
     * 头枕最小高度
     */
    private fun getHeadRestMinHeight(heightCm: Double): Double {
        return when {
            heightCm < 60 -> 200.0
            heightCm < 75 -> 240.0
            heightCm < 87 -> 280.0
            heightCm < 105 -> 320.0
            heightCm < 125 -> 360.0
            heightCm < 145 -> 400.0
            else -> 440.0
        }
    }
    
    /**
     * 头枕最大高度
     */
    private fun getHeadRestMaxHeight(heightCm: Double): Double {
        return getHeadRestMinHeight(heightCm) + 120.0
    }
}

/**
 * 工程设计参数
 * 基于GPS-028人体测量学数据生成
 */
data class EngineeringDesignParameters(
    val seatDimensions: SeatDimensions,
    val harnessParameters: HarnessParameters,
    val isofixParameters: IsofixParameters,
    val materialSpecifications: MaterialSpecifications,
    val applicableDummies: List<String>,
    val standardReferences: List<String>
)

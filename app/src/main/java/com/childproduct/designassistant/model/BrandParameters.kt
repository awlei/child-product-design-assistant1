package com.childproduct.designassistant.model

import com.childproduct.designassistant.model.ProductType

/**
 * 品牌产品参数数据模型
 */
data class BrandParameters(
    val brandName: String,      // 品牌名称，如 Britax, Maxi-Cosi
    val productName: String,    // 产品型号
    val productType: ProductType, // 产品类型
    val specifications: ProductSpec, // 产品规格
    val features: List<ProductFeature>, // 功能特性
    val compliance: ComplianceInfo, // 合规信息
    val imageUrl: String? = null, // 产品图片URL
    val sourceUrl: String? = null // 信息来源链接
)

data class ProductSpec(
    val internalDimensions: InternalDimensions, // 内部尺寸
    val externalDimensions: ExternalDimensions, // 外部尺寸
    val weight: Double,          // 产品重量（kg）
    val weightLimit: WeightLimit, // 重量限制
    val heightLimit: HeightLimit // 身高限制
)

data class InternalDimensions(
    val seatWidth: Double?,      // 座椅宽度（cm）
    val seatDepth: Double?,      // 座椅深度（cm）
    val backrestHeight: Double?, // 靠背高度（cm）
    val headrestWidth: Double?,  // 头托宽度（cm）
    val shoulderWidth: Double?   // 肩宽（cm）
)

data class ExternalDimensions(
    val width: Double,           // 宽度（cm）
    val height: Double,          // 高度（cm）
    val depth: Double            // 深度（cm）
)

data class WeightLimit(
    val min: Double,             // 最小重量（kg）
    val max: Double              // 最大重量（kg）
)

data class HeightLimit(
    val min: Int,                // 最小身高（cm）
    val max: Int                 // 最大身高（cm）
)

data class ProductFeature(
    val name: String,            // 功能名称
    val description: String,     // 功能描述
    val specifications: Map<String, String>? = null // 详细规格（如头托调节范围）
)

data class ComplianceInfo(
    val standards: List<String>, // 符合的标准（如 ECE R129, FMVSS213）
    val envelopeClass: String? = null, // i-Size Envelope 分类
    val certificationNumber: String? = null, // 认证编号
    val certificationDate: String? = null // 认证日期
)

/**
 * 技术问题数据模型
 */
data class TechnicalQuestion(
    val category: QuestionCategory,
    val question: String,
    val context: String? = null
)

enum class QuestionCategory {
    HEADREST_ADJUSTMENT,  // 头托调节
    IMPACT_TESTING,       // 碰撞测试
    INSTALLATION,         // 安装
    MATERIAL_SELECTION,   // 材料选择
    STRUCTURAL_DESIGN,    // 结构设计
    SAFETY_FEATURES,      // 安全特性
    REGULATORY_COMPLIANCE // 法规合规
}

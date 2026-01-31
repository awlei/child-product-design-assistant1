package com.childproduct.designassistant.model

/**
 * 设计建议报告
 */
data class DesignSuggestionReport(
    val id: String,
    val timestamp: Long,
    val productType: ProductType,
    val standard: InternationalStandard,
    val userInput: ProductInput,
    val designSuggestions: DesignSuggestions,
    val brandComparison: BrandComparison,
    val dvpTestMatrix: DVPTestMatrix,
    val standardCompliance: StandardCompliance
)

/**
 * 设计建议
 */
data class DesignSuggestions(
    val productType: ProductType,
    val functionalFeatures: List<FunctionalFeature>,
    val dimensionParameters: DimensionParameters,
    val materialRecommendations: List<MaterialRecommendation>,
    val safetyRecommendations: List<SafetyRecommendation>
)

/**
 * 功能特征建议
 */
data class FunctionalFeature(
    val category: String,
    val description: String,
    val recommendation: String,
    val referenceStandard: String?,
    val implementationDifficulty: DifficultyLevel
)

/**
 * 尺寸参数建议
 */
data class DimensionParameters(
    val externalDimensions: ExternalDimensionSuggestion,
    val internalDimensions: InternalDimensionSuggestion,
    val adjustmentRanges: List<AdjustmentRange>,
    val envelopeCompliance: EnvelopeCompliance?
)

/**
 * 外部尺寸建议
 */
data class ExternalDimensionSuggestion(
    val width: DimensionRecommendation,
    val length: DimensionRecommendation,
    val height: DimensionRecommendation,
    val weight: DimensionRecommendation,
    val notes: String?
)

/**
 * 内部尺寸建议
 */
data class InternalDimensionSuggestion(
    val seatDepth: DimensionRecommendation,
    val seatWidth: DimensionRecommendation,
    val backrestHeight: DimensionRecommendation,
    val headrestWidth: DimensionRecommendation,
    val shoulderWidth: DimensionRecommendation,
    val notes: String?
)

/**
 * 尺寸建议
 */
data class DimensionRecommendation(
    val recommendedValue: DoubleRange,
    val unit: String,
    val rationale: String,
    val standardReference: String?
)

/**
 * 调节范围
 */
data class AdjustmentRange(
    val component: String,
    val minPosition: Double,
    val maxPosition: Double,
    val unit: String,
    val adjustmentSteps: Int,
    val recommendation: String
)

/**
 * 包络尺寸合规性
 */
data class EnvelopeCompliance(
    val isCompliant: Boolean,
    val widthComparison: ComparisonResult,
    val lengthComparison: ComparisonResult,
    val heightComparison: ComparisonResult,
    val recommendations: List<String>
)

/**
 * 对比结果
 */
data class ComparisonResult(
    val actualValue: Double,
    val limitValue: Double,
    val isCompliant: Boolean,
    val difference: Double,
    val unit: String
)

/**
 * 材料建议
 */
data class MaterialRecommendation(
    val component: String,
    val recommendedMaterial: String,
    val properties: List<String>,
    val safetyRating: SafetyRating,
    val supplierSuggestions: List<String>
)

/**
 * 安全建议
 */
data class SafetyRecommendation(
    val aspect: String,
    val description: String,
    val implementation: String,
    val testingRequirement: String?,
    val priority: Priority
)

/**
 * 品牌对比
 */
data class BrandComparison(
    val targetProductType: ProductType,
    val comparedBrands: List<BrandBenchmark>,
    val summaryAnalysis: String,
    var differentiatingSuggestions: List<String>,
    val averageSpecs: AverageSpecs? = null
)

/**
 * 品牌基准
 */
data class BrandBenchmark(
    val brandName: String,
    val productName: String,
    val keyAdvantages: List<String>,
    val technicalSpecs: TechnicalSpecs,
    val marketPosition: String
)

/**
 * 技术规格
 */
data class TechnicalSpecs(
    val dimensions: ProductDimensions,
    val weight: Double,
    val materials: List<String>,
    val certifications: List<String>,
    val uniqueFeatures: List<String>
)

/**
 * 产品尺寸
 */
data class ProductDimensions(
    val width: Double,
    val height: Double,
    val depth: Double,
    val seatWidth: Double,
    val seatDepth: Double
)

/**
 * DVP测试矩阵
 */
data class DVPTestMatrix(
    val productType: ProductType,
    val standard: InternationalStandard,
    val testItems: List<DVPTestItem>
)

/**
 * DVP测试项
 */
data class DVPTestItem(
    val testId: String,
    val testCategory: TestCategory,
    val testName: String,
    val standardReference: String,
    val testMethod: String,
    val acceptanceCriteria: String,
    val testEquipment: String?,
    val sampleSize: Int,
    val estimatedDuration: String,
    val priority: TestPriority,
    val notes: String?
)

/**
 * 测试类别
 */
enum class TestCategory(val displayName: String) {
    IMPACT_TESTING("碰撞测试"),
    DURABILITY_TESTING("耐久性测试"),
    MATERIAL_TESTING("材料测试"),
    CHEMICAL_TESTING("化学测试"),
    FUNCTIONAL_TESTING("功能性测试"),
    SAFETY_TESTING("安全性测试"),
    ERGONOMICS_TESTING("人体工学测试")
}

/**
 * 测试优先级
 */
enum class TestPriority(val displayName: String, val order: Int) {
    MANDATORY("强制测试", 1),
    CRITICAL("关键测试", 2),
    IMPORTANT("重要测试", 3),
    RECOMMENDED("推荐测试", 4)
}

/**
 * 标准合规性
 */
data class StandardCompliance(
    val standard: InternationalStandard,
    val complianceItems: List<ComplianceItem>,
    val overallCompliance: ComplianceStatus,
    val recommendations: List<String>
)

/**
 * 合规项
 */
data class ComplianceItem(
    val requirementId: String,
    val requirement: String,
    val isCompliant: Boolean,
    val gapAnalysis: String?,
    val remediationPlan: String?
)

/**
 * 合规状态
 */
enum class ComplianceStatus(val displayName: String) {
    FULLY_COMPLIANT("完全合规"),
    PARTIALLY_COMPLIANT("部分合规"),
    NON_COMPLIANT("不合规"),
    PENDING_REVIEW("待审核"),
    NOT_APPLICABLE("不适用")
}

/**
 * 难度等级
 */
enum class DifficultyLevel(val displayName: String) {
    EASY("简单"),
    MODERATE("中等"),
    DIFFICULT("困难"),
    VERY_DIFFICULT("非常困难")
}

/**
 * 安全评级
 */
enum class SafetyRating(val displayName: String, val score: Int) {
    EXCELLENT("优秀", 5),
    GOOD("良好", 4),
    SATISFACTORY("满意", 3),
    FAIR("一般", 2),
    POOR("较差", 1)
}

/**
 * 优先级
 */
enum class Priority(val displayName: String, val value: Int) {
    CRITICAL("关键", 1),
    HIGH("高", 2),
    MEDIUM("中", 3),
    LOW("低", 4)
}

/**
 * AI生成请求
 */
data class AIGenerationRequest(
    val productType: ProductType,
    val standard: InternationalStandard,
    val userInput: ProductInput,
    val brandData: List<BrandBenchmark>?,
    val standardData: StandardRequirements?,
    val options: GenerationOptions
)

/**
 * 生成选项
 */
data class GenerationOptions(
    val includeBrandComparison: Boolean = true,
    val includeDVPMatrix: Boolean = true,
    val includeComplianceCheck: Boolean = true,
    val detailLevel: DetailLevel = DetailLevel.STANDARD
)

/**
 * 详情级别
 */
enum class DetailLevel(val displayName: String) {
    BRIEF("简要"),
    STANDARD("标准"),
    DETAILED("详细"),
    COMPREHENSIVE("全面")
}

/**
 * AI生成响应
 */
data class AIGenerationResponse(
    val success: Boolean,
    val designSuggestions: DesignSuggestions?,
    val brandComparison: BrandComparison?,
    val dvpTestMatrix: DVPTestMatrix?,
    val standardCompliance: StandardCompliance?,
    val tokensUsed: Int,
    val modelUsed: String,
    val error: String?
)

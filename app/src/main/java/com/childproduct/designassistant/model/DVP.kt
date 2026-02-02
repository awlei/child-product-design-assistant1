package com.childproduct.designassistant.model

import com.childproduct.designassistant.model.engineering.ProductType

/**
 * 设计验证计划（DVP - Design Validation Plan）数据模型
 */
data class DVP(
    val id: String,
    val projectName: String,
    val productType: ProductType,
    val targetMarket: List<StandardRegion>,
    val version: String,
    val createdDate: String,
    val matrix: List<TestCase>,
    val summary: DVPSummary
)

/**
 * 测试用例
 */
data class TestCase(
    val id: String,
    val category: DVPTestCategory,
    val testItem: String,           // 测试项目
    val testMethod: String,         // 测试方法
    val acceptanceCriteria: String, // 接受标准
    val testSpec: TestSpecification, // 测试规格
    val responsibility: String,     // 负责部门
    val priority: DVPPriority,         // 优先级
    val status: TestStatus = TestStatus.NOT_STARTED
)

data class TestSpecification(
    val testCondition: String,      // 测试条件
    val sampleSize: Int,            // 样本数量
    val testDuration: String? = null, // 测试时长
    val equipment: String? = null   // 所需设备
)

enum class DVPTestCategory(val displayName: String) {
    IMPACT_TESTING("碰撞测试"),
    DURABILITY_TESTING("耐久性测试"),
    MATERIAL_TESTING("材料测试"),
    FUNCTIONAL_TESTING("功能测试"),
    INSTALLATION_TESTING("安装测试"),
    ENVIRONMENTAL_TESTING("环境测试"),
    CHEMICAL_TESTING("化学测试"),
    ELECTRICAL_TESTING("电气测试")
}

enum class DVPPriority(val level: Int) {
    CRITICAL(1),    // 关键（必须通过）
    HIGH(2),        // 高优先级
    MEDIUM(3),      // 中优先级
    LOW(4)          // 低优先级
}

enum class TestStatus {
    NOT_STARTED,
    IN_PROGRESS,
    PASSED,
    FAILED,
    BLOCKED
}

/**
 * DVP 摘要
 */
data class DVPSummary(
    val totalTests: Int,
    val criticalTests: Int,
    val estimatedTimeline: String,  // 预计时间
    val resourceRequirements: List<String>, // 资源需求
    val keyRisks: List<String>      // 关键风险
)

/**
 * 技术建议数据模型
 */
data class TechnicalRecommendation(
    val id: String,
    val inputParameters: InputParameters,
    val matchedStandards: List<StandardMatch>,
    val brandComparison: BrandComparison,
    val suggestedSpecifications: SuggestedSpecifications,
    val dvp: DVP,
    val additionalNotes: List<String>
)

data class InputParameters(
    val heightRange: String,
    val weightRange: String,
    val productType: ProductType,
    val technicalQuestion: TechnicalQuestion
)

data class StandardMatch(
    val standard: TechnicalStandard,
    val matchingGroup: StandardGroup,
    val matchScore: Double,        // 匹配分数 0-1
    val notes: String
)

data class AverageSpecs(
    val avgInternalWidth: Double,
    val avgInternalDepth: Double,
    val avgWeight: Double,
    val commonFeatures: List<String>
)

data class SuggestedSpecifications(
    val internalDimensions: InternalDimensions,
    val externalDimensions: ExternalDimensions,
    val weight: Double,
    val features: List<ProductFeature>,
    val recommendedStandards: List<String>
)

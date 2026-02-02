package com.childproduct.designassistant.model

import com.childproduct.designassistant.model.engineering.ProductType

/**
 * 增强版设计建议报告
 * 每个参数都标注法规依据，融入品牌参数对比
 */
data class EnhancedDesignSuggestionReport(
    val id: String,
    val timestamp: Long,
    val productType: ProductType,
    val standard: InternationalStandard,
    val userInput: ProductInput,
    val dimensionParameters: EnhancedDimensionParameters,
    val functionalParameters: EnhancedFunctionalParameters,
    val complianceTestMatrix: ComplianceTestMatrix,
    val brandParameterComparison: BrandParameterComparison,
    val standardReferences: List<StandardReference>
)

/**
 * 增强版尺寸参数
 * 每个尺寸都标注法规依据
 */
data class EnhancedDimensionParameters(
    val externalDimensions: ExternalDimensionsWithReferences,
    val internalDimensions: InternalDimensionsWithReferences,
    val envelopeCompliance: EnvelopeComplianceDetail
)

/**
 * 外部尺寸（带法规引用）
 */
data class ExternalDimensionsWithReferences(
    val width: DimensionWithReference,
    val length: DimensionWithReference,
    val height: DimensionWithReference,
    val baseThickness: DimensionWithReference,
    val notes: String
)

/**
 * 内部尺寸（带法规引用）
 */
data class InternalDimensionsWithReferences(
    val seatDepth: DimensionWithReference,
    val seatWidth: DimensionWithReference,
    val backrestHeight: DimensionWithReference,
    val headrestWidth: DimensionWithReference,
    val shoulderWidth: DimensionWithReference,
    val headrestInternalWidth: DimensionWithReference,
    val notes: String
)

/**
 * 尺寸带法规引用
 */
data class DimensionWithReference(
    val recommendedRange: DoubleRange,
    val unit: String,
    val tolerance: Double,  // 公差
    val standardReference: String,  // 法规引用，如"ECE R129 Annex 7"
    val rationale: String,  // 设计理由
    val dummyBasis: String? = null  // 假人数据依据
)

/**
 * 增强版功能参数
 * 每个功能都标注法规依据和品牌参考
 */
data class EnhancedFunctionalParameters(
    val headrestAdjustment: FunctionalParameterWithReference,
    val backrestAdjustment: FunctionalParameterWithReference,
    val safetyFeatures: List<SafetyFeatureWithReference>,
    val adjustmentMechanisms: List<AdjustmentMechanismWithReference>
)

/**
 * 功能参数（带法规引用）
 */
data class FunctionalParameterWithReference(
    val componentName: String,
    val adjustmentRange: DoubleRange,
    val adjustmentSteps: Int,
    val unit: String,
    val adjustmentType: String,  // 调节方式
    val standardReference: String,  // 法规引用，如"ECE R129 §5.4.2"
    val rationale: String,  // 设计理由
    val brandReference: BrandParameterReference,  // 品牌参考
    val minMaxReasoning: String  // 最小/最大值依据
)

/**
 * 品牌参数参考
 */
data class BrandParameterReference(
    val brand: String,
    val model: String,
    val value: String,
    val feature: String,
    val differentiation: String  // 差异化建议
)

/**
 * 安全功能（带法规引用）
 */
data class SafetyFeatureWithReference(
    val featureName: String,
    val description: String,
    val implementation: String,
    val standardReference: String,  // 法规引用
    val brandReferences: List<BrandParameterReference>,
    val priority: Priority
)

/**
 * 调节机制（带法规引用）
 */
data class AdjustmentMechanismWithReference(
    val mechanismName: String,
    val description: String,
    val operation: String,
    val standardReference: String,  // 法规引用
    val brandReferences: List<BrandParameterReference>,
    val reliabilityRequirement: String
)

/**
 * 包络尺寸合规性详情
 */
data class EnvelopeComplianceDetail(
    val isCompliant: Boolean,
    val iSizeEnvelope: EnvelopeSpecifications,
    val actualDimensions: EnvelopeSpecifications,
    val complianceDetails: Map<String, ComplianceResult>,
    val recommendations: List<String>
)

/**
 * i-Size包络规格
 */
data class EnvelopeSpecifications(
    val width: Double,
    val length: Double,
    val height: Double,
    val description: String
)

/**
 * 合规结果
 */
data class ComplianceResult(
    val parameter: String,
    val actualValue: Double,
    val limitValue: Double,
    val isCompliant: Boolean,
    val difference: Double,
    val unit: String,
    val actionRequired: String?
)

/**
 * 合规测试矩阵
 * 包含测试项、法规引用、测试方法、合格标准
 */
data class ComplianceTestMatrix(
    val productType: ProductType,
    val targetMarkets: List<TargetMarket>,  // 目标销售地区
    val testItems: List<TestItemWithFullDetails>
)

/**
 * 目标市场
 */
data class TargetMarket(
    val region: String,  // 地区，如"欧盟"、"美国"
    val countryCode: String,  // 国家代码，如"EU"、"US"
    val standard: InternationalStandard,  // 适用标准
    val isSelected: Boolean  // 是否已选择
)

/**
 * 测试项（完整详情）
 */
data class TestItemWithFullDetails(
    val testId: String,
    val testCategory: TestCategory,
    val testName: String,
    val standardReference: String,  // 法规引用，如"ECE R129 §5.3.2"
    val standardSection: String,  // 法规章节，如"§5.3.2"
    val standardUrl: String?,  // 法规原文URL
    val testMethod: String,  // 测试方法
    val testMethodDetails: String?,  // 详细测试方法
    val acceptanceCriteria: String,  // 合格标准
    val criteriaDetails: String?,  // 详细合格标准
    val testEquipment: String?,  // 测试设备
    val sampleSize: Int,  // 样本量
    val estimatedDuration: String,  // 预计时长
    val priority: TestPriority,  // 测试优先级
    val required: Boolean,  // 是否必须测试
    val notes: String?  // 备注
)

/**
 * 品牌参数对比
 * 融入知名品牌参数，对比说明设计优势
 */
data class BrandParameterComparison(
    val targetProductType: ProductType,
    val comparedBrands: List<BrandDetailedComparison>,
    val summaryAnalysis: String,
    val differentiatingSuggestions: List<String>
)

/**
 * 品牌详细对比
 */
data class BrandDetailedComparison(
    val brandName: String,
    val productName: String,
    val marketPosition: String,  // 市场定位：高端/中高端/性价比
    val dimensions: BrandDimensionComparison,
    val weight: BrandWeightComparison,
    val functionalFeatures: BrandFeatureComparison,
    val keyAdvantages: List<String>,
    val recommendedAdoption: List<String>  // 建议采纳的技术
)

/**
 * 品牌尺寸对比
 */
data class BrandDimensionComparison(
    val width: Double,
    val length: Double,
    val height: Double,
    val seatWidth: Double,
    val seatDepth: Double,
    val unit: String
)

/**
 * 品牌重量对比
 */
data class BrandWeightComparison(
    val weight: Double,
    val unit: String,
    val isLightweight: Boolean  // 是否轻量化
)

/**
 * 品牌功能对比
 */
data class BrandFeatureComparison(
    val headrestAdjustment: String,
    val backrestAdjustment: String,
    val uniqueFeatures: List<String>,
    val safetyTechnologies: List<String>
)

/**
 * 法规引用详情
 */
data class StandardReference(
    val standardCode: String,  // 标准编号，如"ECE R129"
    val standardName: String,  // 标准名称
    val region: String,  // 地区，如"欧盟"
    val version: String,  // 版本，如"2023修订版"
    val sections: List<StandardSection>,  // 相关章节
    val url: String?,  // 标准原文URL
    val updateDate: String  // 更新日期
)

/**
 * 法规章节
 */
data class StandardSection(
    val sectionId: String,  // 章节ID，如"§5.3.2"
    val sectionTitle: String,  // 章节标题
    val sectionContent: String,  // 章节内容摘要
    val isMandatory: Boolean  // 是否强制要求
)

/**
 * 知名品牌数据库
 * Britax、Maxi-Cosi、UPPAbaby等头部品牌参数
 */
object BrandDatabase {

    /**
     * 儿童安全座椅品牌
     */
    val safetySeatBrands = listOf(
        BrandDetailedComparison(
            brandName = "Britax",
            productName = "Dualfix M i-Size",
            marketPosition = "高端",
            dimensions = BrandDimensionComparison(
                width = 44.0,
                length = 57.0,
                height = 64.0,
                seatWidth = 34.0,
                seatDepth = 40.0,
                unit = "cm"
            ),
            weight = BrandWeightComparison(
                weight = 13.5,
                unit = "kg",
                isLightweight = false
            ),
            functionalFeatures = BrandFeatureComparison(
                headrestAdjustment = "15档调节（10-30cm）",
                backrestAdjustment = "9档调节（95°-125°）",
                uniqueFeatures = listOf("SafeCell吸能技术", "360°旋转"),
                safetyTechnologies = listOf("SafeCell", "V型头托")
            ),
            keyAdvantages = listOf(
                "SafeCell吸能技术，碰撞时下沉底座减少受力",
                "360°旋转，方便上下车",
                "15档头托调节，提供精细调节",
                "侧面防撞块（SICT）可调节"
            ),
            recommendedAdoption = listOf(
                "采用SafeCell吸能技术（底座设计）",
                "参考15档头托调节标准"
            )
        ),
        BrandDetailedComparison(
            brandName = "Maxi-Cosi",
            productName = "Pebble 360",
            marketPosition = "中高端",
            dimensions = BrandDimensionComparison(
                width = 44.0,
                length = 65.0,
                height = 60.0,
                seatWidth = 33.0,
                seatDepth = 39.0,
                unit = "cm"
            ),
            weight = BrandWeightComparison(
                weight = 12.5,
                unit = "kg",
                isLightweight = false
            ),
            functionalFeatures = BrandFeatureComparison(
                headrestAdjustment = "12档调节（10-25cm）",
                backrestAdjustment = "3档调节（100°-125°）",
                uniqueFeatures = listOf("FamilyFix 360底座", "Clash-free设计"),
                safetyTechnologies = listOf("防撞侧面支撑", "舒适抱垫")
            ),
            keyAdvantages = listOf(
                "FamilyFix 360底座，360°旋转",
                "Clash-free设计，避免前排座椅冲突",
                "防撞侧面支撑，增强侧面防护",
                "舒适抱垫，提升乘坐舒适度"
            ),
            recommendedAdoption = listOf(
                "参考FamilyFix 360底座（旋转功能）",
                "学习Clash-free设计（空间优化）"
            )
        ),
        BrandDetailedComparison(
            brandName = "Cybex",
            productName = "Cloud Z",
            marketPosition = "高端",
            dimensions = BrandDimensionComparison(
                width = 43.0,
                length = 66.0,
                height = 59.0,
                seatWidth = 32.0,
                seatDepth = 38.0,
                unit = "cm"
            ),
            weight = BrandWeightComparison(
                weight = 12.0,
                unit = "kg",
                isLightweight = false
            ),
            functionalFeatures = BrandFeatureComparison(
                headrestAdjustment = "11档调节（10-24cm）",
                backrestAdjustment = "5档调节（100°-130°）",
                uniqueFeatures = listOf("L.S.P.侧面防护", "SensorSafe智能监测"),
                safetyTechnologies = listOf("L.S.P.系统", "SensorSafe")
            ),
            keyAdvantages = listOf(
                "L.S.P.侧面防护系统，多级侧面保护",
                "SensorSafe智能监测，提醒遗忘儿童",
                "平躺式设计，适合新生儿",
                "可旋转，方便上下车"
            ),
            recommendedAdoption = listOf(
                "集成L.S.P.侧面防护系统",
                "考虑SensorSafe智能监测功能"
            )
        ),
        BrandDetailedComparison(
            brandName = "UPPAbaby",
            productName = "Mesa",
            marketPosition = "中高端",
            dimensions = BrandDimensionComparison(
                width = 44.0,
                length = 64.0,
                height = 66.0,
                seatWidth = 34.0,
                seatDepth = 41.0,
                unit = "cm"
            ),
            weight = BrandWeightComparison(
                weight = 11.5,
                unit = "kg",
                isLightweight = true
            ),
            functionalFeatures = BrandFeatureComparison(
                headrestAdjustment = "10档调节（10-22cm）",
                backrestAdjustment = "4档调节（100°-120°）",
                uniqueFeatures = listOf("轻量化设计", "智能折叠"),
                safetyTechnologies = listOf("SMARTSecure系统", "侧撞防护")
            ),
            keyAdvantages = listOf(
                "轻量化设计（11.5kg）",
                "SMARTSecure系统，安装状态指示",
                "智能折叠，快速收纳",
                "侧撞防护，增强安全性能"
            ),
            recommendedAdoption = listOf(
                "学习UPPAbaby的轻量化设计",
                "参考SMARTSecure系统（安装指示）"
            )
        )
    )

    /**
     * 婴儿推车品牌
     */
    val strollerBrands = listOf(
        BrandDetailedComparison(
            brandName = "UPPAbaby",
            productName = "Cruz V2",
            marketPosition = "高端",
            dimensions = BrandDimensionComparison(
                width = 59.0,
                length = 101.0,
                height = 98.0,
                seatWidth = 35.0,
                seatDepth = 42.0,
                unit = "cm"
            ),
            weight = BrandWeightComparison(
                weight = 9.2,
                unit = "kg",
                isLightweight = true
            ),
            functionalFeatures = BrandFeatureComparison(
                headrestAdjustment = "可调节头托（15-25cm）",
                backrestAdjustment = "3档调节（110°-145°）",
                uniqueFeatures = listOf("一键折叠", "前轮避震"),
                safetyTechnologies = listOf("五点式安全带", "驻车制动")
            ),
            keyAdvantages = listOf(
                "一键折叠，单手操作",
                "前轮避震，适应多种路面",
                "超大储物篮，容量大",
                "可适配多种安全座椅"
            ),
            recommendedAdoption = listOf(
                "参考一键折叠设计",
                "学习前轮避震结构"
            )
        ),
        BrandDetailedComparison(
            brandName = "Baby Jogger",
            productName = "City Mini GT2",
            marketPosition = "中高端",
            dimensions = BrandDimensionComparison(
                width = 64.0,
                length = 104.0,
                height = 92.0,
                seatWidth = 36.0,
                seatDepth = 44.0,
                unit = "cm"
            ),
            weight = BrandWeightComparison(
                weight = 11.4,
                unit = "kg",
                isLightweight = false
            ),
            functionalFeatures = BrandFeatureComparison(
                headrestAdjustment = "可调节头托（15-30cm）",
                backrestAdjustment = "3档调节（105°-140°）",
                uniqueFeatures = listOf("专利折叠", "全尺寸轮"),
                safetyTechnologies = listOf("五点式安全带", "手刹")
            ),
            keyAdvantages = listOf(
                "专利折叠，单秒折叠",
                "全尺寸轮，越野性能强",
                "座椅可调节，适配多种场景",
                "五点式安全带，安全性高"
            ),
            recommendedAdoption = listOf(
                "参考专利折叠机构",
                "学习全尺寸轮设计"
            )
        ),
        BrandDetailedComparison(
            brandName = "Cybex",
            productName = "Mios",
            marketPosition = "高端",
            dimensions = BrandDimensionComparison(
                width = 55.0,
                length = 95.0,
                height = 85.0,
                seatWidth = 33.0,
                seatDepth = 40.0,
                unit = "cm"
            ),
            weight = BrandWeightComparison(
                weight = 8.9,
                unit = "kg",
                isLightweight = true
            ),
            functionalFeatures = BrandFeatureComparison(
                headrestAdjustment = "可调节头托（15-25cm）",
                backrestAdjustment = "3档调节（110°-130°）",
                uniqueFeatures = listOf("轻量化", "时尚设计"),
                safetyTechnologies = listOf("五点式安全带", "驻车制动")
            ),
            keyAdvantages = listOf(
                "轻量化设计（8.9kg）",
                "时尚设计，颜值高",
                "一键折叠，操作便捷",
                "舒适座椅，乘坐体验好"
            ),
            recommendedAdoption = listOf(
                "学习轻量化设计",
                "参考时尚设计风格"
            )
        ),
        BrandDetailedComparison(
            brandName = "Bugaboo",
            productName = "Fox 5",
            marketPosition = "高端",
            dimensions = BrandDimensionComparison(
                width = 60.0,
                length = 98.0,
                height = 105.0,
                seatWidth = 37.0,
                seatDepth = 45.0,
                unit = "cm"
            ),
            weight = BrandWeightComparison(
                weight = 9.7,
                unit = "kg",
                isLightweight = true
            ),
            functionalFeatures = BrandFeatureComparison(
                headrestAdjustment = "可调节头托（15-28cm）",
                backrestAdjustment = "3档调节（100°-135°）",
                uniqueFeatures = listOf("多功能模块", "大轮避震"),
                safetyTechnologies = listOf("五点式安全带", "驻车制动")
            ),
            keyAdvantages = listOf(
                "多功能模块，适配多种场景",
                "大轮避震，越野性能强",
                "舒适性高，适合长时间使用",
                "品牌溢价，市场认可度高"
            ),
            recommendedAdoption = listOf(
                "学习多功能模块设计",
                "参考大轮避震结构"
            )
        )
    )

    /**
     * 根据产品类型获取品牌数据
     */
    fun getBrandsByProductType(productType: ProductType): List<BrandDetailedComparison> {
        return when (productType) {
            ProductType.SAFETY_SEAT -> safetySeatBrands
            ProductType.STROLLER -> strollerBrands
            else -> emptyList()
        }
    }

    /**
     * 获取品牌参数对比表（用于生成Markdown表格）
     */
    fun getBrandComparisonTable(productType: ProductType): String {
        val brands = getBrandsByProductType(productType)

        return buildString {
            if (productType == ProductType.SAFETY_SEAT) {
                appendLine("| 品牌 | 型号 | 宽度 | 长度 | 高度 | 重量 | 核心优势 | 市场定位 |")
                appendLine("|------|------|------|------|------|------|----------|----------|")

                brands.forEach { brand ->
                    appendLine("| ${brand.brandName} | ${brand.productName} | ${brand.dimensions.width}cm | ${brand.dimensions.length}cm | ${brand.dimensions.height}cm | ${brand.weight.weight}kg | ${brand.keyAdvantages.first()} | ${brand.marketPosition} |")
                }
            } else if (productType == ProductType.STROLLER) {
                appendLine("| 品牌 | 型号 | 展开尺寸 | 折叠尺寸 | 重量 | 核心优势 | 市场定位 |")
                appendLine("|------|------|----------|----------|------|----------|----------|")

                brands.forEach { brand ->
                    appendLine("| ${brand.brandName} | ${brand.productName} | ${brand.dimensions.length}×${brand.dimensions.height}×${brand.dimensions.width}cm | 未提供 | ${brand.weight.weight}kg | ${brand.keyAdvantages.first()} | ${brand.marketPosition} |")
                }
            }
        }
    }
}

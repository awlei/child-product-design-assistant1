package com.design.assistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.design.assistant.model.*
import com.design.assistant.repository.ChildSeatStandardRepository
import com.design.assistant.repository.GPS028Repository
import com.design.assistant.repository.ProductStandardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * 设计生成ViewModel
 * 负责生成专业的设计参数和兼容性分析
 */
class DesignGenerateViewModel(
    private val gps028Repository: GPS028Repository,
    private val childSeatStandardRepository: ChildSeatStandardRepository,
    private val productStandardRepository: ProductStandardRepository
) : ViewModel() {

    // UI状态
    private val _uiState = MutableStateFlow(DesignGenerateUiState())
    val uiState: StateFlow<DesignGenerateUiState> = _uiState.asStateFlow()

    init {
        initializeDatabases()
    }

    /**
     * 初始化数据库
     */
    private fun initializeDatabases() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                gps028Repository.initializeData()
                childSeatStandardRepository.initializeData()
                productStandardRepository.initializeData()
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    /**
     * 生成设计结果
     */
    fun generateDesign(
        productType: ProductType,
        standards: List<StandardType>,
        selectedGroup: GPS028Group = GPS028Group.GROUP_I,
        selectedPercentile: Int = 50
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // 生成设计结果
                val designResult = when (productType) {
                    ProductType.CHILD_SEAT -> generateChildSeatDesign(
                        standards, selectedGroup, selectedPercentile
                    )
                    ProductType.STROLLER -> generateStrollerDesign(standards)
                    ProductType.HIGH_CHAIR -> generateHighChairDesign(standards)
                    ProductType.CRIB -> generateCribDesign(standards)
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    designResult = designResult,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    /**
     * 生成儿童安全座椅设计方案
     */
    private suspend fun generateChildSeatDesign(
        standards: List<StandardType>,
        selectedGroup: GPS028Group,
        selectedPercentile: Int
    ): DesignResult {
        // 获取GPS028参数
        val gps028Params = gps028Repository.getByGroupAndPercentile(
            selectedGroup.name,
            "${selectedPercentile}%"
        ) ?: StandardConstants.getGPS028PercentileParams(selectedPercentile, selectedGroup)

        // 获取其他标准的参数
        val otherStandardParams = standards.filter { it != StandardType.GPS028 }.mapNotNull { standard ->
            when (standard) {
                StandardType.ECE_R129 -> childSeatStandardRepository.getByStandardAndDummy(
                    "ECE_R129",
                    when (selectedGroup) {
                        GPS028Group.GROUP_0 -> "Q1"
                        GPS028Group.GROUP_0P -> "Q1"
                        GPS028Group.GROUP_I -> "Q3"
                        GPS028Group.GROUP_II -> "Q6"
                        GPS028Group.GROUP_III -> "Q10"
                    }
                )
                StandardType.CMVSS213 -> childSeatStandardRepository.getByStandardAndDummy(
                    "CMVSS213",
                    "Toddler"
                )
                StandardType.FMVSS213 -> childSeatStandardRepository.getByStandardAndDummy(
                    "FMVSS213",
                    "Toddler"
                )
                else -> null
            }
        }

        // 生成兼容性分析
        val compatibility = analyzeCompatibility(standards)

        // 生成设计建议
        val recommendations = generateChildSeatRecommendations(standards, gps028Params)

        // 生成冲突提示
        val conflicts = detectStandardConflicts(standards)

        return DesignResult(
            id = UUID.randomUUID().toString(),
            productType = ProductType.CHILD_SEAT,
            standards = standards,
            gps028Params = gps028Params,
            compatibility = compatibility,
            designRecommendations = recommendations,
            conflicts = conflicts
        )
    }

    /**
     * 生成婴儿推车设计方案
     */
    private suspend fun generateStrollerDesign(standards: List<StandardType>): DesignResult {
        val compatibility = analyzeCompatibility(standards)
        val recommendations = generateStrollerRecommendations(standards)
        val conflicts = detectStandardConflicts(standards)

        return DesignResult(
            id = UUID.randomUUID().toString(),
            productType = ProductType.STROLLER,
            standards = standards,
            compatibility = compatibility,
            designRecommendations = recommendations,
            conflicts = conflicts
        )
    }

    /**
     * 生成高脚椅设计方案
     */
    private suspend fun generateHighChairDesign(standards: List<StandardType>): DesignResult {
        val compatibility = analyzeCompatibility(standards)
        val recommendations = generateHighChairRecommendations(standards)
        val conflicts = detectStandardConflicts(standards)

        return DesignResult(
            id = UUID.randomUUID().toString(),
            productType = ProductType.HIGH_CHAIR,
            standards = standards,
            compatibility = compatibility,
            designRecommendations = recommendations,
            conflicts = conflicts
        )
    }

    /**
     * 生成儿童床设计方案
     */
    private suspend fun generateCribDesign(standards: List<StandardType>): DesignResult {
        val compatibility = analyzeCompatibility(standards)
        val recommendations = generateCribRecommendations(standards)
        val conflicts = detectStandardConflicts(standards)

        return DesignResult(
            id = UUID.randomUUID().toString(),
            productType = ProductType.CRIB,
            standards = standards,
            compatibility = compatibility,
            designRecommendations = recommendations,
            conflicts = conflicts
        )
    }

    /**
     * 分析兼容性
     */
    private fun analyzeCompatibility(standards: List<StandardType>): CompatibilityAnalysis {
        val issues = mutableListOf<String>()
        var score = 100

        // 检查标准之间的兼容性
        if (standards.size > 1) {
            val regions = standards.map { it.getRegion() }.distinct()
            if (regions.size > 1) {
                issues.add("同时选择了多个地区的标准，可能存在设计冲突")
                score -= 20
            }

            val hasISOFIX = standards.contains(StandardType.ISOFIX_Standard)
            val hasUAS = standards.any { it == StandardType.CMVSS213 || it == StandardType.FMVSS213 }
            if (hasISOFIX && hasUAS) {
                issues.add("ISOFIX和UAS系统需要不同的安装接口，需要统一设计方案")
                score -= 15
            }
        }

        val level = when {
            score >= 90 -> CompatibilityLevel.HIGH
            score >= 70 -> CompatibilityLevel.MEDIUM
            score >= 50 -> CompatibilityLevel.LOW
            else -> CompatibilityLevel.INCOMPATIBLE
        }

        return CompatibilityAnalysis(
            score = score,
            level = level,
            issues = issues,
            compatibleStandards = standards
        )
    }

    /**
     * 生成儿童安全座椅设计建议
     */
    private fun generateChildSeatRecommendations(
        standards: List<StandardType>,
        gps028Params: GPS028Params
    ): List<DesignRecommendation> {
        val recommendations = mutableListOf<DesignRecommendation>()

        // 头部保护建议
        recommendations.add(
            DesignRecommendation(
                category = "头部保护",
                title = "头部支撑高度",
                description = "根据GPS028参数，最小头部支撑高度应为${gps028Params.minHeadSupportHeight}mm，确保在碰撞时头部得到充分保护。",
                priority = RecommendationPriority.CRITICAL,
                applicableStandards = standards
            )
        )

        // 侧翼建议
        recommendations.add(
            DesignRecommendation(
                category = "侧翼设计",
                title = "侧翼深度和宽度",
                description = "侧翼最小深度为${gps028Params.minSideWingDepth}mm，最小宽度为${gps028Params.minSideWingWidth}mm，以减少侧面碰撞伤害。",
                priority = RecommendationPriority.HIGH,
                applicableStandards = standards
            )
        )

        // 安全带建议
        recommendations.add(
            DesignRecommendation(
                category = "安全带系统",
                title = "安全带间距",
                description = "安全带最小间距为${gps028Params.minHarnessWidth}mm，胯部扣最小距离为${gps028Params.minCrotchBuckleDistance}mm。",
                priority = RecommendationPriority.CRITICAL,
                applicableStandards = standards
            )
        )

        // ISOFIX建议
        if (standards.contains(StandardType.ECE_R129) || standards.contains(StandardType.ISOFIX_Standard)) {
            recommendations.add(
                DesignRecommendation(
                    category = "ISOFIX系统",
                    title = "ISOFIX接口设计",
                    description = "ISOFIX杆宽度应为${StandardConstants.ECE_R129.ISOFIX_BAR_WIDTH}mm，公差±${StandardConstants.Tolerances.POSITION_TOLERANCE}mm。",
                    priority = RecommendationPriority.CRITICAL,
                    applicableStandards = standards.filter { it == StandardType.ECE_R129 || it == StandardType.ISOFIX_Standard }
                )
            )
        }

        return recommendations
    }

    /**
     * 生成婴儿推车设计建议
     */
    private fun generateStrollerRecommendations(standards: List<StandardType>): List<DesignRecommendation> {
        val recommendations = mutableListOf<DesignRecommendation>()

        recommendations.add(
            DesignRecommendation(
                category = "结构设计",
                title = "车轮尺寸",
                description = "最小轮径应满足各标准要求，欧洲EN1888为${StandardConstants.StrollerStandards.EN1888_MIN_WHEEL_DIA}mm。",
                priority = RecommendationPriority.HIGH,
                applicableStandards = standards
            )
        )

        recommendations.add(
            DesignRecommendation(
                category = "安全设计",
                title = "刹车系统",
                description = "刹车系统应提供至少${StandardConstants.StrollerStandards.EN1888_MIN_BRAKE_FORCE}N的制动力。",
                priority = RecommendationPriority.CRITICAL,
                applicableStandards = standards
            )
        )

        return recommendations
    }

    /**
     * 生成高脚椅设计建议
     */
    private fun generateHighChairRecommendations(standards: List<StandardType>): List<DesignRecommendation> {
        val recommendations = mutableListOf<DesignRecommendation>()

        recommendations.add(
            DesignRecommendation(
                category = "座椅设计",
                title = "托盘深度",
                description = "托盘最小深度为${StandardConstants.HighChairStandards.EN14988_MIN_TRAY_DEPTH}mm，确保物品不会滑落。",
                priority = RecommendationPriority.HIGH,
                applicableStandards = standards
            )
        )

        recommendations.add(
            DesignRecommendation(
                category = "稳定设计",
                title = "稳定性要求",
                description = "应满足最小稳定角度${StandardConstants.HighChairStandards.CSA_B229_MIN_STABILITY_ANGLE}°的要求。",
                priority = RecommendationPriority.CRITICAL,
                applicableStandards = standards
            )
        )

        return recommendations
    }

    /**
     * 生成儿童床设计建议
     */
    private fun generateCribRecommendations(standards: List<StandardType>): List<DesignRecommendation> {
        val recommendations = mutableListOf<DesignRecommendation>()

        recommendations.add(
            DesignRecommendation(
                category = "护栏设计",
                title = "栏杆间距",
                description = "最大栏杆间距为${StandardConstants.CribStandards.EN716_MAX_BAR_SPACING}mm，防止儿童头部卡住。",
                priority = RecommendationPriority.CRITICAL,
                applicableStandards = standards
            )
        )

        recommendations.add(
            DesignRecommendation(
                category = "床垫设计",
                title = "床垫厚度",
                description = "最小床垫厚度为${StandardConstants.CribStandards.EN716_MIN_MATTRESS_THICKNESS}mm。",
                priority = RecommendationPriority.HIGH,
                applicableStandards = standards
            )
        )

        return recommendations
    }

    /**
     * 检测标准冲突
     */
    private fun detectStandardConflicts(standards: List<StandardType>): List<StandardConflict> {
        val conflicts = mutableListOf<StandardConflict>()

        // 检查ISOFIX vs LATCH冲突
        if (standards.contains(StandardType.ECE_R129) && standards.any {
                it == StandardType.CMVSS213 || it == StandardType.FMVSS213
            }) {
            conflicts.add(
                StandardConflict(
                    standard1 = StandardType.ECE_R129,
                    standard2 = StandardType.CMVSS213,
                    description = "ECE R129使用ISOFIX系统，而CMVSS213使用UAS（LATCH）系统，两者接口不同。",
                    severity = ConflictSeverity.HIGH,
                    resolution = "建议设计双重接口或选择单一地区标准"
                )
            )
        }

        // 检查地区标准冲突
        val regions = standards.map { it.getRegion() }.distinct()
        if (regions.size > 1) {
            conflicts.add(
                StandardConflict(
                    standard1 = standards[0],
                    standard2 = standards[1],
                    description = "同时选择了${regions.joinToString("、")}的标准，可能在测试要求和限值上存在差异。",
                    severity = ConflictSeverity.MEDIUM,
                    resolution = "建议按照最严格的标准进行设计，或分地区生产不同版本"
                )
            )
        }

        return conflicts
    }

    /**
     * 清空设计结果
     */
    fun clearDesignResult() {
        _uiState.value = _uiState.value.copy(designResult = null)
    }

    /**
     * 重置状态
     */
    fun reset() {
        _uiState.value = DesignGenerateUiState()
    }
}

/**
 * 设计生成UI状态
 */
data class DesignGenerateUiState(
    val isLoading: Boolean = false,
    val designResult: DesignResult? = null,
    val error: String? = null
)

package com.childproduct.designassistant.ui.standard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.childproduct.designassistant.data.model.DesignProposalRequest
import com.childproduct.designassistant.constants.StandardConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

/**
 * 标准选择ViewModel
 *
 * 负责管理标准选择的UI状态和业务逻辑
 * 修复：确保选中的标准能正确传递给下游ViewModel
 */
class StandardSelectionViewModel : ViewModel() {

    // UI状态
    private val _uiState = MutableStateFlow(StandardSelectionUiState())
    val uiState: StateFlow<StandardSelectionUiState> = _uiState.asStateFlow()

    // 选中的标准（产品ID -> 标准ID列表）
    private val _selectedStandards = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val selectedStandards: StateFlow<Map<String, List<String>>> = _selectedStandards.asStateFlow()

    // 修复：选中的标准类型（使用StandardConstants常量）
    private val _selectedStandardType = MutableStateFlow<String?>(null)
    val selectedStandardType: StateFlow<String?> = _selectedStandardType.asStateFlow()

    // 生成方案事件
    private val _generateEvent = MutableSharedFlow<Map<String, List<String>>>()
    val generateEvent: SharedFlow<Map<String, List<String>>> = _generateEvent.shareIn(
        viewModelScope,
        SharingStarted.Eagerly
    )

    init {
        loadProductData()
    }

    /**
     * 加载产品数据
     */
    private fun loadProductData() {
        _uiState.value = StandardSelectionUiState(
            travelProducts = TravelProductsData,
            homeProducts = HomeProductsData
        )
    }

    /**
     * 切换分类展开/收起
     */
    fun toggleCategory(productId: String) {
        val current = _uiState.value.expandedCategories
        val newSet = if (productId in current) {
            current - productId
        } else {
            current + productId
        }
        _uiState.value = _uiState.value.copy(expandedCategories = newSet)
    }

    /**
     * 修复：切换标准选中状态（添加日志和常量转换）
     */
    fun toggleStandard(productId: String, standardId: String) {
        val current = _selectedStandards.value
        val currentList = current[productId] ?: emptyList()

        val newList = if (standardId in currentList) {
            currentList - standardId
        } else {
            currentList + standardId
        }

        val newMap = if (newList.isEmpty()) {
            current - productId
        } else {
            current + (productId to newList)
        }

        _selectedStandards.value = newMap

        // 修复：使用StandardConstants转换标准ID，并添加日志
        val allSelectedStandards = newMap.values.flatten()
        val firstStandard = allSelectedStandards.firstOrNull()

        if (firstStandard != null) {
            val standardConstant = StandardConstants.getStandardConstant(firstStandard)
            _selectedStandardType.value = standardConstant
            android.util.Log.d("StandardFlow", "状态更新：选中$firstStandard -> $standardConstant")
        } else {
            _selectedStandardType.value = null
            android.util.Log.d("StandardFlow", "状态更新：清除所有选择")
        }
    }

    /**
     * 全选所有标准
     */
    fun selectAllStandards(productId: String, standardIds: List<String>) {
        val current = _selectedStandards.value
        val newMap = current + (productId to standardIds)
        _selectedStandards.value = newMap
    }

    /**
     * 取消选择所有标准
     */
    fun deselectAllStandards(productId: String) {
        val current = _selectedStandards.value
        val newMap = current - productId
        _selectedStandards.value = newMap
    }

    /**
     * 生成设计方案
     */
    fun generateDesign() {
        viewModelScope.launch {
            _generateEvent.emit(_selectedStandards.value)
        }
    }

    /**
     * 重置选择
     */
    fun resetSelection() {
        _selectedStandards.value = emptyMap()
        _selectedStandardType.value = null
    }

    /**
     * 创建设计方案请求
     */
    fun createDesignRequest(productType: String = "儿童安全座椅"): DesignProposalRequest {
        return DesignProposalRequest(
            productType = productType,
            selectedStandards = _selectedStandards.value,
            additionalRequirements = emptyList()
        )
    }

    /**
     * 获取选中的产品类型列表
     */
    fun getSelectedProductTypes(): List<String> {
        return _selectedStandards.value.keys.toList()
    }

    /**
     * 获取选中的标准列表
     */
    fun getSelectedStandardsCount(): Int {
        return _selectedStandards.value.values.sumOf { it.size }
    }
}

/**
 * UI状态数据类
 */
data class StandardSelectionUiState(
    val travelProducts: List<ProductCategory> = emptyList(),
    val homeProducts: List<ProductCategory> = emptyList(),
    val expandedCategories: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * 出行类产品数据
 */
private val TravelProductsData = listOf(
    ProductCategory(
        id = "car_seat",
        name = "儿童安全座椅",
        icon = "🎫",
        standards = listOf(
            StandardItem(
                id = "ece_r129",
                name = "ECE R129 (欧盟i-Size)",
                description = "欧盟儿童约束系统法规，基于身高分类",
                region = "EU",
                databaseRef = "ECE_R129"
            ),
            StandardItem(
                id = "gb_27887_2024",
                name = "GB 27887-2024 (中国新标)",
                description = "中国机动车儿童乘员用约束系统国家标准",
                region = "CN",
                databaseRef = "GB_27887_2024"
            ),
            StandardItem(
                id = "fmvss_213",
                name = "FMVSS 213 (美国标准)",
                description = "美国联邦机动车安全标准",
                region = "US",
                databaseRef = "FMVSS_213"
            ),
            StandardItem(
                id = "as_nzs_1754",
                name = "AS/NZS 1754 (澳洲标准)",
                description = "澳大利亚/新西兰儿童约束系统标准",
                region = "AU",
                databaseRef = "AS_NZS_1754"
            )
        )
    ),
    ProductCategory(
        id = "stroller",
        name = "婴儿推车",
        icon = "🎫",
        standards = listOf(
            StandardItem(
                id = "en_1888",
                name = "EN 1888 (欧盟标准)",
                description = "欧盟婴儿推车安全要求标准",
                region = "EU",
                databaseRef = "EN_1888"
            ),
            StandardItem(
                id = "gb_14748",
                name = "GB 14748 (中国标准)",
                description = "中国婴儿推车安全要求标准",
                region = "CN",
                databaseRef = "GB_14748"
            ),
            StandardItem(
                id = "astm_f833",
                name = "ASTM F833 (美国标准)",
                description = "美国婴儿推车消费品安全标准",
                region = "US",
                databaseRef = "ASTM_F833"
            )
        )
    )
)

/**
 * 家居类产品数据
 */
private val HomeProductsData = listOf(
    ProductCategory(
        id = "high_chair",
        name = "儿童高脚椅",
        icon = "🎫",
        standards = listOf(
            StandardItem(
                id = "en_14988",
                name = "EN 14988 (欧盟标准)",
                description = "欧盟儿童高脚椅安全要求标准",
                region = "EU",
                databaseRef = "EN_14988"
            ),
            StandardItem(
                id = "gb_29281",
                name = "GB 29281 (中国标准)",
                description = "中国儿童高脚椅安全要求标准",
                region = "CN",
                databaseRef = "GB_29281"
            )
        )
    ),
    ProductCategory(
        id = "crib",
        name = "儿童床",
        icon = "🎫",
        standards = listOf(
            StandardItem(
                id = "en_716",
                name = "EN 716 (欧盟标准)",
                description = "欧盟儿童家具-床安全要求标准",
                region = "EU",
                databaseRef = "EN_716"
            ),
            StandardItem(
                id = "gb_28007",
                name = "GB 28007 (中国标准)",
                description = "中国儿童家具通用技术条件标准",
                region = "CN",
                databaseRef = "GB_28007"
            )
        )
    )
)

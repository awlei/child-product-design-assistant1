package com.design.assistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.design.assistant.model.ProductType
import com.design.assistant.model.StandardType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 产品类型和标准选择ViewModel
 * 管理产品类型和标准的选择状态
 */
class ProductStandardSelectViewModel : ViewModel() {

    // UI状态
    private val _uiState = MutableStateFlow(ProductStandardSelectUiState())
    val uiState: StateFlow<ProductStandardSelectUiState> = _uiState.asStateFlow()

    init {
        loadProductTypes()
    }

    /**
     * 加载产品类型
     */
    private fun loadProductTypes() {
        _uiState.value = _uiState.value.copy(
            productTypes = ProductType.values().toList()
        )
    }

    /**
     * 选择产品类型
     */
    fun selectProductType(productType: ProductType) {
        _uiState.value = _uiState.value.copy(
            selectedProductType = productType,
            availableStandards = productType.getSupportedStandards(),
            selectedStandards = emptyList()  // 清空已选标准
        )
    }

    /**
     * 切换标准选择状态
     */
    fun toggleStandard(standard: StandardType) {
        val currentSelection = _uiState.value.selectedStandards.toMutableList()
        if (currentSelection.contains(standard)) {
            currentSelection.remove(standard)
        } else {
            currentSelection.add(standard)
        }
        _uiState.value = _uiState.value.copy(
            selectedStandards = currentSelection
        )
    }

    /**
     * 选择多个标准
     */
    fun selectStandards(standards: List<StandardType>) {
        _uiState.value = _uiState.value.copy(
            selectedStandards = standards
        )
    }

    /**
     * 清空选择
     */
    fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            selectedProductType = null,
            selectedStandards = emptyList()
        )
    }

    /**
     * 重置到初始状态
     */
    fun reset() {
        _uiState.value = ProductStandardSelectUiState(
            productTypes = ProductType.values().toList()
        )
    }

    /**
     * 检查是否可以继续（是否有选择产品类型和标准）
     */
    fun canProceed(): Boolean {
        return _uiState.value.selectedProductType != null &&
               _uiState.value.selectedStandards.isNotEmpty()
    }
}

/**
 * 产品标准选择UI状态
 */
data class ProductStandardSelectUiState(
    val productTypes: List<ProductType> = emptyList(),
    val selectedProductType: ProductType? = null,
    val availableStandards: List<StandardType> = emptyList(),
    val selectedStandards: List<StandardType> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

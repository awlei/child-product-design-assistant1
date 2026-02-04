package com.childproduct.designassistant.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.childproduct.designassistant.data.model.DesignProposal
import com.childproduct.designassistant.data.model.DesignProposalRequest
import com.childproduct.designassistant.service.DesignProposalGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 设计方案展示ViewModel
 */
class DesignProposalViewModel : ViewModel() {

    private val generator = DesignProposalGenerator()

    // UI状态
    private val _uiState = MutableStateFlow<DesignProposalUiState>(DesignProposalUiState.Idle)
    val uiState: StateFlow<DesignProposalUiState> = _uiState.asStateFlow()

    // 当前设计方案
    private val _currentProposal = MutableStateFlow<DesignProposal?>(null)
    val currentProposal: StateFlow<DesignProposal?> = _currentProposal.asStateFlow()

    /**
     * 生成设计方案
     */
    fun generateProposal(request: DesignProposalRequest) {
        viewModelScope.launch {
            _uiState.value = DesignProposalUiState.Loading

            generator.generateProposal(request)
                .onSuccess { proposal ->
                    _currentProposal.value = proposal
                    _uiState.value = DesignProposalUiState.Success(proposal)
                }
                .onFailure { error ->
                    _uiState.value = DesignProposalUiState.Error(
                        error.message ?: "生成设计方案失败"
                    )
                }
        }
    }

    /**
     * 重置状态
     */
    fun reset() {
        _currentProposal.value = null
        _uiState.value = DesignProposalUiState.Idle
    }
}

/**
 * 设计方案UI状态
 */
sealed class DesignProposalUiState {
    /** 空闲状态 */
    object Idle : DesignProposalUiState()

    /** 加载中 */
    object Loading : DesignProposalUiState()

    /** 成功 */
    data class Success(val proposal: DesignProposal) : DesignProposalUiState()

    /** 错误 */
    data class Error(val message: String) : DesignProposalUiState()
}

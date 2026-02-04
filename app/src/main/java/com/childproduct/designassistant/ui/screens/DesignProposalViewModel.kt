package com.childproduct.designassistant.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.childproduct.designassistant.data.model.DesignProposal
import com.childproduct.designassistant.data.model.DesignProposalRequest
import com.childproduct.designassistant.database.CribDatabase
import com.childproduct.designassistant.database.EceR129Database
import com.childproduct.designassistant.database.HighChairDatabase
import com.childproduct.designassistant.service.ChildRestraintDesignService
import com.childproduct.designassistant.service.DesignProposalGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 设计方案展示ViewModel
 */
class DesignProposalViewModel(application: Application) : AndroidViewModel(application) {

    private val eceR129Database = EceR129Database.getDatabase(application)
    private val highChairDatabase = HighChairDatabase.getDatabase(application)
    private val cribDatabase = CribDatabase.getDatabase(application)
    private val generator = DesignProposalGenerator(eceR129Database, highChairDatabase, cribDatabase)
    private val childRestraintDesignService = ChildRestraintDesignService()

    // UI状态
    private val _uiState = MutableStateFlow<DesignProposalUiState>(DesignProposalUiState.Idle)
    val uiState: StateFlow<DesignProposalUiState> = _uiState.asStateFlow()

    // 当前设计方案
    private val _currentProposal = MutableStateFlow<DesignProposal?>(null)
    val currentProposal: StateFlow<DesignProposal?> = _currentProposal.asStateFlow()

    // Markdown内容（用于PDF导出）
    private val _markdownContent = MutableStateFlow("")
    val markdownContent: StateFlow<String> = _markdownContent.asStateFlow()

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
                    
                    // 生成Markdown内容（如果是儿童安全座椅）
                    if (request.productType == "儿童安全座椅") {
                        val standardList = request.selectedStandards["儿童安全座椅"] ?: emptyList()
                        val selection = ChildRestraintDesignService.StandardSelection(
                            eceR129 = standardList.contains("ECE R129"),
                            gb27887 = standardList.contains("GB 28007-2024"),
                            fmvss213 = standardList.contains("FMVSS 213"),
                            asNzs1754 = standardList.contains("AS/NZS 1754"),
                            jisD1601 = standardList.contains("JIS D 1601")
                        )
                        
                        // 解析身高和体重范围
                        val heightStr = request.userInputDummyInfo?.targetHeightRange ?: "40-150"
                        val weightStr = request.userInputDummyInfo?.targetWeightRange ?: "0-36"
                        
                        val heightRange = heightStr.split("-").map { it.trim().toDoubleOrNull() ?: 0.0 }
                        val weightRange = weightStr.split("-").map { it.trim().toDoubleOrNull() ?: 0.0 }
                        
                        val heightCm = heightRange.maxOrNull() ?: 100.0
                        val weightKg = weightRange.maxOrNull() ?: 15.0
                        
                        val designProposal = childRestraintDesignService.generateDesignProposal(
                            selection = selection,
                            heightCm = heightCm,
                            weightKg = weightKg
                        )
                        
                        _markdownContent.value = childRestraintDesignService.formatAsMarkdown(designProposal)
                    }
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
        _markdownContent.value = ""
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

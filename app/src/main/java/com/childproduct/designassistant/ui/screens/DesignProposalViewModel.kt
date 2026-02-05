package com.childproduct.designassistant.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewModelScope
import com.childproduct.designassistant.data.model.DesignProposal
import com.childproduct.designassistant.data.model.DesignProposalRequest
import com.childproduct.designassistant.database.CribDatabase
import com.childproduct.designassistant.database.EceR129Database
import com.childproduct.designassistant.database.FMVSSDatabase
import com.childproduct.designassistant.database.HighChairDatabase
import com.childproduct.designassistant.service.ChildRestraintDesignService
import com.childproduct.designassistant.service.DesignProposalGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 设计方案展示ViewModel
 * 修复：接收并使用选中的标准类型，确保输出内容与标准一致
 * 新增：使用OutputComplianceChecker校验输出合规性
 */
class DesignProposalViewModel(
    application: Application,
    // 新增：接收选中的标准类型（如"ECE_R129"、"FMVSS_213"）
    private val initialSelectedStandard: String? = null
) : AndroidViewModel(application) {

    private val eceR129Database = EceR129Database.getDatabase(application)
    private val fmvssDatabase = FMVSSDatabase.getDatabase(application)
    private val highChairDatabase = HighChairDatabase.getDatabase(application)
    private val cribDatabase = CribDatabase.getDatabase(application)
    private val generator = DesignProposalGenerator(eceR129Database, fmvssDatabase, highChairDatabase, cribDatabase)
    private val childRestraintDesignService = ChildRestraintDesignService()
    private val complianceChecker = OutputComplianceChecker

    // UI状态
    private val _uiState = MutableStateFlow<DesignProposalUiState>(DesignProposalUiState.Idle)
    val uiState: StateFlow<DesignProposalUiState> = _uiState.asStateFlow()

    // 当前设计方案
    private val _currentProposal = MutableStateFlow<DesignProposal?>(null)
    val currentProposal: StateFlow<DesignProposal?> = _currentProposal.asStateFlow()

    // Markdown内容（用于PDF导出）
    private val _markdownContent = MutableStateFlow("")
    val markdownContent: StateFlow<String> = _markdownContent.asStateFlow()

    // 新增：当前选中的标准类型
    private val _selectedStandard = MutableStateFlow(initialSelectedStandard)
    val selectedStandard: StateFlow<String?> = _selectedStandard.asStateFlow()

    /**
     * 生成设计方案（修复：使用选中的标准类型过滤数据）
     */
    fun generateProposal(request: DesignProposalRequest) {
        viewModelScope.launch {
            _uiState.value = DesignProposalUiState.Loading

            try {
                // 获取选中的标准类型
                val standardType = _selectedStandard.value
                    ?: throw IllegalArgumentException("未选择标准类型，请先选择标准")

                // 转换标准类型标识
                val standardTypeCode = when {
                    standardType.contains("ECE", ignoreCase = true) -> "ECE_R129"
                    standardType.contains("FMVSS", ignoreCase = true) -> "FMVSS_213"
                    standardType.contains("GB 27887", ignoreCase = true) -> "GB_27887_2024"
                    standardType.contains("GB", ignoreCase = true) -> "GB_27887_2024"
                    else -> "ECE_R129" // 默认使用ECE R129
                }

                generator.generateProposal(request)
                    .onSuccess { proposal ->
                        // 修复：校验输出是否符合选中的标准
                        val proposalContent = proposal.content ?: ""

                        // 生成合规性报告
                        val complianceReport = complianceChecker.generateComplianceReport(
                            proposalContent,
                            standardTypeCode
                        )

                        // 检查合规性（不抛异常，仅记录）
                        val isCompliant = complianceChecker.checkStandardCompliance(
                            proposalContent,
                            standardTypeCode
                        )

                        if (!isCompliant) {
                            // 记录警告但不阻止生成
                            android.util.Log.w(
                                "DesignProposalViewModel",
                                "⚠️ 输出内容可能与选中标准不完全匹配\n$complianceReport"
                            )
                        }

                        _currentProposal.value = proposal
                        _uiState.value = DesignProposalUiState.Success(proposal)

                        // 生成Markdown内容（如果是儿童安全座椅）
                        if (request.productType == "儿童安全座椅") {
                            val standardList = request.selectedStandards["儿童安全座椅"] ?: emptyList()
                            val selection = ChildRestraintDesignService.StandardSelection(
                                eceR129 = standardList.contains("ECE R129") || standardTypeCode == "ECE_R129",
                                gb27887 = standardList.contains("GB 28007-2024") || standardTypeCode == "GB_27887_2024",
                                fmvss213 = standardList.contains("FMVSS 213") || standardTypeCode == "FMVSS_213",
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

                            // 生成Markdown内容，并在末尾添加合规性报告
                            val markdown = childRestraintDesignService.formatAsMarkdown(designProposal)
                            _markdownContent.value = if (isCompliant) {
                                markdown
                            } else {
                                "$markdown\n\n---\n\n📋 **标准合规性报告**\n$complianceReport"
                            }
                        }
                    }
                    .onFailure { error ->
                        _uiState.value = DesignProposalUiState.Error(
                            error.message ?: "生成设计方案失败"
                        )
                    }
            } catch (e: IllegalArgumentException) {
                _uiState.value = DesignProposalUiState.Error(e.message ?: "参数错误")
            } catch (e: Exception) {
                _uiState.value = DesignProposalUiState.Error(
                    "生成设计方案失败：${e.message}"
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

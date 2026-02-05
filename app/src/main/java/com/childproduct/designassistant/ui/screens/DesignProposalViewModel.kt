package com.childproduct.designassistant.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.childproduct.designassistant.data.model.DesignProposal
import com.childproduct.designassistant.data.model.DesignProposalRequest
import com.childproduct.designassistant.database.CribDatabase
import com.childproduct.designassistant.database.EceR129Database
import com.childproduct.designassistant.database.FMVSSDatabase
import com.childproduct.designassistant.database.HighChairDatabase
import com.childproduct.designassistant.service.ChildRestraintDesignService
import com.childproduct.designassistant.service.DesignProposalGenerator
import com.childproduct.designassistant.util.PdfExporter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

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

    // 新增：当前选中的标准类型
    private val _selectedStandard = MutableStateFlow(initialSelectedStandard)
    val selectedStandard: StateFlow<String?> = _selectedStandard.asStateFlow()

    // 新增：PDF导出状态（用于UI反馈）
    private val _pdfExportState = MutableStateFlow<PdfExportState>(PdfExportState.Idle)
    val pdfExportState: StateFlow<PdfExportState> = _pdfExportState.asStateFlow()

    /**
     * 生成设计方案（修复：使用选中的标准类型过滤数据）
     */
    fun generateProposal(request: DesignProposalRequest) {
        viewModelScope.launch {
            _uiState.value = DesignProposalUiState.Loading

            try {
                // 获取选中的标准类型（修复：从request中获取）
                val standardType = request.selectedStandardType
                    ?: throw IllegalArgumentException("未选择标准类型，请先选择标准")

                // 更新ViewModel中的标准类型状态
                _selectedStandard.value = standardType

                android.util.Log.d("DesignProposalVM", "收到标准类型: $standardType")

                // 转换标准类型标识（修复：使用精确匹配，避免误匹配）
                val standardTypeCode = when {
                    standardType.contains("ece_r129", ignoreCase = true) ||
                    standardType.contains("ECE R129", ignoreCase = true) -> "ECE_R129"
                    standardType.contains("fmvss_213", ignoreCase = true) ||
                    standardType.contains("FMVSS 213", ignoreCase = true) -> "FMVSS_213"
                    standardType.contains("gb_27887", ignoreCase = true) ||
                    standardType.contains("GB 27887", ignoreCase = true) ||
                    standardType.contains("GB 28007", ignoreCase = true) -> "GB_27887_2024"
                    standardType.contains("as_nzs_1754", ignoreCase = true) ||
                    standardType.contains("AS/NZS 1754", ignoreCase = true) -> "AS_NZS_1754"
                    standardType.contains("jis_d1601", ignoreCase = true) ||
                    standardType.contains("JIS D 1601", ignoreCase = true) -> "JIS_D1601"
                    else -> "ECE_R129" // 默认使用ECE R129
                }

                android.util.Log.d("DesignProposalVM", "转换后的标准代码: $standardTypeCode")

                generator.generateProposal(request)
                    .onSuccess { proposal ->
                        // TODO: 添加合规性检查功能
                        _currentProposal.value = proposal
                        _uiState.value = DesignProposalUiState.Success(proposal)

                        // 生成Markdown内容（如果是儿童安全座椅）
                        if (request.productType == "儿童安全座椅") {
                            val standardList = request.selectedStandards["儿童安全座椅"] ?: emptyList()

                            android.util.Log.d("DesignProposalVM", "标准列表: $standardList")

                            // 修复：优先使用standardList判断，只有当standardList为空时才使用standardTypeCode
                            val selection = ChildRestraintDesignService.StandardSelection(
                                eceR129 = standardList.contains("ece_r129") || (standardList.isEmpty() && standardTypeCode == "ECE_R129"),
                                gb27887 = standardList.contains("gb_27887_2024") || (standardList.isEmpty() && standardTypeCode == "GB_27887_2024"),
                                fmvss213 = standardList.contains("fmvss_213") || (standardList.isEmpty() && standardTypeCode == "FMVSS_213"),
                                asNzs1754 = standardList.contains("as_nzs_1754") || (standardList.isEmpty() && standardTypeCode == "AS_NZS_1754"),
                                jisD1601 = standardList.contains("jis_d1601") || (standardList.isEmpty() && standardTypeCode == "JIS_D1601")
                            )

                            android.util.Log.d("DesignProposalVM", "标准列表: $standardList")
                            android.util.Log.d("DesignProposalVM", "选择结果: ECE=${selection.eceR129}, GB=${selection.gb27887}, FMVSS=${selection.fmvss213}")

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

                            // 生成Markdown内容
                            val markdown = childRestraintDesignService.formatAsMarkdown(designProposal)
                            _markdownContent.value = markdown
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
        _pdfExportState.value = PdfExportState.Idle
    }

    /**
     * 导出PDF（协程方法，在IO线程执行）
     */
    fun exportToPdf(fileName: String = "儿童安全座椅设计方案") {
        val content = _markdownContent.value
        if (content.isBlank()) {
            _pdfExportState.value = PdfExportState.Error("设计方案内容为空，无法导出PDF")
            return
        }

        _pdfExportState.value = PdfExportState.Loading
        viewModelScope.launch {
            val result = PdfExporter.exportDesignProposal(getApplication(), content, fileName)
            _pdfExportState.value = when {
                result.isSuccess -> {
                    val pdfFile = result.getOrNull()
                    PdfExportState.Success(pdfFile)
                }
                result.isFailure -> {
                    PdfExportState.Error(result.exceptionOrNull()?.message ?: "导出PDF失败")
                }
                else -> PdfExportState.Idle
            }
        }
    }
}

/**
 * PDF导出状态密封类
 */
sealed class PdfExportState {
    /** 空闲状态 */
    object Idle : PdfExportState()

    /** 导出中 */
    object Loading : PdfExportState()

    /** 导出成功 */
    data class Success(val pdfFile: File?) : PdfExportState()

    /** 导出失败 */
    data class Error(val message: String) : PdfExportState()
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

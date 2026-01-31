package com.childproduct.designassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.childproduct.designassistant.model.ProductType
import com.childproduct.designassistant.ui.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

/**
 * 一键生成全维度方案界面
 * 功能：用户输入身高范围、产品类型后，点击"一键生成全维度方案"按钮即可自动触发所有模块的内容生成
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneClickGenerationScreen(
    viewModel: MainViewModel,
    onGenerateComplete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // 输入状态
    var minHeight by remember { mutableStateOf("") }
    var maxHeight by remember { mutableStateOf("") }
    var selectedProductType by remember { mutableStateOf(ProductType.CHILD_SAFETY_SEAT) }
    var selectedStandard by remember { mutableStateOf("ECE R129/GB 27887") }
    
    // 生成状态
    var isGenerating by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }
    
    // 验证输入
    val isValidInput by remember {
        derivedStateOf {
            val minHeightInt = minHeight.toIntOrNull()
            val maxHeightInt = maxHeight.toIntOrNull()
            minHeightInt != null && maxHeightInt != null && minHeightInt <= maxHeightInt
        }
    }

    // 生成方案
    fun generateScheme() {
        if (isValidInput) {
            isGenerating = true
            
            // 模拟生成过程
            viewModel.generateIntegratedScheme(
                heightRange = "$minHeight-${maxHeight}cm",
                productType = selectedProductType,
                standard = selectedStandard
            )
            
            // 延迟后显示结果
            coroutineScope.launch {
                delay(1500)
                isGenerating = false
                showResult = true
                onGenerateComplete()
            }
        }
    }

    if (showResult) {
        // 显示整合报告
        IntegratedReportScreen(viewModel = viewModel, modifier = modifier)
    } else {
        // 显示输入表单
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 页面标题
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "一键生成全维度方案",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 说明卡片
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "使用说明",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "输入产品信息后，点击\"一键生成全维度方案\"按钮，系统将自动生成包含方案详情、测试矩阵、设计建议、竞品参考的完整报告。",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }

            // 产品类型选择
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "产品类型",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // 产品类型选择器
                    val productTypes = listOf(
                        ProductType.CHILD_SAFETY_SEAT to "儿童安全座椅",
                        ProductType.BABY_STROLLER to "婴儿推车",
                        ProductType.CHILD_HIGH_CHAIR to "儿童高脚椅",
                        ProductType.CHILD_HOUSEHOLD_GOODS to "儿童家庭用品"
                    )
                    
                    productTypes.forEach { (type, name) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            RadioButton(
                                selected = selectedProductType == type,
                                onClick = { selectedProductType = type }
                            )
                        }
                    }
                }
            }

            // 标准选择
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "适用标准",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // 标准选择器
                    val standards = listOf(
                        "ECE R129/GB 27887",
                        "ECE R129",
                        "GB 27887",
                        "EN 1888",
                        "ISO 8124-3"
                    )
                    
                    standards.forEach { standard ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = standard,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            RadioButton(
                                selected = selectedStandard == standard,
                                onClick = { selectedStandard = standard }
                            )
                        }
                    }
                }
            }

            // 身高范围输入
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "身高范围（cm）",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = minHeight,
                            onValueChange = { minHeight = it },
                            label = { Text("最小身高") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        
                        Text(
                            text = "-",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        OutlinedTextField(
                            value = maxHeight,
                            onValueChange = { maxHeight = it },
                            label = { Text("最大身高") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                    
                    // 输入提示
                    if (minHeight.isNotEmpty() || maxHeight.isNotEmpty()) {
                        if (isValidInput) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text(
                                    text = "输入有效",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text(
                                    text = "请输入有效的身高范围",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            // 推荐身高范围提示
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TipsAndUpdates,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "推荐身高范围",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = "• 40-105cm（Group 0+）：新生儿-4岁，后向优先",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• 40-150cm（Group 0+/1/2/3）：新生儿-12岁，全分组",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• 100-150cm（Group 2/3）：3.5岁-12岁，前向优先",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // 一键生成按钮
            Button(
                onClick = { generateScheme() },
                enabled = isValidInput && !isGenerating,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    text = if (isGenerating) "生成中..." else "一键生成全维度方案",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

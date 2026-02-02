package com.childproduct.designassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.childproduct.designassistant.generator.DesignOutputGenerator
import com.childproduct.designassistant.model.*

/**
 * 创意生成界面 V2
 * 使用增强的枚举类和DesignOutputGenerator
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreativeScreenV2(
    modifier: Modifier = Modifier
) {
    // 状态管理
    var selectedProductType by remember { mutableStateOf<EnhancedProductType?>(null) }
    var selectedStandard by remember { mutableStateOf<InternationalStandard?>(null) }
    var selectedInstallMethod by remember { mutableStateOf<EnhancedInstallMethod?>(null) }

    // 参数输入
    var minHeight by remember { mutableStateOf("") }
    var maxHeight by remember { mutableStateOf("") }

    // 设计主题
    var theme by remember { mutableStateOf("") }

    // 格式化输出
    var formattedOutput by remember { mutableStateOf<String?>(null) }
    var isGenerating by remember { mutableStateOf(false) }

    // 获取当前产品类型适用的标准
    val applicableStandards = selectedProductType?.let { productType ->
        InternationalStandard.values().filter { it.applicableProducts.contains(productType.toProductType()) }
    } ?: emptyList()

    // 获取当前产品的安装方式
    val installMethods = if (selectedProductType == EnhancedProductType.SAFETY_SEAT) {
        EnhancedInstallMethod.values().toList()
    } else {
        emptyList()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 页面标题
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "标准适配设计 V2",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ========== 1. 产品类型选择 ==========
                Text(
                    text = "1. 产品类型",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                EnhancedProductType.values().forEach { productType ->
                    EnhancedProductTypeCard(
                        productType = productType,
                        isSelected = selectedProductType == productType,
                        onSelected = {
                            selectedProductType = productType
                            selectedStandard = null
                            selectedInstallMethod = null
                            minHeight = ""
                            maxHeight = ""
                            formattedOutput = null
                        }
                    )
                }

                // ========== 2. 标准选择 ==========
                if (selectedProductType != null && applicableStandards.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "2. 标准（${selectedProductType!!.displayName}）",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    applicableStandards.forEach { standard ->
                        StandardCardV2(
                            standard = standard,
                            isSelected = selectedStandard == standard,
                            onSelected = {
                                selectedStandard = standard
                                selectedInstallMethod = null
                                formattedOutput = null
                            }
                        )
                    }
                }

                // ========== 3. 安装方式选择（仅安全座椅） ==========
                if (selectedProductType == EnhancedProductType.SAFETY_SEAT && installMethods.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "3. 安装方式",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    installMethods.forEach { installMethod ->
                        InstallMethodCard(
                            installMethod = installMethod,
                            isSelected = selectedInstallMethod == installMethod,
                            onSelected = {
                                selectedInstallMethod = installMethod
                                formattedOutput = null
                            }
                        )
                    }
                }

                // ========== 4. 参数输入（仅安全座椅） ==========
                if (selectedProductType?.requiresHeightInput == true) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "4. 参数输入",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "身高范围（cm）",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = minHeight,
                            onValueChange = { minHeight = it },
                            label = { Text("最小值") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            placeholder = { Text("40") }
                        )

                        OutlinedTextField(
                            value = maxHeight,
                            onValueChange = { maxHeight = it },
                            label = { Text("最大值") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            placeholder = { Text("150") }
                        )
                    }
                }

                // ========== 5. 设计主题（可选） ==========
                if (selectedStandard != null) {
                    OutlinedTextField(
                        value = theme,
                        onValueChange = { theme = it },
                        label = { Text("设计主题（可选）") },
                        placeholder = { Text("输入特定设计需求") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // ========== 生成按钮 ==========
                val isGenerateButtonEnabled = selectedProductType != null &&
                        selectedStandard != null &&
                        (!selectedProductType!!.requiresHeightInput || (minHeight.isNotEmpty() && maxHeight.isNotEmpty())) &&
                        (!selectedProductType!!.requiresInstallMethod || selectedInstallMethod != null)

                Button(
                    onClick = {
                        isGenerating = true

                        // 构建设计输入
                        val designInput = DesignInput(
                            productType = selectedProductType!!,
                            standards = listOf(selectedStandard!!),
                            heightRange = if (selectedProductType!!.requiresHeightInput) {
                                "$minHeight-$maxHeight"
                            } else {
                                null
                            },
                            installMethod = selectedInstallMethod,
                            theme = theme
                        )

                        // 生成输出
                        formattedOutput = DesignOutputGenerator.generateDesignOutput(designInput)
                        isGenerating = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isGenerateButtonEnabled
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            "生成设计方案",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 显示生成的结果
        formattedOutput?.let { output ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "设计方案输出",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = output,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

/**
 * 增强的产品类型卡片
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedProductTypeCard(
    productType: EnhancedProductType,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        onClick = onSelected
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = onSelected
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = productType.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * 标准卡片 V2
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardCardV2(
    standard: InternationalStandard,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        onClick = onSelected
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = onSelected
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = standard.displayName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${standard.region} - ${standard.code}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

/**
 * 安装方式卡片
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstallMethodCard(
    installMethod: EnhancedInstallMethod,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.tertiaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        onClick = onSelected
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = onSelected
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = installMethod.displayName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${installMethod.fixedType} - ${installMethod.antiRotationType}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.childproduct.designassistant.model.*
import com.childproduct.designassistant.ui.MainViewModel
import com.childproduct.designassistant.ui.UiState

/**
 * 改进的安全检查界面（测试矩阵）
 * 标题：安全测试矩阵
 */
@Composable
fun SafetyScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val safetyCheck by viewModel.safetyCheck.collectAsState()

    var productName by remember { mutableStateOf("") }
    var minHeight by remember { mutableStateOf("") }
    var maxHeight by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 页面标题
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Science,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Icon(
                imageVector = Icons.Default.LibraryBooks,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "安全测试矩阵",
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
                // 产品名称
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("产品名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 身高范围输入（双栏）
                Text(
                    text = "目标身高范围（cm）",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = minHeight,
                        onValueChange = {
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                minHeight = it
                            }
                        },
                        label = { Text("最小身高") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("如: 40") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        value = maxHeight,
                        onValueChange = {
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                maxHeight = it
                            }
                        },
                        label = { Text("最大身高") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("如: 105") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                // 年龄区间提示
                if (minHeight.isNotBlank() && maxHeight.isNotBlank()) {
                    val ageHint = getAgeRangeHint(minHeight.toIntOrNull() ?: 0, maxHeight.toIntOrNull() ?: 0)
                    if (ageHint != null) {
                        Text(
                            text = "对应年龄区间: $ageHint",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // 检查按钮
                Button(
                    onClick = {
                        if (productName.isNotBlank() && minHeight.isNotBlank() && maxHeight.isNotBlank()) {
                            val ageGroup = inferAgeGroup(minHeight.toIntOrNull() ?: 0, maxHeight.toIntOrNull() ?: 0)
                            viewModel.performSafetyCheck(productName, ageGroup)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState !is UiState.Loading &&
                              productName.isNotBlank() &&
                              minHeight.isNotBlank() &&
                              maxHeight.isNotBlank()
                ) {
                    if (uiState is UiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("生成测试矩阵")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 显示检查结果
        safetyCheck?.let { check ->
            SafetyCheckResultCard(check = check)
        }
    }
}

/**
 * 根据身高范围推断年龄区间提示
 */
private fun getAgeRangeHint(minHeight: Int, maxHeight: Int): String? {
    if (minHeight <= 0 || maxHeight <= 0) return null

    return when {
        maxHeight <= 75 -> "0-18个月"
        minHeight >= 75 && maxHeight <= 100 -> "15个月-4岁"
        minHeight >= 100 && maxHeight <= 150 -> "4岁-12岁"
        else -> null
    }
}

/**
 * 根据身高范围推断年龄段
 */
private fun inferAgeGroup(minHeight: Int, maxHeight: Int): AgeGroup {
    return when {
        maxHeight <= 75 -> AgeGroup.INFANT
        minHeight >= 75 && maxHeight <= 100 -> AgeGroup.PRESCHOOL
        minHeight >= 100 && maxHeight <= 150 -> AgeGroup.SCHOOL_AGE
        else -> AgeGroup.PRESCHOOL
    }
}

/**
 * 安全检查结果卡片
 */
@Composable
fun SafetyCheckResultCard(
    check: SafetyCheck,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (check.passed) {
                Color(0xFFE8F5E9)
            } else {
                Color(0xFFFFEBEE)
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = check.productName,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${check.overallScore}分",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (check.overallScore >= 80) {
                        Color(0xFF4CAF50)
                    } else if (check.overallScore >= 60) {
                        Color(0xFFFF9800)
                    } else {
                        Color(0xFFF44336)
                    }
                )
            }

            Text(
                text = "年龄段: ${check.ageGroup.displayName}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "检查项详情",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                check.checks.forEach { checkItem ->
                    SafetyCheckItem(checkItem = checkItem)
                }
            }

            if (check.recommendations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = "建议",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                check.recommendations.forEach { recommendation ->
                    Text(
                        text = recommendation,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}

/**
 * 安全检查项
 */
@Composable
fun SafetyCheckItem(
    checkItem: com.childproduct.designassistant.model.SafetyItem,
    modifier: Modifier = Modifier
) {
    val (icon, iconColor) = when (checkItem.status) {
        CheckStatus.PASSED -> Icons.Default.CheckCircle to Color(0xFF4CAF50)
        CheckStatus.WARNING -> Icons.Default.Warning to Color(0xFFFF9800)
        CheckStatus.FAILED -> Icons.Default.Cancel to Color(0xFFF44336)
        CheckStatus.NOT_APPLICABLE -> Icons.Default.RemoveCircle to Color(0xFF9E9E9E)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = checkItem.itemName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                if (checkItem.notes.isNotEmpty()) {
                    Text(
                        text = checkItem.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

/**
 * 年龄段选择器（保留用于其他场景）
 */
@Composable
fun SafetyAgeGroupSelector(
    selectedAgeGroup: AgeGroup,
    onAgeGroupSelected: (AgeGroup) -> Unit
) {
    Column {
        AgeGroup.values().forEach { ageGroup ->
            FilterChip(
                selected = selectedAgeGroup == ageGroup,
                onClick = { onAgeGroupSelected(ageGroup) },
                label = { Text(ageGroup.displayName) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

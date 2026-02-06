package com.design.assistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.design.assistant.model.ProductType
import com.design.assistant.model.StandardType
import com.design.assistant.model.getDisplayName
import com.design.assistant.ui.components.ProductTypeCard
import com.design.assistant.ui.components.ProductAccordion
import com.design.assistant.viewmodel.ProductStandardSelectViewModel

/**
 * 标准选择页面
 * 用于选择产品类型和适用的标准
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardSelectScreen(
    viewModel: ProductStandardSelectViewModel,
    onGenerateClick: (com.design.assistant.model.ProductType, List<com.design.assistant.model.StandardType>, Int, Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // 儿童身高和体重输入
    var childHeight by remember { mutableStateOf("") }
    var childWeight by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "选择产品和标准",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "选择产品类型和适用的标准",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.clearSelection() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("清空选择")
                    }

                    Button(
                        onClick = {
                            uiState.selectedProductType?.let { productType ->
                                onGenerateClick(
                                    productType,
                                    uiState.selectedStandards,
                                    childHeight.toIntOrNull() ?: 0,
                                    childWeight.toIntOrNull() ?: 0
                                )
                            }
                        },
                        enabled = viewModel.canProceed() && childHeight.isNotBlank() && childWeight.isNotBlank(),
                        modifier = Modifier.weight(2f)
                    ) {
                        Text("生成设计方案")
                    }
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 产品类型选择
            SectionTitle(title = "1. 选择产品类型")

            uiState.productTypes.forEach { productType ->
                ProductTypeCard(
                    productType = productType,
                    selected = uiState.selectedProductType == productType,
                    onClick = { viewModel.selectProductType(productType) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 标准选择
            if (uiState.selectedProductType != null) {
                SectionTitle(title = "2. 选择适用标准")

                var expanded by remember { mutableStateOf(true) }

                ProductAccordion(
                    productType = uiState.selectedProductType!!,
                    standards = uiState.availableStandards,
                    expanded = expanded,
                    selectedStandards = uiState.selectedStandards,
                    onExpandedChange = { isExpanded: Boolean -> expanded = isExpanded },
                    onStandardClick = { standard: StandardType ->
                        viewModel.toggleStandard(standard)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 儿童身高体重输入
                SectionTitle(title = "3. 输入儿童参数")

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 身高输入
                        OutlinedTextField(
                            value = childHeight,
                            onValueChange = {
                                // 只允许输入数字
                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                    childHeight = it
                                }
                            },
                            label = { Text("儿童身高 (cm)") },
                            placeholder = { Text("1-150") },
                            singleLine = true,
                            isError = childHeight.isNotEmpty() &&
                                       (childHeight.toIntOrNull()?.let { it !in 1..150 } ?: true),
                            supportingText = {
                                if (childHeight.isNotEmpty()) {
                                    val height = childHeight.toIntOrNull()
                                    when {
                                        height == null || height < 1 || height > 150 -> {
                                            Text("请输入1-150之间的数字", color = MaterialTheme.colorScheme.error)
                                        }
                                        else -> {
                                            Text("当前身高: ${height}cm")
                                        }
                                    }
                                }
                            },
                            leadingIcon = {
                                Text("📏")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // 体重输入
                        OutlinedTextField(
                            value = childWeight,
                            onValueChange = {
                                // 只允许输入数字
                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                    childWeight = it
                                }
                            },
                            label = { Text("儿童体重 (kg)") },
                            placeholder = { Text("1-50") },
                            singleLine = true,
                            isError = childWeight.isNotEmpty() &&
                                       (childWeight.toIntOrNull()?.let { it !in 1..50 } ?: true),
                            supportingText = {
                                if (childWeight.isNotEmpty()) {
                                    val weight = childWeight.toIntOrNull()
                                    when {
                                        weight == null || weight < 1 || weight > 50 -> {
                                            Text("请输入1-50之间的数字", color = MaterialTheme.colorScheme.error)
                                        }
                                        else -> {
                                            Text("当前体重: ${weight}kg")
                                        }
                                    }
                                }
                            },
                            leadingIcon = {
                                Text("⚖️")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // 参数提示
                        if (childHeight.isNotBlank() && childWeight.isNotBlank()) {
                            val height = childHeight.toIntOrNull() ?: 0
                            val weight = childWeight.toIntOrNull() ?: 0

                            // 根据身高体重判断年龄段
                            val ageHint = when {
                                height in 1..65 && weight in 1..9 -> "新生儿/婴儿 (0-9个月)"
                                height in 66..85 && weight in 10..13 -> "幼儿 (9-18个月)"
                                height in 86..105 && weight in 14..18 -> "幼儿 (1.5-3岁)"
                                height in 106..125 && weight in 19..25 -> "儿童 (3-6岁)"
                                height in 126..150 && weight in 26..50 -> "学龄儿童 (6-12岁)"
                                else -> "未知年龄段"
                            }

                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    text = "👶 预估年龄段: $ageHint",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 已选择的标准摘要
                if (uiState.selectedStandards.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "已选择 ${uiState.selectedStandards.size} 个标准",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = uiState.selectedStandards.joinToString("、") { standard: StandardType -> standard.getDisplayName() },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                // 提示选择产品类型
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "请先选择产品类型",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * 区块标题
 */
@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(horizontal = 16.dp)
    )
}

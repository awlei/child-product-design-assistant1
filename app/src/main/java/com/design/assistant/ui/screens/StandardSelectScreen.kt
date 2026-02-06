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
import com.design.assistant.model.GPS028Group
import com.design.assistant.viewmodel.ProductStandardSelectViewModel

/**
 * 标准选择页面
 * 用于选择产品类型和适用的标准
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardSelectScreen(
    viewModel: ProductStandardSelectViewModel,
    onGenerateClick: (com.design.assistant.model.ProductType, List<com.design.assistant.model.StandardType>, GPS028Group, Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // 用于儿童安全座椅的组别和百分位选择
    var selectedGroup by remember { mutableStateOf(GPS028Group.GROUP_I) }
    var selectedPercentile by remember { mutableStateOf(50) }

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
                                    selectedGroup,
                                    selectedPercentile
                                )
                            }
                        },
                        enabled = viewModel.canProceed(),
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
                    onExpandedChange = { expanded = it },
                    onStandardClick = { standard ->
                        viewModel.toggleStandard(standard)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 儿童安全座椅特定参数（组别和百分位）
                if (uiState.selectedProductType == com.design.assistant.model.ProductType.CHILD_SEAT) {
                    SectionTitle(title = "3. 选择假人参数")

                    // 组别选择
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "选择组别",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                GPS028Group.values().forEach { group ->
                                    FilterChip(
                                        selected = selectedGroup == group,
                                        onClick = { selectedGroup = group },
                                        label = { Text(group.displayName) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    // 百分位选择
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "选择百分位",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(50, 75, 95).forEach { percentile ->
                                    FilterChip(
                                        selected = selectedPercentile == percentile,
                                        onClick = { selectedPercentile = percentile },
                                        label = { Text("${percentile}%") },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

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
                                text = uiState.selectedStandards.joinToString("、") { it.getDisplayName() },
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

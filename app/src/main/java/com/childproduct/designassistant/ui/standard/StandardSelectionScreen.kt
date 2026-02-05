package com.childproduct.designassistant.ui.standard

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.childproduct.designassistant.ui.theme.ChildProductDesignAssistantTheme

/**
 * 标准适配设计选择界面
 *
 * 功能：
 * - 按产品分类展示（出行类、家居类）
 * - 折叠卡片式UI，默认收起
 * - 支持单选或多选标准
 * - 全选/取消全选快捷按钮
 * - 根据选择的标准调用对应数据库
 * - 修复：传递selectedStandardType，确保标准类型传递到下游
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardSelectionScreen(
    viewModel: StandardSelectionViewModel = viewModel(),
    onGenerateDesign: (
        selectedStandards: Map<String, List<String>>,
        selectedStandardType: String?  // 新增：选中的标准类型
    ) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedStandards by viewModel.selectedStandards.collectAsState()
    val selectedStandardType by viewModel.selectedStandardType.collectAsState()  // 新增：收集选中的标准类型

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("标准适配设计", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onGenerateDesign(selectedStandards, selectedStandardType) },  // 修复：传递selectedStandardType
                containerColor = if (selectedStandards.isNotEmpty())
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (selectedStandards.isNotEmpty())
                    Color.White
                else MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "生成设计方案")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 出行类
            ProductCategorySection(
                title = "出行类",
                products = uiState.travelProducts,
                expandedCategories = uiState.expandedCategories,
                onToggleCategory = { viewModel.toggleCategory(it) },
                onStandardSelected = { productId, standard ->
                    viewModel.toggleStandard(productId, standard)
                },
                onSelectAll = { productId, standards ->
                    viewModel.selectAllStandards(productId, standards)
                },
                onDeselectAll = { productId ->
                    viewModel.deselectAllStandards(productId)
                },
                selectedStandards = selectedStandards
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 家居类
            ProductCategorySection(
                title = "家居类",
                products = uiState.homeProducts,
                expandedCategories = uiState.expandedCategories,
                onToggleCategory = { viewModel.toggleCategory(it) },
                onStandardSelected = { productId, standard ->
                    viewModel.toggleStandard(productId, standard)
                },
                onSelectAll = { productId, standards ->
                    viewModel.selectAllStandards(productId, standards)
                },
                onDeselectAll = { productId ->
                    viewModel.deselectAllStandards(productId)
                },
                selectedStandards = selectedStandards
            )
        }
    }
}

/**
 * 产品分类区域
 */
@Composable
fun ProductCategorySection(
    title: String,
    products: List<ProductCategory>,
    expandedCategories: Set<String>,
    onToggleCategory: (String) -> Unit,
    onStandardSelected: (String, String) -> Unit,
    onSelectAll: (String, List<String>) -> Unit,
    onDeselectAll: (String) -> Unit,
    selectedStandards: Map<String, List<String>>
) {
    Column {
        // 分类标题
        Text(
            text = "📦 $title",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        products.forEach { product ->
            ProductCategoryItem(
                product = product,
                isExpanded = expandedCategories.contains(product.id),
                onToggle = { onToggleCategory(product.id) },
                onStandardSelected = onStandardSelected,
                onSelectAll = { onSelectAll(product.id, product.standards.map { it.id }) },
                onDeselectAll = { onDeselectAll(product.id) },
                selectedStandards = selectedStandards[product.id] ?: emptyList()
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/**
 * 产品分类项（折叠卡片）
 */
@Composable
fun ProductCategoryItem(
    product: ProductCategory,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onStandardSelected: (String, String) -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    selectedStandards: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // 卡片头部（可点击展开/收起）
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = product.icon,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    
                    // 显示已选数量
                    if (selectedStandards.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "${selectedStandards.size}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp 
                                  else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "收起" else "展开"
                )
            }

            // 展开内容（标准选择）
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // 全选/取消全选按钮
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onSelectAll,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("全选")
                        }

                        OutlinedButton(
                            onClick = onDeselectAll,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Cancel, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("取消全选")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 标准选择列表
                    product.standards.forEach { standard ->
                        StandardCheckboxItem(
                            standard = standard,
                            isSelected = standard.id in selectedStandards,
                            onSelectedChange = { selected ->
                                if (selected) {
                                    onStandardSelected(product.id, standard.id)
                                } else {
                                    onStandardSelected(product.id, standard.id)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/**
 * 标准选择项
 */
@Composable
fun StandardCheckboxItem(
    standard: StandardItem,
    isSelected: Boolean,
    onSelectedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        else
            MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectedChange(!isSelected) }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onSelectedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = standard.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                standard.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (standard.region != null) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    Text(
                        text = standard.region,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * 数据类定义
 */
data class ProductCategory(
    val id: String,
    val name: String,
    val icon: String,
    val standards: List<StandardItem>
)

data class StandardItem(
    val id: String,
    val name: String,
    val description: String? = null,
    val region: String? = null,
    val databaseRef: String? = null  // 数据库引用
)

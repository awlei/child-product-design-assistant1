package com.design.assistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.design.assistant.model.DesignResult
import com.design.assistant.model.ProductType
import com.design.assistant.model.StandardType
import com.design.assistant.model.getDisplayName
import com.design.assistant.ui.components.*

/**
 * 设计结果展示页面
 * 用于展示生成的专业设计参数和兼容性分析
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignResultScreen(
    designResult: DesignResult,
    onBackClick: () -> Unit,
    onExportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showFullReport by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "设计结果",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = designResult.productType.getDisplayName(),
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
                },
                actions = {
                    IconButton(onClick = onExportClick) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "导出"
                        )
                    }
                }
            )
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
            // 设计结果卡片
            DesignResultCard(
                designResult = designResult,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // 操作按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { showFullReport = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("完整报告")
                }

                Button(
                    onClick = onExportClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("导出PDF")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // 完整报告对话框
    if (showFullReport) {
        FullReportDialog(
            designResult = designResult,
            onDismiss = { showFullReport = false }
        )
    }
}

/**
 * 完整报告对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullReportDialog(
    designResult: DesignResult,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .heightIn(max = 600.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 标题栏
                TopAppBar(
                    title = {
                        Text(
                            text = "完整设计报告",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "关闭"
                            )
                        }
                    }
                )

                // 报告内容
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = designResult.generateReport(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // 底部按钮
                Surface(
                    tonalElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("关闭")
                        }

                        Button(
                            onClick = {
                                // 复制到剪贴板
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("复制")
                        }
                    }
                }
            }
        }
    }
}

/**
 * 设计结果卡片组件（已在components中定义，这里为了完整性再次引入）
 */
@Composable
fun DesignResultCard(
    designResult: DesignResult,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 标题栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${designResult.productType.getDisplayName()} 设计方案",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "生成时间：${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(designResult.generatedAt))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                // 兼容性评分
                designResult.compatibility?.let { compatibility ->
                    CompatibilityScoreBadge(compatibility = compatibility)
                }
            }

            HorizontalDivider()

            // 适用的标准
            Text(
                text = "适用标准",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                designResult.standards.forEach { standard ->
                    StandardTagCompact(
                        standard = standard,
                        selected = false,
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // GPS028参数（如果是儿童安全座椅）
            designResult.gps028Params?.let { gps028Params ->
                Text(
                    text = "GPS028设计参数",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ParameterCard(
                            title = "组别",
                            value = gps028Params.groupName,
                            modifier = Modifier.weight(1f)
                        )
                        ParameterCard(
                            title = "百分位",
                            value = gps028Params.percentile,
                            modifier = Modifier.weight(1f)
                        )
                        ParameterCard(
                            title = "体重",
                            value = "${gps028Params.weight}kg",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ParameterCard(
                            title = "最大HIC",
                            value = gps028Params.maxHeadInjuryCriterion.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        ParameterCard(
                            title = "最大胸部加速度",
                            value = "${gps028Params.maxChestAcceleration}g",
                            modifier = Modifier.weight(1f)
                        )
                        ParameterCard(
                            title = "最大颈部力矩",
                            value = "${gps028Params.maxNeckMoment}Nm",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // 设计建议
            if (designResult.designRecommendations.isNotEmpty()) {
                Text(
                    text = "设计建议",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                designResult.designRecommendations.take(3).forEach { recommendation ->
                    RecommendationItem(recommendation = recommendation)
                }

                if (designResult.designRecommendations.size > 3) {
                    Text(
                        text = "还有 ${designResult.designRecommendations.size - 3} 条建议...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            // 标准冲突
            if (designResult.conflicts.isNotEmpty()) {
                Text(
                    text = "标准冲突提示",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )

                designResult.conflicts.forEach { conflict ->
                    ConflictItem(conflict = conflict)
                }
            }
        }
    }
}

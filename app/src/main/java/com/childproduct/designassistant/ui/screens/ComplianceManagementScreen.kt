package com.childproduct.designassistant.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.childproduct.designassistant.ui.MainViewModel

/**
 * 合规管理屏幕
 * 
 * 提供FMVSS合规全流程管理功能，包括：
 * - NHTSA认证流程
 * - 用户误用防护
 * - 供应链合规
 * - 售后召回
 * - 标准动态适配
 * - 人机工程学
 * - 竞品风险
 * - 合规风险管理
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplianceManagementScreen(
    viewModel: MainViewModel = viewModel()
) {
    var selectedModule by remember { mutableStateOf<ComplianceModule?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "FMVSS合规管理",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // 标题和说明
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "合规全流程管理",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "覆盖NHTSA认证、设计防护、供应链、售后等合规关键因素",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 合规模块列表
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(ComplianceModule.values()) { module ->
                    ComplianceModuleCard(
                        module = module,
                        onClick = {
                            selectedModule = module
                            showDialog = true
                        }
                    )
                }
            }
        }

        // 详情对话框
        if (showDialog && selectedModule != null) {
            ComplianceModuleDetailDialog(
                module = selectedModule!!,
                onDismiss = { 
                    showDialog = false
                    selectedModule = null 
                },
                onAction = { action ->
                    // TODO: 执行具体操作
                    showDialog = false
                    selectedModule = null
                }
            )
        }
    }
}

/**
 * 合规模块卡片
 */
@Composable
fun ComplianceModuleCard(
    module: ComplianceModule,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = module.color.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = module.icon,
                    contentDescription = null,
                    tint = module.color,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = module.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = module.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // 箭头图标
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

/**
 * 合规模块详情对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplianceModuleDetailDialog(
    module: ComplianceModule,
    onDismiss: () -> Unit,
    onAction: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = module.icon,
                contentDescription = null,
                tint = module.color,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = module.displayName,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = module.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                Divider()

                Text(
                    text = "功能特性：",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                module.features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "• ",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = feature,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "相关标准：",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                module.standards.forEach { standard ->
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "• ",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = standard,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onAction("view_details") }
            ) {
                Text("查看详情")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        }
    )
}

/**
 * 合规模块枚举
 */
enum class ComplianceModule(
    val displayName: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val features: List<String>,
    val standards: List<String>
) {
    NHTSA_CERTIFICATION(
        displayName = "NHTSA认证",
        description = "认证申请、实验室资质、样品一致性管理",
        icon = Icons.Default.Gavel,
        color = Color(0xFF1976D2),
        features = listOf(
            "认证流程跟踪（7个阶段）",
            "实验室资质审核",
            "测试样品一致性验证",
            "3-6个月测试周期管理"
        ),
        standards = listOf("FMVSS 213", "NHTSA认证要求")
    ),
    MISUSE_PROTECTION(
        displayName = "误用防护",
        description = "安装防错、儿童操作限制设计",
        icon = Icons.Default.Shield,
        color = Color(0xFF388E3C),
        features = listOf(
            "安装防错设计（卡扣、LATCH）",
            "儿童操作限制",
            "7种误用场景分析",
            "风险矩阵评估"
        ),
        standards = listOf("FMVSS 213 S5.4.3.5", "NHTSA测试要求")
    ),
    SUPPLY_CHAIN(
        displayName = "供应链合规",
        description = "溯源性、一致性控制",
        icon = Icons.Default.LocalShipping,
        color = Color(0xFFF57C00),
        features = listOf(
            "供应商资质管理",
            "材料批次追溯",
            "量产一致性控制",
            "关键参数偏差监控"
        ),
        standards = listOf("ISO 9001", "供应商合规要求")
    ),
    AFTER_SALES(
        displayName = "售后召回",
        description = "注册系统、使用寿命、追溯",
        icon = Icons.Default.SupportAgent,
        color = Color(0xFF7B1FA2),
        features = listOf(
            "产品注册系统",
            "7年使用寿命管理",
            "召回管理（49 CFR Part 573）",
            "产品标签要求"
        ),
        standards = listOf("FMVSS 213 S5.8", "49 CFR Part 573")
    ),
    STANDARD_ADAPTATION(
        displayName = "标准适配",
        description = "标准修订、州级要求、进口合规",
        icon = Icons.Default.Update,
        color = Color(0xFF0288D1),
        features = listOf(
            "FMVSS标准跟踪",
            "州级额外要求",
            "进口报关合规",
            "标准更新订阅"
        ),
        standards = listOf("49 CFR Part 567", "49 CFR Part 571")
    ),
    ERGONOMICS(
        displayName = "人机工程学",
        description = "安装便捷性、极端环境适配",
        icon = Icons.Default.Chair,
        color = Color(0xFF00897B),
        features = listOf(
            "车辆适配评估",
            "安装便捷性评分",
            "重量控制（≤15kg）",
            "极端环境适应性"
        ),
        standards = listOf("FMVSS 213 S4.4.1.2", "FMVSS 302")
    ),
    COMPETITOR_RISK(
        displayName = "竞品风险",
        description = "案例分析、专利合规",
        icon = Icons.Default.CompareArrows,
        color = Color(0xFFD32F2F),
        features = listOf(
            "竞品合规案例分析",
            "6种合规陷阱",
            "专利风险分析",
            "风险规避策略"
        ),
        standards = listOf("专利法", "竞品分析")
    ),
    RISK_MANAGEMENT(
        displayName = "风险管理",
        description = "风险评估、预警机制",
        icon = Icons.Default.Warning,
        color = Color(0xFFC2185B),
        features = listOf(
            "8个风险类别评估",
            "风险矩阵分析",
            "预警机制",
            "缓解计划管理"
        ),
        standards = listOf("风险管理框架", "NHTSA要求")
    )
}

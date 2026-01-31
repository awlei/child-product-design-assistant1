package com.childproduct.designassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.childproduct.designassistant.ui.MainViewModel

/**
 * 测试矩阵屏幕
 * 功能：展示ECE R129动态测试矩阵，包含17项测试用例
 */
@Composable
fun TestMatrixScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        // 标题
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
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
                            imageVector = Icons.Default.Science,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "ECE R129 动态测试矩阵",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    Text(
                        text = "基于身高40-150cm（Group 0+/1/2/3）自动生成的17项动态测试用例",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // 测试参数说明
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
                            text = "测试参数说明",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Divider()

                    // 输入参数（14个）
                    Text(
                        text = "输入参数（14个）：",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    ParameterItem("脉冲类型", "FRONTAL/REAR/SIDE")
                    ParameterItem("撞击类型", "FRONTAL_RIGID/FRONTAL_OFFSET/REAR")
                    ParameterItem("假人类型", "Q0/Q1/Q1.5/Q3/Q6/Q10")
                    ParameterItem("座椅方向", "REARWARD_FACING/FORWARD_FACING")
                    ParameterItem("安装方式", "ISOFIX_3_PTS/SEAT_BELT")
                    ParameterItem("产品配置", "UPRIGHT/RECLINED")

                    Spacer(modifier = Modifier.height(8.dp))

                    // 输出参数（24个）
                    Text(
                        text = "输出参数（24个）：",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    ParameterItem("头部伤害指标", "HIC15, HIC36, HIC3ms")
                    ParameterItem("胸部伤害指标", "Chest Accel (3ms, 36ms), Chest Deflection")
                    ParameterItem("颈部伤害指标", "Nij, Neck Tension/Compression")
                    ParameterItem("大腿伤害指标", "Femur Axial Force (Left/Right)")
                    ParameterItem("骨盆伤害指标", "Pelvis Acceleration")
                    ParameterItem("位移指标", "Head Excursion, Knee Excursion")
                    ParameterItem("合格标准", "HIC36 ≤ 324, 胸部加速度 ≤ 55g, Nij ≤ 1.0")
                }
            }
        }

        // 测试矩阵列表
        items(17) { index ->
            TestMatrixScreenTestMatrixItemCard(testIndex = index + 1)
        }
    }
}

/**
 * 测试矩阵项卡片
 */
@Composable
fun TestMatrixScreenTestMatrixItemCard(testIndex: Int) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 标题行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "测试 #$testIndex",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ECE_R129_$testIndex",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "折叠" else "展开",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Divider()

            if (expanded) {
                // 详细参数
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    TestMatrixTestParameterRow("脉冲类型", "FRONTAL")
                    TestMatrixTestParameterRow("撞击类型", "FRONTAL_RIGID")
                    TestMatrixTestParameterRow("假人类型", if (testIndex <= 3) "Q0（新生儿）" else if (testIndex <= 6) "Q1（1岁）" else "Q3（3岁）")
                    TestMatrixTestParameterRow("座椅方向", if (testIndex <= 8) "REARWARD_FACING" else "FORWARD_FACING")
                    TestMatrixTestParameterRow("安装方式", "ISOFIX_3_PTS")
                    TestMatrixTestParameterRow("产品配置", "UPRIGHT")
                    TestMatrixTestParameterRow("速度", "50 km/h")

                    Spacer(modifier = Modifier.height(8.dp))

                    // 合格标准
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
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
                                    text = "合格标准",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = "• HIC36 ≤ 324",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "• 胸部加速度 ≤ 55g",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "• Nij ≤ 1.0",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            } else {
                // 简略信息
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = "合格标准: HIC36 ≤ 324，胸部加速度 ≤ 55g",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * 参数项组件
 */
@Composable
fun ParameterItem(name: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Text(
            text = name,
            modifier = Modifier.weight(1.2f),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description,
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

/**
 * 测试参数行组件
 */
@Composable
fun TestMatrixTestParameterRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

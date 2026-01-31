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
import com.childproduct.designassistant.model.CrashTestDummy
import com.childproduct.designassistant.model.CrashTestMapping
import com.childproduct.designassistant.ui.MainViewModel

/**
 * 测试矩阵屏幕
 * 功能：展示ECE R129动态测试矩阵，基于UN R129/GB 27887-2024标准自动生成测试用例
 */
@Composable
fun TestMatrixScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // 获取所有假人类型的映射信息
    val dummyMappings = remember { CrashTestMapping.getAllDummyMappings() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
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
                        text = "基于身高40-150cm（Group 0+/1/2/3）自动生成的动态测试用例",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // 假人类型映射表
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
                            text = "假人类型映射表（基于UN R129标准）",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Divider()

                    // 假人类型列表
                    dummyMappings.forEach { mapping ->
                        DummyTypeMappingItem(mapping = mapping)
                    }
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
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "测试参数说明",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Divider()

                    // 输入参数
                    Text(
                        text = "输入参数（14个）：",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    ParameterItem("脉冲类型", "FRONTAL/REAR/SIDE")
                    ParameterItem("撞击类型", "FRONTAL_RIGID/FRONTAL_OFFSET/REAR")
                    ParameterItem("假人类型", "Q0/Q0+/Q1/Q1.5/Q3/Q3s/Q6/Q10")
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
                    ParameterItem("合格标准", "根据假人类型动态调整")
                }
            }
        }

        // 动态生成的测试矩阵
        items(dummyMappings) { mapping ->
            TestMatrixCard(mapping = mapping)
        }
    }
}

/**
 * 假人类型映射项组件
 */
@Composable
fun DummyTypeMappingItem(
    mapping: CrashTestMapping.DummyDetails
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 假人名称
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mapping.dummyType.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = mapping.productGroup,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 身高范围
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "身高:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = mapping.heightRange,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "体重:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${mapping.weight}kg",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }

            // HIC极限值
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "HIC极限值:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${mapping.hicLimit}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = if (mapping.hicLimit <= 390) MaterialTheme.colorScheme.error 
                           else if (mapping.hicLimit <= 570) MaterialTheme.colorScheme.tertiary 
                           else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * 测试矩阵卡片组件
 */
@Composable
fun TestMatrixCard(
    mapping: CrashTestMapping.DummyDetails
) {
    var expanded by remember { mutableStateOf(false) }
    val dummyType = mapping.dummyType
    val complianceParams = mapping.complianceParams

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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${dummyType.name} 测试矩阵",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "(${mapping.description})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
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

            Divider()

            if (expanded) {
                // 详细参数
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    TestMatrixParameterRow("假人类型", dummyType.name)
                    TestMatrixParameterRow("身高范围", mapping.heightRange)
                    TestMatrixParameterRow("体重", "${mapping.weight}kg")
                    TestMatrixParameterRow("产品分组", mapping.productGroup)
                    TestMatrixParameterRow("座椅方向", 
                        if (dummyType in listOf(CrashTestDummy.Q0, CrashTestDummy.Q0_PLUS, CrashTestDummy.Q1)) {
                            "REARWARD_FACING (反向)"
                        } else {
                            "FORWARD_FACING (正向)"
                        }
                    )
                    TestMatrixParameterRow("撞击类型", "FRONTAL_RIGID")
                    TestMatrixParameterRow("测试速度", "50 km/h")

                    Spacer(modifier = Modifier.height(8.dp))

                    // 合格标准（基于假人类型动态调整）
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
                                    text = "合格标准（基于${dummyType.name}假人）",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            StandardCheckItem(
                                "HIC36", 
                                "≤ ${complianceParams.hicLimit}", 
                                complianceParams.hicLimit <= 390
                            )
                            StandardCheckItem(
                                "胸部加速度", 
                                "≤ ${complianceParams.chestAccelerationLimit}g", 
                                complianceParams.chestAccelerationLimit == 55
                            )
                            StandardCheckItem(
                                "颈部张力", 
                                "≤ ${complianceParams.neckTensionLimit}N", 
                                complianceParams.neckTensionLimit == 1800
                            )
                            StandardCheckItem(
                                "颈部压缩", 
                                "≤ ${complianceParams.neckCompressionLimit}N", 
                                complianceParams.neckCompressionLimit == 2200
                            )
                            StandardCheckItem(
                                "头部位移", 
                                "≤ ${complianceParams.headExcursionLimit}mm", 
                                true
                            )
                            StandardCheckItem(
                                "膝部位移", 
                                "≤ ${complianceParams.kneeExcursionLimit}mm", 
                                true
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
                        text = "HIC36 ≤ ${complianceParams.hicLimit}，胸部加速度 ≤ ${complianceParams.chestAccelerationLimit}g",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * 标准检查项组件
 */
@Composable
fun StandardCheckItem(
    label: String,
    value: String,
    isCritical: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isCritical) Icons.Default.PriorityHigh else Icons.Default.Check,
            contentDescription = null,
            tint = if (isCritical) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "$label $value",
            style = MaterialTheme.typography.bodySmall,
            color = if (isCritical) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
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
fun TestMatrixParameterRow(label: String, value: String) {
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

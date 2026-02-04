package com.childproduct.designassistant.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.childproduct.designassistant.data.model.DesignProposal

/**
 * 设计方案展示界面
 *
 * 结构化展示儿童安全座椅的设计方案，包含适用标准、基础适配数据、设计参数等
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignProposalScreen(
    proposal: DesignProposal,
    onBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("设计方案") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 产品类型标题
            ProductTypeHeader(productType = proposal.productType)

            // 适用标准（蓝色标签）
            ApplicableStandardsCard(standards = proposal.applicableStandards)

            // 基础适配数据
            BasicFitDataCard(basicFitData = proposal.basicFitData)

            // 设计参数
            DesignParametersCard(designParameters = proposal.designParameters)

            // 测试要求
            TestRequirementsCard(testRequirements = proposal.testRequirements)

            // 标准测试项
            StandardTestItemsCard(testItems = proposal.standardTestItems)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * 产品类型标题
 */
@Composable
private fun ProductTypeHeader(productType: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ChildCare,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "📦 儿童安全座椅设计方案",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = productType,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * 适用标准卡片（蓝色标签）
 */
@Composable
private fun ApplicableStandardsCard(standards: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD) // 淡蓝色背景
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "【适用标准】",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2) // 深蓝色
            )
            Spacer(modifier = Modifier.height(8.dp))
            standards.forEach { standard ->
                AssistChip(
                    onClick = {},
                    label = { Text(standard) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

/**
 * 基础适配数据卡片
 */
@Composable
private fun BasicFitDataCard(basicFitData: com.childproduct.designassistant.data.model.BasicFitData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionTitle(title = "📊 基础适配数据", icon = Icons.Default.Person)
            Spacer(modifier = Modifier.height(12.dp))

            // 假人信息
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Man,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🔽 假人",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                ParameterItem(
                    icon = Icons.Default.Height,
                    label = "身高范围",
                    value = basicFitData.dummyInfo.heightRange
                )
                ParameterItem(
                    icon = Icons.Default.Scale,
                    label = "体重范围",
                    value = basicFitData.dummyInfo.weightRange
                )
                ParameterItem(
                    icon = Icons.Default.Compress,
                    label = "安装方向",
                    value = basicFitData.dummyInfo.installationDirection
                )
                basicFitData.dummyInfo.dummyType?.let {
                    ParameterItem(
                        icon = Icons.Default.Badge,
                        label = "假人类型",
                        value = it
                    )
                }
            }
        }
    }
}

/**
 * 设计参数卡片
 */
@Composable
private fun DesignParametersCard(designParameters: com.childproduct.designassistant.data.model.DesignParameters) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionTitle(title = "📏 设计参数", icon = Icons.Default.Ruler)

            Spacer(modifier = Modifier.height(12.dp))

            ParameterItem(
                icon = Icons.Default.ArrowUpward,
                label = "头枕高度",
                value = designParameters.headrestHeightRange
            )
            ParameterItem(
                icon = Icons.Default.Straighten,
                label = "座宽",
                value = designParameters.seatWidth
            )
            ParameterItem(
                icon = Icons.Default.Widgets,
                label = "盒子Envelope",
                value = designParameters.envelope
            )
            ParameterItem(
                icon = Icons.Default.Shield,
                label = "侧防面积",
                value = designParameters.sideImpactProtectionArea
            )

            // 其他参数
            designParameters.additionalParameters.forEach { (key, value) ->
                ParameterItem(
                    icon = Icons.Default.Info,
                    label = key,
                    value = value
                )
            }
        }
    }
}

/**
 * 测试要求卡片
 */
@Composable
private fun TestRequirementsCard(testRequirements: com.childproduct.designassistant.data.model.TestRequirements) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionTitle(title = "⚖️ 测试要求", icon = Icons.Default.Science)

            Spacer(modifier = Modifier.height(12.dp))

            ParameterItem(
                icon = Icons.Default.DirectionsCar,
                label = "正面",
                value = testRequirements.frontalImpact
            )
            ParameterItem(
                icon = Icons.Default.HeartBroken,
                label = "侧撞胸部压缩",
                value = testRequirements.sideImpactChestCompression
            )
            ParameterItem(
                icon = Icons.Default.FitnessCenter,
                label = "织带强度",
                value = testRequirements.harnessStrength
            )

            // 其他要求
            testRequirements.additionalRequirements.forEach { (key, value) ->
                ParameterItem(
                    icon = Icons.Default.Info,
                    label = key,
                    value = value
                )
            }
        }
    }
}

/**
 * 标准测试项卡片
 */
@Composable
private fun StandardTestItemsCard(testItems: com.childproduct.designassistant.data.model.StandardTestItems) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionTitle(title = "🧪 标准测试项", icon = Icons.Default.Checklist)

            Spacer(modifier = Modifier.height(12.dp))

            ParameterItem(
                icon = Icons.Default.Collision,
                label = "动态碰撞：正碰",
                value = testItems.dynamicFrontal
            )
            ParameterItem(
                icon = Icons.Default.Replay,
                label = "动态碰撞：后碰",
                value = testItems.dynamicRear
            )
            ParameterItem(
                icon = Icons.Default.SwitchRight,
                label = "动态碰撞：侧碰",
                value = testItems.dynamicSide
            )
            ParameterItem(
                icon = Icons.Default.LocalFireDepartment,
                label = "阻燃",
                value = testItems.flammability
            )

            // 其他测试
            testItems.additionalTests.forEach { (key, value) ->
                ParameterItem(
                    icon = Icons.Default.Info,
                    label = key,
                    value = value
                )
            }
        }
    }
}

/**
 * 区块标题
 */
@Composable
private fun SectionTitle(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * 参数项
 */
@Composable
private fun ParameterItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$label：",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

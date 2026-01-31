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
 * 设计建议屏幕
 * 功能：展示基于ECE R129/GB 27887标准的专业设计建议
 */
@Composable
fun DesignSuggestionScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        // 标题
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
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
                            imageVector = Icons.Default.EditNote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "专业设计建议",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "基于ECE R129/GB 27887标准的专业设计指导，包含产品核心设计方向和落地设计细节",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // 设计原则
        item {
            DesignPrinciplesCard()
        }

        // 产品核心设计方向
        item {
            DesignSuggestionProductDesignThemeCard()
        }

        // 落地设计细节
        items(listOf("安全结构设计", "功能设计", "用户体验设计", "材料与工艺")) { category ->
            DesignCategoryCard(categoryName = category)
        }
    }
}

/**
 * 设计原则卡片
 */
@Composable
fun DesignPrinciplesCard() {
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
                    imageVector = Icons.Default.Rule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "设计原则",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Divider()

            DesignPrincipleItem(
                icon = Icons.Default.Security,
                title = "安全第一",
                description = "所有设计决策必须首先符合ECE R129/GB 27887标准要求，确保儿童乘坐安全"
            )
            DesignPrincipleItem(
                icon = Icons.Default.SentimentSatisfied,
                title = "舒适体验",
                description = "在确保安全的前提下，优化座椅舒适性，提升儿童乘坐体验"
            )
            DesignPrincipleItem(
                icon = Icons.Default.CheckCircle,
                title = "易于使用",
                description = "简化安装和使用流程，确保家长能够正确、快速地安装和使用"
            )
            DesignPrincipleItem(
                icon = Icons.Default.Palette,
                title = "美观设计",
                description = "在满足安全和功能的前提下，提供美观的视觉效果和良好的品牌形象"
            )
        }
    }
}

/**
 * 产品设计主题卡片
 */
@Composable
fun DesignSuggestionProductDesignThemeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "产品核心设计方向",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Divider()

            Text(
                text = "主题名称：ECE R129全分组安全适配主题（40-150cm，Group 0+/1/2/3）",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            DesignSuggestionDesignSuggestionInfoRow("覆盖年龄段", "0-12岁")
            DesignSuggestionDesignSuggestionInfoRow("推荐朝向", "后向（优先）→ 前向（根据身高切换）")

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "核心设计细节：",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            BulletPoint("头托自适应调节：覆盖40-150cm全身高区间（适配ECE R129 Group 0+/1/2/3对应的Q系列假人）")
            BulletPoint("双固定结构适配：可切换ISOFIX+支撑腿（满足ECE R129不同分组的安装合规性）")
            BulletPoint("分段式侧撞防护：侧撞块采用EPS+EPP双层吸能结构（符合ECE R129 §5.3.3侧撞要求）")
            BulletPoint("多档位靠背调节：后向135-150度，前向95-105度（确保不同身高儿童的乘坐舒适度）")
        }
    }
}

/**
 * 设计分类卡片
 */
@Composable
fun DesignCategoryCard(categoryName: String) {
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (categoryName) {
                            "安全结构设计" -> Icons.Default.Security
                            "功能设计" -> Icons.Default.Build
                            "用户体验设计" -> Icons.Default.Face
                            "材料与工艺" -> Icons.Default.Category
                            else -> Icons.Default.DesignServices
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = categoryName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
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

            if (expanded) {
                Divider()
                
                Spacer(modifier = Modifier.height(8.dp))

                // 分类具体内容
                when (categoryName) {
                    "安全结构设计" -> SafetyStructureDesignContent()
                    "功能设计" -> FunctionDesignContent()
                    "用户体验设计" -> UserExperienceDesignContent()
                    "材料与工艺" -> MaterialAndCraftContent()
                }
            }
        }
    }
}

/**
 * 安全结构设计内容
 */
@Composable
fun SafetyStructureDesignContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BulletPoint("头托结构：采用高强度PP+EPS+EPP复合结构，提供头部保护和颈部支撑")
        BulletPoint("侧翼设计：侧翼内宽≥380mm，符合ECE R129 §5.3.3要求，提供侧撞防护")
        BulletPoint("底座结构：采用高强度钢制骨架，配合ISOFIX接口，提供稳定的固定方式")
        BulletPoint("支撑腿：长度280-450mm，根据车型自动调节，确保最佳支撑角度")
        BulletPoint("安全带系统：五点式安全带，织带宽度≥20mm，符合GB 6095-2021标准")
    }
}

/**
 * 功能设计内容
 */
@Composable
fun FunctionDesignContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BulletPoint("360°旋转：方便抱孩子上下车，安装方向切换无需拆卸座椅")
        BulletPoint("头托无级调节：头托高度可调节范围10-30cm，适配40-150cm身高儿童")
        BulletPoint("靠背多档调节：后向135-150度（3档），前向95-105度（2档）")
        BulletPoint("侧撞防护可调：侧翼展开角度可调节，适配不同车型车门间隙")
        BulletPoint("座椅可拆洗：面料采用透气网布，支持机洗，便于清洁")
    }
}

/**
 * 用户体验设计内容
 */
@Composable
fun UserExperienceDesignContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BulletPoint("人体工学：座椅曲线符合儿童身体发育特点，提供舒适的乘坐姿势")
        BulletPoint("透气设计：采用3D透气网布，增强通风效果，减少闷热感")
        BulletPoint("人性化细节：防晒遮阳篷、水杯架、储物袋等实用配置")
        BulletPoint("色彩搭配：采用柔和色调，避免过于刺眼的颜色，保护儿童视觉")
        BulletPoint("操作便捷：一键旋转、单手调节头托等便捷操作设计")
    }
}

/**
 * 材料与工艺内容
 */
@Composable
fun MaterialAndCraftContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BulletPoint("PP塑料：座椅主体采用高强度PP塑料，符合GB 6675.4-2014标准")
        BulletPoint("EPS吸能材料：采用EPS吸能泡沫，符合GB/T 10801.1-2021标准")
        BulletPoint("EPP吸能材料：关键部位采用EPP吸能泡沫，符合GB/T 10801.2-2021标准")
        BulletPoint("安全带织带：采用高强度尼龙织带，符合GB 6095-2021标准")
        BulletPoint("面料：采用无毒无味的环保面料，符合GB 18401 B类标准")
    }
}

/**
 * 设计原则项组件
 */
@Composable
fun DesignPrincipleItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 8.dp, top = 2.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * 信息行组件
 */
@Composable
fun DesignSuggestionDesignSuggestionInfoRow(label: String, value: String) {
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

/**
 * 列表项组件
 */
@Composable
fun BulletPoint(text: String) {
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
            text = text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

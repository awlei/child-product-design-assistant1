package com.design.assistant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.design.assistant.model.*

/**
 * 设计结果卡片组件
 * 用于展示生成的专业设计参数
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
                GPS028ParamsSection(gps028Params = gps028Params)
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

/**
 * 兼容性评分徽章
 */
@Composable
fun CompatibilityScoreBadge(
    compatibility: CompatibilityAnalysis,
    modifier: Modifier = Modifier
) {
    val (color, text) = when (compatibility.level) {
        CompatibilityLevel.HIGH -> Color(0xFF4CAF50) to "高兼容"
        CompatibilityLevel.MEDIUM -> Color(0xFFFF9800) to "中兼容"
        CompatibilityLevel.LOW -> Color(0xFFF44336) to "低兼容"
        CompatibilityLevel.INCOMPATIBLE -> Color(0xFFB00020) to "不兼容"
    }

    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${compatibility.score}分",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }
    }
}

/**
 * GPS028参数展示区
 */
@Composable
fun GPS028ParamsSection(
    gps028Params: GPS028Params,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "GPS028设计参数",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

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
                title = "头围",
                value = "${gps028Params.headCircumference}mm",
                modifier = Modifier.weight(1f)
            )
            ParameterCard(
                title = "胸围",
                value = "${gps028Params.chestCircumference}mm",
                modifier = Modifier.weight(1f)
            )
            ParameterCard(
                title = "臀围",
                value = "${gps028Params.hipCircumference}mm",
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

/**
 * 参数卡片
 */
@Composable
fun ParameterCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 建议项目
 */
@Composable
fun RecommendationItem(
    recommendation: DesignRecommendation,
    modifier: Modifier = Modifier
) {
    val priorityColor = when (recommendation.priority) {
        RecommendationPriority.CRITICAL -> MaterialTheme.colorScheme.error
        RecommendationPriority.HIGH -> MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
        RecommendationPriority.MEDIUM -> MaterialTheme.colorScheme.primary
        RecommendationPriority.LOW -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = priorityColor.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = priorityColor
                ) {
                    Text(
                        text = recommendation.priority.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = recommendation.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = recommendation.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

/**
 * 冲突项目
 */
@Composable
fun ConflictItem(
    conflict: StandardConflict,
    modifier: Modifier = Modifier
) {
    val severityColor = when (conflict.severity) {
        ConflictSeverity.HIGH -> MaterialTheme.colorScheme.error
        ConflictSeverity.MEDIUM -> MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
        ConflictSeverity.LOW -> MaterialTheme.colorScheme.primary
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        border = androidx.compose.foundation.BorderStroke(1.dp, severityColor)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "冲突",
                    tint = severityColor,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = "${conflict.standard1.getDisplayName()} vs ${conflict.standard2.getDisplayName()}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = severityColor
                )
            }

            Text(
                text = "问题描述：${conflict.description}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Text(
                text = "建议：${conflict.resolution}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

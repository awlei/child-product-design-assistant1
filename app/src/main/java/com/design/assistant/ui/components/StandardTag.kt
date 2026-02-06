package com.design.assistant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.design.assistant.model.StandardType
import com.design.assistant.ui.theme.*

/**
 * 标准标签组件
 * 用于显示和选择标准
 */
@Composable
fun StandardTag(
    standard: StandardType,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        selected -> Primary.copy(alpha = 0.2f)
        else -> when (standard.getRegion()) {
            "中国" -> ChinaColor.copy(alpha = 0.1f)
            "欧洲" -> EuropeColor.copy(alpha = 0.1f)
            "美国" -> USColor.copy(alpha = 0.1f)
            "加拿大" -> CanadaColor.copy(alpha = 0.1f)
            "澳大利亚/新西兰" -> ANZColor.copy(alpha = 0.1f)
            else -> Color.Transparent
        }
    }

    val borderColor = when {
        selected -> Primary
        else -> when (standard.getRegion()) {
            "中国" -> ChinaColor
            "欧洲" -> EuropeColor
            "美国" -> USColor
            "加拿大" -> CanadaColor
            "澳大利亚/新西兰" -> ANZColor
            else -> MaterialTheme.colorScheme.outline
        }
    }

    val textColor = when {
        selected -> Primary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = borderColor
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 4.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = standard.getDisplayName(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (selected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "已选择",
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = standard.getRegion(),
                style = MaterialTheme.typography.bodySmall,
                color = textColor.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * 标准标签（紧凑版）
 */
@Composable
fun StandardTagCompact(
    standard: StandardType,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        selected -> Primary.copy(alpha = 0.2f)
        else -> when (standard.getRegion()) {
            "中国" -> ChinaColor.copy(alpha = 0.1f)
            "欧洲" -> EuropeColor.copy(alpha = 0.1f)
            "美国" -> USColor.copy(alpha = 0.1f)
            "加拿大" -> CanadaColor.copy(alpha = 0.1f)
            "澳大利亚/新西兰" -> ANZColor.copy(alpha = 0.1f)
            else -> Color.Transparent
        }
    }

    val borderColor = when {
        selected -> Primary
        else -> when (standard.getRegion()) {
            "中国" -> ChinaColor
            "欧洲" -> EuropeColor
            "美国" -> USColor
            "加拿大" -> CanadaColor
            "澳大利亚/新西兰" -> ANZColor
            else -> MaterialTheme.colorScheme.outline
        }
    }

    Surface(
        modifier = modifier
            .clickable(onClick = onClick)
            .height(50.dp),
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = standard.getDisplayName(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Primary else MaterialTheme.colorScheme.onSurface
            )

            if (selected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "已选择",
                    tint = Primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

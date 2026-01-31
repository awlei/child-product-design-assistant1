package com.childproduct.designassistant.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 信息行组件 - 用于显示键值对信息
 * @param label 标签文本
 * @param value 值文本
 * @param variant 样式变体
 */
@Composable
fun InfoRow(
    label: String,
    value: String,
    variant: InfoRowVariant = InfoRowVariant.DEFAULT
) {
    when (variant) {
        InfoRowVariant.DEFAULT -> DefaultInfoRow(label, value)
        InfoRowVariant.COMPACT -> CompactInfoRow(label, value)
    }
}

enum class InfoRowVariant {
    DEFAULT,   // 默认样式（原 TechnicalRecommendationScreen 样式）
    COMPACT    // 紧凑样式（原 CreativeScreen 样式）
}

/**
 * 默认样式信息行 - 用于技术推荐页面
 */
@Composable
private fun DefaultInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1.5f)
        )
    }
}

/**
 * 紧凑样式信息行 - 用于创意生成页面
 */
@Composable
private fun CompactInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "$label：",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

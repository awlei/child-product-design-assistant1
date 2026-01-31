package com.childproduct.designassistant.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.childproduct.designassistant.model.StandardClause

/**
 * 标准条款详情对话框
 * 用于展示条款原文，支持条款直达功能
 */
@Composable
fun StandardClauseDialog(
    clause: StandardClause,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // 标题栏
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LibraryBooks,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "标准条款详情",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "关闭"
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                // 条款内容
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 标准名称和条款ID
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = clause.standardName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = clause.clauseId,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // 条款标题
                    Text(
                        text = clause.clauseTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Divider()

                    // 条款类型标签
                    SuggestionChip(
                        onClick = {},
                        label = { Text(getClauseTypeName(clause.clauseType)) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 条款内容
                    Text(
                        text = "条款原文：",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = clause.clauseContent,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5f
                    )

                    // 相关章节
                    if (clause.relatedSections.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "相关章节：",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        clause.relatedSections.forEach { section ->
                            Text(
                                text = "• $section",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 关闭按钮
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("关闭")
                }
            }
        }
    }
}

/**
 * 获取条款类型名称
 */
private fun getClauseTypeName(type: com.childproduct.designassistant.model.ClauseType): String {
    return when (type) {
        com.childproduct.designassistant.model.ClauseType.REQUIREMENT -> "强制要求"
        com.childproduct.designassistant.model.ClauseType.RECOMMENDATION -> "推荐要求"
        com.childproduct.designassistant.model.ClauseType.TESTING_METHOD -> "测试方法"
        com.childproduct.designassistant.model.ClauseType.ACCEPTANCE_CRITERIA -> "验收标准"
        com.childproduct.designassistant.model.ClauseType.DIMENSIONAL_SPEC -> "尺寸规格"
        com.childproduct.designassistant.model.ClauseType.MATERIAL_SPEC -> "材料标准"
        com.childproduct.designassistant.model.ClauseType.QUALITY_CONTROL -> "质量控制"
        com.childproduct.designassistant.model.ClauseType.USER_INFORMATION -> "用户信息"
        com.childproduct.designassistant.model.ClauseType.CERTIFICATION -> "认证要求"
        com.childproduct.designassistant.model.ClauseType.INSTALLATION -> "安装要求"
    }
}

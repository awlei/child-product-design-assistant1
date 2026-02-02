package com.childproduct.designassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.childproduct.designassistant.model.LearnedDocument
import com.childproduct.designassistant.model.LearningStatus
import com.childproduct.designassistant.ui.MainViewModel

/**
 * 文档学习界面
 * 支持上传专业文档、管理已学习文档、查看学习进度
 */
@Composable
fun DocumentLearningScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedDocuments by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showUploadDialog by remember { mutableStateOf(false) }

    // 模拟已学习的文档列表
    val learnedDocuments = remember {
        listOf(
            LearnedDocument(
                id = "doc_001",
                name = "ECE R129标准文档.pdf",
                uploadDate = "2024-01-15",
                size = "2.5 MB",
                pageCount = 45,
                learningStatus = LearningStatus.COMPLETED,
                progress = 100,
                tags = listOf("标准", "儿童安全座椅", "ECE R129")
            ),
            LearnedDocument(
                id = "doc_002",
                name = "GB 27887-2024儿童安全座椅国家标准.docx",
                uploadDate = "2024-01-18",
                size = "1.8 MB",
                pageCount = 32,
                learningStatus = LearningStatus.COMPLETED,
                progress = 100,
                tags = listOf("标准", "国标", "儿童安全座椅")
            ),
            LearnedDocument(
                id = "doc_003",
                name = "ISO 8124-3玩具安全标准.pdf",
                uploadDate = "2024-01-20",
                size = "3.2 MB",
                pageCount = 28,
                learningStatus = LearningStatus.IN_PROGRESS,
                progress = 75,
                tags = listOf("标准", "玩具安全", "ISO")
            ),
            LearnedDocument(
                id = "doc_004",
                name = "EN 1888婴儿推车标准.pdf",
                uploadDate = "2024-01-22",
                size = "1.5 MB",
                pageCount = 22,
                learningStatus = LearningStatus.PENDING,
                progress = 0,
                tags = listOf("标准", "婴儿推车", "EN")
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 页面标题
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "文档学习",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // 统计卡片
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "已学习文档",
                value = learnedDocuments.count { it.learningStatus == LearningStatus.COMPLETED }.toString(),
                icon = Icons.Default.CheckCircle,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "学习中",
                value = learnedDocuments.count { it.learningStatus == LearningStatus.IN_PROGRESS }.toString(),
                icon = Icons.Default.Sync,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "待学习",
                value = learnedDocuments.count { it.learningStatus == LearningStatus.PENDING }.toString(),
                icon = Icons.Default.Schedule,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 操作按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { showUploadDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.UploadFile,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("上传文档")
            }

            OutlinedButton(
                onClick = { /* TODO: 批量学习 */ },
                modifier = Modifier.weight(1f),
                enabled = selectedDocuments.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("开始学习")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 文档列表标题
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "已上传文档",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "共 ${learnedDocuments.size} 个文档",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 文档列表
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(learnedDocuments) { document ->
                DocumentLearningCard(
                    document = document,
                    isSelected = selectedDocuments.contains(document.id),
                    onSelectedChange = { selected ->
                        selectedDocuments = if (selected) {
                            selectedDocuments + document.id
                        } else {
                            selectedDocuments - document.id
                        }
                    }
                )
            }
        }
    }

    // 上传文档对话框
    if (showUploadDialog) {
        UploadDocumentDialog(
            onDismiss = { showUploadDialog = false },
            onUpload = { documentInfo ->
                // TODO: 实现文档上传逻辑
                showUploadDialog = false
            }
        )
    }
}

/**
 * 统计卡片
 */
@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * 文档学习卡片
 */
@Composable
fun DocumentLearningCard(
    document: LearnedDocument,
    isSelected: Boolean,
    onSelectedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 文档名称和复选框
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.InsertDriveFile,
                        contentDescription = null,
                        tint = getDocumentColor(document.name),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = document.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${document.uploadDate} · ${document.size} · ${document.pageCount}页",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Checkbox(
                    checked = isSelected,
                    onCheckedChange = onSelectedChange
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 标签
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                document.tags.forEach { tag ->
                    SuggestionChip(
                        onClick = {},
                        label = { Text(tag) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 学习进度
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val statusIcon = when (document.learningStatus) {
                    LearningStatus.COMPLETED -> Icons.Default.CheckCircle to MaterialTheme.colorScheme.primary
                    LearningStatus.IN_PROGRESS -> Icons.Default.Sync to MaterialTheme.colorScheme.secondary
                    LearningStatus.PENDING -> Icons.Default.Schedule to MaterialTheme.colorScheme.tertiary
                }
                Icon(
                    imageVector = statusIcon.first,
                    contentDescription = null,
                    tint = statusIcon.second,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = when (document.learningStatus) {
                        LearningStatus.COMPLETED -> "学习完成"
                        LearningStatus.IN_PROGRESS -> "学习中 (${document.progress}%)"
                        LearningStatus.PENDING -> "待学习"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (document.learningStatus == LearningStatus.IN_PROGRESS) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = document.progress / 100f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * 上传文档对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDocumentDialog(
    onDismiss: () -> Unit,
    onUpload: (DocumentInfo) -> Unit
) {
    var documentName by remember { mutableStateOf("") }
    var documentType by remember { mutableStateOf<DocumentType>(DocumentType.PDF) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.UploadFile,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("上传专业文档")
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 文档类型选择
                Text(
                    text = "文档类型",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DocumentType.values().forEach { type ->
                        FilterChip(
                            selected = documentType == type,
                            onClick = { documentType = type },
                            label = { Text(type.displayName) }
                        )
                    }
                }

                // 文档名称输入
                OutlinedTextField(
                    value = documentName,
                    onValueChange = { documentName = it },
                    label = { Text("文档名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 文件选择提示
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Column {
                                Text(
                                    text = "点击选择文件",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "支持PDF、Word、Excel格式",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                        Button(onClick = { /* TODO: 打开文件选择器 */ }) {
                            Text("浏览")
                        }
                    }
                }

                // 支持的文档类型说明
                Text(
                    text = "支持的文档类型：",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Column {
                    Text("• PDF文档：标准法规、技术手册")
                    Text("• Word文档：规范说明、设计指南")
                    Text("• Excel表格：参数表、测试数据")
                    Text("• 文本文件：代码、配置文件")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpload(
                        DocumentInfo(
                            name = documentName,
                            type = documentType
                        )
                    )
                },
                enabled = documentName.isNotBlank()
            ) {
                Text("开始上传")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

/**
 * 获取文档图标颜色
 */
private fun getDocumentColor(fileName: String): androidx.compose.ui.graphics.Color {
    return when {
        fileName.endsWith(".pdf") -> androidx.compose.ui.graphics.Color(0xFFD32F2F)
        fileName.endsWith(".doc", ignoreCase = true) || fileName.endsWith(".docx", ignoreCase = true) -> 
            androidx.compose.ui.graphics.Color(0xFF1976D2)
        fileName.endsWith(".xls", ignoreCase = true) || fileName.endsWith(".xlsx", ignoreCase = true) -> 
            androidx.compose.ui.graphics.Color(0xFF388E3C)
        fileName.endsWith(".txt") -> androidx.compose.ui.graphics.Color(0xFF757575)
        else -> androidx.compose.ui.graphics.Color(0xFF9E9E9E)
    }
}

/**
 * 文档类型枚举
 */
enum class DocumentType(val displayName: String, val extension: String) {
    PDF("PDF文档", ".pdf"),
    WORD("Word文档", ".docx"),
    EXCEL("Excel表格", ".xlsx"),
    TEXT("文本文件", ".txt")
}

/**
 * 文档信息
 */
data class DocumentInfo(
    val name: String,
    val type: DocumentType
)

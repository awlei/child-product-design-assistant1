package com.childproduct.designassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.childproduct.designassistant.model.ChatMessage
import com.childproduct.designassistant.model.MessageSender
import com.childproduct.designassistant.ui.MainViewModel
import com.childproduct.designassistant.ui.UiState
import kotlinx.coroutines.launch

/**
 * 对话问答界面
 * 支持通过对话方式询问专业问题，基于已学习文档回答
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatQAScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // 对话状态
    var inputText by remember { mutableStateOf("") }
    val chatMessages = remember {
        mutableStateListOf(
            ChatMessage(
                id = "msg_001",
                content = "你好！我是儿童产品设计助手AI助手。我已经学习了ECE R129、GB 27887等专业标准文档，可以回答你关于儿童产品设计、标准法规、参数要求等专业问题。\n\n你可以问我：\n• \"ECE R129标准对儿童安全座椅头托调节有什么要求？\"\n• \"GB 27887-2024标准的侧撞防护要求是什么？\"\n• \"儿童安全座椅的ISOFIX接口尺寸是多少？\"",
                sender = MessageSender.AI,
                timestamp = System.currentTimeMillis()
            )
        )
    }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // 自动滚动到底部
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 顶部标题栏
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "智能问答",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 知识库状态指示
            AssistChip(
                onClick = {},
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text("已学习 4 个文档")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 对话消息列表
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(chatMessages) { message ->
                ChatMessageBubble(message = message)
            }
            
            // Loading indicator
            if (uiState is UiState.Loading) {
                item {
                    ChatLoadingBubble()
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 输入区域
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("输入你的专业问题...") },
                    modifier = Modifier.weight(1f),
                    minLines = 1,
                    maxLines = 4,
                    shape = RoundedCornerShape(24.dp)
                )

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            // 发送消息
                            val userMessage = ChatMessage(
                                id = "msg_${System.currentTimeMillis()}",
                                content = inputText,
                                sender = MessageSender.USER,
                                timestamp = System.currentTimeMillis()
                            )
                            chatMessages.add(userMessage)
                            
                            val question = inputText
                            inputText = ""
                            
                            // 模拟AI回答（实际应该调用LLM和知识库检索）
                            scope.launch {
                                kotlinx.coroutines.delay(1000) // 模拟网络延迟
                                
                                val aiResponse = generateAIResponse(question)
                                
                                chatMessages.add(
                                    ChatMessage(
                                        id = "msg_${System.currentTimeMillis()}",
                                        content = aiResponse,
                                        sender = MessageSender.AI,
                                        timestamp = System.currentTimeMillis()
                                    )
                                )
                            }
                        }
                    },
                    enabled = inputText.isNotBlank() && uiState !is UiState.Loading
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "发送",
                        tint = if (inputText.isNotBlank()) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        }
                    )
                }
            }
        }
    }
}

/**
 * 聊天消息气泡
 */
@Composable
fun ChatMessageBubble(message: ChatMessage) {
    val isAI = message.sender == MessageSender.AI
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isAI) Arrangement.Start else Arrangement.End
    ) {
        // AI头像
        if (isAI) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = androidx.compose.foundation.shape.CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            // 消息内容
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(
                    topStart = if (isAI) 4.dp else 16.dp,
                    topEnd = if (isAI) 16.dp else 4.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAI) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    }
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // 时间戳
            Text(
                text = formatTimestamp(message.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.padding(
                    start = if (isAI) 0.dp else 8.dp,
                    top = 4.dp
                )
            )
        }

        // 用户头像
        if (!isAI) {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                modifier = Modifier.size(40.dp),
                shape = androidx.compose.foundation.shape.CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

/**
 * 加载中气泡
 */
@Composable
fun ChatLoadingBubble() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = androidx.compose.foundation.shape.CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Card(
            shape = RoundedCornerShape(
                topStart = 4.dp,
                topEnd = 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
                Text(
                    text = "正在思考...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

/**
 * 格式化时间戳
 */
private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60 * 1000 -> "刚刚"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}小时前"
        else -> {
            val date = java.util.Date(timestamp)
            val format = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
            format.format(date)
        }
    }
}

/**
 * 模拟AI回答生成（实际应该调用LLM和知识库检索）
 */
private fun generateAIResponse(question: String): String {
    val lowerQuestion = question.lowercase()
    
    return when {
        lowerQuestion.contains("头托") && lowerQuestion.contains("调节") -> {
            """根据ECE R129 §5.4.2标准要求：

头托调节范围应至少为154mm，每档调节步长不大于22mm，至少提供7档调节位置。

具体要求：
• 调节范围：≥154mm
• 调节步长：≤22mm/档
• 档位数：≥7档
• 适配性：应根据儿童身高进行调整

来源：ECE R129:2013 §5.4.2 头托适配性要求"""
        }
        
        lowerQuestion.contains("侧撞") || lowerQuestion.contains("侧面") -> {
            """根据GB 27887-2024 §6.4标准要求：

侧撞防护需包含EPS吸能结构，确保侧面碰撞时的保护效果。

具体要求：
• 结构要求：需含EPS吸能结构
• 保护范围：覆盖儿童头部、胸部、腰部
• 测试标准：侧撞台车速度32km/h
• 合格标准：侧面防护结构无破裂；安全带无松脱

来源：GB 27887-2024 §6.4 侧撞防护要求"""
        }
        
        lowerQuestion.contains("isofix") && (lowerQuestion.contains("尺寸") || lowerQuestion.contains("间距")) -> {
            """根据ECE R129 §5.5.1标准要求：

ISOFIX接口间距应为280mm±5mm，接口应能承受至少5000N的拉力。

具体参数：
• 接口间距：280mm±5mm
• 拉力强度：≥5000N
• 持续时间：10秒
• 合格标准：接口无变形、无断裂

来源：ECE R129:2013 §5.5.1 接口尺寸规范"""
        }
        
        lowerQuestion.contains("支撑腿") -> {
            """根据ECE R129 §5.5.3标准要求：

支撑腿长度应可调，调节范围120-200mm，防止座椅在碰撞中向前倾倒。

具体参数：
• 可调范围：120-200mm
• 承载能力：15kg负重
• 稳定性测试：静置30分钟
• 合格标准：无压缩失效，座椅无倾斜

来源：ECE R129:2013 §5.5.3 支撑腿适配要求"""
        }
        
        lowerQuestion.contains("envelope") -> {
            """根据ECE R129标准要求：

Envelope（包络线）尺寸规定了座椅在车辆安装空间内的最大尺寸限制。

具体参数：
• 宽度：≤44cm
• 长度：≤75cm
• 目的：确保座椅在车辆安装空间内
• 应用：所有安装模式

来源：ECE R129:2013 包络线要求"""
        }
        
        else -> {
            """感谢你的提问！我已经学习了ECE R129、GB 27887等专业标准文档。

关于你的问题，我可以提供以下相关信息：

你可以问我更具体的问题，例如：
• "ECE R129标准对儿童安全座椅头托调节有什么要求？"
• "GB 27887-2024标准的侧撞防护要求是什么？"
• "儿童安全座椅的ISOFIX接口尺寸是多少？"
• "支撑腿的可调范围是多少？"
• "Envelope尺寸要求是什么？"

我会基于已学习的标准文档为你提供准确的回答。"""
        }
    }
}

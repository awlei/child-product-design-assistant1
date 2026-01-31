package com.childproduct.designassistant.model

/**
 * 学习状态枚举
 */
enum class LearningStatus {
    PENDING,        // 待学习
    IN_PROGRESS,    // 学习中
    COMPLETED       // 已完成
}

/**
 * 学习文档模型
 * 表示已上传到知识库并学习的专业文档
 */
data class LearnedDocument(
    val id: String,                              // 文档唯一标识
    val name: String,                            // 文档名称
    val uploadDate: String,                      // 上传日期
    val size: String,                            // 文件大小
    val pageCount: Int,                          // 页数
    val learningStatus: LearningStatus,          // 学习状态
    val progress: Int,                           // 学习进度 (0-100)
    val tags: List<String>,                      // 文档标签
    val contentHash: String = "",                // 内容哈希值（用于去重）
    val lastLearningTime: Long = 0L,             // 最后学习时间
    val vectorId: String = "",                   // 向量化存储ID
    val metadata: Map<String, String> = emptyMap() // 额外元数据
) {
    /**
     * 是否已完成学习
     */
    val isCompleted: Boolean
        get() = learningStatus == LearningStatus.COMPLETED
    
    /**
     * 是否正在学习中
     */
    val isInProgress: Boolean
        get() = learningStatus == LearningStatus.IN_PROGRESS
    
    /**
     * 是否等待学习
     */
    val isPending: Boolean
        get() = learningStatus == LearningStatus.PENDING
}

/**
 * 文档块（用于向量化存储）
 */
data class DocumentChunk(
    val id: String,                    // 块ID
    val documentId: String,            // 所属文档ID
    val chunkIndex: Int,               // 块索引
    val content: String,               // 块内容
    val metadata: ChunkMetadata        // 元数据
)

/**
 * 文档块元数据
 */
data class ChunkMetadata(
    val pageNumber: Int,               // 页码
    val sectionTitle: String?,         // 章节标题
    val tags: List<String>,            // 标签
    val embeddingVector: List<Float>? = null // 向量嵌入（如果已计算）
)

/**
 * 消息发送者枚举
 */
enum class MessageSender {
    USER,   // 用户
    AI,     // AI助手
    SYSTEM  // 系统消息
}

/**
 * 聊天消息模型
 */
data class ChatMessage(
    val id: String,                        // 消息ID
    val content: String,                   // 消息内容
    val sender: MessageSender,             // 发送者
    val timestamp: Long,                   // 时间戳
    val referencedDocuments: List<String> = emptyList(), // 引用的文档ID列表
    val metadata: Map<String, Any> = emptyMap() // 额外元数据
) {
    /**
     * 是否为用户消息
     */
    val isUserMessage: Boolean
        get() = sender == MessageSender.USER
    
    /**
     * 是否为AI消息
     */
    val isAIMessage: Boolean
        get() = sender == MessageSender.AI
    
    /**
     * 是否为系统消息
     */
    val isSystemMessage: Boolean
        get() = sender == MessageSender.SYSTEM
}

/**
 * 检索结果模型
 */
data class SearchResult(
    val chunk: DocumentChunk,              // 检索到的文档块
    val relevanceScore: Float,             // 相关性分数 (0-1)
    val documentName: String,              // 文档名称
    val pageNumber: Int,                   // 页码
    val highlight: String                  // 高亮显示的片段
) {
    /**
     * 是否具有高相关性
     */
    val isHighlyRelevant: Boolean
        get() = relevanceScore >= 0.8f
    
    /**
     * 是否具有中等相关性
     */
    val isModeratelyRelevant: Boolean
        get() = relevanceScore in 0.5f..0.8f
}

/**
 * 知识库统计信息
 */
data class KnowledgeBaseStats(
    val totalDocuments: Int = 0,           // 总文档数
    val completedDocuments: Int = 0,       // 已学习文档数
    val inProgressDocuments: Int = 0,      // 学习中文档数
    val pendingDocuments: Int = 0,         // 待学习文档数
    val totalChunks: Int = 0,              // 总块数
    val totalMessages: Int = 0,            // 总消息数
    val lastUpdateTime: Long = 0L          // 最后更新时间
) {
    /**
     * 学习完成率
     */
    val completionRate: Float
        get() = if (totalDocuments > 0) {
            completedDocuments.toFloat() / totalDocuments.toFloat()
        } else {
            0f
        }
    
    /**
     * 是否有文档在学习中
     */
    val hasInProgressDocuments: Boolean
        get() = inProgressDocuments > 0
    
    /**
     * 是否有待学习文档
     */
    val hasPendingDocuments: Boolean
        get() = pendingDocuments > 0
}

/**
 * 文档学习任务
 */
data class DocumentLearningTask(
    val id: String,                        // 任务ID
    val documentId: String,                // 文档ID
    val documentName: String,              // 文档名称
    val status: LearningStatus,            // 任务状态
    val progress: Int,                     // 进度 (0-100)
    val startTime: Long,                   // 开始时间
    val estimatedEndTime: Long,            // 预计结束时间
    val errorMessage: String? = null,     // 错误信息
    val currentStep: String = ""           // 当前步骤
)

/**
 * 搜索查询参数
 */
data class SearchQuery(
    val query: String,                     // 查询文本
    val topK: Int = 5,                     // 返回前K个结果
    val minScore: Float = 0.5f,            // 最小相关性分数
    val documentIds: List<String>? = null, // 限制在指定文档中搜索
    val tags: List<String>? = null         // 限制在指定标签中搜索
)

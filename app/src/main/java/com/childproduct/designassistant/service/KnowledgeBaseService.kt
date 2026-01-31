package com.childproduct.designassistant.service

import com.childproduct.designassistant.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.math.max

/**
 * 知识库管理服务
 * 
 * 注意：由于 coze-coding-dev-sdk 只能在后端 Python 环境中使用，
 * 此类提供本地模拟实现。在生产环境中，应该通过后端 API 调用
 * 来实现真正的知识库功能。
 */
class KnowledgeBaseService {
    
    companion object {
        private const val DEFAULT_DATASET = "coze_doc_knowledge"
        private const val CHUNK_SIZE = 500 // 每个块的最大字符数
    }
    
    // 本地模拟存储
    private val documents = mutableMapOf<String, LearnedDocument>()
    private val chunks = mutableListOf<DocumentChunk>()
    private val messages = mutableListOf<ChatMessage>()
    
    // 初始化模拟数据
    init {
        initializeMockData()
    }
    
    /**
     * 添加文档到知识库
     */
    suspend fun addDocument(
        name: String,
        content: String,
        tags: List<String> = emptyList(),
        onProgress: (Int) -> Unit = {}
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val documentId = "doc_${UUID.randomUUID().toString().substring(0, 8)}"
            
            // 创建文档
            val document = LearnedDocument(
                id = documentId,
                name = name,
                uploadDate = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Date()),
                size = "${content.length / 1024.0} KB",
                pageCount = max(1, content.length / 2000), // 估算页数
                learningStatus = LearningStatus.IN_PROGRESS,
                progress = 0,
                tags = tags
            )
            
            documents[documentId] = document
            onProgress(10)
            
            // 分块处理内容
            val chunkedContent = splitIntoChunks(content)
            val docChunks = chunkedContent.mapIndexed { index, chunk ->
                DocumentChunk(
                    id = "chunk_${UUID.randomUUID().toString().substring(0, 8)}",
                    documentId = documentId,
                    chunkIndex = index,
                    content = chunk,
                    metadata = ChunkMetadata(
                        pageNumber = (index / 5) + 1, // 每5个块算一页
                        sectionTitle = null,
                        tags = tags
                    )
                )
            }
            
            chunks.addAll(docChunks)
            onProgress(80)
            
            // 模拟向量化处理
            kotlinx.coroutines.delay(500)
            onProgress(100)
            
            // 更新文档状态
            val completedDoc = document.copy(
                learningStatus = LearningStatus.COMPLETED,
                progress = 100,
                lastLearningTime = System.currentTimeMillis(),
                vectorId = "vec_${UUID.randomUUID().toString().substring(0, 8)}"
            )
            documents[documentId] = completedDoc
            
            Result.success(documentId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 搜索知识库
     */
    suspend fun search(
        query: String,
        topK: Int = 5,
        minScore: Float = 0.5f,
        documentIds: List<String>? = null,
        tags: List<String>? = null
    ): Result<List<SearchResult>> = withContext(Dispatchers.IO) {
        try {
            // 模拟搜索延迟
            kotlinx.coroutines.delay(300)
            
            val filteredChunks = chunks.filter { chunk ->
                // 按文档ID过滤
                val documentMatch = documentIds?.let { it.contains(chunk.documentId) } ?: true
                
                // 按标签过滤
                val tagMatch = tags?.let { tags ->
                    tags.any { tag -> chunk.metadata.tags.contains(tag) }
                } ?: true
                
                documentMatch && tagMatch
            }
            
            // 计算相关性分数（模拟）
            val results = filteredChunks.map { chunk ->
                val score = calculateRelevanceScore(query, chunk.content)
                SearchResult(
                    chunk = chunk,
                    relevanceScore = score,
                    documentName = documents[chunk.documentId]?.name ?: "未知文档",
                    pageNumber = chunk.metadata.pageNumber,
                    highlight = extractHighlight(chunk.content, query)
                )
            }
            .filter { it.relevanceScore >= minScore }
            .sortedByDescending { it.relevanceScore }
            .take(topK)
            
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 获取所有文档
     */
    suspend fun getAllDocuments(): Result<List<LearnedDocument>> = withContext(Dispatchers.IO) {
        try {
            Result.success(documents.values.toList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 获取文档统计信息
     */
    suspend fun getStatistics(): Result<KnowledgeBaseStats> = withContext(Dispatchers.IO) {
        try {
            val docs = documents.values
            val stats = KnowledgeBaseStats(
                totalDocuments = docs.size,
                completedDocuments = docs.count { it.isCompleted },
                inProgressDocuments = docs.count { it.isInProgress },
                pendingDocuments = docs.count { it.isPending },
                totalChunks = chunks.size,
                totalMessages = messages.size,
                lastUpdateTime = System.currentTimeMillis()
            )
            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 保存聊天消息
     */
    suspend fun saveMessage(message: ChatMessage): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            messages.add(message)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 获取聊天历史
     */
    suspend fun getChatHistory(limit: Int = 50): Result<List<ChatMessage>> = withContext(Dispatchers.IO) {
        try {
            val history = messages.takeLast(limit)
            Result.success(history)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 清空聊天历史
     */
    suspend fun clearChatHistory(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            messages.clear()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 删除文档
     */
    suspend fun deleteDocument(documentId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            documents.remove(documentId)
            chunks.removeAll { it.documentId == documentId }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ============ 私有辅助方法 ============
    
    /**
     * 将文本分割成块
     */
    private fun splitIntoChunks(content: String, chunkSize: Int = CHUNK_SIZE): List<String> {
        val chunks = mutableListOf<String>()
        var start = 0
        
        while (start < content.length) {
            val end = minOf(start + chunkSize, content.length)
            var splitPoint = end
            
            // 尝试在句号或换行符处分割
            if (end < content.length) {
                val lastPeriod = content.lastIndexOf('.', end - 1)
                val lastNewline = content.lastIndexOf('\n', end - 1)
                
                splitPoint = maxOf(lastPeriod, lastNewline).let {
                    if (it > start) it + 1 else end
                }
            }
            
            chunks.add(content.substring(start, splitPoint).trim())
            start = splitPoint
        }
        
        return chunks.filter { it.isNotBlank() }
    }
    
    /**
     * 计算相关性分数（模拟）
     * 在实际应用中，应该使用真实的向量相似度计算
     */
    private fun calculateRelevanceScore(query: String, content: String): Float {
        val queryLower = query.lowercase()
        val contentLower = content.lowercase()
        
        val queryWords = queryLower.split(" ", "，", "。", "？", "！")
            .filter { it.isNotBlank() }
        
        if (queryWords.isEmpty()) return 0f
        
        var matches = 0
        for (word in queryWords) {
            if (contentLower.contains(word)) {
                matches++
            }
        }
        
        val baseScore = matches.toFloat() / queryWords.size
        
        // 调整分数：根据匹配位置给予额外分数
        val firstMatchIndex = queryWords.mapNotNull { word -> contentLower.indexOf(word).takeIf { it >= 0 } }.minOrNull() ?: -1
        val positionBonus = if (firstMatchIndex >= 0 && firstMatchIndex < 100) 0.2f else 0f
        
        return (baseScore + positionBonus).coerceAtMost(1.0f)
    }
    
    /**
     * 提取高亮片段
     */
    private fun extractHighlight(content: String, query: String): String {
        val queryLower = query.lowercase()
        val contentLower = content.lowercase()
        
        val matchIndex = contentLower.indexOf(queryLower.take(5)) // 只使用查询的前5个字符
        if (matchIndex < 0) return content.take(100) + "..."
        
        val start = maxOf(0, matchIndex - 20)
        val end = minOf(content.length, matchIndex + query.length + 80)
        
        val prefix = if (start > 0) "..." else ""
        val suffix = if (end < content.length) "..." else ""
        
        return prefix + content.substring(start, end) + suffix
    }
    
    /**
     * 初始化模拟数据
     */
    private fun initializeMockData() {
        // 添加模拟文档
        val standardDocuments = listOf(
            LearnedDocument(
                id = "doc_001",
                name = "ECE R129标准文档.pdf",
                uploadDate = "2024-01-15",
                size = "2.5 MB",
                pageCount = 45,
                learningStatus = LearningStatus.COMPLETED,
                progress = 100,
                tags = listOf("标准", "儿童安全座椅", "ECE R129"),
                vectorId = "vec_001"
            ),
            LearnedDocument(
                id = "doc_002",
                name = "GB 27887-2024儿童安全座椅国家标准.docx",
                uploadDate = "2024-01-18",
                size = "1.8 MB",
                pageCount = 32,
                learningStatus = LearningStatus.COMPLETED,
                progress = 100,
                tags = listOf("标准", "国标", "儿童安全座椅"),
                vectorId = "vec_002"
            ),
            LearnedDocument(
                id = "doc_003",
                name = "ISO 8124-3玩具安全标准.pdf",
                uploadDate = "2024-01-20",
                size = "3.2 MB",
                pageCount = 28,
                learningStatus = LearningStatus.COMPLETED,
                progress = 100,
                tags = listOf("标准", "玩具安全", "ISO"),
                vectorId = "vec_003"
            )
        )
        
        standardDocuments.forEach { documents[it.id] = it }
        
        // 添加模拟文档块
        val standardContent = """
            根据ECE R129 §5.4.2标准要求：
            
            头托调节范围应至少为154mm，每档调节步长不大于22mm，至少提供7档调节位置。
            
            具体要求：
            • 调节范围：≥154mm
            • 调节步长：≤22mm/档
            • 档位数：≥7档
            • 适配性：应根据儿童身高进行调整
            
            来源：ECE R129:2013 §5.4.2 头托适配性要求
        """.trimIndent()
        
        chunks.add(
            DocumentChunk(
                id = "chunk_001",
                documentId = "doc_001",
                chunkIndex = 0,
                content = standardContent,
                metadata = ChunkMetadata(
                    pageNumber = 1,
                    sectionTitle = "头托适配性",
                    tags = listOf("标准", "儿童安全座椅", "ECE R129", "头托")
                )
            )
        )
        
        val sideImpactContent = """
            根据GB 27887-2024 §6.4标准要求：
            
            侧撞防护需包含EPS吸能结构，确保侧面碰撞时的保护效果。
            
            具体要求：
            • 结构要求：需含EPS吸能结构
            • 保护范围：覆盖儿童头部、胸部、腰部
            • 测试标准：侧撞台车速度32km/h
            • 合格标准：侧面防护结构无破裂；安全带无松脱
            
            来源：GB 27887-2024 §6.4 侧撞防护要求
        """.trimIndent()
        
        chunks.add(
            DocumentChunk(
                id = "chunk_002",
                documentId = "doc_002",
                chunkIndex = 0,
                content = sideImpactContent,
                metadata = ChunkMetadata(
                    pageNumber = 1,
                    sectionTitle = "侧撞防护",
                    tags = listOf("标准", "国标", "儿童安全座椅", "侧撞")
                )
            )
        )
        
        val isofixContent = """
            根据ECE R129 §5.5.1标准要求：
            
            ISOFIX接口间距应为280mm±5mm，接口应能承受至少5000N的拉力。
            
            具体参数：
            • 接口间距：280mm±5mm
            • 拉力强度：≥5000N
            • 持续时间：10秒
            • 合格标准：接口无变形、无断裂
            
            来源：ECE R129:2013 §5.5.1 接口尺寸规范
        """.trimIndent()
        
        chunks.add(
            DocumentChunk(
                id = "chunk_003",
                documentId = "doc_001",
                chunkIndex = 1,
                content = isofixContent,
                metadata = ChunkMetadata(
                    pageNumber = 2,
                    sectionTitle = "接口尺寸规范",
                    tags = listOf("标准", "儿童安全座椅", "ECE R129", "ISOFIX")
                )
            )
        )
    }
}

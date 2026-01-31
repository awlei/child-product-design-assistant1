package com.childproduct.designassistant.service

import com.childproduct.designassistant.model.LearningStatus
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

/**
 * KnowledgeBaseService 单元测试
 */
class KnowledgeBaseServiceTest {
    
    private val service = KnowledgeBaseService()
    
    @Test
    fun `测试初始化模拟数据`() = runBlocking {
        val docs = service.getAllDocuments()
        assertTrue(docs.isSuccess)
        assertTrue(docs.getOrNull()?.size!! > 0)
    }
    
    @Test
    fun `测试添加文档`() = runBlocking {
        val result = service.addDocument(
            name = "测试文档",
            content = "这是一个测试文档的内容。包含一些测试数据用于验证知识库功能。",
            tags = listOf("测试", "验证")
        )
        assertTrue(result.isSuccess)
        
        val docId = result.getOrNull()
        assertNotNull(docId)
        
        val docs = service.getAllDocuments()
        assertTrue(docs.getOrNull()?.any { it.id == docId } == true)
    }
    
    @Test
    fun `测试搜索功能`() = runBlocking {
        val result = service.search(
            query = "头托调节",
            topK = 5,
            minScore = 0.5f
        )
        assertTrue(result.isSuccess)
        
        val searchResults = result.getOrNull()
        assertNotNull(searchResults)
        assertTrue(searchResults!!.size > 0)
        assertTrue(searchResults[0].relevanceScore >= 0.5f)
    }
    
    @Test
    fun `测试统计信息`() = runBlocking {
        val statsResult = service.getStatistics()
        assertTrue(statsResult.isSuccess)
        
        val stats = statsResult.getOrNull()
        assertNotNull(stats)
        assertTrue(stats!!.totalDocuments > 0)
        assertTrue(stats.completedDocuments > 0)
    }
    
    @Test
    fun `测试删除文档`() = runBlocking {
        // 先添加一个文档
        val addResult = service.addDocument(
            name = "待删除文档",
            content = "这个文档将被删除",
            tags = listOf("测试")
        )
        assertTrue(addResult.isSuccess)
        val docId = addResult.getOrNull()
        
        // 删除文档
        val deleteResult = service.deleteDocument(docId!!)
        assertTrue(deleteResult.isSuccess)
        
        // 验证文档已删除
        val docs = service.getAllDocuments()
        assertFalse(docs.getOrNull()?.any { it.id == docId } == true)
    }
}

package com.design.assistant.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.Date

/**
 * 设计结果数据类
 * 包含生成的专业设计参数和兼容性分析
 */
@Serializable
data class DesignResult(
    // 基础信息
    val id: String = "",                      // 结果ID（唯一标识）
    val productType: ProductType,             // 产品类型
    val standards: List<StandardType>,        // 适用的标准列表
    val generatedAt: Long = System.currentTimeMillis(),  // 生成时间戳

    // 儿童参数
    val childHeight: Int = 0,                 // 儿童身高（1-150cm）
    val childWeight: Int = 0,                 // 儿童体重（1-50kg）

    // GPS028参数（如果是儿童安全座椅）
    val gps028Params: GPS028Params? = null,

    // 兼容性分析
    val compatibility: CompatibilityAnalysis? = null,

    // 设计建议
    val designRecommendations: List<DesignRecommendation> = emptyList(),

    // 标准冲突提示
    val conflicts: List<StandardConflict> = emptyList(),

    // 额外参数（根据产品类型动态添加，使用String作为值类型以支持序列化）
    val additionalParams: Map<String, String> = emptyMap()
) {
    /**
     * 生成设计报告文本
     */
    fun generateReport(): String {
        return buildString {
            // 标题
            appendLine("📦 ${productType.getDisplayName()}设计方案（严格遵守${standards.firstOrNull()?.getDisplayName() ?: "自定义标准"}）")

            // 标准信息
            appendLine("【适用标准】")
            standards.forEach { standard ->
                appendLine("${standard.getDisplayName()}")
            }
            appendLine("标准版本：2024版 | 实施要求：${if (standards.any { it.getRegion() == "中国" }) "中国强制实施" else "国际标准推荐"}")
            appendLine("🔍 核心要求：动态碰撞三向覆盖，侧防系统强制，ISOFIX接口兼容ISO 14530-3")
            appendLine()

            // GPS028参数
            if (gps028Params != null) {
                appendLine(gps028Params.generateDesignReport())
                appendLine()
            }

            // 兼容性分析
            if (compatibility != null) {
                appendLine("【兼容性分析】")
                appendLine("兼容性评分：${compatibility.score}/100")
                appendLine("兼容性等级：${compatibility.level.name}")
                appendLine("主要兼容问题：")
                compatibility.issues.forEach { issue ->
                    appendLine("  - $issue")
                }
                appendLine()
            }

            // 设计建议
            if (designRecommendations.isNotEmpty()) {
                appendLine("【设计建议】")
                designRecommendations.forEachIndexed { index, recommendation ->
                    appendLine("${index + 1}. [${recommendation.category}] ${recommendation.title}")
                    appendLine("   ${recommendation.description}")
                    appendLine("   优先级：${recommendation.priority.name}")
                    appendLine()
                }
            }

            // 标准冲突
            if (conflicts.isNotEmpty()) {
                appendLine("【标准冲突提示】")
                conflicts.forEach { conflict ->
                    appendLine("冲突：${conflict.standard1.getDisplayName()} vs ${conflict.standard2.getDisplayName()}")
                    appendLine("  - 问题描述：${conflict.description}")
                    appendLine("  - 建议解决方案：${conflict.resolution}")
                    appendLine()
                }
            }
        }
    }

    /**
     * 转换为JSON格式
     */
    fun toJson(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "productType" to productType.name,
            "standards" to standards.map { it.name },
            "generatedAt" to generatedAt,
            "gps028Params" to (gps028Params?.toJson() ?: emptyMap<Any, Any>()),
            "compatibility" to (compatibility?.toMap() ?: emptyMap<Any, Any>()),
            "designRecommendations" to designRecommendations.map { it.toMap() },
            "conflicts" to conflicts.map { it.toMap() },
            "additionalParams" to additionalParams
        )
    }
}

/**
 * 兼容性分析
 */
@Serializable
data class CompatibilityAnalysis(
    val score: Int,                           // 兼容性评分（0-100）
    val level: CompatibilityLevel,            // 兼容性等级
    val issues: List<String> = emptyList(),   // 兼容性问题列表
    val compatibleStandards: List<StandardType> = emptyList()  // 兼容的标准列表
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "score" to score,
            "level" to level.name,
            "issues" to issues,
            "compatibleStandards" to compatibleStandards.map { it.name }
        )
    }
}

/**
 * 兼容性等级
 */
enum class CompatibilityLevel {
    HIGH,          // 高兼容性（90-100）
    MEDIUM,        // 中兼容性（70-89）
    LOW,           // 低兼容性（50-69）
    INCOMPATIBLE   // 不兼容（<50）
}

/**
 * 设计建议
 */
@Serializable
data class DesignRecommendation(
    val category: String,                     // 建议类别
    val title: String,                        // 建议标题
    val description: String,                  // 建议描述
    val priority: RecommendationPriority,     // 优先级
    val applicableStandards: List<StandardType> = emptyList()  // 适用标准
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "category" to category,
            "title" to title,
            "description" to description,
            "priority" to priority.name,
            "applicableStandards" to applicableStandards.map { it.name }
        )
    }
}

/**
 * 建议优先级
 */
enum class RecommendationPriority {
    CRITICAL,   // 关键（必须）
    HIGH,       // 高（强烈建议）
    MEDIUM,     // 中（建议）
    LOW         // 低（可选）
}

/**
 * 标准冲突
 */
@Serializable
data class StandardConflict(
    val standard1: StandardType,              // 标准1
    val standard2: StandardType,              // 标准2
    val description: String,                  // 冲突描述
    val severity: ConflictSeverity,           // 严重程度
    val resolution: String                    // 解决方案
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "standard1" to standard1.name,
            "standard2" to standard2.name,
            "description" to description,
            "severity" to severity.name,
            "resolution" to resolution
        )
    }
}

/**
 * 冲突严重程度
 */
enum class ConflictSeverity {
    HIGH,        // 高（严重影响设计）
    MEDIUM,      // 中（需要调整设计）
    LOW          // 低（轻微影响）
}

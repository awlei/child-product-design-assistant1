package com.childproduct.designassistant.service

import com.childproduct.designassistant.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DocumentService {

    suspend fun generateDesignDocument(
        productName: String,
        creativeIdea: CreativeIdea? = null,
        safetyCheck: SafetyCheck? = null
    ): DesignDocument = withContext(Dispatchers.IO) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        val sections = mutableListOf<DocumentSection>()

        // 1. 产品概述
        sections.add(
            DocumentSection(
                title = "产品概述",
                content = generateProductOverview(productName, creativeIdea),
                order = 1
            )
        )

        // 2. 设计理念
        sections.add(
            DocumentSection(
                title = "设计理念",
                content = generateDesignPhilosophy(creativeIdea),
                order = 2
            )
        )

        // 3. 功能特性
        sections.add(
            DocumentSection(
                title = "功能特性",
                content = generateFeatureList(creativeIdea),
                order = 3
            )
        )

        // 4. 色彩方案
        sections.add(
            DocumentSection(
                title = "色彩方案",
                content = generateColorScheme(creativeIdea),
                order = 4
            )
        )

        // 5. 安全性说明
        sections.add(
            DocumentSection(
                title = "安全性说明",
                content = generateSafetyDescription(creativeIdea, safetyCheck),
                order = 5
            )
        )

        // 6. 目标用户
        sections.add(
            DocumentSection(
                title = "目标用户",
                content = generateTargetUser(creativeIdea),
                order = 6
            )
        )

        // 7. 使用建议
        sections.add(
            DocumentSection(
                title = "使用建议",
                content = generateUsageRecommendations(creativeIdea),
                order = 7
            )
        )

        // 8. 版本信息
        sections.add(
            DocumentSection(
                title = "版本信息",
                content = "版本号: 1.0.0\n创建日期: $currentDate\n状态: 设计阶段",
                order = 8
            )
        )

        DesignDocument(
            id = UUID.randomUUID().toString(),
            productName = productName,
            version = "1.0.0",
            createdDate = currentDate,
            sections = sections,
            images = emptyList()
        )
    }

    private fun generateProductOverview(productName: String, idea: CreativeIdea?): String {
        if (idea == null) {
            return "产品名称: $productName\n\n待补充详细信息"
        }

        return """产品名称: $productName
产品类型: ${idea.productType.displayName}
目标年龄段: ${idea.ageGroup.displayName}
设计主题: ${idea.theme}

产品描述:
${idea.description}

本产品专为${idea.ageGroup.displayName}儿童设计，融合${idea.theme}设计理念，注重安全性、教育性和趣味性的完美结合。""".trimIndent()
    }

    private fun generateDesignPhilosophy(idea: CreativeIdea?): String {
        if (idea == null) return "待补充设计理念"

        return """设计核心:
以儿童为中心，关注儿童的身心发展需求，打造既安全又有趣的产品。

设计原则:
1. 安全第一: 所有设计均符合儿童产品安全标准
2. 启发思维: 通过互动设计激发儿童创造力和想象力
3. 品质保证: 使用环保材料，确保产品耐用性
4. 适龄设计: 充分考虑${idea.ageGroup.displayName}儿童的认知和操作能力

设计主题: ${idea.theme}
通过${idea.theme}的设计语言，为儿童创造一个富有吸引力的使用环境。""".trimIndent()
    }

    private fun generateFeatureList(idea: CreativeIdea?): String {
        if (idea == null) return "待补充功能特性"

        val features = idea.features.mapIndexed { index, feature ->
            "${index + 1}. $feature"
        }.joinToString("\n")

        return """核心功能:
$features

功能说明:
每个功能都经过精心设计，旨在提升儿童的使用体验，同时兼顾教育价值和娱乐性。""".trimIndent()
    }

    private fun generateColorScheme(idea: CreativeIdea?): String {
        if (idea == null) return "待补充色彩方案"

        val colors = idea.colorPalette.mapIndexed { index, color ->
            "${index + 1}. $color"
        }.joinToString("\n")

        return """色彩方案:
$colors

色彩说明:
所选配色方案基于${idea.ageGroup.displayName}儿童的色彩偏好研究，采用明亮、友好的色调，有助于营造愉悦的使用氛围。""".trimIndent()
    }

    private fun generateSafetyDescription(
        idea: CreativeIdea?,
        safetyCheck: SafetyCheck?
    ): String {
        var content = ""

        // 创意中的安全提示
        if (idea != null) {
            content += "设计阶段安全考量:\n"
            idea.safetyNotes.forEachIndexed { index, note ->
                content += "${index + 1}. $note\n"
            }
            content += "\n"
        }

        // 安全检查结果
        if (safetyCheck != null) {
            content += """安全检查结果:
总体评分: ${safetyCheck.overallScore}分
检查状态: ${if (safetyCheck.passed) "✅ 通过" else "❌ 未通过"}

检查项:
"""
            safetyCheck.checks.forEach { check ->
                val statusIcon = when (check.status) {
                    CheckStatus.PASSED -> "✅"
                    CheckStatus.WARNING -> "⚡"
                    CheckStatus.FAILED -> "❌"
                    CheckStatus.NOT_APPLICABLE -> "➖"
                }
                content += "$statusIcon ${check.itemName}: ${check.notes}\n"
            }
        } else {
            content += "安全检查: 待进行"
        }

        return content.trimIndent()
    }

    private fun generateTargetUser(idea: CreativeIdea?): String {
        if (idea == null) return "待补充目标用户信息"

        return """目标年龄: ${idea.ageGroup.displayName}

用户特征:
该年龄段儿童的主要特点包括:
- 认知能力: ${getAgeGroupCharacteristics(idea.ageGroup).cognitive}
- 身体发展: ${getAgeGroupCharacteristics(idea.ageGroup).physical}
- 社会性: ${getAgeGroupCharacteristics(idea.ageGroup).social}
- 兴趣爱好: ${getAgeGroupCharacteristics(idea.ageGroup).interests}

产品设计充分考虑了以上特点，确保产品能够满足目标用户的需求。"""
    }

    private fun generateUsageRecommendations(idea: CreativeIdea?): String {
        return """使用建议:

1. 成人 supervision
   - 对于${if (idea != null && idea.ageGroup in listOf(AgeGroup.INFANT, AgeGroup.TODDLER)) "低龄儿童" else "较小儿童"}，建议在成人监护下使用

2. 定期检查
   - 定期检查产品完整性，发现损坏及时更换

3. 清洁保养
   - 使用温和清洁剂清洁，避免浸泡

4. 妥善保管
   - 不使用时请妥善保管，避免丢失零件

5. 教育指导
   - 首次使用时，向儿童介绍正确的使用方法"""
    }

    private fun getAgeGroupCharacteristics(ageGroup: AgeGroup): AgeCharacteristics {
        return when (ageGroup) {
            AgeGroup.ALL -> AgeCharacteristics(
                "全年龄段通用，需综合考虑各年龄段的特征",
                "生长发育差异大，需可调节设计",
                "需求多样化，需兼顾安全和舒适",
                "适合多功能、可调节的产品设计"
            )
            AgeGroup.INFANT -> AgeCharacteristics(
                "主要通过感官探索世界，对颜色和声音敏感",
                "抓握能力正在发展，大运动开始发展",
                "以自我为中心，开始建立情感依恋",
                "喜欢鲜艳色彩、柔和声音、可抓握的物体"
            )
            AgeGroup.TODDLER -> AgeCharacteristics(
                "语言能力快速发展，开始理解简单指令",
                "行走能力稳定，精细动作开始发展",
                "开始模仿他人，初步社交意识",
                "喜欢模仿、简单游戏、探索周围环境"
            )
            AgeGroup.PRESCHOOL -> AgeCharacteristics(
                "好奇心强，开始问为什么",
                "协调性提高，可以进行复杂动作",
                "开始与同伴互动，学习分享",
                "喜欢角色扮演、创造性活动、故事"
            )
            AgeGroup.SCHOOL_AGE -> AgeCharacteristics(
                "逻辑思维发展，能够进行推理",
                "动作精确，可以进行精细操作",
                "团队意识增强，开始竞争",
                "喜欢挑战、探索、学习新技能"
            )
            AgeGroup.TEEN -> AgeCharacteristics(
                "抽象思维成熟，能够进行复杂分析",
                "身体发育接近成人，力量增强",
                "重视同伴关系，追求个性化",
                "喜欢社交、科技、运动、创意表达"
            )
        }
    }
}

data class AgeCharacteristics(
    val cognitive: String,
    val physical: String,
    val social: String,
    val interests: String
)

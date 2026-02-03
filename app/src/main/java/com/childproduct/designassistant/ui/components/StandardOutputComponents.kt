package com.childproduct.designassistant.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.childproduct.designassistant.model.CreativeIdea
import com.childproduct.designassistant.data.GPS028Database
import com.childproduct.designassistant.data.OtherProductTypesDatabase

/**
 * 标准专属输出组件
 * 按标准类型（美标/欧标/国标）分组展示设计参数
 */
@Composable
fun StandardOutputCard(
    standardType: com.childproduct.designassistant.data.StandardType,
    allMatchedDummies: List<com.childproduct.designassistant.data.GPS028DummyData>,
    ageGroup: com.childproduct.designassistant.model.AgeGroup,
    heightRange: String
) {
    // 按标准类型过滤假人
    val standardDummies = getDummiesByStandardType(allMatchedDummies, standardType)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (standardType) {
                com.childproduct.designassistant.data.StandardType.ECE_R129 -> 
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                com.childproduct.designassistant.data.StandardType.FMVSS_213 -> 
                    Color(0xFFE3F2FD).copy(alpha = 0.5f)
                com.childproduct.designassistant.data.StandardType.GB_27887 -> 
                    Color(0xFFFFF3E0).copy(alpha = 0.5f)
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = when (standardType) {
                com.childproduct.designassistant.data.StandardType.ECE_R129 -> 
                    MaterialTheme.colorScheme.primary
                com.childproduct.designassistant.data.StandardType.FMVSS_213 -> 
                    Color(0xFF1976D2)
                com.childproduct.designassistant.data.StandardType.GB_27887 -> 
                    Color(0xFFFF6F00)
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 标准标识标签
            StandardLabel(standardType)
            
            // 基础适配数据
            StandardBasicDataBlock(standardDummies, standardType, ageGroup, heightRange)
            
            // 核心设计参数
            StandardDesignParametersBlock(standardDummies, standardType)
            
            // 合规约束
            StandardComplianceConstraintsBlock(standardDummies, standardType)
            
            // 测试项
            StandardTestItemsBlock(standardDummies, standardType)
        }
    }
}

/**
 * 标准标签组件
 */
@Composable
fun StandardLabel(standardType: com.childproduct.designassistant.data.StandardType) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = when (standardType) {
            com.childproduct.designassistant.data.StandardType.ECE_R129 -> 
                MaterialTheme.colorScheme.primary
            com.childproduct.designassistant.data.StandardType.FMVSS_213 -> 
                Color(0xFF1976D2)
            com.childproduct.designassistant.data.StandardType.GB_27887 -> 
                Color(0xFFFF6F00)
        }
    ) {
        Text(
            text = "【${standardType.shortName} ${standardType.displayName}】",
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 标准专属基础数据块
 */
@Composable
fun StandardBasicDataBlock(
    standardDummies: List<com.childproduct.designassistant.data.GPS028DummyData>,
    standardType: com.childproduct.designassistant.data.StandardType,
    ageGroup: com.childproduct.designassistant.model.AgeGroup,
    heightRange: String
) {
    SectionBlock(
        icon = Icons.Default.BarChart,
        title = "基础适配数据（${standardType.shortName}假人）",
        subtitle = "匹配${standardDummies.size}个假人"
    ) {
        standardDummies.forEachIndexed { index, dummy ->
            val isLast = index == standardDummies.size - 1
            
            TreeItem(
                label = "🔽 ${dummy.displayName}（${standardType.shortName}专属）",
                value = "",
                level = 0,
                isLast = false
            )
            TreeItem(
                label = "  身高范围",
                value = "${dummy.heightEnvelope.min}-${dummy.heightEnvelope.max}cm（${standardType.shortName}假人数据）",
                level = 1,
                isLast = false
            )
            TreeItem(
                label = "  体重范围",
                value = "${dummy.weightEnvelope.min}-${dummy.weightEnvelope.max}kg（${standardType.shortName}假人数据）",
                level = 1,
                isLast = false
            )
            TreeItem(
                label = "  年龄",
                value = "${dummy.adaptationConditions.minAge}-${dummy.adaptationConditions.maxAge}岁（${dummy.ageMonths}个月）",
                level = 1,
                isLast = isLast && (standardDummies.size == 1)
            )
        }
        
        TreeItem(
            label = "适配年龄",
            value = getAgeSegments(ageGroup),
            level = 0,
            isLast = false
        )
        TreeItem(
            label = "身高范围",
            value = heightRange,
            level = 0,
            isLast = true
        )
    }
}

/**
 * 标准专属设计参数块
 */
@Composable
fun StandardDesignParametersBlock(
    standardDummies: List<com.childproduct.designassistant.data.GPS028DummyData>,
    standardType: com.childproduct.designassistant.data.StandardType
) {
    SectionBlock(
        icon = Icons.Default.Straighten,
        title = "${standardType.shortName}专属设计参数",
        subtitle = "来自${standardType.displayName}标准"
    ) {
        standardDummies.forEachIndexed { index, dummy ->
            val isLast = index == standardDummies.size - 1
            
            TreeItem(
                label = "🔽 ${dummy.displayName}",
                value = "",
                level = 0,
                isLast = false
            )
            TreeItem(
                label = "  头枕高度",
                value = "${dummy.designParameters.headrestHeightRange}（${standardType.displayName}标准）",
                level = 1,
                isLast = false
            )
            TreeItem(
                label = "  座宽",
                value = "${dummy.designParameters.seatWidthRange}（${standardType.shortName}假人肩宽）",
                level = 1,
                isLast = false
            )
            TreeItem(
                label = "  靠背深度",
                value = "${dummy.designParameters.backrestDepthRange}（${standardType.displayName}标准）",
                level = 1,
                isLast = false
            )
            TreeItem(
                label = "  侧防面积",
                value = "${dummy.designParameters.sideProtectionArea}（${standardType.shortName}增强型防护）",
                level = 1,
                isLast = isLast
            )
        }
    }
}

/**
 * 标准专属合规约束块
 */
@Composable
fun StandardComplianceConstraintsBlock(
    standardDummies: List<com.childproduct.designassistant.data.GPS028DummyData>,
    standardType: com.childproduct.designassistant.data.StandardType
) {
    SectionBlock(
        icon = Icons.Default.Verified,
        title = "${standardType.displayName}专属约束",
        subtitle = "按${standardType.shortName}标准条款"
    ) {
        standardDummies.forEachIndexed { index, dummy ->
            val isLast = index == standardDummies.size - 1
            val clauses = dummy.dummyType.standardClauses.joinToString("、")
            
            TreeItem(
                label = "🔽 ${dummy.displayName}",
                value = "",
                level = 0,
                isLast = false
            )
            TreeItem(
                label = "  标准条款",
                value = clauses,
                level = 1,
                isLast = false
            )
            
            when (standardType) {
                com.childproduct.designassistant.data.StandardType.FMVSS_213 -> {
                    TreeItem(
                        label = "  正面HIC≤1000",
                        value = "FMVSS 213 §S5.3",
                        level = 1,
                        isLast = false
                    )
                    TreeItem(
                        label = "  侧撞胸部压缩≤23mm",
                        value = "FMVSS 213 §S5.4",
                        level = 1,
                        isLast = false
                    )
                    TreeItem(
                        label = "  织带强度≥11000N",
                        value = "FMVSS 213 §S6.2",
                        level = 1,
                        isLast = isLast
                    )
                }
                com.childproduct.designassistant.data.StandardType.ECE_R129 -> {
                    TreeItem(
                        label = "  正面HIC≤${dummy.safetyThresholds.hicLimit}",
                        value = "ECE R129 §7.1.2（${dummy.safetyThresholds.hicLimitSource}）",
                        level = 1,
                        isLast = false
                    )
                    TreeItem(
                        label = "  侧防结构要求",
                        value = "ECE R129 §5.1（增强型防护）",
                        level = 1,
                        isLast = isLast
                    )
                }
                com.childproduct.designassistant.data.StandardType.GB_27887 -> {
                    TreeItem(
                        label = "  正面HIC≤324",
                        value = "GB 27887-2024 §6.4.1",
                        level = 1,
                        isLast = isLast
                    )
                }
            }
        }
    }
}

/**
 * 标准专属测试项块
 */
@Composable
fun StandardTestItemsBlock(
    standardDummies: List<com.childproduct.designassistant.data.GPS028DummyData>,
    standardType: com.childproduct.designassistant.data.StandardType
) {
    SectionBlock(
        icon = Icons.Default.Science,
        title = "${standardType.shortName}专属测试项",
        subtitle = "测试方法对应${standardType.displayName}"
    ) {
        when (standardType) {
            com.childproduct.designassistant.data.StandardType.FMVSS_213 -> {
                TreeItem(
                    label = "  动态碰撞",
                    value = "正面50km/h、侧撞32km/h（FMVSS 213测试方法）",
                    level = 1,
                    isLast = false
                )
                TreeItem(
                    label = "  阻燃性能",
                    value = "燃烧速度≤4英寸/分钟（FMVSS 302）",
                    level = 1,
                    isLast = true
                )
            }
            com.childproduct.designassistant.data.StandardType.ECE_R129 -> {
                TreeItem(
                    label = "  动态碰撞",
                    value = "正面50km/h、侧撞24km/h（ECE R129测试方法）",
                    level = 1,
                    isLast = false
                )
                TreeItem(
                    label = "  侧撞防护",
                    value = "增强型侧防（ECE R129 §5.1）",
                    level = 1,
                    isLast = true
                )
            }
            com.childproduct.designassistant.data.StandardType.GB_27887 -> {
                TreeItem(
                    label = "  动态碰撞",
                    value = "正面50km/h（GB 27887-2024测试方法）",
                    level = 1,
                    isLast = true
                )
            }
        }
    }
}

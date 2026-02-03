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

/**
 * 儿童产品设计输出组件
 * 功能：以层级树状结构展示设计输出，支持展开/收起，视觉清晰
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignOutputTree(
    creativeIdea: CreativeIdea,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 页面标题
            SectionHeader(
                icon = Icons.Default.School,
                title = "儿童产品设计输出（工程师专属）",
                color = MaterialTheme.colorScheme.primary
            )

            Divider()

            // 产品类型输出列表
            ProductTypeOutputList(creativeIdea)

            Divider()

            // 输出附加功能
            OutputActions()
        }
    }
}

/**
 * 产品类型输出列表（只显示当前选中的产品类型）
 */
@Composable
private fun ProductTypeOutputList(creativeIdea: CreativeIdea) {
    val productType = creativeIdea.productType
    
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 根据当前产品类型显示对应的输出内容
        when (productType) {
            com.childproduct.designassistant.model.ProductType.SAFETY_SEAT,
            com.childproduct.designassistant.model.ProductType.CHILD_SAFETY_SEAT -> {
                ProductTypeCard(
                    icon = Icons.Default.AirlineSeatReclineExtra,
                    title = "全年龄段儿童安全座椅（${creativeIdea.ageGroup.heightRange}）",
                    subtitle = "核心产品",
                    isDefaultExpanded = true,
                    isPrimary = true
                ) {
                    SafetySeatOutputContent(creativeIdea)
                }
            }
            com.childproduct.designassistant.model.ProductType.STROLLER,
            com.childproduct.designassistant.model.ProductType.CHILD_STROLLER -> {
                ProductTypeCard(
                    icon = Icons.Default.ChildCare,
                    title = "婴儿推车（${creativeIdea.ageGroup.heightRange}）",
                    subtitle = "折叠卡片",
                    isDefaultExpanded = true
                ) {
                    StrollerOutputContent(creativeIdea)
                }
            }
            com.childproduct.designassistant.model.ProductType.HIGH_CHAIR,
            com.childproduct.designassistant.model.ProductType.CHILD_HIGH_CHAIR -> {
                ProductTypeCard(
                    icon = Icons.Default.Chair,
                    title = "儿童高脚椅（${creativeIdea.ageGroup.heightRange}）",
                    subtitle = "折叠卡片",
                    isDefaultExpanded = true
                ) {
                    HighChairOutputContent(creativeIdea)
                }
            }
            com.childproduct.designassistant.model.ProductType.CRIB,
            com.childproduct.designassistant.model.ProductType.CHILD_HOUSEHOLD_GOODS -> {
                ProductTypeCard(
                    icon = Icons.Default.Bed,
                    title = "儿童床（${creativeIdea.ageGroup.heightRange}）",
                    subtitle = "折叠卡片",
                    isDefaultExpanded = true
                ) {
                    CribOutputContent(creativeIdea)
                }
            }
            else -> {
                // 默认显示儿童安全座椅
                ProductTypeCard(
                    icon = Icons.Default.AirlineSeatReclineExtra,
                    title = "全年龄段儿童安全座椅（${creativeIdea.ageGroup.heightRange}）",
                    subtitle = "核心产品",
                    isDefaultExpanded = true,
                    isPrimary = true
                ) {
                    SafetySeatOutputContent(creativeIdea)
                }
            }
        }
    }
}

/**
 * 产品类型卡片组件
 */
@Composable
private fun ProductTypeCard(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    isDefaultExpanded: Boolean = false,
    isPrimary: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    var isExpanded by remember { mutableStateOf(isDefaultExpanded) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        label = "rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = if (isPrimary)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        ),
        border = if (isPrimary)
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        else
            null
    ) {
        Column {
            // 卡片头部（可点击展开/收起）
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isPrimary)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (isPrimary) FontWeight.Bold else FontWeight.SemiBold
                        )
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = if (isExpanded) "收起" else "展开",
                    modifier = Modifier.rotate(rotationAngle),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 展开的内容
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    content()
                }
            }
        }
    }
}

/**
 * 区块组件（带图标的层级）
 */
@Composable
private fun SectionBlock(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 区块标题
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = iconColor
                        )
                    ) {
                        append("├─ $title")
                    }
                },
                style = MaterialTheme.typography.titleSmall
            )
        }

        if (subtitle != null) {
            Text(
                text = "│  └─ $subtitle",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 内容
        Column(
            modifier = Modifier.padding(start = 24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            content()
        }
    }
}

/**
 * 数据项组件（树状结构）
 */
@Composable
private fun TreeItem(
    label: String,
    value: String,
    level: Int = 0,
    isLast: Boolean = false
) {
    val prefix = "│  ".repeat(level)
    val connector = if (isLast) "└─" else "├─"

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    append("$prefix $connector $label：")
                }
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(value)
                }
            },
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/**
 * 儿童安全座椅输出内容（动态生成）
 */
@Composable
private fun SafetySeatOutputContent(creativeIdea: CreativeIdea) {
    val params = creativeIdea.complianceParameters
    val ageGroup = creativeIdea.ageGroup
    val heightRange = creativeIdea.ageGroup.heightRange
    val weightRange = creativeIdea.ageGroup.weightRange
    
    // 解析身高范围
    val heightRangeParts = heightRange.replace("cm", "").split("-")
    val minHeightCm = heightRangeParts.getOrNull(0)?.toIntOrNull() ?: 40
    val maxHeightCm = heightRangeParts.getOrNull(1)?.toIntOrNull() ?: 150

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 基础适配数据
        SectionBlock(
            icon = Icons.Default.BarChart,
            title = "基础适配数据",
            subtitle = "匹配GPS-028全假人"
        ) {
            TreeItem(
                label = "假人覆盖",
                value = getDummyCoverage(minHeightCm, maxHeightCm),
                level = 0,
                isLast = false
            )
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
                isLast = false
            )
            TreeItem(
                label = "安装方向",
                value = getInstallationDirection(heightRange),
                level = 0,
                isLast = true
            )
        }

        // 核心设计参数
        SectionBlock(
            icon = Icons.Default.Straighten,
            title = "核心设计参数",
            subtitle = "来自GPS-028 Dummies表"
        ) {
            TreeItem(
                label = "头枕调节",
                value = getHeadrestAdjustment(heightRange),
                level = 0,
                isLast = false
            )
            TreeItem(
                label = "座宽",
                value = getSeatWidth(heightRange),
                level = 0,
                isLast = false
            )
            TreeItem(
                label = "靠背深度",
                value = getBackrestDepth(heightRange),
                level = 0,
                isLast = false
            )
            TreeItem(
                label = "侧防结构",
                value = getSideProtection(heightRange),
                level = 0,
                isLast = true
            )
        }

        // 合规约束
        SectionBlock(
            icon = Icons.Default.Verified,
            title = "合规约束",
            subtitle = "对应ECE R129/GB 27887-2024"
        ) {
            val dummyType = params?.dummyType
            val isLowAge = dummyType in listOf(
                com.childproduct.designassistant.model.ComplianceDummy.Q0,
                com.childproduct.designassistant.model.ComplianceDummy.Q0_PLUS,
                com.childproduct.designassistant.model.ComplianceDummy.Q1,
                com.childproduct.designassistant.model.ComplianceDummy.Q1_5
            )
            
            if (isLowAge) {
                TreeItem(
                    label = "低龄段（Q0-Q1.5）",
                    value = "HIC≤390、胸部加速度≤55g",
                    level = 0,
                    isLast = false
                )
            } else {
                TreeItem(
                    label = "高龄段（Q3-Q10）",
                    value = "HIC≤1000、侧撞胸部压缩量≤44mm",
                    level = 0,
                    isLast = false
                )
            }
            TreeItem(
                label = "安装系统",
                value = "ISOFIX+支撑腿/Top-tether（双三角固定）",
                level = 0,
                isLast = true
            )
        }

        // 材料选型
        SectionBlock(
            icon = Icons.Default.Science,
            title = "材料选型",
            subtitle = "带性能指标"
        ) {
            TreeItem(
                label = "主体框架",
                value = "食品级PP（抗冲击强度≥20kJ/m²，耐温-30~80℃）",
                level = 0,
                isLast = false
            )
            TreeItem(
                label = "填充层",
                value = "Cobra记忆棉（压缩回弹率≥90%）",
                level = 0,
                isLast = false
            )
            TreeItem(
                label = "织带",
                value = "高强度尼龙（断裂强度≥11000N）",
                level = 0,
                isLast = true
            )
        }

        // 安全验证项
        SectionBlock(
            icon = Icons.Default.CheckCircle,
            title = "安全验证项"
        ) {
            TreeItem(
                label = "动态碰撞",
                value = "正向50km/h、后向30km/h、侧向32km/h",
                level = 0,
                isLast = false
            )
            TreeItem(
                label = "材料检测",
                value = "REACH 118项、EN 71阻燃",
                level = 0,
                isLast = false
            )
            TreeItem(
                label = "耐久测试",
                value = "调节机构≥1000次循环无故障",
                level = 0,
                isLast = true
            )
        }
    }
}

/**
 * 获取假人覆盖范围
 */
private fun getDummyCoverage(minHeight: Int, maxHeight: Int): String {
    val dummies = mutableListOf<String>()
    
    when {
        minHeight <= 50 -> dummies.add("Q0（40-60cm）")
        minHeight <= 60 -> dummies.add("Q0+（50-60cm）")
        minHeight <= 75 -> dummies.add("Q1（60-75cm）")
        minHeight <= 87 -> dummies.add("Q1.5（75-87cm）")
        minHeight <= 105 -> dummies.add("Q3（87-105cm）")
        minHeight <= 125 -> dummies.add("Q3s（105-125cm）")
        minHeight <= 145 -> dummies.add("Q6（125-145cm）")
        else -> dummies.add("Q10（145-150cm）")
    }
    
    when {
        maxHeight >= 145 -> dummies.add("Q10（145-150cm）")
        maxHeight >= 125 -> dummies.add("Q6（125-145cm）")
        maxHeight >= 105 -> dummies.add("Q3s（105-125cm）")
        maxHeight >= 87 -> dummies.add("Q3（87-105cm）")
        maxHeight >= 75 -> dummies.add("Q1.5（75-87cm）")
        maxHeight >= 60 -> dummies.add("Q1（60-75cm）")
        maxHeight >= 50 -> dummies.add("Q0+（50-60cm）")
        maxHeight >= 40 -> dummies.add("Q0（40-50cm）")
    }
    
    return "${dummies.firstOrNull()}→${dummies.lastOrNull()}全假人"
}

/**
 * 获取年龄段分段
 */
private fun getAgeSegments(ageGroup: com.childproduct.designassistant.model.AgeGroup): String {
    return when (ageGroup) {
        com.childproduct.designassistant.model.AgeGroup.ALL -> "0-12岁（分6段：0-1/1-2/2-3/3-4/4-6/6-12岁）"
        com.childproduct.designassistant.model.AgeGroup.INFANT -> "0-3岁（分3段：0-1/1-2/2-3岁）"
        com.childproduct.designassistant.model.AgeGroup.TODDLER -> "3-6岁（分2段：3-4/4-6岁）"
        com.childproduct.designassistant.model.AgeGroup.PRESCHOOL -> "6-9岁"
        com.childproduct.designassistant.model.AgeGroup.SCHOOL_AGE -> "9-12岁"
        com.childproduct.designassistant.model.AgeGroup.TEEN -> "10-12岁"
    }
}

/**
 * 获取安装方向
 */
private fun getInstallationDirection(heightRange: String): String {
    val heightMin = heightRange.split("-").firstOrNull()?.replace("cm", "")?.toIntOrNull() ?: 0
    val heightMax = heightRange.split("-").lastOrNull()?.replace("cm", "")?.toIntOrNull() ?: 150
    
    return when {
        heightMax <= 105 -> "身高≤105cm（4岁前）强制后向"
        heightMin >= 105 -> "身高≥105cm正向"
        else -> "身高≤105cm（4岁前）强制后向，≥105cm正向"
    }
}

/**
 * 获取头枕调节范围
 */
private fun getHeadrestAdjustment(heightRange: String): String {
    val heightMin = heightRange.split("-").firstOrNull()?.replace("cm", "")?.toIntOrNull() ?: 40
    val heightMax = heightRange.split("-").lastOrNull()?.replace("cm", "")?.toIntOrNull() ?: 150
    
    val minAdjust = 300 + ((heightMin - 40) / 15) * 50
    val maxAdjust = 300 + ((heightMax - 40) / 15) * 50
    val stages = ((heightMax - heightMin) / 10).coerceAtLeast(1).coerceAtMost(12)
    
    return "${stages}档（适配${heightMin}cm→${heightMax}cm身高，调节范围${minAdjust}-${maxAdjust}mm）"
}

/**
 * 获取座宽
 */
private fun getSeatWidth(heightRange: String): String {
    val heightMin = heightRange.split("-").firstOrNull()?.replace("cm", "")?.toIntOrNull() ?: 40
    val heightMax = heightRange.split("-").lastOrNull()?.replace("cm", "")?.toIntOrNull() ?: 150
    
    val minWidth = 280 + ((heightMin - 40) / 25) * 40
    val maxWidth = 280 + ((heightMax - 40) / 25) * 40
    
    return "分段适配（${minWidth}mm→${maxWidth}mm，随假人肩宽递增）"
}

/**
 * 获取靠背深度
 */
private fun getBackrestDepth(heightRange: String): String {
    val heightMin = heightRange.split("-").firstOrNull()?.replace("cm", "")?.toIntOrNull() ?: 40
    val heightMax = heightRange.split("-").lastOrNull()?.replace("cm", "")?.toIntOrNull() ?: 150
    
    val minDepth = 350 + ((heightMin - 40) / 20) * 75
    val maxDepth = 350 + ((heightMax - 40) / 20) * 75
    
    return "${minDepth}mm（Q0）→${maxDepth}mm（Q10）"
}

/**
 * 获取侧防结构
 */
private fun getSideProtection(heightRange: String): String {
    val heightMin = heightRange.split("-").firstOrNull()?.replace("cm", "")?.toIntOrNull() ?: 40
    val heightMax = heightRange.split("-").lastOrNull()?.replace("cm", "")?.toIntOrNull() ?: 150
    
    val minArea = 0.6 + ((heightMin - 40) / 100) * 0.1
    val maxArea = 0.6 + ((heightMax - 40) / 100) * 0.1
    
    return "可调节防护面积（${String.format("%.1f", minArea)}㎡→${String.format("%.1f", maxArea)}㎡，匹配不同年龄段）"
}

/**
 * 婴儿推车输出内容
 */
@Composable
private fun StrollerOutputContent(creativeIdea: CreativeIdea) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 基础适配数据
        SectionBlock(
            icon = Icons.Default.BarChart,
            title = "基础适配数据",
            subtitle = "来自GPS-028 Big Infant Anthro表"
        ) {
            TreeItem(
                label = "适配年龄",
                value = "0-36个月（0-3岁）"
            )
            TreeItem(
                label = "身高范围",
                value = "50-95cm"
            )
            TreeItem(
                label = "体重范围",
                value = "3.2-15.0kg",
                isLast = true
            )
        }

        // 核心设计参数
        SectionBlock(
            icon = Icons.Default.Straighten,
            title = "核心设计参数",
            subtitle = "单位：mm，来自GPS-028推车专属表"
        ) {
            TreeItem(
                label = "扶手高度",
                value = "180-260（可调节）"
            )
            TreeItem(
                label = "座宽",
                value = "320-360"
            )
            TreeItem(
                label = "靠背角度",
                value = "140°-175°（多档位调节）"
            )
            TreeItem(
                label = "轮距",
                value = "550-600（防侧翻）",
                isLast = true
            )
        }

        // 合规阈值
        SectionBlock(
            icon = Icons.Default.Verified,
            title = "合规阈值",
            subtitle = "分目标市场"
        ) {
            Text(
                text = "│  ├─ 通用要求（EN 1888/GB 14748）：",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                TreeItem(
                    label = "刹车力",
                    value = "≤50N",
                    level = 1
                )
                TreeItem(
                    label = "侧翻角度",
                    value = "≥30°",
                    level = 1,
                    isLast = true
                )
            }
            Text(
                text = "│  └─ US市场额外（ASTM F833）：",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            TreeItem(
                label = "手柄强度",
                value = "可承受135N拉力无变形",
                level = 1,
                isLast = true
            )
        }

        // 材料与验证依据
        SectionBlock(
            icon = Icons.Default.Science,
            title = "材料与验证依据"
        ) {
            Text(
                text = "│  ├─ 推荐材料：",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                TreeItem(
                    label = "车架",
                    value = "铝合金（抗拉强度≥240MPa）",
                    level = 1
                )
                TreeItem(
                    label = "座布",
                    value = "牛津布（防水等级≥IPX4）",
                    level = 1,
                    isLast = true
                )
            }
            TreeItem(
                label = "数据追溯",
                value = "来自GPS-028婴儿推车人体测量表、Test Data表",
                level = 0,
                isLast = true
            )
        }
    }
}

/**
 * 儿童高脚椅输出内容
 */
@Composable
private fun HighChairOutputContent(creativeIdea: CreativeIdea) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 基础适配数据
        SectionBlock(
            icon = Icons.Default.BarChart,
            title = "基础适配数据",
            subtitle = "来自GPS-028 Infant Anthro表"
        ) {
            TreeItem(
                label = "适配年龄",
                value = "6-36个月（0.5-3岁）"
            )
            TreeItem(
                label = "身高范围",
                value = "65-100cm"
            )
            TreeItem(
                label = "体重范围",
                value = "8.0-15.0kg",
                isLast = true
            )
        }

        // 核心设计参数
        SectionBlock(
            icon = Icons.Default.Straighten,
            title = "核心设计参数",
            subtitle = "单位：mm"
        ) {
            TreeItem(
                label = "座高",
                value = "450-550（可调节）"
            )
            TreeItem(
                label = "座宽",
                value = "300-350"
            )
            TreeItem(
                label = "座深",
                value = "250-300"
            )
            TreeItem(
                label = "托盘尺寸",
                value = "400×300（可拆卸）",
                isLast = true
            )
        }

        // 合规阈值
        SectionBlock(
            icon = Icons.Default.Verified,
            title = "合规阈值",
            subtitle = "分目标市场"
        ) {
            Text(
                text = "│  ├─ 通用要求（EN 14988/GB 22793）：",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                TreeItem(
                    label = "稳定性",
                    value = "前倾、侧倾≥10°无翻倒",
                    level = 1
                )
                TreeItem(
                    label = "五点式安全带",
                    value = "抗拉强度≥750N",
                    level = 1,
                    isLast = true
                )
            }
            Text(
                text = "│  └─ US市场额外（ASTM F404）：",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            TreeItem(
                label = "托盘强度",
                value = "可承受100N压力无变形",
                level = 1,
                isLast = true
            )
        }

        // 材料与验证依据
        SectionBlock(
            icon = Icons.Default.Science,
            title = "材料与验证依据"
        ) {
            Text(
                text = "│  ├─ 推荐材料：",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                TreeItem(
                    label = "座椅框架",
                    value = "食品级PP塑料（耐温-20℃~80℃）",
                    level = 1
                )
                TreeItem(
                    label = "安全带",
                    value = "尼龙织带（断裂强度≥2000N）",
                    level = 1,
                    isLast = true
                )
            }
            TreeItem(
                label = "数据追溯",
                value = "来自GPS-028高脚椅人体测量表",
                level = 0,
                isLast = true
            )
        }
    }
}

/**
 * 儿童床输出内容
 */
@Composable
private fun CribOutputContent(creativeIdea: CreativeIdea) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 基础适配数据
        SectionBlock(
            icon = Icons.Default.BarChart,
            title = "基础适配数据",
            subtitle = "来自GPS-028 Infant Anthro表"
        ) {
            TreeItem(
                label = "适配年龄",
                value = "0-24个月（0-2岁）"
            )
            TreeItem(
                label = "身高范围",
                value = "50-85cm"
            )
            TreeItem(
                label = "体重范围",
                value = "3.0-15.0kg",
                isLast = true
            )
        }

        // 核心设计参数
        SectionBlock(
            icon = Icons.Default.Straighten,
            title = "核心设计参数",
            subtitle = "单位：mm"
        ) {
            TreeItem(
                label = "内尺寸（长×宽）",
                value = "1200×600"
            )
            TreeItem(
                label = "床板高度",
                value = "300-500（可调节，三档）"
            )
            TreeItem(
                label = "护栏高度",
                value = "600（顶部）"
            )
            TreeItem(
                label = "围栏间隙",
                value = "≤60mm（防卡头）",
                isLast = true
            )
        }

        // 合规阈值
        SectionBlock(
            icon = Icons.Default.Verified,
            title = "合规阈值",
            subtitle = "分目标市场"
        ) {
            Text(
                text = "│  ├─ 通用要求（EN 716/GB 28007）：",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                TreeItem(
                    label = "围栏强度",
                    value = "可承受200N拉力无变形",
                    level = 1
                )
                TreeItem(
                    label = "床板强度",
                    value = "可承受100kg静态载荷",
                    level = 1,
                    isLast = true
                )
            }
            Text(
                text = "│  └─ US市场额外（ASTM F1169）：",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            TreeItem(
                label = "床垫厚度",
                value = "≤150mm（防窒息）",
                level = 1,
                isLast = true
            )
        }

        // 材料与验证依据
        SectionBlock(
            icon = Icons.Default.Science,
            title = "材料与验证依据"
        ) {
            Text(
                text = "│  ├─ 推荐材料：",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                TreeItem(
                    label = "床架",
                    value = "实木（环保水性漆）",
                    level = 1
                )
                TreeItem(
                    label = "床垫",
                    value = "椰棕/乳胶（透气、防螨）",
                    level = 1,
                    isLast = true
                )
            }
            TreeItem(
                label = "数据追溯",
                value = "来自GPS-028儿童床人体测量表、Test Data表",
                level = 0,
                isLast = true
            )
        }
    }
}

/**
 * 输出附加功能
 */
@Composable
private fun OutputActions() {
    SectionHeader(
        icon = Icons.Default.MoreVert,
        title = "附加工具",
        color = MaterialTheme.colorScheme.secondary
    )

    var showDummyTable by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 假人分段参数表
        OutlinedButton(
            onClick = { showDummyTable = true },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("假人分段")
        }

        // 导出PDF
        Button(
            onClick = { /* TODO: 实现导出报告 */ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.FileDownload,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("导出PDF")
        }
    }

    // 假人分段参数表弹窗
    if (showDummyTable) {
        DummyParameterTableDialog(
            onDismiss = { showDummyTable = false }
        )
    }
}

/**
 * 假人分段参数表弹窗
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DummyParameterTableDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(Icons.Default.Info, contentDescription = null)
        },
        title = {
            Text(text = "假人分段参数表（ECE R129标准）")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "根据ECE R129 Annex 19标准，不同身高对应的假人类型及参数：",
                    style = MaterialTheme.typography.bodySmall
                )
                
                // 假人参数表
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        // 表头
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = "假人",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "身高(cm)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "体重(kg)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "HIC极限",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        Divider()
                        
                        // Q0
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text("Q0", modifier = Modifier.weight(1f))
                            Text("40-50", modifier = Modifier.weight(1f))
                            Text("2.5", modifier = Modifier.weight(1f))
                            Text("≤390", modifier = Modifier.weight(1f))
                        }
                        
                        // Q0+
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text("Q0+", modifier = Modifier.weight(1f))
                            Text("50-60", modifier = Modifier.weight(1f))
                            Text("4.0", modifier = Modifier.weight(1f))
                            Text("≤390", modifier = Modifier.weight(1f))
                        }
                        
                        // Q1
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text("Q1", modifier = Modifier.weight(1f))
                            Text("60-75", modifier = Modifier.weight(1f))
                            Text("9.0", modifier = Modifier.weight(1f))
                            Text("≤390", modifier = Modifier.weight(1f))
                        }
                        
                        // Q1.5
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text("Q1.5", modifier = Modifier.weight(1f))
                            Text("75-87", modifier = Modifier.weight(1f))
                            Text("11.0", modifier = Modifier.weight(1f))
                            Text("≤570", modifier = Modifier.weight(1f))
                        }
                        
                        // Q3
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text("Q3", modifier = Modifier.weight(1f))
                            Text("87-105", modifier = Modifier.weight(1f))
                            Text("15.0", modifier = Modifier.weight(1f))
                            Text("≤1000", modifier = Modifier.weight(1f))
                        }
                        
                        // Q3s
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text("Q3s", modifier = Modifier.weight(1f))
                            Text("105-125", modifier = Modifier.weight(1f))
                            Text("21.0", modifier = Modifier.weight(1f))
                            Text("≤1000", modifier = Modifier.weight(1f))
                        }
                        
                        // Q6
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text("Q6", modifier = Modifier.weight(1f))
                            Text("125-145", modifier = Modifier.weight(1f))
                            Text("33.0", modifier = Modifier.weight(1f))
                            Text("≤1000", modifier = Modifier.weight(1f))
                        }
                        
                        // Q10
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text("Q10", modifier = Modifier.weight(1f))
                            Text("145-150", modifier = Modifier.weight(1f))
                            Text("38.0", modifier = Modifier.weight(1f))
                            Text("≤1000", modifier = Modifier.weight(1f))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "💡 提示：根据输入的身高范围，系统自动匹配对应的假人类型和参数。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        }
    )
}

/**
 * 区块标题组件
 */
@Composable
private fun SectionHeader(
    icon: ImageVector,
    title: String,
    color: Color
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

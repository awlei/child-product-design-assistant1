package com.childproduct.designassistant.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
 * 产品类型输出列表
 */
@Composable
private fun ProductTypeOutputList(creativeIdea: CreativeIdea) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 儿童安全座椅（核心产品，默认展开）
        ProductTypeCard(
            icon = Icons.Default.AirlineSeatReclineExtra,
            title = "儿童安全座椅",
            subtitle = "核心产品",
            isDefaultExpanded = true,
            isPrimary = true
        ) {
            SafetySeatOutputContent(creativeIdea)
        }

        // 婴儿推车（折叠卡片，默认收起）
        ProductTypeCard(
            icon = Icons.Default.ChildCare,
            title = "婴儿推车",
            subtitle = "折叠卡片",
            isDefaultExpanded = false
        ) {
            StrollerOutputContent(creativeIdea)
        }

        // 儿童高脚椅（折叠卡片，默认收起）
        ProductTypeCard(
            icon = Icons.Default.Chair,
            title = "儿童高脚椅",
            subtitle = "折叠卡片",
            isDefaultExpanded = false
        ) {
            HighChairOutputContent(creativeIdea)
        }

        // 儿童床（折叠卡片，默认收起）
        ProductTypeCard(
            icon = Icons.Default.Bed,
            title = "儿童床",
            subtitle = "折叠卡片",
            isDefaultExpanded = false
        ) {
            CribOutputContent(creativeIdea)
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
            CardDefaults.outlinedCardBorder().copy(
                brush = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
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
 * 儿童安全座椅输出内容
 */
@Composable
private fun SafetySeatOutputContent(creativeIdea: CreativeIdea) {
    val params = creativeIdea.complianceParameters
    val heightRange = creativeIdea.heightRange ?: "100-150cm"
    val ageRange = creativeIdea.ageGroup.displayName

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 基础适配数据
        SectionBlock(
            icon = Icons.Default.BarChart,
            title = "基础适配数据",
            subtitle = "来自GPS-028 Big Infant Anthro+Dummies表"
        ) {
            TreeItem(
                label = "适配假人类型",
                value = params?.dummyType?.displayName ?: "Q6/Q10（3-12岁）"
            )
            TreeItem(
                label = "适配年龄段",
                value = ageRange
            )
            TreeItem(
                label = "身高范围",
                value = heightRange
            )
            TreeItem(
                label = "体重范围",
                value = "${params?.minWeight ?: "18.0"}-${params?.maxWeight ?: "45.0"}kg",
                isLast = true
            )
        }

        // 核心设计参数
        SectionBlock(
            icon = Icons.Default.Straighten,
            title = "核心设计参数",
            subtitle = "单位：mm，来自GPS-028 Dummies表"
        ) {
            TreeItem(
                label = "头枕高度",
                value = "500-600"
            )
            TreeItem(
                label = "座宽",
                value = "440-520"
            )
            TreeItem(
                label = "靠背深度",
                value = "550-650"
            )
            TreeItem(
                label = "侧防结构",
                value = "防护面积≥0.8㎡（覆盖胸部-头部）",
                isLast = true
            )
        }

        // 合规阈值
        SectionBlock(
            icon = Icons.Default.Verified,
            title = "合规阈值",
            subtitle = "分目标市场，来自GPS-028地区专属表"
        ) {
            // 通用要求
            Text(
                text = "│  ├─ 通用要求（ECE R129/GB 27887-2024）：",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                TreeItem(
                    label = "HIC极限值",
                    value = "≤${params?.hicLimit ?: "1000"}",
                    level = 1
                )
                TreeItem(
                    label = "胸部加速度",
                    value = "≤${params?.chestAccelerationLimit ?: "60"}g",
                    level = 1
                )
                TreeItem(
                    label = "头部位移",
                    value = "≤${params?.headExcursionLimit ?: "550"}mm",
                    level = 1,
                    isLast = true
                )
            }

            // US市场额外
            Text(
                text = "│  ├─ US市场额外（FMVSS 213）：",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            TreeItem(
                label = "阻燃性能",
                value = "燃烧速度≤4英寸/分钟（FMVSS 302）",
                level = 1,
                isLast = true
            )

            // China市场额外
            Text(
                text = "│  └─ China市场额外（GB标准）：",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold
            )
            TreeItem(
                label = "有害物质",
                value = "符合GB 6675（无甲醛/重金属）",
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
                    label = "主体框架",
                    value = "食品级PP塑料（耐温-30℃~80℃）",
                    level = 1
                )
                TreeItem(
                    label = "填充层",
                    value = "高回弹海绵（密度30kg/m³）",
                    level = 1
                )
                TreeItem(
                    label = "织带",
                    value = "聚酯纤维（断裂强度≥11000N）",
                    level = 1,
                    isLast = true
                )
            }
            TreeItem(
                label = "数据追溯",
                value = "来自GPS-028 Dummies表Q6/Q10假人数据、Material Specs表",
                level = 0,
                isLast = true
            )
        }
    }
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
        title = "输出附加功能",
        color = MaterialTheme.colorScheme.secondary
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 重新计算
        OutlinedButton(
            onClick = { /* TODO: 实现重新计算 */ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("重新计算")
        }

        // 导出报告
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
            Text("导出报告")
        }

        // 数据说明
        OutlinedButton(
            onClick = { /* TODO: 实现数据说明 */ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.HelpOutline,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("数据说明")
        }
    }
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

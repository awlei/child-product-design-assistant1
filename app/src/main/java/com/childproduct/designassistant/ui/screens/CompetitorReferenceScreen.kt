package com.childproduct.designassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.childproduct.designassistant.ui.MainViewModel

/**
 * 竞品参考屏幕
 * 功能：展示头部品牌竞品参考，包括Britax、Maxi-Cosi、Cybex、UPPAbaby等
 */
@Composable
fun CompetitorReferenceScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        // 标题
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "头部品牌竞品参考",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = "参考Britax、Maxi-Cosi、Cybex、UPPAbaby等头部品牌的同类产品参数，了解市场标杆",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // 市场分析
        item {
            MarketAnalysisCard()
        }

        // 竞品列表
        items(getCompetitorProducts()) { competitor ->
            CompetitorProductCard(competitor = competitor)
        }
    }
}

/**
 * 市场分析卡片
 */
@Composable
fun MarketAnalysisCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "市场分析",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Divider()

            AnalysisItem(
                title = "主流产品类型",
                description = "目前市场上主流产品为Group 0+/1（40-105cm），覆盖新生儿到4岁，后向优先设计"
            )
            AnalysisItem(
                title = "发展趋势",
                description = "产品向360°旋转、智能感应、轻量化、多档调节等方向发展"
            )
            AnalysisItem(
                title = "价格区间",
                description = "中高端产品价格区间为2000-5000元，高端产品可达8000元以上"
            )
            AnalysisItem(
                title = "核心卖点",
                description = "安全性（ECE R129认证）、舒适性（多档调节）、便捷性（360°旋转）"
            )
        }
    }
}

/**
 * 竞品产品卡片
 */
@Composable
fun CompetitorProductCard(competitor: CompetitorProduct) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 标题行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = competitor.brandName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "折叠" else "展开",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (expanded) {
                Divider()

                Spacer(modifier = Modifier.height(8.dp))

                // 产品信息
                Text(
                    text = "对标产品：${competitor.productName}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProductSpecRow("适用身高", "${competitor.minHeight}-${competitor.maxHeight}cm")
                ProductSpecRow("适用年龄", "${competitor.minAge}-${competitor.maxAge}岁")
                ProductSpecRow("产品分组", competitor.group)
                ProductSpecRow("安装方式", competitor.installMethod)
                ProductSpecRow("旋转功能", if (competitor.hasRotation) "支持" else "不支持")
                ProductSpecRow("侧撞防护", if (competitor.hasSideImpactProtection) "支持" else "不支持")
                ProductSpecRow("参考价格", competitor.price)

                Spacer(modifier = Modifier.height(8.dp))

                // 亮点分析
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = "产品亮点",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        competitor.highlights.forEach { highlight ->
                            Text(
                                text = "• $highlight",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            } else {
                // 简略信息
                Text(
                    text = "对标产品：${competitor.productName} | 适用身高：${competitor.minHeight}-${competitor.maxHeight}cm | 价格：${competitor.price}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

/**
 * 分析项组件
 */
@Composable
fun AnalysisItem(
    title: String,
    description: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

/**
 * 产品规格行组件
 */
@Composable
fun ProductSpecRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 竞品产品数据类
 */
data class CompetitorProduct(
    val brandName: String,
    val productName: String,
    val minHeight: Int,
    val maxHeight: Int,
    val minAge: Int,
    val maxAge: Int,
    val group: String,
    val installMethod: String,
    val hasRotation: Boolean,
    val hasSideImpactProtection: Boolean,
    val price: String,
    val highlights: List<String>
)

/**
 * 获取竞品产品列表
 */
fun getCompetitorProducts(): List<CompetitorProduct> {
    return listOf(
        CompetitorProduct(
            brandName = "Britax",
            productName = "Dualfix M i-Size",
            minHeight = 40,
            maxHeight = 105,
            minAge = 0,
            maxAge = 4,
            group = "Group 0+/1",
            installMethod = "ISOFIX + 支撑腿",
            hasRotation = true,
            hasSideImpactProtection = true,
            price = "¥3500-4500",
            highlights = listOf(
                "360°旋转，方便上下车",
                "多档头托调节，适配40-105cm",
                "先进侧撞防护系统（SICT）",
                "多种躺角调节，舒适性优异"
            )
        ),
        CompetitorProduct(
            brandName = "Maxi-Cosi",
            productName = "Pearl 360 i-Size",
            minHeight = 40,
            maxHeight = 105,
            minAge = 0,
            maxAge = 4,
            group = "Group 0+/1",
            installMethod = "ISOFIX + 支撑腿",
            hasRotation = true,
            hasSideImpactProtection = true,
            price = "¥3000-4000",
            highlights = listOf(
                "360°旋转，单手操作",
                "ErgoGrow头托系统，多档调节",
                "ClashFree防碰撞技术",
                "柔软舒适的内衬设计"
            )
        ),
        CompetitorProduct(
            brandName = "Cybex",
            productName = "Sirona SX2 i-Size",
            minHeight = 40,
            maxHeight = 105,
            minAge = 0,
            maxAge = 4,
            group = "Group 0+/1",
            installMethod = "ISOFIX + 支撑腿",
            hasRotation = true,
            hasSideImpactProtection = true,
            price = "¥2800-3800",
            highlights = listOf(
                "360°旋转，便捷操作",
                "线性侧撞保护（L.S.P.系统）",
                "全向360°旋转",
                "德国设计，品质保证"
            )
        ),
        CompetitorProduct(
            brandName = "UPPAbaby",
            productName = "Knox i-Size",
            minHeight = 40,
            maxHeight = 125,
            minAge = 0,
            maxAge = 7,
            group = "Group 0+/1/2",
            installMethod = "ISOFIX + 支撑腿",
            hasRotation = true,
            hasSideImpactProtection = true,
            price = "¥4000-5000",
            highlights = listOf(
                "360°旋转，大尺寸设计",
                "SmartSecure系统，安装提示",
                "创新侧撞防护设计",
                "加宽座椅空间，适用年龄更长"
            )
        ),
        CompetitorProduct(
            brandName = "Graco",
            productName = "Avantload 3.0 i-Size",
            minHeight = 40,
            maxHeight = 105,
            minAge = 0,
            maxAge = 4,
            group = "Group 0+/1",
            installMethod = "ISOFIX + 支撑腿",
            hasRotation = false,
            hasSideImpactProtection = true,
            price = "¥1500-2500",
            highlights = listOf(
                "侧撞防护系统",
                "多档头托调节",
                "简单易用的安装方式",
                "性价比高，适合预算有限家庭"
            )
        ),
        CompetitorProduct(
            brandName = "好孩子",
            productName = "GB Vaya i-Spin 360",
            minHeight = 40,
            maxHeight = 105,
            minAge = 0,
            maxAge = 4,
            group = "Group 0+/1",
            installMethod = "ISOFIX + 支撑腿",
            hasRotation = true,
            hasSideImpactProtection = true,
            price = "¥2000-3000",
            highlights = listOf(
                "360°旋转，便捷操作",
                "智能安装指示系统",
                "多档头托调节",
                "国产知名品牌，售后完善"
            )
        )
    )
}

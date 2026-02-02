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
import com.childproduct.designassistant.model.ProductType
import com.childproduct.designassistant.ui.MainViewModel
import com.childproduct.designassistant.utils.ComplianceParamsData
import com.childproduct.designassistant.utils.DesignSchemeFormatter
import com.childproduct.designassistant.utils.SchemeCardData

/**
 * 整合报告界面
 * 功能：以标签页形式展示"方案详情""测试矩阵""设计建议""竞品参考"等全维度内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntegratedReportScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // 标签页状态
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    val tabs = listOf(
        "方案详情",
        "测试矩阵", 
        "设计建议",
        "竞品参考"
    )
    
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // 标签页导航
        TabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = when (index) {
                                0 -> Icons.Default.Assignment
                                1 -> Icons.Default.Science
                                2 -> Icons.Default.EditNote
                                3 -> Icons.Default.Analytics
                                else -> Icons.Default.Info
                            },
                            contentDescription = null
                        )
                    }
                )
            }
        }
        
        // 标签页内容
        when (selectedTabIndex) {
            0 -> SchemeDetailTab(viewModel, modifier)
            1 -> TestMatrixTab(viewModel, modifier)
            2 -> DesignSuggestionTab(viewModel, modifier)
            3 -> CompetitorReferenceTab(viewModel, modifier)
        }
    }
}

/**
 * 方案详情标签页
 */
@Composable
fun SchemeDetailTab(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val creativeIdea by viewModel.creativeIdea.collectAsState()
    
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
                    containerColor = MaterialTheme.colorScheme.primaryContainer
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
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "全维度方案生成完成",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "基于您的输入，系统已自动生成包含标准匹配、测试矩阵、设计建议、竞品参考等内容的完整方案。",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        // 结构化设计方案展示（新增）
        creativeIdea?.let { idea ->
            item {
                val cardData = DesignSchemeFormatter.formatToCardData(idea)
                StructuredDesignSchemeCard(cardData = cardData)
            }
        }
        
        // 身高匹配分析
        item {
            HeightMatchAnalysisCard()
        }
        
        // 核心参数表
        item {
            CoreParameterTableCard()
        }
        
        // 材料标准标注
        item {
            MaterialStandardTableCard()
        }
        
        // 产品配置信息
        item {
            ProductConfigurationCard()
        }
        
        // 操作提示
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
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "提示",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = "• 点击标准条款（如ECE R129 §5.4.2）可跳转到\"文档学习\"查看完整标准文档",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "• 点击参数名称（如头托调节范围）可唤起\"智能问答\"询问设计逻辑",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

/**
 * 测试矩阵标签页
 */
@Composable
fun TestMatrixTab(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
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
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
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
                            imageVector = Icons.Default.Science,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "ECE R129 动态测试矩阵",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    Text(
                        text = "基于身高40-150cm（Group 0+/1/2/3）自动生成的17项动态测试用例",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        // 测试矩阵列表
        items(17) { index ->
            IntegratedReportTestMatrixItemCard(testIndex = index + 1)
        }
    }
}

/**
 * 设计建议标签页
 */
@Composable
fun DesignSuggestionTab(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
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
                    containerColor = MaterialTheme.colorScheme.primaryContainer
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
                            imageVector = Icons.Default.EditNote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "专业设计建议",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "基于ECE R129/GB 27887标准的专业设计指导",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        // 产品核心设计方向
        item {
            IntegratedReportProductDesignThemeCard()
        }
        
        // 落地设计细节示例
        items(2) { index ->
            DesignDetailExampleCard(exampleIndex = index + 1)
        }
    }
}

/**
 * 竞品参考标签页
 */
@Composable
fun CompetitorReferenceTab(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
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
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = "参考Britax、Maxi-Cosi、Cybex等头部品牌的同类产品参数",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        // 竞品列表
        items(listOf("Britax", "Maxi-Cosi", "Cybex", "UPPAbaby")) { brand ->
            CompetitorProductCard(brandName = brand)
        }
    }
}

/**
 * 身高匹配分析卡片
 */
@Composable
fun HeightMatchAnalysisCard() {
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
                    imageVector = Icons.Default.Height,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "身高匹配分析",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Divider()
            
            // 匹配信息
            IntegratedReportIntegratedReportInfoRow("输入身高", "40-150 cm")
            IntegratedReportIntegratedReportInfoRow("对应年龄", "0-12岁")
            IntegratedReportIntegratedReportInfoRow("标准分组", "Group 0+/1/2/3（全分组）")
            IntegratedReportIntegratedReportInfoRow("覆盖范围", "✅ 全范围（40-150cm，0-12岁）")
            IntegratedReportIntegratedReportInfoRow("推荐朝向", "后向（优先）→ 前向（根据身高切换）")
        }
    }
}

/**
 * 核心参数表卡片
 */
@Composable
fun CoreParameterTableCard() {
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
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "核心参数表（关联标准条款）",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Divider()
            
            // 参数表格
            Column {
                // 表头
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("参数名称", Modifier.weight(1.5f), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    Text("参数值", Modifier.weight(0.5f), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    Text("单位", Modifier.weight(0.5f), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    Text("标准条款", Modifier.weight(1.5f), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
                
                Divider()
                
                // 参数行
                ParameterRow("头托调节范围", "10-30", "cm", "ECE R129 §5.4.2")
                ParameterRow("ISOFIX接口间距", "280", "mm", "GB 27887-2024 §5.5")
                ParameterRow("支撑腿长度", "280-450", "mm", "ECE R129 §5.5.3")
                ParameterRow("侧翼内宽", "≥380", "mm", "ECE R129 §5.3.3")
                ParameterRow("座椅靠背角度（后向）", "135-150", "度", "ECE R129 §5.4.1")
            }
        }
    }
}

/**
 * 材料标准标注卡片
 */
@Composable
fun MaterialStandardTableCard() {
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
                    imageVector = Icons.Default.Inventory,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "材料标准标注",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Divider()
            
            // 材料表格
            Column {
                // 表头
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("材料名称", Modifier.weight(2f), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    Text("符合标准", Modifier.weight(1.5f), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
                
                Divider()
                
                // 材料行
                MaterialRow("PP塑料（座椅主体）", "GB 6675.4-2014")
                MaterialRow("EPS吸能材料", "GB/T 10801.1-2021")
                MaterialRow("EPP吸能材料", "GB/T 10801.2-2021")
                MaterialRow("安全带织带", "GB 6095-2021")
            }
        }
    }
}

/**
 * 产品配置信息卡片
 */
@Composable
fun ProductConfigurationCard() {
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
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "产品配置信息",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Divider()
            
            // 配置列表
            ConfigurationItem(
                "ISOFIX接口",
                "ISOFIX硬连接接口，提供稳固的车辆固定",
                "ECE R129 §5.5.1/§5.5.3",
                "适配带ISOFIX锚点的乘用车"
            )
            ConfigurationItem(
                "可伸缩支撑腿",
                "地面支撑结构，提高座椅稳定性",
                "ECE R129 §5.5.3",
                "适配地板间隙≥5cm的车辆"
            )
            ConfigurationItem(
                "侧撞防护装置（SIP）",
                "可调节的侧面碰撞保护块",
                "ECE R129 §5.3.3",
                "需根据车门间隙调整侧翼展开角度"
            )
        }
    }
}

/**
 * 测试矩阵项卡片
 */
@Composable
fun IntegratedReportTestMatrixItemCard(testIndex: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "测试 #$testIndex",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ECE_R129_$testIndex",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Divider()
            
            // 测试参数

            IntegratedReportTestParameterRow("撞击类型", "FRONTAL_RIGID")
            IntegratedReportTestParameterRow("假人类型", "Q0（新生儿）")
            IntegratedReportTestParameterRow("座椅方向", "REARWARD_FACING")
            IntegratedReportTestParameterRow("安装方式", "ISOFIX_3_PTS")
            IntegratedReportTestParameterRow("产品配置", "UPRIGHT")
            IntegratedReportTestParameterRow("速度", "50 km/h")

            Spacer(modifier = Modifier.height(4.dp))
            
            // 合格标准
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = "合格标准: HIC36 ≤ 324，胸部加速度 ≤ 55g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * 产品设计主题卡片
 */
@Composable
fun IntegratedReportProductDesignThemeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "产品核心设计方向（专业主题）",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Divider()
            
            Text(
                text = "主题名称：ECE R129全分组安全适配主题（40-150cm，Group 0+/1/2/3）",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "覆盖年龄段：0-12岁",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "推荐朝向：后向（优先）→ 前向（根据身高切换）",
                style = MaterialTheme.typography.bodySmall
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "核心设计细节：",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "• 头托自适应调节：覆盖40-150cm全身高区间（适配ECE R129 Group 0+/1/2/3对应的Q系列假人）",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "• 双固定结构适配：可切换ISOFIX+支撑腿（满足ECE R129不同分组的安装合规性）",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "• 分段式侧撞防护：侧撞块采用EPS+EPP双层吸能结构（符合ECE R129 §5.3.3侧撞要求）",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * 设计细节示例卡片
 */
@Composable
fun DesignDetailExampleCard(exampleIndex: Int) {
    val title = when (exampleIndex) {
        1 -> "示例1：教育元素融入（合规前提下）"
        else -> "示例2：轻量化设计（不影响强度）"
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            
            Divider()
            
            if (exampleIndex == 1) {
                Text(
                    text = "• 头托侧面增加可拆洗的数字认知布贴",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• 布贴材料符合GB 18401 B类标准",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• 布贴厚度≤5mm，不影响侧撞防护结构的完整性",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• 布贴位置避开关键吸能区域",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Text(
                    text = "• 底座骨架采用高强度铝合金，替代部分钢材",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• 整体重量控制在15kg以内（满足ISOFIX承载要求）",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• 减重区域选择在非受力部件（如装饰件）",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 竞品产品卡片
 */
@Composable
fun CompetitorProductCard(brandName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                    text = brandName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            
            Divider()
            
            Text(
                text = "对标产品：${brandName} Dualfix M i-Size",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "适用身高：40-105cm",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "安装方式：ISOFIX + 支撑腿",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "旋转功能：360°旋转",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * 信息行组件
 */
@Composable
fun IntegratedReportIntegratedReportInfoRow(label: String, value: String) {
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
 * 参数行组件
 */
@Composable
fun ParameterRow(name: String, value: String, unit: String, clause: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(name, Modifier.weight(1.5f), style = MaterialTheme.typography.bodySmall)
        Text("$value $unit", Modifier.weight(0.5f), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
        Text(clause, Modifier.weight(1.5f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
    }
}

/**
 * 材料行组件
 */
@Composable
fun MaterialRow(name: String, standard: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(name, Modifier.weight(2f), style = MaterialTheme.typography.bodySmall)
        Text(standard, Modifier.weight(1.5f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
    }
}

/**
 * 测试参数行组件
 */
@Composable
fun IntegratedReportTestParameterRow(label: String, value: String) {
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
 * 配置项组件
 */
@Composable
fun ConfigurationItem(
    name: String,
    description: String,
    clause: String,
    requirement: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = clause,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "安装要求：$requirement",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

/**
 * 结构化设计方案展示卡片
 * 使用新的格式化工具，展示结构化、无冗余、易读的设计方案
 */
@Composable
fun StructuredDesignSchemeCard(
    cardData: SchemeCardData,
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
            // 【基本信息】
            SectionCard(
                title = "基本信息",
                icon = Icons.Default.Info,
                iconColor = MaterialTheme.colorScheme.primary
            ) {
                cardData.basicInfo.forEach { (label, value) ->
                    InfoRow(label = label, value = value)
                }
            }

            // 【核心设计特点】
            SectionCard(
                title = "核心设计特点",
                icon = Icons.Default.AutoAwesome,
                iconColor = MaterialTheme.colorScheme.secondary
            ) {
                cardData.coreFeatures.forEach { feature ->
                    ListItem(
                        headlineContent = { Text(feature) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }
            }

            // 【推荐材料】
            SectionCard(
                title = "推荐材料",
                icon = Icons.Default.Category,
                iconColor = MaterialTheme.colorScheme.tertiary
            ) {
                cardData.recommendedMaterials.forEach { material ->
                    ListItem(
                        headlineContent = { Text(material) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }
            }

            // 【合规参数】
            cardData.complianceParams?.let { params ->
                SectionCard(
                    title = "合规参数",
                    icon = Icons.Default.Verified,
                    iconColor = MaterialTheme.colorScheme.primary
                ) {
                    InfoRow(label = "对应标准", value = params.standards)
                    InfoRow(label = "适配假人", value = params.dummyType)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "安全阈值",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    params.thresholds.forEach { (name, value) ->
                        InfoRow(
                            label = "  • $name",
                            value = value
                        )
                    }
                }
            }

            // 【安全注意事项】
            SectionCard(
                title = "安全注意事项",
                icon = Icons.Default.Warning,
                iconColor = MaterialTheme.colorScheme.error
            ) {
                cardData.safetyNotes.forEach { note ->
                    ListItem(
                        headlineContent = { Text(note) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.PriorityHigh,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}

/**
 * 分区卡片组件
 */
@Composable
private fun SectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: androidx.compose.ui.graphics.Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 标题行
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "【$title】",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = iconColor
            )
        }

        Divider()

        // 内容
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            content()
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

/**
 * 信息行组件
 */
@Composable
private fun InfoRow(
    label: String,
    value: String
) {
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

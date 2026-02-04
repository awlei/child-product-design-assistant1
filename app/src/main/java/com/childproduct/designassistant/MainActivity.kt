package com.childproduct.designassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.childproduct.designassistant.ui.MainViewModel
import com.childproduct.designassistant.ui.UiState
import com.childproduct.designassistant.ui.screens.CreativeScreen
import com.childproduct.designassistant.ui.screens.DocumentScreen
import com.childproduct.designassistant.ui.screens.SafetyScreen
import com.childproduct.designassistant.ui.screens.TechnicalRecommendationScreen
import com.childproduct.designassistant.ui.screens.DocumentLearningScreen
import com.childproduct.designassistant.ui.screens.ChatQAScreen
import com.childproduct.designassistant.ui.screens.OneClickGenerationScreen
import com.childproduct.designassistant.ui.screens.IntegratedReportScreen
import com.childproduct.designassistant.ui.screens.TestMatrixScreen
import com.childproduct.designassistant.ui.screens.DesignSuggestionScreen
import com.childproduct.designassistant.ui.screens.CompetitorReferenceScreen
import com.childproduct.designassistant.ui.screens.ComplianceManagementScreen
import com.childproduct.designassistant.ui.screens.DesignProposalScreen
import com.childproduct.designassistant.ui.screens.DesignProposalViewModel
import com.childproduct.designassistant.ui.standard.StandardSelectionScreen
import com.childproduct.designassistant.ui.theme.ChildProductDesignAssistantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化LLM配置
        try {
            com.childproduct.designassistant.config.LLMConfig.init(this)
            android.util.Log.d("MainActivity", "LLM配置初始化成功")
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "LLM配置初始化失败", e)
        }

        setContent {
            ChildProductDesignAssistantTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var showExportDialog by remember { mutableStateOf(false) }
    var selectedModule by remember { mutableStateOf<String?>(null) }
    var showDesignProposal by remember { mutableStateOf(false) }
    val designProposalViewModel: DesignProposalViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("儿童产品设计助手") },
                actions = {
                    IconButton(onClick = { showExportDialog = true }) {
                        Icon(Icons.Default.Share, "导出")
                    }
                }
            )
        },
        bottomBar = {
            val tabLabels = listOf("创意生成", "安全检查", "技术建议", "文档生成", "更多")
            val tabIcons = listOf(
                Icons.Default.Lightbulb,
                Icons.Default.Security,
                Icons.Default.Build,
                Icons.Default.Description,
                Icons.Default.MoreVert
            )
            
            Surface(
                shadowElevation = 8.dp, // 增加阴影，提升视觉层次
                tonalElevation = 2.dp
            ) {
                NavigationBar(
                    modifier = Modifier.height(80.dp), // 确保足够高度
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    tabLabels.forEachIndexed { index, label ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { viewModel.selectTab(index) },
                            alwaysShowLabel = true, // 确保标签始终显示
                            icon = {
                                // 选中状态添加动画效果
                                AnimatedContent(
                                    targetState = selectedTab == index,
                                    label = "icon_animation_$index",
                                    transitionSpec = {
                                        fadeIn(
                                            animationSpec = tween(300)
                                        ) togetherWith fadeOut(
                                            animationSpec = tween(300)
                                        )
                                    }
                                ) { isSelected ->
                                    if (isSelected) {
                                        Icon(
                                            imageVector = tabIcons[index],
                                            contentDescription = label,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier
                                                .size(24.dp) // 确保图标大小一致
                                                .scale(1.1f) // 选中时放大
                                        )
                                    } else {
                                        Icon(
                                            imageVector = tabIcons[index],
                                            contentDescription = label,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            },
                            label = {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                    minLines = 1 // 确保至少占用一行，避免高度跳动
                                )
                            },
                            modifier = Modifier.height(72.dp) // 增加单个点击区域到72dp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        // 添加额外的底部内边距，确保内容不被底部导航栏遮挡
        val screenPadding = PaddingValues(
            start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
            top = paddingValues.calculateTopPadding(),
            end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
            bottom = paddingValues.calculateBottomPadding() + 8.dp // 额外8dp缓冲
        )

        AnimatedContent(
            targetState = selectedTab,
            label = "screen_transition",
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(300)
                ) togetherWith fadeOut(
                    animationSpec = tween(300)
                )
            }
        ) { targetTab ->
            // 显示设计方案界面
            if (showDesignProposal) {
                val designProposalViewModel: DesignProposalViewModel = viewModel()
                DesignProposalScreen(
                    proposal = designProposalViewModel.currentProposal.value ?: return@AnimatedContent,
                    onBack = { showDesignProposal = false }
                )
                return@AnimatedContent
            }

            // 处理 MoreScreen 的导航
            if (selectedModule != null) {
                when (selectedModule) {
                    "文档学习" -> DocumentLearningScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(screenPadding)
                    )
                    "智能问答" -> ChatQAScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(screenPadding)
                    )
                    "一键生成" -> OneClickGenerationScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(screenPadding)
                    )
                    "综合报告" -> IntegratedReportScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(screenPadding)
                    )
                    "测试矩阵" -> TestMatrixScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(screenPadding)
                    )
                    "设计建议" -> DesignSuggestionScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(screenPadding)
                    )
                    "竞品参考" -> CompetitorReferenceScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(screenPadding)
                    )
                    "合规管理" -> ComplianceManagementScreen(
                        viewModel = viewModel
                    )
                    "标准适配" -> StandardSelectionScreen(
                        onGenerateDesign = { selectedStandards ->
                            // 生成设计方案
                            val request = com.childproduct.designassistant.data.model.DesignProposalRequest(
                                productType = "儿童安全座椅",
                                selectedStandards = selectedStandards,
                                additionalRequirements = emptyList()
                            )
                            designProposalViewModel.generateProposal(request)
                            // 导航到设计方案界面
                            showDesignProposal = true
                        }
                    )
                    else -> CreativeScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(screenPadding)
                    )
                }
            } else {
                when (targetTab) {
                    0 -> CreativeScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(screenPadding)
                    )
                    1 -> SafetyScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(screenPadding)
                    )
                    2 -> TechnicalRecommendationScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(screenPadding)
                    )
                    3 -> DocumentScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(screenPadding)
                    )
                    4 -> MoreScreen(
                        onNavigate = { screen -> selectedModule = screen },
                        modifier = Modifier.padding(screenPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MoreScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(
            "文档学习" to Icons.Default.School,
            "智能问答" to Icons.Default.QuestionAnswer,
            "一键生成" to Icons.Default.AutoAwesome,
            "综合报告" to Icons.Default.Assessment,
            "测试矩阵" to Icons.Default.GridOn,
            "设计建议" to Icons.Default.Recommend,
            "竞品参考" to Icons.Default.CompareArrows,
            "合规管理" to Icons.Default.Gavel,
            "标准适配" to Icons.Default.Checklist
        ).forEach { (name, icon) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(name) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(icon, name)
                    Text(
                        text = name,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Default.ChevronRight, null)
                }
            }
        }
    }
}

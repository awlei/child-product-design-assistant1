package com.design.assistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.design.assistant.database.databases.ChildSeatStandardDatabase
import com.design.assistant.database.databases.GPS028Database
import com.design.assistant.database.databases.ProductStandardDatabase
import com.design.assistant.model.DesignResult
import com.design.assistant.model.GPS028Group
import com.design.assistant.repository.ChildSeatStandardRepository
import com.design.assistant.repository.GPS028Repository
import com.design.assistant.repository.ProductStandardRepository
import com.design.assistant.ui.screens.DesignResultScreen
import com.design.assistant.ui.screens.StandardSelectScreen
import com.design.assistant.ui.theme.DesignAssistantTheme
import kotlinx.coroutines.launch

/**
 * 主Activity
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DesignAssistantTheme {
                DesignAssistantNavHost()
            }
        }
    }
}

/**
 * 导航主机
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignAssistantNavHost() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    // 获取数据库实例
    val gps028Database = GPS028Database.getInstance(LocalContext.current)
    val childSeatStandardDatabase = ChildSeatStandardDatabase.getInstance(LocalContext.current)
    val productStandardDatabase = ProductStandardDatabase.getInstance(LocalContext.current)

    // 创建Repository
    val gps028Repository = remember { GPS028Repository(gps028Database) }
    val childSeatStandardRepository = remember { ChildSeatStandardRepository(childSeatStandardDatabase) }
    val productStandardRepository = remember { ProductStandardRepository(productStandardDatabase) }

    // 创建ViewModel
    val designGenerateViewModel: com.design.assistant.viewmodel.DesignGenerateViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return com.design.assistant.viewmodel.DesignGenerateViewModel(
                    gps028Repository,
                    childSeatStandardRepository,
                    productStandardRepository
                ) as T
            }
        }
    )

    val productStandardSelectViewModel: com.design.assistant.viewmodel.ProductStandardSelectViewModel =
        viewModel()

    // 设计结果状态
    var designResult by remember { mutableStateOf<DesignResult?>(null) }

    // 导航状态
    var currentScreen by remember { mutableStateOf("home") }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // 首页
        composable("home") {
            HomeScreen(
                onStartDesign = {
                    currentScreen = "select"
                    navController.navigate("select")
                },
                onViewHistory = {
                    // TODO: 实现历史记录功能
                },
                onSettings = {
                    // TODO: 实现设置功能
                }
            )
        }

        // 标准选择页面
        composable("select") {
            StandardSelectScreen(
                viewModel = productStandardSelectViewModel,
                onGenerateClick = { productType, standards, group, percentile ->
                    designGenerateViewModel.generateDesign(
                        productType,
                        standards,
                        group,
                        percentile
                    )
                    currentScreen = "result"
                    navController.navigate("result")
                },
                onBackClick = {
                    currentScreen = "home"
                    navController.popBackStack()
                }
            )
        }

        // 设计结果页面
        composable("result") {
            val uiState by designGenerateViewModel.uiState.collectAsState()

            LaunchedEffect(uiState.designResult) {
                designResult = uiState.designResult
            }

            if (uiState.isLoading) {
                LoadingScreen()
            } else if (uiState.error != null) {
                ErrorScreen(
                    error = uiState.error ?: "未知错误",
                    onRetry = {
                        // TODO: 重试逻辑
                    },
                    onBackClick = {
                        currentScreen = "select"
                        navController.popBackStack()
                    }
                )
            } else if (designResult != null) {
                DesignResultScreen(
                    designResult = designResult!!,
                    onBackClick = {
                        currentScreen = "select"
                        navController.popBackStack()
                    },
                    onExportClick = {
                        // TODO: 导出功能
                    }
                )
            }
        }
    }
}

/**
 * 首页
 */
@Composable
fun HomeScreen(
    onStartDesign: () -> Unit,
    onViewHistory: () -> Unit,
    onSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "儿童产品设计助手",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "专业设计参数生成工具",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "设置"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // 欢迎卡片
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChildCare,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "欢迎使用",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "选择产品类型和适用标准，\n生成专业的设计参数和兼容性分析",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 开始设计按钮
            Button(
                onClick = onStartDesign,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "开始设计",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // 查看历史记录
            OutlinedButton(
                onClick = onViewHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "查看历史记录",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 底部信息
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "支持的产品类型",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProductTypeIcon(name = "儿童安全座椅")
                    ProductTypeIcon(name = "婴儿推车")
                    ProductTypeIcon(name = "高脚椅")
                    ProductTypeIcon(name = "儿童床")
                }

                Text(
                    text = "v2.0.0 | 支持GPS028、ECE R129、CMVSS213等标准",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

/**
 * 产品类型图标
 */
@Composable
fun ProductTypeIcon(
    name: String
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(
            text = name.take(2),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * 加载界面
 */
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp)
            )

            Text(
                text = "正在生成设计方案...",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * 错误界面
 */
@Composable
fun ErrorScreen(
    error: String,
    onRetry: () -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )

            Text(
                text = "生成失败",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )

            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("返回")
                }

                Button(
                    onClick = onRetry,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("重试")
                }
            }
        }
    }
}

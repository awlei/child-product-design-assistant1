package com.design.assistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.design.assistant.model.ProductType
import com.design.assistant.model.StandardType
import com.design.assistant.model.getDisplayName
import com.design.assistant.ui.components.ProductTypeCard
import com.design.assistant.ui.components.ProductAccordion
import com.design.assistant.viewmodel.ProductStandardSelectViewModel

/**
 * 标准选择页面
 * 用于选择产品类型和适用的标准
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardSelectScreen(
    viewModel: ProductStandardSelectViewModel,
    onGenerateClick: (com.design.assistant.model.ProductType, List<com.design.assistant.model.StandardType>, Int, Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // 儿童身高和体重输入
    var childHeight by remember { mutableStateOf("") }
    var childWeight by remember { mutableStateOf("") }

    // 根据产品类型和标准获取身高体重范围
    val heightWeightRange = remember(uiState.selectedProductType, uiState.selectedStandards) {
        getHeightWeightRange(uiState.selectedProductType, uiState.selectedStandards)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "选择产品和标准",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "选择产品类型和适用的标准",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.clearSelection() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("清空选择")
                    }

                    Button(
                        onClick = {
                            uiState.selectedProductType?.let { productType ->
                                onGenerateClick(
                                    productType,
                                    uiState.selectedStandards,
                                    childHeight.toIntOrNull() ?: 0,
                                    childWeight.toIntOrNull() ?: 0
                                )
                            }
                        },
                        enabled = viewModel.canProceed() &&
                                 childHeight.isNotBlank() &&
                                 childWeight.isNotBlank() &&
                                 childHeight.toIntOrNull()?.let { height ->
                                     height in heightWeightRange.minHeight..heightWeightRange.maxHeight
                                 } == true &&
                                 childWeight.toIntOrNull()?.let { weight ->
                                     weight in heightWeightRange.minWeight..heightWeightRange.maxWeight
                                 } == true,
                        modifier = Modifier.weight(2f)
                    ) {
                        Text("生成设计方案")
                    }
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 产品类型选择
            SectionTitle(title = "1. 选择产品类型")

            uiState.productTypes.forEach { productType ->
                ProductTypeCard(
                    productType = productType,
                    selected = uiState.selectedProductType == productType,
                    onClick = { viewModel.selectProductType(productType) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 标准选择
            if (uiState.selectedProductType != null) {
                SectionTitle(title = "2. 选择适用标准")

                var expanded by remember { mutableStateOf(true) }

                ProductAccordion(
                    productType = uiState.selectedProductType!!,
                    standards = uiState.availableStandards,
                    expanded = expanded,
                    selectedStandards = uiState.selectedStandards,
                    onExpandedChange = { isExpanded: Boolean -> expanded = isExpanded },
                    onStandardClick = { standard: StandardType ->
                        viewModel.toggleStandard(standard)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 儿童身高体重输入
                SectionTitle(title = "3. 输入儿童参数")

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 标准范围提示
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = "📋 ${heightWeightRange.description}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(12.dp)
                            )
                        }

                        // 身高输入
                        OutlinedTextField(
                            value = childHeight,
                            onValueChange = {
                                // 只允许输入数字
                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                    childHeight = it
                                }
                            },
                            label = { Text("儿童身高 (cm)") },
                            placeholder = { Text("${heightWeightRange.minHeight}-${heightWeightRange.maxHeight}") },
                            singleLine = true,
                            isError = childHeight.isNotEmpty() &&
                                       (childHeight.toIntOrNull()?.let { it !in heightWeightRange.minHeight..heightWeightRange.maxHeight } ?: true),
                            supportingText = {
                                if (childHeight.isNotEmpty()) {
                                    val height = childHeight.toIntOrNull()
                                    when {
                                        height == null || height < heightWeightRange.minHeight || height > heightWeightRange.maxHeight -> {
                                            Text("请输入${heightWeightRange.minHeight}-${heightWeightRange.maxHeight}之间的数字", color = MaterialTheme.colorScheme.error)
                                        }
                                        else -> {
                                            Text("✓ 当前身高: ${height}cm (在标准范围内)")
                                        }
                                    }
                                } else {
                                    Text("范围: ${heightWeightRange.minHeight}-${heightWeightRange.maxHeight}cm")
                                }
                            },
                            leadingIcon = {
                                Text("📏")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // 体重输入
                        OutlinedTextField(
                            value = childWeight,
                            onValueChange = {
                                // 只允许输入数字
                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                    childWeight = it
                                }
                            },
                            label = { Text("儿童体重 (kg)") },
                            placeholder = { Text("${heightWeightRange.minWeight}-${heightWeightRange.maxWeight}") },
                            singleLine = true,
                            isError = childWeight.isNotEmpty() &&
                                       (childWeight.toIntOrNull()?.let { it !in heightWeightRange.minWeight..heightWeightRange.maxWeight } ?: true),
                            supportingText = {
                                if (childWeight.isNotEmpty()) {
                                    val weight = childWeight.toIntOrNull()
                                    when {
                                        weight == null || weight < heightWeightRange.minWeight || weight > heightWeightRange.maxWeight -> {
                                            Text("请输入${heightWeightRange.minWeight}-${heightWeightRange.maxWeight}之间的数字", color = MaterialTheme.colorScheme.error)
                                        }
                                        else -> {
                                            Text("✓ 当前体重: ${weight}kg (在标准范围内)")
                                        }
                                    }
                                } else {
                                    Text("范围: ${heightWeightRange.minWeight}-${heightWeightRange.maxWeight}kg")
                                }
                            },
                            leadingIcon = {
                                Text("⚖️")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // 参数提示
                        if (childHeight.isNotBlank() && childWeight.isNotBlank()) {
                            val height = childHeight.toIntOrNull() ?: 0
                            val weight = childWeight.toIntOrNull() ?: 0

                            // 根据产品类型和身高体重判断年龄段
                            val ageHint = when (uiState.selectedProductType) {
                                com.design.assistant.model.ProductType.CHILD_SEAT -> {
                                    when {
                                        height in 40..65 && weight in 0..9 -> "新生儿/婴儿 (0-9个月)"
                                        height in 66..85 && weight in 9..13 -> "幼儿 I组 (9-18个月)"
                                        height in 86..105 && weight in 9..18 -> "幼儿 II组 (1.5-4岁)"
                                        height in 100..125 && weight in 15..25 -> "儿童 III组 (3-6岁)"
                                        height in 125..150 && weight in 22..36 -> "大龄儿童 IV组 (6-12岁)"
                                        else -> "不在标准组别范围内"
                                    }
                                }
                                com.design.assistant.model.ProductType.STROLLER -> {
                                    when {
                                        height in 0..65 -> "新生儿阶段"
                                        height in 66..95 -> "婴儿阶段 (可坐立)"
                                        height in 96..125 -> "幼儿阶段"
                                        else -> "超出适用范围"
                                    }
                                }
                                com.design.assistant.model.ProductType.HIGH_CHAIR -> {
                                    when {
                                        height in 60..80 -> "小童 (约6-12个月)"
                                        height in 81..95 -> "幼儿 (约1-3岁)"
                                        height in 96..110 -> "大童 (约3-6岁)"
                                        else -> "超出适用范围"
                                    }
                                }
                                com.design.assistant.model.ProductType.CRIB -> {
                                    when {
                                        height in 50..85 -> "婴儿期"
                                        height in 86..110 -> "幼儿期"
                                        height in 111..130 -> "儿童期"
                                        else -> "超出适用范围"
                                    }
                                }
                                else -> "未知年龄段"
                            }

                            val isInValidRange = height in heightWeightRange.minHeight..heightWeightRange.maxHeight &&
                                               weight in heightWeightRange.minWeight..heightWeightRange.maxWeight

                            Surface(
                                color = if (isInValidRange)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.errorContainer,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    text = if (isInValidRange)
                                        "👶 预估年龄段: $ageHint"
                                    else
                                        "⚠️ 参数超出标准范围: $ageHint",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 已选择的标准摘要
                if (uiState.selectedStandards.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "已选择 ${uiState.selectedStandards.size} 个标准",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = uiState.selectedStandards.joinToString("、") { standard: StandardType -> standard.getDisplayName() },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                // 提示选择产品类型
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "请先选择产品类型",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * 根据产品类型和标准获取身高体重范围
 */
fun getHeightWeightRange(
    productType: com.design.assistant.model.ProductType?,
    standards: List<com.design.assistant.model.StandardType>
): HeightWeightRange {
    if (productType == null) {
        return HeightWeightRange(
            minHeight = 1,
            maxHeight = 150,
            minWeight = 1,
            maxWeight = 50,
            description = "请先选择产品类型"
        )
    }

    return when (productType) {
        com.design.assistant.model.ProductType.CHILD_SEAT -> {
            // 儿童安全座椅：根据不同标准
            val hasGPS028 = standards.contains(com.design.assistant.model.StandardType.GPS028)
            val hasECE_R129 = standards.contains(com.design.assistant.model.StandardType.ECE_R129)
            val hasCMVSS213 = standards.contains(com.design.assistant.model.StandardType.CMVSS213)
            val hasFMVSS213 = standards.contains(com.design.assistant.model.StandardType.FMVSS213)

            when {
                hasGPS028 || hasECE_R129 -> HeightWeightRange(
                    minHeight = 40,
                    maxHeight = 150,
                    minWeight = 0,
                    maxWeight = 36,
                    description = "GB 27887/ECE R129标准：40-150cm，0-36kg"
                )
                hasCMVSS213 || hasFMVSS213 -> HeightWeightRange(
                    minHeight = 50,
                    maxHeight = 145,
                    minWeight = 2,
                    maxWeight = 36,
                    description = "北美标准：50-145cm，2-36kg"
                )
                else -> HeightWeightRange(
                    minHeight = 40,
                    maxHeight = 150,
                    minWeight = 0,
                    maxWeight = 36,
                    description = "通用范围：40-150cm，0-36kg"
                )
            }
        }
        com.design.assistant.model.ProductType.STROLLER -> {
            // 婴儿推车
            val hasEN1888 = standards.contains(com.design.assistant.model.StandardType.EN1888)
            val hasASTM_F833 = standards.contains(com.design.assistant.model.StandardType.ASTM_F833)
            val hasCSA_B311 = standards.contains(com.design.assistant.model.StandardType.CSA_B311)

            when {
                hasEN1888 -> HeightWeightRange(
                    minHeight = 0,
                    maxHeight = 105,
                    minWeight = 0,
                    maxWeight = 22,
                    description = "EN 1888标准：0-105cm，0-22kg"
                )
                hasASTM_F833 || hasCSA_B311 -> HeightWeightRange(
                    minHeight = 0,
                    maxHeight = 125,
                    minWeight = 0,
                    maxWeight = 22,
                    description = "北美标准：0-125cm，0-22kg"
                )
                else -> HeightWeightRange(
                    minHeight = 0,
                    maxHeight = 125,
                    minWeight = 0,
                    maxWeight = 22,
                    description = "通用范围：0-125cm，0-22kg"
                )
            }
        }
        com.design.assistant.model.ProductType.HIGH_CHAIR -> {
            // 儿童高脚椅
            val hasEN14988 = standards.contains(com.design.assistant.model.StandardType.EN14988)
            val hasASTM_F404 = standards.contains(com.design.assistant.model.StandardType.ASTM_F404)
            val hasCSA_B229 = standards.contains(com.design.assistant.model.StandardType.CSA_B229)

            when {
                hasEN14988 -> HeightWeightRange(
                    minHeight = 60,
                    maxHeight = 105,
                    minWeight = 8,
                    maxWeight = 25,
                    description = "EN 14988标准：60-105cm，8-25kg"
                )
                hasASTM_F404 || hasCSA_B229 -> HeightWeightRange(
                    minHeight = 60,
                    maxHeight = 110,
                    minWeight = 8,
                    maxWeight = 25,
                    description = "北美标准：60-110cm，8-25kg"
                )
                else -> HeightWeightRange(
                    minHeight = 60,
                    maxHeight = 110,
                    minWeight = 8,
                    maxWeight = 25,
                    description = "通用范围：60-110cm，8-25kg"
                )
            }
        }
        com.design.assistant.model.ProductType.CRIB -> {
            // 儿童床
            val hasEN716 = standards.contains(com.design.assistant.model.StandardType.EN716)
            val hasASTM_F1169 = standards.contains(com.design.assistant.model.StandardType.ASTM_F1169)
            val hasCSA_B113 = standards.contains(com.design.assistant.model.StandardType.CSA_B113)

            when {
                hasEN716 -> HeightWeightRange(
                    minHeight = 50,
                    maxHeight = 125,
                    minWeight = 5,
                    maxWeight = 30,
                    description = "EN 716标准：50-125cm，5-30kg"
                )
                hasASTM_F1169 || hasCSA_B113 -> HeightWeightRange(
                    minHeight = 50,
                    maxHeight = 130,
                    minWeight = 5,
                    maxWeight = 35,
                    description = "北美标准：50-130cm，5-35kg"
                )
                else -> HeightWeightRange(
                    minHeight = 50,
                    maxHeight = 130,
                    minWeight = 5,
                    maxWeight = 35,
                    description = "通用范围：50-130cm，5-35kg"
                )
            }
        }
    }
}

/**
 * 身高体重范围数据类
 */
data class HeightWeightRange(
    val minHeight: Int,
    val maxHeight: Int,
    val minWeight: Int,
    val maxWeight: Int,
    val description: String
)

/**
 * 区块标题
 */
@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(horizontal = 16.dp)
    )
}

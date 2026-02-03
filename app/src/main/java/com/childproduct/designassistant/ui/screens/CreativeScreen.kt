package com.childproduct.designassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.childproduct.designassistant.model.*
import com.childproduct.designassistant.model.ProductType
import com.childproduct.designassistant.ui.MainViewModel
import com.childproduct.designassistant.ui.UiState
import com.childproduct.designassistant.utils.TetherType

/**
 * 重构后的创意生成界面（方案生成）
 * 实现产品类型-标准-参数输入联动逻辑
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreativeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val creativeIdea by viewModel.creativeIdea.collectAsState()

    // 状态管理
    var selectedProductType by remember { mutableStateOf<ProductType?>(null) }
    var selectedStandards by remember { mutableStateOf<Set<StandardConfig>>(emptySet()) }
    var expandedProductTypes by remember { mutableStateOf<Set<ProductType>>(emptySet()) }
    
    // 身高范围输入
    var minHeight by remember { mutableStateOf("") }
    var maxHeight by remember { mutableStateOf("") }
    
    // 体重范围输入
    var minWeight by remember { mutableStateOf("") }
    var maxWeight by remember { mutableStateOf("") }
    var weightUnit by remember { mutableStateOf("kg") }
    
    // 年龄范围输入
    var minAge by remember { mutableStateOf("") }
    var maxAge by remember { mutableStateOf("") }
    
    // 设计主题
    var theme by remember { mutableStateOf("") }
    
    // Tether类型选择（仅用于ECE R129标准的儿童安全座椅）
    var selectedTetherType by remember { mutableStateOf(TetherType.BOTH) }
    
    // 参数验证结果
    var paramValidationResult by remember { mutableStateOf<ParamInputResult?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }

    // 格式化输出（用于显示）
    var formattedOutput by remember { mutableStateOf<String?>(null) }

    // 参数验证函数
    fun validateAndSetResult() {
        selectedStandards.firstOrNull()?.let { standard ->
            when (standard.inputItem.inputType) {
                InputType.HEIGHT_RANGE -> {
                    val minH = minHeight.toDoubleOrNull()
                    val maxH = maxHeight.toDoubleOrNull()
                    if (minH != null && maxH != null) {
                        val result = ProductTypeConfigManager.validateHeightRange(minH, maxH, standard)
                        paramValidationResult = result
                        validationError = if (!result.isValid) result.errorMessage else null
                    } else {
                        paramValidationResult = null
                        validationError = null
                    }
                }
                InputType.WEIGHT_RANGE -> {
                    val minW = minWeight.toDoubleOrNull()
                    val maxW = maxWeight.toDoubleOrNull()
                    if (minW != null && maxW != null) {
                        val result = ProductTypeConfigManager.validateWeightRange(minW, maxW, weightUnit == "lb", standard)
                        paramValidationResult = result
                        validationError = if (!result.isValid) result.errorMessage else null
                    } else {
                        paramValidationResult = null
                        validationError = null
                    }
                }
                InputType.AGE_RANGE -> {
                    val minA = minAge.toDoubleOrNull()
                    val maxA = maxAge.toDoubleOrNull()
                    if (minA != null && maxA != null) {
                        val result = ProductTypeConfigManager.validateAgeRange(minA, maxA, standard)
                        paramValidationResult = result
                        validationError = if (!result.isValid) result.errorMessage else null
                    } else {
                        paramValidationResult = null
                        validationError = null
                    }
                }
                else -> {}
            }
        }
    }

    // 获取当前产品类型配置
    val productConfig = selectedProductType?.let { ProductTypeConfigManager.getConfigByProductType(it) }

    // 生成按钮激活条件
    val isGenerateButtonEnabled = selectedProductType != null &&
                                   selectedStandards.isNotEmpty() &&
                                   paramValidationResult?.isValid == true &&
                                   uiState !is UiState.Loading

    // 合规组合显示
    val complianceCombination = remember(selectedProductType, selectedStandards, paramValidationResult) {
        val productType = selectedProductType
        val standard = selectedStandards.firstOrNull()
        val validationResult = paramValidationResult

        if (productType != null && standard != null && validationResult?.isValid == true) {
            val paramValue = when (standard.inputItem.inputType) {
                InputType.HEIGHT_RANGE -> "${minHeight}-${maxHeight}cm"
                InputType.WEIGHT_RANGE -> "${minWeight}-${maxWeight}${weightUnit}"
                InputType.AGE_RANGE -> "${minAge}-${maxAge}岁"
                else -> ""
            }
            ComplianceCombination(
                productType = productType,
                standardName = standard.standardName,
                paramValue = paramValue,
                matchedDummy = validationResult.matchedDummy,
                coreRequirements = standard.coreRequirements,
                isValid = true
            )
        } else {
            null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 页面标题
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "标准适配设计",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ========== 1. 产品类型选择 ==========
                Text(
                    text = "1. 产品类型",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // 出行类
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TravelExplore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "出行类",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                listOf(ProductType.CHILD_SAFETY_SEAT, ProductType.CHILD_STROLLER).forEach { productType ->
                    val standards = ProductTypeConfigManager.getConfigByProductType(productType)?.standards ?: emptyList()
                    ProductTypeAccordion(
                        productType = productType,
                        isExpanded = expandedProductTypes.contains(productType),
                        isSelected = selectedProductType == productType,
                        selectedStandards = selectedStandards.filter { it in standards }.toSet(),
                        standards = standards,
                        onToggleExpand = {
                            expandedProductTypes = if (expandedProductTypes.contains(productType)) {
                                expandedProductTypes - productType
                            } else {
                                expandedProductTypes + productType
                            }
                        },
                        onSelectProduct = {
                            selectedProductType = productType
                            // 切换产品时，重置已选择的标准
                            selectedStandards = emptySet()
                            selectedTetherType = TetherType.SUPPORT_LEG
                            paramValidationResult = null
                            validationError = null
                        },
                        onSelectStandard = { standard, selected ->
                            selectedStandards = if (selected) {
                                selectedStandards + standard
                            } else {
                                selectedStandards - standard
                            }
                            paramValidationResult = null
                            validationError = null
                        },
                        onSelectAllStandards = {
                            selectedStandards = standards.toSet()
                            paramValidationResult = null
                            validationError = null
                        },
                        onDeselectAllStandards = {
                            selectedStandards = emptySet()
                            paramValidationResult = null
                            validationError = null
                        }
                    )
                }

                // 家居类
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "家居类",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                listOf(ProductType.CHILD_HOUSEHOLD_GOODS, ProductType.CHILD_HIGH_CHAIR, ProductType.CRIB).forEach { productType ->
                    val standards = ProductTypeConfigManager.getConfigByProductType(productType)?.standards ?: emptyList()
                    ProductTypeAccordion(
                        productType = productType,
                        isExpanded = expandedProductTypes.contains(productType),
                        isSelected = selectedProductType == productType,
                        selectedStandards = selectedStandards.filter { it in standards }.toSet(),
                        standards = standards,
                        onToggleExpand = {
                            expandedProductTypes = if (expandedProductTypes.contains(productType)) {
                                expandedProductTypes - productType
                            } else {
                                expandedProductTypes + productType
                            }
                        },
                        onSelectProduct = {
                            selectedProductType = productType
                            selectedStandards = emptySet()
                            selectedTetherType = TetherType.SUPPORT_LEG
                            paramValidationResult = null
                            validationError = null
                        },
                        onSelectStandard = { standard, selected ->
                            selectedStandards = if (selected) {
                                selectedStandards + standard
                            } else {
                                selectedStandards - standard
                            }
                            paramValidationResult = null
                            validationError = null
                        },
                        onSelectAllStandards = {
                            selectedStandards = standards.toSet()
                            paramValidationResult = null
                            validationError = null
                        },
                        onDeselectAllStandards = {
                            selectedStandards = emptySet()
                            paramValidationResult = null
                            validationError = null
                        }
                    )
                }

                // ========== 补充标准引导提示 ==========
                if (selectedProductType != null && selectedStandards.isEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "请补充选择标准",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "点击产品类型卡片展开，选择1个或多个适用的安全标准",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }

                // ========== 2. 参数输入（仅在有选中标准后显示） ==========
                if (selectedStandards.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "2. 参数输入",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " *",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // 使用第一个选中的标准来确定输入类型
                    val firstStandard = selectedStandards.first()
                    when (firstStandard.inputItem.inputType) {
                        InputType.HEIGHT_RANGE -> {
                            // 身高范围输入
                            ParameterInputRow(
                                label = firstStandard.inputItem.inputLabel,
                                unit = firstStandard.inputItem.unit,
                                placeholder = firstStandard.inputItem.placeholder,
                                minValue = minHeight,
                                maxValue = maxHeight,
                                onMinValueChange = {
                                    minHeight = it
                                    validateAndSetResult()
                                },
                                onMaxValueChange = {
                                    maxHeight = it
                                    validateAndSetResult()
                                }
                            )
                        }
                        InputType.WEIGHT_RANGE -> {
                            // 体重范围输入（支持lb/kg切换）
                            
                            // 单位选择器
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "单位：",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                SingleChoiceSegmentedButtonRow {
                                    SegmentedButton(
                                        selected = weightUnit == "lb",
                                        onClick = { weightUnit = "lb" },
                                        shape = SegmentedButtonDefaults.itemShape(
                                            index = 0,
                                            count = 2
                                        ),
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    ) {
                                        Text("lb")
                                    }
                                    SegmentedButton(
                                        selected = weightUnit == "kg",
                                        onClick = { weightUnit = "kg" },
                                        shape = SegmentedButtonDefaults.itemShape(
                                            index = 1,
                                            count = 2
                                        ),
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    ) {
                                        Text("kg")
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // 参数输入
                            ParameterInputRow(
                                label = firstStandard.inputItem.inputLabel,
                                unit = weightUnit,
                                placeholder = firstStandard.inputItem.placeholder,
                                minValue = minWeight,
                                maxValue = maxWeight,
                                onMinValueChange = {
                                    minWeight = it
                                    validateAndSetResult()
                                },
                                onMaxValueChange = {
                                    maxWeight = it
                                    validateAndSetResult()
                                }
                            )
                        }
                        InputType.AGE_RANGE -> {
                            // 年龄范围输入
                            ParameterInputRow(
                                label = firstStandard.inputItem.inputLabel,
                                unit = firstStandard.inputItem.unit,
                                placeholder = firstStandard.inputItem.placeholder,
                                minValue = minAge,
                                maxValue = maxAge,
                                onMinValueChange = {
                                    minAge = it
                                    validateAndSetResult()
                                },
                                onMaxValueChange = {
                                    maxAge = it
                                    validateAndSetResult()
                                }
                            )
                        }
                        else -> {}
                    }

                    // 显示验证错误信息
                    validationError?.let { error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    // 显示匹配的假人类型/区间信息
                    paramValidationResult?.let { result ->
                        if (result.isValid) {
                            result.matchedDummy?.let { dummy ->
                                Text(
                                    text = "匹配假人类型: ${dummy.displayName} (${dummy.heightRange})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            result.matchedInterval?.let { interval ->
                                Text(
                                    text = "匹配区间: $interval",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }

                    // 显示选中标准的核心约束
                    if (selectedStandards.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "标准核心约束",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                                selectedStandards.forEach { standard ->
                                    Text(
                                        text = "• ${standard.standardName}: ${standard.coreRequirements}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // ========== 3. 设计主题（可选） ==========
                if (selectedStandards.isNotEmpty()) {
                    OutlinedTextField(
                        value = theme,
                        onValueChange = { theme = it },
                        label = { Text("设计主题（可选）") },
                        placeholder = { Text("输入特定设计需求，如材质风格、功能偏好等") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // ========== 5. Tether类型选择（仅儿童安全座椅ECE R129标准显示）==========
                val isShowTetherSelector = selectedProductType == ProductType.SAFETY_SEAT &&
                                         selectedStandards.any { it.standardId == "ECE_R129" }

                if (isShowTetherSelector) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "5. Tether类型",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    var isExpanded by remember { mutableStateOf(false) }
                    
                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = { isExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedTetherType.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("选择Tether类型") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            singleLine = true
                        )
                        
                        ExposedDropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }
                        ) {
                            TetherType.values().forEach { tetherType ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = tetherType.displayName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = tetherType.description,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedTetherType = tetherType
                                        isExpanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                    
                    // Tether类型说明
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "Tether类型说明：",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = selectedTetherType.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                // ========== 生成按钮 ==========
                Button(
                    onClick = {
                        val firstSelected = selectedStandards.firstOrNull()
                        val heightCm = if (firstSelected?.inputItem?.inputType == InputType.HEIGHT_RANGE) {
                            (minHeight.toIntOrNull() ?: 0) to (maxHeight.toIntOrNull() ?: 0)
                        } else {
                            null
                        }
                        
                        // 使用第一个选中的标准来确定输入类型
                        val firstStandard = selectedStandards.first()
                        when (firstStandard.inputItem.inputType) {
                            InputType.HEIGHT_RANGE -> {
                                val minH = minHeight.toIntOrNull() ?: 0
                                val maxH = maxHeight.toIntOrNull() ?: 0
                                val ageGroup = if (minH <= 40 && maxH >= 150) {
                                    // 覆盖整个标准范围（40-150cm）对应全年龄段（0-12岁）
                                    AgeGroup.ALL
                                } else {
                                    // 其他身高范围按假人类型推断
                                    val dummyType = paramValidationResult?.matchedDummy
                                    when (dummyType) {
                                        CrashTestDummy.Q0, CrashTestDummy.Q0_PLUS, CrashTestDummy.Q1 -> AgeGroup.INFANT
                                        CrashTestDummy.Q1_5 -> AgeGroup.TODDLER
                                        CrashTestDummy.Q3 -> AgeGroup.PRESCHOOL
                                        CrashTestDummy.Q3_S -> AgeGroup.SCHOOL_AGE
                                        CrashTestDummy.Q6 -> AgeGroup.SCHOOL_AGE
                                        else -> {
                                            // 如果假人类型无法确定，根据身高范围推断
                                            when {
                                                maxH <= 87 -> AgeGroup.INFANT
                                                maxH <= 105 -> AgeGroup.TODDLER
                                                maxH <= 125 -> AgeGroup.PRESCHOOL
                                                maxH <= 145 -> AgeGroup.SCHOOL_AGE
                                                else -> AgeGroup.TEEN
                                            }
                                        }
                                    }
                                }
                                
                                // 检查是否是儿童安全座椅且选择了ECE R129标准
                                val currentProductType = selectedProductType
                                val hasECE_R129 = selectedStandards.any { it.standardId == "ECE_R129" }

                                if (currentProductType == ProductType.SAFETY_SEAT && hasECE_R129) {
                                    // 使用Roadmate360OutputGenerator生成ROADMATE 360格式输出
                                    val finalTheme = theme.ifEmpty { "标准设计" }
                                    formattedOutput = com.childproduct.designassistant.utils.Roadmate360OutputGenerator.generateOutput(
                                        minHeightCm = minH,
                                        maxHeightCm = maxH,
                                        productType = currentProductType.displayName,
                                        designTheme = finalTheme,
                                        tetherType = selectedTetherType
                                    )
                                } else {
                                    // 其他情况使用原有的生成逻辑
                                    viewModel.generateCreativeIdea(ageGroup, currentProductType!!, theme)
                                }
                            }
                            InputType.WEIGHT_RANGE -> {
                                // 根据体重推断年龄段
                                val currentProductType = selectedProductType
                                val weight = minWeight.toDoubleOrNull() ?: 0.0
                                val ageGroup = if (weight < 9.0) AgeGroup.INFANT
                                               else if (weight < 18.0) AgeGroup.TODDLER
                                               else if (weight < 21.0) AgeGroup.PRESCHOOL
                                               else if (weight < 33.0) AgeGroup.SCHOOL_AGE
                                               else AgeGroup.TEEN
                                viewModel.generateCreativeIdea(ageGroup, currentProductType!!, theme)
                            }
                            InputType.AGE_RANGE -> {
                                val currentProductType = selectedProductType
                                val age = minAge.toIntOrNull() ?: 0
                                val ageGroup = when (age) {
                                    0, 1, 2 -> AgeGroup.INFANT
                                    3, 4, 5 -> AgeGroup.TODDLER
                                    6, 7, 8 -> AgeGroup.PRESCHOOL
                                    9, 10, 11 -> AgeGroup.SCHOOL_AGE
                                    else -> AgeGroup.TEEN
                                }
                                viewModel.generateCreativeIdea(ageGroup, currentProductType!!, theme)
                            }
                            else -> {}
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isGenerateButtonEnabled
                ) {
                    if (uiState is UiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            "生成设计方案",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                // 提示信息
                if (!isGenerateButtonEnabled) {
                    val hintText = when {
                        selectedProductType == null -> "请先选择产品类型"
                        selectedStandards.isEmpty() -> "⚠️ 请至少选择1个产品标准"
                        paramValidationResult?.isValid != true -> "⚠️ 请输入有效的参数范围"
                        uiState is UiState.Loading -> "正在生成..."
                        else -> ""
                    }
                    
                    Text(
                        text = hintText,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (hintText.contains("⚠️")) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 显示生成的结果
        if (formattedOutput != null) {
            // 显示ROADMATE 360格式输出
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "设计方案输出",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = formattedOutput!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else {
            // 显示原有格式（CreativeIdea）
            creativeIdea?.let { idea ->
                CreativeIdeaCard(idea = idea)
            }
        }
    }
}

/**
 * 产品类型折叠卡片
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductTypeAccordion(
    productType: ProductType,
    isExpanded: Boolean,
    isSelected: Boolean,
    selectedStandards: Set<StandardConfig>,
    standards: List<StandardConfig>,
    onToggleExpand: () -> Unit,
    onSelectProduct: () -> Unit,
    onSelectStandard: (StandardConfig, Boolean) -> Unit,
    onSelectAllStandards: () -> Unit,
    onDeselectAllStandards: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.primaryContainer
                isExpanded -> MaterialTheme.colorScheme.surfaceVariant
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column {
            // 头部：产品类型选择 + 展开/折叠按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (productType) {
                            ProductType.CHILD_SAFETY_SEAT -> Icons.Default.DirectionsCar
                            ProductType.CHILD_STROLLER -> Icons.Default.DirectionsWalk
                            ProductType.CHILD_HOUSEHOLD_GOODS -> Icons.Default.Toys
                            ProductType.CHILD_HIGH_CHAIR -> Icons.Default.Chair
                            ProductType.CRIB -> Icons.Default.Bed
                            else -> Icons.Default.Category
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = productType.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    IconButton(
                        onClick = onToggleExpand
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "收起" else "展开",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 展开后的标准列表
            if (isExpanded) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 全选/取消全选按钮
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (selectedStandards.size == standards.size) {
                            TextButton(
                                onClick = onDeselectAllStandards,
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ClearAll,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("取消全选", style = MaterialTheme.typography.bodySmall)
                            }
                        } else {
                            TextButton(
                                onClick = onSelectAllStandards,
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SelectAll,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("全选", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    // 标准列表（按地区分组）
                    val domesticStandards = standards.filter { it.region == "DOMESTIC" }
                    val internationalStandards = standards.filter { it.region == "INTERNATIONAL" }

                    // 滚动区域
                    Column(
                        modifier = Modifier
                            .height(200.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // 国内标准
                        if (domesticStandards.isNotEmpty()) {
                            Text(
                                text = "国内标准",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            domesticStandards.forEach { standard ->
                                StandardCheckbox(
                                    standard = standard,
                                    isChecked = standard in selectedStandards,
                                    onCheckedChange = { checked ->
                                        if (checked) {
                                            onSelectProduct()
                                        }
                                        onSelectStandard(standard, checked)
                                    }
                                )
                            }

                            // 添加分隔线
                            if (internationalStandards.isNotEmpty()) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    thickness = 0.5.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }

                        // 国际标准
                        if (internationalStandards.isNotEmpty()) {
                            Text(
                                text = "国际标准",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            internationalStandards.forEach { standard ->
                                StandardCheckbox(
                                    standard = standard,
                                    isChecked = standard in selectedStandards,
                                    onCheckedChange = { checked ->
                                        if (checked) {
                                            onSelectProduct()
                                        }
                                        onSelectStandard(standard, checked)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 标准复选框
 */
@Composable
fun StandardCheckbox(
    standard: StandardConfig,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isChecked) 2.dp else 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = standard.standardName,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                    fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                    lineHeight = 18.sp,
                    maxLines = 2
                )
            }
        }
    }
}

/**
 * 参数输入行
 */
@Composable
fun ParameterInputRow(
    label: String,
    unit: String,
    placeholder: String,
    minValue: String,
    maxValue: String,
    onMinValueChange: (String) -> Unit,
    onMaxValueChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = " *",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = minValue,
            onValueChange = { 
                if (it.isEmpty() || it.all { char -> char.isDigit() || char == '.' }) {
                    onMinValueChange(it)
                }
            },
            label = { Text("最小值") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            placeholder = { Text(placeholder) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        OutlinedTextField(
            value = maxValue,
            onValueChange = { 
                if (it.isEmpty() || it.all { char -> char.isDigit() || char == '.' }) {
                    onMaxValueChange(it)
                }
            },
            label = { Text("最大值") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            placeholder = { Text(placeholder) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
    }
}

/**
 * 合规组合卡片
 */
@Composable
fun ComplianceCombinationCard(combination: ComplianceCombination) {
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
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = "当前合规组合",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Text(
                text = combination.getDisplayText(),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = combination.coreRequirements,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * 创意展示卡片
 */
@Composable
fun CreativeIdeaCard(idea: CreativeIdea) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 标题
            Text(
                text = idea.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 描述
            Text(
                text = idea.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 年龄组
            Text(
                text = "年龄段: ${idea.ageGroup.displayName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
            
            // 产品类型
            Text(
                text = "产品类型: ${idea.productType.displayName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
            
            // 设计主题
            if (idea.theme.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "主题: $idea.theme",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
            
            // 特性
            if (idea.features.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "特性:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                idea.features.forEach { feature ->
                    Text(
                        text = "• $feature",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                    )
                }
            }
            
            // 材料
            if (idea.materials.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "材料:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                idea.materials.forEach { material ->
                    Text(
                        text = "• $material",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                    )
                }
            }
            
            // 安全提示
            if (idea.safetyNotes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "安全提示:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                idea.safetyNotes.forEach { note ->
                    Text(
                        text = "⚠ $note",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

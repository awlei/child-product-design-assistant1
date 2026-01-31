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
import com.childproduct.designassistant.model.*
import com.childproduct.designassistant.ui.MainViewModel
import com.childproduct.designassistant.ui.UiState

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
    var selectedStandard by remember { mutableStateOf<StandardConfig?>(null) }
    
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
    
    // 参数验证结果
    var paramValidationResult by remember { mutableStateOf<ParamInputResult?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }

    // 参数验证函数
    fun validateAndSetResult() {
        selectedStandard?.let { standard ->
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
                                   selectedStandard != null &&
                                   paramValidationResult?.isValid == true &&
                                   uiState !is UiState.Loading

    // 合规组合显示
    val complianceCombination = remember(selectedProductType, selectedStandard, paramValidationResult) {
        val productType = selectedProductType
        val standard = selectedStandard
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
                style = MaterialTheme.typography.titleLarge,
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
                
                ProductType.values().forEach { productType ->
                    ProductTypeCard(
                        productType = productType,
                        isSelected = selectedProductType == productType,
                        standardConfigs = ProductTypeConfigManager.getConfigByProductType(productType)?.standards ?: emptyList(),
                        onSelected = { 
                            selectedProductType = productType
                            selectedStandard = null  // 重置标准选择
                            paramValidationResult = null  // 重置验证结果
                            validationError = null
                        }
                    )
                }

                // ========== 2. 标准选择（仅在选中产品类型后显示） ==========
                if (productConfig != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "2. 标准（${productConfig.productTypeName}）",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    productConfig.standards.forEach { standard ->
                        StandardCard(
                            standard = standard,
                            isSelected = selectedStandard == standard,
                            onSelected = { 
                                selectedStandard = standard
                                paramValidationResult = null  // 重置验证结果
                                validationError = null
                            }
                        )
                    }
                }

                // ========== 3. 参数输入（仅在选中标准后显示） ==========
                selectedStandard?.let { standard ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "3. 参数输入",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    when (standard.inputItem.inputType) {
                        InputType.HEIGHT_RANGE -> {
                            // 身高范围输入
                            ParameterInputRow(
                                label = standard.inputItem.inputLabel,
                                unit = standard.inputItem.unit,
                                placeholder = standard.inputItem.placeholder,
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
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                ParameterInputRow(
                                    label = standard.inputItem.inputLabel,
                                    unit = weightUnit,
                                    placeholder = standard.inputItem.placeholder,
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
                        }
                        InputType.AGE_RANGE -> {
                            // 年龄范围输入
                            ParameterInputRow(
                                label = standard.inputItem.inputLabel,
                                unit = standard.inputItem.unit,
                                placeholder = standard.inputItem.placeholder,
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
                }

                // ========== 合规组合显示 ==========
                complianceCombination?.let { combination ->
                    Spacer(modifier = Modifier.height(8.dp))
                    ComplianceCombinationCard(combination = combination)
                }

                // ========== 4. 设计主题（可选） ==========
                if (selectedStandard != null) {
                    OutlinedTextField(
                        value = theme,
                        onValueChange = { theme = it },
                        label = { Text("设计主题（可选）") },
                        placeholder = { Text("输入特定设计需求，如材质风格、功能偏好等") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // ========== 生成按钮 ==========
                Button(
                    onClick = {
                        val heightCm = if (selectedStandard?.inputItem?.inputType == InputType.HEIGHT_RANGE) {
                            (minHeight.toIntOrNull() ?: 0) to (maxHeight.toIntOrNull() ?: 0)
                        } else {
                            null
                        }
                        
                        when (selectedStandard?.inputItem?.inputType) {
                            InputType.HEIGHT_RANGE -> {
                                val minH = minHeight.toIntOrNull() ?: 0
                                val maxH = maxHeight.toIntOrNull() ?: 0
                                val dummyType = paramValidationResult?.matchedDummy
                                val ageGroup = when (dummyType) {
                                    CrashTestDummy.Q0, CrashTestDummy.Q0_PLUS, CrashTestDummy.Q1 -> AgeGroup.INFANT
                                    CrashTestDummy.Q1_5 -> AgeGroup.TODDLER
                                    CrashTestDummy.Q3 -> AgeGroup.PRESCHOOL
                                    CrashTestDummy.Q3_S -> AgeGroup.SCHOOL_AGE
                                    CrashTestDummy.Q6 -> AgeGroup.SCHOOL_AGE
                                    else -> AgeGroup.TEEN
                                }
                                viewModel.generateCreativeIdea(ageGroup, selectedProductType!!, theme)
                            }
                            InputType.WEIGHT_RANGE -> {
                                // 根据体重推断年龄段
                                val weight = minWeight.toDoubleOrNull() ?: 0.0
                                val ageGroup = if (weight < 9.0) AgeGroup.INFANT
                                               else if (weight < 18.0) AgeGroup.TODDLER
                                               else if (weight < 21.0) AgeGroup.PRESCHOOL
                                               else if (weight < 33.0) AgeGroup.SCHOOL_AGE
                                               else AgeGroup.TEEN
                                viewModel.generateCreativeIdea(ageGroup, selectedProductType!!, theme)
                            }
                            InputType.AGE_RANGE -> {
                                val age = minAge.toIntOrNull() ?: 0
                                val ageGroup = when (age) {
                                    0, 1, 2 -> AgeGroup.INFANT
                                    3, 4, 5 -> AgeGroup.TODDLER
                                    6, 7, 8 -> AgeGroup.PRESCHOOL
                                    9, 10, 11 -> AgeGroup.SCHOOL_AGE
                                    else -> AgeGroup.TEEN
                                }
                                viewModel.generateCreativeIdea(ageGroup, selectedProductType!!, theme)
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
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 显示生成的创意
        creativeIdea?.let { idea ->
            CreativeIdeaCard(idea = idea)
        }
    }
}

/**
 * 产品类型卡片
 */
@Composable
fun ProductTypeCard(
    productType: ProductType,
    isSelected: Boolean,
    standardConfigs: List<StandardConfig>,
    onSelected: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        onClick = onSelected
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = onSelected
                    )
                    Text(
                        text = productType.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // 标准列表（默认折叠）
            if (isSelected) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "可用标准:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    standardConfigs.forEach { standard ->
                        Text(
                            text = "• ${standard.standardName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 标准卡片
 */
@Composable
fun StandardCard(
    standard: StandardConfig,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        onClick = onSelected
    ) {
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
                RadioButton(
                    selected = isSelected,
                    onClick = onSelected
                )
                Text(
                    text = standard.standardName,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
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
    Text(
        text = label,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Medium
    )
    
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

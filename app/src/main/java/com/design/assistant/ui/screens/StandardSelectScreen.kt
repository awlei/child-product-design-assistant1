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
import com.design.assistant.model.StandardInputParams
import com.design.assistant.model.getDisplayName
import com.design.assistant.model.getStandardInputDescription
import com.design.assistant.model.getStandardInputUnit
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
    onGenerateClick: (ProductType, List<StandardType>, StandardInputParams) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // 根据选择的标准动态显示输入字段
    // ECE R129: 身高范围（最小身高、最大身高，单位cm）
    var minHeightCm by remember { mutableStateOf("") }
    var maxHeightCm by remember { mutableStateOf("") }
    
    // FMVSS 213: 体重范围（最小体重、最大体重，单位磅）
    var minWeightLb by remember { mutableStateOf("") }
    var maxWeightLb by remember { mutableStateOf("") }
    
    // GPS028: 身高和体重（cm和kg）
    var heightCm by remember { mutableStateOf("") }
    var weightKg by remember { mutableStateOf("") }
    
    // CMVSS 213: 体重范围（最小体重、最大体重，单位kg）
    var minWeightKg by remember { mutableStateOf("") }
    var maxWeightKg by remember { mutableStateOf("") }

    // 获取当前选择的主要标准（用于决定输入类型）
    val primaryStandard = uiState.selectedStandards.firstOrNull()
    
    // 判断输入类型
    val inputType = when (primaryStandard) {
        StandardType.ECE_R129 -> "ECE_R129"
        StandardType.FMVSS213 -> "FMVSS213"
        StandardType.GPS028 -> "GPS028"
        StandardType.CMVSS213 -> "CMVSS213"
        else -> "GENERIC"
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
                                val inputParams = when (inputType) {
                                    "ECE_R129" -> StandardInputParams.EceR129Params(
                                        minHeightCm = minHeightCm.toIntOrNull() ?: 0,
                                        maxHeightCm = maxHeightCm.toIntOrNull() ?: 0
                                    )
                                    "FMVSS213" -> StandardInputParams.Fmvss213Params(
                                        minWeightLb = minWeightLb.toIntOrNull() ?: 0,
                                        maxWeightLb = maxWeightLb.toIntOrNull() ?: 0
                                    )
                                    "GPS028" -> StandardInputParams.Gps028Params(
                                        heightCm = heightCm.toIntOrNull() ?: 0,
                                        weightKg = weightKg.toIntOrNull() ?: 0
                                    )
                                    "CMVSS213" -> StandardInputParams.Cmvss213Params(
                                        minWeightKg = minWeightKg.toIntOrNull() ?: 0,
                                        maxWeightKg = maxWeightKg.toIntOrNull() ?: 0
                                    )
                                    else -> StandardInputParams.GenericParams(
                                        heightCm = heightCm.toIntOrNull() ?: 0,
                                        weightKg = weightKg.toIntOrNull() ?: 0
                                    )
                                }
                                onGenerateClick(
                                    productType,
                                    uiState.selectedStandards,
                                    inputParams
                                )
                            }
                        },
                        enabled = viewModel.canProceed() && validateInputs(
                            inputType = inputType,
                            minHeightCm = minHeightCm,
                            maxHeightCm = maxHeightCm,
                            minWeightLb = minWeightLb,
                            maxWeightLb = maxWeightLb,
                            heightCm = heightCm,
                            weightKg = weightKg,
                            minWeightKg = minWeightKg,
                            maxWeightKg = maxWeightKg
                        ),
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

                // 根据选择的标准动态显示输入字段
                if (uiState.selectedStandards.isNotEmpty()) {
                    SectionTitle(title = "3. 输入儿童参数")

                    DynamicInputSection(
                        inputType = inputType,
                        minHeightCm = minHeightCm,
                        maxHeightCm = maxHeightCm,
                        minWeightLb = minWeightLb,
                        maxWeightLb = maxWeightLb,
                        heightCm = heightCm,
                        weightKg = weightKg,
                        minWeightKg = minWeightKg,
                        maxWeightKg = maxWeightKg,
                        onMinHeightCmChange = { minHeightCm = it },
                        onMaxHeightCmChange = { maxHeightCm = it },
                        onMinWeightLbChange = { minWeightLb = it },
                        onMaxWeightLbChange = { maxWeightLb = it },
                        onHeightCmChange = { heightCm = it },
                        onWeightKgChange = { weightKg = it },
                        onMinWeightKgChange = { minWeightKg = it },
                        onMaxWeightKgChange = { maxWeightKg = it },
                        selectedStandards = uiState.selectedStandards
                    )
                }
            }
        }
    }
}

/**
 * 验证输入是否有效
 */
private fun validateInputs(
    inputType: String,
    minHeightCm: String = "",
    maxHeightCm: String = "",
    minWeightLb: String = "",
    maxWeightLb: String = "",
    heightCm: String = "",
    weightKg: String = "",
    minWeightKg: String = "",
    maxWeightKg: String = ""
): Boolean {
    return when (inputType) {
        "ECE_R129" -> minHeightCm.isNotBlank() && maxHeightCm.isNotBlank() &&
                      minHeightCm.toIntOrNull() ?: 0 > 0 &&
                      maxHeightCm.toIntOrNull() ?: 0 > 0 &&
                      (minHeightCm.toIntOrNull() ?: 0) <= (maxHeightCm.toIntOrNull() ?: Int.MAX_VALUE)
        "FMVSS213" -> minWeightLb.isNotBlank() && maxWeightLb.isNotBlank() &&
                      minWeightLb.toIntOrNull() ?: 0 > 0 &&
                      maxWeightLb.toIntOrNull() ?: 0 > 0 &&
                      (minWeightLb.toIntOrNull() ?: 0) <= (maxWeightLb.toIntOrNull() ?: Int.MAX_VALUE)
        "GPS028" -> heightCm.isNotBlank() && weightKg.isNotBlank() &&
                    heightCm.toIntOrNull() ?: 0 > 0 &&
                    weightKg.toIntOrNull() ?: 0 > 0
        "CMVSS213" -> minWeightKg.isNotBlank() && maxWeightKg.isNotBlank() &&
                      minWeightKg.toIntOrNull() ?: 0 > 0 &&
                      maxWeightKg.toIntOrNull() ?: 0 > 0 &&
                      (minWeightKg.toIntOrNull() ?: 0) <= (maxWeightKg.toIntOrNull() ?: Int.MAX_VALUE)
        else -> true
    }
}

/**
 * 动态输入区域
 * 根据标准类型显示不同的输入字段
 */
@Composable
private fun DynamicInputSection(
    inputType: String,
    minHeightCm: String,
    maxHeightCm: String,
    minWeightLb: String,
    maxWeightLb: String,
    heightCm: String,
    weightKg: String,
    minWeightKg: String,
    maxWeightKg: String,
    onMinHeightCmChange: (String) -> Unit,
    onMaxHeightCmChange: (String) -> Unit,
    onMinWeightLbChange: (String) -> Unit,
    onMaxWeightLbChange: (String) -> Unit,
    onHeightCmChange: (String) -> Unit,
    onWeightKgChange: (String) -> Unit,
    onMinWeightKgChange: (String) -> Unit,
    onMaxWeightKgChange: (String) -> Unit,
    selectedStandards: List<StandardType>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 标准信息提示
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📋 已选择标准：${selectedStandards.joinToString(", ") { it.getDisplayName() }}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "输入要求：${getInputDescription(inputType)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // 根据输入类型显示不同的字段
            when (inputType) {
                "ECE_R129" -> {
                    // ECE R129: 输入身高范围（最小身高、最大身高，单位cm）
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = minHeightCm,
                            onValueChange = { 
                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                    onMinHeightCmChange(it)
                                }
                            },
                            label = { Text("最小身高 (cm)") },
                            placeholder = { Text("87") },
                            singleLine = true,
                            isError = minHeightCm.isNotEmpty() && (minHeightCm.toIntOrNull() ?: 0) <= 0,
                            supportingText = {
                                if (minHeightCm.isNotEmpty()) {
                                    Text("建议范围：40-150cm")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = maxHeightCm,
                            onValueChange = { 
                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                    onMaxHeightCmChange(it)
                                }
                            },
                            label = { Text("最大身高 (cm)") },
                            placeholder = { Text("105") },
                            singleLine = true,
                            isError = maxHeightCm.isNotEmpty() && (maxHeightCm.toIntOrNull() ?: 0) <= 0,
                            supportingText = {
                                if (maxHeightCm.isNotEmpty()) {
                                    Text("建议范围：40-150cm")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // 输入验证提示
                    if (minHeightCm.isNotEmpty() && maxHeightCm.isNotEmpty()) {
                        val min = minHeightCm.toIntOrNull() ?: 0
                        val max = maxHeightCm.toIntOrNull() ?: 0
                        if (min > max) {
                            Text(
                                text = "⚠️ 最小身高不能大于最大身高",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                
                "FMVSS213" -> {
                    // FMVSS 213: 输入体重范围（最小体重、最大体重，单位磅）
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = minWeightLb,
                            onValueChange = { 
                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                    onMinWeightLbChange(it)
                                }
                            },
                            label = { Text("最小体重 (磅)") },
                            placeholder = { Text("20") },
                            singleLine = true,
                            isError = minWeightLb.isNotEmpty() && (minWeightLb.toIntOrNull() ?: 0) <= 0,
                            supportingText = {
                                if (minWeightLb.isNotEmpty()) {
                                    Text("建议范围：5-100磅")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = maxWeightLb,
                            onValueChange = { 
                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                    onMaxWeightLbChange(it)
                                }
                            },
                            label = { Text("最大体重 (磅)") },
                            placeholder = { Text("65") },
                            singleLine = true,
                            isError = maxWeightLb.isNotEmpty() && (maxWeightLb.toIntOrNull() ?: 0) <= 0,
                            supportingText = {
                                if (maxWeightLb.isNotEmpty()) {
                                    Text("建议范围：5-100磅")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // 输入验证提示
                    if (minWeightLb.isNotEmpty() && maxWeightLb.isNotEmpty()) {
                        val min = minWeightLb.toIntOrNull() ?: 0
                        val max = maxWeightLb.toIntOrNull() ?: 0
                        if (min > max) {
                            Text(
                                text = "⚠️ 最小体重不能大于最大体重",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    
                    // 单位转换提示
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "💡 单位换算：1磅(lb) ≈ 0.45千克(kg) | 1千克(kg) ≈ 2.2磅(lb)",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                
                "GPS028" -> {
                    // GPS028 (GB 27887): 输入身高和体重
                    OutlinedTextField(
                        value = heightCm,
                        onValueChange = { 
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                onHeightCmChange(it)
                            }
                        },
                        label = { Text("儿童身高 (cm)") },
                        placeholder = { Text("95") },
                        singleLine = true,
                        isError = heightCm.isNotEmpty() && (heightCm.toIntOrNull() ?: 0) <= 0,
                        supportingText = {
                            if (heightCm.isNotEmpty()) {
                                Text("建议范围：40-150cm")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = weightKg,
                        onValueChange = { 
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                onWeightKgChange(it)
                            }
                        },
                        label = { Text("儿童体重 (kg)") },
                        placeholder = { Text("15") },
                        singleLine = true,
                        isError = weightKg.isNotEmpty() && (weightKg.toIntOrNull() ?: 0) <= 0,
                        supportingText = {
                            if (weightKg.isNotEmpty()) {
                                Text("建议范围：2-36kg")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                "CMVSS213" -> {
                    // CMVSS 213 (加拿大): 输入体重范围（最小体重、最大体重，单位kg）
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = minWeightKg,
                            onValueChange = { 
                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                    onMinWeightKgChange(it)
                                }
                            },
                            label = { Text("最小体重 (kg)") },
                            placeholder = { Text("9") },
                            singleLine = true,
                            isError = minWeightKg.isNotEmpty() && (minWeightKg.toIntOrNull() ?: 0) <= 0,
                            supportingText = {
                                if (minWeightKg.isNotEmpty()) {
                                    Text("建议范围：2-30kg")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = maxWeightKg,
                            onValueChange = { 
                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                    onMaxWeightKgChange(it)
                                }
                            },
                            label = { Text("最大体重 (kg)") },
                            placeholder = { Text("30") },
                            singleLine = true,
                            isError = maxWeightKg.isNotEmpty() && (maxWeightKg.toIntOrNull() ?: 0) <= 0,
                            supportingText = {
                                if (maxWeightKg.isNotEmpty()) {
                                    Text("建议范围：2-30kg")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // 输入验证提示
                    if (minWeightKg.isNotEmpty() && maxWeightKg.isNotEmpty()) {
                        val min = minWeightKg.toIntOrNull() ?: 0
                        val max = maxWeightKg.toIntOrNull() ?: 0
                        if (min > max) {
                            Text(
                                text = "⚠️ 最小体重不能大于最大体重",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                
                else -> {
                    // 通用输入：身高和体重
                    OutlinedTextField(
                        value = heightCm,
                        onValueChange = { 
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                onHeightCmChange(it)
                            }
                        },
                        label = { Text("儿童身高 (cm，可选)") },
                        placeholder = { Text("95") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = weightKg,
                        onValueChange = { 
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                onWeightKgChange(it)
                            }
                        },
                        label = { Text("儿童体重 (kg，可选)") },
                        placeholder = { Text("15") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * 获取输入描述
 */
private fun getInputDescription(inputType: String): String {
    return when (inputType) {
        "ECE_R129" -> "ECE R129标准需要输入儿童身高范围（单位：厘米cm）"
        "FMVSS213" -> "FMVSS 213标准需要输入儿童体重范围（单位：磅lb）"
        "GPS028" -> "GB 27887-2011标准需要输入儿童身高和体重"
        "CMVSS213" -> "CMVSS 213标准需要输入儿童体重范围（单位：千克kg）"
        else -> "请输入儿童身高和体重（可选）"
    }
}

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

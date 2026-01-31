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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.childproduct.designassistant.model.*
import com.childproduct.designassistant.ui.MainViewModel
import com.childproduct.designassistant.ui.UiState
import com.childproduct.designassistant.ui.components.common.InfoRow
import com.childproduct.designassistant.ui.components.common.InfoRowVariant

/**
 * 改进的创意生成界面（方案生成）
 * 标题：标准适配设计
 */
@Composable
fun CreativeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val creativeIdea by viewModel.creativeIdea.collectAsState()

    var minHeight by remember { mutableStateOf("") }
    var maxHeight by remember { mutableStateOf("") }
    var selectedProductType by remember { mutableStateOf(ProductType.CHILD_SAFETY_SEAT) }
    var theme by remember { mutableStateOf("") }
    
    // 生成按钮点击动画状态
    var buttonPressed by remember { mutableStateOf(false) }
    
    // 计算按钮是否可用
    val isButtonEnabled = minHeight.isNotBlank() && 
                           maxHeight.isNotBlank() &&
                           uiState !is UiState.Loading

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 页面标题（标尺+标准文档图标）
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Straighten,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Icon(
                imageVector = Icons.Default.LibraryBooks,
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
                // 身高范围输入（双栏）
                Text(
                    text = "目标身高范围（cm）",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = minHeight,
                        onValueChange = {
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                minHeight = it
                            }
                        },
                        label = { Text("最小身高") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("如: 40") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        value = maxHeight,
                        onValueChange = {
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                maxHeight = it
                            }
                        },
                        label = { Text("最大身高") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        placeholder = { Text("如: 105") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                // 年龄区间提示
                if (minHeight.isNotBlank() && maxHeight.isNotBlank()) {
                    val ageHint = getAgeRangeHint(minHeight.toIntOrNull() ?: 0, maxHeight.toIntOrNull() ?: 0)
                    if (ageHint != null) {
                        Text(
                            text = "对应年龄区间: $ageHint",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // 产品类型选择（带标准信息）
                Text(
                    text = "产品类型（含核心强制标准）",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                ProductTypeWithStandardSelector(
                    selectedProductType = selectedProductType,
                    onProductTypeSelected = { type -> selectedProductType = type }
                )

                // 标准适配提示
                if (minHeight.isNotBlank() && maxHeight.isNotBlank()) {
                    StandardComplianceHintCard(
                        productType = selectedProductType,
                        ageRange = getAgeRangeHint(minHeight.toIntOrNull() ?: 0, maxHeight.toIntOrNull() ?: 0) ?: ""
                    )
                }

                // 设计主题（增加示例提示）
                OutlinedTextField(
                    value = theme,
                    onValueChange = { theme = it },
                    label = { Text("设计主题（可选）") },
                    placeholder = { Text("示例：符合UN R129的6-9岁儿童安全座椅") },
                    supportingText = { 
                        Text(
                            text = "输入特定设计需求，如材质风格、功能偏好等",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 生成按钮（增加点击反馈）
                Button(
                    onClick = {
                        buttonPressed = true
                        val ageGroup = inferAgeGroup(minHeight.toIntOrNull() ?: 0, maxHeight.toIntOrNull() ?: 0)
                        viewModel.generateCreativeIdea(ageGroup, selectedProductType, theme)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(if (buttonPressed) 0.97f else 1f),
                    enabled = isButtonEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isButtonEnabled) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
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
 * 根据身高范围推断年龄区间提示
 */
private fun getAgeRangeHint(minHeight: Int, maxHeight: Int): String? {
    if (minHeight <= 0 || maxHeight <= 0) return null

    return when {
        maxHeight <= 75 -> "0-18个月"
        minHeight >= 75 && maxHeight <= 100 -> "15个月-4岁"
        minHeight >= 100 && maxHeight <= 150 -> "4岁-12岁"
        else -> null
    }
}

/**
 * 根据身高范围推断年龄段
 */
private fun inferAgeGroup(minHeight: Int, maxHeight: Int): AgeGroup {
    return when {
        maxHeight <= 75 -> AgeGroup.INFANT
        minHeight >= 75 && maxHeight <= 100 -> AgeGroup.PRESCHOOL
        minHeight >= 100 && maxHeight <= 150 -> AgeGroup.SCHOOL_AGE
        else -> AgeGroup.PRESCHOOL
    }
}

/**
 * 产品类型选择器（带标准信息）
 */
@Composable
fun ProductTypeWithStandardSelector(
    selectedProductType: ProductType,
    onProductTypeSelected: (ProductType) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProductType.values().forEach { productType ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (selectedProductType == productType) 4.dp else 1.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedProductType == productType) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                onClick = { onProductTypeSelected(productType) }
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
                        // 标准缩写标签
                        SuggestionChip(
                            onClick = {},
                            label = { Text(productType.standardAbbr) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = productType.displayName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = productType.mainStandards,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }

                    RadioButton(
                        selected = selectedProductType == productType,
                        onClick = { onProductTypeSelected(productType) }
                    )
                }
            }
        }
    }
}

/**
 * 标准适配提示卡片
 */
@Composable
fun StandardComplianceHintCard(
    productType: ProductType,
    ageRange: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = "当前合规组合",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$productType + $ageRange → 适用标准：${productType.mainStandards}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}

/**
 * 创意展示卡片 - 儿童安全座椅专用
 * 结构：基本信息 + 合规参数 + 功能特性 + 推荐材料 + 标准关联
 * 核心信息默认展示，其他部分可折叠展开
 */
@Composable
fun CreativeIdeaCard(
    idea: CreativeIdea,
    modifier: Modifier = Modifier
) {
    // 核心信息（基本信息）默认展开，其他部分默认折叠
    var expandedBasicInfo by remember { mutableStateOf(true) }
    var expandedCompliance by remember { mutableStateOf(false) }
    var expandedFeatures by remember { mutableStateOf(false) }
    var expandedMaterials by remember { mutableStateOf(false) }
    var expandedStandards by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 标题
            Text(
                text = idea.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = idea.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // ============ 基本信息 ============
            ExpandableSection(
                title = "基本信息",
                icon = Icons.Default.Info,
                expanded = expandedBasicInfo,
                onExpandedChange = { expandedBasicInfo = it }
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoRow("设计主题", idea.theme, variant = InfoRowVariant.COMPACT)
                    InfoRow("年龄段", idea.ageGroup.displayName, variant = InfoRowVariant.COMPACT)
                    InfoRow("产品类型", idea.productType.displayName, variant = InfoRowVariant.COMPACT)
                    InfoRow("标准编码", idea.productType.standardAbbr, variant = InfoRowVariant.COMPACT)
                }
            }

            // ============ 合规参数（新增） ============
            ExpandableSection(
                title = "合规参数",
                icon = Icons.Default.Verified,
                expanded = expandedCompliance,
                onExpandedChange = { expandedCompliance = it },
                badgeText = "专业"
            ) {
                ComplianceParametersSection(
                    ageGroup = idea.ageGroup,
                    productType = idea.productType
                )
            }

            // ============ 功能特性 ============
            ExpandableSection(
                title = "功能特性",
                icon = Icons.Default.Build,
                expanded = expandedFeatures,
                onExpandedChange = { expandedFeatures = it }
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    idea.features.forEach { feature ->
                        FeatureItem(feature = feature)
                    }
                }
            }

            // ============ 推荐材料（优化） ============
            ExpandableSection(
                title = "推荐材料",
                icon = Icons.Default.Category,
                expanded = expandedMaterials,
                onExpandedChange = { expandedMaterials = it }
            ) {
                MaterialsSection(
                    productType = idea.productType,
                    customMaterials = idea.materials
                )
            }

            // ============ 标准关联（新增） ============
            ExpandableSection(
                title = "标准关联",
                icon = Icons.Default.Gavel,
                expanded = expandedStandards,
                onExpandedChange = { expandedStandards = it },
                badgeText = "重点"
            ) {
                StandardsReferenceSection(
                    productType = idea.productType,
                    ageGroup = idea.ageGroup
                )
            }
        }
    }
}

/**
 * 可展开区域组件
 */
@Composable
fun ExpandableSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    badgeText: String? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (expanded) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (expanded) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (expanded) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                    badgeText?.let {
                        SuggestionChip(
                            onClick = {},
                            label = { Text(it, style = MaterialTheme.typography.labelSmall) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.tertiary
                            )
                        )
                    }
                }
                IconButton(
                    onClick = { onExpandedChange(!expanded) },
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
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                content()
            }
        }
    }
}

/**
 * 合规参数部分
 */
@Composable
fun ComplianceParametersSection(
    ageGroup: AgeGroup,
    productType: ProductType
) {
    // 定义损伤判据数据类
    data class InjuryCriteria(
        val hic: String,
        val chest: String,
        val neckTension: String,
        val neckCompression: String
    )

    val criteria = when (ageGroup) {
        AgeGroup.INFANT -> InjuryCriteria("<=720", "<=55g", "<=2.5kN", "<=1.8kN")
        AgeGroup.TODDLER -> InjuryCriteria("<=720", "<=55g", "<=2.5kN", "<=1.8kN")
        AgeGroup.PRESCHOOL -> InjuryCriteria("<=1000", "<=55g", "<=3.0kN", "<=2.0kN")
        AgeGroup.SCHOOL_AGE -> InjuryCriteria("<=1000", "<=55g", "<=4.0kN", "<=2.5kN")
        AgeGroup.TEEN -> InjuryCriteria("<=1000", "<=55g", "<=4.0kN", "<=2.5kN")
        AgeGroup.ALL -> InjuryCriteria("<=1000", "<=55g", "<=4.0kN", "<=2.5kN")
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "损伤判据（${ageGroup.displayName}）",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        ComplianceParamRow("HIC（头部损伤判据）", criteria.hic)
        ComplianceParamRow("胸部加速度", criteria.chest)
        ComplianceParamRow("颈部张力极限", criteria.neckTension)
        ComplianceParamRow("颈部压缩极限", criteria.neckCompression)

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        Text(
            text = "物理参数",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        when (ageGroup) {
            AgeGroup.INFANT -> {
                ComplianceParamRow("适用假人", "Q0, Q1, Q1.5")
                ComplianceParamRow("肩宽范围", "248-264mm")
                ComplianceParamRow("髋宽范围", "120-130mm")
            }
            AgeGroup.TODDLER -> {
                ComplianceParamRow("适用假人", "Q1.5")
                ComplianceParamRow("肩宽范围", "260-280mm")
                ComplianceParamRow("髋宽范围", "125-140mm")
            }
            AgeGroup.PRESCHOOL -> {
                ComplianceParamRow("适用假人", "Q3, Q6")
                ComplianceParamRow("肩宽范围", "276-310mm")
                ComplianceParamRow("髋宽范围", "140-155mm")
            }
            AgeGroup.SCHOOL_AGE -> {
                ComplianceParamRow("适用假人", "Q10")
                ComplianceParamRow("肩宽范围", "340-380mm")
                ComplianceParamRow("髋宽范围", "170-190mm")
            }
            AgeGroup.TEEN -> {
                ComplianceParamRow("适用假人", "Q10")
                ComplianceParamRow("肩宽范围", "380-420mm")
                ComplianceParamRow("髋宽范围", "200-220mm")
            }
            AgeGroup.ALL -> {
                ComplianceParamRow("适用假人", "全年龄段")
                ComplianceParamRow("肩宽范围", "248-420mm")
                ComplianceParamRow("髋宽范围", "120-220mm")
            }
        }
    }
}

/**
 * 合规参数行
 */
@Composable
fun ComplianceParamRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(80.dp)
        )
    }
}

/**
 * 功能特性项
 */
@Composable
fun FeatureItem(feature: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(16.dp)
                .padding(top = 2.dp)
        )
        Text(
            text = feature,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 推荐材料部分（优化）
 */
@Composable
fun MaterialsSection(
    productType: ProductType,
    customMaterials: List<String>
) {
    val standardMaterials = when (productType) {
        ProductType.CHILD_SAFETY_SEAT -> listOf(
            Triple("阻燃面料", "符合FMVSS 302燃烧性能标准", "美标专用"),
            Triple("ISOFIX金属组件", "高强度镀锌钢材", "GB 15083要求"),
            Triple("PP塑料", "高强度聚丙烯", "GB 6675.4-2014"),
            Triple("EPS吸能材料", "发泡聚苯乙烯", "GB/T 10801.1-2021"),
            Triple("尼龙织带", "高强度安全带织带", "GB 6095-2021，断裂强度≥11000N")
        )
        else -> customMaterials.map { Triple(it, "", "") }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        standardMaterials.forEach { (name, standard, note) ->
            MaterialItem(name = name, standard = standard, note = note)
        }

        // 显示自定义材料（如果有）
        if (customMaterials.isNotEmpty() && productType == ProductType.CHILD_SAFETY_SEAT) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = "自定义材料",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            customMaterials.forEach { material ->
                MaterialItem(name = material, standard = "", note = "")
            }
        }
    }
}

/**
 * 材料项组件
 */
@Composable
fun MaterialItem(name: String, standard: String, note: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FiberManualRecord,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(8.dp)
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                if (note.isNotEmpty()) {
                    SuggestionChip(
                        onClick = {},
                        label = { Text(note, style = MaterialTheme.typography.labelSmall) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                }
            }
            if (standard.isNotEmpty()) {
                Text(
                    text = "标准：$standard",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

/**
 * 标准关联部分（新增）
 */
@Composable
fun StandardsReferenceSection(
    productType: ProductType,
    ageGroup: AgeGroup
) {
    val standards = when (productType) {
        ProductType.CHILD_SAFETY_SEAT -> listOf(
            "UN R129 §5.3.3 - 侧撞防护要求",
            "UN R129 §5.5 - 头部损伤判据（HIC≤1000）",
            "UN R129 §5.6 - 胸部加速度（≤55g）",
            "FMVSS 302 - 燃烧性能（阻燃面料）",
            "FMVSS 213 - 儿童约束系统标准",
            "GB 27887-2011 - 儿童安全座椅国家标准",
            "GB 6095-2021 - 安全带织带标准（断裂强度≥11000N）",
            "GB 6675.4-2014 - 玩具安全标准"
        )
        else -> listOf(productType.mainStandards)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "适用标准体系",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        standards.forEach { standard ->
            StandardItem(standard = standard)
        }
    }
}

/**
 * 标准项组件
 */
@Composable
fun StandardItem(standard: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Gavel,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = standard,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 年龄段选择器（保留用于其他场景）
 */
@Composable
fun AgeGroupSelector(
    selectedAgeGroup: AgeGroup,
    onAgeGroupSelected: (AgeGroup) -> Unit
) {
    Column {
        AgeGroup.values().forEach { ageGroup ->
            FilterChip(
                selected = selectedAgeGroup == ageGroup,
                onClick = { onAgeGroupSelected(ageGroup) },
                label = { Text(ageGroup.displayName) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

/**
 * 产品类型选择器（简化版）
 */
@Composable
fun ProductTypeSelector(
    selectedProductType: ProductType,
    onProductTypeSelected: (ProductType) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProductType.values().forEach { productType ->
            FilterChip(
                selected = selectedProductType == productType,
                onClick = { onProductTypeSelected(productType) },
                label = { Text(productType.displayName) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

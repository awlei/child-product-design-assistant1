# 输出结果标准混淆优化方案

## 问题概述

当前输出的核心问题是：**选中"美标FMVSS 213"后，输出混入了ECE R129（欧标/国标）的内容**，导致美标专属要求与其他标准混淆。

## 优化目标

实现标准-输出强绑定，确保：
1. 选A标准，只出A标准的内容
2. 所有数据、约束、测试均与所选标准绑定
3. 多标准选择时，按标准标签拆分独立模块

## 实施方案

### 1. 数据层改造（已完成✅）

#### 1.1 添加StandardType枚举
```kotlin
enum class StandardType(
    val displayName: String,
    val colorName: String,
    val shortName: String
) {
    ECE_R129("ECE R129", "green", "欧标"),
    FMVSS_213("FMVSS 213", "blue", "美标"),
    GB_27887("GB 27887-2024", "orange", "国标")
}
```

#### 1.2 更新ComplianceDummy枚举
```kotlin
enum class ComplianceDummy(
    val code: String,
    val description: String,
    val standardType: StandardType,  // 标准归属
    val standardClauses: List<String> // 关联的标准条款
) {
    Q3S("Q3s", "3岁假人专用（24-48个月，侧撞测试）", 
        StandardType.FMVSS_213, 
        listOf("FMVSS 213 §S5", "FMVSS 213 §S5.3", "FMVSS 213 §S5.4")),
    // ...
}
```

#### 1.3 更新GPS028DummyData
```kotlin
data class GPS028DummyData(
    // ...
    val standardType: StandardType,  // 标准归属（美标/欧标/国标）
    // ...
)
```

### 2. 输出层改造（进行中🚧）

#### 2.1 添加按标准过滤的辅助函数

```kotlin
/**
 * 根据标准类型过滤假人
 */
fun getDummiesByStandardType(
    dummies: List<GPS028DummyData>,
    standardType: StandardType
): List<GPS028DummyData> {
    return dummies.filter { it.standardType == standardType }
}

/**
 * 获取用户选择的标准类型
 */
fun getSelectedStandards(creativeIdea: CreativeIdea): Set<StandardType> {
    val standards = mutableSetOf<StandardType>()
    
    // 从standardsReference中提取标准类型
    creativeIdea.standardsReference?.let { ref ->
        when {
            ref.mainStandard.contains("FMVSS 213") -> 
                standards.add(StandardType.FMVSS_213)
            ref.mainStandard.contains("ECE R129") -> 
                standards.add(StandardType.ECE_R129)
            ref.mainStandard.contains("GB 27887") -> 
                standards.add(StandardType.GB_27887)
        }
    }
    
    // 默认使用欧标
    if (standards.isEmpty()) {
        standards.add(StandardType.ECE_R129)
    }
    
    return standards
}
```

#### 2.2 修改SafetySeatOutputContent函数

```kotlin
@Composable
private fun SafetySeatOutputContent(creativeIdea: CreativeIdea) {
    val selectedStandards = getSelectedStandards(creativeIdea)
    
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 按标准类型分组输出
        selectedStandards.forEach { standardType ->
            StandardOutputCard(
                standardType = standardType,
                creativeIdea = creativeIdea
            )
        }
    }
}
```

#### 2.3 创建StandardOutputCard组件

```kotlin
@Composable
private fun StandardOutputCard(
    standardType: StandardType,
    creativeIdea: CreativeIdea
) {
    val params = creativeIdea.complianceParameters
    val ageGroup = creativeIdea.ageGroup
    val heightRange = creativeIdea.ageGroup.heightRange
    
    // 解析身高范围
    val heightRangeParts = heightRange.replace("cm", "").split("-")
    val minHeightCm = heightRangeParts.getOrNull(0)?.toIntOrNull() ?: 40
    val maxHeightCm = heightRangeParts.getOrNull(1)?.toIntOrNull() ?: 150
    
    // 获取所有匹配的假人
    val allMatchedDummies = GPS028Database.getDummiesByHeightRange(minHeightCm, maxHeightCm)
    
    // 按标准类型过滤假人
    val standardDummies = getDummiesByStandardType(allMatchedDummies, standardType)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (standardType) {
                StandardType.ECE_R129 -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                StandardType.FMVSS_213 -> Color(0xFFE3F2FD).copy(alpha = 0.5f)  // 蓝色
                StandardType.GB_27887 -> Color(0xFFFFF3E0).copy(alpha = 0.5f)  // 橙色
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = when (standardType) {
                StandardType.ECE_R129 -> MaterialTheme.colorScheme.primary
                StandardType.FMVSS_213 -> Color(0xFF1976D2)  // 蓝色
                StandardType.GB_27887 -> Color(0xFFFF6F00)  // 橙色
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 标准标识标签
            StandardLabel(standardType)
            
            // 基础适配数据（仅显示该标准的假人）
            StandardBasicDataBlock(standardDummies, standardType)
            
            // 核心设计参数（仅显示该标准的数据）
            StandardDesignParametersBlock(standardDummies, standardType)
            
            // 合规约束（仅显示该标准的约束）
            StandardComplianceConstraintsBlock(standardDummies, standardType)
            
            // 测试项（仅显示该标准的测试）
            StandardTestItemsBlock(standardDummies, standardType)
        }
    }
}
```

#### 2.4 创建StandardLabel组件

```kotlin
@Composable
private fun StandardLabel(standardType: StandardType) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = when (standardType) {
            StandardType.ECE_R129 -> MaterialTheme.colorScheme.primary
            StandardType.FMVSS_213 -> Color(0xFF1976D2)
            StandardType.GB_27887 -> Color(0xFFFF6F00)
        }
    ) {
        Text(
            text = "【${standardType.shortName} ${standardType.displayName}】",
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
```

### 3. 优化效果示例

#### 3.1 仅选择美标FMVSS 213

```
├─ 📦 儿童安全座椅设计方案（美标FMVSS 213专属）
│  ├─ 【美标 FMVSS 213】（蓝色标签）
│  │
│  ├─ 📊 基础适配数据（美标假人）
│  │  ├─ 🔽 Q3S假人（3岁，美标专属）
│  │  │  ├─ 身高范围：87.0-105.0cm（FMVSS 213假人数据）
│  │  │  ├─ 体重范围：13.0-18.0kg（FMVSS 213假人数据）
│  │  │  └─ 安装方向：双向可选（美标无强制后向至4岁要求）
│  │
│  ├─ 📏 美标专属设计参数
│  │  ├─ 头枕高度：425-475mm（FMVSS 213 Q3S假人坐高）
│  │  ├─ 座宽：350-370mm（美标假人肩宽180-210mm）
│  │  └─ 侧防面积：0.85㎡（美标增强型防护要求）
│  │
│  ├─ ⚖️ 美标FMVSS 213约束
│  │  ├─ 正面HIC≤1000（FMVSS 213 §S5.3）
│  │  ├─ 侧撞胸部压缩≤23mm（FMVSS 213 §S5.4）
│  │  └─ 织带强度≥11000N（FMVSS 213 §S6.2）
│  │
│  └─ 🧪 美标测试项
│     ├─ 动态碰撞：正面50km/h、侧撞32km/h（FMVSS 213测试方法）
│     └─ 阻燃：燃烧速度≤4英寸/分钟（FMVSS 302）
```

#### 3.2 同时选择美标+欧标

```
├─ 📦 儿童安全座椅设计方案（欧标ECE R129专属）
│  ├─ 【欧标 ECE R129】（绿色标签）
│  ├─ 📊 基础适配数据（欧标假人）
│  │  ├─ 🔽 Q3假人（3岁，欧标专属）
│  │  ├─ 🔽 Q6假人（6岁，欧标专属）
│  ├─ ⚖️ 欧标ECE R129约束
│  │  ├─ 正面HIC≤1000（ECE R129 §7.1.3）
│  │  └─ 侧防结构要求（ECE R129 §5.1）
│
├─ 📦 儿童安全座椅设计方案（美标FMVSS 213专属）
│  ├─ 【美标 FMVSS 213】（蓝色标签）
│  ├─ 📊 基础适配数据（美标假人）
│  │  └─ 🔽 Q3S假人（3岁，美标专属）
│  ├─ ⚖️ 美标FMVSS 213约束
│  │  ├─ 正面HIC≤1000（FMVSS 213 §S5.3）
│  │  └─ 侧撞胸部压缩≤23mm（FMVSS 213 §S5.4）
```

## 实施步骤

1. ✅ 添加StandardType枚举
2. ✅ 更新ComplianceDummy枚举
3. ✅ 更新GPS028DummyData
4. ✅ 为所有假人添加standardType字段
5. 🚧 添加按标准过滤的辅助函数
6. 🚧 创建StandardOutputCard组件
7. 🚧 创建StandardLabel组件
8. 🚧 修改SafetySeatOutputContent函数
9. ⏳ 测试优化后的输出效果

## 预期效果

优化后输出将实现：
- **选A标准，只出A标准的内容**
- **所有数据、约束、测试均与所选标准绑定**
- **多标准选择时，按标准标签拆分独立模块**
- **彻底解决不同标准内容混杂的问题**

---

**文档版本:** v1.0  
**创建时间:** 2024-01-20  
**作者:** Coze Coding Agent

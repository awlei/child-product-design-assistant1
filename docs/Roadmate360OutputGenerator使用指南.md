# Roadmate360OutputGenerator 使用指南

## 概述

`Roadmate360OutputGenerator` 是一个专门用于生成符合 ECE R129 标准的儿童安全座椅设计方案的工具类。

**关键特性：**
- ✅ 严格遵循单一标准 ECE R129，不混用 GB 27887-2024/FMVSS 213
- ✅ 正确的假人映射：105-150cm → Q6(105-145cm) + Q10(145-150cm)
- ✅ 生成 ROADMATE 360 格式的测试矩阵（20列）
- ✅ 生成完整的安全阈值表格（ECE R129 §7.1）
- ✅ 输出纯文本，无代码字段泄露
- ✅ 兼容现有架构，可集成到 `ChildProductDesignScheme`

## 基本使用

### 示例 1：生成完整输出（纯文本）

```kotlin
val output = Roadmate360OutputGenerator.generateOutput(
    minHeightCm = 105,
    maxHeightCm = 150,
    productType = "儿童安全座椅",
    designTheme = "社交元素集成式安全座椅"
)

// 输出格式：
// 【设计方案】
// 产品类型：儿童安全座椅
// 身高范围：105-150cm
// 适配年龄段：4-12岁
// 设计主题：社交元素集成式安全座椅
// 安装方式：ISOFIX 3点连接 + Top-tether上拉带（ECE R129 §5.2强制要求）
// ...
// 【测试矩阵】（ROADMATE 360格式 · ECE R129 Annex 19）
// ...
// 【安全阈值】（ECE R129 §7.1 · 单一标准）
// ...
// 【合规声明】
// ...

// 显示在 UI 中
tvResult.text = output
```

### 示例 2：生成标准化方案（用于数据处理）

```kotlin
val scheme = Roadmate360OutputGenerator.generateStandardizedScheme(
    minHeightCm = 105,
    maxHeightCm = 150,
    productType = "儿童安全座椅",
    designTheme = "社交元素"
)

// scheme 是 ChildProductDesignScheme 对象
// 可以访问其字段进行进一步处理
println(scheme.productType)      // 儿童安全座椅
println(scheme.heightRange)     // 105-150cm
println(scheme.ageRange)        // 4-12岁
println(scheme.dummyType)       // Q6假人(105-145cm) + Q10假人(145-150cm)

// 访问核心特点
scheme.coreFeatures.forEach { feature ->
    println(feature)
}

// 访问安全阈值
scheme.safetyThresholds.forEach { (key, value) ->
    println("$key: $value")
}

// 验证方案
if (scheme.validationResult.isValid) {
    println("方案验证通过")
} else {
    println("方案验证失败: ${scheme.validationResult.errors}")
}
```

### 示例 3：格式化用于 UI 展示

```kotlin
// 先生成标准化方案
val scheme = Roadmate360OutputGenerator.generateStandardizedScheme(
    minHeightCm = 105,
    maxHeightCm = 150
)

// 格式化为纯文本用于显示
val displayText = Roadmate360OutputGenerator.formatForDisplay(scheme)
tvResult.text = displayText
```

### 示例 4：仅生成测试矩阵表格

```kotlin
// 生成测试矩阵项
val testItems = Roadmate360OutputGenerator.generateTestMatrix(105, 150)

// 生成表格字符串
val matrixTable = Roadmate360OutputGenerator.generateRoadmate360Table(testItems)

// 在 UI 中显示表格
tvResult.text = matrixTable
```

## 集成到现有架构

### 方式 1：在 CreativeService 中使用

```kotlin
class CreativeService {

    suspend fun generateCreativeIdeaByHeight(
        minHeightCm: Int,
        maxHeightCm: Int,
        productType: ProductType,
        customTheme: String = ""
    ): CreativeIdeaResult = withContext(Dispatchers.IO) {
        // ... 其他逻辑 ...

        // 如果是儿童安全座椅，使用 Roadmate360OutputGenerator 生成方案
        if (productType == ProductType.CHILD_SAFETY_SEAT) {
            val scheme = Roadmate360OutputGenerator.generateStandardizedScheme(
                minHeightCm = minHeightCm,
                maxHeightCm = maxHeightCm,
                productType = productType.displayName,
                designTheme = customTheme.ifEmpty { "标准设计" }
            )

            // 可以将 scheme 转换为 CreativeIdea 或直接返回
            return@withContext CreativeIdeaResult(
                success = true,
                idea = /* 转换为 CreativeIdea */,
                designScheme = scheme  // 新增字段
            )
        }

        // ... 其他逻辑 ...
    }
}
```

### 方式 2：在 CreativeScreen 中使用

```kotlin
@Composable
fun CreativeScreen(viewModel: MainViewModel = viewModel()) {
    // ... UI 代码 ...

    Button(
        onClick = {
            if (selectedProductType == ProductType.CHILD_SAFETY_SEAT) {
                val scheme = Roadmate360OutputGenerator.generateStandardizedScheme(
                    minHeightCm = minHeight.toIntOrNull() ?: 0,
                    maxHeightCm = maxHeight.toIntOrNull() ?: 0,
                    productType = selectedProductType!!.displayName,
                    designTheme = theme
                )

                val displayText = Roadmate360OutputGenerator.formatForDisplay(scheme)
                generatedText = displayText
                showResult = true
            } else {
                // 其他产品类型的处理
                viewModel.generateCreativeIdea(ageGroup, selectedProductType!!, theme)
            }
        }
    ) {
        Text("生成设计方案")
    }
}
```

## 输出格式说明

### 方案结构

生成的输出包含 4 个主要模块：

1. **【设计方案】**
   - 产品类型
   - 身高范围
   - 适配年龄段
   - 设计主题
   - 安装方式

2. **【测试矩阵】**（ROADMATE 360 格式）
   - 20 列表格
   - 包含 Q6 和 Q10 假人的所有测试项
   - 测试类型：Frontal, Rear, Lateral

3. **【安全阈值】**（ECE R129 §7.1）
   - HIC36 ≤1000
   - 胸部合成加速度(3ms) ≤60g
   - 颈部张力 ≤2000N
   - 颈部压缩 ≤2500N
   - 头部位移 ≤550mm
   - 膝部位移 ≤650mm
   - 胸部位移 ≤52mm

4. **【合规声明】**
   - 假人分类
   - 动态测试要求
   - ISOFIX连接要求
   - 身高范围说明

### 示例输出

```
【设计方案】
产品类型：儿童安全座椅
身高范围：105-150cm
适配年龄段：4-12岁
设计主题：社交元素集成式安全座椅
安装方式：ISOFIX 3点连接 + Top-tether上拉带（ECE R129 §5.2强制要求）

> ⚠️ 标准说明：本方案严格遵循单一标准ECE R129，不混用GB 27887-2024或FMVSS 213
> 身高105-150cm按ECE R129 Annex 19分段适配：
> - Q6假人(105-145cm) + Q10假人(145-150cm)

【测试矩阵】（ROADMATE 360格式 · ECE R129 Annex 19）

| # Sample | Pulse | Impact | Dummy | Position | Installation | Specific Installation | Product Configuration | Isofix anchors | Position of floor | Harness | Top Tether / Support leg | Dashboard | Comments | Buckle | Adjuster | Isofix | TT | QUANTITY | Testn° |
|----------|-------|--------|-------|----------|--------------|----------------------|----------------------|----------------|-------------------|---------|--------------------------|-----------|----------|--------|----------|--------|----|----------|--------|
| R129 | Frontal | Q6 | Q6 | Forward facing | Isofix 3 pts | - | Upright | yes | Low | With | With | With | - | no | no | yes | yes | n/a | - |
...

【安全阈值】（ECE R129 §7.1 · 单一标准）

| 测试项目 | 标准要求 | 适用假人 | 单位 | 标准条款 |
|----------|----------|----------|------|----------|
| HIC36 | ≤1000 | Q6/Q10 | - | ECE R129 §7.1.2 |
| 胸部合成加速度(3ms) | ≤60g | Q6/Q10 | g | ECE R129 §7.1.3 |
...

【合规声明】
本设计方案仅适配ECE R129 (i-Size) 标准，符合以下要求：
- 假人分类：Q6假人(105-145cm) + Q10假人(145-150cm)（ECE R129 §5.2）
- 动态测试：正面/后向/侧向碰撞（ECE R129 §7）
- ISOFIX连接：3点式+Top-tether（ECE R129 §6.1.2）
- 身高范围：105-150cm（ECE R129 §5.1.2）

> ⚠️ 重要提示：如需同时满足GB 27887-2024或FMVSS 213，必须在设计输入阶段明确选择"多标准适配"
```

## 测试

已提供完整的单元测试，位于：
`app/src/test/java/com/childproduct/designassistant/utils/Roadmate360OutputGeneratorTest.kt`

运行测试：
```bash
./gradlew :app:testDebugUnitTest --tests "com.childproduct.designassistant.utils.Roadmate360OutputGeneratorTest"
```

## 注意事项

1. **身高范围限制**：仅在 40-150cm 范围内有效
2. **标准单一性**：严格遵守 ECE R129 标准，不混用其他标准
3. **假人映射**：105-150cm 自动分段适配为 Q6+Q10
4. **不可变集合**：生成的 `ChildProductDesignScheme` 使用不可变集合
5. **验证机制**：生成后会自动验证数据完整性

## 扩展说明

如需支持其他标准（GB 27887-2024、FMVSS 213），可以：
1. 创建新的输出生成器（如 `GB27887OutputGenerator`）
2. 在输入阶段让用户选择"多标准适配"
3. 在生成时调用多个输出生成器并合并结果

## 版本历史

- **v1.0.0** (2024-01-15)
  - 初始版本
  - 支持 ECE R129 单一标准
  - 生成 ROADMATE 360 格式测试矩阵
  - 完整的安全阈值表格

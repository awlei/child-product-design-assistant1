# 动态输入字段功能更新文档

## 概述
本次更新实现了根据不同标准动态显示不同的输入字段功能，用户选择不同标准时，会显示对应的输入字段和单位。

## 更新内容

### 1. 新增数据模型 `StandardInputParams.kt`

**文件**: `app/src/main/java/com/design/assistant/model/StandardInputParams.kt`

创建了密封类 `StandardInputParams`，支持不同标准的输入参数类型：

```kotlin
sealed class StandardInputParams {
    // ECE R129: 身高范围（cm）
    data class EceR129Params(
        val minHeightCm: Int,
        val maxHeightCm: Int
    ) : StandardInputParams()

    // FMVSS 213: 体重范围（磅）
    data class Fmvss213Params(
        val minWeightLb: Int,
        val maxWeightLb: Int
    ) : StandardInputParams()

    // GPS028: 身高和体重（cm和kg）
    data class Gps028Params(
        val heightCm: Int,
        val weightKg: Int
    ) : StandardInputParams()

    // CMVSS 213: 体重范围（kg）
    data class Cmvss213Params(
        val minWeightKg: Int,
        val maxWeightKg: Int
    ) : StandardInputParams()

    // 通用参数
    data class GenericParams(
        val heightCm: Int = 0,
        val weightKg: Int = 0
    ) : StandardInputParams()
}
```

### 2. 更新 `StandardSelectScreen.kt`

**文件**: `app/src/main/java/com/design/assistant/ui/screens/StandardSelectScreen.kt`

实现了动态输入功能：

- **函数签名更新**:
  ```kotlin
  // 之前
  onGenerateClick: (ProductType, List<StandardType>, Int, Int) -> Unit
  
  // 现在
  onGenerateClick: (ProductType, List<StandardType>, StandardInputParams) -> Unit
  ```

- **动态输入字段**:
  - 根据 `primaryStandard` 判断输入类型
  - 根据不同的标准类型显示不同的输入字段

#### 不同标准的输入界面

| 标准 | 输入字段 | 单位 | 示例 |
|------|---------|------|------|
| **ECE R129** | 最小身高、最大身高 | cm | 87-105 cm |
| **FMVSS 213** | 最小体重、最大体重 | 磅 (lb) | 20-65 lb |
| **GPS028** | 身高、体重 | cm / kg | 95 cm, 15 kg |
| **CMVSS 213** | 最小体重、最大体重 | kg | 9-30 kg |
| **其他** | 身高、体重（可选） | cm / kg | - |

#### 输入验证

- **ECE R129**: 最小身高 ≤ 最大身高，范围 40-150cm
- **FMVSS 213**: 最小体重 ≤ 最大体重，范围 5-100磅
- **GPS028**: 身高和体重必须大于0，身高范围 40-150cm，体重范围 2-36kg
- **CMVSS 213**: 最小体重 ≤ 最大体重，范围 2-30kg

### 3. 更新 `MainActivity.kt`

**文件**: `app/src/main/java/com/design/assistant/MainActivity.kt`

处理不同标准的输入参数转换：

```kotlin
val (height, weight) = when (inputParams) {
    is StandardInputParams.EceR129Params -> {
        // ECE R129: 取身高范围的平均值
        val avgHeight = (inputParams.minHeightCm + inputParams.maxHeightCm) / 2
        avgHeight to 0
    }
    is StandardInputParams.Fmvss213Params -> {
        // FMVSS 213: 将磅转换为kg，取体重范围的平均值
        val avgWeightLb = (inputParams.minWeightLb + inputParams.maxWeightLb) / 2.0
        val avgWeightKg = (avgWeightLb * 0.453592).toInt()
        0 to avgWeightKg
    }
    is StandardInputParams.Gps028Params -> {
        // GPS028: 直接使用身高和体重
        inputParams.heightCm to inputParams.weightKg
    }
    is StandardInputParams.Cmvss213Params -> {
        // CMVSS 213: 取体重范围的平均值
        val avgWeightKg = (inputParams.minWeightKg + inputParams.maxWeightKg) / 2
        0 to avgWeightKg
    }
    is StandardInputParams.GenericParams -> {
        // 通用: 直接使用身高和体重
        inputParams.heightCm to inputParams.weightKg
    }
}
```

## 单位换算

| 单位 | 换算公式 |
|------|---------|
| 1磅 (lb) | ≈ 0.453592 千克 (kg) |
| 1千克 (kg) | ≈ 2.20462 磅 (lb) |

## 使用示例

### 示例 1: ECE R129 标准

1. 选择产品类型：儿童安全座椅
2. 选择标准：ECE R129
3. 输入最小身高：87 cm
4. 输入最大身高：105 cm
5. 点击"生成设计方案"

### 示例 2: FMVSS 213 标准

1. 选择产品类型：儿童安全座椅
2. 选择标准：FMVSS 213
3. 输入最小体重：20 磅
4. 输入最大体重：65 磅
5. 点击"生成设计方案"

### 示例 3: GPS028 标准

1. 选择产品类型：儿童安全座椅
2. 选择标准：GPS028 (GB 27887-2011)
3. 输入儿童身高：95 cm
4. 输入儿童体重：15 kg
5. 点击"生成设计方案"

## 用户体验改进

1. **动态提示**: 根据选择的标准显示对应的输入提示
2. **单位提示**: 输入字段显示清晰的单位标识
3. **范围提示**: 每个输入字段显示建议的输入范围
4. **实时验证**: 输入时实时验证数据的合理性
5. **错误提示**: 当最小值大于最大值时，显示错误提示
6. **单位转换**: FMVSS 213标准提供磅与千克的换算提示

## 技术细节

### 输入类型判断

```kotlin
val primaryStandard = uiState.selectedStandards.firstOrNull()
val inputType = when (primaryStandard) {
    StandardType.ECE_R129 -> "ECE_R129"
    StandardType.FMVSS213 -> "FMVSS213"
    StandardType.GPS028 -> "GPS028"
    StandardType.CMVSS213 -> "CMVSS213"
    else -> "GENERIC"
}
```

### 输入验证

```kotlin
private fun validateInputs(inputType: String): Boolean {
    return when (inputType) {
        "ECE_R129" -> minHeightCm.isNotBlank() && maxHeightCm.isNotBlank() &&
                      minHeightCm.toIntOrNull() ?: 0 > 0 &&
                      maxHeightCm.toIntOrNull() ?: 0 > 0 &&
                      (minHeightCm.toIntOrNull() ?: 0) <= (maxHeightCm.toIntOrNull() ?: Int.MAX_VALUE)
        "FMVSS213" -> minWeightLb.isNotBlank() && maxWeightLb.isNotBlank() &&
                      minWeightLb.toIntOrNull() ?: 0 > 0 &&
                      maxWeightLb.toIntOrNull() ?: 0 > 0 &&
                      (minWeightLb.toIntOrNull() ?: 0) <= (maxWeightLb.toIntOrNull() ?: Int.MAX_VALUE)
        // ... 其他类型的验证
        else -> true
    }
}
```

## 后续优化建议

1. **多标准支持**: 当用户同时选择多个标准时，显示多个标准的输入字段
2. **智能推荐**: 根据输入的参数自动推荐合适的组别和百分位
3. **历史记录**: 保存用户的输入历史，方便快速填写
4. **批量导入**: 支持从CSV或Excel批量导入参数
5. **参数模板**: 保存常用的参数配置为模板

## 测试建议

1. **边界测试**: 测试输入范围的最小值和最大值
2. **异常测试**: 测试非法输入（负数、非数字、过大数值）
3. **组合测试**: 测试不同标准之间的切换
4. **UI测试**: 测试在不同屏幕尺寸下的显示效果
5. **转换测试**: 验证单位转换的准确性

## 构建信息

- **Commit**: `5539294`
- **提交时间**: 2026-02-06
- **文件修改**:
  - 新增: `app/src/main/java/com/design/assistant/model/StandardInputParams.kt`
  - 修改: `app/src/main/java/com/design/assistant/ui/screens/StandardSelectScreen.kt`
  - 修改: `app/src/main/java/com/design/assistant/MainActivity.kt`

## 相关文档

- [BUILD_FIX_ROUND9.md](BUILD_FIX_ROUND9.md) - 字符串插值修复报告
- [README.md](README.md) - 项目总体文档
- [USER_MANUAL.md](USER_MANUAL.md) - 用户手册

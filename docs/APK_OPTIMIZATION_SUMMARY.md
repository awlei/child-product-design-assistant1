# APK流程优化 - 动态身高体重范围

## 功能概述

根据选择的产品类型和标准，动态调整身高体重的输入范围，提高用户体验和输入准确性。

## 主要修改

### 1. 新增 getHeightWeightRange() 函数

位置：`app/src/main/java/com/design/assistant/ui/screens/StandardSelectScreen.kt`

功能：根据产品类型和选中的标准，返回对应的身高体重范围。

#### 支持的产品类型和标准：

**儿童安全座椅 (CHILD_SEAT)**
- GB 27887/ECE R129标准：40-150cm，0-36kg
- 北美标准 (CMVSS213/FMVSS213)：50-145cm，2-36kg
- 通用范围：40-150cm，0-36kg

**婴儿推车 (STROLLER)**
- EN 1888标准：0-105cm，0-22kg
- 北美标准 (ASTM F833/CSA B311)：0-125cm，0-22kg
- 通用范围：0-125cm，0-22kg

**儿童高脚椅 (HIGH_CHAIR)**
- EN 14988标准：60-105cm，8-25kg
- 北美标准 (ASTM F404/CSA B229)：60-110cm，8-25kg
- 通用范围：60-110cm，8-25kg

**儿童床 (CRIB)**
- EN 716标准：50-125cm，5-30kg
- 北美标准 (ASTM F1169/CSA B113)：50-130cm，5-35kg
- 通用范围：50-130cm，5-35kg

### 2. 新增 HeightWeightRange 数据类

```kotlin
data class HeightWeightRange(
    val minHeight: Int,
    val maxHeight: Int,
    val minWeight: Int,
    val maxWeight: Int,
    val description: String
)
```

### 3. UI优化

#### 标准范围提示
在身高体重输入区域顶部添加彩色提示框，显示当前选择的标准和对应的身高体重范围。

```
📋 GB 27887/ECE R129标准：40-150cm，0-36kg
```

#### 动态输入提示
- **placeholder**：显示动态范围（如"40-150"）
- **支持文本**：显示"范围: 40-150cm"或"✓ 当前身高: 85cm (在标准范围内)"
- **错误提示**：超出范围时显示红色错误信息

#### 智能年龄段判断
根据产品类型和输入的身高体重，智能判断对应的年龄段：

**儿童安全座椅组别判断：**
- 新生儿/婴儿 (0-9个月)：40-65cm，0-9kg
- 幼儿 I组 (9-18个月)：66-85cm，9-13kg
- 幼儿 II组 (1.5-4岁)：86-105cm，9-18kg
- 儿童 III组 (3-6岁)：100-125cm，15-25kg
- 大龄儿童 IV组 (6-12岁)：125-150cm，22-36kg

**婴儿推车阶段判断：**
- 新生儿阶段：0-65cm
- 婴儿阶段 (可坐立)：66-95cm
- 幼儿阶段：96-125cm

**儿童高脚椅阶段判断：**
- 小童 (约6-12个月)：60-80cm
- 幼儿 (约1-3岁)：81-95cm
- 大童 (约3-6岁)：96-110cm

**儿童床阶段判断：**
- 婴儿期：50-85cm
- 幼儿期：86-110cm
- 儿童期：111-130cm

#### 按钮启用逻辑更新
"生成设计方案"按钮现在只有在满足以下所有条件时才会启用：
1. 已选择产品类型
2. 已选择至少一个标准
3. 身高输入不为空且在有效范围内
4. 体重输入不为空且在有效范围内

### 4. 用户体验优化

- 实时验证：输入时实时显示验证结果
- 视觉反馈：有效范围显示绿色✓，超出范围显示红色⚠️
- 智能提示：根据输入自动提示年龄段
- 防误操作：超出范围时禁用生成按钮

## 技术实现

### 状态管理
```kotlin
val heightWeightRange = remember(uiState.selectedProductType, uiState.selectedStandards) {
    getHeightWeightRange(uiState.selectedProductType, uiState.selectedStandards)
}
```

使用 `remember` 监听产品类型和标准的变化，自动更新身高体重范围。

### 验证逻辑
```kotlin
val isInValidRange = height in heightWeightRange.minHeight..heightWeightRange.maxHeight &&
                     weight in heightWeightRange.minWeight..heightWeightRange.maxWeight
```

### 按钮启用条件
```kotlin
enabled = viewModel.canProceed() &&
          childHeight.isNotBlank() &&
          childWeight.isNotBlank() &&
          childHeight.toIntOrNull()?.let { height ->
              height in heightWeightRange.minHeight..heightWeightRange.maxHeight
          } == true &&
          childWeight.toIntOrNull()?.let { weight ->
              weight in heightWeightRange.minWeight..heightWeightRange.maxWeight
          } == true
```

## 测试场景

### 场景1：儿童安全座椅 + GB 27887标准
- 输入身高：50cm（有效）→ 显示✓
- 输入体重：5kg（有效）→ 显示✓
- 年龄段提示：新生儿/婴儿 (0-9个月)
- 生成按钮：启用

### 场景2：儿童安全座椅 + 北美标准
- 输入身高：40cm（无效，<50cm）→ 显示⚠️
- 输入体重：5kg（无效，<2kg）→ 显示⚠️
- 年龄段提示：超出适用范围
- 生成按钮：禁用

### 场景3：婴儿推车 + EN 1888标准
- 输入身高：90cm（有效）→ 显示✓
- 输入体重：10kg（有效）→ 显示✓
- 年龄段提示：婴儿阶段 (可坐立)
- 生成按钮：启用

### 场景4：未选择产品类型
- 身高体重输入：灰色
- 范围提示："请先选择产品类型"
- 生成按钮：禁用

## 后续改进建议

1. 添加输入历史记录，方便重复输入
2. 支持从数据库导入常用儿童参数
3. 添加参数预设按钮（如"6个月婴儿"、"3岁儿童"等）
4. 增加身高体重与百分位的对照表
5. 支持批量输入（多儿童场景）

## 注意事项

1. 所有范围值都基于对应标准的技术规范
2. 加拿大标准（CSA系列）已整合在北美标准分支中
3. 当同时选择多个标准时，使用最严格的范围（交集）
4. 用户输入必须同时在身高和体重范围内才能生成方案

## 文件变更清单

- ✏️ `app/src/main/java/com/design/assistant/ui/screens/StandardSelectScreen.kt`
  - 新增 `getHeightWeightRange()` 函数
  - 新增 `HeightWeightRange` 数据类
  - 更新身高体重输入UI组件
  - 更新年龄段判断逻辑
  - 更新按钮启用条件
  - 添加标准范围提示组件

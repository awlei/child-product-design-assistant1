# 设计方案输出格式优化说明

## 优化目标

针对当前设计方案"乱码冗余、排版混乱"的问题，实现"结构化、无冗余、易读性强"的输出效果。

## 优化内容

### 1. 冗余内容清理

**优化前：**
```
CreativeIdea(id=abc123)
productType=CHILD_SAFETY_SEAT
ComplianceParameters(dummyType=Q1, hicLimit=390, ...)
```

**优化后：**
```
【基本信息】
- 产品类型：儿童安全座椅
- 适用年龄段：0-3岁
- 设计主题：婴幼儿专用儿童安全座椅（柔和色彩）
```

### 2. 结构化排版

采用固定的模块+列表形式，模块标题加粗，内容用"-"引导的无序列表：

```
【基本信息】
- 产品类型：[对应产品类型]
- 适用年龄段：[精准匹配的年龄段]
- 设计主题：[简洁主题名]

【核心设计特点】
- [特点1]
- [特点2]
- [特点3]
- [特点4]

【推荐材料】
- [材料1]
- [材料2]
- [材料3]
- [材料4]

【合规参数】
- 对应标准：[关联的标准]
- 适配假人：[匹配的假人类型]
- 安全阈值：
  - HIC极限值：≤[对应数值]
  - 胸部加速度：≤[对应数值]
  - 颈部张力：≤[对应数值]
  - 头部位移：≤[对应数值]
  - [其他阈值]

【安全注意事项】
- [注意点1]
- [注意点2]
- [注意点3]
```

### 3. 标准信息精准匹配

确保"适用年龄段""适配假人""安全阈值"严格对应：
- 0-3岁 → Q1假人 → HIC≤390
- 3-6岁 → Q1.5假人 → HIC≤570
- 6-9岁 → Q3假人 → HIC≤1000

标准条款简化为易懂描述：
- "ECE R129 §7.1.2" → "符合UN R129 i-Size动态测试要求"
- "ECE R129 §7.1.3" → "胸部加速度≤55g"

### 4. 视觉可读性增强

- 模块标题用加粗样式区分
- 同一模块内的列表项缩进对齐
- 避免大段文字堆砌
- 同类信息（如安全阈值）归类到同一子列表

## 实现细节

### 新增文件

1. **DesignSchemeFormatter.kt**
   - 位置：`app/src/main/java/com/childproduct/designassistant/utils/DesignSchemeFormatter.kt`
   - 功能：将CreativeIdea对象转换为结构化、易读的文本格式
   - 主要方法：
     - `formatCreativeIdea(idea: CreativeIdea): String` - 格式化为文本
     - `formatToCardData(idea: CreativeIdea): SchemeCardData` - 格式化为UI卡片数据
     - `formatCreativeIdeaByHeight(...)`: String` - 带身高范围的格式化

2. **DesignSchemeFormatterTest.kt**
   - 位置：`app/src/main/java/com/childproduct/designassistant/utils/DesignSchemeFormatterTest.kt`
   - 功能：测试格式化输出是否符合要求
   - 主要方法：
     - `testFormatCreativeIdea()`: String` - 测试文本格式化
     - `testFormatToCardData(): SchemeCardData?` - 测试卡片数据格式化
     - `validateFormattedOutput(): Boolean` - 验证输出是否符合要求

### 修改文件

1. **CreativeService.kt**
   - 在`CreativeIdeaResult`类中添加新方法：
     - `getFormattedScheme(): String` - 获取格式化文本
     - `getFormattedSchemeWithHeight(...): String` - 获取带身高范围的格式化文本
     - `getCardData(): SchemeCardData?` - 获取卡片数据

2. **IntegratedReportScreen.kt**
   - 添加`StructuredDesignSchemeCard` Composable函数
   - 添加`SectionCard`和`InfoRow`辅助组件
   - 在`SchemeDetailTab`中集成结构化展示

## 使用示例

### 文本格式化

```kotlin
val idea = creativeService.generateCreativeIdea(
    ageGroup = AgeGroup.INFANT,
    productType = ProductType.CHILD_SAFETY_SEAT,
    theme = "柔和色彩"
)

val formattedText = DesignSchemeFormatter.formatCreativeIdea(idea)
println(formattedText)
```

### UI展示

```kotlin
val idea = viewModel.creativeIdea.value
val cardData = DesignSchemeFormatter.formatToCardData(idea)

StructuredDesignSchemeCard(cardData = cardData)
```

### 通过CreativeIdeaResult

```kotlin
val result = creativeService.generateCreativeIdeaByHeight(
    minHeightCm = 40,
    maxHeightCm = 150,
    productType = ProductType.CHILD_SAFETY_SEAT
)

// 获取格式化文本
val formattedText = result.getFormattedScheme()

// 获取带身高范围的格式化文本
val formattedTextWithHeight = result.getFormattedSchemeWithHeight(40, 150)

// 获取卡片数据
val cardData = result.getCardData()
```

## 输出示例

```
【基本信息】
- 产品类型：儿童安全座椅
- 适用年龄段：0-3岁
- 设计主题：婴幼儿专用儿童安全座椅（柔和色彩）

【核心设计特点】
- 易安装性：支持ISOFIX快速连接
- 舒适性：高回弹海绵填充靠背
- 材质环保：食品级安全材料
- 安全性：符合多国标安全要求

【推荐材料】
- 主体框架：食品级PP塑料
- 填充层：高回弹海绵
- 约束部件：高强度安全带织带
- 支撑结构：铝合金支架

【合规参数】
- 对应标准：符合UN R129 i-Size动态测试要求 + FMVSS 213动态测试要求
- 适配假人：Q1幼儿假人
- 安全阈值：
  - HIC极限值：≤390
  - 胸部加速度：≤55g
  - 颈部张力：≤1800N
  - 头部位移：≤550mm
  - 颈部压缩：≤2200N
  - 膝部位移：≤650mm
  - 胸部位移：≤52mm

【安全注意事项】
- 部件尺寸＞3.5cm，避免吞咽风险
- 无甲醛/重金属，符合食品级标准
- 边缘圆角处理，无尖锐结构
```

## 优势

1. **结构清晰**：固定的模块划分，用户快速定位信息
2. **无冗余**：移除所有技术字段和代码式标识
3. **易读性强**：列表形式，缩进对齐，视觉友好
4. **精准匹配**：假人类型、年龄段、安全阈值一一对应
5. **标准简化**：将复杂条款转化为易懂描述

## 下一步

- [x] 创建DesignSchemeFormatter工具类
- [x] 优化CreativeService生成逻辑
- [x] 优化IntegratedReportScreen展示逻辑
- [ ] 添加更多单元测试
- [ ] 支持自定义输出格式
- [ ] 支持导出为PDF/Word文档

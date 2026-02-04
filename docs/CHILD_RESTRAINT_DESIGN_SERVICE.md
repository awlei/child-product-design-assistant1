# 儿童安全座椅标准适配设计服务使用指南

## 概述

`ChildRestraintDesignService` 是一个专门用于儿童安全座椅标准适配设计的服务，根据用户选择的标准严格调用相应的数据库，生成符合要求的设计方案。

## 核心特性

1. **标准隔离机制** - 严格区分不同标准，禁止混用
2. **多标准支持** - 支持同时选择多个标准
3. **动态生成** - 根据用户输入（身高、体重）动态生成设计方案
4. **格式化输出** - 支持Markdown格式输出

## 支持的标准

| 标准代码 | 标准名称 | 地区 | 说明 |
|---------|---------|------|------|
| ECE R129 | ECE R129 (i-Size) | 欧洲 | 欧盟最新标准，基于身高分组 |
| GB 28007-2024 | GB 28007-2024 | 中国 | 中国新标准，2026年实施 |
| FMVSS 213 | FMVSS 213/213a | 美国 | 美国联邦标准，包含侧碰测试 |
| AS/NZS 1754 | AS/NZS 1754 | 澳大利亚 | 澳洲标准 |

## 使用示例

### 1. 基本使用

```kotlin
// 创建服务实例
val service = ChildRestraintDesignService()

// 创建标准选择
val selection = ChildRestraintDesignService.StandardSelection(
    eceR129 = true,      // 选择 ECE R129
    gb27887 = false,    // 不选择 GB 28007
    fmvss213 = false,   // 不选择 FMVSS 213
    asNzs1754 = false   // 不选择 AS/NZS 1754
)

// 生成设计方案
val proposal = service.generateDesignProposal(
    selection = selection,
    heightCm = 100.0,   // 儿童身高（cm）
    weightKg = 15.0     // 儿童体重（kg）
)

// 格式化输出为Markdown
val markdownOutput = service.formatAsMarkdown(proposal)
println(markdownOutput)
```

### 2. 多标准选择

```kotlin
// 同时选择多个标准
val selection = ChildRestraintDesignService.StandardSelection(
    eceR129 = true,      // 欧标
    gb27887 = true,     // 国标
    fmvss213 = false,
    asNzs1754 = false
)

// 生成方案（会包含两个标准的内容）
val proposal = service.generateDesignProposal(
    selection = selection,
    heightCm = 83.0,
    weightKg = 11.0
)
```

### 3. 美标选择

```kotlin
// 选择美国标准
val selection = ChildRestraintDesignService.StandardSelection(
    eceR129 = false,
    gb27887 = false,
    fmvss213 = true,    // 美标
    asNzs1754 = false
)

val proposal = service.generateDesignProposal(
    selection = selection,
    heightCm = 125.0,
    weightKg = 22.0
)
```

## 输出结构

```
📦 儿童安全座椅设计方案

### 【适用标准】
🔵 ECE R129
🔵 GB 28007-2024

### 📊 基础适配数据
#### 🔽 假人
- **身高范围**：
  ECE R129: 新生儿至12岁（基于身高分组）
  GB 28007-2024: 新生儿-36kg（12岁以下）
- **体重范围**：
  ECE R129: 0-36kg
  GB 28007-2024: 0-36kg
- **安装方向**：
  ECE R129: 后向安装 / 前向安装
  GB 28007-2024: 反向、前向、增高垫

### 📏 设计参数
- **头枕高度**：
  ECE R129: 参考GPS-028 Q系列假人数据
  GB 28007-2024: 参考欧标要求
- **座宽**：
  ECE R129: ISOFIX SIZE CLASS (B1, B2, D, E)
  GB 28007-2024: 400-500mm（根据年龄分组）
- **盒子 Envelope**：
  ECE R129: External Envelope (基于ISO-FIX)
  GB 28007-2024: 外形尺寸限制
- **侧防面积**：
  ECE R129: 侧面碰撞防护区域
  GB 28007-2024: 侧碰参考欧标

### ⚖️ 测试要求
- **正面**：
  ECE R129: 50km/h ±2km/h 正碰
  GB 28007-2024: 50km/h 正碰
- **侧撞胸部压缩**：
  ECE R129: 侧碰胸部压缩量 ≤ 35mm
  GB 28007-2024: 参考欧标侧碰要求
- **织带强度**：
  ECE R129: 织带最小断裂强度 4.5kN
  GB 28007-2024: 织带最小断裂强度 4.5kN

### 🧪 标准测试项
- **动态碰撞：正碰**：
  ECE R129: 50km/h 正碰 + 脉冲波形
  GB 28007-2024: 50km/h 正碰测试
- **动态碰撞：后碰**：
  ECE R129: 无强制后碰测试要求
  GB 28007-2024: 无强制后碰测试要求
- **动态碰撞：侧碰**：
  ECE R129: 24km/h 侧碰 + Q系列假人
  GB 28007-2024: 侧碰测试（参考欧标）
- **阻燃**：
  ECE R129: UN R118.03 阻燃要求（水平燃烧速度 ≤ 100mm/min）
  GB 28007-2024: GB 8410 阻燃要求
```

## 数据模型

### StandardSelection
```kotlin
data class StandardSelection(
    val eceR129: Boolean = false,      // ECE R129
    val gb27887: Boolean = false,     // GB 28007-2024
    val fmvss213: Boolean = false,    // FMVSS 213
    val asNzs1754: Boolean = false    // AS/NZS 1754
)
```

### DesignProposal
```kotlin
data class DesignProposal(
    val applicableStandards: List<String>,        // 适用标准列表
    val dummyData: DummyDataSection,              // 假人数据
    val designParameters: DesignParametersSection, // 设计参数
    val testRequirements: TestRequirementsSection, // 测试要求
    val standardTestItems: StandardTestItemsSection // 标准测试项
)
```

## 数据库调用规则

| 选择的标准 | 调用的数据库 |
|----------|-------------|
| ECE R129 | EceR129StandardDatabase + GPS028Database |
| GB 28007-2024 | EceR129StandardDatabase + GPS028Database (欧标部分) |
| FMVSS 213 | FMVSSDatabase + EceR129StandardDatabase (美标部分) |
| AS/NZS 1754 | 独立数据库（待实现） |

## UI集成示例

### Jetpack Compose 示例

```kotlin
@Composable
fun StandardSelectionScreen() {
    var eceR129 by remember { mutableStateOf(false) }
    var gb27887 by remember { mutableStateOf(false) }
    var fmvss213 by remember { mutableStateOf(false) }
    var asNzs1754 by remember { mutableStateOf(false) }
    
    val service = remember { ChildRestraintDesignService() }
    
    Column {
        // 标准选择区域
        Text("选择适用标准", style = MaterialTheme.typography.h6)
        
        Row {
            Checkbox(
                checked = eceR129,
                onCheckedChange = { eceR129 = it }
            )
            Text("ECE R129 (欧盟i-Size)")
            
            Checkbox(
                checked = gb27887,
                onCheckedChange = { gb27887 = it }
            )
            Text("GB 28007-2024 (中国新标)")
            
            Checkbox(
                checked = fmvss213,
                onCheckedChange = { fmvss213 = it }
            )
            Text("FMVSS 213 (美国标准)")
            
            Checkbox(
                checked = asNzs1754,
                onCheckedChange = { asNzs1754 = it }
            )
            Text("AS/NZS 1754 (澳洲标准)")
        }
        
        // 快捷按钮
        Row {
            Button(onClick = {
                eceR129 = true
                gb27887 = true
                fmvss213 = true
                asNzs1754 = true
            }) {
                Text("全选")
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(onClick = {
                eceR129 = false
                gb27887 = false
                fmvss213 = false
                asNzs1754 = false
            }) {
                Text("取消全选")
            }
        }
        
        // 生成方案按钮
        Button(
            onClick = {
                val selection = ChildRestraintDesignService.StandardSelection(
                    eceR129 = eceR129,
                    gb27887 = gb27887,
                    fmvss213 = fmvss213,
                    asNzs1754 = asNzs1754
                )
                val proposal = service.generateDesignProposal(
                    selection = selection,
                    heightCm = 100.0,
                    weightKg = 15.0
                )
                // 显示结果
            }
        ) {
            Text("生成设计方案")
        }
    }
}
```

## 测试

运行测试文件：

```kotlin
fun main() {
    val test = ChildRestraintDesignServiceTest()
    test.runAllTests()
}
```

或者调用快速测试函数：

```kotlin
fun main() {
    testService()
}
```

## 注意事项

1. **标准隔离** - 严格根据用户选择调用对应数据库，禁止混用
2. **输出规范** - 未选择的标准不会出现在输出中
3. **数据准确** - 所有数据来自权威标准数据库
4. **动态计算** - 根据用户输入的身高、体重动态匹配假人

## 后续优化

- [ ] 添加 AS/NZS 1754 澳洲标准数据库
- [ ] 实现更多设计参数计算
- [ ] 添加可视化图表支持
- [ ] 支持PDF/Word导出

## 相关文件

- `ChildRestraintDesignService.kt` - 主服务类
- `ChildRestraintDesignServiceTest.kt` - 测试类
- `EceR129StandardDatabase.kt` - 欧标数据库
- `GPS028Database.kt` - GPS参数数据库
- `FMVSSStandardsData.kt` - 美标数据库

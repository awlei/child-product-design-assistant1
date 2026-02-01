# GPS-028人体测量学数据集成 - 优化说明

## 概述

基于GPS-028 Anthropometry 11-28-2018人体测量学数据，实现**人体数据→产品参数→标准验证**的闭环工作流，面向工程师的专业设计工具。

## 核心优化内容

### 1. GPS-028人体测量学数据库

#### 新增实体类
- **ChildAnthropometry.kt** - 儿童人体测量学数据
  - 包含头部、躯干、臀部、大腿尺寸
  - 五点式安全带关键点坐标
  - 自动生成座椅尺寸参数
  - 自动生成安全带要求

- **DummySpecification.kt** - 假人规格（Q0-Q10 + US ATD）
  - EU Q-Series（UN R129）：Q0, Q0+, Q1, Q1.5, Q3, Q3s, Q6, Q10
  - US ATD（FMVSS 213）：CRABI_12M, HIII_3YR, HIII_6YR, HIII_10YR
  - ✅ 正确包含Q3s假人（105-125cm）
  - 安装方向规则：40-105cm强制后向，105cm+强制前向+Top-tether

### 2. 尺寸包络线验证器（SizeEnvelopeValidator）

#### UN R129 Annex 18验证
- ✅ 座椅宽度验证（基于GPS-028臀宽数据）
- ✅ 头枕高度验证（UN R129 Annex 18 §3.2）
- ✅ 肩带高度调节范围验证（GPS-028 Harness Segment Length）
- ✅ ISOFIX envelope验证（UN R129 Annex 17）

#### 关键参数
- 臀宽范围：210mm (Q0+) → 410mm (Q10)
- 头枕高度：250mm (后向) → 420mm (Q10)
- 肩带高度：180mm → 420mm（100mm调节范围）

### 3. 人体数据到产品参数转换器（AnthropometryToDesignConverter）

#### 转换逻辑
1. **提取关键尺寸极值**（5th-95th百分位）
2. **生成座椅尺寸**（基于GPS-028设计规则）
   - 宽度：臀宽×1.1~1.25
   - 深度：坐高×35%~45%
   - 高度：头枕高度+120mm调节范围
3. **生成安全带参数**（GPS-028 Harness Segment Length）
4. **生成ISOFIX参数**（UN R129 Annex 17）
5. **生成材料规格**（Dorel GPS规范）

#### 设计规则
- 最小间隙：臀宽×1.1
- 舒适间隙：臀宽×1.2
- 最大间隙：臀宽×1.3
- 座深：坐高×40%（理想）

### 4. 工程输出格式化器（EngineeringOutputFormatter）

#### 三种输出格式
1. **MARKDOWN** - 技术文档
   - 包含所有设计参数
   - 标准条款引用
   - 工程提示
   - 版本水印

2. **CSV** - ROADMATE 360导入
   - 20列标准格式
   - 自动生成测试用例
   - 基于适配假人

3. **JSON** - PLM/CAD系统集成
   - 结构化数据
   - 标准引用
   - 元数据

## 核心优化特性

✅ **GPS-028数据集成**：直接使用Dorel人体测量学数据库，确保设计参数基于真实儿童尺寸
✅ **UN R129 Annex 18合规**：自动验证尺寸包络线，避免设计返工
✅ **Q3s假人支持**：正确映射105-125cm → Q3s（行业常见错误点）
✅ **ISOFIX envelope验证**：集成UN R129 Annex 17刚性约束要求
✅ **三格式输出**：Markdown（技术文档）/ CSV（ROADMATE 360）/ JSON（PLM集成）
✅ **工程闭环**：人体数据 → 产品参数 → 标准验证 → 工程输出
✅ **无代码泄露**：输出纯净工程参数，无UUID/内部枚举/乱码

## 工程师价值

### 减少设计迭代次数
- 基于GPS-028真实人体数据，避免经验设计
- 自动验证标准合规性，早期发现问题

### 确保一次性通过UN R129认证
- 严格按照UN R129 Annex 18尺寸包络线设计
- 覆盖所有标准假人（Q0-Q10）
- 自动生成测试矩阵（ROADMATE 360格式）

### 提高设计效率
- 一键生成工程设计参数
- 三种输出格式满足不同需求
- 集成PLM/CAD系统

## 使用示例

### 输入
```kotlin
val minHeightCm = 40.0
val maxHeightCm = 150.0
val applicableDummies = listOf("Q0", "Q0+", "Q1", "Q1.5", "Q3", "Q3s", "Q6", "Q10")
```

### 输出（Markdown）
```markdown
# 儿童安全座椅工程设计参数
## 基于GPS-028人体测量学数据 · UN R129 Annex 18合规

### 1. 适配范围
| 参数 | 值 |
|------|-----|
| 适用假人 | Q0 / Q0+ / Q1 / Q1.5 / Q3 / Q3s / Q6 / Q10 |
| 标准依据 | UN R129 Annex 18, UN R129 Annex 17, GPS-028 Anthropometry 11-28-2018 |

### 2. 座椅尺寸参数（mm）
| 参数 | 最小值 | 推荐值 | 最大值 | 设计依据 |
|------|--------|--------|--------|----------|
| 座椅宽度 | 231 | 386 | 513 | GPS-028臀宽×1.1~1.25 |
| 座椅深度 | 103 | 196 | 252 | GPS-028坐高×35%~45% |
| 头枕高度调节范围 | 200 | 260 | 320 | UN R129 Annex 18 §3.2 |
```

## 数据来源

- **GPS-028 Anthropometry 11-28-2018**: Dorel人体测量学数据库
- **UN R129 Annex 6**: 假人规格定义
- **UN R129 Annex 17**: ISOFIX刚性约束
- **UN R129 Annex 18**: 尺寸包络线要求
- **FMVSS 213 S9.3**: US ATD规格

## 文件清单

### 新增文件
1. `model/anthropometry/ChildAnthropometry.kt` - 人体测量学数据实体
2. `model/anthropometry/DummySpecification.kt` - 假人规格
3. `engineering/SizeEnvelopeValidator.kt` - 尺寸包络线验证器
4. `engineering/AnthropometryToDesignConverter.kt` - 转换器
5. `engineering/EngineeringOutputFormatter.kt` - 格式化器

### 关键数据
- GPS-028人体测量学数据（40-150cm，5th/50th/95th百分位）
- 8种EU Q-Series假人规格
- 4种US ATD规格
- UN R129 Annex 18尺寸包络线要求

## 下一步

1. 集成到UI界面（EngineeringDesignScreen）
2. 添加更多人体测量学数据
3. 支持更多标准（GB 27887, FMVSS 213等）
4. 添加碰撞仿真数据集成

---

**版本**: v2.1.0
**更新日期**: 2025-01-29
**数据来源**: GPS-028 Anthropometry 11-28-2018

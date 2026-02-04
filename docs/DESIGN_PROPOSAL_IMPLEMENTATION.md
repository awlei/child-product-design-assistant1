# 设计方案结构化输出功能实施文档

## 概述

**实施时间**: 2026-02-04
**提交ID**: `0c1dabe`
**功能**: 实现儿童安全座椅设计方案的结构化输出功能

## 功能需求

根据用户需求，设计方案需要以结构化的方式展示以下内容：

```
📦 儿童安全座椅设计方案
│
├─ 【适用标准】（蓝色标签）
│
├─ 📊 基础适配数据
│  ├─ 🔽 假人
│  │  ├─ 身高范围
│  │  ├─ 体重范围
│  │  └─ 安装方向
│
├─ 📏 设计参数
│  ├─ 头枕高度
│  ├─ 座宽
│  ├─ 盒子Envelope
│  └─ 侧防面积
│
├─ ⚖️ 测试要求
│  ├─ 正面
│  ├─ 侧撞胸部压缩
│  └─ 织带强度
│
└─ 🧪 标准测试项
   ├─ 动态碰撞：正碰
   ├─ 动态碰撞：后碰
   ├─ 动态碰撞：侧碰
   └─ 阻燃
```

## 实施内容

### 1. 数据模型层

#### 创建的文件

**`app/src/main/java/com/childproduct/designassistant/data/model/DesignProposal.kt`**

定义了以下数据类：
- `DesignProposal`: 设计方案主数据类
- `BasicFitData`: 基础适配数据
- `DummyInfo`: 假人信息
- `DesignParameters`: 设计参数
- `TestRequirements`: 测试要求
- `StandardTestItems`: 标准测试项
- `DesignProposalRequest`: 设计方案生成请求
- `UserDummyInput`: 用户输入的假人信息

**特点**:
- 完整的类型定义
- 支持可选参数
- 包含生成时间戳

### 2. 服务层

#### 创建的文件

**`app/src/main/java/com/childproduct/designassistant/service/StandardDatabaseService.kt`**

标准数据库查询服务，提供：
- 根据标准ID查询标准信息
- 支持儿童安全座椅和婴儿推车两种产品
- 模糊搜索功能
- 批量查询标准摘要信息

**`app/src/main/java/com/childproduct/designassistant/service/DesignProposalGenerator.kt`**

设计方案生成器，负责：
- 构建系统提示词，确保LLM输出符合JSON格式
- 集成标准数据库查询，获取标准详细信息
- 生成用户消息，包含标准信息和用户输入
- 调用LLM生成设计方案
- 解析LLM响应为结构化数据

**系统提示词特点**:
- 明确要求JSON格式输出
- 定义完整的数据结构
- 提供生成规则和注意事项
- 强调数据准确性

### 3. UI层

#### 创建的文件

**`app/src/main/java/com/childproduct/designassistant/ui/screens/DesignProposalScreen.kt`**

设计方案展示界面，特点：
- 使用卡片式布局
- 清晰的视觉层次
- 蓝色标签突出显示适用标准
- 使用图标增强可读性
- 支持滚动查看完整内容
- 返回导航功能

**组件**:
- `ProductTypeHeader`: 产品类型标题
- `ApplicableStandardsCard`: 适用标准卡片（蓝色标签）
- `BasicFitDataCard`: 基础适配数据卡片
- `DesignParametersCard`: 设计参数卡片
- `TestRequirementsCard`: 测试要求卡片
- `StandardTestItemsCard`: 标准测试项卡片

**`app/src/main/java/com/childproduct/designassistant/ui/screens/DesignProposalViewModel.kt`**

设计方案ViewModel，负责：
- 管理设计方案生成状态
- 调用DesignProposalGenerator生成方案
- 处理生成成功/失败状态
- 提供当前设计方案数据

#### 修改的文件

**`app/src/main/java/com/childproduct/designassistant/ui/standard/StandardSelectionViewModel.kt`**

添加了：
- `createDesignRequest()`: 创建设计方案请求
- `getSelectedProductTypes()`: 获取选中的产品类型
- `getSelectedStandardsCount()`: 获取选中的标准数量

**`app/src/main/java/com/childproduct/designassistant/MainActivity.kt`**

添加了：
- `showDesignProposal`: 控制设计方案界面显示的状态
- `designProposalViewModel`: 设计方案ViewModel
- 设计方案界面导航逻辑
- 从标准选择界面生成并展示设计方案的流程

## 数据流

```
用户操作
    ↓
StandardSelectionViewModel
    ↓
MainActivity (onGenerateDesign)
    ↓
DesignProposalViewModel (generateProposal)
    ↓
DesignProposalGenerator
    ├─ StandardDatabaseService (查询标准)
    └─ LLMClient (生成设计方案)
    ↓
DesignProposal (结构化数据)
    ↓
DesignProposalScreen (展示)
```

## 技术亮点

### 1. 结构化输出

- 严格定义的数据模型
- JSON格式的LLM输出
- 类型安全的代码

### 2. 数据库集成

- 从标准数据库查询详细信息
- 支持多种产品类型
- 高效的查询接口

### 3. UI设计

- 清晰的视觉层次
- 使用颜色、图标、间距区分不同层级
- 蓝色标签突出显示适用标准
- 响应式布局

### 4. 状态管理

- 使用ViewModel管理状态
- 清晰的状态定义（Idle/Loading/Success/Error）
- 响应式数据流

## 使用流程

1. 用户打开"标准适配"功能
2. 选择适用的标准（可多选）
3. 点击"生成设计方案"按钮
4. 系统查询标准数据库
5. 调用LLM生成设计方案
6. 展示结构化的设计方案
7. 用户可查看完整的设计参数、测试要求等

## 文件清单

### 新增文件

1. `app/src/main/java/com/childproduct/designassistant/data/model/DesignProposal.kt`
2. `app/src/main/java/com/childproduct/designassistant/service/StandardDatabaseService.kt`
3. `app/src/main/java/com/childproduct/designassistant/service/DesignProposalGenerator.kt`
4. `app/src/main/java/com/childproduct/designassistant/ui/screens/DesignProposalScreen.kt`
5. `app/src/main/java/com/childproduct/designassistant/ui/screens/DesignProposalViewModel.kt`

### 修改文件

1. `app/src/main/java/com/childproduct/designassistant/ui/standard/StandardSelectionViewModel.kt`
2. `app/src/main/java/com/childproduct/designassistant/MainActivity.kt`

## 代码统计

- **新增文件**: 5个
- **修改文件**: 2个
- **总新增代码**: 1046行
- **删除代码**: 1行

## 测试计划

### 功能测试

- [ ] 从标准选择界面生成设计方案
- [ ] 验证设计方案包含所有5大模块
- [ ] 验证适用标准以蓝色标签显示
- [ ] 验证数据准确性
- [ ] 验证返回导航功能

### 数据流测试

- [ ] 标准数据库查询
- [ ] LLM生成逻辑
- [ ] JSON解析
- [ ] UI渲染

### 边界测试

- [ ] 无选中标准时的处理
- [ ] LLM生成失败的处理
- [ ] 数据库查询失败的处理

## 后续优化

1. **性能优化**
   - 缓存标准数据库查询结果
   - 优化LLM提示词长度

2. **功能扩展**
   - 支持导出设计方案为PDF
   - 支持设计方案历史记录
   - 支持方案对比功能

3. **UI优化**
   - 添加加载动画
   - 添加错误提示
   - 优化移动端显示

## 相关文档

- [BUILD_ERROR_FIX.md](BUILD_ERROR_FIX.md) - GitHub Actions构建错误修复
- [GITHUB_BUILD_GUIDE.md](GITHUB_BUILD_GUIDE.md) - GitHub构建和下载指南

## 结论

成功实现了儿童安全座椅设计方案的结构化输出功能，严格按照用户需求的格式进行展示。代码结构清晰，易于维护和扩展。

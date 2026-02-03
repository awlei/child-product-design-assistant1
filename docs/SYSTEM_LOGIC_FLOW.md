# 儿童产品设计助手 - 整体运行逻辑文档

## 一、系统架构概览

### 1.1 应用层级结构
```
┌─────────────────────────────────────────────────────┐
│                   Presentation Layer                │
│              (UI 层 - Compose Screens)              │
├─────────────────────────────────────────────────────┤
│                    ViewModels                       │
│             (MainViewModel - 状态管理)              │
├─────────────────────────────────────────────────────┤
│                   Business Layer                    │
│    (Services - CreativeService, SafetyService...)  │
├─────────────────────────────────────────────────────┤
│                     Model Layer                     │
│      (Data Models - CreativeIdea, ProductType...)  │
├─────────────────────────────────────────────────────┤
│                  Utilities Layer                    │
│  (Formatters, Validators, Generators...)           │
└─────────────────────────────────────────────────────┘
```

### 1.2 核心模块
1. **创意生成模块** (`CreativeService`)
2. **安全检查模块** (`SafetyService`)
3. **技术建议模块** (`TechnicalAnalysisEngine`)
4. **文档生成模块** (`DocumentService`)
5. **测试矩阵模块** (`TestMatrixGenerator`)
6. **设计建议模块** (`DesignSuggestionService`)
7. **竞品参考模块** (`CompetitorAnalysisService`)

---

## 二、完整运行流程

### 2.1 核心流程：创意生成（CreativeScreen）

```
┌─────────────────────────────────────────────────────────────┐
│                        输入阶段                              │
└─────────────────────────────────────────────────────────────┘

用户操作：
├─ 选择产品类型（儿童安全座椅、婴儿推车、儿童高脚椅、儿童床）
├─ 选择适用标准（ECE R129、FMVSS 213、GB标准等）
├─ 输入参数范围（身高、体重、年龄）
│  ├─ 最小值输入框
│  ├─ 最大值输入框
│  └─ 单位切换（kg/lb、cm/inch）
└─ 输入设计主题（可选）

输入验证：
├─ ProductTypeConfigManager.validateHeightRange()
├─ ProductTypeConfigManager.validateWeightRange()
├─ ProductTypeConfigManager.validateAgeRange()
└─ 返回 ParamInputResult（是否有效 + 匹配区间 + 错误提示）


┌─────────────────────────────────────────────────────────────┐
│                     数据库查询阶段                            │
└─────────────────────────────────────────────────────────────┘

本地配置读取：
├─ ProductTypeConfigManager.getProductTypeConfig()
│  ├─ 读取产品类型配置（硬编码数据）
│  ├─ 读取标准配置列表（ECE R129、FMVSS 213等）
│  ├─ 读取参数校验规则（minValue、maxValue、intervals）
│  └─ 读取核心要求（coreRequirements）
│
├─ ProductTypeConfigManager.validateHeightRange()
│  ├─ 根据输入身高匹配对应的假人类型（Q0-Q10）
│  ├─ 查询身高-假人映射表（HEIGHT_TO_DUMMY_MAPPING）
│  ├─ 查询年龄段映射（HEIGHT_AGE_MAPPING_CONFIG）
│  └─ 返回匹配的年龄段和假人类型
│
└─ 返回校验结果（ParamInputResult）


┌─────────────────────────────────────────────────────────────┐
│                   大模型生成阶段                              │
└─────────────────────────────────────────────────────────────┘

生成 CreativeIdea 对象：
├─ CreativeService.generateCreativeIdea()
│  ├─ 1. 生成基础信息
│  │  ├─ 生成标题（generateTitle）
│  │  ├─ 生成描述（generateDescription）
│  │  ├─ 获取主题（ageGroupThemes[ageGroup]）
│  │  └─ 获取特征（productTypeFeatures[productType]）
│  │
│  ├─ 2. 生成合规参数（关键步骤）
│  │  ├─ CrashTestDummy.getByAgeGroup(ageGroup)
│  │  ├─ ComplianceParameters.getByDummy(dummyType)
│  │  │  ├─ 低龄段（Q0-Q1.5）：
│  │  │  │  ├─ HIC ≤ 390
│  │  │  │  ├─ 胸部加速度 ≤ 55g
│  │  │  │  ├─ 颈部张力 ≤ 1800N
│  │  │  │  ├─ 颈部压缩 ≤ 2200N
│  │  │  │  └─ 头部位移 ≤ 550mm
│  │  │  │
│  │  │  ├─ 高龄段（Q3-Q10）：
│  │  │  │  ├─ HIC ≤ 1000
│  │  │  │  ├─ 胸部加速度 ≤ 60g
│  │  │  │  ├─ 颈部张力 ≤ 2000N
│  │  │  │  ├─ 颈部压缩 ≤ 2500N
│  │  │  │  └─ 头部位移 ≤ 550mm
│  │  │  │
│  │  │  └─ 返回 ComplianceParameters 对象
│  │  │
│  ├─ 3. 生成标准关联
│  │  ├─ StandardsReference.getDefaultForProductType(productType)
│  │  │  ├─ 儿童安全座椅：
│  │  │  │  ├─ 主标准：ECE R129 + GB 27887-2024 + FMVSS 213
│  │  │  │  ├─ 关键条款：ECE R129 §5.2、§7.1.2、§7.1.3等
│  │  │  │  ├─ 合规要求：身高-假人映射、动态测试要求、材料要求
│  │  │  │  └─ 假人类型：Q0、Q0+、Q1、Q1.5、Q3、Q3s、Q6、Q10
│  │  │  │
│  │  │  ├─ 婴儿推车：
│  │  │  │  ├─ 主标准：EN 1888-2:2018 + GB 14748-2020
│  │  │  │  ├─ 关键条款：稳定性、制动系统、锁定装置等
│  │  │  │  └─ 合规要求：制动力、侧翻角度、手柄强度等
│  │  │  │
│  │  │  └─ 其他产品类型...
│  │  │
│  ├─ 4. 生成材质规格
│  │  ├─ MaterialSpecs.getDefaultForProductType(productType)
│  │  │  ├─ 儿童安全座椅：
│  │  │  │  ├─ 阻燃面料（FMVSS 302认证）
│  │  │  │  ├─ ISOFIX连接件（抗拉强度≥450MPa）
│  │  │  │  ├─ 吸能材料（EPP/EPS，密度30-50kg/m³）
│  │  │  │  └─ 附加规格（安全带、调节机构、靠背骨架）
│  │  │  │
│  │  │  └─ 其他产品类型...
│  │  │
│  └─ 5. 返回 CreativeIdea 对象
│     ├─ id: UUID
│     ├─ title: 设计方案标题
│     ├─ description: 设计方案描述
│     ├─ ageGroup: 年龄段（0-3岁、3-6岁等）
│     ├─ productType: 产品类型
│     ├─ theme: 设计主题
│     ├─ features: 核心特性列表
│     ├─ materials: 推荐材料列表
│     ├─ colorPalette: 色彩方案
│     ├─ safetyNotes: 安全注意事项
│     ├─ complianceParameters: 合规参数 ⭐
│     ├─ standardsReference: 标准关联 ⭐
│     └─ materialSpecs: 材质规格 ⭐


┌─────────────────────────────────────────────────────────────┐
│                   网络连接阶段（可选）                         │
└─────────────────────────────────────────────────────────────┘

联网搜索（当前版本未启用，预留接口）：
├─ WebSearchTool.search(query)
│  ├─ 搜索最新的标准更新
│  ├─ 搜索竞品信息
│  ├─ 搜索市场趋势
│  └─ 返回搜索结果
│
└─ 注：当前版本使用本地数据库（硬编码配置），不依赖网络


┌─────────────────────────────────────────────────────────────┐
│                    数据处理阶段                              │
└─────────────────────────────────────────────────────────────┘

1. 格式化输出（DesignSchemeFormatter）
├─ formatCreativeIdea(idea: CreativeIdea)
│  ├─ 格式化基本信息（产品类型、年龄段、设计主题）
│  ├─ 格式化核心设计特点（易安装性、舒适性、材质环保、安全性）
│  ├─ 格式化推荐材料
│  ├─ 格式化合规参数
│  │  ├─ 对应标准
│  │  ├─ 适配假人
│  │  └─ 安全阈值（HIC、胸部加速度、颈部张力、头部位移等）
│  └─ 格式化安全注意事项
│
├─ formatToCardData(idea: CreativeIdea)
│  ├─ 转换为卡片展示格式
│  ├─ basicInfo: List<Pair<String, String>>
│  ├─ coreFeatures: List<String>
│  ├─ recommendedMaterials: List<String>
│  ├─ complianceParams: ComplianceParamsData
│  └─ safetyNotes: List<String>
│
└─ formatCreativeIdeaByHeight(minHeight, maxHeight, idea)
   ├─ 根据身高范围格式化
   ├─ 获取身高映射的年龄段信息
   └─ 生成包含精准假人信息的方案


2. 输出合规检查（OutputComplianceChecker）
├─ checkAndFixOutput(rawOutput: String, context: Map<String, Any>)
│  ├─ 检查乱码问题
│  ├─ 检查参数错误
│  ├─ 检查排版杂乱
│  └─ 返回修正后的输出
│
└─ ensureOutputCompliance(output: String)


3. 动态参数生成（StructuredDesignOutput）
├─ getDummyCoverage(minHeight, maxHeight)
│  └─ 根据身高范围返回假人覆盖：Q0→Q10全假人
│
├─ getAgeSegments(ageGroup)
│  └─ 返回年龄段分段：0-12岁（分6段）
│
├─ getInstallationDirection(heightRange)
│  └─ 返回安装方向：身高≤105cm后向，≥105cm正向
│
├─ getHeadrestAdjustment(heightRange)
│  └─ 返回头枕调节：12档（适配40cm→150cm身高）
│
├─ getSeatWidth(heightRange)
│  └─ 返回座宽：分段适配（280mm→520mm）
│
├─ getBackrestDepth(heightRange)
│  └─ 返回靠背深度：350mm（Q0）→ 650mm（Q10）
│
└─ getSideProtection(heightRange)
   └─ 返回侧防结构：可调节防护面积（0.6㎡→0.9㎡）


┌─────────────────────────────────────────────────────────────┐
│                      输出结果展示                            │
└─────────────────────────────────────────────────────────────┘

UI 展示（DesignOutputTree 组件）：
├─ 页面标题：儿童产品设计输出（工程师专属）
│
├─ 产品类型卡片（根据当前选中的产品类型显示）
│  ├─ 标题：全年龄段儿童安全座椅（40-150cm）
│  ├─ 标签：核心产品
│  ├─ 图标：🪑
│  ├─ 展开/收起状态：默认展开
│  └─ 展开内容：
│     │
│     ├─ 📊 基础适配数据（匹配GPS-028全假人）
│     │  ├─ 假人覆盖：Q0（40-60cm）→ Q10（145-150cm）全假人
│     │  ├─ 适配年龄：0-12岁（分6段：0-1/1-2/2-3/3-4/4-6/6-12岁）
│     │  ├─ 身高范围：40-150cm
│     │  └─ 安装方向：身高≤105cm（4岁前）强制后向，≥105cm正向
│     │
│     ├─ 📏 核心设计参数（来自GPS-028 Dummies表）
│     │  ├─ 头枕调节：12档（适配40cm→150cm身高，调节范围300-600mm）
│     │  ├─ 座宽：分段适配（280mm→520mm，随假人肩宽递增）
│     │  ├─ 靠背深度：350mm（Q0）→ 650mm（Q10）
│     │  └─ 侧防结构：可调节防护面积（0.6㎡→0.9㎡，匹配不同年龄段）
│     │
│     ├─ ⚖️ 合规约束（对应ECE R129/GB 27887-2024）
│     │  ├─ 低龄段（Q0-Q1.5）：HIC≤390、胸部加速度≤55g
│     │  ├─ 高龄段（Q3-Q10）：HIC≤1000、侧撞胸部压缩量≤44mm
│     │  └─ 安装系统：ISOFIX+支撑腿/Top-tether（双三角固定）
│     │
│     ├─ 🧪 材料选型（带性能指标）
│     │  ├─ 主体框架：食品级PP（抗冲击强度≥20kJ/m²，耐温-30~80℃）
│     │  ├─ 填充层：Cobra记忆棉（压缩回弹率≥90%）
│     │  └─ 织带：高强度尼龙（断裂强度≥11000N）
│     │
│     └─ ✅ 安全验证项
│        ├─ 动态碰撞：正向50km/h、后向30km/h、侧向32km/h
│        ├─ 材料检测：REACH 118项、EN 71阻燃
│        └─ 耐久测试：调节机构≥1000次循环无故障
│
└─ 附加工具
   ├─ 🔍 假人分段参数表（可展开弹窗）
   │  ├─ Q0：40-50cm，2.5kg，HIC≤390
   │  ├─ Q0+：50-60cm，4.0kg，HIC≤390
   │  ├─ Q1：60-75cm，9.0kg，HIC≤390
   │  ├─ Q1.5：75-87cm，11.0kg，HIC≤570
   │  ├─ Q3：87-105cm，15.0kg，HIC≤1000
   │  ├─ Q3s：105-125cm，21.0kg，HIC≤1000
   │  ├─ Q6：125-145cm，33.0kg，HIC≤1000
   │  └─ Q10：145-150cm，38.0kg，HIC≤1000
   │
   └─ 📄 导出PDF（待实现）
      ├─ 生成PDF报告
      ├─ 包含GPS-028数据追溯
      └─ 包含标准条款引用


┌─────────────────────────────────────────────────────────────┐
│                      其他核心模块                            │
└─────────────────────────────────────────────────────────────┘

1. 一键生成全维度方案（OneClickGenerationScreen）
├─ 输入：身高范围、产品类型、标准
├─ 处理：
│  ├─ 生成创意（generateCreativeIdea）
│  ├─ 生成安全检查（performSafetyCheck）
│  ├─ 生成测试矩阵（TestMatrixGenerator）
│  ├─ 生成设计建议（DesignSuggestionService）
│  ├─ 生成竞品参考（CompetitorAnalysisService）
│  └─ 生成综合报告（IntegratedReportScreen）
└─ 输出：完整的多维度方案


2. 安全检查模块（SafetyService）
├─ 输入：产品名称、年龄段
├─ 处理：
│  ├─ 检查吞咽风险
│  ├─ 检查材质安全
│  ├─ 检查边缘处理
│  ├─ 检查结构稳定性
│  └─ 计算安全得分
└─ 输出：SafetyCheck 对象（得分、检查项、建议）


3. 测试矩阵模块（TestMatrixGenerator）
├─ 输入：身高范围、产品类型、标准
├─ 处理：
│  ├─ 生成正向测试项
│  ├─ 生成后向测试项
│  ├─ 生成侧向测试项
│  ├─ 关联假人类型
│  └─ 关联标准条款
└─ 输出：测试矩阵（TestMatrix 对象）


4. 设计建议模块（DesignSuggestionService）
├─ 输入：CreativeIdea、身高范围、标准
├─ 处理：
│  ├─ 分析设计特点
│  ├─ 提供结构优化建议
│  ├─ 提供材料优化建议
│  ├─ 提供安全优化建议
│  └─ 提供成本优化建议
└─ 输出：设计建议列表


5. 竞品参考模块（CompetitorAnalysisService）
├─ 输入：产品类型、年龄段
├─ 处理：
│  ├─ 搜索竞品信息（本地数据库）
│  ├─ 分析竞品特点
│  ├─ 提取竞品优势
│  └─ 提供参考建议
└─ 输出：竞品参考列表


6. 文档学习模块（DocumentLearningScreen）
├─ 功能：学习标准文档
├─ 支持：ECE R129、FMVSS 213、GB标准等
├─ 特性：
│  ├─ 文档预览
│  ├─ 关键条款高亮
│  ├─ 条款跳转
│  └─ 标注功能
└─ 输出：学习进度、笔记


7. 智能问答模块（ChatQAScreen）
├─ 功能：智能问答
├─ 支持：
│  ├─ 标准条款解释
│  ├─ 设计参数查询
│  ├─ 合规要求咨询
│  └─ 技术问题解答
├─ 处理：
│  ├─ 查询本地知识库
│  ├─ 调用大模型（集成 doubao-seed）
│  └─ 返回答案
└─ 输出：问答结果


8. 合规管理模块（ComplianceManagementScreen）
├─ 功能：合规检查和管理
├─ 支持：
│  ├─ 产品合规性检查
│  ├─ 标准符合性验证
│  ├─ 合规报告生成
│  └─ 合规问题追踪
└─ 输出：合规报告、问题列表


┌─────────────────────────────────────────────────────────────┐
│                      数据流向图                              │
└─────────────────────────────────────────────────────────────┘

用户输入
   ↓
参数验证（ProductTypeConfigManager）
   ↓
数据查询（本地配置）
   ↓
CreativeService 生成 CreativeIdea
   ↓
数据格式化（DesignSchemeFormatter）
   ↓
输出合规检查（OutputComplianceChecker）
   ↓
动态参数生成（StructuredDesignOutput 辅助函数）
   ↓
UI 展示（DesignOutputTree 组件）
   ↓
用户交互（查看详情、展开/收起、导出等）


┌─────────────────────────────────────────────────────────────┐
│                    关键数据模型                              │
└─────────────────────────────────────────────────────────────┘

1. CreativeIdea（创意方案）
├─ 基础信息：id、title、description、ageGroup、productType
├─ 设计属性：theme、features、materials、colorPalette
├─ 合规数据：
│  ├─ complianceParameters：假人类型、安全阈值
│  ├─ standardsReference：标准条款、合规要求
│  └─ materialSpecs：材质规格、性能指标
└─ 安全信息：safetyNotes


2. ComplianceParameters（合规参数）
├─ dummyType：假人类型（Q0-Q10）
├─ hicLimit：HIC极限值（390/570/1000）
├─ chestAccelerationLimit：胸部加速度极限（55g/60g）
├─ neckTensionLimit：颈部张力极限（1800N/2000N）
├─ neckCompressionLimit：颈部压缩极限（2200N/2500N）
├─ headExcursionLimit：头部位移极限（550mm）
├─ kneeExcursionLimit：膝部位移极限（650mm）
└─ chestDeflectionLimit：胸部位移极限（45mm/52mm）


3. StandardsReference（标准关联）
├─ mainStandard：主标准名称
├─ keyClauses：关键条款列表
├─ complianceRequirements：合规要求列表
└─ dummyTypes：适用的假人类型列表


4. ProductTypeConfig（产品类型配置）
├─ productTypeId：产品类型ID
├─ productTypeName：产品类型名称
└─ standards：标准配置列表
   ├─ standardId：标准ID
   ├─ standardName：标准名称
   ├─ region：地区标识（国内/国际）
   ├─ inputItem：输入项配置
   ├─ paramRule：参数校验规则
   └─ coreRequirements：核心要求


5. CrashTestDummy（碰撞测试假人）
├─ name：假人名称（Q0、Q1、Q1.5、Q3、Q3s、Q6、Q10）
├─ ageRange：年龄范围
├─ weightRange：体重范围
├─ heightRange：身高范围
└─ category：假人分类


┌─────────────────────────────────────────────────────────────┐
│                    性能优化策略                              │
└─────────────────────────────────────────────────────────────┘

1. 状态管理优化
├─ 使用 StateFlow 实现响应式数据流
├─ 使用 remember 避免不必要的重组
├─ 使用 derivedStateOf 优化派生状态
└─ 使用 LaunchedEffect 处理副作用


2. 数据加载优化
├─ 使用协程（viewModelScope.launch）处理异步操作
├─ 使用 withContext(Dispatchers.IO) 在后台线程处理耗时操作
├─ 缓存常用数据（productTypeThemes、productTypeFeatures等）
└─ 延迟加载（LazyColumn、LazyRow）


3. UI 渲染优化
├─ 使用 AnimatedContent 实现流畅的页面切换
├─ 使用 AnimatedVisibility 实现展开/收起动画
├─ 使用 LazyColumn 优化长列表渲染
└─ 使用 CardDefaults 减少不必要的重绘


4. 内存优化
├─ 避免内存泄漏（使用 remember 保存状态）
├─ 及时释放资源（在 onDispose 中清理）
├─ 使用数据类（data class）减少对象创建
└─ 复用组件（ProductTypeCard、SectionBlock等）


┌─────────────────────────────────────────────────────────────┐
│                    扩展性设计                                │
└─────────────────────────────────────────────────────────────┘

1. 大模型集成预留
├─ 集成 doubao-seed（豆包大模型）
├─ 支持流式输出
├─ 支持思考模式（thinking）
└─ 支持自定义系统提示词


2. 网络搜索预留
├─ WebSearchTool 接口
├─ 支持多种搜索模式（web、image）
├─ 支持搜索结果总结
└─ 支持自定义搜索参数


3. 数据库扩展预留
├─ 当前使用硬编码配置
├─ 预留本地数据库接口（Room）
├─ 预留远程数据库接口（Firebase）
└─ 支持数据同步


4. 文件导出扩展
├─ PDF 导出（待实现）
├─ Excel 导出（待实现）
├─ Word 导出（待实现）
└─ 自定义格式导出


┌─────────────────────────────────────────────────────────────┐
│                    错误处理机制                              │
└─────────────────────────────────────────────────────────────┘

1. 输入验证错误
├─ 参数范围超限 → 显示错误提示
├─ 参数格式错误 → 显示格式说明
├─ 缺少必填项 → 显示必填提示
└─ 验证失败 → 阻止提交


2. 生成错误
├─ 配置缺失 → 使用默认配置
├─ 假人类型无法匹配 → 使用最接近的假人类型
├─ 标准关联失败 → 使用通用标准
└─ 材质规格缺失 → 使用通用材质


3. UI 渲染错误
├─ 数据为空 → 显示空状态提示
├─ 加载失败 → 显示错误页面
├─ 网络错误 → 显示重试按钮
└─ 超时错误 → 显示超时提示


4. 异常捕获
├─ try-catch 捕获所有异常
├─ 日志记录错误信息
├─ 友好的错误提示
└─ 错误上报（可选）


┌─────────────────────────────────────────────────────────────┐
│                    使用示例                                  │
└─────────────────────────────────────────────────────────────┘

示例1：生成全年龄段儿童安全座椅方案
┌─────────────────────────────────────────────────────────────┐
│ 步骤1：用户输入                                              │
├─ 产品类型：儿童安全座椅                                      │
├─ 适用标准：ECE R129（欧标）                                  │
├─ 身高范围：40-150cm                                         │
└─ 设计主题：全年龄段通用设计                                  │
│
│ 步骤2：参数验证                                              │
├─ ProductTypeConfigManager.validateHeightRange(40, 150)       │
├─ 返回：ParamInputResult(                                    │
│    isValid = true,                                          │
│    matchedIntervals = [Q0, Q1, Q1.5, Q3, Q3s, Q6, Q10],   │
│    ageGroup = AgeGroup.ALL                                  │
│ )                                                           │
│
│ 步骤3：生成 CreativeIdea                                    │
├─ CreativeService.generateCreativeIdea(                      │
│    AgeGroup.ALL,                                            │
│    ProductType.SAFETY_SEAT,                                │
│    "全年龄段通用设计"                                       │
│ )                                                           │
├─ 返回：CreativeIdea(                                       │
│    ageGroup = AgeGroup.ALL (0-12岁, 40-150cm, 2.5-38kg),   │
│    productType = ProductType.SAFETY_SEAT,                   │
│    complianceParameters = ComplianceParameters(              │
│        dummyType = ComplianceDummy.Q10,                     │
│        hicLimit = 1000,                                     │
│        chestAccelerationLimit = 60,                         │
│        neckTensionLimit = 2000,                             │
│        ...                                                  │
│    ),                                                       │
│    standardsReference = StandardsReference(...),            │
│    materialSpecs = MaterialSpecs(...),                      │
│    ...                                                      │
│ )                                                           │
│
│ 步骤4：格式化输出                                            │
├─ DesignOutputTree(creativeIdea = idea)                     │
├─ 动态生成设计参数：                                         │
│    ├─ getDummyCoverage(40, 150) → "Q0（40-60cm）→Q10（145-150cm）全假人" │
│    ├─ getAgeSegments(AgeGroup.ALL) → "0-12岁（分6段：0-1/1-2/2-3/3-4/4-6/6-12岁）" │
│    ├─ getInstallationDirection("40-150cm") → "身高≤105cm（4岁前）强制后向，≥105cm正向" │
│    ├─ getHeadrestAdjustment("40-150cm") → "12档（适配40cm→150cm身高，调节范围300-600mm）" │
│    ├─ getSeatWidth("40-150cm") → "分段适配（280mm→520mm，随假人肩宽递增）" │
│    ├─ getBackrestDepth("40-150cm") → "350mm（Q0）→650mm（Q10）" │
│    └─ getSideProtection("40-150cm") → "可调节防护面积（0.6㎡→0.9㎡，匹配不同年龄段）" │
│
│ 步骤5：UI 展示                                              │
└─ 显示层级树状结构的设计方案输出                               │
└─────────────────────────────────────────────────────────────┘


示例2：生成特定年龄段儿童安全座椅方案
┌─────────────────────────────────────────────────────────────┐
│ 步骤1：用户输入                                              │
├─ 产品类型：儿童安全座椅                                      │
├─ 适用标准：ECE R129（欧标）                                  │
├─ 身高范围：87-105cm（Q3假人）                               │
└─ 设计主题：学前儿童专属设计                                   │
│
│ 步骤2：参数验证                                              │
├─ ProductTypeConfigManager.validateHeightRange(87, 105)      │
├─ 返回：ParamInputResult(                                    │
│    isValid = true,                                          │
│    matchedIntervals = [Q3],                                │
│    ageGroup = AgeGroup.PRESCHOOL (6-9岁)                    │
│ )                                                           │
│
│ 步骤3：生成 CreativeIdea                                    │
├─ CreativeService.generateCreativeIdea(                      │
│    AgeGroup.PRESCHOOL,                                      │
│    ProductType.SAFETY_SEAT,                                │
│    "学前儿童专属设计"                                       │
│ )                                                           │
├─ 返回：CreativeIdea(                                       │
│    ageGroup = AgeGroup.PRESCHOOL (6-9岁, 105-125cm, 15-21kg), │
│    productType = ProductType.SAFETY_SEAT,                   │
│    complianceParameters = ComplianceParameters(              │
│        dummyType = ComplianceDummy.Q3,                      │
│        hicLimit = 1000,                                     │
│        chestAccelerationLimit = 60,                         │
│        neckTensionLimit = 2000,                             │
│        ...                                                  │
│    ),                                                       │
│    standardsReference = StandardsReference(...),            │
│    materialSpecs = MaterialSpecs(...),                      │
│    ...                                                      │
│ )                                                           │
│
│ 步骤4：格式化输出                                            │
├─ DesignOutputTree(creativeIdea = idea)                     │
├─ 动态生成设计参数：                                         │
│    ├─ getDummyCoverage(87, 105) → "Q3（87-105cm）学前儿童假人" │
│    ├─ getAgeSegments(AgeGroup.PRESCHOOL) → "6-9岁" │
│    ├─ getInstallationDirection("87-105cm") → "身高≤105cm（4岁前）强制后向" │
│    ├─ getHeadrestAdjustment("87-105cm") → "8档（适配87cm→105cm身高，调节范围400-500mm）" │
│    ├─ getSeatWidth("87-105cm") → "分段适配（440mm→460mm，学前儿童肩宽）" │
│    ├─ getBackrestDepth("87-105cm") → "550mm（Q3）" │
│    └─ getSideProtection("87-105cm") → "可调节防护面积（0.7㎡，学前儿童）" │
│
│ 步骤5：UI 展示                                              │
└─ 显示针对Q3假人的儿童安全座椅设计方案输出                       │
└─────────────────────────────────────────────────────────────┘


┌─────────────────────────────────────────────────────────────┐
│                    技术栈总结                              │
└─────────────────────────────────────────────────────────────┘

前端：
├─ UI 框架：Jetpack Compose
├─ 状态管理：StateFlow + ViewModel
├─ 导航：Scaffold + BottomNavigation
├─ 动画：AnimatedContent + AnimatedVisibility
└─ 主题：Material 3

业务逻辑：
├─ 异步处理：Kotlin Coroutines
├─ 数据流：StateFlow / MutableStateFlow
├─ 依赖注入：ViewModel Factory
└─ 错误处理：try-catch + UiState

数据处理：
├─ 数据模型：Data Classes
├─ 配置管理：硬编码配置（ProductTypeConfig）
├─ 参数验证：Validator Classes
├─ 格式化：Formatter Classes
└─ 合规检查：Checker Classes

集成：
├─ 大模型：doubao-seed（豆包）
├─ 网络搜索：WebSearchTool（预留）
├─ 数据库：Room（预留）
├─ 文件导出：PDF/Excel（待实现）
└─ 持久化：SharedPreferences（预留）


┌─────────────────────────────────────────────────────────────┐
│                    总结                                    │
└─────────────────────────────────────────────────────────────┘

儿童产品设计助手的整体运行逻辑遵循以下模式：

1. **输入阶段**：用户通过 UI 界面输入产品类型、标准、参数范围
2. **数据查询阶段**：从本地配置（硬编码数据库）读取产品类型配置、标准配置、参数校验规则
3. **大模型生成阶段**：CreativeService 根据输入生成 CreativeIdea 对象，包含合规参数、标准关联、材质规格
4. **数据处理阶段**：格式化输出、合规检查、动态参数生成
5. **输出展示阶段**：通过 DesignOutputTree 组件展示层级树状结构的设计方案

整个系统采用本地数据库 + 大模型生成的方式，确保了：
- ✅ 快速响应（本地配置查询）
- ✅ 数据准确（基于标准合规参数）
- ✅ 灵活扩展（支持多种产品类型和标准）
- ✅ 用户友好（层级树状结构输出，视觉清晰）
- ✅ 专业可靠（基于GPS-028数据库、ECE R129标准）

---
文档版本：v1.0
最后更新：2026-02-03
维护者：Coze Coding

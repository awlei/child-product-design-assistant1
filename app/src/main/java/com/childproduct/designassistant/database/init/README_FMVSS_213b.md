# FMVSS 213b (2026版) 数据库集成文档

## 概述

本文档说明如何在儿童产品设计助手中集成FMVSS 213b（2026版）数据库数据。

## 新增文件

### 1. Fmvss213bDataInitializer.kt
**功能**: FMVSS 213b数据初始化器

**主要方法**:
- `initialize()`: 初始化FMVSS 213b所有数据
- `insertStandard()`: 插入标准信息
- `insertDummies()`: 插入假人数据
- `insertSafetyThresholds()`: 插入安全阈值数据
- `insertTestConfigurations()`: 插入测试配置数据

**支持的假人类型**:
- HIII-3YO: 3岁儿童假人（正碰）
- HIII-6YO: 6岁儿童假人（正碰，替代Hybrid II 6YO）
- HIII-10YO: 10岁儿童假人（正碰）
- Q3S: 侧碰假人（侧碰）

**支持的测试类型**:
- Type 1: 正碰（仅腰带）
- Type 2: 正碰（腰带+肩带，213b新增）
- 侧碰: 侧面碰撞

### 2. Fmvss213bUsageExample.kt
**功能**: FMVSS 213b数据使用示例

**示例场景**:
1. 初始化FMVSS 213b数据
2. 查询FMVSS 213b所有假人
3. 查询正碰测试假人
4. 查询特定假人的安全阈值
5. 查询Type 2测试配置
6. 查询侧碰测试配置
7. 查询所有正碰测试配置
8. 获取FMVSS 213b标准信息
9. 综合查询 - 检查产品是否符合FMVSS 213b要求
10. 产品设计建议查询

### 3. Fmvss213bDataValidator.kt
**功能**: FMVSS 213b数据验证器

**验证内容**:
- 标准信息验证
- 假人数据验证
- 安全阈值验证
- 测试配置验证

**验证结果**:
- ValidationResult: 包含验证状态和问题列表
- ValidationIssue: 验证问题（错误/警告/信息）
- ValidationIssueType: 问题类型（ERROR/WARNING/INFO）

### 4. Fmvss213bIntegrationGuide.kt
**功能**: FMVSS 213b数据集成指南

**包含内容**:
- 集成步骤
- 数据使用场景
- 数据结构说明
- 关键特性说明
- 数据验证方法
- 常见问题解答

## 修改的文件

### FMVSSDao.kt
**新增查询方法**:
- `getFmvss213bDummies()`: 获取FMVSS 213b所有假人
- `getFmvss213bDummiesByScenario(scenario: String)`: 获取特定测试场景的假人
- `getFmvss213bThresholdsByDummy(dummyCode: String)`: 获取特定假人的安全阈值
- `getFmvss213bType2TestConfig()`: 获取Type 2测试配置
- `getFmvss213bSideImpactConfig()`: 获取侧碰测试配置
- `getFmvss213bFrontImpactConfigs()`: 获取正碰测试配置
- `getFmvss213bStandard()`: 获取FMVSS 213b标准信息

## 数据结构

### 标准信息 (FMVSSStandardEntity)
- 标准ID: `FMVSS-213b-2026`
- 标准名称: `FMVSS 213b (2026版)`
- 适用地区: `美国`
- 适用体重: `10kg+（提篮至增高座）`
- 适用年龄: `新生儿至12岁`
- 核心范围: `儿童约束系统正面碰撞、侧面碰撞安全性能测试`
- 生效日期: `2026-01-01`
- 标准状态: `即将生效`

### 假人数据 (FMVSSDummyEntity)

| 假人代码 | 显示名称 | 体重 | 年龄 | 适用场景 |
|---------|---------|------|------|---------|
| HIII-3YO | Hybrid III 3岁儿童假人 | 15.0kg | 3岁 | 正碰 |
| HIII-6YO | Hybrid III 6岁儿童假人 | 20.4kg | 6岁 | 正碰 |
| HIII-10YO | Hybrid III 10岁儿童假人 | 30.0kg | 10岁 | 正碰 |
| Q3S | Q3S侧碰假人 | 15.0kg | 3岁 | 侧碰 |

### 安全阈值 (FMVSSThresholdEntity)

#### 正碰阈值（HIII-3YO, HIII-6YO, HIII-10YO）
- HIC: 1000 HIC（0-175ms窗口）
- 胸部加速度: 60g（3ms持续时间）
- 头部位移: 813mm

#### 侧碰阈值（Q3S）
- HIC: 570 HIC
- 胸部压缩量: 23mm
- 头部接触: 禁碰门板

### 测试配置 (FMVSSTestConfigEntity)

#### Type 1 正碰测试
- 速度: 48±3.2 km/h (30±2 mph)
- 安装方式: 安全带（仅腰带）
- 腰带张力: 53.5-67N

#### Type 2 正碰测试（213b新增）
- 速度: 48±3.2 km/h (30±2 mph)
- 安装方式: 安全带（腰带+肩带）
- 腰带张力: 53.5-67N
- 肩带张力: 9-18N
- Type 2锚点: 必需

#### 侧碰测试
- 速度: 32±2 km/h (20±1.2 mph)
- 测试假人: Q3S
- 测试台: SISA

## 使用方法

### 1. 初始化数据

在Application类中初始化FMVSS 213b数据：

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // 初始化FMVSS 213b数据
        CoroutineScope(Dispatchers.IO).launch {
            val database = FMVSSDatabase.getInstance(applicationContext)
            val dao = database.fmvssDao()
            val initializer = Fmvss213bDataInitializer(applicationContext, dao)
            initializer.initialize()
        }
    }
}
```

### 2. 查询数据

在ViewModel中查询FMVSS 213b数据：

```kotlin
class DesignProposalViewModel : ViewModel() {
    private val database = FMVSSDatabase.getInstance(context)
    private val dao = database.fmvssDao()
    
    fun getFmvss213bRequirements() {
        viewModelScope.launch {
            // 获取FMVSS 213b标准信息
            val standard = dao.getFmvss213bStandard()
            
            // 获取适用的假人
            val dummies = dao.getFmvss213bDummies()
            
            // 获取测试配置
            val type2Config = dao.getFmvss213bType2TestConfig()
        }
    }
}
```

### 3. 验证数据

使用验证器检查数据完整性：

```kotlin
val validator = Fmvss213bDataValidator(dao)
val result = validator.validate()
validator.printReport(result)
```

## FMVSS 213b关键特性

### 1. 禁用Hybrid II 6YO假人
FMVSS 213b要求使用HIII-6YO替代Hybrid II 6YO假人，提供更真实的儿童损伤响应。

### 2. 新增Type 2测试
Type 2测试使用腰带+肩带，适用于具有Type 2锚点的产品。

### 3. 侧碰测试要求
- 使用Q3S假人
- HIC限值570
- 胸部压缩量限值23mm
- 头部禁碰门板

### 4. HIC窗口时间
- 正碰测试HIC窗口: 0-175ms
- 侧碰测试HIC窗口: 完整信号

## 常见问题

### Q1: FMVSS 213b何时生效？
A: FMVSS 213b预计于2026年1月1日生效。

### Q2: Hybrid II 6YO假人还能使用吗？
A: 不能，FMVSS 213b要求使用HIII-6YO替代Hybrid II 6YO。

### Q3: Type 2测试和Type 1测试有什么区别？
A: Type 1测试仅使用腰带，Type 2测试使用腰带+肩带。

### Q4: 侧碰测试使用什么假人？
A: 侧碰测试使用Q3S假人。

### Q5: HIC限值是多少？
A: 正碰测试HIC限值为1000（0-175ms窗口），侧碰测试HIC限值为570。

## 相关资源

- [FMVSS 213b官方文档](https://www.nhtsa.gov/laws-regulations/federal-motor-vehicle-safety-standards)
- [FMVSS数据库设计文档](./README.md)
- [Room数据库文档](https://developer.android.com/training/data-storage/room)

## 更新日志

### 2025-02-05
- 新增FMVSS 213b数据初始化器
- 新增FMVSS 213b数据使用示例
- 新增FMVSS 213b数据验证器
- 新增FMVSS 213b集成指南
- 扩展FMVSSDao，新增FMVSS 213b特定查询方法
- 添加FMVSS 213b数据库集成文档

## 维护者

- Coze Coding Team

## 许可证

- 与项目主许可证保持一致

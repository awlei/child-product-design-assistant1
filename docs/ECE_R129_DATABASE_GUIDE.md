# ECE R129 标准数据库使用指南

## 概述

本数据库系统基于UN R129 Rev.4标准，提供完整的儿童安全座椅设计数据支持，包括假人类型、安全阈值、测试配置等。

## 数据库架构

### 实体模型

1. **CrashTestDummy** - 假人类型实体
   - 8种标准假人（Q0/Q0+/Q1/Q1.5/Q3/Q3s/Q6/Q10）
   - 覆盖40-150cm身高范围
   - 包含详细的生理参数（重量、头围、肩宽、坐高）

2. **SafetyThreshold** - 安全阈值实体
   - 基于UN R129 §7.1动态测试要求
   - 包含HIC、胸部加速度、颈部张力、位移等阈值

3. **TestConfiguration** - 测试配置实体
   - 基于ROADMATE 360格式
   - 包含正面/后向/侧向碰撞测试配置
   - 完整的安装方式和测试参数

4. **StandardReference** - 标准引用实体
   - 标准版本信息
   - 生效日期和官方URL

5. **HeightRangeMapping** - 身高范围映射实体
   - 身高到假人类型的映射
   - 产品分组信息

6. **InstallationMethod** - 安装方式实体
   - ISOFIX安装方式
   - 车辆安全带安装方式

7. **MaterialSpecification** - 材料规格实体
   - 基于FMVSS 302燃烧性能要求
   - ISOFIX连接件规格

8. **IsofixRequirement** - ISOFIX要求实体
   - 基于UN R16和UN R145标准
   - ISOFIX SIZE/SIZE+/UNIVERSAL分类

9. **StandardUpdateLog** - 标准更新日志实体
   - 记录数据同步历史
   - 跟踪版本变更

## 使用方法

### 1. 初始化数据库

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化数据库（首次启动会自动创建并填充数据）
        val database = EceR129Database.getDatabase(applicationContext)
    }
}
```

### 2. 获取Repository实例

```kotlin
class DesignViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = StandardRepository.getInstance(application)
    
    // 获取所有假人类型
    val allDummies: LiveData<List<CrashTestDummy>> = repository.getAllDummies()
    
    // 根据身高获取假人
    fun getDummyByHeight(heightCm: Int) {
        viewModelScope.launch {
            val dummy = repository.getDummyByHeight(heightCm)
            // 使用假人数据...
        }
    }
}
```

### 3. 根据身高范围生成设计方案

```kotlin
class DesignViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = StandardRepository.getInstance(application)
    
    fun generateDesignScheme(minHeight: Int, maxHeight: Int) {
        viewModelScope.launch {
            // 1. 获取涉及的假人类型
            val dummies = repository.getDummiesByHeightRange(minHeight, maxHeight)
            
            // 2. 获取安全阈值
            val thresholds = dummies.flatMap { dummy ->
                val thresholdsList = repository.getSafetyThresholdsByDummy(dummy.dummyId).value ?: emptyList()
                thresholdsList
            }
            
            // 3. 获取测试配置
            val configs = repository.getTestConfigurationsByHeightRange(minHeight, maxHeight)
            
            // 4. 生成设计方案
            val scheme = DesignScheme(
                heightRange = "${minHeight}-${maxHeight} cm",
                applicableDummies = dummies.map { it.dummyName }.joinToString("/"),
                safetyThresholds = thresholds.associate { 
                    it.testItem to "${it.parameterName} ≤ ${it.maxValue}${it.unit}" 
                },
                testConfigurations = configs
            )
            
            _designScheme.postValue(scheme)
        }
    }
}
```

### 4. 查询特定假人的测试配置

```kotlin
fun getTestConfiguration(dummyCode: String) {
    viewModelScope.launch {
        // 根据假人身高决定安装方向
        val dummy = repository.getDummyByHeight(dummyCode.extractHeight()) ?: return@launch
        val direction = if (dummy.minHeightCm < 105) "REARWARD" else "FORWARD"
        
        // 获取测试配置
        repository.getTestConfigurations(dummyCode, direction).observe(viewLifecycleOwner) { configs ->
            // 显示测试配置...
        }
    }
}
```

### 5. 获取安全阈值

```kotlin
fun getSafetyThresholdsForDummy(dummyCode: String) {
    viewModelScope.launch {
        val thresholds = repository.getThresholdsByDummyCode(dummyCode)
        
        thresholds.forEach { threshold ->
            println("测试项目: ${threshold.testItem}")
            println("参数名称: ${threshold.parameterName}")
            println("阈值: ≤ ${threshold.maxValue}${threshold.unit}")
            println("标准来源: ${threshold.standardSource}")
        }
    }
}
```

## 数据示例

### Q0假人数据

```
假人代码: Q0
假人名称: 新生儿
身高范围: 40-50 cm
年龄范围: 0-6个月
产品组: Group 0+
安装方向: REARWARD
重量: 3.47 kg
头围: 360 mm
肩宽: 145 mm
坐高: 255 mm
```

### Q0安全阈值

```
测试项目: 头部伤害准则
参数名称: HIC15
阈值: ≤ 390
测试持续时间: 15ms
标准来源: UN R129 §7.1.2

测试项目: 胸部合成加速度
参数名称: ChestAcc3ms
阈值: ≤ 55 g
测试持续时间: 3ms
标准来源: UN R129 §7.1.3

测试项目: 颈部张力
参数名称: NeckTension
阈值: ≤ 1800 N
标准来源: UN R129 §7.1.4
```

### Q0测试配置

```
配置ID: CONFIG_R129_FRONTAL_Q0_REARWARD_3PTS
测试类型: 正面碰撞
测试速度: 50 km/h
安装方式: ISOFIX 3点安装
产品配置: Upright
是否使用支撑腿: With
是否允许仪表板接触: Yes
最大脉冲: 20 g
制动距离: 650 mm
```

## 标准输出规范

### ROADMATE 360格式示例

```
| # Sample | Pulse   | Impact | Dummy | Position        | Installation     | Product Config | ISOFIX Anchors | Harness | Top tether/Support leg | Dashboard | Buckle test | Adjuster test | ISOFIX test | Top tether test | Quantity | Test Number | Test Speed (km/h) | Max Pulse (g) | Stopping Distance (mm) |
|----------|---------|--------|-------|----------------|-----------------|----------------|----------------|---------|----------------------|-----------|-------------|---------------|-------------|-----------------|----------|-------------|-------------------|---------------|-----------------------|
| 1        | Frontal | Q0     | Q0    | Rearward facing | Isofix 3 pts    | Upright        | Yes            | Yes     | With                 | Yes       | No          | No            | Yes         | No              | n/a      | -           | 50.0              | 20.0          | 650                   |
```

## 注意事项

1. **标准隔离原则**：
   - 输出时严格单一标准（UN R129）
   - 不混用GB或FMVSS参数
   - 所有数据必须标注标准来源条款

2. **年龄段动态计算**：
   - 根据输入的身高范围动态计算年龄段
   - 40-105cm后向安装，105-150cm前向安装

3. **测试矩阵完整性**：
   - 必须包含所有涉及的假人测试配置
   - 正面/后向/侧向碰撞测试都要覆盖
   - Q3假人需要额外测试2点ISOFIX安装

4. **数据准确性**：
   - 所有阈值直接来自UN R129标准
   - 假人参数基于Annex 19定义
   - 测试配置基于Annex 7和ROADMATE 360格式

## 网络同步

数据库支持从网络同步最新标准数据（待实现）：

```kotlin
class DesignViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = StandardRepository.getInstance(application)
    
    fun syncStandards() {
        viewModelScope.launch {
            // TODO: 实现网络同步逻辑
            // val result = repository.syncStandards(force = true)
            // result.onSuccess { ... }.onFailure { ... }
        }
    }
}
```

## 扩展指南

如需支持其他标准（GB 27887、FMVSS 213等），应：

1. 创建新的标准特定的实体类
2. 创建对应的DAO接口
3. 在Database中添加新表
4. 创建对应的Repository方法
5. 确保输出时明确标注标准来源

## 技术支持

如有问题，请参考：
- UN R129官方文档：https://unece.org/transport/documents/2021/03/regs129r4e.pdf
- Roadmate 360格式说明
- 项目代码文档

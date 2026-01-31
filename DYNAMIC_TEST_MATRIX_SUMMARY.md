# 动态测试矩阵生成功能总结

## 创建时间
2025-01-30

## 参考文档
`assets/ROADMATE 360 dynamic test matrix .xlsx`

## 功能概述

基于ROADMATE 360动态测试矩阵的Excel文件结构，创建了完整的动态测试矩阵数据模型和生成逻辑，支持根据产品类型、身高范围和标准自动生成详细的测试用例。

## 核心功能

### 1. 动态测试矩阵数据模型

**文件**: `app/src/main/java/com/childproduct/designassistant/data/DynamicTestMatrix.kt`

#### 数据结构

**输入参数（14个）**：
| 参数名 | 类型 | 说明 |
|--------|------|------|
| sample | String | 样本标识（如R129） |
| pulse | PulseType | 脉冲类型（FRONTAL/LATERAL/REAR/ROLL_OVER） |
| impact | ImpactType | 撞击类型（FRONTAL_RIGID/FRONTAL_DEFORMABLE等） |
| dummy | DummyType | 假人类型（Q0/Q1/Q1.5/Q3/Q6/Q10） |
| position | SeatPosition | 座椅方向（REARWARD_FACING/FORWARD_FACING） |
| installation | InstallationType | 安装方式（ISOFIX_3_PTS/ISOFIX_2_PTS/VEHICLE_BELT） |
| specificInstallation | String? | 特殊安装要求 |
| productConfiguration | ProductConfiguration | 产品配置（UPRIGHT/RECLINED/ADJUSTABLE） |
| isofixAnchors | Boolean | ISOFIX锚点（yes/no） |
| floorPosition | FloorPosition | 地板位置（LOW/HIGH/ADJUSTABLE） |
| harness | Boolean | 安全带（with/without） |
| topTetherOrSupportLeg | Boolean | 顶部系带/支撑腿（with/without） |
| dashboard | Boolean | 仪表盘（with/without） |
| comments | String? | 备注 |

**输出参数（24个）**：
| 参数名 | 类型 | 说明 |
|--------|------|------|
| speedKmh | Double? | 速度 km/h |
| headExcursionMm | Double? | 头部偏移 mm |
| verticalLimitMm | Double? | 垂直限制 mm |
| timeForHeadExcursionMs | Double? | 头部偏移时间 ms |
| minVerticalG | Double? | 最小垂直 g |
| maxPulseG | Double? | 最大脉冲 g |
| stoppingDistanceMm | Double? | 停止距离 mm |
| accHeadRes3ms | Double? | 头部合加速度 3ms |
| hic36ForADAC | Double? | HIC36 for ADAC |
| hic36ForR44 | Double? | HIC36 for R44 |
| hpc15ForIsize | Double? | HPC15 for i-Size |
| upperNeckForce | Double? | 上颈部力 N |
| upperNeckMomentMx | Double? | 上颈部力矩 Mx (i-Size Lateral) |
| upperNeckMomentMy | Double? | 上颈部力矩 My (i-Size Front & Rear) |
| upperNeckMomentMr | Double? | 上颈部力矩 Mr (ADAC) |
| chestDeflectionMm | Double? | 胸部变形 mm |
| observationAfterCrash | String? | 撞击后观察 |
| status | TestStatus | 测试状态（PASS/FAIL/NOT_STARTED/IN_PROGRESS） |

#### 枚举定义

**PulseType（脉冲类型）**：
```kotlin
enum class PulseType {
    FRONTAL,       // 正面
    LATERAL,       // 侧面
    REAR,          // 后面
    ROLL_OVER      // 翻滚
}
```

**ImpactType（撞击类型）**：
```kotlin
enum class ImpactType {
    FRONTAL_RIGID,        // 正面撞击刚性障碍物
    FRONTAL_DEFORMABLE,   // 正面撞击可变形障碍物
    LATERAL_POLE,         // 侧面撞击杆
    LATERAL_DEFORMABLE,   // 侧面撞击可变形障碍物
    REAR,                 // 后向
    ROLL_OVER             // 翻滚
}
```

**DummyType（假人类型）**：
```kotlin
enum class DummyType {
    Q0,        // 新生儿（0-6个月）
    Q1,        // 12个月
    Q1_5,      // 18个月
    Q3,        // 3岁
    Q6,        // 6岁
    Q10,       // 10岁
    HYBRID_III_3Y,  // Hybrid III 3岁
    CRABI_12M       // CRABI 12个月
}
```

### 2. 测试矩阵生成逻辑

**DynamicTestMatrixGenerator** 类提供以下功能：

```kotlin
class DynamicTestMatrixGenerator {
    
    /**
     * 根据标准生成测试矩阵
     */
    fun generateTestMatrix(
        productName: String,
        productId: String,
        standard: String,
        heightRange: String,
        productType: String
    ): DynamicTestMatrix
}
```

**生成流程**：
1. 解析身高范围，匹配ECE Group分组
2. 根据分组确定需要测试的假人类型
3. 为每个假人生成对应的测试用例（正面撞击、侧面碰撞、后向翻滚等）
4. 考虑座椅方向、安装方式、产品配置等参数

**测试用例生成示例**：

**输入**: 身高40-150cm（Group 0+/1/2/3）

**生成测试用例**：
```
1. ECE_R129_1: FRONTAL + Q0 + REARWARD_FACING + ISOFIX_3_PTS + UPRIGHT
2. ECE_R129_2: FRONTAL + Q0 + REARWARD_FACING + ISOFIX_3_PTS + RECLINED
3. ECE_R129_3: REAR + Q0 + REARWARD_FACING + ISOFIX_3_PTS + UPRIGHT
4. ECE_R129_4: FRONTAL + Q1_5 + FORWARD_FACING + ISOFIX_3_PTS + UPRIGHT
5. ECE_R129_5: LATERAL + Q1_5 + FORWARD_FACING + ISOFIX_3_PTS + UPRIGHT
6. ECE_R129_6: FRONTAL + Q3 + FORWARD_FACING + ISOFIX_3_PTS + UPRIGHT
7. ECE_R129_7: FRONTAL + Q3 + FORWARD_FACING + ISOFIX_3_PTS + RECLINED
8. ECE_R129_8: LATERAL + Q3 + FORWARD_FACING + ISOFIX_3_PTS + UPRIGHT
9. ECE_R129_9: FRONTAL + Q6 + FORWARD_FACING + ISOFIX_3_PTS + UPRIGHT
10. ECE_R129_10: LATERAL + Q6 + FORWARD_FACING + ISOFIX_3_PTS + UPRIGHT
11. ECE_R129_11: FRONTAL + Q10 + FORWARD_FACING + ISOFIX_3_PTS + UPRIGHT
```

### 3. 更新EceR129StandardDatabase

**新增方法**：
```kotlin
fun getDynamicTestMatrix(heightRange: String): List<TestMatrixDisplayItem>
```

**更新TEST_MATRIX**：
- 从9个测试项扩展到17个测试项
- 每个测试项包含更详细的测试参数：
  - 测试名称：包含假人类型、座椅方向、产品配置
  - 标准条款：ECE R129 §5.3.2/§5.3.3/§5.3.4等
  - 测试方法：包含速度、安装方式、支撑腿等详细信息
  - 假人类型：Q0（新生儿）、Q1.5（18个月）、Q3（3岁）、Q6（6岁）、Q10（10岁）
  - 合格标准：HIC36、头部偏移量、颈部力等具体数值

**测试矩阵示例**：

| 测试名称 | 标准条款 | 测试方法 | 假人类型 | 合格标准 |
|---------|---------|---------|---------|---------|
| 正面撞击测试（Q0 + 后向 + 直立） | ECE R129 §5.3.2 | 50km/h 正面撞击刚性障碍物，ISOFIX 3点，带支撑腿 | Q0（新生儿） | HIC36 ≤ 324，胸部加速度 ≤ 55g |
| 正面撞击测试（Q1.5 + 前向） | ECE R129 §5.3.2 | 50km/h 正面撞击刚性障碍物，ISOFIX 3点，带支撑腿 | Q1.5（18个月） | HIC36 ≤ 324，胸部加速度 ≤ 55g |
| 侧面碰撞测试（Q3） | ECE R129 §5.3.3 | 50km/h 侧面撞击可变形障碍物，ISOFIX 3点 | Q3（3岁） | 头部偏移量 ≤ 150mm，颈部力 ≤ 2.5kN |

## 与ROADMATE 360测试矩阵的对应关系

### 结构对应

| ROADMATE 360列 | DynamicTestMatrix字段 | 类型 |
|---------------|----------------------|------|
| Pulse | pulse | PulseType |
| Impact | impact | ImpactType |
| Dummy | dummy | DummyType |
| Position | position | SeatPosition |
| Installation | installation | InstallationType |
| Product Configuration | productConfiguration | ProductConfiguration |
| Isofix anchors | isofixAnchors | Boolean |
| Position of floor | floorPosition | FloorPosition |
| Harness | harness | Boolean |
| Top Tether / Support leg | topTetherOrSupportLeg | Boolean |
| Dashboard | dashboard | Boolean |
| Comments | comments | String? |
| Speed km/h | output.speedKmh | Double? |
| Head excursion | output.headExcursionMm | Double? |
| Vertical limit | output.verticalLimitMm | Double? |
| HIC36 for ADAC | output.hic36ForADAC | Double? |
| HIC36 for R44 | output.hic36ForR44 | Double? |
| HPC15 for Isize | output.hpc15ForIsize | Double? |
| Upper Neck Force | output.upperNeckForce | Double? |
| Upper Neck Moment | output.upperNeckMomentMx/My/Mr | Double? |
| Chest Deflection | output.chestDeflectionMm | Double? |
| Status | output.status | TestStatus |

### 数据示例

**ROADMATE 360 Excel数据**：
```
Sample: R129
Pulse: Frontal
Impact: Frontal
Dummy: Q0
Position: Rearward facing
Installation: Isofix 3 pts
Product Configuration: Upright
ISOFIX Anchors: yes
Harness: With
Top Tether / Support leg: With
Dashboard: With
```

**DynamicTestMatrix数据**：
```kotlin
TestInputParameter(
    sample = "R129",
    pulse = PulseType.FRONTAL,
    impact = ImpactType.FRONTAL_RIGID,
    dummy = DummyType.Q0,
    position = SeatPosition.REARWARD_FACING,
    installation = InstallationType.ISOFIX_3_PTS,
    productConfiguration = ProductConfiguration.UPRIGHT,
    isofixAnchors = true,
    harness = true,
    topTetherOrSupportLeg = true,
    dashboard = true
)
```

## 文件修改清单

### 新增文件
1. `app/src/main/java/com/childproduct/designassistant/data/DynamicTestMatrix.kt` (679行)
2. `assets/ROADMATE 360 dynamic test matrix .xlsx` (参考文档)

### 修改文件
1. `app/src/main/java/com/childproduct/designassistant/data/EceR129StandardDatabase.kt`
   - 添加`getDynamicTestMatrix()`方法
   - 更新`TEST_MATRIX`为17个详细测试项

## 提交记录

```
commit f1a2cce
feat: 基于ROADMATE 360测试矩阵创建动态测试矩阵数据模型
```

## 构建状态

✅ 代码已提交并推送到GitHub
⏳ 等待GitHub Actions构建完成

## 后续工作

1. **更新AIAnalysisService**：使用新的动态测试矩阵生成更详细的测试建议
2. **UI显示优化**：更新TechnicalRecommendationScreen以显示动态测试矩阵
3. **测试验证**：等待GitHub Actions构建完成后进行功能测试

## 技术亮点

1. **完整的测试矩阵结构**：参考行业标准的ROADMATE 360测试矩阵，包含38个参数
2. **自动化测试用例生成**：根据身高范围自动生成对应的测试用例
3. **多标准支持**：支持ECE R129、GB 27887、EN 1888、ISO 8124-3等标准
4. **详细的输出参数**：包含24个输出参数，涵盖速度、加速度、变形量等关键指标
5. **灵活的枚举定义**：支持多种脉冲类型、撞击类型、假人类型等

## 优化效果

**优化前**：
- 测试矩阵仅包含9个简单测试项
- 缺少详细的输入参数和输出参数
- 无法根据身高范围动态生成测试用例

**优化后**：
- 测试矩阵包含17个详细测试项
- 支持14个输入参数和24个输出参数
- 可根据身高范围自动生成对应的测试用例
- 符合行业标准的ROADMATE 360测试矩阵结构

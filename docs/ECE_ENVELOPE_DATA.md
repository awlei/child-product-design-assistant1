# ECE R129 Envelope 数据文档

## 概述

Envelope（外形尺寸）数据定义了儿童安全座椅的外部轮廓、内部空间和安装要求，基于ISOFIX尺寸标准（ISO/FDIS 13216）和UN R129标准。

## 数据结构

### 字段说明

| 字段 | 类型 | 说明 | 示例 |
|------|------|------|------|
| `envelopeId` | String | 唯一标识 | `ENV_B1` |
| `sizeClass` | String | ISOFIX尺寸等级 | `B1`, `B2`, `D`, `E` |
| `applicableGroup` | String | 适用产品组 | `Group 0+`, `Group I` |
| `maxLengthMm` | Int | 最大长度(mm) | 690 |
| `maxWidthMm` | Int | 最大宽度(mm) | 460 |
| `maxHeightMm` | Int | 最大高度(mm) | 550 |
| `minCockpitLengthMm` | Int | 最小座舱长度(mm) | 520 |
| `minCockpitWidthMm` | Int | 最小座舱宽度(mm) | 240 |
| `minHeadRestHeightMm` | Int | 最小头枕高度(mm) | 380 |
| `maxHeadRestHeightMm` | Int | 最大头枕高度(mm) | 500 |
| `isofixWidthMm` | Int | ISOFIX宽度(mm) | 280 |
| `topTetherDistanceMm` | Int | Top tether锚点距离(mm) | 1050 |
| `legFootprintMm` | Int | 支撑腿占地面积(mm) | 200 |
| `sideImpactWidthMm` | Int | 侧碰防护宽度(mm) | 450 |

## ISOFIX Size Class

### Size Class B1
- **适用**: Group 0+（40-75cm）
- **外形尺寸**: 690 × 460 × 550 mm
- **座舱尺寸**: 520 × 240 mm
- **头枕高度**: 380-500 mm
- **安装方式**: 后向，使用支撑腿
- **车辆要求**: ISOFIX下固定点间距280mm，支撑腿区域无障碍

### Size Class B2
- **适用**: Group I（60-105cm）
- **外形尺寸**: 730 × 460 × 580 mm
- **座舱尺寸**: 580 × 260 mm
- **头枕高度**: 450-650 mm
- **安装方式**: 可后向或前向，使用Top Tether
- **车辆要求**: ISOFIX下固定点间距280mm，Top tether锚点距离1050mm

### Size Class D
- **适用**: Group 0+（40-75cm），特殊配置
- **外形尺寸**: 690 × 460 × 550 mm
- **座舱尺寸**: 520 × 240 mm
- **头枕高度**: 380-500 mm
- **安装方式**: 后向，使用支撑腿
- **车辆要求**: ISOFIX下固定点间距280mm，支撑腿区域无障碍
- **特点**: 专为婴儿提篮设计

### Size Class E
- **适用**: Group II/III（105-145cm）
- **外形尺寸**: 820 × 460 × 620 mm
- **座舱尺寸**: 650 × 300 mm
- **头枕高度**: 500-750 mm
- **安装方式**: 前向，使用Top Tether
- **车辆要求**: ISOFIX下固定点间距280mm，Top tether锚点距离1250mm

## 数据关系

### Envelope与假人的映射

| 假人代码 | 身高范围 | 产品组 | Size Class |
|---------|---------|--------|-----------|
| Q0 | 40-50 cm | Group 0+ | B1 / D |
| Q0+ | 50-60 cm | Group 0+ | B1 / D |
| Q1 | 60-75 cm | Group I | B2 |
| Q1.5 | 75-87 cm | Group I | B2 |
| Q3 | 87-105 cm | Group I/II | B2 |
| Q6 | 105-125 cm | Group II | E |
| Q10 | 125-145 cm | Group III | E |

### 身高与Envelope的映射

| 身高范围 | 产品组 | Size Class |
|---------|--------|-----------|
| 40-60 cm | Group 0+ | B1 / D |
| 60-87 cm | Group I | B2 |
| 87-105 cm | Group I/II | B2 |
| 105-125 cm | Group II | E |
| 125-145 cm | Group III | E |

## 使用方法

### 1. 根据假人代码获取Envelope

```kotlin
val envelope = EceEnvelope.getByDummyCode("Q3")
// 返回: Size Class B2，适用于Group I/II
```

### 2. 根据身高获取Envelope

```kotlin
val envelope = EceEnvelope.getByHeight(87)
// 返回: Size Class B2，适用于Group I/II
```

### 3. 根据产品组获取Envelope

```kotlin
val envelope = EceEnvelope.getByProductGroup("Group I")
// 返回: Size Class B2
```

### 4. 在Repository中使用

```kotlin
suspend fun getEceEnvelopeByHeight(heightCm: Int): EceEnvelope? {
    return withContext(Dispatchers.IO) {
        eceR129Database.eceEnvelopeDao().getByHeight(heightCm)
    }
}
```

### 5. 在输出生成器中使用

```kotlin
private suspend fun generateEceDesignParameters(dummy: EceCrashTestDummy?): DesignParametersSection {
    val envelope = standardRepository.getEceEnvelopeByDummyCode(dummy?.dummyCode ?: "Q3")

    return DesignParametersSection(
        sizeClass = envelope?.sizeClass ?: "B1",
        externalDimensions = "${envelope?.maxLengthMm}×${envelope?.maxWidthMm}×${envelope?.maxHeightMm}mm",
        cockpitDimensions = "${envelope?.minCockpitLengthMm}×${envelope?.minCockpitWidthMm}mm",
        installationRequirements = envelope?.vehicleRequirements ?: "参考UN R129要求"
    )
}
```

## 数据验证

### 尺寸验证

```kotlin
val isValid = envelope.validateIsSizeClass()
// 返回: true/false，验证尺寸是否符合ISOFIX标准
```

### 假人兼容性验证

```kotlin
val dummyCodes = envelope.getCompatibleDummyCodes()
// 返回: 该Envelope支持的假人代码列表
```

## 设计参数输出示例

### Group 0+ (Q0假人)
```
ISOFIX Size Class: B1
外形尺寸: 690 × 460 × 550 mm
座舱尺寸: 520 × 240 mm
头枕高度: 380-500 mm
安装要求: ISOFIX下固定点间距280mm，支撑腿区域无障碍
侧防宽度: 450 mm
```

### Group I (Q3假人)
```
ISOFIX Size Class: B2
外形尺寸: 730 × 460 × 580 mm
座舱尺寸: 580 × 260 mm
头枕高度: 450-650 mm
安装要求: ISOFIX下固定点间距280mm，Top tether锚点距离1050mm
侧防宽度: 480 mm
```

### Group II/III (Q10假人)
```
ISOFIX Size Class: E
外形尺寸: 820 × 460 × 620 mm
座舱尺寸: 650 × 300 mm
头枕高度: 500-750 mm
安装要求: ISOFIX下固定点间距280mm，Top tether锚点距离1250mm
侧防宽度: 520 mm
```

## 标准引用

- **UN R129 Annex 18**: ISOFIX尺寸要求
- **ISO/FDIS 13216**: 儿童约束系统尺寸分类
- **GPS-028**: 儿童假人尺寸数据

## 更新日志

- **v1.0 (2025)**: 初始版本，包含4个ISOFIX Size Class数据
- **数据库版本**: 8 (EceR129Database)

## 注意事项

1. **尺寸限制**: 所有Envelope必须符合ISOFIX尺寸标准，车辆座椅必须能够容纳
2. **兼容性**: 设计时应确保产品外形尺寸不超过对应Size Class的最大值
3. **安装空间**: 必须考虑Top Tether和支撑腿的空间需求
4. **头部空间**: 头枕高度必须满足假人头部空间要求，避免头部碰撞风险

## 参考资料

- [UN R129 官方文档](https://unece.org/transport/documents/2022/12/un-r129-rev5.pdf)
- [ISOFIX 尺寸标准](https://www.iso.org/standard/71439.html)
- [GPS-028 假人数据](https://www.nhtsa.gov/sites/nhtsa.dot.gov/files/811055.pdf)

# 标准数据库使用指南

## 概述

本项目构建了三个儿童约束系统（CRS）标准数据库：
1. **FMVSS 213**（美标）- 8个核心业务表
2. **ECE R129**（欧标）- 6个核心业务表
3. **GB 27887**（国标）- 7个核心业务表

每个标准都有独立的Manager类，提供统一的CRUD接口。

## 数据库结构

### FMVSS 213（美标）表结构

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `fmvss213_basic_info` | 法规基础信息 | reg_version, effective_date, core_changes, applicable_products |
| `fmvss213_dummy_params` | 假人参数与适用 | dummy_model, weight_range, height_range, test_scenario, compliance_threshold |
| `fmvss213_frontal_test_protocol` | 正面碰撞测试协议 | test_type, speed_requirement, acceleration_curve, test_bench_requirement |
| `fmvss213_side_test_protocol` | 侧面碰撞测试协议 | test_type, speed_requirement, test_bench_requirement, foam_honeycomb_requirement |
| `fmvss213_crs_design_label` | CRS设计与标识要求 | product_type, installation_constraint, core_design_size, label_requirement |
| `fmvss213_material_performance` | 材料性能要求 | material_type, performance_requirement, test_standard, applicable_scenario |
| `fmvss213_safety_thresholds` | 安全合规阈值 | test_scenario, dummy_model, hic_limit, chest_accel_limit, head_excursion_limit |
| `fmvss213_fit_envelope_size` | CRS适配包络尺寸 | envelope_type, applicable_scenario, core_size, adapted_dummy, vehicle_install_requirement |

### ECE R129（欧标）表结构

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `ece129_basic_info` | 法规基础信息 | reg_version, effective_date, core_changes, applicable_products |
| `ece129_dummy_params` | 假人参数与适用 | dummy_model, weight_range, height_range, test_scenario, compliance_threshold |
| `ece129_side_test_protocol` | 侧面碰撞测试协议 | test_type, speed_requirement, test_bench_requirement, foam_requirement |
| `ece129_crs_design_label` | CRS设计与标识要求 | product_type, installation_constraint, core_design_size, label_requirement |
| `ece129_safety_thresholds` | 安全合规阈值 | test_scenario, dummy_model, hic_limit, chest_accel_limit, head_excursion_limit |
| `ece129_fit_envelope_size` | CRS适配包络尺寸 | envelope_type, applicable_scenario, core_size, adapted_dummy |

### GB 27887（国标）表结构

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `gb27887_basic_info` | 法规基础信息 | reg_version, effective_date, core_changes, applicable_products |
| `gb27887_dummy_params` | 假人参数与适用 | dummy_model, weight_range, height_range, test_scenario, compliance_threshold |
| `gb27887_frontal_test_protocol` | 正面碰撞测试协议 | test_type, speed_requirement, acceleration_curve, test_bench_requirement |
| `gb27887_crs_design_label` | CRS设计与标识要求 | product_type, installation_constraint, core_design_size, label_requirement |
| `gb27887_material_performance` | 材料性能要求 | material_type, performance_requirement, test_standard, applicable_scenario |
| `gb27887_safety_thresholds` | 安全合规阈值 | test_scenario, dummy_model, hic_limit, chest_accel_limit, head_excursion_limit |
| `gb27887_fit_envelope_size` | CRS适配包络尺寸 | envelope_type, applicable_scenario, core_size, adapted_dummy |

## 使用示例

### 1. 导入Manager类

```python
from coze_coding_dev_sdk.database import get_session
from storage.database.fmvss213_manager import FMVSS213Manager
from storage.database.ece129_manager import ECE129Manager
from storage.database.gb27887_manager import GB27887Manager
```

### 2. 查询FMVSS 213数据

#### 2.1 获取所有法规基础信息

```python
db = get_session()
try:
    mgr = FMVSS213Manager()
    basic_info_list = mgr.get_all_basic_info(db)
    
    for info in basic_info_list:
        print(f"法规版本: {info.reg_version}")
        print(f"生效日期: {info.effective_date}")
        print(f"核心变更: {info.core_changes}")
        print(f"适用产品: {info.applicable_products}")
        print("-" * 50)
finally:
    db.close()
```

#### 2.2 查询假人参数

```python
db = get_session()
try:
    mgr = FMVSS213Manager()
    
    # 获取所有假人参数
    all_dummies = mgr.get_all_dummy_params(db)
    
    # 获取特定测试场景的假人
    side_impact_dummies = mgr.get_all_dummy_params(db, test_scenario="侧碰")
    
    # 获取特定假人
    q3s_dummy = mgr.get_dummy_params(db, dummy_model="Q3S（572W）")
    
    print(f"Q3S假人合规阈值: {q3s_dummy.compliance_threshold}")
finally:
    db.close()
```

#### 2.3 查询安全阈值

```python
db = get_session()
try:
    mgr = FMVSS213Manager()
    
    # 获取侧碰Q3S假人的安全阈值
    thresholds = mgr.get_safety_thresholds(
        db, 
        test_scenario="侧碰（213a）", 
        dummy_model="Q3S"
    )
    
    if thresholds:
        print(f"HIC限值: {thresholds.hic_limit}")
        print(f"胸部压缩限值: {thresholds.chest_compression_limit}")
        print(f"头部位移限值: {thresholds.head_excursion_limit}")
finally:
    db.close()
```

#### 2.4 综合查询（关联查询）

```python
db = get_session()
try:
    mgr = FMVSS213Manager()
    
    # 获取综合设计数据
    design_data = mgr.get_comprehensive_design_data(
        db, 
        test_scenario="侧碰（213a）", 
        dummy_model="Q3S"
    )
    
    # 访问关联数据
    dummy_params = design_data["dummy_params"]
    safety_thresholds = design_data["safety_thresholds"]
    material_performance = design_data["material_performance"]
    test_protocol = design_data["test_protocol"]
    
    print(f"假人: {dummy_params.dummy_model}")
    print(f"体重范围: {dummy_params.weight_range}")
    print(f"身高范围: {dummy_params.height_range}")
    print(f"合规阈值: {dummy_params.compliance_threshold}")
    
    print(f"\n安全阈值:")
    print(f"  HIC限值: {safety_thresholds.hic_limit}")
    print(f"  胸部压缩限值: {safety_thresholds.chest_compression_limit}")
    
    print(f"\n材料性能:")
    for material in material_performance:
        print(f"  {material.material_type}: {material.performance_requirement}")
finally:
    db.close()
```

### 3. 查询ECE R129数据

```python
db = get_session()
try:
    mgr = ECE129Manager()
    
    # 获取所有法规基础信息
    basic_info_list = mgr.get_all_basic_info(db)
    
    # 获取假人参数
    q3_dummy = mgr.get_dummy_params(db, dummy_model="Q3")
    
    # 获取安全阈值
    thresholds = mgr.get_safety_thresholds(db, test_scenario="侧碰", dummy_model="Q3")
    
    # 获取综合设计数据
    design_data = mgr.get_comprehensive_design_data(db, dummy_model="Q3")
finally:
    db.close()
```

### 4. 查询GB 27887数据

```python
db = get_session()
try:
    mgr = GB27887Manager()
    
    # 获取所有法规基础信息
    basic_info_list = mgr.get_all_basic_info(db)
    
    # 获取假人参数
    q0_dummy = mgr.get_dummy_params(db, dummy_model="Q0")
    
    # 获取材料性能
    webbing_material = mgr.get_material_performance(db, material_type="织带")
    
    # 获取综合设计数据
    design_data = mgr.get_comprehensive_design_data(db, dummy_model="Q0")
finally:
    db.close()
```

### 5. 获取CRS设计与标识要求

```python
db = get_session()
try:
    # FMVSS 213
    fmvss_mgr = FMVSS213Manager()
    infant_seat_label = fmvss_mgr.get_crs_design_label(db, product_type="提篮（Infant Seat）")
    
    # ECE R129
    ece_mgr = ECE129Manager()
    isize_label = ece_mgr.get_crs_design_label(db, product_type="提篮（i-Size）")
    
    # GB 27887
    gb_mgr = GB27887Manager()
    gb_label = gb_mgr.get_crs_design_label(db, product_type="提篮")
    
    print("FMVSS 213提篮标签要求:")
    print(infant_seat_label.label_requirement)
finally:
    db.close()
```

### 6. 获取CRS适配包络尺寸

```python
db = get_session()
try:
    mgr = FMVSS213Manager()
    
    # 获取所有包络尺寸
    all_envelopes = mgr.get_all_fit_envelope_sizes(db)
    
    # 获取特定包络类型
    rs_envelope = mgr.get_fit_envelope_size(db, envelope_type="RS")
    
    print(f"RS包络核心尺寸: {rs_envelope.core_size}")
    print(f"车辆安装要求: {rs_envelope.vehicle_install_requirement}")
finally:
    db.close()
```

## 集成到业务逻辑

### 示例：在StructuredDesignOutput中集成

```python
from coze_coding_dev_sdk.database import get_session
from storage.database.fmvss213_manager import FMVSS213Manager

@Composable
fun StandardOutputCard(
    standardType: StandardType,
    allMatchedDummies: List<GPS028DummyData>,
    ageGroup: AgeGroup,
    heightRange: String
) {
    // 按标准类型过滤假人
    val standardDummies = getDummiesByStandardType(allMatchedDummies, standardType)
    
    // 从数据库获取标准专属数据
    LaunchedEffect(standardType) {
        db = get_session()
        try {
            when (standardType) {
                StandardType.FMVSS_213 -> {
                    val mgr = FMVSS213Manager()
                    val basicInfo = mgr.get_all_basic_info(db)
                    val dummyParams = mgr.get_all_dummy_params(db)
                    // 使用数据更新UI
                }
                StandardType.ECE_R129 -> {
                    val mgr = ECE129Manager()
                    // ...
                }
                StandardType.GB_27887 -> {
                    val mgr = GB27887Manager()
                    // ...
                }
            }
        } finally {
            db.close()
        }
    }
    
    // UI渲染...
}
```

## 数据维护

### 添加新数据

```python
from storage.database.fmvss213_manager import FMVSS213Manager, FMVSS213BasicInfoCreate

db = get_session()
try:
    mgr = FMVSS213Manager()
    
    # 创建新法规版本
    new_version = FMVSS213BasicInfoCreate(
        reg_version="FMVSS 213c",
        effective_date="2027-01-01",
        core_changes="新增...",
        applicable_products="...",
        data_source="..."
    )
    
    created = mgr.create_basic_info(db, new_version)
    print(f"创建成功: {created.reg_version}")
finally:
    db.close()
```

### 更新数据

由于使用了PostgreSQL，可以通过SQL直接更新，或者修改Manager类添加update方法。

## 数据来源引用

所有数据都包含`data_source`字段，引用了原始法规文档的章节，便于追溯和验证：
- FMVSS 213: 附件1§Regulatory Changes、附件3§12.C.1等
- ECE R129: ECE R129法规文档
- GB 27887: GB 27887-2024标准

## 注意事项

1. **连接管理**: 务必使用`try-finally`确保数据库连接正确关闭
2. **标准隔离**: 三个标准的数据完全隔离，避免混淆
3. **数据追溯**: 所有数据都有`data_source`字段，便于追溯来源
4. **性能优化**: 批量查询时使用Manager提供的批量方法
5. **异常处理**: 建议在调用Manager方法时添加异常处理

## 常见问题

### Q: 如何选择使用哪个标准？
A: 根据目标市场和用户选择的标准类型（FMVSS_213、ECE_R129、GB_27887）调用对应的Manager。

### Q: 数据如何更新？
A: 定期从NHTSA、UNECE等官方渠道获取最新法规，通过Manager的create方法或直接SQL更新。

### Q: 是否支持事务？
A: 是的，使用`db.commit()`和`db.rollback()`管理事务。

### Q: 如何扩展标准？
A: 在`model.py`中添加新表的ORM模型，在对应的Manager中添加方法，然后运行`coze-coding-ai db upgrade`。

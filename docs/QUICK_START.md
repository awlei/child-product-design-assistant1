# 标准数据库快速开始指南

## 概述

本项目已成功构建三个儿童约束系统（CRS）标准数据库：
- ✅ **FMVSS 213**（美标）- 8个表
- ✅ **ECE R129**（欧标）- 6个表
- ✅ **GB 27887**（国标）- 7个表

数据库已初始化完成，包含完整的法规数据、假人参数、测试协议等信息。

## 已完成的工作

### 1. 数据库表结构创建 ✅
- 所有表的ORM模型已定义在 `src/storage/database/shared/model.py`
- 共21个数据表（FMVSS 213: 8个，ECE R129: 6个，GB 27887: 7个）

### 2. Manager接口创建 ✅
- `src/storage/database/fmvss213_manager.py` - FMVSS 213数据管理器
- `src/storage/database/ece129_manager.py` - ECE R129数据管理器
- `src/storage/database/gb27887_manager.py` - GB 27887数据管理器

### 3. 数据初始化 ✅
- `src/storage/database/init_standard_db.py` - 数据初始化脚本
- 所有初始数据已成功导入数据库

### 4. 数据库同步 ✅
- 表结构已同步到PostgreSQL数据库
- 数据已成功导入

## 快速使用

### 1分钟快速查询

```python
from coze_coding_dev_sdk.database import get_session
from storage.database.fmvss213_manager import FMVSS213Manager

# 查询FMVSS 213所有法规基础信息
db = get_session()
try:
    mgr = FMVSS213Manager()
    basic_info = mgr.get_all_basic_info(db)
    
    for info in basic_info:
        print(f"法规: {info.reg_version}")
        print(f"生效日期: {info.effective_date}")
        print(f"适用产品: {info.applicable_products}")
finally:
    db.close()
```

## 数据表清单

### FMVSS 213（美标）
```
✅ fmvss213_basic_info          法规基础信息
✅ fmvss213_dummy_params        假人参数与适用
✅ fmvss213_frontal_test_protocol  正面碰撞测试协议
✅ fmvss213_side_test_protocol     侧面碰撞测试协议
✅ fmvss213_crs_design_label    CRS设计与标识要求
✅ fmvss213_material_performance 材料性能要求
✅ fmvss213_safety_thresholds   安全合规阈值
✅ fmvss213_fit_envelope_size   CRS适配包络尺寸
```

### ECE R129（欧标）
```
✅ ece129_basic_info           法规基础信息
✅ ece129_dummy_params         假人参数与适用
✅ ece129_side_test_protocol   侧面碰撞测试协议
✅ ece129_crs_design_label     CRS设计与标识要求
✅ ece129_safety_thresholds    安全合规阈值
✅ ece129_fit_envelope_size    CRS适配包络尺寸
```

### GB 27887（国标）
```
✅ gb27887_basic_info          法规基础信息
✅ gb27887_dummy_params        假人参数与适用
✅ gb27887_frontal_test_protocol  正面碰撞测试协议
✅ gb27887_crs_design_label    CRS设计与标识要求
✅ gb27887_material_performance 材料性能要求
✅ gb27887_safety_thresholds   安全合规阈值
✅ gb27887_fit_envelope_size   CRS适配包络尺寸
```

## 初始数据统计

### FMVSS 213 初始数据
- 法规基础信息: 3条（FMVSS 213现行、FMVSS 213a侧碰、FMVSS 213b正碰）
- 假人参数: 4条（Newborn、CRABI 12MO、Q3S、HIII-3YO）
- 正面碰撞测试协议: 2条（Config I、Config II）
- 侧面碰撞测试协议: 1条（213a侧碰）
- CRS设计与标识要求: 2条（提篮、可转向CRS）
- 材料性能要求: 3条（织带、座椅泡沫、主体框架）
- 安全合规阈值: 2条（正碰、侧碰）
- CRS适配包络尺寸: 3条（RS、RM、FS）

### ECE R129 初始数据
- 法规基础信息: 2条（ECE R129、ECE R129修订版）
- 假人参数: 4条（Q0、Q3、Q6、Q10）
- 侧面碰撞测试协议: 1条（ECE R129侧碰）
- CRS设计与标识要求: 2条（提篮、可转向CRS）
- 安全合规阈值: 2条（Q0、Q3侧碰）
- CRS适配包络尺寸: 2条（Compact、Standard）

### GB 27887 初始数据
- 法规基础信息: 2条（GB 27887-2011、GB 27887-2024）
- 假人参数: 3条（Q0、Q3、Q6）
- 正面碰撞测试协议: 1条（GB 27887正碰）
- CRS设计与标识要求: 2条（提篮、可转向CRS）
- 材料性能要求: 2条（织带、座椅泡沫）
- 安全合规阈值: 2条（Q0、Q3正碰）
- CRS适配包络尺寸: 2条（Compact、Standard）

## 核心功能

### 1. 法规基础信息查询
```python
# 获取所有法规版本
basic_info_list = mgr.get_all_basic_info(db)

# 获取特定法规版本
basic_info = mgr.get_basic_info(db, reg_version="FMVSS 213（现行）")
```

### 2. 假人参数查询
```python
# 获取所有假人
all_dummies = mgr.get_all_dummy_params(db)

# 按测试场景过滤
side_impact_dummies = mgr.get_all_dummy_params(db, test_scenario="侧碰")

# 获取特定假人
dummy = mgr.get_dummy_params(db, dummy_model="Q3S（572W）")
```

### 3. 安全阈值查询
```python
# 获取特定场景和假人的安全阈值
thresholds = mgr.get_safety_thresholds(
    db,
    test_scenario="侧碰（213a）",
    dummy_model="Q3S"
)
```

### 4. 综合查询（关联查询）
```python
# 获取综合设计数据（包含假人参数、安全阈值、材料性能、测试协议）
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
```

### 5. CRS设计要求查询
```python
# 获取CRS设计与标识要求
crs_label = mgr.get_crs_design_label(db, product_type="提篮（Infant Seat）")

# 获取CRS适配包络尺寸
envelope = mgr.get_fit_envelope_size(db, envelope_type="RS")
```

## 文件结构

```
src/storage/database/
├── shared/
│   └── model.py                    # 所有ORM模型定义
├── fmvss213_manager.py             # FMVSS 213 Manager
├── ece129_manager.py               # ECE R129 Manager
├── gb27887_manager.py              # GB 27887 Manager
└── init_standard_db.py             # 数据初始化脚本

docs/
├── STANDARD_DATABASE_USAGE.md      # 详细使用指南
└── QUICK_START.md                  # 本快速开始指南
```

## 下一步

1. **集成到UI**: 参考 `docs/STANDARD_DATABASE_USAGE.md` 中的集成示例
2. **添加查询界面**: 在StructuredDesignOutput中添加标准数据查询功能
3. **数据更新**: 定期更新数据库以保持法规信息最新
4. **扩展功能**: 根据需求添加新的查询方法或数据表

## 技术栈

- **数据库**: PostgreSQL
- **ORM**: SQLAlchemy
- **Python版本**: Python 3.8+
- **数据验证**: Pydantic

## 支持的标准

| 标准 | 地区 | Manager类 | 表数量 |
|------|------|-----------|--------|
| FMVSS 213 | 美国 | FMVSS213Manager | 8 |
| ECE R129 | 欧洲 | ECE129Manager | 6 |
| GB 27887 | 中国 | GB27887Manager | 7 |

## 数据来源

所有数据都包含`data_source`字段，引用了原始法规文档：
- **FMVSS 213**: 附件1§Regulatory Changes、附件2、附件3§12.C.1等
- **ECE R129**: ECE R129法规文档
- **GB 27887**: GB 27887-2024标准

## 常见使用场景

### 场景1: 查询美标侧碰Q3S假人的完整测试要求
```python
db = get_session()
mgr = FMVSS213Manager()

# 获取假人参数
dummy = mgr.get_dummy_params(db, "Q3S（572W）")

# 获取安全阈值
thresholds = mgr.get_safety_thresholds(db, "侧碰（213a）", "Q3S")

# 获取侧碰测试协议
protocol = mgr.get_side_test_protocol(db, "213a侧碰")

print(f"假人: {dummy.dummy_model}")
print(f"体重范围: {dummy.weight_range}")
print(f"HIC限值: {thresholds.hic_limit}")
print(f"测试速度: {protocol.speed_requirement}")

db.close()
```

### 场景2: 对比三个标准的假人参数
```python
db = get_session()

# FMVSS 213
fmvss_mgr = FMVSS213Manager()
fmvss_dummies = fmvss_mgr.get_all_dummy_params(db)

# ECE R129
ece_mgr = ECE129Manager()
ece_dummies = ece_mgr.get_all_dummy_params(db)

# GB 27887
gb_mgr = GB27887Manager()
gb_dummies = gb_mgr.get_all_dummy_params(db)

# 对比分析...
db.close()
```

### 场景3: 查询提篮产品的所有标准要求
```python
db = get_session()

# FMVSS 213
fmvss_mgr = FMVSS213Manager()
fmvss_label = fmvss_mgr.get_crs_design_label(db, "提篮（Infant Seat）")

# ECE R129
ece_mgr = ECE129Manager()
ece_label = ece_mgr.get_crs_design_label(db, "提篮（i-Size）")

# GB 27887
gb_mgr = GB27887Manager()
gb_label = gb_mgr.get_crs_design_label(db, "提篮")

print("FMVSS 213:", fmvss_label.label_requirement)
print("ECE R129:", ece_label.label_requirement)
print("GB 27887:", gb_label.label_requirement)

db.close()
```

## 总结

✅ 数据库已构建完成
✅ 数据已初始化
✅ Manager接口已就绪
✅ 文档已完善

**现在可以开始使用标准数据库了！**

详细信息请参考 `docs/STANDARD_DATABASE_USAGE.md`

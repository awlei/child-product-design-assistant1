from coze_coding_dev_sdk.database import Base

from sqlalchemy import MetaData
from sqlalchemy import BigInteger, Boolean, DateTime, Float, ForeignKey, Index, Integer, String, Text, JSON, func, Date
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column, relationship
from typing import Optional
from datetime import datetime

metadata = MetaData()

# ==================== FMVSS 213 (美标) 数据库表 ====================

class FMVSS213BasicInfo(Base):
    """FMVSS 213法规基础信息表"""
    __tablename__ = "fmvss213_basic_info"

    reg_version: Mapped[str] = mapped_column(String(50), primary_key=True, comment="法规版本（如FMVSS 213、FMVSS 213a）")
    effective_date: Mapped[datetime] = mapped_column(Date, nullable=False, comment="生效日期")
    core_changes: Mapped[str] = mapped_column(Text, nullable=False, comment="核心变更内容")
    applicable_products: Mapped[str] = mapped_column(Text, nullable=False, comment="适用产品类型")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源（附件章节）")


class FMVSS213DummyParams(Base):
    """FMVSS 213假人参数与适用表"""
    __tablename__ = "fmvss213_dummy_params"

    dummy_model: Mapped[str] = mapped_column(String(50), primary_key=True, comment="假人型号（如Newborn 572K）")
    weight_range: Mapped[str] = mapped_column(String(100), nullable=False, comment="适用体重范围（含单位）")
    height_range: Mapped[str] = mapped_column(String(100), nullable=False, comment="适用身高范围（含单位）")
    test_scenario: Mapped[str] = mapped_column(String(50), nullable=False, comment="测试场景（正碰/侧碰）")
    instrument_config: Mapped[str] = mapped_column(Text, nullable=False, comment="关键仪器配置")
    compliance_threshold: Mapped[str] = mapped_column(Text, nullable=False, comment="核心合规阈值")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源（附件章节）")


class FMVSS213FrontalTestProtocol(Base):
    """FMVSS 213正面碰撞测试协议表"""
    __tablename__ = "fmvss213_frontal_test_protocol"

    test_type: Mapped[str] = mapped_column(String(50), primary_key=True, comment="测试类型（如213 Config I）")
    speed_requirement: Mapped[str] = mapped_column(String(100), nullable=False, comment="速度要求（含公差）")
    acceleration_curve: Mapped[str] = mapped_column(Text, nullable=False, comment="加速度曲线参数（配置）")
    test_bench_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="测试台（FISA）要求")
    installation_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="安装要求（LATCH/安全带）")
    env_condition: Mapped[str] = mapped_column(String(200), nullable=False, comment="环境条件（温度/湿度）")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源（附件章节）")


class FMVSS213SideTestProtocol(Base):
    """FMVSS 213a侧面碰撞测试协议表"""
    __tablename__ = "fmvss213_side_test_protocol"

    test_type: Mapped[str] = mapped_column(String(50), primary_key=True, comment="测试类型（如213a侧碰）")
    speed_requirement: Mapped[str] = mapped_column(String(100), nullable=False, comment="速度要求（相对速度）")
    test_bench_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="测试台（SISA）要求")
    foam_honeycomb_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="泡沫与蜂窝要求")
    installation_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="安装要求（LATCH/安全带）")
    env_condition: Mapped[str] = mapped_column(String(200), nullable=False, comment="环境条件")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class FMVSS213CRSDesignLabel(Base):
    """FMVSS 213 CRS设计与标识要求表"""
    __tablename__ = "fmvss213_crs_design_label"

    product_type: Mapped[str] = mapped_column(String(50), primary_key=True, comment="产品类型（如提篮、可转向CRS）")
    installation_constraint: Mapped[str] = mapped_column(Text, nullable=False, comment="安装方向约束")
    core_design_size: Mapped[str] = mapped_column(Text, nullable=False, comment="核心设计尺寸（单位：mm，示例）")
    label_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="标签强制内容")
    registration_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="注册要求（213现行）")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class FMVSS213MaterialPerformance(Base):
    """FMVSS 213材料性能要求表"""
    __tablename__ = "fmvss213_material_performance"

    material_type: Mapped[str] = mapped_column(String(50), primary_key=True, comment="材料类型（如织带、座椅泡沫）")
    performance_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="性能指标要求")
    test_standard: Mapped[str] = mapped_column(String(200), nullable=False, comment="测试标准（如ASTM D3574）")
    applicable_scenario: Mapped[str] = mapped_column(String(200), nullable=False, comment="适用场景")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class FMVSS213SafetyThresholds(Base):
    """FMVSS 213安全合规阈值表"""
    __tablename__ = "fmvss213_safety_thresholds"

    test_scenario: Mapped[str] = mapped_column(String(50), primary_key=True, comment="测试场景（如正碰213、侧碰213a）")
    dummy_model: Mapped[str] = mapped_column(String(50), primary_key=True, comment="假人类型（关联FMVSS213DummyParams.dummy_model）")
    hic_limit: Mapped[str] = mapped_column(String(50), nullable=False, comment="HIC限值（如≤1000）")
    chest_accel_limit: Mapped[Optional[str]] = mapped_column(String(50), nullable=True, comment="胸部加速度限值（如≤60g）")
    chest_compression_limit: Mapped[Optional[str]] = mapped_column(String(50), nullable=True, comment="胸部压缩限值（侧碰，如≤23mm）")
    head_excursion_limit: Mapped[Optional[str]] = mapped_column(String(50), nullable=True, comment="头部位移限值（如≤813mm）")
    other_requirements: Mapped[Optional[str]] = mapped_column(Text, nullable=True, comment="其他要求")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class FMVSS213FitEnvelopeSize(Base):
    """FMVSS 213 CRS适配包络尺寸表"""
    __tablename__ = "fmvss213_fit_envelope_size"

    envelope_type: Mapped[str] = mapped_column(String(50), primary_key=True, comment="包络类型（如RS、RM）")
    applicable_scenario: Mapped[str] = mapped_column(String(50), nullable=False, comment="适用场景（RF/FF，RF=后向，FF=前向）")
    core_size: Mapped[str] = mapped_column(String(200), nullable=False, comment="核心尺寸（长×宽×高，单位：mm）")
    adapted_dummy: Mapped[str] = mapped_column(String(50), nullable=False, comment="适配假人（关联FMVSS213DummyParams.dummy_model）")
    vehicle_install_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="车辆安装要求（最小空间）")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


# ==================== ECE R129 (欧标) 数据库表 ====================

class ECE129BasicInfo(Base):
    """ECE R129法规基础信息表"""
    __tablename__ = "ece129_basic_info"

    reg_version: Mapped[str] = mapped_column(String(50), primary_key=True, comment="法规版本（如ECE R129）")
    effective_date: Mapped[datetime] = mapped_column(Date, nullable=False, comment="生效日期")
    core_changes: Mapped[str] = mapped_column(Text, nullable=False, comment="核心变更内容")
    applicable_products: Mapped[str] = mapped_column(Text, nullable=False, comment="适用产品类型")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class ECE129DummyParams(Base):
    """ECE R129假人参数与适用表"""
    __tablename__ = "ece129_dummy_params"

    dummy_model: Mapped[str] = mapped_column(String(50), primary_key=True, comment="假人型号（如Q0、Q3、Q6）")
    weight_range: Mapped[str] = mapped_column(String(100), nullable=False, comment="适用体重范围（含单位）")
    height_range: Mapped[str] = mapped_column(String(100), nullable=False, comment="适用身高范围（含单位）")
    test_scenario: Mapped[str] = mapped_column(String(50), nullable=False, comment="测试场景（正碰/侧碰）")
    instrument_config: Mapped[str] = mapped_column(Text, nullable=False, comment="关键仪器配置")
    compliance_threshold: Mapped[str] = mapped_column(Text, nullable=False, comment="核心合规阈值")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class ECE129SideTestProtocol(Base):
    """ECE R129侧面碰撞测试协议表"""
    __tablename__ = "ece129_side_test_protocol"

    test_type: Mapped[str] = mapped_column(String(50), primary_key=True, comment="测试类型")
    speed_requirement: Mapped[str] = mapped_column(String(100), nullable=False, comment="速度要求")
    test_bench_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="测试台要求")
    foam_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="泡沫要求")
    installation_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="安装要求")
    env_condition: Mapped[str] = mapped_column(String(200), nullable=False, comment="环境条件")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class ECE129CRSDesignLabel(Base):
    """ECE R129 CRS设计与标识要求表"""
    __tablename__ = "ece129_crs_design_label"

    product_type: Mapped[str] = mapped_column(String(50), primary_key=True, comment="产品类型")
    installation_constraint: Mapped[str] = mapped_column(Text, nullable=False, comment="安装方向约束")
    core_design_size: Mapped[str] = mapped_column(Text, nullable=False, comment="核心设计尺寸（单位：mm）")
    label_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="标签强制内容")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class ECE129SafetyThresholds(Base):
    """ECE R129安全合规阈值表"""
    __tablename__ = "ece129_safety_thresholds"

    test_scenario: Mapped[str] = mapped_column(String(50), primary_key=True, comment="测试场景")
    dummy_model: Mapped[str] = mapped_column(String(50), primary_key=True, comment="假人类型")
    hic_limit: Mapped[str] = mapped_column(String(50), nullable=False, comment="HIC限值")
    chest_accel_limit: Mapped[Optional[str]] = mapped_column(String(50), nullable=True, comment="胸部加速度限值")
    head_excursion_limit: Mapped[Optional[str]] = mapped_column(String(50), nullable=True, comment="头部位移限值")
    neck_force_limit: Mapped[Optional[str]] = mapped_column(String(50), nullable=True, comment="颈部力限值")
    other_requirements: Mapped[Optional[str]] = mapped_column(Text, nullable=True, comment="其他要求")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class ECE129FitEnvelopeSize(Base):
    """ECE R129 CRS适配包络尺寸表"""
    __tablename__ = "ece129_fit_envelope_size"

    envelope_type: Mapped[str] = mapped_column(String(50), primary_key=True, comment="包络类型")
    applicable_scenario: Mapped[str] = mapped_column(String(50), nullable=False, comment="适用场景（RF/FF）")
    core_size: Mapped[str] = mapped_column(String(200), nullable=False, comment="核心尺寸（长×宽×高，单位：mm）")
    adapted_dummy: Mapped[str] = mapped_column(String(50), nullable=False, comment="适配假人")
    vehicle_install_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="车辆安装要求")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


# ==================== GB 27887 (国标) 数据库表 ====================

class GB27887BasicInfo(Base):
    """GB 27887法规基础信息表"""
    __tablename__ = "gb27887_basic_info"

    reg_version: Mapped[str] = mapped_column(String(50), primary_key=True, comment="法规版本（如GB 27887-2011、GB 27887-2024）")
    effective_date: Mapped[datetime] = mapped_column(Date, nullable=False, comment="生效日期")
    core_changes: Mapped[str] = mapped_column(Text, nullable=False, comment="核心变更内容")
    applicable_products: Mapped[str] = mapped_column(Text, nullable=False, comment="适用产品类型")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class GB27887DummyParams(Base):
    """GB 27887假人参数与适用表"""
    __tablename__ = "gb27887_dummy_params"

    dummy_model: Mapped[str] = mapped_column(String(50), primary_key=True, comment="假人型号（如Q0、Q3、Q6）")
    weight_range: Mapped[str] = mapped_column(String(100), nullable=False, comment="适用体重范围（含单位）")
    height_range: Mapped[str] = mapped_column(String(100), nullable=False, comment="适用身高范围（含单位）")
    test_scenario: Mapped[str] = mapped_column(String(50), nullable=False, comment="测试场景（正碰/侧碰）")
    instrument_config: Mapped[str] = mapped_column(Text, nullable=False, comment="关键仪器配置")
    compliance_threshold: Mapped[str] = mapped_column(Text, nullable=False, comment="核心合规阈值")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class GB27887FrontalTestProtocol(Base):
    """GB 27887正面碰撞测试协议表"""
    __tablename__ = "gb27887_frontal_test_protocol"

    test_type: Mapped[str] = mapped_column(String(50), primary_key=True, comment="测试类型")
    speed_requirement: Mapped[str] = mapped_column(String(100), nullable=False, comment="速度要求")
    acceleration_curve: Mapped[str] = mapped_column(Text, nullable=False, comment="加速度曲线参数")
    test_bench_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="测试台要求")
    installation_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="安装要求")
    env_condition: Mapped[str] = mapped_column(String(200), nullable=False, comment="环境条件")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class GB27887CRSDesignLabel(Base):
    """GB 27887 CRS设计与标识要求表"""
    __tablename__ = "gb27887_crs_design_label"

    product_type: Mapped[str] = mapped_column(String(50), primary_key=True, comment="产品类型")
    installation_constraint: Mapped[str] = mapped_column(Text, nullable=False, comment="安装方向约束")
    core_design_size: Mapped[str] = mapped_column(Text, nullable=False, comment="核心设计尺寸（单位：mm）")
    label_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="标签强制内容")
    registration_requirement: Mapped[Optional[str]] = mapped_column(Text, nullable=True, comment="注册要求")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class GB27887MaterialPerformance(Base):
    """GB 27887材料性能要求表"""
    __tablename__ = "gb27887_material_performance"

    material_type: Mapped[str] = mapped_column(String(50), primary_key=True, comment="材料类型")
    performance_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="性能指标要求")
    test_standard: Mapped[str] = mapped_column(String(200), nullable=False, comment="测试标准")
    applicable_scenario: Mapped[str] = mapped_column(String(200), nullable=False, comment="适用场景")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class GB27887SafetyThresholds(Base):
    """GB 27887安全合规阈值表"""
    __tablename__ = "gb27887_safety_thresholds"

    test_scenario: Mapped[str] = mapped_column(String(50), primary_key=True, comment="测试场景")
    dummy_model: Mapped[str] = mapped_column(String(50), primary_key=True, comment="假人类型")
    hic_limit: Mapped[str] = mapped_column(String(50), nullable=False, comment="HIC限值")
    chest_accel_limit: Mapped[Optional[str]] = mapped_column(String(50), nullable=True, comment="胸部加速度限值")
    head_excursion_limit: Mapped[Optional[str]] = mapped_column(String(50), nullable=True, comment="头部位移限值")
    other_requirements: Mapped[Optional[str]] = mapped_column(Text, nullable=True, comment="其他要求")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


class GB27887FitEnvelopeSize(Base):
    """GB 27887 CRS适配包络尺寸表"""
    __tablename__ = "gb27887_fit_envelope_size"

    envelope_type: Mapped[str] = mapped_column(String(50), primary_key=True, comment="包络类型")
    applicable_scenario: Mapped[str] = mapped_column(String(50), nullable=False, comment="适用场景（RF/FF）")
    core_size: Mapped[str] = mapped_column(String(200), nullable=False, comment="核心尺寸（长×宽×高，单位：mm）")
    adapted_dummy: Mapped[str] = mapped_column(String(50), nullable=False, comment="适配假人")
    vehicle_install_requirement: Mapped[str] = mapped_column(Text, nullable=False, comment="车辆安装要求")
    data_source: Mapped[str] = mapped_column(String(200), nullable=False, comment="数据来源")


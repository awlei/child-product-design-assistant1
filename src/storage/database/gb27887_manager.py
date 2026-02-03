"""
GB 27887 (国标) 标准数据库 Manager
用于管理国标GB 27887儿童约束系统相关数据
"""
from typing import List, Optional
from pydantic import BaseModel, Field
from sqlalchemy.orm import Session

from storage.database.shared.model import (
    GB27887BasicInfo,
    GB27887DummyParams,
    GB27887FrontalTestProtocol,
    GB27887CRSDesignLabel,
    GB27887MaterialPerformance,
    GB27887SafetyThresholds,
    GB27887FitEnvelopeSize
)


# ==================== Pydantic Models ====================

class GB27887BasicInfoCreate(BaseModel):
    """创建法规基础信息"""
    reg_version: str = Field(..., description="法规版本")
    effective_date: str = Field(..., description="生效日期（YYYY-MM-DD）")
    core_changes: str = Field(..., description="核心变更内容")
    applicable_products: str = Field(..., description="适用产品类型")
    data_source: str = Field(..., description="数据来源")


class GB27887DummyParamsCreate(BaseModel):
    """创建假人参数"""
    dummy_model: str = Field(..., description="假人型号")
    weight_range: str = Field(..., description="适用体重范围")
    height_range: str = Field(..., description="适用身高范围")
    test_scenario: str = Field(..., description="测试场景")
    instrument_config: str = Field(..., description="关键仪器配置")
    compliance_threshold: str = Field(..., description="核心合规阈值")
    data_source: str = Field(..., description="数据来源")


class GB27887FrontalTestProtocolCreate(BaseModel):
    """创建正面碰撞测试协议"""
    test_type: str = Field(..., description="测试类型")
    speed_requirement: str = Field(..., description="速度要求")
    acceleration_curve: str = Field(..., description="加速度曲线")
    test_bench_requirement: str = Field(..., description="测试台要求")
    installation_requirement: str = Field(..., description="安装要求")
    env_condition: str = Field(..., description="环境条件")
    data_source: str = Field(..., description="数据来源")


class GB27887CRSDesignLabelCreate(BaseModel):
    """创建CRS设计与标识要求"""
    product_type: str = Field(..., description="产品类型")
    installation_constraint: str = Field(..., description="安装方向约束")
    core_design_size: str = Field(..., description="核心设计尺寸")
    label_requirement: str = Field(..., description="标签强制内容")
    registration_requirement: Optional[str] = Field(None, description="注册要求")
    data_source: str = Field(..., description="数据来源")


class GB27887MaterialPerformanceCreate(BaseModel):
    """创建材料性能要求"""
    material_type: str = Field(..., description="材料类型")
    performance_requirement: str = Field(..., description="性能指标要求")
    test_standard: str = Field(..., description="测试标准")
    applicable_scenario: str = Field(..., description="适用场景")
    data_source: str = Field(..., description="数据来源")


class GB27887SafetyThresholdsCreate(BaseModel):
    """创建安全合规阈值"""
    test_scenario: str = Field(..., description="测试场景")
    dummy_model: str = Field(..., description="假人类型")
    hic_limit: str = Field(..., description="HIC限值")
    chest_accel_limit: Optional[str] = Field(None, description="胸部加速度限值")
    head_excursion_limit: Optional[str] = Field(None, description="头部位移限值")
    other_requirements: Optional[str] = Field(None, description="其他要求")
    data_source: str = Field(..., description="数据来源")


class GB27887FitEnvelopeSizeCreate(BaseModel):
    """创建CRS适配包络尺寸"""
    envelope_type: str = Field(..., description="包络类型")
    applicable_scenario: str = Field(..., description="适用场景")
    core_size: str = Field(..., description="核心尺寸")
    adapted_dummy: str = Field(..., description="适配假人")
    vehicle_install_requirement: str = Field(..., description="车辆安装要求")
    data_source: str = Field(..., description="数据来源")


# ==================== Manager Class ====================

class GB27887Manager:
    """GB 27887标准数据管理器"""

    def create_basic_info(self, db: Session, data_in: GB27887BasicInfoCreate) -> GB27887BasicInfo:
        """创建法规基础信息"""
        data = data_in.model_dump()
        from datetime import datetime
        if isinstance(data['effective_date'], str):
            data['effective_date'] = datetime.strptime(data['effective_date'], '%Y-%m-%d').date()
        
        db_data = GB27887BasicInfo(**data)
        db.add(db_data)
        try:
            db.commit()
            db.refresh(db_data)
            return db_data
        except Exception:
            db.rollback()
            raise

    def get_basic_info(self, db: Session, reg_version: str) -> Optional[GB27887BasicInfo]:
        """获取法规基础信息"""
        return db.query(GB27887BasicInfo).filter(
            GB27887BasicInfo.reg_version == reg_version
        ).first()

    def get_all_basic_info(self, db: Session) -> List[GB27887BasicInfo]:
        """获取所有法规基础信息"""
        return db.query(GB27887BasicInfo).order_by(
            GB27887BasicInfo.effective_date.desc()
        ).all()

    def create_dummy_params(self, db: Session, data_in: GB27887DummyParamsCreate) -> GB27887DummyParams:
        """创建假人参数"""
        data = data_in.model_dump()
        db_data = GB27887DummyParams(**data)
        db.add(db_data)
        try:
            db.commit()
            db.refresh(db_data)
            return db_data
        except Exception:
            db.rollback()
            raise

    def get_dummy_params(self, db: Session, dummy_model: str) -> Optional[GB27887DummyParams]:
        """获取假人参数"""
        return db.query(GB27887DummyParams).filter(
            GB27887DummyParams.dummy_model == dummy_model
        ).first()

    def get_all_dummy_params(self, db: Session, test_scenario: Optional[str] = None) -> List[GB27887DummyParams]:
        """获取所有假人参数（可按测试场景过滤）"""
        query = db.query(GB27887DummyParams)
        if test_scenario:
            query = query.filter(GB27887DummyParams.test_scenario == test_scenario)
        return query.all()

    def get_safety_thresholds(self, db: Session, test_scenario: str, dummy_model: str) -> Optional[GB27887SafetyThresholds]:
        """获取安全合规阈值"""
        return db.query(GB27887SafetyThresholds).filter(
            GB27887SafetyThresholds.test_scenario == test_scenario,
            GB27887SafetyThresholds.dummy_model == dummy_model
        ).first()

    def get_all_safety_thresholds(self, db: Session, test_scenario: Optional[str] = None) -> List[GB27887SafetyThresholds]:
        """获取所有安全合规阈值（可按测试场景过滤）"""
        query = db.query(GB27887SafetyThresholds)
        if test_scenario:
            query = query.filter(GB27887SafetyThresholds.test_scenario == test_scenario)
        return query.all()

    def get_crs_design_label(self, db: Session, product_type: str) -> Optional[GB27887CRSDesignLabel]:
        """获取CRS设计与标识要求"""
        return db.query(GB27887CRSDesignLabel).filter(
            GB27887CRSDesignLabel.product_type == product_type
        ).first()

    def get_all_crs_design_labels(self, db: Session) -> List[GB27887CRSDesignLabel]:
        """获取所有CRS设计与标识要求"""
        return db.query(GB27887CRSDesignLabel).all()

    def get_material_performance(self, db: Session, material_type: str) -> Optional[GB27887MaterialPerformance]:
        """获取材料性能要求"""
        return db.query(GB27887MaterialPerformance).filter(
            GB27887MaterialPerformance.material_type == material_type
        ).first()

    def get_all_material_performance(self, db: Session) -> List[GB27887MaterialPerformance]:
        """获取所有材料性能要求"""
        return db.query(GB27887MaterialPerformance).all()

    def get_frontal_test_protocol(self, db: Session, test_type: str) -> Optional[GB27887FrontalTestProtocol]:
        """获取正面碰撞测试协议"""
        return db.query(GB27887FrontalTestProtocol).filter(
            GB27887FrontalTestProtocol.test_type == test_type
        ).first()

    def get_all_frontal_test_protocols(self, db: Session) -> List[GB27887FrontalTestProtocol]:
        """获取所有正面碰撞测试协议"""
        return db.query(GB27887FrontalTestProtocol).all()

    def get_fit_envelope_size(self, db: Session, envelope_type: str) -> Optional[GB27887FitEnvelopeSize]:
        """获取CRS适配包络尺寸"""
        return db.query(GB27887FitEnvelopeSize).filter(
            GB27887FitEnvelopeSize.envelope_type == envelope_type
        ).first()

    def get_all_fit_envelope_sizes(self, db: Session) -> List[GB27887FitEnvelopeSize]:
        """获取所有CRS适配包络尺寸"""
        return db.query(GB27887FitEnvelopeSize).all()

    def get_comprehensive_design_data(self, db: Session, dummy_model: str) -> dict:
        """获取综合设计数据（关联查询）"""
        # 获取假人参数
        dummy_params = self.get_dummy_params(db, dummy_model)
        
        # 获取安全阈值
        safety_thresholds = self.get_safety_thresholds(db, "正碰", dummy_model)
        
        # 获取正面碰撞测试协议
        frontal_test_protocol = self.get_frontal_test_protocol(db, "GB 27887正碰")
        
        # 获取材料性能
        material_performance = self.get_all_material_performance(db)
        
        return {
            "dummy_params": dummy_params,
            "safety_thresholds": safety_thresholds,
            "frontal_test_protocol": frontal_test_protocol,
            "material_performance": material_performance
        }

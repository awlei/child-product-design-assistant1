"""
ECE R129 (欧标) 标准数据库 Manager
用于管理欧标ECE R129儿童约束系统相关数据
"""
from typing import List, Optional
from pydantic import BaseModel, Field
from sqlalchemy.orm import Session

from storage.database.shared.model import (
    ECE129BasicInfo,
    ECE129DummyParams,
    ECE129SideTestProtocol,
    ECE129CRSDesignLabel,
    ECE129SafetyThresholds,
    ECE129FitEnvelopeSize
)


# ==================== Pydantic Models ====================

class ECE129BasicInfoCreate(BaseModel):
    """创建法规基础信息"""
    reg_version: str = Field(..., description="法规版本")
    effective_date: str = Field(..., description="生效日期（YYYY-MM-DD）")
    core_changes: str = Field(..., description="核心变更内容")
    applicable_products: str = Field(..., description="适用产品类型")
    data_source: str = Field(..., description="数据来源")


class ECE129DummyParamsCreate(BaseModel):
    """创建假人参数"""
    dummy_model: str = Field(..., description="假人型号")
    weight_range: str = Field(..., description="适用体重范围")
    height_range: str = Field(..., description="适用身高范围")
    test_scenario: str = Field(..., description="测试场景")
    instrument_config: str = Field(..., description="关键仪器配置")
    compliance_threshold: str = Field(..., description="核心合规阈值")
    data_source: str = Field(..., description="数据来源")


class ECE129SideTestProtocolCreate(BaseModel):
    """创建侧面碰撞测试协议"""
    test_type: str = Field(..., description="测试类型")
    speed_requirement: str = Field(..., description="速度要求")
    test_bench_requirement: str = Field(..., description="测试台要求")
    foam_requirement: str = Field(..., description="泡沫要求")
    installation_requirement: str = Field(..., description="安装要求")
    env_condition: str = Field(..., description="环境条件")
    data_source: str = Field(..., description="数据来源")


class ECE129CRSDesignLabelCreate(BaseModel):
    """创建CRS设计与标识要求"""
    product_type: str = Field(..., description="产品类型")
    installation_constraint: str = Field(..., description="安装方向约束")
    core_design_size: str = Field(..., description="核心设计尺寸")
    label_requirement: str = Field(..., description="标签强制内容")
    data_source: str = Field(..., description="数据来源")


class ECE129SafetyThresholdsCreate(BaseModel):
    """创建安全合规阈值"""
    test_scenario: str = Field(..., description="测试场景")
    dummy_model: str = Field(..., description="假人类型")
    hic_limit: str = Field(..., description="HIC限值")
    chest_accel_limit: Optional[str] = Field(None, description="胸部加速度限值")
    head_excursion_limit: Optional[str] = Field(None, description="头部位移限值")
    neck_force_limit: Optional[str] = Field(None, description="颈部力限值")
    other_requirements: Optional[str] = Field(None, description="其他要求")
    data_source: str = Field(..., description="数据来源")


class ECE129FitEnvelopeSizeCreate(BaseModel):
    """创建CRS适配包络尺寸"""
    envelope_type: str = Field(..., description="包络类型")
    applicable_scenario: str = Field(..., description="适用场景")
    core_size: str = Field(..., description="核心尺寸")
    adapted_dummy: str = Field(..., description="适配假人")
    vehicle_install_requirement: str = Field(..., description="车辆安装要求")
    data_source: str = Field(..., description="数据来源")


# ==================== Manager Class ====================

class ECE129Manager:
    """ECE R129标准数据管理器"""

    def create_basic_info(self, db: Session, data_in: ECE129BasicInfoCreate) -> ECE129BasicInfo:
        """创建法规基础信息"""
        data = data_in.model_dump()
        from datetime import datetime
        if isinstance(data['effective_date'], str):
            data['effective_date'] = datetime.strptime(data['effective_date'], '%Y-%m-%d').date()
        
        db_data = ECE129BasicInfo(**data)
        db.add(db_data)
        try:
            db.commit()
            db.refresh(db_data)
            return db_data
        except Exception:
            db.rollback()
            raise

    def get_basic_info(self, db: Session, reg_version: str) -> Optional[ECE129BasicInfo]:
        """获取法规基础信息"""
        return db.query(ECE129BasicInfo).filter(
            ECE129BasicInfo.reg_version == reg_version
        ).first()

    def get_all_basic_info(self, db: Session) -> List[ECE129BasicInfo]:
        """获取所有法规基础信息"""
        return db.query(ECE129BasicInfo).order_by(
            ECE129BasicInfo.effective_date.desc()
        ).all()

    def create_dummy_params(self, db: Session, data_in: ECE129DummyParamsCreate) -> ECE129DummyParams:
        """创建假人参数"""
        data = data_in.model_dump()
        db_data = ECE129DummyParams(**data)
        db.add(db_data)
        try:
            db.commit()
            db.refresh(db_data)
            return db_data
        except Exception:
            db.rollback()
            raise

    def get_dummy_params(self, db: Session, dummy_model: str) -> Optional[ECE129DummyParams]:
        """获取假人参数"""
        return db.query(ECE129DummyParams).filter(
            ECE129DummyParams.dummy_model == dummy_model
        ).first()

    def get_all_dummy_params(self, db: Session, test_scenario: Optional[str] = None) -> List[ECE129DummyParams]:
        """获取所有假人参数（可按测试场景过滤）"""
        query = db.query(ECE129DummyParams)
        if test_scenario:
            query = query.filter(ECE129DummyParams.test_scenario == test_scenario)
        return query.all()

    def get_safety_thresholds(self, db: Session, test_scenario: str, dummy_model: str) -> Optional[ECE129SafetyThresholds]:
        """获取安全合规阈值"""
        return db.query(ECE129SafetyThresholds).filter(
            ECE129SafetyThresholds.test_scenario == test_scenario,
            ECE129SafetyThresholds.dummy_model == dummy_model
        ).first()

    def get_all_safety_thresholds(self, db: Session, test_scenario: Optional[str] = None) -> List[ECE129SafetyThresholds]:
        """获取所有安全合规阈值（可按测试场景过滤）"""
        query = db.query(ECE129SafetyThresholds)
        if test_scenario:
            query = query.filter(ECE129SafetyThresholds.test_scenario == test_scenario)
        return query.all()

    def get_crs_design_label(self, db: Session, product_type: str) -> Optional[ECE129CRSDesignLabel]:
        """获取CRS设计与标识要求"""
        return db.query(ECE129CRSDesignLabel).filter(
            ECE129CRSDesignLabel.product_type == product_type
        ).first()

    def get_all_crs_design_labels(self, db: Session) -> List[ECE129CRSDesignLabel]:
        """获取所有CRS设计与标识要求"""
        return db.query(ECE129CRSDesignLabel).all()

    def get_side_test_protocol(self, db: Session, test_type: str) -> Optional[ECE129SideTestProtocol]:
        """获取侧面碰撞测试协议"""
        return db.query(ECE129SideTestProtocol).filter(
            ECE129SideTestProtocol.test_type == test_type
        ).first()

    def get_all_side_test_protocols(self, db: Session) -> List[ECE129SideTestProtocol]:
        """获取所有侧面碰撞测试协议"""
        return db.query(ECE129SideTestProtocol).all()

    def get_fit_envelope_size(self, db: Session, envelope_type: str) -> Optional[ECE129FitEnvelopeSize]:
        """获取CRS适配包络尺寸"""
        return db.query(ECE129FitEnvelopeSize).filter(
            ECE129FitEnvelopeSize.envelope_type == envelope_type
        ).first()

    def get_all_fit_envelope_sizes(self, db: Session) -> List[ECE129FitEnvelopeSize]:
        """获取所有CRS适配包络尺寸"""
        return db.query(ECE129FitEnvelopeSize).all()

    def get_comprehensive_design_data(self, db: Session, dummy_model: str) -> dict:
        """获取综合设计数据（关联查询）"""
        # 获取假人参数
        dummy_params = self.get_dummy_params(db, dummy_model)
        
        # 获取安全阈值
        safety_thresholds = self.get_safety_thresholds(db, "侧碰", dummy_model)
        
        # 获取侧碰测试协议
        side_test_protocol = self.get_side_test_protocol(db, "ECE R129侧碰")
        
        return {
            "dummy_params": dummy_params,
            "safety_thresholds": safety_thresholds,
            "side_test_protocol": side_test_protocol
        }

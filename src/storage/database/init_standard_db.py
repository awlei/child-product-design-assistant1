"""
标准数据库初始化脚本
用于初始化FMVSS 213、ECE R129、GB 27887三个标准的数据库数据
"""
import sys
import os
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '../../..'))

from datetime import datetime
from coze_coding_dev_sdk.database import get_session
from src.storage.database.shared.model import (
    # FMVSS 213
    FMVSS213BasicInfo,
    FMVSS213DummyParams,
    FMVSS213FrontalTestProtocol,
    FMVSS213SideTestProtocol,
    FMVSS213CRSDesignLabel,
    FMVSS213MaterialPerformance,
    FMVSS213SafetyThresholds,
    FMVSS213FitEnvelopeSize,
    # ECE R129
    ECE129BasicInfo,
    ECE129DummyParams,
    ECE129SideTestProtocol,
    ECE129CRSDesignLabel,
    ECE129SafetyThresholds,
    ECE129FitEnvelopeSize,
    # GB 27887
    GB27887BasicInfo,
    GB27887DummyParams,
    GB27887FrontalTestProtocol,
    GB27887CRSDesignLabel,
    GB27887MaterialPerformance,
    GB27887SafetyThresholds,
    GB27887FitEnvelopeSize
)


def init_fmvss213_data(db):
    """初始化FMVSS 213数据"""
    print("正在初始化FMVSS 213数据...")
    
    # 法规基础信息
    fmvss213_basic_info = [
        FMVSS213BasicInfo(
            reg_version='FMVSS 213（现行）',
            effective_date=datetime(2024, 12, 5),
            core_changes='1. 注册卡简化（支持QR码）；2. 标签需标各使用模式的身高/体重限值；3. 增高座最小体重≥18.4kg；4. 限制注册页面广告内容',
            applicable_products='提篮、可转向CRS、前向安全带式CRS、增高座（HB/NB）',
            data_source='附件1§Regulatory Changes、附件3§12.C.1'
        ),
        FMVSS213BasicInfo(
            reg_version='FMVSS 213a（侧碰）',
            effective_date=datetime(2025, 6, 30),
            core_changes='1. 新增侧碰测试协议；2. 引入SISA测试台；3. Q3S/CRABI假人分级；4. 明确泡沫硬度要求；5. 侧碰门板无破裂要求',
            applicable_products='体重≤18.1kg或身高≤1100mm的CRS（提篮除外，≤30lb仅用CRABI测试）',
            data_source='附件1§FMVSS 213a、附件3§12.E'
        ),
        FMVSS213BasicInfo(
            reg_version='FMVSS 213b（正碰）',
            effective_date=datetime(2026, 12, 5),
            core_changes='1. 新增Type 2（腰带+肩带）测试；2. 禁用Hybrid II 6YO假人；3. 增高座HIC窗口0-175ms；4. 校车安全带Type 1测试延至2029.9.1',
            applicable_products='所有CRS（校车安全带仍用Type 1测试至2029.9.1）',
            data_source='附件1§FMVSS 213b、附件3§12.D.6.5'
        )
    ]
    
    # 假人参数
    fmvss213_dummy_params = [
        FMVSS213DummyParams(
            dummy_model='Newborn（572K）',
            weight_range='≤5kg（11lb）',
            height_range='≤650mm（26in）',
            test_scenario='正碰（提篮）',
            instrument_config='无内置仪器',
            compliance_threshold='无HIC要求，需完全约束头部/躯干',
            data_source='附件3§12.D.4.1、表8'
        ),
        FMVSS213DummyParams(
            dummy_model='CRABI 12MO（572R）',
            weight_range='5-13.6kg（11-30lb）',
            height_range='650-870mm（26-34in）',
            test_scenario='正碰/侧碰',
            instrument_config='头部3轴加速度计、躯干3轴加速度计',
            compliance_threshold='正碰：HIC≤390、胸部加速度≤55g；侧碰：仅评估头部是否碰门板',
            data_source='附件1§213a、附件3§12.E.4.1'
        ),
        FMVSS213DummyParams(
            dummy_model='Q3S（572W）',
            weight_range='13.6-18kg（30-40lb）',
            height_range='870-1100mm（34-43in）',
            test_scenario='侧碰',
            instrument_config='头部3轴加速度计、胸部IR-TRACC（测压缩）',
            compliance_threshold='HIC≤570、胸部压缩≤23mm（动态速率≤5mm/ms）、头部禁碰门板',
            data_source='附件1§213a、附件3§12.E.8.3'
        ),
        FMVSS213DummyParams(
            dummy_model='HIII-3YO（572P）',
            weight_range='10-18kg（22-40lb）',
            height_range='850-1100mm（34-43in）',
            test_scenario='正碰',
            instrument_config='头部3轴加速度计、躯干3轴加速度计',
            compliance_threshold='HIC≤1000、胸部加速度≤60g（3ms）',
            data_source='附件3§12.D.4.1、表8'
        )
    ]
    
    # 正面碰撞测试协议
    fmvss213_frontal_test_protocol = [
        FMVSS213FrontalTestProtocol(
            test_type='213 Config I',
            speed_requirement='48±3.2km/h（30±2mph）',
            acceleration_curve='上限：0ms→3g，10ms→25g，52ms→25g，90ms→0g；下限：4ms→0g，13ms→19g，46ms→19g，75ms→0g',
            test_bench_requirement='座椅泡沫：51mm厚25%压陷力20.4-24.9kg，102mm厚9.5-12.2kg；刚性杆1045钢需每次更换',
            installation_requirement='非增高座：LATCH张力53.5-67N，tether张力45-53.5N；增高座：Type II安全带张力9-18N',
            env_condition='温度20.6-22.2℃，湿度10-70%',
            data_source='附件1§213b、附件3§12.D.3'
        ),
        FMVSS213FrontalTestProtocol(
            test_type='213 Config II',
            speed_requirement='32±3.2km/h（20±2mph）',
            acceleration_curve='上限：9.5ms→14g，14ms→17g，20ms→17.7g；下限：9.5ms→9.4g，14ms→13.5g，20ms→14g',
            test_bench_requirement='同Config I，需模拟固定/活动表面"误用"场景（无tether）',
            installation_requirement='仅用Type I安全带，张力53.5-67N',
            env_condition='温度20.6-22.2℃，湿度10-70%',
            data_source='附件3§12.D.3.2、图12'
        )
    ]
    
    # 侧面碰撞测试协议
    fmvss213_side_test_protocol = [
        FMVSS213SideTestProtocol(
            test_type='213a侧碰',
            speed_requirement='相对速度30.66-31.94km/h（偏置10°）',
            test_bench_requirement='滑动座椅加速度：0ms→0.5g，6ms→25.5g，44ms→25.5g，58ms→0g；需4台高速相机（≥2000fps）',
            foam_honeycomb_requirement='座椅泡沫：51mm厚50%压陷力255-345N，102mm厚374-506N；铝蜂窝40mm厚，需每5次更换',
            installation_requirement='后向：仅LATCH（53.5-67N）；前向：LATCH+ tether（45-53.5N），tether动态张力≤80N',
            env_condition='温度20.6-22.2℃，湿度10-70%',
            data_source='附件1§213a、附件3§12.E.3'
        )
    ]
    
    # CRS设计与标识要求
    fmvss213_crs_design_label = [
        FMVSS213CRSDesignLabel(
            product_type='提篮（Infant Seat）',
            installation_constraint='仅后向，≤13.6kg（30lb）',
            core_design_size='头枕高度300-350，座宽280-320，靠背深度350',
            label_requirement='标"仅后向安装+禁用副驾"、体重≤13.6kg、泡沫硬度值；需含后向安装图+副驾禁用警示图（红色≥20mm×20mm）',
            registration_requirement='注册卡含QR码（链接NHTSA官方平台）、型号/序列号、制造商地址+电话；需验证邮箱真实性',
            data_source='附件1§213、附件3§12.C.1'
        ),
        FMVSS213CRSDesignLabel(
            product_type='可转向CRS（Convertible）',
            installation_constraint='后向≤105cm/18kg，正向≥105cm/18kg',
            core_design_size='Q6假人：头枕500-550，座宽440-480，靠背550-600；Q10假人：头枕550-600，座宽480-520',
            label_requirement='标安装方向切换条件、假人类型（Q6/Q10）、侧碰防护面积≥0.8㎡；需含LATCH安装图+Type 2安全带路由图',
            registration_requirement='注册卡需收集车辆VIN码（用于适配查询）；支持NHTSA App扫码注册',
            data_source='附件1§213、附件3§12.C.4'
        )
    ]
    
    # 材料性能要求
    fmvss213_material_performance = [
        FMVSS213MaterialPerformance(
            material_type='织带（约束CRS）',
            performance_requirement='断裂强度≥15000N；耐磨次数≥5000次；耐光（碳弧200h后强度≥60%）；动态摩擦系数≥0.35',
            test_standard='ASTM D3574、FMVSS 209（2025版）、ASTM D1894-2025',
            applicable_scenario='LATCH系统、tether带',
            data_source='附件3§12.B.2.2、12.B.2.3'
        ),
        FMVSS213MaterialPerformance(
            material_type='座椅泡沫',
            performance_requirement='51mm厚：25%压陷20.4-24.9kg，50%压陷255-345N；102mm厚：25%压陷9.5-12.2kg，50%压陷374-506N',
            test_standard='ASTM D3574（2025版）',
            applicable_scenario='FISA/SISA测试台、CRS座垫/靠背',
            data_source='附件1§213a、附件3§12.E.1.1'
        ),
        FMVSS213MaterialPerformance(
            material_type='主体框架（PP）',
            performance_requirement='抗冲击强度≥20kJ/m²；耐温-30~80℃；食品级（GB 6675有害物质）；耐老化（1000h）强度≥85%',
            test_standard='ISO 179、ASTM D756（2025版）、ISO 1879-2025',
            applicable_scenario='CRS主体结构',
            data_source='附件1§材料选型、附件3§12.B.3'
        )
    ]
    
    # 安全合规阈值
    fmvss213_safety_thresholds = [
        FMVSS213SafetyThresholds(
            test_scenario='正碰（213）',
            dummy_model='低龄（Q0-Q1.5）',
            hic_limit='≤390',
            chest_accel_limit='≤55g（3ms）',
            head_excursion_limit='后向≤720mm',
            other_requirements='靠背角度≤70°；颈部张力≤1800N（ISO 6487-2025）',
            data_source='附件1§213、附件3§12.D.8.3'
        ),
        FMVSS213SafetyThresholds(
            test_scenario='侧碰（213a）',
            dummy_model='Q3S',
            hic_limit='≤570',
            chest_compression_limit='≤23mm（动态速率≤5mm/ms）',
            head_excursion_limit='禁碰门板',
            other_requirements='侧防面积≥0.8㎡；门板碰撞后无破裂',
            data_source='附件1§213a、附件3§12.E.8.3'
        )
    ]
    
    # CRS适配包络尺寸
    fmvss213_fit_envelope_size = [
        FMVSS213FitEnvelopeSize(
            envelope_type='RS',
            applicable_scenario='RF（小）',
            core_size='500×440×350',
            adapted_dummy='CRABI 12MO',
            vehicle_install_requirement='后向安装空间≥600mm（距前座）；横向空间≥460mm（适配窄体车型）',
            data_source='附件2§Final Designs'
        ),
        FMVSS213FitEnvelopeSize(
            envelope_type='RM',
            applicable_scenario='RF（中）',
            core_size='600×480×400',
            adapted_dummy='Q3S/HIII-3YO',
            vehicle_install_requirement='后向安装空间≥700mm（距前座）',
            data_source='附件2§Final Designs'
        ),
        FMVSS213FitEnvelopeSize(
            envelope_type='FS',
            applicable_scenario='FF（小）',
            core_size='550×460×400',
            adapted_dummy='HIII-3YO',
            vehicle_install_requirement='正向安装空间≥650mm（距前座）；头枕高度≥500mm（兼容电动头枕车型）',
            data_source='附件2§Final Designs'
        )
    ]
    
    # 批量添加
    db.add_all(fmvss213_basic_info)
    db.add_all(fmvss213_dummy_params)
    db.add_all(fmvss213_frontal_test_protocol)
    db.add_all(fmvss213_side_test_protocol)
    db.add_all(fmvss213_crs_design_label)
    db.add_all(fmvss213_material_performance)
    db.add_all(fmvss213_safety_thresholds)
    db.add_all(fmvss213_fit_envelope_size)
    
    print("FMVSS 213数据初始化完成！")


def init_ece129_data(db):
    """初始化ECE R129数据"""
    print("正在初始化ECE R129数据...")
    
    # 法规基础信息
    ece129_basic_info = [
        ECE129BasicInfo(
            reg_version='ECE R129',
            effective_date=datetime(2013, 7, 1),
            core_changes='1. 基于体重的分类系统；2. 侧面碰撞测试；3. 增强型侧面防护；4. Q系列假人',
            applicable_products='提篮（i-Size）、可转向CRS、增高座',
            data_source='ECE R129法规文档'
        ),
        ECE129BasicInfo(
            reg_version='ECE R129修订版',
            effective_date=datetime(2020, 9, 1),
            core_changes='1. 修正部分HIC限值；2. 更新假人校准要求；3. 优化侧碰测试协议',
            applicable_products='提篮（i-Size）、可转向CRS、增高座',
            data_source='ECE R129修订法规'
        )
    ]
    
    # 假人参数
    ece129_dummy_params = [
        ECE129DummyParams(
            dummy_model='Q0',
            weight_range='0-10kg',
            height_range='0-67cm',
            test_scenario='正碰/侧碰',
            instrument_config='头部6轴力传感器、胸部加速度计',
            compliance_threshold='HIC≤390、胸部加速度≤55g（3ms）',
            data_source='ECE R129§7.1'
        ),
        ECE129DummyParams(
            dummy_model='Q3',
            weight_range='9-18kg',
            height_range='61-105cm',
            test_scenario='正碰/侧碰',
            instrument_config='头部6轴力传感器、胸部加速度计',
            compliance_threshold='HIC≤570、胸部加速度≤55g（3ms）',
            data_source='ECE R129§7.1'
        ),
        ECE129DummyParams(
            dummy_model='Q6',
            weight_range='15-25kg',
            height_range='100-125cm',
            test_scenario='正碰/侧碰',
            instrument_config='头部6轴力传感器、胸部加速度计',
            compliance_threshold='HIC≤650、胸部加速度≤55g（3ms）',
            data_source='ECE R129§7.1'
        ),
        ECE129DummyParams(
            dummy_model='Q10',
            weight_range='22-36kg',
            height_range='125-150cm',
            test_scenario='正碰/侧碰',
            instrument_config='头部6轴力传感器、胸部加速度计',
            compliance_threshold='HIC≤1000、胸部加速度≤60g（3ms）',
            data_source='ECE R129§7.1'
        )
    ]
    
    # 侧面碰撞测试协议
    ece129_side_test_protocol = [
        ECE129SideTestProtocol(
            test_type='ECE R129侧碰',
            speed_requirement='50km/h（相对）',
            test_bench_requirement='使用EuroSID-2型测试台，偏置角度10°',
            foam_requirement='座椅泡沫密度30-40kg/m³',
            installation_requirement='使用ISOFIX或安全带，张力符合规范',
            env_condition='温度20-22℃，湿度10-70%',
            data_source='ECE R129§7.1.2'
        )
    ]
    
    # CRS设计与标识要求
    ece129_crs_design_label = [
        ECE129CRSDesignLabel(
            product_type='提篮（i-Size）',
            installation_constraint='仅后向，≤13kg',
            core_design_size='头枕高度280-330，座宽260-300，靠背深度320',
            label_requirement='标i-Size标识、体重限值、后向安装图',
            data_source='ECE R129§5'
        ),
        ECE129CRSDesignLabel(
            product_type='可转向CRS',
            installation_constraint='后向≤105cm，正向≥100cm',
            core_design_size='头枕高度可调300-600，座宽350-450',
            label_requirement='标i-Size标识、体重/身高限值、安装方向切换图',
            data_source='ECE R129§5'
        )
    ]
    
    # 安全合规阈值
    ece129_safety_thresholds = [
        ECE129SafetyThresholds(
            test_scenario='侧碰',
            dummy_model='Q0',
            hic_limit='≤390',
            head_excursion_limit='≤550mm',
            other_requirements='门板无破裂、侧防结构完好',
            data_source='ECE R129§7.1.2'
        ),
        ECE129SafetyThresholds(
            test_scenario='侧碰',
            dummy_model='Q3',
            hic_limit='≤570',
            head_excursion_limit='≤650mm',
            other_requirements='门板无破裂、侧防结构完好',
            data_source='ECE R129§7.1.2'
        )
    ]
    
    # CRS适配包络尺寸
    ece129_fit_envelope_size = [
        ECE129FitEnvelopeSize(
            envelope_type='Compact',
            applicable_scenario='RF',
            core_size='480×420×330',
            adapted_dummy='Q0',
            vehicle_install_requirement='后向安装空间≥550mm（距前座）',
            data_source='ECE R129附录'
        ),
        ECE129FitEnvelopeSize(
            envelope_type='Standard',
            applicable_scenario='FF',
            core_size='520×440×400',
            adapted_dummy='Q6',
            vehicle_install_requirement='正向安装空间≥600mm（距前座）',
            data_source='ECE R129附录'
        )
    ]
    
    # 批量添加
    db.add_all(ece129_basic_info)
    db.add_all(ece129_dummy_params)
    db.add_all(ece129_side_test_protocol)
    db.add_all(ece129_crs_design_label)
    db.add_all(ece129_safety_thresholds)
    db.add_all(ece129_fit_envelope_size)
    
    print("ECE R129数据初始化完成！")


def init_gb27887_data(db):
    """初始化GB 27887数据"""
    print("正在初始化GB 27887数据...")
    
    # 法规基础信息
    gb27887_basic_info = [
        GB27887BasicInfo(
            reg_version='GB 27887-2011',
            effective_date=datetime(2011, 5, 1),
            core_changes='1. 基于GB 14166修订；2. 采用基于体重的分类；3. 增加侧面碰撞要求',
            applicable_products='提篮、可转向CRS、增高座',
            data_source='GB 27887-2011标准'
        ),
        GB27887BasicInfo(
            reg_version='GB 27887-2024',
            effective_date=datetime(2024, 12, 5),
            core_changes='1. 更新HIC限值至≤324；2. 引入Q系列假人；3. 优化测试协议',
            applicable_products='提篮、可转向CRS、增高座',
            data_source='GB 27887-2024标准'
        )
    ]
    
    # 假人参数
    gb27887_dummy_params = [
        GB27887DummyParams(
            dummy_model='Q0',
            weight_range='0-10kg',
            height_range='0-67cm',
            test_scenario='正碰',
            instrument_config='头部6轴力传感器、胸部加速度计',
            compliance_threshold='HIC≤324、胸部加速度≤55g（3ms）',
            data_source='GB 27887-2024§6.4'
        ),
        GB27887DummyParams(
            dummy_model='Q3',
            weight_range='9-18kg',
            height_range='61-105cm',
            test_scenario='正碰',
            instrument_config='头部6轴力传感器、胸部加速度计',
            compliance_threshold='HIC≤324、胸部加速度≤55g（3ms）',
            data_source='GB 27887-2024§6.4'
        ),
        GB27887DummyParams(
            dummy_model='Q6',
            weight_range='15-25kg',
            height_range='100-125cm',
            test_scenario='正碰',
            instrument_config='头部6轴力传感器、胸部加速度计',
            compliance_threshold='HIC≤324、胸部加速度≤55g（3ms）',
            data_source='GB 27887-2024§6.4'
        )
    ]
    
    # 正面碰撞测试协议
    gb27887_frontal_test_protocol = [
        GB27887FrontalTestProtocol(
            test_type='GB 27887正碰',
            speed_requirement='50km/h±2km/h',
            acceleration_curve='符合GB 14166要求',
            test_bench_requirement='使用符合GB 14166的测试台',
            installation_requirement='使用安全带或ISOFIX',
            env_condition='温度20-22℃，湿度10-70%',
            data_source='GB 27887-2024§6.3'
        )
    ]
    
    # CRS设计与标识要求
    gb27887_crs_design_label = [
        GB27887CRSDesignLabel(
            product_type='提篮',
            installation_constraint='仅后向，≤10kg',
            core_design_size='头枕高度280-320，座宽250-290，靠背深度300',
            label_requirement='标GB 27887标识、体重限值、安装图',
            registration_requirement='需含注册信息',
            data_source='GB 27887-2024§5'
        ),
        GB27887CRSDesignLabel(
            product_type='可转向CRS',
            installation_constraint='后向≤105cm，正向≥100cm',
            core_design_size='头枕高度可调300-550，座宽340-420',
            label_requirement='标GB 27887标识、体重/身高限值、安装图',
            data_source='GB 27887-2024§5'
        )
    ]
    
    # 材料性能要求
    gb27887_material_performance = [
        GB27887MaterialPerformance(
            material_type='织带',
            performance_requirement='断裂强度≥15000N',
            test_standard='GB/T 3923.1',
            applicable_scenario='安全带系统',
            data_source='GB 27887-2024§4.2'
        ),
        GB27887MaterialPerformance(
            material_type='座椅泡沫',
            performance_requirement='密度30-40kg/m³，硬度20-30Shore C',
            test_standard='GB/T 10808',
            applicable_scenario='CRS座垫',
            data_source='GB 27887-2024§4.2'
        )
    ]
    
    # 安全合规阈值
    gb27887_safety_thresholds = [
        GB27887SafetyThresholds(
            test_scenario='正碰',
            dummy_model='Q0',
            hic_limit='≤324',
            chest_accel_limit='≤55g（3ms）',
            head_excursion_limit='≤650mm',
            other_requirements='靠背角度≤70°',
            data_source='GB 27887-2024§6.4'
        ),
        GB27887SafetyThresholds(
            test_scenario='正碰',
            dummy_model='Q3',
            hic_limit='≤324',
            chest_accel_limit='≤55g（3ms）',
            head_excursion_limit='≤720mm',
            other_requirements='靠背角度≤70°',
            data_source='GB 27887-2024§6.4'
        )
    ]
    
    # CRS适配包络尺寸
    gb27887_fit_envelope_size = [
        GB27887FitEnvelopeSize(
            envelope_type='Compact',
            applicable_scenario='RF',
            core_size='470×400×320',
            adapted_dummy='Q0',
            vehicle_install_requirement='后向安装空间≥540mm（距前座）',
            data_source='GB 27887-2024附录'
        ),
        GB27887FitEnvelopeSize(
            envelope_type='Standard',
            applicable_scenario='FF',
            core_size='510×430×390',
            adapted_dummy='Q6',
            vehicle_install_requirement='正向安装空间≥590mm（距前座）',
            data_source='GB 27887-2024附录'
        )
    ]
    
    # 批量添加
    db.add_all(gb27887_basic_info)
    db.add_all(gb27887_dummy_params)
    db.add_all(gb27887_frontal_test_protocol)
    db.add_all(gb27887_crs_design_label)
    db.add_all(gb27887_material_performance)
    db.add_all(gb27887_safety_thresholds)
    db.add_all(gb27887_fit_envelope_size)
    
    print("GB 27887数据初始化完成！")


def main():
    """主函数：初始化所有标准数据"""
    db = get_session()
    try:
        # 初始化FMVSS 213数据
        init_fmvss213_data(db)
        
        # 初始化ECE R129数据
        init_ece129_data(db)
        
        # 初始化GB 27887数据
        init_gb27887_data(db)
        
        # 提交所有更改
        db.commit()
        print("\n所有标准数据库初始化完成！")
        
    except Exception as e:
        db.rollback()
        print(f"初始化失败：{str(e)}")
        raise
    finally:
        db.close()


if __name__ == "__main__":
    main()

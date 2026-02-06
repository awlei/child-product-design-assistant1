package com.childproduct.designassistant.model

/**
 * 最终设计方案结果（专业儿童产品设计工程师版）
 * 四大品类通用，严格匹配工程师工作流：适用标准→基础适配→设计参数→测试要求→专属测试
 * 所有参数可追溯，含标准条款/量化阈值/测试设备，直接对接实验室/CAD建模
 */
data class DesignResult(
    // 基础标识（内部使用，不展示）
    val standardCode: String,
    val productType: String,
    val childHeightCm: Int,
    val childWeightKg: Int,
    // 【适用标准】层级（醒目蓝色标签，含版本/实施要求）
    val standardName: String,           // 标准专业名称（如ECE R129:2021 (欧盟i-Size)）
    val standardVersion: String,        // 标准版本（如2021版）
    val standardImplement: String,      // 实施要求（如欧盟强制/中国2025强制）
    val standardKeyRequire: String,     // 标准核心要求（工程师快速了解重点）
    // 📊 基础适配数据（基于儿童身高/体重，含假人/尺寸范围）
    val dummyModel: String?,            // 假人模型（仅座椅/高脚椅，如Q3/HIII-3YO）
    val sizeRange: String,              // 尺寸适配范围（身高/体重/年龄）
    val materialRequire: String,        // 材料基础要求（如Q235钢/ISO 3795阻燃面料）
    val heightMatchTip: String,         // 身高匹配提示（如中值，适配性最优）
    // 📏 设计参数（四大品类专属，含GPS028/UN R16参数）
    val designParams: BaseDesignParams, // 密封类，各品类差异化参数
    // ⚖️ 测试要求（量化阈值+标准条款，四大品类通用）
    val dynamicCrashTests: List<DynamicCrashItem>,  // 动态碰撞测试（座椅/推车）
    val staticStrengthTests: List<StaticStrengthItem>, // 静态强度测试（所有品类）
    val materialSafetyTests: List<MaterialSafetyItem>, // 材料安全测试（所有品类）
    val specialTests: List<SpecialTestItem> // 品类专属测试（如推车刹车/床防护栏）
)

/**
 * 基础设计参数（密封类）→ 四大品类各自实现，工程师易扩展
 */
sealed class BaseDesignParams
// 儿童安全座椅设计参数（含GPS028/UN R16 Annex17）
data class ChildSeatDesignParams(
    val isofixEnvelopSize: String,
    val isofixEnvelopDetail: String,
    val headrestHeightRange: String,
    val seatWidth: String,
    val sideProtectionArea: String,
    val sideProtectionCover: String
) : BaseDesignParams()
// 婴儿推车设计参数（专业级，含稳定角/刹车类型）
data class BabyStrollerDesignParams(
    val wheelbase: String,        // 轴距（含公差）
    val brakeSystemType: String,  // 刹车类型（如双轮锁死/脚踏式）
    val foldingDimension: String, // 折叠尺寸（符合GB 14748限制）
    val stabilityAngle: String,   // 稳定角（≥15°，ECE R48要求）
    val handleHeightRange: String // 扶手高度调节范围
) : BaseDesignParams()
// 儿童高脚椅设计参数（专业级，含防倾倒/安全带类型）
data class HighChairDesignParams(
    val seatHeightAdjustRange: String, // 座椅高度调节范围
    val safetyBeltType: String,        // 安全带类型（如五点式/三点式）
    val tableEdgeDistance: String,     // 与桌面边缘距离（防夹手）
    val antiTipOverDimension: String   // 防倾倒尺寸（静态载荷≥500N）
) : BaseDesignParams()
// 儿童床设计参数（专业级，含防护栏间距/床架强度）
data class ChildBedDesignParams(
    val guardrailHeight: String,   // 防护栏高度（≥60cm，国标强制）
    val guardrailSpacing: String,  // 防护栏间距（≤6cm，防夹头）
    val mattressThickness: String, // 床垫厚度（≤10cm，防窒息）
    val bedFrameStrength: String   // 床架强度（静态载荷≥800N）
) : BaseDesignParams()

/**
 * 测试项实体类（专业级，含测试设备/条件/合格判据，直接对接实验室）
 */
// 动态碰撞测试（座椅/推车）
data class DynamicCrashItem(
    val testDevice: String,     // 测试设备（如HYGE电动碰撞台）
    val testCondition: String,  // 测试条件（速度/姿态/约束系统）
    val qualifiedCriteria: String // 合格判据（量化阈值，无模糊表述）
)
// 静态强度测试（所有品类）
data class StaticStrengthItem(
    val testItem: String,       // 测试项（如车架强度/防护栏强度）
    val loadRequire: String,    // 载荷要求（如纵向500N/横向300N）
    val qualifiedCriteria: String // 合格判据（变形量≤5mm/无断裂）
)
// 材料安全测试（所有品类，含阻燃/重金属）
data class MaterialSafetyItem(
    val materialType: String,   // 材料类型（如面料/塑料件/钢材）
    val testStd: String,        // 测试标准（如ISO 3795:2019/EN 71-3:2019）
    val qualifiedCriteria: String // 合格判据（量化阈值）
)
// 品类专属测试（如推车刹车/床折叠机构/高脚椅防倾倒）
data class SpecialTestItem(
    val testName: String,       // 测试名称
    val testMethod: String,     // 测试方法（如倾斜台测试/反复折叠500次）
    val qualifiedCriteria: String // 合格判据
)

/**
 * 多标准兼容建议（专业版）
 */
data class StdCompatibleTip(
    val stdCombination: String,         // 选中标准组合（如ECE R129+GB 27887-2024）
    val commonPoints: List<String>,     // 标准通用点（无需额外调整，直接兼容）
    val diffPoints: List<String>,       // 标准差异点（需针对性设计，满足最严要求）
    val designSuggest: List<String>     // 兼容设计建议（工程师可直接采纳，落地性强）
)

/**
 * UI状态封装（加载/成功/失败/空闲）
 * 设计方案生成状态管理，工程师可通过状态调试
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>() // 加载中（显示进度条）
    data class Success<out T>(val data: T) : UiState<T>() // 成功（返回设计结果）
    data class Error(val message: String) : UiState<Nothing>() // 失败（错误信息）
    object Idle : UiState<Nothing>() // 空闲（初始状态）
}

package com.childproduct.designassistant.data.model

import kotlinx.serialization.Serializable

/**
 * 儿童安全座椅设计方案数据模型
 *
 * 包含完整的设计方案信息，结构化展示设计参数、测试要求等
 */
@Serializable
data class DesignProposal(
    /** 产品类型（如：儿童安全座椅、婴儿提篮、餐椅等） */
    val productType: String,

    /** 适用标准列表 */
    val applicableStandards: List<String>,

    /** 基础适配数据 */
    val basicFitData: BasicFitData,

    /** 设计参数 */
    val designParameters: DesignParameters,

    /** 测试要求 */
    val testRequirements: TestRequirements,

    /** 标准测试项 */
    val standardTestItems: StandardTestItems,

    /** 生成时间 */
    val generatedAt: Long = System.currentTimeMillis()
)

/**
 * 基础适配数据
 */
@Serializable
data class BasicFitData(
    /** 假人信息 */
    val dummyInfo: DummyInfo
)

/**
 * 假人信息
 */
@Serializable
data class DummyInfo(
    /** 身高范围（mm） */
    val heightRange: String,

    /** 体重范围（kg） */
    val weightRange: String,

    /** 安装方向（如：前向、后向、双向） */
    val installationDirection: String,

    /** 假人类型（如：Q1.5、Q3、Q6、Q10等） */
    val dummyType: String? = null
)

/**
 * 设计参数
 */
@Serializable
data class DesignParameters(
    /** 头枕高度范围（mm） */
    val headrestHeightRange: String,

    /** 座宽（mm） */
    val seatWidth: String,

    /** 盒子Envelope（ISOFIX尺寸等级） */
    val envelope: String,

    /** 侧防面积（mm²） */
    val sideImpactProtectionArea: String,

    /** 其他设计参数（可选） */
    val additionalParameters: Map<String, String> = emptyMap()
)

/**
 * 测试要求
 */
@Serializable
data class TestRequirements(
    /** 正面碰撞要求 */
    val frontalImpact: String,

    /** 侧撞胸部压缩要求 */
    val sideImpactChestCompression: String,

    /** 织带强度要求 */
    val harnessStrength: String,

    /** 其他测试要求（可选） */
    val additionalRequirements: Map<String, String> = emptyMap()
)

/**
 * 标准测试项
 */
@Serializable
data class StandardTestItems(
    /** 动态碰撞：正碰 */
    val dynamicFrontal: String,

    /** 动态碰撞：后碰 */
    val dynamicRear: String,

    /** 动态碰撞：侧碰 */
    val dynamicSide: String,

    /** 阻燃测试 */
    val flammability: String,

    /** 其他测试项（可选） */
    val additionalTests: Map<String, String> = emptyMap()
)

/**
 * 设计方案生成请求
 */
@Serializable
data class DesignProposalRequest(
    /** 产品类型 */
    val productType: String,

    /** 选中的标准ID列表（产品类型 -> 标准ID列表） */
    val selectedStandards: Map<String, List<String>>,

    /** 选中的标准类型（ECE_R129、FMVSS_213、GB_27887_2024等） */
    val selectedStandardType: String? = null, // 新增：标准类型

    /** 用户输入的假人信息 */
    val userInputDummyInfo: UserDummyInput? = null,

    /** 其他用户需求 */
    val additionalRequirements: List<String> = emptyList()
)

/**
 * 用户输入的假人信息
 */
@Serializable
data class UserDummyInput(
    /** 目标身高范围（mm） */
    val targetHeightRange: String? = null,

    /** 目标体重范围（kg） */
    val targetWeightRange: String? = null,

    /** 目标安装方向 */
    val targetInstallationDirection: String? = null
)

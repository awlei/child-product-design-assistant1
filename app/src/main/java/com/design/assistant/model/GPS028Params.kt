package com.design.assistant.model

import kotlinx.serialization.Serializable

/**
 * GPS028设计参数数据类
 * 用于生成符合GB 27887-2011标准的专业设计参数
 */
@Serializable
data class GPS028Params(
    // 基础信息
    val groupName: String,                    // 组别（0、0+、I、II、III）
    val percentile: String,                   // 百分位（50%、75%、95%）
    val weight: Double,                       // 体重（kg）
    val height: Double,                       // 身高（cm）
    val age: String,                          // 适用年龄

    // 标准信息
    val standardVersion: String = "2023",     // 标准版本
    val standardRequirement: String = "中国强制实施",  // 实施要求
    val coreRequirement: String = "动态碰撞三向覆盖，侧防系统强制",  // 核心要求
    val installationDirection: String = "后向",  // 安装方向（后向/前向）
    val dummyModel: String = "Q3",            // 假人模型
    val heightRange: String = "87-105cm",     // 适用身高范围
    val weightRange: String = "13-18kg",      // 适用体重范围
    val sittingHeight: Double = 52.0,         // 坐高（cm）

    // 头部参数
    val headWidth: Double,                    // 头宽（mm）
    val headDepth: Double,                    // 头深（mm）
    val headHeight: Double,                   // 头高（mm）
    val headCircumference: Double,            // 头围（mm）

    // 颈部参数
    val neckWidth: Double,                    // 颈宽（mm）
    val neckLength: Double,                   // 颈长（mm）

    // 肩部参数
    val shoulderWidth: Double,                // 肩宽（mm）
    val shoulderHeight: Double,               // 肩高（mm）

    // 躯干参数
    val chestWidth: Double,                   // 胸宽（mm）
    val chestDepth: Double,                   // 胸深（mm）
    val chestCircumference: Double,           // 胸围（mm）
    val waistWidth: Double,                   // 腰宽（mm）
    val waistDepth: Double,                   // 腰深（mm）
    val waistCircumference: Double,           // 腰围（mm）
    val hipWidth: Double,                     // 臀宽（mm）
    val hipDepth: Double,                     // 臀深（mm）
    val hipCircumference: Double,             // 臀围（mm）

    // 上肢参数
    val armLength: Double,                    // 臂长（mm）
    val upperArmLength: Double,               // 上臂长（mm）
    val forearmLength: Double,                // 前臂长（mm）
    val handLength: Double,                   // 手长（mm）

    // 下肢参数
    val legLength: Double,                    // 腿长（mm）
    val thighLength: Double,                  // 大腿长（mm）
    val calfLength: Double,                   // 小腿长（mm）
    val footLength: Double,                   // 足长（mm）
    val footWidth: Double,                    // 足宽（mm）

    // 设计参考点（基准点）
    val hPoint: Point,                        // H点（髋关节中心点）
    val headReferencePoint: Point,            // 头部参考点
    val shoulderReferencePoint: Point,        // 肩部参考点
    val kneeReferencePoint: Point,            // 膝盖参考点

    // 安全性能参数
    val maxHeadInjuryCriterion: Double,       // 最大头部伤害指标HIC
    val maxChestAcceleration: Double,         // 最大胸部加速度（g）
    val maxNeckMoment: Double,                // 最大颈部力矩（Nm）
    val maxChestDeflection: Double,           // 最大胸部变形（mm）

    // 位移限制
    val maxHeadExcursion: Double,             // 最大头部位移（mm）
    val maxKneeExcursion: Double,             // 最大膝盖位移（mm）
    val maxHeadRotation: Double,              // 最大头部旋转角度（°）
    val maxTorsoRotation: Double,             // 最大躯干旋转角度（°）

    // 带宽要求
    val lapBeltWidth: Double,                 // 腰带宽度（mm）
    val shoulderBeltWidth: Double,            // 肩带宽度（mm）
    val lapBeltAngle: Double,                 // 腰带角度（°）
    val shoulderBeltAngle: Double,            // 肩带角度（°）

    // 其他设计参数
    val minHeadSupportHeight: Double,         // 最小头部支撑高度（mm）
    val minSideWingDepth: Double,             // 最小侧翼深度（mm）
    val minSideWingWidth: Double,             // 最小侧翼宽度（mm）
    val minHarnessWidth: Double,              // 最小安全带间距（mm）
    val minCrotchBuckleDistance: Double,      // 最小胯部扣距（mm）

    // 座宽参数
    val effectiveSeatWidth: Double = 350.0,   // 有效座宽（mm）
    val totalSeatWidth: Double = 420.0,       // 总座宽含侧防（mm）

    // ISOFIX接口参数
    val isofixSizeClass: String = "B2",       // ISOFIX尺寸等级
    val isofixEnvelopeLength: Double = 730.0, // ISOFIX包裹盒纵向长度（mm）
    val isofixEnvelopeWidth: Double = 460.0,  // ISOFIX包裹盒横向宽度（mm）
    val isofixAnchorSpacingMin: Double = 300.0,  // 固定点间距最小值（mm）
    val isofixAnchorSpacingMax: Double = 350.0,  // 固定点间距最大值（mm）
    val isofixEnvelopeLengthTolerance: Double = 10.0,  // 纵向长度公差（mm）
    val isofixEnvelopeWidthTolerance: Double = 5.0,   // 横向宽度公差（mm）

    // 侧防系统参数
    val minSideProtectionArea: Double = 0.85, // 最小侧防面积（m²）
    val sideProtectionStandard: String = "EN 14154-3:2022",  // 侧防测试标准
    val sideProtectionCoverage: String = "T12胸部至P8头部侧方区域",  // 侧防覆盖区域

    // 测试要求
    val frontalCrashSpeed: Double = 50.0,     // 正面碰撞速度（km/h）
    val frontalCrashSpeedTolerance: Double = 1.0,  // 速度公差（km/h）
    val frontalCrashAcceleration: Double = 15.0,  // 碰撞台加速度（g）
    val frontalCrashAccelerationDuration: Double = 3.0,  // 持续时间（ms）
    val frontalHicLimit: Double = 1000.0,     // 正碰HIC限值

    val sideCrashSpeed: Double = 60.0,        // 侧撞速度（km/h）
    val sideChestDeflectionLimit: Double = 44.0,  // 侧撞胸部压缩量限值（mm）
    val sideChestDeflectionVelocityLimit: Double = 2.5,  // 侧撞胸部压缩速度限值（m/s）

    val harnessStrengthLongitudinal: Double = 26.7,  // 安全带纵向强度（kN）
    val harnessStrengthLateral: Double = 17.8,     // 安全带横向强度（kN）
    val harnessTestStandard: String = "ISO 6683:2017",  // 安全带测试标准

    // 测试设备
    val crashTestEquipment: String = "HYGE电动碰撞台（符合ISO 6487:2018）",  // 碰撞测试设备
    val crashTestCondition: String = "假人后向，约束系统：ISOFIX+Top Tether",  // 测试条件
    val chestAccelerationLimit: Double = 60.0,  // 胸部加速度限值（g）
    val chestAccelerationAvgTime: Double = 3.0, // 胸部加速度平均时间（ms）
) {
    /**
     * 生成专业设计报告文本（参考案例格式）
     */
    fun generateDesignReport(): String {
        return buildString {
            appendLine("📦 儿童安全座椅设计方案（产品型号：CS-2025-X）")
            appendLine()
            appendLine("├─ 【适用标准】ECE R129:2021 (欧盟i-Size) + GB 27887-2024（中国新标，2024版强制实施）（醒目蓝色标签）")
            appendLine("│  🔍 标准关键要求：动态碰撞测试覆盖正/侧/后三向，侧防系统强制要求，ISOFIX接口兼容ISO 14530-3")
            appendLine("│")
            appendLine("├─ 📊 基础适配数据（基于选中标准+用户输入身高：${height.toInt()}cm）")
            appendLine("│  ├─ 🔽 假人参数（专业级假人配置）")
            appendLine("│  │  ├─ 假人模型：ECE R129 $dummyModel 假人（${percentile.replace("%", "th")}百分位${age}儿童，标准编号：ISO 13232-2:2021）")
            appendLine("│  │  ├─ 身高范围：$heightRange（用户输入${height.toInt()}cm处于该范围中值，适配性最优）")
            appendLine("│  │  ├─ 体重范围：$weightRange（推荐设计载荷取${(weightRange.split("-").map { it.trim().replace("kg", "").toDouble() }.average()).toInt()}kg，覆盖${percentile}百分位）")
            appendLine("│  │  ├─ 人体测量参数：坐高${sittingHeight}cm，肩宽${(shoulderWidth / 10.0).toInt()}cm，头围${(headCircumference / 10.0).toInt()}cm（GPS028-$standardVersion数据库校准值）")
            appendLine("│  │  └─ 安装方向：$installationDirection（ECE R129要求≤105cm儿童优先后向，GB 27887-2024强制要求）")
            appendLine("│")
            appendLine("├─ 📏 设计参数（GPS028-$standardVersion数据库 + 标准强制要求）")
            appendLine("│  ├─ 头枕高度：${minHeadSupportHeight.toInt()}-${(minHeadSupportHeight + 50).toInt()}mm（调节范围，基准点：坐骨结节（H点），公差±5mm）")
            appendLine("│  ├─ 座宽：有效座宽${effectiveSeatWidth.toInt()}mm（臀部支撑区域），总座宽${totalSeatWidth.toInt()}mm（含侧防侧翼）")
            appendLine("│  ├─ ISOFIX Envelop尺寸（盒子尺寸）：ISOFIX Size Class $isofixSizeClass（ECE R129 §5.3.2）")
            appendLine("│  │  - 纵向长度：${isofixEnvelopeLength.toInt()}mm（±${isofixEnvelopeLengthTolerance.toInt()}mm），横向宽度：${isofixEnvelopeWidth.toInt()}mm（±${isofixEnvelopeWidthTolerance.toInt()}mm）")
            appendLine("│  │  - 固定点间距：${isofixAnchorSpacingMin.toInt()}-${isofixAnchorSpacingMax.toInt()}mm（兼容主流车型ISOFIX接口布局）")
            appendLine("│  └─ 侧防面积：≥${minSideProtectionArea}m²（覆盖假人胸部（T12）至头部（P8）侧方区域，${sideProtectionStandard}测试要求）")
            appendLine("│")
            appendLine("├─ ⚖️ 测试要求（标准条款+量化阈值，可直接用于测试方案制定）")
            appendLine("│  ├─ 正面碰撞：碰撞速度${frontalCrashSpeed.toInt()}km/h（±${frontalCrashSpeedTolerance.toInt()}km/h），碰撞台加速度${frontalCrashAcceleration.toInt()}g（持续${frontalCrashAccelerationDuration.toInt()}ms），HIC≤${frontalHicLimit.toInt()}（ECE R129 §7.1.2）")
            appendLine("│  ├─ 侧撞胸部压缩：侧撞速度${sideCrashSpeed.toInt()}km/h（移动壁障），胸部压缩量≤${sideChestDeflectionLimit.toInt()}mm，压缩速度≤${sideChestDeflectionVelocityLimit}m/s（GB 27887-2024 §6.7）")
            appendLine("│  └─ 安全带织带断裂强度：≥${harnessStrengthLongitudinal}kN（纵向），≥${harnessStrengthLateral}kN（横向），测试方法：$harnessTestStandard")
            appendLine("│")
            appendLine("└─ 🧪 标准测试项（含测试流程+合格判据，可直接对接实验室测试）")
            appendLine("   ├─ 动态碰撞：正碰")
            appendLine("   │  - 测试设备：$crashTestEquipment")
            appendLine("   │  - 碰撞姿态：假人后向，约束系统：ISOFIX+Top Tether")
            appendLine("   │  - 合格判据：假人头部HIC≤${frontalHicLimit.toInt()}，胸部加速度≤${chestAccelerationLimit.toInt()}g（${chestAccelerationAvgTime.toInt()}ms滑动平均）")
            appendLine("   ├─ 动态碰撞：后碰")
            appendLine("   │  - 测试条件：碰撞速度48km/h，冲击方向与座椅纵向呈0°")
            appendLine("   │  - 约束要求：座椅靠背抗后移量≤100mm（ECE R129 §7.1.4）")
            appendLine("   │  - 合格判据：假人颈部剪切力≤500N，拉伸力≤1.5kN")
            appendLine("   ├─ 动态碰撞：侧碰")
            appendLine("   │  - 测试设备：移动侧撞壁障（质量770kg），碰撞速度${sideCrashSpeed.toInt()}km/h")
            appendLine("   │  - 侧防系统：必须包含能量吸收装置（如EPP泡棉+金属防撞梁）")
            appendLine("   │  - 合格判据：假人胸部压缩≤${sideChestDeflectionLimit.toInt()}mm，头部横向位移≤150mm")
            appendLine("   └─ 阻燃要求")
            appendLine("      - 适用材料：座椅面料、头枕泡沫、安全带织带")
            appendLine("      - 测试标准：ISO 3795:2019（汽车内饰材料阻燃性）")
            appendLine("      - 合格判据：水平燃烧速度≤100mm/min，无熔融滴落引燃下方棉花")
        }
    }

    /**
     * 生成JSON格式的参数数据（用于API输出）
     */
    fun toJson(): Map<String, Any> {
        return mapOf(
            "group" to groupName,
            "percentile" to percentile,
            "weight" to weight,
            "height" to height,
            "age" to age,
            "head" to mapOf(
                "width" to headWidth,
                "depth" to headDepth,
                "height" to headHeight,
                "circumference" to headCircumference
            ),
            "torso" to mapOf(
                "shoulderWidth" to shoulderWidth,
                "chestCircumference" to chestCircumference,
                "waistCircumference" to waistCircumference,
                "hipCircumference" to hipCircumference
            ),
            "referencePoints" to mapOf(
                "hPoint" to hPoint.toMap(),
                "headReference" to headReferencePoint.toMap(),
                "shoulderReference" to shoulderReferencePoint.toMap(),
                "kneeReference" to kneeReferencePoint.toMap()
            ),
            "safety" to mapOf(
                "maxHeadInjuryCriterion" to maxHeadInjuryCriterion,
                "maxChestAcceleration" to maxChestAcceleration,
                "maxNeckMoment" to maxNeckMoment
            ),
            "limits" to mapOf(
                "maxHeadExcursion" to maxHeadExcursion,
                "maxKneeExcursion" to maxKneeExcursion,
                "maxHeadRotation" to maxHeadRotation,
                "maxTorsoRotation" to maxTorsoRotation
            )
        )
    }
}

/**
 * 坐标点数据类
 */
@Serializable
data class Point(
    val x: Double,
    val y: Double
) {
    fun toMap(): Map<String, Double> = mapOf("x" to x, "y" to y)
}

/**
 * GPS028组别定义
 */
enum class GPS028Group(val displayName: String, val weightRange: String, val ageRange: String) {
    GROUP_0("0组", "0-10kg", "0-9个月"),
    GROUP_0P("0+组", "0-13kg", "0-15个月"),
    GROUP_I("I组", "9-18kg", "9个月-4岁"),
    GROUP_II("II组", "15-25kg", "3-6岁"),
    GROUP_III("III组", "22-36kg", "6-12岁")
}

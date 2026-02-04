package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 儿童高脚椅标准实体
 * 
 * 支持标准：
 * - EN 14988 (欧盟标准)
 * - GB 29281 (中国标准)
 */
@Entity(tableName = "high_chair_standard")
data class HighChairStandard(
    @PrimaryKey val standardId: String,      // 标准ID: en_14988, gb_29281
    val standardName: String,                // 标准名称
    val region: String,                      // 地区: EU, CN
    val version: String,                     // 版本号
    val effectiveDate: String,               // 生效日期
    val status: String,                      // 状态: ACTIVE, DEPRECATED
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 儿童高脚椅年龄段实体
 */
@Entity(
    tableName = "high_chair_age_group",
    foreignKeys = [
        ForeignKey(
            entity = HighChairStandard::class,
            parentColumns = ["standardId"],
            childColumns = ["standardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HighChairAgeGroup(
    @PrimaryKey val groupId: String,         // 年龄组ID
    val standardId: String,                  // 关联标准ID
    val ageRange: String,                    // 年龄范围: "6-36 months"
    val weightRangeKg: String,               // 重量范围: "max 15kg"
    val description: String,                 // 描述
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 儿童高脚椅安全要求实体
 */
@Entity(
    tableName = "high_chair_safety_requirement",
    foreignKeys = [
        ForeignKey(
            entity = HighChairStandard::class,
            parentColumns = ["standardId"],
            childColumns = ["standardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HighChairSafetyRequirement(
    @PrimaryKey val requirementId: String,   // 要求ID
    val standardId: String,                  // 关联标准ID
    val category: String,                    // 类别: stability, restraints, materials
    val requirementCode: String,             // 要求编号: "EN 14988 §5.1"
    val requirementDesc: String,             // 要求描述
    val testMethod: String?,                 // 测试方法
    val acceptanceCriteria: String?,         // 接受标准
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 儿童高脚椅稳定性要求实体
 */
@Entity(
    tableName = "high_chair_stability",
    foreignKeys = [
        ForeignKey(
            entity = HighChairStandard::class,
            parentColumns = ["standardId"],
            childColumns = ["standardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HighChairStability(
    @PrimaryKey val stabilityId: String,      // 稳定性ID
    val standardId: String,                  // 关联标准ID
    val testType: String,                    // 测试类型: tilt, tip-over
    val tiltAngleDegrees: Double?,           // 倾斜角度（度）
    val loadCondition: String,               // 载荷条件
    val stabilityCriteria: String,           // 稳定性标准
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 儿童高脚椅约束系统要求实体
 */
@Entity(
    tableName = "high_chair_restraint",
    foreignKeys = [
        ForeignKey(
            entity = HighChairStandard::class,
            parentColumns = ["standardId"],
            childColumns = ["standardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HighChairRestraint(
    @PrimaryKey val restraintId: String,      // 约束ID
    val standardId: String,                  // 关联标准ID
    val restraintType: String,                // 约束类型: harness, tray, crotch strap
    val minStrapWidthMm: Int?,               // 最小带宽度
    val minStrapThicknessMm: Int?,           // 最小带厚度
    val buckleRequirement: String,           // 卡扣要求
    val releaseForceLimitN: Double?,         // 释放力限制
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 标准数据初始化
 */
object HighChairStandardsData {
    
    /**
     * EN 14988 欧盟标准数据
     */
    val EN_14988_STANDARD = HighChairStandard(
        standardId = "en_14988",
        standardName = "EN 14988",
        region = "EU",
        version = "2017+A1:2020",
        effectiveDate = "2017-09-01",
        status = "ACTIVE"
    )

    /**
     * GB 29281 中国标准数据
     */
    val GB_29281_STANDARD = HighChairStandard(
        standardId = "gb_29281",
        standardName = "GB 29281",
        region = "CN",
        version = "2012",
        effectiveDate = "2012-12-31",
        status = "ACTIVE"
    )

    /**
     * 年龄组数据
     */
    val AGE_GROUPS = listOf(
        HighChairAgeGroup(
            groupId = "age_6_36",
            standardId = "en_14988",
            ageRange = "6-36 months",
            weightRangeKg = "max 15kg",
            description = "能够独坐至36个月的儿童"
        ),
        HighChairAgeGroup(
            groupId = "age_6_36_cn",
            standardId = "gb_29281",
            ageRange = "6-36 months",
            weightRangeKg = "max 15kg",
            description = "能够独坐至36个月的儿童"
        )
    )

    /**
     * 安全要求数据
     */
    val SAFETY_REQUIREMENTS = listOf(
        // EN 14988 要求
        HighChairSafetyRequirement(
            requirementId = "req_stability_en",
            standardId = "en_14988",
            category = "stability",
            requirementCode = "EN 14988 §5.1",
            requirementDesc = "高脚椅在各种载荷条件下不得倾倒",
            testMethod = "倾斜测试和倾倒测试",
            acceptanceCriteria = "在10°倾斜角度下不得倾倒"
        ),
        HighChairSafetyRequirement(
            requirementId = "req_restraint_en",
            standardId = "en_14988",
            category = "restraints",
            requirementCode = "EN 14988 §5.2",
            requirementDesc = "必须配备有效的约束系统防止儿童滑落",
            testMethod = "动态测试",
            acceptanceCriteria = "儿童在任何情况下不得脱离约束"
        ),
        HighChairSafetyRequirement(
            requirementId = "req_materials_en",
            standardId = "en_14988",
            category = "materials",
            requirementCode = "EN 14988 §5.3",
            requirementDesc = "材料不得含有有害物质，边角应圆滑",
            testMethod = "化学测试和边缘测试",
            acceptanceCriteria = "符合EN 71-3有害物质限量要求"
        ),
        // GB 29281 要求
        HighChairSafetyRequirement(
            requirementId = "req_stability_cn",
            standardId = "gb_29281",
            category = "stability",
            requirementCode = "GB 29281 §5.1",
            requirementDesc = "高脚椅应具有足够的稳定性",
            testMethod = "稳定性测试",
            acceptanceCriteria = "在规定倾斜角度下不得倾倒"
        ),
        HighChairSafetyRequirement(
            requirementId = "req_restraint_cn",
            standardId = "gb_29281",
            category = "restraints",
            requirementCode = "GB 29281 §5.2",
            requirementDesc = "应配备安全带或其他约束装置",
            testMethod = "约束测试",
            acceptanceCriteria = "儿童不得脱离约束"
        )
    )

    /**
     * 稳定性数据
     */
    val STABILITY_DATA = listOf(
        HighChairStability(
            stabilityId = "stability_tilt_en",
            standardId = "en_14988",
            testType = "tilt",
            tiltAngleDegrees = 10.0,
            loadCondition = "最大载荷15kg",
            stabilityCriteria = "不得倾倒"
        ),
        HighChairStability(
            stabilityId = "stability_tilt_cn",
            standardId = "gb_29281",
            testType = "tilt",
            tiltAngleDegrees = 10.0,
            loadCondition = "最大载荷15kg",
            stabilityCriteria = "不得倾倒"
        )
    )

    /**
     * 约束系统数据
     */
    val RESTRAINT_DATA = listOf(
        HighChairRestraint(
            restraintId = "restraint_harness_en",
            standardId = "en_14988",
            restraintType = "harness",
            minStrapWidthMm = 20,
            minStrapThicknessMm = 2,
            buckleRequirement = "成人无法单手释放，儿童无法释放",
            releaseForceLimitN = null
        ),
        HighChairRestraint(
            restraintId = "restraint_harness_cn",
            standardId = "gb_29281",
            restraintType = "harness",
            minStrapWidthMm = 20,
            minStrapThicknessMm = 2,
            buckleRequirement = "需要成人操作才能释放",
            releaseForceLimitN = null
        )
    )
}

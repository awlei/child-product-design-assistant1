package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 儿童床标准实体
 * 
 * 支持标准：
 * - EN 716 (欧盟标准)
 * - GB 28007 (中国标准)
 */
@Entity(tableName = "crib_standard")
data class CribStandard(
    @PrimaryKey val standardId: String,      // 标准ID: en_716, gb_28007
    val standardName: String,                // 标准名称
    val region: String,                      // 地区: EU, CN
    val version: String,                     // 版本号
    val effectiveDate: String,               // 生效日期
    val status: String,                      // 状态: ACTIVE, DEPRECATED
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 儿童床尺寸要求实体
 */
@Entity(
    tableName = "crib_dimension",
    foreignKeys = [
        ForeignKey(
            entity = CribStandard::class,
            parentColumns = ["standardId"],
            childColumns = ["standardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CribDimension(
    @PrimaryKey val dimensionId: String,      // 尺寸ID
    val standardId: String,                  // 关联标准ID
    val dimensionType: String,               // 尺寸类型: internal, external, mattress
    val minLengthMm: Int?,                    // 最小长度
    val maxLengthMm: Int?,                    // 最大长度
    val minWidthMm: Int?,                     // 最小宽度
    val maxWidthMm: Int?,                     // 最大宽度
    val minHeightMm: Int?,                    // 最小高度
    val maxHeightMm: Int?,                    // 最大高度
    val description: String,                 // 描述
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 儿童床床垫间隙要求实体
 */
@Entity(
    tableName = "crib_mattress_gap",
    foreignKeys = [
        ForeignKey(
            entity = CribStandard::class,
            parentColumns = ["standardId"],
            childColumns = ["standardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CribMattressGap(
    @PrimaryKey val gapId: String,            // 间隙ID
    val standardId: String,                   // 关联标准ID
    val location: String,                     // 位置: side, end, corner
    val maxGapMm: Double,                     // 最大间隙（毫米）
    val testMethod: String,                   // 测试方法
    val acceptanceCriteria: String,           // 接受标准
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 儿童床栏杆要求实体
 */
@Entity(
    tableName = "crib_railing",
    foreignKeys = [
        ForeignKey(
            entity = CribStandard::class,
            parentColumns = ["standardId"],
            childColumns = ["standardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CribRailing(
    @PrimaryKey val railingId: String,        // 栏杆ID
    val standardId: String,                   // 关联标准ID
    val railingType: String,                  // 栏杆类型: side, end
    val minHeightMm: Int?,                    // 最小高度
    val minSpacingMm: Int?,                   // 最小间距
    val maxSpacingMm: Int?,                   // 最大间距
    val strengthRequirement: String,          // 强度要求
    val testForceN: Double?,                  // 测试力值（牛顿）
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 儿童床安全要求实体
 */
@Entity(
    tableName = "crib_safety_requirement",
    foreignKeys = [
        ForeignKey(
            entity = CribStandard::class,
            parentColumns = ["standardId"],
            childColumns = ["standardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CribSafetyRequirement(
    @PrimaryKey val requirementId: String,   // 要求ID
    val standardId: String,                  // 关联标准ID
    val category: String,                    // 类别: dimensions, gaps, railings, materials
    val requirementCode: String,             // 要求编号: "EN 716 §4.1"
    val requirementDesc: String,             // 要求描述
    val testMethod: String?,                 // 测试方法
    val acceptanceCriteria: String?,         // 接受标准
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 标准数据初始化
 */
object CribStandardsData {
    
    /**
     * EN 716 欧盟标准数据
     */
    val EN_716_STANDARD = CribStandard(
        standardId = "en_716",
        standardName = "EN 716",
        region = "EU",
        version = "2017+A1:2020",
        effectiveDate = "2017-04-01",
        status = "ACTIVE"
    )

    /**
     * GB 28007 中国标准数据
     */
    val GB_28007_STANDARD = CribStandard(
        standardId = "gb_28007",
        standardName = "GB 28007",
        region = "CN",
        version = "2011",
        effectiveDate = "2012-08-01",
        status = "ACTIVE"
    )

    /**
     * 尺寸要求数据
     */
    val DIMENSIONS = listOf(
        // EN 716 尺寸
        CribDimension(
            dimensionId = "dim_internal_en",
            standardId = "en_716",
            dimensionType = "internal",
            minLengthMm = 900,
            maxLengthMm = 1400,
            minWidthMm = 600,
            maxWidthMm = 800,
            description = "内部尺寸（床垫放置区域）"
        ),
        CribDimension(
            dimensionId = "dim_mattress_en",
            standardId = "en_716",
            dimensionType = "mattress",
            minLengthMm = 900,
            maxLengthMm = 1400,
            minWidthMm = 600,
            maxWidthMm = 800,
            minHeightMm = 100,
            description = "床垫尺寸"
        ),
        // GB 28007 尺寸
        CribDimension(
            dimensionId = "dim_internal_cn",
            standardId = "gb_28007",
            dimensionType = "internal",
            minLengthMm = 900,
            maxLengthMm = 1400,
            minWidthMm = 600,
            maxWidthMm = 800,
            description = "内部尺寸"
        ),
        CribDimension(
            dimensionId = "dim_mattress_cn",
            standardId = "gb_28007",
            dimensionType = "mattress",
            minLengthMm = 900,
            maxLengthMm = 1400,
            minWidthMm = 600,
            maxWidthMm = 800,
            minHeightMm = 100,
            description = "床垫尺寸"
        )
    )

    /**
     * 床垫间隙要求数据
     */
    val MATTRESS_GAPS = listOf(
        CribMattressGap(
            gapId = "gap_side_en",
            standardId = "en_716",
            location = "side",
            maxGapMm = 30.0,
            testMethod = "使用探针测试",
            acceptanceCriteria = "探针不得通过"
        ),
        CribMattressGap(
            gapId = "gap_side_cn",
            standardId = "gb_28007",
            location = "side",
            maxGapMm = 30.0,
            testMethod = "使用探针测试",
            acceptanceCriteria = "探针不得通过"
        )
    )

    /**
     * 栏杆要求数据
     */
    val RAILINGS = listOf(
        CribRailing(
            railingId = "railing_side_en",
            standardId = "en_716",
            railingType = "side",
            minHeightMm = 600,
            minSpacingMm = 45,
            maxSpacingMm = 65,
            strengthRequirement = "栏杆应能承受规定的测试力",
            testForceN = 250.0
        ),
        CribRailing(
            railingId = "railing_side_cn",
            standardId = "gb_28007",
            railingType = "side",
            minHeightMm = 600,
            minSpacingMm = 45,
            maxSpacingMm = 65,
            strengthRequirement = "栏杆应能承受规定的测试力",
            testForceN = 200.0
        )
    )

    /**
     * 安全要求数据
     */
    val SAFETY_REQUIREMENTS = listOf(
        // EN 716 要求
        CribSafetyRequirement(
            requirementId = "req_dim_en",
            standardId = "en_716",
            category = "dimensions",
            requirementCode = "EN 716 §4.1",
            requirementDesc = "床的内部尺寸应适合标准床垫",
            testMethod = "尺寸测量",
            acceptanceCriteria = "内部尺寸应在规定范围内"
        ),
        CribSafetyRequirement(
            requirementId = "req_gap_en",
            standardId = "en_716",
            category = "gaps",
            requirementCode = "EN 716 §4.2",
            requirementDesc = "床垫与床体之间的间隙不得卡住儿童",
            testMethod = "间隙测试",
            acceptanceCriteria = "最大间隙不超过30mm"
        ),
        CribSafetyRequirement(
            requirementId = "req_railing_en",
            standardId = "en_716",
            category = "railings",
            requirementCode = "EN 716 §4.3",
            requirementDesc = "栏杆应有足够高度防止儿童攀爬",
            testMethod = "高度测试和强度测试",
            acceptanceCriteria = "栏杆高度不低于600mm，间距45-65mm"
        ),
        // GB 28007 要求
        CribSafetyRequirement(
            requirementId = "req_dim_cn",
            standardId = "gb_28007",
            category = "dimensions",
            requirementCode = "GB 28007 §5.1",
            requirementDesc = "床的内部尺寸应适合标准床垫",
            testMethod = "尺寸测量",
            acceptanceCriteria = "内部尺寸应在规定范围内"
        ),
        CribSafetyRequirement(
            requirementId = "req_gap_cn",
            standardId = "gb_28007",
            category = "gaps",
            requirementCode = "GB 28007 §5.2",
            requirementDesc = "床垫与床体之间的间隙不得卡住儿童",
            testMethod = "间隙测试",
            acceptanceCriteria = "最大间隙不超过30mm"
        ),
        CribSafetyRequirement(
            requirementId = "req_railing_cn",
            standardId = "gb_28007",
            category = "railings",
            requirementCode = "GB 28007 §5.3",
            requirementDesc = "栏杆应有足够高度防止儿童攀爬",
            testMethod = "高度测试和强度测试",
            acceptanceCriteria = "栏杆高度不低于600mm，间距45-65mm"
        )
    )
}

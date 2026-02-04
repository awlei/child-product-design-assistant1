package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import com.childproduct.designassistant.model.InstallDirection

/**
 * 假人类型实体
 * 基于UN R129 Annex 19标准（Rev.5，2022版）
 */
@Entity(
    tableName = "crash_test_dummy",
    indices = [Index(value = ["dummyCode"], unique = true)]
)
data class CrashTestDummy(
    @PrimaryKey val dummyId: String,          // 唯一ID: DUMMY_Q0, DUMMY_Q1...
    val dummyCode: String,                    // 假人代码: Q0, Q1, Q1.5, Q3, Q3s, Q6, Q10
    val dummyName: String,                    // 中文名称: 新生儿, 幼儿...
    val minHeightCm: Int,                     // 最小身高(cm)
    val maxHeightCm: Int,                     // 最大身高(cm)
    val ageRange: String,                     // 年龄范围: "0-6个月"
    val productGroup: String,                 // 产品组: Group 0+, Group 1...
    val installDirection: InstallDirection,   // 安装方向: REARWARD/FORWARD
    val description: String,                  // 详细描述
    val standardClause: String,               // 标准条款引用: "UN R129 Annex 19 §4.1"
    val weightKg: Double,                     // 假人重量(kg)
    val headCircumferenceMm: Int?,            // 头围(mm)
    val shoulderWidthMm: Int?,                // 肩宽(mm)
    val sittingHeightMm: Int?,                // 坐高(mm)
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    // 验证身高范围是否符合UN R129 Rev.5标准
    fun validateHeightRange(): Boolean {
        return when (dummyCode) {
            "Q0" -> minHeightCm == 40 && maxHeightCm == 50
            "Q0+" -> minHeightCm == 50 && maxHeightCm == 60
            "Q1" -> minHeightCm == 60 && maxHeightCm == 75
            "Q1.5" -> minHeightCm == 75 && maxHeightCm == 87
            "Q3" -> minHeightCm == 87 && maxHeightCm == 105
            "Q6" -> minHeightCm == 105 && maxHeightCm == 125
            "Q10" -> minHeightCm == 125 && maxHeightCm == 145
            else -> false
        }
    }

    // 获取适用的安装方式
    fun getRequiredInstallationMethods(): List<String> {
        return when (installDirection) {
            InstallDirection.REARWARD -> listOf("ISOFIX + 支撑腿", "ISOFIX 2点 + 支撑腿")
            InstallDirection.FORWARD -> listOf("ISOFIX + Top-tether", "ISOFIX 2点 + Top-tether")
        }
    }

    companion object {
        // 标准假人配置（基于UN R129 Rev.4）
        val STANDARD_DUMMIES = listOf(
            CrashTestDummy(
                dummyId = "DUMMY_Q0",
                dummyCode = "Q0",
                dummyName = "新生儿",
                minHeightCm = 40,
                maxHeightCm = 50,
                ageRange = "0-6个月",
                productGroup = "Group 0+",
                installDirection = InstallDirection.REARWARD,
                description = "Q0假人用于40-50cm身高范围新生儿",
                standardClause = "UN R129 Annex 19 §4.1",
                weightKg = 3.47,
                headCircumferenceMm = 360,
                shoulderWidthMm = 145,
                sittingHeightMm = 255
            ),
            CrashTestDummy(
                dummyId = "DUMMY_Q0_PLUS",
                dummyCode = "Q0+",
                dummyName = "婴儿",
                minHeightCm = 50,
                maxHeightCm = 60,
                ageRange = "3-15个月",
                productGroup = "Group 0+",
                installDirection = InstallDirection.REARWARD,
                description = "Q0+假人用于50-60cm身高范围婴儿，强制后向安装",
                standardClause = "UN R129 Annex 19 §4.2",
                weightKg = 7.0,
                headCircumferenceMm = 395,
                shoulderWidthMm = 160,
                sittingHeightMm = 285
            ),
            CrashTestDummy(
                dummyId = "DUMMY_Q1",
                dummyCode = "Q1",
                dummyName = "幼儿",
                minHeightCm = 60,
                maxHeightCm = 75,
                ageRange = "9-18个月",
                productGroup = "Group I",
                installDirection = InstallDirection.REARWARD,
                description = "Q1假人用于60-75cm身高范围幼儿",
                standardClause = "UN R129 Annex 19 §4.3",
                weightKg = 9.0,
                headCircumferenceMm = 430,
                shoulderWidthMm = 175,
                sittingHeightMm = 315
            ),
            CrashTestDummy(
                dummyId = "DUMMY_Q1_5",
                dummyCode = "Q1.5",
                dummyName = "学步儿童",
                minHeightCm = 75,
                maxHeightCm = 87,
                ageRange = "18-36个月",
                productGroup = "Group I",
                installDirection = InstallDirection.REARWARD,
                description = "Q1.5假人用于75-87cm身高范围学步儿童，强制后向安装",
                standardClause = "UN R129 Annex 19 §4.4",
                weightKg = 11.0,
                headCircumferenceMm = 455,
                shoulderWidthMm = 185,
                sittingHeightMm = 340
            ),
            CrashTestDummy(
                dummyId = "DUMMY_Q3",
                dummyCode = "Q3",
                dummyName = "幼童",
                minHeightCm = 87,
                maxHeightCm = 105,
                ageRange = "36-48个月",
                productGroup = "Group I/II",
                installDirection = InstallDirection.REARWARD,
                description = "Q3假人用于87-105cm身高范围幼童，可后向或前向安装",
                standardClause = "UN R129 Annex 19 §4.5",
                weightKg = 15.0,
                headCircumferenceMm = 485,
                shoulderWidthMm = 200,
                sittingHeightMm = 370
            ),
            CrashTestDummy(
                dummyId = "DUMMY_Q6",
                dummyCode = "Q6",
                dummyName = "学龄儿童",
                minHeightCm = 105,
                maxHeightCm = 125,
                ageRange = "48-72个月",
                productGroup = "Group II",
                installDirection = InstallDirection.FORWARD,
                description = "Q6假人用于105-125cm身高范围学龄儿童，前向安装",
                standardClause = "UN R129 Annex 19 §4.6",
                weightKg = 21.0,
                headCircumferenceMm = 540,
                shoulderWidthMm = 280,
                sittingHeightMm = 445
            ),
            CrashTestDummy(
                dummyId = "DUMMY_Q10",
                dummyCode = "Q10",
                dummyName = "青少年",
                minHeightCm = 125,
                maxHeightCm = 145,
                ageRange = "72-144个月",
                productGroup = "Group III",
                installDirection = InstallDirection.FORWARD,
                description = "Q10假人用于125-145cm身高范围青少年，前向安装",
                standardClause = "UN R129 Annex 19 §4.7",
                weightKg = 35.58,
                headCircumferenceMm = 560,
                shoulderWidthMm = 335,
                sittingHeightMm = 473
            )
        )
    }
}

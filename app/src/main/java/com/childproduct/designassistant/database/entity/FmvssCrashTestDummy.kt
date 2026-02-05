package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import com.childproduct.designassistant.model.InstallDirection

/**
 * FMVSS 213 专属假人类型实体
 * 物理隔离：仅存储FMVSS标准的假人数据（Q3s, HIII-3y, HIII-6y, HIII-10y等）
 * 不包含任何ECE假人（Q0, Q1, Q1.5, Q3, Q6, Q10）
 */
@Entity(
    tableName = "fmvss_crash_test_dummy",
    indices = [
        Index(value = ["dummyCode"], unique = true)
    ]
)
data class FmvssCrashTestDummy(
    @PrimaryKey val dummyId: String,          // 唯一ID: FMVSS_Q3S, FMVSS_HIII_3Y...
    val dummyCode: String,                    // 假人代码: Q3s, 3y, 6y, 10y
    val dummyName: String,                    // 中文名称: Q3s假人, 3岁Hybrid III假人...
    val minHeightIn: Int,                     // 最小高度(inches) - FMVSS使用英寸
    val maxHeightIn: Int,                     // 最大高度(inches)
    val weightLb: Double,                     // 重量(pounds) - FMVSS使用磅
    val ageRange: String,                     // 年龄范围: "1-3岁", "3-6岁"...
    val fmvssTestStandard: String,            // FMVSS测试标准: "FMVSS 213", "FMVSS 213a"
    val installDirection: InstallDirection,   // 安装方向: REARWARD/FORWARD
    val description: String,                  // 详细描述
    val standardClause: String,               // 标准条款引用: "FMVSS 213 S6.1"
    val chestDeflectionLimitIn: Double?,      // 胸部压缩限制(inches)
    val fmvssImpactVelocityMph: Double,       // FMVSS碰撞速度(mph)
    val fmvssTestConfiguration: String,       // FMVSS专属测试配置
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * 验证是否为纯FMVSS假人（不包含Q0, Q1等ECE假人）
     */
    fun validateIsPureFmvss(): Boolean {
        val validFmvssCodes = listOf("Q3s", "3y", "6y", "10y", "3yr", "6yr", "10yr")
        return dummyCode.lowercase().replace("y", "yr") in validFmvssCodes.map { it.lowercase().replace("y", "yr") }
    }

    /**
     * 将英寸转换为厘米
     */
    fun toHeightCm(): Pair<Int, Int> {
        return (minHeightIn * 2.54).toInt() to (maxHeightIn * 2.54).toInt()
    }

    /**
     * 将磅转换为千克
     */
    fun toWeightKg(): Double {
        return weightLb * 0.453592
    }

    companion object {
        /**
         * 创建FMVSS Q3s假人（侧碰专用，对应3岁儿童）
         */
        fun createQ3s() = FmvssCrashTestDummy(
            dummyId = "FMVSS_Q3S",
            dummyCode = "Q3s",
            dummyName = "Q3s 侧碰假人（3岁）",
            minHeightIn = 34,
            maxHeightIn = 40,
            weightLb = 33.0,
            ageRange = "1-3岁",
            fmvssTestStandard = "FMVSS 213a",
            installDirection = InstallDirection.FORWARD,
            description = "FMVSS 213a侧碰测试专用假人，对应3岁儿童（34-40英寸，33磅）",
            standardClause = "FMVSS 213a S5.2",
            chestDeflectionLimitIn = 2.0,
            fmvssImpactVelocityMph = 30.0,
            fmvssTestConfiguration = "侧碰30mph，冲击角度20度"
        )

        /**
         * 创建FMVSS 3岁Hybrid III假人（正碰）
         */
        fun create3yrHybridIII() = FmvssCrashTestDummy(
            dummyId = "FMVSS_HIII_3Y",
            dummyCode = "3y",
            dummyName = "3岁Hybrid III假人",
            minHeightIn = 34,
            maxHeightIn = 40,
            weightLb = 33.0,
            ageRange = "1-3岁",
            fmvssTestStandard = "FMVSS 213",
            installDirection = InstallDirection.FORWARD,
            description = "FMVSS正碰测试用3岁Hybrid III假人",
            standardClause = "FMVSS 213 S6.1",
            chestDeflectionLimitIn = 2.0,
            fmvssImpactVelocityMph = 30.0,
            fmvssTestConfiguration = "正碰30mph，48km/h，固定滑台"
        )

        /**
         * 创建FMVSS 6岁Hybrid III假人
         */
        fun create6yrHybridIII() = FmvssCrashTestDummy(
            dummyId = "FMVSS_HIII_6Y",
            dummyCode = "6y",
            dummyName = "6岁Hybrid III假人",
            minHeightIn = 48,
            maxHeightIn = 56,
            weightLb = 55.0,
            ageRange = "4-7岁",
            fmvssTestStandard = "FMVSS 213",
            installDirection = InstallDirection.FORWARD,
            description = "FMVSS测试用6岁Hybrid III假人",
            standardClause = "FMVSS 213 S6.2",
            chestDeflectionLimitIn = 2.0,
            fmvssImpactVelocityMph = 30.0,
            fmvssTestConfiguration = "正碰30mph，固定滑台"
        )

        /**
         * 创建FMVSS 10岁Hybrid III假人
         */
        fun create10yrHybridIII() = FmvssCrashTestDummy(
            dummyId = "FMVSS_HIII_10Y",
            dummyCode = "10y",
            dummyName = "10岁Hybrid III假人",
            minHeightIn = 60,
            maxHeightIn = 66,
            weightLb = 80.0,
            ageRange = "9-11岁",
            fmvssTestStandard = "FMVSS 213",
            installDirection = InstallDirection.FORWARD,
            description = "FMVSS测试用10岁Hybrid III假人",
            standardClause = "FMVSS 213 S6.3",
            chestDeflectionLimitIn = 2.0,
            fmvssImpactVelocityMph = 30.0,
            fmvssTestConfiguration = "正碰30mph，固定滑台"
        )
    }
}

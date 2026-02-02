package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import com.childproduct.designassistant.model.TestPulseType
import com.childproduct.designassistant.model.ImpactDirection
import com.childproduct.designassistant.model.EnhancedProductType.InstallDirection

/**
 * 测试配置实体
 * 基于ROADMATE 360格式和UN R129 Annex 7
 */
@Entity(
    tableName = "test_configuration",
    foreignKeys = [
        ForeignKey(
            entity = CrashTestDummy::class,
            parentColumns = ["dummyId"],
            childColumns = ["dummyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["dummyId", "pulseType", "impactDirection", "installDirection"], unique = true),
        Index(value = ["standardReference"])
    ]
)
data class TestConfiguration(
    @PrimaryKey val configId: String,         // CONFIG_R129_FRONTAL_Q0_REARWARD
    val standardReference: String,            // "UN R129 Rev.4"
    val pulseType: TestPulseType,             // FRONTAL, REAR, LATERAL
    val impactDirection: ImpactDirection,     // Q0, Q1, Q3...
    val dummyId: String,                      // DUMMY_Q0
    val installDirection: InstallDirection,   // REARWARD, FORWARD
    val installationType: String,             // "Isofix 3 pts", "Isofix 2 pts", "Vehicle Seat Belt"
    val productConfiguration: String,         // "Upright", "Reclined"
    val isofixAnchors: Boolean,               // 是否使用ISOFIX锚点
    val floorPosition: String,                // "Low", "High"
    val harness: Boolean,                     // 是否使用安全带
    val topTetherOrSupportLeg: String,        // "With", "Without", "no"
    val dashboardContact: Boolean,            // 是否允许与仪表板接触
    val comments: String?,                    // 特殊说明
    val buckleTest: Boolean,                  // 需要进行 buckle 测试
    val adjusterTest: Boolean,                // 需要进行 adjuster 测试
    val isofixTest: Boolean,                  // 需要进行 ISOFIX 测试
    val topTetherTest: Boolean,               // 需要进行 Top Tether 测试
    val quantity: String,                     // "n/a", "1", "2"
    val testNumber: String,                   // 测试编号
    val testSpeedKmh: Double,                 // 测试速度(km/h): 50.0 (正面), 30.0 (后向)
    val maxPulseG: Double,                    // 最大脉冲(g)
    val stoppingDistanceMm: Int,              // 制动距离(mm)
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        // 标准测试配置（基于ROADMATE 360格式和UN R129 Annex 7）
        fun getStandardConfigurations(): List<TestConfiguration> {
            val configs = mutableListOf<TestConfiguration>()

            // 后向安装测试配置（40-105cm）
            listOf("Q0", "Q0+", "Q1", "Q1.5", "Q3").forEach { dummyCode ->
                val dummyId = "DUMMY_${dummyCode.replace(".", "_").replace("+", "_PLUS").uppercase()}"
                val isQ3 = dummyCode == "Q3"

                // 正面碰撞测试
                configs.add(
                    TestConfiguration(
                        configId = "CONFIG_R129_FRONTAL_${dummyCode.replace(".", "_").replace("+", "_PLUS").uppercase()}_REARWARD_3PTS",
                        standardReference = "UN R129 Rev.4",
                        pulseType = TestPulseType.FRONTAL,
                        impactDirection = ImpactDirection.valueOf(dummyCode.replace(".", "_").replace("+", "_PLUS").uppercase()),
                        dummyId = dummyId,
                        installDirection = InstallDirection.REARWARD,
                        installationType = "Isofix 3 pts",
                        productConfiguration = "Upright",
                        isofixAnchors = true,
                        floorPosition = "Low",
                        harness = true,
                        topTetherOrSupportLeg = "With",
                        dashboardContact = true,
                        comments = "if contact repeat the test without dashboard",
                        buckleTest = false,
                        adjusterTest = false,
                        isofixTest = true,
                        topTetherTest = false,
                        quantity = "n/a",
                        testNumber = "-",
                        testSpeedKmh = 50.0,
                        maxPulseG = 20.0,
                        stoppingDistanceMm = 650
                    )
                )

                // Q3额外测试：2点ISOFIX安装
                if (isQ3) {
                    configs.add(
                        TestConfiguration(
                            configId = "CONFIG_R129_FRONTAL_Q3_REARWARD_2PTS",
                            standardReference = "UN R129 Rev.4",
                            pulseType = TestPulseType.FRONTAL,
                            impactDirection = ImpactDirection.Q3,
                            dummyId = dummyId,
                            installDirection = InstallDirection.REARWARD,
                            installationType = "Isofix 2 pts",
                            productConfiguration = "Upright",
                            isofixAnchors = true,
                            floorPosition = "Low",
                            harness = true,
                            topTetherOrSupportLeg = "no",
                            dashboardContact = false,
                            comments = null,
                            buckleTest = false,
                            adjusterTest = false,
                            isofixTest = true,
                            topTetherTest = false,
                            quantity = "n/a",
                            testNumber = "-",
                            testSpeedKmh = 50.0,
                            maxPulseG = 20.0,
                            stoppingDistanceMm = 650
                        )
                    )
                }

                // 后向碰撞测试
                configs.add(
                    TestConfiguration(
                        configId = "CONFIG_R129_REAR_${dummyCode.replace(".", "_").replace("+", "_PLUS").uppercase()}_REARWARD",
                        standardReference = "UN R129 Rev.4",
                        pulseType = TestPulseType.REAR,
                        impactDirection = ImpactDirection.valueOf(dummyCode.replace(".", "_").replace("+", "_PLUS").uppercase()),
                        dummyId = dummyId,
                        installDirection = InstallDirection.REARWARD,
                        installationType = "Isofix 3 pts",
                        productConfiguration = "Upright",
                        isofixAnchors = true,
                        floorPosition = "Low",
                        harness = true,
                        topTetherOrSupportLeg = "With",
                        dashboardContact = false,
                        comments = null,
                        buckleTest = false,
                        adjusterTest = false,
                        isofixTest = true,
                        topTetherTest = false,
                        quantity = "n/a",
                        testNumber = "-",
                        testSpeedKmh = 30.0,
                        maxPulseG = 21.0,
                        stoppingDistanceMm = 275
                    )
                )

                // 侧向碰撞测试
                configs.add(
                    TestConfiguration(
                        configId = "CONFIG_R129_LATERAL_${dummyCode.replace(".", "_").replace("+", "_PLUS").uppercase()}_REARWARD",
                        standardReference = "UN R129 Rev.4",
                        pulseType = TestPulseType.LATERAL,
                        impactDirection = ImpactDirection.valueOf(dummyCode.replace(".", "_").replace("+", "_PLUS").uppercase()),
                        dummyId = dummyId,
                        installDirection = InstallDirection.REARWARD,
                        installationType = "Isofix 3 pts",
                        productConfiguration = "Upright",
                        isofixAnchors = true,
                        floorPosition = "Low",
                        harness = true,
                        topTetherOrSupportLeg = "With",
                        dashboardContact = false,
                        comments = null,
                        buckleTest = false,
                        adjusterTest = false,
                        isofixTest = true,
                        topTetherTest = false,
                        quantity = "n/a",
                        testNumber = "-",
                        testSpeedKmh = 32.0,
                        maxPulseG = 25.0,
                        stoppingDistanceMm = 250
                    )
                )
            }

            // 前向安装测试配置（105-150cm）
            listOf("Q3s", "Q6", "Q10").forEach { dummyCode ->
                val dummyId = "DUMMY_${dummyCode.replace(".", "_").replace("s", "S").uppercase()}"
                val impactDir = dummyCode.replace("s", "")

                // 正面碰撞测试（必须使用Top-tether）
                configs.add(
                    TestConfiguration(
                        configId = "CONFIG_R129_FRONTAL_${dummyCode.replace(".", "_").replace("s", "S").uppercase()}_FORWARD",
                        standardReference = "UN R129 Rev.4",
                        pulseType = TestPulseType.FRONTAL,
                        impactDirection = ImpactDirection.valueOf(impactDir.replace(".", "_").uppercase()),
                        dummyId = dummyId,
                        installDirection = InstallDirection.FORWARD,
                        installationType = "Isofix 3 pts",
                        productConfiguration = "Upright",
                        isofixAnchors = true,
                        floorPosition = "Low",
                        harness = true,
                        topTetherOrSupportLeg = "With",
                        dashboardContact = false,
                        comments = null,
                        buckleTest = false,
                        adjusterTest = false,
                        isofixTest = true,
                        topTetherTest = true,
                        quantity = "n/a",
                        testNumber = "-",
                        testSpeedKmh = 50.0,
                        maxPulseG = 20.0,
                        stoppingDistanceMm = 650
                    )
                )

                // 侧向碰撞测试
                configs.add(
                    TestConfiguration(
                        configId = "CONFIG_R129_LATERAL_${dummyCode.replace(".", "_").replace("s", "S").uppercase()}_FORWARD",
                        standardReference = "UN R129 Rev.4",
                        pulseType = TestPulseType.LATERAL,
                        impactDirection = ImpactDirection.valueOf(impactDir.replace(".", "_").uppercase()),
                        dummyId = dummyId,
                        installDirection = InstallDirection.FORWARD,
                        installationType = "Isofix 3 pts",
                        productConfiguration = "Upright",
                        isofixAnchors = true,
                        floorPosition = "Low",
                        harness = true,
                        topTetherOrSupportLeg = "no",  // 侧向测试不需要Top-tether
                        dashboardContact = false,
                        comments = null,
                        buckleTest = false,
                        adjusterTest = false,
                        isofixTest = true,
                        topTetherTest = false,
                        quantity = "n/a",
                        testNumber = "-",
                        testSpeedKmh = 32.0,
                        maxPulseG = 25.0,
                        stoppingDistanceMm = 250
                    )
                )
            }

            return configs
        }
    }
}

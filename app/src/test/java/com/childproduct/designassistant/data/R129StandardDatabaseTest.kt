package com.childproduct.designassistant.data

import com.childproduct.designassistant.data.model.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * R129r4e标准数据库测试
 */
class R129StandardDatabaseTest {

    private lateinit var r129Database: R129r4eStandardDatabase
    private lateinit var detailService: R129StandardDetailsService

    @Before
    fun setup() {
        r129Database = R129r4eStandardDatabase()
        detailService = R129StandardDetailsService()
    }

    @Test
    fun `测试获取所有假人规格`() {
        val dummies = r129Database.DUMMY_SPECS
        assertEquals(6, dummies.size)
        assertEquals("Q0", dummies[0].dummyType)
        assertEquals("Q10", dummies[5].dummyType)
    }

    @Test
    fun `测试假人Q0规格`() {
        val q0 = r129Database.getDummySpec("Q0")
        assertNotNull(q0)
        assertEquals("≤60cm", q0?.statureRange)
        assertEquals(3.47, q0?.mass, 0.01)
        assertEquals(355.0, q0?.keyDimensions?.sittingHeight, 0.01)
    }

    @Test
    fun `测试假人伤害判据`() {
        val q0 = r129Database.getDummySpec("Q0")
        assertNotNull(q0?.injuryCriteria)
        assertEquals(75.0, q0?.injuryCriteria?.headAcceleration3ms?.lowThreshold, 0.01)
        assertEquals(600, q0?.injuryCriteria?.hpc?.lowThreshold)
        assertEquals(55.0, q0?.injuryCriteria?.chestAcceleration3ms, 0.01)
    }

    @Test
    fun `测试Q1.5腹部压力判据`() {
        val q1_5 = r129Database.getDummySpec("Q1.5")
        assertNotNull(q1_5?.injuryCriteria?.abdominalPressure)
        assertEquals(1.2, q1_5?.injuryCriteria?.abdominalPressure?.q1_5Threshold, 0.01)
        assertEquals(1.0, q1_5?.injuryCriteria?.abdominalPressure?.q3_q6Threshold, 0.01)
    }

    @Test
    fun `测试防旋转装置支撑腿规格`() {
        val supportLeg = r129Database.getAntiRotationDeviceSpec(AntiRotationDeviceType.SUPPORT_LEG)
        assertNotNull(supportLeg)
        assertNotNull(supportLeg?.supportLegSpec)
        assertEquals(2500.0, supportLeg?.supportLegSpec?.footRequirements?.minContactArea, 0.01)
        assertEquals(20.0, supportLeg?.supportLegSpec?.geometryRequirements?.adjustmentStep, 0.01)
    }

    @Test
    fun `测试防旋转装置上拉带规格`() {
        val topTether = r129Database.getAntiRotationDeviceSpec(AntiRotationDeviceType.TOP_TETHER)
        assertNotNull(topTether)
        assertNotNull(topTether?.topTetherSpec)
        assertEquals(2000.0, topTether?.topTetherSpec?.minLength, 0.01)
        assertEquals(50.0, topTether?.topTetherSpec?.tension, 0.01)
        assertTrue(topTether?.topTetherSpec?.hasNoSlackIndicator == true)
    }

    @Test
    fun `测试碰撞测试曲线`() {
        val frontalCurve = r129Database.getImpactTestCurve(ImpactTestType.FRONTAL_IMPACT)
        assertNotNull(frontalCurve)
        assertEquals(ImpactTestType.FRONTAL_IMPACT, frontalCurve?.testType)
        assertTrue(frontalCurve?.curvePoints?.size!! > 0)
        assertEquals("50±2km/h", frontalCurve?.velocityRange)
    }

    @Test
    fun `测试材料标准要求`() {
        val materials = r129Database.MATERIAL_STANDARDS
        assertTrue(materials.isNotEmpty())
        val toxicity = materials.find { it.materialType == MaterialType.TOXICITY }
        assertNotNull(toxicity)
        assertEquals("EN 71-3:2013+A1:2014(III类)", toxicity?.standard)
    }

    @Test
    fun `测试R129r4e关键阈值`() {
        val thresholds = r129Database.THRESHOLDS
        assertEquals(83.0, thresholds.maxRearwardHeight, 0.01)
        assertEquals(76.0, thresholds.minForwardHeight, 0.01)
        assertEquals(100.0, thresholds.minBoosterHeight, 0.01)
        assertEquals(15, thresholds.mandatoryRearwardMonths)
    }

    @Test
    fun `测试假人安装垫片高度`() {
        val q0Spacer = r129Database.getSpacerHeight("Q0")
        assertEquals(173, q0Spacer)
        val q3Spacer = r129Database.getSpacerHeight("Q3")
        assertEquals(250, q3Spacer)
    }

    @Test
    fun `测试外部尺寸ISO包络`() {
        val envelopes = r129Database.EXTERNAL_ENVELOPES
        assertEquals(3, envelopes.size)
        val forwardFacing = envelopes["iSizeForwardFacing"]
        assertEquals(600.0, forwardFacing?.width, 0.01)
        assertEquals(800.0, forwardFacing?.height, 0.01)
    }

    @Test
    fun `测试标准概述`() {
        val overview = r129Database.getStandardOverview()
        assertEquals("ECE R129 / i-Size", overview.standardName)
        assertEquals("Revision 4 + 02/03 Series Amendments", overview.revision)
        assertEquals("40-150cm", overview.heightRange)
        assertEquals("3.47-35.58kg", overview.weightRange)
    }

    @Test
    fun `测试根据身高范围获取适用假人`() {
        val dummies = detailService.getApplicableDummiesByHeight("40-60cm")
        assertTrue(dummies.isNotEmpty())
        assertTrue(dummies.any { it.dummyType == "Q0" })
    }

    @Test
    fun `测试朝向要求判断`() {
        val rearward = detailService.determineOrientationRequirement("40-83cm")
        assertTrue(rearward.contains("强制后向"))
        val forward = detailService.determineOrientationRequirement("76-105cm")
        assertTrue(forward.contains("前向"))
    }

    @Test
    fun `测试伤害判据合规性验证`() {
        val result = detailService.validateInjuryCriteria(
            dummyType = "Q0",
            headAcceleration3ms = 70.0,
            hpc = 500,
            chestAcceleration3ms = 50.0
        )
        assertTrue(result.isCompliant)
        assertTrue(result.details.contains("符合标准"))
    }

    @Test
    fun `测试伤害判据不合规情况`() {
        val result = detailService.validateInjuryCriteria(
            dummyType = "Q0",
            headAcceleration3ms = 80.0,
            hpc = 700,
            chestAcceleration3ms = 60.0
        )
        assertFalse(result.isCompliant)
        assertTrue(result.details.contains("超标"))
    }

    @Test
    fun `测试ECRS分类`() {
        val classifications = r129Database.ECRS_CLASSIFICATIONS
        assertTrue(classifications.isNotEmpty())
        val isizeUniversal = classifications.find { it.classificationType == ECRSType.INTEGRAL_ISOFIX_UNIVERSAL }
        assertNotNull(isizeUniversal)
        assertEquals("整体式通用ISOFIX型(i-Size)", isizeUniversal?.description)
    }

    @Test
    fun `测试生产一致性控制`() {
        val conformity = r129Database.PRODUCTION_CONFORMITY
        assertTrue(conformity.testItems.isNotEmpty())
        assertEquals(1, conformity.batchSampling.batchSizeSmall.sampleSize)
        assertEquals(2, conformity.batchSampling.batchSizeLarge.sampleSize)
    }

    @Test
    fun `测试标识要求`() {
        val markings = r129Database.MARKING_REQUIREMENTS
        assertEquals(4, markings.size)
        val warningLabel = markings.find { it.markType == "后向约束警告标签" }
        assertNotNull(warningLabel)
        assertEquals("最小60×120mm", warningLabel?.size)
    }

    @Test
    fun `测试用户说明书要求`() {
        val manual = r129Database.USER_MANUAL_REQUIREMENTS
        assertTrue(manual.mandatoryContent.size >= 9)
        assertTrue(manual.mandatoryContent.any { it.item.contains("安装步骤") })
    }
}

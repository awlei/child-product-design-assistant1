package com.childproduct.designassistant.data

/**
 * 高脚椅标准数据库对象
 * 
 * 包含内容：
 * - EN 14988-1:2006+A1:2012 欧洲高脚椅标准
 * - EN 16120:2012+A1:2015 欧洲家用高脚椅标准
 * - ASTM F404-21 美国高脚椅标准
 * - AS 4684:2009 澳大利亚高脚椅标准
 * - GB 22793.1-2008 中国儿童高脚椅标准
 * 
 * @see EN 14988
 * @see ASTM F404
 */

/**
 * 高脚椅标准类型
 */
enum class HighChairStandardType(val code: String, val displayName: String, val region: String) {
    EN_14988_1("EN 14988-1:2006+A1:2012", "欧洲高脚椅标准", "欧盟"),
    EN_16120("EN 16120:2012+A1:2015", "欧洲家用高脚椅标准", "欧盟"),
    ASTM_F404("ASTM F404-21", "美国高脚椅标准", "美国"),
    AS_4684("AS 4684:2009", "澳大利亚高脚椅标准", "澳大利亚"),
    GB_22793_1("GB 22793.1-2008", "中国儿童高脚椅标准", "中国")
}

/**
 * 高脚椅稳定性数据
 */
data class HighChairStabilityData(
    val stabilityId: String,
    val standard: HighChairStandardType,
    val testCondition: String,
    val trayAngle: Int,
    val seatAngle: Int,
    const val stabilityAngle: Int,
    const val stabilityAngleRange: String,
    val loadWeight: Double,
    const val loadWeightRange: String,
    val passCriteria: String
)

/**
 * 高脚椅约束系统数据
 */
data class HighChairRestraintData(
    val restraintId: String,
    val standard: HighChairStandardType,
    val restraintType: String,
    val minNumber: Int,
    val maxNumber: Int,
    val minBeltLength: Double,
    val maxBeltLength: Double,
    val buckleStrength: Double,
    const val buckleStrengthRange: String,
    val testForce: Double,
    val passCriteria: String
)

/**
 * 高脚椅结构强度数据
 */
data class HighChairStructuralStrengthData(
    val strengthId: String,
    val standard: HighChairStandardType,
    val testType: String,
    val loadWeight: Double,
    val loadWeightRange: String,
    val loadDuration: Int,
    val loadDurationRange: String,
    val deformationLimit: Double,
    val deformationLimitRange: String,
    val passCriteria: String
)

/**
 * 高脚椅标准数据库对象
 */
object HighChairStandardDatabase {
    
    // ========== 标准基本信息 ==========
    
    val STANDARDS = mapOf(
        "EN 14988-1" to StandardBasicInfo(
            standardId = "EN-14988-1",
            standardName = "Children's furniture - Child care articles - High chairs",
            standardType = StandardCategory.INTERNATIONAL,
            applicableRegion = "欧洲 (ECE)",
            applicableWeight = "通常≤15kg",
            applicableAge = "约6个月至3岁",
            coreScope = "规定高脚椅的安全性要求，包括稳定性、结构强度、约束系统、锁定机制、锐利边缘和窒息危险等。2024年版为最新版本，由意大利统一标准化组织发布",
            effectiveDate = "2024年（最新版）",
            standardStatus = "Current",
            dataSource = "European Committee for Standardization (CEN)"
        ),
        "ASTM F404" to StandardBasicInfo(
            standardId = "ASTM-F404",
            standardName = "Standard Consumer Safety Specification for High Chairs",
            standardType = StandardCategory.NATIONAL,
            applicableRegion = "美国 (USA)",
            applicableWeight = "通常≤15kg",
            applicableAge = "约6个月至3岁",
            coreScope = "规定高脚椅的安全性能要求，包括稳定性、强度、标签、警示标签、化学要求等。ASTM F404-21 为最新版本，对高脚椅不得使用含铅量超标的油漆喷涂，可触及部件总铅含量有严格限制",
            effectiveDate = "2021年（最新版）",
            standardStatus = "Current",
            dataSource = "ASTM International"
        ),
        "GB 22793.1" to StandardBasicInfo(
            standardId = "GB-22793.1",
            standardName = "儿童高椅 第1部分：安全要求",
            standardType = StandardCategory.NATIONAL,
            applicableRegion = "中国 (CN)",
            applicableWeight = "通常≤15kg",
            applicableAge = "约6个月至3岁",
            coreScope = "规定儿童高椅的安全要求，包括材料、结构、稳定性、强度、标志和使用说明等",
            effectiveDate = "2008年",
            standardStatus = "Current",
            dataSource = "国家市场监督管理总局"
        ),
        "EN 16120" to StandardBasicInfo(
            standardId = "EN-16120",
            standardName = "Child use and care articles - Home use high chairs",
            standardType = StandardCategory.INTERNATIONAL,
            applicableRegion = "欧洲 (ECE)",
            applicableWeight = "通常≤15kg",
            applicableAge = "约6个月至3岁",
            coreScope = "规定家用高脚椅的安全要求，包括可调节高度、可拆卸部件的锁定机制等",
            effectiveDate = "2015年",
            standardStatus = "Current",
            dataSource = "European Committee for Standardization (CEN)"
        ),
        "AS 4684" to StandardBasicInfo(
            standardId = "AS-4684",
            standardName = "Children's furniture - High chairs - Safety requirements",
            standardType = StandardCategory.NATIONAL,
            applicableRegion = "澳大利亚 (AU)",
            applicableWeight = "通常≤15kg",
            applicableAge = "约6个月至3岁",
            coreScope = "规定高脚椅的安全要求，包括稳定性、强度、约束系统、可折叠机构的锁定机制等",
            effectiveDate = "2009年",
            standardStatus = "Current",
            dataSource = "Standards Australia"
        )
    )
    
    // ========== 稳定性数据 ==========
    
    val STABILITY_DATA = mapOf(
        "EN 14988-1" to listOf(
            HighChairStabilityData(
                stabilityId = "EN14988-STAB-FRONT",
                standard = HighChairStandardType.EN_14988_1,
                testCondition = "前向稳定性测试（托盘展开）",
                trayAngle = 0,
                seatAngle = 10,
                stabilityAngle = 10,
                stabilityAngleRange = "≥10°",
                loadWeight = 9.0,
                loadWeightRange = "9kg±0.1kg",
                passCriteria = "高脚椅不应翻倒"
            ),
            HighChairStabilityData(
                stabilityId = "EN14988-STAB-REAR",
                standard = HighChairStandardType.EN_14988_1,
                testCondition = "后向稳定性测试（托盘折叠）",
                trayAngle = 15,
                seatAngle = 10,
                stabilityAngle = 12,
                stabilityAngleRange = "≥12°",
                loadWeight = 9.0,
                loadWeightRange = "9kg±0.1kg",
                passCriteria = "高脚椅不应翻倒"
            ),
            HighChairStabilityData(
                stabilityId = "EN14988-STAB-SIDE",
                standard = HighChairStandardType.EN_14988_1,
                testCondition = "侧向稳定性测试",
                trayAngle = 0,
                seatAngle = 10,
                stabilityAngle = 8,
                stabilityAngleRange = "≥8°",
                loadWeight = 9.0,
                loadWeightRange = "9kg±0.1kg",
                passCriteria = "高脚椅不应翻倒"
            )
        ),
        "ASTM F404" to listOf(
            HighChairStabilityData(
                stabilityId = "ASTM-F404-STAB-FRONT",
                standard = HighChairStandardType.ASTM_F404,
                testCondition = "前向稳定性测试",
                trayAngle = 0,
                seatAngle = 10,
                stabilityAngle = 10,
                stabilityAngleRange = "≥10°",
                loadWeight = 15.0,
                loadWeightRange = "15kg±0.1kg",
                passCriteria = "高脚椅不应翻倒"
            ),
            HighChairStabilityData(
                stabilityId = "ASTM-F404-STAB-REAR",
                standard = HighChairStandardType.ASTM_F404,
                testCondition = "后向稳定性测试",
                trayAngle = 15,
                seatAngle = 10,
                stabilityAngle = 12,
                stabilityAngleRange = "≥12°",
                loadWeight = 15.0,
                loadWeightRange = "15kg±0.1kg",
                passCriteria = "高脚椅不应翻倒"
            )
        ),
        "GB 22793.1" to listOf(
            HighChairStabilityData(
                stabilityId = "GB-STAB-FRONT",
                standard = HighChairStandardType.GB_22793_1,
                testCondition = "前向稳定性测试",
                trayAngle = 0,
                seatAngle = 10,
                stabilityAngle = 10,
                stabilityAngleRange = "≥10°",
                loadWeight = 9.0,
                loadWeightRange = "9kg±0.1kg",
                passCriteria = "高脚椅不应翻倒"
            ),
            HighChairStabilityData(
                stabilityId = "GB-STAB-REAR",
                standard = HighChairStandardType.GB_22793_1,
                testCondition = "后向稳定性测试",
                trayAngle = 15,
                seatAngle = 10,
                stabilityAngle = 12,
                stabilityAngleRange = "≥12°",
                loadWeight = 9.0,
                loadWeightRange = "9kg±0.1kg",
                passCriteria = "高脚椅不应翻倒"
            )
        ),
        "EN 16120" to listOf(
            HighChairStabilityData(
                stabilityId = "EN16120-STAB-FRONT",
                standard = HighChairStandardType.EN_16120,
                testCondition = "前向稳定性测试（可调节高度）",
                trayAngle = 0,
                seatAngle = 10,
                stabilityAngle = 10,
                stabilityAngleRange = "≥10°",
                loadWeight = 9.0,
                loadWeightRange = "9kg±0.1kg",
                passCriteria = "高脚椅不应翻倒"
            )
        ),
        "AS 4684" to listOf(
            HighChairStabilityData(
                stabilityId = "AS4684-STAB-FRONT",
                standard = HighChairStandardType.AS_4684,
                testCondition = "前向稳定性测试",
                trayAngle = 0,
                seatAngle = 10,
                stabilityAngle = 10,
                stabilityAngleRange = "≥10°",
                loadWeight = 15.0,
                loadWeightRange = "15kg±0.1kg",
                passCriteria = "高脚椅不应翻倒"
            ),
            HighChairStabilityData(
                stabilityId = "AS4684-STAB-REAR",
                standard = HighChairStandardType.AS_4684,
                testCondition = "后向稳定性测试",
                trayAngle = 15,
                seatAngle = 10,
                stabilityAngle = 12,
                stabilityAngleRange = "≥12°",
                loadWeight = 15.0,
                loadWeightRange = "15kg±0.1kg",
                passCriteria = "高脚椅不应翻倒"
            )
        )
    )
    
    // ========== 约束系统数据 ==========
    
    val RESTRAINT_DATA = mapOf(
        "EN 14988-1" to listOf(
            HighChairRestraintData(
                restraintId = "EN14988-RESTRAINT-3POINT",
                standard = HighChairStandardType.EN_14988_1,
                restraintType = "三点式约束带",
                minNumber = 3,
                maxNumber = 5,
                minBeltLength = 300.0,
                maxBeltLength = 800.0,
                buckleStrength = 250.0,
                buckleStrengthRange = "≥250N",
                testForce = 200.0,
                passCriteria = "带扣和连接点不应断裂，约束带不应滑脱"
            ),
            HighChairRestraintData(
                restraintId = "EN14988-RESTRAINT-5POINT",
                standard = HighChairStandardType.EN_14988_1,
                restraintType = "五点式约束带",
                minNumber = 5,
                maxNumber = 5,
                minBeltLength = 300.0,
                maxBeltLength = 900.0,
                buckleStrength = 250.0,
                buckleStrengthRange = "≥250N",
                testForce = 200.0,
                passCriteria = "带扣和连接点不应断裂，约束带不应滑脱"
            )
        ),
        "ASTM F404" to listOf(
            HighChairRestraintData(
                restraintId = "ASTM-F404-RESTRAINT",
                standard = HighChairStandardType.ASTM_F404,
                restraintType = "三点式约束带（最低）",
                minNumber = 3,
                maxNumber = 5,
                minBeltLength = 300.0,
                maxBeltLength = 800.0,
                buckleStrength = 250.0,
                buckleStrengthRange = "≥250N",
                testForce = 200.0,
                passCriteria = "带扣和连接点不应断裂，约束带不应滑脱"
            )
        ),
        "GB 22793.1" to listOf(
            HighChairRestraintData(
                restraintId = "GB-RESTRAINT-3POINT",
                standard = HighChairStandardType.GB_22793_1,
                restraintType = "三点式约束带",
                minNumber = 3,
                maxNumber = 5,
                minBeltLength = 300.0,
                maxBeltLength = 800.0,
                buckleStrength = 250.0,
                buckleStrengthRange = "≥250N",
                testForce = 200.0,
                passCriteria = "带扣和连接点不应断裂，约束带不应滑脱"
            )
        ),
        "EN 16120" to listOf(
            HighChairRestraintData(
                restraintId = "EN16120-RESTRAINT",
                standard = HighChairStandardType.EN_16120,
                restraintType = "三点式约束带",
                minNumber = 3,
                maxNumber = 5,
                minBeltLength = 300.0,
                maxBeltLength = 800.0,
                buckleStrength = 250.0,
                buckleStrengthRange = "≥250N",
                testForce = 200.0,
                passCriteria = "带扣和连接点不应断裂，约束带不应滑脱"
            )
        ),
        "AS 4684" to listOf(
            HighChairRestraintData(
                restraintId = "AS4684-RESTRAINT",
                standard = HighChairStandardType.AS_4684,
                restraintType = "三点式约束带",
                minNumber = 3,
                maxNumber = 5,
                minBeltLength = 300.0,
                maxBeltLength = 800.0,
                buckleStrength = 250.0,
                buckleStrengthRange = "≥250N",
                testForce = 200.0,
                passCriteria = "带扣和连接点不应断裂，约束带不应滑脱"
            )
        )
    )
    
    // ========== 结构强度数据 ==========
    
    val STRUCTURAL_STRENGTH_DATA = mapOf(
        "EN 14988-1" to listOf(
            HighChairStructuralStrengthData(
                strengthId = "EN14988-SEAT-LOAD",
                standard = HighChairStandardType.EN_14988_1,
                testType = "座面静态载荷",
                loadWeight = 120.0,
                loadWeightRange = "120kg±5kg",
                loadDuration = 5,
                loadDurationRange = "5min",
                deformationLimit = 5.0,
                deformationLimitRange = "≤5mm",
                passCriteria = "座面永久变形≤5mm，无破裂或断裂"
            ),
            HighChairStructuralStrengthData(
                strengthId = "EN14988-TRAY-LOAD",
                standard = HighChairStandardType.EN_14988_1,
                testType = "托盘静态载荷",
                loadWeight = 50.0,
                loadWeightRange = "50kg±2kg",
                loadDuration = 5,
                loadDurationRange = "5min",
                deformationLimit = 5.0,
                deformationLimitRange = "≤5mm",
                passCriteria = "托盘永久变形≤5mm，无破裂或断裂"
            ),
            HighChairStructuralStrengthData(
                strengthId = "EN14988-IMPACT",
                standard = HighChairStandardType.EN_14988_1,
                testType = "冲击测试",
                loadWeight = 15.0,
                loadWeightRange = "15kg±0.1kg",
                loadDuration = 0,
                loadDurationRange = "瞬时",
                deformationLimit = 10.0,
                deformationLimitRange = "≤10mm",
                passCriteria = "高脚椅不应倒塌，部件不应断裂"
            )
        ),
        "ASTM F404" to listOf(
            HighChairStructuralStrengthData(
                strengthId = "ASTM-F404-SEAT-LOAD",
                standard = HighChairStandardType.ASTM_F404,
                testType = "座面静态载荷",
                loadWeight = 120.0,
                loadWeightRange = "120kg±5kg",
                loadDuration = 5,
                loadDurationRange = "5min",
                deformationLimit = 5.0,
                deformationLimitRange = "≤5mm",
                passCriteria = "座面永久变形≤5mm，无破裂或断裂"
            )
        ),
        "GB 22793.1" to listOf(
            HighChairStructuralStrengthData(
                strengthId = "GB-SEAT-LOAD",
                standard = HighChairStandardType.GB_22793_1,
                testType = "座面静态载荷",
                loadWeight = 100.0,
                loadWeightRange = "100kg±5kg",
                loadDuration = 5,
                loadDurationRange = "5min",
                deformationLimit = 5.0,
                deformationLimitRange = "≤5mm",
                passCriteria = "座面永久变形≤5mm，无破裂或断裂"
            )
        ),
        "EN 16120" to listOf(
            HighChairStructuralStrengthData(
                strengthId = "EN16120-SEAT-LOAD",
                standard = HighChairStandardType.EN_16120,
                testType = "座面静态载荷",
                loadWeight = 120.0,
                loadWeightRange = "120kg±5kg",
                loadDuration = 5,
                loadDurationRange = "5min",
                deformationLimit = 5.0,
                deformationLimitRange = "≤5mm",
                passCriteria = "座面永久变形≤5mm，无破裂或断裂"
            )
        ),
        "AS 4684" to listOf(
            HighChairStructuralStrengthData(
                strengthId = "AS4684-SEAT-LOAD",
                standard = HighChairStandardType.AS_4684,
                testType = "座面静态载荷",
                loadWeight = 120.0,
                loadWeightRange = "120kg±5kg",
                loadDuration = 5,
                loadDurationRange = "5min",
                deformationLimit = 5.0,
                deformationLimitRange = "≤5mm",
                passCriteria = "座面永久变形≤5mm，无破裂或断裂"
            )
        )
    )
    
    // ========== 查询方法 ==========
    
    /**
     * 获取所有标准信息
     */
    fun getAllStandards(): List<StandardBasicInfo> {
        return STANDARDS.values.toList()
    }
    
    /**
     * 根据标准ID获取标准信息
     */
    fun getStandardById(standardId: String): StandardBasicInfo? {
        return STANDARDS.values.find { it.standardId == standardId }
    }
    
    /**
     * 根据区域获取标准
     */
    fun getStandardsByRegion(region: String): List<StandardBasicInfo> {
        return STANDARDS.values.filter { it.applicableRegion.contains(region) }
    }
    
    /**
     * 获取标准的稳定性数据
     */
    fun getStabilityDataByStandard(standardCode: String): List<HighChairStabilityData>? {
        return STABILITY_DATA[standardCode]
    }
    
    /**
     * 获取标准的约束系统数据
     */
    fun getRestraintDataByStandard(standardCode: String): List<HighChairRestraintData>? {
        return RESTRAINT_DATA[standardCode]
    }
    
    /**
     * 获取标准的结构强度数据
     */
    fun getStructuralStrengthDataByStandard(standardCode: String): List<HighChairStructuralStrengthData>? {
        return STRUCTURAL_STRENGTH_DATA[standardCode]
    }
}

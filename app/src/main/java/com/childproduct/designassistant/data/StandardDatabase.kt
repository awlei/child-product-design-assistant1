package com.childproduct.designassistant.data

import com.childproduct.designassistant.model.*
import com.childproduct.designassistant.model.StandardCategory

/**
 * 本地标准数据库
 * 包含常见的国际儿童产品安全标准
 */
object StandardDatabase {

    /**
     * ECE R129 标准（欧洲 i-Size 标准）
     */
    val eceR129 = TechnicalStandard(
        id = "STD-001",
        code = "ECE R129",
        name = "Enhanced Child Restraint Systems",
        region = StandardRegion.EUROPE,
        category = StandardCategory.SAFETY_SEAT,
        groups = listOf(
            StandardGroup(
                code = "Group 0+",
                weightRange = "0-13 kg",
                heightRange = "40-85 cm",
                ageRange = "0-15 months",
                envelopeClass = "Envelope A"
            ),
            StandardGroup(
                code = "Group 1",
                weightRange = "9-18 kg",
                heightRange = "75-105 cm",
                ageRange = "9 months - 4 years",
                envelopeClass = "Envelope B"
            ),
            StandardGroup(
                code = "Group 1/2",
                weightRange = "9-25 kg",
                heightRange = "75-125 cm",
                ageRange = "9 months - 6 years",
                envelopeClass = "Envelope B/C"
            ),
            StandardGroup(
                code = "Group 2/3",
                weightRange = "15-36 kg",
                heightRange = "100-150 cm",
                ageRange = "4-12 years",
                envelopeClass = "Envelope C"
            )
        ),
        requirements = listOf(
            Requirement(
                id = "R129-001",
                category = RequirementCategory.IMPACT_TEST,
                description = "Frontal impact test at 50 km/h",
                testMethod = "ECE R129 Annex 7",
                passCriteria = "Head excursion < 550mm, Chest acceleration < 55g"
            ),
            Requirement(
                id = "R129-002",
                category = RequirementCategory.IMPACT_TEST,
                description = "Side impact test",
                testMethod = "ECE R129 Annex 8",
                passCriteria = "Head injury criteria < 778, Neck forces within limits"
            ),
            Requirement(
                id = "R129-003",
                category = RequirementCategory.INSTALLATION,
                description = "ISOFIX + Top Tether or Support Leg",
                testMethod = "Visual inspection + force test",
                passCriteria = "Proper anchorage, no excessive movement"
            ),
            Requirement(
                id = "R129-004",
                category = RequirementCategory.MATERIAL_SAFETY,
                description = "Flame retardant materials",
                testMethod = "FMVSS 302",
                passCriteria = "Burning rate ≤ 102 mm/min"
            ),
            Requirement(
                id = "R129-005",
                category = RequirementCategory.STRUCTURAL_INTEGRITY,
                description = "Structural integrity test",
                testMethod = "Static load test + dynamic test",
                passCriteria = "No permanent deformation, all components functional"
            )
        ),
        lastUpdated = "2023-06-01",
        sourceUrl = "https://unece.org/fileadmin/DAM/trans/main/wp29/wp29ws-2013-170.pdf"
    )

    /**
     * FMVSS 213 标准（美国联邦机动车安全标准）
     */
    val fmvss213 = TechnicalStandard(
        id = "STD-002",
        code = "FMVSS 213",
        name = "Child Restraint Systems",
        region = StandardRegion.USA,
        category = StandardCategory.SAFETY_SEAT,
        groups = listOf(
            StandardGroup(
                code = "Group 0",
                weightRange = "0-10 kg",
                heightRange = "< 66 cm",
                ageRange = "0-12 months",
                envelopeClass = null
            ),
            StandardGroup(
                code = "Group 1",
                weightRange = "9-18 kg",
                heightRange = "66-101 cm",
                ageRange = "1-4 years",
                envelopeClass = null
            ),
            StandardGroup(
                code = "Group 2",
                weightRange = "13-23 kg",
                heightRange = "97-132 cm",
                ageRange = "3-7 years",
                envelopeClass = null
            ),
            StandardGroup(
                code = "Group 3",
                weightRange = "15-36 kg",
                heightRange = "122-150 cm",
                ageRange = "5-12 years",
                envelopeClass = null
            )
        ),
        requirements = listOf(
            Requirement(
                id = "FMVSS-001",
                category = RequirementCategory.IMPACT_TEST,
                description = "Frontal impact test at 48 km/h",
                testMethod = "FMVSS 213 S5.1",
                passCriteria = "Head excursion < 813mm, Chest acceleration < 60g"
            ),
            Requirement(
                id = "FMVSS-002",
                category = RequirementCategory.FLAMMABILITY,
                description = "Flame resistance",
                testMethod = "FMVSS 302",
                passCriteria = "Burning rate ≤ 102 mm/min"
            ),
            Requirement(
                id = "FMVSS-003",
                category = RequirementCategory.LABELING,
                description = "Permanent labeling requirements",
                testMethod = "Visual inspection + durability test",
                passCriteria = "All required labels present and readable after testing"
            ),
            Requirement(
                id = "FMVSS-004",
                category = RequirementCategory.STRUCTURAL_INTEGRITY,
                description = "Belt path integrity",
                testMethod = "Load test",
                passCriteria = "No deformation or failure under 1000 N load"
            )
        ),
        lastUpdated = "2022-09-01",
        sourceUrl = "https://www.nhtsa.gov/equipment/child-restraints"
    )

    /**
     * GB 27887 标准（中国儿童安全座椅标准）
     */
    val gb27887 = TechnicalStandard(
        id = "STD-003",
        code = "GB 27887-2011",
        name = "机动车儿童乘员用约束系统",
        region = StandardRegion.CHINA,
        category = StandardCategory.SAFETY_SEAT,
        groups = listOf(
            StandardGroup(
                code = "Group 0+",
                weightRange = "0-13 kg",
                heightRange = "40-85 cm",
                ageRange = "0-15 months",
                envelopeClass = null
            ),
            StandardGroup(
                code = "Group I",
                weightRange = "9-18 kg",
                heightRange = "75-105 cm",
                ageRange = "9 months - 4 years",
                envelopeClass = null
            ),
            StandardGroup(
                code = "Group II",
                weightRange = "15-25 kg",
                heightRange = "100-125 cm",
                ageRange = "3-6 years",
                envelopeClass = null
            ),
            StandardGroup(
                code = "Group III",
                weightRange = "22-36 kg",
                heightRange = "125-150 cm",
                ageRange = "6-12 years",
                envelopeClass = null
            )
        ),
        requirements = listOf(
            Requirement(
                id = "GB-001",
                category = RequirementCategory.IMPACT_TEST,
                description = "Frontal impact test at 50 km/h",
                testMethod = "GB 27887 5.4",
                passCriteria = "Head excursion < 550mm, Chest acceleration < 55g"
            ),
            Requirement(
                id = "GB-002",
                category = RequirementCategory.FLAMMABILITY,
                description = "Flame retardant test",
                testMethod = "GB 8410",
                passCriteria = "Burning rate ≤ 100 mm/min"
            ),
            Requirement(
                id = "GB-003",
                category = RequirementCategory.CHEMICAL_SAFETY,
                description = "Heavy metals and phthalates limits",
                testMethod = "GB 6675",
                passCriteria = "Lead < 90 mg/kg, Phthalates < 0.1%"
            ),
            Requirement(
                id = "GB-004",
                category = RequirementCategory.STRUCTURAL_INTEGRITY,
                description = "Belt path and structural test",
                testMethod = "Static and dynamic load test",
                passCriteria = "No permanent deformation, all components functional"
            )
        ),
        lastUpdated = "2011-05-01",
        sourceUrl = null
    )

    /**
     * EN 1888 标准（欧洲婴儿推车标准）
     */
    val en1888 = TechnicalStandard(
        id = "STD-004",
        code = "EN 1888",
        name = "Child care articles - Wheeled child conveyances",
        region = StandardRegion.EUROPE,
        category = StandardCategory.STROLLER,
        groups = listOf(
            StandardGroup(
                code = "Single Stroller",
                weightRange = "0-15 kg",
                heightRange = "N/A",
                ageRange = "0-36 months",
                envelopeClass = null
            ),
            StandardGroup(
                code = "Double Stroller",
                weightRange = "0-15 kg x 2",
                heightRange = "N/A",
                ageRange = "0-36 months",
                envelopeClass = null
            )
        ),
        requirements = listOf(
            Requirement(
                id = "EN1888-001",
                category = RequirementCategory.STRUCTURAL_INTEGRITY,
                description = "Stability test",
                testMethod = "EN 1888 8.5",
                passCriteria = "No tip-over on 10° incline with 15 kg load"
            ),
            Requirement(
                id = "EN1888-002",
                category = RequirementCategory.STRUCTURAL_INTEGRITY,
                description = "Brake effectiveness",
                testMethod = "EN 1888 8.6",
                passCriteria = "No movement on 10° incline with 15 kg load"
            ),
            Requirement(
                id = "EN1888-003",
                category = RequirementCategory.FLAMMABILITY,
                description = "Textile flammability",
                testMethod = "EN 71-2",
                passCriteria = "Flame spread < 50 mm/s"
            ),
            Requirement(
                id = "EN1888-004",
                category = RequirementCategory.MATERIAL_SAFETY,
                description = "Finger entrapment test",
                testMethod = "EN 1888 8.7",
                passCriteria = "No finger entrapment in moving parts"
            )
        ),
        lastUpdated = "2012-07-01",
        sourceUrl = null
    )

    /**
     * ASTM F2050 标准（美国婴儿推车标准）
     */
    val astmF2050 = TechnicalStandard(
        id = "STD-005",
        code = "ASTM F2050",
        name = "Standard Consumer Safety Performance Specification for Carriages and Strollers",
        region = StandardRegion.USA,
        category = StandardCategory.STROLLER,
        groups = listOf(
            StandardGroup(
                code = "Stroller",
                weightRange = "0-20 kg",
                heightRange = "N/A",
                ageRange = "0-48 months",
                envelopeClass = null
            )
        ),
        requirements = listOf(
            Requirement(
                id = "ASTM-001",
                category = RequirementCategory.STRUCTURAL_INTEGRITY,
                description = "Stability and parking brake test",
                testMethod = "ASTM F2050 Section 8",
                passCriteria = "No tip-over, brake holds on 12° incline"
            ),
            Requirement(
                id = "ASTM-002",
                category = RequirementCategory.STRUCTURAL_INTEGRITY,
                description = "Harness restraint test",
                testMethod = "ASTM F2050 Section 9",
                passCriteria = "Child dummy remains seated under impact"
            ),
            Requirement(
                id = "ASTM-003",
                category = RequirementCategory.FLAMMABILITY,
                description = "Flame resistance",
                testMethod = "16 CFR 1632",
                passCriteria = "Burning rate ≤ 102 mm/min"
            )
        ),
        lastUpdated = "2019-06-01",
        sourceUrl = null
    )

    /**
     * 获取所有标准
     */
    fun getAllStandards(): List<TechnicalStandard> {
        return listOf(eceR129, fmvss213, gb27887, en1888, astmF2050)
    }

    /**
     * 根据产品类型获取标准
     */
    fun getStandardsByCategory(category: StandardCategory): List<TechnicalStandard> {
        return getAllStandards().filter { it.category == category }
    }

    /**
     * 根据地区获取标准
     */
    fun getStandardsByRegion(region: StandardRegion): List<TechnicalStandard> {
        return getAllStandards().filter { it.region == region }
    }

    /**
     * 根据标准代码查找
     */
    fun getStandardByCode(code: String): TechnicalStandard? {
        return getAllStandards().find { it.code == code }
    }

    /**
     * 根据身高和重量范围查找匹配的分组
     */
    fun findMatchingGroups(
        heightRange: String,  // 如 "60-120cm"
        weightRange: String,  // 如 "9-36kg"
        category: com.childproduct.designassistant.model.StandardCategory
    ): List<Pair<TechnicalStandard, StandardGroup>> {
        val results = mutableListOf<Pair<TechnicalStandard, StandardGroup>>()

        // 解析输入范围
        val (minH, maxH) = parseRange(heightRange)
        val (minW, maxW) = parseRange(weightRange)

        val standards = getStandardsByCategory(category)

        standards.forEach { standard ->
            standard.groups.forEach { group ->
                val (gMinH, gMaxH) = parseRange(group.heightRange)
                val (gMinW, gMaxW) = parseRange(group.weightRange)

                // 检查是否有重叠
                if (rangesOverlap(minH, maxH, gMinH, gMaxH) &&
                    rangesOverlap(minW, maxW, gMinW, gMaxW)) {
                    results.add(Pair(standard, group))
                }
            }
        }

        return results
    }

    /**
     * 解析范围字符串
     */
    private fun parseRange(rangeStr: String): Pair<Double, Double> {
        // 移除单位并解析
        val cleaned = rangeStr.replace("[^0-9-]".toRegex(), "")
        val parts = cleaned.split("-")
        return if (parts.size == 2) {
            Pair(parts[0].toDouble(), parts[1].toDouble())
        } else {
            Pair(parts[0].toDouble(), parts[0].toDouble())
        }
    }

    /**
     * 检查范围是否重叠
     */
    private fun rangesOverlap(min1: Double, max1: Double, min2: Double, max2: Double): Boolean {
        return !(max1 < min2 || max2 < min1)
    }
}

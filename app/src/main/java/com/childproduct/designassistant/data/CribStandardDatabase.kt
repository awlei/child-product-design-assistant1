package com.childproduct.designassistant.data

/**
 * 儿童床标准数据库对象
 * 
 * 包含内容：
 * - EN 1130:2019 欧洲儿童床和摇篮标准
 * - ASTM F1169-19 美国全尺寸婴儿床标准
 * - ASTM F1821-21 美国非全尺寸婴儿床标准
 * - AS/NZS 2172:2013 澳大利亚/新西兰婴儿床标准
 * - GB/T 33266-2016 中国婴儿床标准
 * 
 * @see EN 1130
 * @see ASTM F1169
 */

/**
 * 儿童床标准类型
 */
enum class CribStandardType(val code: String, val displayName: String, val region: String) {
    EN_1130("EN 1130:2019", "欧洲儿童床标准", "欧盟"),
    ASTM_F1169("ASTM F1169-19", "美国全尺寸婴儿床标准", "美国"),
    ASTM_F1821("ASTM F1821-21", "美国非全尺寸婴儿床标准", "美国"),
    AS_NZS_2172("AS/NZS 2172:2013", "澳大利亚婴儿床标准", "澳大利亚/新西兰"),
    GB_T_33266("GB/T 33266-2016", "中国婴儿床标准", "中国")
}

/**
 * 儿童床尺寸数据
 */
data class CribDimensionData(
    val dimensionId: String,
    val standard: CribStandardType,
    val dimensionName: String,
    val minValue: Double,
    val maxValue: Double,
    const val nominalValue: Double,
    val unit: String,
    val tolerance: String,
    val passCriteria: String
)

/**
 * 儿童床栏杆数据
 */
data class CribSlatData(
    val slatId: String,
    val standard: CribStandardType,
    val slatType: String, // 竖杆或横杆
    const val minSpacing: Double,
    const val maxSpacing: Double,
    const val nominalSpacing: Double,
    val unit: String,
    val tolerance: String,
    val strength: Double,
    const val strengthRange: String,
    val passCriteria: String
)

/**
 * 儿童床凸起数据
 */
data class CribProtrusionData(
    val protrusionId: String,
    val standard: CribStandardType,
    val location: String,
    const val maxHeight: Double,
    const val heightRange: String,
    const val maxDiameter: Double,
    const val diameterRange: String,
    val unit: String,
    val passCriteria: String
)

/**
 * 儿童床床垫数据
 */
data class CribMattressData(
    val mattressId: String,
    val standard: CribStandardType,
    val mattressType: String,
    const val minThickness: Double,
    const val maxThickness: Double,
    const val nominalThickness: Double,
    val unit: String,
    const val gapLimit: Double,
    const val gapLimitRange: String,
    val passCriteria: String
)

/**
 * 儿童床标准数据库对象
 */
object CribStandardDatabase {
    
    // ========== 标准基本信息 ==========
    
    val STANDARDS = mapOf(
        "EN 1130" to StandardBasicInfo(
            standardId = "EN-1130",
            standardName = "Children's furniture - Cots",
            standardType = StandardCategory.INTERNATIONAL,
            applicableRegion = "欧洲 (ECE)",
            applicableWeight = "通常≤15kg",
            applicableAge = "新生儿至约18个月",
            coreScope = "规定儿童床的安全要求，包括尺寸、栏杆间距、凸起物、床垫、可折叠机构的锁定机制等",
            effectiveDate = "2019年",
            standardStatus = "Current",
            dataSource = "European Committee for Standardization (CEN)"
        ),
        "ASTM F1169" to StandardBasicInfo(
            standardId = "ASTM-F1169",
            standardName = "Standard Consumer Safety Specification for Full-Size Baby Cribs",
            standardType = StandardCategory.NATIONAL,
            applicableRegion = "美国 (USA)",
            applicableWeight = "通常≤23kg",
            applicableAge = "新生儿至约3岁",
            coreScope = "规定全尺寸婴儿床的安全性能要求，包括可调节侧栏的禁止、护栏高度、栏杆间距等",
            effectiveDate = "2019年",
            standardStatus = "Current",
            dataSource = "ASTM International"
        ),
        "ASTM F1821" to StandardBasicInfo(
            standardId = "ASTM-F1821",
            standardName = "Standard Consumer Safety Specification for Non-Full-Size Baby Cribs",
            standardType = StandardCategory.NATIONAL,
            applicableRegion = "美国 (USA)",
            applicableWeight = "通常≤15kg",
            applicableAge = "新生儿至约18个月",
            coreScope = "规定非全尺寸婴儿床的安全性能要求",
            effectiveDate = "2021年",
            standardStatus = "Current",
            dataSource = "ASTM International"
        ),
        "GB/T 33266" to StandardBasicInfo(
            standardId = "GB/T-33266",
            standardName = "婴儿床安全要求",
            standardType = StandardCategory.NATIONAL,
            applicableRegion = "中国 (CN)",
            applicableWeight = "通常≤15kg",
            applicableAge = "新生儿至约18个月",
            coreScope = "规定婴儿床的安全要求，包括材料、结构、尺寸、标志和使用说明等",
            effectiveDate = "2016年",
            standardStatus = "Current",
            dataSource = "国家市场监督管理总局"
        ),
        "AS/NZS 2172" to StandardBasicInfo(
            standardId = "AS-NZS-2172",
            standardName = "Cots for household use - Safety requirements",
            standardType = StandardCategory.INTERNATIONAL,
            applicableRegion = "澳大利亚/新西兰 (AU/NZ)",
            applicableWeight = "通常≤23kg",
            applicableAge = "新生儿至约3岁",
            coreScope = "规定家用婴儿床的安全要求",
            effectiveDate = "2013年",
            standardStatus = "Current",
            dataSource = "Standards Australia"
        )
    )
    
    // ========== 尺寸数据 ==========
    
    val DIMENSION_DATA = mapOf(
        "EN 1130" to listOf(
            CribDimensionData(
                dimensionId = "EN1130-LENGTH",
                standard = CribStandardType.EN_1130,
                dimensionName = "长度",
                minValue = 1200.0,
                maxValue = 1400.0,
                nominalValue = 1300.0,
                unit = "mm",
                tolerance = "±50mm",
                passCriteria = "长度应在1200-1400mm范围内"
            ),
            CribDimensionData(
                dimensionId = "EN1130-WIDTH",
                standard = CribStandardType.EN_1130,
                dimensionName = "宽度",
                minValue = 600.0,
                maxValue = 700.0,
                nominalValue = 650.0,
                unit = "mm",
                tolerance = "±50mm",
                passCriteria = "宽度应在600-700mm范围内"
            ),
            CribDimensionData(
                dimensionId = "EN1130-HEIGHT",
                standard = CribStandardType.EN_1130,
                dimensionName = "护栏高度",
                minValue = 600.0,
                maxValue = 700.0,
                nominalValue = 650.0,
                unit = "mm",
                tolerance = "±50mm",
                passCriteria = "护栏高度≥600mm"
            )
        ),
        "ASTM F1169" to listOf(
            CribDimensionData(
                dimensionId = "ASTM-F1169-LENGTH",
                standard = CribStandardType.ASTM_F1169,
                dimensionName = "长度",
                minValue = 1317.0,
                maxValue = 1320.0,
                nominalValue = 1320.0,
                unit = "mm",
                tolerance = "±3mm",
                passCriteria = "长度应在1317-1320mm范围内"
            ),
            CribDimensionData(
                dimensionId = "ASTM-F1169-WIDTH",
                standard = CribStandardType.ASTM_F1169,
                dimensionName = "宽度",
                minValue = 787.0,
                maxValue = 787.0,
                nominalValue = 787.0,
                unit = "mm",
                tolerance = "±3mm",
                passCriteria = "宽度应为787±3mm"
            ),
            CribDimensionData(
                dimensionId = "ASTM-F1169-HEIGHT",
                standard = CribStandardType.ASTM_F1169,
                dimensionName = "护栏高度",
                minValue = 660.0,
                maxValue = 660.0,
                nominalValue = 660.0,
                unit = "mm",
                tolerance = "±3mm",
                passCriteria = "护栏高度≥660mm"
            )
        ),
        "ASTM F1821" to listOf(
            CribDimensionData(
                dimensionId = "ASTM-F1821-LENGTH",
                standard = CribStandardType.ASTM_F1821,
                dimensionName = "长度",
                minValue = 1000.0,
                maxValue = 1200.0,
                nominalValue = 1100.0,
                unit = "mm",
                tolerance = "±50mm",
                passCriteria = "长度应在1000-1200mm范围内"
            ),
            CribDimensionData(
                dimensionId = "ASTM-F1821-WIDTH",
                standard = CribStandardType.ASTM_F1821,
                dimensionName = "宽度",
                minValue = 550.0,
                maxValue = 700.0,
                nominalValue = 625.0,
                unit = "mm",
                tolerance = "±50mm",
                passCriteria = "宽度应在550-700mm范围内"
            )
        ),
        "GB/T 33266" to listOf(
            CribDimensionData(
                dimensionId = "GB-LENGTH",
                standard = CribStandardType.GB_T_33266,
                dimensionName = "长度",
                minValue = 1200.0,
                maxValue = 1400.0,
                nominalValue = 1300.0,
                unit = "mm",
                tolerance = "±50mm",
                passCriteria = "长度应在1200-1400mm范围内"
            ),
            CribDimensionData(
                dimensionId = "GB-WIDTH",
                standard = CribStandardType.GB_T_33266,
                dimensionName = "宽度",
                minValue = 600.0,
                maxValue = 700.0,
                nominalValue = 650.0,
                unit = "mm",
                tolerance = "±50mm",
                passCriteria = "宽度应在600-700mm范围内"
            ),
            CribDimensionData(
                dimensionId = "GB-HEIGHT",
                standard = CribStandardType.GB_T_33266,
                dimensionName = "护栏高度",
                minValue = 600.0,
                maxValue = 700.0,
                nominalValue = 650.0,
                unit = "mm",
                tolerance = "±50mm",
                passCriteria = "护栏高度≥600mm"
            )
        ),
        "AS/NZS 2172" to listOf(
            CribDimensionData(
                dimensionId = "ASNZS-LENGTH",
                standard = CribStandardType.AS_NZS_2172,
                dimensionName = "长度",
                minValue = 1300.0,
                maxValue = 1400.0,
                nominalValue = 1350.0,
                unit = "mm",
                tolerance = "±50mm",
                passCriteria = "长度应在1300-1400mm范围内"
            ),
            CribDimensionData(
                dimensionId = "ASNZS-WIDTH",
                standard = CribStandardType.AS_NZS_2172,
                dimensionName = "宽度",
                minValue = 700.0,
                maxValue = 800.0,
                nominalValue = 750.0,
                unit = "mm",
                tolerance = "±50mm",
                passCriteria = "宽度应在700-800mm范围内"
            ),
            CribDimensionData(
                dimensionId = "ASNZS-HEIGHT",
                standard = CribStandardType.AS_NZS_2172,
                dimensionName = "护栏高度",
                minValue = 600.0,
                maxValue = 700.0,
                nominalValue = 650.0,
                unit = "mm",
                tolerance = "±50mm",
                passCriteria = "护栏高度≥600mm"
            )
        )
    )
    
    // ========== 栏杆数据 ==========
    
    val SLAT_DATA = mapOf(
        "EN 1130" to listOf(
            CribSlatData(
                slatId = "EN1130-SLAT",
                standard = CribStandardType.EN_1130,
                slatType = "竖杆",
                minSpacing = 45.0,
                maxSpacing = 65.0,
                nominalSpacing = 55.0,
                unit = "mm",
                tolerance = "±10mm",
                strength = 250.0,
                strengthRange = "≥250N",
                passCriteria = "栏杆间距应在45-65mm之间，强度≥250N"
            )
        ),
        "ASTM F1169" to listOf(
            CribSlatData(
                slatId = "ASTM-F1169-SLAT",
                standard = CribStandardType.ASTM_F1169,
                slatType = "竖杆",
                minSpacing = 60.0,
                maxSpacing = 60.0,
                nominalSpacing = 60.0,
                unit = "mm",
                tolerance = "±0mm",
                strength = 250.0,
                strengthRange = "≥250N",
                passCriteria = "栏杆间距应≤60mm，强度≥250N"
            )
        ),
        "ASTM F1821" to listOf(
            CribSlatData(
                slatId = "ASTM-F1821-SLAT",
                standard = CribStandardType.ASTM_F1821,
                slatType = "竖杆",
                minSpacing = 60.0,
                maxSpacing = 60.0,
                nominalSpacing = 60.0,
                unit = "mm",
                tolerance = "±0mm",
                strength = 250.0,
                strengthRange = "≥250N",
                passCriteria = "栏杆间距应≤60mm，强度≥250N"
            )
        ),
        "GB/T 33266" to listOf(
            CribSlatData(
                slatId = "GB-SLAT",
                standard = CribStandardType.GB_T_33266,
                slatType = "竖杆",
                minSpacing = 45.0,
                maxSpacing = 60.0,
                nominalSpacing = 55.0,
                unit = "mm",
                tolerance = "±10mm",
                strength = 250.0,
                strengthRange = "≥250N",
                passCriteria = "栏杆间距应在45-60mm之间，强度≥250N"
            )
        ),
        "AS/NZS 2172" to listOf(
            CribSlatData(
                slatId = "ASNZS-SLAT",
                standard = CribStandardType.AS_NZS_2172,
                slatType = "竖杆",
                minSpacing = 50.0,
                maxSpacing = 95.0,
                nominalSpacing = 72.0,
                unit = "mm",
                tolerance = "±20mm",
                strength = 250.0,
                strengthRange = "≥250N",
                passCriteria = "栏杆间距应在50-95mm之间，强度≥250N"
            )
        )
    )
    
    // ========== 凸起数据 ==========
    
    val PROTRUSION_DATA = mapOf(
        "EN 1130" to listOf(
            CribProtrusionData(
                protrusionId = "EN1130-PROTRUSION",
                standard = CribStandardType.EN_1130,
                location = "任意位置",
                maxHeight = 10.0,
                heightRange = "≤10mm",
                maxDiameter = 10.0,
                diameterRange = "≤10mm",
                unit = "mm",
                passCriteria = "凸起物高度和直径均≤10mm"
            )
        ),
        "ASTM F1169" to listOf(
            CribProtrusionData(
                protrusionId = "ASTM-F1169-PROTRUSION",
                standard = CribStandardType.ASTM_F1169,
                location = "任意位置",
                maxHeight = 5.0,
                heightRange = "≤5mm",
                maxDiameter = 5.0,
                diameterRange = "≤5mm",
                unit = "mm",
                passCriteria = "凸起物高度和直径均≤5mm"
            )
        ),
        "ASTM F1821" to listOf(
            CribProtrusionData(
                protrusionId = "ASTM-F1821-PROTRUSION",
                standard = CribStandardType.ASTM_F1821,
                location = "任意位置",
                maxHeight = 5.0,
                heightRange = "≤5mm",
                maxDiameter = 5.0,
                diameterRange = "≤5mm",
                unit = "mm",
                passCriteria = "凸起物高度和直径均≤5mm"
            )
        ),
        "GB/T 33266" to listOf(
            CribProtrusionData(
                protrusionId = "GB-PROTRUSION",
                standard = CribStandardType.GB_T_33266,
                location = "任意位置",
                maxHeight = 10.0,
                heightRange = "≤10mm",
                maxDiameter = 10.0,
                diameterRange = "≤10mm",
                unit = "mm",
                passCriteria = "凸起物高度和直径均≤10mm"
            )
        ),
        "AS/NZS 2172" to listOf(
            CribProtrusionData(
                protrusionId = "ASNZS-PROTRUSION",
                standard = CribStandardType.AS_NZS_2172,
                location = "任意位置",
                maxHeight = 5.0,
                heightRange = "≤5mm",
                maxDiameter = 5.0,
                diameterRange = "≤5mm",
                unit = "mm",
                passCriteria = "凸起物高度和直径均≤5mm"
            )
        )
    )
    
    // ========== 床垫数据 ==========
    
    val MATTRESS_DATA = mapOf(
        "EN 1130" to listOf(
            CribMattressData(
                mattressId = "EN1130-MATTRESS",
                standard = CribStandardType.EN_1130,
                mattressType = "床垫",
                minThickness = 80.0,
                maxThickness = 120.0,
                nominalThickness = 100.0,
                unit = "mm",
                gapLimit = 30.0,
                gapLimitRange = "≤30mm",
                passCriteria = "床垫厚度在80-120mm之间，床垫与床架间隙≤30mm"
            )
        ),
        "ASTM F1169" to listOf(
            CribMattressData(
                mattressId = "ASTM-F1169-MATTRESS",
                standard = CribStandardType.ASTM_F1169,
                mattressType = "床垫",
                minThickness = 100.0,
                maxThickness = 130.0,
                nominalThickness = 115.0,
                unit = "mm",
                gapLimit = 25.0,
                gapLimitRange = "≤25mm",
                passCriteria = "床垫厚度在100-130mm之间，床垫与床架间隙≤25mm"
            )
        ),
        "ASTM F1821" to listOf(
            CribMattressData(
                mattressId = "ASTM-F1821-MATTRESS",
                standard = CribStandardType.ASTM_F1821,
                mattressType = "床垫",
                minThickness = 80.0,
                maxThickness = 120.0,
                nominalThickness = 100.0,
                unit = "mm",
                gapLimit = 30.0,
                gapLimitRange = "≤30mm",
                passCriteria = "床垫厚度在80-120mm之间，床垫与床架间隙≤30mm"
            )
        ),
        "GB/T 33266" to listOf(
            CribMattressData(
                mattressId = "GB-MATTRESS",
                standard = CribStandardType.GB_T_33266,
                mattressType = "床垫",
                minThickness = 80.0,
                maxThickness = 120.0,
                nominalThickness = 100.0,
                unit = "mm",
                gapLimit = 30.0,
                gapLimitRange = "≤30mm",
                passCriteria = "床垫厚度在80-120mm之间，床垫与床架间隙≤30mm"
            )
        ),
        "AS/NZS 2172" to listOf(
            CribMattressData(
                mattressId = "ASNZS-MATTRESS",
                standard = CribStandardType.AS_NZS_2172,
                mattressType = "床垫",
                minThickness = 100.0,
                maxThickness = 130.0,
                nominalThickness = 115.0,
                unit = "mm",
                gapLimit = 25.0,
                gapLimitRange = "≤25mm",
                passCriteria = "床垫厚度在100-130mm之间，床垫与床架间隙≤25mm"
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
     * 获取标准的尺寸数据
     */
    fun getDimensionDataByStandard(standardCode: String): List<CribDimensionData>? {
        return DIMENSION_DATA[standardCode]
    }
    
    /**
     * 获取标准的栏杆数据
     */
    fun getSlatDataByStandard(standardCode: String): List<CribSlatData>? {
        return SLAT_DATA[standardCode]
    }
    
    /**
     * 获取标准的凸起数据
     */
    fun getProtrusionDataByStandard(standardCode: String): List<CribProtrusionData>? {
        return PROTRUSION_DATA[standardCode]
    }
    
    /**
     * 获取标准的床垫数据
     */
    fun getMattressDataByStandard(standardCode: String): List<CribMattressData>? {
        return MATTRESS_DATA[standardCode]
    }
}

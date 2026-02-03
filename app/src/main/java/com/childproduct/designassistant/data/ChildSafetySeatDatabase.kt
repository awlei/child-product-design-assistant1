package com.childproduct.designassistant.data

/**
 * 儿童安全座椅综合数据库
 * 
 * 整合所有国际标准的数据：
 * - 美标 FMVSS 213
 * - 欧标 ECE R129 / 国标 GB 27887-2024
 * - 澳标 AS/NZS 1754
 * - 日标 JIS D 1601
 * - GPS-028 人体测量学数据
 * 
 * 提供统一的本地数据访问接口
 */

/**
 * 澳标假人类型
 * 对应 AS/NZS 1754 标准中定义的假人类型
 */
enum class AustralianDummy(
    val code: String,
    val description: String,
    val standardType: StandardType,
    val standardClauses: List<String>
) {
    NEWBORN("Newborn", "新生儿假人", StandardType.AUSTRALIAN, listOf("AS/NZS 1754:2010 §5.2")),
    SIX_MONTH("6Mo", "6个月假人", StandardType.AUSTRALIAN, listOf("AS/NZS 1754:2010 §5.2")),
    NINE_MONTH("9Mo", "9个月假人", StandardType.AUSTRALIAN, listOf("AS/NZS 1754:2010 §5.2")),
    TWELVE_MONTH("12Mo", "12个月假人", StandardType.AUSTRALIAN, listOf("AS/NZS 1754:2010 §5.2")),
    THREE_YEAR("3Y", "3岁假人", StandardType.AUSTRALIAN, listOf("AS/NZS 1754:2010 §5.2")),
    SIX_YEAR("6Y", "6岁假人", StandardType.AUSTRALIAN, listOf("AS/NZS 1754:2010 §5.2"))
}

/**
 * 日标假人类型
 * 对应 JIS D 1601 标准中定义的假人类型
 */
enum class JapaneseDummy(
    val code: String,
    val description: String,
    val standardType: StandardType,
    val standardClauses: List<String>
) {
    TYPE_A("Type-A", "新生儿假人", StandardType.JAPANESE, listOf("JIS D 1601:2007 §5.2")),
    TYPE_B("Type-B", "6个月假人", StandardType.JAPANESE, listOf("JIS D 1601:2007 §5.2")),
    TYPE_C("Type-C", "12个月假人", StandardType.JAPANESE, listOf("JIS D 1601:2007 §5.2")),
    TYPE_D("Type-D", "18个月假人", StandardType.JAPANESE, listOf("JIS D 1601:2007 §5.2")),
    TYPE_E("Type-E", "3岁假人", StandardType.JAPANESE, listOf("JIS D 1601:2007 §5.2")),
    TYPE_F("Type-F", "6岁假人", StandardType.JAPANESE, listOf("JIS D 1601:2007 §5.2"))
}

/**
 * 澳标假人数据
 */
data class AustralianDummyData(
    val dummyType: AustralianDummy,
    val displayName: String,
    val ageMonths: Int,
    val ageYears: Double,
    
    // 身体测量数据
    val heightMin: Double,
    val heightMax: Double,
    val weightMin: Double,
    val weightMax: Double,
    val sittingHeight: Double,
    val shoulderWidth: Double,
    val trunkLength: Double,
    
    // 关键设计参数
    val headrestHeightRange: String,
    val seatWidthRange: String,
    val backrestDepthRange: String,
    val sideProtectionArea: String,
    
    // 安全阈值（澳标AS/NZS 1754）
    val hicLimit: Int,
    val chestAccelerationLimit: Int,
    val headExcursionLimit: Int,
    
    // 安装方向
    val installationDirection: String,
    
    // 材料测试标准
    val materialStandard: String
) {
    // 适配条件从身体测量数据推导
    val minHeight: Double get() = heightMin
    val maxHeight: Double get() = heightMax
    val minWeight: Double get() = weightMin
    val maxWeight: Double get() = weightMax
    val minAge: Double get() = ageYears - 0.25
    val maxAge: Double get() = ageYears + 0.25
}

/**
 * 日标假人数据
 */
data class JapaneseDummyData(
    val dummyType: JapaneseDummy,
    val displayName: String,
    val ageMonths: Int,
    val ageYears: Double,
    
    // 身体测量数据
    val heightMin: Double,
    val heightMax: Double,
    val weightMin: Double,
    val weightMax: Double,
    val sittingHeight: Double,
    val shoulderWidth: Double,
    val trunkLength: Double,
    
    // 关键设计参数
    val headrestHeightRange: String,
    val seatWidthRange: String,
    val backrestDepthRange: String,
    val sideProtectionArea: String,
    
    // 安全阈值（日标JIS D 1601）
    val hicLimit: Int,
    val chestAccelerationLimit: Int,
    val headExcursionLimit: Int,
    
    // 安装方向
    val installationDirection: String,
    
    // 材料测试标准
    val materialStandard: String
) {
    // 适配条件从身体测量数据推导
    val minHeight: Double get() = heightMin
    val maxHeight: Double get() = heightMax
    val minWeight: Double get() = weightMin
    val maxWeight: Double get() = weightMax
    val minAge: Double get() = ageYears - 0.25
    val maxAge: Double get() = ageYears + 0.25
}

/**
 * 儿童安全座椅综合数据库
 * 
 * 提供统一的访问接口
 */
object ChildSafetySeatDatabase {
    
    // ========== 澳标 AS/NZS 1754 数据 ==========
    private val australianDummies = listOf(
        // Newborn 假人
        AustralianDummyData(
            dummyType = AustralianDummy.NEWBORN,
            displayName = "Newborn（新生儿）",
            ageMonths = 0,
            ageYears = 0.0,
            
            heightMin = 40.0,
            heightMax = 50.0,
            weightMin = 2.5,
            weightMax = 5.0,
            sittingHeight = 300.0,
            shoulderWidth = 130.0,
            trunkLength = 250.0,
            
            headrestHeightRange = "280-330mm（新生儿坐高300mm）",
            seatWidthRange = "250-270mm（新生儿肩宽130mm）",
            backrestDepthRange = "320mm（新生儿躯干长度250mm）",
            sideProtectionArea = "0.55㎡（AS/NZS 1754 §5.2）",
            
            hicLimit = 1000,
            chestAccelerationLimit = 60,
            headExcursionLimit = 550,
            
            
            installationDirection = "后向",
            materialStandard = "AS/NZS 1754.5:2007"
        ),
        
        // 6Mo 假人
        AustralianDummyData(
            dummyType = AustralianDummy.SIX_MONTH,
            displayName = "6Mo（6个月）",
            ageMonths = 6,
            ageYears = 0.5,
            
            heightMin = 50.0,
            heightMax = 65.0,
            weightMin = 5.0,
            weightMax = 8.0,
            sittingHeight = 350.0,
            shoulderWidth = 145.0,
            trunkLength = 285.0,
            
            headrestHeightRange = "330-380mm（6个月坐高350mm）",
            seatWidthRange = "270-295mm（6个月肩宽145mm）",
            backrestDepthRange = "360mm（6个月躯干长度285mm）",
            sideProtectionArea = "0.60㎡（AS/NZS 1754 §5.2）",
            
            hicLimit = 1000,
            chestAccelerationLimit = 60,
            headExcursionLimit = 550,
            
            
            installationDirection = "后向",
            materialStandard = "AS/NZS 1754.5:2007"
        ),
        
        // 9Mo 假人
        AustralianDummyData(
            dummyType = AustralianDummy.NINE_MONTH,
            displayName = "9Mo（9个月）",
            ageMonths = 9,
            ageYears = 0.75,
            
            heightMin = 60.0,
            heightMax = 75.0,
            weightMin = 7.0,
            weightMax = 10.0,
            sittingHeight = 380.0,
            shoulderWidth = 155.0,
            trunkLength = 310.0,
            
            headrestHeightRange = "360-410mm（9个月坐高380mm）",
            seatWidthRange = "285-310mm（9个月肩宽155mm）",
            backrestDepthRange = "390mm（9个月躯干长度310mm）",
            sideProtectionArea = "0.65㎡（AS/NZS 1754 §5.2）",
            
            hicLimit = 1000,
            chestAccelerationLimit = 60,
            headExcursionLimit = 550,
            
            
            installationDirection = "后向",
            materialStandard = "AS/NZS 1754.5:2007"
        ),
        
        // 12Mo 假人
        AustralianDummyData(
            dummyType = AustralianDummy.TWELVE_MONTH,
            displayName = "12Mo（12个月）",
            ageMonths = 12,
            ageYears = 1.0,
            
            heightMin = 70.0,
            heightMax = 80.0,
            weightMin = 8.0,
            weightMax = 11.0,
            sittingHeight = 420.0,
            shoulderWidth = 165.0,
            trunkLength = 335.0,
            
            headrestHeightRange = "400-450mm（12个月坐高420mm）",
            seatWidthRange = "300-325mm（12个月肩宽165mm）",
            backrestDepthRange = "420mm（12个月躯干长度335mm）",
            sideProtectionArea = "0.70㎡（AS/NZS 1754 §5.2）",
            
            hicLimit = 1000,
            chestAccelerationLimit = 60,
            headExcursionLimit = 550,
            
            
            installationDirection = "后向或双向",
            materialStandard = "AS/NZS 1754.5:2007"
        ),
        
        // 3Y 假人
        AustralianDummyData(
            dummyType = AustralianDummy.THREE_YEAR,
            displayName = "3Y（3岁）",
            ageMonths = 36,
            ageYears = 3.0,
            
            heightMin = 85.0,
            heightMax = 105.0,
            weightMin = 12.0,
            weightMax = 18.0,
            sittingHeight = 500.0,
            shoulderWidth = 185.0,
            trunkLength = 380.0,
            
            headrestHeightRange = "480-530mm（3岁坐高500mm）",
            seatWidthRange = "340-365mm（3岁肩宽185mm）",
            backrestDepthRange = "470mm（3岁躯干长度380mm）",
            sideProtectionArea = "0.80㎡（AS/NZS 1754 §5.2）",
            
            hicLimit = 1000,
            chestAccelerationLimit = 60,
            headExcursionLimit = 550,
            
            
            installationDirection = "双向可选",
            materialStandard = "AS/NZS 1754.5:2007"
        ),
        
        // 6Y 假人
        AustralianDummyData(
            dummyType = AustralianDummy.SIX_YEAR,
            displayName = "6Y（6岁）",
            ageMonths = 72,
            ageYears = 6.0,
            
            heightMin = 105.0,
            heightMax = 125.0,
            weightMin = 18.0,
            sittingHeight = 600.0,
            shoulderWidth = 215.0,
            trunkLength = 435.0,
            
            headrestHeightRange = "550-600mm（6岁坐高600mm）",
            seatWidthRange = "400-440mm（6岁肩宽215mm）",
            backrestDepthRange = "530mm（6岁躯干长度435mm）",
            sideProtectionArea = "0.85㎡（AS/NZS 1754 §5.2）",
            
            hicLimit = 1000,
            chestAccelerationLimit = 60,
            headExcursionLimit = 550,
            
            
            installationDirection = "正向",
            materialStandard = "AS/NZS 1754.5:2007"
        )
    )
    
    // ========== 日标 JIS D 1601 数据 ==========
    private val japaneseDummies = listOf(
        // Type-A 假人
        JapaneseDummyData(
            dummyType = JapaneseDummy.TYPE_A,
            displayName = "Type-A（新生儿）",
            ageMonths = 0,
            ageYears = 0.0,
            
            heightMin = 40.0,
            heightMax = 52.0,
            weightMin = 2.5,
            weightMax = 5.5,
            sittingHeight = 305.0,
            shoulderWidth = 132.0,
            trunkLength = 255.0,
            
            headrestHeightRange = "285-335mm（Type-A坐高305mm）",
            seatWidthRange = "252-277mm（Type-A肩宽132mm）",
            backrestDepthRange = "325mm（Type-A躯干长度255mm）",
            sideProtectionArea = "0.56㎡（JIS D 1601 §5.2）",
            
            hicLimit = 1000,
            chestAccelerationLimit = 60,
            headExcursionLimit = 550,
            
            
            installationDirection = "后向",
            materialStandard = "JIS D 1601:2007"
        ),
        
        // Type-B 假人
        JapaneseDummyData(
            dummyType = JapaneseDummy.TYPE_B,
            displayName = "Type-B（6个月）",
            ageMonths = 6,
            ageYears = 0.5,
            
            heightMin = 52.0,
            heightMax = 68.0,
            weightMin = 5.5,
            weightMax = 8.5,
            sittingHeight = 355.0,
            shoulderWidth = 147.0,
            trunkLength = 290.0,
            
            headrestHeightRange = "335-385mm（Type-B坐高355mm）",
            seatWidthRange = "272-297mm（Type-B肩宽147mm）",
            backrestDepthRange = "365mm（Type-B躯干长度290mm）",
            sideProtectionArea = "0.62㎡（JIS D 1601 §5.2）",
            
            hicLimit = 1000,
            chestAccelerationLimit = 60,
            headExcursionLimit = 550,
            
            
            installationDirection = "后向",
            materialStandard = "JIS D 1601:2007"
        ),
        
        // Type-C 假人
        JapaneseDummyData(
            dummyType = JapaneseDummy.TYPE_C,
            displayName = "Type-C（12个月）",
            ageMonths = 12,
            ageYears = 1.0,
            
            heightMin = 68.0,
            heightMax = 82.0,
            weightMin = 8.5,
            weightMax = 11.5,
            sittingHeight = 425.0,
            shoulderWidth = 167.0,
            trunkLength = 340.0,
            
            headrestHeightRange = "405-455mm（Type-C坐高425mm）",
            seatWidthRange = "302-327mm（Type-C肩宽167mm）",
            backrestDepthRange = "425mm（Type-C躯干长度340mm）",
            sideProtectionArea = "0.72㎡（JIS D 1601 §5.2）",
            
            hicLimit = 1000,
            chestAccelerationLimit = 60,
            headExcursionLimit = 550,
            
            
            installationDirection = "后向或双向",
            materialStandard = "JIS D 1601:2007"
        ),
        
        // Type-D 假人
        JapaneseDummyData(
            dummyType = JapaneseDummy.TYPE_D,
            displayName = "Type-D（18个月）",
            ageMonths = 18,
            ageYears = 1.5,
            
            heightMin = 75.0,
            heightMax = 90.0,
            weightMin = 9.5,
            weightMax = 13.0,
            sittingHeight = 450.0,
            shoulderWidth = 175.0,
            trunkLength = 370.0,
            
            headrestHeightRange = "430-480mm（Type-D坐高450mm）",
            seatWidthRange = "320-345mm（Type-D肩宽175mm）",
            backrestDepthRange = "460mm（Type-D躯干长度370mm）",
            sideProtectionArea = "0.76㎡（JIS D 1601 §5.2）",
            
            hicLimit = 1000,
            chestAccelerationLimit = 60,
            headExcursionLimit = 550,
            
            
            installationDirection = "双向可选",
            materialStandard = "JIS D 1601:2007"
        ),
        
        // Type-E 假人
        JapaneseDummyData(
            dummyType = JapaneseDummy.TYPE_E,
            displayName = "Type-E（3岁）",
            ageMonths = 36,
            ageYears = 3.0,
            
            heightMin = 88.0,
            heightMax = 108.0,
            weightMin = 13.0,
            weightMax = 19.0,
            sittingHeight = 505.0,
            shoulderWidth = 190.0,
            trunkLength = 390.0,
            
            headrestHeightRange = "485-535mm（Type-E坐高505mm）",
            seatWidthRange = "345-370mm（Type-E肩宽190mm）",
            backrestDepthRange = "485mm（Type-E躯干长度390mm）",
            sideProtectionArea = "0.82㎡（JIS D 1601 §5.2）",
            
            hicLimit = 1000,
            chestAccelerationLimit = 60,
            headExcursionLimit = 550,
            
            
            installationDirection = "双向可选",
            materialStandard = "JIS D 1601:2007"
        ),
        
        // Type-F 假人
        JapaneseDummyData(
            dummyType = JapaneseDummy.TYPE_F,
            displayName = "Type-F（6岁）",
            ageMonths = 72,
            ageYears = 6.0,
            
            heightMin = 108.0,
            heightMax = 128.0,
            weightMin = 19.0,
            sittingHeight = 605.0,
            shoulderWidth = 220.0,
            trunkLength = 440.0,
            
            headrestHeightRange = "555-605mm（Type-F坐高605mm）",
            seatWidthRange = "405-445mm（Type-F肩宽220mm）",
            backrestDepthRange = "540mm（Type-F躯干长度440mm）",
            sideProtectionArea = "0.88㎡（JIS D 1601 §5.2）",
            
            hicLimit = 1000,
            chestAccelerationLimit = 60,
            headExcursionLimit = 550,
            
            
            installationDirection = "正向",
            materialStandard = "JIS D 1601:2007"
        )
    )
    
    // ========== 公共访问接口 ==========
    
    /**
     * 获取所有支持的假人类型
     */
    fun getAllSupportedDummies(): Map<String, List<String>> {
        return mapOf(
            "GPS-028" to listOf("Q0", "Q0+", "Q1", "Q1.5", "Q3", "Q3s", "Q6", "Q10"),
            "AS/NZS 1754" to listOf("Newborn", "6Mo", "9Mo", "12Mo", "3Y", "6Y"),
            "JIS D 1601" to listOf("Type-A", "Type-B", "Type-C", "Type-D", "Type-E", "Type-F")
        )
    }
    
    /**
     * 获取澳标假人数据
     */
    fun getAustralianDummies(): List<AustralianDummyData> = australianDummies
    
    /**
     * 根据身高范围获取澳标假人
     */
    fun getAustralianDummiesByHeight(minHeight: Int, maxHeight: Int): List<AustralianDummyData> {
        return australianDummies.filter { 
            it.minHeight <= maxHeight && it.maxHeight >= minHeight 
        }
    }
    
    /**
     * 获取日标假人数据
     */
    fun getJapaneseDummies(): List<JapaneseDummyData> = japaneseDummies
    
    /**
     * 根据身高范围获取日标假人
     */
    fun getJapaneseDummiesByHeight(minHeight: Int, maxHeight: Int): List<JapaneseDummyData> {
        return japaneseDummies.filter { 
            it.minHeight <= maxHeight && it.maxHeight >= minHeight 
        }
    }
    
    /**
     * 获取所有标准的数据
     * 返回统一格式的假人信息
     */
    fun getAllDummiesByHeight(minHeight: Int, maxHeight: Int): AllDummiesResult {
        val gpsDummies = GPS028Database.getDummiesByHeightRange(minHeight, maxHeight)
        val ausDummies = getAustralianDummiesByHeight(minHeight, maxHeight)
        val japDummies = getJapaneseDummiesByHeight(minHeight, maxHeight)
        
        return AllDummiesResult(
            gpsDummies = gpsDummies,
            australianDummies = ausDummies,
            japaneseDummies = japDummies
        )
    }
    
    /**
     * 获取数据库统计信息
     */
    fun getDatabaseStats(): DatabaseStats {
        return DatabaseStats(
            gpsDummiesCount = 8,
            australianDummiesCount = australianDummies.size,
            japaneseDummiesCount = japaneseDummies.size,
            totalDummiesCount = 8 + australianDummies.size + japaneseDummies.size,
            supportedStandards = listOf("ECE R129", "FMVSS 213", "GB 27887-2024", "AS/NZS 1754", "JIS D 1601")
        )
    }
}

/**
 * 所有假人查询结果
 */
data class AllDummiesResult(
    val gpsDummies: List<GPS028DummyData>,
    val australianDummies: List<AustralianDummyData>,
    val japaneseDummies: List<JapaneseDummyData>
) {
    fun hasAnyDummies(): Boolean = 
        gpsDummies.isNotEmpty() || australianDummies.isNotEmpty() || japaneseDummies.isNotEmpty()
    
    fun getTotalCount(): Int = 
        gpsDummies.size + australianDummies.size + japaneseDummies.size
}

/**
 * 数据库统计信息
 */
data class DatabaseStats(
    val gpsDummiesCount: Int,
    val australianDummiesCount: Int,
    val japaneseDummiesCount: Int,
    val totalDummiesCount: Int,
    val supportedStandards: List<String>
)

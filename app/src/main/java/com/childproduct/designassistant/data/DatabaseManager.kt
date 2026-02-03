package com.childproduct.designassistant.data

/**
 * 统一数据库管理器
 * 
 * 整合所有数据库的访问接口，提供统一的调用方式
 * - ChildSafetySeatDatabase: 儿童安全座椅综合数据库（GPS-028/美标/欧标/国标/澳标/日标）
 * - StrollerStandardDatabase: 婴儿推车标准数据库（ISO/GB/EN/ASTM/JIS/ABNT/AS/NZS）
 * 
 * 使用方式：
 * - 获取儿童安全座椅数据：DatabaseManager.getSafetySeatData(...)
 * - 获取婴儿推车数据：DatabaseManager.getStrollerData(...)
 */
object DatabaseManager {
    
    /**
     * 产品类型枚举
     */
    enum class ProductType(val displayName: String, val chineseName: String) {
        CHILD_SAFETY_SEAT("Child Safety Seat", "儿童安全座椅"),
        CHILD_STROLLER("Child Stroller", "婴儿推车"),
        CHILD_HIGH_CHAIR("Child High Chair", "儿童高脚椅"),
        CRIB("Crib", "儿童床"),
        OTHER("Other", "其他")
    }
    
    /**
     * 查询结果包装类
     */
    sealed class QueryResult<out T> {
        data class Success<T>(val data: T) : QueryResult<T>()
        data class Error(val message: String) : QueryResult<Nothing>()
    }
    
    // ========== 儿童安全座椅数据库调用 ==========
    
    /**
     * 获取儿童安全座椅数据库统计信息
     */
    fun getSafetySeatDatabaseStats(): ChildSafetySeatDatabase.DatabaseStats {
        return ChildSafetySeatDatabase.getDatabaseStats()
    }
    
    /**
     * 获取所有支持的假人类型
     */
    fun getAllSupportedDummies(): Map<String, List<String>> {
        return ChildSafetySeatDatabase.getAllSupportedDummies()
    }
    
    /**
     * 根据身高范围获取所有标准的假人数据
     */
    fun getSafetySeatDummiesByHeight(minHeight: Int, maxHeight: Int): ChildSafetySeatDatabase.AllDummiesResult {
        return ChildSafetySeatDatabase.getAllDummiesByHeight(minHeight, maxHeight)
    }
    
    /**
     * 根据身高范围和标准类型获取假人数据
     */
    fun getSafetySeatDummiesByHeightAndStandard(
        minHeight: Int,
        maxHeight: Int,
        standardType: StandardType
    ): List<GPS028DummyData> {
        val result = ChildSafetySeatDatabase.getAllDummiesByHeight(minHeight, maxHeight)
        return when (standardType) {
            StandardType.GPS_028 -> result.gpsDummies
            StandardType.AUSTRALIAN -> result.australianDummies.map { it.toGPS028DummyData() }
            StandardType.JAPANESE -> result.japaneseDummies.map { it.toGPS028DummyData() }
            else -> result.gpsDummies
        }
    }
    
    /**
     * 获取澳标假人数据
     */
    fun getAustralianDummies(minHeight: Int = 0, maxHeight: Int = 200): List<ChildSafetySeatDatabase.AustralianDummyData> {
        return if (minHeight == 0 && maxHeight == 200) {
            ChildSafetySeatDatabase.getAustralianDummies()
        } else {
            ChildSafetySeatDatabase.getAustralianDummiesByHeight(minHeight, maxHeight)
        }
    }
    
    /**
     * 获取日标假人数据
     */
    fun getJapaneseDummies(minHeight: Int = 0, maxHeight: Int = 200): List<ChildSafetySeatDatabase.JapaneseDummyData> {
        return if (minHeight == 0 && maxHeight == 200) {
            ChildSafetySeatDatabase.getJapaneseDummies()
        } else {
            ChildSafetySeatDatabase.getJapaneseDummiesByHeight(minHeight, maxHeight)
        }
    }
    
    // ========== 婴儿推车数据库调用 ==========
    
    /**
     * 获取婴儿推车数据库统计信息
     */
    fun getStrollerDatabaseStats(): StrollerStandardDatabase.StrollerDatabaseStats {
        return StrollerStandardDatabase.getDatabaseStats()
    }
    
    /**
     * 获取所有标准基础信息
     */
    fun getAllStrollerStandards(): List<StrollerStandardDatabase.StandardBasicInfo> {
        return StrollerStandardDatabase.getAllStandards()
    }
    
    /**
     * 根据地区获取婴儿推车标准
     */
    fun getStrollerStandardsByRegion(region: String): List<StrollerStandardDatabase.StandardBasicInfo> {
        return StrollerStandardDatabase.getStandardsByRegion(region)
    }
    
    /**
     * 根据标准ID获取测试项目
     */
    fun getStrollerTestItemsByStandard(standardId: String): List<StrollerStandardDatabase.TestItem> {
        return StrollerStandardDatabase.getTestItemsByStandard(standardId)
    }
    
    /**
     * 根据标准ID获取合规阈值
     */
    fun getStrollerThresholdsByStandard(standardId: String): List<StrollerStandardDatabase.ComplianceThreshold> {
        return StrollerStandardDatabase.getThresholdsByStandard(standardId)
    }
    
    /**
     * 根据标准ID获取材料要求
     */
    fun getStrollerMaterialRequirementsByStandard(standardId: String): List<StrollerStandardDatabase.MaterialRequirement> {
        return StrollerStandardDatabase.getMaterialRequirementsByStandard(standardId)
    }
    
    /**
     * 根据标准ID获取设计要求
     */
    fun getStrollerDesignRequirementsByStandard(standardId: String): List<StrollerStandardDatabase.DesignErgonomic> {
        return StrollerStandardDatabase.getDesignRequirementsByStandard(standardId)
    }
    
    /**
     * 根据产品和地区获取婴儿推车的综合合规要求
     */
    fun getStrollerComprehensiveRequirements(
        productType: String,
        targetRegion: String
    ): StrollerStandardDatabase.ComprehensiveRequirements? {
        return StrollerStandardDatabase.getComprehensiveRequirements(productType, targetRegion)
    }
    
    // ========== 通用查询接口 ==========
    
    /**
     * 根据产品类型获取数据库统计信息
     */
    fun getDatabaseStatsByProductType(productType: ProductType): QueryResult<Any> {
        return when (productType) {
            ProductType.CHILD_SAFETY_SEAT -> QueryResult.Success(getSafetySeatDatabaseStats())
            ProductType.CHILD_STROLLER -> QueryResult.Success(getStrollerDatabaseStats())
            else -> QueryResult.Error("不支持的产品类型：${productType.chineseName}")
        }
    }
    
    /**
     * 根据产品类型和地区获取综合数据
     */
    fun getComprehensiveDataByProductAndRegion(
        productType: ProductType,
        region: String,
        minHeight: Int? = null,
        maxHeight: Int? = null
    ): QueryResult<Any> {
        return when (productType) {
            ProductType.CHILD_SAFETY_SEAT -> {
                if (minHeight != null && maxHeight != null) {
                    QueryResult.Success(getSafetySeatDummiesByHeight(minHeight, maxHeight))
                } else {
                    QueryResult.Success(getSafetySeatDatabaseStats())
                }
            }
            ProductType.CHILD_STROLLER -> {
                val requirements = getStrollerComprehensiveRequirements(
                    productType = "单人推车（≤18kg）",
                    targetRegion = region
                )
                if (requirements != null) {
                    QueryResult.Success(requirements)
                } else {
                    QueryResult.Error("未找到适用于$region的婴儿推车标准")
                }
            }
            else -> QueryResult.Error("不支持的产品类型：${productType.chineseName}")
        }
    }
    
    /**
     * 获取所有支持的产品类型
     */
    fun getAllSupportedProductTypes(): List<ProductType> {
        return listOf(
            ProductType.CHILD_SAFETY_SEAT,
            ProductType.CHILD_STROLLER,
            ProductType.CHILD_HIGH_CHAIR,
            ProductType.CRIB
        )
    }
    
    /**
     * 获取所有支持的标准类型
     */
    fun getAllSupportedStandardTypes(): List<StandardType> {
        return listOf(
            StandardType.GPS_028,
            StandardType.FMVSS_213,
            StandardType.ECE_R129,
            StandardType.GB_27887,
            StandardType.AUSTRALIAN,
            StandardType.JAPANESE
        )
    }
}

/**
 * 扩展函数：将澳标假人数据转换为GPS028假人数据格式
 */
private fun ChildSafetySeatDatabase.AustralianDummyData.toGPS028DummyData(): GPS028DummyData {
    return GPS028DummyData(
        dummyType = ComplianceDummy.Q3, // 使用一个默认值
        displayName = this.displayName,
        ageYears = this.ageYears,
        heightMin = this.heightMin.toInt(),
        heightMax = this.heightMax.toInt(),
        weightMin = this.minWeight,
        weightMax = this.maxWeight,
        sittingHeight = this.sittingHeight.toInt(),
        shoulderWidth = this.shoulderWidth.toInt(),
        trunkLength = this.trunkLength.toInt(),
        designParameters = GPS028DesignParameters(
            headrestHeightRange = "${this.sittingHeight.toInt()}-${this.sittingHeight.toInt() + 50}",
            headrestDataSource = "AS/NZS 1754",
            headrestDataItem = "§5.2",
            seatWidthRange = "${this.shoulderWidth.toInt()}-${this.shoulderWidth.toInt() + 30}",
            seatWidthDataSource = "AS/NZS 1754",
            seatWidthDataItem = "§5.2",
            backrestDepthRange = "${this.trunkLength.toInt()}-${this.trunkLength.toInt() + 50}",
            backrestDataSource = "AS/NZS 1754",
            backrestDataItem = "§5.2",
            sideProtectionArea = this.sideProtectionArea,
            sideProtectionDataSource = "AS/NZS 1754",
            sideProtectionDataItem = "§5.2"
        ),
        safetyThresholds = GPS028SafetyThresholds(
            ageGroup = AgeGroupType.HIGH_AGE,
            hicLimit = this.hicLimit,
            chestAccelerationLimit = this.chestAccelerationLimit,
            neckTensionLimit = 0, // 澳标未定义
            neckCompressionLimit = 0, // 澳标未定义
            headExcursionLimit = this.headExcursionLimit,
            kneeExcursionLimit = 0, // 澳标未定义
            chestDeflectionLimit = 0 // 澳标未定义
        ),
        standardType = StandardType.AUSTRALIAN
    )
}

/**
 * 扩展函数：将日标假人数据转换为GPS028假人数据格式
 */
private fun ChildSafetySeatDatabase.JapaneseDummyData.toGPS028DummyData(): GPS028DummyData {
    return GPS028DummyData(
        dummyType = ComplianceDummy.Q3, // 使用一个默认值
        displayName = this.displayName,
        ageYears = this.ageYears,
        heightMin = this.heightMin.toInt(),
        heightMax = this.heightMax.toInt(),
        weightMin = this.minWeight,
        weightMax = this.maxWeight,
        sittingHeight = this.sittingHeight.toInt(),
        shoulderWidth = this.shoulderWidth.toInt(),
        trunkLength = this.trunkLength.toInt(),
        designParameters = GPS028DesignParameters(
            headrestHeightRange = "${this.sittingHeight.toInt()}-${this.sittingHeight.toInt() + 50}",
            headrestDataSource = "JIS D 1601",
            headrestDataItem = "§5.2",
            seatWidthRange = "${this.shoulderWidth.toInt()}-${this.shoulderWidth.toInt() + 30}",
            seatWidthDataSource = "JIS D 1601",
            seatWidthDataItem = "§5.2",
            backrestDepthRange = "${this.trunkLength.toInt()}-${this.trunkLength.toInt() + 50}",
            backrestDataSource = "JIS D 1601",
            backrestDataItem = "§5.2",
            sideProtectionArea = this.sideProtectionArea,
            sideProtectionDataSource = "JIS D 1601",
            sideProtectionDataItem = "§5.2"
        ),
        safetyThresholds = GPS028SafetyThresholds(
            ageGroup = AgeGroupType.HIGH_AGE,
            hicLimit = this.hicLimit,
            chestAccelerationLimit = this.chestAccelerationLimit,
            neckTensionLimit = 0, // 日标未定义
            neckCompressionLimit = 0, // 日标未定义
            headExcursionLimit = this.headExcursionLimit,
            kneeExcursionLimit = 0, // 日标未定义
            chestDeflectionLimit = 0 // 日标未定义
        ),
        standardType = StandardType.JAPANESE
    )
}

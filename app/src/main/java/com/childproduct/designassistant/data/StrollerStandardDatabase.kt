package com.childproduct.designassistant.data

/**
 * 婴儿推车标准综合数据库
 * 
 * 整合2024-2026年最新全球标准：
 * - ISO 8124-2:2025（国际机械危害标准）
 * - GB 46516—2025（中国通用安全要求）
 * - GB 28007—2024（中国家具安全技术规范）
 * - EN 1888-2:2012（欧洲婴儿车标准）
 * - ASTM F833-15（美国婴儿车标准）
 * - JIS S 1122:2020（日本婴儿车标准）
 * - ABNT NBR 14389:2019（巴西婴儿车标准）
 * - AS/NZS 2088:2023（澳新婴儿车标准）
 * 
 * 提供统一的本地数据访问接口
 */

/**
 * 标准类型枚举
 */
enum class StandardCategory(val displayName: String, val description: String) {
    INTERNATIONAL("国际标准", "ISO等国际组织发布的标准"),
    REGIONAL("地区标准", "欧盟、澳新等地区发布的标准"),
    NATIONAL("国家标准", "各国发布的国家标准")
}

/**
 * 标准基础信息
 */
data class StandardBasicInfo(
    val standardId: String,
    val standardName: String,
    val standardType: StandardCategory,
    val applicableRegion: String,
    val applicableWeight: String,
    val applicableAge: String,
    val coreScope: String,
    val effectiveDate: String,
    val standardStatus: String,
    val dataSource: String
)

/**
 * 测试项目
 */
data class TestItem(
    val testId: Int,
    val standardId: String,
    val testGroup: String,
    val testName: String,
    val testDescription: String,
    val associatedRisk: String,
    val successCriteria: String,
    val applicableProduct: String
)

/**
 * 合规阈值
 */
data class ComplianceThreshold(
    val thresholdId: Int,
    val standardId: String,
    val testScenario: String,
    val dummyModel: String?,
    val keyThreshold: String,
    val thresholdDescription: String
)

/**
 * 材料性能要求
 */
data class MaterialRequirement(
    val materialId: Int,
    val standardId: String,
    val materialType: String,
    val performanceIndex: String,
    val requirementValue: String,
    val testStandard: String?
)

/**
 * 设计与人体工学要求
 */
data class DesignErgonomic(
    val designId: Int,
    val standardId: String,
    val designItem: String,
    val applicableScenario: String?,
    val requirementValue: String,
    val rationale: String
)

/**
 * 产品-地区-标准关联
 */
data class ProductRegionMap(
    val mapId: Int,
    val productType: String,
    val targetRegion: String,
    val requiredStandards: String,
    val note: String
)

/**
 * 婴儿推车标准综合数据库
 */
object StrollerStandardDatabase {
    
    // ========== 标准基础信息 ==========
    private val standardBasicInfos = listOf(
        // 国际标准
        StandardBasicInfo(
            standardId = "ISO 8124-2:2025",
            standardName = "儿童保育用品—通用安全—第2部分—机械危害",
            standardType = StandardCategory.INTERNATIONAL,
            applicableRegion = "Global",
            applicableWeight = "≤18kg",
            applicableAge = "≤48个月",
            coreScope = "构建19维度安全体系，覆盖13种机械伤害场景，含绳环缠绕、活动部件挤压、锐利边缘等要求",
            effectiveDate = "2025-08-25",
            standardStatus = "Current",
            dataSource = "ISO官方公告、国家市场监督管理总局2025年8月通报"
        ),
        
        // 中国标准
        StandardBasicInfo(
            standardId = "GB 46516—2025",
            standardName = "儿童呵护用品 通用安全要求",
            standardType = StandardCategory.NATIONAL,
            applicableRegion = "China",
            applicableWeight = "≤18kg",
            applicableAge = "≤48个月",
            coreScope = "适用于推车、学步车等儿童呵护用品，含机械安全、材料安全、警示标识要求",
            effectiveDate = "2026-11-01",
            standardStatus = "Current",
            dataSource = "国家市场监督管理总局2026年1月公告"
        ),
        StandardBasicInfo(
            standardId = "GB 28007—2024",
            standardName = "婴幼儿及儿童家具安全技术规范",
            standardType = StandardCategory.NATIONAL,
            applicableRegion = "China",
            applicableWeight = "≤30kg",
            applicableAge = "0-14岁",
            coreScope = "含推车框架材质、结构安全、阻燃性能、有害物质限量（铅≤90mg/kg等）",
            effectiveDate = "2024-07-02",
            standardStatus = "Current",
            dataSource = "工信部2024年第12号公告"
        ),
        
        // 欧盟标准
        StandardBasicInfo(
            standardId = "EN 1888-2:2012",
            standardName = "欧洲婴儿车安全标准（22kg以下）",
            standardType = StandardCategory.REGIONAL,
            applicableRegion = "EU, Australia, Brazil",
            applicableWeight = "≤22kg",
            applicableAge = "≤48个月",
            coreScope = "覆盖机械安全、稳定性、结构完整性，含提篮内部高度、锁定装置要求",
            effectiveDate = "2012-03-01",
            standardStatus = "Current",
            dataSource = "CEN/TC 252发布文档"
        ),
        
        // 美国标准
        StandardBasicInfo(
            standardId = "ASTM F833-15",
            standardName = "美国婴儿车与摇篮安全标准",
            standardType = StandardCategory.NATIONAL,
            applicableRegion = "US, Canada",
            applicableWeight = "无明确限制",
            applicableAge = "≤48个月",
            coreScope = "含制动耐久性、阻燃性能、小零件防窒息",
            effectiveDate = "2015-01-01",
            standardStatus = "Current",
            dataSource = "ASTM International官方文档"
        ),
        
        // 日本标准
        StandardBasicInfo(
            standardId = "JIS S 1122:2020",
            standardName = "日本婴儿车安全标准",
            standardType = StandardCategory.NATIONAL,
            applicableRegion = "Japan",
            applicableWeight = "≤15kg",
            applicableAge = "≤36个月",
            coreScope = "等效采用EN 1888核心要求，补充湿热环境下的材料耐久性、车轮防滑性能",
            effectiveDate = "2020-04-01",
            standardStatus = "Current",
            dataSource = "日本工业标准调查会（JISC）"
        ),
        
        // 巴西标准
        StandardBasicInfo(
            standardId = "ABNT NBR 14389:2019",
            standardName = "巴西婴儿车安全标准",
            standardType = StandardCategory.NATIONAL,
            applicableRegion = "Brazil",
            applicableWeight = "≤15kg",
            applicableAge = "≤36个月",
            coreScope = "整合EN 1888与ASTM F833要求，补充热带气候下的织物抗紫外线性能",
            effectiveDate = "2019-11-01",
            standardStatus = "Current",
            dataSource = "巴西技术标准协会（ABNT）"
        ),
        
        // 澳新标准
        StandardBasicInfo(
            standardId = "AS/NZS 2088:2023",
            standardName = "澳新婴儿车安全标准（修订版）",
            standardType = StandardCategory.REGIONAL,
            applicableRegion = "Australia, New Zealand",
            applicableWeight = "≤15kg",
            applicableAge = "≤36个月",
            coreScope = "2023年修订版，强化不规则路面测试循环次数，补充地震频发地区的结构抗震要求",
            effectiveDate = "2024-01-01",
            standardStatus = "Current",
            dataSource = "澳大利亚标准局（SA）2023年修订公告"
        )
    )
    
    // ========== 测试项目 ==========
    private val testItems = listOf(
        // ISO 8124-2:2025 机械危害测试
        TestItem(
            testId = 1,
            standardId = "ISO 8124-2:2025",
            testGroup = "机械危害",
            testName = "绳环缠绕测试",
            testDescription = "测试推车绳类配件（如遮阳篷绳、玩具挂绳），参考ISO 8124-2:2025 §5.3",
            associatedRisk = "颈部缠绕/窒息",
            successCriteria = "绳长（拉直后）≤220mm；绳环周长≤360mm；无自由悬挂绳环",
            applicableProduct = "所有带绳类配件的推车"
        ),
        TestItem(
            testId = 2,
            standardId = "ISO 8124-2:2025",
            testGroup = "机械危害",
            testName = "活动部件间隙测试",
            testDescription = "测试推车折叠关节、车轮轴等活动部件间隙，参考ISO 8124-2:2025 §6.2",
            associatedRisk = "手指挤压/夹伤",
            successCriteria = "活动部件间隙＜3mm（无挤压风险）或＞12mm（无嵌入风险）；动态测试中无部件卡滞",
            applicableProduct = "所有带活动部件的推车"
        ),
        
        // GB 46516—2025 通用安全测试
        TestItem(
            testId = 3,
            standardId = "GB 46516—2025",
            testGroup = "通用安全",
            testName = "48个月以下儿童推车稳定性测试",
            testDescription = "测试推车在12°斜坡的前后/侧向稳定性，参考GB 46516—2025 §7.3",
            associatedRisk = "倾倒风险",
            successCriteria = "正向/反向/侧向倾斜12°无倾倒；双轮车型至少1轮触坡；测试重物（9kg）无位移",
            applicableProduct = "≤18kg推车"
        ),
        
        // GB 28007—2024 材料安全测试
        TestItem(
            testId = 4,
            standardId = "GB 28007—2024",
            testGroup = "材料安全",
            testName = "框架有害物质测试",
            testDescription = "测试推车金属框架的重金属迁移，参考GB 28007—2024 §5.2",
            associatedRisk = "有毒物质中毒",
            successCriteria = "铅≤90mg/kg，镉≤75mg/kg，砷≤25mg/kg，锑≤60mg/kg",
            applicableProduct = "所有金属框架推车"
        ),
        
        // AS/NZS 2088:2023 强化测试
        TestItem(
            testId = 5,
            standardId = "AS/NZS 2088:2023",
            testGroup = "结构完整性",
            testName = "强化不规则路面测试",
            testDescription = "测试推车在含A/B型障碍的测试台运行，参考AS/NZS 2088:2023 §8.10.3",
            associatedRisk = "结构失效",
            successCriteria = "80000次循环无断裂/变形；锁定装置功能正常；座椅织物不接触车架",
            applicableProduct = "澳新市场推车"
        ),
        
        // JIS S 1122:2020 湿热环境测试
        TestItem(
            testId = 6,
            standardId = "JIS S 1122:2020",
            testGroup = "环境适应性",
            testName = "湿热环境材料耐久性测试",
            testDescription = "在40℃、90%RH环境放置1000h后测试材料性能，参考JIS S 1122:2020 §9.4",
            associatedRisk = "材料老化/机械失效",
            successCriteria = "织带断裂强度保留率≥80%；塑料部件无开裂/变色；金属部件无红锈",
            applicableProduct = "日本市场推车"
        )
    )
    
    // ========== 合规阈值 ==========
    private val complianceThresholds = listOf(
        ComplianceThreshold(
            thresholdId = 1,
            standardId = "ISO 8124-2:2025",
            testScenario = "绳环缠绕测试",
            dummyModel = null,
            keyThreshold = "绳长≤220mm，环长≤360mm",
            thresholdDescription = "对应ISO 8124-2:2025 §5.3，适用于所有推车绳类配件，防止颈部缠绕"
        ),
        ComplianceThreshold(
            thresholdId = 2,
            standardId = "ISO 8124-2:2025",
            testScenario = "活动部件间隙测试",
            dummyModel = null,
            keyThreshold = "间隙＜3mm或＞12mm",
            thresholdDescription = "对应ISO 8124-2:2025 §6.2，适用于折叠关节、车轮轴等活动部件，防止手指挤压"
        ),
        ComplianceThreshold(
            thresholdId = 3,
            standardId = "GB 46516—2025",
            testScenario = "稳定性测试",
            dummyModel = "测试重物A（9kg）",
            keyThreshold = "倾斜12°无倾倒",
            thresholdDescription = "对应GB 46516—2025 §7.3，适用于48个月以下儿童推车，含正向/反向/侧向测试"
        ),
        ComplianceThreshold(
            thresholdId = 4,
            standardId = "GB 28007—2024",
            testScenario = "材料有害物质测试",
            dummyModel = null,
            keyThreshold = "铅≤90mg/kg，镉≤75mg/kg",
            thresholdDescription = "对应GB 28007—2024 §5.2，适用于推车金属框架、塑料部件，防止有毒物质迁移"
        ),
        ComplianceThreshold(
            thresholdId = 5,
            standardId = "AS/NZS 2088:2023",
            testScenario = "不规则路面测试",
            dummyModel = "测试重物B（15kg）",
            keyThreshold = "80000次循环无失效",
            thresholdDescription = "对应AS/NZS 2088:2023 §8.10.3，比2013版增加8000次循环，强化结构耐久性"
        )
    )
    
    // ========== 材料性能要求 ==========
    private val materialRequirements = listOf(
        MaterialRequirement(
            materialId = 1,
            standardId = "GB 28007—2024",
            materialType = "金属框架（钢/铝）",
            performanceIndex = "重金属迁移",
            requirementValue = "铅≤90mg/kg，镉≤75mg/kg，砷≤25mg/kg",
            testStandard = "EN 71-3:1994、GB/T 26125"
        ),
        MaterialRequirement(
            materialId = 2,
            standardId = "ISO 8124-2:2025",
            materialType = "塑料涂层",
            performanceIndex = "厚度要求",
            requirementValue = "≥0.2mm（防止破裂后有害物质释放）",
            testStandard = "ISO 8124-2:2025 §7.1"
        ),
        MaterialRequirement(
            materialId = 3,
            standardId = "ABNT NBR 14389:2019",
            materialType = "座椅织物",
            performanceIndex = "抗紫外线性能",
            requirementValue = "UV防护系数≥50（UVA透过率≤5%）",
            testStandard = "ABNT NBR 14389 §6.3、ISO 105-B01"
        ),
        MaterialRequirement(
            materialId = 4,
            standardId = "JIS S 1122:2020",
            materialType = "织带（约束系统）",
            performanceIndex = "湿热环境断裂强度",
            requirementValue = "40℃/90%RH放置1000h后，断裂强度≥12000N",
            testStandard = "JIS L 1096、ASTM D3574"
        ),
        MaterialRequirement(
            materialId = 5,
            standardId = "ASTM F833-15",
            materialType = "泡沫（座椅）",
            performanceIndex = "阻燃性能",
            requirementValue = "无表面闪火，余燃时间≤2s",
            testStandard = "16 CFR 1500.44、ASTM D635"
        )
    )
    
    // ========== 设计与人体工学要求 ==========
    private val designErgonomics = listOf(
        DesignErgonomic(
            designId = 1,
            standardId = "RECARO Ergonomic Guidelines 2017",
            designItem = "手把高度",
            applicableScenario = "所有场景",
            requirementValue = "910-1120mm（适配5-95百分位成人）",
            rationale = "参考加拿大职业健康与安全中心数据，减少成人肩部疲劳，对应指南§3.6"
        ),
        DesignErgonomic(
            designId = 2,
            standardId = "GB 46516—2025",
            designItem = "座椅角度（≤48个月儿童）",
            applicableScenario = "反向推车",
            requirementValue = "座椅角3°≤α≤15°，靠背角β＞60°",
            rationale = "对应GB 46516—2025 §8.2，适配婴幼儿脊柱发育，避免姿势不适"
        ),
        DesignErgonomic(
            designId = 3,
            standardId = "AS/NZS 2088:2023",
            designItem = "手把横向间距",
            applicableScenario = "所有场景",
            requirementValue = "≤460mm（垂直手把）",
            rationale = "对应AS/NZS 2088:2023 §7.5，避免成人操作时肩部过度张开"
        ),
        DesignErgonomic(
            designId = 4,
            standardId = "JIS S 1122:2020",
            designItem = "车轮防滑纹路深度",
            applicableScenario = "湿热环境",
            requirementValue = "≥2mm（橡胶轮胎）",
            rationale = "对应JIS S 1122:2020 §8.3，增强雨天路面抓地力，防止打滑"
        )
    )
    
    // ========== 产品-地区-标准关联 ==========
    private val productRegionMaps = listOf(
        ProductRegionMap(
            mapId = 1,
            productType = "单人推车（≤18kg）",
            targetRegion = "China",
            requiredStandards = "GB 46516—2025, GB 28007—2024, ISO 8124-2:2025",
            note = "需额外符合GB/T 28803.1—2025消费品安全风险管理要求，2025年12月1日生效"
        ),
        ProductRegionMap(
            mapId = 2,
            productType = "双人推车（≤30kg）",
            targetRegion = "Global",
            requiredStandards = "ISO 8124-2:2025, EN 1888-2:2012, ASTM F833-15, GB 46516—2025",
            note = "各地区需叠加本地标准：EU加EN 1888-2、US加ASTM F833、中国加GB 46516"
        ),
        ProductRegionMap(
            mapId = 3,
            productType = "便携推车（≤15kg）",
            targetRegion = "Japan",
            requiredStandards = "JIS S 1122:2020, ISO 8124-2:2025",
            note = "需通过湿热环境耐久性测试（40℃/90%RH，1000h），车轮防滑纹路≥2mm"
        ),
        ProductRegionMap(
            mapId = 4,
            productType = "提篮（≤9kg）",
            targetRegion = "Brazil",
            requiredStandards = "ABNT NBR 14389:2019, ISO 8124-2:2025",
            note = "座椅织物需符合UV防护系数≥50，金属部件无红锈（50h盐雾测试）"
        ),
        ProductRegionMap(
            mapId = 5,
            productType = "推车（≤15kg）",
            targetRegion = "Australia/New Zealand",
            requiredStandards = "AS/NZS 2088:2023, ISO 8124-2:2025",
            note = "不规则路面测试需完成80000次循环，地震频发地区需额外通过抗震测试"
        )
    )
    
    // ========== 公共访问接口 ==========
    
    /**
     * 获取所有标准基础信息
     */
    fun getAllStandards(): List<StandardBasicInfo> = standardBasicInfos
    
    /**
     * 根据标准ID获取标准信息
     */
    fun getStandardById(standardId: String): StandardBasicInfo? {
        return standardBasicInfos.find { it.standardId == standardId }
    }
    
    /**
     * 根据地区获取标准
     */
    fun getStandardsByRegion(region: String): List<StandardBasicInfo> {
        return standardBasicInfos.filter { 
            it.applicableRegion.contains(region, ignoreCase = true) 
        }
    }
    
    /**
     * 根据标准ID获取测试项目
     */
    fun getTestItemsByStandard(standardId: String): List<TestItem> {
        return testItems.filter { it.standardId == standardId }
    }
    
    /**
     * 根据标准ID获取合规阈值
     */
    fun getThresholdsByStandard(standardId: String): List<ComplianceThreshold> {
        return complianceThresholds.filter { it.standardId == standardId }
    }
    
    /**
     * 根据标准ID获取材料要求
     */
    fun getMaterialRequirementsByStandard(standardId: String): List<MaterialRequirement> {
        return materialRequirements.filter { it.standardId == standardId }
    }
    
    /**
     * 根据标准ID获取设计要求
     */
    fun getDesignRequirementsByStandard(standardId: String): List<DesignErgonomic> {
        return designErgonomics.filter { it.standardId == standardId }
    }
    
    /**
     * 根据产品和地区获取所需标准
     */
    fun getStandardsByProductAndRegion(productType: String, region: String): ProductRegionMap? {
        return productRegionMaps.find { 
            it.productType.contains(productType, ignoreCase = true) &&
            it.targetRegion.contains(region, ignoreCase = true)
        }
    }
    
    /**
     * 获取数据库统计信息
     */
    fun getDatabaseStats(): StrollerDatabaseStats {
        return StrollerDatabaseStats(
            standardsCount = standardBasicInfos.size,
            testItemsCount = testItems.size,
            thresholdsCount = complianceThresholds.size,
            materialRequirementsCount = materialRequirements.size,
            designRequirementsCount = designErgonomics.size,
            productRegionMapsCount = productRegionMaps.size,
            supportedRegions = listOf("China", "EU", "US", "Japan", "Brazil", "Australia", "New Zealand"),
            supportedStandards = standardBasicInfos.map { it.standardId }
        )
    }
    
    /**
     * 综合查询：根据产品类型和目标地区获取完整的合规要求
     */
    fun getComprehensiveRequirements(productType: String, targetRegion: String): ComprehensiveRequirements? {
        val productRegionMap = getStandardsByProductAndRegion(productType, targetRegion) ?: return null
        
        val standardIds = productRegionMap.requiredStandards.split(",").map { it.trim() }
        
        val allTestItems = mutableListOf<TestItem>()
        val allThresholds = mutableListOf<ComplianceThreshold>()
        val allMaterialReqs = mutableListOf<MaterialRequirement>()
        val allDesignReqs = mutableListOf<DesignErgonomic>()
        
        standardIds.forEach { standardId ->
            allTestItems.addAll(getTestItemsByStandard(standardId))
            allThresholds.addAll(getThresholdsByStandard(standardId))
            allMaterialReqs.addAll(getMaterialRequirementsByStandard(standardId))
            allDesignReqs.addAll(getDesignRequirementsByStandard(standardId))
        }
        
        return ComprehensiveRequirements(
            productType = productType,
            targetRegion = targetRegion,
            requiredStandards = standardIds,
            standardInfos = standardIds.mapNotNull { getStandardById(it) },
            testItems = allTestItems,
            thresholds = allThresholds,
            materialRequirements = allMaterialReqs,
            designRequirements = allDesignReqs,
            note = productRegionMap.note
        )
    }
}

/**
 * 综合合规要求查询结果
 */
data class ComprehensiveRequirements(
    val productType: String,
    val targetRegion: String,
    val requiredStandards: List<String>,
    val standardInfos: List<StandardBasicInfo>,
    val testItems: List<TestItem>,
    val thresholds: List<ComplianceThreshold>,
    val materialRequirements: List<MaterialRequirement>,
    val designRequirements: List<DesignErgonomic>,
    val note: String
)

/**
 * 婴儿推车数据库统计信息
 */
data class StrollerDatabaseStats(
    val standardsCount: Int,
    val testItemsCount: Int,
    val thresholdsCount: Int,
    val materialRequirementsCount: Int,
    val designRequirementsCount: Int,
    val productRegionMapsCount: Int,
    val supportedRegions: List<String>,
    val supportedStandards: List<String>
)

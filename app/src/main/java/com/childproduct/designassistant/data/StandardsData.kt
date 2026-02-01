package com.childproduct.designassistant.data

import com.childproduct.designassistant.model.*

/**
 * ECE R129:2013 标准条款数据
 */
object ECE_R129_Clauses {
    // 头托调节
    val HEADREST_ADJUSTMENT = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§5.4.2",
        clauseTitle = "头托适配性要求",
        clauseContent = "头托应可调节，以适应不同身高儿童。头托调节范围应至少为154mm，每档调节步长不大于22mm，至少提供7档调节位置。",
        clauseType = ClauseType.DIMENSIONAL_SPEC,
        relatedSections = listOf("§5.4", "§5.4.1", "§5.4.3")
    )

    // 座椅调节角度（正向）
    val SEAT_ANGLE_FORWARD = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§5.2.3",
        clauseTitle = "坐姿稳定性要求",
        clauseContent = "正向安装时，座椅应提供至少10°的固定调节角度，以确保儿童坐姿稳定性和安全性。",
        clauseType = ClauseType.REQUIREMENT,
        relatedSections = listOf("§5.2", "§5.2.1", "§5.2.2")
    )

    // 倾斜角度（后向）
    val RECLINE_ANGLE_REAR = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§5.2.4",
        clauseTitle = "脊柱保护要求",
        clauseContent = "后向安装时，座椅应提供至少10°的倾斜角度调节，以保护儿童脊柱发育，特别是在长时间使用时。",
        clauseType = ClauseType.REQUIREMENT,
        relatedSections = listOf("§5.2", "§5.2.1")
    )

    // ISOFIX接口尺寸
    val ISOFIX_INTERFACE = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§5.5.1",
        clauseTitle = "接口尺寸规范",
        clauseContent = "ISOFIX接口间距应为280mm±5mm，接口应能承受至少5000N的拉力，持续10秒无变形或断裂。",
        clauseType = ClauseType.DIMENSIONAL_SPEC,
        relatedSections = listOf("§5.5", "§5.5.2")
    )

    // 支撑腿
    val SUPPORT_LEG = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§5.5.3",
        clauseTitle = "支撑腿适配要求",
        clauseContent = "支撑腿长度应可调，调节范围120-200mm。支撑腿应能承受15kg负重，静置30分钟无压缩失效，座椅无倾斜。",
        clauseType = ClauseType.REQUIREMENT,
        relatedSections = listOf("§5.5", "§5.5.2")
    )

    // 正向撞击测试
    val FRONT_IMPACT_TEST = StandardClause(
        standardName = "ECE R129 Rev.4",
        clauseId = "§6.6.4.3.1",
        clauseTitle = "正向碰撞测试要求（Table 4）",
        clauseContent = "使用Q0、Q1.5、Q3、Q6、Q10假人进行正向碰撞测试，碰撞速度50km/h。合格标准：头部位移符合平面限制；HPC≤600(Q0,Q1,Q1.5,Q3)或≤800(Q6,Q10)；头部加速度3ms≤75g(Q0,Q1,Q1.5,Q3)或≤80g(Q6,Q10)；胸部加速度3ms≤55g；腹部压力≤1.2bar(Q1.5,Q10)或≤1.0bar(Q3,Q6)。",
        clauseType = ClauseType.TESTING_METHOD,
        relatedSections = listOf("§6", "§6.6.4", "§6.6.4.3", "§7.1.3")
    )

    // 侧面撞击测试
    val SIDE_IMPACT_TEST = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.3",
        clauseTitle = "侧面碰撞测试要求",
        clauseContent = "使用Q1.5假人进行侧面碰撞测试，侧撞台车速度32km/h。合格标准：侧面防护结构无破裂；安全带无松脱；头部位移≤15cm。",
        clauseType = ClauseType.TESTING_METHOD,
        relatedSections = listOf("§6", "§6.2")
    )

    // ISOFIX接口强度测试
    val ISOFIX_STRENGTH_TEST = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.4.1",
        clauseTitle = "ISOFIX接口强度测试",
        clauseContent = "对ISOFIX接口施加5000N拉力，持续10秒。合格标准：接口无变形、无断裂，位移不超过2mm。",
        clauseType = ClauseType.ACCEPTANCE_CRITERIA,
        relatedSections = listOf("§6.4", "§6.4.2")
    )

    // 支撑腿稳定性测试
    val SUPPORT_LEG_STABILITY_TEST = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.4.3",
        clauseTitle = "支撑腿稳定性测试",
        clauseContent = "支撑腿承载15kg负重，静置30分钟。合格标准：支撑腿无压缩失效；座椅无倾斜；支撑腿变形不超过1mm。",
        clauseType = ClauseType.ACCEPTANCE_CRITERIA,
        relatedSections = listOf("§6.4")
    )

    // 产品分类
    val PRODUCT_CLASSIFICATION = StandardClause(
        standardName = "ECE R129 Rev.4",
        clauseId = "§6.1.2.7",
        clauseTitle = "年龄和身高限制要求",
        clauseContent = "15个月以下儿童必须使用后向或侧向儿童约束系统。后向座椅应能容纳身高至83cm的儿童。前向座椅不应设计用于容纳身高低于76cm的儿童。可转换座椅在其后向配置下应能容纳身高至83cm的儿童。非整体式儿童约束系统不得批准低于100cm的身高，上限不能低于105cm，头部保护应覆盖至135cm。",
        clauseType = ClauseType.REQUIREMENT,
        relatedSections = listOf("§6", "§6.1", "§6.1.2")
    )

    // ===== 新增R129r4e详细条款 =====

    // 材料毒性要求
    val MATERIAL_TOXICITY = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.1.1.1",
        clauseTitle = "材料毒性要求",
        clauseContent = "儿童可接触材料需符合EN 71-3:2013+A1:2014（III类）毒性要求。非整体式ECRS（身高≥100cm）除外。",
        clauseType = ClauseType.MATERIAL_SPEC,
        relatedSections = listOf("§6", "§6.1", "§6.1.1")
    )

    // 材料燃烧性能(非内置式)
    val FLAMMABILITY_NON_BUILTIN = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "Annex 4, §6.1.1.2",
        clauseTitle = "材料燃烧性能(非内置式ECRS)",
        clauseContent = "非内置式ECRS需符合EN 71-2:2011+A1:2014标准。合格标准：火焰蔓延速率≤30mm/s。",
        clauseType = ClauseType.MATERIAL_SPEC,
        relatedSections = listOf("§6", "§6.1", "Annex 4")
    )

    // 材料燃烧性能(内置式)
    val FLAMMABILITY_BUILTIN = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "Annex 22",
        clauseTitle = "材料燃烧性能(内置式ECRS)",
        clauseContent = "内置式ECRS需通过Annex 22测试方法。合格标准：火焰蔓延速率≤100mm/min；或60秒内熄灭且燃烧距离≤51mm。",
        clauseType = ClauseType.MATERIAL_SPEC,
        relatedSections = listOf("§6", "§6.1", "Annex 22")
    )

    // 织带要求
    val WEBBING_REQUIREMENT = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.2.2",
        clauseTitle = "织带强度与性能要求",
        clauseContent = "最小宽度≥25mm（儿童接触部位）；断裂强度≥3.6kN（i-Size系统）；耐磨后强度保留率≥75%；耐光后强度保留率≥60%；低温（-30℃）无断裂。",
        clauseType = ClauseType.MATERIAL_SPEC,
        relatedSections = listOf("§6", "§6.2", "Annex 16")
    )

    // 卡扣要求
    val BUCKLE_REQUIREMENT = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.2.3",
        clauseTitle = "卡扣性能要求",
        clauseContent = "释放力：无载时40-80N，碰撞后≤80N；强度：适配≤13kg儿童的≥4kN，＞13kg的≥10kN；释放按钮：封闭型面积≥4.5cm²，非封闭型≥2.5cm²，红色标识；耐久性：5000次循环测试。",
        clauseType = ClauseType.MATERIAL_SPEC,
        relatedSections = listOf("§6", "§6.2", "Annex 15")
    )

    // 调节装置
    val ADJUSTMENT_DEVICE = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.2.4",
        clauseTitle = "快速调节装置要求",
        clauseContent = "类型需为'快速调节器'（Quick adjuster），单手可操作；操作力≤50N；循环测试5000次后无失效；织带滑移量：单个调节器≤25mm，总滑移≤40mm。",
        clauseType = ClauseType.REQUIREMENT,
        relatedSections = listOf("§6", "§6.2", "Annex 17")
    )

    // 自动锁止卷收器
    val AUTO_LOCKING_RETRACTOR = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.2.5.1",
        clauseTitle = "自动锁止卷收器要求",
        clauseContent = "锁定间隙≤30mm；卷收力：腰带≥7N，胸带2-7N；耐久性：10000次循环测试。",
        clauseType = ClauseType.MATERIAL_SPEC,
        relatedSections = listOf("§6", "§6.2", "Annex 13")
    )

    // 紧急锁止卷收器
    val EMERGENCY_LOCKING_RETRACTOR = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.2.5.2",
        clauseTitle = "紧急锁止卷收器要求",
        clauseContent = "车辆减速≥0.45g时锁定，strap加速度≥0.8g时锁定，倾斜＞27°时锁定；耐久性：40000次循环测试。",
        clauseType = ClauseType.MATERIAL_SPEC,
        relatedSections = listOf("§6", "§6.2", "Annex 13")
    )

    // 防旋转装置(支撑腿)
    val SUPPORT_LEG_DEVICE = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.1.2.1, Annex 19",
        clauseTitle = "支撑腿防旋转装置要求",
        clauseContent = "接触面积≥2500mm²，边缘半径≥3.2mm；长度可调，步距≤20mm；几何要求：X'轴585-695mm，Y'轴±100mm；强度测试：承受2.5kN垂直载荷无变形。",
        clauseType = ClauseType.DIMENSIONAL_SPEC,
        relatedSections = listOf("§6", "§6.1", "Annex 19")
    )

    // 防旋转装置(上拉带)
    val TOP_TETHER_DEVICE = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.1.2.1",
        clauseTitle = "上拉带防旋转装置要求",
        clauseContent = "最小长度≥2000mm；张力50±5N；需有无松弛指示器；安装要求：与车辆顶部系带点连接。",
        clauseType = ClauseType.DIMENSIONAL_SPEC,
        relatedSections = listOf("§6", "§6.1")
    )

    // 内部几何尺寸
    val INTERNAL_GEOMETRY = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.1.3, Annex 18",
        clauseTitle = "内部几何尺寸要求",
        clauseContent = "需满足对应身高范围儿童的：坐姿高度、肩宽、髋宽（95百分位最小值）；肩高（5百分位最小值、95百分位最大值）；测量装置质量10kg，接触力50N；坐姿高度公差：≤87cm时-5%，＞87cm时-10%。",
        clauseType = ClauseType.DIMENSIONAL_SPEC,
        relatedSections = listOf("§6", "§6.1", "Annex 18")
    )

    // 外部尺寸包络
    val EXTERNAL_ENVELOPE = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.1.4",
        clauseTitle = "外部尺寸包络要求",
        clauseContent = "i-Size前向：需纳入ISO/F2x尺寸包络（宽600mm，高800mm，深650mm）；i-Size后向：需纳入ISO/R2尺寸包络（宽600mm，高900mm，深700mm）；i-Size增高座：需纳入ISO/B2尺寸包络（宽550mm，高750mm，深500mm）。",
        clauseType = ClauseType.DIMENSIONAL_SPEC,
        relatedSections = listOf("§6", "§6.1", "Annex 18")
    )

    // 后向碰撞测试
    val REAR_IMPACT_TEST = StandardClause(
        standardName = "ECE R129 Rev.4",
        clauseId = "§7.1.3.2",
        clauseTitle = "后向碰撞测试要求",
        clauseContent = "适用后向/侧向ECRS；速度30±2km/h，加速度走廊符合附件7附录2；合格标准：头部HPC≤600（Q0-Q1.5）/≤800（Q3-Q10）；假人加速度≤25g；座椅无结构性变形。",
        clauseType = ClauseType.TESTING_METHOD,
        relatedSections = listOf("§7", "§7.1", "Annex 7 Appendix 2")
    )

    // 侧面碰撞测试
    val SIDE_IMPACT_TEST_R129 = StandardClause(
        standardName = "ECE R129 Rev.4",
        clauseId = "§7.1.3.3, §6.6.4.5.1",
        clauseTitle = "侧面碰撞测试要求",
        clauseContent = "相对速度6.375-7.25m/s；最大侵入250±50mm；合格标准：头部无车门接触；头部3ms累计加速度≤75g（Q0-Q6）；头部偏移量≤150mm；侧面防护结构无破裂。",
        clauseType = ClauseType.TESTING_METHOD,
        relatedSections = listOf("§7", "§7.1", "Annex 7 Appendix 3")
    )

    // 测试安装要求
    val TEST_INSTALLATION = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "Annex 7",
        clauseTitle = "测试安装要求",
        clauseContent = "整体式ISOFIX型：施加135±15N预紧力，上拉带张力50±5N；安全带固定型：预紧力50±5N，卷收器残留织带≥150mm；Dummy定位：用垫片隔离座椅靠背；安装后10分钟内完成测试。",
        clauseType = ClauseType.TESTING_METHOD,
        relatedSections = listOf("§7", "§7.1", "Annex 6", "Annex 7")
    )

    // 生产一致性控制
    val PRODUCTION_CONFORMITY = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§9, Annex 12",
        clauseTitle = "生产一致性控制要求",
        clauseContent = "批量控制：首批量50-5000件，抽取5件动态测试；抽检频率：无ISO体系每半年1次，有ISO体系每年1次；合格标准：头部位移≤1.05倍限值，均值+标准差≤限值；测试项目：卷收器、织带、微滑移、能量吸收、动态碰撞。",
        clauseType = ClauseType.QUALITY_CONTROL,
        relatedSections = listOf("§9", "Annex 12")
    )

    // 用户说明书要求
    val USER_MANUAL = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§10",
        clauseTitle = "用户说明书要求",
        clauseContent = "语言：销售国官方语言，含多语言可选；核心内容：安装步骤（图文）、适配车型、身高/体重范围、调节方法、清洁说明、碰撞后更换提示；警告信息：后向约束禁用前排气囊位、不可改装、超期禁用（通常5-7年）；需保留至ECRS使用寿命结束。",
        clauseType = ClauseType.USER_INFORMATION,
        relatedSections = listOf("§10", "§10.1", "§10.2")
    )

    // 标识要求
    val MARKING_REQUIREMENT = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§11",
        clauseTitle = "标识要求",
        clauseContent = "基础信息标签：制造商、生产日期、朝向、身高范围、最大体重；后向约束警告标签：最小60×120mm，儿童头部区域；i-Size标识：最小25×25mm，安装时可见；织带路径标识：绿色标识，区分腰带/肩带路径。",
        clauseType = ClauseType.USER_INFORMATION,
        relatedSections = listOf("§11", "§11.1", "§11.2", "Annex 2")
    )

    // 认证申请材料
    val APPLICATION_DOCUMENTS = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§13, Annex 20",
        clauseTitle = "认证申请材料要求",
        clauseContent = "核心材料：申请表、技术描述（材料/结构/载荷限制）、毒性/燃烧声明、安装说明书、产品图纸、样品（含10米织带）；特定车型型额外提供：车辆适配列表、车辆结构图纸、安装示意图；样品数量：按技术服务机构要求。",
        clauseType = ClauseType.CERTIFICATION,
        relatedSections = listOf("§13", "Annex 20")
    )

    // 环境与耐久性测试
    val ENVIRONMENTAL_TEST = StandardClause(
        standardName = "ECE R129:2013",
        clauseId = "§6.3",
        clauseTitle = "环境与耐久性测试要求",
        clauseContent = "温度测试：80℃×24h + 3个循环（100℃×6h→0℃×6h→23℃）；腐蚀测试：50小时盐雾测试，无明显腐蚀；粉尘测试：5小时石英粉尘测试；能量吸收：头部冲击区域峰值加速度＜60g（附件13测试方法）。",
        clauseType = ClauseType.TESTING_METHOD,
        relatedSections = listOf("§6", "§6.3", "Annex 3", "Annex 4")
    )

    fun getAllClauses(): List<StandardClause> {
        return listOf(
            HEADREST_ADJUSTMENT,
            SEAT_ANGLE_FORWARD,
            RECLINE_ANGLE_REAR,
            ISOFIX_INTERFACE,
            SUPPORT_LEG,
            FRONT_IMPACT_TEST,
            SIDE_IMPACT_TEST,
            ISOFIX_STRENGTH_TEST,
            SUPPORT_LEG_STABILITY_TEST,
            PRODUCT_CLASSIFICATION,
            // 新增R129r4e详细条款
            MATERIAL_TOXICITY,
            FLAMMABILITY_NON_BUILTIN,
            FLAMMABILITY_BUILTIN,
            WEBBING_REQUIREMENT,
            BUCKLE_REQUIREMENT,
            ADJUSTMENT_DEVICE,
            AUTO_LOCKING_RETRACTOR,
            EMERGENCY_LOCKING_RETRACTOR,
            SUPPORT_LEG_DEVICE,
            TOP_TETHER_DEVICE,
            INTERNAL_GEOMETRY,
            EXTERNAL_ENVELOPE,
            REAR_IMPACT_TEST,
            SIDE_IMPACT_TEST_R129,
            TEST_INSTALLATION,
            PRODUCTION_CONFORMITY,
            USER_MANUAL,
            MARKING_REQUIREMENT,
            APPLICATION_DOCUMENTS,
            ENVIRONMENTAL_TEST
        )
    }
}

/**
 * 儿童安全座椅产品配置项
 */
object ChildSafetySeatConfigurations {
    val ISOFIX_INTERFACE = ProductConfiguration(
        configId = "isofix_interface",
        configName = "ISOFIX接口",
        applicableProductTypes = listOf(ProductType.SAFETY_SEAT),
        isRequired = true,
        relatedClauses = listOf(ECE_R129_Clauses.ISOFIX_INTERFACE),
        description = "国际标准化固定接口，提供更稳固的安装方式"
    )

    val SUPPORT_LEG = ProductConfiguration(
        configId = "support_leg",
        configName = "支撑腿",
        applicableProductTypes = listOf(ProductType.SAFETY_SEAT),
        isRequired = true,
        relatedClauses = listOf(ECE_R129_Clauses.SUPPORT_LEG),
        description = "防止座椅在碰撞中向前倾倒，提高稳定性"
    )

    val TOP_TETHER = ProductConfiguration(
        configId = "top_tether",
        configName = "顶部系带",
        applicableProductTypes = listOf(ProductType.SAFETY_SEAT),
        isRequired = false,
        relatedClauses = emptyList(),
        description = "提供额外的固定点，适用于正向安装"
    )

    val SIDE_IMPACT_PROTECTION = ProductConfiguration(
        configId = "side_impact_protection",
        configName = "侧面碰撞保护",
        applicableProductTypes = listOf(ProductType.SAFETY_SEAT),
        isRequired = true,
        relatedClauses = listOf(ECE_R129_Clauses.SIDE_IMPACT_TEST),
        description = "提供侧面碰撞时的额外保护"
    )

    val ADJUSTABLE_HEADREST = ProductConfiguration(
        configId = "adjustable_headrest",
        configName = "可调头托",
        applicableProductTypes = listOf(ProductType.SAFETY_SEAT),
        isRequired = true,
        relatedClauses = listOf(ECE_R129_Clauses.HEADREST_ADJUSTMENT),
        description = "根据儿童身高调节头托高度，提供最佳保护"
    )

    fun getConfigurationsForProduct(productType: ProductType): List<ProductConfiguration> {
        return when (productType) {
            ProductType.SAFETY_SEAT -> listOf(
                ISOFIX_INTERFACE,
                SUPPORT_LEG,
                TOP_TETHER,
                SIDE_IMPACT_PROTECTION,
                ADJUSTABLE_HEADREST
            )
            else -> emptyList()
        }
    }
}

/**
 * 标准匹配服务
 */
class StandardMatchingService {

    /**
     * 根据身高范围匹配标准
     */
    fun matchStandardByHeight(
        minHeightCm: Int,
        maxHeightCm: Int,
        productType: ProductType
    ): StandardMatchResult? {
        return when (productType) {
            ProductType.SAFETY_SEAT -> matchChildSafetySeatStandard(minHeightCm, maxHeightCm)
            else -> null
        }
    }

    /**
     * 匹配儿童安全座椅标准
     */
    private fun matchChildSafetySeatStandard(
        minHeightCm: Int,
        maxHeightCm: Int
    ): StandardMatchResult {
        val standardName = "ECE R129 Rev.4"
        var productClassification = ""
        var ageRange = ""
        var configurationRequirements = mutableListOf<String>()
        val relevantClauses = mutableListOf<StandardClause>()

        // 根据身高范围确定分组（基于R129r4e §6.1.2.7要求）
        when {
            // 新生儿到15个月（40-83cm）- 必须后向
            maxHeightCm <= 83 -> {
                productClassification = "后向安装 / i-Size"
                ageRange = "新生儿 - 15个月"
                configurationRequirements.add("后向安装（ECE R129 §6.1.2.7）")
                configurationRequirements.add("ISOFIX接口（ECE R129 §6.1.2.1）")
                configurationRequirements.add("支撑腿或顶部系带（ECE R129 §6.1.2.1）")
                relevantClauses.addAll(listOf(
                    ECE_R129_Clauses.PRODUCT_CLASSIFICATION,
                    ECE_R129_Clauses.RECLINE_ANGLE_REAR,
                    ECE_R129_Clauses.ISOFIX_INTERFACE,
                    ECE_R129_Clauses.SUPPORT_LEG
                ))
            }
            // 15个月以上，76-105cm - 可以前向
            minHeightCm >= 76 && maxHeightCm <= 105 -> {
                productClassification = "前向安装 / i-Size"
                ageRange = "15个月 - 4岁"
                configurationRequirements.add("ISOFIX接口（ECE R129 §6.1.2.1）")
                configurationRequirements.add("支撑腿或顶部系带（ECE R129 §6.1.2.1）")
                relevantClauses.addAll(listOf(
                    ECE_R129_Clauses.PRODUCT_CLASSIFICATION,
                    ECE_R129_Clauses.ISOFIX_INTERFACE,
                    ECE_R129_Clauses.SUPPORT_LEG
                ))
            }
            // 增高垫（100cm以上）- 非整体式
            minHeightCm >= 100 && maxHeightCm >= 105 -> {
                productClassification = "非整体式 / i-Size booster seat"
                ageRange = "4岁 - 12岁"
                configurationRequirements.add("侧面碰撞保护至135cm（ECE R129 §6.1.3.3）")
                configurationRequirements.add("ISOFIX接口（可选）")
                relevantClauses.addAll(listOf(
                    ECE_R129_Clauses.PRODUCT_CLASSIFICATION,
                    ECE_R129_Clauses.SIDE_IMPACT_TEST
                ))
            }
            else -> {
                // 可转换座椅（覆盖多个分组）
                productClassification = "可转换座椅 / i-Size"
                ageRange = "新生儿 - 12岁"
                configurationRequirements.add("后向前向可转换（ECE R129 §6.1.2.7）")
                configurationRequirements.add("ISOFIX接口（ECE R129 §6.1.2.1）")
                configurationRequirements.add("支撑腿或顶部系带（ECE R129 §6.1.2.1）")
                relevantClauses.addAll(listOf(
                    ECE_R129_Clauses.PRODUCT_CLASSIFICATION,
                    ECE_R129_Clauses.RECLINE_ANGLE_REAR,
                    ECE_R129_Clauses.ISOFIX_INTERFACE,
                    ECE_R129_Clauses.SUPPORT_LEG
                ))
            }
        }

        // 添加通用相关条款
        relevantClauses.add(ECE_R129_Clauses.HEADREST_ADJUSTMENT)
        relevantClauses.add(ECE_R129_Clauses.SEAT_ANGLE_FORWARD)

        // 计算对应的重量范围（按标准公式）
        val minWeightKg = (minHeightCm * 0.25).toInt()
        val maxWeightKg = (maxHeightCm * 0.30).toInt()
        val weightRange = "${minWeightKg}-${maxWeightKg} kg"

        return StandardMatchResult(
            standardName = standardName,
            productClassification = productClassification,
            ageRange = ageRange,
            heightRange = "${minHeightCm}-${maxHeightCm} cm",
            weightRange = weightRange,
            relevantClauses = relevantClauses,
            configurationRequirements = configurationRequirements
        )
    }

    /**
     * 根据选中的配置获取设计参数
     */
    fun getDesignParameters(
        productType: ProductType,
        selectedConfigs: List<ProductConfiguration>,
        heightRange: HeightRangeInput
    ): List<DesignParameter> {
        val parameters = mutableListOf<DesignParameter>()

        when (productType) {
            ProductType.SAFETY_SEAT -> {
                // 头托调节范围（根据身高范围计算）
                val adjustmentRange = 154
                val steps = 7
                parameters.add(DesignParameter(
                    parameterName = "头托调节范围",
                    specificParameter = "${adjustmentRange}mm（分度：每档22mm，共${steps}档）",
                    relatedClause = ECE_R129_Clauses.HEADREST_ADJUSTMENT
                ))

                // 座椅调节角度
                parameters.add(DesignParameter(
                    parameterName = "座椅调节角度（正向）",
                    specificParameter = "10°（固定调节档位）",
                    relatedClause = ECE_R129_Clauses.SEAT_ANGLE_FORWARD
                ))

                // 倾斜角度（后向）
                parameters.add(DesignParameter(
                    parameterName = "倾斜角度（后向兼容）",
                    specificParameter = "10°（后向安装时启用）",
                    relatedClause = ECE_R129_Clauses.RECLINE_ANGLE_REAR
                ))

                // ISOFIX接口尺寸
                if (selectedConfigs.any { it.configId == "isofix_interface" }) {
                    parameters.add(DesignParameter(
                        parameterName = "ISOFIX接口尺寸",
                        specificParameter = "间距280mm±5mm",
                        tolerance = "±5mm",
                        relatedClause = ECE_R129_Clauses.ISOFIX_INTERFACE
                    ))
                }

                // 支撑腿长度
                if (selectedConfigs.any { it.configId == "support_leg" }) {
                    parameters.add(DesignParameter(
                        parameterName = "支撑腿有效长度",
                        specificParameter = "可调范围：120-200mm",
                        relatedClause = ECE_R129_Clauses.SUPPORT_LEG
                    ))
                }
            }
            else -> {}
        }

        return parameters
    }

    /**
     * 获取合规测试矩阵
     */
    fun getComplianceTests(
        productType: ProductType,
        selectedConfigs: List<ProductConfiguration>
    ): List<ComplianceTestItem> {
        val tests = mutableListOf<ComplianceTestItem>()

        when (productType) {
            ProductType.SAFETY_SEAT -> {
                // 正向撞击测试
                tests.add(ComplianceTestItem(
                    testName = "正向撞击测试（Q0, Q1, Q1.5）",
                    testDummy = "Q0, Q1, Q1.5",
                    testConditions = "碰撞速度50km/h，ΔV=52km/h",
                    acceptanceCriteria = "HPC≤600；头部加速度3ms≤75g；胸部加速度3ms≤55g；腹部压力≤1.2bar(Q1.5)",
                    relatedClause = ECE_R129_Clauses.FRONT_IMPACT_TEST,
                    testStandard = "ECE R129 §7.1.3, §6.6.4.3.1"
                ))

                tests.add(ComplianceTestItem(
                    testName = "正向撞击测试（Q3）",
                    testDummy = "Q3",
                    testConditions = "碰撞速度50km/h，ΔV=52km/h",
                    acceptanceCriteria = "HPC≤800；头部加速度3ms≤80g；胸部加速度3ms≤55g；腹部压力≤1.0bar",
                    relatedClause = ECE_R129_Clauses.FRONT_IMPACT_TEST,
                    testStandard = "ECE R129 §7.1.3, §6.6.4.3.1"
                ))

                tests.add(ComplianceTestItem(
                    testName = "正向撞击测试（Q6, Q10）",
                    testDummy = "Q6, Q10",
                    testConditions = "碰撞速度50km/h，ΔV=52km/h",
                    acceptanceCriteria = "HPC≤800；头部加速度3ms≤80g；胸部加速度3ms≤55g；腹部压力≤1.0bar(Q6), 1.2bar(Q10)",
                    relatedClause = ECE_R129_Clauses.FRONT_IMPACT_TEST,
                    testStandard = "ECE R129 §7.1.3, §6.6.4.3.1"
                ))

                // 侧面撞击测试
                if (selectedConfigs.any { it.configId == "side_impact_protection" }) {
                    tests.add(ComplianceTestItem(
                        testName = "侧面撞击测试",
                        testDummy = "Q1.5",
                        testConditions = "侧撞台车速度32km/h",
                        acceptanceCriteria = "侧面防护结构无破裂；安全带无松脱",
                        relatedClause = ECE_R129_Clauses.SIDE_IMPACT_TEST,
                        testStandard = "ECE R129 §6.3"
                    ))
                }

                // ISOFIX接口强度测试
                if (selectedConfigs.any { it.configId == "isofix_interface" }) {
                    tests.add(ComplianceTestItem(
                        testName = "ISOFIX接口强度",
                        testDummy = "-",
                        testConditions = "施加5000N拉力，持续10s",
                        acceptanceCriteria = "接口无变形、无断裂",
                        relatedClause = ECE_R129_Clauses.ISOFIX_STRENGTH_TEST,
                        testStandard = "ECE R129 §6.4.1"
                    ))
                }

                // 支撑腿稳定性测试
                if (selectedConfigs.any { it.configId == "support_leg" }) {
                    tests.add(ComplianceTestItem(
                        testName = "支撑腿稳定性",
                        testDummy = "-",
                        testConditions = "承载15kg负重，静置30min",
                        acceptanceCriteria = "支撑腿无压缩失效；座椅无倾斜",
                        relatedClause = ECE_R129_Clauses.SUPPORT_LEG_STABILITY_TEST,
                        testStandard = "ECE R129 §6.4.3"
                    ))
                }
            }
            else -> {}
        }

        return tests
    }
}

/**
 * 标准核心参数预览数据
 */
data class StandardParameterPreview(
    val paramName: String,
    val paramValue: String,
    val standardSource: String,
    val description: String
)

/**
 * 标准关键词联想
 */
data class StandardKeywordSuggestion(
    val keyword: String,
    val displayText: String,
    val clause: StandardClause
)

/**
 * 标准参数预览服务
 */
class StandardParameterPreviewService {

    /**
     * 获取标准核心参数预览
     */
    fun getParameterPreview(
        productType: ProductType,
        minHeight: Int,
        maxHeight: Int
    ): List<StandardParameterPreview> {
        return when (productType) {
            ProductType.SAFETY_SEAT -> getChildSafetySeatParameters(minHeight, maxHeight)
            ProductType.STROLLER -> getStrollerParameters(minHeight, maxHeight)
            ProductType.CHILD_HOUSEHOLD_GOODS -> getHouseholdGoodsParameters(minHeight, maxHeight)
            ProductType.HIGH_CHAIR -> getHighChairParameters(minHeight, maxHeight)
        }
    }

    /**
     * 儿童安全座椅核心参数
     */
    private fun getChildSafetySeatParameters(minHeight: Int, maxHeight: Int): List<StandardParameterPreview> {
        val params = mutableListOf<StandardParameterPreview>()

        // 外尺寸上限（Envelope要求）
        params.add(StandardParameterPreview(
            paramName = "外尺寸上限",
            paramValue = "宽≤44cm、长≤75cm",
            standardSource = "ECE R129 Envelope",
            description = "确保座椅在车辆安装空间内"
        ))

        // 头托调节范围
        val headrestRange = when {
            maxHeight <= 95 -> "10-25cm"
            maxHeight <= 105 -> "20-35cm"
            maxHeight <= 125 -> "30-50cm"
            else -> "40-60cm"
        }
        params.add(StandardParameterPreview(
            paramName = "头托调节范围",
            paramValue = headrestRange,
            standardSource = "ECE R129 §5.4.2",
            description = "适配身高${minHeight}-${maxHeight}cm"
        ))

        // ISOFIX接口
        params.add(StandardParameterPreview(
            paramName = "ISOFIX接口间距",
            paramValue = "280mm±5mm",
            standardSource = "ECE R129 §5.5.1",
            description = "国际标准化固定接口尺寸"
        ))

        // 支撑腿长度
        params.add(StandardParameterPreview(
            paramName = "支撑腿可调范围",
            paramValue = "120-200mm",
            standardSource = "ECE R129 §5.5.3",
            description = "防止座椅向前倾倒"
        ))

        // 侧撞防护
        params.add(StandardParameterPreview(
            paramName = "侧撞防护结构",
            paramValue = "需含EPS吸能结构",
            standardSource = "GB 27887-2024 §6.4",
            description = "侧面碰撞保护要求"
        ))

        // 座椅角度
        params.add(StandardParameterPreview(
            paramName = "座椅调节角度",
            paramValue = "≥10°固定档位",
            standardSource = "ECE R129 §5.2.3",
            description = "坐姿稳定性要求"
        ))

        return params
    }

    /**
     * 婴儿推车核心参数
     */
    private fun getStrollerParameters(minHeight: Int, maxHeight: Int): List<StandardParameterPreview> {
        val params = mutableListOf<StandardParameterPreview>()

        params.add(StandardParameterPreview(
            paramName = "折叠后尺寸",
            paramValue = "长×宽×高≤ 100×60×30cm",
            standardSource = "GB 14748-2020",
            description = "便于收纳和携带"
        ))

        params.add(StandardParameterPreview(
            paramName = "刹车距离",
            paramValue = "≤ 1.0m（10°斜坡）",
            standardSource = "EN 1888",
            description = "刹车性能要求"
        ))

        params.add(StandardParameterPreview(
            paramName = "座椅角度",
            paramValue = "可调范围135°-175°",
            standardSource = "GB 14748-2020 §4.3",
            description = "保护婴儿脊柱"
        ))

        return params
    }

    /**
     * 儿童家庭用品核心参数
     */
    private fun getHouseholdGoodsParameters(minHeight: Int, maxHeight: Int): List<StandardParameterPreview> {
        return listOf(
            StandardParameterPreview(
                paramName = "材料安全",
                paramValue = "无有害物质释放",
                standardSource = "ISO 8124-3",
                description = "重金属和塑化剂限制"
            ),
            StandardParameterPreview(
                paramName = "结构强度",
                paramValue = "承受1.5倍额定载荷",
                standardSource = "GB 28007-2011",
                description = "使用安全性"
            )
        )
    }

    /**
     * 儿童高脚椅核心参数
     */
    private fun getHighChairParameters(minHeight: Int, maxHeight: Int): List<StandardParameterPreview> {
        return listOf(
            StandardParameterPreview(
                paramName = "座面高度",
                paramValue = "可调范围60-90cm",
                standardSource = "ISO 8124-3",
                description = "适配不同餐桌高度"
            ),
            StandardParameterPreview(
                paramName = "安全带",
                paramValue = "五点式安全带",
                standardSource = "GB 28007-2011 §5.2",
                description = "防止儿童跌落"
            ),
            StandardParameterPreview(
                paramName = "稳定性",
                paramValue = "15°倾斜不翻倒",
                standardSource = "ISO 8124-3 §4.4",
                description = "抗倾倒要求"
            )
        )
    }
}

/**
 * 标准关键词联想服务
 */
class StandardKeywordSuggestionService {

    fun getSuggestions(input: String): List<StandardKeywordSuggestion> {
        if (input.length < 2) return emptyList()

        val suggestions = mutableListOf<StandardKeywordSuggestion>()

        // Envelope相关
        if (input.contains("env", ignoreCase = true)) {
            suggestions.add(StandardKeywordSuggestion(
                keyword = "Envelope",
                displayText = "ECE R129 §5.2.1 Envelope尺寸要求：宽≤44cm",
                clause = ECE_R129_Clauses.SEAT_ANGLE_FORWARD
            ))
        }

        // 侧撞防护相关
        if (input.contains("侧", ignoreCase = true) || input.contains("side", ignoreCase = true)) {
            suggestions.add(StandardKeywordSuggestion(
                keyword = "侧撞防护",
                displayText = "GB 27887-2024 §6.4 侧撞防护：需含EPS吸能结构",
                clause = ECE_R129_Clauses.SIDE_IMPACT_TEST
            ))
        }

        // 头托相关
        if (input.contains("头托", ignoreCase = true) || input.contains("head", ignoreCase = true)) {
            suggestions.add(StandardKeywordSuggestion(
                keyword = "头托调节",
                displayText = "ECE R129 §5.4.2 头托调节范围≥154mm",
                clause = ECE_R129_Clauses.HEADREST_ADJUSTMENT
            ))
        }

        // ISOFIX相关
        if (input.contains("isofix", ignoreCase = true)) {
            suggestions.add(StandardKeywordSuggestion(
                keyword = "ISOFIX",
                displayText = "ECE R129 §5.5.1 ISOFIX接口间距280mm±5mm",
                clause = ECE_R129_Clauses.ISOFIX_INTERFACE
            ))
        }

        // 支撑腿相关
        if (input.contains("支撑", ignoreCase = true) || input.contains("leg", ignoreCase = true)) {
            suggestions.add(StandardKeywordSuggestion(
                keyword = "支撑腿",
                displayText = "ECE R129 §5.5.3 支撑腿可调范围120-200mm",
                clause = ECE_R129_Clauses.SUPPORT_LEG
            ))
        }

        // 碰撞测试相关
        if (input.contains("碰撞", ignoreCase = true) || input.contains("test", ignoreCase = true)) {
            suggestions.add(StandardKeywordSuggestion(
                keyword = "正向碰撞测试",
                displayText = "ECE R129 §6.2 正向碰撞：头部位移＜25cm；HIC＜700",
                clause = ECE_R129_Clauses.FRONT_IMPACT_TEST
            ))
        }

        return suggestions
    }
}

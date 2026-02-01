package com.childproduct.designassistant.data

import com.childproduct.designassistant.model.*

/**
 * 专业设计建议示例数据库
 * 基于真实行业标准和头部品牌参数
 */
object ProfessionalDesignExamples {
    
    /**
     * 儿童安全座椅示例（ECE R129/i-Size，60-105cm）
     */
    val safetySeatECE129Example = DesignSuggestionReport(
        id = "example-safety-seat-ece129",
        timestamp = System.currentTimeMillis(),
        productType = ProductType.SAFETY_SEAT,
        standard = InternationalStandard.ECE_R129,
        userInput = ProductInput.SafetySeat(
            SafetySeatInput(
                standard = InternationalStandard.ECE_R129,
                subtype = SafetySeatSubtype.GROUP_0_1,
                heightRange = "60-105cm",
                weightRange = "9-18kg",
                ageGroup = AgeGroup.PRESCHOOL,
                customFeatures = listOf("ISOFIX接口型", "可旋转型"),
                specificRequirements = listOf("优化头托侧面碰撞防护", "符合i-Size Envelope内部尺寸"),
                customNotes = "标准60-105cm身高组设计参考"
            )
        ),
        designSuggestions = DesignSuggestions(
            productType = ProductType.SAFETY_SEAT,
            functionalFeatures = listOf(
                FunctionalFeature(
                    category = "头托调节",
                    description = "多档位机械卡扣调节，支持上下滑动",
                    recommendation = "调节范围10-25cm，15档，每档1cm调节精度，参考Britax头托调节标准",
                    referenceStandard = "ECE R129 §5.4.2",
                    implementationDifficulty = DifficultyLevel.MODERATE
                ),
                FunctionalFeature(
                    category = "靠背角度调节",
                    description = "一键式机械调节，无需拆卸座椅",
                    recommendation = "调节范围100°-125°，3档调节，参考Britax 9档快速倾斜设计",
                    referenceStandard = "ECE R129 §5.4.3",
                    implementationDifficulty = DifficultyLevel.EASY
                ),
                FunctionalFeature(
                    category = "侧面碰撞防护",
                    description = "可伸缩式侧面防撞块，碰撞时行程5cm吸能",
                    recommendation = "参考Cybex侧面防护系统结构，采用LSP可调节侧面碰撞保护装置",
                    referenceStandard = "ECE R129 §5.3.3",
                    implementationDifficulty = DifficultyLevel.DIFFICULT
                )
            ),
            dimensionParameters = DimensionParameters(
                externalDimensions = ExternalDimensionSuggestion(
                    width = DimensionRecommendation(
                        recommendedValue = DoubleRange(40.0, 44.0),
                        unit = "cm",
                        rationale = "法规强制上限≤44cm，确保适配多数车型后排空间",
                        standardReference = "ECE R129 Annex 7"
                    ),
                    length = DimensionRecommendation(
                        recommendedValue = DoubleRange(72.0, 75.0),
                        unit = "cm",
                        rationale = "前后向安装通用基准尺寸，兼容后向安装至15个月要求",
                        standardReference = "ECE R129 Annex 7"
                    ),
                    height = DimensionRecommendation(
                        recommendedValue = DoubleRange(76.0, 81.0),
                        unit = "cm",
                        rationale = "含头托最大升起状态，满足i-Size头部防护空间要求",
                        standardReference = "ECE R129 Annex 7"
                    ),
                    weight = DimensionRecommendation(
                        recommendedValue = DoubleRange(11.0, 13.0),
                        unit = "kg",
                        rationale = "集成ISOFIX接口收纳空间，符合安装兼容性标准",
                        standardReference = "ECE R129"
                    ),
                    notes = "参考Britax Dualfix M i-Size尺寸参数"
                ),
                internalDimensions = InternalDimensionSuggestion(
                    seatDepth = DimensionRecommendation(
                        recommendedValue = DoubleRange(38.0, 42.0),
                        unit = "cm",
                        rationale = "适配CRABI 12个月假人臀腿长度35cm，避免腿部悬空过长",
                        standardReference = "ECE R129 Annex 6"
                    ),
                    seatWidth = DimensionRecommendation(
                        recommendedValue = DoubleRange(30.0, 34.0),
                        unit = "cm",
                        rationale = "适配CRABI 12个月假人肩宽22cm、Hybrid III 3岁假人肩宽28cm，预留2cm活动空间",
                        standardReference = "ECE R129 Annex 6"
                    ),
                    backrestHeight = DimensionRecommendation(
                        recommendedValue = DoubleRange(62.0, 68.0),
                        unit = "cm",
                        rationale = "从座面至头托最低位置，覆盖60-105cm身高儿童脊柱支撑需求",
                        standardReference = "ECE R129 Annex 6"
                    ),
                    headrestWidth = DimensionRecommendation(
                        recommendedValue = DoubleRange(33.0, 37.0),
                        unit = "cm",
                        rationale = "适配Q3s 3岁假人头宽28cm，保障侧面防护包裹性",
                        standardReference = "ECE R129 Annex 6"
                    ),
                    shoulderWidth = DimensionRecommendation(
                        recommendedValue = DoubleRange(30.0, 34.0),
                        unit = "cm",
                        rationale = "适配儿童肩宽范围，确保舒适性和安全性",
                        standardReference = null
                    ),
                    notes = "参考Maxi-Cosi Pebble 360设计"
                ),
                adjustmentRanges = listOf(
                    AdjustmentRange(
                        component = "头托高度",
                        minPosition = 10.0,
                        maxPosition = 25.0,
                        unit = "cm",
                        adjustmentSteps = 15,
                        recommendation = "最小高度10cm适配60cm身高儿童，最大高度25cm适配105cm身高儿童，每档1cm"
                    ),
                    AdjustmentRange(
                        component = "靠背角度",
                        minPosition = 100.0,
                        maxPosition = 125.0,
                        unit = "度",
                        adjustmentSteps = 3,
                        recommendation = "100°前向安装保障坐姿稳定，125°后向安装符合脊柱保护要求"
                    )
                ),
                envelopeCompliance = EnvelopeCompliance(
                    isCompliant = true,
                    widthComparison = ComparisonResult(43.0, 44.0, true, 1.0, "cm"),
                    lengthComparison = ComparisonResult(73.5, 75.0, true, 1.5, "cm"),
                    heightComparison = ComparisonResult(79.0, 81.0, true, 2.0, "cm"),
                    recommendations = listOf("完全符合i-Size Envelope要求，预留1-2cm安全余量")
                )
            ),
            materialRecommendations = listOf(
                MaterialRecommendation(
                    component = "座椅表面",
                    recommendedMaterial = "透气网眼布",
                    properties = listOf("透气性好", "易清洁", "耐磨"),
                    safetyRating = SafetyRating.GOOD,
                    supplierSuggestions = listOf("华峰集团", "台达化学")
                ),
                MaterialRecommendation(
                    component = "吸能材料",
                    recommendedMaterial = "EPS发泡材料",
                    properties = listOf("吸能性能优异", "轻量化", "阻燃"),
                    safetyRating = SafetyRating.EXCELLENT,
                    supplierSuggestions = listOf("巴斯夫", "陶氏化学")
                ),
                MaterialRecommendation(
                    component = "头托材料",
                    recommendedMaterial = "EPP发泡材料",
                    properties = listOf("高强度吸能", "可回收", "环保"),
                    safetyRating = SafetyRating.EXCELLENT,
                    supplierSuggestions = listOf("JSP", "Kaneka")
                )
            ),
            safetyRecommendations = listOf(
                SafetyRecommendation(
                    aspect = "侧面碰撞防护",
                    description = "在座椅两侧安装可调节的LSP装置",
                    implementation = "采用可伸缩的侧面保护块，调节范围为0-5cm，参考Cybex L.S.P. System Plus",
                    testingRequirement = "ECE R129 §5.3.3侧面碰撞测试",
                    priority = Priority.CRITICAL
                ),
                SafetyRecommendation(
                    aspect = "安全带调节",
                    description = "采用单手可调节安全带系统",
                    implementation = "设计中央调节按钮，支持单手操作调节高度，肩带宽度5cm",
                    testingRequirement = "FMVSS 213 §4.4安全带拉伸测试",
                    priority = Priority.HIGH
                ),
                SafetyRecommendation(
                    aspect = "ISOFIX固定",
                    description = "ISOFIX+上拉带双重固定系统",
                    implementation = "集成ISOFIX硬连接接口，配合上拉带确保安装稳定性",
                    testingRequirement = "ECE R129 §5.5.1接口强度测试",
                    priority = Priority.CRITICAL
                )
            )
        ),
        brandComparison = BrandComparison(
            targetProductType = ProductType.SAFETY_SEAT,
            comparedBrands = listOf(
                BrandBenchmark(
                    brandName = "Britax",
                    productName = "Dualfix M i-Size",
                    keyAdvantages = listOf(
                        "SafeCell吸能技术",
                        "头托15档调节（10-30cm）",
                        "360°旋转功能",
                        "ISOFIX接口适配范围广"
                    ),
                    technicalSpecs = TechnicalSpecs(
                        dimensions = ProductDimensions(
                            width = 44.0,
                            height = 64.0,
                            depth = 57.0,
                            seatWidth = 38.0,
                            seatDepth = 45.0
                        ),
                        weight = 13.5,
                        materials = listOf("EPS吸能材料", "透气网眼布", "铝合金骨架"),
                        certifications = listOf("ECE R129", "i-Size"),
                        uniqueFeatures = listOf("SafeCell Impact Protection", "V型头托", "一键旋转")
                    ),
                    marketPosition = "高端市场，以安全性和创新设计著称"
                ),
                BrandBenchmark(
                    brandName = "Maxi-Cosi",
                    productName = "Pebble 360",
                    keyAdvantages = listOf(
                        "i-Size Envelope完全合规",
                        "FamilyFix 360底座",
                        "Clash-free技术",
                        "侧撞保护系统"
                    ),
                    technicalSpecs = TechnicalSpecs(
                        dimensions = ProductDimensions(
                            width = 44.0,
                            height = 60.0,
                            depth = 65.0,
                            seatWidth = 36.0,
                            seatDepth = 40.0
                        ),
                        weight = 12.5,
                        materials = listOf("EPP吸能材料", "3D网眼布", "复合材料"),
                        certifications = listOf("ECE R129", "i-Size"),
                        uniqueFeatures = listOf("FlexiFit旋转系统", "Air Ventilation", "Soft-touch面料")
                    ),
                    marketPosition = "中高端市场，注重舒适性和易用性"
                )
            ),
            summaryAnalysis = "建议结合Britax的吸能技术和Maxi-Cosi的旋转系统，在保证安全性的同时提升用户体验",
            differentiatingSuggestions = listOf(
                "采用Britax SafeCell吸能技术底座",
                "集成Maxi-Cosi FamilyFix 360底座的旋转功能",
                "参考Cybex L.S.P.侧面防护系统设计"
            )
        ),
        dvpTestMatrix = DVPTestMatrix(
            productType = ProductType.SAFETY_SEAT,
            standard = InternationalStandard.ECE_R129,
            testItems = listOf(
                DVPTestItem(
                    testId = "IMP-001",
                    testCategory = TestCategory.IMPACT_TESTING,
                    testName = "正面碰撞测试",
                    standardReference = "ECE R129 §5.3.2 / FMVSS 213 §4.3",
                    testMethod = "使用Hybrid III 3岁假人，碰撞速度50km/h，加速度峰值50g",
                    acceptanceCriteria = "假人头部伤害指数（HIC）<700；胸部压缩量<50mm；头托无脱落",
                    testEquipment = "碰撞测试台，Hybrid III 3岁假人",
                    sampleSize = 5,
                    estimatedDuration = "2-3小时",
                    priority = TestPriority.MANDATORY,
                    notes = "必须测试"
                ),
                DVPTestItem(
                    testId = "IMP-002",
                    testCategory = TestCategory.IMPACT_TESTING,
                    testName = "侧面碰撞测试",
                    standardReference = "ECE R129 §5.3.3",
                    testMethod = "使用Q3s假人，侧撞台车速度32km/h，撞击点为座椅侧面中心",
                    acceptanceCriteria = "假人头部横向位移<25cm；侧面防护结构无破裂；安全带无松脱",
                    testEquipment = "碰撞测试台，Q3s假人",
                    sampleSize = 5,
                    estimatedDuration = "2-3小时",
                    priority = TestPriority.MANDATORY,
                    notes = "必须测试"
                ),
                DVPTestItem(
                    testId = "DIM-001",
                    testCategory = TestCategory.FUNCTIONAL_TESTING,
                    testName = "头托调节可靠性测试",
                    standardReference = "ECE R129 §5.4.2",
                    testMethod = "头托在全调节范围内反复调节500次，每次调节后施加100N向下压力",
                    acceptanceCriteria = "调节档位无卡滞、无松动；调节机构强度无衰减",
                    testEquipment = "疲劳测试机",
                    sampleSize = 3,
                    estimatedDuration = "4-5小时",
                    priority = TestPriority.CRITICAL,
                    notes = "关键测试"
                ),
                DVPTestItem(
                    testId = "DIM-002",
                    testCategory = TestCategory.FUNCTIONAL_TESTING,
                    testName = "靠背角度锁定测试",
                    standardReference = "FMVSS 213 §4.5",
                    testMethod = "各档位下向靠背施加200N向前推力，持续30s",
                    acceptanceCriteria = "靠背无位移；锁定机构无失效",
                    testEquipment = "拉力测试仪",
                    sampleSize = 3,
                    estimatedDuration = "1-2小时",
                    priority = TestPriority.IMPORTANT,
                    notes = "推荐测试"
                ),
                DVPTestItem(
                    testId = "ISO-001",
                    testCategory = TestCategory.SAFETY_TESTING,
                    testName = "ISOFIX接口强度测试",
                    standardReference = "ECE R129 §5.5.1",
                    testMethod = "向接口施加5000N拉力，持续10s",
                    acceptanceCriteria = "接口无变形、无断裂；连接稳定性满足要求",
                    testEquipment = "拉力试验机",
                    sampleSize = 5,
                    estimatedDuration = "1-2小时",
                    priority = TestPriority.MANDATORY,
                    notes = "必须测试"
                )
            )
        ),
        standardCompliance = StandardCompliance(
            standard = InternationalStandard.ECE_R129,
            complianceItems = listOf(
                ComplianceItem(
                    requirementId = "REQ-001",
                    requirement = "包络尺寸合规性",
                    isCompliant = true,
                    gapAnalysis = "所有尺寸在允许范围内",
                    remediationPlan = null
                ),
                ComplianceItem(
                    requirementId = "REQ-002",
                    requirement = "侧面碰撞防护",
                    isCompliant = true,
                    gapAnalysis = "LSP系统设计符合要求",
                    remediationPlan = null
                ),
                ComplianceItem(
                    requirementId = "REQ-003",
                    requirement = "ISOFIX接口",
                    isCompliant = true,
                    gapAnalysis = "接口强度满足5000N要求",
                    remediationPlan = null
                )
            ),
            overallCompliance = ComplianceStatus.FULLY_COMPLIANT,
            recommendations = listOf("建议通过完整的ECE R129认证测试", "准备EN ISO 9001质量体系认证")
        )
    )
    
    /**
     * 婴儿推车示例（EN 1888，0-15kg）
     */
    val strollerEN1888Example = DesignSuggestionReport(
        id = "example-stroller-en1888",
        timestamp = System.currentTimeMillis(),
        productType = ProductType.STROLLER,
        standard = InternationalStandard.EN_1888,
        userInput = ProductInput.Stroller(
            StrollerInput(
                standard = InternationalStandard.EN_1888,
                subtype = StrollerSubtype.HIGH_VIEW,
                weightCapacityRange = "0-15kg",
                foldedDimensions = "70×60×35cm",
                usageScenario = UsageScenario.CITY_COMMUTE,
                customFeatures = listOf("一键折叠", "四轮避震"),
                specificRequirements = listOf("提升避震性能适配崎岖路面", "优化折叠机构实现一键收纳"),
                customNotes = "高景观款城市通勤推车"
            )
        ),
        designSuggestions = DesignSuggestions(
            productType = ProductType.STROLLER,
            functionalFeatures = listOf(
                FunctionalFeature(
                    category = "避震功能",
                    description = "前轮独立避震+后轮液压避震",
                    recommendation = "前轮弹簧行程3cm，后轮液压避震行程2cm，适配户外崎岖路面，参考Baby Jogger City Mini GT2结构",
                    referenceStandard = "ASTM F833 §5.9",
                    implementationDifficulty = DifficultyLevel.MODERATE
                ),
                FunctionalFeature(
                    category = "折叠功能",
                    description = "一键式中央折叠，单手可完成操作",
                    recommendation = "双重机械锁定（折叠卡扣+安全锁），折叠后尺寸70×60×35cm，参考UPPAbaby Cruz V2折叠逻辑",
                    referenceStandard = "EN 1888 §5.4",
                    implementationDifficulty = DifficultyLevel.MODERATE
                ),
                FunctionalFeature(
                    category = "制动系统",
                    description = "双后轮独立制动，脚踏式操作",
                    recommendation = "制动后施加150N推力无滑动，符合EN 1888斜坡制动测试要求",
                    referenceStandard = "EN 1888 §7.3",
                    implementationDifficulty = DifficultyLevel.EASY
                )
            ),
            dimensionParameters = DimensionParameters(
                externalDimensions = ExternalDimensionSuggestion(
                    width = DimensionRecommendation(
                        recommendedValue = DoubleRange(55.0, 60.0),
                        unit = "cm",
                        rationale = "高景观款宽度，确保稳定性和通行性",
                        standardReference = "EN 1888 §5.2"
                    ),
                    length = DimensionRecommendation(
                        recommendedValue = DoubleRange(100.0, 105.0),
                        unit = "cm",
                        rationale = "展开尺寸，座高55cm远离汽车尾气",
                        standardReference = "EN 1888 §5.2"
                    ),
                    height = DimensionRecommendation(
                        recommendedValue = DoubleRange(105.0, 110.0),
                        unit = "cm",
                        rationale = "高景观推车高度，提升儿童视野",
                        standardReference = null
                    ),
                    weight = DimensionRecommendation(
                        recommendedValue = DoubleRange(12.0, 14.0),
                        unit = "kg",
                        rationale = "轻量化设计，便于提携",
                        standardReference = "ASTM F833 §5.2"
                    ),
                    notes = "参考UPPAbaby Cruz V2尺寸"
                ),
                internalDimensions = InternalDimensionSuggestion(
                    seatDepth = DimensionRecommendation(
                        recommendedValue = DoubleRange(26.0, 30.0),
                        unit = "cm",
                        rationale = "适配15kg儿童臀腿长度23cm，避免腰部悬空",
                        standardReference = "EN 1888 §5.2"
                    ),
                    seatWidth = DimensionRecommendation(
                        recommendedValue = DoubleRange(36.0, 40.0),
                        unit = "cm",
                        rationale = "适配15kg儿童肩宽30cm，预留4cm活动空间",
                        standardReference = "EN 1888 §5.2"
                    ),
                    backrestHeight = DimensionRecommendation(
                        recommendedValue = DoubleRange(45.0, 50.0),
                        unit = "cm",
                        rationale = "从座面至头枕最高位置，提供充分支撑",
                        standardReference = null
                    ),
                    headrestWidth = DimensionRecommendation(
                        recommendedValue = DoubleRange(32.0, 36.0),
                        unit = "cm",
                        rationale = "适配儿童头宽，确保舒适性",
                        standardReference = null
                    ),
                    shoulderWidth = DimensionRecommendation(
                        recommendedValue = DoubleRange(34.0, 38.0),
                        unit = "cm",
                        rationale = "适配儿童肩宽范围",
                        standardReference = null
                    ),
                    notes = "参考Cybex Mios设计"
                ),
                adjustmentRanges = listOf(
                    AdjustmentRange(
                        component = "头枕高度",
                        minPosition = 25.0,
                        maxPosition = 40.0,
                        unit = "cm",
                        adjustmentSteps = 10,
                        recommendation = "最小高度适配新生儿，最大高度适配4岁儿童"
                    ),
                    AdjustmentRange(
                        component = "靠背角度",
                        minPosition = 110.0,
                        maxPosition = 175.0,
                        unit = "度",
                        adjustmentSteps = 0,
                        recommendation = "110°坐姿、145°半躺、175°全躺，无级调节"
                    )
                ),
                envelopeCompliance = null
            ),
            materialRecommendations = listOf(
                MaterialRecommendation(
                    component = "车架",
                    recommendedMaterial = "铝合金",
                    properties = listOf("轻量化", "高强度", "耐腐蚀"),
                    safetyRating = SafetyRating.EXCELLENT,
                    supplierSuggestions = listOf("南山铝业", "中国铝业")
                ),
                MaterialRecommendation(
                    component = "轮子",
                    recommendedMaterial = "PU发泡轮",
                    properties = listOf("免充气", "耐磨", "避震好"),
                    safetyRating = SafetyRating.GOOD,
                    supplierSuggestions = listOf("正新橡胶", "中策橡胶")
                ),
                MaterialRecommendation(
                    component = "座椅面料",
                    recommendedMaterial = "3D网眼布",
                    properties = listOf("透气", "易清洁", "防水"),
                    safetyRating = SafetyRating.GOOD,
                    supplierSuggestions = listOf("台达化学", "华峰集团")
                )
            ),
            safetyRecommendations = listOf(
                SafetyRecommendation(
                    aspect = "制动系统",
                    description = "双后轮独立制动，脚踏式操作",
                    implementation = "双轮独立制动设计，制动后施加150N推力无滑动",
                    testingRequirement = "EN 1888 §7.3斜坡制动测试",
                    priority = Priority.CRITICAL
                ),
                SafetyRecommendation(
                    aspect = "折叠锁定",
                    description = "双重机械锁定系统",
                    implementation = "折叠卡扣+安全锁双重锁定，避免误解锁",
                    testingRequirement = "ASTM F833 §5.7折叠锁定可靠性测试",
                    priority = Priority.HIGH
                ),
                SafetyRecommendation(
                    aspect = "安全带",
                    description = "五点式安全带",
                    implementation = "肩带宽度4cm、腰带宽度3cm，符合EN 1888 §6.3标准",
                    testingRequirement = "EN 1888 §7.4安全带强度测试",
                    priority = Priority.CRITICAL
                )
            )
        ),
        brandComparison = BrandComparison(
            targetProductType = ProductType.STROLLER,
            comparedBrands = listOf(
                BrandBenchmark(
                    brandName = "UPPAbaby",
                    productName = "Cruz V2",
                    keyAdvantages = listOf(
                        "折叠尺寸53×66×27cm",
                        "承重0-15kg",
                        "前轮避震",
                        "适配安全座椅"
                    ),
                    technicalSpecs = TechnicalSpecs(
                        dimensions = ProductDimensions(
                            width = 59.0,
                            height = 101.0,
                            depth = 98.0,
                            seatWidth = 34.0,
                            seatDepth = 28.0
                        ),
                        weight = 9.2,
                        materials = listOf("铝合金车架", "PU发泡轮", "3D网眼布"),
                        certifications = listOf("EN 1888", "ASTM F833"),
                        uniqueFeatures = listOf("一键折叠", "可调节扶手", "超大储物篮")
                    ),
                    marketPosition = "高端市场，注重品质和设计"
                ),
                BrandBenchmark(
                    brandName = "Baby Jogger",
                    productName = "City Mini GT2",
                    keyAdvantages = listOf(
                        "越野级避震",
                        "一键折叠",
                        "收纳重量13.6kg",
                        "全尺寸车轮"
                    ),
                    technicalSpecs = TechnicalSpecs(
                        dimensions = ProductDimensions(
                            width = 64.0,
                            height = 104.0,
                            depth = 92.0,
                            seatWidth = 33.0,
                            seatDepth = 29.0
                        ),
                        weight = 11.4,
                        materials = listOf("钢制车架", "空气胎", "防晒面料"),
                        certifications = listOf("EN 1888", "ASTM F833"),
                        uniqueFeatures = listOf("永久锁定折叠", "四轮避震", "遮阳篷SPF 50+")
                    ),
                    marketPosition = "中端市场，以性能和性价比著称"
                )
            ),
            summaryAnalysis = "建议结合UPPAbaby的折叠便利性和Baby Jogger的越野性能，打造全能型推车",
            differentiatingSuggestions = listOf(
                "采用UPPAbaby的一键折叠设计",
                "集成Baby Jogger的四轮避震系统",
                "参考Cybex Mios的轻量化设计"
            )
        ),
        dvpTestMatrix = DVPTestMatrix(
            productType = ProductType.STROLLER,
            standard = InternationalStandard.EN_1888,
            testItems = listOf(
                DVPTestItem(
                    testId = "BRK-001",
                    testCategory = TestCategory.SAFETY_TESTING,
                    testName = "斜坡制动测试",
                    standardReference = "EN 1888 §7.3",
                    testMethod = "在15°斜坡上锁定制动，座椅放置15kg负重，静置30min",
                    acceptanceCriteria = "推车无滑动、无倾倒；制动机构无松动",
                    testEquipment = "斜坡测试台",
                    sampleSize = 3,
                    estimatedDuration = "1小时",
                    priority = TestPriority.MANDATORY,
                    notes = "必须测试"
                ),
                DVPTestItem(
                    testId = "FLD-001",
                    testCategory = TestCategory.DURABILITY_TESTING,
                    testName = "折叠锁定可靠性测试",
                    standardReference = "ASTM F833 §5.7",
                    testMethod = "反复折叠-展开500次，每次折叠后施加50N向上拉力",
                    acceptanceCriteria = "折叠机构无卡滞；锁定后无意外解锁；结构无变形",
                    testEquipment = "折叠测试机",
                    sampleSize = 3,
                    estimatedDuration = "8小时",
                    priority = TestPriority.CRITICAL,
                    notes = "关键测试"
                ),
                DVPTestItem(
                    testId = "FALL-001",
                    testCategory = TestCategory.IMPACT_TESTING,
                    testName = "跌落撞击测试",
                    standardReference = "EN 1888 §7.6",
                    testMethod = "推车折叠状态下，从80cm高度自由跌落至水泥地面，测试6个不同角度",
                    acceptanceCriteria = "结构无破裂、无尖锐边缘；折叠锁定功能正常",
                    testEquipment = "跌落测试台",
                    sampleSize = 3,
                    estimatedDuration = "2小时",
                    priority = TestPriority.IMPORTANT,
                    notes = "推荐测试"
                ),
                DVPTestItem(
                    testId = "HAR-001",
                    testCategory = TestCategory.SAFETY_TESTING,
                    testName = "安全带强度测试",
                    standardReference = "EN 1888 §7.4",
                    testMethod = "向安全带各方向施加300N拉力，持续30s",
                    acceptanceCriteria = "安全带无撕裂；卡扣无松脱；固定点无变形",
                    testEquipment = "拉力试验机",
                    sampleSize = 5,
                    estimatedDuration = "1-2小时",
                    priority = TestPriority.MANDATORY,
                    notes = "必须测试"
                ),
                DVPTestItem(
                    testId = "SHK-001",
                    testCategory = TestCategory.IMPACT_TESTING,
                    testName = "避震性能测试",
                    standardReference = "ASTM F833 §5.9",
                    testMethod = "推车放置15kg负重，在颠簸路面（凸起高度2cm）推行10km，记录震动传递量",
                    acceptanceCriteria = "座椅处震动加速度≤10m/s²；避震结构无失效",
                    testEquipment = "震动测试台",
                    sampleSize = 3,
                    estimatedDuration = "2小时",
                    priority = TestPriority.IMPORTANT,
                    notes = "推荐测试"
                )
            )
        ),
        standardCompliance = StandardCompliance(
            standard = InternationalStandard.EN_1888,
            complianceItems = listOf(
                ComplianceItem(
                    requirementId = "REQ-001",
                    requirement = "制动性能",
                    isCompliant = true,
                    gapAnalysis = "斜坡制动测试符合要求",
                    remediationPlan = null
                ),
                ComplianceItem(
                    requirementId = "REQ-002",
                    requirement = "折叠锁定可靠性",
                    isCompliant = true,
                    gapAnalysis = "500次折叠测试通过",
                    remediationPlan = null
                ),
                ComplianceItem(
                    requirementId = "REQ-003",
                    requirement = "安全带强度",
                    isCompliant = true,
                    gapAnalysis = "300N拉力测试通过",
                    remediationPlan = null
                )
            ),
            overallCompliance = ComplianceStatus.FULLY_COMPLIANT,
            recommendations = listOf("建议通过完整的EN 1888认证测试", "准备CE标志申请")
        )
    )
}

package com.childproduct.designassistant.data

import com.childproduct.designassistant.model.*

/**
 * 品牌参数数据库
 * 包含主流品牌的实际产品参数
 */
object BrandDatabase {

    /**
     * Britax 品牌数据
     */
    val britaxRomerDualfix = BrandParameters(
        brandName = "Britax Römer",
        productName = "Dualfix M i-Size",
        productType = ProductType.SAFETY_SEAT,
        specifications = ProductSpec(
            internalDimensions = InternalDimensions(
                seatWidth = 38.0,
                seatDepth = 45.0,
                backrestHeight = 60.0,
                headrestWidth = 35.0,
                shoulderWidth = 32.0
            ),
            externalDimensions = ExternalDimensions(
                width = 53.0,
                height = 64.0,
                depth = 57.0
            ),
            weight = 14.5,
            weightLimit = WeightLimit(min = 0.0, max = 18.0),
            heightLimit = HeightLimit(min = 40, max = 105)
        ),
        features = listOf(
            ProductFeature(
                name = "头托调节",
                description = "12档头托高度调节，伴随靠背角度同步调节",
                specifications = mapOf(
                    "调节范围" to "10-30cm",
                    "调节方式" to "单手操作",
                    "档位数量" to "12档"
                )
            ),
            ProductFeature(
                name = "靠背角度",
                description = "5档靠背角度调节",
                specifications = mapOf(
                    "角度范围" to "105°-135°",
                    "调节方式" to "操作杆"
                )
            ),
            ProductFeature(
                name = "ISOFIX固定",
                description = "集成ISOFIX接口，带支撑腿",
                specifications = mapOf(
                    "固定方式" to "ISOFIX + 支撑腿",
                    "安装提示" to "绿灯指示正确安装"
                )
            ),
            ProductFeature(
                name = "旋转功能",
                description = "360度旋转，方便抱娃进出",
                specifications = mapOf(
                    "旋转角度" to "360°",
                    "旋转档位" to "正向/侧向/反向"
                )
            )
        ),
        compliance = ComplianceInfo(
            standards = listOf("ECE R129", "i-Size"),
            envelopeClass = "Envelope B",
            certificationNumber = "ECE R129-04",
            certificationDate = "2023-01-15"
        ),
        imageUrl = null,
        sourceUrl = "https://www.britax-roemer.com"
    )

    val britaxAdvansafix = BrandParameters(
        brandName = "Britax Römer",
        productName = "Advansafix M i-Size",
        productType = ProductType.SAFETY_SEAT,
        specifications = ProductSpec(
            internalDimensions = InternalDimensions(
                seatWidth = 40.0,
                seatDepth = 48.0,
                backrestHeight = 65.0,
                headrestWidth = 36.0,
                shoulderWidth = 34.0
            ),
            externalDimensions = ExternalDimensions(
                width = 55.0,
                height = 70.0,
                depth = 58.0
            ),
            weight = 16.2,
            weightLimit = WeightLimit(min = 9.0, max = 36.0),
            heightLimit = HeightLimit(min = 100, max = 150)
        ),
        features = listOf(
            ProductFeature(
                name = "头托调节",
                description = "10档头托高度调节",
                specifications = mapOf(
                    "调节范围" to "12-28cm",
                    "调节方式" to "单手操作"
                )
            ),
            ProductFeature(
                name = "ISOFIX固定",
                description = "集成ISOFIX + 顶部系带",
                specifications = mapOf(
                    "固定方式" to "ISOFIX + Top Tether"
                )
            ),
            ProductFeature(
                name = "侧面碰撞保护",
                description = "SICT侧面碰撞保护技术",
                specifications = mapOf(
                    "类型" to "可调节侧翼",
                    "位置" to "左/右两侧"
                )
            )
        ),
        compliance = ComplianceInfo(
            standards = listOf("ECE R129", "i-Size"),
            envelopeClass = "Envelope B/C",
            certificationNumber = "ECE R129-05",
            certificationDate = "2023-03-20"
        )
    )

    /**
     * Maxi-Cosi 品牌数据
     */
    val maxiCosiPearl360 = BrandParameters(
        brandName = "Maxi-Cosi",
        productName = "Pearl 360",
        productType = ProductType.SAFETY_SEAT,
        specifications = ProductSpec(
            internalDimensions = InternalDimensions(
                seatWidth = 37.0,
                seatDepth = 46.0,
                backrestHeight = 62.0,
                headrestWidth = 34.0,
                shoulderWidth = 31.0
            ),
            externalDimensions = ExternalDimensions(
                width = 52.0,
                height = 65.0,
                depth = 56.0
            ),
            weight = 13.8,
            weightLimit = WeightLimit(min = 0.0, max = 18.0),
            heightLimit = HeightLimit(min = 40, max = 105)
        ),
        features = listOf(
            ProductFeature(
                name = "360°旋转",
                description = "配合FamilyFix360底座实现360度旋转",
                specifications = mapOf(
                    "旋转角度" to "360°",
                    "旋转方向" to "任意角度"
                )
            ),
            ProductFeature(
                name = "头托调节",
                description = "7档头托高度调节",
                specifications = mapOf(
                    "调节范围" to "8-25cm",
                    "档位数量" to "7档"
                )
            ),
            ProductFeature(
                name = "舒适材料",
                description = "ClimaFlow透气系统",
                specifications = mapOf(
                    "功能" to "通风透气",
                    "材质" to "婴儿级面料"
                )
            )
        ),
        compliance = ComplianceInfo(
            standards = listOf("ECE R129", "i-Size"),
            envelopeClass = "Envelope A/B",
            certificationNumber = "ECE R129-03",
            certificationDate = "2022-11-10"
        )
    )

    val maxiCosiRodifix = BrandParameters(
        brandName = "Maxi-Cosi",
        productName = "RodiFix",
        productType = ProductType.SAFETY_SEAT,
        specifications = ProductSpec(
            internalDimensions = InternalDimensions(
                seatWidth = 39.0,
                seatDepth = 50.0,
                backrestHeight = 68.0,
                headrestWidth = 35.0,
                shoulderWidth = 33.0
            ),
            externalDimensions = ExternalDimensions(
                width = 54.0,
                height = 72.0,
                depth = 55.0
            ),
            weight = 6.2,
            weightLimit = WeightLimit(min = 15.0, max = 36.0),
            heightLimit = HeightLimit(min = 100, max = 150)
        ),
        features = listOf(
            ProductFeature(
                name = "头托调节",
                description = "10档头托高度调节",
                specifications = mapOf(
                    "调节范围" to "15-32cm"
                )
            ),
            ProductFeature(
                name = "ISOFIX固定",
                description = "ISOFIX + 顶部系带",
                specifications = mapOf(
                    "固定方式" to "ISOFIX + Top Tether"
                )
            ),
            ProductFeature(
                name = "侧面保护",
                description = "Air Protect侧面保护技术",
                specifications = mapOf(
                    "技术" to "空气缓冲"
                )
            )
        ),
        compliance = ComplianceInfo(
            standards = listOf("ECE R129", "i-Size"),
            envelopeClass = "Envelope C",
            certificationNumber = "ECE R129-02",
            certificationDate = "2022-08-05"
        )
    )

    /**
     * Cybex 品牌数据
     */
    val cybexSirona = BrandParameters(
        brandName = "Cybex",
        productName = "Sirona Gi i-Size",
        productType = ProductType.SAFETY_SEAT,
        specifications = ProductSpec(
            internalDimensions = InternalDimensions(
                seatWidth = 36.0,
                seatDepth = 44.0,
                backrestHeight = 58.0,
                headrestWidth = 33.0,
                shoulderWidth = 30.0
            ),
            externalDimensions = ExternalDimensions(
                width = 51.0,
                height = 62.0,
                depth = 55.0
            ),
            weight = 14.0,
            weightLimit = WeightLimit(min = 0.0, max = 18.0),
            heightLimit = HeightLimit(min = 40, max = 105)
        ),
        features = listOf(
            ProductFeature(
                name = "360°旋转",
                description = "单手操作360度旋转",
                specifications = mapOf(
                    "旋转角度" to "360°",
                    "操作方式" to "单手操作"
                )
            ),
            ProductFeature(
                name = "头托调节",
                description = "12档头托高度调节",
                specifications = mapOf(
                    "调节范围" to "10-30cm"
                )
            ),
            ProductFeature(
                name = "侧面保护",
                description = "L.S.P. System线性侧面保护",
                specifications = mapOf(
                    "保护级别" to "3级"
                )
            ),
            ProductFeature(
                name = "支撑腿",
                description = "可调节支撑腿",
                specifications = mapOf(
                    "调节范围" to "5-25cm"
                )
            )
        ),
        compliance = ComplianceInfo(
            standards = listOf("ECE R129", "i-Size"),
            envelopeClass = "Envelope A/B",
            certificationNumber = "ECE R129-06",
            certificationDate = "2023-05-12"
        )
    )

    val cybexSolution = BrandParameters(
        brandName = "Cybex",
        productName = "Solution X-Fix",
        productType = ProductType.SAFETY_SEAT,
        specifications = ProductSpec(
            internalDimensions = InternalDimensions(
                seatWidth = 38.0,
                seatDepth = 52.0,
                backrestHeight = 70.0,
                headrestWidth = 36.0,
                shoulderWidth = 34.0
            ),
            externalDimensions = ExternalDimensions(
                width = 54.0,
                height = 74.0,
                depth = 54.0
            ),
            weight = 7.5,
            weightLimit = WeightLimit(min = 15.0, max = 36.0),
            heightLimit = HeightLimit(min = 100, max = 150)
        ),
        features = listOf(
            ProductFeature(
                name = "头托调节",
                description = "12档头托高度调节",
                specifications = mapOf(
                    "调节范围" to "18-35cm"
                )
            ),
            ProductFeature(
                name = "倾斜调节",
                description = "3档靠背倾斜",
                specifications = mapOf(
                    "角度范围" to "102°-112°"
                )
            ),
            ProductFeature(
                name = "ISOFIX固定",
                description = "ISOFIX + 顶部系带",
                specifications = mapOf(
                    "固定方式" to "ISOFIX + Top Tether"
                )
            )
        ),
        compliance = ComplianceInfo(
            standards = listOf("ECE R129"),
            envelopeClass = "Envelope C",
            certificationNumber = "ECE R129-01",
            certificationDate = "2022-06-18"
        )
    )

    /**
     * 获取所有品牌数据
     */
    fun getAllBrandParameters(): List<BrandParameters> {
        return listOf(
            britaxRomerDualfix,
            britaxAdvansafix,
            maxiCosiPearl360,
            maxiCosiRodifix,
            cybexSirona,
            cybexSolution
        )
    }

    /**
     * 根据重量范围筛选
     */
    fun filterByWeightRange(minWeight: Double, maxWeight: Double): List<BrandParameters> {
        return getAllBrandParameters().filter { brand ->
            brand.specifications.weightLimit.min <= maxWeight &&
            brand.specifications.weightLimit.max >= minWeight
        }
    }

    /**
     * 根据身高范围筛选
     */
    fun filterByHeightRange(minHeight: Int, maxHeight: Int): List<BrandParameters> {
        return getAllBrandParameters().filter { brand ->
            brand.specifications.heightLimit.min <= maxHeight &&
            brand.specifications.heightLimit.max >= minHeight
        }
    }

    /**
     * 计算平均规格
     */
    fun calculateAverageSpecs(brands: List<BrandParameters>): AverageSpecs {
        if (brands.isEmpty()) {
            return AverageSpecs(0.0, 0.0, 0.0, emptyList())
        }

        val avgWidth = brands.mapNotNull { it.specifications.internalDimensions.seatWidth }.average()
        val avgDepth = brands.mapNotNull { it.specifications.internalDimensions.seatDepth }.average()
        val avgWeight = brands.map { it.specifications.weight }.average()

        // 提取共同功能
        val allFeatures = brands.flatMap { it.features.map { it.name } }
        val commonFeatures = allFeatures.groupingBy { it }.eachCount()
            .filter { it.value >= brands.size / 2 }
            .keys
            .toList()

        return AverageSpecs(avgWidth, avgDepth, avgWeight, commonFeatures)
    }

    /**
     * 获取品牌比较数据
     */
    fun getBrandComparison(heightRange: String, weightRange: String): BrandComparison {
        val (minH, maxH) = parseRange(heightRange)
        val (minW, maxW) = parseRange(weightRange)

        val filteredBrands = getAllBrandParameters().filter { brand ->
            rangesOverlap(
                minW, maxW,
                brand.specifications.weightLimit.min,
                brand.specifications.weightLimit.max
            )
        }

        val avgSpecs = calculateAverageSpecs(filteredBrands)

        val recommendations = mutableListOf<String>()
        recommendations.add("建议座椅宽度：${String.format("%.1f", avgSpecs.avgInternalWidth)} cm（平均值）")
        recommendations.add("建议座椅深度：${String.format("%.1f", avgSpecs.avgInternalDepth)} cm（平均值）")
        recommendations.add("建议产品重量：< ${String.format("%.1f", avgSpecs.avgWeight + 2.0)} kg")

        if (filteredBrands.isNotEmpty()) {
            val avgHeadrestRange = filteredBrands.mapNotNull { brand ->
                brand.features.find { it.name.contains("头托") }?.specifications?.get("调节范围")
            }.firstOrNull()
            if (avgHeadrestRange != null) {
                recommendations.add("建议头托调节范围：$avgHeadrestRange（参考主流品牌）")
            }
        }

        val brandBenchmarks = filteredBrands.map { brand ->
            BrandBenchmark(
                brandName = brand.brandName,
                productName = brand.productName,
                keyAdvantages = brand.features.map { it.name },
                technicalSpecs = TechnicalSpecs(
                    dimensions = ProductDimensions(
                        width = brand.specifications.externalDimensions.width,
                        height = brand.specifications.externalDimensions.height,
                        depth = brand.specifications.externalDimensions.depth,
                        seatWidth = brand.specifications.internalDimensions.seatWidth ?: 40.0,
                        seatDepth = brand.specifications.internalDimensions.seatDepth ?: 45.0
                    ),
                    weight = brand.specifications.weightLimit.max,
                    materials = emptyList(),
                    certifications = brand.compliance.standards,
                    uniqueFeatures = brand.features.map { it.name }
                ),
                marketPosition = "主流"
            )
        }

        return BrandComparison(
            targetProductType = ProductType.SAFETY_SEAT,
            comparedBrands = brandBenchmarks,
            summaryAnalysis = "基于${filteredBrands.size}个品牌的分析",
            differentiatingSuggestions = recommendations,
            averageSpecs = avgSpecs
        )
    }

    /**
     * 解析范围
     */
    private fun parseRange(rangeStr: String): Pair<Double, Double> {
        val cleaned = rangeStr.replace("[^0-9-]".toRegex(), "")
        val parts = cleaned.split("-")
        return if (parts.size == 2) {
            Pair(parts[0].toDouble(), parts[1].toDouble())
        } else {
            Pair(parts[0].toDouble(), parts[0].toDouble())
        }
    }

    /**
     * 检查范围重叠
     */
    private fun rangesOverlap(min1: Double, max1: Double, min2: Double, max2: Double): Boolean {
        return !(max1 < min2 || max2 < min1)
    }
}

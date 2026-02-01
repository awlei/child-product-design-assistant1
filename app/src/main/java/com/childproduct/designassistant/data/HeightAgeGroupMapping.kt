package com.childproduct.designassistant.data

import com.childproduct.designassistant.model.ProductType
import com.childproduct.designassistant.model.StandardRegion

/**
 * 身高-年龄段-标准分组映射数据
 * 用于根据输入的身高范围，自动匹配对应的年龄段和标准分组
 */

/**
 * ECE R129 标准分组定义（基于R129r4e §6.1.2.7）
 */
enum class ECEGroup(val code: String, val displayName: String, val weightRange: String, val heightRange: String, val ageRange: String, val isRearwardRequired: Boolean) {
    GROUP_0(
        code = "Group 0",
        displayName = "Group 0",
        weightRange = "0-10kg",
        heightRange = "40-60cm",
        ageRange = "新生儿-6个月",
        isRearwardRequired = true
    ),
    GROUP_0_PLUS(
        code = "Group 0+",
        displayName = "Group 0+ / i-Size",
        weightRange = "0-13kg",
        heightRange = "40-83cm",  // R129r4e: 后向座椅应能容纳身高至83cm的儿童
        ageRange = "新生儿-15个月",  // R129r4e: 15个月以下必须后向
        isRearwardRequired = true
    ),
    GROUP_1(
        code = "Group 1",
        displayName = "Group 1 / i-Size",
        weightRange = "9-18kg",
        heightRange = "76-105cm",  // R129r4e: 前向座椅不应低于76cm
        ageRange = "15个月-4岁",
        isRearwardRequired = false
    ),
    GROUP_2(
        code = "Group 2",
        displayName = "Group 2 / i-Size booster seat",
        weightRange = "15-25kg",
        heightRange = "100-125cm",  // R129r4e: 非整体式座椅不得低于100cm，上限不能低于105cm
        ageRange = "3.5岁-7岁",
        isRearwardRequired = false
    ),
    GROUP_3(
        code = "Group 3",
        displayName = "Group 3 / i-Size booster seat",
        weightRange = "22-36kg",
        heightRange = "125-150cm",
        ageRange = "6岁-12岁",
        isRearwardRequired = false
    ),
    GROUP_0_1_2_3(
        code = "Group 0+/1/2/3",
        displayName = "Group 0+/1/2/3（全分组可转换）",
        weightRange = "0-36kg",
        heightRange = "40-150cm",
        ageRange = "新生儿-12岁",
        isRearwardRequired = true  // 15个月以下必须后向
    )
}

/**
 * 儿童安全座椅身高分段匹配结果
 */
data class HeightSegmentMatch(
    val minHeight: Double,        // 最小身高 cm
    val maxHeight: Double,        // 最大身高 cm
    val matchedGroups: List<ECEGroup>, // 匹配的标准分组
    val ageRange: String,         // 建议年龄范围
    val recommendedDirection: String, // 推荐朝向（后向/前向/双向）
    val isFullRange: Boolean      // 是否覆盖全范围（40-150cm）
)

/**
 * 标准分组映射服务
 */
class HeightAgeGroupMapper {

    companion object {
        // ECE R129 身高阈值定义（基于R129r4e §6.1.2.7和§7.1.3.6 Table 8）
        private val HEIGHT_THRESHOLDS = listOf(
            40.0 to 60.0,   // Group 0 / Q0
            40.0 to 75.0,   // Group 0+ / Q1
            40.0 to 83.0,   // Group 0+（后向座椅最大83cm，R129r4e要求）
            76.0 to 105.0,  // Group 1 / Q3（前向座椅最小76cm）
            100.0 to 125.0, // Group 2 / Q6（非整体式座椅最小100cm）
            125.0 to 150.0  // Group 3 / Q10
        )

        // i-Size 分组（基于身高，R129r4e Annex 8 Table 8）
        private val ISIZE_GROUPS = mapOf(
            40.0 to 60.0 to "Q0",    // 新生儿（≤60cm）
            60.0 to 75.0 to "Q1",    // 6-12个月（60-75cm）
            75.0 to 87.0 to "Q1.5",  // 9-18个月（75-87cm）
            87.0 to 105.0 to "Q3",   // 18个月-4岁（87-105cm）
            105.0 to 125.0 to "Q6",  // 4-7岁（105-125cm）
            125.0 to 150.0 to "Q10"  // 6-12岁（125-150cm）
        )

        // R129r4e 关键身高阈值
        const val MAX_REARWARD_HEIGHT = 83.0      // 后向座椅最大83cm（15个月）
        const val MIN_FORWARD_HEIGHT = 76.0      // 前向座椅最小76cm（15个月）
        const val MIN_BOOSTER_HEIGHT = 100.0     // 非整体式座椅最小100cm
        const val MIN_BOOSTER_UPPER_LIMIT = 105.0 // 非整体式座椅最小上限105cm
        const val BOOSTER_HEAD_PROTECTION = 135.0 // 增高座椅头部保护至135cm
    }

    /**
     * 根据身高范围匹配标准分组和年龄段
     */
    fun matchHeightRange(heightRange: String, productType: ProductType): HeightSegmentMatch {
        val (minHeight, maxHeight) = parseHeightRange(heightRange)

        return when (productType) {
            ProductType.SAFETY_SEAT -> matchSafetySeatHeight(minHeight, maxHeight)
            ProductType.STROLLER -> matchStrollerHeight(minHeight, maxHeight)
            ProductType.HIGH_CHAIR -> matchHighChairHeight(minHeight, maxHeight)
            ProductType.CRIB -> matchHouseholdGoodsHeight(minHeight, maxHeight)
        }
    }

    /**
     * 儿童安全座椅身高匹配（基于R129r4e §6.1.2.7）
     */
    private fun matchSafetySeatHeight(minHeight: Double, maxHeight: Double): HeightSegmentMatch {
        val matchedGroups = mutableListOf<ECEGroup>()

        // 判断覆盖范围（基于R129r4e关键阈值）
        when {
            // 覆盖全范围（40-150cm）
            minHeight <= 40.0 && maxHeight >= 150.0 -> {
                matchedGroups.add(ECEGroup.GROUP_0_1_2_3)
            }
            // 覆盖后向阶段（40-83cm，15个月以下必须后向）
            maxHeight <= MAX_REARWARD_HEIGHT -> {
                matchedGroups.add(ECEGroup.GROUP_0)
                if (maxHeight >= 60.0) matchedGroups.add(ECEGroup.GROUP_0_PLUS)
            }
            // 覆盖前向阶段（76-105cm，15个月以上）
            minHeight >= MIN_FORWARD_HEIGHT && maxHeight <= 105.0 -> {
                matchedGroups.add(ECEGroup.GROUP_1)
            }
            // 覆盖增高垫阶段（100cm以上，非整体式）
            minHeight >= MIN_BOOSTER_HEIGHT && maxHeight >= MIN_BOOSTER_UPPER_LIMIT -> {
                matchedGroups.add(ECEGroup.GROUP_2)
                if (maxHeight >= 125.0) matchedGroups.add(ECEGroup.GROUP_3)
            }
            // 跨越多个分组
            minHeight <= 40.0 && maxHeight >= 105.0 -> {
                matchedGroups.add(ECEGroup.GROUP_0_PLUS)
                matchedGroups.add(ECEGroup.GROUP_1)
            }
            minHeight <= 75.0 && maxHeight >= 125.0 -> {
                matchedGroups.add(ECEGroup.GROUP_1)
                matchedGroups.add(ECEGroup.GROUP_2)
            }
            minHeight <= 100.0 && maxHeight >= 150.0 -> {
                matchedGroups.add(ECEGroup.GROUP_2)
                matchedGroups.add(ECEGroup.GROUP_3)
            }
            else -> {
                // 精确匹配
                matchedGroups.addAll(matchSingleHeightRange(minHeight, maxHeight))
            }
        }

        // 确定年龄范围
        val ageRange = calculateAgeRange(minHeight, maxHeight)

        // 确定推荐朝向（基于R129r4e §6.1.2.7）
        val recommendedDirection = determineRecommendedDirectionR129(minHeight, maxHeight)

        val isFullRange = matchedGroups.contains(ECEGroup.GROUP_0_1_2_3)

        return HeightSegmentMatch(
            minHeight = minHeight,
            maxHeight = maxHeight,
            matchedGroups = matchedGroups,
            ageRange = ageRange,
            recommendedDirection = recommendedDirection,
            isFullRange = isFullRange
        )
    }

    /**
     * 单范围精确匹配
     */
    private fun matchSingleHeightRange(minHeight: Double, maxHeight: Double): List<ECEGroup> {
        val groups = mutableListOf<ECEGroup>()

        HEIGHT_THRESHOLDS.forEach { (min, max) ->
            if (maxHeight <= max && minHeight >= min) {
                groups.add(getECEGroupByHeight(max))
            }
        }

        return groups
    }

    /**
     * 根据最大身高获取对应的 ECE 分组
     */
    private fun getECEGroupByHeight(height: Double): ECEGroup {
        return when {
            height <= 60.0 -> ECEGroup.GROUP_0
            height <= 75.0 -> ECEGroup.GROUP_0_PLUS
            height <= 105.0 -> ECEGroup.GROUP_1
            height <= 125.0 -> ECEGroup.GROUP_2
            else -> ECEGroup.GROUP_3
        }
    }

    /**
     * 根据身高计算年龄范围
     */
    private fun calculateAgeRange(minHeight: Double, maxHeight: Double): String {
        val minAge = calculateAgeByHeight(minHeight)
        val maxAge = calculateAgeByHeight(maxHeight)

        return when {
            minAge == "新生儿" -> "$minAge-$maxAge"
            else -> "${minAge}岁-${maxAge}岁"
        }
    }

    /**
     * 根据身高估算年龄
     */
    private fun calculateAgeByHeight(height: Double): String {
        return when {
            height <= 60.0 -> "新生儿"
            height <= 75.0 -> "12个月"
            height <= 87.0 -> "18个月"
            height <= 105.0 -> "4岁"
            height <= 125.0 -> "7岁"
            else -> "12岁"
        }
    }

    /**
     * 确定推荐朝向（基于R129r4e §6.1.2.7）
     */
    private fun determineRecommendedDirection(groups: List<ECEGroup>): String {
        return when {
            groups.any { it == ECEGroup.GROUP_0 || it == ECEGroup.GROUP_0_PLUS } -> "后向（优先）→ 前向"
            groups.any { it == ECEGroup.GROUP_1 } -> "后向/前向（根据身高切换）"
            groups.any { it == ECEGroup.GROUP_2 || it == ECEGroup.GROUP_3 } -> "前向"
            else -> "前向"
        }
    }

    /**
     * 确定推荐朝向（基于R129r4e §6.1.2.7标准）
     * 15个月以下儿童必须使用后向或侧向
     */
    private fun determineRecommendedDirectionR129(minHeight: Double, maxHeight: Double): String {
        return when {
            // 15个月以下（身高≤83cm）必须后向
            maxHeight <= MAX_REARWARD_HEIGHT -> "后向（R129r4e强制要求）"
            // 跨越15个月界限（如40-105cm）
            minHeight <= MAX_REARWARD_HEIGHT && maxHeight >= MIN_FORWARD_HEIGHT -> "后向（≤15个月/≤83cm）→ 前向（≥15个月/≥76cm）"
            // 15个月以上
            minHeight >= MIN_FORWARD_HEIGHT -> "前向"
            else -> "前向"
        }
    }

    /**
     * 婴儿推车身高匹配
     */
    private fun matchStrollerHeight(minHeight: Double, maxHeight: Double): HeightSegmentMatch {
        val ageRange = when {
            maxHeight <= 75.0 -> "新生儿-15个月"
            maxHeight <= 95.0 -> "新生儿-3岁"
            else -> "新生儿-4岁"
        }

        return HeightSegmentMatch(
            minHeight = minHeight,
            maxHeight = maxHeight,
            matchedGroups = emptyList(),
            ageRange = ageRange,
            recommendedDirection = "不适用",
            isFullRange = false
        )
    }

    /**
     * 儿童高脚椅身高匹配
     */
    private fun matchHighChairHeight(minHeight: Double, maxHeight: Double): HeightSegmentMatch {
        val ageRange = when {
            maxHeight <= 105.0 -> "6个月-4岁"
            maxHeight <= 125.0 -> "6个月-7岁"
            else -> "6个月-10岁"
        }

        return HeightSegmentMatch(
            minHeight = minHeight,
            maxHeight = maxHeight,
            matchedGroups = emptyList(),
            ageRange = ageRange,
            recommendedDirection = "不适用",
            isFullRange = false
        )
    }

    /**
     * 儿童家庭用品身高匹配
     */
    private fun matchHouseholdGoodsHeight(minHeight: Double, maxHeight: Double): HeightSegmentMatch {
        val ageRange = when {
            maxHeight <= 105.0 -> "3岁-4岁"
            maxHeight <= 125.0 -> "3岁-7岁"
            maxHeight <= 150.0 -> "3岁-12岁"
            else -> "3岁-14岁"
        }

        return HeightSegmentMatch(
            minHeight = minHeight,
            maxHeight = maxHeight,
            matchedGroups = emptyList(),
            ageRange = ageRange,
            recommendedDirection = "不适用",
            isFullRange = false
        )
    }

    /**
     * 解析身高范围字符串
     */
    private fun parseHeightRange(heightRange: String): Pair<Double, Double> {
        // 格式: "40-150cm" 或 "40 - 150 cm"
        val cleaned = heightRange.replace("cm", "").replace(" ", "")
        val parts = cleaned.split("-")
        return Pair(parts[0].toDouble(), parts[1].toDouble())
    }

    /**
     * 根据 i-Size 规范获取 Q 系列假人
     */
    fun getISizeDummies(heightRange: String): List<String> {
        val (minHeight, maxHeight) = parseHeightRange(heightRange)
        val dummies = mutableListOf<String>()

        ISIZE_GROUPS.forEach { (heightPair, dummy) ->
            val (min, max) = heightPair
            if (maxHeight >= min && minHeight <= max) {
                if (!dummies.contains(dummy)) {
                    dummies.add(dummy)
                }
            }
        }

        return dummies
    }
}

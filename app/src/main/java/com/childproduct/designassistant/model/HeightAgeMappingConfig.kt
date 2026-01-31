package com.childproduct.designassistant.model

/**
 * 身高-年龄段-dummy 配置
 * 核心映射规则：身高40-150cm → 0-12岁（适配Q0-Q10 dummy）
 */
object HeightAgeMappingConfig {

    /**
     * 身高区间配置
     */
    data class HeightIntervalConfig(
        val minHeight: Double,      // 最小身高（cm）
        val maxHeight: Double,      // 最大身高（cm）
        val ageRange: String,       // 年龄段描述
        val minAgeMonths: Int,      // 最小年龄（月）
        val maxAgeMonths: Int,      // 最大年龄（月）
        val dummyType: CrashTestDummy,  // 对应的假人类型
        val installDirection: InstallDirection,  // 安装方向
        val standardRequirement: String  // 标准要求
    )

    /**
     * 核心映射表：身高40-150cm → 0-12岁（Q0-Q10）
     */
    val HEIGHT_INTERVALS = listOf(
        HeightIntervalConfig(
            minHeight = 40.0,
            maxHeight = 60.0,
            ageRange = "0-1岁",
            minAgeMonths = 0,
            maxAgeMonths = 12,
            dummyType = CrashTestDummy.Q0,
            installDirection = InstallDirection.REARWARD,
            standardRequirement = "后向安装强制要求、HIC≤750"
        ),
        HeightIntervalConfig(
            minHeight = 60.0,
            maxHeight = 75.0,
            ageRange = "1-2岁",
            minAgeMonths = 12,
            maxAgeMonths = 24,
            dummyType = CrashTestDummy.Q1,
            installDirection = InstallDirection.REARWARD,
            standardRequirement = "后向安装强制要求、HIC≤750"
        ),
        HeightIntervalConfig(
            minHeight = 75.0,
            maxHeight = 87.0,
            ageRange = "2-3岁",
            minAgeMonths = 24,
            maxAgeMonths = 36,
            dummyType = CrashTestDummy.Q1_5,
            installDirection = InstallDirection.REARWARD,
            standardRequirement = "后向安装强制要求、HIC≤750"
        ),
        HeightIntervalConfig(
            minHeight = 87.0,
            maxHeight = 105.0,
            ageRange = "3-4岁",
            minAgeMonths = 36,
            maxAgeMonths = 48,
            dummyType = CrashTestDummy.Q3,
            installDirection = InstallDirection.REARWARD,
            standardRequirement = "后向安装强制要求、HIC≤750"
        ),
        HeightIntervalConfig(
            minHeight = 105.0,
            maxHeight = 125.0,
            ageRange = "4-6岁",
            minAgeMonths = 48,
            maxAgeMonths = 72,
            dummyType = CrashTestDummy.Q6,
            installDirection = InstallDirection.FORWARD,
            standardRequirement = "前向安装、ISOFIX+上拉带防旋转、HIC≤1000"
        ),
        HeightIntervalConfig(
            minHeight = 125.0,
            maxHeight = 150.0,
            ageRange = "6-12岁",
            minAgeMonths = 72,
            maxAgeMonths = 144,
            dummyType = CrashTestDummy.Q10,
            installDirection = InstallDirection.FORWARD,
            standardRequirement = "前向安装、ISOFIX+上拉带防旋转、HIC≤1000"
        )
    )

    /**
     * 全年龄段配置（40-150cm）
     */
    val FULL_AGE_RANGE = AgeRangeInfo(
        ageRange = "0-12岁",
        minAgeMonths = 0,
        maxAgeMonths = 144,
        dummyRange = "Q0-Q10",
        description = "覆盖0-12岁全年龄段，适配Q0-Q10 dummy"
    )

    /**
     * 年龄段信息
     */
    data class AgeRangeInfo(
        val ageRange: String,        // 年龄段描述
        val minAgeMonths: Int,       // 最小年龄（月）
        val maxAgeMonths: Int,       // 最大年龄（月）
        val dummyRange: String,      // Dummy范围
        val description: String      // 详细描述
    )

    /**
     * 根据身高获取对应的区间配置
     */
    fun getIntervalByHeight(height: Double): HeightIntervalConfig? {
        return HEIGHT_INTERVALS.find { interval ->
            height >= interval.minHeight && height < interval.maxHeight
        }
    }

    /**
     * 根据身高范围获取所有涉及的区间
     */
    fun getIntervalsByHeightRange(minHeight: Double, maxHeight: Double): List<HeightIntervalConfig> {
        return HEIGHT_INTERVALS.filter { interval ->
            // 检查区间是否与输入范围有交集
            !(interval.maxHeight <= minHeight || interval.minHeight >= maxHeight)
        }
    }

    /**
     * 获取身高范围对应的年龄段信息
     */
    fun getAgeRangeInfo(minHeight: Double, maxHeight: Double): AgeRangeInfo {
        // 强制规则：40-150cm → 0-12岁
        return if (minHeight >= 40.0 && maxHeight <= 150.0) {
            FULL_AGE_RANGE
        } else {
            // 如果范围超出，返回对应的区间信息
            val intervals = getIntervalsByHeightRange(minHeight, maxHeight)
            if (intervals.isNotEmpty()) {
                val minAge = intervals.minOf { it.minAgeMonths }
                val maxAge = intervals.maxOf { it.maxAgeMonths }
                val dummyTypes = intervals.map { it.dummyType.name }.distinct().joinToString("-")
                AgeRangeInfo(
                    ageRange = "${minAge / 12}-${maxAge / 12}岁",
                    minAgeMonths = minAge,
                    maxAgeMonths = maxAge,
                    dummyRange = dummyTypes,
                    description = "覆盖${minAge / 12}-${maxAge / 12}岁，适配$dummyTypes dummy"
                )
            } else {
                FULL_AGE_RANGE
            }
        }
    }

    /**
     * 获取实时输入反馈信息
     */
    fun getRealtimeFeedback(minHeight: Double, maxHeight: Double): String {
        if (minHeight >= 40.0 && maxHeight <= 150.0) {
            val intervals = getIntervalsByHeightRange(minHeight, maxHeight)
            val intervalDesc = if (intervals.size == 1) {
                intervals[0].ageRange
            } else {
                "多区间"
            }
            return "当前身高对应0-12岁（$intervalDesc，适配Q0-Q10 dummy），符合UN R129 i-Size标准"
        } else if (minHeight < 40.0 || maxHeight > 150.0) {
            return "身高范围建议40-150cm，超出范围可能不符合标准要求"
        } else {
            return "请输入有效的身高范围"
        }
    }

    /**
     * 获取修正提示信息
     */
    fun getCorrectionHint(): String {
        return "检测到身高与年龄段不匹配，已将年龄段修正为0-12岁"
    }

    /**
     * 获取标准合规性说明
     */
    fun getStandardComplianceText(): String {
        return """
            符合UN R129 i-Size儿童标准，覆盖Q0-Q10 dummy，满足全年龄段安全要求。
            
            功能适配：
            • 支持多档位肩带高度调节（适配Q0-Q10肩高范围）
            • 可拆卸头枕（适配不同身高儿童）
            • 360°旋转功能（便于上下车）
            • 侧撞防护系统（满足ECE R129侧撞要求）
            • ISOFIX+上拉带固定系统（安装便捷性）
        """.trimIndent()
    }

    /**
     * 根据身高区间获取具体的标准要求
     */
    fun getStandardRequirementByHeight(height: Double): String {
        val interval = getIntervalByHeight(height)
        return interval?.standardRequirement ?: ""
    }

    /**
     * 验证身高范围是否符合标准
     */
    fun isValidHeightRange(minHeight: Double, maxHeight: Double): Boolean {
        return minHeight >= 40.0 && maxHeight <= 150.0 && minHeight < maxHeight
    }

    /**
     * 获取所有支持的Dummy类型
     */
    fun getAllDummyTypes(): List<CrashTestDummy> {
        return HEIGHT_INTERVALS.map { it.dummyType }.distinct()
    }
}

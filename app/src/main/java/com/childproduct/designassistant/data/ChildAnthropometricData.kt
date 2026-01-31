package com.childproduct.designassistant.data

/**
 * 多地区儿童人体测量学数据模型
 * 参考：GPS-028人体测量学数据文件
 * 支持：美国（US）、欧盟（EU）、中国（CN）三个地区
 */

/**
 * 地区枚举
 */
enum class Region(val displayName: String, val code: String) {
    UNITED_STATES("美国", "US"),
    EUROPEAN_UNION("欧盟", "EU"),
    CHINA("中国", "CN");

    companion object {
        fun fromCode(code: String): Region? = values().find { it.code == code }
    }
}

/**
 * 百分位类型
 */
enum class Percentile(val displayName: String, val value: Double) {
    FIFTH("5th百分位", 5.0),
    MEAN("平均值", 50.0),
    NINETY_FIFTH("95th百分位", 95.0)
}

/**
 * 单位系统
 */
enum class UnitSystem(val displayName: String) {
    METRIC("公制（cm, kg）"),
    IMPERIAL("英制（in, lb）")
}

/**
 * 儿童人体测量学数据
 * 包含20+项关键人体参数
 */
data class ChildAnthropometricData(
    // 基本信息
    val region: Region,                    // 地区
    val ageMonths: Int,                    // 年龄（月）
    val ageYears: Double,                  // 年龄（年）
    val gender: Gender,                    // 性别

    // 核心测量数据（公制单位：cm, kg）
    val stature: Double,                   // 身高（cm）
    val weight: Double,                    // 体重（kg）
    val sittingHeight: Double,             // 坐高（cm）
    val shoulderHeight: Double,            // 肩高（cm）
    val elbowHeight: Double,               // 肘高（cm）
    val hipHeight: Double,                 // 臀高（cm）
    val shoulderWidth: Double,             // 肩宽（cm）
    val hipWidth: Double,                  // 臀宽（cm）
    val chestDepth: Double,                // 胸深（cm）
    val chestWidth: Double,                // 胸宽（cm）
    val abdominalDepth: Double,            // 腹深（cm）
    val headLength: Double,                // 头长（cm）
    val headBreadth: Double,               // 头宽（cm）
    val headCircumference: Double,         // 头围（cm）
    val neckCircumference: Double,         // 颈围（cm）
    val upperArmLength: Double,            // 上臂长（cm）
    val forearmLength: Double,             // 前臂长（cm）
    val handLength: Double,                // 手长（cm）
    val thighLength: Double,               // 大腿长（cm）
    val lowerLegLength: Double,            // 小腿长（cm）
    val footLength: Double,                // 足长（cm）

    // 百分位信息
    val percentile: Percentile,            // 百分位类型
    val percentileValue: Double,           // 百分位数值

    // 数据来源
    val dataSource: String,                // 数据来源（如：CDC, WHO, 中国标准）
    val sampleSize: Int,                   // 样本量
    val measurementYear: Int,              // 测量年份
) {
    /**
     * 转换为英制单位
     */
    fun toImperial(): ChildAnthropometricData {
        return this.copy(
            stature = stature * 0.393701,              // cm → in
            weight = weight * 2.20462,                // kg → lb
            sittingHeight = sittingHeight * 0.393701,
            shoulderHeight = shoulderHeight * 0.393701,
            elbowHeight = elbowHeight * 0.393701,
            hipHeight = hipHeight * 0.393701,
            shoulderWidth = shoulderWidth * 0.393701,
            hipWidth = hipWidth * 0.393701,
            chestDepth = chestDepth * 0.393701,
            chestWidth = chestWidth * 0.393701,
            abdominalDepth = abdominalDepth * 0.393701,
            headLength = headLength * 0.393701,
            headBreadth = headBreadth * 0.393701,
            headCircumference = headCircumference * 0.393701,
            neckCircumference = neckCircumference * 0.393701,
            upperArmLength = upperArmLength * 0.393701,
            forearmLength = forearmLength * 0.393701,
            handLength = handLength * 0.393701,
            thighLength = thighLength * 0.393701,
            lowerLegLength = lowerLegLength * 0.393701,
            footLength = footLength * 0.393701
        )
    }

    /**
     * 获取年龄段标签
     */
    fun getAgeGroupLabel(): String {
        return when {
            ageMonths < 6 -> "新生儿（0-6个月）"
            ageMonths < 12 -> "婴儿（6-12个月）"
            ageMonths < 24 -> "幼儿（1-2岁）"
            ageMonths < 48 -> "学龄前（2-4岁）"
            ageMonths < 84 -> "学龄期（4-7岁）"
            ageMonths < 120 -> "学龄期（7-10岁）"
            else -> "青少年（10岁+）"
        }
    }

    /**
     * 计算BMI
     */
    fun calculateBMI(): Double {
        val heightInMeters = stature / 100.0
        return weight / (heightInMeters * heightInMeters)
    }

    /**
     * 获取关键参数摘要
     */
    fun getKeyParametersSummary(): Map<String, Double> {
        return mapOf(
            "身高" to stature,
            "体重" to weight,
            "肩宽" to shoulderWidth,
            "臀宽" to hipWidth,
            "坐高" to sittingHeight,
            "头围" to headCircumference
        )
    }
}

/**
 * 性别枚举
 */
enum class Gender(val displayName: String) {
    MALE("男"),
    FEMALE("女"),
    ALL("全部")
}

/**
 * 儿童数据查询参数
 */
data class ChildDataQuery(
    val region: Region,                    // 地区
    val ageMonths: Int? = null,            // 年龄（月）
    val gender: Gender = Gender.ALL,       // 性别
    val percentile: Percentile = Percentile.MEAN,  // 百分位
    val unitSystem: UnitSystem = UnitSystem.METRIC  // 单位系统
)

/**
 * 多地区儿童数据集
 */
data class MultiRegionChildDataSet(
    val region: Region,
    val dataSource: String,
    val data: List<ChildAnthropometricData>,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    /**
     * 根据查询条件筛选数据
     */
    fun query(query: ChildDataQuery): List<ChildAnthropometricData> {
        return data.filter { childData ->
            // 地区匹配
            val regionMatch = childData.region == query.region

            // 年龄匹配
            val ageMatch = query.ageMonths == null ||
                (childData.ageMonths >= query.ageMonths - 3 &&
                 childData.ageMonths <= query.ageMonths + 3)

            // 性别匹配
            val genderMatch = query.gender == Gender.ALL ||
                childData.gender == query.gender

            // 百分位匹配
            val percentileMatch = childData.percentile == query.percentile

            regionMatch && ageMatch && genderMatch && percentileMatch
        }.map {
            if (query.unitSystem == UnitSystem.IMPERIAL) {
                it.toImperial()
            } else {
                it
            }
        }
    }

    /**
     * 获取特定年龄段的数据
     */
    fun getDataByAgeRange(minMonths: Int, maxMonths: Int): List<ChildAnthropometricData> {
        return data.filter {
            it.ageMonths >= minMonths && it.ageMonths <= maxMonths
        }
    }

    /**
     * 获取特定百分位的数据
     */
    fun getDataByPercentile(percentile: Percentile): List<ChildAnthropometricData> {
        return data.filter { it.percentile == percentile }
    }
}

/**
 * 地区对比数据
 */
data class RegionComparisonData(
    val ageMonths: Int,
    val usData: ChildAnthropometricData?,
    val euData: ChildAnthropometricData?,
    val cnData: ChildAnthropometricData?,
    val percentile: Percentile = Percentile.MEAN
) {
    /**
     * 获取身高对比
     */
    fun getStatureComparison(): Map<String, Double> {
        val map = mutableMapOf<String, Double>()
        usData?.let { map["美国"] = it.stature }
        euData?.let { map["欧盟"] = it.stature }
        cnData?.let { map["中国"] = it.stature }
        return map
    }

    /**
     * 获取体重对比
     */
    fun getWeightComparison(): Map<String, Double> {
        val map = mutableMapOf<String, Double>()
        usData?.let { map["美国"] = it.weight }
        euData?.let { map["欧盟"] = it.weight }
        cnData?.let { map["中国"] = it.weight }
        return map
    }

    /**
     * 获取肩宽对比
     */
    fun getShoulderWidthComparison(): Map<String, Double> {
        val map = mutableMapOf<String, Double>()
        usData?.let { map["美国"] = it.shoulderWidth }
        euData?.let { map["欧盟"] = it.shoulderWidth }
        cnData?.let { map["中国"] = it.shoulderWidth }
        return map
    }

    /**
     * 获取臀宽对比
     */
    fun getHipWidthComparison(): Map<String, Double> {
        val map = mutableMapOf<String, Double>()
        usData?.let { map["美国"] = it.hipWidth }
        euData?.let { map["欧盟"] = it.hipWidth }
        cnData?.let { map["中国"] = it.hipWidth }
        return map
    }
}

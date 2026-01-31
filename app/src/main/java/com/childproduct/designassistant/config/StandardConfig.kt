package com.childproduct.designassistant.config

import com.childproduct.designassistant.model.CrashTestDummy

/**
 * 标准化配置常量（统一管理所有硬编码参数，支持配置文件扩展）
 *
 * 优化目标：
 * - 抽离硬编码参数，便于维护和更新
 * - 版本化管理标准配置，支持动态扩展
 * - 统一安全阈值和合规标准，避免散落各处
 */
object StandardConfig {
    // ========== 版本配置 ==========
    const val STANDARD_VERSION = "UN R129:2024 / GB 27887-2024"
    const val FMVSS_VERSION = "FMVSS 213:2023"

    // ========== 身高-假人-年龄段映射配置 ==========
    data class HeightMappingConfig(
        val heightRange: String,
        val ageRange: String,
        val dummyType: CrashTestDummy,
        val installDirection: String,
        val hicLimit: String,
        val chestAccelerationLimit: String
    )

    /**
     * 不可变映射表（避免运行时修改）
     * 符合 UN R129 / GB 27887-2024 标准
     */
    val HEIGHT_DUMMY_MAPPING: Map<String, HeightMappingConfig> = mapOf(
        "40-60cm" to HeightMappingConfig(
            heightRange = "40-60cm",
            ageRange = "0-1岁",
            dummyType = CrashTestDummy.Q0,
            installDirection = "后向安装",
            hicLimit = "≤390",
            chestAccelerationLimit = "≤55g"
        ),
        "60-75cm" to HeightMappingConfig(
            heightRange = "60-75cm",
            ageRange = "1-2岁",
            dummyType = CrashTestDummy.Q1,
            installDirection = "后向安装",
            hicLimit = "≤390",
            chestAccelerationLimit = "≤55g"
        ),
        "75-87cm" to HeightMappingConfig(
            heightRange = "75-87cm",
            ageRange = "2-3岁",
            dummyType = CrashTestDummy.Q1_5,
            installDirection = "后向安装",
            hicLimit = "≤570",
            chestAccelerationLimit = "≤55g"
        ),
        "87-105cm" to HeightMappingConfig(
            heightRange = "87-105cm",
            ageRange = "3-4岁",
            dummyType = CrashTestDummy.Q3,
            installDirection = "后向安装",
            hicLimit = "≤1000",
            chestAccelerationLimit = "≤60g"
        ),
        "105-125cm" to HeightMappingConfig(
            heightRange = "105-125cm",
            ageRange = "4-6岁",
            dummyType = CrashTestDummy.Q6,
            installDirection = "前向安装",
            hicLimit = "≤1000",
            chestAccelerationLimit = "≤60g"
        ),
        "125-150cm" to HeightMappingConfig(
            heightRange = "125-150cm",
            ageRange = "6-12岁",
            dummyType = CrashTestDummy.Q10,
            installDirection = "前向安装",
            hicLimit = "≤1000",
            chestAccelerationLimit = "≤60g"
        ),
        "40-150cm" to HeightMappingConfig(
            heightRange = "40-150cm",
            ageRange = "0-12岁",
            dummyType = CrashTestDummy.Q0, // 兜底标识，实际为全假人
            installDirection = "双向安装",
            hicLimit = "≤390（Q0-Q1.5）/≤1000（Q3-Q10）",
            chestAccelerationLimit = "≤55g（Q0-Q1.5）/≤60g（Q3-Q10）"
        )
    )

    // ========== 安全阈值配置（去重+标准化） ==========
    val SAFETY_THRESHOLDS = mapOf(
        "HIC极限值" to "${HEIGHT_DUMMY_MAPPING["40-150cm"]?.hicLimit}",
        "胸部加速度" to "${HEIGHT_DUMMY_MAPPING["40-150cm"]?.chestAccelerationLimit}",
        "颈部张力极限" to "≤1800N（Q3-Q10）",
        "颈部压缩极限" to "≤2200N（Q0-Q1.5）/≤2500N（Q3-Q10）",
        "头部位移极限" to "≤550mm（全假人）",
        "膝部位移极限" to "≤650mm（全假人）",
        "胸部压缩极限" to "≤45mm（Q0-Q1.5）/≤52mm（Q3-Q10）",
        "阻燃性能" to "符合$FMVSS_VERSION（燃烧速度≤4英寸/分钟）"
    )

    // ========== 合规标准配置 ==========
    val COMPLIANCE_STANDARDS = listOf(
        STANDARD_VERSION,
        FMVSS_VERSION,
        "EN 71-3:2021（材质安全）",
        "ISO 8124-1:2020（机械安全）"
    )

    // ========== 推荐材料配置（含性能参数） ==========
    val RECOMMENDED_MATERIALS = listOf(
        "主体框架：食品级PP塑料（耐温-30℃~80℃，抗冲击强度≥20kJ/m²）",
        "填充层：高回弹海绵（压缩回弹率≥90%，无异味，密度30kg/m³）",
        "约束部件：高强度安全带织带（断裂强度≥11000N，耐磨后强度保留率≥75%）",
        "支撑结构：铝合金支架（盐雾测试50小时无腐蚀，抗拉强度≥300MPa）",
        "面料：阻燃聚酯纤维（符合$FMVSS_VERSION，燃烧速度≤4英寸/分钟）"
    )

    // ========== 安全注意事项配置 ==========
    val SAFETY_NOTES = listOf(
        "防吞咽风险：所有可拆卸部件尺寸≥3.5cm（ISO 8124-2:2020要求）",
        "材质安全：使用食品级PP塑料，无甲醛/重金属残留（GB 6675.4/EN 71-3）",
        "边缘安全：产品边缘做圆角处理（R≥2mm），无尖锐突出物（ISO 8124-1）",
        "防火阻燃：面料通过$FMVSS_VERSION认证（燃烧速度≤4英寸/分钟）",
        "安装警示：必须严格按照ISOFIX/安全带固定说明安装（$STANDARD_VERSION §5.2）"
    )

    // ========== 辅助方法 ==========

    /**
     * 根据身高范围获取配置
     */
    fun getHeightConfig(heightRange: String): HeightMappingConfig? {
        return HEIGHT_DUMMY_MAPPING[heightRange]
    }

    /**
     * 获取安全阈值
     */
    fun getSafetyThreshold(thresholdKey: String): String? {
        return SAFETY_THRESHOLDS[thresholdKey]
    }

    /**
     * 验证身高范围是否符合标准
     */
    fun isValidHeightRange(heightRange: String): Boolean {
        return HEIGHT_DUMMY_MAPPING.containsKey(heightRange)
    }
}

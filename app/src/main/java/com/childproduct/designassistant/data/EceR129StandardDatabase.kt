package com.childproduct.designassistant.data

import com.childproduct.designassistant.model.*

/**
 * ECE R129 / GB 27887 专业参数数据库
 * 包含标准条款、设计参数、材料标准、测试矩阵等
 */

/**
 * 核心设计参数（关联标准条款）
 */
data class CoreDesignParameter(
    val parameterName: String,        // 参数名称
    val value: String,                // 参数值/范围
    val unit: String,                 // 单位
    val standardClause: String,       // 关联标准条款
    val description: String           // 参数说明
)

/**
 * 材料标准标注
 */
data class MaterialStandard(
    val materialName: String,         // 材料名称
    val standard: String,             // 符合的标准
    val requirement: String,          // 标准要求
    val application: String           // 应用部位
)

/**
 * 测试矩阵项
 */
data class TestMatrixItem(
    val testName: String,            // 测试名称
    val standardClause: String,      // 标准条款
    val testMethod: String,          // 测试方法
    val dummyType: String,           // 假人类型
    val acceptanceCriteria: String   // 合格标准
)

/**
 * 尺寸阈值
 */
data class DimensionalThreshold(
    val dimensionType: String,       // 尺寸类型
    val limit: String,               // 限制值
    val unit: String,                // 单位
    val standardClause: String,      // 标准条款
    val description: String          // 说明
)

/**
 * 产品配置信息
 */
data class ProductConfigurationInfo(
    val configType: String,          // 配置类型
    val description: String,         // 配置描述
    val standardClause: String?,     // 标准条款（可选）
    val installationRequirement: String // 安装要求
)

/**
 * ECE R129 / GB 27887 标准数据库
 */
class EceR129StandardDatabase {

    companion object {
        // 核心设计参数
        val CORE_PARAMETERS = listOf(
            CoreDesignParameter(
                parameterName = "头托调节范围",
                value = "10-30",
                unit = "cm",
                standardClause = "ECE R129 §5.4.2",
                description = "头托可调节范围，需覆盖40-150cm身高区间"
            ),
            CoreDesignParameter(
                parameterName = "ISOFIX接口间距",
                value = "280",
                unit = "mm",
                standardClause = "GB 27887-2024 §5.5",
                description = "ISOFIX锚点中心距，允许偏差±5mm"
            ),
            CoreDesignParameter(
                parameterName = "ISOFIX上拉带挂钩高度",
                value = "500-1200",
                unit = "mm",
                standardClause = "ECE R129 §5.5.1",
                description = "上拉带挂钩相对于锚点的高度范围"
            ),
            CoreDesignParameter(
                parameterName = "支撑腿长度",
                value = "280-450",
                unit = "mm",
                standardClause = "ECE R129 §5.5.3",
                description = "支撑腿可调节长度范围"
            ),
            CoreDesignParameter(
                parameterName = "侧翼内宽",
                value = "≥380",
                unit = "mm",
                standardClause = "ECE R129 §5.3.3",
                description = "侧面碰撞防护结构最小宽度"
            ),
            CoreDesignParameter(
                parameterName = "座椅靠背角度（后向）",
                value = "135-150",
                unit = "度",
                standardClause = "ECE R129 §5.4.1",
                description = "后向安装时座椅靠背角度范围"
            ),
            CoreDesignParameter(
                parameterName = "座椅靠背角度（前向）",
                value = "15-30",
                unit = "度",
                standardClause = "ECE R129 §5.4.1",
                description = "前向安装时座椅靠背角度范围（相对于垂直线）"
            ),
            CoreDesignParameter(
                parameterName = "安全带织带宽度",
                value = "25-30",
                unit = "mm",
                standardClause = "GB 6095-2021 §4.2",
                description = "安全带织带标准宽度"
            ),
            CoreDesignParameter(
                parameterName = "安全带锁止强度",
                value = "≥5",
                unit = "kN",
                standardClause = "GB 6095-2021 §4.3",
                description = "安全带锁止机构最小承受力"
            )
        )

        // 材料标准
        val MATERIAL_STANDARDS = listOf(
            MaterialStandard(
                materialName = "PP塑料（座椅主体）",
                standard = "GB 6675.4-2014",
                requirement = "可迁移元素限值：铅≤90mg/kg，镉≤75mg/kg",
                application = "座椅主体、头托、底座"
            ),
            MaterialStandard(
                materialName = "ABS塑料（结构部件）",
                standard = "GB 6675.4-2014",
                requirement = "可迁移元素限值：符合GB 6675所有要求",
                application = "底座骨架、连接件"
            ),
            MaterialStandard(
                materialName = "EPS吸能材料",
                standard = "GB/T 10801.1-2021",
                requirement = "密度≥30kg/m³，抗压强度≥150kPa",
                application = "侧面防护、头部保护"
            ),
            MaterialStandard(
                materialName = "EPP吸能材料",
                standard = "GB/T 10801.2-2021",
                requirement = "密度≥45kg/m³，能量吸收≥30%",
                application = "侧翼、后背板"
            ),
            MaterialStandard(
                materialName = "安全带织带",
                standard = "GB 6095-2021",
                requirement = "断裂强度≥5kN，延伸率≤10%（在5kN力作用下）",
                application = "五点式安全带、肩带"
            ),
            MaterialStandard(
                materialName = "安全带插扣",
                standard = "GB 6675.3-2014",
                requirement = "锁止力≥5kN，解锁力≤300N",
                application = "安全带卡扣"
            ),
            MaterialStandard(
                materialName = "座椅面料",
                standard = "GB 18401 B类",
                requirement = "甲醛含量≤75mg/kg，pH值4.0-7.5",
                application = "座椅表面、头枕套"
            ),
            MaterialStandard(
                materialName = "阻燃面料",
                standard = "GB 6675.3-2014",
                requirement = "垂直燃烧速度≤100mm/min",
                application = "易燃区域面料"
            )
        )

        // 动态测试矩阵（参考ROADMATE 360）
        // 基于输入参数和输出参数的详细测试用例
        fun getDynamicTestMatrix(heightRange: String): List<TestMatrixDisplayItem> {
            val mapper = HeightAgeGroupMapper()
            val heightMatch = mapper.matchHeightRange(heightRange,
                com.childproduct.designassistant.model.ProductType.CHILD_SAFETY_SEAT)
            
            val generator = DynamicTestMatrixGenerator()
            val testMatrix = generator.generateTestMatrix(
                productName = "儿童安全座椅",
                productId = "CHILD_SAFETY_SEAT",
                standard = "ECE R129",
                heightRange = heightRange,
                productType = "CHILD_SAFETY_SEAT"
            )
            
            return testMatrix.testCases.map { it.toDisplayItem() }
        }
        
        // 测试矩阵（简化版，用于显示，基于R129r4e §6.6.4.3.1 Table 4）
        val TEST_MATRIX = listOf(
            TestMatrixItem(
                testName = "正面撞击测试（Q0 + 后向 + 直立）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点，带支撑腿",
                dummyType = "Q0（新生儿，≤60cm）",
                acceptanceCriteria = "HPC≤600，头部加速度3ms≤75g，胸部加速度3ms≤55g"
            ),
            TestMatrixItem(
                testName = "正面撞击测试（Q0 + 后向 + 倾斜）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点，带支撑腿，靠背倾斜",
                dummyType = "Q0（新生儿，≤60cm）",
                acceptanceCriteria = "HPC≤600，头部加速度3ms≤75g，胸部加速度3ms≤55g"
            ),
            TestMatrixItem(
                testName = "后向翻滚测试（Q0）",
                standardClause = "ECE R129 Rev.4 §7.1.3.2",
                testMethod = "30km/h 后向撞击，ΔV=32km/h，ISOFIX 3点，带支撑腿",
                dummyType = "Q0（新生儿，≤60cm）",
                acceptanceCriteria = "假人加速度≤25g，座椅无结构性变形"
            ),
            TestMatrixItem(
                testName = "正面撞击测试（Q1.5 + 前向）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点，带支撑腿",
                dummyType = "Q1.5（18个月，75-87cm）",
                acceptanceCriteria = "HPC≤600，头部加速度3ms≤75g，胸部加速度3ms≤55g，腹部压力≤1.2bar"
            ),
            TestMatrixItem(
                testName = "侧面碰撞测试（Q1.5）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.5.1",
                testMethod = "32km/h 侧面撞击可变形障碍物，门侵入深度，ISOFIX 3点",
                dummyType = "Q1.5（18个月，75-87cm）",
                acceptanceCriteria = "头部偏移量≤150mm，头部加速度3ms≤75g，头部无接触车门"
            ),
            TestMatrixItem(
                testName = "正面撞击测试（Q3 + 前向 + 直立）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点，带支撑腿",
                dummyType = "Q3（3岁，87-105cm）",
                acceptanceCriteria = "HPC≤800，头部加速度3ms≤80g，胸部加速度3ms≤55g，腹部压力≤1.0bar"
            ),
            TestMatrixItem(
                testName = "正面撞击测试（Q3 + 前向 + 倾斜）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点，带支撑腿，靠背倾斜",
                dummyType = "Q3（3岁，87-105cm）",
                acceptanceCriteria = "HPC≤800，头部加速度3ms≤80g，胸部加速度3ms≤55g，腹部压力≤1.0bar"
            ),
            TestMatrixItem(
                testName = "侧面碰撞测试（Q3）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.5.1",
                testMethod = "32km/h 侧面撞击可变形障碍物，门侵入深度，ISOFIX 3点",
                dummyType = "Q3（3岁，87-105cm）",
                acceptanceCriteria = "头部偏移量≤150mm，头部加速度3ms≤80g，头部无接触车门"
            ),
            TestMatrixItem(
                testName = "正面撞击测试（Q6 + 前向）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点，带支撑腿",
                dummyType = "Q6（6岁，105-125cm）",
                acceptanceCriteria = "HPC≤800，头部加速度3ms≤80g，胸部加速度3ms≤55g，腹部压力≤1.0bar"
            ),
            TestMatrixItem(
                testName = "侧面碰撞测试（Q6）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.5.1",
                testMethod = "32km/h 侧面撞击可变形障碍物，门侵入深度，ISOFIX 3点",
                dummyType = "Q6（6岁，105-125cm）",
                acceptanceCriteria = "头部偏移量≤150mm，头部加速度3ms≤80g，头部无接触车门"
            ),
            TestMatrixItem(
                testName = "正面撞击测试（Q10 + 前向）",
                standardClause = "ECE R129 Rev.4 §7.1.3, §6.6.4.3.1",
                testMethod = "50km/h 正面撞击刚性障碍物，ΔV=52km/h，ISOFIX 3点",
                dummyType = "Q10（10岁，125-150cm）",
                acceptanceCriteria = "HPC≤800，头部加速度3ms≤80g，胸部加速度3ms≤55g，腹部压力≤1.2bar"
            ),
            TestMatrixItem(
                testName = "ISOFIX强度测试",
                standardClause = "GB 27887-2024 §6.2",
                testMethod = "施加10kN拉力至ISOFIX锚点",
                dummyType = "无",
                acceptanceCriteria = "ISOFIX连接件无变形，位移 ≤ 15mm"
            ),
            TestMatrixItem(
                testName = "支撑腿强度测试",
                standardClause = "ECE R129 §5.5.3",
                testMethod = "施加2.5kN垂直载荷至支撑腿",
                dummyType = "无",
                acceptanceCriteria = "支撑腿无断裂，偏转 ≤ 10mm"
            ),
            TestMatrixItem(
                testName = "安全带锁止测试",
                standardClause = "GB 6095-2021 §5.2",
                testMethod = "施加5kN拉力至安全带",
                dummyType = "无",
                acceptanceCriteria = "锁止机构无解锁，延伸率 ≤ 10%"
            ),
            TestMatrixItem(
                testName = "头托调节耐久性测试",
                standardClause = "ECE R129 §5.4.2",
                testMethod = "反复调节头托10000次",
                dummyType = "无",
                acceptanceCriteria = "调节机构无卡滞，档位准确"
            ),
            TestMatrixItem(
                testName = "材料燃烧性测试",
                standardClause = "GB 6675.3-2014 §5.3",
                testMethod = "垂直燃烧测试",
                dummyType = "无",
                acceptanceCriteria = "燃烧速率 ≤ 100mm/min"
            ),
            TestMatrixItem(
                testName = "可迁移元素测试",
                standardClause = "GB 6675.4-2014 §4",
                testMethod = "ICP-MS 检测",
                dummyType = "无",
                acceptanceCriteria = "符合GB 6675限值要求"
            ),
            TestMatrixItem(
                testName = "材料燃烧性测试",
                standardClause = "GB 6675.3-2014 §5.3",
                testMethod = "垂直燃烧测试",
                dummyType = "无",
                acceptanceCriteria = "燃烧速率≤100mm/min"
            ),
            TestMatrixItem(
                testName = "可迁移元素测试",
                standardClause = "GB 6675.4-2014 §4",
                testMethod = "ICP-MS 检测",
                dummyType = "无",
                acceptanceCriteria = "符合GB 6675限值要求"
            )
        )

        // 尺寸阈值
        val DIMENSIONAL_THRESHOLDS = listOf(
            DimensionalThreshold(
                dimensionType = "外宽度上限",
                limit = "≤50",
                unit = "cm",
                standardClause = "ECE R129 Envelope要求",
                description = "i-Size 包络宽度限制，确保车辆安装兼容性"
            ),
            DimensionalThreshold(
                dimensionType = "外长度上限",
                limit = "≤75",
                unit = "cm",
                standardClause = "ECE R129 Envelope要求",
                description = "i-Size 包络长度限制，确保车辆安装兼容性"
            ),
            DimensionalThreshold(
                dimensionType = "外高度上限",
                limit = "≤85",
                unit = "cm",
                standardClause = "ECE R129 Envelope要求",
                description = "i-Size 包络高度限制，确保车辆安装兼容性"
            ),
            DimensionalThreshold(
                dimensionType = "ISOFIX接口间距",
                limit = "280±5",
                unit = "mm",
                standardClause = "GB 27887-2024 §5.5",
                description = "ISOFIX锚点标准间距"
            ),
            DimensionalThreshold(
                dimensionType = "座椅内宽",
                limit = "≥38",
                unit = "cm",
                standardClause = "ECE R129 §5.2",
                description = "座椅内部最小宽度，确保儿童舒适性"
            )
        )

        // 产品配置
        val PRODUCT_CONFIGURATIONS = listOf(
            ProductConfigurationInfo(
                configType = "ISOFIX接口",
                description = "ISOFIX硬连接接口，提供稳固的车辆固定",
                standardClause = "ECE R129 §5.5.1 / §5.5.3",
                installationRequirement = "适配带ISOFIX锚点的乘用车（2006年后欧盟车辆，2012年后中国车辆）"
            ),
            ProductConfigurationInfo(
                configType = "可伸缩支撑腿",
                description = "地面支撑结构，提高座椅稳定性",
                standardClause = "ECE R129 §5.5.3",
                installationRequirement = "适配地板间隙≥5cm的车辆，支撑腿长度可调280-450mm"
            ),
            ProductConfigurationInfo(
                configType = "顶部系带挂钩",
                description = "上拉带连接车辆顶部系带点",
                standardClause = "ECE R129 §5.5.1",
                installationRequirement = "适配车辆后排座椅顶部有系带锚点的车型"
            ),
            ProductConfigurationInfo(
                configType = "侧撞防护装置（SIP）",
                description = "可调节的侧面碰撞保护块",
                standardClause = "ECE R129 §5.3.3",
                installationRequirement = "需根据车门间隙调整侧翼展开角度，避免影响车门关闭"
            ),
            ProductConfigurationInfo(
                configType = "360°旋转机构",
                description = "座椅可360度旋转，方便抱娃进出",
                standardClause = "ECE R129 §5.2（需通过动态测试）",
                installationRequirement = "需确保旋转机构的锁止强度≥5kN"
            ),
            ProductConfigurationInfo(
                configType = "头托高度调节",
                description = "多档位头托高度调节，适应不同身高儿童",
                standardClause = "ECE R129 §5.4.2",
                installationRequirement = "调节范围10-30cm，建议分12-15档"
            ),
            ProductConfigurationInfo(
                configType = "靠背角度调节",
                description = "座椅靠背角度可调（后向135-150°，前向15-30°）",
                standardClause = "ECE R129 §5.4.1",
                installationRequirement = "角度锁止机构需通过耐久性测试10000次"
            )
        )
    }

    /**
     * 根据产品类型获取核心参数
     */
    fun getCoreParameters(productType: String): List<CoreDesignParameter> {
        return when (productType) {
            "儿童安全座椅" -> CORE_PARAMETERS
            "婴儿推车" -> getStrollerParameters()
            "儿童高脚椅" -> getHighChairParameters()
            else -> emptyList()
        }
    }

    /**
     * 根据产品类型获取材料标准
     */
    fun getMaterialStandards(productType: String): List<MaterialStandard> {
        return when (productType) {
            "儿童安全座椅" -> MATERIAL_STANDARDS
            "婴儿推车" -> getStrollerMaterialStandards()
            "儿童高脚椅" -> getHighChairMaterialStandards()
            else -> emptyList()
        }
    }

    /**
     * 根据产品类型获取测试矩阵
     */
    fun getTestMatrix(productType: String): List<TestMatrixItem> {
        return when (productType) {
            "儿童安全座椅" -> TEST_MATRIX
            "婴儿推车" -> getStrollerTestMatrix()
            "儿童高脚椅" -> getHighChairTestMatrix()
            else -> emptyList()
        }
    }

    /**
     * 根据产品类型获取尺寸阈值
     */
    fun getDimensionalThresholds(productType: String): List<DimensionalThreshold> {
        return when (productType) {
            "儿童安全座椅" -> DIMENSIONAL_THRESHOLDS
            "婴儿推车" -> getStrollerDimensionalThresholds()
            "儿童高脚椅" -> getHighChairDimensionalThresholds()
            else -> emptyList()
        }
    }

    /**
     * 根据产品类型获取产品配置
     */
    fun getProductConfigurationInfos(productType: String): List<ProductConfigurationInfo> {
        return when (productType) {
            "儿童安全座椅" -> PRODUCT_CONFIGURATIONS
            "婴儿推车" -> getStrollerConfigurations()
            "儿童高脚椅" -> getHighChairConfigurations()
            else -> emptyList()
        }
    }

    // ===== 婴儿推车专用数据 =====

    private fun getStrollerParameters(): List<CoreDesignParameter> = listOf(
        CoreDesignParameter("推车宽度", "≤50", "cm", "EN 1888 §5.2", "确保通过标准门洞"),
        CoreDesignParameter("折叠尺寸", "≤50×30×80", "cm", "EN 1888 §5.2", "便于存放和携带"),
        CoreDesignParameter("靠背角度范围", "110-175", "度", "EN 1888 §5.3", "坐姿、半躺、全躺三种状态"),
        CoreDesignParameter("制动强度", "≥1.5", "g", "EN 1888 §7.3", "15°斜坡制动性能要求")
    )

    private fun getStrollerMaterialStandards(): List<MaterialStandard> = listOf(
        MaterialStandard("铝合金车架", "EN 71-3", "可迁移元素限值符合EN 71-3", "车架结构"),
        MaterialStandard("织物面料", "GB 18401 B类", "甲醛≤75mg/kg", "座椅面料、遮阳篷")
    )

    private fun getStrollerTestMatrix(): List<TestMatrixItem> = listOf(
        TestMatrixItem("稳定性测试", "EN 1888 §7.3", "15°斜坡制动", "无", "无滑动、无倾倒"),
        TestMatrixItem("折叠锁定测试", "ASTM F833 §5.7", "折叠-展开500次", "无", "无卡滞、无意外解锁")
    )

    private fun getStrollerDimensionalThresholds(): List<DimensionalThreshold> = listOf(
        DimensionalThreshold("推车宽度", "≤50", "cm", "EN 1888", "通过标准门洞（80cm）"),
        DimensionalThreshold("折叠尺寸", "≤50×30×80", "cm", "EN 1888", "便于携带存放")
    )

    private fun getStrollerConfigurations(): List<ProductConfigurationInfo> = listOf(
        ProductConfigurationInfo("四轮避震", "四轮独立避震系统", null, "减震行程≥20mm"),
        ProductConfigurationInfo("一键折叠", "单手折叠机构", null, "折叠操作力≤50N")
    )

    // ===== 儿童高脚椅专用数据 =====

    private fun getHighChairParameters(): List<CoreDesignParameter> = listOf(
        CoreDesignParameter("座面高度", "45-65", "cm", "GB 28007-2011 §4.1", "适配标准餐桌高度"),
        CoreDesignParameter("安全带间距", "15-20", "cm", "ISO 8124-3 §4.2", "防止儿童滑落"),
        CoreDesignParameter("脚踏板承重", "≥50", "kg", "ISO 8124-3 §4.3", "确保结构强度")
    )

    private fun getHighChairMaterialStandards(): List<MaterialStandard> = listOf(
        MaterialStandard("食品级PP塑料", "GB 4806.7", "符合食品接触材料标准", "托盘、餐盘"),
        MaterialStandard("ABS塑料", "GB 6675.4-2014", "可迁移元素限值符合", "椅腿、底座")
    )

    private fun getHighChairTestMatrix(): List<TestMatrixItem> = listOf(
        TestMatrixItem("稳定性测试", "ISO 8124-3", "倾斜测试", "无", "倾斜角度<10°"),
        TestMatrixItem("强度测试", "GB 28007", "静载测试", "无", "无明显变形")
    )

    private fun getHighChairDimensionalThresholds(): List<DimensionalThreshold> = listOf(
        DimensionalThreshold("座面高度", "45-65", "cm", "GB 28007-2011", "适配标准餐桌高度75cm"),
        DimensionalThreshold("整体高度", "≤95", "cm", "ISO 8124-3", "避免重心过高")
    )

    private fun getHighChairConfigurations(): List<ProductConfigurationInfo> = listOf(
        ProductConfigurationInfo("五点式安全带", "防止儿童滑落", "ISO 8124-3", "肩带、腰带、跨带五点固定"),
        ProductConfigurationInfo("高度调节", "多档位高度调节", null, "调节范围20cm，至少5档")
    )
}

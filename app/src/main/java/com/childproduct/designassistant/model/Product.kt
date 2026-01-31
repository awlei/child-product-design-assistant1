package com.childproduct.designassistant.model

import com.childproduct.designassistant.config.StandardConfig
import com.childproduct.designassistant.helper.SchemeOptimizer
import com.childproduct.designassistant.validator.Validator
import com.childproduct.designassistant.validator.ValidatorFactory

/**
 * 产品抽象接口（支持扩展不同儿童产品类型）
 *
 * 优化目标：
 * - 抽象产品接口，支持快速扩展新产品类型
 * - 每个产品类型实现自己的验证器和方案生成逻辑
 * - 产品工厂模式统一创建实例，降低耦合
 */
interface Product {
    val productType: String
    val validator: Validator<SchemeOptimizer.UserInput>
    fun generateScheme(input: SchemeOptimizer.UserInput): ChildProductDesignScheme

    /**
     * 获取产品支持的身高范围
     */
    fun getSupportedHeightRanges(): List<String>

    /**
     * 获取产品支持的标准列表
     */
    fun getSupportedStandards(): List<String>
}

/**
 * 儿童安全座椅实现（当前核心产品）
 */
class ChildSafetySeat : Product {
    override val productType: String = "儿童安全座椅"

    override val validator: Validator<SchemeOptimizer.UserInput> =
        ValidatorFactory.productInputValidator()

    override fun getSupportedHeightRanges(): List<String> = listOf(
        "40-60cm", "60-75cm", "75-87cm", "87-105cm",
        "105-125cm", "125-150cm", "40-150cm"
    )

    override fun getSupportedStandards(): List<String> = listOf(
        StandardConfig.STANDARD_VERSION,
        StandardConfig.FMVSS_VERSION,
        "EN 71-3:2021",
        "ISO 8124-1:2020"
    )

    override fun generateScheme(input: SchemeOptimizer.UserInput): ChildProductDesignScheme {
        // 复用现有优化后的生成逻辑
        return SchemeOptimizer.generateOptimizedScheme(input)
    }
}

/**
 * 婴儿推车实现（扩展示例）
 */
class BabyStroller : Product {
    override val productType: String = "婴儿推车"

    override val validator: Validator<SchemeOptimizer.UserInput> =
        ValidatorFactory.productInputValidator()

    override fun getSupportedHeightRanges(): List<String> = listOf(
        "40-60cm", "60-75cm", "75-87cm", "40-87cm"
    )

    override fun getSupportedStandards(): List<String> = listOf(
        "EN 1888-2:2018",
        "GB 14748-2020",
        "ISO 8124-1:2020"
    )

    override fun generateScheme(input: SchemeOptimizer.UserInput): ChildProductDesignScheme {
        // 婴儿推车专属逻辑
        val heightConfig = StandardConfig.getHeightConfig(input.heightRange)
            ?: StandardConfig.HEIGHT_DUMMY_MAPPING["40-60cm"]!!

        return ChildProductDesignScheme.builder(
            productType = productType,
            heightRange = input.heightRange
        )
            .ageRange(heightConfig.ageRange)
            .designTheme("${input.themeKeyword} - 婴儿推车专属设计")
            .installMethodDesc("一键折叠+五点式安全带")
            .coreFeatures(listOf(
                "易安装性：一键折叠（收纳尺寸≤50×30×20cm）",
                "安装方向：前向安装（符合EN 1888-2:2018要求）",
                "安全性：符合${getSupportedStandards().joinToString("、")}",
                "舒适性：配备悬挂避震系统，乘坐平稳",
                "便携性：轻量化设计（整车重量≤8kg）"
            ))
            .dummyType("Q0-Q1.5假人（婴儿推车专用）")
            .safetyThresholds(mapOf(
                "制动性能" to "在10°斜面上不滑行（GB 14748-2020 §5.4）",
                "折叠机构安全" to "手指探针不能插入（EN 1888-2 §4.2）",
                "静态强度" to "座椅承受100kg重量不变形",
                "锁定装置" to "手动释放力≥50N"
            ))
            .safetyNotes(listOf(
                "刹车系统：双轮同步刹车（防止侧翻）",
                "面料安全：可水洗+无荧光剂（符合EN 71-3）",
                "折叠安全：折叠机构安全防夹（R≥2mm）",
                "使用警示：禁止在台阶、斜坡上使用",
                "载重限制：最大承重15kg"
            ))
            .build()
    }
}

/**
 * 儿童餐椅实现（扩展示例）
 */
class ChildHighChair : Product {
    override val productType: String = "儿童餐椅"

    override val validator: Validator<SchemeOptimizer.UserInput> =
        ValidatorFactory.productInputValidator()

    override fun getSupportedHeightRanges(): List<String> = listOf(
        "60-75cm", "75-87cm", "87-105cm", "60-105cm"
    )

    override fun getSupportedStandards(): List<String> = listOf(
        "GB 22793-2008",
        "EN 14988:2017",
        "ISO 8124-1:2020"
    )

    override fun generateScheme(input: SchemeOptimizer.UserInput): ChildProductDesignScheme {
        // 儿童餐椅专属逻辑
        val heightConfig = StandardConfig.getHeightConfig(input.heightRange)
            ?: StandardConfig.HEIGHT_DUMMY_MAPPING["75-87cm"]!!

        return ChildProductDesignScheme.builder(
            productType = productType,
            heightRange = input.heightRange
        )
            .ageRange(heightConfig.ageRange)
            .designTheme("${input.themeKeyword} - 儿童餐椅专属设计")
            .installMethodDesc("可调节高度+五点式安全带")
            .coreFeatures(listOf(
                "易安装性：快速拆装（无需工具，便于清洁）",
                "安装方向：固定式安装（符合GB 22793-2008要求）",
                "安全性：符合${getSupportedStandards().joinToString("、")}",
                "舒适性：座椅角度多档可调（3-5档位）",
                "稳定性：防倾倒设计（重心降低+防滑脚垫）"
            ))
            .dummyType("Q1-Q3假人（儿童餐椅专用）")
            .safetyThresholds(mapOf(
                "稳定性测试" to "倾斜10°不倾倒（GB 22793-2008 §5.2）",
                "锁定装置" to "锁定力≥50N",
                "静态强度" to "托盘承重≥30kg",
                "背板强度" to "背板承重≥60kg"
            ))
            .safetyNotes(listOf(
                "安全带系统：五点式安全带（防止滑落）",
                "托盘安全：边缘圆角处理（R≥2mm）",
                "防滑设计：底部防滑脚垫（防止滑动）",
                "年龄限制：建议6个月-3岁使用",
                "载重限制：最大承重15kg",
                "使用警示：必须有成人监护"
            ))
            .build()
    }
}

/**
 * 产品工厂（创建不同产品实例）
 *
 * 优化目标：
 * - 统一创建逻辑，降低耦合
 * - 支持产品类型扩展
 * - 提供产品列表查询
 */
object ProductFactory {
    private val products = mapOf(
        "儿童安全座椅" to ChildSafetySeat(),
        "safety seat" to ChildSafetySeat(),
        "婴儿推车" to BabyStroller(),
        "stroller" to BabyStroller(),
        "儿童餐椅" to ChildHighChair(),
        "high chair" to ChildHighChair()
    )

    /**
     * 创建产品实例
     * @param productType  产品类型名称
     * @return  产品实例
     * @throws IllegalArgumentException  如果产品类型不支持
     */
    fun createProduct(productType: String): Product {
        return products[productType.lowercase()]
            ?: throw IllegalArgumentException(
                "不支持的产品类型：$productType。支持的产品类型：${getSupportedProductTypes().joinToString("、")}"
            )
    }

    /**
     * 获取支持的产品类型列表
     */
    fun getSupportedProductTypes(): List<String> {
        return products.keys.toList().distinct()
    }

    /**
     * 检查产品类型是否支持
     */
    fun isProductSupported(productType: String): Boolean {
        return products.containsKey(productType.lowercase())
    }

    /**
     * 获取所有产品实例
     */
    fun getAllProducts(): List<Product> {
        return products.values.distinctBy { it.productType }
    }

    /**
     * 根据身高范围推荐产品
     */
    fun recommendProductsByHeight(heightRange: String): List<Product> {
        return getAllProducts().filter { product ->
            product.getSupportedHeightRanges().contains(heightRange)
        }
    }
}

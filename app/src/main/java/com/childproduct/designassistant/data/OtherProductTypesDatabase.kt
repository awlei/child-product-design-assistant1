package com.childproduct.designassistant.data

/**
 * 其他产品类型数据库
 * 
 * 包含：婴儿推车、儿童高脚椅、儿童床等产品类型的数据
 * 数据来源：GPS-028人体测量学文件、EN 1888、GB 14748、EN 14988等标准
 * 
 * 说明：由于这些产品类型的产品特点与儿童安全座椅不同，
 * 它们使用的假人类型、设计参数和标准要求也有所不同。
 */

/**
 * 婴儿推车设计参数
 */
data class StrollerDesignParameters(
    val ageRange: String,                    // 适用年龄范围
    val heightRange: String,                 // 身高范围（cm）
    val weightRange: String,                 // 体重范围（kg）
    val handleHeightRange: String,           // 扶手高度（mm）
    val seatWidthRange: String,              // 座宽（mm）
    val backrestAngleRange: String,          // 靠背角度
    val wheelbaseRange: String,              // 轮距（mm）
    val standardRequirements: StrollerStandardRequirements  // 标准要求
)

/**
 * 婴儿推车标准要求
 */
data class StrollerStandardRequirements(
    val brakeForce: String,                  // 刹车力（N）
    val stabilityAngle: String,              // 侧翻角度（°）
    val handleStrength: String,              // 手柄强度（N）
    val dataSource: String,                  // 数据来源
    val testStandard: String                 // 测试标准
)

/**
 * 儿童高脚椅设计参数
 */
data class HighChairDesignParameters(
    val ageRange: String,                    // 适用年龄范围
    val heightRange: String,                 // 身高范围（cm）
    val weightRange: String,                 // 体重范围（kg）
    val seatHeightRange: String,             // 座高（mm）
    val seatWidthRange: String,              // 座宽（mm）
    val backrestHeightRange: String,         // 靠背高度（mm）
    val trayWidthRange: String,              // 托盘宽度（mm）
    val standardRequirements: HighChairStandardRequirements  // 标准要求
)

/**
 * 儿童高脚椅标准要求
 */
data class HighChairStandardRequirements(
    val stabilityTest: String,               // 稳定性测试
    val restraintStrength: String,           // 约束强度（N）
    val trayLoadCapacity: String,            // 托盘承载能力（kg）
    val dataSource: String,                  // 数据来源
    val testStandard: String                 // 测试标准
)

/**
 * 儿童床设计参数
 */
data class CribDesignParameters(
    val ageRange: String,                    // 适用年龄范围
    val heightRange: String,                 // 身高范围（cm）
    val weightRange: String,                 // 体重范围（kg）
    val mattressSizeRange: String,           // 床垫尺寸（mm）
    val sideRailHeightRange: String,         // 侧栏高度（mm）
    val slatSpacingRange: String,            // 床栏间距（mm）
    val standardRequirements: CribStandardRequirements  // 标准要求
)

/**
 * 儿童床标准要求
 */
data class CribStandardRequirements(
    val mattressSupport: String,             // 床垫支撑能力（kg）
    val sideRailStrength: String,            // 侧栏强度（N）
    val slatSpacingLimit: String,            // 床栏间距极限（mm）
    val dataSource: String,                  // 数据来源
    val testStandard: String                 // 测试标准
)

/**
 * 其他产品类型数据库
 */
object OtherProductTypesDatabase {
    
    /**
     * 获取婴儿推车设计参数
     * 数据来源：GPS-028 Big Infant Anthro表、EN 1888、GB 14748
     */
    fun getStrollerParameters(): StrollerDesignParameters {
        return StrollerDesignParameters(
            ageRange = "0-36个月（0-3岁）",
            heightRange = "50-95cm（GPS-028 Big Infant Anthro表Q0-Q3假人）",
            weightRange = "3.2-15.0kg（GPS-028 Big Infant Anthro表Q0-Q3假人体重）",
            
            handleHeightRange = "180-260mm（可调节，适配不同身高的推车人）",
            seatWidthRange = "320-360mm（GPS-028 Dummies表Q0-Q3假人肩宽+安全余量）",
            backrestAngleRange = "140°-175°（多档位调节，EN 1888要求）",
            wheelbaseRange = "550-600mm（防侧翻，EN 1888稳定性要求）",
            
            standardRequirements = StrollerStandardRequirements(
                brakeForce = "≤50N（EN 1888 §8.3）",
                stabilityAngle = "≥30°（EN 1888 §8.4）",
                handleStrength = "可承受135N拉力无变形（ASTM F833）",
                dataSource = "EN 1888-2:2018、GB 14748-2020",
                testStandard = "ISO 20932-1:2018"
            )
        )
    }
    
    /**
     * 获取儿童高脚椅设计参数
     * 数据来源：GPS-028 Big Infant Anthro表、EN 14988、GB 22793
     */
    fun getHighChairParameters(): HighChairDesignParameters {
        return HighChairDesignParameters(
            ageRange = "6-36个月（6个月-3岁）",
            heightRange = "65-100cm（GPS-028 Big Infant Anthro表Q1-Q3假人）",
            weightRange = "7.0-15.0kg（GPS-028 Big Infant Anthro表Q1-Q3假人体重）",
            
            seatHeightRange = "450-550mm（适配成人餐桌高度750mm）",
            seatWidthRange = "280-320mm（GPS-028 Dummies表Q1-Q3假人臀宽+安全余量）",
            backrestHeightRange = "350-450mm（GPS-028 Dummies表Q1-Q3假人坐高）",
            trayWidthRange = "400-450mm（标准托盘尺寸）",
            
            standardRequirements = HighChairStandardRequirements(
                stabilityTest = "通过6°斜面测试（EN 14988 §5.3）",
                restraintStrength = "≥750N（5点式安全带）",
                trayLoadCapacity = "≥10kg（EN 14988 §5.4）",
                dataSource = "EN 14988:2017、GB 22793-2008",
                testStandard = "ISO 9221-1:1992"
            )
        )
    }
    
    /**
     * 获取儿童床设计参数
     * 数据来源：GPS-028 Big Infant Anthro表、EN 716、GB 29281
     */
    fun getCribParameters(): CribDesignParameters {
        return CribDesignParameters(
            ageRange = "0-36个月（0-3岁）",
            heightRange = "50-95cm（GPS-028 Big Infant Anthro表Q0-Q3假人）",
            weightRange = "3.2-15.0kg（GPS-028 Big Infant Anthro表Q0-Q3假人体重）",
            
            mattressSizeRange = "600×1200mm（标准婴儿床尺寸）",
            sideRailHeightRange = "≥600mm（防止儿童攀爬，EN 716 §5.2）",
            slatSpacingRange = "≤60mm（防止头部卡住，EN 716 §5.3）",
            
            standardRequirements = CribStandardRequirements(
                mattressSupport = "≥30kg（EN 716 §5.4）",
                sideRailStrength = "≥750N（侧栏强度测试）",
                slatSpacingLimit = "≤60mm（EN 716 §5.3）",
                dataSource = "EN 716-1:2017、GB 29281-2012",
                testStandard = "ISO 7175-1:1997"
            )
        )
    }
}

package com.childproduct.designassistant.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.childproduct.designassistant.database.dao.BrandDao
import com.childproduct.designassistant.database.entity.*

/**
 * 品牌数据库
 * 
 * 功能：
 * - 持久化品牌产品信息
 * - 存储产品技术参数、功能特性
 * - 记录产品合规性和用户评价
 * - 支持产品对比和搜索
 * 
 * 使用场景：
 * - 品牌产品查询和管理
 * - 产品参数对比分析
 * - 市场调研和竞品分析
 * - 用户评价收集和分析
 */
@Database(
    entities = [
        BrandProductEntity::class,
        BrandParameterEntity::class,
        BrandFeatureEntity::class,
        BrandComplianceEntity::class,
        BrandReviewEntity::class,
        BrandComparisonEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class BrandDatabase : RoomDatabase() {
    abstract fun brandDao(): BrandDao
    
    companion object {
        const val DATABASE_NAME = "brand_database.db"
    }
}

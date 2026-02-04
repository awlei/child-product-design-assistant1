package com.childproduct.designassistant.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.childproduct.designassistant.database.dao.FMVSSDao
import com.childproduct.designassistant.database.entity.*

/**
 * FMVSS数据库
 * 
 * 功能：
 * - 持久化FMVSS 213/213a标准数据
 * - 存储测试配置和安全阈值
 * - 记录产品测试结果和认证信息
 * 
 * 使用场景：
 * - FMVSS标准查询和分析
 * - 产品测试结果管理
 * - 认证证书跟踪
 */
@Database(
    entities = [
        FMVSSStandardEntity::class,
        FMVSSDummyEntity::class,
        FMVSSTestConfigEntity::class,
        FMVSSThresholdEntity::class,
        FMVSSTestRecordEntity::class,
        FMVSSProductTestEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class FMVSSDatabase : RoomDatabase() {
    abstract fun fmvssDao(): FMVSSDao
    
    companion object {
        const val DATABASE_NAME = "fmvss_database.db"
    }
}

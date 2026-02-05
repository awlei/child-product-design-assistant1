package com.childproduct.designassistant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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
        CrashTestDummy::class,
        FMVSSStandardEntity::class,
        FMVSSTestConfigEntity::class,
        FMVSSThresholdEntity::class,
        FMVSSTestRecordEntity::class,
        FMVSSProductTestEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class FMVSSDatabase : RoomDatabase() {
    abstract fun fmvssDao(): FMVSSDao

    companion object {
        const val DATABASE_NAME = "fmvss_database.db"

        @Volatile private var INSTANCE: FMVSSDatabase? = null

        fun getDatabase(context: Context): FMVSSDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FMVSSDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun closeDatabase() {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
}

package com.design.assistant.database.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.design.assistant.database.dao.GPS028DAO
import com.design.assistant.database.entities.GPS028Entity
import com.design.assistant.model.GPS028Group
import com.design.assistant.model.StandardConstants

/**
 * GPS028数据库
 * 专门用于存储GB 27887-2011标准的设计参数数据
 * 采用物理隔离策略，与其他标准数据库分开
 */
@Database(
    entities = [GPS028Entity::class],
    version = 1,
    exportSchema = false
)
abstract class GPS028Database : RoomDatabase() {

    /**
     * 获取GPS028DAO
     */
    abstract fun gps028DAO(): GPS028DAO

    companion object {
        private const val DATABASE_NAME = "gps028_database.db"

        @Volatile
        private var INSTANCE: GPS028Database? = null

        /**
         * 获取数据库实例
         */
        fun getInstance(context: Context): GPS028Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GPS028Database::class.java,
                    DATABASE_NAME
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * 数据库回调，用于初始化数据
         */
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // 数据库创建时初始化数据
                // 注意：这里需要在主线程中执行，实际使用时应该在后台线程初始化
            }
        }

        /**
         * 数据库迁移（如需要升级版本时使用）
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 如需要迁移，在这里添加SQL语句
                // 例如：database.execSQL("ALTER TABLE gps028_params ADD COLUMN newColumn TEXT")
            }
        }
    }

    /**
     * 初始化GPS028标准数据
     */
    suspend fun initializeGPS028Data() {
        val dao = gps028DAO()

        // 检查是否已有数据
        if (dao.getCount() > 0) {
            return
        }

        // 插入所有组别的50%、75%、95%百分位数据
        val groups = listOf(
            GPS028Group.GROUP_0,
            GPS028Group.GROUP_0P,
            GPS028Group.GROUP_I,
            GPS028Group.GROUP_II,
            GPS028Group.GROUP_III
        )

        val percentiles = listOf(50, 75, 95)

        groups.forEach { group ->
            percentiles.forEach { percentile ->
                val params = StandardConstants.getGPS028PercentileParams(percentile, group)
                val entity = GPS028Entity.fromGPS028Params(params)
                dao.insert(entity)
            }
        }
    }
}

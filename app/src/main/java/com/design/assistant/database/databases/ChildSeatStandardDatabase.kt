package com.design.assistant.database.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.design.assistant.database.dao.ChildSeatStandardDAO
import com.design.assistant.database.entities.ChildSeatStandardEntity

/**
 * 儿童安全座椅标准数据库
 * 专门用于存储ECE R129、CMVSS213、FMVSS213等标准的设计参数数据
 * 采用物理隔离策略，与GPS028数据库分开
 */
@Database(
    entities = [ChildSeatStandardEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ChildSeatStandardDatabase : RoomDatabase() {

    /**
     * 获取ChildSeatStandardDAO
     */
    abstract fun childSeatStandardDAO(): ChildSeatStandardDAO

    companion object {
        private const val DATABASE_NAME = "child_seat_standard_database.db"

        @Volatile
        private var INSTANCE: ChildSeatStandardDatabase? = null

        /**
         * 获取数据库实例
         */
        fun getInstance(context: Context): ChildSeatStandardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChildSeatStandardDatabase::class.java,
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
        val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 如需要迁移，在这里添加SQL语句
                // 例如：database.execSQL("ALTER TABLE child_seat_standard_params ADD COLUMN newColumn TEXT")
            }
        }
    }

    /**
     * 初始化儿童安全座椅标准数据
     */
    suspend fun initializeChildSeatStandardData() {
        val dao = childSeatStandardDAO()

        // 检查是否已有数据
        if (dao.getCount() > 0) {
            return
        }

        // 插入ECE R129标准数据
        val eceR129Entities = listOf(
            ChildSeatStandardEntity.createECE_R129_Q1(),
            ChildSeatStandardEntity.createECE_R129_Q3(),
            ChildSeatStandardEntity.createECE_R129_Q6()
        )
        dao.insertAll(eceR129Entities)

        // 插入CMVSS213标准数据（加拿大）
        val cmvssEntities = listOf(
            ChildSeatStandardEntity.createCMVSS213_Toddler()
        )
        dao.insertAll(cmvssEntities)

        // 插入FMVSS213标准数据（美国）
        val fmvssEntities = listOf(
            ChildSeatStandardEntity.createFMVSS213_Toddler()
        )
        dao.insertAll(fmvssEntities)

        // 插入AS/NZS1754标准数据（澳大利亚/新西兰）
        val asNzsEntities = listOf(
            ChildSeatStandardEntity.createAS_NZS1754_I()
        )
        dao.insertAll(asNzsEntities)
    }
}

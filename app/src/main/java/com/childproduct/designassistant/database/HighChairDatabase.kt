package com.childproduct.designassistant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.childproduct.designassistant.database.converter.Converters
import com.childproduct.designassistant.database.dao.*
import com.childproduct.designassistant.database.entity.*

/**
 * 儿童高脚椅标准数据库
 *
 * 支持标准：
 * - EN 14988 (欧盟儿童高脚椅安全标准)
 * - GB 29281 (中国儿童高脚椅安全标准)
 * - ASTM F404 (美国儿童高脚椅安全标准)
 */
@Database(
    entities = [
        HighChairStandard::class,
        HighChairAgeGroup::class,
        HighChairSafetyRequirement::class,
        HighChairStability::class,
        HighChairRestraint::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class HighChairDatabase : RoomDatabase() {

    // DAO
    abstract fun highChairStandardDao(): HighChairStandardDao
    abstract fun highChairAgeGroupDao(): HighChairAgeGroupDao
    abstract fun highChairSafetyRequirementDao(): HighChairSafetyRequirementDao
    abstract fun highChairStabilityDao(): HighChairStabilityDao
    abstract fun highChairRestraintDao(): HighChairRestraintDao

    companion object {
        @Volatile private var INSTANCE: HighChairDatabase? = null

        fun getDatabase(context: Context): HighChairDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HighChairDatabase::class.java,
                    "high_chair_database"
                )
                    .fallbackToDestructiveMigration()  // 开发阶段允许破坏性迁移
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * 关闭数据库连接
         */
        fun closeDatabase() {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
}

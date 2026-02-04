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
 * 儿童床标准数据库
 *
 * 支持标准：
 * - EN 716 (欧盟儿童床安全标准)
 * - GB 28007 (中国儿童床安全标准)
 * - ASTM F1169 (美国儿童床安全标准)
 * - ISO 7175-1 (国际儿童床标准)
 */
@Database(
    entities = [
        CribStandard::class,
        CribDimension::class,
        CribMattressGap::class,
        CribRailing::class,
        CribSafetyRequirement::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class CribDatabase : RoomDatabase() {

    // DAO
    abstract fun cribStandardDao(): CribStandardDao
    abstract fun cribDimensionDao(): CribDimensionDao
    abstract fun cribMattressGapDao(): CribMattressGapDao
    abstract fun cribRailingDao(): CribRailingDao
    abstract fun cribSafetyRequirementDao(): CribSafetyRequirementDao

    companion object {
        @Volatile private var INSTANCE: CribDatabase? = null

        fun getDatabase(context: Context): CribDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CribDatabase::class.java,
                    "crib_database"
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

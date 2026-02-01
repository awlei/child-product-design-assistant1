package com.childproduct.designassistant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.childproduct.designassistant.database.converter.Converters
import com.childproduct.designassistant.database.dao.*
import com.childproduct.designassistant.database.entity.*

/**
 * ECE R129标准数据库
 * 基于UN R129 Rev.4标准
 */
@Database(
    entities = [
        CrashTestDummy::class,
        HeightRangeMapping::class,
        SafetyThreshold::class,
        TestConfiguration::class,
        StandardReference::class,
        InstallationMethod::class,
        MaterialSpecification::class,
        IsofixRequirement::class,
        StandardUpdateLog::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class EceR129Database : RoomDatabase() {

    abstract fun crashTestDummyDao(): CrashTestDummyDao
    abstract fun heightRangeMappingDao(): HeightRangeMappingDao
    abstract fun safetyThresholdDao(): SafetyThresholdDao
    abstract fun testConfigurationDao(): TestConfigurationDao
    abstract fun standardReferenceDao(): StandardReferenceDao
    abstract fun installationMethodDao(): InstallationMethodDao
    abstract fun materialSpecificationDao(): MaterialSpecificationDao
    abstract fun isofixRequirementDao(): IsofixRequirementDao
    abstract fun standardUpdateLogDao(): StandardUpdateLogDao

    companion object {
        @Volatile private var INSTANCE: EceR129Database? = null

        fun getDatabase(context: Context): EceR129Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EceR129Database::class.java,
                    "ece_r129_database"
                )
                    // 数据库初始化回调
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // 数据库创建时初始化数据（异步执行）
                            INSTANCE?.let { database ->
                                DatabaseInitializer(context).also { initializer ->
                                    initializer.execute(database)
                                }
                            }
                        }
                    })
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

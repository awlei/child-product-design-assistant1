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
 * ECE R129 数据库
 *
 * 存储 ECE R129 标准相关的所有数据
 *
 * 数据库版本: 8
 */
@Database(
    entities = [
        CrashTestDummy::class,
        EceEnvelope::class,             // 新增：ECE Envelope实体
        SafetyThreshold::class,
        TestConfiguration::class,
        StandardReference::class,
        InstallationMethod::class,
        IsofixRequirement::class,
        MaterialSpecification::class,
        HeightRangeMapping::class,
        StandardUpdateLog::class
    ],
    version = 8,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class EceR129Database : RoomDatabase() {
    
    // DAO 访问方法
    abstract fun crashTestDummyDao(): CrashTestDummyDao
    abstract fun eceEnvelopeDao(): EceEnvelopeDao              // 新增：ECE Envelope DAO
    abstract fun safetyThresholdDao(): SafetyThresholdDao
    abstract fun testConfigurationDao(): TestConfigurationDao
    abstract fun standardReferenceDao(): StandardReferenceDao
    abstract fun installationMethodDao(): InstallationMethodDao
    abstract fun isofixRequirementDao(): IsofixRequirementDao
    abstract fun materialSpecificationDao(): MaterialSpecificationDao
    abstract fun heightRangeMappingDao(): HeightRangeMappingDao
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

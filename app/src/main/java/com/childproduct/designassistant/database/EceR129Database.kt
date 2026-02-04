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
        StandardUpdateLog::class,
        // 儿童高脚椅相关实体
        HighChairStandard::class,
        HighChairAgeGroup::class,
        HighChairSafetyRequirement::class,
        HighChairStability::class,
        HighChairRestraint::class,
        // 儿童床相关实体
        CribStandard::class,
        CribDimension::class,
        CribMattressGap::class,
        CribRailing::class,
        CribSafetyRequirement::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class EceR129Database : RoomDatabase() {

    // ECE R129 相关 DAO
    abstract fun crashTestDummyDao(): CrashTestDummyDao
    abstract fun heightRangeMappingDao(): HeightRangeMappingDao
    abstract fun safetyThresholdDao(): SafetyThresholdDao
    abstract fun testConfigurationDao(): TestConfigurationDao
    abstract fun standardReferenceDao(): StandardReferenceDao
    abstract fun installationMethodDao(): InstallationMethodDao
    abstract fun materialSpecificationDao(): MaterialSpecificationDao
    abstract fun isofixRequirementDao(): IsofixRequirementDao
    abstract fun standardUpdateLogDao(): StandardUpdateLogDao
    
    // 儿童高脚椅相关 DAO
    abstract fun highChairStandardDao(): HighChairStandardDao
    abstract fun highChairAgeGroupDao(): HighChairAgeGroupDao
    abstract fun highChairSafetyRequirementDao(): HighChairSafetyRequirementDao
    abstract fun highChairStabilityDao(): HighChairStabilityDao
    abstract fun highChairRestraintDao(): HighChairRestraintDao
    
    // 儿童床相关 DAO
    abstract fun cribStandardDao(): CribStandardDao
    abstract fun cribDimensionDao(): CribDimensionDao
    abstract fun cribMattressGapDao(): CribMattressGapDao
    abstract fun cribRailingDao(): CribRailingDao
    abstract fun cribSafetyRequirementDao(): CribSafetyRequirementDao

    companion object {
        @Volatile private var INSTANCE: EceR129Database? = null

        fun getDatabase(context: Context): EceR129Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EceR129Database::class.java,
                    "ece_r129_database"
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

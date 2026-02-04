package com.childproduct.designassistant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.childproduct.designassistant.database.dao.*
import com.childproduct.designassistant.database.entity.*

/**
 * 儿童高脚椅数据库
 * 
 * 包含所有高脚椅相关的实体和静态标准数据
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
    exportSchema = false
)
abstract class HighChairDatabase : RoomDatabase() {
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

        /**
         * 获取所有高脚椅标准
         */
        fun getAllStandards(): List<HighChairStandard> {
            return HighChairStandardsData.STANDARDS
        }

        /**
         * 根据ID获取标准
         */
        fun getStandardById(standardId: String): HighChairStandard? {
            return HighChairStandardsData.STANDARDS.find { it.standardId == standardId }
        }

        /**
         * 获取某个标准的所有年龄组
         */
        fun getAgeGroupsByStandardId(standardId: String): List<HighChairAgeGroup> {
            return HighChairStandardsData.AGE_GROUPS.filter { it.standardId == standardId }
        }

        /**
         * 获取某个标准的所有安全要求
         */
        fun getSafetyRequirementsByStandardId(standardId: String): List<HighChairSafetyRequirement> {
            return HighChairStandardsData.SAFETY_REQUIREMENTS.filter { it.standardId == standardId }
        }

        /**
         * 获取某个标准的所有稳定性数据
         */
        fun getStabilityDataByStandardId(standardId: String): List<HighChairStability> {
            return HighChairStandardsData.STABILITY_DATA.filter { it.standardId == standardId }
        }

        /**
         * 获取某个标准的所有约束系统数据
         */
        fun getRestraintDataByStandardId(standardId: String): List<HighChairRestraint> {
            return HighChairStandardsData.RESTRAINT_DATA.filter { it.standardId == standardId }
        }

        /**
         * 根据地区获取标准列表
         */
        fun getStandardsByRegion(region: String): List<HighChairStandard> {
            return HighChairStandardsData.STANDARDS.filter { it.region == region }
        }

        /**
         * 根据地区和标准名称获取标准
         */
        fun getStandardByNameAndRegion(standardName: String, region: String): HighChairStandard? {
            return HighChairStandardsData.STANDARDS.find {
                it.standardName == standardName && it.region == region
            }
        }
    }
}

package com.childproduct.designassistant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.childproduct.designassistant.database.dao.*
import com.childproduct.designassistant.database.entity.*

/**
 * 儿童床数据库
 * 
 * 包含所有儿童床相关的实体和静态标准数据
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
    exportSchema = false
)
abstract class CribDatabase : RoomDatabase() {
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

        /**
         * 获取所有儿童床标准
         */
        fun getAllStandards(): List<CribStandard> {
            return CribStandardsData.STANDARDS
        }

        /**
         * 根据ID获取标准
         */
        fun getStandardById(standardId: String): CribStandard? {
            return CribStandardsData.STANDARDS.find { it.standardId == standardId }
        }

        /**
         * 获取某个标准的所有尺寸要求
         */
        fun getDimensionsByStandardId(standardId: String): List<CribDimension> {
            return CribStandardsData.DIMENSIONS.filter { it.standardId == standardId }
        }

        /**
         * 获取某个标准的所有床垫间隙要求
         */
        fun getMattressGapsByStandardId(standardId: String): List<CribMattressGap> {
            return CribStandardsData.MATTRESS_GAPS.filter { it.standardId == standardId }
        }

        /**
         * 获取某个标准的所有栏杆要求
         */
        fun getRailingsByStandardId(standardId: String): List<CribRailing> {
            return CribStandardsData.RAILINGS.filter { it.standardId == standardId }
        }

        /**
         * 获取某个标准的所有安全要求
         */
        fun getSafetyRequirementsByStandardId(standardId: String): List<CribSafetyRequirement> {
            return CribStandardsData.SAFETY_REQUIREMENTS.filter { it.standardId == standardId }
        }

        /**
         * 根据地区获取标准列表
         */
        fun getStandardsByRegion(region: String): List<CribStandard> {
            return CribStandardsData.STANDARDS.filter { it.region == region }
        }

        /**
         * 根据地区和标准名称获取标准
         */
        fun getStandardByNameAndRegion(standardName: String, region: String): CribStandard? {
            return CribStandardsData.STANDARDS.find {
                it.standardName == standardName && it.region == region
            }
        }
    }
}

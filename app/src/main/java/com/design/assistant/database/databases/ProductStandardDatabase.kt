package com.design.assistant.database.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.design.assistant.database.dao.ProductStandardDAO
import com.design.assistant.database.entities.ProductStandardEntity

/**
 * 产品标准数据库
 * 专门用于存储婴儿推车、高脚椅、儿童床等产品的标准设计参数数据
 * 采用物理隔离策略，与GPS028和儿童安全座椅数据库分开
 */
@Database(
    entities = [ProductStandardEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ProductStandardDatabase : RoomDatabase() {

    /**
     * 获取ProductStandardDAO
     */
    abstract fun productStandardDAO(): ProductStandardDAO

    companion object {
        private const val DATABASE_NAME = "product_standard_database.db"

        @Volatile
        private var INSTANCE: ProductStandardDatabase? = null

        /**
         * 获取数据库实例
         */
        fun getInstance(context: Context): ProductStandardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductStandardDatabase::class.java,
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
    }

    /**
     * 初始化产品标准数据
     */
    suspend fun initializeProductStandardData() {
        val dao = productStandardDAO()

        // 检查是否已有数据
        if (dao.getCount() > 0) {
            return
        }

        // 插入婴儿推车标准数据
        val strollerEntities = listOf(
            ProductStandardEntity.createEN1888_Stroller(),
            ProductStandardEntity.createASTM_F833_Stroller(),
            ProductStandardEntity.createCSA_B311_Stroller()
        )
        dao.insertAll(strollerEntities)

        // 插入高脚椅标准数据
        val highChairEntities = listOf(
            ProductStandardEntity.createEN14988_HighChair(),
            ProductStandardEntity.createASTM_F404_HighChair(),
            ProductStandardEntity.createCSA_B229_HighChair()
        )
        dao.insertAll(highChairEntities)

        // 插入儿童床标准数据
        val cribEntities = listOf(
            ProductStandardEntity.createEN716_Crib(),
            ProductStandardEntity.createASTM_F1169_Crib(),
            ProductStandardEntity.createCSA_B113_Crib()
        )
        dao.insertAll(cribEntities)
    }

    /**
     * 数据库迁移（如需要升级版本时使用）
     */
    companion object {
        val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 如需要迁移，在这里添加SQL语句
                // 例如：database.execSQL("ALTER TABLE product_standard_params ADD COLUMN newColumn TEXT")
            }
        }
    }
}

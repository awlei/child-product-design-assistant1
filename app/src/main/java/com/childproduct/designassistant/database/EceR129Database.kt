package com.childproduct.designassistant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.childproduct.designassistant.database.dao.StandardUpdateLogDao
import com.childproduct.designassistant.database.entity.StandardUpdateLog

/**
 * ECE R129标准数据库
 * 基于UN R129 Rev.4标准
 */
@Database(
    entities = [StandardUpdateLog::class],
    version = 6,
    exportSchema = true
)
abstract class EceR129Database : RoomDatabase() {

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

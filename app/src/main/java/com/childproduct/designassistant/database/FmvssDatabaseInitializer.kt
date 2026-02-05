package com.childproduct.designassistant.database

import android.content.Context
import com.childproduct.designassistant.database.dao.FmvssCrashTestDummyDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * FMVSS数据库初始化器
 * 首次创建数据库时，初始化FMVSS标准数据
 * 物理隔离：仅初始化FMVSS数据，不包含ECE数据
 */
class FmvssDatabaseInitializer(private val context: Context) {

    fun execute(database: FMVSSDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. 初始化FMVSS专属假人数据（物理隔离）
                database.fmvssCrashTestDummyDao().initializeFmvssDummies()

                // 2. 记录初始化日志（如果有日志表的话）
                val dummyCount = database.fmvssCrashTestDummyDao().getCount()
                android.util.Log.d("FmvssDatabaseInitializer", "FMVSS数据库初始化完成，包含${dummyCount}个FMVSS专属假人")

            } catch (e: Exception) {
                android.util.Log.e("FmvssDatabaseInitializer", "FMVSS数据库初始化失败: ${e.message}")
            }
        }
    }
}

package com.childproduct.designassistant.database

import android.content.Context
import android.util.Log
import com.childproduct.designassistant.database.dao.EceCrashTestDummyDao
import com.childproduct.designassistant.database.dao.FmvssCrashTestDummyDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 数据库清理和重新初始化器
 * 功能：
 * 1. 清除旧的混用数据
 * 2. 重新初始化物理隔离的ECE和FMVSS数据
 * 3. 验证数据完整性
 */
class DatabaseCleanAndReinitializer(private val context: Context) {

    companion object {
        private const val TAG = "DatabaseCleanAndReinit"
    }

    fun execute(
        eceR129Database: EceR129Database,
        fmvssDatabase: FMVSSDatabase
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "========== 开始数据库清理和重新初始化 ==========")

                // 1. 清除ECE专属假人数据
                cleanEceData(eceR129Database.eceCrashTestDummyDao())

                // 2. 清除FMVSS专属假人数据
                cleanFmvssData(fmvssDatabase.fmvssCrashTestDummyDao())

                // 3. 重新初始化ECE数据
                initializeEceData(eceR129Database.eceCrashTestDummyDao())

                // 4. 重新初始化FMVSS数据
                initializeFmvssData(fmvssDatabase.fmvssCrashTestDummyDao())

                // 5. 验证数据完整性
                validateDataIntegrity(eceR129Database.eceCrashTestDummyDao(), fmvssDatabase.fmvssCrashTestDummyDao())

                Log.d(TAG, "========== 数据库清理和重新初始化完成 ==========")

            } catch (e: Exception) {
                Log.e(TAG, "数据库清理和重新初始化失败: ${e.message}", e)
            }
        }
    }

    /**
     * 清除ECE专属假人数据
     */
    private suspend fun cleanEceData(eceCrashTestDummyDao: EceCrashTestDummyDao) {
        Log.d(TAG, "[ECE] 清除旧数据...")
        eceCrashTestDummyDao.deleteAll()
        Log.d(TAG, "[ECE] 数据已清除")
    }

    /**
     * 清除FMVSS专属假人数据
     */
    private suspend fun cleanFmvssData(fmvssCrashTestDummyDao: FmvssCrashTestDummyDao) {
        Log.d(TAG, "[FMVSS] 清除旧数据...")
        fmvssCrashTestDummyDao.deleteAll()
        Log.d(TAG, "[FMVSS] 数据已清除")
    }

    /**
     * 重新初始化ECE数据
     */
    private suspend fun initializeEceData(eceCrashTestDummyDao: EceCrashTestDummyDao) {
        Log.d(TAG, "[ECE] 重新初始化数据...")

        // 初始化ECE标准假人（Q0, Q1, Q1.5, Q3, Q6, Q10）
        eceCrashTestDummyDao.initializeEceDummies()

        Log.d(TAG, "[ECE] 初始化完成，共 ${eceCrashTestDummyDao.getCount()} 个ECE假人")
    }

    /**
     * 重新初始化FMVSS数据
     */
    private suspend fun initializeFmvssData(fmvssCrashTestDummyDao: FmvssCrashTestDummyDao) {
        Log.d(TAG, "[FMVSS] 重新初始化数据...")

        // 初始化FMVSS标准假人（Q3s, 3y, 6y, 10y）
        fmvssCrashTestDummyDao.initializeFmvssDummies()

        Log.d(TAG, "[FMVSS] 初始化完成，共 ${fmvssCrashTestDummyDao.getCount()} 个FMVSS假人")
    }

    /**
     * 验证数据完整性
     */
    private suspend fun validateDataIntegrity(
        eceCrashTestDummyDao: EceCrashTestDummyDao,
        fmvssCrashTestDummyDao: FmvssCrashTestDummyDao
    ) {
        Log.d(TAG, "========== 数据完整性验证 ==========")

        // 1. 验证ECE数据
        val eceInvalidDummies = eceCrashTestDummyDao.getInvalidEceDummies()
        if (eceInvalidDummies.isNotEmpty()) {
            Log.e(TAG, "[ECE] 发现无效假人: ${eceInvalidDummies.joinToString(", ")}")
        } else {
            Log.d(TAG, "[ECE] 所有假人都是有效的ECE假人")
        }

        // 2. 验证FMVSS数据
        val fmvssInvalidDummies = fmvssCrashTestDummyDao.getInvalidFmvssDummies()
        if (fmvssInvalidDummies.isNotEmpty()) {
            Log.e(TAG, "[FMVSS] 发现无效假人: ${fmvssInvalidDummies.joinToString(", ")}")
        } else {
            Log.d(TAG, "[FMVSS] 所有假人都是有效的FMVSS假人")
        }

        // 3. 验证假人数量
        val eceCount = eceCrashTestDummyDao.getCount()
        val fmvssCount = fmvssCrashTestDummyDao.getCount()

        Log.d(TAG, "[统计] ECE假人数量: $eceCount (期望: 7)")
        Log.d(TAG, "[统计] FMVSS假人数量: $fmvssCount (期望: 4)")

        if (eceCount == 7 && fmvssCount == 4) {
            Log.d(TAG, "========== 数据完整性验证通过 ==========")
        } else {
            Log.w(TAG, "========== 数据完整性验证失败 ==========")
        }
    }

    /**
     * 重置整个数据库（清除所有数据）
     * 警告：此操作不可逆
     */
    suspend fun fullReset(
        eceR129Database: EceR129Database,
        fmvssDatabase: FMVSSDatabase
    ) {
        Log.w(TAG, "========== 开始完全重置数据库 ==========")

        // 清除所有数据
        eceR129Database.eceCrashTestDummyDao().deleteAll()
        eceR129Database.crashTestDummyDao().deleteAll()  // 旧版假人
        fmvssDatabase.fmvssCrashTestDummyDao().deleteAll()

        // 重新初始化
        execute(eceR129Database, fmvssDatabase)

        Log.d(TAG, "========== 完全重置数据库完成 ==========")
    }
}

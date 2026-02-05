package com.childproduct.designassistant.database

import android.content.Context
import android.util.Log
import com.childproduct.designassistant.database.dao.EceEnvelopeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 数据库清理和重新初始化器
 * 功能：
 * 1. 清除旧数据
 * 2. 重新初始化Envelope数据
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

                // 1. 清除Envelope数据
                cleanEnvelopeData(eceR129Database.eceEnvelopeDao())

                // 2. 重新初始化Envelope数据
                initializeEnvelopeData(eceR129Database.eceEnvelopeDao())

                // 3. 验证数据完整性
                validateDataIntegrity(eceR129Database.eceEnvelopeDao())

                Log.d(TAG, "========== 数据库清理和重新初始化完成 ==========")

            } catch (e: Exception) {
                Log.e(TAG, "数据库清理和重新初始化失败: ${e.message}", e)
            }
        }
    }

    /**
     * 清除Envelope数据
     */
    private suspend fun cleanEnvelopeData(eceEnvelopeDao: EceEnvelopeDao) {
        Log.d(TAG, "[ECE] 清除Envelope数据...")
        eceEnvelopeDao.deleteAll()
        Log.d(TAG, "[ECE] Envelope数据已清除")
    }

    /**
     * 重新初始化Envelope数据
     */
    private suspend fun initializeEnvelopeData(eceEnvelopeDao: EceEnvelopeDao) {
        Log.d(TAG, "[ECE] 重新初始化Envelope数据...")

        // 初始化ECE标准Envelope（B1, B2, D, E）
        eceEnvelopeDao.initializeStandardEnvelopes()

        Log.d(TAG, "[ECE] Envelope初始化完成，共 ${eceEnvelopeDao.getCount()} 个Envelope")
    }

    /**
     * 验证数据完整性
     */
    private suspend fun validateDataIntegrity(eceEnvelopeDao: EceEnvelopeDao) {
        Log.d(TAG, "========== 数据完整性验证 ==========")

        // 验证Envelope数据
        val invalidSizeClasses = eceEnvelopeDao.getInvalidSizeClasses()
        if (invalidSizeClasses.isNotEmpty()) {
            Log.e(TAG, "[ECE] 发现无效的Size Class: ${invalidSizeClasses.joinToString(", ")}")
        } else {
            Log.d(TAG, "[ECE] 所有Size Class都是有效的")
        }

        // 验证Envelope数量
        val envelopeCount = eceEnvelopeDao.getCount()
        Log.d(TAG, "[统计] Envelope数量: $envelopeCount (期望: 4)")

        if (envelopeCount == 4) {
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
        eceR129Database.eceEnvelopeDao().deleteAll()
        eceR129Database.crashTestDummyDao().deleteAll()

        // 重新初始化
        execute(eceR129Database, fmvssDatabase)

        Log.d(TAG, "========== 完全重置数据库完成 ==========")
    }
}

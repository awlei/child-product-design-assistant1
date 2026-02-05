package com.childproduct.designassistant.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.childproduct.designassistant.constants.StandardConstants
import com.childproduct.designassistant.database.EceR129Database
import com.childproduct.designassistant.database.FMVSSDatabase
import com.childproduct.designassistant.database.dao.*
import com.childproduct.designassistant.database.entity.*
import com.childproduct.designassistant.model.InstallDirection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 标准数据仓库（重构版）
 * 提供对ECE R129和FMVSS 213标准数据的统一访问接口
 * 物理隔离：ECE和FMVSS数据完全分离
 */
class StandardRepository private constructor(
    private val context: Context,
    private val eceR129Database: EceR129Database,
    private val highChairDatabase: com.childproduct.designassistant.database.HighChairDatabase,
    private val cribDatabase: com.childproduct.designassistant.database.CribDatabase,
    private val fmvssDatabase: FMVSSDatabase  // 新增：FMVSS数据库
) {

    companion object {
        private const val TAG = "StandardRepository"

        @Volatile
        private var INSTANCE: StandardRepository? = null

        fun getInstance(
            context: Context,
            eceR129Database: EceR129Database,
            highChairDatabase: com.childproduct.designassistant.database.HighChairDatabase,
            cribDatabase: com.childproduct.designassistant.database.CribDatabase,
            fmvssDatabase: FMVSSDatabase
        ): StandardRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: StandardRepository(
                    context,
                    eceR129Database,
                    highChairDatabase,
                    cribDatabase,
                    fmvssDatabase
                ).also { INSTANCE = it }
            }
        }
    }

    // ========== ECE R129 专属假人操作 ==========

    /**
     * 获取所有ECE假人类型
     * 物理隔离：仅查询ece_crash_test_dummy表
     */
    suspend fun getAllEceDummies(): List<EceCrashTestDummy> {
        return withContext(Dispatchers.IO) {
            Log.d(TAG, "[ECE] 获取所有ECE假人")
            eceR129Database.eceCrashTestDummyDao().getAll()
        }
    }

    /**
     * 根据身高获取ECE假人
     */
    suspend fun getEceDummyByHeight(heightCm: Int): EceCrashTestDummy? {
        return withContext(Dispatchers.IO) {
            Log.d(TAG, "[ECE] 根据身高${heightCm}cm获取ECE假人")
            val dummies = eceR129Database.eceCrashTestDummyDao().getByHeight(heightCm)
            dummies.firstOrNull()
        }
    }

    /**
     * 根据假人代码获取ECE假人
     */
    suspend fun getEceDummyByCode(dummyCode: String): EceCrashTestDummy? {
        return withContext(Dispatchers.IO) {
            Log.d(TAG, "[ECE] 根据代码${dummyCode}获取ECE假人")
            eceR129Database.eceCrashTestDummyDao().getByDummyCode(dummyCode)
        }
    }

    /**
     * 根据产品组获取ECE假人
     */
    suspend fun getEceDummiesByProductGroup(productGroup: String): List<EceCrashTestDummy> {
        return withContext(Dispatchers.IO) {
            Log.d(TAG, "[ECE] 根据产品组${productGroup}获取ECE假人")
            eceR129Database.eceCrashTestDummyDao().getByProductGroup(productGroup)
        }
    }

    // ========== FMVSS 213 专属假人操作 ==========

    /**
     * 获取所有FMVSS假人类型
     * 物理隔离：仅查询fmvss_crash_test_dummy表
     */
    suspend fun getAllFmvssDummies(): List<FmvssCrashTestDummy> {
        return withContext(Dispatchers.IO) {
            Log.d(TAG, "[FMVSS] 获取所有FMVSS假人")
            fmvssDatabase.fmvssCrashTestDummyDao().getAll()
        }
    }

    /**
     * 根据身高（厘米）获取FMVSS假人
     */
    suspend fun getFmvssDummyByHeight(heightCm: Int): FmvssCrashTestDummy? {
        return withContext(Dispatchers.IO) {
            Log.d(TAG, "[FMVSS] 根据身高${heightCm}cm获取FMVSS假人")
            val heightIn = (heightCm / 2.54).toInt()
            val dummies = fmvssDatabase.fmvssCrashTestDummyDao().getByHeightInches(heightIn)
            dummies.firstOrNull()
        }
    }

    /**
     * 根据假人代码获取FMVSS假人
     */
    suspend fun getFmvssDummyByCode(dummyCode: String): FmvssCrashTestDummy? {
        return withContext(Dispatchers.IO) {
            Log.d(TAG, "[FMVSS] 根据代码${dummyCode}获取FMVSS假人")
            fmvssDatabase.fmvssCrashTestDummyDao().getByDummyCode(dummyCode)
        }
    }

    /**
     * 获取FMVSS侧碰测试假人（Q3s）
     */
    suspend fun getFmvssSideImpactDummy(): FmvssCrashTestDummy? {
        return withContext(Dispatchers.IO) {
            Log.d(TAG, "[FMVSS] 获取侧碰测试假人（Q3s）")
            fmvssDatabase.fmvssCrashTestDummyDao().getSideImpactDummy()
        }
    }

    // ========== 全链路标准拦截 ==========

    /**
     * 根据标准类型获取假人（全链路拦截）
     * 关键：确保ECE查询只返回ECE数据，FMVSS查询只返回FMVSS数据
     */
    suspend fun getDummiesByStandardType(
        standardType: String,
        heightCm: Int? = null
    ): List<*> {
        Log.d(TAG, "[StandardFlow] 全链路标准拦截: standardType=${standardType}, heightCm=${heightCm}")

        // 验证标准类型是否为有效常量
        val validStandardType = StandardConstants.getStandardConstant(standardType)
        if (validStandardType == null) {
            Log.e(TAG, "[StandardFlow] 无效的标准类型: ${standardType}")
            return emptyList<Any>()
        }

        return when (validStandardType) {
            StandardConstants.ECE_R129 -> {
                Log.d(TAG, "[StandardFlow] 查询ECE数据")
                if (heightCm != null) {
                    eceR129Database.eceCrashTestDummyDao().getByHeight(heightCm)
                } else {
                    eceR129Database.eceCrashTestDummyDao().getAll()
                }
            }
            StandardConstants.FMVSS_213 -> {
                Log.d(TAG, "[StandardFlow] 查询FMVSS数据")
                if (heightCm != null) {
                    val heightIn = (heightCm / 2.54).toInt()
                    fmvssDatabase.fmvssCrashTestDummyDao().getByHeightInches(heightIn)
                } else {
                    fmvssDatabase.fmvssCrashTestDummyDao().getAll()
                }
            }
            else -> {
                Log.e(TAG, "[StandardFlow] 未支持的标准类型: ${validStandardType}")
                emptyList<Any>()
            }
        }
    }

    /**
     * 验证标准类型（全链路验证）
     * 确保传入的标准类型是有效的常量
     */
    fun isValidStandardType(standardType: String): Boolean {
        val isValid = StandardConstants.getStandardConstant(standardType) != null
        Log.d(TAG, "[StandardFlow] 验证标准类型: ${standardType}, 结果=${isValid}")
        return isValid
    }

    /**
     * 获取标准显示名称
     */
    fun getStandardDisplayName(standardType: String): String {
        return StandardConstants.getDisplayName(standardType)
    }

    // ========== 保持向后兼容的旧方法（逐步废弃） ==========

    /**
     * 获取所有假人类型（旧版，向后兼容）
     * @deprecated 使用getAllEceDummies()或getAllFmvssDummies()
     */
    fun getAllDummies(): LiveData<List<CrashTestDummy>> {
        Log.w(TAG, "[Deprecated] getAllDummies()已废弃，请使用getAllEceDummies()或getAllFmvssDummies()")
        return eceR129Database.crashTestDummyDao().getAllDummies()
    }

    /**
     * 根据身高获取假人（旧版，向后兼容）
     * @deprecated 使用getEceDummyByHeight()或getFmvssDummyByHeight()
     */
    suspend fun getDummyByHeight(heightCm: Int): CrashTestDummy? {
        Log.w(TAG, "[Deprecated] getDummyByHeight()已废弃，请使用getEceDummyByHeight()或getFmvssDummyByHeight()")
        return withContext(Dispatchers.IO) {
            eceR129Database.crashTestDummyDao().getDummyByHeightRange(heightCm)
        }
    }
}

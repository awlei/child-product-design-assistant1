package com.childproduct.designassistant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.childproduct.designassistant.database.entity.EceEnvelope
import kotlinx.coroutines.flow.Flow

/**
 * ECE R129 Envelope数据访问对象
 * 提供对儿童安全座椅外形尺寸数据的CRUD操作
 */
@Dao
interface EceEnvelopeDao {
    /**
     * 插入Envelope数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(envelope: EceEnvelope)

    /**
     * 批量插入Envelope数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(envelopes: List<EceEnvelope>)

    /**
     * 根据ID查询Envelope
     */
    @Query("SELECT * FROM ece_envelope WHERE envelopeId = :envelopeId LIMIT 1")
    suspend fun getById(envelopeId: String): EceEnvelope?

    /**
     * 根据Size Class查询Envelope
     */
    @Query("SELECT * FROM ece_envelope WHERE sizeClass = :sizeClass LIMIT 1")
    suspend fun getBySizeClass(sizeClass: String): EceEnvelope?

    /**
     * 根据产品组查询Envelope
     */
    @Query("SELECT * FROM ece_envelope WHERE applicableGroup = :productGroup LIMIT 1")
    suspend fun getByProductGroup(productGroup: String): EceEnvelope?

    /**
     * 查询所有Envelope
     */
    @Query("SELECT * FROM ece_envelope ORDER BY sizeClass")
    suspend fun getAll(): List<EceEnvelope>

    /**
     * Flow方式查询所有Envelope（响应式）
     */
    @Query("SELECT * FROM ece_envelope ORDER BY sizeClass")
    fun getAllFlow(): Flow<List<EceEnvelope>>

    /**
     * 根据尺寸范围查询Envelope（过滤符合条件的）
     */
    @Query("SELECT * FROM ece_envelope WHERE maxLengthMm >= :minLength AND maxWidthMm >= :minWidth ORDER BY maxLengthMm")
    suspend fun getByMinSize(minLength: Int, minWidth: Int): List<EceEnvelope>

    /**
     * 查询需要支撑腿的Envelope（Group 0+）
     */
    @Query("SELECT * FROM ece_envelope WHERE applicableGroup IN ('Group 0+', 'Group 0+') ORDER BY sizeClass")
    suspend fun getBySupportLegRequired(): List<EceEnvelope>

    /**
     * 查询需要Top Tether的Envelope（Group I及以上）
     */
    @Query("SELECT * FROM ece_envelope WHERE applicableGroup IN ('Group I', 'Group I/II', 'Group II', 'Group III') ORDER BY sizeClass")
    suspend fun getByTopTetherRequired(): List<EceEnvelope>

    /**
     * 更新Envelope数据
     */
    @Update
    suspend fun update(envelope: EceEnvelope)

    /**
     * 删除指定Envelope
     */
    @Query("DELETE FROM ece_envelope WHERE envelopeId = :envelopeId")
    suspend fun deleteById(envelopeId: String)

    /**
     * 清空所有Envelope数据
     */
    @Query("DELETE FROM ece_envelope")
    suspend fun deleteAll()

    /**
     * 获取Envelope数量
     */
    @Query("SELECT COUNT(*) FROM ece_envelope")
    suspend fun getCount(): Int

    /**
     * 验证Size Class数据完整性
     */
    @Query("SELECT sizeClass FROM ece_envelope WHERE sizeClass NOT IN ('B1', 'B2', 'D', 'E')")
    suspend fun getInvalidSizeClasses(): List<String>

    /**
     * 根据假人代码获取适用的Envelope
     */
    suspend fun getByDummyCode(dummyCode: String): EceEnvelope? {
        val productGroup = when (dummyCode) {
            "Q0", "Q0+" -> "Group 0+"
            "Q1", "Q1.5" -> "Group I"
            "Q3" -> "Group I/II"
            "Q6" -> "Group II"
            "Q10" -> "Group III"
            else -> return null
        }
        return getByProductGroup(productGroup)
    }

    /**
     * 根据身高获取适用的Envelope
     */
    suspend fun getByHeight(heightCm: Int): EceEnvelope? {
        val productGroup = when (heightCm) {
            in 40..60 -> "Group 0+"
            in 60..87 -> "Group I"
            in 87..105 -> "Group I/II"
            in 105..125 -> "Group II"
            in 125..145 -> "Group III"
            else -> return null
        }
        return getByProductGroup(productGroup)
    }

    /**
     * 初始化标准Envelope数据
     */
    suspend fun initializeStandardEnvelopes() {
        insertAll(EceEnvelope.STANDARD_ENVELOPES)
    }
}

package com.childproduct.designassistant.data

import com.childproduct.designassistant.data.EceR129StandardDatabase
import com.childproduct.designassistant.data.model.*

/**
 * R129标准详细解析服务
 * 提供ECE R129标准的详细解析功能,包括假人规格、伤害判据等
 * 
 * 注意: 此服务正在重构中，部分功能尚未完成
 */
class R129StandardDetailsService {

    /**
     * 获取标准概述
     */
    fun getStandardOverview(): StandardBasicInfo {
        return EceR129StandardDatabase.getStandardInfo()
    }

    /**
     * 获取所有假人规格
     */
    fun getAllDummySpecs(): List<EceR129DummySpec> {
        return EceR129StandardDatabase.getAllDummySpecs()
    }

    /**
     * 根据假人类型获取详细规格
     */
    fun getDummySpecDetail(dummyType: String): EceR129DummySpec? {
        val type = try {
            EceR129DummyType.valueOf(dummyType)
        } catch (e: Exception) {
            return null
        }
        return EceR129StandardDatabase.getDummySpecByType(type)
    }

    /**
     * 根据身高范围获取适用假人
     */
    fun getApplicableDummiesByHeight(heightCm: Double): List<EceR129DummyType>? {
        return EceR129StandardDatabase.getDummiesByHeightRange(heightCm)
    }

    /**
     * 根据体重范围获取适用假人
     */
    fun getApplicableDummiesByWeight(weightKg: Double): List<EceR129DummyType>? {
        return EceR129StandardDatabase.getDummiesByWeightRange(weightKg)
    }

    /**
     * 获取假人的伤害判据
     */
    fun getInjuryCriteriaByDummy(dummyType: String): List<EceR129InjuryCriteria>? {
        val type = try {
            EceR129DummyType.valueOf(dummyType)
        } catch (e: Exception) {
            return null
        }
        return EceR129StandardDatabase.getInjuryCriteriaByDummy(type)
    }

    /**
     * 获取假人的尺寸阈值
     */
    fun getDimensionThresholdsByDummy(dummyType: String): List<EceR129DimensionThreshold>? {
        val type = try {
            EceR129DummyType.valueOf(dummyType)
        } catch (e: Exception) {
            return null
        }
        return EceR129StandardDatabase.getDimensionThresholdsByDummy(type)
    }

    /**
     * 获取假人的测试矩阵
     */
    fun getTestMatrixByDummy(dummyType: String): List<EceR129TestMatrix>? {
        val type = try {
            EceR129DummyType.valueOf(dummyType)
        } catch (e: Exception) {
            return null
        }
        return EceR129StandardDatabase.getTestMatrixByDummy(type)
    }

    // TODO: 实现其他方法 (防旋转装置、碰撞测试曲线、材料标准等)
}

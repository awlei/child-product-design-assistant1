package com.childproduct.designassistant.model.anthropometry

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.childproduct.designassistant.model.engineering.InstallDirection

/**
 * 假人规格实体
 * 数据来源：UN R129 Annex 6, FMVSS 213 S9.3
 * 标准参考：Q0-Q10假人规格 + US ATD映射
 */
@Entity(tableName = "dummy_specification")
data class DummySpecification(
    @PrimaryKey val dummyId: String,            // DUMMY_Q0, DUMMY_Q1, DUMMY_Q3S, DUMMY_Q6, DUMMY_Q10...
    val dummyType: DummyType,                   // EU_Q_SERIES, US_CRABI, US_HIII
    val dummyCode: String,                      // Q0, Q1.5, CRABI_18M...
    val minHeightCm: Double,                    // 最小身高(cm)
    val maxHeightCm: Double,                    // 最大身高(cm)
    val ageRange: String,                       // "0-6月", "4-6岁"...
    
    // 关键尺寸（来自GPS-028及UN R129 Annex 6）
    val headCircumferenceMm: Double,            // 头围(mm)
    val shoulderWidthMm: Double,                // 肩宽(mm)
    val sittingHeightMm: Double,                // 坐高(mm)
    val hipWidthMm: Double,                    // 臀宽(mm)
    
    // 安装方向要求
    val requiredDirection: InstallDirection,    // REARWARD (40-105cm) / FORWARD (105-150cm)
    
    // 标准来源
    val standardSource: String,                 // "UN R129 Annex 6", "FMVSS 213 S9.3"
    val revisionDate: String                    // "2021-11-28"
) {
    /**
     * 假人类型枚举
     */
    enum class DummyType {
        EU_Q_SERIES,    // 欧盟Q系列假人（UN R129）
        US_CRABI,       // 美国CRABI假人（FMVSS 213）
        US_HIII,        // 美国Hybrid III假人（FMVSS 213）
        OTHER
    }
    
    /**
     * 判断是否支持指定身高
     */
    fun supportsHeight(heightCm: Double): Boolean {
        return heightCm in minHeightCm..maxHeightCm
    }
    
    /**
     * 获取显示名称
     */
    fun getDisplayName(): String {
        return "$dummyCode ($ageRange, ${minHeightCm.toInt()}-${maxHeightCm.toInt()}cm)"
    }
    
    companion object {
        /**
         * GPS-028标准假人映射
         * 包含EU Q-Series和US ATD完整列表
         */
        val STANDARD_DUMMIES = listOf(
            // ==================== EU Q-Series (UN R129) ====================
            // Q0: 0-6个月婴儿，强制后向安装
            DummySpecification(
                dummyId = "DUMMY_Q0",
                dummyType = DummyType.EU_Q_SERIES,
                dummyCode = "Q0",
                minHeightCm = 40.0,
                maxHeightCm = 50.0,
                ageRange = "0-6月",
                headCircumferenceMm = 360.0,
                shoulderWidthMm = 145.0,
                sittingHeightMm = 255.0,
                hipWidthMm = 180.0,
                requiredDirection = InstallDirection.REARWARD,
                standardSource = "UN R129 Annex 6",
                revisionDate = "2021-11-28"
            ),
            
            // Q0+: 6-12个月婴儿，强制后向安装
            DummySpecification(
                dummyId = "DUMMY_Q0_PLUS",
                dummyType = DummyType.EU_Q_SERIES,
                dummyCode = "Q0+",
                minHeightCm = 50.0,
                maxHeightCm = 60.0,
                ageRange = "6-12月",
                headCircumferenceMm = 400.0,
                shoulderWidthMm = 165.0,
                sittingHeightMm = 295.0,
                hipWidthMm = 210.0,
                requiredDirection = InstallDirection.REARWARD,
                standardSource = "UN R129 Annex 6",
                revisionDate = "2021-11-28"
            ),
            
            // Q1: 1-2岁幼儿，强制后向安装
            DummySpecification(
                dummyId = "DUMMY_Q1",
                dummyType = DummyType.EU_Q_SERIES,
                dummyCode = "Q1",
                minHeightCm = 60.0,
                maxHeightCm = 75.0,
                ageRange = "1-2岁",
                headCircumferenceMm = 440.0,
                shoulderWidthMm = 195.0,
                sittingHeightMm = 345.0,
                hipWidthMm = 245.0,
                requiredDirection = InstallDirection.REARWARD,
                standardSource = "UN R129 Annex 6",
                revisionDate = "2021-11-28"
            ),
            
            // Q1.5: 2-3岁幼儿，强制后向安装
            DummySpecification(
                dummyId = "DUMMY_Q1_5",
                dummyType = DummyType.EU_Q_SERIES,
                dummyCode = "Q1.5",
                minHeightCm = 75.0,
                maxHeightCm = 87.0,
                ageRange = "2-3岁",
                headCircumferenceMm = 470.0,
                shoulderWidthMm = 225.0,
                sittingHeightMm = 385.0,
                hipWidthMm = 275.0,
                requiredDirection = InstallDirection.REARWARD,
                standardSource = "UN R129 Annex 6",
                revisionDate = "2021-11-28"
            ),
            
            // Q3: 3-4岁儿童，后向或前向安装（推荐后向）
            DummySpecification(
                dummyId = "DUMMY_Q3",
                dummyType = DummyType.EU_Q_SERIES,
                dummyCode = "Q3",
                minHeightCm = 87.0,
                maxHeightCm = 105.0,
                ageRange = "3-4岁",
                headCircumferenceMm = 500.0,
                shoulderWidthMm = 260.0,
                sittingHeightMm = 430.0,
                hipWidthMm = 310.0,
                requiredDirection = InstallDirection.REARWARD,
                standardSource = "UN R129 Annex 6",
                revisionDate = "2021-11-28"
            ),
            
            // Q3s: 4-6岁儿童，强制前向安装，必须使用Top-tether（重要！）
            DummySpecification(
                dummyId = "DUMMY_Q3S",
                dummyType = DummyType.EU_Q_SERIES,
                dummyCode = "Q3s",
                minHeightCm = 105.0,
                maxHeightCm = 125.0,
                ageRange = "4-6岁",
                headCircumferenceMm = 530.0,
                shoulderWidthMm = 295.0,
                sittingHeightMm = 480.0,
                hipWidthMm = 345.0,
                requiredDirection = InstallDirection.FORWARD,
                standardSource = "UN R129 Annex 6",
                revisionDate = "2021-11-28"
            ),
            
            // Q6: 6-10岁儿童，前向安装，必须使用Top-tether
            DummySpecification(
                dummyId = "DUMMY_Q6",
                dummyType = DummyType.EU_Q_SERIES,
                dummyCode = "Q6",
                minHeightCm = 125.0,
                maxHeightCm = 145.0,
                ageRange = "6-10岁",
                headCircumferenceMm = 560.0,
                shoulderWidthMm = 335.0,
                sittingHeightMm = 520.0,
                hipWidthMm = 380.0,
                requiredDirection = InstallDirection.FORWARD,
                standardSource = "UN R129 Annex 6",
                revisionDate = "2021-11-28"
            ),
            
            // Q10: 10-12岁儿童，前向安装，必须使用Top-tether
            DummySpecification(
                dummyId = "DUMMY_Q10",
                dummyType = DummyType.EU_Q_SERIES,
                dummyCode = "Q10",
                minHeightCm = 145.0,
                maxHeightCm = 150.0,
                ageRange = "10-12岁",
                headCircumferenceMm = 590.0,
                shoulderWidthMm = 370.0,
                sittingHeightMm = 560.0,
                hipWidthMm = 410.0,
                requiredDirection = InstallDirection.FORWARD,
                standardSource = "UN R129 Annex 6",
                revisionDate = "2021-11-28"
            ),
            
            // ==================== US ATD (FMVSS 213) ====================
            // CRABI 12月：12个月婴儿
            DummySpecification(
                dummyId = "DUMMY_CRABI_12M",
                dummyType = DummyType.US_CRABI,
                dummyCode = "CRABI_12M",
                minHeightCm = 65.0,
                maxHeightCm = 75.0,
                ageRange = "12月",
                headCircumferenceMm = 440.0,
                shoulderWidthMm = 190.0,
                sittingHeightMm = 340.0,
                hipWidthMm = 240.0,
                requiredDirection = InstallDirection.REARWARD,
                standardSource = "FMVSS 213 S9.3",
                revisionDate = "2021-11-28"
            ),
            
            // Hybrid III 3岁：3岁儿童
            DummySpecification(
                dummyId = "DUMMY_HIII_3YR",
                dummyType = DummyType.US_HIII,
                dummyCode = "HIII_3YR",
                minHeightCm = 95.0,
                maxHeightCm = 110.0,
                ageRange = "3岁",
                headCircumferenceMm = 500.0,
                shoulderWidthMm = 255.0,
                sittingHeightMm = 425.0,
                hipWidthMm = 305.0,
                requiredDirection = InstallDirection.FORWARD,
                standardSource = "FMVSS 213 S9.3",
                revisionDate = "2021-11-28"
            ),
            
            // Hybrid III 6岁：6岁儿童
            DummySpecification(
                dummyId = "DUMMY_HIII_6YR",
                dummyType = DummyType.US_HIII,
                dummyCode = "HIII_6YR",
                minHeightCm = 115.0,
                maxHeightCm = 125.0,
                ageRange = "6岁",
                headCircumferenceMm = 540.0,
                shoulderWidthMm = 290.0,
                sittingHeightMm = 475.0,
                hipWidthMm = 340.0,
                requiredDirection = InstallDirection.FORWARD,
                standardSource = "FMVSS 213 S9.3",
                revisionDate = "2021-11-28"
            ),
            
            // Hybrid III 10岁：10岁儿童
            DummySpecification(
                dummyId = "DUMMY_HIII_10YR",
                dummyType = DummyType.US_HIII,
                dummyCode = "HIII_10YR",
                minHeightCm = 135.0,
                maxHeightCm = 150.0,
                ageRange = "10岁",
                headCircumferenceMm = 580.0,
                shoulderWidthMm = 330.0,
                sittingHeightMm = 530.0,
                hipWidthMm = 375.0,
                requiredDirection = InstallDirection.FORWARD,
                standardSource = "FMVSS 213 S9.3",
                revisionDate = "2021-11-28"
            )
        )
        
        /**
         * 根据身高范围获取适用的假人列表
         * @param minHeightCm 最小身高(cm)
         * @param maxHeightCm 最大身高(cm)
         * @return 适用的假人列表
         */
        fun getApplicableDummies(minHeightCm: Double, maxHeightCm: Double): List<DummySpecification> {
            return STANDARD_DUMMIES.filter { dummy ->
                // 假人范围与输入范围有重叠
                !(dummy.maxHeightCm < minHeightCm || dummy.minHeightCm > maxHeightCm)
            }.sortedBy { it.minHeightCm }
        }
        
        /**
         * 根据假人代码获取假人规格
         */
        fun getByCode(code: String): DummySpecification? {
            return STANDARD_DUMMIES.find { it.dummyCode.equals(code, ignoreCase = true) }
        }
    }
}

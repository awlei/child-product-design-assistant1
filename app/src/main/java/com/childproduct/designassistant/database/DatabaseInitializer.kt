package com.childproduct.designassistant.database

import android.content.Context
import com.childproduct.designassistant.database.dao.*
import com.childproduct.designassistant.database.entity.*
import com.childproduct.designassistant.model.InstallDirection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 数据库初始化器（重构版 - 支持标准隔离）
 * 
 * 修复说明：
 * - initEceR129Database: 仅初始化ECE R129标准的假人，插入到EceR129Database
 * - initFmvssDatabase: 仅初始化FMVSS 213标准的假人，插入到FMVSSDatabase
 * - initializeAllDatabases: 对外统一初始化方法，分别初始化两个数据库
 * 
 * 彻底隔离：每个数据库只包含对应标准的假人数据，避免混用
 */
class DatabaseInitializer(private val context: Context) {

    // ========== ECE R129数据库专属初始化 ==========

    /**
     * 初始化ECE R129数据库
     * 仅插入ECE R129标准的假人，不含任何FMVSS假人
     */
    suspend fun initEceR129Database(eceDb: EceR129Database) {
        withContext(Dispatchers.IO) {
            try {
                // 1. 清空ECE数据库的假人表（避免旧数据污染）
                eceDb.crashTestDummyDao().deleteAll()
                
                // 2. 仅插入ECE R129标准的假人（standardType = "ECE_R129"）
                val eceDummies = getEceR129StandardDummies()
                eceDb.crashTestDummyDao().insertAll(eceDummies)

                // 3. 初始化ECE专属数据（Envelope/阈值等）
                initEceEnvelopes(eceDb.eceEnvelopeDao())
                initEceSafetyThresholds(eceDb.safetyThresholdDao())
                initEceTestConfigurations(eceDb.testConfigurationDao())
                initEceStandardReference(eceDb.standardReferenceDao())
                initEceInstallationMethods(eceDb.installationMethodDao())
                initEceIsofixRequirements(eceDb.isofixRequirementDao())
                initEceMaterialSpecifications(eceDb.materialSpecificationDao())
                initEceHeightRangeMappings(eceDb.heightRangeMappingDao())

                // 4. 记录初始化日志
                val initLog = StandardUpdateLog(
                    logId = "INIT_ECE_${System.currentTimeMillis()}",
                    regulationNumber = "UN R129",
                    version = "Rev.5",
                    syncType = "INITIAL",
                    status = "SUCCESS",
                    timestamp = System.currentTimeMillis(),
                    details = "ECE数据库初始化完成，包含${eceDummies.size}个ECE标准假人（Q0/Q0+/Q1/Q1.5/Q3/Q6/Q10）"
                )
                eceDb.standardUpdateLogDao().insert(initLog)

            } catch (e: Exception) {
                val errorLog = StandardUpdateLog(
                    logId = "INIT_ECE_ERROR_${System.currentTimeMillis()}",
                    regulationNumber = "UN R129",
                    version = "Rev.5",
                    syncType = "INITIAL",
                    status = "FAILED",
                    timestamp = System.currentTimeMillis(),
                    details = "ECE数据库初始化失败: ${e.message}"
                )
                eceDb.standardUpdateLogDao().insert(errorLog)
                throw e
            }
        }
    }

    // ========== FMVSS数据库专属初始化 ==========

    /**
     * 初始化FMVSS数据库
     * 仅插入FMVSS 213标准的假人，不含任何ECE假人
     */
    suspend fun initFmvssDatabase(fmvssDb: FMVSSDatabase) {
        withContext(Dispatchers.IO) {
            try {
                // 1. 清空FMVSS数据库的假人表（避免旧数据污染）
                fmvssDb.fmvssDao().deleteAllDummies()
                
                // 2. 仅插入FMVSS 213标准的假人（standardType = "FMVSS_213"）
                val fmvssDummies = getFmvssStandardDummies()
                fmvssDb.fmvssDao().insertDummies(fmvssDummies)

                // 3. 初始化FMVSS专属数据
                initFmvssStandards(fmvssDb.fmvssDao())
                initFmvssThresholds(fmvssDb.fmvssDao())
                initFmvssTestConfigs(fmvssDb.fmvssDao())

                // 4. 记录初始化日志
                // 注意：FMVSSDatabase可能没有StandardUpdateLog表，这里简化处理
                val logMessage = "FMVSS数据库初始化完成，包含${fmvssDummies.size}个FMVSS标准假人（Q3s/HIII）"
                android.util.Log.d("DatabaseInitializer", logMessage)

            } catch (e: Exception) {
                android.util.Log.e("DatabaseInitializer", "FMVSS数据库初始化失败: ${e.message}")
                throw e
            }
        }
    }

    // ========== 对外统一初始化接口 ==========

    /**
     * 初始化所有数据库
     * 分别初始化ECE和FMVSS数据库，确保数据完全隔离
     */
    suspend fun initializeAllDatabases() {
        val eceDb = EceR129Database.getDatabase(context)
        val fmvssDb = FMVSSDatabase.getDatabase(context)
        
        // 分别初始化，数据完全隔离
        initEceR129Database(eceDb)
        initFmvssDatabase(fmvssDb)
    }

    // ========== ECE R129标准假人数据 ==========

    /**
     * 获取ECE R129标准的假人列表（仅包含7个标准Q假人）
     */
    private fun getEceR129StandardDummies(): List<CrashTestDummy> {
        return listOf(
            CrashTestDummy(
                dummyId = "DUMMY_Q0",
                dummyCode = "Q0",
                dummyName = "新生儿",
                minHeightCm = 40,
                maxHeightCm = 50,
                ageRange = "0-6个月",
                productGroup = "Group 0+",
                installDirection = InstallDirection.REARWARD,
                description = "Q0假人用于40-50cm身高范围新生儿",
                standardClause = "UN R129 Annex 19 §4.1",
                weightKg = 3.47,
                headCircumferenceMm = 360,
                shoulderWidthMm = 145,
                sittingHeightMm = 255,
                standardType = "ECE_R129"  // 强制标识为ECE标准
            ),
            CrashTestDummy(
                dummyId = "DUMMY_Q0_PLUS",
                dummyCode = "Q0+",
                dummyName = "婴儿",
                minHeightCm = 50,
                maxHeightCm = 60,
                ageRange = "3-15个月",
                productGroup = "Group 0+",
                installDirection = InstallDirection.REARWARD,
                description = "Q0+假人用于50-60cm身高范围婴儿，强制后向安装",
                standardClause = "UN R129 Annex 19 §4.2",
                weightKg = 7.0,
                headCircumferenceMm = 395,
                shoulderWidthMm = 160,
                sittingHeightMm = 285,
                standardType = "ECE_R129"
            ),
            CrashTestDummy(
                dummyId = "DUMMY_Q1",
                dummyCode = "Q1",
                dummyName = "幼儿",
                minHeightCm = 60,
                maxHeightCm = 75,
                ageRange = "9-18个月",
                productGroup = "Group I",
                installDirection = InstallDirection.REARWARD,
                description = "Q1假人用于60-75cm身高范围幼儿",
                standardClause = "UN R129 Annex 19 §4.3",
                weightKg = 9.0,
                headCircumferenceMm = 430,
                shoulderWidthMm = 175,
                sittingHeightMm = 315,
                standardType = "ECE_R129"
            ),
            CrashTestDummy(
                dummyId = "DUMMY_Q1_5",
                dummyCode = "Q1.5",
                dummyName = "学步儿童",
                minHeightCm = 75,
                maxHeightCm = 87,
                ageRange = "18-36个月",
                productGroup = "Group I",
                installDirection = InstallDirection.REARWARD,
                description = "Q1.5假人用于75-87cm身高范围学步儿童，强制后向安装",
                standardClause = "UN R129 Annex 19 §4.4",
                weightKg = 11.0,
                headCircumferenceMm = 455,
                shoulderWidthMm = 185,
                sittingHeightMm = 340,
                standardType = "ECE_R129"
            ),
            CrashTestDummy(
                dummyId = "DUMMY_Q3",
                dummyCode = "Q3",
                dummyName = "幼童",
                minHeightCm = 87,
                maxHeightCm = 105,
                ageRange = "36-48个月",
                productGroup = "Group I/II",
                installDirection = InstallDirection.REARWARD,
                description = "Q3假人用于87-105cm身高范围幼童，可后向或前向安装",
                standardClause = "UN R129 Annex 19 §4.5",
                weightKg = 15.0,
                headCircumferenceMm = 485,
                shoulderWidthMm = 200,
                sittingHeightMm = 370,
                standardType = "ECE_R129"
            ),
            CrashTestDummy(
                dummyId = "DUMMY_Q6",
                dummyCode = "Q6",
                dummyName = "学龄儿童",
                minHeightCm = 105,
                maxHeightCm = 125,
                ageRange = "48-72个月",
                productGroup = "Group II",
                installDirection = InstallDirection.FORWARD,
                description = "Q6假人用于105-125cm身高范围学龄儿童，前向安装",
                standardClause = "UN R129 Annex 19 §4.6",
                weightKg = 21.0,
                headCircumferenceMm = 540,
                shoulderWidthMm = 280,
                sittingHeightMm = 445,
                standardType = "ECE_R129"
            ),
            CrashTestDummy(
                dummyId = "DUMMY_Q10",
                dummyCode = "Q10",
                dummyName = "青少年",
                minHeightCm = 125,
                maxHeightCm = 145,
                ageRange = "72-144个月",
                productGroup = "Group III",
                installDirection = InstallDirection.FORWARD,
                description = "Q10假人用于125-145cm身高范围青少年，前向安装",
                standardClause = "UN R129 Annex 19 §4.7",
                weightKg = 35.58,
                headCircumferenceMm = 560,
                shoulderWidthMm = 335,
                sittingHeightMm = 473,
                standardType = "ECE_R129"
            )
        )
    }

    // ========== FMVSS 213标准假人数据 ==========

    /**
     * 获取FMVSS 213标准的假人列表（仅包含Q3s、HIII等FMVSS专用假人）
     */
    private fun getFmvssStandardDummies(): List<CrashTestDummy> {
        return listOf(
            CrashTestDummy(
                dummyId = "DUMMY_Q3s",
                dummyCode = "Q3s",
                dummyName = "Q3s假人（FMVSS专用）",
                minHeightCm = 90,
                maxHeightCm = 105,
                ageRange = "3-4岁",
                productGroup = "Group I/II",
                installDirection = InstallDirection.FORWARD,
                description = "Q3s假人用于FMVSS 213测试，90-105cm身高范围，前向安装",
                standardClause = "FMVSS 213 §571.213",
                weightKg = 15.5,
                headCircumferenceMm = 485,
                shoulderWidthMm = 200,
                sittingHeightMm = 370,
                standardType = "FMVSS_213"  // 强制标识为FMVSS标准
            ),
            CrashTestDummy(
                dummyId = "DUMMY_HIII",
                dummyCode = "HIII",
                dummyName = "Hybrid III假人",
                minHeightCm = 125,
                maxHeightCm = 145,
                ageRange = "6-10岁",
                productGroup = "Group III",
                installDirection = InstallDirection.FORWARD,
                description = "Hybrid III假人用于FMVSS 213测试，125-145cm身高范围，前向安装",
                standardClause = "FMVSS 213 §571.213",
                weightKg = 35.0,
                headCircumferenceMm = 560,
                shoulderWidthMm = 335,
                sittingHeightMm = 473,
                standardType = "FMVSS_213"
            )
        )
    }

    // ========== ECE专属数据初始化方法 ==========

    private suspend fun initEceEnvelopes(dao: EceEnvelopeDao) {
        dao.initializeStandardEnvelopes()
    }

    private suspend fun initEceSafetyThresholds(dao: SafetyThresholdDao) {
        SafetyThreshold.STANDARD_THRESHOLDS.forEach { threshold ->
            dao.insert(threshold)
        }
    }

    private suspend fun initEceTestConfigurations(dao: TestConfigurationDao) {
        TestConfiguration.getStandardConfigurations().forEach { config ->
            dao.insert(config)
        }
    }

    private suspend fun initEceStandardReference(dao: StandardReferenceDao) {
        val standardRef = StandardReference(
            referenceId = "REF_UN_R129_REV5",
            regulationNumber = "UN R129",
            regulationName = "Enhanced Child Restraint Systems used on board of motor vehicles",
            currentVersion = "Rev.5",
            entryIntoForceDate = "30 November 2022",
            officialUrl = "https://unece.org/transport/documents/2022/12/un-r129-rev5.pdf",
            status = "ACTIVE",
            relatedRegulations = listOf("UN R16", "UN R14", "UN R145", "UN R118.03"),
            lastUpdated = System.currentTimeMillis()
        )
        dao.insert(standardRef)
    }

    private suspend fun initEceInstallationMethods(dao: InstallationMethodDao) {
        InstallationMethod.STANDARD_METHODS.forEach { method ->
            dao.insert(method)
        }
    }

    private suspend fun initEceIsofixRequirements(dao: IsofixRequirementDao) {
        IsofixRequirement.STANDARD_REQUIREMENTS.forEach { requirement ->
            dao.insert(requirement)
        }
    }

    private suspend fun initEceMaterialSpecifications(dao: MaterialSpecificationDao) {
        MaterialSpecification.STANDARD_SPECIFICATIONS.forEach { spec ->
            dao.insert(spec)
        }
    }

    private suspend fun initEceHeightRangeMappings(dao: HeightRangeMappingDao) {
        HeightRangeMapping.STANDARD_MAPPINGS.forEach { mapping ->
            dao.insert(mapping)
        }
    }

    // ========== FMVSS专属数据初始化方法 ==========

    private suspend fun initFmvssStandards(dao: FMVSSDao) {
        // 初始化FMVSS标准实体
        val standard = com.childproduct.designassistant.database.entity.FMVSSStandardEntity(
            standardId = "FMVSS_213",
            standardName = "Federal Motor Vehicle Safety Standard 213",
            version = "213a",
            description = "儿童约束系统标准",
            effectiveDate = "2025-02-01",
            lastUpdated = System.currentTimeMillis()
        )
        dao.insertStandard(standard)
    }

    private suspend fun initFmvssThresholds(dao: FMVSSDao) {
        // 初始化FMVSS安全阈值
        val thresholdQ3s = com.childproduct.designassistant.database.entity.FMVSSThresholdEntity(
            thresholdId = "THRESH_Q3s",
            dummyCode = "Q3s",
            hicLimit = 1000,
            chestAccelerationGLimit = 60,
            chestDeflectionMmLimit = 40,
            headInjuryCriterionDescription = "头部损伤准则 ≤ 1000",
            chestAccelerationDescription = "胸部加速度 ≤ 60g",
            chestDeflectionDescription = "胸部偏转 ≤ 40mm",
            lastUpdated = System.currentTimeMillis()
        )
        dao.insertThreshold(thresholdQ3s)

        val thresholdHIII = com.childproduct.designassistant.database.entity.FMVSSThresholdEntity(
            thresholdId = "THRESH_HIII",
            dummyCode = "HIII",
            hicLimit = 1000,
            chestAccelerationGLimit = 60,
            chestDeflectionMmLimit = 40,
            headInjuryCriterionDescription = "头部损伤准则 ≤ 1000",
            chestAccelerationDescription = "胸部加速度 ≤ 60g",
            chestDeflectionDescription = "胸部偏转 ≤ 40mm",
            lastUpdated = System.currentTimeMillis()
        )
        dao.insertThreshold(thresholdHIII)
    }

    private suspend fun initFmvssTestConfigs(dao: FMVSSDao) {
        // 初始化FMVSS测试配置
        val configQ3s = com.childproduct.designassistant.database.entity.FMVSSTestConfigEntity(
            configId = "CONFIG_Q3s",
            dummyCode = "Q3s",
            testSpeedKmph = 48,  // 30mph
            testType = "Side Impact",
            installDirection = "FORWARD",
            description = "FMVSS 213侧碰测试，30mph",
            lastUpdated = System.currentTimeMillis()
        )
        dao.insertTestConfig(configQ3s)

        val configHIII = com.childproduct.designassistant.database.entity.FMVSSTestConfigEntity(
            configId = "CONFIG_HIII",
            dummyCode = "HIII",
            testSpeedKmph = 48,
            testType = "Frontal Impact",
            installDirection = "FORWARD",
            description = "FMVSS 213前碰测试，30mph",
            lastUpdated = System.currentTimeMillis()
        )
        dao.insertTestConfig(configHIII)
    }
}

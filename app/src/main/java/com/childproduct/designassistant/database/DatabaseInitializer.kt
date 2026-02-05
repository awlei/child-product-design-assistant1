package com.childproduct.designassistant.database

import android.content.Context
import com.childproduct.designassistant.database.dao.*
import com.childproduct.designassistant.database.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 数据库初始化器
 * 首次创建数据库时，初始化标准数据
 */
class DatabaseInitializer(private val context: Context) {

    fun execute(database: EceR129Database) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. 初始化假人数据
                CrashTestDummy.STANDARD_DUMMIES.forEach { dummy ->
                    database.crashTestDummyDao().insert(dummy)
                }

                // 2. 初始化Envelope数据
                database.eceEnvelopeDao().initializeStandardEnvelopes()

                // 3. 初始化安全阈值
                SafetyThreshold.STANDARD_THRESHOLDS.forEach { threshold ->
                    database.safetyThresholdDao().insert(threshold)
                }

                // 3. 初始化测试配置
                TestConfiguration.getStandardConfigurations().forEach { config ->
                    database.testConfigurationDao().insert(config)
                }

                // 4. 初始化标准引用
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
                database.standardReferenceDao().insert(standardRef)

                // 5. 初始化安装方式
                InstallationMethod.STANDARD_METHODS.forEach { method ->
                    database.installationMethodDao().insert(method)
                }

                // 6. 初始化ISOFIX要求
                IsofixRequirement.STANDARD_REQUIREMENTS.forEach { requirement ->
                    database.isofixRequirementDao().insert(requirement)
                }

                // 7. 初始化材料规格
                MaterialSpecification.STANDARD_SPECIFICATIONS.forEach { spec ->
                    database.materialSpecificationDao().insert(spec)
                }

                // 8. 初始化身高范围映射
                HeightRangeMapping.STANDARD_MAPPINGS.forEach { mapping ->
                    database.heightRangeMappingDao().insert(mapping)
                }

                // 9. 记录初始化日志
                val initLog = StandardUpdateLog(
                    logId = "INIT_${System.currentTimeMillis()}",
                    regulationNumber = "UN R129",
                    version = "Rev.5",
                    syncType = "INITIAL",
                    status = "SUCCESS",
                    timestamp = System.currentTimeMillis(),
                    details = "数据库初始化完成，包含${CrashTestDummy.STANDARD_DUMMIES.size}个假人类型，${database.eceEnvelopeDao().getCount()}个Envelope，${SafetyThreshold.STANDARD_THRESHOLDS.size}个安全阈值，${TestConfiguration.getStandardConfigurations().size}个测试配置"
                )
                database.standardUpdateLogDao().insert(initLog)

            } catch (e: Exception) {
                // 记录初始化失败日志
                val errorLog = StandardUpdateLog(
                    logId = "INIT_ERROR_${System.currentTimeMillis()}",
                    regulationNumber = "UN R129",
                    version = "Rev.5",
                    syncType = "INITIAL",
                    status = "FAILED",
                    timestamp = System.currentTimeMillis(),
                    details = "数据库初始化失败: ${e.message}"
                )
                database.standardUpdateLogDao().insert(errorLog)
            }
        }
    }
}

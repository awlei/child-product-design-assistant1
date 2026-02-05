package com.childproduct.designassistant

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.childproduct.designassistant.database.EceR129Database
import com.childproduct.designassistant.database.FMVSSDatabase
import com.childproduct.designassistant.database.dao.CrashTestDummyDao
import com.childproduct.designassistant.database.dao.FMVSSDao
import com.childproduct.designassistant.database.entity.CrashTestDummy
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/**
 * 标准隔离验证测试
 * 
 * 测试目标：
 * 1. 验证ECE数据库只包含ECE标准假人
 * 2. 验证FMVSS数据库只包含FMVSS标准假人
 * 3. 验证StandardRepository按标准路由查询
 * 4. 验证不会出现跨标准的数据混用
 * 
 * 预期结果：
 * - ECE数据库：7个假人（Q0/Q0+/Q1/Q1.5/Q3/Q6/Q10），standardType都是"ECE_R129"
 * - FMVSS数据库：2个假人（Q3s/HIII），standardType都是"FMVSS_213"
 * - 查询ECE标准：只返回ECE假人
 * - 查询FMVSS标准：只返回FMVSS假人
 */
@RunWith(AndroidJUnit4::class)
class StandardIsolationTest {

    private lateinit var context: Context
    private lateinit var eceDatabase: EceR129Database
    private lateinit var fmvssDatabase: FMVSSDatabase
    private lateinit var eceDao: CrashTestDummyDao
    private lateinit var fmvssDao: FMVSSDao

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<Context>()
        
        // 获取数据库实例
        eceDatabase = EceR129Database.getDatabase(context)
        fmvssDatabase = FMVSSDatabase.getDatabase(context)
        
        // 获取DAO
        eceDao = eceDatabase.crashTestDummyDao()
        fmvssDao = fmvssDatabase.fmvssDao()
        
        // 清空测试数据
        runBlocking {
            eceDao.deleteAll()
            fmvssDao.deleteAllDummies()
        }
    }

    @After
    fun tearDown() {
        // 清理测试数据
        runBlocking {
            eceDao.deleteAll()
            fmvssDao.deleteAllDummies()
        }
    }

    /**
     * 测试1：验证ECE数据库只包含ECE标准假人
     */
    @Test
    fun testEceDatabaseContainsOnlyEceDummies() = runBlocking {
        // 插入ECE假人
        val eceDummies = listOf(
            CrashTestDummy(
                dummyId = "DUMMY_Q0",
                dummyCode = "Q0",
                dummyName = "新生儿",
                minHeightCm = 40,
                maxHeightCm = 50,
                ageRange = "0-6个月",
                productGroup = "Group 0+",
                installDirection = com.childproduct.designassistant.model.InstallDirection.REARWARD,
                description = "Q0假人用于40-50cm身高范围新生儿",
                standardClause = "UN R129 Annex 19 §4.1",
                weightKg = 3.47,
                headCircumferenceMm = 360,
                shoulderWidthMm = 145,
                sittingHeightMm = 255,
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
                installDirection = com.childproduct.designassistant.model.InstallDirection.REARWARD,
                description = "Q3假人用于87-105cm身高范围幼童",
                standardClause = "UN R129 Annex 19 §4.5",
                weightKg = 15.0,
                headCircumferenceMm = 485,
                shoulderWidthMm = 200,
                sittingHeightMm = 370,
                standardType = "ECE_R129"
            )
        )
        eceDao.insertAll(eceDummies)

        // 插入FMVSS假人（模拟错误情况）
        val fmvssDummy = CrashTestDummy(
            dummyId = "DUMMY_Q3s",
            dummyCode = "Q3s",
            dummyName = "Q3s假人（FMVSS专用）",
            minHeightCm = 90,
            maxHeightCm = 105,
            ageRange = "3-4岁",
            productGroup = "Group I/II",
            installDirection = com.childproduct.designassistant.model.InstallDirection.FORWARD,
            description = "Q3s假人用于FMVSS 213测试",
            standardClause = "FMVSS 213 §571.213",
            weightKg = 15.5,
            headCircumferenceMm = 485,
            shoulderWidthMm = 200,
            sittingHeightMm = 370,
            standardType = "FMVSS_213"
        )
        eceDao.insertAll(listOf(fmvssDummy))

        // 查询ECE数据库的所有假人
        val allEceDummies = eceDao.getAllDummiesList()

        // 验证：所有假人的standardType都应该是"ECE_R129"
        allEceDummies.forEach { dummy ->
            assertEquals(
                "ECE数据库中的假人${dummy.dummyCode}的标准类型应该是ECE_R129",
                "ECE_R129",
                dummy.standardType
            )
        }

        // 查询ECE标准假人（使用新的过滤方法）
        val eceOnlyDummies = eceDao.getDummiesByStandard("ECE_R129")
        
        // 验证：只返回ECE假人
        assertEquals("ECE标准查询应该只返回ECE假人", 2, eceOnlyDummies.size)
        eceOnlyDummies.forEach { dummy ->
            assertEquals("ECE标准查询返回的假人${dummy.dummyCode}的标准类型应该是ECE_R129", "ECE_R129", dummy.standardType)
        }

        println("✅ 测试1通过：ECE数据库只包含ECE标准假人")
    }

    /**
     * 测试2：验证FMVSS数据库只包含FMVSS标准假人
     */
    @Test
    fun testFmvssDatabaseContainsOnlyFmvssDummies() = runBlocking {
        // 插入FMVSS假人
        val fmvssDummies = listOf(
            CrashTestDummy(
                dummyId = "DUMMY_Q3s",
                dummyCode = "Q3s",
                dummyName = "Q3s假人（FMVSS专用）",
                minHeightCm = 90,
                maxHeightCm = 105,
                ageRange = "3-4岁",
                productGroup = "Group I/II",
                installDirection = com.childproduct.designassistant.model.InstallDirection.FORWARD,
                description = "Q3s假人用于FMVSS 213测试",
                standardClause = "FMVSS 213 §571.213",
                weightKg = 15.5,
                headCircumferenceMm = 485,
                shoulderWidthMm = 200,
                sittingHeightMm = 370,
                standardType = "FMVSS_213"
            ),
            CrashTestDummy(
                dummyId = "DUMMY_HIII",
                dummyCode = "HIII",
                dummyName = "Hybrid III假人",
                minHeightCm = 125,
                maxHeightCm = 145,
                ageRange = "6-10岁",
                productGroup = "Group III",
                installDirection = com.childproduct.designassistant.model.InstallDirection.FORWARD,
                description = "Hybrid III假人用于FMVSS 213测试",
                standardClause = "FMVSS 213 §571.213",
                weightKg = 35.0,
                headCircumferenceMm = 560,
                shoulderWidthMm = 335,
                sittingHeightMm = 473,
                standardType = "FMVSS_213"
            )
        )
        fmvssDao.insertDummies(fmvssDummies)

        // 查询FMVSS数据库的所有假人
        val allFmvssDummies = fmvssDao.getAllDummies()

        // 验证：所有假人的standardType都应该是"FMVSS_213"
        allFmvssDummies.forEach { dummy ->
            assertEquals(
                "FMVSS数据库中的假人${dummy.dummyCode}的标准类型应该是FMVSS_213",
                "FMVSS_213",
                dummy.standardType
            )
        }

        // 验证：只返回2个假人
        assertEquals("FMVSS数据库应该只包含2个假人（Q3s/HIII）", 2, allFmvssDummies.size)

        println("✅ 测试2通过：FMVSS数据库只包含FMVSS标准假人")
    }

    /**
     * 测试3：验证按标准+身高查询（核心功能）
     */
    @Test
    fun testGetDummyByStandardAndHeight() = runBlocking {
        // 插入ECE假人
        val eceDummy = CrashTestDummy(
            dummyId = "DUMMY_Q3",
            dummyCode = "Q3",
            dummyName = "幼童",
            minHeightCm = 87,
            maxHeightCm = 105,
            ageRange = "36-48个月",
            productGroup = "Group I/II",
            installDirection = com.childproduct.designassistant.model.InstallDirection.REARWARD,
            description = "Q3假人用于87-105cm身高范围幼童",
            standardClause = "UN R129 Annex 19 §4.5",
            weightKg = 15.0,
            headCircumferenceMm = 485,
            shoulderWidthMm = 200,
            sittingHeightMm = 370,
            standardType = "ECE_R129"
        )
        eceDao.insertAll(listOf(eceDummy))

        // 插入FMVSS假人（相同身高范围）
        val fmvssDummy = CrashTestDummy(
            dummyId = "DUMMY_Q3s",
            dummyCode = "Q3s",
            dummyName = "Q3s假人（FMVSS专用）",
            minHeightCm = 90,
            maxHeightCm = 105,
            ageRange = "3-4岁",
            productGroup = "Group I/II",
            installDirection = com.childproduct.designassistant.model.InstallDirection.FORWARD,
            description = "Q3s假人用于FMVSS 213测试",
            standardClause = "FMVSS 213 §571.213",
            weightKg = 15.5,
            headCircumferenceMm = 485,
            shoulderWidthMm = 200,
            sittingHeightMm = 370,
            standardType = "FMVSS_213"
        )
        eceDao.insertAll(listOf(fmvssDummy))

        // 查询ECE标准，身高90cm
        val eceResult = eceDao.getDummyByStandardAndHeight("ECE_R129", 90)
        
        // 验证：应该返回ECE假人（Q3）
        assertNotNull("查询ECE标准应该返回假人", eceResult)
        assertEquals("查询ECE标准应该返回Q3假人", "Q3", eceResult!!.dummyCode)
        assertEquals("返回假人的标准类型应该是ECE_R129", "ECE_R129", eceResult.standardType)

        // 查询FMVSS标准，身高90cm（应该在FMVSS数据库中查询）
        val fmvssResult = fmvssDao.getDummyByHeight(90)
        
        // 验证：应该返回FMVSS假人（Q3s）
        assertNotNull("查询FMVSS标准应该返回假人", fmvssResult)
        assertEquals("查询FMVSS标准应该返回Q3s假人", "Q3s", fmvssResult!!.dummyCode)
        assertEquals("返回假人的标准类型应该是FMVSS_213", "FMVSS_213", fmvssResult.standardType)

        println("✅ 测试3通过：按标准+身高查询正确隔离了ECE和FMVSS假人")
    }

    /**
     * 测试4：验证不会出现跨标准的数据混用
     */
    @Test
    fun testNoCrossStandardMixing() = runBlocking {
        // 插入ECE假人
        val eceDummies = listOf(
            CrashTestDummy(
                dummyId = "DUMMY_Q0",
                dummyCode = "Q0",
                dummyName = "新生儿",
                minHeightCm = 40,
                maxHeightCm = 50,
                ageRange = "0-6个月",
                productGroup = "Group 0+",
                installDirection = com.childproduct.designassistant.model.InstallDirection.REARWARD,
                description = "Q0假人用于40-50cm身高范围新生儿",
                standardClause = "UN R129 Annex 19 §4.1",
                weightKg = 3.47,
                headCircumferenceMm = 360,
                shoulderWidthMm = 145,
                sittingHeightMm = 255,
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
                installDirection = com.childproduct.designassistant.model.InstallDirection.REARWARD,
                description = "Q3假人用于87-105cm身高范围幼童",
                standardClause = "UN R129 Annex 19 §4.5",
                weightKg = 15.0,
                headCircumferenceMm = 485,
                shoulderWidthMm = 200,
                sittingHeightMm = 370,
                standardType = "ECE_R129"
            )
        )
        eceDao.insertAll(eceDummies)

        // 插入FMVSS假人到FMVSS数据库
        val fmvssDummies = listOf(
            CrashTestDummy(
                dummyId = "DUMMY_Q3s",
                dummyCode = "Q3s",
                dummyName = "Q3s假人（FMVSS专用）",
                minHeightCm = 90,
                maxHeightCm = 105,
                ageRange = "3-4岁",
                productGroup = "Group I/II",
                installDirection = com.childproduct.designassistant.model.InstallDirection.FORWARD,
                description = "Q3s假人用于FMVSS 213测试",
                standardClause = "FMVSS 213 §571.213",
                weightKg = 15.5,
                headCircumferenceMm = 485,
                shoulderWidthMm = 200,
                sittingHeightMm = 370,
                standardType = "FMVSS_213"
            ),
            CrashTestDummy(
                dummyId = "DUMMY_HIII",
                dummyCode = "HIII",
                dummyName = "Hybrid III假人",
                minHeightCm = 125,
                maxHeightCm = 145,
                ageRange = "6-10岁",
                productGroup = "Group III",
                installDirection = com.childproduct.designassistant.model.InstallDirection.FORWARD,
                description = "Hybrid III假人用于FMVSS 213测试",
                standardClause = "FMVSS 213 §571.213",
                weightKg = 35.0,
                headCircumferenceMm = 560,
                shoulderWidthMm = 335,
                sittingHeightMm = 473,
                standardType = "FMVSS_213"
            )
        )
        fmvssDao.insertDummies(fmvssDummies)

        // 查询ECE标准假人
        val eceOnlyDummies = eceDao.getDummiesByStandard("ECE_R129")
        
        // 验证：只返回ECE假人，不包含FMVSS假人
        assertEquals("ECE标准查询应该只返回2个ECE假人", 2, eceOnlyDummies.size)
        eceOnlyDummies.forEach { dummy ->
            assertTrue("ECE假人代码应该是Q0或Q3", dummy.dummyCode in listOf("Q0", "Q3"))
            assertEquals("ECE假人的标准类型应该是ECE_R129", "ECE_R129", dummy.standardType)
        }

        // 查询FMVSS标准假人
        val fmvssOnlyDummies = fmvssDao.getAllDummies()
        
        // 验证：只返回FMVSS假人，不包含ECE假人
        assertEquals("FMVSS标准查询应该只返回2个FMVSS假人", 2, fmvssOnlyDummies.size)
        fmvssOnlyDummies.forEach { dummy ->
            assertTrue("FMVSS假人代码应该是Q3s或HIII", dummy.dummyCode in listOf("Q3s", "HIII"))
            assertEquals("FMVSS假人的标准类型应该是FMVSS_213", "FMVSS_213", dummy.standardType)
        }

        println("✅ 测试4通过：没有出现跨标准的数据混用")
    }

    /**
     * 测试5：验证FMVSSDao强制过滤standardType
     */
    @Test
    fun testFmvssDaoForcesStandardTypeFilter() = runBlocking {
        // 在FMVSS数据库中插入ECE假人（模拟错误情况）
        val eceDummy = CrashTestDummy(
            dummyId = "DUMMY_Q0",
            dummyCode = "Q0",
            dummyName = "新生儿",
            minHeightCm = 40,
            maxHeightCm = 50,
            ageRange = "0-6个月",
            productGroup = "Group 0+",
            installDirection = com.childproduct.designassistant.model.InstallDirection.REARWARD,
            description = "Q0假人用于40-50cm身高范围新生儿",
            standardClause = "UN R129 Annex 19 §4.1",
            weightKg = 3.47,
            headCircumferenceMm = 360,
            shoulderWidthMm = 145,
            sittingHeightMm = 255,
            standardType = "ECE_R129"  // 错误：ECE假人插入到FMVSS数据库
        )
        fmvssDao.insertDummies(listOf(eceDummy))

        // 插入FMVSS假人
        val fmvssDummy = CrashTestDummy(
            dummyId = "DUMMY_Q3s",
            dummyCode = "Q3s",
            dummyName = "Q3s假人（FMVSS专用）",
            minHeightCm = 90,
            maxHeightCm = 105,
            ageRange = "3-4岁",
            productGroup = "Group I/II",
            installDirection = com.childproduct.designassistant.model.InstallDirection.FORWARD,
            description = "Q3s假人用于FMVSS 213测试",
            standardClause = "FMVSS 213 §571.213",
            weightKg = 15.5,
            headCircumferenceMm = 485,
            shoulderWidthMm = 200,
            sittingHeightMm = 370,
            standardType = "FMVSS_213"
        )
        fmvssDao.insertDummies(listOf(fmvssDummy))

        // 查询FMVSS假人（应该强制过滤standardType）
        val result = fmvssDao.getAllDummies()

        // 验证：只返回FMVSS假人，不返回ECE假人
        assertEquals("FMVSSDao应该强制过滤，只返回FMVSS假人", 1, result.size)
        assertEquals("返回的假人应该是Q3s", "Q3s", result[0].dummyCode)
        assertEquals("返回假人的标准类型应该是FMVSS_213", "FMVSS_213", result[0].standardType)

        println("✅ 测试5通过：FMVSSDao强制过滤standardType")
    }
}

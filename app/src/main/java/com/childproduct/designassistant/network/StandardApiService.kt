package com.childproduct.designassistant.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 标准API服务接口
 * 从UNECE官方获取最新标准数据
 */
interface StandardApiService {

    /**
     * 获取标准元数据（版本、生效日期等）
     */
    @GET("api/regulations/{regNumber}/metadata")
    suspend fun getRegulationMetadata(
        @Path("regNumber") regNumber: Int
    ): RegulationMetadata

    /**
     * 获取标准修订历史
     */
    @GET("api/regulations/{regNumber}/amendments")
    suspend fun getAmendmentHistory(
        @Path("regNumber") regNumber: Int
    ): List<AmendmentRecord>

    /**
     * 获取测试配置（ROADMATE 360格式）
     */
    @GET("api/regulations/R129/test-matrix")
    suspend fun getTestMatrix(
        @Query("version") version: String = "Rev.4",
        @Query("format") format: String = "ROADMATE_360"
    ): TestMatrixResponse
}

/**
 * 标准元数据
 */
data class RegulationMetadata(
    val regulationNumber: String,      // "UN R129"
    val currentVersion: String,        // "Rev.4"
    val entryIntoForceDate: String,    // "29 December 2018"
    val latestAmendment: String?,      // "Supplement 3 to 02 series"
    val status: String,                // "ACTIVE"
    val officialUrl: String            // "https://unece.org/transport/documents/2021/03/regs129r4e.pdf"
)

/**
 * 修订记录
 */
data class AmendmentRecord(
    val amendmentId: String,           // "03_SERIES"
    val description: String,           // "03 series of amendments"
    val entryIntoForceDate: String,    // "29 December 2018"
    val changesSummary: String         // "Added Q3s dummy type for 105-125cm range"
)

/**
 * 测试矩阵响应
 */
data class TestMatrixResponse(
    val version: String,
    val lastUpdated: String,
    val configurations: List<TestConfigurationDto>
)

/**
 * 测试配置DTO
 */
data class TestConfigurationDto(
    val configId: String,
    val pulse: String,                 // "Frontal", "Rear", "Lateral"
    val impact: String,                // "Q0", "Q1", "Q3"...
    val dummy: String,
    val position: String,              // "Rearward facing", "Forward facing"
    val installation: String,          // "Isofix 3 pts", "Isofix 2 pts"
    val productConfiguration: String,  // "Upright", "Reclined"
    val isofixAnchors: Boolean,
    val harness: Boolean,
    val topTetherSupportLeg: String,   // "With", "Without", "no"
    val dashboard: Boolean,
    val testSpeedKmh: Double,
    val stoppingDistanceMm: Int
)

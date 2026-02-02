package com.childproduct.designassistant.model.engineering

import com.childproduct.designassistant.model.InstallDirection

/**
 * 测试矩阵 - ROADMATE 360标准格式
 * 修正：Impact列填假人类型（Q0/Q1/Q3/Q3s/Q6/Q10），非碰撞方向
 */
data class RoadmateTestMatrix(
    val rows: List<TestMatrixRow>,
    val metadata: TestMatrixMetadata
) {
    companion object {
        /**
         * 生成ROADMATE 360格式的测试矩阵
         * @param dummyTypes 假人类型列表
         * @param installMethod 安装方法
         * @param standard 适用的标准
         * @return 测试矩阵
         */
        fun generate(
            dummyTypes: List<DummyType>,
            installMethod: InstallMethod?,
            standard: Standard
        ): RoadmateTestMatrix {
            val rows = mutableListOf<TestMatrixRow>()

            dummyTypes.forEach { dummy ->
                // 为每个假人生成正面碰撞测试
                rows.add(createFrontalImpactRow(dummy, installMethod, standard))
                
                // 为特定假人生成后向和侧面碰撞测试
                if (dummy == DummyType.Q0 || dummy == DummyType.Q6) {
                    rows.add(createRearImpactRow(dummy, installMethod, standard))
                }
                if (dummy == DummyType.Q0 || dummy == DummyType.Q6) {
                    rows.add(createLateralImpactRow(dummy, installMethod, standard))
                }
            }

            return RoadmateTestMatrix(
                rows = rows,
                metadata = TestMatrixMetadata(
                    generatedAt = System.currentTimeMillis(),
                    standard = standard.code,
                    installMethod = installMethod?.displayName ?: "N/A",
                    rowCount = rows.size,
                    dummyCoverage = dummyTypes.map { it.code }.joinToString(" → ")
                )
            )
        }

        /**
         * 创建正面碰撞测试行
         */
        private fun createFrontalImpactRow(
            dummy: DummyType,
            installMethod: InstallMethod?,
            standard: Standard
        ): TestMatrixRow {
            return TestMatrixRow(
                sample = standard.code,
                pulse = "Frontal",
                impact = dummy.code,           // 修正：假人类型，非碰撞方向
                dummyType = dummy.code,       // ROADMATE 360双列冗余要求
                position = dummy.installDirection.displayName,
                installation = installMethod?.displayName ?: "Isofix 3 pts",
                specificInstallation = "-",
                productConfiguration = "Upright",
                isofixAnchors = "yes",
                positionOfFloor = "Low",
                harness = "With",
                topTetherOrSupportLeg = getAntiRotationType(dummy),
                dashboard = "With",
                comments = "if contact repeat without dashboard",
                buckle = "no",
                adjuster = "no",
                isofix = "yes",
                topTether = getTopTetherFlag(dummy),
                quantity = "n/a",
                testNumber = "-"
            )
        }

        /**
         * 创建后向碰撞测试行
         */
        private fun createRearImpactRow(
            dummy: DummyType,
            installMethod: InstallMethod?,
            standard: Standard
        ): TestMatrixRow {
            return TestMatrixRow(
                sample = standard.code,
                pulse = "Rear",
                impact = dummy.code,
                dummyType = dummy.code,
                position = dummy.installDirection.displayName,
                installation = installMethod?.displayName ?: "Isofix 3 pts",
                specificInstallation = "-",
                productConfiguration = "Upright",
                isofixAnchors = "yes",
                positionOfFloor = "Low",
                harness = "With",
                topTetherOrSupportLeg = getAntiRotationType(dummy),
                dashboard = "With",
                comments = "-",
                buckle = "no",
                adjuster = "no",
                isofix = "yes",
                topTether = "no",
                quantity = "n/a",
                testNumber = "-"
            )
        }

        /**
         * 创建侧面碰撞测试行
         */
        private fun createLateralImpactRow(
            dummy: DummyType,
            installMethod: InstallMethod?,
            standard: Standard
        ): TestMatrixRow {
            return TestMatrixRow(
                sample = standard.code,
                pulse = "Lateral",
                impact = dummy.code,
                dummyType = dummy.code,
                position = dummy.installDirection.displayName,
                installation = installMethod?.displayName ?: "Isofix 3 pts",
                specificInstallation = "-",
                productConfiguration = "Upright",
                isofixAnchors = "yes",
                positionOfFloor = "Low",
                harness = "With",
                topTetherOrSupportLeg = getAntiRotationType(dummy),
                dashboard = "no",
                comments = "-",
                buckle = "no",
                adjuster = "no",
                isofix = "yes",
                topTether = "no",
                quantity = "n/a",
                testNumber = "-"
            )
        }

        /**
         * 获取防旋转装置类型
         */
        private fun getAntiRotationType(dummy: DummyType): String {
            return when (dummy.antiRotationType) {
                AntiRotationType.SUPPORT_LEG -> "With"  // 支撑腿
                AntiRotationType.TOP_TETHER -> "With"  // Top-tether
                null -> "no"
            }
        }

        /**
         * 获取Top-tether测试标志
         */
        private fun getTopTetherFlag(dummy: DummyType): String {
            return when (dummy.antiRotationType) {
                AntiRotationType.TOP_TETHER -> "yes"  // 前向安装必须为"yes"
                AntiRotationType.SUPPORT_LEG -> "no"
                null -> "no"
            }
        }
    }

    /**
     * 生成CSV格式（可直接导入ROADMATE 360）
     */
    fun toCsv(): String {
        val header = "Sample,Pulse,Impact,Dummy,Position,Installation,Specific Installation," +
                      "Product Configuration,Isofix anchors,Position of floor,Harness," +
                      "Top Tether / Support leg,Dashboard,Comments,Buckle,Adjuster," +
                      "Isofix,TT,QUANTITY,Testn°"
        
        val rows = rows.joinToString("\n") { row ->
            "${row.sample},${row.pulse},${row.impact},${row.dummyType}," +
            "${row.position},${row.installation},${row.specificInstallation}," +
            "${row.productConfiguration},${row.isofixAnchors},${row.positionOfFloor}," +
            "${row.harness},${row.topTetherOrSupportLeg},${row.dashboard}," +
            "${row.comments},${row.buckle},${row.adjuster},${row.isofix}," +
            "${row.topTether},${row.quantity},${row.testNumber}"
        }
        
        return "$header\n$rows"
    }

    /**
     * 生成Markdown表格
     */
    fun toMarkdown(): String {
        val header = "| Sample | Pulse | Impact | Dummy | Position | Installation | Harness | Top Tether / Support leg | TT |"
        val separator = "|--------|-------|--------|-------|----------|--------------|---------|-------------------------|----|"
        
        val rows = rows.joinToString("\n") { row ->
            "| ${row.sample} | ${row.pulse} | ${row.impact} | ${row.dummyType} | " +
            "${row.position} | ${row.installation} | ${row.harness} | " +
            "${row.topTetherOrSupportLeg} | ${row.topTether} |"
        }
        
        return "$header\n$separator\n$rows"
    }
}

/**
 * 测试矩阵行（ROADMATE 360格式：20列）
 */
data class TestMatrixRow(
    val sample: String,                    // 标准代码（如 R129, FMVSS）
    val pulse: String,                     // 碰撞类型（Frontal, Rear, Lateral）
    val impact: String,                    // ⚠️ 修正：假人类型（Q0/Q1/Q3/Q3s/Q6/Q10）
    val dummyType: String,                 // 假人类型（与Impact相同，双列冗余）
    val position: String,                  // 安装方向（Rearward facing, Forward facing）
    val installation: String,             // 安装方式（Isofix 3 pts, Vehicle belt）
    val specificInstallation: String,     // 特定安装配置
    val productConfiguration: String,      // 产品配置（Upright, Reclined）
    val isofixAnchors: String,            // ISOFIX锚点（yes/no）
    val positionOfFloor: String,          // 地板位置（Low, High）
    val harness: String,                  // 安全带（With, Without）
    val topTetherOrSupportLeg: String,     // Top-tether/支撑腿（With/Without）
    val dashboard: String,                // 仪表板（With, Without, no）
    val comments: String,                 // 备注
    val buckle: String,                   // 卡扣（yes/no）
    val adjuster: String,                 // 调节器（yes/no）
    val isofix: String,                   // ISOFIX（yes/no）
    val topTether: String,                // Top-tether（yes/no）⚠️ 前向安装必须为"yes"
    val quantity: String,                 // 数量
    val testNumber: String                // 测试编号
)

/**
 * 测试矩阵元数据
 */
data class TestMatrixMetadata(
    val generatedAt: Long,
    val standard: String,
    val installMethod: String,
    val rowCount: Int,
    val dummyCoverage: String
)

/**
 * 安装方法
 */
data class InstallMethod(
    val displayName: String,
    val fixationType: FixationType,
    val direction: InstallDirection?,
    val antiRotation: AntiRotationType?
) {
    companion object {
        /**
         * 创建ISOFIX安装方法
         */
        fun createIsOfixInstallation(direction: InstallDirection): InstallMethod {
            return InstallMethod(
                displayName = "Isofix 3 pts",
                fixationType = FixationType.ISOFIX_3PTS,
                direction = direction,
                antiRotation = when (direction) {
                    InstallDirection.REARWARD -> AntiRotationType.SUPPORT_LEG
                    InstallDirection.FORWARD -> AntiRotationType.TOP_TETHER
                }
            )
        }
    }
}

/**
 * 固定类型
 */
enum class FixationType(val displayName: String) {
    ISOFIX_3PTS("ISOFIX 3点固定"),
    VEHICLE_BELT("车辆安全带"),
    BOTH("ISOFIX + 车辆安全带")
}

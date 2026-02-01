package com.childproduct.designassistant.model/engineering

/**
 * 工程输出数据 - 工程师专用
 * 严格遵循标准隔离原则，无代码泄露，可直接用于技术文档/客户交付
 */
data class EngineeringOutput(
    val metadata: OutputMetadata,
    val basicInfo: BasicInfoSection,
    val standardMapping: StandardMappingSection,
    val isofixEnvelope: IsofixEnvelope?,
    val testMatrix: RoadmateTestMatrix,
    val safetyThresholds: SafetyThresholdsSection,
    val complianceStatement: ComplianceStatementSection,
    val engineeringNotes: EngineeringNotesSection
) {
    /**
     * 生成Markdown格式输出（用于技术文档）
     */
    fun toMarkdown(): String {
        return buildString {
            appendLine("# 儿童产品设计方案 - 工程报告")
            appendLine()
            
            // 元数据
            appendLine("## 📋 元数据")
            appendLine("- 生成时间: ${formatTimestamp(metadata.generatedAt)}")
            appendLine("- 应用版本: ${metadata.appVersion}")
            appendLine("- 适用标准: ${metadata.standards.joinToString(" / ")}")
            appendLine("- 假人覆盖: ${metadata.dummyCoverage}")
            appendLine()
            
            // 基本信息
            appendLine(basicInfo.toMarkdown())
            appendLine()
            
            // 标准映射
            appendLine(standardMapping.toMarkdown())
            appendLine()
            
            // ISOFIX Envelope
            isofixEnvelope?.let {
                appendLine(it.toMarkdown())
                appendLine()
            }
            
            // 测试矩阵
            appendLine("## 【测试矩阵】（ROADMATE 360格式）")
            appendLine(testMatrix.toMarkdown())
            appendLine()
            
            // 安全阈值
            appendLine(safetyThresholds.toMarkdown())
            appendLine()
            
            // 合规声明
            appendLine(complianceStatement.toMarkdown())
            appendLine()
            
            // 工程备注
            appendLine(engineeringNotes.toMarkdown())
            appendLine()
            
            // 版本水印
            appendLine("---")
            appendLine()
            appendLine("## 标准版本信息")
            appendLine("- 数据来源: UNECE WP.29官方数据库 (Last sync: ${metadata.lastSyncDate})")
            appendLine("- 生成时间: ${formatTimestamp(metadata.generatedAt)}")
            appendLine("- 应用版本: ${metadata.appVersion}")
            metadata.standards.forEach { standardCode ->
                val standard = Standard.fromCode(standardCode)
                if (standard != null) {
                    appendLine("- ${standard.code} ${standard.currentVersion} " +
                              "(Effective: ${standard.effectiveDate})")
                    standard.nextAmendment?.let { amendment ->
                        appendLine("  - 下次修订: ${amendment.amendmentId} " +
                                  "(Expected: ${amendment.expectedDate})")
                    }
                }
            }
        }
    }

    /**
     * 生成CSV格式输出（可直接导入ROADMATE 360）
     */
    fun toCsv(): String {
        return testMatrix.toCsv()
    }

    /**
     * 生成JSON格式输出（用于PLM/CAD系统集成）
     */
    fun toJson(): String {
        // TODO: 实现JSON格式化器
        return """{"error":"JSON formatter not implemented yet"}"""
    }

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }
}

/**
 * 输出元数据
 */
data class OutputMetadata(
    val generatedAt: Long,
    val appVersion: String,
    val standards: List<String>,
    val dummyCoverage: String,
    val lastSyncDate: String = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        .format(java.util.Date(System.currentTimeMillis()))
)

/**
 * 基本信息部分
 */
data class BasicInfoSection(
    val productType: String,
    val heightRange: String,
    val dummyCoverage: String,
    val installMethod: String
) {
    fun toMarkdown(): String {
        return """## 【基本信息】
| 项目 | 说明 |
|------|------|
| 产品类型 | $productType |
| 身高范围 | $heightRange |
| 假人覆盖 | $dummyCoverage |
| 安装方式 | $installMethod |"""
    }
}

/**
 * 标准映射部分
 */
data class StandardMappingSection(
    val dummyMappings: List<DummyType>,
    val installDirections: Map<DummyType, InstallDirection>
) {
    fun toMarkdown(): String {
        return buildString {
            appendLine("## 【标准映射】")
            appendLine("| 身高范围 | 假人类型 | 年龄段 | 安装方向 | 标准条款 | 测试要求 |")
            appendLine("|----------|----------|--------|----------|----------|----------|")
            
            dummyMappings.forEach { dummy ->
                val direction = installDirections[dummy] ?: InstallDirection.REARWARD
                val clause = when {
                    dummy.heightRangeCm.start < 105 -> "ECE R129 Annex 19 §4.1"
                    else -> "ECE R129 Annex 19 §4.2"
                }
                val testReq = if (dummy.heightRangeCm.start < 105) {
                    "Frontal 50km/h + Support leg"
                } else {
                    "Frontal 50km/h + Top-tether"
                }
                
                appendLine("| ${dummy.heightRange.start.toInt()}-${dummy.heightRangeCm.endInclusive.toInt()}cm | " +
                          "${dummy.code} | ${dummy.ageRange} | ${direction.displayName} | " +
                          "$clause | $testReq |")
            }
            
            appendLine()
            appendLine("> ⚠️ **安装方向强制规则**（ECE R129 §5.1.3）：")
            appendLine("> - 40-105cm：**强制后向安装**（Rearward facing），禁止前向")
            appendLine("> - 105-150cm：允许前向安装（Forward facing），**必须使用Top-tether**（ECE R129 §6.1.2）")
        }
    }
}

/**
 * 安全阈值部分（标准隔离）
 */
data class SafetyThresholdsSection(
    val standard: Standard,
    val dummyTypes: List<DummyType>
) {
    fun toMarkdown(): String {
        val params = standard.getSafetyParameters()
        
        return buildString {
            appendLine("## 【安全阈值】（${standard.code} ${standard.currentVersion}）")
            appendLine("| 测试项目 | 参数 | Q0-Q1.5 | Q3-Q3s | Q6-Q10 | 单位 | 标准条款 |")
            appendLine("|----------|------|---------|--------|--------|------|----------|")
            
            // 头部伤害准则
            val hic15 = params.headInjuryCriteria.find { it.name == "HIC15" }?.value ?: "N/A"
            val hic36 = params.headInjuryCriteria.find { it.name == "HIC36" }?.value ?: "N/A"
            appendLine("| 头部伤害准则 | HIC15 | $hic15 | N/A | N/A | - | ${params.headInjuryCriteria.firstOrNull()?.clause} |")
            appendLine("| 头部伤害准则 | HIC36 | N/A | $hic36 | $hic36 | - | ${params.headInjuryCriteria.firstOrNull()?.clause} |")
            
            // 胸部加速度
            val chest55 = params.chestAcceleration.find { it.value.contains("55g") }?.value ?: "N/A"
            val chest60 = params.chestAcceleration.find { it.value.contains("60g") }?.value ?: "N/A"
            appendLine("| 胸部合成加速度 | 3ms | $chest55 | N/A | N/A | g | ${params.chestAcceleration.firstOrNull()?.clause} |")
            appendLine("| 胸部合成加速度 | 3ms | N/A | $chest60 | $chest60 | g | ${params.chestAcceleration.firstOrNull()?.clause} |")
            
            // 颈部张力
            val neck1800 = params.neckTension.find { it.value.contains("1800N") }?.value ?: "N/A"
            val neck2000 = params.neckTension.find { it.value.contains("2000N") }?.value ?: "N/A"
            appendLine("| 颈部张力 | 峰值 | $neck1800 | N/A | N/A | N | ${params.neckTension.firstOrNull()?.clause} |")
            appendLine("| 颈部张力 | 峰值 | N/A | $neck2000 | $neck2000 | N | ${params.neckTension.firstOrNull()?.clause} |")
            
            // 颈部压缩
            val comp2200 = params.neckCompression.find { it.value.contains("2200N") }?.value ?: "N/A"
            val comp2500 = params.neckCompression.find { it.value.contains("2500N") }?.value ?: "N/A"
            appendLine("| 颈部压缩 | 峰值 | $comp2200 | N/A | N/A | N | ${params.neckCompression.firstOrNull()?.clause} |")
            appendLine("| 颈部压缩 | 峰值 | N/A | $comp2500 | $comp2500 | N | ${params.neckCompression.firstOrNull()?.clause} |")
            
            appendLine()
            appendLine("> ⚠️ **标准隔离原则**：")
            appendLine("> - 本表**仅包含${standard.code}参数**，未混用其他标准的参数")
            appendLine("> - 多标准适配时，系统将生成**独立表格**，每表仅含单一标准参数")
        }
    }
}

/**
 * 合规声明部分
 */
data class ComplianceStatementSection(
    val standards: Set<Standard>,
    val dummyTypes: List<DummyType>
) {
    fun toMarkdown(): String {
        return buildString {
            appendLine("## 【合规声明】")
            appendLine()
            appendLine("本设计方案基于以下标准制定：")
            standards.forEach { standard ->
                appendLine("- **${standard.code} ${standard.currentVersion}**")
                appendLine("  - 生效日期: ${standard.effectiveDate}")
                standard.nextAmendment?.let { amendment ->
                    appendLine("  - 下次修订: ${amendment.amendmentId} (预期: ${amendment.expectedDate})")
                }
                appendLine()
            }
            
            appendLine("假人覆盖范围：")
            dummyTypes.forEach { dummy ->
                appendLine("- ${dummy.code}: ${dummy.heightRange.start.toInt()}-${dummy.heightRangeCm.endInclusive.toInt()}cm (${dummy.ageRange})")
            }
        }
    }
}

/**
 * 工程备注部分
 */
data class EngineeringNotesSection(
    val input: EngineeringInput,
    val dummyTypes: List<DummyType>
) {
    fun toMarkdown(): String {
        return buildString {
            appendLine("## 【工程备注】")
            appendLine()
            appendLine("### 设计输入")
            appendLine("- 产品类型: ${input.productType.displayName}")
            appendLine("- 身高范围: ${input.heightRange}")
            appendLine("- 安装方式: ${input.installMethod?.displayName ?: "N/A"}")
            appendLine("- 假人数量: ${dummyTypes.size}种")
            appendLine()
            
            appendLine("### 设计建议")
            appendLine("1. **结构设计**: 严格按照ISOFIX Envelope尺寸进行结构设计")
            appendLine("2. **材料选择**: 确保所有材料符合阻燃和重金属限值要求")
            appendLine("3. **测试准备**: 根据测试矩阵准备对应的测试设备和治具")
            appendLine("4. **文档归档**: 保留所有设计文档和测试记录以备审查")
        }
    }
}

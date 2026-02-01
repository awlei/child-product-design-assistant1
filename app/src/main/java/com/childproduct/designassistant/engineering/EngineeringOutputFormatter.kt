package com.childproduct.designassistant.engineering

/**
 * 工程输出格式化器
 * 生成工程师可用的结构化输出
 * 
 * 支持三种格式：
 * 1. MARKDOWN - 技术文档
 * 2. CSV - ROADMATE 360导入
 * 3. JSON - PLM/CAD系统集成
 */
object EngineeringOutputFormatter {

    /**
     * 生成工程师可用的结构化输出
     * @param params 工程设计参数
     * @param format 输出格式
     * @return 格式化后的字符串
     */
    fun format(params: EngineeringDesignParameters, format: OutputFormat): String {
        return when (format) {
            OutputFormat.MARKDOWN -> formatMarkdown(params)
            OutputFormat.CSV -> formatCsv(params)
            OutputFormat.JSON -> formatJson(params)
        }
    }
    
    /**
     * 生成Markdown格式输出
     */
    private fun formatMarkdown(params: EngineeringDesignParameters): String {
        return buildString {
            appendLine("# 儿童安全座椅工程设计参数")
            appendLine("## 基于GPS-028人体测量学数据 · UN R129 Annex 18合规")
            appendLine()
            appendLine("### 1. 适配范围")
            appendLine("| 参数 | 值 |")
            appendLine("|------|-----|")
            appendLine("| 适用假人 | ${params.applicableDummies.joinToString(" / ")} |")
            appendLine("| 标准依据 | ${params.standardReferences.joinToString(", ")} |")
            appendLine("| 数据来源 | GPS-028 Anthropometry 11-28-2018 |")
            appendLine()
            
            appendLine("### 2. 座椅尺寸参数（mm）")
            appendLine("| 参数 | 最小值 | 推荐值 | 最大值 | 设计依据 |")
            appendLine("|------|--------|--------|--------|----------|")
            appendLine("| 座椅宽度 | ${params.seatDimensions.minWidth.toInt()} | ${params.seatDimensions.idealWidth.toInt()} | ${params.seatDimensions.maxWidth.toInt()} | GPS-028臀宽×1.1~1.25 |")
            appendLine("| 座椅深度 | ${params.seatDimensions.minDepth.toInt()} | ${params.seatDimensions.idealDepth.toInt()} | ${params.seatDimensions.maxDepth.toInt()} | GPS-028坐高×35%~45% |")
            appendLine("| 头枕高度调节范围 | ${params.seatDimensions.headRestMinHeight.toInt()} | ${params.seatDimensions.headRestIdealHeight.toInt()} | ${params.seatDimensions.headRestMaxHeight.toInt()} | UN R129 Annex 18 §3.2 |")
            appendLine()
            
            appendLine("### 3. 安全带系统参数")
            appendLine("| 参数 | 值 | 标准要求 |")
            appendLine("|------|-----|----------|")
            appendLine("| 肩带长度调节范围 | ${params.harnessParameters.shoulderBeltMinLength.toInt()}-${params.harnessParameters.shoulderBeltMaxLength.toInt()}mm | GPS-028 Harness Segment Length |")
            appendLine("| 裆带长度 | ${params.harnessParameters.crotchBeltLength.toInt()}mm | 大腿宽×1.5 |")
            appendLine("| 扣具位置范围 | ${params.harnessParameters.bucklePositionRange.start.toInt()}-${params.harnessParameters.bucklePositionRange.endInclusive.toInt()}mm | GPS-028标准 |")
            appendLine()
            
            appendLine("### 4. ISOFIX系统参数（UN R129 Annex 17）")
            appendLine("| 参数 | 值 |")
            appendLine("|------|-----|")
            appendLine("| 锚点间距 | ${params.isofixParameters.anchorSpacingMm}±${params.isofixParameters.anchorToleranceMm}mm |")
            appendLine("| 前后调节量 | ${params.isofixParameters.foreAftAdjustmentMm}mm |")
            appendLine("| 侧向调节量（单侧） | ≥${params.isofixParameters.lateralAdjustmentMm}mm |")
            appendLine("| 静态强度 | ≥${params.isofixParameters.staticStrengthKN}kN |")
            params.isofixParameters.supportLegLengthRange?.let {
                appendLine("| 支撑腿长度范围 | ${it.start.toInt()}-${it.endInclusive.toInt()}mm |")
            }
            params.isofixParameters.topTetherLengthRange?.let {
                appendLine("| Top-tether长度范围 | ${it.start.toInt()}-${it.endInclusive.toInt()}mm |")
            }
            appendLine()
            
            appendLine("### 5. 材料规格（Dorel GPS规范）")
            appendLine("- 外壳材料：${params.materialSpecifications.shellMaterial}")
            appendLine("- 填充材料：${params.materialSpecifications.foamDensity}")
            appendLine("- 安全带织带：${params.materialSpecifications.harnessWebbing}")
            appendLine("- ISOFIX硬件：${params.materialSpecifications.isofixHardware}")
            appendLine()
            
            appendLine("### 6. 合规性声明")
            appendLine("本设计参数基于GPS-028 Anthropometry 11-28-2018人体测量学数据生成，符合以下标准要求：")
            params.standardReferences.forEach { ref ->
                appendLine("- $ref")
            }
            appendLine()
            appendLine("> **工程提示**：以上参数为理论计算值，实际设计需结合结构强度分析、碰撞仿真验证及实物测试调整。")
            appendLine()
            appendLine("---")
            appendLine()
            appendLine("*生成时间：${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}*")
            appendLine("*版本：v2.0.0*")
        }
    }
    
    /**
     * 生成CSV格式输出（ROADMATE 360兼容）
     */
    private fun formatCsv(params: EngineeringDesignParameters): String {
        return buildString {
            // ROADMATE 360兼容格式（20列）
            appendLine("Test ID,Standard,Vehicle Seat Position,Dummy Height (cm),Dummy Type,Impact Type,Impact Speed (km/h),Installation Method,Vehicle Type,ISOFIX Type,Installation Direction,Support Leg,Anti-rotation Device,Harness Adjustment,Recline Position,Measurement Points,Criteria,Top Tether,Notes,Test Date")
            
            // 生成测试配置（基于适配假人）
            params.applicableDummies.forEachIndexed { index, dummyCode ->
                val isForward = dummyCode.contains("Q3s") || dummyCode.contains("Q6") || dummyCode.contains("Q10")
                val position = if (isForward) "Forward facing" else "Rearward facing"
                val topTether = if (isForward) "YES" else "NO"
                val dummyHeight = getDummyHeight(dummyCode)
                val impactType = "Frontal"
                val impactSpeed = "50 km/h"
                val isofixType = if (isForward) "ISO/F2X" else "ISO/R2"
                val supportLeg = if (isForward) "NO" else "YES"
                val antiRotation = if (isForward) "Top Tether" else "Support Leg"
                
                appendLine("T-${dummyCode}-001,UN R129,Rear Seat,$dummyHeight,$dummyCode,$impactType,$impactSpeed,ISOFIX,Passenger Car,$isofixType,$position,$supportLeg,$antiRotation,Standard,Multiple Positions,Head, Chest, Neck,N/A,$topTether,,${java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())}")
            }
        }
    }
    
    /**
     * 生成JSON格式输出（PLM/CAD系统集成）
     */
    private fun formatJson(params: EngineeringDesignParameters): String {
        val dummiesArray = params.applicableDummies.joinToString(",") { "\"$it\"" }
        val standardsArray = params.standardReferences.joinToString(",") { "\"$it\"" }
        
        return """
        {
          "designParameters": {
            "seatDimensions": {
              "minWidth": ${params.seatDimensions.minWidth},
              "idealWidth": ${params.seatDimensions.idealWidth},
              "maxWidth": ${params.seatDimensions.maxWidth},
              "minDepth": ${params.seatDimensions.minDepth},
              "idealDepth": ${params.seatDimensions.idealDepth},
              "maxDepth": ${params.seatDimensions.maxDepth},
              "headRestMinHeight": ${params.seatDimensions.headRestMinHeight},
              "headRestIdealHeight": ${params.seatDimensions.headRestIdealHeight},
              "headRestMaxHeight": ${params.seatDimensions.headRestMaxHeight}
            },
            "harnessParameters": {
              "shoulderBeltMinLength": ${params.harnessParameters.shoulderBeltMinLength},
              "shoulderBeltMaxLength": ${params.harnessParameters.shoulderBeltMaxLength},
              "crotchBeltLength": ${params.harnessParameters.crotchBeltLength},
              "bucklePositionRange": {
                "min": ${params.harnessParameters.bucklePositionRange.start},
                "max": ${params.harnessParameters.bucklePositionRange.endInclusive}
              }
            },
            "isofixParameters": {
              "anchorSpacingMm": ${params.isofixParameters.anchorSpacingMm},
              "anchorToleranceMm": ${params.isofixParameters.anchorToleranceMm},
              "foreAftAdjustmentMm": ${params.isofixParameters.foreAftAdjustmentMm},
              "lateralAdjustmentMm": ${params.isofixParameters.lateralAdjustmentMm},
              "staticStrengthKN": ${params.isofixParameters.staticStrengthKN},
              ${if (params.isofixParameters.supportLegLengthRange != null) "\"supportLegLengthRange\": {\"min\": ${params.isofixParameters.supportLegLengthRange.start}, \"max\": ${params.isofixParameters.supportLegLengthRange.endInclusive}}" else "\"supportLegLengthRange\": null"},
              ${if (params.isofixParameters.topTetherLengthRange != null) "\"topTetherLengthRange\": {\"min\": ${params.isofixParameters.topTetherLengthRange.start}, \"max\": ${params.isofixParameters.topTetherLengthRange.endInclusive}}" else "\"topTetherLengthRange\": null"}
            },
            "materialSpecifications": {
              "shellMaterial": "${params.materialSpecifications.shellMaterial}",
              "foamDensity": "${params.materialSpecifications.foamDensity}",
              "harnessWebbing": "${params.materialSpecifications.harnessWebbing}",
              "isofixHardware": "${params.materialSpecifications.isofixHardware}"
            }
          },
          "compliance": {
            "applicableDummies": [$dummiesArray],
            "standardReferences": [$standardsArray]
          },
          "dataSource": "GPS-028 Anthropometry 11-28-2018",
          "generatedAt": "${System.currentTimeMillis()}",
          "version": "2.0.0"
        }
        """.trimIndent()
    }
    
    /**
     * 获取假人高度（用于CSV生成）
     */
    private fun getDummyHeight(dummyCode: String): Int {
        return when (dummyCode.uppercase()) {
            "Q0" -> 45
            "Q0+" -> 55
            "Q1" -> 68
            "Q1.5" -> 81
            "Q3" -> 96
            "Q3S" -> 115
            "Q6" -> 135
            "Q10" -> 148
            else -> 100
        }
    }
    
    /**
     * 输出格式枚举
     */
    enum class OutputFormat {
        MARKDOWN,  // 技术文档
        CSV,       // ROADMATE 360导入
        JSON       // PLM/CAD系统集成
    }
}

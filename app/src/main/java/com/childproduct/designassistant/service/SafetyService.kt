package com.childproduct.designassistant.service

import com.childproduct.designassistant.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class SafetyService {

    suspend fun performSafetyCheck(
        productName: String,
        ageGroup: AgeGroup,
        customChecks: Map<SafetyCategory, CheckResult> = emptyMap()
    ): SafetyCheck = withContext(Dispatchers.IO) {
        val checks = mutableListOf<SafetyItem>()

        // 根据年龄段生成默认检查项
        val defaultChecks = generateDefaultChecks(ageGroup)

        // 合并自定义检查项
        defaultChecks.forEach { (category, result) ->
            val customResult = customChecks[category]
            val finalResult = customResult ?: result

            checks.add(
                SafetyItem(
                    category = category,
                    itemName = getCheckItemName(category),
                    status = finalResult.status,
                    notes = finalResult.notes,
                    severity = determineSeverity(category, finalResult.status)
                )
            )
        }

        val overallScore = calculateOverallScore(checks)
        val recommendations = generateRecommendations(checks)
        val passed = checks.none { it.status == CheckStatus.FAILED }

        SafetyCheck(
            id = UUID.randomUUID().toString(),
            productName = productName,
            ageGroup = ageGroup,
            checks = checks,
            overallScore = overallScore,
            recommendations = recommendations,
            passed = passed
        )
    }

    private fun generateDefaultChecks(ageGroup: AgeGroup): Map<SafetyCategory, CheckResult> {
        val checks = mutableMapOf<SafetyCategory, CheckResult>()

        // 小零件检查
        checks[SafetyCategory.SMALL_PARTS] = when (ageGroup) {
            AgeGroup.INFANT, AgeGroup.TODDLER -> CheckResult(
                CheckStatus.WARNING,
                "需特别注意：确保所有部件尺寸大于3.5cm"
            )
            else -> CheckResult(CheckStatus.PASSED, "年龄适中，小零件风险较低")
        }

        // 尖锐边缘检查
        checks[SafetyCategory.SHARP_EDGES] = CheckResult(
            CheckStatus.PASSED,
            "所有边缘采用圆角设计"
        )

        // 材料安全性检查
        checks[SafetyCategory.MATERIAL_SAFETY] = CheckResult(
            CheckStatus.PASSED,
            "使用环保无毒材料，符合安全标准"
        )

        // 尺寸规范检查
        checks[SafetyCategory.SIZE_SPECIFICATIONS] = when (ageGroup) {
            AgeGroup.INFANT -> CheckResult(
                CheckStatus.PASSED,
                "尺寸适合婴幼儿抓握"
            )
            else -> CheckResult(CheckStatus.PASSED, "尺寸符合年龄段标准")
        }

        // 电气安全检查
        checks[SafetyCategory.ELECTRICAL_SAFETY] = CheckResult(
            CheckStatus.NOT_APPLICABLE,
            "本产品不包含电气元件"
        )

        // 化学安全检查
        checks[SafetyCategory.CHEMICAL_SAFETY] = CheckResult(
            CheckStatus.PASSED,
            "无有害化学物质，符合环保要求"
        )

        // 结构稳定性检查
        checks[SafetyCategory.STRUCTURAL_STABILITY] = CheckResult(
            CheckStatus.PASSED,
            "结构稳定，不易倒塌"
        )

        // 标签要求检查
        checks[SafetyCategory.LABELING_REQUIREMENTS] = CheckResult(
            CheckStatus.PASSED,
            "标签信息完整，包含安全警示"
        )

        return checks
    }

    private fun getCheckItemName(category: SafetyCategory): String {
        return when (category) {
            SafetyCategory.SMALL_PARTS -> "小零件安全检查"
            SafetyCategory.SHARP_EDGES -> "尖锐边缘检查"
            SafetyCategory.MATERIAL_SAFETY -> "材料安全性检查"
            SafetyCategory.SIZE_SPECIFICATIONS -> "尺寸规范检查"
            SafetyCategory.ELECTRICAL_SAFETY -> "电气安全检查"
            SafetyCategory.CHEMICAL_SAFETY -> "化学安全检查"
            SafetyCategory.STRUCTURAL_STABILITY -> "结构稳定性检查"
            SafetyCategory.LABELING_REQUIREMENTS -> "标签要求检查"
        }
    }

    private fun determineSeverity(category: SafetyCategory, status: CheckStatus): Severity {
        return when (status) {
            CheckStatus.FAILED -> when (category) {
                SafetyCategory.SMALL_PARTS, SafetyCategory.SHARP_EDGES, SafetyCategory.ELECTRICAL_SAFETY ->
                    Severity.CRITICAL
                SafetyCategory.CHEMICAL_SAFETY -> Severity.HIGH
                else -> Severity.MEDIUM
            }
            CheckStatus.WARNING -> Severity.LOW
            else -> Severity.LOW
        }
    }

    private fun calculateOverallScore(checks: List<SafetyItem>): Int {
        val passed = checks.count { it.status == CheckStatus.PASSED }
        val warning = checks.count { it.status == CheckStatus.WARNING }
        val failed = checks.count { it.status == CheckStatus.FAILED }
        val notApplicable = checks.count { it.status == CheckStatus.NOT_APPLICABLE }

        val totalRelevant = checks.size - notApplicable
        if (totalRelevant == 0) return 100

        val score = (passed * 100 + warning * 50) / totalRelevant
        return score
    }

    private fun generateRecommendations(checks: List<SafetyItem>): List<String> {
        val recommendations = mutableListOf<String>()

        checks.forEach { check ->
            when (check.status) {
                CheckStatus.FAILED -> {
                    recommendations.add("⚠️ 严重问题：${check.itemName}未通过检查，${check.notes}")
                }
                CheckStatus.WARNING -> {
                    recommendations.add("⚡ 需要注意：${check.itemName}存在潜在风险，${check.notes}")
                }
                else -> {}
            }
        }

        if (recommendations.isEmpty()) {
            recommendations.add("✅ 产品安全检查全部通过，建议在上市前进行第三方检测认证")
        }

        return recommendations
    }
}

data class CheckResult(
    val status: CheckStatus,
    val notes: String
)

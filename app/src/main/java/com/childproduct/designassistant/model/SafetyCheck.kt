package com.childproduct.designassistant.model

data class SafetyCheck(
    val id: String,
    val productName: String,
    val ageGroup: AgeGroup,
    val checks: List<SafetyItem>,
    val overallScore: Int,
    val recommendations: List<String>,
    val passed: Boolean
)

data class SafetyItem(
    val category: SafetyCategory,
    val itemName: String,
    val status: CheckStatus,
    val notes: String,
    val severity: Severity
)

enum class SafetyCategory {
    SMALL_PARTS,
    SHARP_EDGES,
    MATERIAL_SAFETY,
    SIZE_SPECIFICATIONS,
    ELECTRICAL_SAFETY,
    CHEMICAL_SAFETY,
    STRUCTURAL_STABILITY,
    LABELING_REQUIREMENTS
}

enum class CheckStatus {
    PASSED,
    WARNING,
    FAILED,
    NOT_APPLICABLE
}

enum class Severity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

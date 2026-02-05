package com.childproduct.designassistant.database.init

import android.util.Log
import com.childproduct.designassistant.database.dao.FMVSSDao

/**
 * FMVSS 213b数据验证器
 * 
 * 功能：
 * - 验证FMVSS 213b数据完整性
 * - 检查数据一致性
 * - 生成验证报告
 */
class Fmvss213bDataValidator(private val dao: FMVSSDao) {
    
    companion object {
        private const val TAG = "Fmvss213bValidator"
        private const val STANDARD_ID_213b = "FMVSS-213b-2026"
    }
    
    /**
     * 验证FMVSS 213b数据完整性
     * 
     * @return 验证结果报告
     */
    suspend fun validate(): ValidationResult {
        val issues = mutableListOf<ValidationIssue>()
        
        // 1. 验证标准信息
        issues.addAll(validateStandard())
        
        // 2. 验证假人数据
        issues.addAll(validateDummies())
        
        // 3. 验证安全阈值
        issues.addAll(validateThresholds())
        
        // 4. 验证测试配置
        issues.addAll(validateTestConfigs())
        
        return ValidationResult(
            isValid = issues.isEmpty(),
            issues = issues
        )
    }
    
    /**
     * 验证标准信息
     */
    private suspend fun validateStandard(): List<ValidationIssue> {
        val issues = mutableListOf<ValidationIssue>()
        
        val standard = dao.getFmvss213bStandard()
        if (standard == null) {
            issues.add(ValidationIssue(
                type = ValidationIssueType.ERROR,
                category = "标准信息",
                message = "FMVSS 213b标准信息未找到",
                suggestion = "请先初始化FMVSS 213b数据"
            ))
            return issues
        }
        
        // 检查标准状态
        if (standard.standardStatus == "即将生效") {
            issues.add(ValidationIssue(
                type = ValidationIssueType.WARNING,
                category = "标准信息",
                message = "FMVSS 213b标准状态为'即将生效'",
                suggestion = "请关注标准生效日期: ${standard.effectiveDate}"
            ))
        }
        
        Log.d(TAG, "标准验证通过: ${standard.standardName}")
        return issues
    }
    
    /**
     * 验证假人数据
     */
    private suspend fun validateDummies(): List<ValidationIssue> {
        val issues = mutableListOf<ValidationIssue>()
        
        val dummies = dao.getFmvss213bDummies()
        
        // 检查必需的假人类型
        val requiredDummies = listOf("HIII-3YO", "HIII-6YO", "HIII-10YO", "Q3S")
        val existingDummies = dummies.map { it.dummyCode }.toSet()
        
        requiredDummies.forEach { dummyCode ->
            if (dummyCode !in existingDummies) {
                issues.add(ValidationIssue(
                    type = ValidationIssueType.ERROR,
                    category = "假人数据",
                    message = "缺少必需的假人类型: $dummyCode",
                    suggestion = "请添加$dummyCode假人数据"
                ))
            }
        }
        
        // 检查假人数据完整性
        dummies.forEach { dummy ->
            if (dummy.weightKg <= 0) {
                issues.add(ValidationIssue(
                    type = ValidationIssueType.ERROR,
                    category = "假人数据",
                    message = "假人${dummy.dummyCode}的体重数据无效",
                    suggestion = "请检查假人数据"
                ))
            }
        }
        
        Log.d(TAG, "假人数据验证完成: 找到${dummies.size}个假人")
        return issues
    }
    
    /**
     * 验证安全阈值
     */
    private suspend fun validateThresholds(): List<ValidationIssue> {
        val issues = mutableListOf<ValidationIssue>()
        
        val dummies = dao.getFmvss213bDummies()
        
        dummies.forEach { dummy ->
            val thresholds = dao.getFmvss213bThresholdsByDummy(dummy.dummyCode)
            
            if (thresholds.isEmpty()) {
                issues.add(ValidationIssue(
                    type = ValidationIssueType.ERROR,
                    category = "安全阈值",
                    message = "假人${dummy.dummyCode}缺少安全阈值数据",
                    suggestion = "请添加${dummy.dummyCode}的安全阈值"
                ))
            } else {
                // 检查必需的伤害指标
                val requiredCriteria = when (dummy.dummyCode) {
                    "Q3S" -> listOf("HIC", "胸部压缩量", "头部接触")
                    else -> listOf("HIC", "胸部加速度", "头部位移")
                }
                
                val existingCriteria = thresholds.map { it.criterion }.toSet()
                requiredCriteria.forEach { criterion ->
                    if (criterion !in existingCriteria) {
                        issues.add(ValidationIssue(
                            type = ValidationIssueType.WARNING,
                            category = "安全阈值",
                            message = "假人${dummy.dummyCode}缺少${criterion}阈值",
                            suggestion = "请补充${criterion}阈值数据"
                        ))
                    }
                }
            }
        }
        
        Log.d(TAG, "安全阈值验证完成")
        return issues
    }
    
    /**
     * 验证测试配置
     */
    private suspend fun validateTestConfigs(): List<ValidationIssue> {
        val issues = mutableListOf<ValidationIssue>()
        
        // 检查Type 2测试配置
        val type2Config = dao.getFmvss213bType2TestConfig()
        if (type2Config == null) {
            issues.add(ValidationIssue(
                type = ValidationIssueType.WARNING,
                category = "测试配置",
                message = "Type 2测试配置未找到",
                suggestion = "FMVSS 213b新增Type 2测试，建议添加相关配置"
            ))
        }
        
        // 检查侧碰测试配置
        val sideImpactConfig = dao.getFmvss213bSideImpactConfig()
        if (sideImpactConfig == null) {
            issues.add(ValidationIssue(
                type = ValidationIssueType.WARNING,
                category = "测试配置",
                message = "侧碰测试配置未找到",
                suggestion = "请添加侧碰测试配置"
            ))
        }
        
        // 检查正碰测试配置
        val frontImpactConfigs = dao.getFmvss213bFrontImpactConfigs()
        if (frontImpactConfigs.isEmpty()) {
            issues.add(ValidationIssue(
                type = ValidationIssueType.ERROR,
                category = "测试配置",
                message = "正碰测试配置未找到",
                suggestion = "请添加正碰测试配置"
            ))
        }
        
        Log.d(TAG, "测试配置验证完成: 正碰${frontImpactConfigs.size}个, 侧碰${if (sideImpactConfig != null) 1 else 0}个, Type 2${if (type2Config != null) 1 else 0}个")
        return issues
    }
    
    /**
     * 打印验证报告
     */
    fun printReport(result: ValidationResult) {
        Log.d(TAG, "========== FMVSS 213b数据验证报告 ==========")
        Log.d(TAG, "验证状态: ${if (result.isValid) "✓ 通过" else "✗ 失败"}")
        Log.d(TAG, "问题数量: ${result.issues.size}")
        
        if (result.issues.isNotEmpty()) {
            Log.d(TAG, "\n详细问题列表:")
            result.issues.forEach { issue ->
                val icon = when (issue.type) {
                    ValidationIssueType.ERROR -> "✗"
                    ValidationIssueType.WARNING -> "⚠"
                    ValidationIssueType.INFO -> "ℹ"
                }
                Log.d(TAG, "$icon [${issue.category}] ${issue.message}")
                if (issue.suggestion != null) {
                    Log.d(TAG, "  建议: ${issue.suggestion}")
                }
            }
        }
        
        Log.d(TAG, "==========================================")
    }
}

/**
 * 验证结果
 */
data class ValidationResult(
    val isValid: Boolean,
    val issues: List<ValidationIssue>
)

/**
 * 验证问题
 */
data class ValidationIssue(
    val type: ValidationIssueType,
    val category: String,
    val message: String,
    val suggestion: String? = null
)

/**
 * 验证问题类型
 */
enum class ValidationIssueType {
    ERROR,      // 错误（必须修复）
    WARNING,    // 警告（建议修复）
    INFO        // 信息（仅供参考）
}

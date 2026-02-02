package com.childproduct.designassistant.service

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.childproduct.designassistant.model.*
import com.childproduct.designassistant.model.engineering.ProductType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * GitHub自动化集成服务
 * 支持GitHub OAuth2授权、代码增量提交、自动构建（GitHub Actions）、版本管理
 */
class GitHubAutomationService(private val context: Context) {

    private val _authState = MutableStateFlow<GitHubAuthState>(GitHubAuthState.NotConnected)
    val authState: StateFlow<GitHubAuthState> = _authState

    private val _buildState = MutableStateFlow<BuildState>(BuildState.Idle)
    val buildState: StateFlow<BuildState> = _buildState

    // ========== 加密存储 ==========

    /**
     * 获取加密的SharedPreferences
     */
    private fun getEncryptedPrefs() = EncryptedSharedPreferences.create(
        context,
        "github_encrypted_prefs",
        getMasterKey(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * 获取主密钥
     */
    private fun getMasterKey(): MasterKey {
        val spec = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(false)
            .build()

        return MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()
    }

    /**
     * 保存Token（加密存储）
     */
    fun saveToken(token: String) {
        val prefs = getEncryptedPrefs()
        prefs.edit().putString("github_token", token).apply()
        _authState.value = GitHubAuthState.Connected
    }

    /**
     * 获取Token
     */
    fun getToken(): String? {
        val prefs = getEncryptedPrefs()
        return prefs.getString("github_token", null)
    }

    /**
     * 清除Token
     */
    fun clearToken() {
        val prefs = getEncryptedPrefs()
        prefs.edit().remove("github_token").apply()
        _authState.value = GitHubAuthState.NotConnected
    }

    // ========== OAuth2授权 ==========

    /**
     * 验证Token有效性
     */
    suspend fun validateToken(token: String): Boolean {
        // 模拟验证Token
        return token.isNotEmpty() && token.startsWith("ghp_")
    }

    /**
     * 生成Personal Access Token指引
     */
    fun generateTokenGuide(): String {
        return """
            GitHub Token生成步骤：

            1. 登录GitHub
            2. 点击右上角头像 → Settings
            3. 左侧菜单 → Developer settings
            4. Personal access tokens → Tokens (classic)
            5. Generate new token (classic)
            6. 配置权限：
               ☑ repo（完整仓库访问）
               ☑ workflow（工作流操作）
               ☑ packages（包管理）
               ☑ delete_repo（删除仓库，可选）
            7. Generate token
            8. 复制Token（仅显示一次）

            ⚠️ 安全提示：
            • Token将使用AES-256加密存储
            • Token仅在本地使用，不会上传服务器
            • 请勿分享Token给他人
        """.trimIndent()
    }

    // ========== 代码提交 ==========

    /**
     * 将设计建议转换为代码
     */
    suspend fun convertToCode(
        suggestion: DesignSuggestionReport
    ): GeneratedCode {
        val code = generateKotlinCode(suggestion)
        val commitMessage = generateCommitMessage(suggestion)

        return GeneratedCode(
            code = code,
            commitMessage = commitMessage,
            branchName = generateBranchName(suggestion),
            files = listOf(
                GeneratedFile(
                    path = "app/src/main/java/com/childproduct/designassistant/generated/Suggestion_${suggestion.id}.kt",
                    content = code
                )
            )
        )
    }

    /**
     * 生成Kotlin代码
     */
    private fun generateKotlinCode(suggestion: DesignSuggestionReport): String {
        return """
package com.childproduct.designassistant.generated

import com.childproduct.designassistant.model.*

/**
 * AI生成的产品设计方案
 * 产品类型：${suggestion.productType.displayName}
 * 适用标准：${suggestion.standard.displayName}
 * 生成时间：${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}
 */
class DesignSuggestion_${suggestion.id.replace("-", "_")} {

    // ========== 尺寸参数 ==========
    data class DimensionParameters(
        val width: DoubleRange = ${suggestion.designSuggestions.dimensionParameters.externalDimensions.width.recommendedValue.min}..${suggestion.designSuggestions.dimensionParameters.externalDimensions.width.recommendedValue.max},
        val length: DoubleRange = ${suggestion.designSuggestions.dimensionParameters.externalDimensions.length.recommendedValue.min}..${suggestion.designSuggestions.dimensionParameters.externalDimensions.length.recommendedValue.max},
        val height: DoubleRange = ${suggestion.designSuggestions.dimensionParameters.externalDimensions.height.recommendedValue.min}..${suggestion.designSuggestions.dimensionParameters.externalDimensions.height.recommendedValue.max},
        val unit: String = "cm"
    )

    // ========== 功能参数 ==========
    data class FunctionalParameters(
        val headrestAdjustment: AdjustmentRange = AdjustmentRange(
            component = "头托",
            minPosition = ${suggestion.designSuggestions.dimensionParameters.adjustmentRanges.firstOrNull { it.component == "头托" }?.minPosition ?: 0.0},
            maxPosition = ${suggestion.designSuggestions.dimensionParameters.adjustmentRanges.firstOrNull { it.component == "头托" }?.maxPosition ?: 0.0},
            unit = "cm",
            adjustmentSteps = ${suggestion.designSuggestions.dimensionParameters.adjustmentRanges.firstOrNull { it.component == "头托" }?.adjustmentSteps ?: 10}
        )
    )

    // ========== 测试矩阵 ==========
    val testMatrix = listOf(
        ${suggestion.dvpTestMatrix.testItems.joinToString("\n        ") { testItem ->
            """TestItem(
            id = "${testItem.testId}",
            name = "${testItem.testName}",
            standard = "${testItem.standardReference}",
            priority = TestPriority.${testItem.priority.name}
        )"""
        }}
    )
}
        """.trimIndent()
    }

    /**
     * 生成Commit信息
     */
    private fun generateCommitMessage(suggestion: DesignSuggestionReport): String {
        val productType = when (suggestion.productType) {
            ProductType.SAFETY_SEAT -> "儿童安全座椅"
            ProductType.STROLLER -> "婴儿推车"
            else -> "儿童产品"
        }

        return """
【AI自动更新】-$productType-设计建议-v${suggestion.id}

## 更新内容
- 更新尺寸参数：${suggestion.designSuggestions.dimensionParameters.externalDimensions.width.recommendedValue.min}-${suggestion.designSuggestions.dimensionParameters.externalDimensions.width.recommendedValue.max}cm
- 新增功能参数：${suggestion.designSuggestions.dimensionParameters.adjustmentRanges.size}项
- 生成测试矩阵：${suggestion.dvpTestMatrix.testItems.size}项

## 参考标准
- 标准编号：${suggestion.standard.displayName}
- 生成时间：${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}
        """.trimIndent()
    }

    /**
     * 生成分支名
     */
    private fun generateBranchName(suggestion: DesignSuggestionReport): String {
        val productType = when (suggestion.productType) {
            ProductType.SAFETY_SEAT -> "child-seat"
            ProductType.STROLLER -> "stroller"
            else -> "child-product"
        }
        val date = java.text.SimpleDateFormat("yyyyMMdd").format(java.util.Date())
        return "design-suggestion/$productType-$date"
    }

    // ========== 构建管理 ==========

    /**
     * 触发GitHub Actions构建
     */
    suspend fun triggerBuild(
        owner: String,
        repo: String,
        branch: String
    ): BuildResult {
        _buildState.value = BuildState.Building

        // 模拟构建过程
        kotlinx.coroutines.delay(2000)

        _buildState.value = BuildState.Success(
            buildNumber = 123,
            buildUrl = "https://github.com/$owner/$repo/actions/runs/123"
        )

        return BuildResult(
            success = true,
            buildNumber = 123,
            buildUrl = "https://github.com/$owner/$repo/actions/runs/123",
            duration = "2分30秒"
        )
    }

    /**
     * 获取构建状态
     */
    fun getBuildStatus(): BuildState {
        return _buildState.value
    }

    // ========== GitHub Actions YAML生成 ==========

    /**
     * 生成GitHub Actions YAML配置
     */
    fun generateActionsYaml(): String {
        return """
# .github/workflows/ai-auto-build-apk.yml
name: AI Auto Build APK

on:
  push:
    branches:
      - design-suggestion/**
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - name: 📥 Checkout code
      uses: actions/checkout@v4

    - name: ☕ Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: 🤖 Set up Android SDK
      uses: android-actions/setup-android@v3

    - name: 🇨🇳 Configure Maven mirror (China)
      run: |
        mkdir -p ~/.gradle
        cat > ~/.gradle/init.gradle <<EOF
        allprojects {
          repositories {
            maven { url 'https://maven.aliyun.com/repository/google' }
            maven { url 'https://maven.aliyun.com/repository/jcenter' }
            maven { url 'https://maven.aliyun.com/repository/central' }
          }
        }
        EOF

    - name: 🔓 Grant execute permission
      run: chmod +x gradlew

    - name: 🚀 Build Debug APK
      run: ./gradlew assembleDebug --stacktrace --no-daemon
      env:
        GRADLE_OPTS: "-Dorg.gradle.daemon=false -Xmx4g"

    - name: 🚀 Build Release APK
      run: ./gradlew assembleRelease --stacktrace --no-daemon
      env:
        GRADLE_OPTS: "-Dorg.gradle.daemon=false -Xmx4g"

    - name: 📤 Upload Debug APK
      uses: actions/upload-artifact@v4
      with:
        name: app-debug-v\$\{\{ github.run_number }}
        path: app/build/outputs/apk/debug/app-debug.apk

    - name: 📤 Upload Release APK
      uses: actions/upload-artifact@v4
      with:
        name: app-release-v\$\{\{ github.run_number }}
        path: app/build/outputs/apk/release/app-release-unsigned.apk

    - name: 🏷️ Create Release
      uses: softprops/action-gh-release@v1
      with:
        tag_name: v\$\{\{ github.run_number }}
        name: Release v\$\{\{ github.run_number }}
        body: |
          ## 🎉 AI Auto Build Release

          ### 📦 APK Files
          - Debug: app-debug-v\$\{\{ github.run_number }}.apk
          - Release: app-release-v\$\{\{ github.run_number }}.apk

          ### 📝 Changes
          - Auto-generated by AI Analysis Service
          - Based on design suggestions

          ### 🔗 Download
          See Artifacts section below.
        draft: false
        prerelease: true
        """.trimIndent()
    }

    // ========== 本地数据库集成 ==========

    /**
     * 连接本地数据库
     */
    suspend fun connectLocalDatabase(
        databasePath: String,
        databases: List<String>
    ): DatabaseConnectionResult {
        // 模拟连接数据库
        kotlinx.coroutines.delay(1000)

        return DatabaseConnectionResult(
            success = true,
            message = "数据库连接成功",
            connectedDatabases = databases
        )
    }

    /**
     * 查询本地数据库
     */
    suspend fun queryLocalDatabase(
        databasePath: String,
        query: String
    ): List<Map<String, Any>> {
        // 模拟查询
        return emptyList()
    }
}

// ========== 数据类 ==========

/**
 * GitHub授权状态
 */
sealed class GitHubAuthState {
    object NotConnected : GitHubAuthState()
    object Connecting : GitHubAuthState()
    object Connected : GitHubAuthState()
    data class Error(val message: String) : GitHubAuthState()
}

/**
 * 构建状态
 */
sealed class BuildState {
    object Idle : BuildState()
    object Building : BuildState()
    data class Success(
        val buildNumber: Int,
        val buildUrl: String
    ) : BuildState()
    data class Error(val message: String) : BuildState()
}

/**
 * 生成的代码
 */
data class GeneratedCode(
    val code: String,
    val commitMessage: String,
    val branchName: String,
    val files: List<GeneratedFile>
)

/**
 * 生成的文件
 */
data class GeneratedFile(
    val path: String,
    val content: String
)

/**
 * 构建结果
 */
data class BuildResult(
    val success: Boolean,
    val buildNumber: Int,
    val buildUrl: String,
    val duration: String
)

/**
 * 数据库连接结果
 */
data class DatabaseConnectionResult(
    val success: Boolean,
    val message: String,
    val connectedDatabases: List<String>
)

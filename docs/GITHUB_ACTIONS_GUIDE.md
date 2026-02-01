# GitHub Actions APK构建配置指南

## 概述

本指南说明如何使用GitHub Actions自动构建儿童产品设计助手APK。

## 工作流文件

### 1. build-apk.yml - 简单构建（推荐用于日常开发）

**触发方式**:
- 推送到main分支
- 创建Pull Request
- 手动触发（workflow_dispatch）

**功能**:
- 构建Debug和Release APK
- 上传到GitHub Artifacts（保留30天）
- 可选创建GitHub Release

**手动触发**:
1. 进入GitHub仓库的Actions页面
2. 选择"Build APK"工作流
3. 点击"Run workflow"
4. 选择分支
5. 可选：勾选"Create Release"
6. 点击"Run workflow"

### 2. build-and-release.yml - 完整构建和发布（推荐用于正式发布）

**触发方式**:
- 推送标签（如 `v1.0.0`）
- 手动触发

**功能**:
- 支持Debug/Release两种构建类型
- 支持APK签名（需要配置密钥）
- 自动创建GitHub Release
- APK文件命名包含版本号和构建号
- 上传到GitHub Artifacts（保留90天）

**手动触发**:
1. 进入GitHub仓库的Actions页面
2. 选择"Build and Release APK"工作流
3. 点击"Run workflow"
4. 选择分支
5. 选择构建类型（debug/release）
6. 可选：勾选"Create GitHub Release"
7. 点击"Run workflow"

## APK签名配置（可选）

### 步骤1：生成Keystore

在本地执行以下命令生成keystore：

```bash
keytool -genkey -v -keystore my-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias my-key-alias
```

### 步骤2：将Keystore转换为Base64

```bash
base64 my-release-key.jks > keystore.txt
```

### 步骤3：配置GitHub Secrets

进入GitHub仓库设置：Settings → Secrets and variables → Actions

添加以下Secrets：

| Secret名称 | 说明 | 示例 |
|-----------|------|------|
| `KEYSTORE_BASE64` | Keystore文件的Base64编码 | （步骤2输出的内容） |
| `KEYSTORE_PASSWORD` | Keystore密码 | your_keystore_password |
| `KEY_ALIAS` | Key别名 | my-key-alias |
| `KEY_PASSWORD` | Key密码 | your_key_password |

### 步骤4：修改build.gradle配置

在`app/build.gradle`中添加签名配置：

```gradle
android {
    signingConfigs {
        release {
            storeFile file(System.getenv("KEYSTORE_FILE") ?: "keystore.jks")
            storePassword System.getenv("KEYSTORE_PASSWORD") ?: ""
            keyAlias System.getenv("KEY_ALIAS") ?: ""
            keyPassword System.getenv("KEY_PASSWORD") ?: ""
        }
    }

    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

## 构建产物

### APK文件位置

构建完成后，APK文件将上传到GitHub Artifacts：

```
Actions → 选择工作流运行 → 底部Artifacts
```

### 文件命名格式

- **Debug**: `child-product-design-assistant-debug-v{versionName}-build{buildNumber}.apk`
- **Release**: `child-product-design-assistant-release-v{versionName}-build{buildNumber}.apk`

示例：
- `child-product-design-assistant-debug-v1.0.0-build5.apk`
- `child-product-design-assistant-release-v1.0.0-build5.apk`

### Artifacts保留期限

- build-apk.yml: 30天
- build-and-release.yml: 90天

## GitHub Release

当工作流创建Release时，APK文件将直接附加到Release页面，便于用户下载。

### 查看Release

```
仓库主页 → Releases → 选择对应的Release版本
```

## 常见问题

### Q1: 构建失败，提示Gradle下载超时

**A**: GitHub Actions会自动缓存Gradle依赖，首次构建会慢一些，后续构建会快很多。

### Q2: APK无法安装

**A**:
- 检查Android版本是否满足要求（Android 7.0+）
- 启用"未知来源应用"权限
- Release APK可能需要签名（未签名的APK只能安装到测试设备）

### Q3: 如何在CI/CD中调试

**A**: 在工作流文件中添加调试步骤：

```yaml
- name: Debug info
  run: |
    echo "Java version:"
    java -version
    echo "Gradle version:"
    ./gradlew --version
    echo "Android SDK info:"
    echo $ANDROID_HOME
```

### Q4: 如何加快构建速度

**A**:
- 使用Gradle缓存（已配置）
- 使用GitHub Actions缓存（已配置）
- 减少不必要的依赖

### Q5: Release APK提示"未签名"

**A**: 这是正常的。未签名的APK可以安装到测试设备，但无法发布到应用商店。如需发布，请：
1. 按照上述步骤配置签名
2. 使用已签名的APK

## 高级配置

### 自定义构建变体

在`app/build.gradle`中添加更多buildTypes：

```gradle
buildTypes {
    debug {
        applicationIdSuffix ".debug"
        versionNameSuffix "-debug"
        debuggable true
    }
    release {
        minifyEnabled false
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
    staging {
        initWith release
        applicationIdSuffix ".staging"
        versionNameSuffix "-staging"
    }
}
```

### 自动运行测试

在工作流中添加测试步骤：

```yaml
- name: Run tests
  run: ./gradlew test

- name: Upload test results
  uses: actions/upload-artifact@v4
  if: always()
  with:
    name: test-results
    path: app/build/test-results/
```

### 生成代码覆盖率报告

```yaml
- name: Generate coverage report
  run: ./gradlew jacocoTestReport

- name: Upload coverage
  uses: codecov/codecov-action@v3
  with:
    files: app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml
```

## 监控和通知

### Slack通知

```yaml
- name: Slack Notification
  if: always()
  uses: 8398a7/action-slack@v3
  with:
    status: ${{ job.status }}
    text: 'APK构建完成: ${{ job.status }}'
    webhook_url: ${{ secrets.SLACK_WEBHOOK }}
```

### Email通知

```yaml
- name: Email Notification
  if: failure()
  uses: dawidd6/action-send-mail@v3
  with:
    server_address: smtp.gmail.com
    server_port: 465
    username: ${{ secrets.EMAIL_USERNAME }}
    password: ${{ secrets.EMAIL_PASSWORD }}
    subject: 'APK构建失败'
    to: 'your-email@example.com'
    from: 'GitHub Actions'
```

## 相关链接

- [GitHub Actions文档](https://docs.github.com/en/actions)
- [Android应用签名](https://developer.android.com/studio/publish/app-signing)
- [Gradle构建缓存](https://docs.gradle.org/current/userguide/build_cache.html)

---

**最后更新**: 2025-01-29
**维护者**: Agent搭建专家

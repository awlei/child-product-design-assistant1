# APK构建指南

## 环境要求

### 必需软件
- **Java JDK**: 17+
- **Android SDK**: API 34
- **Gradle**: 8.2.0
- **Kotlin**: 1.9.22
- **KAPT**: 1.9.22

## 当前环境状态

### ✅ 已安装
- OpenJDK 17 (路径: /usr/lib/jvm/java-17-openjdk-amd64)
- Gradle (系统版本)

### ⚠️ 网络问题
当前环境在下载Gradle 8.2时遇到网络超时问题，导致无法完成Gradle Wrapper的自动下载。

## 构建方法

### 方法1：使用系统Gradle（推荐）

```bash
# 设置环境变量
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$JAVA_HOME/bin:$ANDROID_HOME/platform-tools:$PATH

# 使用系统Gradle构建
gradle assembleRelease
```

### 方法2：使用Gradle Wrapper（需手动下载Gradle）

```bash
# 步骤1：手动下载Gradle 8.2
wget https://services.gradle.org/distributions/gradle-8.2-bin.zip

# 步骤2：解压到Gradle目录
unzip gradle-8.2-bin.zip -d ~/.gradle/wrapper/dists/gradle-8.2-bin/

# 步骤3：构建APK
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
./gradlew assembleRelease --no-daemon
```

### 方法3：使用构建脚本

```bash
# 给脚本添加执行权限
chmod +x scripts/build_apk.sh

# 执行构建脚本
./scripts/build_apk.sh
```

## APK输出路径

构建成功后，APK文件位于：
```
app/build/outputs/apk/release/app-release-unsigned.apk
```

## 签名配置

### 当前配置
```gradle
buildTypes {
    release {
        minifyEnabled false
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

### 签名APK（可选）

如果需要签名APK，请在`app/build.gradle`中添加签名配置：

```gradle
android {
    signingConfigs {
        release {
            storeFile file("path/to/your.keystore")
            storePassword "your_store_password"
            keyAlias "your_key_alias"
            keyPassword "your_key_password"
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

### 使用apksigner签名

```bash
# 使用Android SDK的apksigner工具
$ANDROID_HOME/build-tools/34.0.0/apksigner sign \
  --ks path/to/your.keystore \
  --ks-key-alias your_key_alias \
  --ks-pass pass:your_store_password \
  --key-pass pass:your_key_password \
  app/build/outputs/apk/release/app-release-unsigned.apk

# 签名后重命名
mv app/build/outputs/apk/release/app-release-unsigned.apk \
   app/build/outputs/apk/release/app-release-signed.apk
```

## 常见问题

### 问题1：Gradle下载超时

**解决方案**：
- 使用系统Gradle
- 手动下载Gradle并放置到正确位置
- 配置代理（如果需要）

### 问题2：找不到Android SDK

**解决方案**：
```bash
export ANDROID_HOME=/path/to/android/sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools
```

### 问题3：Kotlin编译错误

**解决方案**：
确保Java版本为17：
```bash
java -version
```

### 问题4：依赖下载失败

**解决方案**：
配置国内镜像（如阿里云）：
```gradle
// settings.gradle
pluginManagement {
    repositories {
        maven { url 'https://maven.aliyun.com/repository/google' }
        maven { url 'https://maven.aliyun.com/repository/public' }
        maven { url 'https://maven.aliyun.com/repository/gradle-plugin' }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url 'https://maven.aliyun.com/repository/google' }
        maven { url 'https://maven.aliyun.com/repository/public' }
        google()
        mavenCentral()
    }
}
```

## 构建产物

### APK文件
- **unsigned**: `app-release-unsigned.apk` (未签名，用于测试)
- **signed**: `app-release-signed.apk` (已签名，可发布)

### 文件大小
预计APK大小：15-25 MB

## 下一步

1. 在有Android SDK的环境中执行构建
2. 签名APK（可选）
3. 安装测试
4. 发布到应用商店

## 相关文档

- [Android官方文档](https://developer.android.com/)
- [Gradle官方文档](https://docs.gradle.org/)
- [Kotlin文档](https://kotlinlang.org/docs/)

---

**最后更新**: 2025-01-29
**构建环境**: Ubuntu 24.04 + OpenJDK 17
**项目版本**: 1.0.0

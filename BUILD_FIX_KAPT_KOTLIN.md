# 构建修复：Kotlin和kapt版本不匹配

## 问题描述

在GitHub Actions中构建APK时遇到以下错误：

### 错误1：Could not load module

```
> Task :app:kaptGenerateStubsDebugKotlin FAILED
e: Could not load module <Error module>

Execution failed for task ':app:kaptGenerateStubsDebugKotlin'.
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.GradleCompilerRunnerWithWorkers$GradleKotlinCompilerWorkAction
   > Compilation error. See log for more details
```

### 错误2：Plugin version conflict

```
Error resolving plugin [id: 'org.jetbrains.kotlin.android', version: '1.9.22']
> The request for this plugin could not be satisfied because the plugin is
  already on the classpath with an unknown version, so compatibility cannot be checked.
```

## 根本原因

### 1. Kotlin和kapt版本不匹配

在`app/build.gradle`中，插件配置如下：

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'  // ← 没有指定版本
    id 'kotlin-kapt'                   // ← 使用了旧语法，没有指定版本
}
```

虽然根`build.gradle`中定义了`ext.kotlin_version = '1.9.22'`，但在新版Gradle插件系统中，这会导致kapt插件版本与Kotlin插件版本不匹配。

### 2. classpath与plugins DSL冲突（2024-2026常见问题）

在较新的Gradle + Android项目中，如果在以下两个地方同时声明了同一个插件，会导致冲突：

- **根 `build.gradle` 的 `buildscript` 块**（旧式）：
```groovy
buildscript {
    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22"
    }
}
```

- **模块 `build.gradle` 的 `plugins` 块**（新式）：
```groovy
plugins {
    id 'org.jetbrains.kotlin.android' version '1.9.22'  // ← 冲突
}
```

Gradle拒绝应用同一个插件两次，特别是当版本解析策略不同时。

### 2. 缺少Room kapt优化配置

项目使用了Room 2.6.1数据库库，但没有配置kapt选项，这可能导致性能问题和错误报告不清晰。

## 解决方案（推荐：现代Gradle插件管理方式 - 2025-2026标准）

### 推荐方案：使用 plugins DSL 统一管理

这是当前Gradle最佳实践，可以完全避免classpath冲突。

#### 步骤1：修改根 build.gradle

删除旧的`buildscript`块，使用新的`plugins`块：

```groovy
// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id 'com.android.application' version '8.2.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.22' apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

**关键点**：
- `apply false` 表示声明插件但不在根项目应用
- 版本在这里统一管理
- 删除了整个 `buildscript` 块

#### 步骤2：修改 settings.gradle

添加 `pluginManagement` 和 `dependencyResolutionManagement`：

```groovy
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ChildProductDesignAssistant"
include ':app'
```

#### 步骤3：修改 app/build.gradle

plugins 块中不再指定版本：

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'  // 版本从根build.gradle继承
    id 'org.jetbrains.kotlin.kapt'     // 版本自动匹配Kotlin
}

android {
    namespace 'com.childproduct.designassistant'
    // ... 其余配置
}

// Room kapt优化配置
defaultConfig {
    // ... 其他配置

    kapt {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental", "true")
            arg("room.expandProjection", "true")
        }
    }
}

// 全局kapt配置
kapt {
    useBuildCache = true
    correctErrorTypes = true
    generateStubs = true
}
```

---

## 旧版解决方案（不推荐，仅供参考）

### 修复1：明确指定Kotlin和kapt版本

修改`app/build.gradle`中的plugins配置：

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android' version '1.9.22'
    id 'org.jetbrains.kotlin.kapt' version '1.9.22'  // ← 与Kotlin版本匹配
}
```

**重要**：Kotlin插件版本和kapt插件版本必须完全一致。

**注意**：此方案会导致与根 `build.gradle` 的 `buildscript` 块冲突，仅在没有 `buildscript` 块时使用。

### 修复2：添加Room kapt优化配置

在`defaultConfig`块中添加Room配置：

```groovy
defaultConfig {
    applicationId "com.childproduct.designassistant"
    minSdk 24
    targetSdk 34
    versionCode 1
    versionName "1.0.0"

    testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
        useSupportLibrary true
    }

    // Room kapt配置
    kapt {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental", "true")
            arg("room.expandProjection", "true")
        }
    }
}
```

在`android`块之后添加全局kapt配置：

```groovy
// 全局kapt配置
kapt {
    useBuildCache = true
    correctErrorTypes = true
    generateStubs = true
}
```

## 配置说明

### Room kapt参数

| 参数 | 说明 | 作用 |
|------|------|------|
| `room.schemaLocation` | schema文件存储位置 | 用于Room数据库迁移 |
| `room.incremental` | 启用增量编译 | 加速后续构建 |
| `room.expandProjection` | 扩展投影查询 | 优化复杂查询性能 |

### 全局kapt选项

| 选项 | 说明 | 好处 |
|------|------|------|
| `useBuildCache` | 使用构建缓存 | 加速构建 |
| `correctErrorTypes` | 修正错误类型 | 提供更准确的错误信息 |
| `generateStubs` | 生成stub文件 | 支持Java和Kotlin互操作 |

## 版本兼容性矩阵

| Kotlin版本 | kapt版本 | AGP版本 | Room版本 | 状态 |
|-----------|---------|---------|----------|------|
| 1.9.22 | 1.9.22 | 8.2.0 | 2.6.1 | ✅ 已测试 |
| 2.0.0+ | 2.0.0+ | 8.3.0+ | 2.6.1 | ✅ 推荐（未来） |

**注意**：如果升级到Kotlin 2.0+，需要同时升级：
- AGP到8.3.0或更高
- Compose Compiler Extension到1.5.14或更高

## 后续优化建议

### 1. 升级到Kotlin 2.0（可选）

如果需要更快的编译速度和更好的性能，可以考虑升级：

```groovy
plugins {
    id 'com.android.application' version '8.3.0'
    id 'org.jetbrains.kotlin.android' version '2.0.20'
    id 'org.jetbrains.kotlin.kapt' version '2.0.20'
}
```

### 2. 清理Gradle缓存

如果遇到缓存问题，可以手动清理：

```bash
./gradlew clean
./gradlew --stop
rm -rf ~/.gradle/caches/
```

### 3. 增加构建日志详细程度

在GitHub Actions中，如果需要更详细的错误信息，可以修改构建命令：

```bash
./gradlew assembleDebug --stacktrace --info --no-daemon
```

## 相关资源

- [Kotlin Gradle Plugin文档](https://kotlinlang.org/docs/gradle-configure-project.html)
- [Room数据库官方文档](https://developer.android.com/training/data-storage/room)
- [KAPT文档](https://kotlinlang.org/docs/kapt.html)

## 提交记录

| Commit | 说明 |
|--------|------|
| `da1350b` | fix: 修复Kotlin和kapt版本不匹配问题（已废弃） |
| `308186e` | feat: 添加Room kapt优化配置 |
| `ded85ac` | fix: 移除plugins块中的版本声明，避免与classpath冲突（部分修复） |
| `aa0c8d5` | refactor: 迁移到现代Gradle插件管理方式（推荐方案） |

## 工作原理

### plugins DSL vs buildscript

| 特性 | buildscript（旧式） | plugins DSL（新式） |
|------|---------------------|---------------------|
| 版本管理 | 在 `dependencies.classpath` 中 | 在 `plugins` 块中 |
| 可靠性 | 可能与 `plugins` 块冲突 | 类型安全，推荐方式 |
| 性能 | 慢（需要解析整个 classpath） | 快（延迟加载） |
| IDE支持 | 较弱 | 强（自动补全） |

### 版本解析流程

```
app/build.gradle (plugins)
    ↓ 继承版本
root/build.gradle (plugins with apply false)
    ↓ 解析插件
pluginManagement.repositories
    → Google / Maven Central / Gradle Portal
```

## 验证

修复后，GitHub Actions应该能够成功构建APK。检查构建日志，确认以下步骤正常完成：

1. ✅ Kotlin编译成功
2. ✅ kapt stub生成成功
3. ✅ Room注解处理成功
4. ✅ APK生成成功

如果构建仍然失败，请检查构建日志中的详细错误信息，重点关注：
- 依赖下载错误
- 注解处理错误
- 内存不足错误

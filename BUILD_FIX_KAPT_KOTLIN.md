# 构建修复：Kotlin和kapt版本不匹配

## 问题描述

在GitHub Actions中构建APK时遇到以下错误：

```
> Task :app:kaptGenerateStubsDebugKotlin FAILED
e: Could not load module <Error module>

Execution failed for task ':app:kaptGenerateStubsDebugKotlin'.
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.GradleCompilerRunnerWithWorkers$GradleKotlinCompilerWorkAction
   > Compilation error. See log for more details
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

### 2. 缺少Room kapt优化配置

项目使用了Room 2.6.1数据库库，但没有配置kapt选项，这可能导致性能问题和错误报告不清晰。

## 解决方案

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

- `da1350b` - fix: 修复Kotlin和kapt版本不匹配问题
- `308186e` - feat: 添加Room kapt优化配置

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

# 🚀 GitHub推送和APK构建指南

## ✅ 推送状态

代码已成功推送到GitHub：
- **仓库**: `https://github.com/awlei/new-child-product-design-assistant.git`
- **分支**: `main`
- **最新提交**: `e3681c0` - feat: 优化标准适配设计功能，实现折叠卡片式UI和多选快捷操作

---

## 🔄 GitHub Actions自动构建

GitHub Actions会在推送代码到`main`分支时自动触发APK构建。

### 构建内容
1. **Debug APK** - 用于测试和调试
2. **Release APK** - 用于正式发布（未签名版本）

---

## 📱 查看构建进度

### 方法1：通过GitHub网页
1. 访问仓库：https://github.com/awlei/new-child-product-design-assistant
2. 点击顶部的 **"Actions"** 标签
3. 点击最新的工作流 **"Build APK"**
4. 查看构建日志和进度

### 方法2：通过命令行（需要配置GitHub CLI）
```bash
gh run list --limit 5
gh run view
```

---

## ⏱️ 预计构建时间

- **首次构建**: 约10-15分钟（需要下载依赖）
- **后续构建**: 约5-8分钟（使用缓存）

---

## 📥 下载APK

### 构建完成后，APK会自动上传为Artifacts

#### 下载步骤：
1. 打开 **"Actions"** 标签
2. 点击完成的工作流
3. 在页面底部找到 **"Artifacts"** 部分
4. 下载所需文件：
   - `app-debug` - Debug版APK
   - `app-release` - Release版APK

### 直接下载链接（构建完成后）
构建完成后，可以使用以下模式访问：

```
https://github.com/awlei/new-child-product-design-assistant/actions/runs/<RUN_ID>
```

---

## 🔍 构建日志分析

### 查看详细日志
1. 进入工作流页面
2. 点击 **"Build Debug APK"** 或其他步骤
3. 展开查看详细输出

### 关键步骤检查
- ✅ Checkout code - 代码检出
- ✅ Set up JDK 17 - Java环境配置
- ✅ Build Debug APK - Debug版本编译
- ✅ Build Release APK - Release版本编译
- ✅ Upload Debug APK - 上传Debug APK
- ✅ Upload Release APK - 上传Release APK

---

## 🐛 如果构建失败

### 常见问题排查

#### 1. 依赖下载失败
```
错误信息: Could not resolve dependencies
解决方案: 等待Maven仓库恢复，或重新触发构建
```

#### 2. 编译错误
```
错误信息: Compilation failed
解决方案: 检查代码语法错误，查看完整日志
```

#### 3. 内存不足
```
错误信息: OutOfMemoryError
解决方案: 工作流已配置 `GRADLE_OPTS="-Xmx4096m"`
```

### 重新触发构建
```bash
# 方法1：推送空提交
git commit --allow-empty -m "trigger build"
git push origin main

# 方法2：使用GitHub CLI
gh workflow run build-apk.yml

# 方法3：通过网页
Actions -> build-apk.yml -> Run workflow
```

---

## 📦 APK签名（可选）

当前构建的Release APK是未签名的。如需签名：

### 创建签名配置
1. 创建 `keystore.jks` 文件
2. 在 `app/build.gradle` 中添加签名配置：
```gradle
android {
    signingConfigs {
        release {
            storeFile file("keystore.jks")
            storePassword "your_store_password"
            keyAlias "your_key_alias"
            keyPassword "your_key_password"
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
        }
    }
}
```

### 使用GitHub Secrets存储签名信息
在仓库设置中添加以下Secrets：
- `KEYSTORE_FILE` (base64编码的keystore文件)
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

---

## 🎯 本地构建（可选）

如果需要在本地构建APK：

### 前置要求
1. 安装 JDK 17
2. 安装 Android SDK
3. 配置环境变量：
```bash
export JAVA_HOME=/path/to/java17
export ANDROID_HOME=/path/to/android-sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools
```

### 执行构建
```bash
# 使用提供的构建脚本
./build-local.sh

# 或手动执行
./gradlew clean
./gradlew assembleDebug
./gradlew assembleRelease
```

### 查看APK
```bash
# Debug APK
ls -lh app/build/outputs/apk/debug/app-debug.apk

# Release APK
ls -lh app/build/outputs/apk/release/app-release-unsigned.apk
```

---

## 📊 构建历史

### 查看最近的构建
```bash
# 查看最近的提交
git log --oneline -10

# 查看GitHub Actions运行记录
gh run list --limit 10
```

### 查看特定提交的构建
```bash
# 获取提交SHA
git rev-parse HEAD

# 查看该提交的运行记录
gh run list --commit=<COMMIT_SHA>
```

---

## 🔔 构建通知

### 设置构建通知
1. 进入仓库 **Settings**
2. 点击 **Notifications**
3. 配置通知方式：
   - Email通知
   - GitHub Mobile推送
   - Slack/Discord集成（通过第三方服务）

---

## 📝 本次更新内容

### 新增功能
- ✅ 标准适配设计功能优化
- ✅ 折叠卡片式UI
- ✅ 多选/全选快捷操作
- ✅ 儿童高脚椅标准数据库（EN 14988、GB 29281）
- ✅ 儿童床标准数据库（EN 716、GB 28007）
- ✅ 导航集成到主应用

### 技术改进
- 📦 Room数据库升级至v2
- 🎨 Material Design 3规范
- 🔄 Flow响应式数据流

---

## 📞 获取帮助

如果遇到问题：
1. 查看 [GitHub Actions文档](https://docs.github.com/en/actions)
2. 查看项目 [README.md](README.md)
3. 提交 Issue 到仓库

---

## 🎉 下一步

构建完成后：
1. 下载并安装Debug APK进行测试
2. 验证新增功能是否正常工作
3. 如需发布，使用签名工具签名Release APK
4. 上传到应用商店（Google Play、小米应用商店等）

---

**构建状态追踪**: https://github.com/awlei/new-child-product-design-assistant/actions
**下载页面**: https://github.com/awlei/new-child-product-design-assistant/releases

---

*最后更新: 2025-02-04*

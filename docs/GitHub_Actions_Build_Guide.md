# GitHub Actions 构建指南

## 📱 当前构建状态

代码已成功推送到远程仓库，GitHub Actions 构建已自动启动。

### 🔗 查看构建状态

**GitHub Actions 工作流链接**:
```
https://github.com/awlei/new-child-product-design-assistant/actions
```

**最新构建链接**:
```
https://github.com/awlei/new-child-product-design-assistant/actions/runs/latest
```

---

## 🚀 构建工作流

### 工作流名称
Build Android APK (Improved)

### 触发条件
- ✅ 代码推送到 main/master/develop 分支
- ✅ Pull Request 到 main/master/develop 分支
- ✅ 手动触发 (workflow_dispatch)

### 构建步骤

1. **📥 Checkout code** - 检出代码
2. **☕ Set up JDK 17** - 配置Java 17环境
3. **📦 Install Gradle** - 安装Gradle 8.2
4. **💾 Cache Gradle packages** - 缓存依赖加速构建
5. **📋 Display Gradle info** - 显示构建信息
6. **🔨 Clean build cache** - 清理构建缓存
7. **🚀 Build Debug APK** - 构建Debug版本
8. **🚀 Build Release APK** - 构建Release版本
9. **📊 Get APK Info** - 获取APK信息
10. **📤 Upload Debug APK** - 上传Debug APK
11. **📤 Upload Release APK** - 上传Release APK
12. **✅ Build Summary** - 生成构建摘要

---

## 📦 构建产物

### Debug APK
- **文件名**: `app-debug-v{构建编号}.apk`
- **路径**: `app/build/outputs/apk/debug/app-debug.apk`
- **保留天数**: 30天
- **签名**: Debug签名（默认签名）

### Release APK
- **文件名**: `app-release-v{构建编号}.apk`
- **路径**: `app/build/outputs/apk/release/app-release-unsigned.apk`
- **保留天数**: 30天
- **签名**: 未签名（需要后续签名）

---

## ⏱️ 构建时间估算

- **首次构建**: 8-12分钟（需要下载所有依赖）
- **增量构建**: 3-5分钟（使用缓存）
- **清理构建**: 8-10分钟

---

## 📥 下载APK文件

### 方法1: 通过GitHub Actions界面（推荐）

1. 打开 GitHub Actions 页面:
   ```
   https://github.com/awlei/new-child-product-design-assistant/actions
   ```

2. 点击最新的 "Build Android APK (Improved)" 工作流

3. 在页面底部找到 "Artifacts" 区域

4. 点击下载对应的APK:
   - `app-debug-v{编号}` - Debug版本
   - `app-release-v{编号}` - Release版本

### 方法2: 使用 GitHub CLI

```bash
# 列出所有artifacts
gh run list

# 下载特定run的artifacts
gh run download {run-id}

# 下载最新的artifacts
gh run download --name app-debug-v{编号}
```

---

## 🔍 查看构建日志

### 在GitHub界面查看
1. 进入构建详情页面
2. 点击 "Build Android APK (Improved)"
3. 展开各个步骤查看详细日志

### 关键日志位置

**编译错误查看**:
- 查找 "Build Debug APK" 或 "Build Release APK" 步骤

**依赖下载问题**:
- 查找 "Cache Gradle packages" 步骤

**APK生成确认**:
- 查看 "Get APK Info" 步骤的输出

---

## 📱 应用信息

### 基本信息
- **应用名称**: 儿童产品设计助手
- **包名**: com.childproduct.designassistant
- **版本**: v1.1.0
- **版本代码**: 2

### Android版本
- **最低要求**: Android 7.0 (API 24)
- **目标版本**: Android 14 (API 34)
- **编译版本**: Android 14 (API 34)

### 应用大小
- **Debug版本**: 约 8-15MB
- **Release版本**: 约 6-12MB（优化后）

---

## ✅ 本次更新内容

### 核心功能更新
1. **R129r4e标准数据修正**
   - 更新身高分组阈值（40-83cm, 76-105cm等）
   - 修正伤害评估准则（HPC、头部加速度3ms、胸部加速度3ms、腹部压力）
   - 更新测试标准和测试条件

2. **构建错误修复**
   - 修复Kotlin字符串模板解析错误
   - 修复enum类命名冲突
   - 优化代码结构

### 技术细节
- **假人类型**: Q0, Q1, Q1.5, Q3, Q6, Q10
- **测试类型**: 正面撞击、后向撞击、侧面撞击
- **合格标准**: 基于ECE R129 Rev.4标准

---

## 🧪 测试APK

### 安装Debug APK
```bash
# 使用ADB安装
adb install app-debug.apk

# 如果已安装，使用-r参数覆盖
adb install -r app-debug.apk
```

### 验证R129r4e更新
1. 打开应用 → 选择"安全标准检查"功能
2. 输入测试身高：
   - 50cm → 应匹配 Group 0+（后向，40-83cm）
   - 80cm → 应跨接15个月界限
   - 90cm → 应匹配 Group I（前向，76-105cm）
   - 110cm → 应匹配 Group II/III（非整体式，100cm+）

3. 查看生成的测试矩阵：
   - 确认伤害评估准则包含HPC、头部加速度3ms、胸部加速度3ms、腹部压力
   - 确认合格标准按假人类型区分

---

## ⚠️ 常见问题

### Q1: 构建失败怎么办？
**A**:
1. 查看 "Build Failure Summary" 了解错误类型
2. 查看详细构建日志定位问题
3. 常见原因：
   - Gradle依赖下载失败 → 重试构建
   - 编译错误 → 检查代码语法
   - 内存不足 → 检查Gradle配置

### Q2: APK无法安装？
**A**:
- **Debug版本**: 可以直接安装（使用debug签名）
- **Release版本**: 需要签名后才能安装（未签名状态）
- **签名方式**: 使用 `jarsigner` 或 `apksigner` 进行签名

### Q3: 如何查看构建历史？
**A**:
1. 进入 GitHub Actions 页面
2. 左侧可以筛选工作流、分支、状态
3. 点击任意构建查看详情

### Q4: 构建时间太长？
**A**:
- 首次构建需要下载所有依赖，时间较长
- 后续构建会使用缓存，速度会加快
- 可以在Actions设置中调整缓存保留策略

---

## 🔧 手动触发构建

如果需要手动触发构建（不推送代码）：

1. 进入 GitHub Actions 页面
2. 点击左侧 "Build Android APK (Improved)"
3. 点击 "Run workflow" 按钮
4. 选择分支和构建类型（debug/release/all）
5. 点击 "Run workflow"

---

## 📊 构建状态码

| 状态 | 含义 |
|------|------|
| ✅ Success | 构建成功，APK已生成 |
| ❌ Failed | 构建失败，查看日志排查 |
| 🟡 In Progress | 构建进行中 |
| ⏸️ Queued | 构建排队中 |
| 🚫 Cancelled | 构建已取消 |

---

## 📞 支持

如果遇到问题：
1. 查看 GitHub Actions 构建日志
2. 检查 `docs/` 目录下的相关文档
3. 查看项目 README.md

---

**构建时间**: 当前构建正在运行中...
**预计完成**: 3-5分钟后（如果使用缓存）
**下载链接**: 构建完成后在 GitHub Actions 页面下载


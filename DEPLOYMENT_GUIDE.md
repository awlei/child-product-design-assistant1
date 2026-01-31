# 🚀 GitHub 构建部署指南

本文档指导你如何在 GitHub 上自动构建儿童产品设计助手 APK。

## 📋 前置准备

### 1. GitHub 账号
确保你有一个 GitHub 账号（如果没有，请先注册：https://github.com/signup）

### 2. 本地环境（可选）
如果你想本地测试，需要安装：
- Git
- JDK 17
- Android Studio（可选）

---

## 🔄 部署步骤

### 步骤 1：创建 GitHub 仓库

1. 登录 GitHub
2. 点击右上角的 `+` 按钮
3. 选择 `New repository`
4. 填写仓库信息：
   - **Repository name**: `ChildProductDesignAssistant`（或自定义名称）
   - **Description**: 儿童产品设计助手 Android 应用
   - **Visibility**: 选择 `Public`（公开）或 `Private`（私有）
   - 勾选 `Add a README file`
5. 点击 `Create repository`

### 步骤 2：上传项目文件

#### 方法 A：通过网页界面上传（适合文件较少）

1. 在新创建的仓库页面，点击 `uploading an existing file`
2. 将项目文件拖拽到上传区域
3. 在底部提交信息框中填写：
   - `Commit message`: `Initial commit: Child Product Design Assistant`
4. 点击 `Commit changes`

#### 方法 B：通过 Git 命令行上传（推荐）

```bash
# 1. 进入项目目录
cd /workspace/projects

# 2. 初始化 Git 仓库
git init

# 3. 添加所有文件
git add .

# 4. 提交更改
git commit -m "Initial commit: Child Product Design Assistant"

# 5. 设置远程仓库（替换 YOUR_USERNAME 和 REPO_NAME）
git remote add origin https://github.com/YOUR_USERNAME/REPO_NAME.git

# 6. 推送到 GitHub（首次推送需要输入用户名和密码/token）
git branch -M main
git push -u origin main
```

**提示**: 如果推送时需要身份验证，GitHub 现在推荐使用 Personal Access Token (PAT) 而不是密码。

### 步骤 3：配置 GitHub Actions

项目已包含 `.github/workflows/build-apk.yml` 文件，无需额外配置。

当你推送代码后，GitHub Actions 会自动触发构建流程。

### 步骤 4：查看构建状态

1. 进入你的 GitHub 仓库
2. 点击顶部的 **Actions** 标签
3. 你会看到 `Build Android APK` workflow 正在运行
4. 点击具体的 workflow 查看详细日志

### 步骤 5：下载 APK

构建完成后（通常需要 5-10 分钟）：

1. 在 workflow 运行页面，滚动到底部
2. 找到 **Artifacts** 区域
3. 点击 `app-debug` 或 `app-release` 下载 ZIP 文件
4. 解压 ZIP 文件，得到 `.apk` 文件

### 步骤 6：安装到 Android 设备

#### 方法 A：直接安装
1. 将 APK 文件传输到 Android 设备（通过 USB、蓝牙、网盘等）
2. 在设备上找到 APK 文件
3. 点击安装
4. 如有提示，允许「未知来源」应用安装

#### 方法 B：使用 ADB（开发者）
```bash
# 连接设备
adb devices

# 安装 APK
adb install app-debug.apk

# 启动应用
adb shell am start -n com.childproduct.designassistant/.MainActivity
```

---

## ⚙️ GitHub Actions 工作流说明

### 构建流程
```
Checkout 代码 → 设置 JDK 17 → 缓存 Gradle → 构建 Debug APK → 构建 Release APK → 上传 Artifact
```

### 触发条件
- `push` 到 `main`、`master` 或 `develop` 分支
- 创建 `pull_request` 到这些分支
- 手动触发 `workflow_dispatch`

### 输出产物
- `app-debug.apk`: 调试版本（可以直接安装）
- `app-release-unsigned.apk`: 发布版本（未签名）

---

## 🔐 配置签名版本（可选）

如需构建带签名的 Release APK，需要配置 GitHub Secrets：

### 1. 生成签名密钥
```bash
keytool -genkey -v -keystore release-keystore.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias your-alias-name
```

### 2. 转换为 Base64
```bash
# macOS
base64 -i release-keystore.jks | pbcopy

# Linux
base64 -w 0 release-keystore.jks > keystore-base64.txt
cat keystore-base64.txt
```

### 3. 在 GitHub 添加 Secrets
1. 进入仓库 Settings
2. 左侧选择 `Secrets and variables` → `Actions`
3. 点击 `New repository secret`
4. 添加以下 Secrets：

| Secret 名称 | 说明 | 示例 |
|------------|------|------|
| `SIGNING_KEYSTORE_BASE64` | Base64 编码的 keystore 文件 | `u3Ny...（很长的字符串）` |
| `SIGNING_KEYSTORE_PASSWORD` | Keystore 密码 | `your-password` |
| `SIGNING_KEY_ALIAS` | Key 别名 | `your-alias-name` |
| `SIGNING_KEY_PASSWORD` | Key 密码 | `your-key-password` |

### 4. 更新 Workflow
修改 `.github/workflows/build-apk.yml` 中的签名逻辑（已包含占位代码）

---

## 🐛 常见问题

### Q1: 构建失败怎么办？
- 查看 Actions 日志中的错误信息
- 常见原因：Gradle 版本不匹配、依赖下载失败
- 尝试重新推送代码触发新构建

### Q2: APK 安装失败？
- 确保开启了「未知来源」安装权限
- 检查 Android 版本是否 ≥ 7.0
- 尝试卸载旧版本后重新安装

### Q3: 如何更新应用？
- 修改代码后推送新提交到 GitHub
- GitHub Actions 自动构建新版本
- 下载新 APK 覆盖安装

### Q4: 构建时间太长？
- 首次构建较慢（约 10-15 分钟）
- 后续构建会使用缓存，更快（约 3-5 分钟）

---

## 📊 性能优化建议

1. **启用 Gradle 缓存**: 已在 workflow 中配置
2. **并行构建**: 可根据需要调整 workflow
3. **使用 GitHub 宿主 Runner**: 免费且稳定
4. **减少构建频率**: 仅在必要时推送

---

## 🔗 相关链接

- [GitHub Actions 文档](https://docs.github.com/en/actions)
- [Gradle 官方文档](https://docs.gradle.org/)
- [Android 开发指南](https://developer.android.com/guide)

---

**💡 提示**: 如遇到问题，可以查看 [GitHub Issues](https://github.com/YOUR_USERNAME/REPO_NAME/issues) 或提交新的 Issue。

# GitHub Actions 构建说明

## 概述

代码已成功推送到GitHub，GitHub Actions会自动触发APK构建。

---

## 查看构建状态

### 方法1：通过GitHub网页查看

1. 访问项目主页：
   ```
   https://github.com/awlei/new-child-product-design-assistant
   ```

2. 点击顶部的 **"Actions"** 标签

3. 查看最新的构建运行状态：
   - ✅ **绿色** = 构建成功
   - 🔵 **蓝色** = 构建进行中
   - ❌ **红色** = 构建失败

### 方法2：通过GitHub CLI查看

```bash
# 安装GitHub CLI（如果未安装）
# Ubuntu/Debian
curl -fsSL https://cli.github.com/packages/githubcli-archive-keyring.gpg | sudo dd of=/usr/share/keyrings/githubcli-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" | sudo tee /etc/apt/sources.list.d/github-cli.list > /dev/null
sudo apt update
sudo apt install gh

# 登录GitHub
gh auth login

# 查看构建状态
gh run list --repo awlei/new-child-product-design-assistant

# 查看最新构建的详细信息
gh run view --repo awlei/new-child-product-design-assistant
```

---

## 下载APK

### 方法1：通过GitHub网页下载

1. 访问项目主页：
   ```
   https://github.com/awlei/new-child-product-design-assistant
   ```

2. 点击顶部的 **"Actions"** 标签

3. 点击最新的构建运行（绿色的✅或蓝色的🔵）

4. 在页面底部找到 **"Artifacts"** 部分

5. 点击下载：
   - **app-debug** - Debug版本的APK（测试用）
   - **app-release** - Release版本的APK（发布用）

### 方法2：通过GitHub CLI下载

```bash
# 列出可用的artifacts
gh run view --repo awlei/new-child-product-design-assistant --log

# 下载Debug版本的APK
gh run download --repo awlei/new-child-product-design-assistant --name app-debug

# 下载Release版本的APK
gh run download --repo awlei/new-child-product-design-assistant --name app-release
```

---

## 手动触发构建

如果需要手动触发构建（不等待代码推送）：

### 方法1：通过GitHub网页手动触发

1. 访问项目主页：
   ```
   https://github.com/awlei/new-child-product-design-assistant
   ```

2. 点击顶部的 **"Actions"** 标签

3. 在左侧选择 **"Build APK"** 工作流

4. 点击右侧的 **"Run workflow"** 按钮

5. 选择 **main** 分支

6. 点击 **"Run workflow"** 确认

### 方法2：通过GitHub CLI手动触发

```bash
# 触发构建
gh workflow run "Build APK" --repo awlei/new-child-product-design-assistant

# 查看运行状态
gh run list --repo awlei/new-child-product-design-assistant
```

---

## 创建GitHub Release

如果需要创建GitHub Release（发布版本）：

### 方法1：通过GitHub网页创建

1. 访问项目主页：
   ```
   https://github.com/awlei/new-child-product-design-assistant
   ```

2. 点击顶部的 **"Actions"** 标签

3. 在左侧选择 **"Build APK"** 工作流

4. 点击右侧的 **"Run workflow"** 按钮

5. 选择 **main** 分支

6. 在 **"Release"** 选项中选择 **"true"**

7. 点击 **"Run workflow"** 确认

### 方法2：通过GitHub CLI创建

```bash
# 触发构建并创建Release
gh workflow run "Build APK" --repo awlei/new-child-product-design-assistant -f release=true
```

---

## 构建时间预估

- **Debug版本**：约10-15分钟
- **Release版本**：约15-20分钟
- **总时间**：约20-30分钟

---

## 构建失败排查

如果构建失败，请查看构建日志：

1. 访问项目主页：
   ```
   https://github.com/awlei/new-child-product-design-assistant
   ```

2. 点击顶部的 **"Actions"** 标签

3. 点击失败的构建运行

4. 点击失败的步骤，查看详细错误信息

---

## 本地构建（可选）

如果需要在本地构建APK：

### 前提条件

- JDK 17或更高版本
- Android SDK
- Gradle 8.2或更高版本

### 构建步骤

```bash
# 1. 克隆仓库
git clone https://github.com/awlei/new-child-product-design-assistant.git
cd new-child-product-design-assistant

# 2. 授予gradlew执行权限
chmod +x gradlew

# 3. 构建Debug版本APK
./gradlew assembleDebug

# 4. 构建Release版本APK
./gradlew assembleRelease

# 5. 查看APK文件
ls -lh app/build/outputs/apk/debug/app-debug.apk
ls -lh app/build/outputs/apk/release/app-release-unsigned.apk
```

---

## 联系方式

如有问题，请通过以下方式联系：

- GitHub Issues：
  ```
  https://github.com/awlei/new-child-product-design-assistant/issues
  ```

---

**文档版本**：v1.0  
**最后更新**：2024-01-20  
**作者**：Coze Coding Agent

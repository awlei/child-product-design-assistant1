# 推送到 GitHub 指南

## 📋 前提条件

1. 你需要有一个 GitHub 账号
2. 需要在 GitHub 上创建一个新的仓库
3. 需要配置 GitHub 认证（HTTPS token 或 SSH 密钥）

## 🚀 推送步骤

### 方法一：使用推送脚本（推荐）

#### 1. 添加远程仓库

```bash
# 将 YOUR_USERNAME 替换为你的 GitHub 用户名
# 将 YOUR_REPO 替换为你的仓库名称
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git

# 例如：
# git remote add origin https://github.com/awlei/new-child-product-design-assistant.git
```

#### 2. 运行推送脚本

```bash
chmod +x push-to-github.sh
./push-to-github.sh
```

脚本会自动：
- 检查远程仓库配置
- 提交未提交的更改
- 推送代码到 GitHub
- 显示构建链接

---

### 方法二：手动推送

#### 1. 配置远程仓库

```bash
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git
```

#### 2. 推送代码

```bash
# 如果还没有提交过，先提交
git add .
git commit -m "Initial commit"

# 推送到 GitHub
git push -u origin main
```

---

## 🔐 GitHub 认证配置

### HTTPS 方式（推荐新手）

#### 1. 生成 Personal Access Token

1. 登录 GitHub
2. 进入 Settings → Developer settings → Personal access tokens → Tokens (classic)
3. 点击 "Generate new token (classic)"
4. 设置 token 名称和过期时间
5. 勾选以下权限：
   - `repo` (完整仓库访问权限)
   - `workflow` (GitHub Actions 权限)
6. 点击 "Generate token"
7. **重要**：复制 token（只显示一次）

#### 2. 配置 Git 使用 Token

```bash
# 设置 Git 凭据助手
git config --global credential.helper store

# 推送时会提示输入用户名和密码
# 用户名：你的 GitHub 用户名
# 密码：刚才生成的 Personal Access Token
```

或者一次性设置：

```bash
git remote set-url origin https://YOUR_USERNAME:YOUR_TOKEN@github.com/YOUR_USERNAME/YOUR_REPO.git
```

---

### SSH 方式（推荐有经验的用户）

#### 1. 生成 SSH 密钥

```bash
# 生成 SSH 密钥（如果还没有）
ssh-keygen -t ed25519 -C "your_email@example.com"

# 查看公钥
cat ~/.ssh/id_ed25519.pub
```

#### 2. 添加 SSH 密钥到 GitHub

1. 复制公钥内容
2. 进入 GitHub Settings → SSH and GPG keys
3. 点击 "New SSH key"
4. 粘贴公钥内容
5. 点击 "Add SSH key"

#### 3. 测试 SSH 连接

```bash
ssh -T git@github.com
```

#### 4. 使用 SSH URL

```bash
git remote set-url origin git@github.com:YOUR_USERNAME/YOUR_REPO.git
```

---

## 📦 查看构建状态

推送成功后：

1. 访问你的 GitHub 仓库
2. 点击 "Actions" 标签
3. 查看 "Build APK" workflow 的运行状态
4. 等待构建完成（通常需要 5-10 分钟）
5. 在 workflow 运行结果中下载 APK 文件

---

## ❌ 常见问题

### 问题 1：推送时提示认证失败

**解决方法**：
- 检查 token 是否正确
- 检查 token 是否有足够的权限
- 尝试重新生成 token

### 问题 2：推送后 Actions 没有运行

**解决方法**：
- 检查 `.github/workflows/build-apk.yml` 文件是否存在
- 检查分支名称是否为 `main`
- 尝试手动触发 workflow：
  1. 进入 Actions 页面
  2. 选择 "Build APK" workflow
  3. 点击 "Run workflow"

### 问题 3：构建失败

**解决方法**：
- 查看 workflow 运行日志
- 检查 Gradle 版本是否兼容
- 检查依赖版本是否正确

---

## 📞 获取帮助

如果遇到问题，可以：

1. 查看 [GitHub 文档](https://docs.github.com/zh)
2. 查看 [Actions 文档](https://docs.github.com/en/actions)
3. 在仓库的 Issues 中提问

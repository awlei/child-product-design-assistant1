# ⚡ 快速开始指南

## 🎯 一分钟部署到 GitHub

### 步骤 1：准备 GitHub 仓库
1. 访问 https://github.com/new
2. 创建新仓库（仓库名：`ChildProductDesignAssistant`）
3. 点击 "Create repository"

### 步骤 2：推送代码
```bash
# 方法 A：使用自动脚本（推荐）
chmod +x deploy_to_github.sh
./deploy_to_github.sh YOUR_USERNAME ChildProductDesignAssistant

# 方法 B：手动推送
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/ChildProductDesignAssistant.git
git branch -M main
git push -u origin main
```

### 步骤 3：等待自动构建
1. 进入仓库的 **Actions** 标签
2. 等待 "Build Android APK" 完成（约 5-10 分钟）
3. 在 workflow 结果中下载 APK

### 步骤 4：安装到手机
1. 解压下载的 ZIP 文件
2. 将 `app-debug.apk` 传输到 Android 设备
3. 点击安装即可

## 📱 应用功能演示

### 1. 创意生成 🎨
```
打开应用 → 创意生成 → 选择"3-6岁" → 选择"玩具" → 点击生成
→ 查看生成的创意、功能、色彩方案
```

### 2. 安全检查 🛡️
```
切换到"安全检查" → 输入"积木玩具" → 选择"3-6岁" → 点击检查
→ 查看安全评分、检查项、改进建议
```

### 3. 设计文档 📝
```
切换到"设计文档" → 输入"益智积木套装" → 点击生成
→ 查看完整的设计文档（8个章节）
```

## 🔧 常见问题

### Q: GitHub Actions 构建失败？
A: 查看构建日志，常见原因：
- Gradle 下载超时 → 重新运行 workflow
- 依赖版本冲突 → 更新 build.gradle

### Q: APK 无法安装？
A:
- 开启"未知来源"安装权限
- 检查 Android 版本是否 ≥ 7.0
- 尝试卸载旧版本后重新安装

### Q: 如何更新应用？
A:
- 修改代码后推送新提交
- GitHub Actions 自动构建新版本
- 下载新 APK 覆盖安装

## 📞 获取帮助

- 📖 详细文档：查看 README.md
- 🚀 部署指南：查看 DEPLOYMENT_GUIDE.md
- 📊 项目总结：查看 PROJECT_SUMMARY.md
- 🔍 验证项目：运行 `./validate_project.sh`

## ✅ 部署检查清单

- [ ] 创建 GitHub 仓库
- [ ] 推送所有代码文件
- [ ] GitHub Actions 开始构建
- [ ] 构建成功完成
- [ ] 下载 APK 文件
- [ ] 安装到 Android 设备
- [ ] 测试应用功能

**🎉 完成以上步骤，你将获得一个功能完整的儿童产品设计助手 APK！**

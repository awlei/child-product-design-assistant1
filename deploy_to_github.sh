#!/bin/bash

# 快速部署到 GitHub 脚本
# 使用方法：./deploy_to_github.sh YOUR_USERNAME REPO_NAME

set -e

if [ -z "$1" ] || [ -z "$2" ]; then
    echo "❌ 使用方法: ./deploy_to_github.sh YOUR_USERNAME REPO_NAME"
    echo ""
    echo "示例:"
    echo "  ./deploy_to_github.sh johndoe ChildProductDesignAssistant"
    echo ""
    echo "这将创建仓库: https://github.com/johndoe/ChildProductDesignAssistant"
    exit 1
fi

USERNAME=$1
REPO_NAME=$2
GITHUB_REPO="https://github.com/$USERNAME/$REPO_NAME.git"

echo "🚀 开始部署到 GitHub..."
echo "📍 仓库地址: $GITHUB_REPO"
echo ""

# 检查是否已初始化 Git
if [ ! -d ".git" ]; then
    echo "📦 初始化 Git 仓库..."
    git init
    git branch -M main
else
    echo "✅ Git 仓库已初始化"
fi

# 检查是否有远程仓库
if git remote get-url origin > /dev/null 2>&1; then
    echo "⚠️  已存在远程仓库，更新 remote URL..."
    git remote set-url origin "$GITHUB_REPO"
else
    echo "➕ 添加远程仓库..."
    git remote add origin "$GITHUB_REPO"
fi

echo ""
echo "📝 添加所有文件..."
git add .

echo ""
echo "💾 创建提交..."
git commit -m "Initial commit: Child Product Design Assistant

✨ Features:
- Creative idea generation based on age groups
- Safety check with 8 inspection categories
- Design document generation
- Modern UI with Jetpack Compose

🔧 Tech Stack:
- Kotlin
- Jetpack Compose
- MVVM Architecture
- GitHub Actions for automatic APK building"

echo ""
echo "⚠️  准备推送到 GitHub..."
echo "请在 GitHub 上创建仓库: $GITHUB_REPO"
echo ""
echo "仓库创建步骤:"
echo "1. 访问 https://github.com/new"
echo "2. 仓库名称: $REPO_NAME"
echo "3. 描述: 儿童产品设计助手 Android 应用"
echo "4. 选择 Public 或 Private"
echo "5. 点击 'Create repository'"
echo ""
read -p "仓库已创建？按 Enter 继续..."

echo ""
echo "📤 推送到 GitHub..."
echo "首次推送可能需要 GitHub 身份验证"
echo ""

# 尝试推送
if git push -u origin main; then
    echo ""
    echo "✅ 推送成功！"
    echo ""
    echo "🎉 部署完成！"
    echo ""
    echo "📋 下一步操作:"
    echo "1. 访问仓库: $GITHUB_REPO"
    echo "2. 点击 'Actions' 标签查看构建状态"
    echo "3. 等待构建完成（约 5-10 分钟）"
    echo "4. 下载 APK 文件"
    echo ""
    echo "📱 构建完成后，你将获得:"
    echo "  - app-debug.apk (调试版本)"
    echo "  - app-release-unsigned.apk (发布版本)"
    echo ""
else
    echo ""
    echo "❌ 推送失败"
    echo ""
    echo "常见原因:"
    echo "1. 仓库尚未在 GitHub 上创建"
    echo "2. 身份验证失败（需要 GitHub Personal Access Token）"
    echo "3. 网络连接问题"
    echo ""
    echo "解决方法:"
    echo "1. 先在 GitHub 上创建仓库: https://github.com/new"
    echo "2. 重新运行此脚本"
    echo "3. 或手动推送: git push -u origin main"
    echo ""
    exit 1
fi

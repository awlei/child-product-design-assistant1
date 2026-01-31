#!/bin/bash

# 推送到GitHub并触发APK构建的脚本

echo "=========================================="
echo "  推送代码到GitHub并触发APK构建"
echo "=========================================="
echo ""

# 检查是否有远程仓库
if [ -z "$(git remote -v)" ]; then
    echo "❌ 没有配置GitHub远程仓库"
    echo ""
    echo "请先添加远程仓库："
    echo "  git remote add origin https://github.com/<用户名>/<仓库名>.git"
    echo ""
    echo "例如："
    echo "  git remote add origin https://github.com/awlei/new-child-product-design-assistant.git"
    echo ""
    echo "然后再运行此脚本。"
    exit 1
fi

echo "✓ 找到远程仓库："
git remote -v
echo ""

# 检查当前分支
CURRENT_BRANCH=$(git branch --show-current)
echo "✓ 当前分支: $CURRENT_BRANCH"
echo ""

# 显示待提交的更改
if [ -n "$(git status --porcelain)" ]; then
    echo "⚠️  有未提交的更改："
    git status --short
    echo ""
    read -p "是否先提交这些更改？(y/N) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        git add .
        echo "请输入提交信息："
        read commit_msg
        git commit -m "$commit_msg"
        echo "✓ 提交成功"
    fi
    echo ""
fi

# 推送到GitHub
echo "📤 推送到GitHub..."
git push origin $CURRENT_BRANCH

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 推送成功！"
    echo ""
    echo "📦 GitHub Actions 将自动开始构建 APK"
    echo ""
    echo "查看构建状态："
    echo "  https://github.com/$(git config --get remote.origin.url | sed 's|.*github.com/||' | sed 's|\.git||')/actions"
else
    echo ""
    echo "❌ 推送失败"
    echo ""
    echo "请检查："
    echo "  1. 是否已配置正确的远程仓库地址"
    echo "  2. 是否有GitHub访问权限"
    echo "  3. 是否已配置GitHub认证（token或SSH密钥）"
    exit 1
fi

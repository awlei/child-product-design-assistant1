#!/bin/bash

# 快速状态检查脚本

echo "🚀 儿童产品设计助手 - 快速状态检查"
echo "=================================="

# 颜色
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

# 1. Git 状态
echo -e "\n📋 Git 状态"
echo "----------------------------------"
git log --oneline -1
echo "分支: $(git branch --show-current)"
if [ -n "$(git status --porcelain)" ]; then
    echo "状态: 有未提交更改"
else
    echo "状态: 干净"
fi

# 2. 远程仓库
echo -e "\n🌐 远程仓库"
echo "----------------------------------"
if git remote get-url origin >/dev/null 2>&1; then
    echo "✓ 远程仓库已配置"
    git remote get-url origin | sed 's/.*github.com\///' | sed 's/\.git$//'
else
    echo "✗ 未配置远程仓库"
fi

# 3. 项目验证
echo -e "\n✅ 项目验证"
echo "----------------------------------"
if [ -f "build.gradle" ]; then echo "✓ build.gradle"; else echo "✗ build.gradle"; fi
if [ -f "app/build.gradle" ]; then echo "✓ app/build.gradle"; else echo "✗ app/build.gradle"; fi
if [ -f ".github/workflows/build-apk.yml" ]; then echo "✓ GitHub Actions"; else echo "✗ GitHub Actions"; fi

# 4. 文件统计
echo -e "\n📊 文件统计"
echo "----------------------------------"
KOTLIN_COUNT=$(find app/src/main/java -name "*.kt" 2>/dev/null | wc -l)
WORKFLOW_COUNT=$(find .github/workflows -name "*.yml" 2>/dev/null | wc -l)
echo "Kotlin 文件: $KOTLIN_COUNT"
echo "Workflow 文件: $WORKFLOW_COUNT"

# 5. 构建状态
echo -e "\n🔄 构建状态"
echo "----------------------------------"
echo "当前提交: $(git log --oneline -1 | cut -d' ' -f1)"
echo "Actions 状态: 访问 https://github.com/awlei/new-child-product-design-assistant/actions"

# 6. 快速操作
echo -e "\n⚡ 快速操作"
echo "----------------------------------"
echo "1. 查看完整状态: ./scripts/build-monitor.sh"
echo "2. 验证项目: ./validate_project.sh"
echo "3. 部署到 GitHub: ./deploy_to_github.sh"
echo "4. 查看文档: cat BUILD_TRACKING.md"

echo -e "\n=================================="
echo -e "${GREEN}✓${NC} 检查完成"

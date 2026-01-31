#!/bin/bash

# 构建监控和改进脚本

echo "🔍 构建状态检查和改进工具"
echo "================================"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 检查 Git 状态
check_git_status() {
    echo -e "\n📋 Git 状态检查"
    echo "--------------------"
    git status
}

# 检查最近的提交
check_recent_commits() {
    echo -e "\n📝 最近提交"
    echo "--------------------"
    git log --oneline -5
}

# 验证项目结构
validate_project() {
    echo -e "\n✅ 项目验证"
    echo "--------------------"

    # 检查必要文件
    required_files=(
        "build.gradle"
        "settings.gradle"
        "app/build.gradle"
        "app/src/main/AndroidManifest.xml"
    )

    for file in "${required_files[@]}"; do
        if [ -f "$file" ]; then
            echo -e "${GREEN}✓${NC} $file"
        else
            echo -e "${RED}✗${NC} $file (缺失)"
        fi
    done

    # 检查 Kotlin 文件
    kotlin_count=$(find app/src/main/java -name "*.kt" | wc -l)
    echo -e "${GREEN}✓${NC} Kotlin 文件: $kotlin_count"
}

# 检查 Gradle 配置
check_gradle_config() {
    echo -e "\n⚙️  Gradle 配置"
    echo "--------------------"

    if [ -f "gradle/wrapper/gradle-wrapper.properties" ]; then
        gradle_version=$(grep "distributionUrl" gradle/wrapper/gradle-wrapper.properties | grep -oP '\d+\.\d+')
        echo -e "${GREEN}✓${NC} Gradle 版本: $gradle_version"
    fi

    if [ -f "build.gradle" ]; then
        agp_version=$(grep "com.android.tools.build:gradle" build.gradle | grep -oP '\d+\.\d+\.\d+')
        echo -e "${GREEN}✓${NC} Android Gradle Plugin: $agp_version"
    fi
}

# 验证 GitHub Actions
validate_github_actions() {
    echo -e "\n🔄 GitHub Actions 验证"
    echo "--------------------"

    workflow_dir=".github/workflows"
    if [ -d "$workflow_dir" ]; then
        workflow_count=$(ls "$workflow_dir"/*.yml 2>/dev/null | wc -l)
        echo -e "${GREEN}✓${NC} Workflow 文件: $workflow_count"

        for workflow in "$workflow_dir"/*.yml; do
            if [ -f "$workflow" ]; then
                workflow_name=$(basename "$workflow")
                echo -e "  - $workflow_name"

                # 检查是否使用最新版本
                if grep -q "actions/upload-artifact@v4" "$workflow"; then
                    echo -e "    ${GREEN}✓${NC} 使用 artifact v4"
                elif grep -q "actions/upload-artifact@v3" "$workflow"; then
                    echo -e "    ${YELLOW}⚠${NC} 使用 artifact v3 (已废弃)"
                fi
            fi
        done
    else
        echo -e "${RED}✗${NC} 未找到 .github/workflows 目录"
    fi
}

# 提供改进建议
suggest_improvements() {
    echo -e "\n💡 改进建议"
    echo "--------------------"
    echo "1. 优化构建速度："
    echo "   - 使用 Gradle 缓存"
    echo "   - 并行构建"
    echo ""
    echo "2. 增强错误处理："
    echo "   - 添加构建失败通知"
    echo "   - 记录详细日志"
    echo ""
    echo "3. 改进 APK 管理："
    echo "   - 版本号管理"
    echo "   - 签名配置"
}

# 推送前检查
pre_push_check() {
    echo -e "\n🚀 推送前检查"
    echo "--------------------"

    # 检查是否有未提交的更改
    if [ -n "$(git status --porcelain)" ]; then
        echo -e "${YELLOW}⚠${NC} 有未提交的更改"
        git status --short
    else
        echo -e "${GREEN}✓${NC} 没有未提交的更改"
    fi

    # 检查远程仓库
    if git remote get-url origin >/dev/null 2>&1; then
        echo -e "${GREEN}✓${NC} 远程仓库已配置"
    else
        echo -e "${RED}✗${NC} 未配置远程仓库"
    fi
}

# 主函数
main() {
    check_git_status
    check_recent_commits
    validate_project
    check_gradle_config
    validate_github_actions
    suggest_improvements
    pre_push_check

    echo -e "\n================================"
    echo -e "${GREEN}✓${NC} 检查完成"
    echo ""
    echo "下一步操作："
    echo "1. 如有未提交更改：git add . && git commit"
    echo "2. 推送到 GitHub：git push"
    echo "3. 查看 Actions：访问 GitHub 仓库页面"
}

# 执行主函数
main

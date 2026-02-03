#!/bin/bash

# GitHub Actions 构建状态检查脚本
# 使用方法: ./check-build-status.sh

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# GitHub仓库信息
REPO="awlei/new-child-product-design-assistant"
WORKFLOW="build-apk.yml"
API_URL="https://api.github.com/repos/$REPO/actions/workflows/$WORKFLOW/runs"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}GitHub Actions 构建状态检查${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# 检查是否安装了必要的工具
if ! command -v curl &> /dev/null; then
    echo -e "${RED}❌ 错误: 未安装 curl${NC}"
    echo "请先安装 curl: sudo apt-get install curl (Linux) 或 brew install curl (macOS)"
    exit 1
fi

if ! command -v jq &> /dev/null; then
    echo -e "${YELLOW}⚠️  警告: 未安装 jq (可选，用于格式化JSON输出)${NC}"
    echo "安装 jq 以获得更好的输出格式: sudo apt-get install jq"
    HAS_JQ=false
else
    HAS_JQ=true
fi

echo -e "${BLUE}正在检查构建状态...${NC}"
echo ""

# 获取构建状态
if [ "$HAS_JQ" = true ]; then
    RESPONSE=$(curl -s -H "Accept: application/vnd.github.v3+json" "$API_URL")
    
    if [ -z "$RESPONSE" ]; then
        echo -e "${RED}❌ 无法获取构建信息${NC}"
        echo "请检查网络连接或GitHub仓库访问权限"
        exit 1
    fi
    
    # 解析JSON
    TOTAL_RUNS=$(echo "$RESPONSE" | jq -r '.total_count')
    
    if [ "$TOTAL_RUNS" -eq 0 ]; then
        echo -e "${YELLOW}⚠️  未找到构建记录${NC}"
        echo "请确保已经推送代码到GitHub仓库"
        exit 0
    fi
    
    LATEST_RUN=$(echo "$RESPONSE" | jq -r '.workflow_runs[0]')
    STATUS=$(echo "$LATEST_RUN" | jq -r '.status')
    CONCLUSION=$(echo "$LATEST_RUN" | jq -r '.conclusion // "运行中"')
    CREATED_AT=$(echo "$LATEST_RUN" | jq -r '.created_at')
    UPDATED_AT=$(echo "$LATEST_RUN" | jq -r '.updated_at')
    RUN_NUMBER=$(echo "$LATEST_RUN" | jq -r '.run_number')
    HTML_URL=$(echo "$LATEST_RUN" | jq -r '.html_url')
    
    # 显示状态
    echo -e "${BLUE}构建信息:${NC}"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "📊 运行次数: $RUN_NUMBER"
    echo "📅 创建时间: $CREATED_AT"
    echo "🔄 更新时间: $UPDATED_AT"
    echo "🔗 链接: $HTML_URL"
    echo ""
    
    echo -e "${BLUE}构建状态:${NC}"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    
    case "$STATUS" in
        "completed")
            if [ "$CONCLUSION" = "success" ]; then
                echo -e "${GREEN}✅ 构建成功${NC}"
                echo ""
                echo -e "${BLUE}构建产物:${NC}"
                echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
                echo "📦 Debug APK: 可在Artifacts中下载"
                echo "📦 Release APK: 可在Artifacts中下载"
                echo ""
                echo -e "${BLUE}下载步骤:${NC}"
                echo "1. 访问: $HTML_URL"
                echo "2. 滚动到底部"
                echo "3. 在 'Artifacts' 部分下载APK"
            else
                echo -e "${RED}❌ 构建失败${NC}"
                echo "结论: $CONCLUSION"
                echo ""
                echo -e "${YELLOW}请查看日志排查问题:${NC}"
                echo "$HTML_URL"
            fi
            ;;
        "in_progress"|"queued")
            echo -e "${YELLOW}⏳ 构建中...${NC}"
            echo "状态: $STATUS"
            echo ""
            echo -e "${BLUE}预计完成时间: 5-15分钟${NC}"
            echo ""
            echo "你可以实时查看构建进度:"
            echo "$HTML_URL"
            ;;
        *)
            echo -e "${YELLOW}⚠️  未知状态: $STATUS${NC}"
            ;;
    esac
    
else
    echo -e "${YELLOW}使用简洁模式（未安装jq）${NC}"
    echo ""
    echo "请访问以下链接查看构建状态："
    echo "https://github.com/$REPO/actions"
fi

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}快速链接:${NC}"
echo -e "${BLUE}========================================${NC}"
echo "📱 GitHub Actions: https://github.com/$REPO/actions"
echo "📦 Releases: https://github.com/$REPO/releases"
echo "📖 README: https://github.com/$REPO#readme"
echo ""

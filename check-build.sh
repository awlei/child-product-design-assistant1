#!/bin/bash

# GitHub Actions构建状态检查脚本

echo "========================================="
echo "  GitHub Actions构建状态检查"
echo "========================================="
echo ""

REPO="awlei/new-child-product-design-assistant"

echo "📋 仓库信息："
echo "   名称：$REPO"
echo "   Actions：https://github.com/$REPO/actions"
echo "   Releases：https://github.com/$REPO/releases"
echo ""

echo "📋 如何查看构建状态："
echo ""
echo "1. 访问GitHub Actions页面"
echo "   https://github.com/$REPO/actions"
echo ""
echo "2. 查看workflow运行列表"
echo "   - ⚪ 蓝色：构建中"
echo "   - ✅ 绿色：构建成功"
echo "   - ❌ 红色：构建失败"
echo ""
echo "3. 点击运行查看详情"
echo "   - 查看每个步骤的日志"
echo "   - 下载APK（如果构建成功）"
echo ""

echo "📋 预计构建时间：10-15分钟"
echo ""

echo "📋 构建产物："
echo "   - Debug APK：app-debug-v{run_number}.apk"
echo "   - Release APK：app-release-v{run_number}.apk"
echo ""

echo "========================================="
echo "  提示："
echo "========================================="
echo ""
echo "1. 如果构建失败，请查看详细日志"
echo "2. 复制错误信息，搜索解决方案"
echo "3. 或者创建GitHub Issue寻求帮助"
echo ""

echo "📋 快速链接："
echo "   - Actions：https://github.com/$REPO/actions"
echo "   - 最新构建：https://github.com/$REPO/actions"
echo "   - Releases：https://github.com/$REPO/releases"
echo ""

echo "========================================="
echo "  监控完成"
echo "========================================="

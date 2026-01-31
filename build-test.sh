#!/bin/bash

# APK本地构建测试脚本
# 用于在推送之前验证构建是否正常

echo "========================================="
echo "  APK本地构建测试脚本"
echo "========================================="
echo ""

# 检查Java版本
echo "📋 检查Java版本..."
java -version
if [ $? -ne 0 ]; then
    echo "❌ Java未安装或版本不正确"
    echo "   请安装Java 17或更高版本"
    exit 1
fi
echo "✅ Java版本检查通过"
echo ""

# 检查Gradle
echo "📋 检查Gradle..."
if [ ! -f "./gradlew" ]; then
    echo "❌ gradlew文件不存在"
    exit 1
fi
echo "✅ Gradle wrapper存在"
echo ""

# 赋予执行权限
echo "📋 赋予gradlew执行权限..."
chmod +x gradlew
echo "✅ 执行权限已授予"
echo ""

# 清理之前的构建
echo "📋 清理之前的构建..."
./gradlew clean
if [ $? -ne 0 ]; then
    echo "❌ 清理失败"
    exit 1
fi
echo "✅ 清理完成"
echo ""

# 编译Debug版本
echo "📋 开始编译Debug版本..."
echo "   这可能需要几分钟，请耐心等待..."
echo ""

./gradlew assembleDebug --stacktrace --no-daemon
BUILD_STATUS=$?

echo ""
if [ $BUILD_STATUS -eq 0 ]; then
    echo "========================================="
    echo "  ✅ Debug版本编译成功！"
    echo "========================================="
    echo ""
    
    # 检查APK文件
    APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
    if [ -f "$APK_PATH" ]; then
        APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
        echo "📦 APK文件位置：$APK_PATH"
        echo "📦 APK文件大小：$APK_SIZE"
        echo ""
        echo "✅ APK文件已生成，可以安装测试"
    else
        echo "❌ APK文件未找到"
        echo "   预期位置：$APK_PATH"
        exit 1
    fi
else
    echo "========================================="
    echo "  ❌ Debug版本编译失败！"
    echo "========================================="
    echo ""
    echo "请查看上方的错误信息，修复问题后重试"
    exit 1
fi

echo ""
echo "========================================="
echo "  构建测试完成"
echo "========================================="

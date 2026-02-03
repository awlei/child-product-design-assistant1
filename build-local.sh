#!/bin/bash

# 本地构建验证脚本
# 用于在推送到GitHub之前验证代码是否可以成功编译

echo "=========================================="
echo "开始本地构建验证"
echo "=========================================="

# 检查Java版本
echo ""
echo "检查Java版本..."
java -version

# 清理构建缓存
echo ""
echo "清理构建缓存..."
./gradlew clean --no-daemon

# 构建Debug版本
echo ""
echo "构建Debug版本..."
./gradlew assembleDebug --no-daemon --stacktrace

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✅ 构建成功！"
    echo "=========================================="
    
    # 输出APK文件位置
    echo ""
    echo "APK文件位置："
    find app/build/outputs/apk/debug -name "*.apk" -type f
    
    exit 0
else
    echo ""
    echo "=========================================="
    echo "❌ 构建失败！"
    echo "=========================================="
    exit 1
fi

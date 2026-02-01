#!/bin/bash
# APK构建脚本

echo "=== 开始构建APK ==="

# 设置Java环境
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "Java版本："
java -version
echo ""

# 检查Android SDK
echo "检查Android SDK环境..."
if [ -z "$ANDROID_HOME" ]; then
    echo "错误：ANDROID_HOME未设置"
    echo "请设置环境变量：export ANDROID_HOME=/path/to/android/sdk"
    exit 1
fi

echo "Android SDK路径：$ANDROID_HOME"
echo ""

# 尝试使用系统的Gradle
if command -v gradle &> /dev/null; then
    echo "使用系统Gradle构建..."
    gradle assembleRelease
else
    echo "使用Gradle Wrapper构建..."
    # 检查是否已经有Gradle
    if [ ! -d "$HOME/.gradle/wrapper/dists/gradle-8.2-bin" ]; then
        echo "警告：Gradle未完全下载，构建可能失败"
        echo "建议：手动下载Gradle或使用代理"
    fi
    
    ./gradlew assembleRelease --no-daemon
fi

echo ""
echo "=== 构建完成 ==="

# 检查APK文件
if [ -f "app/build/outputs/apk/release/app-release-unsigned.apk" ]; then
    echo "✅ APK构建成功！"
    echo "APK路径：app/build/outputs/apk/release/app-release-unsigned.apk"
    ls -lh app/build/outputs/apk/release/*.apk
else
    echo "❌ APK构建失败"
    echo "请检查构建日志"
    exit 1
fi

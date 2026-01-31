#!/bin/bash

# 项目验证脚本

echo "🔍 验证项目结构..."

# 检查必要的目录
echo "检查目录结构..."
required_dirs=(
    "app/src/main/java/com/childproduct/designassistant"
    "app/src/main/res/layout"
    "app/src/main/res/values"
    ".github/workflows"
)

for dir in "${required_dirs[@]}"; do
    if [ -d "$dir" ]; then
        echo "✅ $dir 存在"
    else
        echo "❌ $dir 不存在"
        exit 1
    fi
done

# 检查必要的文件
echo ""
echo "检查必要文件..."
required_files=(
    "build.gradle"
    "settings.gradle"
    "app/build.gradle"
    "app/src/main/AndroidManifest.xml"
    ".github/workflows/build-apk.yml"
    "README.md"
    "DEPLOYMENT_GUIDE.md"
)

for file in "${required_files[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $file 存在"
    else
        echo "❌ $file 不存在"
        exit 1
    fi
done

# 检查 Kotlin 源文件
echo ""
echo "检查 Kotlin 源文件..."
kotlin_files=(
    "app/src/main/java/com/childproduct/designassistant/MainActivity.kt"
    "app/src/main/java/com/childproduct/designassistant/ui/MainViewModel.kt"
    "app/src/main/java/com/childproduct/designassistant/service/CreativeService.kt"
    "app/src/main/java/com/childproduct/designassistant/service/SafetyService.kt"
    "app/src/main/java/com/childproduct/designassistant/service/DocumentService.kt"
    "app/src/main/java/com/childproduct/designassistant/ui/screens/CreativeScreen.kt"
    "app/src/main/java/com/childproduct/designassistant/ui/screens/SafetyScreen.kt"
    "app/src/main/java/com/childproduct/designassistant/ui/screens/DocumentScreen.kt"
)

for file in "${kotlin_files[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $file 存在"
    else
        echo "❌ $file 不存在"
        exit 1
    fi
done

echo ""
echo "🎉 所有文件和目录验证通过！"
echo ""
echo "📋 项目统计:"
echo "Kotlin 文件数: $(find app/src/main/java -name "*.kt" | wc -l)"
echo "配置文件数: $(find . -maxdepth 2 -name "*.gradle" -o -name "*.xml" -o -name "*.yml" | wc -l)"
echo ""
echo "✨ 项目已准备就绪，可以推送到 GitHub 进行构建！"

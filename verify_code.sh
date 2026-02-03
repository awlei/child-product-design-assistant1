#!/bin/bash

# 验证脚本：检查Kotlin代码的语法正确性

echo "======================================"
echo "开始验证代码..."
echo "======================================"

# 1. 检查Kotlin文件是否存在语法错误
echo ""
echo "1. 检查Kotlin文件..."
kotlin_files=$(find app/src/main/java -name "*.kt" | wc -l)
echo "找到 $kotlin_files 个Kotlin文件"

# 2. 检查新增的关键文件
echo ""
echo "2. 检查新增的关键文件..."

if [ -f "app/src/main/java/com/childproduct/designassistant/data/GPS028Database.kt" ]; then
    echo "✓ GPS028Database.kt 存在"
    lines=$(wc -l < app/src/main/java/com/childproduct/designassistant/data/GPS028Database.kt)
    echo "  行数: $lines"
else
    echo "✗ GPS028Database.kt 不存在"
fi

if [ -f "app/src/main/java/com/childproduct/designassistant/data/OtherProductTypesDatabase.kt" ]; then
    echo "✓ OtherProductTypesDatabase.kt 存在"
    lines=$(wc -l < app/src/main/java/com/childproduct/designassistant/data/OtherProductTypesDatabase.kt)
    echo "  行数: $lines"
else
    echo "✗ OtherProductTypesDatabase.kt 不存在"
fi

if [ -f "app/src/main/java/com/childproduct/designassistant/ui/components/StructuredDesignOutput.kt" ]; then
    echo "✓ StructuredDesignOutput.kt 存在"
    lines=$(wc -l < app/src/main/java/com/childproduct/designassistant/ui/components/StructuredDesignOutput.kt)
    echo "  行数: $lines"
else
    echo "✗ StructuredDesignOutput.kt 不存在"
fi

if [ -f "docs/OUTPUT_ACCURACY_VALIDATION.md" ]; then
    echo "✓ OUTPUT_ACCURACY_VALIDATION.md 存在"
    lines=$(wc -l < docs/OUTPUT_ACCURACY_VALIDATION.md)
    echo "  行数: $lines"
else
    echo "✗ OUTPUT_ACCURACY_VALIDATION.md 不存在"
fi

# 3. 检查git状态
echo ""
echo "3. 检查Git状态..."
git_status=$(git status --short)
if [ -z "$git_status" ]; then
    echo "✓ 工作区干净，所有更改已提交"
else
    echo "✗ 工作区有未提交的更改："
    echo "$git_status"
fi

# 4. 检查最近的提交
echo ""
echo "4. 检查最近的提交..."
git log -1 --oneline

# 5. 检查远程仓库状态
echo ""
echo "5. 检查远程仓库..."
if git diff origin/main --quiet; then
    echo "✓ 本地与远程同步"
else
    echo "✗ 本地与远程不同步"
    echo "  请运行: git push origin main"
fi

echo ""
echo "======================================"
echo "验证完成"
echo "======================================"

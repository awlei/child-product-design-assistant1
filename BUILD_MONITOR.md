# APK构建监控和故障排查指南

## 📊 构建监控

### 实时查看构建状态

由于我无法直接访问GitHub Actions，请使用以下方法查看构建状态：

#### 方法1：GitHub网站（推荐）

1. 访问：https://github.com/awlei/new-child-product-design-assistant/actions
2. 查看最近的workflow运行
3. 点击运行查看详细状态

**状态标识**：
- ⚪ 蓝色图标 - 构建中
- ✅ 绿色勾 - 构建成功
- ❌ 红色叉 - 构建失败
- 🟡 黄色图标 - 已取消

#### 方法2：GitHub CLI

```bash
# 安装GitHub CLI（如果尚未安装）
# macOS: brew install gh
# Linux: https://cli.github.com/

# 登录GitHub
gh auth login

# 查看最近的运行
gh run list --repo awlei/new-child-product-design-assistant

# 查看特定运行的详情
gh run view <run-id> --repo awlei/new-child-product-design-assistant

# 实时查看日志
gh run view <run-id> --log --repo awlei/new-child-product-design-assistant
```

#### 方法3：GitHub API

```bash
# 获取最近的运行
curl -s \
  -H "Authorization: token YOUR_GITHUB_TOKEN" \
  https://api.github.com/repos/awlei/new-child-product-design-assistant/actions/runs \
  | jq '.workflow_runs[0]'
```

---

## 🔍 常见构建失败原因及解决方案

### 1. 依赖下载失败

**症状**：
```
Could not resolve com.android.tools.build:gradle:8.2.0
```

**解决方案**：
- ✅ 已配置阿里云镜像源（在workflow中）
- 如果仍然失败，增加超时时间

**检查点**：
```yaml
# 检查 .github/workflows/ai-auto-build-apk.yml
- name: 🇨🇳 Configure Maven mirror (China)
  run: |
    cat > ~/.gradle/init.gradle <<EOF
    allprojects {
      repositories {
        maven { url 'https://maven.aliyun.com/repository/google' }
        maven { url 'https://maven.aliyun.com/repository/jcenter' }
        maven { url 'https://maven.aliyun.com/repository/central' }
      }
    }
    EOF
```

### 2. 编译错误

**症状**：
```
Task :app:compileDebugKotlin FAILED
e: file:///app/src/main/java/xxx.kt:10:5: Unresolved reference
```

**解决方案**：
1. 查看构建日志，找到具体的错误行
2. 修复代码错误
3. 提交修复，重新构建

**常见编译错误**：
- 未解析的引用
- 类型不匹配
- 缺少导入
- 语法错误

### 3. Android SDK配置问题

**症状**：
```
SDK location not found. Define location with sdk.dir in the local.properties file
```

**解决方案**：
- ✅ 使用`android-actions/setup-android@v3`自动配置
- 如果失败，尝试更新到最新版本

### 4. 内存不足

**症状**：
```
Execution failed for task ':app:compileDebugKotlin'.
> Process 'Gradle worker daemon' finished with non-zero exit value 137
```

**解决方案**：
```yaml
# 增加 Gradle 内存限制
env:
  GRADLE_OPTS: "-Dorg.gradle.daemon=false -Xmx6g -XX:MaxMetaspaceSize=512m"
```

### 5. 签名配置问题

**症状**：
```
Execution failed for task ':app:packageRelease'.
> Keystore file '/tmp/keystore.jks' not found
```

**解决方案**：
- Debug构建不需要签名
- Release构建需要配置GitHub Secrets（可选）

### 6. Kotlin编译器版本不匹配

**症状**：
```
e: error: incompatible classes were found in depends
```

**解决方案**：
```gradle
// 检查 app/build.gradle
composeOptions {
    kotlinCompilerExtensionVersion '1.5.4'  // 确保与Compose BOM版本匹配
}
```

---

## 🛠️ 构建优化建议

### 1. 缩短构建时间

```yaml
# 使用Gradle缓存
- name: 🚀 Build Debug APK
  uses: gradle/gradle-build-action@v2
  with:
    gradle-version: 8.2
    cache-read-only: false
```

### 2. 并行构建

```gradle
// 在 app/build.gradle 中添加
android {
    ...
    kotlinOptions {
        freeCompilerArgs += ['-Xallow-result-return-type']
    }
}
```

### 3. 禁用不必要的任务

```bash
./gradlew assembleDebug \
  --exclude-task lint \
  --exclude-task test \
  --no-daemon
```

---

## 📝 构建日志分析

### 关键日志位置

1. **Checkout步骤**
   ```
   📥 Checkout code
   ```

2. **JDK安装**
   ```
   ☕ Set up JDK 17
   ```

3. **Android SDK安装**
   ```
   🤖 Set up Android SDK
   ```

4. **Gradle构建**
   ```
   🚀 Build Debug APK
   ```

5. **APK上传**
   ```
   📤 Upload Debug APK
   ```

### 日志分析要点

1. **查找ERROR关键词**
   ```bash
   grep -i "error\|failed" build.log
   ```

2. **查找警告信息**
   ```bash
   grep -i "warning" build.log
   ```

3. **查找编译错误**
   ```bash
   grep -A 5 "compileDebugKotlin FAILED" build.log
   ```

---

## 🔄 构建失败处理流程

### 步骤1：查看构建日志

1. 进入失败的构建页面
2. 点击失败的步骤
3. 展开日志查看详细错误信息

### 步骤2：定位错误原因

根据日志信息，判断错误类型：
- 依赖问题
- 代码错误
- 配置问题
- 环境问题

### 步骤3：修复错误

**依赖问题**：
- 更新依赖版本
- 清理本地缓存
- 重新拉取依赖

**代码错误**：
- 修复语法错误
- 添加缺失的导入
- 更新API调用

**配置问题**：
- 检查build.gradle配置
- 检查AndroidManifest.xml
- 检查ProGuard规则

### 步骤4：验证修复

1. 在本地运行构建测试
2. 确保本地构建成功
3. 提交修复到GitHub
4. 触发新的构建

### 步骤5：监控新构建

1. 等待新构建完成
2. 验证构建产物
3. 测试APK安装和运行

---

## 📋 构建检查清单

### 构建前检查

- [ ] 代码已提交到GitHub
- [ ] 分支配置正确（main/develop）
- [ ] GitHub Actions已启用
- [ ] 工作流文件存在且正确
- [ ] 仓库设置允许Actions

### 构建中监控

- [ ] 查看Actions页面
- [ ] 监控构建进度
- [ ] 检查是否有错误日志
- [ ] 确认网络连接正常

### 构建后验证

- [ ] 构建状态为成功
- [ ] APK已上传到Artifacts
- [ ] Release已创建
- [ ] 可以下载APK
- [ ] APK可以正常安装
- [ ] 应用可以正常启动

---

## 🆘 获取帮助

### 如果构建失败

1. **查看文档**
   - [GitHub Actions文档](https://docs.github.com/en/actions)
   - [Android Gradle插件文档](https://developer.android.com/studio/build)

2. **查看错误日志**
   - 在GitHub Actions页面查看详细日志
   - 复制错误信息搜索解决方案

3. **寻求帮助**
   - GitHub Issues：https://github.com/awlei/new-child-product-design-assistant/issues
   - 官方论坛：https://stackoverflow.com

### 联系我

如果遇到构建问题，请提供以下信息：
1. 构建失败的具体步骤
2. 错误日志的完整内容
3. GitHub Actions运行链接

---

## 📊 当前构建信息

**仓库**：https://github.com/awlei/new-child-product-design-assistant
**分支**：main
**Workflow**：AI Auto Build APK
**配置文件**：`.github/workflows/ai-auto-build-apk.yml`

**最近推送**：
- 提交1：29318d6 (APK说明书编写与代码优化)
- 提交2：4621581 (更新README和添加构建状态文档)
- 提交3：a143f39 (添加GitHub推送和构建完成总结)

**预计构建时间**：10-15分钟/次

---

**最后更新**：2026-01-29 16:35
**状态**：等待构建结果

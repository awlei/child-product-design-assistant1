# APK构建指南

> 更新时间：2025-01-08
> 项目版本：v1.0.0
> 构建环境要求：Android Studio Arctic Fox+ / Android SDK 34+

---

## 📋 前置要求

### 1. 安装Android SDK

**方式一：使用Android Studio（推荐）**
1. 下载并安装Android Studio：https://developer.android.com/studio
2. 打开Android Studio，安装Android SDK 34
3. 安装Build Tools 34.0.0
4. 安装Platform Tools

**方式二：使用命令行工具**
```bash
# 下载Android Command Line Tools
wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
unzip commandlinetools-linux-9477386_latest.zip
mkdir -p ~/Android/sdk/cmdline-tools
mv cmdline-tools ~/Android/sdk/cmdline-tools/latest

# 设置环境变量
export ANDROID_HOME=~/Android/sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

# 安装必需的包
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
```

### 2. 配置local.properties

在项目根目录创建`local.properties`文件：

```properties
sdk.dir=/path/to/your/Android/sdk
```

例如：
```properties
sdk.dir=/Users/yourname/Library/Android/sdk
```

### 3. 安装JDK 17

确保系统已安装JDK 17：

```bash
java -version
# 输出应该包含：openjdk version "17.x.x"
```

---

## 🚀 构建步骤

### 1. 克隆项目

```bash
git clone https://github.com/awlei/new-child-product-design-assistant.git
cd new-child-product-design-assistant
```

### 2. 配置local.properties

创建`local.properties`文件，配置Android SDK路径：

```bash
echo "sdk.dir=$ANDROID_HOME" > local.properties
```

### 3. 清理之前的构建

```bash
./gradlew clean
```

### 4. 构建Debug APK

```bash
./gradlew assembleDebug
```

构建成功后，APK文件位于：
```
app/build/outputs/apk/debug/app-debug.apk
```

### 5. 构建Release APK

```bash
./gradlew assembleRelease
```

构建成功后，APK文件位于：
```
app/build/outputs/apk/release/app-release.apk
```

---

## 📦 构建输出

### Debug APK
- **文件路径**：`app/build/outputs/apk/debug/app-debug.apk`
- **签名**：使用默认debug签名
- **应用ID**：`com.childproduct.designassistant.debug`
- **版本**：`1.0.0-debug`

### Release APK
- **文件路径**：`app/build/outputs/apk/release/app-release.apk`
- **签名**：需要配置release签名
- **应用ID**：`com.childproduct.designassistant`
- **版本**：`1.0.0`

---

## 🔐 配置Release签名（可选）

如果需要构建带签名的Release APK，需要在`app/build.gradle`中配置签名：

```gradle
android {
    signingConfigs {
        release {
            storeFile file("path/to/your/keystore.jks")
            storePassword "your_store_password"
            keyAlias "your_key_alias"
            keyPassword "your_key_password"
        }
    }

    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

---

## 🧪 运行单元测试

```bash
# 运行所有单元测试
./gradlew test

# 运行特定测试类
./gradlew test --tests "com.childproduct.designassistant.helper.SchemeOptimizerTest"

# 查看测试报告
open app/build/reports/tests/testDebugUnitTest/index.html
```

---

## 📊 常见构建错误

### 错误1：SDK location not found

**错误信息**：
```
SDK location not found. Define a valid SDK location with an ANDROID_HOME environment variable or by setting the sdk.dir path in your project's local properties file.
```

**解决方案**：
1. 确保已安装Android SDK
2. 创建`local.properties`文件，配置`sdk.dir`路径
3. 或设置环境变量：`export ANDROID_HOME=/path/to/your/sdk`

### 错误2：Java版本不兼容

**错误信息**：
```
Unsupported class file major version 61
```

**解决方案**：
确保使用JDK 17：
```bash
java -version
export JAVA_HOME=/path/to/jdk-17
```

### 错误3：依赖下载失败

**错误信息**：
```
Could not resolve com.android.tools.build:gradle:x.x.x
```

**解决方案**：
```bash
# 清理Gradle缓存
./gradlew clean --refresh-dependencies

# 或使用国内镜像
# 在 gradle.properties 中添加：
# maven { url 'https://maven.aliyun.com/repository/google' }
# maven { url 'https://maven.aliyun.com/repository/jcenter' }
```

---

## 📱 安装APK到设备

### 通过ADB安装

```bash
# 连接设备
adb devices

# 安装Debug APK
adb install app/build/outputs/apk/debug/app-debug.apk

# 安装Release APK
adb install -r app/build/outputs/apk/release/app-release.apk
```

### 直接安装

1. 将APK文件复制到Android设备
2. 在设备上点击APK文件
3. 允许"未知来源"安装
4. 完成安装

---

## 🔍 验证APK功能

### 测试清单

- [ ] 应用正常启动
- [ ] 输入产品类型"儿童安全座椅"
- [ ] 输入身高范围"40-150cm"
- [ ] 选择安装方式（ISOFIX等）
- [ ] 输入设计主题（如：拼图游戏）
- [ ] 点击"生成设计方案"
- [ ] 验证生成的方案包含所有必要信息
- [ ] 验证验证器正常工作
- [ ] 测试无效输入的错误提示
- [ ] 测试其他产品类型（婴儿推车、儿童餐椅）

---

## 📝 构建日志

构建过程中的详细日志位于：
```
app/build/outputs/logs/
```

---

## 🚀 CI/CD自动化构建

如果需要在GitHub Actions中自动构建APK，可以创建`.github/workflows/build.yml`：

```yaml
name: Build APK

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Build Debug APK
      run: ./gradlew assembleDebug

    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: app/build/outputs/apk/debug/app-debug.apk
```

---

## 📞 支持

如果遇到构建问题，请：
1. 检查上述常见错误及解决方案
2. 查看构建日志：`app/build/outputs/logs/`
3. 提交Issue到GitHub仓库

---

**文档结束**

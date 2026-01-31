# 🎨 儿童产品设计助手 APK

一个专为儿童产品设计师打造的 Android 应用，提供创意生成、安全检查、技术建议和设计文档生成功能。

## ✨ 功能特性

### 1. 🎨 创意生成
- 根据年龄段（0-3岁至12岁以上）生成设计创意
- 支持多种产品类型：玩具、文具、服装、家具、教育用品、创意手工
- 自动推荐功能特性、色彩方案和安全提示
- 可自定义设计主题

### 2. 🛡️ 安全检查
- 基于 GB 6675 和 EN71 等国际安全标准
- 涵盖 8 大安全检查类别：
  - 小零件安全
  - 尖锐边缘检查
  - 材料安全性
  - 尺寸规范
  - 电气安全
  - 化学安全
  - 结构稳定性
  - 标签要求
- 自动评分和风险评估
- 生成改进建议

### 3. 🔬 技术建议（全新功能）
- 基于国际标准的专业技术建议
- 支持标准匹配：ECE R129, FMVSS213, GB 27887, EN 1888, ASTM F2050
- 品牌参数对比：Britax, Maxi-Cosi, Cybex 等主流品牌
- 智能规格推荐：基于品牌平均值 + 10% 安全余量
- DVP 测试矩阵生成：Design Validation Plan
- 技术问题解答：头托调节、碰撞测试、安装要求等

### 4. 📝 设计文档生成
- 一键生成完整的设计文档
- 包含产品概述、设计理念、功能特性等 8 个章节
- 整合创意生成和安全检查结果
- 导出格式化的专业文档

### 5. 📚 文档学习（全新功能）
- 上传专业文档到知识库（PDF、Word、Excel、文本）
- 支持 ECE R129、GB 27887、ISO 8124 等标准文档学习
- 实时学习进度跟踪
- 文档分类和标签管理
- 批量学习功能

### 6. 💬 智能问答（全新功能）
- 基于已学习文档的智能问答
- 支持自然语言对话交互
- 自动引用相关标准条款
- 实时搜索和匹配知识库
- 聊天历史记录

## 📋 系统要求

- **最低 Android 版本**: Android 7.0 (API 24)
- **目标 Android 版本**: Android 14 (API 34)
- **存储空间**: 约 50MB

## 🚀 快速开始

### 方式一：从 GitHub 构建（推荐）

#### 1. 克隆仓库到你的 GitHub
```bash
# 在你的 GitHub 账号上创建新仓库
# 然后克隆项目文件上传到你的仓库
```

#### 2. 推送代码到 GitHub
```bash
# 初始化 git 仓库（如果还没有）
git init

# 添加远程仓库（替换为你的仓库地址）
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git

# 添加所有文件
git add .

# 提交更改
git commit -m "Initial commit: Child Product Design Assistant"

# 推送到 GitHub
git push -u origin main
```

或者使用提供的脚本：
```bash
# 1. 添加远程仓库
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git

# 2. 运行推送脚本
chmod +x push-to-github.sh
./push-to-github.sh
```

#### 3. 触发自动构建
推送代码后，GitHub Actions 会自动开始构建 APK：
- 进入仓库的 **Actions** 标签
- 等待 "Build APK" workflow 完成
- 在 workflow 运行结果中下载 APK 文件

#### 4. 下载并安装 APK
- 在 Actions 页面找到构建完成的任务
- 在 **Artifacts** 区域下载 `app-debug.apk`
- 将 APK 传输到 Android 设备并安装

### 方式二：本地构建

#### 前置要求
- JDK 17 或更高版本
- Android SDK
- Gradle 8.2

#### 构建步骤
```bash
# 1. 克隆仓库
git clone https://github.com/YOUR_USERNAME/YOUR_REPO.git
cd YOUR_REPO

# 2. 赋予执行权限
chmod +x gradlew

# 3. 构建 Debug APK
./gradlew assembleDebug

# 4. 构建 Release APK
./gradlew assembleRelease

# 5. APK 文件位置
# Debug: app/build/outputs/apk/debug/app-debug.apk
# Release: app/build/outputs/apk/release/app-release-unsigned.apk
```

## 📱 使用指南

### 创意生成流程
1. 打开应用，选择「创意生成」标签
2. 选择目标年龄段（如 3-6 岁幼儿）
3. 选择产品类型（如玩具）
4. （可选）输入自定义设计主题
5. 点击「生成创意」按钮
6. 查看生成的创意详情，包括功能特性和色彩方案

### 安全检查流程
1. 切换到「安全检查」标签
2. 输入产品名称
3. 选择目标年龄段
4. 点击「开始安全检查」
5. 查看检查结果和改进建议

### 设计文档生成流程
1. 切换到「设计文档」标签
2. 输入产品名称
3. 点击「生成设计文档」
4. 查看生成的完整设计文档（包含之前的创意和安全检查结果）

### 文档学习流程
1. 切换到「文档学习」标签
2. 查看已学习的文档列表和统计信息
3. 点击「上传文档」按钮
4. 选择文档类型（PDF、Word、Excel、文本）
5. 输入文档名称并选择文件
6. 点击「开始上传」开始学习
7. 查看学习进度和状态

### 智能问答流程
1. 切换到「智能问答」标签
2. 在输入框中输入你的专业问题
3. 点击发送按钮或按回车键
4. AI 会基于已学习的文档回答问题
5. 查看回答内容和相关标准引用

## 🔧 配置正式签名（可选）

如需构建签名的 Release APK，需要在 GitHub 仓库中配置 Secrets：

1. 生成签名密钥：
```bash
keytool -genkey -v -keystore release-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias your-alias
```

2. 将 keystore 文件转换为 base64：
```bash
base64 -i release-keystore.jks | pbcopy  # macOS
base64 -w 0 release-keystore.jks        # Linux
```

3. 在 GitHub 仓库设置中添加以下 Secrets：
   - `SIGNING_KEYSTORE_BASE64`: 上一步的 base64 字符串
   - `SIGNING_KEYSTORE_PASSWORD`: keystore 密码
   - `SIGNING_KEY_ALIAS`: key 别名
   - `SIGNING_KEY_PASSWORD`: key 密码

## 🛠️ 技术栈

- **语言**: Kotlin
- **UI 框架**: Jetpack Compose
- **架构**: MVVM
- **最低 SDK**: API 24 (Android 7.0)
- **目标 SDK**: API 34 (Android 14)
- **构建工具**: Gradle 8.2

## 📂 项目结构

```
app/
├── src/main/java/com/childproduct/designassistant/
│   ├── MainActivity.kt                 # 应用入口
│   ├── model/                          # 数据模型
│   │   ├── CreativeIdea.kt
│   │   ├── SafetyCheck.kt
│   │   ├── DesignDocument.kt
│   │   └── LearnedDocument.kt          # 文档学习模型
│   ├── service/                        # 业务逻辑
│   │   ├── CreativeService.kt
│   │   ├── SafetyService.kt
│   │   ├── DocumentService.kt
│   │   ├── KnowledgeBaseService.kt     # 知识库服务
│   │   └── TechnicalAnalysisEngine.kt
│   └── ui/                             # UI 层
│       ├── MainViewModel.kt
│       ├── screens/                    # 页面组件
│       │   ├── CreativeScreen.kt
│       │   ├── SafetyScreen.kt
│       │   ├── DocumentScreen.kt
│       │   ├── TechnicalRecommendationScreen.kt
│       │   ├── DocumentLearningScreen.kt    # 文档学习界面
│       │   └── ChatQAScreen.kt             # 智能问答界面
│       └── theme/                      # 主题配置
├── src/test/java/com/childproduct/designassistant/
│   └── service/
│       └── KnowledgeBaseServiceTest.kt     # 知识库服务测试
└── .github/workflows/
    └── build-apk.yml                   # GitHub Actions 配置
```

## 🔐 安全标准

本应用遵循以下安全标准：
- GB 6675-2014 国家玩具安全标准
- EN71 欧洲玩具安全标准
- ASTM F963 美国玩具安全标准

## 📝 版本信息

- **当前版本**: v1.2.0 (2026-01-30)
- **发布日期**: 2026-01-30
- **许可证**: MIT

### 🎉 最新更新（v1.2.0）

#### ✨ 新增功能

**1. 文档学习模块**
- ✅ 支持上传专业文档（PDF、Word、Excel、文本）
- ✅ 文档学习进度跟踪
- ✅ 学习状态管理（待学习、学习中、已完成）
- ✅ 文档分类和标签系统
- ✅ 批量学习功能
- ✅ 文档统计信息展示

**2. 智能问答模块**
- ✅ 基于已学习文档的智能问答
- ✅ 自然语言对话交互
- ✅ 自动引用相关标准条款
- ✅ 实时搜索和匹配知识库
- ✅ 聊天历史记录
- ✅ 支持标准参数查询（头托调节、侧撞防护、ISOFIX等）

**3. 知识库服务**
- ✅ 本地模拟知识库功能
- ✅ 文档分块和向量化（模拟）
- ✅ 语义搜索能力
- ✅ 相关性评分
- ✅ 高亮显示搜索结果

#### 🔧 改进

- 新增底部导航栏入口（文档学习、智能问答）
- 优化界面交互体验
- 完善数据模型（LearnedDocument、ChatMessage等）

#### 🧪 测试

- 添加 KnowledgeBaseService 单元测试
- 测试覆盖文档添加、搜索、删除等功能
- 所有测试用例通过

### 🎉 历史更新（v1.1.0）

#### ✨ 新增功能

**1. 极简输入模块**
- ✅ 仅需输入身高或重量范围，系统自动匹配法规
- ✅ 智能联想功能，提示可能的参数
- ✅ 实时校验并提示错误

**2. 增强输出模块**
- ✅ 每个尺寸参数标注法规引用（如ECE R129 Annex 7）
- ✅ 每个功能参数标注法规依据和品牌参考
- ✅ 集成头部品牌参数（Britax、Maxi-Cosi、UPPAbaby等）
- ✅ 包络尺寸合规性详情

**3. 全球法规展示功能**
- ✅ 按地区+品类分类展示（欧盟、美国、中国、日本、澳大利亚）
- ✅ 10个核心法规，含详细章节
- ✅ 法规更新监测
- ✅ 支持点击展开查看标准文本

**4. GitHub自动化集成**
- ✅ OAuth2授权管理
- ✅ 代码增量提交
- ✅ 自动构建（GitHub Actions）
- ✅ 版本管理
- ✅ Releases发布

**5. APK说明书**
- ✅ 完整的使用指南
- ✅ 功能模块详解
- ✅ 技术适配说明
- ✅ 常见问题解答

#### 🔧 改进

- 优化输入模块，支持极简设计
- 增强输出模块，添加法规引用和品牌对比
- 完善全球法规库，覆盖5个地区
- 新增GitHub自动化功能

#### 🧪 测试

- 20个测试用例全部通过
- 测试通过率100%

详细更新日志请查看：[CHANGELOG.md](CHANGELOG.md)

---

## 🚀 当前构建状态

### 最新推送
- **提交**：9ee0a63 (docs: 添加构建监控和故障排查文档)
- **时间**：2026-01-29 16:50
- **状态**：✅ 已推送到GitHub

### GitHub Actions构建
- **Workflow**：AI Auto Build APK
- **状态**：⏳ 自动触发中
- **预计时间**：10-15分钟
- **查看链接**：https://github.com/awlei/new-child-product-design-assistant/actions

### 构建监控文档

如果需要监控构建状态或解决构建问题，请查看：
- [BUILD_MONITOR.md](BUILD_MONITOR.md) - 构建监控和故障排查指南
- [BUILD_ADJUSTMENT_GUIDE.md](BUILD_ADJUSTMENT_GUIDE.md) - 构建调整流程
- [BUILD_MONITOR_SUMMARY.md](BUILD_MONITOR_SUMMARY.md) - 构建监控总结

---

## 📚 文档

- [USER_MANUAL.md](USER_MANUAL.md) - APK说明书（完整版）
- [TEST_REPORT.md](TEST_REPORT.md) - 功能测试报告
- [CHANGELOG.md](CHANGELOG.md) - 更新日志
- [BUILD_STATUS.md](BUILD_STATUS.md) - 构建状态报告
- [SUMMARY.md](SUMMARY.md) - 工作总结

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📞 支持

如有问题或建议，请：
- 提交 GitHub Issue
- 或通过邮箱联系我们

## 📄 许可证

本项目采用 MIT 许可证 - 详见 LICENSE 文件

---

**⚠️ 免责声明**: 本应用提供的设计建议仅供参考，实际产品开发请遵循相关法律法规和行业标准，并进行专业安全检测。

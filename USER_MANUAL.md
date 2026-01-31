# 儿童产品设计APK说明书

## 儿童产品设计辅助应用专业操作指南

---

## 文档信息

| 项目 | 内容 |
|------|------|
| 文档版本 | v1.0.0 |
| 应用版本 | v1.0.0 |
| 编写日期 | 2026-01-29 |
| 适用人群 | 儿童产品设计者、研发人员、合规专员 |
| 支持品类 | 儿童安全座椅、婴儿推车 |

---

## 目录

- [一、前言](#一前言)
- [二、安装与初始化](#二安装与初始化)
- [三、核心操作流程](#三核心操作流程)
- [四、功能模块详解](#四功能模块详解)
- [五、设计建议解读](#五设计建议解读)
- [六、技术适配说明](#六技术适配说明)
- [七、常见问题](#七常见问题)
- [八、附录](#八附录)

---

## 一、前言

### 1.1 APK核心定位

**儿童产品设计APK**是一款专为儿童安全座椅、婴儿推车设计者打造的智能化设计辅助工具。

#### 核心价值

您仅需输入儿童身高范围或重量范围，APK即可：

- ✅ **自动匹配全球法规**：智能识别适用标准（ECE R129、FMVSS 213、EN 1888等）
- ✅ **生成专业设计建议**：包含尺寸参数、功能参数、合规测试矩阵
- ✅ **提供品牌参数参考**：对比Britax、Maxi-Cosi、UPPAbaby等头部品牌
- ✅ **支持GitHub自动化**：代码增量提交、自动构建、版本管理
- ✅ **适配手机端操作**：单手操作、语音输入、离线缓存

#### 适用人群

- 🎯 **儿童安全座椅设计工程师**：快速生成符合i-Size标准的设计方案
- 🎯 **婴儿推车产品经理**：参考头部品牌参数，优化产品竞争力
- 🎯 **合规专员**：一键获取全球法规要求，确保产品合规
- 🎯 **研发人员**：GitHub自动化构建，提升开发效率

---

### 1.2 核心功能概览

#### 📱 输入模块（极简设计）

```
用户输入：身高范围 60-105cm
          ↓
系统自动：
  • 识别品类：儿童安全座椅
  • 匹配标准：ECE R129/i-Size
  • 推荐分组：Group 0/1
          ↓
生成建议：专业设计方案
```

#### 📄 输出模块（专业建议）

生成内容包含：
1. **尺寸参数**（外部尺寸+内部尺寸，标注公差范围）
2. **功能参数**（头托调节范围、靠背角度、避震结构等）
3. **合规测试矩阵**（测试项、法规引用、测试方法、合格标准）
4. **品牌参数对比**（Britax、Maxi-Cosi、UPPAbaby等）
5. **法规依据标注**（ECE R129 §5.3.2等）

#### 🌐 全球法规展示

- 🇪🇺 欧盟：ECE R129、EN 1888
- 🇺🇸 美国：FMVSS 213、ASTM F833
- 🇨🇳 中国：GB 27887、GB 14748
- 🇯🇵 日本：JIS D 0161、JIS D9302
- 🇦🇺 澳大利亚：AS/NZS 1754、AS/NZS 2088

#### ⚙️ 技术集成功能

- 💾 **本地数据库调用**：复用用户提供的成熟技术文档
- 🔧 **GitHub自动化**：OAuth2授权、代码提交、自动构建
- 📦 **性能优化**：APK≤50MB，支持Android 10+，弱网/离线模式

---

## 二、安装与初始化

### 2.1 手机端安装要求

#### 系统要求

| 项目 | 要求 |
|------|------|
| Android版本 | Android 10.0 (API 29) 及以上 |
| 存储空间 | ≥ 100MB（含数据库） |
| 内存 | ≥ 3GB RAM |
| 网络 | Wi-Fi或4G/5G（支持离线模式） |

#### 支持机型

- ✅ 华为：Mate系列、P系列（EMUI 10+）
- ✅ 小米：数字系列、Redmi K系列（MIUI 12+）
- ✅ OPPO：Find系列、Reno系列（ColorOS 11+）
- ✅ vivo：X系列、iQOO系列（OriginOS 1.0+）
- ✅ 三星：Galaxy S系列、Note系列（One UI 2.0+）
- ✅ 其他：Android 10+主流机型

#### 安装步骤

1. **下载APK**
   ```
   方式1：GitHub Releases下载
   方式2：企业内部分发
   方式3：应用商店（计划中）
   ```

2. **安装应用**
   - 允许"未知来源"安装（Android 10以下）
   - Android 10+自动提示安装

3. **授予权限**
   - 📱 存储权限：保存设计建议、导出PDF
   - 🌐 网络权限：访问法规数据库、GitHub API
   - 🎤 麦克风权限：语音输入（可选）
   - 📂 相机权限：扫描二维码（可选）

---

### 2.2 首次使用引导

#### 步骤1：启动应用

首次启动会显示欢迎界面：

```
┌─────────────────────────┐
│                         │
│   🎨 儿童产品设计辅助     │
│                         │
│  专为儿童安全座椅、      │
│  婴儿推车设计者打造      │
│                         │
│  [开始使用]              │
│  [查看教程]              │
│                         │
└─────────────────────────┘
```

#### 步骤2：GitHub授权（可选）

若需使用GitHub自动化功能，需完成授权：

```
GitHub授权流程：
  1. 点击"连接GitHub"
  2. 扫描二维码或输入Token
  3. 授权以下权限：
     • repo（代码仓库访问）
     • workflow（工作流操作）
     • packages（包管理）
  4. 加密存储Token（Android Keystore）
```

**注意事项**：
- ⚠️ Token采用AES-256加密存储
- ⚠️ 支持AI操作白名单（仅允许操作指定分支）
- ⚠️ 异常操作会立即推送通知提醒

#### 步骤3：本地数据库连接（可选）

若需使用本地技术文档数据库：

```
连接步骤：
  1. 点击"设置" → "数据库管理"
  2. 选择"连接本地数据库"
  3. 配置连接参数：
     • 数据库类型：SQLite
     • 数据库路径：/sdcard/child_product_db/
     • 数据库文件：standards.db, brands.db
  4. 测试连接并保存
```

**连接参数示例**：
```
数据库类型：SQLite
数据库路径：/sdcard/child_product_db/
用户名：（无需）
密码：（无需）
```

#### 步骤4：完成初始化

初始化完成后，进入主界面：

```
┌─────────────────────────┐
│  🎨 儿童产品设计         │
│  ┌─────────────────┐   │
│  │ 📱 新建设计       │   │
│  └─────────────────┘   │
│  ┌─────────────────┐   │
│  │ 📜 历史记录       │   │
│  └─────────────────┘   │
│  ┌─────────────────┐   │
│  │ 🌐 法规库         │   │
│  └─────────────────┘   │
│  ┌─────────────────┐   │
│  │ ⚙️  设置         │   │
│  └─────────────────┘   │
└─────────────────────────┘
```

---

### 2.3 权限申请说明

| 权限 | 用途 | 是否必须 | 备注 |
|------|------|----------|------|
| 存储权限 | 保存设计建议、导出PDF、本地数据库 | ✅ 必须 | Android 10+自动授予 |
| 网络权限 | 访问法规数据库、GitHub API | ✅ 必须 | 支持离线模式 |
| 麦克风权限 | 语音输入核心参数 | ❌ 可选 | 单手操作时使用 |
| 相机权限 | 扫描二维码（GitHub授权） | ❌ 可选 | 可手动输入Token |

---

## 三、核心操作流程

### 3.1 快速开始流程

```
输入身高/重量 → 自动匹配标准 → 生成设计建议 → 查看法规依据 → 导出/分享 → 触发GitHub构建
```

#### 🎯 场景1：儿童安全座椅设计（身高输入）

**步骤1：输入参数**

```
1. 打开应用，点击"新建设计"
2. 选择品类：儿童安全座椅
3. 输入身高范围：60-105cm
   （系统自动识别：
    • 适用标准：ECE R129/i-Size
    • 推荐分组：Group 0/1
    • 安装方向：后向至15个月）
4. （可选）选择产品细分：Group 0/1
5. （可选）输入专项需求：优化头托侧撞防护
6. 点击"生成设计建议"
```

**输入界面示例**：
```
┌─────────────────────────┐
│  新建设计 - 安全座椅     │
│                         │
│  品类：儿童安全座椅      │
│  [切换]                 │
│                         │
│  输入身高范围：          │
│  [60] - [105] cm  [🎤] │
│                         │
│  系统自动识别：          │
│  • 标准：ECE R129       │
│  • 分组：Group 0/1      │
│                         │
│  产品细分（可选）：       │
│  [Group 0/1 ▼]          │
│                         │
│  专项需求（可选）：       │
│  [优化头托侧撞防护]      │
│  [+ 添加更多]            │
│                         │
│  [生成设计建议]          │
│                         │
└─────────────────────────┘
```

**步骤2：查看设计建议**

```
生成完成后，自动跳转至建议页面：
  • 尺寸参数（外部/内部）
  • 功能参数（头托调节、靠背角度）
  • 合规测试矩阵
  • 品牌参数对比
```

**步骤3：查看法规依据**

```
点击法规标识（如"ECE R129 §5.3.2"）：
  • 跳转至法规详情页
  • 显示标准原文片段
  • 高亮相关条款
```

**步骤4：导出/分享**

```
1. 点击右上角"分享"按钮
2. 选择导出格式：
   • PDF（精简版/完整版）
   • 图片（长截图）
3. 选择分享方式：
   • 微信
   • 邮箱
   • 生成分享链接（有效期7天）
```

**步骤5：触发GitHub构建（可选）**

```
1. 点击"更多操作"
2. 选择"生成代码"
3. 确认GitHub分支：
   • design-suggestion/child-seat-20260129
4. 系统自动：
   • 提交代码到GitHub
   • 触发GitHub Actions构建
   • 推送构建完成通知
```

---

#### 🎯 场景2：婴儿推车设计（重量输入）

**步骤1：输入参数**

```
1. 打开应用，点击"新建设计"
2. 选择品类：婴儿推车
3. 输入重量范围：0-15kg
   （系统自动识别：
    • 适用标准：EN 1888
    • 产品类型：通用型）
4. （可选）选择使用场景：城市通勤
5. （可选）选择产品细分：高景观型
6. （可选）输入专项需求：提升避震性能
7. 点击"生成设计建议"
```

**输入界面示例**：
```
┌─────────────────────────┐
│  新建设计 - 婴儿推车     │
│                         │
│  品类：婴儿推车          │
│  [切换]                 │
│                         │
│  输入重量范围：          │
│  [0] - [15] kg   [🎤]  │
│  或：[0] - [33] lb      │
│                         │
│  系统自动识别：          │
│  • 标准：EN 1888        │
│  • 类型：通用型          │
│                         │
│  使用场景（可选）：       │
│  [城市通勤 ▼]            │
│                         │
│  产品细分（可选）：       │
│  [高景观型 ▼]            │
│                         │
│  专项需求（可选）：       │
│  [提升避震性能]          │
│  [+ 添加更多]            │
│                         │
│  [生成设计建议]          │
│                         │
└─────────────────────────┘
```

**步骤2-5**：与安全座椅流程相同

---

### 3.2 核心操作流程图

```
开始
  ↓
[打开应用]
  ↓
[选择品类]
  ↓
[输入身高/重量范围]
  ↓
[系统自动匹配标准]
  ├─→ 身高范围 → ECE R129/i-Size
  ├─→ 重量范围 → FMVSS 213 / EN 1888
  └─→ 推荐分组/类型
  ↓
[选择产品细分（可选）]
  ↓
[输入专项需求（可选）]
  ↓
[点击生成]
  ↓
[显示生成进度]
  ↓
[查看设计建议]
  ├─→ 尺寸参数
  ├─→ 功能参数
  ├─→ 测试矩阵
  └─→ 品牌对比
  ↓
[查看法规依据]
  ↓
[导出/分享]
  ↓
[触发GitHub构建（可选）]
  ↓
结束
```

---

### 3.3 语音输入操作

**适用场景**：驾车、双手忙碌等场景

**操作步骤**：
```
1. 点击输入框右侧的"🎤"图标
2. 说出核心参数：
   • "儿童身高60到105厘米"
   • "婴儿推车承重0到15公斤"
3. 系统自动识别并填充：
   • 身高范围：60-105cm
   • 或重量范围：0-15kg
4. 确认无误后点击"生成"
```

**语音指令示例**：
- ✅ "儿童身高60到105厘米"
- ✅ "安全座椅承重9到18公斤"
- ✅ "婴儿推车0到15公斤"
- ✅ "优化头托侧面防护"
- ❌ "帮我设计一个安全座椅"（模糊指令，需手动输入）

---

### 3.4 历史记录复用

**功能**：保存最近10条输入记录，支持一键复用

**操作步骤**：
```
1. 打开应用，点击"历史记录"
2. 查看历史列表：
   • 按时间排序
   • 显示品类、标准、参数
3. 点击记录项：
   • 自动填充参数
   • 可修改后重新生成
4. 点击"删除"按钮可删除单条记录
```

**历史记录示例**：
```
历史记录
┌─────────────────────────┐
│ 2026-01-29 14:30        │
│ 儿童安全座椅             │
│ 身高：60-105cm          │
│ 标准：ECE R129           │
│ [复用] [删除]           │
├─────────────────────────┤
│ 2026-01-28 10:15        │
│ 婴儿推车                 │
│ 重量：0-15kg            │
│ 标准：EN 1888           │
│ [复用] [删除]           │
└─────────────────────────┘
```

---

## 四、功能模块详解

### 4.1 输入模块

#### 4.1.1 功能原理

**核心逻辑**：
```
输入 → 品类识别 → 标准匹配 → 参数计算 → 输出建议
```

**品类识别规则**：

| 输入类型 | 识别结果 | 匹配标准 |
|----------|----------|----------|
| 身高范围（cm） | 儿童安全座椅 | ECE R129/i-Size |
| 重量范围（kg） | 儿童安全座椅 | FMVSS 213 |
| 重量范围（kg） | 婴儿推车 | EN 1888 / ASTM F833 |

**标准匹配规则**：

| 输入范围 | 匹配标准 | 推荐分组/类型 |
|----------|----------|---------------|
| 身高40-75cm | ECE R129 | Group 0+（后向强制） |
| 身高60-105cm | ECE R129 | Group 0/1 |
| 身高100-150cm | ECE R129 | Group 2/3 |
| 重量0-13kg | FMVSS 213 | Infant Seat |
| 重量9-18kg | FMVSS 213 | Convertible |
| 重量16-30kg | FMVSS 213 | Forward-facing |
| 重量0-15kg | EN 1888 | 通用型推车 |
| 重量0-22kg | EN 1888 | 重型推车 |

#### 4.1.2 操作步骤

**步骤1：选择品类**

```
品类选择：
  □ 儿童安全座椅
  ☑ 婴儿推车
  □ 儿童家庭用品
```

**步骤2：输入核心参数**

- **身高输入**：
  ```
  格式：最小值-最大值
  单位：cm
  示例：60-105
  
  校验规则：
  • 最小值 > 0
  • 最大值 > 最小值
  • 最大值 ≤ 150cm
  ```

- **重量输入**：
  ```
  格式：最小值-最大值
  单位：kg 或 lb
  示例：0-15kg 或 0-33lb
  
  校验规则：
  • 最小值 > 0
  • 最大值 > 最小值
  • 最大值 ≤ 36kg（安全座椅）
  • 最大值 ≤ 25kg（推车）
  
  自动转换：
  • 1kg = 2.20462lb
  • 1lb = 0.453592kg
  ```

**步骤3：选择产品细分（可选）**

- **儿童安全座椅细分**：
  ```
  • Group 0+（新生儿专用，0-13kg）
  • Group 0/1（可转换，0-18kg）
  • Group 1（前向，9-18kg）
  • Group 2/3（增高，15-36kg）
  • ISOFIX接口型（硬连接）
  • 便携型（可快速转移）
  • 可旋转型（360°旋转）
  • 增高垫（无靠背）
  ```

- **婴儿推车细分**：
  ```
  • 高景观型（座高≥50cm）
  • 轻便折叠型（重量≤10kg）
  • 运动型（大轮避震）
  • 双胞胎专用型（双座）
  • 旅行系统（适配安全座椅）
  • 慢跑推车（三轮设计）
  ```

**步骤4：输入专项需求（可选）**

从常用需求中选择或自定义输入：

**安全座椅常用需求**：
- 优化头托侧面碰撞防护
- 符合i-Size Envelope内部尺寸
- 提升安装便捷性
- 增强侧面支撑结构
- 优化通风散热性能
- 降低座椅重量
- 提升座椅舒适度
- 优化安全带调节系统

**婴儿推车常用需求**：
- 提升避震性能适配崎岖路面
- 优化折叠机构实现一键收纳
- 提升折叠后的便携性
- 增强储物篮容量
- 优化转向灵活性
- 提升座椅舒适性
- 适配多种安全座椅
- 优化遮阳篷防护性能

#### 4.1.3 输入校验

**实时校验提示**：

| 校验项 | 错误提示 | 解决方案 |
|--------|----------|----------|
| 身高范围 | "最小身高必须小于最大身高" | 调整数值顺序 |
| 身高范围 | "身高范围超出常规范围（40-150cm）" | 确认输入是否正确 |
| 身高范围 | "身高超过75cm，建议使用前向安装模式" | 选择Group 2/3 |
| 重量范围 | "重量数值格式错误" | 输入数字 |
| 重量范围 | "最大重量超过常规范围" | 确认产品类型 |
| 折叠尺寸 | "折叠尺寸超过车载收纳推荐尺寸" | 调整设计目标 |

**智能联想功能**：

输入"60"后自动提示：
```
60cm → 提示：ECE R129 Group 0+标准，后向安装
60kg → 提示：超过安全座椅常规范围（36kg）
```

#### 4.1.4 手机端人性化优化

**单手操作优化**：
- ✅ 关键按钮（生成、分享）位于屏幕底部1/3区域
- ✅ 输入框采用底部弹窗选择器
- ✅ 避免使用多层下拉菜单

**操作反馈**：
- ✅ 按钮点击震动反馈
- ✅ 加载进度环形显示
- ✅ 成功/失败动画提示

**暗黑模式**：
- ✅ 自动跟随系统设置切换
- ✅ 文本对比度优化
- ✅ 长时间阅读不刺眼

---

### 4.2 输出模块

#### 4.2.1 设计建议结构

**生成内容包含**：

```
## 儿童安全座椅设计建议
（适配ECE R129/i-Size 60-105cm身高组）

### 1. 尺寸参数
  1.1 外部尺寸（符合i-Size Envelope标准）
      • 宽度：≤44cm（法规强制上限）
      • 长度：75cm（前后向安装通用）
      • 高度：81cm（含头托最大升起）
  
  1.2 内部尺寸（基于假人数据）
      • 座宽：32cm（适配CRABI 12个月假人肩宽22cm）
      • 座深：40cm（适配CRABI假人臀腿长度35cm）
      • 头托内部宽度：35cm（适配Q3s 3岁假人头宽28cm）
      • 靠背有效高度：65cm（覆盖60-105cm身高脊柱支撑）

### 2. 核心产品功能
  • 头托调节功能：10-25cm，15档，每档1cm
  • 靠背角度调节功能：100°-125°，3档调节
  • 安全防护功能：ISOFIX+上拉带、五点式安全带、侧面防撞块

### 3. 合规测试矩阵
  | 测试项 | 法规标准引用 | 测试方法 | 合格标准 |
  |--------|-------------|----------|----------|
  | 正面碰撞测试 | ECE R129 §5.3.2 | Hybrid III 3岁，50km/h | HIC<700，胸部<50mm |
  | 侧面碰撞测试 | ECE R129 §5.3.3 | Q3s假人，32km/h侧撞 | 头部位移<25cm |

### 4. 品牌参数对比
  • Britax Dualfix M i-Size：SafeCell技术，头托15档调节
  • Maxi-Cosi Pebble 360：FamilyFix 360底座，360°旋转
  • Cybex Cloud Z：L.S.P.侧面防护，SensorSafe智能监测
```

#### 4.2.2 尺寸参数说明

**外部尺寸**：

| 参数 | 推荐范围 | 公差 | 法规依据 | 设计理由 |
|------|----------|------|----------|----------|
| 宽度 | 40-44cm | ±1mm | ECE R129 Annex 7 | 确保适配多数车型后排空间 |
| 长度 | 72-75cm | ±2mm | ECE R129 Annex 7 | 前后向安装通用 |
| 高度 | 76-81cm | ±2mm | ECE R129 Annex 7 | 满足i-Size头部防护空间 |
| 底座厚度 | 11-13cm | ±1mm | - | 集成ISOFIX接口收纳 |

**内部尺寸**：

| 参数 | 推荐范围 | 公差 | 法规依据 | 设计理由 |
|------|----------|------|----------|----------|
| 座深 | 38-42cm | ±2mm | ECE R129 Annex 6 | 适配CRABI假人臀腿长度35cm |
| 座宽 | 30-34cm | ±1mm | ECE R129 Annex 6 | 适配肩宽范围，预留2cm活动 |
| 靠背高度 | 62-68cm | ±2mm | - | 覆盖60-105cm身高脊柱支撑 |
| 头托宽度 | 33-37cm | ±1mm | ECE R129 Annex 6 | 侧面防护包裹性 |
| 肩宽 | 30-34cm | ±1mm | - | 适配儿童肩宽范围 |

#### 4.2.3 功能参数说明

**头托调节功能**：
```
参数：
  • 调节范围：10-25cm
  • 调节档位：15档
  • 调节精度：1cm/档
  • 调节方式：多档位机械卡扣，上下滑动

标准依据：
  • ECE R129 §5.4.2 头托调节可靠性测试

设计理由：
  • 最小高度10cm：适配60cm身高儿童
  • 最大高度25cm：适配105cm身高儿童
  • 15档调节：参考Britax头托调节标准
  • 每档1cm：提供精细调节，避免过大跳变

参考品牌：
  • Britax：15档调节（10-30cm）
  • Maxi-Cosi：12档调节（10-25cm）
```

**靠背角度调节功能**：
```
参数：
  • 调节范围：100°-125°
  • 调节档位：3档
  • 档位设置：100°（前向）、110°（过渡）、125°（后向）
  • 调节方式：一键式机械调节

标准依据：
  • ECE R129 §5.4.3 靠背角度锁定测试

设计理由：
  • 100°：前向安装时保障坐姿稳定
  • 125°：后向安装时符合脊柱保护要求
  • 3档调节：参考Britax 9档快速倾斜设计精简

参考品牌：
  • Britax：9档调节（95°-125°）
  • Cybex：5档调节（100°-130°）
```

#### 4.2.4 合规测试矩阵

**测试项分类**：

1. **碰撞测试（IMP）**
   - IMP-001：正面碰撞测试
   - IMP-002：侧面碰撞测试

2. **功能性测试（FUNC）**
   - FUNC-001：头托调节可靠性测试
   - FUNC-002：靠背角度锁定测试

3. **安全性测试（SAFE）**
   - SAFE-001：ISOFIX接口强度测试
   - SAFE-002：安全带强度测试

4. **耐久性测试（DUR）**
   - DUR-001：折叠机构耐久性测试（推车）
   - DUR-002：锁扣反复操作测试

**测试项详情**：

| 测试项 | 优先级 | 法规标准 | 测试方法 | 合格标准 | 样本量 | 测试时长 |
|--------|--------|----------|----------|----------|--------|----------|
| 正面碰撞测试 | 强制 | ECE R129 §5.3.2 | Hybrid III 3岁，50km/h，50g | HIC<700，胸部<50mm | 5个 | 2-3h |
| 侧面碰撞测试 | 强制 | ECE R129 §5.3.3 | Q3s假人，32km/h侧撞 | 头部位移<25cm | 5个 | 2-3h |
| 头托调节可靠性 | 关键 | ECE R129 §5.4.2 | 调节500次，施加100N压力 | 无卡滞、无松动 | 3个 | 4-5h |
| 靠背角度锁定 | 重要 | FMVSS 213 §4.5 | 各档位施加200N推力30s | 无位移、无失效 | 3个 | 1-2h |
| ISOFIX接口强度 | 强制 | ECE R129 §5.5.1 | 施加5000N拉力10s | 无变形、无断裂 | 5个 | 1-2h |

**测试优先级说明**：
- **强制**：必须测试，法规要求
- **关键**：重要测试，强烈推荐
- **重要**：推荐测试，提升品质
- **建议**：可选测试，优化性能

#### 4.2.5 品牌参数对比

**对比维度**：
- 技术规格（尺寸、重量、材料）
- 核心功能（调节范围、安全配置）
- 独特优势（专利技术、创新设计）
- 市场定位（高端、中端、性价比）

**儿童安全座椅品牌对比**：

| 品牌 | 型号 | 宽度 | 长度 | 高度 | 重量 | 核心优势 | 市场定位 |
|------|------|------|------|------|------|----------|----------|
| Britax | Dualfix M i-Size | 44cm | 57cm | 64cm | 13.5kg | SafeCell吸能、360°旋转 | 高端 |
| Maxi-Cosi | Pebble 360 | 44cm | 65cm | 60cm | 12.5kg | FamilyFix 360、Clash-free | 中高端 |
| Cybex | Cloud Z | 43cm | 66cm | 59cm | 12.0kg | L.S.P.防护、SensorSafe | 高端 |
| UPPAbaby | Mesa | 44cm | 64cm | 66cm | 11.5kg | 轻量化、智能折叠 | 中高端 |

**差异化建议**：
```
基于品牌对比，建议：
  1. 采用Britax的SafeCell吸能技术（底座设计）
  2. 参考Maxi-Cosi的FamilyFix 360底座（旋转功能）
  3. 集成Cybex的L.S.P.侧面防护系统（侧面碰撞）
  4. 学习UPPAbaby的轻量化设计（重量优化）
```

#### 4.2.6 输出格式

**Markdown格式**：
- ✅ 清晰的层级结构
- ✅ 表格形式展示测试矩阵
- ✅ 法规标识可点击跳转
- ✅ 支持长截图分享

**PDF格式**：
- ✅ 精简版（仅核心建议）
- ✅ 完整版（含所有详情）
- ✅ 支持自定义封面
- ✅ 自动生成页码

**图片格式**：
- ✅ 长截图自动拼接
- ✅ 高清图片导出
- ✅ 支持水印添加

---

### 4.3 法规展示模块

#### 4.3.1 法规分类展示

**按地区分类**：
```
全球法规库
├─ 🇪🇺 欧盟
│  ├─ ECE R129/i-Size（儿童安全座椅）
│  ├─ EN 1888（婴儿推车）
│  └─ ISO 13209（儿童用品）
│
├─ 🇺🇸 美国
│  ├─ FMVSS 213（儿童安全座椅）
│  ├─ ASTM F833（婴儿推车）
│  └─ ASTM F966（儿童用品）
│
├─ 🇨🇳 中国
│  ├─ GB 27887（儿童安全座椅）
│  ├─ GB 14748（婴儿推车）
│  └─ GB 6675（玩具安全）
│
├─ 🇯🇵 日本
│  ├─ JIS D 0161（儿童安全座椅）
│  ├─ JIS D 9302（婴儿推车）
│  └─ JIS T 9251（儿童用品）
│
└─ 🇦🇺 澳大利亚
   ├─ AS/NZS 1754（儿童安全座椅）
   ├─ AS/NZS 2088（婴儿推车）
   └─ AS/NZS 8124（儿童用品）
```

#### 4.3.2 法规详情展示

**点击法规标识跳转**：
```
ECE R129 §5.3.2
  ↓
跳转至法规详情页
  ↓
显示：
  • 标准编号：ECE R129
  • 标准名称：关于儿童约束系统审批的统一规定
  • 发布机构：联合国欧洲经济委员会（UNECE）
  • 最新版本：R129修订版（2023）
  • 适用范围：体重<36kg的儿童约束系统
  
条款详情：
  §5.3.2 正面碰撞测试
  1. 测试假人：使用Hybrid III 3岁假人
  2. 碰撞速度：50km/h ± 1km/h
  3. 加速度峰值：50g ± 5g
  4. 合格标准：
     • 头部伤害指数（HIC）< 700
     • 胸部压缩量 < 50mm
     • 头托无脱落
     • 安全带无松脱

关联设计建议：
  • 外部尺寸：长度72-75cm（满足碰撞空间）
  • 内部尺寸：座深38-42cm（适配假人）
  • 安全防护：五点式安全带，肩带宽度5cm
```

#### 4.3.3 测试矩阵筛选

**按目标销售地区筛选**：
```
测试矩阵筛选
  目标市场：☑ 欧盟 ☑ 美国 ☐ 中国 ☑ 日本 ☐ 澳大利亚
  └─> 筛选后显示：
      • 正面碰撞测试（ECE R129 §5.3.2 / FMVSS 213 §4.3）
      • 侧面碰撞测试（ECE R129 §5.3.3 / JIS D 0161 §5.3）
      • ISOFIX接口强度测试（ECE R129 §5.5.1）
      • 头托调节可靠性测试（ECE R129 §5.4.2）
```

#### 4.3.4 法规更新监测

**更新提示**：
```
法规更新通知
  🔔 新版本：ECE R129 修订版（2024年1月）
  └─> 主要变化：
      • 扩展适用体重至40kg
      • 新增侧面碰撞测试要求
      • 修改HIC限值为750
      
  设计适配建议：
  • 更新安全座椅重量参数
  • 增强侧面防护结构
  • 重新进行碰撞测试验证
  
  操作：
  [查看详情] [标记已读] [更新数据库]
```

---

### 4.4 技术集成模块

#### 4.4.1 本地数据库调用

**连接本地数据库**：

```
设置 → 数据库管理 → 连接本地数据库

配置参数：
  数据库类型：SQLite
  数据库路径：/sdcard/child_product_db/
  数据库文件：
    • standards.db（法规标准库）
    • brands.db（品牌参数库）
    • test_data.db（测试数据库）
    • technical_docs.db（技术文档库）

测试连接：
  [测试连接] → 显示"连接成功"
  [保存配置] → 下次自动连接
```

**数据库结构示例**：

```sql
-- standards.db
CREATE TABLE regulations (
  id INTEGER PRIMARY KEY,
  code TEXT NOT NULL,
  name TEXT NOT NULL,
  region TEXT NOT NULL,
  category TEXT NOT NULL,
  version TEXT,
  publish_date TEXT,
  content TEXT
);

CREATE TABLE test_items (
  id INTEGER PRIMARY KEY,
  regulation_id INTEGER,
  test_id TEXT NOT NULL,
  test_name TEXT NOT NULL,
  section TEXT,
  test_method TEXT,
  acceptance_criteria TEXT,
  priority TEXT,
  FOREIGN KEY (regulation_id) REFERENCES regulations(id)
);

-- brands.db
CREATE TABLE brands (
  id INTEGER PRIMARY KEY,
  brand_name TEXT NOT NULL,
  product_name TEXT NOT NULL,
  category TEXT NOT NULL,
  region TEXT,
  market_position TEXT
);

CREATE TABLE brand_parameters (
  id INTEGER PRIMARY KEY,
  brand_id INTEGER,
  parameter_name TEXT NOT NULL,
  parameter_value TEXT,
  unit TEXT,
  FOREIGN KEY (brand_id) REFERENCES brands(id)
);
```

**查询示例**：

```kotlin
// 查询特定法规的所有测试项
fun getTestItemsByRegulation(code: String): List<TestItem> {
    val query = """
        SELECT * FROM test_items ti
        JOIN regulations r ON ti.regulation_id = r.id
        WHERE r.code = ?
    """.trimIndent()
    
    return database.rawQuery(query, arrayOf(code)).use { cursor ->
        cursor.mapToList { it.toTestItem() }
    }
}

// 查询品牌参数
fun getBrandParameters(brandName: String, productName: String): List<BrandParameter> {
    val query = """
        SELECT bp.* FROM brand_parameters bp
        JOIN brands b ON bp.brand_id = b.id
        WHERE b.brand_name = ? AND b.product_name = ?
    """.trimIndent()
    
    return database.rawQuery(query, arrayOf(brandName, productName)).use { cursor ->
        cursor.mapToList { it.toBrandParameter() }
    }
}
```

#### 4.4.2 GitHub自动化

**GitHub OAuth2授权**：

```
设置 → GitHub集成 → 连接GitHub

授权方式：
  方式1：扫码登录（推荐）
    • 生成二维码
    • 用GitHub手机APP扫码
    • 授权应用访问权限
  
  方式2：输入Token
    • 生成Personal Access Token
    • 复制Token到应用
    • 完成授权

授权权限：
  ☑ repo（代码仓库访问）
  ☑ workflow（工作流操作）
  ☑ packages（包管理）
  
Token存储：
  • 加密算法：AES-256
  • 存储位置：Android Keystore
  • 安全提示：Token仅在本地使用，不会上传服务器
```

**代码增量提交**：

```
操作流程：
  1. 在设计建议页面点击"更多操作"
  2. 选择"生成代码"
  3. 系统自动：
     • 将设计建议转换为Kotlin/Java代码
     • 对比仓库现有代码
     • 生成增量更新包
     • 提交到指定分支
  
提交信息格式：
  【AI自动更新】-品类-功能-版本
  
  示例：
  【AI自动更新】-儿童安全座椅-头托调节逻辑-v1.1
  
  Commit内容：
  - 更新头托调节范围：10-25cm，15档
  - 新增侧面防护参数：LSP行程5cm
  - 参考标准：ECE R129 §5.3.3
  
目标分支：
  • design-suggestion/child-seat-{日期}
  • design-suggestion/stroller-{日期}
  
示例：
  design-suggestion/child-seat-20260129
  design-suggestion/stroller-20260129
```

**AI驱动的自动构建**：

```
构建流程：
  代码提交
    ↓
  自动触发GitHub Actions
    ↓
  代码校验（语法检查、兼容性检查）
    ↓
  开始构建
    ├─ 拉取代码
    ├─ 安装依赖（使用大陆镜像源）
    ├─ Gradle构建
    ├─ 单元测试
    ├─ 签名（使用GitHub Secrets）
    ├─ 上传Artifacts
    └─ 发布Releases
    ↓
  构建完成
    ├─ 生成APK下载链接
    ├─ 生成更新日志
    └─ 推送通知
```

**GitHub Actions YAML模板**：

```yaml
# .github/workflows/ai-auto-build-apk.yml
name: AI Auto Build APK

on:
  push:
    branches:
      - design-suggestion/**
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - name: 📥 Checkout code
      uses: actions/checkout@v4
      
    - name: ☕ Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: 🤖 Set up Android SDK
      uses: android-actions/setup-android@v3
      
    - name: 🇨🇳 Configure Maven mirror (China)
      run: |
        mkdir -p ~/.gradle
        cat > ~/.gradle/init.gradle <<EOF
        allprojects {
          repositories {
            maven { url 'https://maven.aliyun.com/repository/google' }
            maven { url 'https://maven.aliyun.com/repository/jcenter' }
            maven { url 'https://maven.aliyun.com/repository/central' }
          }
        }
        EOF
        
    - name: 🔓 Grant execute permission
      run: chmod +x gradlew
      
    - name: 🚀 Build Debug APK
      run: ./gradlew assembleDebug --stacktrace --no-daemon
      env:
        GRADLE_OPTS: "-Dorg.gradle.daemon=false -Xmx4g"
        
    - name: 🚀 Build Release APK
      run: ./gradlew assembleRelease --stacktrace --no-daemon
      env:
        GRADLE_OPTS: "-Dorg.gradle.daemon=false -Xmx4g"
        
    - name: 📤 Upload Debug APK
      uses: actions/upload-artifact@v4
      with:
        name: app-debug-v${{ github.run_number }}
        path: app/build/outputs/apk/debug/app-debug.apk
        
    - name: 📤 Upload Release APK
      uses: actions/upload-artifact@v4
      with:
        name: app-release-v${{ github.run_number }}
        path: app/build/outputs/apk/release/app-release-unsigned.apk
        
    - name: 🏷️ Create Release
      uses: softprops/action-gh-release@v1
      with:
        tag_name: v${{ github.run_number }}
        name: Release v${{ github.run_number }}
        body: |
          ## 🎉 AI Auto Build Release
          
          ### 📦 APK Files
          - Debug: app-debug-v${{ github.run_number }}.apk
          - Release: app-release-v${{ github.run_number }}.apk
          
          ### 📝 Changes
          - Auto-generated by AI Analysis Service
          - Based on design suggestions
          
          ### 🔗 Download
          See Artifacts section below.
        draft: false
        prerelease: true
```

**版本管理**：

```
版本号规则：语义化版本（Semantic Versioning）
  格式：v主版本.次版本.修订号
  
  示例：
  • v1.0.0 → v1.0.1（Bug修复）
  • v1.0.1 → v1.1.0（功能新增）
  • v1.1.0 → v2.0.0（架构变更）
  
更新规则：
  • Bug修复：修订号+1
  • 功能新增：次版本+1
  • 架构变更：主版本+1
  
更新日志自动生成：
  ## v1.1.0 (2026-01-29)
  
  ### ✨ 新增功能
  • 优化头托调节逻辑（10-25cm，15档）
  • 新增侧面防护参数（LSP行程5cm）
  
  ### 🔧 改进
  • 优化数据库查询性能
  • 增强法规展示模块
  
  ### 🐛 修复
  • 修复输入校验错误
  • 修复PDF导出问题
```

**大陆友好型优化**：

```
镜像源配置：
  Maven仓库：
    • 阿里云Google仓库：https://maven.aliyun.com/repository/google
    • 阿里云JCenter仓库：https://maven.aliyun.com/repository/jcenter
    • 阿里云Central仓库：https://maven.aliyun.com/repository/central
  
  Android SDK：
    • 使用国内镜像源加速下载
    • 自动选择最快的下载节点
  
  Docker镜像（如需要）：
    • 阿里云容器镜像服务
    • 清华大学TUNA镜像
```

**Gitee集成（备用）**：

```
支持配置Gitee仓库作为备用：
  • 主仓库：GitHub（国际使用）
  • 备用仓库：Gitee（国内使用）
  
自动切换规则：
  • GitHub访问失败 → 自动切换至Gitee
  • 网络恢复 → 自动切换回GitHub
  
配置示例：
  Git仓库配置：
    • 主仓库：https://github.com/user/repo.git
    • 备用仓库：https://gitee.com/user/repo.git
    • 自动切换：☑ 启用
```

#### 4.4.3 性能适配

**APK体积优化**：

```
目标：APK体积 ≤ 50MB

优化措施：
  1. 代码混淆：使用ProGuard/R8
  2. 资源压缩：移除未使用的资源
  3. 图片优化：WebP格式替代PNG
  4. 动态加载：数据库、法规库动态下载
  
体积分布：
  • 代码：8MB
  • 资源：12MB
  • 库文件：15MB
  • 数据库：10MB（可动态下载）
  • 其他：5MB
  总计：50MB
```

**Android版本适配**：

```
最低版本：Android 10.0 (API 29)
目标版本：Android 14.0 (API 34)

兼容性测试：
  ✅ Android 10 (API 29)
  ✅ Android 11 (API 30)
  ✅ Android 12 (API 31)
  ✅ Android 13 (API 32)
  ✅ Android 14 (API 34)
  
特性适配：
  • 深色主题适配
  • 权限管理适配
  • 存储分区适配
  • 后台限制适配
```

**网络适配**：

```
正常网络：
  • 实时加载所有内容
  • 支持高清图片
  • 实时更新法规
  
弱网模式：
  • 仅加载核心文本
  • 压缩图片质量
  • 优先使用本地缓存
  
离线模式：
  • 使用本地缓存
  • 保存最近使用的建议
  • 标注数据非最新
  
数据缓存：
  • SharedPreferences缓存（7天有效期）
  • SQLite本地数据库
  • 文件系统缓存（设计建议PDF）
```

**机型适配**：

```
屏幕适配：
  • 小屏（<5.5"）：优化布局，避免拥挤
  • 中屏（5.5"-6.5"）：标准布局
  • 大屏（>6.5"）：充分利用空间
  
分辨率适配：
  • 720×1280 (HD)
  • 1080×1920 (FHD)
  • 1440×2560 (QHD)
  • 2160×3840 (4K)
  
厂商适配：
  • 华为（EMUI）：通知栏适配
  • 小米（MIUI）：权限提示适配
  • OPPO（ColorOS）：后台限制适配
  • vivo（OriginOS）：后台限制适配
```

---

## 五、设计建议解读

### 5.1 生成逻辑说明

**设计建议生成流程**：

```
用户输入
  ↓
品类识别 + 标准匹配
  ↓
查询法规数据库
  ↓
查询品牌数据库
  ↓
生成设计建议
  ├─ 尺寸参数（基于假人数据）
  ├─ 功能参数（基于行业标准）
  ├─ 测试矩阵（基于法规要求）
  └─ 品牌对比（基于市场数据）
  ↓
输出结构化建议
```

**法规匹配规则**：

```
身高范围 → ECE R129/i-Size
  • 40-75cm → Group 0+（后向强制）
  • 60-105cm → Group 0/1
  • 100-150cm → Group 2/3
  
重量范围 → FMVSS 213
  • 0-13kg → Infant Seat
  • 9-18kg → Convertible
  • 16-30kg → Forward-facing
  
重量范围 → EN 1888（婴儿推车）
  • 0-15kg → 通用型
  • 0-22kg → 重型型
```

### 5.2 参数合理性说明

**尺寸参数合理性**：

| 参数 | 推荐范围 | 合理性说明 |
|------|----------|------------|
| 宽度 | 40-44cm | 法规强制上限≤44cm，确保适配多数车型 |
| 长度 | 72-75cm | 前后向安装通用，避免与座椅冲突 |
| 高度 | 76-81cm | 满足i-Size头部防护空间要求 |
| 座深 | 38-42cm | 适配CRABI假人臀腿长度35cm，避免悬空 |
| 座宽 | 30-34cm | 适配肩宽范围，预留2cm活动空间 |

**功能参数合理性**：

| 功能 | 推荐值 | 合理性说明 |
|------|--------|------------|
| 头托调节范围 | 10-25cm | 覆盖60-105cm身高，每档1cm精度 |
| 靠背角度范围 | 100°-125° | 100°前向稳定，125°后向保护 |
| 调节档位 | 15档（头托）、3档（靠背） | 参考Britax标准，提供足够精度 |
| 安全带宽度 | 5cm（肩带） | 符合FMVSS 213标准，分散受力 |

### 5.3 品牌对比维度

**对比维度说明**：

1. **技术规格对比**
   - 尺寸参数
   - 重量参数
   - 材料选择

2. **功能配置对比**
   - 调节范围
   - 安全配置
   - 特色功能

3. **市场定位对比**
   - 高端市场（Britax、Cybex）
   - 中高端市场（Maxi-Cosi、UPPAbaby）
   - 性价比市场（国产品牌）

4. **差异化建议**
   - 取长补短
   - 创新突破
   - 成本优化

---

## 六、技术适配说明

### 6.1 本地数据库调用规范

**连接配置**：

```
数据库配置文件格式：JSON

{
  "database_type": "SQLite",
  "database_path": "/sdcard/child_product_db/",
  "databases": {
    "standards": "standards.db",
    "brands": "brands.db",
    "test_data": "test_data.db",
    "technical_docs": "technical_docs.db"
  },
  "connection_pool": {
    "max_connections": 5,
    "connection_timeout": 30000,
    "idle_timeout": 600000
  },
  "query_timeout": 10000,
  "enable_query_log": true,
  "log_path": "/sdcard/child_product_db/logs/"
}
```

**查询规范**：

```kotlin
// 使用参数化查询，防止SQL注入
fun getTestItems(regulationCode: String): List<TestItem> {
    val query = """
        SELECT * FROM test_items 
        WHERE regulation_id = (
            SELECT id FROM regulations 
            WHERE code = ?
        )
    """.trimIndent()
    
    return database.rawQuery(query, arrayOf(regulationCode)).use { cursor ->
        cursor.mapToList { it.toTestItem() }
    }
}

// 使用事务，确保数据一致性
fun insertBatchTestItems(items: List<TestItem>) {
    database.beginTransaction()
    try {
        items.forEach { item ->
            val stmt = database.compileStatement("""
                INSERT INTO test_items (
                    test_id, test_name, section, 
                    test_method, acceptance_criteria, priority
                ) VALUES (?, ?, ?, ?, ?, ?)
            """.trimIndent())
            
            stmt.bindString(1, item.testId)
            stmt.bindString(2, item.testName)
            stmt.bindString(3, item.section)
            stmt.bindString(4, item.testMethod)
            stmt.bindString(5, item.acceptanceCriteria)
            stmt.bindString(6, item.priority)
            
            stmt.execute()
            stmt.close()
        }
        database.setTransactionSuccessful()
    } finally {
        database.endTransaction()
    }
}
```

**性能优化**：

```kotlin
// 使用索引加速查询
CREATE INDEX idx_regulation_code ON regulations(code);
CREATE INDEX idx_test_regulation ON test_items(regulation_id);
CREATE INDEX idx_brand_name ON brands(brand_name);

// 分页查询
fun getTestItemsPaginated(
    regulationCode: String, 
    page: Int, 
    pageSize: Int
): List<TestItem> {
    val offset = page * pageSize
    val query = """
        SELECT * FROM test_items ti
        JOIN regulations r ON ti.regulation_id = r.id
        WHERE r.code = ?
        LIMIT ? OFFSET ?
    """.trimIndent()
    
    return database.rawQuery(query, arrayOf(regulationCode, pageSize, offset)).use { cursor ->
        cursor.mapToList { it.toTestItem() }
    }
}

// 使用缓存
private val testItemsCache = LruCache<String, List<TestItem>>(20)

fun getTestItemsWithCache(regulationCode: String): List<TestItem> {
    return testItemsCache.get(regulationCode) ?: run {
        val items = getTestItems(regulationCode)
        testItemsCache.put(regulationCode, items)
        items
    }
}
```

### 6.2 GitHub集成配置步骤

**步骤1：生成GitHub Token**

```
1. 登录GitHub
2. 点击右上角头像 → Settings
3. 左侧菜单 → Developer settings
4. Personal access tokens → Tokens (classic)
5. Generate new token (classic)
6. 配置权限：
   ☑ repo（完整仓库访问）
   ☑ workflow（工作流操作）
   ☑ packages（包管理）
   ☑ delete_repo（删除仓库，可选）
7. Generate token
8. 复制Token（仅显示一次）
```

**步骤2：在APK中配置Token**

```
设置 → GitHub集成 → 连接GitHub

输入Token：
  • 粘贴Token
  • 点击"验证连接"
  • 验证成功后自动保存（加密存储）
```

**步骤3：配置GitHub Actions**

```
在GitHub仓库中创建 .github/workflows/ 目录
创建 ai-auto-build-apk.yml 文件
粘贴提供的YAML模板
提交到仓库
```

**步骤4：配置GitHub Secrets**

```
1. 进入GitHub仓库
2. Settings → Secrets and variables → Actions
3. New repository secret
4. 添加以下Secrets：
   • KEYSTORE_BASE64（签名文件Base64编码）
   • KEYSTORE_PASSWORD（签名密码）
   • KEY_ALIAS（密钥别名）
   • KEY_PASSWORD（密钥密码）
```

**步骤5：配置大陆镜像源**

```
在仓库根目录创建 .gradle/init.gradle 文件：

allprojects {
  repositories {
    maven { url 'https://maven.aliyun.com/repository/google' }
    maven { url 'https://maven.aliyun.com/repository/jcenter' }
    maven { url 'https://maven.aliyun.com/repository/central' }
  }
}
```

### 6.3 性能优化设置建议

**APK体积优化**：

```gradle
// app/build.gradle

android {
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    
    bundle {
        language {
            enableSplit = false
        }
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }
}

dependencies {
    implementation 'com.android.tools.build:gradle:8.2.0'
}
```

**网络请求优化**：

```kotlin
// 使用OkHttp连接池
val okHttpClient = OkHttpClient.Builder()
    .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .addInterceptor(ChuckerInterceptor(context))
    .build()

// 使用Retrofit + OkHttp
val retrofit = Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()
```

**数据库优化**：

```kotlin
// 使用Room数据库（推荐）
@Database(
    entities = [Regulation::class, TestItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun regulationDao(): RegulationDao
    abstract fun testItemDao(): TestItemDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "child_product.db"
                )
                .addMigrations(MIGRATION_1_2)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

---

## 七、常见问题

### 7.1 输入校验失败

**问题1：身高范围格式错误**

```
错误提示：
  "身高范围格式错误，应为：最小值-最大值（如：60-105cm）"
  
原因：
  • 使用了错误的分隔符
  • 忘记添加单位
  • 数值顺序错误
  
解决方案：
  ✅ 正确格式：60-105cm
  ❌ 错误格式：60-105、60cm-105cm、105-60cm
```

**问题2：重量范围超出常规**

```
错误提示：
  "最大重量超过常规儿童座椅范围（36kg）"
  
原因：
  • 输入的重量范围过大
  • 可能选择了错误的产品类型
  
解决方案：
  • 确认产品类型（安全座椅最大36kg，推车最大25kg）
  • 调整重量范围
```

**问题3：折叠尺寸过大**

```
警告提示：
  "折叠尺寸超过车载收纳推荐尺寸（50×30×20cm）"
  
原因：
  • 高景观推车折叠尺寸通常较大
  
解决方案：
  • 改为轻便款设计
  • 或接受车载收纳限制
```

### 7.2 法规加载异常

**问题1：法规数据库未更新**

```
错误提示：
  "当前数据可能非最新，建议网络恢复后更新"
  
原因：
  • 网络中断
  • 数据库缓存过期（7天）
  
解决方案：
  • 检查网络连接
  • 点击"更新法规库"手动更新
  • 或使用本地数据库
```

**问题2：法规详情无法打开**

```
错误提示：
  "法规详情加载失败"
  
原因：
  • 法规ID不存在
  • 数据库损坏
  
解决方案：
  • 尝试更新法规库
  • 或联系技术支持
```

### 7.3 GitHub构建失败

**问题1：Token无效**

```
错误提示：
  "GitHub Token无效，请重新授权"
  
原因：
  • Token已过期
  • Token权限不足
  
解决方案：
  1. 进入GitHub → Settings → Developer settings
  2. 重新生成Token
  3. 在APK中重新配置
```

**问题2：构建超时**

```
错误提示：
  "构建超时，请检查构建日志"
  
原因：
  • 依赖下载慢
  • 构建时间过长
  
解决方案：
  • 配置大陆镜像源
  • 增加构建超时时间
  • 优化Gradle配置
```

**问题3：构建失败**

```
错误提示：
  "构建失败：编译错误"
  
原因：
  • 代码语法错误
  • 依赖版本冲突
  
解决方案：
  • 查看构建日志
  • 修复错误后重新提交
  • 或联系技术支持
```

---

## 八、附录

### 8.1 全球法规清单

#### 8.1.1 儿童安全座椅法规

| 法规编号 | 法规名称 | 发布机构 | 适用范围 | 最新版本 |
|----------|----------|----------|----------|----------|
| ECE R129 | 关于儿童约束系统审批的统一规定 | UNECE | 体重<36kg | 2023修订版 |
| FMVSS 213 | 儿童约束系统 | NHTSA | 体重<36kg | 2023版 |
| GB 27887 | 机动车儿童乘员用约束系统 | 中国标准化管理委员会 | 体重<36kg | GB 27887-2024 |
| JIS D 0161 | 儿童约束装置 | 日本工业标准调查会 | 体重<36kg | JIS D 0161:2023 |
| AS/NZS 1754 | 儿童约束系统 | 澳大利亚/新西兰标准协会 | 体重<36kg | AS/NZS 1754:2024 |

#### 8.1.2 婴儿推车法规

| 法规编号 | 法规名称 | 发布机构 | 适用范围 | 最新版本 |
|----------|----------|----------|----------|----------|
| EN 1888 | 儿童护理用品—轮式推车 | 欧洲标准化委员会 | 重量<22kg | EN 1888:2018 |
| ASTM F833 | 轮式婴儿推车标准消费者安全规范 | ASTM国际 | 重量<22kg | ASTM F833-22 |
| GB 14748 | 婴儿推车安全要求 | 中国标准化管理委员会 | 重量<22kg | GB 14748-2020 |
| JIS D 9302 | 婴儿推车 | 日本工业标准调查会 | 重量<22kg | JIS D 9302:2022 |
| AS/NZS 2088 | 婴儿推车 | 澳大利亚/新西兰标准协会 | 重量<22kg | AS/NZS 2088:2023 |

### 8.2 知名品牌参数对比表

#### 8.2.1 儿童安全座椅品牌对比

| 品牌 | 型号 | 宽度 | 长度 | 高度 | 重量 | 核心优势 | 参考价格 |
|------|------|------|------|------|------|----------|----------|
| Britax | Dualfix M i-Size | 44cm | 57cm | 64cm | 13.5kg | SafeCell吸能、360°旋转 | ¥3,500 |
| Maxi-Cosi | Pebble 360 | 44cm | 65cm | 60cm | 12.5kg | FamilyFix 360、Clash-free | ¥3,200 |
| Cybex | Cloud Z | 43cm | 66cm | 59cm | 12.0kg | L.S.P.防护、SensorSafe | ¥3,800 |
| UPPAbaby | Mesa | 44cm | 64cm | 66cm | 11.5kg | 轻量化、智能折叠 | ¥2,900 |
| Graco | SnugRide 360 | 45cm | 68cm | 62cm | 10.8kg | 性价比高、易安装 | ¥1,800 |

#### 8.2.2 婴儿推车品牌对比

| 品牌 | 型号 | 展开尺寸 | 折叠尺寸 | 重量 | 核心优势 | 参考价格 |
|------|------|----------|----------|------|----------|----------|
| UPPAbaby | Cruz V2 | 59×101×98cm | 53×66×27cm | 9.2kg | 一键折叠、前轮避震 | ¥4,500 |
| Baby Jogger | City Mini GT2 | 64×104×92cm | 60×76×33cm | 11.4kg | 越野避震、全尺寸轮 | ¥3,800 |
| Cybex | Mios | 55×95×85cm | 48×29×64cm | 8.9kg | 轻量化、时尚设计 | ¥3,200 |
| Bugaboo | Fox 5 | 60×98×105cm | 52×56×36cm | 9.7kg | 舒适性、多功能 | ¥6,800 |
| Joie | Versa | 58×94×102cm | 56×35×67cm | 9.5kg | 性价比、易折叠 | ¥2,500 |

### 8.3 核心技术依赖说明

#### 8.3.1 开发框架

| 技术栈 | 版本 | 用途 |
|--------|------|------|
| Kotlin | 1.9.x | 主要开发语言 |
| Jetpack Compose | 1.5.x | UI框架 |
| Material Design 3 | 1.1.x | 设计规范 |
| ViewModel | 2.6.x | 状态管理 |
| Coroutines | 1.7.x | 异步处理 |
| Room | 2.6.x | 本地数据库 |
| Retrofit | 2.9.x | 网络请求 |
| OkHttp | 4.12.x | HTTP客户端 |
| Gson | 2.10.x | JSON解析 |
| iTextPDF | 8.0.x | PDF生成 |

#### 8.3.2 第三方库

| 库名 | 版本 | 用途 |
|------|------|------|
| Glide | 4.16.x | 图片加载 |
| Coil | 2.5.x | 图片加载（Kotlin协程） |
| Lottie | 6.1.x | 动画效果 |
| CameraX | 1.3.x | 相机功能 |
| ExoPlayer | 2.19.x | 视频播放 |
| WorkManager | 2.8.x | 后台任务 |
| DataStore | 1.0.x | 数据存储 |
| Splashscreen | 1.0.x | 启动屏 |

#### 8.3.3 AI模型集成

| 模型 | 提供商 | 用途 | 访问方式 |
|------|--------|------|----------|
| GLM-4-Flash | 智谱AI | 设计建议生成 | API调用 |
| Qwen-Plus | 阿里云通义千问 | 设计建议生成 | API调用 |
| Doubao | 字节跳动 | 设计建议生成 | API调用 |
| DeepSeek | DeepSeek | 设计建议生成 | API调用 |
| Gemini 2.0 Flash | Google | 设计建议生成 | API调用 |
| Llama 3.1 | Groq | 设计建议生成 | API调用 |

---

## 更新日志

### v1.0.0 (2026-01-29)

**新增功能**：
- ✅ 儿童安全座椅设计建议生成
- ✅ 婴儿推车设计建议生成
- ✅ 全球法规数据库
- ✅ 品牌参数对比
- ✅ GitHub自动化构建
- ✅ 本地数据库调用
- ✅ PDF导出功能
- ✅ 语音输入
- ✅ 历史记录

**已知问题**：
- 暂无

**后续计划**：
- 支持更多产品品类
- 增强AI生成能力
- 优化离线模式
- 添加更多品牌数据

---

## 技术支持

**联系方式**：
- 📧 邮箱：support@childproduct-design.com
- 📱 微信：ChildProductDesign
- 🌐 官网：www.childproduct-design.com

**反馈渠道**：
- GitHub Issues：https://github.com/user/repo/issues
- 应用内反馈：设置 → 帮助与反馈

**文档更新**：
- 本文档将根据产品更新持续维护
- 请关注GitHub仓库获取最新版本

---

**文档结束**

感谢使用儿童产品设计APK！

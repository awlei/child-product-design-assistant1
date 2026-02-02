package com.childproduct.designassistant.service

import android.content.Context
import com.childproduct.designassistant.model.*
import com.childproduct.designassistant.model.ProductType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import org.json.JSONArray
import org.json.JSONObject

/**
 * AI分析与生成服务
 */
class AIAnalysisService(private val context: Context) {
    
    companion object {
        private const val TAG = "AIAnalysisService"
        
        // 模型配置
        const val DEFAULT_MODEL = "zhipu_glm4_flash"
        const val TIMEOUT_MS = 30000
        
        // 首选模型列表（按优先级）
        private val PREFERRED_MODELS = listOf(
            "zhipu_glm4_flash",
            "qwen_plus",
            "doubao_pro",
            "deepseek_chat",
            "gemini_2_flash",
            "groq_llama3_1"
        )
    }
    
    /**
     * 生成设计建议
     */
    suspend fun generateDesignSuggestions(
        request: AIGenerationRequest
    ): Result<AIGenerationResponse> = withContext(Dispatchers.IO) {
        try {
            // 构建提示词
            val prompt = buildPrompt(request)
            
            // 选择模型
            val selectedModel = selectModel(request.options.detailLevel)
            
            // 调用LLM
            val response = callLLM(selectedModel, prompt)
            
            // 解析响应
            val suggestions = parseLLMResponse(response, request)
            
            Result.success(
                AIGenerationResponse(
                    success = true,
                    designSuggestions = suggestions.designSuggestions,
                    brandComparison = suggestions.brandComparison,
                    dvpTestMatrix = suggestions.dvpTestMatrix,
                    standardCompliance = suggestions.standardCompliance,
                    tokensUsed = response.tokensUsed,
                    modelUsed = selectedModel,
                    error = null
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 构建提示词
     */
    private fun buildPrompt(request: AIGenerationRequest): String {
        val sb = StringBuilder()
        
        // 角色定义
        sb.appendLine("你是一位专业的儿童产品设计专家，专注于儿童安全座椅和婴儿推车领域。")
        sb.appendLine("你的任务是基于用户需求、国际标准和品牌参数，生成专业、可落地的设计建议。")
        sb.appendLine()
        
        // 输入信息
        sb.appendLine("## 产品信息")
        sb.appendLine("- 产品类型: ${request.productType.displayName}")
        sb.appendLine("- 适用标准: ${request.standard.displayName} (${request.standard.code})")
        sb.appendLine()
        
        // 用户需求
        sb.appendLine("## 用户需求")
        when (val input = request.userInput) {
            is ProductInput.SafetySeat -> {
                sb.appendLine("- 标准选择: ${input.data.standard?.displayName}")
                input.data.subtype?.let { sb.appendLine("- 产品细分: ${it.displayName}") }
                input.data.heightRange?.let { sb.appendLine("- 身高范围: ${it}") }
                input.data.weightRange?.let { sb.appendLine("- 重量范围: ${it}") }
                input.data.ageGroup?.let { sb.appendLine("- 年龄组: ${it.displayName}") }
                if (input.data.customFeatures.isNotEmpty()) {
                    sb.appendLine("- 自定义特征: ${input.data.customFeatures.joinToString(", ")}")
                }
                if (input.data.specificRequirements.isNotEmpty()) {
                    sb.appendLine("- 专项需求: ${input.data.specificRequirements.joinToString(", ")}")
                }
            }
            is ProductInput.Stroller -> {
                sb.appendLine("- 标准选择: ${input.data.standard?.displayName}")
                input.data.subtype?.let { sb.appendLine("- 产品细分: ${it.displayName}") }
                input.data.weightCapacityRange?.let { sb.appendLine("- 承重范围: ${it}") }
                input.data.foldedDimensions?.let { sb.appendLine("- 折叠尺寸: ${it}") }
                input.data.usageScenario?.let { sb.appendLine("- 使用场景: ${it.displayName}") }
                if (input.data.customFeatures.isNotEmpty()) {
                    sb.appendLine("- 自定义特征: ${input.data.customFeatures.joinToString(", ")}")
                }
                if (input.data.specificRequirements.isNotEmpty()) {
                    sb.appendLine("- 专项需求: ${input.data.specificRequirements.joinToString(", ")}")
                }
            }
            is ProductInput.HouseholdGood -> {
                sb.appendLine("- 产品类别: ${input.data.productCategory}")
                sb.appendLine("- 目标年龄组: ${input.data.targetAgeGroup.displayName}")
                sb.appendLine("- 使用场景: ${input.data.usageScenario}")
                if (input.data.keyFeatures.isNotEmpty()) {
                    sb.appendLine("- 关键特征: ${input.data.keyFeatures.joinToString(", ")}")
                }
            }
        }
        sb.appendLine()
        
        // 标准要求
        request.standardData?.let { standard ->
            sb.appendLine("## 标准要求")
            sb.appendLine("- 适用标准: ${standard.standard.displayName}")
            standard.heightRange?.let {
                sb.appendLine("  - 身高要求: ${it.minHeight}-${it.maxHeight}cm, ${it.recommendedDirection.displayName}")
            }
            standard.weightRange?.let {
                sb.appendLine("  - 重量要求: ${it.minWeight}-${it.maxWeight}${it.unit.symbol}")
            }
            standard.dimensionalRequirements?.let {
                sb.appendLine("  - 尺寸要求:")
                it.envelopeDimensions?.let { envelope ->
                    sb.appendLine("    - i-Size包络: ${envelope.width}×${envelope.length}×${envelope.height}cm")
                }
            }
            standard.performanceRequirements?.let {
                sb.appendLine("  - 性能要求:")
                it.impactTestGForce?.let { gforce ->
                    sb.appendLine("    - 碰撞测试G-force: ${gforce.min}-${gforce.max}g")
                }
            }
            sb.appendLine()
        }
        
        // 品牌基准
        if (request.options.includeBrandComparison && request.brandData != null) {
            sb.appendLine("## 品牌基准对比")
            request.brandData.forEach { brand ->
                sb.appendLine("- ${brand.brandName} ${brand.productName}")
                brand.keyAdvantages.forEach { advantage ->
                    sb.appendLine("  - 优势: $advantage")
                }
                brand.technicalSpecs.uniqueFeatures.forEach { feature ->
                    sb.appendLine("  - 特色: $feature")
                }
            }
            sb.appendLine()
        }
        
        // 输出格式要求
        sb.appendLine("## 输出格式要求")
        sb.appendLine("请按照以下结构输出设计建议（参考示例格式）：")
        sb.appendLine()
        sb.appendLine("---")
        sb.appendLine()
        
        when (request.productType) {
            ProductType.SAFETY_SEAT -> {
                sb.appendLine("## 儿童安全座椅专业设计建议（适配${request.standard.displayName} ${request.userInput.let { input ->
                    when (input) {
                        is ProductInput.SafetySeat -> input.data.heightRange ?: "60-105cm身高组"
                        else -> ""
                    }
                }}）")
                sb.appendLine()
                sb.appendLine("### 1. 核心参数表（关联标准条款）")
                sb.appendLine("| 参数名称 | 参数值 | 单位 | 标准条款 | 说明 |")
                sb.appendLine("|---------|--------|------|---------|------|")
                sb.appendLine("| 头托调节范围 | 10-30 | cm | ECE R129 §5.4.2 | 覆盖40-150cm身高区间 |")
                sb.appendLine("| ISOFIX接口间距 | 280 | mm | GB 27887-2024 §5.5 | 允许偏差±5mm |")
                sb.appendLine("| 支撑腿长度 | 280-450 | mm | ECE R129 §5.5.3 | 可伸缩调节 |")
                sb.appendLine("| 侧翼内宽 | ≥380 | mm | ECE R129 §5.3.3 | 侧面碰撞防护最小宽度 |")
                sb.appendLine("| 座椅靠背角度（后向） | 135-150 | 度 | ECE R129 §5.4.1 | 后向安装角度范围 |")
                sb.appendLine("| 座椅靠背角度（前向） | 15-30 | 度 | ECE R129 §5.4.1 | 前向安装角度范围 |")
                sb.appendLine("| 安全带织带宽度 | 25-30 | mm | GB 6095-2021 §4.2 | 标准织带宽度 |")
                sb.appendLine("| 安全带锁止强度 | ≥5 | kN | GB 6095-2021 §4.3 | 最小承受力 |")
                sb.appendLine()
                sb.appendLine("### 2. 材料标准标注")
                sb.appendLine("| 材料名称 | 符合标准 | 标准要求 | 应用部位 |")
                sb.appendLine("|---------|---------|---------|---------|")
                sb.appendLine("| PP塑料（座椅主体） | GB 6675.4-2014 | 可迁移元素限值：铅≤90mg/kg，镉≤75mg/kg | 座椅主体、头托、底座 |")
                sb.appendLine("| EPS吸能材料 | GB/T 10801.1-2021 | 密度≥30kg/m³，抗压强度≥150kPa | 侧面防护、头部保护 |")
                sb.appendLine("| EPP吸能材料 | GB/T 10801.2-2021 | 密度≥45kg/m³，能量吸收≥30% | 侧翼、后背板 |")
                sb.appendLine("| 安全带织带 | GB 6095-2021 | 断裂强度≥5kN，延伸率≤10%（在5kN力作用下） | 五点式安全带、肩带 |")
                sb.appendLine("| 座椅面料 | GB 18401 B类 | 甲醛含量≤75mg/kg，pH值4.0-7.5 | 座椅表面、头枕套 |")
                sb.appendLine("| 阻燃面料 | GB 6675.3-2014 | 垂直燃烧速度≤100mm/min | 易燃区域面料 |")
                sb.appendLine()
                sb.appendLine("### 3. 标准合规清单")
                sb.appendLine("#### 3.1 测试矩阵")
                sb.appendLine("| 测试项 | 法规标准引用 | 测试方法 | 假人类型 | 合格标准 |")
                sb.appendLine("|--------|-------------|----------|---------|----------|")
                sb.appendLine("| 正面撞击测试 | ECE R129 §5.3.2 | 50km/h 正面撞击刚性障碍物 | Q0/Q1.5/Q3/Q6/Q10 | HIC≤324，胸部加速度≤55g |")
                sb.appendLine("| 侧面碰撞测试 | ECE R129 §5.3.3 | 50km/h 侧面撞击可变形障碍物 | Q1.5/Q3/Q6 | 头部偏移量≤150mm，颈部力≤2.5kN |")
                sb.appendLine("| 后向翻滚测试 | ECE R129 §5.3.4 | 30km/h 后向翻滚 | Q0/Q1.5 | 假人加速度≤25g，座椅无结构性变形 |")
                sb.appendLine("| ISOFIX强度测试 | GB 27887-2024 §6.2 | 施加10kN拉力至ISOFIX锚点 | 无 | ISOFIX连接件无变形，位移≤15mm |")
                sb.appendLine("| 支撑腿强度测试 | ECE R129 §5.5.3 | 施加2.5kN垂直载荷至支撑腿 | 无 | 支撑腿无断裂，偏转≤10mm |")
                sb.appendLine("| 安全带锁止测试 | GB 6095-2021 §5.2 | 施加5kN拉力至安全带 | 无 | 锁止机构无解锁，延伸率≤10% |")
                sb.appendLine()
                sb.appendLine("#### 3.2 尺寸阈值")
                sb.appendLine("| 尺寸类型 | 限制值 | 单位 | 标准条款 | 说明 |")
                sb.appendLine("|---------|--------|------|---------|------|")
                sb.appendLine("| 外宽度上限 | ≤50 | cm | ECE R129 Envelope要求 | i-Size包络宽度限制 |")
                sb.appendLine("| 外长度上限 | ≤75 | cm | ECE R129 Envelope要求 | i-Size包络长度限制 |")
                sb.appendLine("| 外高度上限 | ≤85 | cm | ECE R129 Envelope要求 | i-Size包络高度限制 |")
                sb.appendLine("| 座椅内宽 | ≥38 | cm | ECE R129 §5.2 | 座椅内部最小宽度 |")
                sb.appendLine()
                sb.appendLine("### 4. 产品配置信息")
                sb.appendLine("| 配置类型 | 配置描述 | 标准条款 | 安装要求 |")
                sb.appendLine("|---------|---------|---------|---------|")
                sb.appendLine("| ISOFIX接口 | ISOFIX硬连接接口，提供稳固的车辆固定 | ECE R129 §5.5.1/§5.5.3 | 适配带ISOFIX锚点的乘用车（2006年后欧盟车辆，2012年后中国车辆） |")
                sb.appendLine("| 可伸缩支撑腿 | 地面支撑结构，提高座椅稳定性 | ECE R129 §5.5.3 | 适配地板间隙≥5cm的车辆，支撑腿长度可调280-450mm |")
                sb.appendLine("| 顶部系带挂钩 | 上拉带连接车辆顶部系带点 | ECE R129 §5.5.1 | 适配车辆后排座椅顶部有系带锚点的车型 |")
                sb.appendLine("| 侧撞防护装置（SIP） | 可调节的侧面碰撞保护块 | ECE R129 §5.3.3 | 需根据车门间隙调整侧翼展开角度，避免影响车门关闭 |")
                sb.appendLine("| 360°旋转机构 | 座椅可360度旋转，方便抱娃进出 | ECE R129 §5.2（需通过动态测试） | 需确保旋转机构的锁止强度≥5kN |")
                sb.appendLine()
                sb.appendLine("### 5. 产品核心设计方向（专业主题）")
                sb.appendLine("根据输入的身高范围和标准，确定以下专业设计方向：")
                sb.appendLine()
                sb.appendLine("**当输入身高40-150cm时：**")
                sb.appendLine("- **主题名称**：ECE R129全分组安全适配主题（40-150cm，Group 0+/1/2/3）")
                sb.appendLine("- **覆盖年龄段**：0-12岁")
                sb.appendLine("- **推荐朝向**：后向（优先）→ 前向（根据身高切换）")
                sb.appendLine("- **核心设计细节**：")
                sb.appendLine("  - 头托自适应调节：覆盖40-150cm全身高区间（适配ECE R129 Group 0+/1/2/3对应的Q系列假人）")
                sb.appendLine("  - 双固定结构适配：可切换ISOFIX+支撑腿（满足ECE R129不同分组的安装合规性）")
                sb.appendLine("  - 分段式侧撞防护：侧撞块采用EPS+EPP双层吸能结构（符合ECE R129 §5.3.3侧撞要求）")
                sb.appendLine()
                sb.appendLine("### 6. 落地设计细节示例")
                sb.appendLine("**示例1：教育元素融入（合规前提下）**")
                sb.appendLine("- 头托侧面增加可拆洗的数字认知布贴")
                sb.appendLine("- 布贴材料符合GB 18401 B类标准")
                sb.appendLine("- 布贴厚度≤5mm，不影响侧撞防护结构的完整性")
                sb.appendLine("- 布贴位置避开关键吸能区域")
                sb.appendLine()
                sb.appendLine("**示例2：轻量化设计（不影响强度）**")
                sb.appendLine("- 底座骨架采用高强度铝合金，替代部分钢材")
                sb.appendLine("- 整体重量控制在15kg以内（满足ISOFIX承载要求）")
                sb.appendLine("- 减重区域选择在非受力部件（如装饰件）")
            }
            ProductType.CRIB -> {
                sb.appendLine("## 儿童家庭用品设计建议（适配${request.standard.displayName}）")
                sb.appendLine()
                sb.appendLine("### 1. 尺寸参数")
                sb.appendLine("- 根据具体产品类型确定尺寸参数")
            }
            ProductType.HIGH_CHAIR -> {
                sb.appendLine("## 儿童高脚椅设计建议（适配${request.standard.displayName}）")
                sb.appendLine()
                sb.appendLine("### 1. 核心设计要求")
                sb.appendLine("- 符合ISO 8124-3 & GB 28007-2011标准")
            }
            ProductType.STROLLER -> {
                sb.appendLine("## 婴儿推车设计建议（适配${request.standard.displayName} 0-15kg重量组）")
                sb.appendLine()
                sb.appendLine("### 1. 尺寸参数")
                sb.appendLine()
                sb.appendLine("#### 1.1 外部尺寸（符合EN 1888 & ASTM F833标准）")
                sb.appendLine("- 展开尺寸（高景观款）：长度105cm × 宽度60cm × 高度110cm（座高55cm，远离汽车尾气）")
                sb.appendLine("- 折叠尺寸（高景观款）：长度70cm × 宽度60cm × 高度35cm（兼容多数SUV后备箱收纳）")
                sb.appendLine("- 展开尺寸（轻便款）：长度85cm × 宽度55cm × 高度95cm（座高40cm，适配城市通勤）")
                sb.appendLine("- 折叠尺寸（轻便款）：长度50cm × 宽度30cm × 高度20cm（符合ASTM F833便携要求，可登机）")
                sb.appendLine()
                sb.appendLine("#### 1.2 内部尺寸（基于0-15kg儿童身体尺寸数据）")
                sb.appendLine("- 座宽：38cm（适配15kg儿童肩宽30cm，预留4cm活动空间）")
                sb.appendLine("- 座深：28cm（适配15kg儿童臀腿长度23cm，避免腰部悬空）")
                sb.appendLine("- 头枕高度调节范围：25-40cm（最小高度适配新生儿，最大高度适配4岁儿童）")
                sb.appendLine("- 脚踏板有效长度：20cm（适配15kg儿童小腿长度18cm）")
                sb.appendLine()
                sb.appendLine("### 2. 核心产品功能")
                sb.appendLine("- 避震功能：")
                sb.appendLine("  - 高景观款：前轮独立避震（弹簧行程3cm）+ 后轮液压避震（行程2cm），适配户外崎岖路面")
                sb.appendLine("  - 轻便款：前轮弹性橡胶避震（行程1cm），适配城市平坦路面")
                sb.appendLine("- 折叠功能：")
                sb.appendLine("  - 操作方式：一键式中央折叠，单手可完成操作")
                sb.appendLine("  - 锁定机制：双重机械锁定（折叠卡扣+安全锁），避免误解锁")
                sb.appendLine("- 安全防护功能：")
                sb.appendLine("  - 安全带：五点式安全带（肩带宽度4cm、腰带宽度3cm）")
                sb.appendLine("  - 制动系统：双后轮独立制动，脚踏式操作")
                sb.appendLine("  - 头枕调节：10档高度调节（调节范围25-40cm）")
                sb.appendLine("- 附加功能：")
                sb.appendLine("  - 靠背角度调节：110°-175°无级调节（110°坐姿、145°半躺、175°全躺）")
                sb.appendLine("  - 储物空间：底部置物篮容积≥15L，承重≥5kg")
                sb.appendLine()
                sb.appendLine("### 3. 合规测试矩阵")
                sb.appendLine("| 测试项 | 法规标准引用 | 测试方法 | 合格标准 |")
                sb.appendLine("|--------|-------------|----------|----------|")
                sb.appendLine("| 斜坡制动测试 | EN 1888 §7.3 | 在15°斜坡上锁定制动，座椅放置15kg负重，静置30min | 推车无滑动、无倾倒；制动机构无松动 |")
                sb.appendLine("| 折叠锁定可靠性测试 | ASTM F833 §5.7 | 反复折叠-展开500次 | 折叠机构无卡滞；锁定后无意外解锁 |")
            }
            ProductType.CRIB -> {
                sb.appendLine("## 儿童家庭用品设计建议")
                sb.appendLine()
                sb.appendLine("### 1. 尺寸参数")
                sb.appendLine("- 根据具体产品类型确定尺寸参数")
                sb.appendLine()
                sb.appendLine("### 2. 核心产品功能")
                sb.appendLine("- 根据具体产品类型确定功能特性")
                sb.appendLine()
                sb.appendLine("### 3. 合规测试矩阵")
                sb.appendLine("| 测试项 | 法规标准引用 | 测试方法 | 合格标准 |")
                sb.appendLine("|--------|-------------|----------|----------|")
                sb.appendLine("| 材料阻燃性测试 | ISO 13209 | 垂直燃烧测试 | 燃烧速率 < 100mm/min |")
                sb.appendLine("| 重金属含量测试 | GB 6675 | ICP-MS检测 | 符合GB 6675限值 |")
            }
            ProductType.HIGH_CHAIR -> {
                sb.appendLine("## 儿童高脚椅设计建议（适配ISO 8124-3 & GB 28007-2011）")
                sb.appendLine()
                sb.appendLine("### 1. 尺寸参数")
                sb.appendLine()
                sb.appendLine("#### 1.1 外部尺寸（符合ISO 8124-3标准）")
                sb.appendLine("- 整体高度：75-95cm（根据年龄段可调节）")
                sb.appendLine("- 座面高度：45-65cm（适配标准餐桌高度）")
                sb.appendLine("- 座面宽度：35-40cm（确保儿童舒适坐姿）")
                sb.appendLine("- 座面深度：30-35cm（提供良好支撑）")
                sb.appendLine()
                sb.appendLine("#### 1.2 内部尺寸（基于3-6岁儿童身体尺寸）")
                sb.appendLine("- 座宽：32cm（适配儿童肩宽）")
                sb.appendLine("- 座深：28cm（避免腿部悬空）")
                sb.appendLine("- 背靠高度：40-50cm（提供背部支撑）")
                sb.appendLine("- 脚踏板调节范围：15-35cm（适应不同身高儿童）")
                sb.appendLine()
                sb.appendLine("### 2. 核心产品功能")
                sb.appendLine("- 高度调节功能：")
                sb.appendLine("  - 调节方式：多档位高度调节，支持快速调整")
                sb.appendLine("  - 调节范围：20cm（至少5档调节）")
                sb.appendLine("- 安全防护功能：")
                sb.appendLine("  - 安全带：三点式或五点式安全带，防止儿童滑落")
                sb.appendLine("  - 稳定性：四脚结构，底部防滑垫，防止滑动")
                sb.appendLine("  - 托盘：可拆卸托盘，边缘防溢设计")
                sb.appendLine()
                sb.appendLine("### 3. 合规测试矩阵")
                sb.appendLine("| 测试项 | 法规标准引用 | 测试方法 | 合格标准 |")
                sb.appendLine("|--------|-------------|----------|----------|")
                sb.appendLine("| 稳定性测试 | ISO 8124-3 | 倾斜测试 | 倾斜角度<10° |")
                sb.appendLine("| 强度测试 | GB 28007 | 静载测试 | 无明显变形 |")
            }
            else -> {
                sb.appendLine("## 产品设计建议（适配${request.standard.displayName}）")
                sb.appendLine()
                sb.appendLine("请根据具体产品类型参考相关标准进行设计。")
            }
        }
        
        sb.appendLine()
        sb.appendLine("---")
        sb.appendLine()
        
        // 指导原则
        sb.appendLine("## 指导原则")
        sb.appendLine("1. 所有建议必须基于国际标准和行业最佳实践")
        sb.appendLine("2. 优先考虑儿童安全，避免任何潜在风险")
        sb.appendLine("3. 提供具体、可量化的参数（数值范围）")
        sb.appendLine("4. 参考头部品牌（Britax、Maxi-Cosi、Cybex、UPPAbaby等）的设计理念")
        sb.appendLine("5. 针对用户具体需求提供差异化建议")
        sb.appendLine("6. 确保建议的技术可行性和成本合理性")
        sb.appendLine("7. 详细说明每个参数的设计理由和标准依据")
        sb.appendLine()
        
        return sb.toString()
    }
    
    /**
     * 选择模型
     */
    private fun selectModel(detailLevel: DetailLevel): String {
        return when (detailLevel) {
            DetailLevel.BRIEF -> "zhipu_glm4_flash"
            DetailLevel.STANDARD -> "qwen_plus"
            DetailLevel.DETAILED -> "doubao_pro"
            DetailLevel.COMPREHENSIVE -> "deepseek_chat"
        }
    }
    
    /**
     * 调用LLM API
     */
    private suspend fun callLLM(model: String, prompt: String): LLMResponse = withContext(Dispatchers.IO) {
        // 这里应该集成真实的LLM API调用
        // 当前使用模拟响应作为示例
        
        // 模拟网络延迟
        kotlinx.coroutines.delay(1000)
        
        LLMResponse(
            text = generateMockResponse(prompt),
            model = model,
            tokensUsed = 1500
        )
    }
    
    /**
     * 生成模拟响应（用于演示）
     */
    private fun generateMockResponse(prompt: String): String {
        val response = JSONObject()
        
        val designSuggestions = JSONObject()
        val functionalFeatures = JSONArray()
        
        val feature1 = JSONObject()
        feature1.put("category", "头托调节")
        feature1.put("description", "多档调节头托高度以适应不同身高儿童")
        feature1.put("recommendation", "建议采用10-30cm调节范围，分15档调节，参考Britax头托设计")
        feature1.put("referenceStandard", "ECE R129 i-Size")
        feature1.put("implementationDifficulty", "MODERATE")
        functionalFeatures.put(feature1)
        
        val feature2 = JSONObject()
        feature2.put("category", "侧面碰撞防护")
        feature2.put("description", "增强侧面碰撞时的儿童头部和颈部保护")
        feature2.put("recommendation", "参考Cybex侧面防护系统，采用LSP可调节侧面碰撞保护装置")
        feature2.put("referenceStandard", "ECE R129")
        feature2.put("implementationDifficulty", "DIFFICULT")
        functionalFeatures.put(feature2)
        
        designSuggestions.put("functionalFeatures", functionalFeatures)
        
        val dimensionParameters = JSONObject()
        val externalDimensions = JSONObject()
        val width = JSONObject()
        width.put("recommendedValue", JSONArray(listOf(44.0, 44.0)))
        width.put("unit", "cm")
        width.put("rationale", "符合i-Size Envelope宽度限制")
        externalDimensions.put("width", width)
        
        val height = JSONObject()
        height.put("recommendedValue", JSONArray(listOf(70.0, 81.0)))
        height.put("unit", "cm")
        height.put("rationale", "符合i-Size Envelope高度限制，确保安装空间")
        externalDimensions.put("height", height)
        
        dimensionParameters.put("externalDimensions", externalDimensions)
        designSuggestions.put("dimensionParameters", dimensionParameters)
        
        designSuggestions.put("materialRecommendations", JSONArray())
        designSuggestions.put("safetyRecommendations", JSONArray())
        
        response.put("designSuggestions", designSuggestions)
        
        return response.toString()
    }
    
    /**
     * 解析LLM响应
     */
    private fun parseLLMResponse(
        response: LLMResponse,
        request: AIGenerationRequest
    ): ParsedSuggestions {
        // 这里应该解析真实的LLM响应
        // 当前返回模拟数据
        
        val suggestions = DesignSuggestions(
            productType = request.productType,
            functionalFeatures = emptyList(),
            dimensionParameters = DimensionParameters(
                externalDimensions = ExternalDimensionSuggestion(
                    width = DimensionRecommendation(DoubleRange(44.0, 44.0), "cm", "符合i-Size", "ECE R129"),
                    length = DimensionRecommendation(DoubleRange(70.0, 75.0), "cm", "符合i-Size", "ECE R129"),
                    height = DimensionRecommendation(DoubleRange(70.0, 81.0), "cm", "符合i-Size", "ECE R129"),
                    weight = DimensionRecommendation(DoubleRange(12.0, 15.0), "kg", "优化重量", null),
                    notes = "参考Britax Dualfix M i-Size"
                ),
                internalDimensions = InternalDimensionSuggestion(
                    seatDepth = DimensionRecommendation(DoubleRange(40.0, 45.0), "cm", "适配CRABI假人", "ECE R129"),
                    seatWidth = DimensionRecommendation(DoubleRange(38.0, 40.0), "cm", "确保舒适性", null),
                    backrestHeight = DimensionRecommendation(DoubleRange(55.0, 60.0), "cm", "提供支撑", null),
                    headrestWidth = DimensionRecommendation(DoubleRange(35.0, 38.0), "cm", "侧面防护", "ECE R129"),
                    shoulderWidth = DimensionRecommendation(DoubleRange(32.0, 35.0), "cm", "适配儿童体型", null),
                    notes = "参考Maxi-Cosi设计"
                ),
                adjustmentRanges = listOf(
                    AdjustmentRange("头托高度", 10.0, 30.0, "cm", 15, "参考Britax 15档调节"),
                    AdjustmentRange("靠背角度", -15.0, 30.0, "度", 5, "参考Cybex倾斜设计")
                ),
                envelopeCompliance = EnvelopeCompliance(
                    isCompliant = true,
                    widthComparison = ComparisonResult(44.0, 44.0, true, 0.0, "cm"),
                    lengthComparison = ComparisonResult(73.0, 75.0, true, 2.0, "cm"),
                    heightComparison = ComparisonResult(79.0, 81.0, true, 2.0, "cm"),
                    recommendations = listOf("完全符合i-Size Envelope要求")
                )
            ),
            materialRecommendations = listOf(
                MaterialRecommendation(
                    "座椅表面",
                    "透气网眼布",
                    listOf("透气性好", "易清洁", "耐磨"),
                    SafetyRating.GOOD,
                    listOf("华峰集团", "台达化学")
                ),
                MaterialRecommendation(
                    "吸能材料",
                    "EPS发泡材料",
                    listOf("吸能性能优异", "轻量化", "阻燃"),
                    SafetyRating.EXCELLENT,
                    listOf("巴斯夫", "陶氏化学")
                )
            ),
            safetyRecommendations = listOf(
                SafetyRecommendation(
                    "侧面碰撞防护",
                    "在座椅两侧安装可调节的LSP装置",
                    "采用可伸缩的侧面保护块，调节范围为0-5cm",
                    "ECE R129侧面碰撞测试",
                    Priority.CRITICAL
                ),
                SafetyRecommendation(
                    "安全带调节",
                    "采用单手可调节安全带系统",
                    "设计中央调节按钮，支持单手操作调节高度",
                    "FMVSS 213安全带拉伸测试",
                    Priority.HIGH
                )
            )
        )
        
        return ParsedSuggestions(
            designSuggestions = suggestions,
            brandComparison = null,
            dvpTestMatrix = generateDefaultDVPTestMatrix(request.productType, request.standard),
            standardCompliance = generateDefaultStandardCompliance(request.standard)
        )
    }
    
    /**
     * 生成默认DVP测试项
     */
    private fun generateDefaultDVPTestItems(
        productType: ProductType,
        standard: InternationalStandard
    ): List<DVPTestItem> {
        val testItems = mutableListOf<DVPTestItem>()
        
        when (productType) {
            ProductType.SAFETY_SEAT -> {
                testItems.addAll(listOf(
                    DVPTestItem(
                        testId = "IMP-001",
                        testCategory = TestCategory.IMPACT_TESTING,
                        testName = "正面碰撞测试",
                        standardReference = "ECE R129 Annex 8",
                        testMethod = "使用Hybrid III 3岁假人，50km/h正面碰撞",
                        acceptanceCriteria = "G-force < 50g, 头部伤害指标 < 1000",
                        testEquipment = "碰撞测试台，Hybrid III假人",
                        sampleSize = 5,
                        estimatedDuration = "2-3小时",
                        priority = TestPriority.MANDATORY,
                        notes = "必须测试"
                    ),
                    DVPTestItem(
                        testId = "IMP-002",
                        testCategory = TestCategory.IMPACT_TESTING,
                        testName = "侧面碰撞测试",
                        standardReference = "ECE R129 Annex 8",
                        testMethod = "使用Q3s假人，24km/h侧面碰撞",
                        acceptanceCriteria = "G-force < 50g, 头部位移 < 550mm",
                        testEquipment = "碰撞测试台，Q3s假人",
                        sampleSize = 5,
                        estimatedDuration = "2-3小时",
                        priority = TestPriority.MANDATORY,
                        notes = "必须测试"
                    ),
                    DVPTestItem(
                        testId = "DIM-001",
                        testCategory = TestCategory.FUNCTIONAL_TESTING,
                        testName = "包络尺寸合规性测试",
                        standardReference = "ECE R129 Annex 7",
                        testMethod = "测量座椅在所有调节位置的外部尺寸",
                        acceptanceCriteria = "宽度≤44cm, 长度≤75cm, 高度≤81cm",
                        testEquipment = "三维测量仪",
                        sampleSize = 1,
                        estimatedDuration = "30分钟",
                        priority = TestPriority.MANDATORY,
                        notes = "i-Size强制要求"
                    )
                ))
            }
            ProductType.STROLLER -> {
                testItems.addAll(listOf(
                    DVPTestItem(
                        testId = "BRK-001",
                        testCategory = TestCategory.SAFETY_TESTING,
                        testName = "制动性能测试",
                        standardReference = "EN 1888",
                        testMethod = "在10°斜坡上制动，检查是否滑动",
                        acceptanceCriteria = "无滑动，保持稳定",
                        testEquipment = "斜坡测试台",
                        sampleSize = 3,
                        estimatedDuration = "1小时",
                        priority = TestPriority.MANDATORY,
                        notes = "必须测试"
                    ),
                    DVPTestItem(
                        testId = "FLD-001",
                        testCategory = TestCategory.DURABILITY_TESTING,
                        testName = "折叠机构耐久性测试",
                        standardReference = "EN 1888",
                        testMethod = "重复折叠展开1000次",
                        acceptanceCriteria = "无故障，机构正常",
                        testEquipment = "折叠测试机",
                        sampleSize = 3,
                        estimatedDuration = "8小时",
                        priority = TestPriority.CRITICAL,
                        notes = "关键测试"
                    ),
                    DVPTestItem(
                        testId = "SHK-001",
                        testCategory = TestCategory.IMPACT_TESTING,
                        testName = "避震性能测试",
                        standardReference = "EN 1888",
                        testMethod = "通过颠簸路面，测量加速度",
                        acceptanceCriteria = "加速度 < 3g",
                        testEquipment = "震动测试台",
                        sampleSize = 3,
                        estimatedDuration = "2小时",
                        priority = TestPriority.IMPORTANT,
                        notes = "推荐测试"
                    )
                ))
            }
            ProductType.CRIB -> {
                testItems.addAll(listOf(
                    DVPTestItem(
                        testId = "MAT-001",
                        testCategory = TestCategory.MATERIAL_TESTING,
                        testName = "材料阻燃性测试",
                        standardReference = "ISO 13209",
                        testMethod = "垂直燃烧测试",
                        acceptanceCriteria = "燃烧速率 < 100mm/min",
                        testEquipment = "阻燃测试仪",
                        sampleSize = 5,
                        estimatedDuration = "1小时",
                        priority = TestPriority.MANDATORY,
                        notes = "必须测试"
                    ),
                    DVPTestItem(
                        testId = "CHM-001",
                        testCategory = TestCategory.CHEMICAL_TESTING,
                        testName = "重金属含量测试",
                        standardReference = "GB 6675",
                        testMethod = "ICP-MS检测",
                        acceptanceCriteria = "符合GB 6675限值",
                        testEquipment = "ICP-MS",
                        sampleSize = 1,
                        estimatedDuration = "4小时",
                        priority = TestPriority.MANDATORY,
                        notes = "必须测试"
                    )
                ))
            }
            ProductType.HIGH_CHAIR -> {
                testItems.addAll(listOf(
                    DVPTestItem(
                        testId = "STB-001",
                        testCategory = TestCategory.SAFETY_TESTING,
                        testName = "稳定性测试",
                        standardReference = "ISO 8124-3",
                        testMethod = "倾斜测试，检查倾倒角度",
                        acceptanceCriteria = "倾斜角度 < 10°",
                        testEquipment = "倾斜测试台",
                        sampleSize = 3,
                        estimatedDuration = "1小时",
                        priority = TestPriority.MANDATORY,
                        notes = "必须测试"
                    ),
                    DVPTestItem(
                        testId = "STR-001",
                        testCategory = TestCategory.DURABILITY_TESTING,
                        testName = "强度测试",
                        standardReference = "GB 28007",
                        testMethod = "静载测试，施加100kg负荷",
                        acceptanceCriteria = "无明显变形，结构完好",
                        testEquipment = "强度测试机",
                        sampleSize = 3,
                        estimatedDuration = "2小时",
                        priority = TestPriority.CRITICAL,
                        notes = "关键测试"
                    ),
                    DVPTestItem(
                        testId = "HLT-001",
                        testCategory = TestCategory.FUNCTIONAL_TESTING,
                        testName = "高度调节测试",
                        standardReference = "ISO 8124-3",
                        testMethod = "测试高度调节机构的可靠性",
                        acceptanceCriteria = "调节顺畅，无卡滞，锁定可靠",
                        testEquipment = "高度测试机",
                        sampleSize = 5,
                        estimatedDuration = "1小时",
                        priority = TestPriority.IMPORTANT,
                        notes = "推荐测试"
                    )
                ))
            }
            else -> {
                // 添加默认测试项
                testItems.add(
                    DVPTestItem(
                        testId = "GEN-001",
                        testCategory = TestCategory.SAFETY_TESTING,
                        testName = "通用安全测试",
                        testMethod = "参照相关标准进行测试",
                        acceptanceCriteria = "符合相关标准要求",
                        testEquipment = "通用测试设备",
                        sampleSize = 1,
                        estimatedDuration = "2小时",
                        priority = TestPriority.MANDATORY,
                        notes = "必须测试"
                    )
                )
            }
        }
        
        return testItems
    }
    
    /**
     * LLM响应
     */
    private data class LLMResponse(
        val text: String,
        val model: String,
        val tokensUsed: Int
    )
    
    /**
     * 解析后的建议
     */
    private data class ParsedSuggestions(
        val designSuggestions: DesignSuggestions,
        val brandComparison: BrandComparison?,
        val dvpTestMatrix: DVPTestMatrix,
        val standardCompliance: StandardCompliance?
    )
    
    private fun generateDefaultDVPTestMatrix(
        productType: ProductType,
        standard: InternationalStandard
    ): DVPTestMatrix {
        return DVPTestMatrix(
            productType = productType,
            standard = standard,
            testItems = generateDefaultDVPTestItems(productType, standard)
        )
    }
    
    private fun generateDefaultStandardCompliance(
        standard: InternationalStandard
    ): StandardCompliance {
        return StandardCompliance(
            standard = standard,
            complianceItems = emptyList(),
            overallCompliance = ComplianceStatus.FULLY_COMPLIANT,
            recommendations = listOf("建议通过完整的认证测试")
        )
    }
}

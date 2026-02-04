package com.childproduct.designassistant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 材料规格实体
 * 基于UN R118.03燃烧性能要求和材料标准（Rev.5，2022版）
 */
@Entity(tableName = "material_specification")
data class MaterialSpecification(
    @PrimaryKey val specId: String,              // SPEC_FLAME_RETARDANT
    val specName: String,                       // "阻燃面料"
    val specCode: String,                       // "FLAME_RETARDANT"
    val description: String,                    // 详细描述
    val standardRequirement: String,            // "FMVSS 302: 燃烧速度 < 4英寸/分钟"
    val testMethod: String,                     // "FMVSS 302"
    val applicableComponents: List<String>,     // ["座椅套", "垫子", "布料"]
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        val STANDARD_SPECIFICATIONS = listOf(
            MaterialSpecification(
                specId = "SPEC_FLAME_RETARDANT",
                specName = "阻燃面料",
                specCode = "FLAME_RETARDANT",
                description = "通过UN R118.03认证的阻燃面料",
                standardRequirement = "UN R118.03: 水平燃烧速度 ≤ 100mm/min，垂直燃烧自熄时间 ≤ 10s",
                testMethod = "UN R118.03 Annex 5, Annex 6",
                applicableComponents = listOf("座椅套", "垫子", "布料", "肩带")
            ),
            MaterialSpecification(
                specId = "SPEC_ISO_FIX_CONNECTOR",
                specName = "ISOFIX连接件",
                specCode = "ISOFIX_CONNECTOR",
                description = "高强度钢材ISOFIX连接件",
                standardRequirement = "抗拉强度 >= 450MPa",
                testMethod = "ISO 12217-1",
                applicableComponents = listOf("ISOFIX连接器", "连接臂")
            ),
            MaterialSpecification(
                specId = "SPEC_SUPPORT_LEG",
                specName = "支撑腿",
                specCode = "SUPPORT_LEG",
                description = "可调节支撑腿",
                standardRequirement = "长度范围: 285-540mm",
                testMethod = "UN R129 §6.1.2.2",
                applicableComponents = listOf("支撑腿")
            ),
            MaterialSpecification(
                specId = "SPEC_TOP_TETHER",
                specName = "顶部系带",
                specCode = "TOP_TETHER",
                description = "可调节顶部系带",
                standardRequirement = "最小断裂强度: 10kN",
                testMethod = "UN R129 §6.1.3",
                applicableComponents = listOf("顶部系带", "调节器")
            ),
            MaterialSpecification(
                specId = "SPEC_HARNESS_WEBBING",
                specName = "安全带织带",
                specCode = "HARNESS_WEBBING",
                description = "高强度安全带织带",
                standardRequirement = "最小断裂强度: 4.5kN",
                testMethod = "ISO 3376",
                applicableComponents = listOf("安全带", "肩带", "腰带")
            )
        )
    }
}

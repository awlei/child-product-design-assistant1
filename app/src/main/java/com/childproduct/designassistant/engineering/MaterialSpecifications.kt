package com.childproduct.designassistant.engineering

/**
 * 材料规格
 * 基于 Dorel GPS 规范
 */
data class MaterialSpecifications(
    val shellMaterial: String,        // 外壳材料
    val foamDensity: String,          // 泡沫密度
    val harnessWebbing: String,       // 安全带织带
    val isofixHardware: String       // ISOFIX 硬件
)

package com.childproduct.designassistant.service

import android.content.Context
import com.childproduct.designassistant.data.GlobalRegulationLibrary
import com.childproduct.designassistant.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * 法规更新监测模块
 * 实时同步新规动态并提示设计适配建议
 */
class RegulationUpdateMonitor(private val context: Context) {

    private val _updates = MutableStateFlow<List<RegulationUpdate>>(emptyList())
    val updates: StateFlow<List<RegulationUpdate>> = _updates

    /**
     * 检查法规更新
     */
    suspend fun checkUpdates(): List<RegulationUpdate> {
        // 模拟从服务器获取更新
        val updates = listOf(
            RegulationUpdate(
                id = "UPDATE_20240129_001",
                regulationCode = "ECE R129",
                regulationName = "关于儿童约束系统审批的统一规定",
                version = "2024修订版",
                publishDate = "2024-01-15",
                updateType = UpdateType.MINOR_UPDATE,
                summary = "更新了侧面碰撞测试要求，增加了新的假人类型",
                changes = listOf(
                    RegulationChange(
                        sectionId = "§5.3.3",
                        sectionTitle = "侧面碰撞测试",
                        changeType = ChangeType.MODIFIED,
                        oldContent = "使用Q3s假人，碰撞速度32km/h",
                        newContent = "使用Q3.5假人，碰撞速度32km/h，增加头部加速度监测"
                    ),
                    RegulationChange(
                        sectionId = "§5.5.2",
                        sectionTitle = "侧面防撞块测试",
                        changeType = ChangeType.ADDED,
                        oldContent = null,
                        newContent = "新增侧面防撞块强度测试要求"
                    )
                ),
                adaptationSuggestion = "建议更新侧面防护结构设计，增加侧面防撞块强度",
                urgency = Urgency.MEDIUM,
                actionRequired = true
            ),
            RegulationUpdate(
                id = "UPDATE_20240128_001",
                regulationCode = "GB 27887",
                regulationName = "机动车儿童乘员用约束系统",
                version = "GB 27887-2025（草案）",
                publishDate = "2024-01-20",
                updateType = UpdateType.MAJOR_UPDATE,
                summary = "新增对增高垫的详细要求，修改了碰撞测试假人",
                changes = listOf(
                    RegulationChange(
                        sectionId = "§5.4",
                        sectionTitle = "增高垫要求",
                        changeType = ChangeType.ADDED,
                        oldContent = null,
                        newContent = "新增增高垫的具体技术要求，包括高度调节、靠背支撑等"
                    ),
                    RegulationChange(
                        sectionId = "§5.1",
                        sectionTitle = "正面碰撞测试",
                        changeType = ChangeType.MODIFIED,
                        oldContent = "使用Hybrid III 3岁假人",
                        newContent = "使用Hybrid III 3岁假人或Q3假人"
                    )
                ),
                adaptationSuggestion = "如设计产品包含增高垫，需新增相应技术要求",
                urgency = Urgency.HIGH,
                actionRequired = true
            )
        )

        _updates.value = updates
        return updates
    }

    /**
     * 标记更新已读
     */
    fun markAsRead(updateId: String) {
        val currentUpdates = _updates.value.toMutableList()
        val index = currentUpdates.indexOfFirst { it.id == updateId }
        if (index != -1) {
            currentUpdates[index] = currentUpdates[index].copy(isRead = true)
            _updates.value = currentUpdates
        }
    }

    /**
     * 获取未读更新
     */
    fun getUnreadUpdates(): List<RegulationUpdate> {
        return _updates.value.filter { !it.isRead }
    }

    /**
     * 根据法规代码获取更新
     */
    fun getUpdatesByRegulationCode(code: String): List<RegulationUpdate> {
        return _updates.value.filter { it.regulationCode == code }
    }
}

/**
 * 法规更新
 */
data class RegulationUpdate(
    val id: String,
    val regulationCode: String,
    val regulationName: String,
    val version: String,
    val publishDate: String,
    val updateType: UpdateType,
    val summary: String,
    val changes: List<RegulationChange>,
    val adaptationSuggestion: String,
    val urgency: Urgency,
    val actionRequired: Boolean,
    val isRead: Boolean = false
)

/**
 * 更新类型
 */
enum class UpdateType(val displayName: String) {
    MAJOR_UPDATE("重大更新"),
    MINOR_UPDATE("次要更新"),
    CORRECTION("勘误"),
    WITHDRAWAL("废止")
}

/**
 * 法规变更
 */
data class RegulationChange(
    val sectionId: String,
    val sectionTitle: String,
    val changeType: ChangeType,
    val oldContent: String?,
    val newContent: String?
)

/**
 * 变更类型
 */
enum class ChangeType(val displayName: String) {
    ADDED("新增"),
    MODIFIED("修改"),
    DELETED("删除"),
    REPLACED("替换")
}

/**
 * 紧急程度
 */
enum class Urgency(val displayName: String, val level: Int) {
    LOW("低", 1),
    MEDIUM("中", 2),
    HIGH("高", 3),
    CRITICAL("紧急", 4)
}

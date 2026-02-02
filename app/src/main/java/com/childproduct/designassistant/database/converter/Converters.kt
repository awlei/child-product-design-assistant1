package com.childproduct.designassistant.database.converter

import androidx.room.TypeConverter
import com.childproduct.designassistant.model.InstallDirection
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room数据库类型转换器
 * 用于处理复杂数据类型的存储
 */
class Converters {

    private val gson = Gson()

    /**
     * InstallDirection 转换器
     */
    @TypeConverter
    fun fromInstallDirection(direction: InstallDirection): String {
        return direction.name
    }

    @TypeConverter
    fun toInstallDirection(value: String): InstallDirection {
        return try {
            InstallDirection.valueOf(value)
        } catch (e: IllegalArgumentException) {
            InstallDirection.REARWARD
        }
    }

    /**
     * String List 转换器
     */
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isBlank()) {
            emptyList()
        } else {
            try {
                val listType = object : TypeToken<List<String>>() {}.type
                gson.fromJson(value, listType) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}

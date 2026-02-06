package com.design.assistant

import android.app.Application
import com.design.assistant.database.databases.ChildSeatStandardDatabase
import com.design.assistant.database.databases.GPS028Database
import com.design.assistant.database.databases.ProductStandardDatabase

/**
 * 应用程序入口
 */
class DesignAssistantApp : Application() {

    companion object {
        @Volatile
        private var instance: DesignAssistantApp? = null

        fun getInstance(): DesignAssistantApp {
            return instance ?: synchronized(this) {
                instance ?: DesignAssistantApp().also { instance = it }
            }
        }
    }

    // 数据库实例
    lateinit var gps028Database: GPS028Database
        private set

    lateinit var childSeatStandardDatabase: ChildSeatStandardDatabase
        private set

    lateinit var productStandardDatabase: ProductStandardDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        // 初始化数据库
        initializeDatabases()
    }

    private fun initializeDatabases() {
        gps028Database = GPS028Database.getInstance(this)
        childSeatStandardDatabase = ChildSeatStandardDatabase.getInstance(this)
        productStandardDatabase = ProductStandardDatabase.getInstance(this)
    }
}

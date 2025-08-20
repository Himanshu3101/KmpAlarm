package com.himanshu.alarm.data.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.himanshu.alarm.db.AlarmDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class DatabaseDriverFactory(private val context: Context) {
    @Suppress("RedundantSuspendModifier")
    actual suspend fun createDriver(dbName:String) : SqlDriver = withContext(Dispatchers.IO) {
        AndroidSqliteDriver(AlarmDb.Schema, context, dbName)
    }
}
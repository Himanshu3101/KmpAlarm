package com.himanshu.alarm.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.himanshu.alarm.db.AlarmDb

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(dbName: String): SqlDriver{
        return NativeSqliteDriver(AlarmDb.Schema, dbName)
    }
}
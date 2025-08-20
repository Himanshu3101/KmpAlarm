package com.himanshu.alarm.data.db

import app.cash.sqldelight.db.SqlDriver
import com.himanshu.alarm.db.AlarmDb

object DatabaseBuilder{
    fun create(driver: SqlDriver): AlarmDb = AlarmDb.Companion(driver)
}
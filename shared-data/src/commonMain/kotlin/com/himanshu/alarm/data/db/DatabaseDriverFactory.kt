package com.himanshu.alarm.data.db

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory{
    suspend fun createDriver(dbName:String = "alarm.db") : SqlDriver
}
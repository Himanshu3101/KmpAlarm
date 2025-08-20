package com.himanshu.alarm.data.di

import com.himanshu.alarm.data.db.DatabaseBuilder
import com.himanshu.alarm.data.db.DatabaseDriverFactory
import com.himanshu.alarm.data.local.AlarmDao
import com.himanshu.alarm.data.repo.AlarmRepositoryImpl
import com.himanshu.alarm.data.repo.SoundRepositoryImpl
import com.himanshu.alarm.db.AlarmDb
import com.himanshu.alarm.domain.repository.AlarmRepository
import com.himanshu.alarm.domain.repository.SoundRepository
import org.koin.dsl.module


// We pass in a DatabaseDriverFactory from the platform layer.
fun dataModule(factory: DatabaseDriverFactory) = module {
    single { factory }
    single {
        val driver = get<DatabaseDriverFactory>().createDriver()
        DatabaseBuilder.create(driver)
    }
    single { AlarmDao(get<AlarmDb>()) }

    single<AlarmRepository> { AlarmRepositoryImpl(db = get(), alarmDao = get()) }
    single<SoundRepository> { SoundRepositoryImpl(db = get()) }

    // TODO next steps: add SleepRepositoryImpl, ReportRepositoryImpl, etc.
}
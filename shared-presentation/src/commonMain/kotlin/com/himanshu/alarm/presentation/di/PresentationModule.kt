package com.himanshu.alarm.presentation.di

import com.himanshu.alarm.presentation.list.AlarmListViewModel
import org.koin.dsl.module

val presentationModule = module{
    // no special DSL; use factory since it is a plain Kotlin VM
    factory { AlarmListViewModel(repo = get()) }
}
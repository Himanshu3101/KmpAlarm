package com.himanshu.alarm.presentation.di

import com.himanshu.alarm.presentation.alarm.AlarmListViewModel
import org.koin.dsl.module

//Koin module for presentation
//Why: Presentation depends on domain interfaces and is wired by DI—no platform code.
//Provide the ViewModel as a singleton (or factory if you want a new one each time).

val presentationModule = module{

    // no special DSL; use factory since it is a plain Kotlin VM
//    factory { AlarmListViewModel(repo = get()) }
                        /*OR*/
    single { AlarmListViewModel(repo = get()) }
}
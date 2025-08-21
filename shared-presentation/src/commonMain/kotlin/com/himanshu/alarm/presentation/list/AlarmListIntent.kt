package com.himanshu.alarm.presentation.list

sealed interface AlarmListIntent{
    data object Refresh : AlarmListIntent
    data class ToggleEnabled(val id: Long, val enabled: Boolean): AlarmListIntent
    data object AddDummy : AlarmListIntent
}
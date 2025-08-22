package com.himanshu.alarm.presentation.alarm

import com.himanshu.alarm.domain.entity.AlarmId

//Why: Keeping intents explicit makes UI testable and the VM platform-agnostic.

sealed interface AlarmListIntent{
    data object Refresh : AlarmListIntent
    data class ToggleEnabled(val alarmId: AlarmId, val enabled: Boolean): AlarmListIntent
    data object AddDummy : AlarmListIntent
}
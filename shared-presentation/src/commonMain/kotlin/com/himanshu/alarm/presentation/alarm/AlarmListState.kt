package com.himanshu.alarm.presentation.alarm

import com.himanshu.alarm.domain.entity.Alarm

data class AlarmListState(
    val isLoading : Boolean = true,
    val alarms:List<Alarm> = emptyList(),
    val error:String? = null
)
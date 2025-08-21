package com.himanshu.alarm.presentation.list

import com.himanshu.alarm.domain.entity.Alarm

data class AlarmListState(
    val isLoading: Boolean = true,
    val items:List<Alarm> = emptyList(),
    val error:String? = null
)
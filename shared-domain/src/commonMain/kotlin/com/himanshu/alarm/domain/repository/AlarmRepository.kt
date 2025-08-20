package com.himanshu.alarm.domain.repository

import com.himanshu.alarm.domain.entity.Alarm
import com.himanshu.alarm.domain.entity.AlarmId
import com.himanshu.alarm.domain.entity.AlarmMissionLink
import com.himanshu.alarm.domain.entity.MissionId
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun observeAll():Flow<List<Alarm>>
    suspend fun get(id : AlarmId): Alarm?
    suspend fun upsert(alarm: Alarm): AlarmId
    suspend fun setEnabled(id: AlarmId, enabled: Boolean)
    suspend fun delete(id: AlarmId)

    suspend fun linkMission(alarmId: AlarmId, missionId: MissionId, required: Boolean)
    suspend fun unlinkMission(alarmId: AlarmId, missionId: MissionId)
    suspend fun missionFor(alarmId: AlarmId): List<AlarmMissionLink>
}

//Later, :shared-data will implement these using the SQLDelight-generated APIs.
//:shared-presentation ViewModels depend only on these interfaces.
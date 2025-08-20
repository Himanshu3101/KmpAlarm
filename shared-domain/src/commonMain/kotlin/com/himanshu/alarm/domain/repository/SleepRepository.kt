package com.himanshu.alarm.domain.repository

import com.himanshu.alarm.domain.entity.AlarmId
import com.himanshu.alarm.domain.entity.SessionId
import com.himanshu.alarm.domain.entity.SnoreSegment

interface SleepRepository {
    suspend fun startSession(alarmId: AlarmId): SessionId
    suspend fun endSession(id: SessionId)
    suspend fun addEvent(sessionId: SessionId, tsEpochSec: Long, kind:String)
    suspend fun addSnoreSegment(seg: SnoreSegment)
    suspend fun segmentFor(sessionId: SessionId): List<SnoreSegment>
}

//Later, :shared-data will implement these using the SQLDelight-generated APIs.
//:shared-presentation ViewModels depend only on these interfaces.
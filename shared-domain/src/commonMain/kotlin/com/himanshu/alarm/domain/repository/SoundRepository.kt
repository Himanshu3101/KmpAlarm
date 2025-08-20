package com.himanshu.alarm.domain.repository

import com.himanshu.alarm.domain.entity.Sound
import com.himanshu.alarm.domain.entity.SoundId

interface SoundRepository {
    suspend fun all(): List<Sound>
    suspend fun get(id: SoundId): Sound?
    suspend fun upsert(sound: Sound): SoundId
    suspend fun delete(id: SoundId)
}

//Later, :shared-data will implement these using the SQLDelight-generated APIs.
//:shared-presentation ViewModels depend only on these interfaces.
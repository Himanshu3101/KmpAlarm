package com.himanshu.alarm.data.repo

import com.himanshu.alarm.db.AlarmDb
import com.himanshu.alarm.domain.entity.*
import com.himanshu.alarm.domain.repository.SoundRepository

class SoundRepositoryImpl(
    private val db: AlarmDb
) : SoundRepository {

    override suspend fun all(): List<Sound> =
        db.soundQueries.selectAll().executeAsList().map {
            Sound(
                id = SoundId(it.id),
                name = it.name,
                uri = it.uri,
                assetKey = it.asset_key,
                isExtraLoud = it.is_extra_loud == 1L
            )
        }

    override suspend fun get(id: SoundId): Sound? =
        db.soundQueries.selectById(id.value).executeAsOneOrNull()?.let {
            Sound(SoundId(it.id), it.name, it.uri, it.asset_key, it.is_extra_loud == 1L)
        }

    override suspend fun upsert(sound: Sound): SoundId {
        return if (sound.id.value == 0L) {
            db.soundQueries.insert(
                name = sound.name,
                uri = sound.uri,
                asset_key = sound.assetKey,
                is_extra_loud = if (sound.isExtraLoud) 1 else 0
            )
            // NOTE: selectLastInsertId is defined in sound.sq, so call it from soundQueries
            val newId = db.soundQueries.selectLastInsertId().executeAsOne()
            SoundId(newId)
        } else {
            db.soundQueries.update(
                name = sound.name,
                uri = sound.uri,
                asset_key = sound.assetKey,
                is_extra_loud = if (sound.isExtraLoud) 1 else 0,
                id = sound.id.value
            )
            sound.id
        }
    }


    override suspend fun delete(id: SoundId) {
        db.soundQueries.delete(id.value)
    }
}
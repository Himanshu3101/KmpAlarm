package com.himanshu.alarm.data.repo

import com.himanshu.alarm.data.local.AlarmDao
import com.himanshu.alarm.data.mapper.mapAlarmRow
import com.himanshu.alarm.data.mapper.toDbFields
import com.himanshu.alarm.db.AlarmDb
import com.himanshu.alarm.domain.entity.Alarm
import com.himanshu.alarm.domain.entity.AlarmId
import com.himanshu.alarm.domain.entity.AlarmMissionLink
import com.himanshu.alarm.domain.entity.MissionId
import com.himanshu.alarm.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

class AlarmRepositoryImpl(
    private val db: AlarmDb,
    private val alarmDao: AlarmDao
) : AlarmRepository {

    override fun observeAll(): Flow<List<Alarm>> = alarmDao.observeAll()


    override suspend fun get(id: AlarmId): Alarm? =
        db.alarmQueries.getAlarmById(id.value).executeAsOneOrNull()?.let {
            mapAlarmRow(
                id = it.id, label = it.label, time_h = it.time_h, time_m = it.time_m,
                next_trigger = it.next_trigger, enabled = it.enabled, repeat_mask = it.repeat_mask,
                allow_snooze = it.allow_snooze, snooze_minutes = it.snooze_minutes, volume = it.volume,
                gradual_increase_sec = it.gradual_increase_sec, sound_id = it.sound_id,
                prevent_turn_off = it.prevent_turn_off, wake_up_check = it.wake_up_check
            )
        }

    override suspend fun upsert(alarm: Alarm): AlarmId {
        val fields = alarm.toDbFields()
        if (alarm.id.value == 0L) {
            // INSERT (use INSERT ... RETURNING id in your .sq to get id directly)
            db.alarmQueries.insertAlarm(
                label = fields.label,
                time_h = fields.time_h.toLong(),
                time_m = fields.time_m.toLong(),
                next_trigger = fields.next_trigger,
                enabled = fields.enabled,
                repeat_mask = fields.repeat_mask,
                allow_snooze = fields.allow_snooze,
                snooze_minutes = fields.snooze_minutes,
                volume = fields.volume,
                gradual_increase_sec = fields.gradual_increase_sec,
                sound_id = fields.sound_id,
                prevent_turn_off = fields.prevent_turn_off,
                wake_up_check = fields.wake_up_check
            )
            // fallback: query last row
            val newId = db.alarmQueries.selectLastInsertId().executeAsOne() // add this query in alarm.sq
            return AlarmId(newId)
        } else {
            // simple update by reusing INSERT OR REPLACE variant (add in .sq) or specific UPDATEs
            db.alarmQueries.updateAlarmFull(
                label = fields.label,
                time_h = fields.time_h.toLong(),
                time_m = fields.time_m.toLong(),
                next_trigger = fields.next_trigger,
                enabled = fields.enabled,
                repeat_mask = fields.repeat_mask,
                allow_snooze = fields.allow_snooze,
                snooze_minutes = fields.snooze_minutes,
                volume = fields.volume,
                gradual_increase_sec = fields.gradual_increase_sec,
                sound_id = fields.sound_id,
                prevent_turn_off = fields.prevent_turn_off,
                wake_up_check = fields.wake_up_check,
                id = alarm.id.value
            )
            return alarm.id
        }
    }


    /*override suspend fun upsert(alarm: Alarm): AlarmId {
        val fields = alarm.toDbFields()
        return if (alarm.id.value == 0L) {
            val newId = alarmDao.insert(fields)
            AlarmId(newId)
        } else {
            db.alarmQueries.updateAlarmFull(
                label = fields.label,
                time_h = fields.time_h.toLong(),
                time_m = fields.time_m.toLong(),
                next_trigger = fields.next_trigger,
                enabled = fields.enabled,
                repeat_mask = fields.repeat_mask,
                allow_snooze = fields.allow_snooze,
                snooze_minutes = fields.snooze_minutes,
                volume = fields.volume,
                gradual_increase_sec = fields.gradual_increase_sec,
                sound_id = fields.sound_id,
                prevent_turn_off = fields.prevent_turn_off,
                wake_up_check = fields.wake_up_check,
                id = alarm.id.value
            )
            alarm.id
        }
    }*/


    override suspend fun setEnabled(
        id: AlarmId,
        enabled: Boolean
    ) {
        db.alarmQueries.updateAlarmEnabled(if (enabled) 1 else 0, id.value)
    }

    override suspend fun delete(id: AlarmId) {
        db.alarmQueries.deleteAlarm(id.value)
    }

    override suspend fun linkMission(
        alarmId: AlarmId,
        missionId: MissionId,
        required: Boolean
    ) {
        db.missionQueries.linkMissionToAlarm(alarmId.value, missionId.value, if (required) 1 else 0)
    }

    override suspend fun unlinkMission(
        alarmId: AlarmId,
        missionId: MissionId
    ) {
        db.missionQueries.unlinkMissionFromAlarm(alarmId.value, missionId.value)
    }

    override suspend fun missionFor(alarmId: AlarmId): List<AlarmMissionLink> {
        return db.missionQueries.getMissionsForAlarm(alarmId.value).executeAsList().map { row ->
            AlarmMissionLink(
                alarmId = alarmId,
                missionId = MissionId(row.id),
                required = row.required == 1L
            )
        }
    }
}
package com.himanshu.alarm.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.himanshu.alarm.data.mapper.AlarmDbFields
import com.himanshu.alarm.data.mapper.mapAlarmRow
import com.himanshu.alarm.db.AlarmDb
import com.himanshu.alarm.domain.entity.Alarm
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AlarmDao(
    private val db: AlarmDb,
    private val io: CoroutineDispatcher = Dispatchers.Default
) {
    fun observeAll(): Flow<List<Alarm>> =
        db.alarmQueries.getAllAlarms()
            .asFlow()
            .mapToList(io)
            .map { rows ->
                rows.map { row ->
                    mapAlarmRow(
                        id = row.id,
                        label = row.label,
                        time_h = row.time_h,
                        time_m = row.time_m,
                        next_trigger = row.next_trigger,
                        enabled = row.enabled,
                        repeat_mask = row.repeat_mask,
                        allow_snooze = row.allow_snooze,
                        snooze_minutes = row.snooze_minutes,
                        volume = row.volume,
                        gradual_increase_sec = row.gradual_increase_sec,
                        sound_id = row.sound_id,
                        prevent_turn_off = row.prevent_turn_off,
                        wake_up_check = row.wake_up_check
                    )
                }
            }

    fun get(id: Long) = db.alarmQueries.getAlarmById(id).executeAsOneOrNull()

    suspend fun insert(fields: AlarmDbFields): Long {
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
        return db.alarmQueries.selectLastInsertId().executeAsOne()
    }

    suspend fun updateEnabled(id: Long, enabled: Boolean) =
        db.alarmQueries.updateAlarmEnabled(if (enabled) 1 else 0, id)

    suspend fun delete(id: Long) = db.alarmQueries.deleteAlarm(id)

}
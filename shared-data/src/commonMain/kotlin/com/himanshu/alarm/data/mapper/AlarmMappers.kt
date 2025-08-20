package com.himanshu.alarm.data.mapper

import com.himanshu.alarm.domain.entity.*
import kotlinx.datetime.*

private fun localTime(h: Long,m:Long) = LocalTime(hour = h.toInt(),minute = m.toInt())

fun Alarm.toDbFields(): AlarmDbFields = AlarmDbFields(
    label = label,
    time_h = time.hour,
    time_m = time.minute,
    next_trigger = nextTrigger?.epochSeconds,
    enabled = if (enabled) 1L else 0L,
    repeat_mask = repeatMask.toLong(),
    allow_snooze = if (allowSnooze) 1L else 0L,
    snooze_minutes = snoozeMinutes.toLong(),
    volume = volume.toLong(),
    gradual_increase_sec = gradualIncreaseSec.toLong(),
    sound_id = soundId?.value?.toLong(),
    prevent_turn_off = if (preventTurnOff) 1L else 0L,
    wake_up_check = if (wakeUpCheck) 1L else 0L
)

data class AlarmDbFields(
    val label: String,
    val time_h: Int,
    val time_m: Int,
    val next_trigger: Long?,
    val enabled: Long,
    val repeat_mask: Long,
    val allow_snooze: Long,
    val snooze_minutes: Long,
    val volume: Long,
    val gradual_increase_sec: Long,
    val sound_id: Long?,
    val prevent_turn_off: Long,
    val wake_up_check: Long
)

fun mapAlarmRow(
    id: Long,
    label: String,
    time_h: Long,
    time_m: Long,
    next_trigger: Long?,
    enabled: Long,
    repeat_mask: Long,
    allow_snooze: Long,
    snooze_minutes: Long,
    volume: Long,
    gradual_increase_sec: Long,
    sound_id: Long?,
    prevent_turn_off: Long,
    wake_up_check: Long
): Alarm = Alarm(
    id = AlarmId(id),
    label = label,
    time = localTime(time_h, time_m),
    nextTrigger = next_trigger?.let { Instant.fromEpochSeconds(it) },
    enabled = enabled == 1L,
    repeatMask = repeat_mask.toInt(),
    allowSnooze = allow_snooze == 1L,
    snoozeMinutes = snooze_minutes.toInt(),
    volume = volume.toInt(),
    gradualIncreaseSec = gradual_increase_sec.toInt(),
    soundId = sound_id?.let { SoundId(it) },
    preventTurnOff = prevent_turn_off == 1L,
    wakeUpCheck = wake_up_check == 1L
)
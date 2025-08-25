package com.himanshu.alarm.domain.entity

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime

enum class MissionType { MATH, QR, SHAKE, STEPS, MEMORY, TYPING }
enum class MissionDifficulty { EASY, MEDIUM, HARD }
//entities
data class AlarmId(val value: Long)
data class SoundId(val value: Long)
data class MissionId(val value: Long)
data class SessionId(val value: Long)
data class SnoreId(val value: Long)

data class Alarm(
    val id: AlarmId,
    val label: String,
    val timeH: Int,//LocalTime,             // local alarm time
    val timeM: Int,//LocalTime,
    val nextTrigger: Instant?,       // computed, cached for fast resume
    val enabled: Boolean,
    val repeatMask: Int,            // local alarm time - Efficient cross-platform bitmask for days of week (0=Sun..6=Sat). No joins needed to show “repeats M-F”.
    val allowSnooze: Boolean,
    val snoozeMinutes: Int,
    val volume: Int,                // 0..100
    val gradualIncreaseSec: Int,    // fade-in seconds
    val soundId: SoundId?,          // null → default
    val preventTurnOff: Boolean,    // requires mission completion
    val wakeUpCheck: Boolean        // requires “I’m up” check later
)

data class Mission(
    val id: MissionId,
    val type: MissionType,
    val difficulty: MissionDifficulty,
    val configJson: String          // type-specific config (serialized) - Each mission has different fields (e.g., MATH number of questions, SHAKE count). JSON keeps the schema stable while we add new mission types without DB migrations.
)

data class AlarmMissionLink(
    val alarmId: AlarmId,
    val missionId: MissionId,
    val required: Boolean
)

data class Sound(
    val id: SoundId,
    val name: String,
    val uri: String?,                 // external or file path
    val assetKey: String?,            // bundled asset key if any
    val isExtraLoud: Boolean
)

data class SleepSession(
    val id: SessionId,
    val startedAt: Instant,
    val endedAt: Instant?,
    val alarmId: AlarmId
)

data class SnoreSegment(
    val id: SnoreId,
    val sessionId: SessionId,
    val startedAt:Instant,
    val endedAt: Instant,
    val rms:Double,                   // loudness metric
    val filePath:String               // per-segment audio (or sprite index)
)

data class MorningReport(
    val sessionId: SessionId,
    val totalSleepMin:Int,
    val snoreMinutes: Int,
    val wakeCount:Int,
    val missionCompleted: Boolean,
    val subjectiveScore: Int?         // user input 1..5
)
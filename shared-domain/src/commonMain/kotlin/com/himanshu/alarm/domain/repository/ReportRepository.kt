package com.himanshu.alarm.domain.repository

import com.himanshu.alarm.domain.entity.MorningReport

interface ReportRepository {
    suspend fun upsert(report: MorningReport)
    suspend fun latest(listIterator: Int = 14): List<MorningReport>
}

//Later, :shared-data will implement these using the SQLDelight-generated APIs.
//:shared-presentation ViewModels depend only on these interfaces.
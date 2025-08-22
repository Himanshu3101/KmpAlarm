package com.himanshu.alarm.presentation.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

//Why: We need lifecycle-aware coroutines but without AndroidX. Hosts (Android / iOS) call close() when appropriate.

abstract class BaseViewModel{
    protected val vmJob = SupervisorJob()
    protected val vmScope = CoroutineScope(Dispatchers.Default + vmJob)

    open fun close(){
        vmScope.cancel()
    }
}
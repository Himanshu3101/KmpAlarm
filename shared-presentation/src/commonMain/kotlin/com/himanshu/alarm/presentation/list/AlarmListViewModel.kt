package com.himanshu.alarm.presentation.list

import com.himanshu.alarm.domain.repository.AlarmRepository
import com.himanshu.alarm.presentation.list.AlarmListIntent
import com.himanshu.alarm.presentation.list.AlarmListState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlarmListViewModel(
    private val repo: AlarmRepository,
    private val io: CoroutineDispatcher = Dispatchers.Default
) {
    private val scope = CoroutineScope(SupervisorJob() + io)

    private val _state = MutableStateFlow(AlarmListState())
    val state : StateFlow<AlarmListState> = _state.asStateFlow()

    //Called by the host when done (Activity.onDestroy / iOS dealloc)
    fun close(){scope.cancel()}

    init {
        observeAlarms()
    }

    private fun observeAlarms() {
        scope.launch {
            repo.observeAll()                                    // Flow<List<Alarm>>
                .onStart { _state.update { it.copy(isLoading = true, error = null) } }
                .catch { e->_state.update { it.copy(isLoading = false, error = e.message) } }
                .collect{list->
                    _state.update { it.copy(isLoading = false, item = list, error = null) }

                }
        }
    }

    fun dispatch(intent : AlarmListIntent){
        when(intent){
            AlarmListIntent.AddDummy -> addDummy()
            AlarmListIntent.Refresh -> refresh()
            is AlarmListIntent.ToggleEnabled -> toggleEnabled(intent.id, intent.enabled)
        }
    }

    private fun refresh() {
        // If repo has an explicit refresh, call it; otherwise rely on observeAll flow
        // _state.emit(...) if you want a spinner animation
    }

    private fun toggleEnabled(id: Long, enabled: Boolean) {
        scope.launch{
            runCatching { repo.setEnabled(id,enabled) }
                .onFailure { e->_state.update { it.copy(error = e.message) } }
        }
    }

    private fun addDummy() {
        scope.launch {
            // Demo; replace with your dialog/screen later
            runCatching {
                repo.createOrUpdate(
                    label = "Morning Alarm",
                    hour = 7,
                    minute = 0,
                    repeatMask = 0,          // once
                    soundId = null,
                    allowSnooze = true,
                    snoozeMinutes = 5,
                    volume = 80,
                    gradualIncreaseSec = 0,
                    preventTurnOff = false,
                    wakeUpCheck = false,
                    enabled = true
                )
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
        }
    }





}
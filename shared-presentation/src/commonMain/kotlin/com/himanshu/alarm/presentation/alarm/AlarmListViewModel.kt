package com.himanshu.alarm.presentation.alarm

import com.himanshu.alarm.domain.repository.AlarmRepository
import com.himanshu.alarm.presentation.core.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


//Why: StateFlow is the cross-platform state holder; VM never imports Android UI libraries.

class AlarmListViewModel(
    private val repo : AlarmRepository
) : BaseViewModel(){

    private val _state = MutableStateFlow(AlarmListState())
    val state : StateFlow<AlarmListState> = _state

    init{
        observeAlarms()
    }

    private fun observeAlarms() {
        vmScope.launch {
            repo.observeAll()                       //Flow<List<Alarm>>
                .onStart { _state.update { it.copy(isLoading = true, error = null) } }
                .catch { e -> _state.update { it.copy(isLoading = false, error = e.message) } }
                .collect { list->
                    _state.update { it.copy(isLoading = false, alarms = list, error = null) }
                }
        }
    }

    fun onIntent(intent: AlarmListIntent){
        when(intent) {
            AlarmListIntent.Refresh -> observeAlarms()
            is AlarmListIntent.ToggleEnabled -> vmScope.launch {
                runCatching { repo.setEnabled(intent.alarmId, intent.enabled) }
                    .onFailure { e -> _state.update { it.copy(error = e.message) } }
            }

            is AlarmListIntent.AddDummy -> vmScope.launch {
                runCatching { repo.addDummy() }
                    .onFailure { e -> _state.update { it.copy(error = e.message) } }
            }
        }
    }
}
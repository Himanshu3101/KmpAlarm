package com.himanshu.alarm.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.himanshu.alarm.presentation.alarm.AlarmListState
import com.himanshu.alarm.presentation.alarm.AlarmListIntent

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.unit.dp
import com.himanshu.alarm.domain.entity.Alarm
import com.himanshu.alarm.ui.design.AlarmTheme
import com.himanshu.alarm.ui.design.AlarmThemeProvider

//Why: stateless UI is easy to reuse across platforms. It takes state + onIntent from any host.

@Composable
fun AlarmListScreen(
    state : AlarmListState,
    onIntent: (AlarmListIntent) -> Unit,
) {
    AlarmThemeProvider {
        AlarmListScreen(state = state, onIntent = onIntent)
    }
}

@Composable
private fun AlarmListScreen(
    state : AlarmListState,
    onIntent: (AlarmListIntent) -> Unit,
) {

    val spacing = AlarmTheme.spacing
    val onIntent = rememberUpdatedState(onIntent)


    Scaffold (
        floatingActionButton = {
            FloatingActionButton (onClick = {onIntentState.value(AlarmListIntent.AddDummy)}){
                Text("+")
            }
        }
    ){ padding->
        when{
            state.isLoading -> Box(Modifier.fillMaxSize().padding(padding)){
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            state.error != null -> Box(Modifier.fillMaxSize().padding(padding)){
                Text("Error : ${state.error}", Modifier.align (Alignment.Center))
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(12.dp),
                verticalArrangement = Arrangement.spacedBy { spacing.sm }
            ){
                items(state.alarms, key = {it.id.value}) {alarm ->
                    AlarmRow(alarm = alarm, onIntent = onIntentState.value)}
            }


        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AlarmRow(alarm: Alarm, onIntent: (AlarmListIntent) -> Unit) {

    val spacing = AlarmTheme.spacing
    val type = AlarmTheme.typeScale

     Card (Modifier.fillMaxWidth()){
         Row (
             modifier = Modifier
                 .padding(12.dp)
                 .fillMaxWidth(),
             verticalAlignment = Alignment.CenterVertically
         ){
             Column(Modifier.weight(1f)){
                 Text(alarm.label.ifBlank {"Alarm #${alarm.id.value}"}, style = MaterialTheme.typography.titleMedium)
                 Text("${alarm.timemeH.toString().padStart(2,'0')} :${alarm.timeM.toString().padStart(2,'0')}")
             }
             Switch(
                 checked = alarm.enabled,
                 onCheckedChange = {onIntent(AlarmListIntent.ToggleEnabled(alarm.id, it))}
             )
         }
     }
}

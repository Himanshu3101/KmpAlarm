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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.himanshu.alarm.domain.entity.Alarm
import com.himanshu.alarm.ui.design.AlarmTheme
import com.himanshu.alarm.ui.design.AlarmThemeProvider

//Why: stateless UI is easy to reuse across platforms. It takes state + onIntent from any host.

@Composable
fun AlarmListRoot(
    state: AlarmListState,
    onIntent: (AlarmListIntent) -> Unit,
) {
    AlarmThemeProvider {
        AlarmListScreen(state = state, onIntent = onIntent)
    }
}

@Composable
private fun AlarmListScreen(
    state: AlarmListState,
    onIntent: (AlarmListIntent) -> Unit,
) {

    val spacing = AlarmTheme.spacing
    val onIntentState = rememberUpdatedState(onIntent)


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onIntentState.value(AlarmListIntent.AddDummy) }) {
                Text("+")
            }
        }
    ) { padding ->
        when {
            state.isLoading -> Box(Modifier.fillMaxSize().padding(padding)) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            state.error != null -> Box(Modifier.fillMaxSize().padding(padding)) {
                Text("Error : ${state.error}", Modifier.align(Alignment.Center))
            }

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(spacing.sm)
            ) {
                items(state.alarms, key = { it.id.value }) { alarm ->
                    AlarmRow(alarm = alarm, onIntent = onIntentState.value)
                }
            }


        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AlarmRow(alarm: Alarm, onIntent: (AlarmListIntent) -> Unit) {

    val spacing = AlarmTheme.spacing
    val type = AlarmTheme.typeScale

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(spacing.md)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = alarm.label.ifBlank { "Alarm #${alarm.id.value}" },
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = type.title, fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = formatTime(alarm.timeH, alarm.timeM),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = type.headline, fontWeight = FontWeight.Medium)
                    )

                }
                Switch(
                    checked = alarm.enabled,
                    onCheckedChange = {
                        onIntent(
                            AlarmListIntent.ToggleEnabled(
                                alarm.id,
                                it
                            )
                        )
                    }
                )
            }

            //Wrapping details using FlowRow
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing.sm),
                horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                verticalArrangement = Arrangement.spacedBy(spacing.xs)
            ) {
                Capsule(text = repeatText(alarm.repeatMask))
                Capsule(text = if (alarm.allowSnooze) "Snooze ${alarm.snoozeMinutes}m" else "No Snooze")
                Capsule(text = "Vol ${alarm.volume}%")
                if (alarm.gradualIncreaseSec > 0) Capsule(text = "Fade-in ${alarm.gradualIncreaseSec}s")
                if (alarm.preventTurnOff) Capsule(text = "Mission")
                if (alarm.wakeUpCheck) Capsule(text = "Wake-up Check")
            }
        }
    }
}

@Composable
private fun Capsule(text:String){
    val spacing = AlarmTheme.spacing
    Text(
        text = text,
        modifier = Modifier
            .wrapContentWidth()
            .padding(horizontal = spacing.xs, vertical = spacing.xxs),
        style = MaterialTheme.typography.labelSmall
    )
}

private fun formatTime(h:Int, m:Int):String =
    "${h.coerceIn(0,23).toString().padStart(2,'0')}:${m.coerceIn(0,59).toString().padStart(2,'0')}"

private fun repeatText(mask:Int):String{
    // simple placeholder; replace with real mask decoding later
    return if (mask == 0) "Once" else "Repeat"
}

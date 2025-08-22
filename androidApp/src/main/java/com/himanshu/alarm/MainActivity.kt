package com.himanshu.alarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.himanshu.alarm.ui.theme.KmpAlarmTheme


import com.himanshu.alarm.data.db.DatabaseDriverFactory
import com.himanshu.alarm.data.di.dataModule
import com.himanshu.alarm.presentation.alarm.AlarmListViewModel
import com.himanshu.alarm.presentation.di.presentationModule
import com.himanshu.alarm.ui.AlarmListScreen

import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin


import com.himanshu.alarm.ui.App

//Why: We host the shared VM in Android’s composition via Koin. No AndroidX ViewModel, still lifecycle-friendly by closing on destroy.

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* enableEdgeToEdge()
        setContent {
            com.himanshu.alarm.ui.App()
        }*/
        if (GlobalContext.getOrNull() == null) {
            val factory = DatabaseDriverFactory(this@MainActivity) // Android actual needs Context
            startKoin { modules(dataModule(factory), presentationModule) }
        }

        setContent {
            // Obtain the VM from Koin and keep it for the composition lifetime
            val vm = remember { GlobalContext.get().get<AlarmListViewModel>() }
            val state by vm.state.collectAsState()

            AlarmListScreen(
                state = state,
                onIntent = vm::onIntent
            )
        }
    }

    override fun onDestroy() {
        // If you want to eagerly cancel VM coroutines when Activity finishes
        runCatching { GlobalContext.get().get<AlarmListViewModel>().close() }
        super.onDestroy()
    }
}

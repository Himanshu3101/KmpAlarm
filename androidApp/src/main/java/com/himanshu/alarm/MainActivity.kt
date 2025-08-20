package com.himanshu.alarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.himanshu.alarm.ui.theme.KmpAlarmTheme


import com.himanshu.alarm.data.db.DatabaseDriverFactory
import com.himanshu.alarm.data.di.dataModule

import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin


import com.himanshu.alarm.ui.App


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* enableEdgeToEdge()
        setContent {
            com.himanshu.alarm.ui.App()
        }*/
        if (GlobalContext.getOrNull() == null) {
            val factory = DatabaseDriverFactory(this) // Android actual needs Context
            startKoin { modules(dataModule(factory)) }
        }

        setContent { App() }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KmpAlarmTheme {
        Greeting("Android")
    }
}
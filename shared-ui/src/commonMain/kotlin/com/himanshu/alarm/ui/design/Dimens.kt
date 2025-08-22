package com.himanshu.alarm.ui.design

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Spacing(
    val xxs: Dp = 4.dp,
    val xs: Dp = 8.dp,
    val sm: Dp = 12.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 20.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp
)

data class TypeScale(
    val label: TextUnit = 12.sp,
    val body: TextUnit = 14.sp,
    val title: TextUnit = 16.sp,
    val headline: TextUnit = 20.sp
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
val LocalTypeScale = staticCompositionLocalOf { TypeScale() }

object AlarmTheme{
    val spacing : Spacing @Composable get() = LocalSpacing.current
    val typeScale : TypeScale @Composable get() = LocalTypeScale.current
}

@Composable
fun AlarmThemeProvider(
    spacing : Spacing = Spacing(),
    type : TypeScale = TypeScale(),
    content : @Composable () -> Unit
){
    CompositionLocalProvider (
        LocalSpacing provides spacing,
        LocalTypeScale provides type,
        content = content
    )
}
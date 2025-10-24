package com.arkhe.menu.presentation.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class TrafficLightColors(
    val stop: Color,
    val caution: Color,
    val go: Color,
)

val LocalTrafficLightColors = staticCompositionLocalOf {
    TrafficLightColors(
        stop = Color.Unspecified,
        caution = Color.Unspecified,
        go = Color.Unspecified
    )
}
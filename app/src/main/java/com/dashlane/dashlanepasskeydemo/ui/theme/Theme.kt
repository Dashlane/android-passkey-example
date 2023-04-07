package com.dashlane.dashlanepasskeydemo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val LightColorPalette = lightColorScheme(
    primary = Purple,
    secondary = Teal200
)

@Composable
fun DashlanePasskeyDemoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorPalette,
        typography = Typography,
        content = content,
    )
}
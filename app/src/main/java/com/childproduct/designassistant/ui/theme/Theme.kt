package com.childproduct.designassistant.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 专业导向型深色主题配色
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryBlue,
    tertiary = InfoBlue,
    background = DarkBackground,
    surface = DarkSurface,
    error = ErrorRed,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = LightGray,
    onSurface = LightGray,
    onError = White,
    primaryContainer = DarkGray,
    onPrimaryContainer = LightGray,
    secondaryContainer = MediumGray,
    onSecondaryContainer = LightGray
)

// 专业导向型浅色主题配色
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryBlue,
    tertiary = InfoBlue,
    background = LightGray,
    surface = White,
    error = ErrorRed,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = DarkGray,
    onSurface = DarkGray,
    onError = White,
    primaryContainer = PrimaryBlue.copy(alpha = 0.1f),
    onPrimaryContainer = DarkGray,
    secondaryContainer = SecondaryBlue.copy(alpha = 0.1f),
    onSecondaryContainer = DarkGray
)

@Composable
fun ChildProductDesignAssistantTheme(
    darkTheme: Boolean = true,  // 默认使用深色主题，符合专业工具定位
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

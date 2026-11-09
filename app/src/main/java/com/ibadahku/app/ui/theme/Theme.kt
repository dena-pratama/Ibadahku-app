package com.ibadahku.app.ui.theme

import android.app.Activity
import android.os.Build
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

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = CardSurface,
    primaryContainer = SageContainer,
    onPrimaryContainer = DarkForestGreen,
    secondary = PrimaryGreen,
    onSecondary = CardSurface,
    secondaryContainer = SageGreenLight,
    onSecondaryContainer = DarkForestGreen,
    background = CreamBackground,
    onBackground = TextDark,
    surface = CardSurface,
    onSurface = TextDark,
    surfaceVariant = SageMuted,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = SoftSageGreen,
    onPrimary = Color.White,
    primaryContainer = DarkSurfaceBorder,
    onPrimaryContainer = DarkTextPrimary,
    secondary = PrimaryGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1E3328),
    onSecondaryContainer = DarkTextPrimary,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceBorder,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkSurfaceBorder
)

@Composable
fun IbadahkuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

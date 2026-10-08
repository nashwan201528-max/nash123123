package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val DarkColorScheme = darkColorScheme(
    primary = StudioCyan,
    onPrimary = StudioDark,
    primaryContainer = StudioSurfaceVariant,
    onPrimaryContainer = StudioCyanLight,
    secondary = StudioViolet,
    onSecondary = StudioTextPrimary,
    secondaryContainer = StudioSurfaceVariant,
    onSecondaryContainer = StudioVioletLight,
    tertiary = StudioMagenta,
    onTertiary = StudioTextPrimary,
    background = StudioDark,
    onBackground = StudioTextPrimary,
    surface = StudioSurface,
    onSurface = StudioTextPrimary,
    surfaceVariant = StudioSurfaceVariant,
    onSurfaceVariant = StudioTextSecondary,
    outline = StudioSurfaceBorder,
    error = StudioRose,
    onError = StudioTextPrimary
)

@Composable
fun MontageProTheme(
    darkTheme: Boolean = true, // Creative studio always defaults to sleek dark mode
    content: @Composable () -> Unit
) {
    // Provide Right-to-Left Arabic direction as standard
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = Typography,
            content = content
        )
    }
}

package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DevColorScheme = darkColorScheme(
    primary = DevCyan,
    onPrimary = Color(0xFF030712),
    primaryContainer = Color(0xFF1E293B),
    onPrimaryContainer = DevCyan,
    secondary = DevPurple,
    onSecondary = Color(0xFF030712),
    secondaryContainer = Color(0xFF2E1065),
    onSecondaryContainer = Color(0xFFE9D5FF),
    tertiary = DevBlue,
    onTertiary = Color(0xFF030712),
    background = DevBgDark,
    onBackground = DevTextPrimary,
    surface = DevSurfaceDark,
    onSurface = DevTextPrimary,
    surfaceVariant = DevCardSurface,
    onSurfaceVariant = DevTextSecondary,
    outline = DevBorderGlass,
    outlineVariant = DevBorderMuted
)

@Composable
fun DEV_IsrafilTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DevColorScheme,
        typography = Typography,
        content = content
    )
}

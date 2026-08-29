package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Dark Palette
val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF181818)
val DarkSurfaceCard = Color(0xFF1E1E1E)
val DarkSurfaceVariant = Color(0xFF262626)
val TextPrimaryDark = Color(0xFFEDEDED)
val TextSecondaryDark = Color(0xFF9E9E9E)
val TextTertiaryDark = Color(0xFF6E6E6E)
val BorderSubtleDark = Color(0xFF2E2E2E)

// Light Palette
val LightBackground = Color(0xFFF6F7F9)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceCard = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFEEF1F4)
val TextPrimaryLight = Color(0xFF121417)
val TextSecondaryLight = Color(0xFF5C6470)
val TextTertiaryLight = Color(0xFF8A93A0)
val BorderSubtleLight = Color(0xFFDEE2E8)

// Default Accent Colors
val DefaultPastelGreen = Color(0xFFA8E6CF)
val PastelGreen = Color(0xFFA8E6CF)
val PastelGreenVariant = Color(0xFF88D4B8)
val PastelGreenContainer = Color(0xFF1C2D24)

val TextPrimary = Color(0xFFEDEDED)
val TextSecondary = Color(0xFF9E9E9E)
val TextTertiary = Color(0xFF6E6E6E)

val BorderSubtle = Color(0xFF2E2E2E)
val BorderActive = Color(0xFFA8E6CF)

val ErrorRed = Color(0xFFE57373)
val ErrorContainer = Color(0xFF331919)

data class AppColorScheme(
  val isDark: Boolean,
  val background: Color,
  val surface: Color,
  val surfaceCard: Color,
  val surfaceVariant: Color,
  val textPrimary: Color,
  val textSecondary: Color,
  val textTertiary: Color,
  val borderSubtle: Color,
  val borderActive: Color,
  val accent: Color,
  val accentContainer: Color,
  val onAccent: Color,
  val error: Color = ErrorRed
)

val LocalAppColorScheme = staticCompositionLocalOf {
  AppColorScheme(
    isDark = true,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceCard = DarkSurfaceCard,
    surfaceVariant = DarkSurfaceVariant,
    textPrimary = TextPrimaryDark,
    textSecondary = TextSecondaryDark,
    textTertiary = TextTertiaryDark,
    borderSubtle = BorderSubtleDark,
    borderActive = PastelGreen,
    accent = PastelGreen,
    accentContainer = PastelGreenContainer,
    onAccent = DarkBackground,
    error = ErrorRed
  )
}

object AppTheme {
  val colors: AppColorScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColorScheme.current
}

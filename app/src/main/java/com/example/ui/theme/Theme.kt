package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.model.AppThemeMode
import com.example.ui.components.ColorUtils

@Composable
fun MyApplicationTheme(
  themeMode: AppThemeMode = AppThemeMode.DARK,
  accentColorHex: String = "#A8E6CF",
  content: @Composable () -> Unit,
) {
  val isDark = when (themeMode) {
    AppThemeMode.DARK -> true
    AppThemeMode.LIGHT -> false
    AppThemeMode.SYSTEM -> isSystemInDarkTheme()
  }

  val accentColor = remember(accentColorHex) {
    ColorUtils.parseHexColor(accentColorHex, DefaultPastelGreen)
  }

  val onAccent = remember(accentColor) {
    ColorUtils.getContrastingTextColor(accentColor)
  }

  val accentContainer = remember(accentColor, isDark) {
    if (isDark) {
      accentColor.copy(alpha = 0.18f)
    } else {
      accentColor.copy(alpha = 0.15f)
    }
  }

  val appColors = remember(isDark, accentColor, onAccent, accentContainer) {
    if (isDark) {
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
        borderActive = accentColor,
        accent = accentColor,
        accentContainer = accentContainer,
        onAccent = onAccent,
        error = ErrorRed
      )
    } else {
      AppColorScheme(
        isDark = false,
        background = LightBackground,
        surface = LightSurface,
        surfaceCard = LightSurfaceCard,
        surfaceVariant = LightSurfaceVariant,
        textPrimary = TextPrimaryLight,
        textSecondary = TextSecondaryLight,
        textTertiary = TextTertiaryLight,
        borderSubtle = BorderSubtleLight,
        borderActive = accentColor,
        accent = accentColor,
        accentContainer = accentContainer,
        onAccent = onAccent,
        error = ErrorRed
      )
    }
  }

  val materialColorScheme = if (isDark) {
    darkColorScheme(
      primary = accentColor,
      onPrimary = onAccent,
      primaryContainer = accentContainer,
      onPrimaryContainer = accentColor,
      secondary = accentColor,
      onSecondary = onAccent,
      secondaryContainer = DarkSurfaceVariant,
      onSecondaryContainer = TextPrimaryDark,
      background = DarkBackground,
      onBackground = TextPrimaryDark,
      surface = DarkSurface,
      onSurface = TextPrimaryDark,
      surfaceVariant = DarkSurfaceVariant,
      onSurfaceVariant = TextSecondaryDark,
      surfaceContainer = DarkSurfaceCard,
      surfaceContainerHigh = DarkSurfaceVariant,
      outline = BorderSubtleDark,
      outlineVariant = BorderSubtleDark,
      error = ErrorRed,
      onError = Color(0xFF121212)
    )
  } else {
    lightColorScheme(
      primary = accentColor,
      onPrimary = onAccent,
      primaryContainer = accentContainer,
      onPrimaryContainer = accentColor,
      secondary = accentColor,
      onSecondary = onAccent,
      secondaryContainer = LightSurfaceVariant,
      onSecondaryContainer = TextPrimaryLight,
      background = LightBackground,
      onBackground = TextPrimaryLight,
      surface = LightSurface,
      onSurface = TextPrimaryLight,
      surfaceVariant = LightSurfaceVariant,
      onSurfaceVariant = TextSecondaryLight,
      surfaceContainer = LightSurfaceCard,
      surfaceContainerHigh = LightSurfaceVariant,
      outline = BorderSubtleLight,
      outlineVariant = BorderSubtleLight,
      error = ErrorRed,
      onError = Color.White
    )
  }

  CompositionLocalProvider(LocalAppColorScheme provides appColors) {
    MaterialTheme(
      colorScheme = materialColorScheme,
      typography = Typography,
      content = content
    )
  }
}

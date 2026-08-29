package com.example.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object ColorUtils {

  fun parseHexColor(hex: String, defaultColor: Color = Color(0xFFA8E6CF)): Color {
    val clean = hex.trim().removePrefix("#")
    return try {
      when (clean.length) {
        6 -> {
          val r = clean.substring(0, 2).toInt(16)
          val g = clean.substring(2, 4).toInt(16)
          val b = clean.substring(4, 6).toInt(16)
          Color(r, g, b)
        }
        8 -> {
          val a = clean.substring(0, 2).toInt(16)
          val r = clean.substring(2, 4).toInt(16)
          val g = clean.substring(4, 6).toInt(16)
          val b = clean.substring(6, 8).toInt(16)
          Color(r, g, b, a)
        }
        3 -> {
          val r = clean.substring(0, 1).repeat(2).toInt(16)
          val g = clean.substring(1, 2).repeat(2).toInt(16)
          val b = clean.substring(2, 3).repeat(2).toInt(16)
          Color(r, g, b)
        }
        else -> defaultColor
      }
    } catch (_: Exception) {
      defaultColor
    }
  }

  fun colorToHex(color: Color): String {
    val r = (color.red * 255).toInt().coerceIn(0, 255)
    val g = (color.green * 255).toInt().coerceIn(0, 255)
    val b = (color.blue * 255).toInt().coerceIn(0, 255)
    return String.format("#%02X%02X%02X", r, g, b)
  }

  fun colorToHsv(color: Color): FloatArray {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(color.toArgb(), hsv)
    return hsv
  }

  fun hsvToColor(hue: Float, saturation: Float, value: Float): Color {
    val hsv = floatArrayOf(
      hue.coerceIn(0f, 360f),
      saturation.coerceIn(0f, 1f),
      value.coerceIn(0f, 1f)
    )
    val argb = android.graphics.Color.HSVToColor(hsv)
    return Color(argb)
  }

  fun getContrastingTextColor(backgroundColor: Color): Color {
    // Relative luminance calculation
    val luminance = (0.299 * backgroundColor.red + 0.587 * backgroundColor.green + 0.114 * backgroundColor.blue)
    return if (luminance > 0.55) Color(0xFF121212) else Color(0xFFFFFFFF)
  }
}

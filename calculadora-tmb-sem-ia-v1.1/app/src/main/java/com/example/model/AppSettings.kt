package com.example.model

enum class AppThemeMode(val displayName: String, val iconLabel: String) {
  DARK("Modo Escuro", "🌙"),
  LIGHT("Modo Claro", "☀️"),
  SYSTEM("Padrão do Sistema", "📱")
}

data class AppSettings(
  val themeMode: AppThemeMode = AppThemeMode.DARK,
  val accentColorHex: String = "#A8E6CF"
)

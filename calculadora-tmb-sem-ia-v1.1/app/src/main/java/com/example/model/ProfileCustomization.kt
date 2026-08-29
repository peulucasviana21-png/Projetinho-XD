package com.example.model

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class AvatarPreset(
  val id: String,
  val emoji: String,
  val title: String,
  val bgColorHex: String
)

data class BannerPreset(
  val id: String,
  val title: String,
  val startColorHex: String,
  val endColorHex: String,
  val accentColorHex: String
) {
  val brush: Brush
    get() = Brush.linearGradient(
      listOf(
        Color(android.graphics.Color.parseColor(startColorHex)),
        Color(android.graphics.Color.parseColor(endColorHex))
      )
    )
}

object ProfileCustomizationPresets {
  val AVATARS = listOf(
    AvatarPreset("avatar_1", "🏃‍♂️", "Corrida & Cardio", "#1E293B"),
    AvatarPreset("avatar_2", "🏋️‍♀️", "Musculação", "#334155"),
    AvatarPreset("avatar_3", "🥑", "Nutrição & Saúde", "#064E3B"),
    AvatarPreset("avatar_4", "🧘", "Bem-estar & Zen", "#312E81"),
    AvatarPreset("avatar_5", "⚡", "Alta Performance", "#78350F"),
    AvatarPreset("avatar_6", "🚴", "Ciclismo", "#1E3A8A"),
    AvatarPreset("avatar_7", "💧", "Hidratação", "#0C4A6E"),
    AvatarPreset("avatar_8", "🌟", "Foco & Disciplina", "#701A75")
  )

  val BANNERS = listOf(
    BannerPreset(
      id = "banner_1",
      title = "Aurora Tropical",
      startColorHex = "#0F2027",
      endColorHex = "#203A43",
      accentColorHex = "#2C5364"
    ),
    BannerPreset(
      id = "banner_2",
      title = "Sunset Energy",
      startColorHex = "#4A0E4E",
      endColorHex = "#881337",
      accentColorHex = "#F43F5E"
    ),
    BannerPreset(
      id = "banner_3",
      title = "Deep Ocean",
      startColorHex = "#021B79",
      endColorHex = "#0575E6",
      accentColorHex = "#38BDF8"
    ),
    BannerPreset(
      id = "banner_4",
      title = "Mint Forest",
      startColorHex = "#064E3B",
      endColorHex = "#047857",
      accentColorHex = "#A8E6CF"
    ),
    BannerPreset(
      id = "banner_5",
      title = "Cosmic Neon",
      startColorHex = "#311042",
      endColorHex = "#6B21A8",
      accentColorHex = "#C084FC"
    ),
    BannerPreset(
      id = "banner_6",
      title = "Charcoal Gold",
      startColorHex = "#18181B",
      endColorHex = "#27272A",
      accentColorHex = "#FBBF24"
    )
  )

  fun getAvatar(id: String): AvatarPreset =
    AVATARS.find { it.id == id } ?: AVATARS.first()

  fun getBanner(id: String): BannerPreset =
    BANNERS.find { it.id == id } ?: BANNERS.first()
}

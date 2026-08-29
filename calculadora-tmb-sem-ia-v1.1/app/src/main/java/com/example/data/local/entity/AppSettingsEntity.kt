package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
  @PrimaryKey val id: Int = 1,
  val themeMode: String = "DARK",      // "DARK", "LIGHT", "SYSTEM"
  val accentColorHex: String = "#A8E6CF",
  val updatedAt: Long = System.currentTimeMillis()
)

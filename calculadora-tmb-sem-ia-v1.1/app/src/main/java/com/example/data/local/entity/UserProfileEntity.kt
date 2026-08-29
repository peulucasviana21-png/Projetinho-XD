package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
  @PrimaryKey val id: Int = 1,
  val userName: String = "Meu Perfil",
  val avatarId: String = "avatar_1",
  val bannerId: String = "banner_1",
  val customAvatarUri: String? = null,
  val customBannerUri: String? = null,
  val gender: String,          // "MALE" or "FEMALE"
  val age: Int,
  val weightKg: Double,
  val heightCm: Double,
  val activityLevel: String,   // ActivityLevel name
  val bmr: Double,
  val dailyExpenditure: Double,
  val waterMl: Int = 2000,
  val updatedAt: Long = System.currentTimeMillis()
)

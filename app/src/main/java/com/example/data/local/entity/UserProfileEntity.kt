package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
  @PrimaryKey val id: Int = 1,
  val gender: String,          // "MALE" or "FEMALE"
  val age: Int,
  val weightKg: Double,
  val heightCm: Double,
  val activityLevel: String,   // ActivityLevel name
  val bmr: Double,
  val dailyExpenditure: Double,
  val updatedAt: Long = System.currentTimeMillis()
)

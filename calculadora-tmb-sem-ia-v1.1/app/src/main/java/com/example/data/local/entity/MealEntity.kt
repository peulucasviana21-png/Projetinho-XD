package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val calories: Int,
  val category: String,        // "BREAKFAST", "LUNCH", "DINNER", "SNACK", "OTHER"
  val dateString: String,      // Format: "YYYY-MM-DD"
  val timestamp: Long = System.currentTimeMillis()
)

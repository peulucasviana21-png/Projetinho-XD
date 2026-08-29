package com.example.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class MealCategory(val displayName: String, val iconLabel: String) {
  BREAKFAST("Café da Manhã", "☕"),
  LUNCH("Almoço", "🍽️"),
  DINNER("Jantar", "🍲"),
  SNACK("Lanche", "🍎"),
  OTHER("Outro", "🥗")
}

data class Meal(
  val id: Long = 0,
  val title: String,
  val calories: Int,
  val category: MealCategory,
  val dateString: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
  val timestamp: Long = System.currentTimeMillis()
)

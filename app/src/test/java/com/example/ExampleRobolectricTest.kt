package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.repository.MetabolicRepository
import com.example.model.ActivityLevel
import com.example.model.AppSettings
import com.example.model.AppThemeMode
import com.example.model.Gender
import com.example.model.Meal
import com.example.model.MealCategory
import com.example.model.MetabolicCalculator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  private lateinit var database: AppDatabase
  private lateinit var repository: MetabolicRepository

  @Before
  fun setUp() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    repository = MetabolicRepository(
      userProfileDao = database.userProfileDao(),
      mealDao = database.mealDao(),
      settingsDao = database.settingsDao()
    )
  }

  @After
  fun tearDown() {
    database.close()
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Calculadora TMB", appName)
  }

  @Test
  fun `verify Mifflin-St Jeor calculation for male`() {
    // Homem: 25 anos, 75kg, 180cm
    // (10 * 75) + (6.25 * 180) - (5 * 25) + 5
    // 750 + 1125 - 125 + 5 = 1755 kcal
    val bmr = MetabolicCalculator.calculateBmr(
      weightKg = 75.0,
      heightCm = 180.0,
      ageYears = 25,
      gender = Gender.MALE
    )
    assertEquals(1755.0, bmr, 0.01)

    // Atividade Moderada (1.55) -> 1755 * 1.55 = 2720.25
    val total = MetabolicCalculator.calculateTotalExpenditure(bmr, ActivityLevel.MODERATE)
    assertEquals(2720.25, total, 0.01)
  }

  @Test
  fun `verify Mifflin-St Jeor calculation for female`() {
    // Mulher: 30 anos, 60kg, 165cm
    // (10 * 60) + (6.25 * 165) - (5 * 30) - 161
    // 600 + 1031.25 - 150 - 161 = 1320.25 kcal
    val bmr = MetabolicCalculator.calculateBmr(
      weightKg = 60.0,
      heightCm = 165.0,
      ageYears = 30,
      gender = Gender.FEMALE
    )
    assertEquals(1320.25, bmr, 0.01)

    // Atividade Sedentária (1.2) -> 1320.25 * 1.2 = 1584.3
    val total = MetabolicCalculator.calculateTotalExpenditure(bmr, ActivityLevel.SEDENTARY)
    assertEquals(1584.3, total, 0.01)
  }

  @Test
  fun `verify meal insertion and total calorie calculation`() = runBlocking {
    val date = "2026-08-28"
    val meal1 = Meal(
      title = "Café da Manhã",
      calories = 400,
      category = MealCategory.BREAKFAST,
      dateString = date
    )
    val meal2 = Meal(
      title = "Almoço",
      calories = 750,
      category = MealCategory.LUNCH,
      dateString = date
    )

    repository.addMeal(meal1)
    repository.addMeal(meal2)

    val meals = repository.getMealsByDateFlow(date).first()
    assertEquals(2, meals.size)
    val totalCalories = meals.sumOf { it.calories }
    assertEquals(1150, totalCalories)
  }

  @Test
  fun `verify settings persistence in room`() = runBlocking {
    val settings = AppSettings(
      themeMode = AppThemeMode.LIGHT,
      accentColorHex = "#FF5722"
    )

    repository.saveSettings(settings)
    val loaded = repository.settingsFlow.first()
    assertEquals(AppThemeMode.LIGHT, loaded.themeMode)
    assertEquals("#FF5722", loaded.accentColorHex)
  }
}

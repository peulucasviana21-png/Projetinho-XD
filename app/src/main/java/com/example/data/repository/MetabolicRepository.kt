package com.example.data.repository

import com.example.data.local.dao.MealDao
import com.example.data.local.dao.SettingsDao
import com.example.data.local.dao.UserProfileDao
import com.example.data.local.entity.AppSettingsEntity
import com.example.data.local.entity.MealEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.model.ActivityLevel
import com.example.model.AppSettings
import com.example.model.AppThemeMode
import com.example.model.Gender
import com.example.model.Meal
import com.example.model.MealCategory
import com.example.model.MetabolicCalculationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MetabolicRepository(
  private val userProfileDao: UserProfileDao,
  private val mealDao: MealDao,
  private val settingsDao: SettingsDao
) {
  val settingsFlow: Flow<AppSettings> =
    settingsDao.getSettingsFlow().map { entity ->
      if (entity != null) {
        val mode = try {
          AppThemeMode.valueOf(entity.themeMode)
        } catch (_: Exception) {
          AppThemeMode.DARK
        }
        AppSettings(themeMode = mode, accentColorHex = entity.accentColorHex)
      } else {
        AppSettings(themeMode = AppThemeMode.DARK, accentColorHex = "#A8E6CF")
      }
    }

  suspend fun saveSettings(settings: AppSettings) {
    settingsDao.saveSettings(
      AppSettingsEntity(
        id = 1,
        themeMode = settings.themeMode.name,
        accentColorHex = settings.accentColorHex
      )
    )
  }
  val userProfileFlow: Flow<MetabolicCalculationResult?> =
    userProfileDao.getUserProfileFlow().map { entity ->
      entity?.let {
        val gender = try {
          Gender.valueOf(it.gender)
        } catch (_: Exception) {
          Gender.MALE
        }
        val activity = try {
          ActivityLevel.valueOf(it.activityLevel)
        } catch (_: Exception) {
          ActivityLevel.SEDENTARY
        }
        MetabolicCalculationResult(
          bmr = it.bmr,
          dailyCaloricExpenditure = it.dailyExpenditure,
          age = it.age,
          weightKg = it.weightKg,
          heightCm = it.heightCm,
          gender = gender,
          activityLevel = activity
        )
      }
    }

  suspend fun saveUserProfile(result: MetabolicCalculationResult) {
    userProfileDao.saveUserProfile(
      UserProfileEntity(
        id = 1,
        gender = result.gender.name,
        age = result.age,
        weightKg = result.weightKg,
        heightCm = result.heightCm,
        activityLevel = result.activityLevel.name,
        bmr = result.bmr,
        dailyExpenditure = result.dailyCaloricExpenditure
      )
    )
  }

  fun getMealsByDateFlow(date: String): Flow<List<Meal>> {
    return mealDao.getMealsByDateFlow(date).map { list ->
      list.map { it.toDomain() }
    }
  }

  fun getMealsForDatesFlow(dates: List<String>): Flow<List<Meal>> {
    return mealDao.getMealsForDatesFlow(dates).map { list ->
      list.map { it.toDomain() }
    }
  }

  suspend fun addMeal(meal: Meal): Long {
    return mealDao.insertMeal(
      MealEntity(
        id = meal.id,
        title = meal.title,
        calories = meal.calories,
        category = meal.category.name,
        dateString = meal.dateString,
        timestamp = meal.timestamp
      )
    )
  }

  suspend fun deleteMeal(id: Long) {
    mealDao.deleteMealById(id)
  }

  private fun MealEntity.toDomain(): Meal {
    val cat = try {
      MealCategory.valueOf(category)
    } catch (_: Exception) {
      MealCategory.OTHER
    }
    return Meal(
      id = id,
      title = title,
      calories = calories,
      category = cat,
      dateString = dateString,
      timestamp = timestamp
    )
  }
}

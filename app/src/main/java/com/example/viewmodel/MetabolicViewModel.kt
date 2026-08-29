package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.repository.MetabolicRepository
import com.example.model.ActivityLevel
import com.example.model.AppSettings
import com.example.model.AppThemeMode
import com.example.model.Gender
import com.example.model.Meal
import com.example.model.MealCategory
import com.example.model.MetabolicCalculationResult
import com.example.model.MetabolicCalculator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

enum class HubTab(val title: String, val subtitle: String) {
  PROFILE("Perfil", "TMB"),
  MEALS("Refeições", "Calorias"),
  SETTINGS("Configurações", "Aparência")
}

enum class TimeframeMode(val label: String) {
  DAILY("Diário"),
  WEEKLY("Semanal")
}

enum class TargetComparisonType(val label: String) {
  DAILY_EXPENDITURE("Gasto Total"),
  BMR("TMB (Repouso)")
}

data class ProfileFormState(
  val ageInput: String = "",
  val weightInput: String = "",
  val heightInput: String = "",
  val gender: Gender = Gender.MALE,
  val activityLevel: ActivityLevel = ActivityLevel.SEDENTARY,
  val result: MetabolicCalculationResult? = null,
  val ageError: String? = null,
  val weightError: String? = null,
  val heightError: String? = null,
  val isSaved: Boolean = false
) {
  val isValid: Boolean
    get() {
      val age = ageInput.toIntOrNull()
      val weight = weightInput.replace(',', '.').toDoubleOrNull()
      val height = heightInput.replace(',', '.').toDoubleOrNull()

      return age != null && age in 1..120 &&
             weight != null && weight in 10.0..400.0 &&
             height != null && height in 50.0..260.0
    }
}

data class AddMealDialogState(
  val isVisible: Boolean = false,
  val title: String = "",
  val calories: String = "",
  val category: MealCategory = MealCategory.LUNCH,
  val caloriesError: String? = null
)

class MetabolicViewModel(
  private val repository: MetabolicRepository
) : ViewModel() {

  private val _selectedTab = MutableStateFlow(HubTab.PROFILE)
  val selectedTab: StateFlow<HubTab> = _selectedTab.asStateFlow()

  private val _profileState = MutableStateFlow(ProfileFormState())
  val profileState: StateFlow<ProfileFormState> = _profileState.asStateFlow()

  private val _timeframeMode = MutableStateFlow(TimeframeMode.DAILY)
  val timeframeMode: StateFlow<TimeframeMode> = _timeframeMode.asStateFlow()

  private val _targetComparisonType = MutableStateFlow(TargetComparisonType.DAILY_EXPENDITURE)
  val targetComparisonType: StateFlow<TargetComparisonType> = _targetComparisonType.asStateFlow()

  private val _selectedDate = MutableStateFlow(LocalDate.now())
  val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

  private val _addMealDialogState = MutableStateFlow(AddMealDialogState())
  val addMealDialogState: StateFlow<AddMealDialogState> = _addMealDialogState.asStateFlow()

  // App Settings from Room (Theme Mode & Accent Color)
  val appSettings: StateFlow<AppSettings> = repository.settingsFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

  // Saved User Profile from Room
  val savedProfile: StateFlow<MetabolicCalculationResult?> = repository.userProfileFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  @OptIn(ExperimentalCoroutinesApi::class)
  val currentMeals: StateFlow<List<Meal>> = combine(_selectedDate, _timeframeMode) { date, mode ->
    Pair(date, mode)
  }.flatMapLatest { (date, mode) ->
    if (mode == TimeframeMode.DAILY) {
      repository.getMealsByDateFlow(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
    } else {
      val dates = getWeekDates(date).map { it.format(DateTimeFormatter.ISO_LOCAL_DATE) }
      repository.getMealsForDatesFlow(dates)
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  init {
    // Populate form with saved profile if available
    viewModelScope.launch {
      repository.userProfileFlow.collect { saved ->
        if (saved != null && _profileState.value.result == null) {
          _profileState.update { current ->
            current.copy(
              ageInput = saved.age.toString(),
              weightInput = if (saved.weightKg % 1.0 == 0.0) saved.weightKg.toInt().toString() else saved.weightKg.toString(),
              heightInput = if (saved.heightCm % 1.0 == 0.0) saved.heightCm.toInt().toString() else saved.heightCm.toString(),
              gender = saved.gender,
              activityLevel = saved.activityLevel,
              result = saved,
              isSaved = true
            )
          }
        }
      }
    }
  }

  fun selectTab(tab: HubTab) {
    _selectedTab.value = tab
  }

  fun setTimeframeMode(mode: TimeframeMode) {
    _timeframeMode.value = mode
  }

  fun setTargetComparisonType(type: TargetComparisonType) {
    _targetComparisonType.value = type
  }

  fun selectPreviousDay() {
    _selectedDate.update { it.minusDays(1) }
  }

  fun selectNextDay() {
    _selectedDate.update { it.plusDays(1) }
  }

  fun selectToday() {
    _selectedDate.value = LocalDate.now()
  }

  // Settings Actions
  fun onThemeModeChanged(mode: AppThemeMode) {
    val current = appSettings.value
    viewModelScope.launch {
      repository.saveSettings(current.copy(themeMode = mode))
    }
  }

  fun onAccentColorChanged(hexColor: String) {
    val current = appSettings.value
    val formatted = if (!hexColor.startsWith("#")) "#$hexColor" else hexColor
    viewModelScope.launch {
      repository.saveSettings(current.copy(accentColorHex = formatted.uppercase()))
    }
  }

  // Profile Form actions
  fun onAgeChanged(age: String) {
    val filtered = age.filter { it.isDigit() }.take(3)
    _profileState.update { it.copy(ageInput = filtered, ageError = null) }
  }

  fun onWeightChanged(weight: String) {
    val sanitized = weight.replace(',', '.')
    val valid = sanitized.count { it == '.' } <= 1 && sanitized.all { it.isDigit() || it == '.' }
    if (valid && sanitized.length <= 6) {
      _profileState.update { it.copy(weightInput = weight, weightError = null) }
    }
  }

  fun onHeightChanged(height: String) {
    val sanitized = height.replace(',', '.')
    val valid = sanitized.count { it == '.' } <= 1 && sanitized.all { it.isDigit() || it == '.' }
    if (valid && sanitized.length <= 6) {
      _profileState.update { it.copy(heightInput = height, heightError = null) }
    }
  }

  fun onGenderChanged(gender: Gender) {
    _profileState.update { current ->
      val updated = current.copy(gender = gender)
      if (updated.result != null && updated.isValid) {
        val res = calculateFromState(updated)
        updated.copy(result = res)
      } else {
        updated
      }
    }
  }

  fun onActivityLevelChanged(activityLevel: ActivityLevel) {
    _profileState.update { current ->
      val updated = current.copy(activityLevel = activityLevel)
      if (updated.result != null && updated.isValid) {
        val res = calculateFromState(updated)
        updated.copy(result = res)
      } else {
        updated
      }
    }
  }

  fun calculateProfile() {
    val current = _profileState.value
    var hasError = false
    var ageErr: String? = null
    var weightErr: String? = null
    var heightErr: String? = null

    val age = current.ageInput.toIntOrNull()
    if (age == null || age !in 1..120) {
      ageErr = "Informe uma idade válida (1 a 120 anos)"
      hasError = true
    }

    val weight = current.weightInput.replace(',', '.').toDoubleOrNull()
    if (weight == null || weight !in 10.0..400.0) {
      weightErr = "Informe um peso válido (10 a 400 kg)"
      hasError = true
    }

    val height = current.heightInput.replace(',', '.').toDoubleOrNull()
    if (height == null || height !in 50.0..260.0) {
      heightErr = "Informe uma altura válida (50 a 260 cm)"
      hasError = true
    }

    if (hasError) {
      _profileState.update {
        it.copy(
          ageError = ageErr,
          weightError = weightErr,
          heightError = heightErr
        )
      }
      return
    }

    val result = calculateFromState(current)
    if (result != null) {
      _profileState.update {
        it.copy(
          result = result,
          ageError = null,
          weightError = null,
          heightError = null,
          isSaved = true
        )
      }
      viewModelScope.launch {
        repository.saveUserProfile(result)
      }
    }
  }

  fun resetProfile() {
    _profileState.value = ProfileFormState()
  }

  private fun calculateFromState(state: ProfileFormState): MetabolicCalculationResult? {
    val age = state.ageInput.toIntOrNull() ?: return null
    val weight = state.weightInput.replace(',', '.').toDoubleOrNull() ?: return null
    val height = state.heightInput.replace(',', '.').toDoubleOrNull() ?: return null

    return MetabolicCalculator.calculate(
      weightKg = weight,
      heightCm = height,
      ageYears = age,
      gender = state.gender,
      activityLevel = state.activityLevel
    )
  }

  // Meal Management
  fun openAddMealDialog(category: MealCategory = MealCategory.LUNCH) {
    _addMealDialogState.value = AddMealDialogState(
      isVisible = true,
      title = "",
      calories = "",
      category = category,
      caloriesError = null
    )
  }

  fun closeAddMealDialog() {
    _addMealDialogState.value = AddMealDialogState(isVisible = false)
  }

  fun onMealTitleChanged(title: String) {
    _addMealDialogState.update { it.copy(title = title) }
  }

  fun onMealCaloriesChanged(calories: String) {
    val filtered = calories.filter { it.isDigit() }.take(5)
    _addMealDialogState.update { it.copy(calories = filtered, caloriesError = null) }
  }

  fun onMealCategoryChanged(category: MealCategory) {
    _addMealDialogState.update { it.copy(category = category) }
  }

  fun saveMeal() {
    val dialog = _addMealDialogState.value
    val cal = dialog.calories.toIntOrNull()
    if (cal == null || cal <= 0 || cal > 10000) {
      _addMealDialogState.update { it.copy(caloriesError = "Informe as calorias (1 a 10000 kcal)") }
      return
    }

    val title = if (dialog.title.isBlank()) dialog.category.displayName else dialog.title.trim()
    val dateStr = _selectedDate.value.format(DateTimeFormatter.ISO_LOCAL_DATE)

    viewModelScope.launch {
      repository.addMeal(
        Meal(
          title = title,
          calories = cal,
          category = dialog.category,
          dateString = dateStr,
          timestamp = System.currentTimeMillis()
        )
      )
      closeAddMealDialog()
    }
  }

  fun deleteMeal(id: Long) {
    viewModelScope.launch {
      repository.deleteMeal(id)
    }
  }

  private fun getWeekDates(date: LocalDate): List<LocalDate> {
    val startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    return (0..6).map { startOfWeek.plusDays(it.toLong()) }
  }

  companion object {
    fun provideFactory(repository: MetabolicRepository): ViewModelProvider.Factory =
      object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          return MetabolicViewModel(repository) as T
        }
      }
  }
}

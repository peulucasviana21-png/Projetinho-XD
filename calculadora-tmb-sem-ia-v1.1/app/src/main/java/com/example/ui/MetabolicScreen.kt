package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.components.AddMealDialog
import com.example.ui.components.EditProfileHeaderDialog
import com.example.ui.components.HubBottomNavigationBar
import com.example.ui.screens.MealsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.AppTheme
import com.example.viewmodel.HubTab
import com.example.viewmodel.MetabolicViewModel

@Composable
fun MetabolicScreen(
  viewModel: MetabolicViewModel,
  modifier: Modifier = Modifier
) {
  val selectedTab by viewModel.selectedTab.collectAsState()
  val profileSubSection by viewModel.profileSubSection.collectAsState()
  val profileState by viewModel.profileState.collectAsState()
  val editHeaderDialogState by viewModel.editHeaderDialogState.collectAsState()
  val savedProfile by viewModel.savedProfile.collectAsState()
  val currentMeals by viewModel.currentMeals.collectAsState()
  val timeframeMode by viewModel.timeframeMode.collectAsState()
  val targetComparisonType by viewModel.targetComparisonType.collectAsState()
  val selectedDate by viewModel.selectedDate.collectAsState()
  val addMealDialogState by viewModel.addMealDialogState.collectAsState()
  val appSettings by viewModel.appSettings.collectAsState()

  val scrollState = rememberScrollState()
  val colors = AppTheme.colors

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = colors.background,
    contentWindowInsets = WindowInsets.safeDrawing,
    bottomBar = {
      HubBottomNavigationBar(
        selectedTab = selectedTab,
        onTabSelected = { viewModel.selectTab(it) }
      )
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      contentAlignment = Alignment.TopCenter
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .widthIn(max = 600.dp)
          .verticalScroll(scrollState)
          .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        when (selectedTab) {
          HubTab.PROFILE -> {
            ProfileScreen(
              state = profileState,
              subSection = profileSubSection,
              themeMode = appSettings.themeMode,
              accentColorHex = appSettings.accentColorHex,
              onSubSectionSelected = { viewModel.selectProfileSubSection(it) },
              onOpenEditHeader = { viewModel.openEditHeaderDialog() },
              onAgeChanged = { viewModel.onAgeChanged(it) },
              onWeightChanged = { viewModel.onWeightChanged(it) },
              onHeightChanged = { viewModel.onHeightChanged(it) },
              onGenderChanged = { viewModel.onGenderChanged(it) },
              onActivityLevelChanged = { viewModel.onActivityLevelChanged(it) },
              onCalculate = { viewModel.calculateProfile() },
              onReset = { viewModel.resetProfile() },
              onNavigateToMeals = { viewModel.selectTab(HubTab.MEALS) },
              onThemeModeChanged = { viewModel.onThemeModeChanged(it) },
              onAccentColorChanged = { viewModel.onAccentColorChanged(it) }
            )
          }

          HubTab.MEALS -> {
            MealsScreen(
              meals = currentMeals,
              savedProfile = savedProfile ?: profileState.result,
              timeframeMode = timeframeMode,
              targetComparisonType = targetComparisonType,
              selectedDate = selectedDate,
              onTimeframeModeChanged = { viewModel.setTimeframeMode(it) },
              onTargetComparisonTypeChanged = { viewModel.setTargetComparisonType(it) },
              onPreviousDay = { viewModel.selectPreviousDay() },
              onNextDay = { viewModel.selectNextDay() },
              onSelectToday = { viewModel.selectToday() },
              onAddMealClick = { viewModel.openAddMealDialog(it) },
              onDeleteMeal = { viewModel.deleteMeal(it) },
              onNavigateToProfile = { viewModel.selectTab(HubTab.PROFILE) }
            )
          }
        }
      }
    }
  }

  // Edit Profile Header Dialog (Avatar, Banner, Name, Custom Images)
  EditProfileHeaderDialog(
    state = editHeaderDialogState,
    onNameChange = { viewModel.onHeaderNameChanged(it) },
    onAvatarSelect = { viewModel.onHeaderAvatarSelected(it) },
    onBannerSelect = { viewModel.onHeaderBannerSelected(it) },
    onCustomAvatarSelect = { viewModel.onHeaderCustomAvatarSelected(it) },
    onCustomBannerSelect = { viewModel.onHeaderCustomBannerSelected(it) },
    onConfirm = { viewModel.saveProfileHeader() },
    onDismiss = { viewModel.closeEditHeaderDialog() }
  )

  // Add Meal Dialog
  AddMealDialog(
    state = addMealDialogState,
    onTitleChange = { viewModel.onMealTitleChanged(it) },
    onCaloriesChange = { viewModel.onMealCaloriesChanged(it) },
    onCategoryChange = { viewModel.onMealCategoryChanged(it) },
    onConfirm = { viewModel.saveMeal() },
    onDismiss = { viewModel.closeAddMealDialog() }
  )
}


package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.data.local.AppDatabase
import com.example.data.repository.MetabolicRepository
import com.example.ui.MetabolicScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MetabolicViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: MetabolicViewModel by viewModels {
    val database = AppDatabase.getInstance(applicationContext)
    val repository = MetabolicRepository(
      userProfileDao = database.userProfileDao(),
      mealDao = database.mealDao(),
      settingsDao = database.settingsDao()
    )
    MetabolicViewModel.provideFactory(repository)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val appSettings by viewModel.appSettings.collectAsState()

      MyApplicationTheme(
        themeMode = appSettings.themeMode,
        accentColorHex = appSettings.accentColorHex
      ) {
        MetabolicScreen(viewModel = viewModel)
      }
    }
  }
}

package com.example

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.repository.MetabolicRepository
import com.example.model.AppThemeMode
import com.example.ui.MetabolicScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MetabolicViewModel
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun metabolic_screen_screenshot() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val inMemoryDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    val repository = MetabolicRepository(
      userProfileDao = inMemoryDb.userProfileDao(),
      mealDao = inMemoryDb.mealDao(),
      settingsDao = inMemoryDb.settingsDao()
    )
    val viewModel = MetabolicViewModel(repository).apply {
      onAgeChanged("28")
      onWeightChanged("70")
      onHeightChanged("175")
      calculateProfile()
    }

    composeTestRule.setContent {
      MyApplicationTheme(
        themeMode = AppThemeMode.DARK,
        accentColorHex = "#A8E6CF"
      ) {
        MetabolicScreen(viewModel = viewModel)
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}

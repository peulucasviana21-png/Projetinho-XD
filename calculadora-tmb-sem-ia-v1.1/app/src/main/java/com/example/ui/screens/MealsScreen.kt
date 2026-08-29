package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Meal
import com.example.model.MealCategory
import com.example.model.MetabolicCalculationResult
import com.example.ui.theme.AppTheme
import com.example.viewmodel.TargetComparisonType
import com.example.viewmodel.TimeframeMode
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
fun MealsScreen(
  meals: List<Meal>,
  savedProfile: MetabolicCalculationResult?,
  timeframeMode: TimeframeMode,
  targetComparisonType: TargetComparisonType,
  selectedDate: LocalDate,
  onTimeframeModeChanged: (TimeframeMode) -> Unit,
  onTargetComparisonTypeChanged: (TargetComparisonType) -> Unit,
  onPreviousDay: () -> Unit,
  onNextDay: () -> Unit,
  onSelectToday: () -> Unit,
  onAddMealClick: (MealCategory) -> Unit,
  onDeleteMeal: (Long) -> Unit,
  onNavigateToProfile: () -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = AppTheme.colors
  val totalCalories = remember(meals) { meals.sumOf { it.calories } }

  val baseTarget = when (targetComparisonType) {
    TargetComparisonType.DAILY_EXPENDITURE -> savedProfile?.dailyCaloricExpenditure ?: 2000.0
    TargetComparisonType.BMR -> savedProfile?.bmr ?: 1600.0
  }

  val targetCalories = if (timeframeMode == TimeframeMode.DAILY) {
    baseTarget.toInt()
  } else {
    (baseTarget * 7).toInt()
  }

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.Start
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "Registro de Calorias",
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            fontSize = 22.sp
          )
        )
        Text(
          text = "Acompanhe refeições e compare com seu gasto.",
          style = MaterialTheme.typography.bodyMedium.copy(
            color = colors.textSecondary,
            fontSize = 13.sp
          )
        )
      }

      Button(
        onClick = { onAddMealClick(MealCategory.LUNCH) },
        colors = ButtonDefaults.buttonColors(
          containerColor = colors.accent,
          contentColor = colors.onAccent
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.testTag("add_meal_top_button")
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Adicionar Refeição",
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Adicionar",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Timeframe Mode Selector: Diário vs Semanal
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .background(colors.surfaceCard)
        .border(1.dp, colors.borderSubtle, RoundedCornerShape(10.dp))
        .padding(3.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      TimeframeMode.entries.forEach { mode ->
        val isSelected = mode == timeframeMode
        val tag = if (mode == TimeframeMode.DAILY) "mode_daily" else "mode_weekly"

        Box(
          modifier = Modifier
            .weight(1f)
            .height(38.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(if (isSelected) colors.accentContainer else Color.Transparent)
            .border(
              width = if (isSelected) 1.dp else 0.dp,
              color = if (isSelected) colors.accent else Color.Transparent,
              shape = RoundedCornerShape(7.dp)
            )
            .clickable { onTimeframeModeChanged(mode) }
            .testTag(tag),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = mode.label,
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
              color = if (isSelected) colors.accent else colors.textSecondary,
              fontSize = 13.sp
            )
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Date Navigation Header
    if (timeframeMode == TimeframeMode.DAILY) {
      DailyDateNavigator(
        selectedDate = selectedDate,
        onPreviousDay = onPreviousDay,
        onNextDay = onNextDay,
        onSelectToday = onSelectToday
      )
    } else {
      WeeklyDateHeader(selectedDate = selectedDate)
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Calorie Comparison Card
    CalorieComparisonCard(
      totalConsumed = totalCalories,
      targetCalories = targetCalories,
      timeframeMode = timeframeMode,
      targetComparisonType = targetComparisonType,
      hasSavedProfile = savedProfile != null,
      onTargetComparisonTypeChanged = onTargetComparisonTypeChanged,
      onNavigateToProfile = onNavigateToProfile
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Quick Add Categories
    QuickAddCategoryRow(onCategoryClick = onAddMealClick)

    Spacer(modifier = Modifier.height(20.dp))

    // Meals List Section
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = if (timeframeMode == TimeframeMode.DAILY) "Refeições do Dia (${meals.size})" else "Refeições da Semana (${meals.size})",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.SemiBold,
          color = colors.textPrimary,
          fontSize = 15.sp
        )
      )

      if (meals.isNotEmpty()) {
        Text(
          text = "Total: $totalCalories kcal",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.accent,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    if (meals.isEmpty()) {
      EmptyMealsState(
        timeframeMode = timeframeMode,
        onAddMeal = { onAddMealClick(MealCategory.LUNCH) }
      )
    } else {
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        meals.forEach { meal ->
          MealItemCard(
            meal = meal,
            onDelete = { onDeleteMeal(meal.id) }
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(30.dp))
  }
}

@Composable
private fun DailyDateNavigator(
  selectedDate: LocalDate,
  onPreviousDay: () -> Unit,
  onNextDay: () -> Unit,
  onSelectToday: () -> Unit
) {
  val colors = AppTheme.colors
  val isToday = selectedDate == LocalDate.now()
  val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM", Locale("pt", "BR"))
  val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE", Locale("pt", "BR"))

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(colors.surfaceCard)
      .border(1.dp, colors.borderSubtle, RoundedCornerShape(10.dp))
      .padding(horizontal = 8.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    IconButton(
      onClick = onPreviousDay,
      modifier = Modifier.size(36.dp).testTag("prev_day_button")
    ) {
      Icon(
        imageVector = Icons.Default.ChevronLeft,
        contentDescription = "Dia anterior",
        tint = colors.textSecondary
      )
    }

    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .clickable(onClick = onSelectToday)
        .padding(horizontal = 8.dp)
    ) {
      Text(
        text = if (isToday) "Hoje, ${selectedDate.format(formatter)}" else selectedDate.format(formatter),
        style = MaterialTheme.typography.bodyMedium.copy(
          fontWeight = FontWeight.SemiBold,
          color = if (isToday) colors.accent else colors.textPrimary,
          fontSize = 14.sp
        )
      )
      Text(
        text = selectedDate.format(dayOfWeekFormatter).replaceFirstChar { it.uppercase() },
        style = MaterialTheme.typography.bodySmall.copy(
          color = colors.textSecondary,
          fontSize = 11.sp
        )
      )
    }

    IconButton(
      onClick = onNextDay,
      modifier = Modifier.size(36.dp).testTag("next_day_button")
    ) {
      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = "Próximo dia",
        tint = colors.textSecondary
      )
    }
  }
}

@Composable
private fun WeeklyDateHeader(selectedDate: LocalDate) {
  val colors = AppTheme.colors
  val startOfWeek = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
  val endOfWeek = startOfWeek.plusDays(6)
  val formatter = DateTimeFormatter.ofPattern("dd 'de' MMM", Locale("pt", "BR"))

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(colors.surfaceCard)
      .border(1.dp, colors.borderSubtle, RoundedCornerShape(10.dp))
      .padding(horizontal = 14.dp, vertical = 10.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "Semana: ${startOfWeek.format(formatter)} a ${endOfWeek.format(formatter)}",
        style = MaterialTheme.typography.bodyMedium.copy(
          fontWeight = FontWeight.SemiBold,
          color = colors.accent,
          fontSize = 14.sp
        )
      )
      Text(
        text = "Consolidado dos últimos 7 dias",
        style = MaterialTheme.typography.bodySmall.copy(
          color = colors.textSecondary,
          fontSize = 11.sp
        )
      )
    }
  }
}

@Composable
private fun CalorieComparisonCard(
  totalConsumed: Int,
  targetCalories: Int,
  timeframeMode: TimeframeMode,
  targetComparisonType: TargetComparisonType,
  hasSavedProfile: Boolean,
  onTargetComparisonTypeChanged: (TargetComparisonType) -> Unit,
  onNavigateToProfile: () -> Unit
) {
  val colors = AppTheme.colors
  val remaining = targetCalories - totalConsumed
  val progress = if (targetCalories > 0) (totalConsumed.toFloat() / targetCalories.toFloat()).coerceIn(0f, 1f) else 0f
  val isOver = remaining < 0

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = colors.surfaceCard,
    border = BorderStroke(1.dp, if (isOver) colors.error.copy(alpha = 0.6f) else colors.accent),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Card Header with Target Switcher
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = if (timeframeMode == TimeframeMode.DAILY) "Balanço Calórico Diário" else "Balanço Calórico Semanal",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.SemiBold,
              color = colors.textPrimary,
              fontSize = 15.sp
            )
          )
          Text(
            text = "Comparado a: ${targetComparisonType.label}",
            style = MaterialTheme.typography.bodySmall.copy(
              color = colors.textSecondary,
              fontSize = 11.sp
            )
          )
        }

        // Toggle button between Expenditure and BMR
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(colors.surfaceVariant)
            .border(1.dp, colors.borderSubtle, RoundedCornerShape(6.dp))
            .padding(2.dp)
        ) {
          TargetComparisonType.entries.forEach { type ->
            val isSel = type == targetComparisonType
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(if (isSel) colors.accentContainer else Color.Transparent)
                .clickable { onTargetComparisonTypeChanged(type) }
                .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
              Text(
                text = if (type == TargetComparisonType.DAILY_EXPENDITURE) "Gasto" else "TMB",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = if (isSel) colors.accent else colors.textSecondary,
                  fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 11.sp
                )
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Numbers Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
      ) {
        // Consumed
        Column {
          Text(
            text = "Ingerido",
            style = MaterialTheme.typography.bodySmall.copy(
              color = colors.textSecondary,
              fontSize = 12.sp
            )
          )
          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = "$totalConsumed",
              style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = if (isOver) colors.error else colors.accent,
                fontSize = 24.sp
              )
            )
            Text(
              text = " kcal",
              style = MaterialTheme.typography.bodySmall.copy(
                color = colors.textTertiary,
                fontSize = 12.sp
              ),
              modifier = Modifier.padding(bottom = 2.dp)
            )
          }
        }

        // Target
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "Meta Estimada",
            style = MaterialTheme.typography.bodySmall.copy(
              color = colors.textSecondary,
              fontSize = 12.sp
            )
          )
          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = "$targetCalories",
              style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary,
                fontSize = 20.sp
              )
            )
            Text(
              text = " kcal",
              style = MaterialTheme.typography.bodySmall.copy(
                color = colors.textTertiary,
                fontSize = 12.sp
              ),
              modifier = Modifier.padding(bottom = 2.dp)
            )
          }
        }

        // Balance / Remaining
        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = if (isOver) "Superávit" else "Restante",
            style = MaterialTheme.typography.bodySmall.copy(
              color = if (isOver) colors.error else colors.textSecondary,
              fontSize = 12.sp,
              fontWeight = if (isOver) FontWeight.Bold else FontWeight.Normal
            )
          )
          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = if (isOver) "+${Math.abs(remaining)}" else "$remaining",
              style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = if (isOver) colors.error else colors.textPrimary,
                fontSize = 24.sp
              )
            )
            Text(
              text = " kcal",
              style = MaterialTheme.typography.bodySmall.copy(
                color = colors.textTertiary,
                fontSize = 12.sp
              ),
              modifier = Modifier.padding(bottom = 2.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Progress Bar
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp)),
        color = if (isOver) colors.error else colors.accent,
        trackColor = colors.surfaceVariant
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Informative status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "${(progress * 100).toInt()}% da meta atingida",
          style = MaterialTheme.typography.bodySmall.copy(
            color = if (isOver) colors.error else colors.accent,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        )

        if (!hasSavedProfile) {
          Text(
            text = "Configurar Perfil ➜",
            style = MaterialTheme.typography.bodySmall.copy(
              color = colors.accent,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.clickable(onClick = onNavigateToProfile)
          )
        }
      }
    }
  }
}

@Composable
private fun QuickAddCategoryRow(
  onCategoryClick: (MealCategory) -> Unit
) {
  val colors = AppTheme.colors
  Column {
    Text(
      text = "Adicionar Refeição Rápida",
      style = MaterialTheme.typography.bodySmall.copy(
        color = colors.textSecondary,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
      )
    )
    Spacer(modifier = Modifier.height(6.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      MealCategory.entries.take(4).forEach { category ->
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(colors.surfaceCard)
            .border(1.dp, colors.borderSubtle, RoundedCornerShape(8.dp))
            .clickable { onCategoryClick(category) }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = category.iconLabel, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = category.displayName.split(" ").first(),
              style = MaterialTheme.typography.bodySmall.copy(
                color = colors.textSecondary,
                fontSize = 11.sp
              )
            )
          }
        }
      }
    }
  }
}

@Composable
private fun MealItemCard(
  meal: Meal,
  onDelete: () -> Unit
) {
  val colors = AppTheme.colors
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(colors.surfaceCard)
      .border(1.dp, colors.borderSubtle, RoundedCornerShape(10.dp))
      .padding(horizontal = 12.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1f)
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(colors.surfaceVariant),
        contentAlignment = Alignment.Center
      ) {
        Text(text = meal.category.iconLabel, fontSize = 16.sp)
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column {
        Text(
          text = meal.title,
          style = MaterialTheme.typography.bodyMedium.copy(
            color = colors.textPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
          )
        )
        Text(
          text = meal.category.displayName,
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.textSecondary,
            fontSize = 11.sp
          )
        )
      }
    }

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(colors.accentContainer)
          .padding(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Text(
          text = "${meal.calories} kcal",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.accent,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        )
      }

      IconButton(
        onClick = onDelete,
        modifier = Modifier.size(32.dp).testTag("delete_meal_${meal.id}")
      ) {
        Icon(
          imageVector = Icons.Default.Delete,
          contentDescription = "Excluir refeição",
          tint = colors.textTertiary,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

@Composable
private fun EmptyMealsState(
  timeframeMode: TimeframeMode,
  onAddMeal: () -> Unit
) {
  val colors = AppTheme.colors
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(colors.surfaceCard)
      .border(1.dp, colors.borderSubtle, RoundedCornerShape(12.dp))
      .padding(24.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(text = "🍽️", fontSize = 32.sp)
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = if (timeframeMode == TimeframeMode.DAILY) "Nenhuma refeição registrada hoje" else "Nenhuma refeição registrada nesta semana",
        style = MaterialTheme.typography.bodyMedium.copy(
          color = colors.textPrimary,
          fontWeight = FontWeight.SemiBold,
          fontSize = 14.sp
        ),
        textAlign = TextAlign.Center
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Adicione suas refeições para comparar seu consumo com sua meta.",
        style = MaterialTheme.typography.bodySmall.copy(
          color = colors.textSecondary,
          fontSize = 12.sp
        ),
        textAlign = TextAlign.Center
      )
      Spacer(modifier = Modifier.height(14.dp))
      Button(
        onClick = onAddMeal,
        colors = ButtonDefaults.buttonColors(
          containerColor = colors.accent,
          contentColor = colors.onAccent
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.testTag("empty_state_add_button")
      ) {
        Text("Registrar Primeira Refeição", fontSize = 13.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}

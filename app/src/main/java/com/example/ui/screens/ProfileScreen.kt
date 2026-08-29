package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MinimalActivityLevelSelector
import com.example.ui.components.MinimalGenderSelector
import com.example.ui.components.MinimalNumericInputField
import com.example.ui.components.ResultCard
import com.example.ui.theme.AppTheme
import com.example.viewmodel.ProfileFormState

@Composable
fun ProfileScreen(
  state: ProfileFormState,
  onAgeChanged: (String) -> Unit,
  onWeightChanged: (String) -> Unit,
  onHeightChanged: (String) -> Unit,
  onGenderChanged: (com.example.model.Gender) -> Unit,
  onActivityLevelChanged: (com.example.model.ActivityLevel) -> Unit,
  onCalculate: () -> Unit,
  onReset: () -> Unit,
  onNavigateToMeals: () -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = AppTheme.colors
  val focusManager = LocalFocusManager.current

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.Start
  ) {
    // Header
    Text(
      text = "Dados do Perfil",
      style = MaterialTheme.typography.headlineMedium.copy(
        fontWeight = FontWeight.Bold,
        color = colors.textPrimary,
        fontSize = 22.sp
      )
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = "Preencha suas informações corporais para calcular a TMB e seu Gasto Calórico Diário.",
      style = MaterialTheme.typography.bodyMedium.copy(
        color = colors.textSecondary,
        fontSize = 14.sp
      )
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Gender selector
    MinimalGenderSelector(
      selectedGender = state.gender,
      onGenderSelected = onGenderChanged,
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Age input
    MinimalNumericInputField(
      value = state.ageInput,
      onValueChange = onAgeChanged,
      label = "Idade",
      unit = "anos",
      errorMessage = state.ageError,
      testTag = "age_input",
      keyboardType = KeyboardType.Number,
      imeAction = ImeAction.Next,
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Weight and Height inputs
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      MinimalNumericInputField(
        value = state.weightInput,
        onValueChange = onWeightChanged,
        label = "Peso",
        unit = "kg",
        errorMessage = state.weightError,
        testTag = "weight_input",
        keyboardType = KeyboardType.Decimal,
        imeAction = ImeAction.Next,
        modifier = Modifier.weight(1f)
      )

      MinimalNumericInputField(
        value = state.heightInput,
        onValueChange = onHeightChanged,
        label = "Altura",
        unit = "cm",
        errorMessage = state.heightError,
        testTag = "height_input",
        keyboardType = KeyboardType.Decimal,
        imeAction = ImeAction.Done,
        onImeAction = {
          focusManager.clearFocus()
          onCalculate()
        },
        modifier = Modifier.weight(1f)
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Activity Level selector
    MinimalActivityLevelSelector(
      selectedActivity = state.activityLevel,
      onActivitySelected = onActivityLevelChanged,
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(22.dp))

    // Action buttons
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(
        onClick = {
          focusManager.clearFocus()
          onCalculate()
        },
        colors = ButtonDefaults.buttonColors(
          containerColor = colors.accent,
          contentColor = colors.onAccent
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
          .weight(1f)
          .height(48.dp)
          .testTag("calculate_button")
      ) {
        Text(
          text = if (state.isSaved) "Recalcular & Salvar" else "Calcular TMB",
          style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
        )
      }

      if (state.result != null || state.ageInput.isNotEmpty() || state.weightInput.isNotEmpty() || state.heightInput.isNotEmpty()) {
        OutlinedButton(
          onClick = {
            focusManager.clearFocus()
            onReset()
          },
          shape = RoundedCornerShape(10.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderSubtle),
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = colors.textSecondary
          ),
          modifier = Modifier
            .height(48.dp)
            .testTag("reset_button")
        ) {
          Text(
            text = "Limpar",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Medium,
              fontSize = 14.sp
            )
          )
        }
      }
    }

    // Results
    if (state.result != null) {
      Spacer(modifier = Modifier.height(24.dp))
      ResultCard(result = state.result)

      Spacer(modifier = Modifier.height(14.dp))

      // Direct call to action to track meals
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(colors.surfaceCard)
          .border(1.dp, colors.borderSubtle, RoundedCornerShape(10.dp))
          .padding(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Pronto para comparar?",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = colors.textPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
              )
            )
            Text(
              text = "Acompanhe suas calorias ingeridas no painel de Refeições.",
              style = MaterialTheme.typography.bodySmall.copy(
                color = colors.textSecondary,
                fontSize = 12.sp
              )
            )
          }

          Button(
            onClick = onNavigateToMeals,
            colors = ButtonDefaults.buttonColors(
              containerColor = colors.accentContainer,
              contentColor = colors.accent
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.accent),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(start = 8.dp)
          ) {
            Text("Ir para Refeições", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(28.dp))

    // Footer info
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Fórmula Mifflin-St Jeor (Padrão Ouro)",
        style = MaterialTheme.typography.bodySmall.copy(
          color = colors.textSecondary,
          fontWeight = FontWeight.Medium,
          fontSize = 12.sp
        )
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = "Homens: (10×peso) + (6.25×altura) − (5×idade) + 5\nMulheres: (10×peso) + (6.25×altura) − (5×idade) − 161",
        style = MaterialTheme.typography.bodySmall.copy(
          color = colors.textTertiary,
          fontSize = 11.sp,
          lineHeight = 15.sp
        ),
        textAlign = TextAlign.Center
      )
    }
  }
}

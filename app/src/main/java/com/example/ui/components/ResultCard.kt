package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MetabolicCalculationResult
import com.example.ui.theme.AppTheme
import java.util.Locale

@Composable
fun ResultCard(
  result: MetabolicCalculationResult,
  modifier: Modifier = Modifier
) {
  val colors = AppTheme.colors
  val bmrFormatted = String.format(Locale.US, "%.0f", result.bmr)
  val tdeeFormatted = String.format(Locale.US, "%.0f", result.dailyCaloricExpenditure)

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = colors.surfaceCard,
    border = BorderStroke(1.dp, colors.accent),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Resultado do Cálculo",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = colors.textPrimary,
            fontSize = 16.sp
          )
        )
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(colors.accentContainer)
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = "Mifflin-St Jeor",
            style = MaterialTheme.typography.labelSmall.copy(
              color = colors.accent,
              fontWeight = FontWeight.Medium,
              fontSize = 11.sp
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Highlighted Metric 1: TMB (Taxa Metabólica Basal)
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(colors.surfaceVariant)
          .border(1.dp, colors.borderSubtle, RoundedCornerShape(8.dp))
          .padding(14.dp)
          .testTag("tmb_result")
      ) {
        Text(
          text = "Taxa Metabólica Basal (TMB)",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.textSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          verticalAlignment = Alignment.Bottom,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = bmrFormatted,
            style = MaterialTheme.typography.headlineMedium.copy(
              color = colors.textPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 28.sp
            )
          )
          Text(
            text = "kcal / dia",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = colors.textTertiary,
              fontWeight = FontWeight.Normal,
              fontSize = 14.sp
            ),
            modifier = Modifier.padding(bottom = 3.dp)
          )
        }
        Text(
          text = "Energia mínima necessária para funções vitais em repouso.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.textTertiary,
            fontSize = 11.sp
          ),
          modifier = Modifier.padding(top = 2.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Highlighted Metric 2: Gasto Calórico Diário Total
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(colors.accentContainer)
          .border(1.dp, colors.accent, RoundedCornerShape(8.dp))
          .padding(14.dp)
          .testTag("tdee_result")
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Gasto Calórico Diário Total",
            style = MaterialTheme.typography.bodySmall.copy(
              color = colors.accent,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          )
          Text(
            text = "TMB × ${result.activityLevel.factor}",
            style = MaterialTheme.typography.labelSmall.copy(
              color = colors.accent.copy(alpha = 0.8f),
              fontSize = 11.sp
            )
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          verticalAlignment = Alignment.Bottom,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = tdeeFormatted,
            style = MaterialTheme.typography.headlineLarge.copy(
              color = colors.accent,
              fontWeight = FontWeight.Bold,
              fontSize = 32.sp
            )
          )
          Text(
            text = "kcal / dia",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = colors.accent.copy(alpha = 0.8f),
              fontWeight = FontWeight.Medium,
              fontSize = 14.sp
            ),
            modifier = Modifier.padding(bottom = 4.dp)
          )
        }
        Text(
          text = "Calorias estimadas gastas no dia de acordo com sua atividade.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.textSecondary,
            fontSize = 11.sp
          ),
          modifier = Modifier.padding(top = 2.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      HorizontalDivider(color = colors.borderSubtle, thickness = 1.dp)

      Spacer(modifier = Modifier.height(14.dp))

      // Reference Targets
      Text(
        text = "Metas Calóricas de Referência",
        style = MaterialTheme.typography.bodySmall.copy(
          color = colors.textSecondary,
          fontWeight = FontWeight.Medium,
          fontSize = 12.sp
        )
      )

      Spacer(modifier = Modifier.height(8.dp))

      val tdee = result.dailyCaloricExpenditure
      val loseWeight = String.format(Locale.US, "%.0f", (tdee - 400).coerceAtLeast(result.bmr))
      val maintainWeight = String.format(Locale.US, "%.0f", tdee)
      val gainWeight = String.format(Locale.US, "%.0f", tdee + 400)

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        GoalMiniCard(
          title = "Perder Peso",
          calories = loseWeight,
          modifier = Modifier.weight(1f)
        )
        GoalMiniCard(
          title = "Manter",
          calories = maintainWeight,
          modifier = Modifier.weight(1f)
        )
        GoalMiniCard(
          title = "Ganhar Massa",
          calories = gainWeight,
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
private fun GoalMiniCard(
  title: String,
  calories: String,
  modifier: Modifier = Modifier
) {
  val colors = AppTheme.colors
  Column(
    modifier = modifier
      .clip(RoundedCornerShape(6.dp))
      .background(colors.surfaceVariant)
      .border(1.dp, colors.borderSubtle, RoundedCornerShape(6.dp))
      .padding(vertical = 8.dp, horizontal = 6.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.bodySmall.copy(
        color = colors.textSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal
      )
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = "$calories kcal",
      style = MaterialTheme.typography.bodyMedium.copy(
        color = colors.textPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold
      )
    )
  }
}

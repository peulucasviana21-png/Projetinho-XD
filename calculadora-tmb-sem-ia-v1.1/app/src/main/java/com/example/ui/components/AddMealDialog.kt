package com.example.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.MealCategory
import com.example.ui.theme.AppTheme
import com.example.viewmodel.AddMealDialogState

@Composable
fun AddMealDialog(
  state: AddMealDialogState,
  onTitleChange: (String) -> Unit,
  onCaloriesChange: (String) -> Unit,
  onCategoryChange: (MealCategory) -> Unit,
  onConfirm: () -> Unit,
  onDismiss: () -> Unit
) {
  if (!state.isVisible) return
  val colors = AppTheme.colors

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = colors.surface,
      border = BorderStroke(1.dp, colors.borderSubtle),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = "Adicionar Refeição",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            fontSize = 18.sp
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Informe o que você ingeriu e as calorias.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.textSecondary,
            fontSize = 13.sp
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category Selector Chips
        Text(
          text = "Categoria",
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
          MealCategory.entries.take(3).forEach { category ->
            CategoryChip(
              category = category,
              isSelected = category == state.category,
              onSelect = { onCategoryChange(category) },
              modifier = Modifier.weight(1f)
            )
          }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          MealCategory.entries.drop(3).forEach { category ->
            CategoryChip(
              category = category,
              isSelected = category == state.category,
              onSelect = { onCategoryChange(category) },
              modifier = Modifier.weight(1f)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title Input (Optional description)
        Text(
          text = "Descrição (opcional)",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.textSecondary,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = state.title,
          onValueChange = onTitleChange,
          placeholder = {
            Text(
              text = "Ex: Arroz, frango e salada",
              style = MaterialTheme.typography.bodyMedium.copy(color = colors.textTertiary, fontSize = 14.sp)
            )
          },
          singleLine = true,
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colors.textPrimary,
            unfocusedTextColor = colors.textPrimary,
            focusedContainerColor = colors.surfaceCard,
            unfocusedContainerColor = colors.surfaceCard,
            cursorColor = colors.accent,
            focusedBorderColor = colors.accent,
            unfocusedBorderColor = colors.borderSubtle
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("meal_title_input")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Calories Input
        Text(
          text = "Calorias ingeridas *",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.textSecondary,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = state.calories,
          onValueChange = onCaloriesChange,
          placeholder = {
            Text(
              text = "Ex: 450",
              style = MaterialTheme.typography.bodyMedium.copy(color = colors.textTertiary, fontSize = 14.sp)
            )
          },
          trailingIcon = {
            Text(
              text = "kcal",
              style = MaterialTheme.typography.bodyMedium.copy(color = colors.textTertiary, fontSize = 13.sp),
              modifier = Modifier.padding(end = 12.dp)
            )
          },
          singleLine = true,
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
          ),
          isError = state.caloriesError != null,
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colors.textPrimary,
            unfocusedTextColor = colors.textPrimary,
            focusedContainerColor = colors.surfaceCard,
            unfocusedContainerColor = colors.surfaceCard,
            errorContainerColor = colors.surfaceCard,
            cursorColor = colors.accent,
            focusedBorderColor = colors.accent,
            unfocusedBorderColor = colors.borderSubtle,
            errorBorderColor = colors.error
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("meal_calories_input")
        )

        if (state.caloriesError != null) {
          Text(
            text = state.caloriesError,
            style = MaterialTheme.typography.bodySmall.copy(color = colors.error, fontSize = 11.sp),
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
          )
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, colors.borderSubtle),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textSecondary),
            modifier = Modifier
              .weight(1f)
              .height(46.dp)
          ) {
            Text("Cancelar", fontSize = 14.sp)
          }

          Button(
            onClick = onConfirm,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = colors.accent,
              contentColor = colors.onAccent
            ),
            modifier = Modifier
              .weight(1f)
              .height(46.dp)
              .testTag("save_meal_button")
          ) {
            Text("Salvar", fontWeight = FontWeight.Bold, fontSize = 14.sp)
          }
        }
      }
    }
  }
}

@Composable
private fun CategoryChip(
  category: MealCategory,
  isSelected: Boolean,
  onSelect: () -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = AppTheme.colors
  Box(
    modifier = modifier
      .height(38.dp)
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) colors.accentContainer else colors.surfaceCard)
      .border(
        width = 1.dp,
        color = if (isSelected) colors.accent else colors.borderSubtle,
        shape = RoundedCornerShape(8.dp)
      )
      .clickable(onClick = onSelect)
      .padding(horizontal = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = "${category.iconLabel} ${category.displayName}",
      style = MaterialTheme.typography.bodySmall.copy(
        color = if (isSelected) colors.accent else colors.textSecondary,
        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
        fontSize = 11.sp
      ),
      maxLines = 1
    )
  }
}

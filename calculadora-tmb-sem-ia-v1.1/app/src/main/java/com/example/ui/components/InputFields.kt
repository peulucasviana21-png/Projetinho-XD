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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActivityLevel
import com.example.model.Gender
import com.example.ui.theme.AppTheme

@Composable
fun MinimalNumericInputField(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
  unit: String,
  errorMessage: String?,
  testTag: String,
  keyboardType: KeyboardType = KeyboardType.Number,
  imeAction: ImeAction = ImeAction.Next,
  onImeAction: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val colors = AppTheme.colors

  Column(modifier = modifier) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.Medium,
        color = colors.textSecondary,
        fontSize = 13.sp
      ),
      modifier = Modifier.padding(bottom = 6.dp)
    )

    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      singleLine = true,
      keyboardOptions = KeyboardOptions(
        keyboardType = keyboardType,
        imeAction = imeAction
      ),
      keyboardActions = KeyboardActions(
        onNext = { onImeAction() },
        onDone = { onImeAction() }
      ),
      trailingIcon = {
        Text(
          text = unit,
          style = MaterialTheme.typography.bodyMedium.copy(
            color = colors.textTertiary,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
          ),
          modifier = Modifier.padding(end = 12.dp)
        )
      },
      isError = errorMessage != null,
      colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = colors.textPrimary,
        unfocusedTextColor = colors.textPrimary,
        focusedContainerColor = colors.surfaceCard,
        unfocusedContainerColor = colors.surfaceCard,
        errorContainerColor = colors.surfaceCard,
        cursorColor = colors.accent,
        focusedBorderColor = colors.accent,
        unfocusedBorderColor = colors.borderSubtle,
        errorBorderColor = colors.error,
        errorTextColor = colors.textPrimary
      ),
      shape = RoundedCornerShape(10.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag(testTag)
    )

    if (errorMessage != null) {
      Text(
        text = errorMessage,
        style = MaterialTheme.typography.bodySmall.copy(
          color = colors.error,
          fontSize = 12.sp
        ),
        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
      )
    }
  }
}

@Composable
fun MinimalGenderSelector(
  selectedGender: Gender,
  onGenderSelected: (Gender) -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = AppTheme.colors

  Column(modifier = modifier) {
    Text(
      text = "Gênero",
      style = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.Medium,
        color = colors.textSecondary,
        fontSize = 13.sp
      ),
      modifier = Modifier.padding(bottom = 6.dp)
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .background(colors.surfaceCard)
        .border(1.dp, colors.borderSubtle, RoundedCornerShape(10.dp))
        .padding(4.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Gender.entries.forEach { gender ->
        val isSelected = gender == selectedGender
        val tag = if (gender == Gender.MALE) "gender_male" else "gender_female"

        Box(
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) colors.accentContainer else Color.Transparent)
            .border(
              width = if (isSelected) 1.dp else 0.dp,
              color = if (isSelected) colors.accent else Color.Transparent,
              shape = RoundedCornerShape(8.dp)
            )
            .clickable { onGenderSelected(gender) }
            .testTag(tag),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = gender.label,
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
              color = if (isSelected) colors.accent else colors.textSecondary,
              fontSize = 14.sp
            )
          )
        }
      }
    }
  }
}

@Composable
fun MinimalActivityLevelSelector(
  selectedActivity: ActivityLevel,
  onActivitySelected: (ActivityLevel) -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = AppTheme.colors
  var expanded by remember { mutableStateOf(false) }

  Column(modifier = modifier) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Nível de Atividade Física",
        style = MaterialTheme.typography.bodyMedium.copy(
          fontWeight = FontWeight.Medium,
          color = colors.textSecondary,
          fontSize = 13.sp
        )
      )
      Text(
        text = "Fator ×${selectedActivity.factor}",
        style = MaterialTheme.typography.bodySmall.copy(
          color = colors.accent,
          fontWeight = FontWeight.Medium,
          fontSize = 12.sp
        )
      )
    }

    Spacer(modifier = Modifier.height(6.dp))

    Box(modifier = Modifier.fillMaxWidth()) {
      Surface(
        onClick = { expanded = !expanded },
        shape = RoundedCornerShape(10.dp),
        color = colors.surfaceCard,
        border = BorderStroke(1.dp, if (expanded) colors.accent else colors.borderSubtle),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("activity_selector")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = selectedActivity.label,
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary,
                fontSize = 15.sp
              )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = selectedActivity.description,
              style = MaterialTheme.typography.bodySmall.copy(
                color = colors.textSecondary,
                fontSize = 12.sp
              )
            )
          }

          Text(
            text = if (expanded) "▲" else "▼",
            style = MaterialTheme.typography.bodySmall.copy(
              color = colors.textTertiary,
              fontSize = 10.sp
            ),
            modifier = Modifier.padding(start = 8.dp)
          )
        }
      }

      DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
          .fillMaxWidth(0.9f)
          .background(colors.surfaceVariant)
          .border(1.dp, colors.borderSubtle, RoundedCornerShape(8.dp))
      ) {
        ActivityLevel.entries.forEach { level ->
          val isSelected = level == selectedActivity
          DropdownMenuItem(
            text = {
              Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = level.label,
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                      color = if (isSelected) colors.accent else colors.textPrimary,
                      fontSize = 14.sp
                    )
                  )
                  Text(
                    text = "×${level.factor}",
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = if (isSelected) colors.accent else colors.textTertiary,
                      fontWeight = FontWeight.Medium,
                      fontSize = 12.sp
                    )
                  )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = level.description,
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = colors.textSecondary,
                    fontSize = 11.sp
                  )
                )
              }
            },
            onClick = {
              onActivitySelected(level)
              expanded = false
            },
            modifier = Modifier.background(
              if (isSelected) colors.accentContainer else Color.Transparent
            )
          )
        }
      }
    }
  }
}

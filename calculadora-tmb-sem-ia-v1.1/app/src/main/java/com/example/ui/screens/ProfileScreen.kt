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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.io.File
import com.example.model.ActivityLevel
import com.example.model.AppThemeMode
import com.example.model.Gender
import com.example.model.ProfileCustomizationPresets
import com.example.ui.components.ColorUtils
import com.example.ui.components.HexColorWheelPicker
import com.example.ui.components.MinimalActivityLevelSelector
import com.example.ui.components.MinimalGenderSelector
import com.example.ui.components.MinimalNumericInputField
import com.example.ui.components.ResultCard
import com.example.ui.theme.AppTheme
import com.example.viewmodel.ProfileFormState
import com.example.viewmodel.ProfileSubSection
import java.util.Locale

@Composable
fun ProfileScreen(
  state: ProfileFormState,
  subSection: ProfileSubSection,
  themeMode: AppThemeMode,
  accentColorHex: String,
  onSubSectionSelected: (ProfileSubSection) -> Unit,
  onOpenEditHeader: () -> Unit,
  onAgeChanged: (String) -> Unit,
  onWeightChanged: (String) -> Unit,
  onHeightChanged: (String) -> Unit,
  onGenderChanged: (Gender) -> Unit,
  onActivityLevelChanged: (ActivityLevel) -> Unit,
  onCalculate: () -> Unit,
  onReset: () -> Unit,
  onNavigateToMeals: () -> Unit,
  onThemeModeChanged: (AppThemeMode) -> Unit,
  onAccentColorChanged: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = AppTheme.colors
  val focusManager = LocalFocusManager.current
  val avatar = ProfileCustomizationPresets.getAvatar(state.avatarId)
  val banner = ProfileCustomizationPresets.getBanner(state.bannerId)

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.Start
  ) {
    // ==========================================
    // 1. HERO PROFILE HEADER (Banner + Avatar + Customization)
    // ==========================================
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = colors.surfaceCard,
      border = BorderStroke(1.dp, colors.borderSubtle),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        // Banner Box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .then(
              if (state.customBannerUri != null) Modifier else Modifier.background(banner.brush)
            )
            .testTag("profile_banner")
        ) {
          if (state.customBannerUri != null) {
            AsyncImage(
              model = File(state.customBannerUri),
              contentDescription = "Banner do perfil",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          }

          // Banner Edit Button
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(10.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color.Black.copy(alpha = 0.5f))
              .clickable { onOpenEditHeader() }
              .padding(horizontal = 8.dp, vertical = 4.dp)
              .testTag("edit_header_button"),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar Foto e Capa",
                tint = Color.White,
                modifier = Modifier.size(13.dp)
              )
              Text(
                text = "Editar Perfil",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium
                )
              )
            }
          }
        }

        // Avatar + User Info Row (Overlapping the banner)
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 14.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            // Circular Avatar overlapping banner
            Box(
              modifier = Modifier
                .offset(y = (-36).dp)
                .size(76.dp)
                .clip(CircleShape)
                .background(colors.surfaceCard)
                .border(3.dp, colors.surfaceCard, CircleShape)
                .clickable { onOpenEditHeader() }
                .testTag("profile_avatar"),
              contentAlignment = Alignment.Center
            ) {
              if (state.customAvatarUri != null) {
                AsyncImage(
                  model = File(state.customAvatarUri),
                  contentDescription = "Foto de perfil",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                )
              } else {
                Box(
                  modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color(android.graphics.Color.parseColor(avatar.bgColorHex))),
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = avatar.emoji, fontSize = 34.sp)
                }
              }

              // Camera badge
              Box(
                modifier = Modifier
                  .align(Alignment.BottomEnd)
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(colors.accent)
                  .border(2.dp, colors.surfaceCard, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.CameraAlt,
                  contentDescription = "Trocar avatar",
                  tint = colors.onAccent,
                  modifier = Modifier.size(13.dp)
                )
              }
            }

            // Quick Status Pill
            Box(
              modifier = Modifier
                .padding(bottom = 6.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(if (state.result != null) colors.accentContainer else colors.surfaceVariant)
                .border(
                  1.dp,
                  if (state.result != null) colors.accent.copy(alpha = 0.5f) else colors.borderSubtle,
                  RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(
                text = if (state.result != null) "✓ Perfil Ativo" else "⚙️ Aguardando dados",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = if (state.result != null) colors.accent else colors.textTertiary,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 11.sp
                )
              )
            }
          }

          // User Name & Subtitle
          Text(
            text = state.userName.ifBlank { "Meu Perfil" },
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = colors.textPrimary,
              fontSize = 19.sp
            ),
            modifier = Modifier.offset(y = (-24).dp)
          )

          // Summary metrics row (if result exists)
          if (state.result != null) {
            val r = state.result
            val waterL = String.format(Locale("pt", "BR"), "%.1f", r.dailyWaterRequirementLiters)
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-18).dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              SummaryMetricPill(
                icon = "🔥",
                label = "Gasto Diário",
                value = "${r.dailyCaloricExpenditure.toInt()} kcal",
                modifier = Modifier.weight(1f)
              )
              SummaryMetricPill(
                icon = "⚡",
                label = "TMB Repouso",
                value = "${r.bmr.toInt()} kcal",
                modifier = Modifier.weight(1f)
              )
              SummaryMetricPill(
                icon = "💧",
                label = "Água Diária",
                value = "$waterL L",
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // ==========================================
    // 2. SUB-SECTION SEGMENTED BAR (Meus Dados | Aparência | Sobre)
    // ==========================================
    Surface(
      shape = RoundedCornerShape(12.dp),
      color = colors.surfaceCard,
      border = BorderStroke(1.dp, colors.borderSubtle),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        ProfileSubSection.entries.forEach { section ->
          val isSelected = section == subSection
          val tag = when (section) {
            ProfileSubSection.BODY_DATA -> "subsection_body_data"
            ProfileSubSection.APPEARANCE -> "subsection_appearance"
            ProfileSubSection.ABOUT -> "subsection_about"
          }

          Box(
            modifier = Modifier
              .weight(1f)
              .height(40.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) colors.accentContainer else Color.Transparent)
              .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) colors.accent else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
              )
              .clickable { onSubSectionSelected(section) }
              .testTag(tag),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(text = section.icon, fontSize = 13.sp)
              Text(
                text = section.title,
                style = MaterialTheme.typography.bodySmall.copy(
                  color = if (isSelected) colors.accent else colors.textSecondary,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 12.sp
                )
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // ==========================================
    // 3. SUB-SECTION CONTENTS
    // ==========================================
    when (subSection) {
      ProfileSubSection.BODY_DATA -> {
        BodyDataSubSection(
          state = state,
          onAgeChanged = onAgeChanged,
          onWeightChanged = onWeightChanged,
          onHeightChanged = onHeightChanged,
          onGenderChanged = onGenderChanged,
          onActivityLevelChanged = onActivityLevelChanged,
          onCalculate = onCalculate,
          onReset = onReset,
          onNavigateToMeals = onNavigateToMeals
        )
      }

      ProfileSubSection.APPEARANCE -> {
        AppearanceSubSection(
          themeMode = themeMode,
          accentColorHex = accentColorHex,
          onThemeModeChanged = onThemeModeChanged,
          onAccentColorChanged = onAccentColorChanged
        )
      }

      ProfileSubSection.ABOUT -> {
        AboutSubSection()
      }
    }

    Spacer(modifier = Modifier.height(28.dp))
  }
}

// ----------------------------------------------------
// SUBSECTION A: Meus Dados & Cálculo de TMB / Gasto / Água
// ----------------------------------------------------
@Composable
private fun BodyDataSubSection(
  state: ProfileFormState,
  onAgeChanged: (String) -> Unit,
  onWeightChanged: (String) -> Unit,
  onHeightChanged: (String) -> Unit,
  onGenderChanged: (Gender) -> Unit,
  onActivityLevelChanged: (ActivityLevel) -> Unit,
  onCalculate: () -> Unit,
  onReset: () -> Unit,
  onNavigateToMeals: () -> Unit
) {
  val colors = AppTheme.colors
  val focusManager = LocalFocusManager.current

  Column(modifier = Modifier.fillMaxWidth()) {
    // Header
    Text(
      text = "Dados Corporais & Fatores de Cálculo",
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        color = colors.textPrimary,
        fontSize = 16.sp
      )
    )
    Text(
      text = "Insira seus dados para calcular a TMB, Gasto Calórico e quantidade de água diária.",
      style = MaterialTheme.typography.bodySmall.copy(
        color = colors.textSecondary,
        fontSize = 12.sp
      )
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Gender selector
    MinimalGenderSelector(
      selectedGender = state.gender,
      onGenderSelected = onGenderChanged,
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(14.dp))

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

    Spacer(modifier = Modifier.height(14.dp))

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

    Spacer(modifier = Modifier.height(14.dp))

    // Activity Level selector
    MinimalActivityLevelSelector(
      selectedActivity = state.activityLevel,
      onActivitySelected = onActivityLevelChanged,
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(20.dp))

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
          text = if (state.isSaved) "Recalcular & Salvar" else "Calcular TMB & Água",
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
          border = BorderStroke(1.dp, colors.borderSubtle),
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

    // Results (BMR + Total Caloric Expenditure + Daily Water Intake + Goals)
    if (state.result != null) {
      Spacer(modifier = Modifier.height(22.dp))
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
              text = "Comparar com Refeições",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = colors.textPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
              )
            )
            Text(
              text = "Acompanhe calorias ingeridas em tempo real.",
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
            border = BorderStroke(1.dp, colors.accent),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(start = 8.dp)
          ) {
            Text("Refeições", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
          }
        }
      }
    }
  }
}

// ----------------------------------------------------
// SUBSECTION B: Aparência, Tema e Cor Secundária
// ----------------------------------------------------
@Composable
private fun AppearanceSubSection(
  themeMode: AppThemeMode,
  accentColorHex: String,
  onThemeModeChanged: (AppThemeMode) -> Unit,
  onAccentColorChanged: (String) -> Unit
) {
  val colors = AppTheme.colors
  val accentColor = ColorUtils.parseHexColor(accentColorHex)
  val onAccentTextColor = ColorUtils.getContrastingTextColor(accentColor)

  Column(modifier = Modifier.fillMaxWidth()) {
    // Theme Mode Section
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = colors.surfaceCard,
      border = BorderStroke(1.dp, colors.borderSubtle),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = "Modo de Exibição",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            fontSize = 16.sp
          )
        )
        Text(
          text = "Escolha o tema de contraste entre escuro, claro ou sistema.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.textSecondary,
            fontSize = 12.sp
          )
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          AppThemeMode.entries.forEach { mode ->
            val isSelected = mode == themeMode
            val tag = when (mode) {
              AppThemeMode.DARK -> "theme_dark"
              AppThemeMode.LIGHT -> "theme_light"
              AppThemeMode.SYSTEM -> "theme_system"
            }

            Box(
              modifier = Modifier
                .weight(1f)
                .height(72.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) colors.accentContainer else colors.surfaceVariant)
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) colors.accent else colors.borderSubtle,
                  shape = RoundedCornerShape(10.dp)
                )
                .clickable { onThemeModeChanged(mode) }
                .testTag(tag)
                .padding(8.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
              ) {
                Text(text = mode.iconLabel, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                  text = mode.displayName,
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) colors.accent else colors.textSecondary,
                    fontSize = 11.sp
                  ),
                  textAlign = TextAlign.Center
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Color Wheel & Hex Picker Section
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = colors.surfaceCard,
      border = BorderStroke(1.dp, colors.borderSubtle),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Roda de Cores Secundária",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                fontSize = 16.sp
              )
            )
            Text(
              text = "Toque na roda de cores ou digite o código hex para alterar os destaques.",
              style = MaterialTheme.typography.bodySmall.copy(
                color = colors.textSecondary,
                fontSize = 12.sp
              )
            )
          }

          OutlinedButton(
            onClick = { onAccentColorChanged("#A8E6CF") },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, colors.borderSubtle),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textSecondary),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier
              .height(34.dp)
              .testTag("reset_accent_color_button")
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "Restaurar cor padrão",
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Padrão", fontSize = 11.sp)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HexColorWheelPicker(
          currentColorHex = accentColorHex,
          onColorSelected = onAccentColorChanged
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Preview
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = colors.surfaceCard,
      border = BorderStroke(1.dp, colors.borderSubtle),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = "Prévia com a Nova Cor",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            fontSize = 15.sp
          )
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
              containerColor = accentColor,
              contentColor = onAccentTextColor
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .weight(1f)
              .height(40.dp)
          ) {
            Text("Botão Primário", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }

          Box(
            modifier = Modifier
              .weight(1f)
              .height(40.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(accentColor.copy(alpha = 0.18f))
              .border(1.dp, accentColor, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "Tag de Destaque",
              color = accentColor,
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp
            )
          }
        }
      }
    }
  }
}

// ----------------------------------------------------
// SUBSECTION C: Sobre o App e Metodologia Científica
// ----------------------------------------------------
@Composable
private fun AboutSubSection() {
  val colors = AppTheme.colors

  Column(modifier = Modifier.fillMaxWidth()) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = colors.surfaceCard,
      border = BorderStroke(1.dp, colors.borderSubtle),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = "Metodologia & Fórmulas",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            fontSize = 16.sp
          )
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "1. Taxa Metabólica Basal (TMB) - Mifflin-St Jeor (1990)",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.accent,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "• Homens: (10 × peso) + (6.25 × altura) − (5 × idade) + 5\n• Mulheres: (10 × peso) + (6.25 × altura) − (5 × idade) − 161",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.textSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
          )
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = colors.borderSubtle, thickness = 1.dp)
        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "2. Hidratação Diária Recomendada",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.accent,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "A recomendação base é de 35 ml por quilograma de peso corporal, com acréscimo proporcional para atividades físicas moderadas a intensas para repor a perda hídrica.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.textSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
          )
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = colors.borderSubtle, thickness = 1.dp)
        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "3. Armazenamento Offline Seguro",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.accent,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Todas as suas informações corporais, histórico de refeições e preferências visuais são armazenadas exclusivamente no seu aparelho via banco de dados Room (SQLite).",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.textSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
          )
        )
      }
    }
  }
}

@Composable
private fun SummaryMetricPill(
  icon: String,
  label: String,
  value: String,
  modifier: Modifier = Modifier
) {
  val colors = AppTheme.colors
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(colors.surfaceVariant)
      .border(1.dp, colors.borderSubtle, RoundedCornerShape(8.dp))
      .padding(vertical = 6.dp, horizontal = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
      ) {
        Text(text = icon, fontSize = 11.sp)
        Text(
          text = label,
          style = MaterialTheme.typography.labelSmall.copy(
            color = colors.textSecondary,
            fontSize = 10.sp
          )
        )
      }
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.bodySmall.copy(
          color = colors.textPrimary,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp
        )
      )
    }
  }
}

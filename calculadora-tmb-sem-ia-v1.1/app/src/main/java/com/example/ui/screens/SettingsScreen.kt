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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppThemeMode
import com.example.ui.components.ColorUtils
import com.example.ui.components.HexColorWheelPicker
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.PastelGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun SettingsScreen(
  themeMode: AppThemeMode,
  accentColorHex: String,
  onThemeModeChanged: (AppThemeMode) -> Unit,
  onAccentColorChanged: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val accentColor = ColorUtils.parseHexColor(accentColorHex)
  val onAccentTextColor = ColorUtils.getContrastingTextColor(accentColor)

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.Start
  ) {
    // Screen Title
    Text(
      text = "Configurações",
      style = MaterialTheme.typography.headlineMedium.copy(
        fontWeight = FontWeight.Bold,
        color = TextPrimary,
        fontSize = 22.sp
      )
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = "Personalize o tema visual e a cor secundária do aplicativo.",
      style = MaterialTheme.typography.bodyMedium.copy(
        color = TextSecondary,
        fontSize = 13.sp
      )
    )

    Spacer(modifier = Modifier.height(20.dp))

    // SECTION 1: Theme Mode Selection
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = DarkSurfaceCard,
      border = BorderStroke(1.dp, BorderSubtle),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = "Aparência do Design",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            fontSize = 16.sp
          )
        )
        Text(
          text = "Escolha o modo de exibição entre claro, escuro ou padrão.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextSecondary,
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
                .height(76.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) accentColor.copy(alpha = 0.15f) else DarkSurfaceVariant)
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) accentColor else BorderSubtle,
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = mode.displayName,
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) accentColor else TextSecondary,
                    fontSize = 11.sp
                  ),
                  textAlign = TextAlign.Center,
                  maxLines = 2
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // SECTION 2: Color Wheel & Hex Picker
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = DarkSurfaceCard,
      border = BorderStroke(1.dp, BorderSubtle),
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
                color = TextPrimary,
                fontSize = 16.sp
              )
            )
            Text(
              text = "Selecione qualquer cor na roda ou digite o código hexadecimal.",
              style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 12.sp
              )
            )
          }

          // Reset to default button
          OutlinedButton(
            onClick = { onAccentColorChanged("#A8E6CF") },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, BorderSubtle),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier.height(34.dp).testTag("reset_accent_color_button")
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

        // Interactive Color Wheel Picker
        HexColorWheelPicker(
          currentColorHex = accentColorHex,
          onColorSelected = onAccentColorChanged
        )
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // SECTION 3: Live UI Elements Preview
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = DarkSurfaceCard,
      border = BorderStroke(1.dp, BorderSubtle),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = "Prévia dos Elementos",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
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
            modifier = Modifier.weight(1f).height(40.dp)
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
              text = "Tag / Destaque",
              color = accentColor,
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // SECTION 4: About & Privacy
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = DarkSurfaceCard,
      border = BorderStroke(1.dp, BorderSubtle),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = "Sobre o Aplicativo",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            fontSize = 14.sp
          )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "• Cálculo de TMB baseado na equação Mifflin-St Jeor (1990).\n• Dados de perfil, preferências de cores e histórico de refeições salvos 100% offline em banco de dados Room (SQLite).\n• Total privacidade sem envio de dados para servidores externos.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(30.dp))
  }
}

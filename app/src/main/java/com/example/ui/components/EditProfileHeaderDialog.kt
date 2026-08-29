package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.model.ProfileCustomizationPresets
import com.example.ui.theme.AppTheme
import com.example.util.ImageStorageHelper
import com.example.viewmodel.EditHeaderDialogState
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun EditProfileHeaderDialog(
  state: EditHeaderDialogState,
  onNameChange: (String) -> Unit,
  onAvatarSelect: (String) -> Unit,
  onBannerSelect: (String) -> Unit,
  onCustomAvatarSelect: (String?) -> Unit,
  onCustomBannerSelect: (String?) -> Unit,
  onConfirm: () -> Unit,
  onDismiss: () -> Unit
) {
  if (!state.isVisible) return
  val colors = AppTheme.colors
  val scrollState = rememberScrollState()
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  var isAvatarLoading by remember { mutableStateOf(false) }
  var isBannerLoading by remember { mutableStateOf(false) }

  // Activity launcher to select Avatar image (.png, .jpeg, .webp, etc.)
  val avatarPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    if (uri != null) {
      isAvatarLoading = true
      coroutineScope.launch {
        val savedPath = ImageStorageHelper.saveImageToInternalStorage(context, uri, "avatar")
        isAvatarLoading = false
        if (savedPath != null) {
          onCustomAvatarSelect(savedPath)
        }
      }
    }
  }

  // Activity launcher to select Banner image (.png, .jpeg, .webp, etc.)
  val bannerPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    if (uri != null) {
      isBannerLoading = true
      coroutineScope.launch {
        val savedPath = ImageStorageHelper.saveImageToInternalStorage(context, uri, "banner")
        isBannerLoading = false
        if (savedPath != null) {
          onCustomBannerSelect(savedPath)
        }
      }
    }
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = colors.surface,
      border = BorderStroke(1.dp, colors.borderSubtle),
      modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(scrollState)
          .padding(18.dp)
      ) {
        // Header
        Text(
          text = "Personalizar Perfil",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            fontSize = 18.sp
          )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Escolha imagens (.png, .jpeg) do seu dispositivo ou use presets.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.textSecondary,
            fontSize = 12.sp
          )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Name input
        Text(
          text = "Nome ou Apelido",
          style = MaterialTheme.typography.bodySmall.copy(
            color = colors.textSecondary,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = state.userName,
          onValueChange = onNameChange,
          placeholder = {
            Text("Ex: João Silva", style = MaterialTheme.typography.bodyMedium.copy(color = colors.textTertiary))
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
            .testTag("user_name_input")
        )

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = colors.borderSubtle, thickness = 1.dp)
        Spacer(modifier = Modifier.height(14.dp))

        // ========================================================
        // 1. FOTO DE PERFIL (AVATAR) - 400x400 pi
        // ========================================================
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Foto de Perfil (Avatar)",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = colors.textPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
              )
            )
            Text(
              text = "Recomendado: 400 × 400 px (1:1) • .png, .jpeg",
              style = MaterialTheme.typography.bodySmall.copy(
                color = colors.textTertiary,
                fontSize = 11.sp
              )
            )
          }

          if (state.customAvatarUri != null) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(colors.accent.copy(alpha = 0.15f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "Foto Própria Ativa",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = colors.accent,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.SemiBold
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Custom Avatar File Selector & Preview
        if (state.customAvatarUri != null) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(colors.surfaceCard)
              .border(1.dp, colors.accent, RoundedCornerShape(10.dp))
              .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              AsyncImage(
                model = File(state.customAvatarUri),
                contentDescription = "Prévia da foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .border(1.5.dp, colors.accent, CircleShape)
              )
              Column {
                Text(
                  text = "Imagem personalizada",
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = colors.textPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                  )
                )
                Text(
                  text = "400x400 px carregada",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = colors.textSecondary,
                    fontSize = 11.sp
                  )
                )
              }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              OutlinedButton(
                onClick = { avatarPickerLauncher.launch("image/*") },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, colors.borderSubtle),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textSecondary),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.height(34.dp)
              ) {
                Text("Trocar", fontSize = 11.sp)
              }

              OutlinedButton(
                onClick = { onCustomAvatarSelect(null) },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, colors.borderSubtle),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textTertiary),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                modifier = Modifier.height(34.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Remover foto personalizada",
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }
        } else {
          // Button to Pick Avatar Image
          Button(
            onClick = { avatarPickerLauncher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(
              containerColor = colors.surfaceCard,
              contentColor = colors.textPrimary
            ),
            border = BorderStroke(1.dp, colors.borderSubtle),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("pick_custom_avatar_button")
          ) {
            if (isAvatarLoading) {
              CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = colors.accent,
                strokeWidth = 2.dp
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text("Carregando imagem...", fontSize = 12.sp)
            } else {
              Icon(
                imageVector = Icons.Default.AddPhotoAlternate,
                contentDescription = "Escolher arquivo de foto",
                tint = colors.accent,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Escolher Imagem (400x400 .png / .jpeg)",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Ou selecione um avatar predefinido:",
          style = MaterialTheme.typography.labelSmall.copy(
            color = colors.textTertiary,
            fontSize = 11.sp
          )
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Avatars Grid (2 rows of 4)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          ProfileCustomizationPresets.AVATARS.take(4).forEach { avatar ->
            val isSelected = state.customAvatarUri == null && avatar.id == state.avatarId
            AvatarChoiceItem(
              emoji = avatar.emoji,
              isSelected = isSelected,
              onClick = { onAvatarSelect(avatar.id) },
              modifier = Modifier.weight(1f)
            )
          }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          ProfileCustomizationPresets.AVATARS.drop(4).take(4).forEach { avatar ->
            val isSelected = state.customAvatarUri == null && avatar.id == state.avatarId
            AvatarChoiceItem(
              emoji = avatar.emoji,
              isSelected = isSelected,
              onClick = { onAvatarSelect(avatar.id) },
              modifier = Modifier.weight(1f)
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))
        HorizontalDivider(color = colors.borderSubtle, thickness = 1.dp)
        Spacer(modifier = Modifier.height(14.dp))

        // ========================================================
        // 2. BANNER DE CAPA - 1500x500 pi
        // ========================================================
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Banner de Capa",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = colors.textPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
              )
            )
            Text(
              text = "Recomendado: 1500 × 500 px (3:1) • .png, .jpeg",
              style = MaterialTheme.typography.bodySmall.copy(
                color = colors.textTertiary,
                fontSize = 11.sp
              )
            )
          }

          if (state.customBannerUri != null) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(colors.accent.copy(alpha = 0.15f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "Banner Próprio Ativo",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = colors.accent,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.SemiBold
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Custom Banner File Selector & Preview
        if (state.customBannerUri != null) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(60.dp)
              .clip(RoundedCornerShape(10.dp))
              .border(1.5.dp, colors.accent, RoundedCornerShape(10.dp))
          ) {
            AsyncImage(
              model = File(state.customBannerUri),
              contentDescription = "Prévia do banner",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )

            // Overlay with Controls
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
                .padding(horizontal = 10.dp),
              contentAlignment = Alignment.CenterStart
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Banner 1500x500 Carregado",
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                  )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  Button(
                    onClick = { bannerPickerLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(
                      containerColor = Color.Black.copy(alpha = 0.6f),
                      contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                  ) {
                    Text("Trocar", fontSize = 11.sp)
                  }

                  Button(
                    onClick = { onCustomBannerSelect(null) },
                    colors = ButtonDefaults.buttonColors(
                      containerColor = Color.Black.copy(alpha = 0.6f),
                      contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Close,
                      contentDescription = "Remover banner",
                      modifier = Modifier.size(13.dp)
                    )
                  }
                }
              }
            }
          }
        } else {
          // Button to Pick Banner Image
          Button(
            onClick = { bannerPickerLauncher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(
              containerColor = colors.surfaceCard,
              contentColor = colors.textPrimary
            ),
            border = BorderStroke(1.dp, colors.borderSubtle),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("pick_custom_banner_button")
          ) {
            if (isBannerLoading) {
              CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = colors.accent,
                strokeWidth = 2.dp
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text("Carregando imagem...", fontSize = 12.sp)
            } else {
              Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "Escolher arquivo de capa",
                tint = colors.accent,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Escolher Banner (1500x500 .png / .jpeg)",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Ou selecione um gradiente predefinido:",
          style = MaterialTheme.typography.labelSmall.copy(
            color = colors.textTertiary,
            fontSize = 11.sp
          )
        )
        Spacer(modifier = Modifier.height(6.dp))

        Column(
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          ProfileCustomizationPresets.BANNERS.forEach { banner ->
            val isSelected = state.customBannerUri == null && banner.id == state.bannerId
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(banner.brush)
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) colors.accent else Color.White.copy(alpha = 0.2f),
                  shape = RoundedCornerShape(10.dp)
                )
                .clickable { onBannerSelect(banner.id) }
                .padding(horizontal = 12.dp),
              contentAlignment = Alignment.CenterStart
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = banner.title,
                  style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                  )
                )
                if (isSelected) {
                  Box(
                    modifier = Modifier
                      .size(20.dp)
                      .clip(CircleShape)
                      .background(colors.accent),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = "Selecionado",
                      tint = colors.onAccent,
                      modifier = Modifier.size(13.dp)
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

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
              .height(44.dp)
          ) {
            Text("Cancelar", fontSize = 13.sp)
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
              .height(44.dp)
              .testTag("save_profile_header_button")
          ) {
            Text("Salvar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
      }
    }
  }
}

@Composable
private fun AvatarChoiceItem(
  emoji: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = AppTheme.colors
  Box(
    modifier = modifier
      .height(50.dp)
      .clip(RoundedCornerShape(10.dp))
      .background(if (isSelected) colors.accentContainer else colors.surfaceCard)
      .border(
        width = if (isSelected) 1.5.dp else 1.dp,
        color = if (isSelected) colors.accent else colors.borderSubtle,
        shape = RoundedCornerShape(10.dp)
      )
      .clickable(onClick = onClick),
    contentAlignment = Alignment.Center
  ) {
    Text(text = emoji, fontSize = 22.sp)
  }
}

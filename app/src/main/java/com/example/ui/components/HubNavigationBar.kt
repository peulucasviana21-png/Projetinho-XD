package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppTheme
import com.example.viewmodel.HubTab

@Composable
fun HubBottomNavigationBar(
  selectedTab: HubTab,
  onTabSelected: (HubTab) -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = AppTheme.colors

  Surface(
    color = colors.surfaceCard,
    tonalElevation = 6.dp,
    shadowElevation = 8.dp,
    modifier = modifier.fillMaxWidth()
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .border(
          width = 1.dp,
          color = colors.borderSubtle
        )
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        HubTab.entries.forEach { tab ->
          val isSelected = tab == selectedTab
          val testTag = when (tab) {
            HubTab.PROFILE -> "tab_profile"
            HubTab.MEALS -> "tab_meals"
            HubTab.SETTINGS -> "tab_settings"
          }

          val icon: ImageVector = when (tab) {
            HubTab.PROFILE -> if (isSelected) Icons.Filled.Person else Icons.Outlined.Person
            HubTab.MEALS -> if (isSelected) Icons.Filled.Restaurant else Icons.Outlined.Restaurant
            HubTab.SETTINGS -> if (isSelected) Icons.Filled.Settings else Icons.Outlined.Settings
          }

          val interactionSource = remember { MutableInteractionSource() }

          Box(
            modifier = Modifier
              .weight(1f)
              .height(56.dp)
              .clip(RoundedCornerShape(12.dp))
              .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = colors.accent)
              ) { onTabSelected(tab) }
              .testTag(testTag),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center,
              modifier = Modifier.padding(vertical = 2.dp)
            ) {
              // Pill background when selected
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(16.dp))
                  .background(if (isSelected) colors.accentContainer else Color.Transparent)
                  .padding(horizontal = 16.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = icon,
                  contentDescription = tab.title,
                  tint = if (isSelected) colors.accent else colors.textTertiary,
                  modifier = Modifier.size(22.dp)
                )
              }

              Spacer(modifier = Modifier.height(2.dp))

              Text(
                text = tab.title,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) colors.accent else colors.textSecondary,
                  fontSize = 11.sp
                )
              )
            }
          }
        }
      }
    }
  }
}

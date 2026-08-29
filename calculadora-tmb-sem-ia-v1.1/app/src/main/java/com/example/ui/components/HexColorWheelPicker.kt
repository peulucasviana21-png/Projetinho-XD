package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

val PRESET_ACCENT_COLORS = listOf(
  "#A8E6CF" to "Verde Pastel",
  "#64B5F6" to "Azul Céu",
  "#4DD0E1" to "Ciano Menta",
  "#BA68C8" to "Lavanda",
  "#FF8A80" to "Coral",
  "#FFB74D" to "Laranja",
  "#FFD54F" to "Amarelo Ouro",
  "#81C784" to "Esmeralda",
  "#F48FB1" to "Rosa",
  "#80CBC4" to "Turquesa"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HexColorWheelPicker(
  currentColorHex: String,
  onColorSelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val focusManager = LocalFocusManager.current

  // Parse current color to HSV
  val initialColor = remember(currentColorHex) { ColorUtils.parseHexColor(currentColorHex) }
  val initialHsv = remember(currentColorHex) { ColorUtils.colorToHsv(initialColor) }

  var hue by remember(currentColorHex) { mutableFloatStateOf(initialHsv[0]) }
  var saturation by remember(currentColorHex) { mutableFloatStateOf(initialHsv[1]) }
  var value by remember(currentColorHex) { mutableFloatStateOf(initialHsv[2]) }

  var hexInputText by remember(currentColorHex) { mutableStateOf(currentColorHex.uppercase()) }

  val activeColor = remember(hue, saturation, value) {
    ColorUtils.hsvToColor(hue, saturation, value)
  }

  val activeHex = remember(activeColor) {
    ColorUtils.colorToHex(activeColor)
  }

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Live Color Swatch & Hex Display Card
    Surface(
      shape = RoundedCornerShape(12.dp),
      color = DarkSurfaceVariant,
      border = BorderStroke(1.dp, BorderSubtle),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Live Circle Swatch
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(activeColor)
              .border(2.dp, Color.White.copy(alpha = 0.6f), CircleShape)
          )

          Column {
            Text(
              text = "Cor Secundária Ativa",
              style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 12.sp
              )
            )
            Text(
              text = activeHex,
              style = MaterialTheme.typography.titleMedium.copy(
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 17.sp
              )
            )
          }
        }

        // Preview Tag
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(activeColor.copy(alpha = 0.2f))
            .border(1.dp, activeColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Text(
            text = "Destaque",
            style = MaterialTheme.typography.labelSmall.copy(
              color = activeColor,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            )
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Interactive Color Wheel Canvas
    Box(
      modifier = Modifier
        .size(230.dp)
        .aspectRatio(1f)
        .testTag("color_wheel_picker"),
      contentAlignment = Alignment.Center
    ) {
      Canvas(
        modifier = Modifier
          .size(220.dp)
          .pointerInput(Unit) {
            detectTapGestures { offset ->
              val center = Offset(size.width / 2f, size.height / 2f)
              val dx = offset.x - center.x
              val dy = offset.y - center.y
              val maxRadius = (size.width / 2f) - 10f
              val distance = sqrt(dx * dx + dy * dy)
              if (distance <= maxRadius) {
                var angle = (atan2(dy, dx) * 180f / PI).toFloat()
                if (angle < 0) angle += 360f
                val sat = (distance / maxRadius).coerceIn(0.05f, 1f)

                hue = angle
                saturation = sat
                val newColor = ColorUtils.hsvToColor(hue, saturation, value)
                val newHex = ColorUtils.colorToHex(newColor)
                hexInputText = newHex
                onColorSelected(newHex)
              }
            }
          }
          .pointerInput(Unit) {
            detectDragGestures { change, _ ->
              val center = Offset(size.width / 2f, size.height / 2f)
              val dx = change.position.x - center.x
              val dy = change.position.y - center.y
              val maxRadius = (size.width / 2f) - 10f
              val distance = sqrt(dx * dx + dy * dy)
              if (distance <= maxRadius) {
                var angle = (atan2(dy, dx) * 180f / PI).toFloat()
                if (angle < 0) angle += 360f
                val sat = (distance / maxRadius).coerceIn(0.05f, 1f)

                hue = angle
                saturation = sat
                val newColor = ColorUtils.hsvToColor(hue, saturation, value)
                val newHex = ColorUtils.colorToHex(newColor)
                hexInputText = newHex
                onColorSelected(newHex)
              }
            }
          }
      ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = (size.width / 2f) - 10f

        // Draw radial hue segments
        val step = 2f
        for (angle in 0 until 360 step 2) {
          val startAngle = angle.toFloat()
          val colorAtHue = ColorUtils.hsvToColor(startAngle, 1f, value)
          val rad = startAngle * PI / 180.0
          val edgeX = center.x + radius * cos(rad).toFloat()
          val edgeY = center.y + radius * sin(rad).toFloat()

          drawLine(
            brush = Brush.linearGradient(
              colors = listOf(Color.White, colorAtHue),
              start = center,
              end = Offset(edgeX, edgeY)
            ),
            start = center,
            end = Offset(edgeX, edgeY),
            strokeWidth = 5f
          )
        }

        // Draw outer ring border
        drawCircle(
          color = Color(0xFF444444),
          radius = radius,
          center = center,
          style = Stroke(width = 2.dp.toPx())
        )

        // Draw indicator thumb on current hue & saturation
        val thumbAngleRad = hue * PI / 180.0
        val thumbDist = saturation * radius
        val thumbX = center.x + thumbDist * cos(thumbAngleRad).toFloat()
        val thumbY = center.y + thumbDist * sin(thumbAngleRad).toFloat()

        // Indicator shadow/border & center
        drawCircle(
          color = Color.Black,
          radius = 12.dp.toPx(),
          center = Offset(thumbX, thumbY)
        )
        drawCircle(
          color = Color.White,
          radius = 10.dp.toPx(),
          center = Offset(thumbX, thumbY)
        )
        drawCircle(
          color = activeColor,
          radius = 7.dp.toPx(),
          center = Offset(thumbX, thumbY)
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Brightness / Value Slider
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Luminosidade / Brilho",
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextSecondary,
            fontSize = 12.sp
          )
        )
        Text(
          text = "${(value * 100).toInt()}%",
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
          )
        )
      }

      Slider(
        value = value,
        onValueChange = { newValue ->
          value = newValue
          val newColor = ColorUtils.hsvToColor(hue, saturation, value)
          val newHex = ColorUtils.colorToHex(newColor)
          hexInputText = newHex
          onColorSelected(newHex)
        },
        valueRange = 0.2f..1.0f,
        colors = SliderDefaults.colors(
          thumbColor = activeColor,
          activeTrackColor = activeColor,
          inactiveTrackColor = DarkSurfaceVariant
        ),
        modifier = Modifier.testTag("brightness_slider")
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Direct Hex Code Input Field
    Text(
      text = "Digitar Código Hexadecimal",
      style = MaterialTheme.typography.bodySmall.copy(
        color = TextSecondary,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
      ),
      modifier = Modifier.align(Alignment.Start)
    )
    Spacer(modifier = Modifier.height(4.dp))

    OutlinedTextField(
      value = hexInputText,
      onValueChange = { input ->
        val sanitized = input.uppercase().filter { it == '#' || it.isDigit() || it in 'A'..'F' }.take(7)
        hexInputText = sanitized
        if (sanitized.length == 7 && sanitized.startsWith("#")) {
          val parsed = ColorUtils.parseHexColor(sanitized)
          val hsv = ColorUtils.colorToHsv(parsed)
          hue = hsv[0]
          saturation = hsv[1]
          value = hsv[2]
          onColorSelected(sanitized)
        }
      },
      placeholder = {
        Text(text = "#A8E6CF", color = TextTertiary, fontSize = 14.sp)
      },
      leadingIcon = {
        Box(
          modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(activeColor)
            .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
        )
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Characters,
        imeAction = ImeAction.Done
      ),
      keyboardActions = KeyboardActions(
        onDone = {
          focusManager.clearFocus()
          if (hexInputText.isNotEmpty()) {
            val formatted = if (!hexInputText.startsWith("#")) "#$hexInputText" else hexInputText
            val parsed = ColorUtils.parseHexColor(formatted)
            val finalHex = ColorUtils.colorToHex(parsed)
            hexInputText = finalHex
            val hsv = ColorUtils.colorToHsv(parsed)
            hue = hsv[0]
            saturation = hsv[1]
            value = hsv[2]
            onColorSelected(finalHex)
          }
        }
      ),
      shape = RoundedCornerShape(10.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary,
        focusedContainerColor = DarkSurfaceCard,
        unfocusedContainerColor = DarkSurfaceCard,
        cursorColor = activeColor,
        focusedBorderColor = activeColor,
        unfocusedBorderColor = BorderSubtle
      ),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("hex_color_input")
    )

    Spacer(modifier = Modifier.height(18.dp))

    // Preset Swatches
    Text(
      text = "Cores Rápidas Predefinidas",
      style = MaterialTheme.typography.bodySmall.copy(
        color = TextSecondary,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
      ),
      modifier = Modifier.align(Alignment.Start)
    )
    Spacer(modifier = Modifier.height(8.dp))

    FlowRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      PRESET_ACCENT_COLORS.forEach { (hex, name) ->
        val swatchColor = ColorUtils.parseHexColor(hex)
        val isSelected = currentColorHex.equals(hex, ignoreCase = true)

        Surface(
          onClick = {
            hexInputText = hex
            val hsv = ColorUtils.colorToHsv(swatchColor)
            hue = hsv[0]
            saturation = hsv[1]
            value = hsv[2]
            onColorSelected(hex)
          },
          shape = RoundedCornerShape(8.dp),
          color = if (isSelected) swatchColor.copy(alpha = 0.25f) else DarkSurfaceVariant,
          border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) swatchColor else BorderSubtle
          ),
          modifier = Modifier.testTag("preset_color_${hex.removePrefix("#")}")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(swatchColor)
            )
            Text(
              text = name,
              style = MaterialTheme.typography.bodySmall.copy(
                color = if (isSelected) TextPrimary else TextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
              )
            )
          }
        }
      }
    }
  }
}

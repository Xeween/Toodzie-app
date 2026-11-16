package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RarityTier
import com.example.ui.theme.ObsidianTextStyles
import com.example.ui.theme.ObsidianTheme
import com.example.ui.theme.SpaceGroteskFontFamily

@Composable
fun MangaPanelCard(
  modifier: Modifier = Modifier,
  borderColor: Color = ObsidianTheme.colors.borderIdle,
  containerColor: Color = ObsidianTheme.colors.surfaceMid,
  showScreentone: Boolean = false,
  onClick: (() -> Unit)? = null,
  contentPadding: PaddingValues = PaddingValues(16.dp),
  content: @Composable ColumnScope.() -> Unit,
) {
  val shape = RoundedCornerShape(4.dp)
  val dotColor = ObsidianTheme.colors.textPrimary.copy(alpha = 0.03f)
  val interactiveModifier = if (onClick != null) {
    Modifier.clickable(onClick = onClick)
  } else {
    Modifier
  }

  Surface(
    modifier = modifier
      .clip(shape)
      .then(interactiveModifier),
    shape = shape,
    color = containerColor,
    border = BorderStroke(1.dp, borderColor),
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .drawBehind {
          if (showScreentone) {
            val step = 10.dp.toPx()
            val radius = 1.dp.toPx()
            var x = step * 0.5f
            var rowIndex = 0
            var y = step * 0.5f
            while (y < size.height) {
              val offsetX = if (rowIndex % 2 == 0) 0f else step * 0.5f
              x = step * 0.5f + offsetX
              while (x < size.width) {
                drawCircle(
                  color = dotColor,
                  radius = radius,
                  center = Offset(x, y),
                )
                x += step
              }
              y += step
              rowIndex++
            }
          }
        }
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(contentPadding),
        content = content,
      )
    }
  }
}

@Composable
fun ShowcaseHeader(
  title: String,
  tag: String? = null,
  accentColor: Color = ObsidianTheme.colors.crimson,
  modifier: Modifier = Modifier,
) {
  val borderColor = ObsidianTheme.colors.borderIdle
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(bottom = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1f),
    ) {
      Box(
        modifier = Modifier
          .width(4.dp)
          .height(16.dp)
          .background(accentColor),
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = title,
        style = ObsidianTextStyles.LabelCode.copy(fontWeight = FontWeight.Bold),
        color = ObsidianTheme.colors.textPrimary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      Spacer(modifier = Modifier.width(10.dp))
      Box(
        modifier = Modifier
          .weight(1f)
          .height(1.dp)
          .background(borderColor),
      )
    }
    if (tag != null) {
      Spacer(modifier = Modifier.width(10.dp))
      Box(
        modifier = Modifier
          .background(ObsidianTheme.colors.trough, RoundedCornerShape(2.dp))
          .border(1.dp, borderColor, RoundedCornerShape(2.dp))
          .padding(horizontal = 8.dp, vertical = 3.dp),
      ) {
        Text(
          text = tag,
          style = ObsidianTextStyles.LabelBadge,
          color = ObsidianTheme.colors.cyan,
        )
      }
    }
  }
}

@Composable
fun DoubleRingLevelCounter(
  level: Int,
  progress: Float,
  accentColor: Color,
  size: Dp = 68.dp,
  modifier: Modifier = Modifier,
) {
  val troughColor = ObsidianTheme.colors.trough
  val innerPlateColor = ObsidianTheme.colors.surfaceElevated
  val ringTrack = ObsidianTheme.colors.borderSubtle

  Box(
    modifier = modifier.size(size),
    contentAlignment = Alignment.Center,
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val outerStroke = 4.dp.toPx()
      val inset = outerStroke / 2f + 2.dp.toPx()
      val arcSize = Size(this.size.width - inset * 2f, this.size.height - inset * 2f)

      // Outer XP track
      drawArc(
        color = ringTrack,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = Offset(inset, inset),
        size = arcSize,
        style = Stroke(width = outerStroke),
      )
      // Outer XP active fill
      drawArc(
        color = accentColor,
        startAngle = -90f,
        sweepAngle = 360f * progress.coerceIn(0f, 1f),
        useCenter = false,
        topLeft = Offset(inset, inset),
        size = arcSize,
        style = Stroke(width = outerStroke, cap = StrokeCap.Butt),
      )

      // Inner dark ring separator & plate
      val innerRadius = (this.size.minDimension / 2f) - outerStroke - 5.dp.toPx()
      drawCircle(
        color = troughColor,
        radius = innerRadius + 2.dp.toPx(),
      )
      drawCircle(
        color = innerPlateColor,
        radius = innerRadius,
      )
      drawCircle(
        color = accentColor.copy(alpha = 0.5f),
        radius = innerRadius,
        style = Stroke(width = 1.dp.toPx()),
      )
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "LVL",
        style = ObsidianTextStyles.LabelBadge.copy(fontSize = 8.sp, lineHeight = 9.sp),
        color = ObsidianTheme.colors.textSecondary,
      )
      Text(
        text = level.toString(),
        style = MaterialTheme.typography.titleMedium.copy(
          fontFamily = SpaceGroteskFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 19.sp,
          lineHeight = 20.sp,
        ),
        color = ObsidianTheme.colors.textPrimary,
      )
    }
  }
}

@Composable
fun RarityChip(
  rarity: RarityTier,
  modifier: Modifier = Modifier,
) {
  val colors = ObsidianTheme.colors
  val (bg, border, fg) = when (rarity) {
    RarityTier.COMMON -> Triple(colors.surfaceElevated, colors.borderSubtle, colors.textSecondary)
    RarityTier.RARE -> Triple(colors.rareBg, colors.cyan, colors.cyan)
    RarityTier.MYTHIC -> Triple(colors.crimsonMuted, colors.crimson, colors.textPrimary)
  }

  Box(
    modifier = modifier
      .height(24.dp)
      .background(bg, RoundedCornerShape(2.dp))
      .border(1.dp, border, RoundedCornerShape(2.dp))
      .padding(horizontal = 8.dp),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = rarity.label,
      style = ObsidianTextStyles.LabelBadge,
      color = fg,
    )
  }
}

@Composable
fun TroughProgressBar(
  progress: Float,
  fillColor: Color = ObsidianTheme.colors.crimson,
  height: Dp = 8.dp,
  modifier: Modifier = Modifier,
) {
  val shape = RoundedCornerShape(2.dp)
  val clamped = progress.coerceIn(0f, 1f)
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(height)
      .background(ObsidianTheme.colors.trough, shape)
      .border(1.dp, ObsidianTheme.colors.borderIdle, shape)
      .padding(1.dp),
  ) {
    if (clamped > 0f) {
      Box(
        modifier = Modifier
          .fillMaxWidth(clamped)
          .fillMaxHeight()
          .background(
            brush = Brush.horizontalGradient(
              colors = listOf(fillColor, fillColor.copy(alpha = 0.85f)),
            ),
            shape = RoundedCornerShape(1.dp),
          ),
      )
    }
  }
}

@Composable
fun CrimsonButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  content: (@Composable () -> Unit)? = null,
) {
  Button(
    onClick = onClick,
    enabled = enabled,
    modifier = modifier.heightIn(min = 48.dp),
    shape = RoundedCornerShape(4.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = ObsidianTheme.colors.crimson,
      contentColor = Color.White,
      disabledContainerColor = ObsidianTheme.colors.surfaceElevated,
      disabledContentColor = ObsidianTheme.colors.textMuted,
    ),
    border = BorderStroke(1.dp, if (enabled) Color.White.copy(alpha = 0.2f) else ObsidianTheme.colors.borderIdle),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
  ) {
    if (content != null) {
      content()
    } else {
      Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Composable
fun SlateInsetButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  activeBorder: Boolean = false,
) {
  OutlinedButton(
    onClick = onClick,
    enabled = enabled,
    modifier = modifier.heightIn(min = 48.dp),
    shape = RoundedCornerShape(4.dp),
    colors = ButtonDefaults.outlinedButtonColors(
      containerColor = ObsidianTheme.colors.surfaceElevated,
      contentColor = ObsidianTheme.colors.textPrimary,
      disabledContainerColor = ObsidianTheme.colors.surfaceMid,
      disabledContentColor = ObsidianTheme.colors.textMuted,
    ),
    border = BorderStroke(
      1.dp,
      if (activeBorder) ObsidianTheme.colors.crimson else ObsidianTheme.colors.borderSubtle,
    ),
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
  ) {
    Text(
      text = text.uppercase(),
      style = MaterialTheme.typography.labelLarge,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
    )
  }
}

@Composable
fun VioletTranscendButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
) {
  Button(
    onClick = onClick,
    enabled = enabled,
    modifier = modifier.heightIn(min = 48.dp),
    shape = RoundedCornerShape(4.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = ObsidianTheme.colors.violet,
      contentColor = Color.White,
      disabledContainerColor = ObsidianTheme.colors.surfaceElevated,
      disabledContentColor = ObsidianTheme.colors.textMuted,
    ),
    border = BorderStroke(
      1.dp,
      if (enabled) ObsidianTheme.colors.violetLight.copy(alpha = 0.55f) else ObsidianTheme.colors.borderIdle,
    ),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
  ) {
    Text(
      text = text.uppercase(),
      style = MaterialTheme.typography.labelLarge,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
    )
  }
}

@Composable
fun ObsidianTextField(
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String,
  modifier: Modifier = Modifier,
  singleLine: Boolean = true,
  minLines: Int = 1,
  leadingIcon: (@Composable () -> Unit)? = null,
  trailingIcon: (@Composable () -> Unit)? = null,
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isFocused by interactionSource.collectIsFocusedAsState()
  val borderColor by animateColorAsState(
    targetValue = if (isFocused) ObsidianTheme.colors.crimson else ObsidianTheme.colors.borderIdle,
    label = "obsidian_input_border",
  )

  Row(
    modifier = modifier
      .fillMaxWidth()
      .heightIn(min = 48.dp)
      .background(ObsidianTheme.colors.canvas, RoundedCornerShape(4.dp))
      .border(1.dp, borderColor, RoundedCornerShape(4.dp))
      .padding(horizontal = 12.dp, vertical = 10.dp),
    verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top,
  ) {
    if (leadingIcon != null) {
      leadingIcon()
      Spacer(modifier = Modifier.width(8.dp))
    }
    Box(modifier = Modifier.weight(1f)) {
      if (value.isEmpty()) {
        Text(
          text = placeholder,
          style = MaterialTheme.typography.bodyMedium,
          color = ObsidianTheme.colors.textMuted,
        )
      }
      BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        minLines = minLines,
        interactionSource = interactionSource,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
          color = ObsidianTheme.colors.textPrimary,
        ),
        cursorBrush = SolidColor(ObsidianTheme.colors.crimson),
        modifier = Modifier.fillMaxWidth(),
      )
    }
    if (trailingIcon != null) {
      Spacer(modifier = Modifier.width(8.dp))
      trailingIcon()
    }
  }
}

@Composable
fun ObsidianCheckboxRow(
  label: String,
  sublabel: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .minimumInteractiveComponentSize()
      .clickable(role = Role.Checkbox) { onCheckedChange(!checked) }
      .padding(vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        color = ObsidianTheme.colors.textPrimary,
      )
      Text(
        text = sublabel,
        style = MaterialTheme.typography.bodySmall,
        color = ObsidianTheme.colors.textSecondary,
      )
    }
    Spacer(modifier = Modifier.width(12.dp))
    Box(
      modifier = Modifier
        .size(18.dp)
        .background(
          if (checked) ObsidianTheme.colors.crimson else ObsidianTheme.colors.canvas,
          RoundedCornerShape(2.dp),
        )
        .border(
          2.dp,
          if (checked) ObsidianTheme.colors.crimson else ObsidianTheme.colors.borderSubtle,
          RoundedCornerShape(2.dp),
        ),
      contentAlignment = Alignment.Center,
    ) {
      if (checked) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(13.dp),
        )
      }
    }
  }
}

@Composable
fun HardDropPlate(
  modifier: Modifier = Modifier,
  borderColor: Color = ObsidianTheme.colors.borderSubtle,
  content: @Composable BoxScope.() -> Unit,
) {
  Box(modifier = modifier) {
    Box(
      modifier = Modifier
        .matchParentSize()
        .padding(top = 6.dp)
        .background(Color.Black.copy(alpha = 0.45f), RoundedCornerShape(4.dp)),
    )
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 6.dp)
        .background(ObsidianTheme.colors.surfaceElevated, RoundedCornerShape(4.dp))
        .border(1.dp, borderColor, RoundedCornerShape(4.dp)),
      content = content,
    )
  }
}

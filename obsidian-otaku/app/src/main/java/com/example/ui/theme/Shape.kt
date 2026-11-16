package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

val ObsidianShapes = Shapes(
  extraSmall = RoundedCornerShape(2.dp),
  small = RoundedCornerShape(4.dp),
  medium = RoundedCornerShape(4.dp),
  large = RoundedCornerShape(6.dp),
  extraLarge = RoundedCornerShape(8.dp),
)

class ChamferedShape(private val cornerSize: Dp = 8.dp) : Shape {
  override fun createOutline(
    size: Size,
    layoutDirection: LayoutDirection,
    density: Density,
  ): Outline {
    val cut = with(density) { cornerSize.toPx() }.coerceAtMost(minOf(size.width, size.height) / 2f)
    val path = Path().apply {
      moveTo(cut, 0f)
      lineTo(size.width - cut, 0f)
      lineTo(size.width, cut)
      lineTo(size.width, size.height - cut)
      lineTo(size.width - cut, size.height)
      lineTo(cut, size.height)
      lineTo(0f, size.height - cut)
      lineTo(0f, cut)
      close()
    }
    return Outline.Generic(path)
  }
}

class ShieldShape : Shape {
  override fun createOutline(
    size: Size,
    layoutDirection: LayoutDirection,
    density: Density,
  ): Outline {
    val w = size.width
    val h = size.height
    val path = Path().apply {
      moveTo(w * 0.5f, 0f)
      lineTo(w * 0.94f, h * 0.18f)
      lineTo(w * 0.90f, h * 0.66f)
      lineTo(w * 0.5f, h)
      lineTo(w * 0.10f, h * 0.66f)
      lineTo(w * 0.06f, h * 0.18f)
      close()
    }
    return Outline.Generic(path)
  }
}

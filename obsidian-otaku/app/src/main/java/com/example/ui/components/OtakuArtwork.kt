package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.RarityTier
import com.example.ui.theme.ChamferedShape
import com.example.ui.theme.ObsidianTheme
import com.example.ui.theme.ShieldShape

@Composable
fun HeroNightscapeBanner(
  modifier: Modifier = Modifier,
) {
  val canvasBg = ObsidianTheme.colors.trough
  val midBg = ObsidianTheme.colors.surfaceElevated
  val crimson = ObsidianTheme.colors.crimson
  val violet = ObsidianTheme.colors.violet
  val cyan = ObsidianTheme.colors.cyan

  Canvas(modifier = modifier) {
    val w = size.width
    val h = size.height

    // Nightscape sky gradient
    drawRect(
      brush = Brush.linearGradient(
        colors = listOf(canvasBg, Color(0xFF141829), midBg),
        start = Offset(0f, 0f),
        end = Offset(w, h),
      ),
    )

    // Crimson eclipse moon
    val moonCenter = Offset(w * 0.78f, h * 0.34f)
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(crimson.copy(alpha = 0.5f), violet.copy(alpha = 0.16f), Color.Transparent),
        center = moonCenter,
        radius = h * 0.65f,
      ),
      center = moonCenter,
      radius = h * 0.65f,
    )
    drawCircle(
      color = crimson.copy(alpha = 0.88f),
      radius = h * 0.24f,
      center = moonCenter,
    )
    drawCircle(
      color = canvasBg,
      radius = h * 0.20f,
      center = Offset(moonCenter.x - h * 0.06f, moonCenter.y - h * 0.03f),
    )

    // Cyber-Tokyo skyline silhouettes
    val buildings = listOf(
      0.00f to 0.56f, 0.08f to 0.40f, 0.16f to 0.64f, 0.23f to 0.30f,
      0.32f to 0.52f, 0.41f to 0.36f, 0.50f to 0.60f, 0.58f to 0.42f,
      0.67f to 0.66f, 0.76f to 0.48f, 0.86f to 0.34f, 0.94f to 0.54f,
    )
    for (i in buildings.indices) {
      val startX = buildings[i].first * w
      val nextX = if (i + 1 < buildings.size) buildings[i + 1].first * w else w
      val topY = buildings[i].second * h
      drawRect(
        color = Color(0xFF0A0D13),
        topLeft = Offset(startX, topY),
        size = Size(nextX - startX + 2f, h - topY),
      )
      if (i % 2 == 1) {
        drawLine(
          color = cyan.copy(alpha = 0.35f),
          start = Offset(startX + 4f, topY + 6f),
          end = Offset(startX + 4f, h * 0.85f),
          strokeWidth = 1.5.dp.toPx(),
        )
      }
    }

    // Manga speedline slash
    val slashPath = Path().apply {
      moveTo(0f, h * 0.82f)
      lineTo(w * 0.85f, h * 0.18f)
      lineTo(w * 0.88f, h * 0.21f)
      lineTo(w * 0.04f, h * 0.88f)
      close()
    }
    drawPath(slashPath, color = crimson.copy(alpha = 0.32f))

    // Rain & HUD grid lines
    for (i in 0..8) {
      val rx = w * (0.06f + i * 0.11f)
      drawLine(
        color = cyan.copy(alpha = 0.14f),
        start = Offset(rx, 0f),
        end = Offset(rx - 22f, h),
        strokeWidth = 1.dp.toPx(),
      )
    }

    // Bottom vignette fade into surface
    drawRect(
      brush = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color(0xEE161B26)),
        startY = h * 0.45f,
        endY = h,
      ),
    )
  }
}

@Composable
fun OperatorAvatarEmblem(
  size: Dp = 76.dp,
  contentDescriptionText: String,
  modifier: Modifier = Modifier,
) {
  val colors = ObsidianTheme.colors
  val shape = ChamferedShape(10.dp)
  Box(
    modifier = modifier
      .size(size)
      .clip(shape)
      .background(colors.trough)
      .border(1.5.dp, colors.cyan, shape)
      .semantics { contentDescription = contentDescriptionText },
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = this.size.width
      val h = this.size.height

      drawRect(
        brush = Brush.linearGradient(
          colors = listOf(Color(0xFF141824), Color(0xFF251A3D)),
        ),
      )

      // Stylized cyber-oni operator silhouette
      val hood = Path().apply {
        moveTo(w * 0.5f, h * 0.14f)
        lineTo(w * 0.82f, h * 0.36f)
        lineTo(w * 0.86f, h * 0.90f)
        lineTo(w * 0.14f, h * 0.90f)
        lineTo(w * 0.18f, h * 0.36f)
        close()
      }
      drawPath(hood, color = Color(0xFF1E2538))
      drawPath(hood, color = colors.violet.copy(alpha = 0.7f), style = Stroke(width = 1.5.dp.toPx()))

      // Oni horns
      val leftHorn = Path().apply {
        moveTo(w * 0.30f, h * 0.32f)
        lineTo(w * 0.20f, h * 0.10f)
        lineTo(w * 0.38f, h * 0.24f)
        close()
      }
      val rightHorn = Path().apply {
        moveTo(w * 0.70f, h * 0.32f)
        lineTo(w * 0.80f, h * 0.10f)
        lineTo(w * 0.62f, h * 0.24f)
        close()
      }
      drawPath(leftHorn, color = colors.crimson)
      drawPath(rightHorn, color = colors.crimson)

      // Visor slit
      val visor = Path().apply {
        moveTo(w * 0.26f, h * 0.44f)
        lineTo(w * 0.50f, h * 0.49f)
        lineTo(w * 0.74f, h * 0.44f)
        lineTo(w * 0.68f, h * 0.57f)
        lineTo(w * 0.50f, h * 0.54f)
        lineTo(w * 0.32f, h * 0.57f)
        close()
      }
      drawPath(visor, color = colors.trough)
      drawLine(
        color = colors.cyan,
        start = Offset(w * 0.32f, h * 0.50f),
        end = Offset(w * 0.47f, h * 0.52f),
        strokeWidth = 2.5.dp.toPx(),
      )
      drawLine(
        color = colors.crimson,
        start = Offset(w * 0.53f, h * 0.52f),
        end = Offset(w * 0.68f, h * 0.50f),
        strokeWidth = 2.5.dp.toPx(),
      )

      // Collar telemetry stripe
      drawLine(
        color = colors.violet,
        start = Offset(w * 0.22f, h * 0.78f),
        end = Offset(w * 0.78f, h * 0.78f),
        strokeWidth = 2.dp.toPx(),
      )
    }
  }
}

@Composable
fun FeaturedArtworkCanvas(
  artKey: String,
  contentDescriptionText: String,
  modifier: Modifier = Modifier,
) {
  val colors = ObsidianTheme.colors
  Canvas(
    modifier = modifier.semantics { contentDescription = contentDescriptionText },
  ) {
    val w = size.width
    val h = size.height

    val (bgStart, bgEnd, accentA, accentB) = when (artKey) {
      "shinjuku_rain" -> listOf(Color(0xFF0A101E), Color(0xFF1F1635), colors.cyan, colors.crimson)
      "valkyrie_unit" -> listOf(Color(0xFF130E24), Color(0xFF28184D), colors.violet, colors.cyan)
      "crimson_blade" -> listOf(Color(0xFF1A0B12), Color(0xFF2B121C), colors.crimson, Color(0xFFF8FAFC))
      else -> listOf(Color(0xFF09131C), Color(0xFF16263A), colors.cyan, colors.violet)
    }

    drawRect(
      brush = Brush.linearGradient(
        colors = listOf(bgStart, bgEnd),
        start = Offset(0f, 0f),
        end = Offset(w, h),
      ),
    )

    // Geometric manga frame grid
    drawLine(
      color = Color.White.copy(alpha = 0.06f),
      start = Offset(w * 0.12f, 0f),
      end = Offset(w * 0.12f, h),
      strokeWidth = 1.dp.toPx(),
    )
    drawLine(
      color = Color.White.copy(alpha = 0.06f),
      start = Offset(0f, h * 0.72f),
      end = Offset(w, h * 0.72f),
      strokeWidth = 1.dp.toPx(),
    )

    when (artKey) {
      "shinjuku_rain" -> {
        // Glowing torii gate & rain neon
        drawCircle(
          color = accentB.copy(alpha = 0.25f),
          radius = h * 0.34f,
          center = Offset(w * 0.5f, h * 0.44f),
        )
        drawRect(accentB, Offset(w * 0.24f, h * 0.28f), Size(w * 0.52f, h * 0.06f))
        drawRect(accentB, Offset(w * 0.30f, h * 0.38f), Size(w * 0.40f, h * 0.04f))
        drawRect(accentB, Offset(w * 0.34f, h * 0.30f), Size(w * 0.05f, h * 0.52f))
        drawRect(accentB, Offset(w * 0.61f, h * 0.30f), Size(w * 0.05f, h * 0.52f))
        for (i in 0..10) {
          val rx = w * (0.05f + i * 0.09f)
          drawLine(accentA.copy(alpha = 0.45f), Offset(rx, 0f), Offset(rx - 18f, h), 1.2.dp.toPx())
        }
      }
      "valkyrie_unit" -> {
        // Mecha wing geometry
        val leftWing = Path().apply {
          moveTo(w * 0.48f, h * 0.52f)
          lineTo(w * 0.14f, h * 0.20f)
          lineTo(w * 0.22f, h * 0.62f)
          close()
        }
        val rightWing = Path().apply {
          moveTo(w * 0.52f, h * 0.52f)
          lineTo(w * 0.86f, h * 0.20f)
          lineTo(w * 0.78f, h * 0.62f)
          close()
        }
        drawPath(leftWing, accentA.copy(alpha = 0.7f))
        drawPath(rightWing, accentA.copy(alpha = 0.7f))
        drawCircle(accentB, radius = h * 0.14f, center = Offset(w * 0.5f, h * 0.48f))
        drawCircle(Color.White, radius = h * 0.05f, center = Offset(w * 0.5f, h * 0.48f))
      }
      "crimson_blade" -> {
        // Eclipse + katana slash
        drawCircle(accentA, radius = h * 0.28f, center = Offset(w * 0.5f, h * 0.46f))
        drawCircle(bgStart, radius = h * 0.22f, center = Offset(w * 0.52f, h * 0.46f))
        val blade = Path().apply {
          moveTo(w * 0.14f, h * 0.78f)
          lineTo(w * 0.86f, h * 0.18f)
          lineTo(w * 0.89f, h * 0.22f)
          lineTo(w * 0.19f, h * 0.83f)
          close()
        }
        drawPath(blade, accentB)
      }
      else -> {
        // Phantom terminal concentric HUD
        val center = Offset(w * 0.5f, h * 0.48f)
        drawCircle(accentA.copy(alpha = 0.6f), radius = h * 0.30f, center = center, style = Stroke(2.dp.toPx()))
        drawCircle(accentB.copy(alpha = 0.8f), radius = h * 0.20f, center = center, style = Stroke(2.dp.toPx()))
        drawLine(accentA, Offset(w * 0.18f, h * 0.48f), Offset(w * 0.82f, h * 0.48f), 1.5.dp.toPx())
        drawLine(accentB, Offset(w * 0.5f, h * 0.14f), Offset(w * 0.5f, h * 0.82f), 1.5.dp.toPx())
      }
    }

    // HUD corner brackets
    val bLen = 14.dp.toPx()
    val pad = 10.dp.toPx()
    drawLine(accentA, Offset(pad, pad), Offset(pad + bLen, pad), 2.dp.toPx())
    drawLine(accentA, Offset(pad, pad), Offset(pad, pad + bLen), 2.dp.toPx())
    drawLine(accentA, Offset(w - pad - bLen, h - pad), Offset(w - pad, h - pad), 2.dp.toPx())
    drawLine(accentA, Offset(w - pad, h - pad - bLen), Offset(w - pad, h - pad), 2.dp.toPx())
  }
}

@Composable
fun AchievementEmblemIcon(
  iconKey: String,
  rarityTier: RarityTier,
  unlocked: Boolean,
  size: Dp = 64.dp,
  contentDescriptionText: String,
  modifier: Modifier = Modifier,
) {
  val colors = ObsidianTheme.colors
  val frameColor = when {
    !unlocked -> colors.borderSubtle
    rarityTier == RarityTier.MYTHIC -> colors.crimson
    rarityTier == RarityTier.RARE -> colors.violet
    else -> colors.cyan
  }
  val bgBrush = if (unlocked) {
    Brush.linearGradient(
      colors = listOf(
        frameColor.copy(alpha = 0.28f),
        colors.trough,
      ),
    )
  } else {
    Brush.linearGradient(
      colors = listOf(
        colors.surfaceElevated.copy(alpha = 0.4f),
        colors.trough,
      ),
    )
  }

  Box(
    modifier = modifier
      .size(size)
      .clip(RoundedCornerShape(4.dp))
      .background(bgBrush)
      .border(1.dp, frameColor, RoundedCornerShape(4.dp))
      .semantics { contentDescription = contentDescriptionText },
    contentAlignment = Alignment.Center,
  ) {
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .alpha(if (unlocked) 1f else 0.35f),
    ) {
      drawIconGlyph(
        iconKey = iconKey,
        primary = if (unlocked) frameColor else colors.textMuted,
        accent = if (unlocked) colors.textPrimary else colors.textMuted,
      )
    }
    if (!unlocked) {
      Box(
        modifier = Modifier
          .size(26.dp)
          .background(colors.trough.copy(alpha = 0.88f), RoundedCornerShape(2.dp))
          .border(1.dp, colors.borderSubtle, RoundedCornerShape(2.dp)),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Default.Lock,
          contentDescription = null,
          tint = colors.textSecondary,
          modifier = Modifier.size(15.dp),
        )
      }
    }
  }
}

@Composable
fun BadgeShieldEmblem(
  iconKey: String,
  rarityTier: RarityTier,
  owned: Boolean,
  size: Dp = 48.dp,
  contentDescriptionText: String,
  modifier: Modifier = Modifier,
) {
  val colors = ObsidianTheme.colors
  val foilColor = when {
    !owned -> colors.borderSubtle
    rarityTier == RarityTier.MYTHIC -> colors.violet
    rarityTier == RarityTier.RARE -> colors.cyan
    else -> colors.textSecondary
  }
  val shape = ShieldShape()
  Box(
    modifier = modifier
      .size(size)
      .clip(shape)
      .background(
        brush = Brush.linearGradient(
          colors = if (owned) {
            listOf(foilColor.copy(alpha = 0.34f), colors.trough)
          } else {
            listOf(colors.surfaceElevated.copy(alpha = 0.35f), colors.trough)
          },
        ),
      )
      .border(1.5.dp, foilColor, shape)
      .semantics { contentDescription = contentDescriptionText },
    contentAlignment = Alignment.Center,
  ) {
    Canvas(
      modifier = Modifier
        .fillMaxSize(0.68f)
        .alpha(if (owned) 1f else 0.35f),
    ) {
      drawIconGlyph(
        iconKey = iconKey,
        primary = foilColor,
        accent = if (owned) colors.crimson else colors.textMuted,
      )
    }
  }
}

private fun DrawScope.drawIconGlyph(
  iconKey: String,
  primary: Color,
  accent: Color,
) {
  val w = size.width
  val h = size.height
  when (iconKey) {
    "katana" -> {
      val blade = Path().apply {
        moveTo(w * 0.22f, h * 0.78f)
        lineTo(w * 0.78f, h * 0.18f)
        lineTo(w * 0.84f, h * 0.24f)
        lineTo(w * 0.28f, h * 0.82f)
        close()
      }
      drawPath(blade, accent)
      drawLine(primary, Offset(w * 0.26f, h * 0.62f), Offset(w * 0.42f, h * 0.78f), 3.dp.toPx())
    }
    "valkyrie" -> {
      val wings = Path().apply {
        moveTo(w * 0.5f, h * 0.24f)
        lineTo(w * 0.86f, h * 0.42f)
        lineTo(w * 0.5f, h * 0.78f)
        lineTo(w * 0.14f, h * 0.42f)
        close()
      }
      drawPath(wings, primary.copy(alpha = 0.45f))
      drawPath(wings, primary, style = Stroke(width = 2.dp.toPx()))
      drawCircle(accent, radius = w * 0.10f, center = Offset(w * 0.5f, h * 0.48f))
    }
    "oni" -> {
      val mask = Path().apply {
        moveTo(w * 0.28f, h * 0.24f)
        lineTo(w * 0.38f, h * 0.40f)
        lineTo(w * 0.62f, h * 0.40f)
        lineTo(w * 0.72f, h * 0.24f)
        lineTo(w * 0.76f, h * 0.68f)
        lineTo(w * 0.50f, h * 0.82f)
        lineTo(w * 0.24f, h * 0.68f)
        close()
      }
      drawPath(mask, primary.copy(alpha = 0.5f))
      drawPath(mask, accent, style = Stroke(width = 1.8.dp.toPx()))
    }
    "terminal" -> {
      drawRect(
        color = primary,
        topLeft = Offset(w * 0.20f, h * 0.26f),
        size = Size(w * 0.60f, h * 0.48f),
        style = Stroke(width = 2.dp.toPx()),
      )
      drawLine(accent, Offset(w * 0.30f, h * 0.44f), Offset(w * 0.44f, h * 0.52f), 2.dp.toPx())
      drawLine(accent, Offset(w * 0.48f, h * 0.56f), Offset(w * 0.68f, h * 0.56f), 2.dp.toPx())
    }
    "mecha" -> {
      val hex = Path().apply {
        moveTo(w * 0.5f, h * 0.18f)
        lineTo(w * 0.80f, h * 0.36f)
        lineTo(w * 0.80f, h * 0.66f)
        lineTo(w * 0.5f, h * 0.82f)
        lineTo(w * 0.20f, h * 0.66f)
        lineTo(w * 0.20f, h * 0.36f)
        close()
      }
      drawPath(hex, primary, style = Stroke(width = 2.dp.toPx()))
      drawCircle(accent, radius = w * 0.14f, center = Offset(w * 0.5f, h * 0.5f))
    }
    else -> {
      val diamond = Path().apply {
        moveTo(w * 0.5f, h * 0.16f)
        lineTo(w * 0.84f, h * 0.5f)
        lineTo(w * 0.5f, h * 0.84f)
        lineTo(w * 0.16f, h * 0.5f)
        close()
      }
      drawPath(diamond, primary.copy(alpha = 0.45f))
      drawPath(diamond, accent, style = Stroke(width = 2.dp.toPx()))
    }
  }
}

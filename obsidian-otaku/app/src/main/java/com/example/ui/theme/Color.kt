package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val ObsidianCanvas = Color(0xFF0E1117)
val ObsidianTrough = Color(0xFF080A0E)
val ObsidianSurfaceLowest = Color(0xFF0B0E14)
val ObsidianSurfaceLow = Color(0xFF161B26)
val ObsidianSurfaceMid = Color(0xFF1D2026)
val ObsidianSurfaceElevated = Color(0xFF1F2636)
val ObsidianSurfaceElevatedHover = Color(0xFF262F42)
val ObsidianSurfaceBright = Color(0xFF363940)

val ObsidianBorderIdle = Color(0xFF222938)
val ObsidianBorderSubtle = Color(0xFF2E384D)

val CrimsonSpark = Color(0xFFE63946)
val CrimsonSparkHover = Color(0xFFF04754)
val CrimsonContainer = Color(0xFFFF535B)
val CrimsonOnPrimary = Color(0xFFFFFFFF)
val CrimsonMutedSurface = Color(0xFF2A1219)

val VioletRelic = Color(0xFF8B5CF6)
val VioletContainer = Color(0xFF571BC1)
val VioletLight = Color(0xFFD0BCFF)
val VioletMutedSurface = Color(0xFF1E1736)

val CyanEcho = Color(0xFF38BDF8)
val CyanRareBg = Color(0xFF0C1B29)
val CyanLight = Color(0xFF7BD0FF)

val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

val ErrorRed = Color(0xFFFFB4AB)
val ErrorContainer = Color(0xFF93000A)

@Immutable
data class ObsidianColors(
  val canvas: Color = ObsidianCanvas,
  val trough: Color = ObsidianTrough,
  val surfaceMid: Color = ObsidianSurfaceLow,
  val surfaceElevated: Color = ObsidianSurfaceElevated,
  val surfaceElevatedHover: Color = ObsidianSurfaceElevatedHover,
  val borderIdle: Color = ObsidianBorderIdle,
  val borderSubtle: Color = ObsidianBorderSubtle,
  val crimson: Color = CrimsonSpark,
  val crimsonHover: Color = CrimsonSparkHover,
  val crimsonMuted: Color = CrimsonMutedSurface,
  val violet: Color = VioletRelic,
  val violetLight: Color = VioletLight,
  val violetMuted: Color = VioletMutedSurface,
  val cyan: Color = CyanEcho,
  val rareBg: Color = CyanRareBg,
  val textPrimary: Color = TextPrimary,
  val textSecondary: Color = TextSecondary,
  val textMuted: Color = TextMuted,
)

val LocalObsidianColors = staticCompositionLocalOf { ObsidianColors() }

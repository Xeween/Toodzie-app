package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

private val ObsidianDarkColorScheme = darkColorScheme(
  primary = CrimsonSpark,
  onPrimary = CrimsonOnPrimary,
  primaryContainer = CrimsonContainer,
  onPrimaryContainer = TextPrimary,
  secondary = VioletRelic,
  onSecondary = TextPrimary,
  secondaryContainer = VioletContainer,
  onSecondaryContainer = VioletLight,
  tertiary = CyanEcho,
  onTertiary = ObsidianTrough,
  tertiaryContainer = CyanRareBg,
  onTertiaryContainer = CyanLight,
  background = ObsidianCanvas,
  onBackground = TextPrimary,
  surface = ObsidianSurfaceLow,
  onSurface = TextPrimary,
  surfaceVariant = ObsidianSurfaceElevated,
  onSurfaceVariant = TextSecondary,
  outline = ObsidianBorderSubtle,
  outlineVariant = ObsidianBorderIdle,
  error = ErrorRed,
  onError = ObsidianTrough,
  errorContainer = ErrorContainer,
  onErrorContainer = TextPrimary,
  surfaceContainerLowest = ObsidianTrough,
  surfaceContainerLow = ObsidianSurfaceLow,
  surfaceContainer = ObsidianSurfaceMid,
  surfaceContainerHigh = ObsidianSurfaceElevated,
  surfaceContainerHighest = ObsidianSurfaceBright,
)

object ObsidianTheme {
  val colors: ObsidianColors
    @Composable
    @ReadOnlyComposable
    get() = LocalObsidianColors.current
}

@Composable
fun ObsidianOtakuTheme(
  content: @Composable () -> Unit,
) {
  val obsidianColors = ObsidianColors()
  CompositionLocalProvider(LocalObsidianColors provides obsidianColors) {
    MaterialTheme(
      colorScheme = ObsidianDarkColorScheme,
      typography = ObsidianTypography,
      shapes = ObsidianShapes,
      content = content,
    )
  }
}

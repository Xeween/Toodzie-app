package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val SpaceGroteskFontFamily = FontFamily(
  Font(R.font.space_grotesk, FontWeight.Normal),
  Font(R.font.space_grotesk, FontWeight.Medium),
  Font(R.font.space_grotesk, FontWeight.SemiBold),
  Font(R.font.space_grotesk, FontWeight.Bold),
)

val HankenGroteskFontFamily = FontFamily(
  Font(R.font.hanken_grotesk, FontWeight.Normal),
  Font(R.font.hanken_grotesk, FontWeight.Medium),
  Font(R.font.hanken_grotesk, FontWeight.SemiBold),
)

val JetBrainsMonoFontFamily = FontFamily(
  Font(R.font.jetbrains_mono, FontWeight.Normal),
  Font(R.font.jetbrains_mono, FontWeight.Medium),
  Font(R.font.jetbrains_mono, FontWeight.Bold),
)

object ObsidianTextStyles {
  val LabelNumeric = TextStyle(
    fontFamily = JetBrainsMonoFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.8.sp,
  )

  val LabelCode = TextStyle(
    fontFamily = JetBrainsMonoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.96.sp,
  )

  val LabelBadge = TextStyle(
    fontFamily = JetBrainsMonoFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 10.sp,
    lineHeight = 12.sp,
    letterSpacing = 1.0.sp,
  )
}

val ObsidianTypography = Typography(
  displayMedium = TextStyle(
    fontFamily = SpaceGroteskFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 36.sp,
    lineHeight = 44.sp,
    letterSpacing = (-0.72).sp,
  ),
  headlineLarge = TextStyle(
    fontFamily = SpaceGroteskFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 28.sp,
    lineHeight = 36.sp,
    letterSpacing = (-0.28).sp,
  ),
  headlineMedium = TextStyle(
    fontFamily = SpaceGroteskFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 22.sp,
    lineHeight = 28.sp,
  ),
  headlineSmall = TextStyle(
    fontFamily = SpaceGroteskFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    lineHeight = 28.sp,
  ),
  titleMedium = TextStyle(
    fontFamily = SpaceGroteskFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    lineHeight = 22.sp,
  ),
  titleSmall = TextStyle(
    fontFamily = JetBrainsMonoFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.96.sp,
  ),
  bodyLarge = TextStyle(
    fontFamily = HankenGroteskFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
  ),
  bodyMedium = TextStyle(
    fontFamily = HankenGroteskFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
  ),
  bodySmall = TextStyle(
    fontFamily = HankenGroteskFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 18.sp,
  ),
  labelLarge = TextStyle(
    fontFamily = JetBrainsMonoFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.8.sp,
  ),
  labelMedium = TextStyle(
    fontFamily = JetBrainsMonoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 14.sp,
    letterSpacing = 0.88.sp,
  ),
  labelSmall = TextStyle(
    fontFamily = JetBrainsMonoFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 10.sp,
    lineHeight = 12.sp,
    letterSpacing = 1.0.sp,
  ),
)

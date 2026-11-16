package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.StatusMode
import com.example.ui.components.CrimsonButton
import com.example.ui.components.MangaPanelCard
import com.example.ui.components.ObsidianCheckboxRow
import com.example.ui.components.ObsidianTextField
import com.example.ui.components.ShowcaseHeader
import com.example.ui.components.SlateInsetButton
import com.example.ui.theme.ObsidianTextStyles
import com.example.ui.theme.ObsidianTheme
import com.example.ui.viewmodel.ObsidianUiState

@Composable
fun LoadoutScreen(
  state: ObsidianUiState.Ready,
  onSaveProfile: (String, String, String, StatusMode, String) -> Unit,
  onUpdateHudSettings: (String, Boolean, Boolean) -> Unit,
  modifier: Modifier = Modifier,
) {
  val profile = state.profile
  val colors = ObsidianTheme.colors

  var handle by remember(profile.handle) { mutableStateOf(profile.handle) }
  var rankTitle by remember(profile.rankTitle) { mutableStateOf(profile.rankTitle) }
  var bio by remember(profile.bio) { mutableStateOf(profile.bio) }
  var activeGame by remember(profile.activeGame) { mutableStateOf(profile.activeGame) }
  var selectedStatus by remember(profile.statusMode) {
    mutableStateOf(StatusMode.fromString(profile.statusMode))
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(colors.canvas),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item {
      MangaPanelCard(
        borderColor = colors.borderSubtle,
        showScreentone = profile.screentoneEnabled,
      ) {
        ShowcaseHeader(
          title = "/// OPERATOR DOSSIER",
          tag = "LIVE SYNC",
          accentColor = colors.crimson,
        )

        Text(
          text = "OPERATOR HANDLE",
          style = ObsidianTextStyles.LabelBadge,
          color = colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(6.dp))
        ObsidianTextField(
          value = handle,
          onValueChange = { handle = it },
          placeholder = "KURO_09",
          modifier = Modifier.testTag("handle_input"),
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "PRESTIGE RANK TAG",
          style = ObsidianTextStyles.LabelBadge,
          color = colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(6.dp))
        ObsidianTextField(
          value = rankTitle,
          onValueChange = { rankTitle = it },
          placeholder = "MYTHIC ARCHIVIST",
          modifier = Modifier.testTag("rank_input"),
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "STATUS MODE",
          style = ObsidianTextStyles.LabelBadge,
          color = colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(StatusMode.entries) { mode ->
            val selected = selectedStatus == mode
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
                .background(if (selected) colors.crimson else colors.surfaceElevated)
                .border(
                  1.dp,
                  if (selected) colors.crimson else colors.borderSubtle,
                  RoundedCornerShape(2.dp),
                )
                .clickable { selectedStatus = mode }
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("status_${mode.name.lowercase()}"),
            ) {
              Text(
                text = mode.label,
                style = ObsidianTextStyles.LabelBadge,
                color = if (selected) Color.White else colors.textSecondary,
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "ACTIVE TELEMETRY GAME",
          style = ObsidianTextStyles.LabelBadge,
          color = colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(6.dp))
        ObsidianTextField(
          value = activeGame,
          onValueChange = { activeGame = it },
          placeholder = "CYBER-RONIN ZERO",
          modifier = Modifier.testTag("active_game_input"),
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "OPERATOR BIO",
          style = ObsidianTextStyles.LabelBadge,
          color = colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(6.dp))
        ObsidianTextField(
          value = bio,
          onValueChange = { bio = it },
          placeholder = "Write your operator lore...",
          singleLine = false,
          minLines = 3,
          modifier = Modifier.testTag("bio_input"),
        )

        Spacer(modifier = Modifier.height(16.dp))
        CrimsonButton(
          text = "COMMIT DOSSIER",
          onClick = {
            onSaveProfile(handle, rankTitle, bio, selectedStatus, activeGame)
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("save_dossier_button"),
        )
      }
    }

    item {
      MangaPanelCard(borderColor = colors.borderIdle) {
        ShowcaseHeader(
          title = "/// HUD CALIBRATION",
          tag = "DISPLAY",
          accentColor = colors.violet,
        )

        Text(
          text = "LEVEL RING AURA",
          style = ObsidianTextStyles.LabelBadge,
          color = colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          SlateInsetButton(
            text = "VIOLET RELIC",
            onClick = {
              onUpdateHudSettings(
                "VIOLET",
                profile.screentoneEnabled,
                profile.telemetryTickerEnabled,
              )
            },
            activeBorder = profile.levelRingAccent == "VIOLET",
            modifier = Modifier
              .weight(1f)
              .testTag("accent_violet_button"),
          )
          SlateInsetButton(
            text = "CRIMSON SPARK",
            onClick = {
              onUpdateHudSettings(
                "CRIMSON",
                profile.screentoneEnabled,
                profile.telemetryTickerEnabled,
              )
            },
            activeBorder = profile.levelRingAccent == "CRIMSON",
            modifier = Modifier
              .weight(1f)
              .testTag("accent_crimson_button"),
          )
        }

        Spacer(modifier = Modifier.height(10.dp))
        ObsidianCheckboxRow(
          label = "Manga Screentone Matrix",
          sublabel = "Apply 3% opacity halftone dot pattern across showcase backing plates.",
          checked = profile.screentoneEnabled,
          onCheckedChange = { checked ->
            onUpdateHudSettings(
              profile.levelRingAccent,
              checked,
              profile.telemetryTickerEnabled,
            )
          },
          modifier = Modifier.testTag("screentone_checkbox"),
        )
        ObsidianCheckboxRow(
          label = "Hero Telemetry Ticker",
          sublabel = "Display live online presence & active game pill over banner artwork.",
          checked = profile.telemetryTickerEnabled,
          onCheckedChange = { checked ->
            onUpdateHudSettings(
              profile.levelRingAccent,
              profile.screentoneEnabled,
              checked,
            )
          },
          modifier = Modifier.testTag("telemetry_checkbox"),
        )
      }
    }

    item {
      MangaPanelCard(borderColor = colors.borderIdle) {
        ShowcaseHeader(
          title = stringResource(R.string.showcase_system_logs),
          tag = "PATCH NOTES",
          accentColor = colors.cyan,
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          state.patchLogs.forEach { log ->
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .background(colors.trough, RoundedCornerShape(4.dp))
                .border(1.dp, colors.borderIdle, RoundedCornerShape(4.dp))
                .padding(12.dp),
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .background(colors.rareBg, RoundedCornerShape(2.dp))
                      .border(1.dp, colors.cyan, RoundedCornerShape(2.dp))
                      .padding(horizontal = 6.dp, vertical = 2.dp),
                  ) {
                    Text(
                      text = log.tag,
                      style = ObsidianTextStyles.LabelBadge,
                      color = colors.cyan,
                    )
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = log.version,
                    style = ObsidianTextStyles.LabelCode,
                    color = colors.textPrimary,
                  )
                }
                Text(
                  text = log.date,
                  style = ObsidianTextStyles.LabelBadge,
                  color = colors.crimson,
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = log.title,
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary,
              )
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = log.summary,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary,
              )
            }
          }
        }
      }
    }
  }
}

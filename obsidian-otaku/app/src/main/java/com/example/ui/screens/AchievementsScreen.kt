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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.AchievementEntity
import com.example.data.RarityTier
import com.example.ui.components.AchievementEmblemIcon
import com.example.ui.components.CrimsonButton
import com.example.ui.components.HardDropPlate
import com.example.ui.components.MangaPanelCard
import com.example.ui.components.ObsidianTextField
import com.example.ui.components.RarityChip
import com.example.ui.components.ShowcaseHeader
import com.example.ui.components.SlateInsetButton
import com.example.ui.components.TroughProgressBar
import com.example.ui.theme.ObsidianTextStyles
import com.example.ui.theme.ObsidianTheme
import com.example.ui.viewmodel.AchievementFilter
import com.example.ui.viewmodel.ObsidianUiState

@Composable
fun AchievementsScreen(
  state: ObsidianUiState.Ready,
  onSelectFilter: (AchievementFilter) -> Unit,
  onSearchChange: (String) -> Unit,
  onToggleUnlock: (String) -> Unit,
  onTogglePin: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  var inspectedAchievement by remember { mutableStateOf<AchievementEntity?>(null) }
  val colors = ObsidianTheme.colors

  val unlockedCount = state.achievements.count { it.unlocked }
  val totalCount = state.achievements.size.coerceAtLeast(1)
  val completionRatio = unlockedCount.toFloat() / totalCount.toFloat()
  val rarestUnlock = state.achievements.filter { it.unlocked }.minByOrNull { it.rarityPercent }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(colors.canvas),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    item {
      MangaPanelCard(
        borderColor = colors.borderSubtle,
        showScreentone = state.profile.screentoneEnabled,
      ) {
        ShowcaseHeader(
          title = "/// TROPHY TELEMETRY",
          tag = "${(completionRatio * 100).toInt()}% SYNC",
          accentColor = colors.crimson,
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Column {
            Text(
              text = "$unlockedCount / $totalCount UNLOCKED",
              style = MaterialTheme.typography.headlineMedium,
              color = colors.textPrimary,
            )
            if (rarestUnlock != null) {
              Text(
                text = "RAREST: ${rarestUnlock.title.uppercase()} (${rarestUnlock.rarityPercent}%)",
                style = ObsidianTextStyles.LabelBadge,
                color = colors.cyan,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
        TroughProgressBar(
          progress = completionRatio,
          fillColor = colors.crimson,
          height = 10.dp,
        )
      }
    }

    item {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ObsidianTextField(
          value = state.achievementSearch,
          onValueChange = onSearchChange,
          placeholder = "Filter trophies or game title...",
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = stringResource(R.string.cd_search_achievements),
              tint = colors.textSecondary,
              modifier = Modifier.size(18.dp),
            )
          },
          trailingIcon = if (state.achievementSearch.isNotEmpty()) {
            {
              IconButton(
                onClick = { onSearchChange("") },
                modifier = Modifier.size(28.dp),
              ) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = stringResource(R.string.cd_clear_search),
                  tint = colors.textSecondary,
                  modifier = Modifier.size(16.dp),
                )
              }
            }
          } else {
            null
          },
          modifier = Modifier.testTag("achievement_search_input"),
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(AchievementFilter.entries) { filter ->
            val selected = state.achievementFilter == filter
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
                .background(if (selected) colors.crimson else colors.surfaceElevated)
                .border(
                  1.dp,
                  if (selected) colors.crimson else colors.borderSubtle,
                  RoundedCornerShape(2.dp),
                )
                .clickable { onSelectFilter(filter) }
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("filter_${filter.name.lowercase()}"),
            ) {
              Text(
                text = filter.label,
                style = ObsidianTextStyles.LabelBadge,
                color = if (selected) colors.textPrimary else colors.textSecondary,
              )
            }
          }
        }
      }
    }

    if (state.filteredAchievements.isEmpty()) {
      item {
        MangaPanelCard(borderColor = colors.borderIdle) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Text(
              text = "NO MATCHING RECORDS",
              style = ObsidianTextStyles.LabelCode,
              color = colors.crimson,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Adjust filter tags or clear search query to display operator trophies.",
              style = MaterialTheme.typography.bodySmall,
              color = colors.textSecondary,
            )
          }
        }
      }
    } else {
      items(state.filteredAchievements, key = { it.id }) { achievement ->
        SteamOtakuAchievementCard(
          achievement = achievement,
          onClick = { inspectedAchievement = achievement },
        )
      }
    }
  }

  inspectedAchievement?.let { ach ->
    val live = state.achievements.firstOrNull { it.id == ach.id } ?: ach
    AchievementInspectDialog(
      achievement = live,
      onToggleUnlock = { onToggleUnlock(live.id) },
      onTogglePin = { onTogglePin(live.id) },
      onDismiss = { inspectedAchievement = null },
    )
  }
}

@Composable
private fun SteamOtakuAchievementCard(
  achievement: AchievementEntity,
  onClick: () -> Unit,
) {
  val colors = ObsidianTheme.colors
  val rarity = RarityTier.fromString(achievement.rarityTier)
  val borderColor = when {
    !achievement.unlocked -> colors.borderIdle
    rarity == RarityTier.MYTHIC -> colors.crimson.copy(alpha = 0.7f)
    rarity == RarityTier.RARE -> colors.violet.copy(alpha = 0.6f)
    else -> colors.borderSubtle
  }

  MangaPanelCard(
    borderColor = borderColor,
    containerColor = colors.surfaceMid,
    onClick = onClick,
    contentPadding = PaddingValues(12.dp),
    modifier = Modifier.testTag("achievement_card_${achievement.id}"),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      // Left flank: 64x64px high-contrast icon frame
      AchievementEmblemIcon(
        iconKey = achievement.iconKey,
        rarityTier = rarity,
        unlocked = achievement.unlocked,
        size = 64.dp,
        contentDescriptionText = stringResource(R.string.cd_achievement_icon),
      )

      Spacer(modifier = Modifier.width(12.dp))

      // Center: Space Grotesk title, Hanken Grotesk description, JetBrains Mono timestamp
      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
          Text(
            text = achievement.gameTitle,
            style = ObsidianTextStyles.LabelBadge,
            color = colors.textMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false),
          )
          if (achievement.pinned) {
            Icon(
              imageVector = Icons.Filled.PushPin,
              contentDescription = "Pinned to showcase",
              tint = colors.crimson,
              modifier = Modifier.size(12.dp),
            )
          }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = achievement.title,
          style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
          color = if (achievement.unlocked) colors.textPrimary else colors.textSecondary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = achievement.description,
          style = MaterialTheme.typography.bodySmall,
          color = colors.textSecondary,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = achievement.unlockTimestamp,
          style = ObsidianTextStyles.LabelBadge,
          color = if (achievement.unlocked) colors.crimson else colors.textMuted,
          maxLines = 1,
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      // Right flank: Global rarity percentage tag pill (#080A0E bg, #38BDF8 text)
      Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        Box(
          modifier = Modifier
            .background(colors.trough, RoundedCornerShape(2.dp))
            .border(1.dp, colors.borderSubtle, RoundedCornerShape(2.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
          Text(
            text = "${achievement.rarityPercent}%",
            style = ObsidianTextStyles.LabelCode,
            color = colors.cyan,
          )
        }
        RarityChip(rarity = rarity)
      }
    }
  }
}

@Composable
private fun AchievementInspectDialog(
  achievement: AchievementEntity,
  onToggleUnlock: () -> Unit,
  onTogglePin: () -> Unit,
  onDismiss: () -> Unit,
) {
  val colors = ObsidianTheme.colors
  val rarity = RarityTier.fromString(achievement.rarityTier)
  Dialog(onDismissRequest = onDismiss) {
    HardDropPlate(
      borderColor = if (rarity == RarityTier.MYTHIC) colors.crimson else colors.violet,
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = achievement.gameTitle,
            style = ObsidianTextStyles.LabelCode,
            color = colors.cyan,
          )
          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = stringResource(R.string.cd_close_dialog),
              tint = colors.textSecondary,
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          AchievementEmblemIcon(
            iconKey = achievement.iconKey,
            rarityTier = rarity,
            unlocked = achievement.unlocked,
            size = 64.dp,
            contentDescriptionText = achievement.title,
          )
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            RarityChip(rarity = rarity)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = achievement.title,
              style = MaterialTheme.typography.headlineSmall,
              color = colors.textPrimary,
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = achievement.description,
          style = MaterialTheme.typography.bodyMedium,
          color = colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(colors.trough, RoundedCornerShape(4.dp))
            .border(1.dp, colors.borderIdle, RoundedCornerShape(4.dp))
            .padding(10.dp),
        ) {
          Column {
            Text(
              text = "LORE // TELEMETRY LOG",
              style = ObsidianTextStyles.LabelBadge,
              color = colors.crimson,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = achievement.loreNote,
              style = MaterialTheme.typography.bodySmall,
              color = colors.textSecondary,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "GLOBAL RARITY: ${achievement.rarityPercent}%  •  REWARD: +${achievement.xpReward} XP",
              style = ObsidianTextStyles.LabelBadge,
              color = colors.cyan,
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          SlateInsetButton(
            text = if (achievement.pinned) "UNPIN" else "PIN SHOWCASE",
            onClick = onTogglePin,
            activeBorder = achievement.pinned,
            modifier = Modifier
              .weight(1f)
              .testTag("toggle_pin_achievement_button"),
          )
          CrimsonButton(
            text = if (achievement.unlocked) "LOCK" else "UNLOCK (+${achievement.xpReward} XP)",
            onClick = onToggleUnlock,
            modifier = Modifier
              .weight(1f)
              .testTag("toggle_unlock_achievement_button"),
          )
        }
      }
    }
  }
}

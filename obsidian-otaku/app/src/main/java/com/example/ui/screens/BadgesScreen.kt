package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.BadgeEntity
import com.example.data.RarityTier
import com.example.ui.components.BadgeShieldEmblem
import com.example.ui.components.HardDropPlate
import com.example.ui.components.MangaPanelCard
import com.example.ui.components.RarityChip
import com.example.ui.components.ShowcaseHeader
import com.example.ui.components.SlateInsetButton
import com.example.ui.components.VioletTranscendButton
import com.example.ui.theme.ObsidianTextStyles
import com.example.ui.theme.ObsidianTheme
import com.example.ui.viewmodel.ObsidianUiState

@Composable
fun BadgesScreen(
  state: ObsidianUiState.Ready,
  onToggleEquip: (String) -> Unit,
  onCraftOrUpgrade: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  var selectedBadge by remember { mutableStateOf<BadgeEntity?>(null) }
  val colors = ObsidianTheme.colors

  BoxWithConstraints(
    modifier = modifier
      .fillMaxSize()
      .background(colors.canvas),
  ) {
    val columns = when {
      maxWidth >= 840.dp -> 4
      maxWidth >= 600.dp -> 3
      else -> 2
    }

    LazyVerticalGrid(
      columns = GridCells.Fixed(columns),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.fillMaxSize(),
    ) {
      item(span = { GridItemSpan(columns) }) {
        MangaPanelCard(
          borderColor = colors.violet.copy(alpha = 0.65f),
          showScreentone = state.profile.screentoneEnabled,
        ) {
          ShowcaseHeader(
            title = "/// RELIC FORGE & BADGES",
            tag = "${state.equippedBadges.size}/3 EQUIPPED",
            accentColor = colors.violet,
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Column {
              Text(
                text = "${state.profile.shards} SHARDS",
                style = MaterialTheme.typography.headlineMedium,
                color = colors.violetLight,
              )
              Text(
                text = "${state.badges.count { it.owned }} / ${state.badges.size} RELICS UNLOCKED IN VAULT",
                style = ObsidianTextStyles.LabelBadge,
                color = colors.textSecondary,
              )
            }
            Box(
              modifier = Modifier
                .background(colors.violetMuted, RoundedCornerShape(4.dp))
                .border(1.dp, colors.violet, RoundedCornerShape(4.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.AutoAwesome,
                  contentDescription = null,
                  tint = colors.violetLight,
                  modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "TIER V CAP",
                  style = ObsidianTextStyles.LabelBadge,
                  color = colors.textPrimary,
                )
              }
            }
          }
        }
      }

      items(state.badges, key = { it.id }) { badge ->
        BadgeGridTile(
          badge = badge,
          onClick = { selectedBadge = badge },
        )
      }
    }
  }

  selectedBadge?.let { b ->
    val live = state.badges.firstOrNull { it.id == b.id } ?: b
    BadgeForgeDialog(
      badge = live,
      availableShards = state.profile.shards,
      onToggleEquip = { onToggleEquip(live.id) },
      onCraftOrUpgrade = { onCraftOrUpgrade(live.id) },
      onDismiss = { selectedBadge = null },
    )
  }
}

@Composable
private fun BadgeGridTile(
  badge: BadgeEntity,
  onClick: () -> Unit,
) {
  val colors = ObsidianTheme.colors
  val rarity = RarityTier.fromString(badge.rarityTier)
  val accentColor = when {
    !badge.owned -> colors.borderIdle
    badge.equipped -> colors.violet
    rarity == RarityTier.MYTHIC -> colors.crimson
    rarity == RarityTier.RARE -> colors.cyan
    else -> colors.borderSubtle
  }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(4.dp))
      .background(
        brush = if (badge.owned && rarity == RarityTier.MYTHIC) {
          Brush.verticalGradient(
            colors = listOf(colors.violetMuted.copy(alpha = 0.55f), colors.surfaceMid),
          )
        } else {
          Brush.verticalGradient(
            colors = listOf(colors.surfaceMid, colors.surfaceMid),
          )
        },
      )
      .border(1.dp, accentColor, RoundedCornerShape(4.dp))
      .clickable(onClick = onClick)
      .padding(12.dp)
      .testTag("badge_tile_${badge.id}"),
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        RarityChip(rarity = rarity)
        if (badge.equipped) {
          Box(
            modifier = Modifier
              .background(colors.violet, RoundedCornerShape(2.dp))
              .padding(horizontal = 6.dp, vertical = 3.dp),
          ) {
            Text(
              text = "EQUIPPED",
              style = ObsidianTextStyles.LabelBadge.copy(fontSize = 9.sp),
              color = Color.White,
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
      BadgeShieldEmblem(
        iconKey = badge.iconKey,
        rarityTier = rarity,
        owned = badge.owned,
        size = 56.dp,
        contentDescriptionText = stringResource(R.string.cd_badge_shield),
      )
      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = badge.name,
        style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
        color = if (badge.owned) colors.textPrimary else colors.textSecondary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = badge.series,
        style = ObsidianTextStyles.LabelBadge,
        color = colors.textMuted,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )

      Spacer(modifier = Modifier.height(10.dp))
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(colors.trough, RoundedCornerShape(2.dp))
          .border(1.dp, colors.borderIdle, RoundedCornerShape(2.dp))
          .padding(horizontal = 8.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = if (!badge.owned) {
            "FORGE // ${badge.craftCost} SHARDS"
          } else if (badge.tier >= badge.maxTier) {
            "MAX TIER V"
          } else {
            "TIER ${badge.tier} / ${badge.maxTier}"
          },
          style = ObsidianTextStyles.LabelBadge.copy(fontWeight = FontWeight.Bold),
          color = if (badge.owned) colors.cyan else colors.crimson,
          maxLines = 1,
        )
      }
    }
  }
}

@Composable
private fun BadgeForgeDialog(
  badge: BadgeEntity,
  availableShards: Int,
  onToggleEquip: () -> Unit,
  onCraftOrUpgrade: () -> Unit,
  onDismiss: () -> Unit,
) {
  val colors = ObsidianTheme.colors
  val rarity = RarityTier.fromString(badge.rarityTier)
  val canCraft = (!badge.owned || badge.tier < badge.maxTier) && availableShards >= badge.craftCost

  Dialog(onDismissRequest = onDismiss) {
    HardDropPlate(borderColor = colors.violet) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = "RELIC INSPECT // FORGE",
            style = ObsidianTextStyles.LabelCode,
            color = colors.violetLight,
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
          BadgeShieldEmblem(
            iconKey = badge.iconKey,
            rarityTier = rarity,
            owned = badge.owned,
            size = 64.dp,
            contentDescriptionText = badge.name,
          )
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            RarityChip(rarity = rarity)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = badge.name,
              style = MaterialTheme.typography.headlineSmall,
              color = colors.textPrimary,
            )
            Text(
              text = "${badge.series} // TIER ${if (badge.owned) badge.tier else 0} OF ${badge.maxTier}",
              style = ObsidianTextStyles.LabelBadge,
              color = colors.cyan,
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = badge.description,
          style = MaterialTheme.typography.bodyMedium,
          color = colors.textSecondary,
        )

        Spacer(modifier = Modifier.height(12.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(colors.trough, RoundedCornerShape(4.dp))
            .border(1.dp, colors.borderIdle, RoundedCornerShape(4.dp))
            .padding(10.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Text(
              text = "SHARD RESERVE",
              style = ObsidianTextStyles.LabelBadge,
              color = colors.textSecondary,
            )
            Text(
              text = "$availableShards SHARDS",
              style = ObsidianTextStyles.LabelCode,
              color = colors.violetLight,
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          if (badge.owned) {
            SlateInsetButton(
              text = if (badge.equipped) "UNEQUIP" else "EQUIP SLOT",
              onClick = onToggleEquip,
              activeBorder = badge.equipped,
              modifier = Modifier
                .weight(1f)
                .testTag("equip_badge_button"),
            )
          }
          val craftLabel = when {
            !badge.owned -> "FORGE (${badge.craftCost})"
            badge.tier >= badge.maxTier -> "MAXED"
            else -> "UPGRADE (${badge.craftCost})"
          }
          VioletTranscendButton(
            text = craftLabel,
            onClick = onCraftOrUpgrade,
            enabled = canCraft,
            modifier = Modifier
              .weight(1f)
              .testTag("forge_badge_button"),
          )
        }
      }
    }
  }
}

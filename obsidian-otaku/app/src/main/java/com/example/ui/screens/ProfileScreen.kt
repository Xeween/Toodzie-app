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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.ArtworkItemEntity
import com.example.data.BadgeEntity
import com.example.data.GameActivityEntity
import com.example.data.LoreCommentEntity
import com.example.data.RarityTier
import com.example.data.SquadMember
import com.example.data.StatusMode
import com.example.ui.components.AchievementEmblemIcon
import com.example.ui.components.BadgeShieldEmblem
import com.example.ui.components.CrimsonButton
import com.example.ui.components.DoubleRingLevelCounter
import com.example.ui.components.FeaturedArtworkCanvas
import com.example.ui.components.HardDropPlate
import com.example.ui.components.HeroNightscapeBanner
import com.example.ui.components.MangaPanelCard
import com.example.ui.components.ObsidianTextField
import com.example.ui.components.OperatorAvatarEmblem
import com.example.ui.components.RarityChip
import com.example.ui.components.ShowcaseHeader
import com.example.ui.components.SlateInsetButton
import com.example.ui.components.TroughProgressBar
import com.example.ui.theme.ObsidianTextStyles
import com.example.ui.theme.ObsidianTheme
import com.example.ui.viewmodel.ObsidianUiState

@Composable
fun ProfileScreen(
  state: ObsidianUiState.Ready,
  onLikeArtwork: (String) -> Unit,
  onPostComment: (String) -> Unit,
  onNavigateToBadges: () -> Unit,
  onNavigateToAchievements: () -> Unit,
  onNavigateToLoadout: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var selectedArtwork by remember { mutableStateOf<ArtworkItemEntity?>(null) }
  var commentDraft by remember { mutableStateOf("") }

  val profile = state.profile
  val ringAccent = if (profile.levelRingAccent == "CRIMSON") {
    ObsidianTheme.colors.crimson
  } else {
    ObsidianTheme.colors.violet
  }
  val unlockedCount = state.achievements.count { it.unlocked }
  val pinnedAchievements = state.achievements.filter { it.pinned }.take(2)

  BoxWithConstraints(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianTheme.colors.canvas),
  ) {
    val isExpanded = maxWidth >= 840.dp

    if (isExpanded) {
      // Desktop / Tablet Asymmetric Profile Matrix (8 cols left / 4 cols right)
      Row(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
      ) {
        LazyColumn(
          modifier = Modifier.weight(0.65f),
          contentPadding = PaddingValues(vertical = 16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          item {
            HeroBannerHeaderCard(
              state = state,
              onEditClick = onNavigateToLoadout,
            )
          }
          item {
            FeaturedArtworkShowcase(
              artworks = state.artworks,
              screentoneEnabled = profile.screentoneEnabled,
              onSelectArtwork = { selectedArtwork = it },
              onLikeArtwork = onLikeArtwork,
            )
          }
          if (pinnedAchievements.isNotEmpty()) {
            item {
              PinnedAchievementsMiniModule(
                pinned = pinnedAchievements,
                screentoneEnabled = profile.screentoneEnabled,
                onViewAll = onNavigateToAchievements,
              )
            }
          }
          item {
            RecentGameActivityModule(
              games = state.gameActivities,
              screentoneEnabled = profile.screentoneEnabled,
            )
          }
          item {
            OperatorCommentWall(
              comments = state.loreComments,
              draft = commentDraft,
              onDraftChange = { commentDraft = it },
              onSubmit = {
                onPostComment(commentDraft)
                commentDraft = ""
              },
            )
          }
        }

        LazyColumn(
          modifier = Modifier.weight(0.35f),
          contentPadding = PaddingValues(vertical = 16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          item {
            ProfileSummaryModule(
              state = state,
              ringAccent = ringAccent,
              unlockedCount = unlockedCount,
              onNavigateToBadges = onNavigateToBadges,
              onNavigateToAchievements = onNavigateToAchievements,
            )
          }
          item {
            SquadRosterModule(squad = state.squadMembers)
          }
        }
      }
    } else {
      // Mobile Compact Single-Column Stacked Flow
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        item {
          HeroBannerHeaderCard(
            state = state,
            onEditClick = onNavigateToLoadout,
          )
        }
        item {
          ProfileSummaryModule(
            state = state,
            ringAccent = ringAccent,
            unlockedCount = unlockedCount,
            onNavigateToBadges = onNavigateToBadges,
            onNavigateToAchievements = onNavigateToAchievements,
          )
        }
        item {
          FeaturedArtworkShowcase(
            artworks = state.artworks,
            screentoneEnabled = profile.screentoneEnabled,
            onSelectArtwork = { selectedArtwork = it },
            onLikeArtwork = onLikeArtwork,
          )
        }
        if (pinnedAchievements.isNotEmpty()) {
          item {
            PinnedAchievementsMiniModule(
              pinned = pinnedAchievements,
              screentoneEnabled = profile.screentoneEnabled,
              onViewAll = onNavigateToAchievements,
            )
          }
        }
        item {
          RecentGameActivityModule(
            games = state.gameActivities,
            screentoneEnabled = profile.screentoneEnabled,
          )
        }
        item {
          SquadRosterModule(squad = state.squadMembers)
        }
        item {
          OperatorCommentWall(
            comments = state.loreComments,
            draft = commentDraft,
            onDraftChange = { commentDraft = it },
            onSubmit = {
              onPostComment(commentDraft)
              commentDraft = ""
            },
          )
        }
      }
    }
  }

  selectedArtwork?.let { art ->
    val liveArt = state.artworks.firstOrNull { it.id == art.id } ?: art
    ArtworkInspectDialog(
      artwork = liveArt,
      onLike = { onLikeArtwork(liveArt.id) },
      onDismiss = { selectedArtwork = null },
    )
  }
}

@Composable
private fun HeroBannerHeaderCard(
  state: ObsidianUiState.Ready,
  onEditClick: () -> Unit,
) {
  val profile = state.profile
  val colors = ObsidianTheme.colors
  val statusMode = StatusMode.fromString(profile.statusMode)
  val statusColor = when (statusMode) {
    StatusMode.IN_GAME -> colors.cyan
    StatusMode.ONLINE -> colors.violet
    StatusMode.STEALTH -> colors.textSecondary
    StatusMode.AFK -> colors.crimson
  }

  MangaPanelCard(
    borderColor = colors.borderSubtle,
    contentPadding = PaddingValues(0.dp),
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(176.dp),
    ) {
      HeroNightscapeBanner(modifier = Modifier.fillMaxSize())

      // Top telemetry bar
      if (profile.telemetryTickerEnabled) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Row(
            modifier = Modifier
              .background(colors.trough.copy(alpha = 0.85f), RoundedCornerShape(2.dp))
              .border(1.dp, statusColor.copy(alpha = 0.6f), RoundedCornerShape(2.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Box(
              modifier = Modifier
                .size(7.dp)
                .background(statusColor, CircleShape),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (statusMode == StatusMode.IN_GAME) {
                "${statusMode.label} // ${profile.activeGame}"
              } else {
                "${statusMode.label} // ${profile.serverRegion}"
              },
              style = ObsidianTextStyles.LabelBadge,
              color = colors.textPrimary,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }

          IconButton(
            onClick = onEditClick,
            modifier = Modifier
              .testTag("edit_profile_button")
              .size(48.dp),
          ) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .background(colors.trough.copy(alpha = 0.85f), RoundedCornerShape(4.dp))
                .border(1.dp, colors.borderSubtle, RoundedCornerShape(4.dp)),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = stringResource(R.string.cd_edit_profile),
                tint = colors.textPrimary,
                modifier = Modifier.size(16.dp),
              )
            }
          }
        }
      }

      // Bottom operator identity row overlapping banner edge
      Row(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Bottom,
      ) {
        OperatorAvatarEmblem(
          size = 76.dp,
          contentDescriptionText = stringResource(R.string.cd_operator_avatar),
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
          Box(
            modifier = Modifier
              .background(colors.crimsonMuted, RoundedCornerShape(2.dp))
              .border(1.dp, colors.crimson, RoundedCornerShape(2.dp))
              .padding(horizontal = 6.dp, vertical = 2.dp),
          ) {
            Text(
              text = profile.rankTitle,
              style = ObsidianTextStyles.LabelBadge,
              color = colors.textPrimary,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = profile.handle,
            style = MaterialTheme.typography.headlineLarge,
            color = colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
          Text(
            text = profile.serverRegion,
            style = ObsidianTextStyles.LabelCode,
            color = colors.textSecondary,
            maxLines = 1,
          )
        }
      }
    }
  }
}

@Composable
private fun ProfileSummaryModule(
  state: ObsidianUiState.Ready,
  ringAccent: Color,
  unlockedCount: Int,
  onNavigateToBadges: () -> Unit,
  onNavigateToAchievements: () -> Unit,
) {
  val profile = state.profile
  val colors = ObsidianTheme.colors
  val xpProgress = profile.currentXp.toFloat() / profile.maxXp.coerceAtLeast(1).toFloat()

  MangaPanelCard(
    borderColor = colors.borderIdle,
    showScreentone = profile.screentoneEnabled,
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      DoubleRingLevelCounter(
        level = profile.level,
        progress = xpProgress,
        accentColor = ringAccent,
      )
      Spacer(modifier = Modifier.width(14.dp))
      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = "OPERATOR XP",
            style = ObsidianTextStyles.LabelBadge,
            color = colors.textSecondary,
          )
          Text(
            text = "${profile.currentXp} / ${profile.maxXp}",
            style = ObsidianTextStyles.LabelCode,
            color = colors.cyan,
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        TroughProgressBar(
          progress = xpProgress,
          fillColor = ringAccent,
          height = 8.dp,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "${profile.maxXp - profile.currentXp} XP TO LVL ${profile.level + 1} // ${profile.shards} SHARDS",
          style = ObsidianTextStyles.LabelBadge,
          color = colors.textSecondary,
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))
    Text(
      text = profile.bio,
      style = MaterialTheme.typography.bodyMedium,
      color = colors.textSecondary,
    )

    Spacer(modifier = Modifier.height(14.dp))
    HorizontalDivider(color = colors.borderIdle, thickness = 1.dp)
    Spacer(modifier = Modifier.height(12.dp))

    // Dense hub telemetry metrics
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      StatCell(
        label = "GAMES",
        value = profile.gamesOwned.toString(),
        modifier = Modifier.weight(1f),
      )
      StatCell(
        label = "TROPHIES",
        value = "$unlockedCount/${state.achievements.size}",
        modifier = Modifier
          .weight(1f)
          .clickable(onClick = onNavigateToAchievements),
      )
      StatCell(
        label = "RELICS",
        value = state.badges.count { it.owned }.toString(),
        modifier = Modifier
          .weight(1f)
          .clickable(onClick = onNavigateToBadges),
      )
      StatCell(
        label = "CAPTURES",
        value = profile.screenshotsCount.toString(),
        modifier = Modifier.weight(1f),
      )
    }

    Spacer(modifier = Modifier.height(16.dp))
    ShowcaseHeader(
      title = stringResource(R.string.showcase_badge_collection),
      tag = "${state.equippedBadges.size}/3 EQUIPPED",
      accentColor = colors.violet,
    )

    if (state.equippedBadges.isEmpty()) {
      SlateInsetButton(
        text = "EQUIP RELICS IN FORGE",
        onClick = onNavigateToBadges,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("empty_equip_badges_button"),
      )
    } else {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        state.equippedBadges.forEach { badge ->
          EquippedBadgeMiniPlate(
            badge = badge,
            onClick = onNavigateToBadges,
            modifier = Modifier.weight(1f),
          )
        }
      }
    }
  }
}

@Composable
private fun StatCell(
  label: String,
  value: String,
  modifier: Modifier = Modifier,
) {
  val colors = ObsidianTheme.colors
  Column(
    modifier = modifier
      .background(colors.trough, RoundedCornerShape(4.dp))
      .border(1.dp, colors.borderIdle, RoundedCornerShape(4.dp))
      .padding(horizontal = 8.dp, vertical = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = value,
      style = ObsidianTextStyles.LabelNumeric,
      color = colors.textPrimary,
      maxLines = 1,
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = label,
      style = ObsidianTextStyles.LabelBadge,
      color = colors.textSecondary,
      maxLines = 1,
    )
  }
}

@Composable
private fun EquippedBadgeMiniPlate(
  badge: BadgeEntity,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = ObsidianTheme.colors
  val rarity = RarityTier.fromString(badge.rarityTier)
  val border = when (rarity) {
    RarityTier.MYTHIC -> colors.violet
    RarityTier.RARE -> colors.cyan
    RarityTier.COMMON -> colors.borderSubtle
  }
  Column(
    modifier = modifier
      .clip(RoundedCornerShape(4.dp))
      .background(colors.surfaceElevated)
      .border(1.dp, border.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
      .clickable(onClick = onClick)
      .padding(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    BadgeShieldEmblem(
      iconKey = badge.iconKey,
      rarityTier = rarity,
      owned = badge.owned,
      size = 38.dp,
      contentDescriptionText = "${badge.name} shield",
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = badge.name,
      style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
      color = colors.textPrimary,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
    )
    Text(
      text = "TIER ${badge.tier}",
      style = ObsidianTextStyles.LabelBadge,
      color = colors.violetLight,
      maxLines = 1,
    )
  }
}

@Composable
private fun FeaturedArtworkShowcase(
  artworks: List<ArtworkItemEntity>,
  screentoneEnabled: Boolean,
  onSelectArtwork: (ArtworkItemEntity) -> Unit,
  onLikeArtwork: (String) -> Unit,
) {
  val colors = ObsidianTheme.colors
  MangaPanelCard(
    borderColor = colors.borderIdle,
    showScreentone = screentoneEnabled,
  ) {
    ShowcaseHeader(
      title = stringResource(R.string.showcase_featured_artwork),
      tag = "${artworks.size} PLATES",
      accentColor = colors.crimson,
    )
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      items(artworks, key = { it.id }) { artwork ->
        Column(
          modifier = Modifier
            .width(248.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(colors.surfaceElevated)
            .border(1.dp, colors.borderSubtle, RoundedCornerShape(4.dp))
            .clickable { onSelectArtwork(artwork) }
            .testTag("artwork_card_${artwork.id}"),
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(136.dp),
          ) {
            FeaturedArtworkCanvas(
              artKey = artwork.artKey,
              contentDescriptionText = artwork.title,
              modifier = Modifier.fillMaxSize(),
            )
            Box(
              modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .background(colors.trough.copy(alpha = 0.88f), RoundedCornerShape(2.dp))
                .border(1.dp, colors.crimson, RoundedCornerShape(2.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
              Text(
                text = artwork.tag,
                style = ObsidianTextStyles.LabelBadge,
                color = colors.textPrimary,
              )
            }
          }
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = artwork.title,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )
              Text(
                text = artwork.artist,
                style = ObsidianTextStyles.LabelBadge,
                color = colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )
            }
            Row(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .minimumInteractiveComponentSize()
                .clickable { onLikeArtwork(artwork.id) }
                .padding(horizontal = 6.dp, vertical = 4.dp)
                .testTag("like_artwork_${artwork.id}"),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(
                imageVector = if (artwork.likedByUser) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = stringResource(R.string.cd_like_artwork),
                tint = if (artwork.likedByUser) colors.crimson else colors.textSecondary,
                modifier = Modifier.size(16.dp),
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = artwork.likes.toString(),
                style = ObsidianTextStyles.LabelCode,
                color = if (artwork.likedByUser) colors.crimson else colors.textSecondary,
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun PinnedAchievementsMiniModule(
  pinned: List<com.example.data.AchievementEntity>,
  screentoneEnabled: Boolean,
  onViewAll: () -> Unit,
) {
  val colors = ObsidianTheme.colors
  MangaPanelCard(
    borderColor = colors.crimson.copy(alpha = 0.65f),
    showScreentone = screentoneEnabled,
  ) {
    ShowcaseHeader(
      title = "/// PINNED ACHIEVEMENTS",
      tag = "SHOWCASE",
      accentColor = colors.crimson,
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      pinned.forEach { ach ->
        val rarity = RarityTier.fromString(ach.rarityTier)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(colors.trough, RoundedCornerShape(4.dp))
            .border(1.dp, colors.borderIdle, RoundedCornerShape(4.dp))
            .clickable(onClick = onViewAll)
            .padding(10.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          AchievementEmblemIcon(
            iconKey = ach.iconKey,
            rarityTier = rarity,
            unlocked = ach.unlocked,
            size = 48.dp,
            contentDescriptionText = ach.title,
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = ach.title,
              style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
              color = colors.textPrimary,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
            Text(
              text = ach.gameTitle,
              style = ObsidianTextStyles.LabelBadge,
              color = colors.crimson,
              maxLines = 1,
            )
          }
          Box(
            modifier = Modifier
              .background(colors.surfaceMid, RoundedCornerShape(2.dp))
              .border(1.dp, colors.borderSubtle, RoundedCornerShape(2.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp),
          ) {
            Text(
              text = "${ach.rarityPercent}%",
              style = ObsidianTextStyles.LabelCode,
              color = colors.cyan,
            )
          }
        }
      }
    }
  }
}

@Composable
private fun RecentGameActivityModule(
  games: List<GameActivityEntity>,
  screentoneEnabled: Boolean,
) {
  val colors = ObsidianTheme.colors
  MangaPanelCard(
    borderColor = colors.borderIdle,
    showScreentone = screentoneEnabled,
  ) {
    ShowcaseHeader(
      title = stringResource(R.string.showcase_recent_activity),
      tag = "427.2 HRS",
      accentColor = colors.cyan,
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      games.forEach { game ->
        val progress = game.unlockedCount.toFloat() / game.totalCount.coerceAtLeast(1).toFloat()
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(colors.surfaceElevated, RoundedCornerShape(4.dp))
            .border(1.dp, colors.borderIdle, RoundedCornerShape(4.dp))
            .padding(12.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f),
            ) {
              AchievementEmblemIcon(
                iconKey = game.coverKey,
                rarityTier = RarityTier.RARE,
                unlocked = true,
                size = 44.dp,
                contentDescriptionText = game.title,
              )
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = game.title,
                  style = MaterialTheme.typography.titleMedium,
                  color = colors.textPrimary,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                )
                Text(
                  text = game.genre,
                  style = ObsidianTextStyles.LabelBadge,
                  color = colors.textSecondary,
                  maxLines = 1,
                )
              }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "${game.hoursPlayed} HRS",
                style = ObsidianTextStyles.LabelCode,
                color = colors.textPrimary,
              )
              Text(
                text = game.lastPlayed,
                style = ObsidianTextStyles.LabelBadge,
                color = if (game.lastPlayed == "ACTIVE NOW") colors.cyan else colors.textSecondary,
              )
            }
          }
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Text(
              text = "ACHIEVEMENTS ${game.unlockedCount}/${game.totalCount}",
              style = ObsidianTextStyles.LabelBadge,
              color = colors.textSecondary,
            )
            Text(
              text = "${(progress * 100).toInt()}%",
              style = ObsidianTextStyles.LabelBadge,
              color = colors.crimson,
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          TroughProgressBar(
            progress = progress,
            fillColor = colors.crimson,
            height = 7.dp,
          )
        }
      }
    }
  }
}

@Composable
private fun SquadRosterModule(
  squad: List<SquadMember>,
) {
  val colors = ObsidianTheme.colors
  MangaPanelCard(borderColor = colors.borderIdle) {
    ShowcaseHeader(
      title = stringResource(R.string.showcase_squad_roster),
      tag = "${squad.count { it.statusMode == StatusMode.ONLINE || it.statusMode == StatusMode.IN_GAME }} ACTIVE",
      accentColor = colors.violet,
    )
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      items(squad, key = { it.handle }) { member ->
        val dotColor = when (member.statusMode) {
          StatusMode.IN_GAME -> colors.cyan
          StatusMode.ONLINE -> colors.violet
          StatusMode.STEALTH -> colors.textMuted
          StatusMode.AFK -> colors.crimson
        }
        Column(
          modifier = Modifier
            .width(138.dp)
            .background(colors.surfaceElevated, RoundedCornerShape(4.dp))
            .border(1.dp, colors.borderIdle, RoundedCornerShape(4.dp))
            .padding(10.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .background(dotColor, CircleShape),
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = member.handle,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp),
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )
            }
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "LVL ${member.level} // ${member.statusMode.label}",
            style = ObsidianTextStyles.LabelBadge,
            color = dotColor,
            maxLines = 1,
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = member.activity,
            style = MaterialTheme.typography.bodySmall,
            color = colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }
    }
  }
}

@Composable
private fun OperatorCommentWall(
  comments: List<LoreCommentEntity>,
  draft: String,
  onDraftChange: (String) -> Unit,
  onSubmit: () -> Unit,
) {
  val colors = ObsidianTheme.colors
  MangaPanelCard(borderColor = colors.borderIdle) {
    ShowcaseHeader(
      title = stringResource(R.string.showcase_operator_feed),
      tag = "${comments.size} LOGS",
      accentColor = colors.crimson,
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      ObsidianTextField(
        value = draft,
        onValueChange = onDraftChange,
        placeholder = "Leave +rep or lore note on wall...",
        modifier = Modifier
          .weight(1f)
          .testTag("lore_comment_input"),
      )
      Spacer(modifier = Modifier.width(8.dp))
      CrimsonButton(
        text = "POST",
        onClick = onSubmit,
        enabled = draft.isNotBlank(),
        modifier = Modifier.testTag("post_comment_button"),
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.Send,
          contentDescription = stringResource(R.string.cd_post_comment),
          modifier = Modifier.size(18.dp),
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      comments.forEach { comment ->
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              if (comment.isSelf) colors.crimsonMuted.copy(alpha = 0.45f) else colors.trough,
              RoundedCornerShape(4.dp),
            )
            .border(
              1.dp,
              if (comment.isSelf) colors.crimson.copy(alpha = 0.6f) else colors.borderIdle,
              RoundedCornerShape(4.dp),
            )
            .padding(12.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = comment.authorHandle,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                color = colors.textPrimary,
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .background(colors.surfaceElevated, RoundedCornerShape(2.dp))
                  .border(1.dp, colors.borderSubtle, RoundedCornerShape(2.dp))
                  .padding(horizontal = 5.dp, vertical = 1.dp),
              ) {
                Text(
                  text = "LVL ${comment.authorLevel}",
                  style = ObsidianTextStyles.LabelBadge,
                  color = colors.violetLight,
                )
              }
            }
            Text(
              text = comment.timestampText,
              style = ObsidianTextStyles.LabelBadge,
              color = colors.crimson,
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = comment.message,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textSecondary,
          )
        }
      }
    }
  }
}

@Composable
private fun ArtworkInspectDialog(
  artwork: ArtworkItemEntity,
  onLike: () -> Unit,
  onDismiss: () -> Unit,
) {
  val colors = ObsidianTheme.colors
  Dialog(onDismissRequest = onDismiss) {
    HardDropPlate(borderColor = colors.crimson) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Outlined.MilitaryTech,
              contentDescription = null,
              tint = colors.crimson,
              modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "ARTWORK INSPECT",
              style = ObsidianTextStyles.LabelCode,
              color = colors.cyan,
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = stringResource(R.string.cd_close_dialog),
              tint = colors.textSecondary,
            )
          }
        }
        Spacer(modifier = Modifier.height(8.dp))
        FeaturedArtworkCanvas(
          artKey = artwork.artKey,
          contentDescriptionText = artwork.title,
          modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, colors.borderSubtle, RoundedCornerShape(4.dp)),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = artwork.title,
          style = MaterialTheme.typography.headlineSmall,
          color = colors.textPrimary,
        )
        Text(
          text = "${artwork.artist} // ${artwork.dimensionsLabel}",
          style = ObsidianTextStyles.LabelCode,
          color = colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(14.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          RarityChip(rarity = RarityTier.MYTHIC)
          Spacer(modifier = Modifier.weight(1f))
          CrimsonButton(
            text = if (artwork.likedByUser) "ENDORSED (${artwork.likes})" else "ENDORSE (${artwork.likes})",
            onClick = onLike,
            modifier = Modifier.testTag("dialog_like_artwork_button"),
          )
        }
      }
    }
  }
}

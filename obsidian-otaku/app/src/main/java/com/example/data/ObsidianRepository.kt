package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class ObsidianRepository(private val dao: ObsidianDao) {
  private val seedMutex = Mutex()

  val profile: Flow<OperatorProfileEntity?> = dao.observeProfile()
  val achievements: Flow<List<AchievementEntity>> = dao.observeAchievements()
  val badges: Flow<List<BadgeEntity>> = dao.observeBadges()
  val artworks: Flow<List<ArtworkItemEntity>> = dao.observeArtworks()
  val gameActivities: Flow<List<GameActivityEntity>> = dao.observeGameActivities()
  val loreComments: Flow<List<LoreCommentEntity>> = dao.observeLoreComments()

  suspend fun ensureSeeded() = withContext(Dispatchers.IO) {
    seedMutex.withLock {
      if (dao.getProfileOnce() == null) {
        dao.upsertProfile(SeedData.initialProfile)
        dao.insertAchievements(SeedData.initialAchievements)
        dao.insertBadges(SeedData.initialBadges)
        dao.insertArtworks(SeedData.initialArtworks)
        dao.insertGameActivities(SeedData.initialGameActivities)
        dao.insertLoreComments(SeedData.initialComments)
      }
    }
  }

  suspend fun toggleAchievementUnlock(id: String): String = withContext(Dispatchers.IO) {
    val item = dao.getAchievementById(id) ?: return@withContext "Achievement not found"
    val profile = dao.getProfileOnce() ?: return@withContext "Profile missing"
    val newUnlocked = !item.unlocked
    val updatedAchievement = item.copy(
      unlocked = newUnlocked,
      unlockTimestamp = if (newUnlocked) "2026.11.16 // JUST NOW" else "LOCKED // ${item.progressCurrent} OF ${item.progressTarget}",
      progressCurrent = if (newUnlocked) item.progressTarget else (item.progressTarget - 1).coerceAtLeast(0),
    )
    dao.updateAchievement(updatedAchievement)

    val xpDelta = if (newUnlocked) item.xpReward else -item.xpReward
    val updatedProfile = applyXpDelta(profile, xpDelta)
    dao.upsertProfile(updatedProfile)

    if (newUnlocked) "Unlocked '${item.title}' (+${item.xpReward} XP)" else "Reset '${item.title}' to locked state"
  }

  suspend fun toggleAchievementPin(id: String): String = withContext(Dispatchers.IO) {
    val item = dao.getAchievementById(id) ?: return@withContext "Achievement not found"
    dao.updateAchievement(item.copy(pinned = !item.pinned))
    if (!item.pinned) "Pinned '${item.title}' to showcase" else "Unpinned '${item.title}'"
  }

  suspend fun toggleBadgeEquip(id: String): String = withContext(Dispatchers.IO) {
    val badge = dao.getBadgeById(id) ?: return@withContext "Badge not found"
    if (!badge.owned) return@withContext "Forge this relic before equipping"
    if (!badge.equipped && dao.countEquippedBadges() >= 3) {
      return@withContext "Showcase full (max 3 relics). Unequip one first."
    }
    val nextEquipped = !badge.equipped
    dao.updateBadge(badge.copy(equipped = nextEquipped))
    if (nextEquipped) "Equipped '${badge.name}' to Profile Showcase" else "Removed '${badge.name}' from Showcase"
  }

  suspend fun craftOrUpgradeBadge(id: String): String = withContext(Dispatchers.IO) {
    val badge = dao.getBadgeById(id) ?: return@withContext "Badge not found"
    val profile = dao.getProfileOnce() ?: return@withContext "Profile missing"
    if (badge.owned && badge.tier >= badge.maxTier) {
      return@withContext "'${badge.name}' is already MAX TIER"
    }
    if (profile.shards < badge.craftCost) {
      return@withContext "Insufficient Shards (need ${badge.craftCost})"
    }

    val nextOwned = true
    val nextTier = if (!badge.owned) 1 else (badge.tier + 1).coerceAtMost(badge.maxTier)
    val nextCost = if (nextTier >= badge.maxTier) badge.craftCost else badge.craftCost + 100
    dao.updateBadge(
      badge.copy(
        owned = nextOwned,
        tier = nextTier,
        craftCost = nextCost,
      )
    )

    val xpBonus = 250
    val updatedProfile = applyXpDelta(
      profile.copy(shards = profile.shards - badge.craftCost),
      xpBonus,
    )
    dao.upsertProfile(updatedProfile)
    if (!badge.owned) {
      "Forged '${badge.name}' TIER I (+250 XP)"
    } else {
      "Upgraded '${badge.name}' to TIER $nextTier (+250 XP)"
    }
  }

  suspend fun toggleArtworkLike(id: String) = withContext(Dispatchers.IO) {
    val art = dao.getArtworkById(id) ?: return@withContext
    val nextLiked = !art.likedByUser
    val nextLikes = if (nextLiked) art.likes + 1 else (art.likes - 1).coerceAtLeast(0)
    dao.updateArtwork(art.copy(likedByUser = nextLiked, likes = nextLikes))
  }

  suspend fun postLoreComment(message: String) = withContext(Dispatchers.IO) {
    val trimmed = message.trim()
    if (trimmed.isEmpty()) return@withContext
    val profile = dao.getProfileOnce() ?: return@withContext
    dao.insertLoreComment(
      LoreCommentEntity(
        authorHandle = profile.handle,
        authorLevel = profile.level,
        authorTag = profile.rankTitle,
        message = trimmed,
        timestampText = "JUST NOW",
        isSelf = true,
        createdAtMillis = System.currentTimeMillis(),
      )
    )
  }

  suspend fun updateProfileIdentity(
    handle: String,
    rankTitle: String,
    bio: String,
    statusMode: StatusMode,
    activeGame: String,
  ) = withContext(Dispatchers.IO) {
    val profile = dao.getProfileOnce() ?: return@withContext
    dao.upsertProfile(
      profile.copy(
        handle = handle.trim().ifEmpty { profile.handle }.uppercase(),
        rankTitle = rankTitle.trim().ifEmpty { profile.rankTitle }.uppercase(),
        bio = bio.trim().ifEmpty { profile.bio },
        statusMode = statusMode.name,
        activeGame = activeGame.trim().ifEmpty { profile.activeGame }.uppercase(),
      )
    )
  }

  suspend fun updateHudSettings(
    levelRingAccent: String,
    screentoneEnabled: Boolean,
    telemetryTickerEnabled: Boolean,
  ) = withContext(Dispatchers.IO) {
    val profile = dao.getProfileOnce() ?: return@withContext
    dao.upsertProfile(
      profile.copy(
        levelRingAccent = levelRingAccent,
        screentoneEnabled = screentoneEnabled,
        telemetryTickerEnabled = telemetryTickerEnabled,
      )
    )
  }

  private fun applyXpDelta(profile: OperatorProfileEntity, delta: Int): OperatorProfileEntity {
    var level = profile.level
    var xp = profile.currentXp + delta
    while (xp >= profile.maxXp) {
      xp -= profile.maxXp
      level += 1
    }
    while (xp < 0 && level > 1) {
      level -= 1
      xp += profile.maxXp
    }
    return profile.copy(
      level = level,
      currentXp = xp.coerceAtLeast(0),
    )
  }
}

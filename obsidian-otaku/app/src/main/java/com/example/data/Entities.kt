package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RarityTier(val label: String) {
  COMMON("COMMON"),
  RARE("RARE"),
  MYTHIC("MYTHIC");

  companion object {
    fun fromString(value: String): RarityTier =
      entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: COMMON
  }
}

enum class StatusMode(val label: String) {
  IN_GAME("IN-GAME"),
  ONLINE("ONLINE"),
  STEALTH("STEALTH"),
  AFK("AFK");

  companion object {
    fun fromString(value: String): StatusMode =
      entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: ONLINE
  }
}

@Entity(tableName = "operator_profile")
data class OperatorProfileEntity(
  @PrimaryKey val id: Int = 1,
  val handle: String,
  val rankTitle: String,
  val bio: String,
  val serverRegion: String,
  val statusMode: String,
  val activeGame: String,
  val level: Int,
  val currentXp: Int,
  val maxXp: Int,
  val shards: Int,
  val gamesOwned: Int,
  val screenshotsCount: Int,
  val levelRingAccent: String,
  val screentoneEnabled: Boolean,
  val telemetryTickerEnabled: Boolean,
)

@Entity(tableName = "achievements")
data class AchievementEntity(
  @PrimaryKey val id: String,
  val gameTitle: String,
  val title: String,
  val description: String,
  val rarityPercent: Float,
  val rarityTier: String,
  val unlocked: Boolean,
  val unlockTimestamp: String,
  val progressCurrent: Int,
  val progressTarget: Int,
  val xpReward: Int,
  val iconKey: String,
  val loreNote: String,
  val pinned: Boolean,
)

@Entity(tableName = "badges")
data class BadgeEntity(
  @PrimaryKey val id: String,
  val name: String,
  val series: String,
  val tier: Int,
  val maxTier: Int,
  val rarityTier: String,
  val owned: Boolean,
  val equipped: Boolean,
  val craftCost: Int,
  val iconKey: String,
  val description: String,
)

@Entity(tableName = "artworks")
data class ArtworkItemEntity(
  @PrimaryKey val id: String,
  val title: String,
  val artist: String,
  val tag: String,
  val likes: Int,
  val likedByUser: Boolean,
  val artKey: String,
  val dimensionsLabel: String,
)

@Entity(tableName = "game_activities")
data class GameActivityEntity(
  @PrimaryKey val id: String,
  val title: String,
  val genre: String,
  val hoursPlayed: Float,
  val lastPlayed: String,
  val unlockedCount: Int,
  val totalCount: Int,
  val coverKey: String,
)

@Entity(tableName = "lore_comments")
data class LoreCommentEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val authorHandle: String,
  val authorLevel: Int,
  val authorTag: String,
  val message: String,
  val timestampText: String,
  val isSelf: Boolean,
  val createdAtMillis: Long,
)

data class SquadMember(
  val handle: String,
  val level: Int,
  val statusMode: StatusMode,
  val activity: String,
  val emblemKey: String,
)

data class SystemPatchLog(
  val version: String,
  val date: String,
  val tag: String,
  val title: String,
  val summary: String,
)

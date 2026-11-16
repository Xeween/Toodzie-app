package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ObsidianDao {
  @Query("SELECT * FROM operator_profile WHERE id = 1")
  fun observeProfile(): Flow<OperatorProfileEntity?>

  @Query("SELECT * FROM operator_profile WHERE id = 1")
  suspend fun getProfileOnce(): OperatorProfileEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertProfile(profile: OperatorProfileEntity)

  @Query("SELECT * FROM achievements ORDER BY unlocked DESC, rarityPercent ASC")
  fun observeAchievements(): Flow<List<AchievementEntity>>

  @Query("SELECT * FROM achievements WHERE id = :id")
  suspend fun getAchievementById(id: String): AchievementEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAchievements(items: List<AchievementEntity>)

  @Update
  suspend fun updateAchievement(item: AchievementEntity)

  @Query("SELECT * FROM badges ORDER BY equipped DESC, owned DESC, tier DESC")
  fun observeBadges(): Flow<List<BadgeEntity>>

  @Query("SELECT * FROM badges WHERE id = :id")
  suspend fun getBadgeById(id: String): BadgeEntity?

  @Query("SELECT COUNT(*) FROM badges WHERE equipped = 1")
  suspend fun countEquippedBadges(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBadges(items: List<BadgeEntity>)

  @Update
  suspend fun updateBadge(badge: BadgeEntity)

  @Query("SELECT * FROM artworks")
  fun observeArtworks(): Flow<List<ArtworkItemEntity>>

  @Query("SELECT * FROM artworks WHERE id = :id")
  suspend fun getArtworkById(id: String): ArtworkItemEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertArtworks(items: List<ArtworkItemEntity>)

  @Update
  suspend fun updateArtwork(item: ArtworkItemEntity)

  @Query("SELECT * FROM game_activities ORDER BY hoursPlayed DESC")
  fun observeGameActivities(): Flow<List<GameActivityEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGameActivities(items: List<GameActivityEntity>)

  @Query("SELECT * FROM lore_comments ORDER BY createdAtMillis DESC")
  fun observeLoreComments(): Flow<List<LoreCommentEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLoreComment(comment: LoreCommentEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLoreComments(comments: List<LoreCommentEntity>)
}

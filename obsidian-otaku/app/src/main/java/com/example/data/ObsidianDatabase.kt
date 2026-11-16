package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [
    OperatorProfileEntity::class,
    AchievementEntity::class,
    BadgeEntity::class,
    ArtworkItemEntity::class,
    GameActivityEntity::class,
    LoreCommentEntity::class,
  ],
  version = 1,
  exportSchema = false,
)
abstract class ObsidianDatabase : RoomDatabase() {
  abstract fun obsidianDao(): ObsidianDao

  companion object {
    @Volatile
    private var INSTANCE: ObsidianDatabase? = null

    fun getInstance(context: Context): ObsidianDatabase {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: Room.databaseBuilder(
          context.applicationContext,
          ObsidianDatabase::class.java,
          "obsidian_otaku.db",
        ).build().also { INSTANCE = it }
      }
    }
  }
}

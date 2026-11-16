package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AchievementEntity
import com.example.data.ArtworkItemEntity
import com.example.data.BadgeEntity
import com.example.data.GameActivityEntity
import com.example.data.LoreCommentEntity
import com.example.data.ObsidianRepository
import com.example.data.OperatorProfileEntity
import com.example.data.RarityTier
import com.example.data.SeedData
import com.example.data.SquadMember
import com.example.data.StatusMode
import com.example.data.SystemPatchLog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AchievementFilter(val label: String) {
  ALL("ALL"),
  UNLOCKED("UNLOCKED"),
  LOCKED("LOCKED"),
  MYTHIC("MYTHIC"),
}

sealed interface ObsidianUiState {
  data object Loading : ObsidianUiState
  data class Ready(
    val profile: OperatorProfileEntity,
    val achievements: List<AchievementEntity>,
    val filteredAchievements: List<AchievementEntity>,
    val achievementFilter: AchievementFilter,
    val achievementSearch: String,
    val badges: List<BadgeEntity>,
    val equippedBadges: List<BadgeEntity>,
    val artworks: List<ArtworkItemEntity>,
    val gameActivities: List<GameActivityEntity>,
    val loreComments: List<LoreCommentEntity>,
    val squadMembers: List<SquadMember>,
    val patchLogs: List<SystemPatchLog>,
  ) : ObsidianUiState
}

class ObsidianViewModel(
  private val repository: ObsidianRepository,
) : ViewModel() {

  private val achievementFilter = MutableStateFlow(AchievementFilter.ALL)
  private val achievementSearch = MutableStateFlow("")

  private val _events = MutableSharedFlow<String>()
  val events: SharedFlow<String> = _events.asSharedFlow()

  init {
    viewModelScope.launch {
      repository.ensureSeeded()
    }
  }

  private val catalogFlow = combine(
    repository.profile,
    repository.achievements,
    repository.badges,
    repository.artworks,
    repository.gameActivities,
  ) { profile, achievements, badges, artworks, games ->
    CatalogBundle(profile, achievements, badges, artworks, games)
  }

  val uiState: StateFlow<ObsidianUiState> = combine(
    catalogFlow,
    repository.loreComments,
    achievementFilter,
    achievementSearch,
  ) { catalog, comments, filter, query ->
    val profile = catalog.profile ?: return@combine ObsidianUiState.Loading
    val normalizedQuery = query.trim().lowercase()
    val filtered = catalog.achievements.filter { ach ->
      val matchesFilter = when (filter) {
        AchievementFilter.ALL -> true
        AchievementFilter.UNLOCKED -> ach.unlocked
        AchievementFilter.LOCKED -> !ach.unlocked
        AchievementFilter.MYTHIC -> RarityTier.fromString(ach.rarityTier) == RarityTier.MYTHIC
      }
      val matchesSearch = normalizedQuery.isEmpty() ||
        ach.title.lowercase().contains(normalizedQuery) ||
        ach.gameTitle.lowercase().contains(normalizedQuery) ||
        ach.description.lowercase().contains(normalizedQuery)
      matchesFilter && matchesSearch
    }

    ObsidianUiState.Ready(
      profile = profile,
      achievements = catalog.achievements,
      filteredAchievements = filtered,
      achievementFilter = filter,
      achievementSearch = query,
      badges = catalog.badges,
      equippedBadges = catalog.badges.filter { it.equipped }.take(3),
      artworks = catalog.artworks,
      gameActivities = catalog.gameActivities,
      loreComments = comments,
      squadMembers = SeedData.squadMembers,
      patchLogs = SeedData.systemPatchLogs,
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = ObsidianUiState.Loading,
  )

  fun setAchievementFilter(filter: AchievementFilter) {
    achievementFilter.value = filter
  }

  fun setAchievementSearch(query: String) {
    achievementSearch.value = query
  }

  fun toggleAchievementUnlock(id: String) {
    viewModelScope.launch {
      val msg = repository.toggleAchievementUnlock(id)
      _events.emit(msg)
    }
  }

  fun toggleAchievementPin(id: String) {
    viewModelScope.launch {
      val msg = repository.toggleAchievementPin(id)
      _events.emit(msg)
    }
  }

  fun toggleBadgeEquip(id: String) {
    viewModelScope.launch {
      val msg = repository.toggleBadgeEquip(id)
      _events.emit(msg)
    }
  }

  fun craftOrUpgradeBadge(id: String) {
    viewModelScope.launch {
      val msg = repository.craftOrUpgradeBadge(id)
      _events.emit(msg)
    }
  }

  fun toggleArtworkLike(id: String) {
    viewModelScope.launch {
      repository.toggleArtworkLike(id)
    }
  }

  fun postLoreComment(message: String) {
    viewModelScope.launch {
      if (message.trim().isNotEmpty()) {
        repository.postLoreComment(message)
        _events.emit("Lore entry logged to Operator Feed")
      }
    }
  }

  fun updateProfileIdentity(
    handle: String,
    rankTitle: String,
    bio: String,
    statusMode: StatusMode,
    activeGame: String,
  ) {
    viewModelScope.launch {
      repository.updateProfileIdentity(handle, rankTitle, bio, statusMode, activeGame)
      _events.emit("Operator dossier updated")
    }
  }

  fun updateHudSettings(
    levelRingAccent: String,
    screentoneEnabled: Boolean,
    telemetryTickerEnabled: Boolean,
  ) {
    viewModelScope.launch {
      repository.updateHudSettings(levelRingAccent, screentoneEnabled, telemetryTickerEnabled)
    }
  }

  private data class CatalogBundle(
    val profile: OperatorProfileEntity?,
    val achievements: List<AchievementEntity>,
    val badges: List<BadgeEntity>,
    val artworks: List<ArtworkItemEntity>,
    val gameActivities: List<GameActivityEntity>,
  )

  class Factory(private val repository: ObsidianRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return ObsidianViewModel(repository) as T
    }
  }
}

package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.ObsidianDatabase
import com.example.data.ObsidianRepository
import com.example.ui.screens.AchievementsScreen
import com.example.ui.screens.BadgesScreen
import com.example.ui.screens.LoadoutScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.ObsidianOtakuTheme
import com.example.ui.theme.ObsidianTextStyles
import com.example.ui.theme.ObsidianTheme
import com.example.ui.viewmodel.ObsidianUiState
import com.example.ui.viewmodel.ObsidianViewModel

object ObsidianRoutes {
  const val PROFILE = "profile"
  const val ACHIEVEMENTS = "achievements"
  const val BADGES = "badges"
  const val LOADOUT = "loadout"
}

private data class ObsidianNavDestination(
  val route: String,
  val labelRes: Int,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val testTag: String,
)

private val navDestinations = listOf(
  ObsidianNavDestination(
    route = ObsidianRoutes.PROFILE,
    labelRes = R.string.nav_profile,
    selectedIcon = Icons.Filled.Person,
    unselectedIcon = Icons.Outlined.Person,
    testTag = "nav_profile",
  ),
  ObsidianNavDestination(
    route = ObsidianRoutes.ACHIEVEMENTS,
    labelRes = R.string.nav_achievements,
    selectedIcon = Icons.Filled.EmojiEvents,
    unselectedIcon = Icons.Outlined.EmojiEvents,
    testTag = "nav_achievements",
  ),
  ObsidianNavDestination(
    route = ObsidianRoutes.BADGES,
    labelRes = R.string.nav_badges,
    selectedIcon = Icons.Filled.Shield,
    unselectedIcon = Icons.Outlined.Shield,
    testTag = "nav_badges",
  ),
  ObsidianNavDestination(
    route = ObsidianRoutes.LOADOUT,
    labelRes = R.string.nav_loadout,
    selectedIcon = Icons.Filled.Tune,
    unselectedIcon = Icons.Outlined.Tune,
    testTag = "nav_loadout",
  ),
)

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
    )

    val database = ObsidianDatabase.getInstance(applicationContext)
    val repository = ObsidianRepository(database.obsidianDao())

    setContent {
      ObsidianOtakuTheme {
        val viewModel: ObsidianViewModel = viewModel(
          factory = ObsidianViewModel.Factory(repository),
        )
        ObsidianOtakuApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun ObsidianOtakuApp(viewModel: ObsidianViewModel) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val navController = rememberNavController()
  val snackbarHostState = remember { SnackbarHostState() }
  val colors = ObsidianTheme.colors

  LaunchedEffect(viewModel) {
    viewModel.events.collect { message ->
      snackbarHostState.showSnackbar(message)
    }
  }

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination

  BoxWithConstraints(
    modifier = Modifier
      .fillMaxSize()
      .background(colors.canvas),
  ) {
    val useNavRail = maxWidth >= 840.dp

    Scaffold(
      containerColor = colors.canvas,
      contentWindowInsets = WindowInsets(0, 0, 0, 0),
      topBar = {
        ObsidianTopTelemetryBar(
          state = uiState,
        )
      },
      bottomBar = {
        if (!useNavRail) {
          Column(modifier = Modifier.background(colors.trough)) {
            HorizontalDivider(color = colors.borderIdle, thickness = 1.dp)
            NavigationBar(
              containerColor = colors.trough,
              contentColor = colors.textPrimary,
              windowInsets = WindowInsets.navigationBars,
            ) {
              navDestinations.forEach { dest ->
                val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
                NavigationBarItem(
                  selected = selected,
                  onClick = { navigateToTab(navController, dest.route) },
                  icon = {
                    Icon(
                      imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                      contentDescription = stringResource(dest.labelRes),
                    )
                  },
                  label = {
                    Text(
                      text = stringResource(dest.labelRes),
                      style = ObsidianTextStyles.LabelBadge,
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.crimson,
                    selectedTextColor = colors.textPrimary,
                    indicatorColor = colors.crimsonMuted,
                    unselectedIconColor = colors.textSecondary,
                    unselectedTextColor = colors.textSecondary,
                  ),
                  modifier = Modifier.testTag(dest.testTag),
                )
              }
            }
          }
        }
      },
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) { data ->
          Snackbar(
            snackbarData = data,
            containerColor = colors.surfaceElevated,
            contentColor = colors.textPrimary,
            actionColor = colors.crimson,
            shape = RoundedCornerShape(4.dp),
          )
        }
      },
    ) { innerPadding ->
      Row(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .let { mod ->
            if (useNavRail) mod.windowInsetsPadding(WindowInsets.navigationBars) else mod
          },
      ) {
        if (useNavRail) {
          Row(modifier = Modifier.fillMaxHeight()) {
            NavigationRail(
              containerColor = colors.trough,
              contentColor = colors.textPrimary,
            ) {
              Spacer(modifier = Modifier.height(12.dp))
              navDestinations.forEach { dest ->
                val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
                NavigationRailItem(
                  selected = selected,
                  onClick = { navigateToTab(navController, dest.route) },
                  icon = {
                    Icon(
                      imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                      contentDescription = stringResource(dest.labelRes),
                    )
                  },
                  label = {
                    Text(
                      text = stringResource(dest.labelRes),
                      style = ObsidianTextStyles.LabelBadge,
                    )
                  },
                  colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = colors.crimson,
                    selectedTextColor = colors.textPrimary,
                    indicatorColor = colors.crimsonMuted,
                    unselectedIconColor = colors.textSecondary,
                    unselectedTextColor = colors.textSecondary,
                  ),
                  modifier = Modifier.testTag(dest.testTag),
                )
              }
            }
            Box(
              modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(colors.borderIdle),
            )
          }
        }

        when (val readyState = uiState) {
          ObsidianUiState.Loading -> {
            Box(
              modifier = Modifier.fillMaxSize(),
              contentAlignment = Alignment.Center,
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                  color = colors.crimson,
                  strokeWidth = 3.dp,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                  text = "SYNCING OPERATOR TERMINAL...",
                  style = ObsidianTextStyles.LabelCode,
                  color = colors.textSecondary,
                )
              }
            }
          }

          is ObsidianUiState.Ready -> {
            NavHost(
              navController = navController,
              startDestination = ObsidianRoutes.PROFILE,
              modifier = Modifier.fillMaxSize(),
            ) {
              composable(ObsidianRoutes.PROFILE) {
                ProfileScreen(
                  state = readyState,
                  onLikeArtwork = viewModel::toggleArtworkLike,
                  onPostComment = viewModel::postLoreComment,
                  onNavigateToBadges = { navigateToTab(navController, ObsidianRoutes.BADGES) },
                  onNavigateToAchievements = { navigateToTab(navController, ObsidianRoutes.ACHIEVEMENTS) },
                  onNavigateToLoadout = { navigateToTab(navController, ObsidianRoutes.LOADOUT) },
                )
              }
              composable(ObsidianRoutes.ACHIEVEMENTS) {
                AchievementsScreen(
                  state = readyState,
                  onSelectFilter = viewModel::setAchievementFilter,
                  onSearchChange = viewModel::setAchievementSearch,
                  onToggleUnlock = viewModel::toggleAchievementUnlock,
                  onTogglePin = viewModel::toggleAchievementPin,
                )
              }
              composable(ObsidianRoutes.BADGES) {
                BadgesScreen(
                  state = readyState,
                  onToggleEquip = viewModel::toggleBadgeEquip,
                  onCraftOrUpgrade = viewModel::craftOrUpgradeBadge,
                )
              }
              composable(ObsidianRoutes.LOADOUT) {
                LoadoutScreen(
                  state = readyState,
                  onSaveProfile = viewModel::updateProfileIdentity,
                  onUpdateHudSettings = viewModel::updateHudSettings,
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ObsidianTopTelemetryBar(
  state: ObsidianUiState,
) {
  val colors = ObsidianTheme.colors
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(colors.trough)
      .windowInsetsPadding(WindowInsets.statusBars),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .background(colors.crimson, RoundedCornerShape(2.dp)),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "OBSIDIAN // OTAKU",
          style = ObsidianTextStyles.LabelCode.copy(fontWeight = FontWeight.Bold),
          color = colors.textPrimary,
        )
      }

      if (state is ObsidianUiState.Ready) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
            modifier = Modifier
              .background(colors.surfaceMid, RoundedCornerShape(2.dp))
              .border(1.dp, colors.borderSubtle, RoundedCornerShape(2.dp))
              .padding(horizontal = 8.dp, vertical = 3.dp),
          ) {
            Text(
              text = "LVL ${state.profile.level}",
              style = ObsidianTextStyles.LabelBadge,
              color = colors.violetLight,
            )
          }
          Box(
            modifier = Modifier
              .background(colors.rareBg, RoundedCornerShape(2.dp))
              .border(1.dp, colors.cyan.copy(alpha = 0.65f), RoundedCornerShape(2.dp))
              .padding(horizontal = 8.dp, vertical = 3.dp),
          ) {
            Text(
              text = "${state.profile.shards} SHARDS",
              style = ObsidianTextStyles.LabelBadge,
              color = colors.cyan,
            )
          }
        }
      }
    }
    HorizontalDivider(color = colors.borderIdle, thickness = 1.dp)
  }
}

private fun navigateToTab(navController: NavHostController, route: String) {
  navController.navigate(route) {
    popUpTo(navController.graph.findStartDestination().id) {
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }
}

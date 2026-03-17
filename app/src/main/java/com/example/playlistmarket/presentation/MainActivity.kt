package com.example.playlistmarket.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.ui.theme.PlaylistMarketTheme
import com.example.playlistmarket.utils.Creator

sealed class Screen {
    object Main : Screen()
    object Search : Screen()
    object Settings : Screen()
    object Playlists : Screen()
    object Playlist : Screen()
    object Favorites : Screen()
    object NewPlaylist : Screen()
    object TrackDetails : Screen()
}

class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels {
        SearchViewModel.getViewModelFactory()
    }

    private val playlistsViewModel: PlaylistsViewModel by viewModels {
        PlaylistsViewModel.getViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Creator.init(applicationContext)

        enableEdgeToEdge()
        setContent {
            PlaylistMarketTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        searchViewModel = searchViewModel,
                        playlistsViewModel = playlistsViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel,
    playlistsViewModel: PlaylistsViewModel
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Main) }
    var selectedTrack by remember { mutableStateOf<Track?>(null) }
    var selectedPlaylistId by remember { mutableLongStateOf(0L) }

    when (currentScreen) {
        is Screen.Main -> {
            MainScreen(
                onSearchClick = { currentScreen = Screen.Search },
                onPlaylistsClick = { currentScreen = Screen.Playlists },
                onFavoritesClick = { currentScreen = Screen.Favorites },
                onSettingsClick = { currentScreen = Screen.Settings }
            )
        }

        is Screen.Search -> {
            SearchScreen(
                modifier = modifier,
                viewModel = searchViewModel,
                playlistsViewModel = playlistsViewModel,
                onBackClick = { currentScreen = Screen.Main },
                onTrackClick = { track ->
                    selectedTrack = track
                    currentScreen = Screen.TrackDetails
                }
            )
        }

        is Screen.Settings -> {
            SettingsScreen(
                onBackClick = { currentScreen = Screen.Main }
            )
        }

        is Screen.Playlists -> {
            PlaylistsScreen(
                viewModel = playlistsViewModel,
                onCreatePlaylistClick = { currentScreen = Screen.NewPlaylist },
                onPlaylistClick = { playlistId ->
                    selectedPlaylistId = playlistId
                    currentScreen = Screen.Playlist
                },
                onBackClick = { currentScreen = Screen.Main }
            )
        }

        is Screen.Playlist -> {
            val playlistViewModel = remember(selectedPlaylistId) {
                PlaylistViewModel(
                    repository = Creator.getPlaylistsRepository(),
                    playlistId = selectedPlaylistId
                )
            }

            PlaylistScreen(
                modifier = modifier,
                viewModel = playlistViewModel,
                onTrackClick = { track ->
                    selectedTrack = track
                    currentScreen = Screen.TrackDetails
                },
                onBackClick = { currentScreen = Screen.Playlists }
            )
        }

        is Screen.Favorites -> {
            FavoritesScreen(
                viewModel = playlistsViewModel,
                onBackClick = { currentScreen = Screen.Main }
            )
        }

        is Screen.NewPlaylist -> {
            NewPlaylistScreen(
                onBackClick = { currentScreen = Screen.Playlists },
                onSaveClick = { name, description ->
                    playlistsViewModel.addNewPlaylist(name, description)
                    currentScreen = Screen.Playlists
                }
            )
        }

        is Screen.TrackDetails -> {
            selectedTrack?.let { track ->
                TrackDetailsScreen(
                    track = track,
                    playlistsViewModel = playlistsViewModel,
                    onBackClick = { currentScreen = Screen.Search }
                )
            }
        }
    }
}
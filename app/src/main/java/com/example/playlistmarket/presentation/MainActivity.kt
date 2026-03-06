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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.playlistmarket.ui.theme.PlaylistMarketTheme
import com.example.playlistmarket.utils.Creator

sealed class Screen {
    object Main : Screen()
    object Search : Screen()
    object Settings : Screen()
}

class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels {
        SearchViewModel.getViewModelFactory()
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
                        searchViewModel = searchViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Main) }

    when (currentScreen) {
        is Screen.Main -> {
            MainScreen(
                onSearchClick = { currentScreen = Screen.Search },
                onSettingsClick = { currentScreen = Screen.Settings }
            )
        }
        is Screen.Search -> {
            SearchScreen(
                modifier = modifier,
                viewModel = searchViewModel,
                onBackClick = { currentScreen = Screen.Main }
            )
        }
        is Screen.Settings -> {
            SettingsScreen(
                onBackClick = { currentScreen = Screen.Main }
            )
        }
    }
}
package com.example.playlistmarket.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.playlistmarket.domain.models.Playlist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistsScreen(
    viewModel: PlaylistsViewModel,
    onCreatePlaylistClick: () -> Unit
) {
    val playlists by viewModel.playlists.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Playlists") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreatePlaylistClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add playlist"
                )
            }
        }
    ) { paddingValues ->

        if (playlists.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Плейлисты пока пустые",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(playlists) { playlist ->
                    PlaylistItem(playlist = playlist)
                }
            }
        }
    }
}

@Composable
fun PlaylistItem(
    playlist: Playlist
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = playlist.name,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = playlist.description,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Треков: ${playlist.tracks.size}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
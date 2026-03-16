package com.example.playlistmarket.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    track: Track,
    playlistsViewModel: PlaylistsViewModel,
    onBackClick: () -> Unit
) {
    val playlists by playlistsViewModel.playlists.collectAsState(initial = emptyList())
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(playlists) { playlist ->
                    PlaylistSheetItem(
                        playlist = playlist,
                        onClick = {
                            playlistsViewModel.insertTrackToPlaylist(track, playlist.id)
                            showSheet = false
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track details") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Название: ${track.trackName}")
            Text(text = "Исполнитель: ${track.artistName}")
            Text(text = "Альбом: ${track.collectionName}")
            Text(text = "Жанр: ${track.primaryGenreName}")
            Text(text = "Страна: ${track.country}")
            Text(text = "Длительность: ${track.trackTime}")

            Button(
                onClick = { playlistsViewModel.updateFavoriteStatus(track, true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить в избранное")
            }

            Button(
                onClick = { showSheet = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить в плейлист")
            }

            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад")
            }
        }
    }
}

@Composable
fun PlaylistSheetItem(
    playlist: Playlist,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = playlist.name,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = playlist.description,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Button(onClick = onClick) {
            Text("Выбрать")
        }
    }
}
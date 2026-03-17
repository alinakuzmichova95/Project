package com.example.playlistmarket.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.playlistmarket.domain.models.Track

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel,
    onTrackClick: (Track) -> Unit,
    onBackClick: () -> Unit
) {
    val playlist by viewModel.playlist.collectAsState()
    val currentPlaylist = playlist

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = onBackClick,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("← Назад")
        }

        if (currentPlaylist == null) {
            Text("Плейлист не найден")
            return
        }

        Text(
            text = currentPlaylist.name,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = currentPlaylist.description,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (currentPlaylist.tracks.isEmpty()) {
            Text("В этом плейлисте пока нет треков")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currentPlaylist.tracks) { track ->
                    PlaylistTrackItem(
                        track = track,
                        onClick = { onTrackClick(track) }
                    )
                }
            }
        }
    }
}

@Composable
fun PlaylistTrackItem(
    track: Track,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Text(text = track.trackName)
        Text(text = track.artistName)
        Text(text = track.trackTime)
    }
}
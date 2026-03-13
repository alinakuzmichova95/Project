package com.example.playlistmarket.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.ui.theme.PlaylistMarketTheme

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    playlistsViewModel: PlaylistsViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val screenState by viewModel.searchScreenState.collectAsStateWithLifecycle()
    var text by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Button(
            onClick = onBackClick,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text("← Назад")
        }

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                if (it.isEmpty()) {
                    viewModel.search("")
                }
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        viewModel.search(text)
                    },
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search_icon_description)
                )
            },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable {
                            text = ""
                            viewModel.search("")
                        },
                        imageVector = Icons.Filled.Clear,
                        contentDescription = stringResource(R.string.clear_icon_description)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.search_placeholder)) }
        )

        when (screenState) {
            is SearchState.Initial -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.initial_hint), fontSize = 18.sp)
                }
            }

            is SearchState.Searching -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SearchState.Success -> {
                val tracks = (screenState as SearchState.Success).foundList
                if (tracks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.empty_result), fontSize = 18.sp)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = tracks,
                            key = { track -> track.trackId }
                        ) { track ->
                            TrackListItem(
                                track = track,
                                onClick = { onTrackClick(track) },
                                onFavoriteClick = {
                                    playlistsViewModel.insertTrackToPlaylist(track, -1)
                                    playlistsViewModel.updateFavoriteStatus(track, true)
                                }
                            )
                            HorizontalDivider(thickness = 0.5.dp)
                        }
                    }
                }
            }

            is SearchState.Fail -> {
                val error = (screenState as SearchState.Fail).error
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.error_message, error),
                        color = Color.Red,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TrackListItem(
    track: Track,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_music),
            contentDescription = stringResource(R.string.track_icon_description, track.trackName),
            modifier = Modifier
                .size(48.dp)
                .padding(end = 8.dp)
        )

        Column(
            modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = track.trackName,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = track.artistName,
                maxLines = 1
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = track.trackTime)

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Добавить в избранное",
                    tint = Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackListItemPreview() {
    PlaylistMarketTheme {
        TrackListItem(
            track = Track(
                trackId = 1,
                trackName = "Test Track",
                artistName = "Test Artist",
                trackTimeMillis = 180000,
                artworkUrl100 = "",
                collectionName = "Test Collection",
                releaseDate = "2024",
                primaryGenreName = "Test Genre",
                country = "US"
            ),
            onClick = {},
            onFavoriteClick = {}
        )
    }
}
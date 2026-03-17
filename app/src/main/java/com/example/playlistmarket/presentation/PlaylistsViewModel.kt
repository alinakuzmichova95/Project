package com.example.playlistmarket.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.domain.repository.PlaylistsRepository
import com.example.playlistmarket.domain.repository.TracksRepository
import com.example.playlistmarket.utils.Creator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val tracksRepository: TracksRepository
) : ViewModel() {

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()

    val favoriteTracks: Flow<List<Track>> = tracksRepository.getFavoriteTracks()

    fun addNewPlaylist(name: String, description: String) {
        viewModelScope.launch {
            playlistsRepository.addNewPlaylist(name, description)
        }
    }

    fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        viewModelScope.launch {
            tracksRepository.insertTrackToPlaylist(track, playlistId)
        }
    }

    fun updateFavoriteStatus(track: Track, favorite: Boolean) {
        viewModelScope.launch {
            tracksRepository.updateFavoriteStatus(track, favorite)
        }
    }

    fun deletePlaylistById(id: Long) {
        viewModelScope.launch {
            tracksRepository.deleteTracksByPlaylistId(id)
            playlistsRepository.deletePlaylistById(id)
        }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlaylistsViewModel(
                        playlistsRepository = Creator.getPlaylistsRepository(),
                        tracksRepository = Creator.getTracksRepository()
                    ) as T
                }
            }
    }
}
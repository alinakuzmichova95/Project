package com.example.playlistmarket.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.domain.repository.PlaylistsRepository
import com.example.playlistmarket.domain.repository.TracksRepository
import com.example.playlistmarket.utils.Creator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PlaylistsViewModel : ViewModel() {

    private val playlistsRepository: PlaylistsRepository = Creator.getPlaylistsRepository()
    private val tracksRepository: TracksRepository = Creator.getTracksRepository()

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()

    val favoriteList: Flow<List<Track>> = tracksRepository.getFavoriteTracks()

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

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            tracksRepository.deleteTrackFromPlaylist(track)
        }
    }

    fun deletePlaylistById(id: Long) {
        viewModelScope.launch {
            tracksRepository.deleteTracksByPlaylistId(id)
            playlistsRepository.deletePlaylistById(id)
        }
    }

    suspend fun isExist(track: Track): Track? {
        return tracksRepository.getTrackByNameAndArtist(track).firstOrNull()
    }
}
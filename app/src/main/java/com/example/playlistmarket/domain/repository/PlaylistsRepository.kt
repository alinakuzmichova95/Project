package com.example.playlistmarket.domain.repository

import com.example.playlistmarket.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    fun getPlaylist(playlistId: Long): Flow<Playlist?>

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addNewPlaylist(name: String, description: String)

    suspend fun deletePlaylistById(id: Long)
}
package com.example.playlistmarket.domain.repository

import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    suspend fun searchTracks(expression: String): List<Track>

    fun getTrackByNameAndArtist(track: Track): Flow<Track?>

    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long)

    suspend fun deleteTrackFromPlaylist(track: Track)

    suspend fun deleteTracksByPlaylistId(id: Long)

    suspend fun updateFavoriteStatus(track: Track, favorite: Boolean)
}
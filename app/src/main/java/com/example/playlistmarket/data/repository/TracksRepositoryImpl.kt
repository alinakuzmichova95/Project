package com.example.playlistmarket.data.repository

import com.example.playlistmarket.data.storage.DatabaseMock
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.domain.repository.NetworkClient
import com.example.playlistmarket.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import android.util.Log

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: DatabaseMock
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.searchTracks(expression)

        Log.d("SEARCH_DEBUG", "expression = $expression")
        Log.d("SEARCH_DEBUG", "resultCount = ${response.resultCount}")
        Log.d("SEARCH_DEBUG", "results size = ${response.results.size}")
        Log.d("SEARCH_DEBUG", "results = ${response.results}")

        return response.results.map { dto ->
            Track(
                trackId = dto.trackId,
                trackName = dto.trackName,
                artistName = dto.artistName,
                trackTimeMillis = dto.trackTimeMillis,
                artworkUrl100 = dto.artworkUrl100,
                collectionName = dto.collectionName,
                releaseDate = dto.releaseDate,
                primaryGenreName = dto.primaryGenreName,
                country = dto.country
            )
        }
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return database.getTrackByNameAndArtist(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return database.getFavoriteTracks()
    }

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        database.insertTrackToPlaylist(track, playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track) {
        database.deleteTrackFromPlaylist(track)
    }

    override suspend fun deleteTracksByPlaylistId(id: Long) {
        database.deleteTracksByPlaylistId(id)
    }

    override suspend fun updateFavoriteStatus(track: Track, favorite: Boolean) {
        database.updateFavoriteStatus(track, favorite)
    }
}
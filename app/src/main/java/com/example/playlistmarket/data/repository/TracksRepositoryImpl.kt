package com.example.playlistmarket.data.repository

import com.example.playlistmarket.domain.repository.NetworkClient // Изменен импорт
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.domain.repository.TracksRepository
import kotlinx.coroutines.CancellationException

class TracksRepositoryImpl(
    private val networkClient: NetworkClient // Тип изменен на интерфейс
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        return try {
            val response = networkClient.searchTracks(expression)
            response.results.map { dto ->
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("TracksRepository", "Search error", e)
            emptyList()
        }
    }
}
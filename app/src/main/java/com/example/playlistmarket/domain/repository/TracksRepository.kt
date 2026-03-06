package com.example.playlistmarket.domain.repository

import com.example.playlistmarket.domain.models.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
}
// Интерфейс NetworkClient теперь в отдельном файле
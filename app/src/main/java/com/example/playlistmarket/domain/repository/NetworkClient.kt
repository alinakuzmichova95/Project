package com.example.playlistmarket.domain.repository

import com.example.playlistmarket.data.dto.TrackResponse

interface NetworkClient {
    suspend fun searchTracks(expression: String): TrackResponse
}
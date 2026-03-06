package com.example.playlistmarket.data.network

import com.example.playlistmarket.data.dto.TrackResponse
import com.example.playlistmarket.domain.repository.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("search?entity=song")
    suspend fun search(@Query("term") term: String): TrackResponse
}

class RetrofitNetworkClient(private val storage: Storage) : NetworkClient { // Добавлен интерфейс

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ITunesApi::class.java)

    override suspend fun searchTracks(expression: String): TrackResponse { // override
        storage.saveSearchHistory(expression)
        return api.search(expression)
    }

    companion object {
        private const val BASE_URL = "https://itunes.apple.com/"
    }
}
package com.example.playlistmarket.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmarket.data.network.RetrofitNetworkClient
import com.example.playlistmarket.data.network.Storage
import com.example.playlistmarket.data.repository.TracksRepositoryImpl
import com.example.playlistmarket.domain.repository.TracksRepository

object Creator {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    private fun getSharedPreferences(): SharedPreferences {
        return applicationContext.getSharedPreferences("playlist_prefs", Context.MODE_PRIVATE)
    }

    fun getTracksRepository(): TracksRepository {
        val storage = Storage(getSharedPreferences())
        val networkClient = RetrofitNetworkClient(storage)
        return TracksRepositoryImpl(networkClient)
    }


}
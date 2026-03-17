package com.example.playlistmarket.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmarket.data.network.RetrofitNetworkClient
import com.example.playlistmarket.data.network.Storage
import com.example.playlistmarket.data.repository.PlaylistsRepositoryImpl
import com.example.playlistmarket.data.repository.TracksRepositoryImpl
import com.example.playlistmarket.data.storage.DatabaseMock
import com.example.playlistmarket.domain.repository.PlaylistsRepository
import com.example.playlistmarket.domain.repository.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object Creator {

    private lateinit var applicationContext: Context

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val databaseMock: DatabaseMock by lazy {
        DatabaseMock(scope = appScope)
    }

    fun init(context: Context) {
        applicationContext = context
    }

    private fun getSharedPreferences(): SharedPreferences {
        return applicationContext.getSharedPreferences("playlist_prefs", Context.MODE_PRIVATE)
    }

    fun getTracksRepository(): TracksRepository {
        val storage = Storage(sharedPreferences = getSharedPreferences())
        val networkClient = RetrofitNetworkClient(storage)

        return TracksRepositoryImpl(
            networkClient = networkClient,
            database = databaseMock
        )
    }

    fun getPlaylistsRepository(): PlaylistsRepository {
        return PlaylistsRepositoryImpl(
            database = databaseMock
        )
    }


}
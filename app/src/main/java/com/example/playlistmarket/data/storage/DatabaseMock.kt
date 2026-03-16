package com.example.playlistmarket.data.storage

import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DatabaseMock(
    private val scope: CoroutineScope
) {

    private val playlists = mutableListOf<Playlist>()
    private val tracks = mutableListOf<Track>()

    fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        delay(500)

        val filteredPlaylists = playlists.map { playlist ->
            val playlistTracks = tracks.filter { it.playlistId == playlist.id }
            playlist.copy(tracks = playlistTracks)
        }

        emit(filteredPlaylists)
    }

    fun getFavoriteTracks(): Flow<List<Track>> = flow {
        delay(500)
        emit(tracks.filter { it.favorite })
    }

    suspend fun addNewPlaylist(name: String, description: String) {
        val newPlaylist = Playlist(
            id = if (playlists.isEmpty()) 1 else playlists.last().id + 1,
            name = name,
            description = description,
            tracks = emptyList()
        )

        playlists.add(newPlaylist)
    }

    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        val existingTrack = tracks.find {
            it.trackName == track.trackName && it.artistName == track.artistName
        }

        if (existingTrack != null) {
            existingTrack.playlistId = playlistId
        } else {
            tracks.add(track.copy(playlistId = playlistId))
        }
    }

    suspend fun updateFavoriteStatus(track: Track, favorite: Boolean) {
        val existingTrack = tracks.find {
            it.trackName == track.trackName && it.artistName == track.artistName
        }

        if (existingTrack != null) {
            existingTrack.favorite = favorite
        } else {
            tracks.add(track.copy(favorite = favorite))
        }
    }

    suspend fun deleteTrackFromPlaylist(track: Track) {
        tracks.find {
            it.trackName == track.trackName && it.artistName == track.artistName
        }?.playlistId = null
    }

    suspend fun deleteTracksByPlaylistId(id: Long) {
        tracks.forEach {
            if (it.playlistId == id) {
                it.playlistId = null
            }
        }
    }

    suspend fun deletePlaylistById(id: Long) {
        playlists.removeIf { it.id == id }
    }

    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow {
        emit(
            tracks.find {
                it.trackName == track.trackName &&
                        it.artistName == track.artistName
            }
        )
    }
}
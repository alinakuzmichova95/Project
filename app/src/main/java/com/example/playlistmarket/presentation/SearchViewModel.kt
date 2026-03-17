package com.example.playlistmarket.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.domain.repository.TracksRepository
import com.example.playlistmarket.utils.Creator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface SearchState {
    data object Initial : SearchState
    data object Searching : SearchState
    data class Success(val foundList: List<Track>) : SearchState
    data class Empty(val message: String) : SearchState
    data class Fail(val error: String) : SearchState
}

class SearchViewModel(
    private val tracksRepository: TracksRepository
) : ViewModel() {

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()

    private var lastSearchQuery: String = ""

    fun search(whatSearch: String) {
        if (whatSearch.isBlank()) {
            _searchScreenState.update { SearchState.Initial }
            return
        }

        lastSearchQuery = whatSearch

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchScreenState.update { SearchState.Searching }
                val list = tracksRepository.searchTracks(expression = whatSearch)

                if (list.isEmpty()) {
                    _searchScreenState.update {
                        SearchState.Empty("Ничего не найдено")
                    }
                } else {
                    _searchScreenState.update {
                        SearchState.Success(foundList = list)
                    }
                }
            } catch (e: Exception) {
                _searchScreenState.update {
                    SearchState.Fail(e.message ?: "Ошибка сервера")
                }
            }
        }
    }

    fun retrySearch() {
        if (lastSearchQuery.isNotBlank()) {
            search(lastSearchQuery)
        }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(Creator.getTracksRepository()) as T
                }
            }
    }
}
package com.example.playlistmarket.data.network

import android.content.SharedPreferences
import androidx.core.content.edit

class Storage(private val sharedPreferences: SharedPreferences) {

    fun saveSearchHistory(query: String) {
        val history = getSearchHistory().toMutableList()
        if (query.isNotBlank() && !history.contains(query)) {
            history.add(0, query)
            if (history.size > MAX_HISTORY_SIZE) {
                history.removeAt(history.lastIndex)
            }
            sharedPreferences.edit {
                putString(SEARCH_HISTORY_KEY, history.joinToString(HISTORY_SEPARATOR))
            }
        }
    }

    fun getSearchHistory(): List<String> {
        val saved = sharedPreferences.getString(SEARCH_HISTORY_KEY, "") ?: ""
        return if (saved.isNotBlank()) saved.split(HISTORY_SEPARATOR) else emptyList()
    }

    fun clearSearchHistory() {
        sharedPreferences.edit {
            remove(SEARCH_HISTORY_KEY)
        }
    }
    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
        private const val HISTORY_SEPARATOR = ";;;"
        private const val MAX_HISTORY_SIZE = 10
    }

}
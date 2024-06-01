package com.tastytrade.kurshin.data.prefs

import android.content.SharedPreferences
import com.tastytrade.kurshin.domain.WatchList
import com.tastytrade.kurshin.domain.irepository.IWatchListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.channels.awaitClose

class WatchListRepositoryPrefsImpl(private val sharedPreferences: SharedPreferences) : IWatchListRepository {

    companion object {
        private const val WATCH_LISTS_KEY = "watch_lists"
    }

    override fun getAllWatchLists(): Flow<List<WatchList>> {
        return sharedPreferences.getStringFlow(WATCH_LISTS_KEY)
            .map { list ->
                list.map { json ->
                    json.toWatchList()
                }
            }
    }

    override suspend fun addWatchlist(newList: WatchList) {
        val watchLists = sharedPreferences.getWatchLists().toMutableList()
        watchLists.add(newList)
        sharedPreferences.saveWatchLists(watchLists)
    }

    override suspend fun updateWatchlist(newList: WatchList) {
        val watchLists = sharedPreferences.getWatchLists().toMutableList()
        val index = watchLists.indexOfFirst { it.name == newList.name }
        if (index != -1) {
            watchLists[index] = newList
            sharedPreferences.saveWatchLists(watchLists)
        }
    }

    override suspend fun removeWatchlist(newList: WatchList) {
        val watchLists = sharedPreferences.getWatchLists().toMutableList()
        watchLists.removeAll { it.name == newList.name }
        sharedPreferences.saveWatchLists(watchLists)
    }

    private fun SharedPreferences.getWatchLists(): List<WatchList> {
        val jsonStringSet = getStringSet(WATCH_LISTS_KEY, emptySet())
        return jsonStringSet?.map { it.toWatchList() } ?: emptyList()
    }

    private fun SharedPreferences.saveWatchLists(watchLists: List<WatchList>) {
        val jsonStringSet = watchLists.map { it.toJsonString() }.toSet()
        edit().putStringSet(WATCH_LISTS_KEY, jsonStringSet).apply()
    }

    private fun String.toWatchList(): WatchList {
        val parts = split(":")
        return WatchList(parts[0], parts[1].toBoolean())
    }

    private fun WatchList.toJsonString(): String {
        return "$name:$isDefault"
    }

    private fun SharedPreferences.getStringFlow(key: String): Flow<Set<String>> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, updatedKey ->
            if (updatedKey == key) {
                trySend(getStringSet(key, emptySet()) ?: emptySet())
            }
        }

        trySend(getStringSet(key, emptySet()) ?: emptySet())

        registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
}

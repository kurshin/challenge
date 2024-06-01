package com.tastytrade.kurshin.domain.repository

import com.tastytrade.kurshin.domain.WatchList
import kotlinx.coroutines.flow.Flow

interface IWatchListRepository {
    fun getAllWatchLists(): Flow<List<WatchList>>
    suspend fun addWatchlist(newList: WatchList)
    suspend fun updateWatchlist(newList: WatchList)
    suspend fun removeWatchlist(newList: WatchList)
}
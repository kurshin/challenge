package com.tastytrade.kurshin.domain.irepository

import com.tastytrade.kurshin.domain.WatchList
import kotlinx.coroutines.flow.Flow

interface IWatchListRepository {
    fun getAllWatchLists(): Flow<List<WatchList>>
    suspend fun getAllWatchListsSync(): List<WatchList>
    suspend fun addWatchlist(newList: WatchList): Long
    suspend fun updateWatchlist(newList: WatchList)
    suspend fun removeWatchlist(newList: WatchList)
}
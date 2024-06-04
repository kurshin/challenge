package com.tastytrade.kurshin.data.persisted

import com.tastytrade.kurshin.data.persisted.dao.WatchListDao
import com.tastytrade.kurshin.data.persisted.entity.WatchListEntity
import com.tastytrade.kurshin.domain.irepository.IWatchListRepository
import com.tastytrade.kurshin.domain.WatchList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WatchListRepositoryDBImpl(private val watchlistDao: WatchListDao) : IWatchListRepository {

    override fun getAllWatchLists(): Flow<List<WatchList>> {
        return watchlistDao.getAllWatchLists().map { list -> list.map { WatchList(it.name, id = it.id, isDefault = it.isDefault) } }
    }

    override suspend fun getAllWatchListsSync(): List<WatchList> {
        return watchlistDao.getAllWatchListsSync().map { WatchList(it.name, id = it.id, isDefault = it.isDefault) }
    }

    override suspend fun addWatchlist(newList: WatchList): Long {
        return withContext(Dispatchers.IO) {
            watchlistDao.insertWatchList(WatchListEntity(newList.id, newList.name, newList.isDefault))
        }
    }

    override suspend fun updateWatchlist(newList: WatchList) {
        withContext(Dispatchers.IO) {
            watchlistDao.updateWatchList(WatchListEntity(newList.id, newList.name))
        }
    }

    override suspend fun removeWatchlist(newList: WatchList) {
        withContext(Dispatchers.IO) {
            watchlistDao.deleteWatchList(WatchListEntity(newList.id, newList.name))
        }
    }
}
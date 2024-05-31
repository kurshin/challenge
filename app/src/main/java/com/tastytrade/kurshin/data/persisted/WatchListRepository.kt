package com.tastytrade.kurshin.data.persisted

import com.tastytrade.kurshin.domain.WatchList

class WatchListRepository {

    val watchList = mutableListOf<WatchList>()

    fun addWatchlist(newList: WatchList) {
        watchList.add(newList)
    }
}
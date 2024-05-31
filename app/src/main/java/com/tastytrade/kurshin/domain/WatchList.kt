package com.tastytrade.kurshin.domain

data class WatchList(var name: String, val isDefault: Boolean = false)

val DEFAULT_WATCHLIST = WatchList("", true)

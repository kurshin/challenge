package com.tastytrade.kurshin.domain

data class WatchList(val name: String, val isDefault: Boolean = false)

val DEFAULT_WATCHLIST = WatchList("", true)

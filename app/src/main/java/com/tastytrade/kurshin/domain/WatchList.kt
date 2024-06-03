package com.tastytrade.kurshin.domain

data class WatchList(var name: String, val isDefault: Boolean = false, val id: Long = 0)

val DEFAULT_WATCHLIST = WatchList("", true)

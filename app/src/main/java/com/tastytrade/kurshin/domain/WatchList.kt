package com.tastytrade.kurshin.domain

data class WatchList(var name: String, val isDefault: Boolean = false, val id: Long = 0) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WatchList

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

val DEFAULT_WATCHLIST = WatchList("", true)

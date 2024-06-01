package com.tastytrade.kurshin.domain

data class Symbol(
    val name: String,
    var isChecked: Boolean = false,
    var watchList: WatchList = DEFAULT_WATCHLIST
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Symbol

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

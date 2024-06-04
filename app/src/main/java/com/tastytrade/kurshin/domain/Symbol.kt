package com.tastytrade.kurshin.domain

import java.io.Serializable

data class Symbol(
    var id: Long,
    val name: String,
    var isChecked: Boolean = false,
    var watchListId: Long = 0L,
    var lastPrice: Double = 0.0,
    var askPrice: Double = 0.0,
    var bidPrice: Double = 0.0
): Serializable {
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

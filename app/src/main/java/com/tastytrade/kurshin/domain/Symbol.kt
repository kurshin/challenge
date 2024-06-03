package com.tastytrade.kurshin.domain

import java.io.Serializable

data class Symbol(
    val name: String,
    var isChecked: Boolean = false,
    var watchListId: Long = 0L,
    var lastPrice: Double = 0.0,
    var askPrice: Double = 0.0,
    var bidPrice: Double = 0.0
): Serializable

package com.tastytrade.kurshin.domain

data class Quote(
    val symbol: String,
    val lastPrice: Double,
    val askPrice: Double,
    val bidPrice: Double,
    val date: String = "today"
)

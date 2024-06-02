package com.tastytrade.kurshin.domain

data class Chart(
    val symbol: String,
    val open: Double,
    val close: Double,
    val high: Double,
    val low: Double,
    val date: String
)

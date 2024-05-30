package com.tastytrade.kurshin.data.dto.chart

data class ChartDto(
    val change: Double,
    val changeOverTime: Double,
    val changePercent: Double,
    val close: Double,
    val date: String,
    val fClose: Double,
    val fHigh: Double,
    val fLow: Double,
    val fOpen: Double,
    val fVolume: Int,
    val high: Double,
    val id: String,
    val key: String,
    val label: String,
    val low: Double,
    val marketChangeOverTime: Double,
    val priceDate: String,
    val subkey: String,
    val symbol: String,
    val uClose: Double,
    val uHigh: Double,
    val uLow: Double,
    val uOpen: Double,
    val uVolume: Int,
    val updated: Double,
    val volume: Int
)
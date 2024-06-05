package com.tastytrade.kurshin.data.dto.symbol

import com.google.gson.annotations.SerializedName

data class Item(
    val autocomplete: Int = 0,
    val description: String = "",
    val etf: Boolean = false,
    @SerializedName("instrument-type")
    val instrumentType: String = "",
    @SerializedName("listed-market")
    val listedMarket: String = "",
    val options: Boolean = false,
    @SerializedName("price-increments")
    val priceIncrements: String = "",
    val symbol: String = "",
    @SerializedName("trading-hours")
    val tradingHours: String = ""
)
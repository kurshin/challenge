package com.tastytrade.kurshin.data.dto.symbol

import com.google.gson.annotations.SerializedName

data class Item(
    val autocomplete: Int,
    val description: String,
    val etf: Boolean,
    @SerializedName("instrument-type")
    val instrumentType: String,
    @SerializedName("listed-market")
    val listedMarket: String,
    val options: Boolean,
    @SerializedName("price-increments")
    val priceIncrements: String,
    val symbol: String,
    @SerializedName("trading-hours")
    val tradingHours: String
)
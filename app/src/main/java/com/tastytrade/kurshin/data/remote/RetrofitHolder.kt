package com.tastytrade.kurshin.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHolder {

    private const val BASE_URL = "https://api.iex.cloud/v1/"
    internal val stockService: StockService

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        stockService = retrofit.create(StockService::class.java)
    }
}
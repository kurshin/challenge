package com.tastytrade.kurshin.data.remote.stock

import com.tastytrade.kurshin.BuildConfig
import com.tastytrade.kurshin.data.dto.chart.ChartResponse
import com.tastytrade.kurshin.data.dto.quote.QuoteResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StockService {

    @GET("stock/{symbol}/batch")
    suspend fun fetchQuote(
        @Path("symbol") symbol: String,
        @Query("types") types: String = "quote",
        @Query("token") token: String = BuildConfig.API_KEY
    ): QuoteResponse

    @GET("stock/{symbol}/chart/1m")
    suspend fun fetchChart(
        @Path("symbol") symbol: String,
        @Query("token") token: String = BuildConfig.API_KEY
    ): ChartResponse
}
package com.tastytrade.kurshin.data.remote.symbol

import com.tastytrade.kurshin.data.dto.symbol.SymbolResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SymbolService {
    @GET("symbols/search/{symbol}")
    suspend fun fetchSymbols(
        @Path("symbol") symbol: String
    ): SymbolResponse
}
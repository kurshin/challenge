package com.tastytrade.kurshin.domain.irepository

import com.tastytrade.kurshin.domain.Chart
import com.tastytrade.kurshin.domain.Symbol

interface IStockRepository {
    suspend fun fetchQuote(symbol: String): Symbol
    suspend fun fetchChart(symbol: String): List<Chart>
}
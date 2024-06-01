package com.tastytrade.kurshin.domain.repository

import com.tastytrade.kurshin.domain.Chart
import com.tastytrade.kurshin.domain.Quote

interface IStockRepository {
    suspend fun fetchQuote(symbol: String): Quote
    suspend fun fetchChart(symbol: String): List<Chart>
}
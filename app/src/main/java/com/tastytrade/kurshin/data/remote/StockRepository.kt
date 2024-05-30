package com.tastytrade.kurshin.data.remote

import com.tastytrade.kurshin.data.dto.chart.ChartDto
import com.tastytrade.kurshin.domain.Chart
import com.tastytrade.kurshin.domain.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StockRepository(private val stockService: StockService) {

    suspend fun fetchQuote(symbol: String): Quote {
        return withContext(Dispatchers.IO) {
            val quoteDto = stockService.fetchQuote(symbol).quote
            Quote(
                quoteDto.symbol,
                quoteDto.latestPrice,
                quoteDto.iexAskPrice,
                quoteDto.iexBidPrice,
                quoteDto.latestTime
            )
        }
    }

    suspend fun fetchChart(symbol: String): List<Chart> {
        return withContext(Dispatchers.IO) {
            stockService.fetchChart(symbol).map {
                Chart(it.symbol, it.close, it.date)
            }
        }
    }
}
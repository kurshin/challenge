package com.tastytrade.kurshin.data.remote.stock

import com.tastytrade.kurshin.domain.Chart
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.irepository.IStockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StockRepositoryImpl(private val stockService: StockService) : IStockRepository {

    override suspend fun fetchQuote(symbol: String): Symbol {
        return withContext(Dispatchers.IO) {
            val quoteDto = stockService.fetchQuote(symbol).quote
            Symbol(
                0,
                quoteDto.symbol,
                false,
                0,
                quoteDto.latestPrice,
                quoteDto.iexAskPrice,
                quoteDto.iexBidPrice
            )
        }
    }

    override suspend fun fetchChart(symbol: String): List<Chart> {
        return withContext(Dispatchers.IO) {
            stockService.fetchChart(symbol).map {
                Chart(it.symbol, it.fOpen, it.fClose, it.fHigh, it.fLow, it.date)
            }
        }
    }
}
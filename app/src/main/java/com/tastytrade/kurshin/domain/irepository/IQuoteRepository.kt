package com.tastytrade.kurshin.domain.irepository

import com.tastytrade.kurshin.domain.Symbol
import kotlinx.coroutines.flow.Flow

interface IQuoteRepository {

    fun getAllQuotes(): Flow<List<Symbol>>
    suspend fun getAllQuotesSync(): List<Symbol>
    suspend fun getQuotesForWatchlist(watchlistId: Long): List<Symbol>
    suspend fun insertQuote(quote: Symbol)
    suspend fun deleteQuote(quote: Symbol)
}
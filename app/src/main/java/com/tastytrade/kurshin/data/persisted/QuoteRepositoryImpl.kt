package com.tastytrade.kurshin.data.persisted

import com.tastytrade.kurshin.data.persisted.dao.QuoteDao
import com.tastytrade.kurshin.data.persisted.entity.QuoteEntity
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.irepository.IQuoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class QuoteRepositoryImpl(private val quoteDao: QuoteDao) : IQuoteRepository {

    override fun getAllQuotes(): Flow<List<Symbol>> {
        return quoteDao.getAllQuotes().map {
            it.map { quote ->
                Symbol(
                    id = quote.id,
                    name = quote.name,
                    lastPrice = quote.lastPrice,
                    askPrice = quote.askPrice,
                    bidPrice = quote.bidPrice,
                    watchListId = quote.watchlistId
                )
            }
        }
    }

    override suspend fun getAllQuotesSync(): List<Symbol> {
        return quoteDao.getAllQuotesSync().map { quote ->
            Symbol(
                id = quote.id,
                name = quote.name,
                lastPrice = quote.lastPrice,
                askPrice = quote.askPrice,
                bidPrice = quote.bidPrice,
                watchListId = quote.watchlistId
            )
        }
    }

    override suspend fun getQuotesForWatchlist(watchlistId: Long): List<Symbol> {
        return withContext(Dispatchers.IO) {
            quoteDao.getQuotesForWatchlist(watchlistId).map {
                Symbol(
                    id = it.id,
                    name = it.name,
                    lastPrice = it.lastPrice,
                    askPrice = it.askPrice,
                    bidPrice = it.bidPrice,
                    watchListId = it.watchlistId
                )
            }
        }
    }

    override suspend fun insertQuote(symbol: Symbol): Long {
        return withContext(Dispatchers.IO) {
            quoteDao.insertQuote(
                QuoteEntity(
                    symbol.id,
                    symbol.name,
                    symbol.watchListId,
                    symbol.lastPrice,
                    symbol.askPrice,
                    symbol.bidPrice
                )
            )
        }
    }

    override suspend fun deleteQuote(symbol: Symbol) {
        withContext(Dispatchers.IO) {
            quoteDao.deleteQuote(
                QuoteEntity(
                    symbol.id,
                    symbol.name,
                    symbol.watchListId,
                    symbol.lastPrice,
                    symbol.askPrice,
                    symbol.bidPrice
                )
            )
        }
    }
}
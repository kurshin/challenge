package com.tastytrade.kurshin.data.persisted

import com.tastytrade.kurshin.data.persisted.dao.QuoteDao
import com.tastytrade.kurshin.data.persisted.entity.QuoteEntity
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.irepository.IQuoteRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class QuoteRepositoryImplTest {

    private lateinit var quoteDao: QuoteDao
    private lateinit var quoteRepository: IQuoteRepository

    @Before
    fun setup() {
        quoteDao = mockk()
        quoteRepository = QuoteRepositoryImpl(quoteDao)
    }

    @Test
    fun `getAllQuotes returns list of symbols`() = runTest {
        // Arrange
        val quoteEntities = listOf(
            QuoteEntity(1L, "AAPL", 1L, 150.0, 151.0, 149.0),
            QuoteEntity(2L, "GOOG", 1L, 2500.0, 2501.0, 2499.0)
        )
        val expectedSymbols = listOf(
            Symbol(1L, "AAPL", false, 1L, 150.0, 151.0, 149.0),
            Symbol(2L, "GOOG", false, 1L, 2500.0, 2501.0, 2499.0)
        )

        coEvery { quoteDao.getAllQuotes() } returns flowOf(quoteEntities)

        // Act
        val result = quoteRepository.getAllQuotes()

        // Assert
        result.collect { symbols ->
            assertEquals(expectedSymbols, symbols)
        }
        coVerify { quoteDao.getAllQuotes() }
    }

    @Test
    fun `getAllQuotesSync returns list of symbols`() = runTest {
        // Arrange
        val quoteEntities = listOf(
            QuoteEntity(1L, "AAPL", 1L, 150.0, 151.0, 149.0),
            QuoteEntity(2L, "GOOG", 1L, 2500.0, 2501.0, 2499.0)
        )
        val expectedSymbols = listOf(
            Symbol(1L, "AAPL", false, 1L, 150.0, 151.0, 149.0),
            Symbol(2L, "GOOG", false, 1L, 2500.0, 2501.0, 2499.0)
        )

        coEvery { quoteDao.getAllQuotesSync() } returns quoteEntities

        // Act
        val result = quoteRepository.getAllQuotesSync()

        // Assert
        assertEquals(expectedSymbols, result)
        coVerify { quoteDao.getAllQuotesSync() }
    }

    @Test
    fun `getQuotesForWatchlist returns list of symbols`() = runTest {
        // Arrange
        val watchlistId = 1L
        val quoteEntities = listOf(
            QuoteEntity(1L, "AAPL", watchlistId, 150.0, 151.0, 149.0),
            QuoteEntity(2L, "GOOG", watchlistId, 2500.0, 2501.0, 2499.0)
        )
        val expectedSymbols = listOf(
            Symbol(1L, "AAPL", false, watchlistId, 150.0, 151.0, 149.0),
            Symbol(2L, "GOOG", false, watchlistId, 2500.0, 2501.0, 2499.0)
        )

        coEvery { quoteDao.getQuotesForWatchlist(watchlistId) } returns quoteEntities

        // Act
        val result = withContext(Dispatchers.IO) { quoteRepository.getQuotesForWatchlist(watchlistId) }

        // Assert
        assertEquals(expectedSymbols, result)
        coVerify { quoteDao.getQuotesForWatchlist(watchlistId) }
    }

    @Test
    fun `insertQuote inserts symbol`() = runTest {
        // Arrange
        val symbol = Symbol(1L, "AAPL", true, 1L, 150.0, 151.0, 149.0)
        val quoteEntity = QuoteEntity(1L, "AAPL", 1L, 150.0, 151.0, 149.0)
        coEvery { quoteDao.insertQuote(quoteEntity) } returns 1L

        // Act
        val result = withContext(Dispatchers.IO) { quoteRepository.insertQuote(symbol) }

        // Assert
        assertEquals(1L, result)
        coVerify { quoteDao.insertQuote(quoteEntity) }
    }

    @Test
    fun `deleteQuote deletes symbol`() = runTest {
        // Arrange
        val symbol = Symbol(1L, "AAPL", true, 1L, 150.0, 151.0, 149.0)
        val quoteEntity = QuoteEntity(1L, "AAPL", 1L, 150.0, 151.0, 149.0)
        coEvery { quoteDao.deleteQuote(quoteEntity) } just Runs

        // Act
        withContext(Dispatchers.IO) { quoteRepository.deleteQuote(symbol) }

        // Assert
        coVerify { quoteDao.deleteQuote(quoteEntity) }
    }
}

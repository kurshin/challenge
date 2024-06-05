package com.tastytrade.kurshin.data.remote.stock

import com.tastytrade.kurshin.domain.irepository.IStockRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class StockSimulationRepositoryImplTest {

    private lateinit var stockRepository: IStockRepository

    @Before
    fun setup() {
        stockRepository = StockSimulationRepositoryImpl()
    }

    @Test
    fun `fetchQuote returns symbol with random prices`() = runTest {
        // Arrange
        val symbol = "AAPL"

        // Act
        val result = stockRepository.fetchQuote(symbol)

        // Assert
        assertEquals(symbol, result.name)
        assertTrue(result.lastPrice in 0.0..100.0)
        assertTrue(result.askPrice in 0.0..100.0)
        assertTrue(result.bidPrice in 0.0..100.0)
    }

    @Test
    fun `fetchChart returns list of chart data`() = runTest {
        // Arrange
        val symbol = "AAPL"
        val periodDays = 30

        // Act
        val result = stockRepository.fetchChart(symbol)

        // Assert
        assertEquals(periodDays, result.size)
        assertTrue(result.all { it.symbol == symbol })
        assertTrue(result.all { it.open in 0.0..100.0 })
        assertTrue(result.all { it.close in 0.0..100.0 })
        assertTrue(result.all { it.high in 0.0..100.0 })
        assertTrue(result.all { it.low in 0.0..100.0 })
        assertTrue(result.map { it.date }.distinct().size == periodDays)
    }

    @Test
    fun `randomDoubleInRange returns positive value`() {
        // Arrange
        val repo = StockSimulationRepositoryImpl()

        // Act
        val result = repo.randomDoubleInRange()

        // Assert
        assertTrue(result in 0.0..100.0)
    }

    @Test
    fun `generateQuotes returns list of chart data`() {
        // Arrange
        val repo = StockSimulationRepositoryImpl()
        val symbol = "AAPL"
        val periodDays = 30

        // Act
        val result = repo.generateQuotes(symbol)

        // Assert
        assertEquals(periodDays, result.size)
        assertTrue(result.all { it.symbol == symbol })
        assertTrue(result.all { it.open in 0.0..100.0 })
        assertTrue(result.all { it.close in 0.0..100.0 })
        assertTrue(result.all { it.high in 0.0..100.0 })
        assertTrue(result.all { it.low in 0.0..100.0 })
        assertTrue(result.map { it.date }.distinct().size == periodDays)
    }
}

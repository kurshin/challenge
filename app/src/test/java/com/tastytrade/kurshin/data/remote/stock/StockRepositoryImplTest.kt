package com.tastytrade.kurshin.data.remote.stock

import com.tastytrade.kurshin.data.dto.chart.ChartDto
import com.tastytrade.kurshin.data.dto.chart.ChartResponse
import com.tastytrade.kurshin.data.dto.quote.QuoteDto
import com.tastytrade.kurshin.data.dto.quote.QuoteResponse
import com.tastytrade.kurshin.domain.Chart
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.irepository.IStockRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class StockRepositoryImplTest {

    private lateinit var stockService: StockService
    private lateinit var stockRepository: IStockRepository

    @Before
    fun setup() {
        stockService = mockk()
        stockRepository = StockRepositoryImpl(stockService)
    }

    @Test
    fun `fetchQuote returns symbol`() = runTest {
        // Arrange
        val symbol = "AAPL"
        val quoteDto = QuoteDto(symbol = symbol, latestPrice = 150.0, iexAskPrice = 151.0, iexBidPrice = 149.0)
        val expectedSymbol = Symbol(0, "AAPL", false, 0, 150.0, 151.0, 149.0)
        coEvery { stockService.fetchQuote(symbol) } returns QuoteResponse(quoteDto)

        // Act
        val result = withContext(Dispatchers.IO) { stockRepository.fetchQuote(symbol) }

        // Assert
        assertEquals(expectedSymbol, result)
        coVerify { stockService.fetchQuote(symbol) }
    }

    @Test
    fun `fetchChart returns list of chart data`() = runTest {
        // Arrange
        val symbol = "AAPL"
        val chartDto = listOf(
            ChartDto(symbol = "AAPL", fOpen = 150.0, fClose = 151.0, fHigh = 152.0, fLow = 149.0, date = "2024-01-01"),
            ChartDto(symbol = "AAPL", fOpen = 151.0, fClose = 152.0, fHigh = 153.0, fLow = 150.0, date = "2024-01-02")
        )
        val expectedChart = listOf(
            Chart("AAPL", 150.0, 151.0, 152.0, 149.0, "2024-01-01"),
            Chart("AAPL", 151.0, 152.0, 153.0, 150.0, "2024-01-02")
        )
        coEvery { stockService.fetchChart(symbol) } returns ChartResponse().apply { addAll(chartDto) }

        // Act
        val result = withContext(Dispatchers.IO) { stockRepository.fetchChart(symbol) }

        // Assert
        assertEquals(expectedChart, result)
        coVerify { stockService.fetchChart(symbol) }
    }
}

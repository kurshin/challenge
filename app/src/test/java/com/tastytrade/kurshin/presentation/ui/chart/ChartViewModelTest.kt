package com.tastytrade.kurshin.presentation.ui.chart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tastytrade.kurshin.domain.Chart
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.irepository.IStockRepository
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class ChartViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var stockRepo: IStockRepository
    private lateinit var chartViewModel: ChartViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        stockRepo = mockk()
        chartViewModel = ChartViewModel(stockRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getQuoteDataRepeatedly updates quote LiveData`() = runTest {
        // Arrange
        val symbol = "AAPL"
        val expectedQuote = Symbol(0, "AAPL", true, 1L)
        coEvery { stockRepo.fetchQuote(symbol) } returns expectedQuote

        // Act
        chartViewModel.fetchQuoteData(symbol)

        // Assert
        advanceUntilIdle()
        assertEquals(expectedQuote, chartViewModel.quote.value)
        coVerify { stockRepo.fetchQuote(symbol) }
    }

    @Test
    fun `getChartData updates chart LiveData`() = runTest {
        // Arrange
        val symbol = "AAPL"
        val expectedChart = listOf(Chart("AAPL", 150.0, 0.0, 0.0, 0.0, ""))
        coEvery { stockRepo.fetchChart(symbol) } returns expectedChart

        // Act
        chartViewModel.getChartData(symbol)

        // Assert
        advanceUntilIdle()
        assertEquals(expectedChart, chartViewModel.chart.value)
        coVerify { stockRepo.fetchChart(symbol) }
    }

    @Test
    fun `errorHandler posts error message`() = runTest {
        // Arrange
        val exception = RuntimeException("Test exception")
        var handlerCalled = false

        chartViewModel.errorHandler = CoroutineExceptionHandler { _, throwable ->
            handlerCalled = true
            chartViewModel.error.postValue(throwable.message)
        }

        coEvery { stockRepo.fetchChart(any()) } throws exception

        // Act
        chartViewModel.getChartData("AAPL")

        // Assert
        advanceUntilIdle()
        assertEquals("Test exception", chartViewModel.error.value)
        assertTrue(handlerCalled)
    }
}

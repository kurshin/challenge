package com.tastytrade.kurshin.presentation.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.WatchList
import com.tastytrade.kurshin.domain.irepository.IQuoteRepository
import com.tastytrade.kurshin.domain.irepository.IStockRepository
import com.tastytrade.kurshin.domain.irepository.ISymbolRepository
import com.tastytrade.kurshin.domain.irepository.IWatchListRepository
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var stockRepo: IStockRepository
    private lateinit var watchListRepo: IWatchListRepository
    private lateinit var symbolRepo: ISymbolRepository
    private lateinit var quoteRepo: IQuoteRepository
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        stockRepo = mockk()
        watchListRepo = mockk()
        symbolRepo = mockk()
        quoteRepo = mockk()

        mainViewModel = MainViewModel(stockRepo, watchListRepo, symbolRepo, quoteRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetch watchlist data on init`() = runTest {
        // Arrange
        val expectedWatchlist = listOf(WatchList("Test Watchlist"))
        coEvery { watchListRepo.getAllWatchLists() } returns flowOf(expectedWatchlist)
        coEvery { watchListRepo.getAllWatchListsSync() } returns expectedWatchlist
        coEvery { watchListRepo.addWatchlist(any()) } returns 1L

        // Act
        mainViewModel.fetchWatchlistData()

        // Assert
        advanceUntilIdle()
        assertEquals(expectedWatchlist, mainViewModel.watchList)
        coVerify { watchListRepo.getAllWatchLists() }
    }

    @Test
    fun `addWatchList updates currentWatchlist`() = runTest {
        // Arrange
        val watchList = WatchList("Test Watchlist")
        coEvery { watchListRepo.addWatchlist(watchList) } returns 1L

        // Act
        mainViewModel.addWatchList(watchList)

        // Assert
        advanceUntilIdle()
        assertEquals(watchList.apply { id = 1L }, mainViewModel.currentWatchlist.value)
        coVerify { watchListRepo.addWatchlist(watchList) }
    }

    @Test
    fun `updateWatchList updates currentWatchlist`() = runTest {
        // Arrange
        val watchList = WatchList("Test Watchlist", id = 1L)
        coEvery { watchListRepo.updateWatchlist(watchList) } just Runs

        // Act
        mainViewModel.updateWatchList(watchList)

        // Assert
        advanceUntilIdle()
        assertEquals(watchList, mainViewModel.currentWatchlist.value)
        coVerify { watchListRepo.updateWatchlist(watchList) }
    }

    @Test
    fun `refreshWatchList refreshes currentWatchlist`() = runTest {
        // Arrange
        val watchList = WatchList("Test Watchlist", id = 1L)
        mainViewModel.currentWatchlist.value = watchList

        // Act
        mainViewModel.refreshWatchList()

        // Assert
        advanceUntilIdle()
        assertEquals(watchList, mainViewModel.currentWatchlist.value)
    }

    @Test
    fun `deleteWatchList updates currentWatchlist`() = runTest {
        // Arrange
        val watchList = WatchList("Test Watchlist", id = 1L)
        val allWatchLists = listOf(watchList)
        coEvery { watchListRepo.removeWatchlist(watchList) } just Runs
        coEvery { watchListRepo.getAllWatchListsSync() } returns emptyList()

        // Act
        mainViewModel.currentWatchlist.value = watchList
        mainViewModel.deleteWatchList(watchList)

        // Assert
        advanceUntilIdle()
        assertNull(mainViewModel.currentWatchlist.value)
        coVerify { watchListRepo.removeWatchlist(watchList) }
    }

    @Test
    fun `removeSymbol deletes symbol`() = runTest {
        // Arrange
        val symbol = Symbol(0, "AAPL", true, 1L)
        coEvery { quoteRepo.deleteQuote(symbol) } just Runs

        // Act
        mainViewModel.removeSymbol(symbol)

        // Assert
        advanceUntilIdle()
        coVerify { quoteRepo.deleteQuote(symbol) }
    }

    @Test
    fun `addSymbol inserts symbol`() = runTest {
        // Arrange
        val symbol = Symbol(0, "AAPL", true, 1L)
        coEvery { quoteRepo.insertQuote(symbol) } returns 1L

        // Act
        mainViewModel.addSymbol(symbol)

        // Assert
        advanceUntilIdle()
        coVerify { quoteRepo.insertQuote(symbol) }
    }

    @Test
    fun `fetchQuoteData returns data`() = runTest {
        // Arrange
        val symbol = "AAPL"
        val expectedQuote = Symbol(0, "AAPL", true, 1L)
        coEvery { stockRepo.fetchQuote(symbol) } returns expectedQuote

        // Act
        val result = mainViewModel.fetchQuoteData(symbol)

        // Assert
        assertEquals(expectedQuote, result)
        coVerify { stockRepo.fetchQuote(symbol) }
    }

    @Test
    fun `getSymbolsForWatchlist returns symbols`() = runTest {
        // Arrange
        val watchList = WatchList("Test Watchlist", id = 1L)
        val expectedSymbols = listOf(Symbol(0, "AAPL", true, 1L))
        coEvery { quoteRepo.getQuotesForWatchlist(watchList.id) } returns expectedSymbols
        coEvery { quoteRepo.getAllQuotesSync() } returns expectedSymbols

        // Act
        val result = mainViewModel.getSymbolsForWatchlist(watchList)

        // Assert
        advanceUntilIdle()
        assertEquals(expectedSymbols, result.value)
    }

    @Test
    fun `fulfillDBInitialData adds default watchlist and symbols when empty`() = runTest {
        // Arrange
        val emptyWatchLists = emptyList<WatchList>()
        val defaultWatchList = WatchList("My first list")
        val symbols = listOf("AAPL", "GOOG", "MSFT")

        coEvery { watchListRepo.getAllWatchListsSync() } returns emptyWatchLists
        coEvery { watchListRepo.addWatchlist(any()) } returns 1L
        coEvery { quoteRepo.insertQuote(any()) } returnsMany listOf(1L, 2L, 3L)

        // Act
        mainViewModel.fulfillDBInitialData()

        // Assert
        advanceUntilIdle()
        coVerify { watchListRepo.addWatchlist(match { it.name == "My first list" }) }
        coVerify { quoteRepo.insertQuote(match { it.name == "AAPL" && it.watchListId == 1L }) }
        coVerify { quoteRepo.insertQuote(match { it.name == "GOOG" && it.watchListId == 1L }) }
        coVerify { quoteRepo.insertQuote(match { it.name == "MSFT" && it.watchListId == 1L }) }
        assertEquals(defaultWatchList.apply { id = 1L }, mainViewModel.currentWatchlist.value)
    }

    @Test
    fun `fulfillDBInitialData does not add default watchlist and symbols when not empty`() = runTest {
        // Arrange
        val existingWatchLists = listOf(WatchList("Existing Watchlist", id = 1L))

        coEvery { watchListRepo.getAllWatchListsSync() } returns existingWatchLists

        // Act
        mainViewModel.fulfillDBInitialData()

        // Assert
        advanceUntilIdle()
        coVerify(exactly = 0) { watchListRepo.addWatchlist(any()) }
        coVerify(exactly = 0) { quoteRepo.insertQuote(any()) }
        assertEquals(existingWatchLists.firstOrNull(), mainViewModel.currentWatchlist.value)
    }

    @Test
    fun `searchSymbol updates symbolsForAutofill with fetched symbols`() = runTest {
        // Arrange
        val symbolQuery = "AAPL"
        val expectedSymbols = listOf(Symbol(0, "AAPL", true, 1L))
        coEvery { symbolRepo.fetchSymbols(symbolQuery) } returns expectedSymbols

        // Act
        mainViewModel.searchSymbol(symbolQuery)

        // Assert
        advanceUntilIdle()
        assertEquals(expectedSymbols, mainViewModel.symbolsForAutofill.value)
        coVerify { symbolRepo.fetchSymbols(symbolQuery) }
    }

    @Test
    fun `networkErrorHandler posts error on exception`() = runTest {
        // Arrange
        val exception = RuntimeException("Test exception")
        var handlerCalled = false

        mainViewModel.networkErrorHandler = CoroutineExceptionHandler { _, throwable ->
            handlerCalled = true
            mainViewModel.error.postValue(throwable)
        }

        coEvery { symbolRepo.fetchSymbols(any()) } throws exception

        // Act
        mainViewModel.searchSymbol("AAPL")

        // Assert
        advanceUntilIdle()
        assertEquals(exception, mainViewModel.error.value)
        assertTrue(handlerCalled)
    }
}

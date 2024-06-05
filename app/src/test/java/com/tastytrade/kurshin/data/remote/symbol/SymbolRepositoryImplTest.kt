package com.tastytrade.kurshin.data.remote.symbol

import com.tastytrade.kurshin.data.dto.symbol.Data
import com.tastytrade.kurshin.data.dto.symbol.Item
import com.tastytrade.kurshin.data.dto.symbol.SymbolResponse
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.irepository.ISymbolRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SymbolRepositoryImplTest {

    private lateinit var symbolService: SymbolService
    private lateinit var symbolRepository: ISymbolRepository

    @Before
    fun setup() {
        symbolService = mockk()
        symbolRepository = SymbolRepositoryImpl(symbolService)
    }

    @Test
    fun `fetchSymbols returns list of symbols`() = runTest {
        // Arrange
        val symbol = "AAPL"
        val symbolItems = listOf(Item(symbol = "AAPL"), Item(symbol = "GOOG"))
        val symbolData = Data(symbolItems)
        coEvery { symbolService.fetchSymbols(symbol) } returns SymbolResponse(symbolData)

        val expectedSymbols = listOf(Symbol(0, "AAPL"), Symbol(0, "GOOG"))

        // Act
        val result = withContext(Dispatchers.IO) { symbolRepository.fetchSymbols(symbol) }

        // Assert
        assertEquals(expectedSymbols, result)
        coVerify { symbolService.fetchSymbols(symbol) }
    }
}

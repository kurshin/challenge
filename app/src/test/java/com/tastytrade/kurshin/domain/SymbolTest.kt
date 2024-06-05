package com.tastytrade.kurshin.domain

import org.junit.Assert.*
import org.junit.Test

class SymbolTest {

    @Test
    fun `symbol default values`() {
        // Arrange & Act
        val symbol = Symbol(id = 1, name = "AAPL")

        // Assert
        assertEquals(1L, symbol.id)
        assertEquals("AAPL", symbol.name)
        assertFalse(symbol.isChecked)
        assertEquals(0L, symbol.watchListId)
        assertEquals(0.0, symbol.lastPrice, 0.0)
        assertEquals(0.0, symbol.askPrice, 0.0)
        assertEquals(0.0, symbol.bidPrice, 0.0)
    }

    @Test
    fun `symbol equality`() {
        // Arrange
        val symbol1 = Symbol(id = 1, name = "AAPL")
        val symbol2 = Symbol(id = 2, name = "AAPL")

        // Act & Assert
        assertEquals(symbol1, symbol2)
    }

    @Test
    fun `symbol inequality`() {
        // Arrange
        val symbol1 = Symbol(id = 1, name = "AAPL")
        val symbol2 = Symbol(id = 2, name = "GOOG")

        // Act & Assert
        assertNotEquals(symbol1, symbol2)
    }

    @Test
    fun `symbol hashCode`() {
        // Arrange
        val symbol1 = Symbol(id = 1, name = "AAPL")
        val symbol2 = Symbol(id = 2, name = "AAPL")

        // Act & Assert
        assertEquals(symbol1.hashCode(), symbol2.hashCode())
    }

    @Test
    fun `symbol different hashCode`() {
        // Arrange
        val symbol1 = Symbol(id = 1, name = "AAPL")
        val symbol2 = Symbol(id = 2, name = "GOOG")

        // Act & Assert
        assertNotEquals(symbol1.hashCode(), symbol2.hashCode())
    }
}

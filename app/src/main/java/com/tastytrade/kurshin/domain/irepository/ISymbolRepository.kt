package com.tastytrade.kurshin.domain.irepository

import com.tastytrade.kurshin.domain.Symbol

interface ISymbolRepository {
    suspend fun fetchSymbols(symbol: String): List<Symbol>
}
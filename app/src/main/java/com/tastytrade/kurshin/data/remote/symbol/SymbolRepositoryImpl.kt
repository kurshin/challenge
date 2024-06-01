package com.tastytrade.kurshin.data.remote.symbol

import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.irepository.ISymbolRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SymbolRepositoryImpl(private val symbolService: SymbolService) : ISymbolRepository {

    override suspend fun fetchSymbols(symbol: String): List<Symbol> {
        return withContext(Dispatchers.IO) {
            symbolService.fetchSymbols(symbol).data.items.map { Symbol(it.symbol) }
        }
    }
}
package com.tastytrade.kurshin.data.remote.stock

import com.tastytrade.kurshin.domain.Chart
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.irepository.IStockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class StockSimulationRepositoryImpl : IStockRepository {
    companion object {
        private const val NETWORK_SIMULATED_DELAY_long = 400L
        private const val NETWORK_SIMULATED_DELAY_quick = 120L
        private const val CHART_PERIOD_DAYS = 30
    }

    override suspend fun fetchQuote(symbol: String): Symbol {
        return withContext(Dispatchers.IO) {
            delay(NETWORK_SIMULATED_DELAY_quick)
            Symbol(
                symbol,
                false,
                0,
                randomDoubleInRange(),
                randomDoubleInRange(),
                randomDoubleInRange(),
            )
        }
    }

    override suspend fun fetchChart(symbol: String): List<Chart> {
        return withContext(Dispatchers.IO) {
            delay(NETWORK_SIMULATED_DELAY_long)
            generateQuotes(symbol)
        }
    }

    private fun generateQuotes(symbol: String): List<Chart> {
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM")
        return (0 until CHART_PERIOD_DAYS).map { daysAgo ->
            val date = LocalDate.now().minusDays(daysAgo.toLong()).format(dateFormatter)
            Chart(
                symbol = symbol,
                open = randomDoubleInRange(),
                close = randomDoubleInRange(),
                high = randomDoubleInRange(),
                low = randomDoubleInRange(),
                date = date
            )
        }.reversed()
    }

    private fun randomDoubleInRange(): Double {
        return Random.nextDouble(0.0, 100.0).takeIf { it > 0.0 } ?: 1.0
    }
}
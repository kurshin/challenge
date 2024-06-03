package com.tastytrade.kurshin.presentation.ui.chart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tastytrade.kurshin.data.remote.stock.StockSimulationRepositoryImpl
import com.tastytrade.kurshin.domain.Chart
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.irepository.IStockRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val refreshDelayMillis = 5000L
class ChartViewModel(private val stockRepo: IStockRepository) : ViewModel() {

    val chart = MutableLiveData<List<Chart>>()
    val quote = MutableLiveData<Symbol>()
    val error = MutableLiveData<String>()

    fun getQuoteDataRepeatedly(symbol: String) = viewModelScope.launch(errorHandler) {
        while (isActive) {
            val result = stockRepo.fetchQuote(symbol)
            quote.postValue(result)
            delay(refreshDelayMillis)
        }
    }

    fun getChartData(symbol: String) = viewModelScope.launch(errorHandler) {
        val result = stockRepo.fetchChart(symbol)
        chart.postValue(result)
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        error.postValue(exception.message)
    }
}

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChartViewModel(
            StockSimulationRepositoryImpl()
//            StockRepositoryImpl(stockRetrofit.service) // IEX surprisingly stopped working for free on June 1
        ) as T
    }
}
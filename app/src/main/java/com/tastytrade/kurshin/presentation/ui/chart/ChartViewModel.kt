package com.tastytrade.kurshin.presentation.ui.chart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tastytrade.kurshin.domain.Chart
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.irepository.IStockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(private val stockRepo: IStockRepository) : ViewModel() {

    val chart = MutableLiveData<List<Chart>>()
    val quote = MutableLiveData<Symbol>()
    val error = MutableLiveData<String>()

    suspend fun getQuoteDataRepeatedly(symbol: String) {
        val result = stockRepo.fetchQuote(symbol)
        quote.postValue(result)
    }

    fun getChartData(symbol: String) = viewModelScope.launch(errorHandler) {
        val result = stockRepo.fetchChart(symbol)
        chart.postValue(result)
    }

    val errorHandler = CoroutineExceptionHandler { _, exception ->
        error.postValue(exception.message)
    }
}
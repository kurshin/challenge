package com.tastytrade.kurshin.presentation.ui.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tastytrade.kurshin.data.remote.StockRepository
import com.tastytrade.kurshin.domain.Chart
import com.tastytrade.kurshin.presentation.ui.main.launch
import kotlinx.coroutines.CoroutineExceptionHandler

class ChartViewModel(private val stockRepo: StockRepository) : ViewModel() {

    private val _chart = MutableLiveData<List<Chart>>()
    val chart: LiveData<List<Chart>> get() = _chart

    private val _error = MutableLiveData<String>()

    fun getChartData(symbol: String) = launch(errorHandler) {
        val result = stockRepo.fetchChart(symbol)
        _chart.postValue(result)
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _error.postValue(exception.message)
    }
}
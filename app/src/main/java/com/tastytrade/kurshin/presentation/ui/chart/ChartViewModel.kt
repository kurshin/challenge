package com.tastytrade.kurshin.presentation.ui.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tastytrade.kurshin.data.remote.StockRepositoryImpl
import com.tastytrade.kurshin.domain.Chart
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ChartViewModel(private val stockRepo: StockRepositoryImpl) : ViewModel() {

    private val _chart = MutableLiveData<List<Chart>>()
    val chart: LiveData<List<Chart>> get() = _chart

    private val _error = MutableLiveData<String>()

    fun getChartData(symbol: String) = viewModelScope.launch(errorHandler) {
        val result = stockRepo.fetchChart(symbol)
        _chart.postValue(result)
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _error.postValue(exception.message)
    }
}
package com.tastytrade.kurshin.presentation.ui.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tastytrade.kurshin.data.remote.stock.StockRepositoryImpl
import com.tastytrade.kurshin.data.remote.stockRetrofit
import com.tastytrade.kurshin.domain.Chart
import com.tastytrade.kurshin.domain.irepository.IStockRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ChartViewModel(private val stockRepo: IStockRepository) : ViewModel() {

    private val _chart = MutableLiveData<List<Chart>>()
    val chart: LiveData<List<Chart>> get() = _chart

    val error = MutableLiveData<String>()

    fun getChartData(symbol: String) = viewModelScope.launch(errorHandler) {
        val result = stockRepo.fetchChart(symbol)
        _chart.postValue(result)
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        error.postValue(exception.message)
    }
}

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChartViewModel(StockRepositoryImpl(stockRetrofit.service)) as T
    }
}
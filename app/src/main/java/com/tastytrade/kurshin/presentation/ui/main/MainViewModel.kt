package com.tastytrade.kurshin.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tastytrade.kurshin.data.persisted.WatchListRepository
import com.tastytrade.kurshin.data.remote.StockRepository
import com.tastytrade.kurshin.data.remote.RetrofitHolder
import com.tastytrade.kurshin.domain.DEFAULT_WATCHLIST
import com.tastytrade.kurshin.domain.Quote
import com.tastytrade.kurshin.domain.WatchList
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    private val stockRepo: StockRepository,
    private val watchListRepo: WatchListRepository
) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _quote = MutableLiveData<Quote>()
    val quote: LiveData<Quote> get() = _quote

    internal val currentWatchlist = MutableLiveData(DEFAULT_WATCHLIST)

    val watchList: List<WatchList> get() = watchListRepo.watchList
    fun addWatchList(watchList: WatchList) {
        watchListRepo.addWatchlist(watchList)
        currentWatchlist.postValue(watchList)
    }

    fun updateWatchList(watchList: WatchList) {
        currentWatchlist.postValue(watchList)
        watchListRepo.updateWatchlist(watchList)
    }

    fun deleteWatchList(watchList: WatchList) {
        if (watchList.name == currentWatchlist.value?.name) {
            currentWatchlist.postValue(DEFAULT_WATCHLIST)
        }
        watchListRepo.removeWatchlist(watchList)
    }

    fun getSymbolData(symbol: String) = launch(errorHandler) {
        val result = stockRepo.fetchQuote(symbol)
        _quote.postValue(result)
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _error.postValue(exception.message)
    }
}

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(StockRepository(RetrofitHolder.stockService), WatchListRepository()) as T
    }
}

fun ViewModel.launch(
    context: CoroutineContext = Dispatchers.Main,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = viewModelScope.launch(context, start, block)
package com.tastytrade.kurshin.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.WatchList
import com.tastytrade.kurshin.domain.irepository.IQuoteRepository
import com.tastytrade.kurshin.domain.irepository.IStockRepository
import com.tastytrade.kurshin.domain.irepository.ISymbolRepository
import com.tastytrade.kurshin.domain.irepository.IWatchListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stockRepo: IStockRepository,
    private val watchListRepo: IWatchListRepository,
    private val symbolRepo: ISymbolRepository,
    private val quoteRepo: IQuoteRepository,
) : ViewModel() {

    val currentWatchlist = MutableLiveData<WatchList?>()
    val error = MutableLiveData<Throwable>()
    val symbolsForAutofill = MutableLiveData<List<Symbol>>()
    var watchList: List<WatchList> = emptyList()

    var networkErrorHandler = CoroutineExceptionHandler { _, exception ->
        error.postValue(exception)
    }

    private val dbErrorHandler = CoroutineExceptionHandler { _, _ -> }

    init {
        fetchWatchlistData()
        fulfillDBInitialData()
    }

    fun addWatchList(watchList: WatchList) = viewModelScope.launch(dbErrorHandler) {
        val id = watchListRepo.addWatchlist(watchList)
        currentWatchlist.postValue(watchList.apply { watchList.id = id })
    }

    fun updateWatchList(watchList: WatchList) = viewModelScope.launch(dbErrorHandler) {
        currentWatchlist.postValue(watchList)
        watchListRepo.updateWatchlist(watchList)
    }

    fun refreshWatchList() = viewModelScope.launch(dbErrorHandler) {
        currentWatchlist.postValue(currentWatchlist.value)
    }

    fun deleteWatchList(watchList: WatchList) = viewModelScope.launch(dbErrorHandler) {
        watchListRepo.removeWatchlist(watchList)
        if (watchList.name == currentWatchlist.value?.name) {
            val allWatchLists = watchListRepo.getAllWatchListsSync()
            currentWatchlist.postValue(allWatchLists.firstOrNull())
        }
    }

    fun removeSymbol(symbol: Symbol) = viewModelScope.launch(dbErrorHandler) {
        quoteRepo.deleteQuote(symbol)
    }

    fun addSymbol(symbol: Symbol) = viewModelScope.launch(dbErrorHandler) {
        quoteRepo.insertQuote(symbol)
    }

    suspend fun fetchQuoteData(symbol: String) = stockRepo.fetchQuote(symbol)

    fun searchSymbol(symbol: String) = viewModelScope.launch(networkErrorHandler) {
        val result = symbolRepo.fetchSymbols(symbol)
        symbolsForAutofill.postValue(result)
    }

    fun getSymbolsForWatchlist(watchlist: WatchList): LiveData<List<Symbol>> {
        val result = MutableLiveData<List<Symbol>>()
        viewModelScope.launch {
            result.value = if (watchlist.isDefault) {
                quoteRepo.getAllQuotesSync()
            } else {
                quoteRepo.getQuotesForWatchlist(watchlist.id)
            }
        }
        return result
    }

    internal fun fetchWatchlistData() = viewModelScope.launch(dbErrorHandler) {
        watchListRepo.getAllWatchLists().collect { watchList = it }
    }

    internal fun fulfillDBInitialData() = viewModelScope.launch(dbErrorHandler) {
        val allWatchLists = watchListRepo.getAllWatchListsSync()
        if (allWatchLists.isEmpty()) {
            val defaultWatchList = WatchList("My first list")
            val id = watchListRepo.addWatchlist(defaultWatchList)
            defaultWatchList.id = id

            insertSymbol("AAPL", id)
            insertSymbol("GOOG", id)
            insertSymbol("MSFT", id)
            currentWatchlist.postValue(defaultWatchList)
        } else {
            currentWatchlist.postValue(allWatchLists.firstOrNull())
        }
    }

    private suspend fun insertSymbol(name: String, watchlistId: Long) {
        val symbol = Symbol(0, name, true, watchlistId)
        val id = quoteRepo.insertQuote(symbol)
        symbol.id = id
    }
}
package com.tastytrade.kurshin.presentation.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tastytrade.kurshin.data.persisted.AppDatabase
import com.tastytrade.kurshin.data.persisted.QuoteRepositoryImpl
import com.tastytrade.kurshin.data.persisted.WatchListRepositoryDBImpl
import com.tastytrade.kurshin.data.remote.stock.StockSimulationRepositoryImpl
import com.tastytrade.kurshin.data.remote.symbol.SymbolRepositoryImpl
import com.tastytrade.kurshin.data.remote.symbolRetrofit
import com.tastytrade.kurshin.domain.DEFAULT_WATCHLIST
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.WatchList
import com.tastytrade.kurshin.domain.irepository.IQuoteRepository
import com.tastytrade.kurshin.domain.irepository.IStockRepository
import com.tastytrade.kurshin.domain.irepository.ISymbolRepository
import com.tastytrade.kurshin.domain.irepository.IWatchListRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MainViewModel(
    private val stockRepo: IStockRepository,
    private val watchListRepo: IWatchListRepository,
    private val symbolRepo: ISymbolRepository,
    private val quoteRepo: IQuoteRepository,
) : ViewModel() {

    val currentWatchlist = MutableLiveData(DEFAULT_WATCHLIST)
    var selectedSymbols:List<Symbol> = emptyList()
    var selectedSymbolsFlow = quoteRepo.getAllQuotes()

    val error = MutableLiveData<String>()
    val symbolsForAutofill = MutableLiveData<List<Symbol>>()
    var watchList: List<WatchList> = emptyList()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        error.postValue(exception.message)
        Log.i("1111", "error = $exception.message")
    }

    init {
        fulfillWatchlistData()
        fulfillQuotesData()
        fulfillDBInitialData()
    }

    fun addWatchList(watchList: WatchList) = viewModelScope.launch(errorHandler) {
        watchListRepo.addWatchlist(watchList)
        currentWatchlist.postValue(watchList)
    }

    fun updateWatchList(watchList: WatchList) = viewModelScope.launch(errorHandler) {
        currentWatchlist.postValue(watchList)
        watchListRepo.updateWatchlist(watchList)
    }

    fun refreshWatchList() = viewModelScope.launch(errorHandler) {
        currentWatchlist.postValue(currentWatchlist.value)
    }

    fun deleteWatchList(watchList: WatchList) = viewModelScope.launch(errorHandler) {
        if (watchList.name == currentWatchlist.value?.name) {
            currentWatchlist.postValue(DEFAULT_WATCHLIST)
        }
        watchListRepo.removeWatchlist(watchList)
    }

    fun removeSymbol(symbol: Symbol) = viewModelScope.launch(errorHandler) {
        quoteRepo.deleteQuote(symbol)
//        currentWatchlist.postValue(currentWatchlist.value)
    }

    fun addSymbol(symbol: Symbol) = viewModelScope.launch(errorHandler) {
        quoteRepo.insertQuote(symbol)
    }

    suspend fun fetchQuoteData(symbol: String) = stockRepo.fetchQuote(symbol)

    fun searchSymbol(symbol: String) = viewModelScope.launch(errorHandler) {
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

    private fun fulfillWatchlistData() = viewModelScope.launch(errorHandler) {
        watchListRepo.getAllWatchLists().collect { watchList = it }
    }

    private fun fulfillQuotesData() = viewModelScope.launch(errorHandler) {
        quoteRepo.getAllQuotes().collect { selectedSymbols = it }
    }

    private fun fulfillDBInitialData() = viewModelScope.launch(errorHandler) {
        val allWatchLists = watchListRepo.getAllWatchListsSync()
        if (allWatchLists.isEmpty()) {
            watchListRepo.addWatchlist(DEFAULT_WATCHLIST)

            val defaultWatchList = WatchList("My first list")
            val id = watchListRepo.addWatchlist(defaultWatchList)
            defaultWatchList.id = id
            quoteRepo.insertQuote(Symbol("AAPL", true, id))
            quoteRepo.insertQuote(Symbol("GOOG", true, id))
            quoteRepo.insertQuote(Symbol("MSFT", true, id))

            currentWatchlist.postValue(defaultWatchList)
        } else {
            val defaultWatchlist = allWatchLists.first { it.isDefault }
            currentWatchlist.postValue(defaultWatchlist)
        }
    }
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val appDatabase: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            StockSimulationRepositoryImpl(),
//            StockRepositoryImpl(stockRetrofit.service), // IEX surprisingly stopped working for free on June 1
            WatchListRepositoryDBImpl(appDatabase.watchListDao()),
            SymbolRepositoryImpl(symbolRetrofit.service),
            QuoteRepositoryImpl(appDatabase.quoteDao())
        ) as T
    }
}

fun Context.getTastyTradeSharedPrefs(): SharedPreferences {
    return getSharedPreferences("tastytrade_prefs", Context.MODE_PRIVATE)
}
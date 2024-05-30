package com.tastytrade.kurshin.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tastytrade.kurshin.data.remote.ChartRepository

class MainViewModel(private val repository: ChartRepository) : ViewModel() {
}

object ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(ChartRepository()) as T
    }
}
package com.tastytrade.kurshin

import android.app.Application
import androidx.room.Room
import com.tastytrade.kurshin.data.persisted.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TestyTradeApplication: Application()
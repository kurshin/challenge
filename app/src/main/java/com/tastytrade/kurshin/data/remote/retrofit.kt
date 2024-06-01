package com.tastytrade.kurshin.data.remote

import com.tastytrade.kurshin.BuildConfig
import com.tastytrade.kurshin.data.remote.stock.StockService
import com.tastytrade.kurshin.data.remote.symbol.SymbolService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance<T>(serviceClass: Class<T>, baseUrl: String) {
    val service: T

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(serviceClass)
    }
}

inline fun <reified T> createRetrofitInstance(baseUrl: String): RetrofitInstance<T> {
    return RetrofitInstance(T::class.java, baseUrl)
}

val symbolRetrofit = createRetrofitInstance<SymbolService>(BuildConfig.BASE_URL_SYMBOL_API)
val stockRetrofit = createRetrofitInstance<StockService>(BuildConfig.BASE_URL_STOCK_API)

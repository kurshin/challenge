package com.tastytrade.kurshin.data.remote

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

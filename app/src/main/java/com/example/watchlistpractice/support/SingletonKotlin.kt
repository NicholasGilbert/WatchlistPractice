package com.example.watchlistpractice.support

import com.example.watchlistpractice.data.ApiData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

    object SingletonKotlin {
    val RETROFIT_INTERFACE by lazy{
        RetrofitInterface.create()
    }
//    init {
//        val RETROFIT_INTERFACE by lazy{
//            RetrofitInterface.create()
//        }
//    }
}
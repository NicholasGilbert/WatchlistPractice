package com.example.watchlistpractice.support

import com.example.watchlistpractice.data.ApiData
import com.example.watchlistpractice.data.Response
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {
    companion object{
        fun create(): RetrofitInterface{
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl("https://api.themoviedb.org/3/search/")
                .build()

            return retrofit.create(RetrofitInterface::class.java)
        }
    }

    @GET("movie?api_key=21d29fc06b9df12fa42a5d15af0651c1")
    fun findMovie(@Query("query") query: String): Call<ApiData.Response>
}
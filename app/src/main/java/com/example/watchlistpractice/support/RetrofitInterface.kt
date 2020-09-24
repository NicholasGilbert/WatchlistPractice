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

interface RetrofitInterface {
    companion object{
        fun create(): RetrofitInterface{
            val sLogging    = HttpLoggingInterceptor()
            sLogging.level  = HttpLoggingInterceptor.Level.BODY
            val sOkHttpClient = OkHttpClient.Builder()
                                                         .addInterceptor(sLogging)
                                                         .build()
            val sRetrofit   :Retrofit = Retrofit.Builder()
                                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .client(sOkHttpClient)
                                                .baseUrl("https://api.themoviedb.org/3/")
                                                .build()

            return sRetrofit.create(RetrofitInterface::class.java)
        }
    }

    @GET("search/movie?api_key=21d29fc06b9df12fa42a5d15af0651c1&sort_by=popularity.desc")
    fun findMovie(@Query("query") query: String): Call<ApiData.Response>

    @GET("discover/movie?api_key=21d29fc06b9df12fa42a5d15af0651c1&sort_by=popularity.desc")
    fun discoverMovie(@Query("with_genres") with_genres: Int?): Call<ApiData.Response>
}
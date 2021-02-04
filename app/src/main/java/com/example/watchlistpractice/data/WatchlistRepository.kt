package com.example.watchlistpractice.data

import com.example.watchlistpractice.support.database.RoomMovieDatabase
import com.example.watchlistpractice.support.network.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WatchlistRepository(inDatabase: RoomMovieDatabase) {
    val database: RoomMovieDatabase = inDatabase
    private val retrofitInterface by lazy {
        RetrofitInterface.create()
    }

    var movieList: MutableList<RoomMovie> = mutableListOf()

    fun findMovie(search: String): MutableList<RoomMovie> {
        val getMovie: MutableList<RoomMovie> = mutableListOf()
        val sCall: Call<ApiData.Response> = retrofitInterface.findMovie(search)
        val sRes = sCall.enqueue(object : Callback<ApiData.Response> {
            override fun onFailure(call: Call<ApiData.Response>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<ApiData.Response>,
                response: Response<ApiData.Response>
            ) {
                if (response.body() != null) {
                    if (response.body()?.results != null) {
                        val results = response.body()?.results ?: ArrayList()
                        bypass(results)
                    }
                }
            }
        })
        return movieList
    }

    fun bypass(inMovies: ArrayList<ApiData.ResultsItem>) {
        movieList.clear()
        for (movies in inMovies) {
            movieList.add(
                RoomMovie(
                    movies.id,
                    movies.title,
                    movies.voteAverage,
                    movies.releaseDate,
                    movies.originalLanguage,
                    movies.overview,
                    "/zlyhKMi2aLk25nOHnNm43MpZMtQ.jpg"
                )
            )
        }
    }
}
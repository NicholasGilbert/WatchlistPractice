package com.example.watchlistpractice.activitiy.main

import com.example.watchlistpractice.data.ApiData
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.fragment.MovieDetailFragment
import com.example.watchlistpractice.support.ListCardAdapter
import com.example.watchlistpractice.support.RetrofitInterface
import com.example.watchlistpractice.support.RoomMovieDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(view: MainContract.View) : MainContract.Presenter, ListCardAdapter.OnMovieListener, ListCardAdapter.DeleteHelper {

    private val RETROFIT_INTERFACE by lazy{
        RetrofitInterface.create()
    }

    lateinit var database: RoomMovieDatabase

    private var view: MainContract.View? = view

    override fun onSearch(inString: String) {
        getData(inString)
    }

    override fun addData(inMovie: RoomMovie) {
//        var mChecker = true
//
//        for(movie in database.DataDAO().getData()){
//            if (inMovie.roomMovieId == movie.roomMovieId) mChecker = false
//        }
//        if (mChecker == true) database.DataDAO().insert(inMovie)
    }

    override fun onDestroy() {
        this.view = null
    }

    fun getData(inString: String){
        val movieList: ArrayList<RoomMovie> = ArrayList()
        val sCall: Call<ApiData.Response> = RETROFIT_INTERFACE.findMovie(inString)
        val sRes = sCall!!.enqueue(object: Callback<ApiData.Response> {
            override fun onFailure(call: Call<ApiData.Response>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<ApiData.Response>, response: Response<ApiData.Response>) {
                for (movies in response.body()!!.results!!){
                    movieList.add(RoomMovie(movies.id!!,
                        movies.title!!,
                        movies.vote_average!!,
                        movies.release_date!!,
                        movies.original_language!!,
                        movies.overview!!,
                        "/zlyhKMi2aLk25nOHnNm43MpZMtQ.jpg"))
                }
                view!!.setList(movieList, this@MainPresenter, this@MainPresenter)
            }
        })
    }

    override fun onMovieClick(movie: RoomMovie) {
        view!!.onMovieClick(movie)
    }

    override fun onSwipe(task: RoomMovie) {
        TODO("Not yet implemented")
    }

}
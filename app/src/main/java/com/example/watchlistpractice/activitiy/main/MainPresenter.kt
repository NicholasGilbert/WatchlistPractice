package com.example.watchlistpractice.activitiy.main

import androidx.room.Room
import com.example.watchlistpractice.data.ApiData
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.data.WatchlistRepository
import com.example.watchlistpractice.support.adapter.ListCardAdapter
import com.example.watchlistpractice.support.network.RetrofitInterface
import com.example.watchlistpractice.support.database.RoomMovieDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(view: MainContract.View, inDatabase: RoomMovieDatabase) : MainContract.Presenter, ListCardAdapter.OnMovieListener, ListCardAdapter.DeleteHelper {

    private val retrofitInterface by lazy{
        RetrofitInterface.create()
    }

    val database = inDatabase
//    lateinit var database: RoomMovieDatabase

    private var view: MainContract.View? = view

    val repository = WatchlistRepository(database)

    override fun onSearch(inString: String) {
        getData(inString)
    }



    override fun addData(inMovie: RoomMovie) {
        var mChecker = true

        for(movie in database.DataDAO().getData()){
            if (inMovie.roomMovieId == movie.roomMovieId) mChecker = false
        }
        if (mChecker == true) database.DataDAO().insert(inMovie)
    }

    override fun actSetList() {
        view!!.setList(this@MainPresenter, this@MainPresenter)
    }

    override fun onDestroy() {
        this.view = null
    }

    fun getData(inString: String){
        val movie = repository.findMovie(inString)
        view?.updateList(movie)
    }

    override fun onMovieClick(movie: RoomMovie) {
        view!!.onMovieClick(movie)
    }

    override fun onSwipe(task: RoomMovie) {
        TODO("Not yet implemented")
    }

}
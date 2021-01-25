package com.example.watchlistpractice.activitiy.main

import com.example.watchlistpractice.activitiy.BaseView
import com.example.watchlistpractice.activitiy.BasePresenter
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.support.adapter.ListCardAdapter
import com.example.watchlistpractice.support.database.RoomMovieDatabase

interface MainContract {
    interface Presenter : BasePresenter {
        fun onSearch(inString: String)
        fun addData(inMovie: RoomMovie, database: RoomMovieDatabase)
        fun actSetList()
    }

    interface View :
        BaseView<Presenter> {
        fun setList(inMovieListener: ListCardAdapter.OnMovieListener, inDelete : ListCardAdapter.DeleteHelper)
        fun onMovieClick(movie: RoomMovie)
        fun setDatabase()
        fun updateList(movieList: MutableList<RoomMovie>)
    }
}
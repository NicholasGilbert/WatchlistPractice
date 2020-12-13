package com.example.watchlistpractice.activitiy.main

import com.example.watchlistpractice.BaseView
import com.example.watchlistpractice.activitiy.BasePresenter
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.support.ListCardAdapter

interface MainContract {
    interface Presenter : BasePresenter {
        fun onSearch(inString: String)
        fun addData(movie: RoomMovie)
    }

    interface View : BaseView<Presenter> {
        fun setList(inList: ArrayList<RoomMovie>, inMovieListener: ListCardAdapter.OnMovieListener, inDelete : ListCardAdapter.DeleteHelper)
        fun onMovieClick(movie: RoomMovie)
    }
}
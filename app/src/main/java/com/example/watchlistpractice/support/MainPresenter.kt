package com.example.watchlistpractice.support

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.watchlistpractice.R
import com.example.watchlistpractice.activitiy.main.MainActivity
import com.example.watchlistpractice.data.ApiData
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.fragment.MovieDetailFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter (val act: MainActivity): ListCardAdapter.OnMovieListener, ListCardAdapter.DeleteHelper{

    private val RETROFIT_INTERFACE by lazy{
        RetrofitInterface.create()
    }

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListCardAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var database: RoomMovieDatabase

    fun setStatusColor(){
        act.window.statusBarColor = act.resources.getColor(R.color.colorPrimaryDark)
    }

    fun setRecycler(){
        recyclerView = act.findViewById(R.id.recycler_view_main)
    }

    fun setToolbar(){
        act.setSupportActionBar(act.findViewById(R.id.main_toolbar))
    }

    fun setDatabase(){
        database = Room.databaseBuilder(act.applicationContext, RoomMovieDatabase::class.java, "data.db").build()
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
                setList(movieList)
            }
        })
    }

    fun setList(inList: ArrayList<RoomMovie>){
        adapter = ListCardAdapter(inList, this, this)

        layoutManager = GridLayoutManager(act, 2)
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = adapter
    }

    fun addData(inMovie: RoomMovie){
        var mChecker = true

        for(movie in database.DataDAO().getData()){
            if (inMovie.roomMovieId == movie.roomMovieId) mChecker = false
        }
        if (mChecker == true) database.DataDAO().insert(inMovie)
    }

    override fun onMovieClick(movie: RoomMovie) {
        MovieDetailFragment(movie, act, 1).apply {
            show(act.supportFragmentManager, tag)
        }
    }

    override fun onSwipe(task: RoomMovie) {
        TODO("Not yet implemented")
    }
}
package com.example.watchlistpractice.support

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.watchlistpractice.R
import com.example.watchlistpractice.activitiy.discover.DiscoverMoviesActivity
import com.example.watchlistpractice.data.ApiData
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.fragment.MovieDetailFragment
import com.example.watchlistpractice.support.adapter.ListCardAdapter
import com.example.watchlistpractice.support.database.RoomMovieDatabase
import com.example.watchlistpractice.support.network.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscoverPresenter(val act: DiscoverMoviesActivity) : ListCardAdapter.OnMovieListener,
    ListCardAdapter.DeleteHelper {

    private val RETROFIT_INTERFACE by lazy {
        RetrofitInterface.create()
    }

    lateinit var dropdown: Spinner
    lateinit var spinnerAdapter: ArrayAdapter<String>
    val OPTIONS: List<String> = listOf("Discover", "Action", "Adventure", "Animation", "Comedy")

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListCardAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var database: RoomMovieDatabase

    fun setStatusColor() {
        act.window.statusBarColor = act.resources.getColor(R.color.colorPrimaryDark)
    }

    fun setRecycler() {
        recyclerView = act.findViewById(R.id.rv_discover_movie)
    }

    fun setDatabase() {
        database =
            Room.databaseBuilder(act.applicationContext, RoomMovieDatabase::class.java, "data.db")
                .build()
    }

    fun setDropdownSpinner() {
        dropdown = act.findViewById(R.id.spinner_genre)
        spinnerAdapter = ArrayAdapter(act, R.layout.item_genre, OPTIONS)
        dropdown.adapter = spinnerAdapter
    }

    fun getGenreId(inGenre: String): Int? {
        if (inGenre == "Discover") return null
        else if (inGenre == "Action") return 28
        else if (inGenre == "Adventure") return 12
        else if (inGenre == "Animation") return 16
        else if (inGenre == "Comedy") return 35
        else return null
    }

    fun getData() {
        var movieList: ArrayList<RoomMovie> = ArrayList()

        val sGenreId: Int? = getGenreId(dropdown.selectedItem.toString())

        val sCall: Call<ApiData.Response> = RETROFIT_INTERFACE.discoverMovie(sGenreId)
        val sRes: Unit = sCall!!.enqueue(object : Callback<ApiData.Response> {
            override fun onFailure(call: Call<ApiData.Response>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<ApiData.Response>,
                response: Response<ApiData.Response>
            ) {
                for (movies in response.body()!!.results!!) {
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
                setList(movieList)
            }
        })
    }

    fun setList(inList: ArrayList<RoomMovie>) {
        adapter = ListCardAdapter(
            inList,
            this,
            this
        )

        layoutManager = GridLayoutManager(act, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    fun addData(inMovie: RoomMovie) {
        var mChecker = true

        for (movie in database.DataDAO().getData()) {
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
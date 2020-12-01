package com.example.watchlistpractice.support

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watchlistpractice.activitiy.MainActivity
import com.example.watchlistpractice.data.ApiData
import com.example.watchlistpractice.data.RoomMovie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter (val main: MainActivity) {

    private val RETROFIT_INTERFACE by lazy{
        RetrofitInterface.create()
    }

    lateinit var adapter: ListCardAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager

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
        adapter = ListCardAdapter(inList, main, main)

        layoutManager = GridLayoutManager(main, 2)
        main.recyclerView.layoutManager =  layoutManager
        main.recyclerView.adapter = adapter
    }
}
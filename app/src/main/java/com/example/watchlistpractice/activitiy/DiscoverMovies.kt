package com.example.watchlistpractice.activitiy

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.ApiData
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.support.CardAdapter
import com.example.watchlistpractice.support.RetrofitInterface
import com.example.watchlistpractice.support.RoomMovieDatabase
import kotlinx.android.synthetic.main.activity_discover_movies.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscoverMovies : AppCompatActivity(), CardAdapter.OnMovieListener {
    lateinit var dropdown: Spinner
    lateinit var spinnerAdapter: ArrayAdapter<String>
    val options: List<String> = listOf("Discover", "Action", "Adventure", "Animation", "Comedy")

    private val retrofitInterface by lazy{
        RetrofitInterface.create()
    }

    var movieList: ArrayList<ApiData.ResultsItem> = ArrayList()

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CardAdapter
    lateinit var layoutManager: LinearLayoutManager

    lateinit var popup: PopupWindow

    lateinit var db: RoomMovieDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover_movies)

        recyclerView = findViewById(R.id.discover_recycler_view)

        db = Room.databaseBuilder(applicationContext, RoomMovieDatabase::class.java, "data.db").allowMainThreadQueries().build()

        dropdown = findViewById(R.id.spinner)
        spinnerAdapter = ArrayAdapter(this, R.layout.spinner_layout, options)
        dropdown.adapter = spinnerAdapter

        btnSearchCategory.setOnClickListener {
            val genreId = getGenreId(dropdown.selectedItem.toString())

            val call = retrofitInterface.discoverMovie(genreId)
            val res = call!!.enqueue(object : Callback<ApiData.Response> {
                override fun onFailure(call: Call<ApiData.Response>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<ApiData.Response>,
                    response: Response<ApiData.Response>
                ) {
                    movieList = response.body()!!.results!!
                    refreshList()
                }

            })
        }
    }

    fun refreshList(){
        adapter = CardAdapter(movieList, this)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = adapter
    }

    fun getGenreId(inGenre: String): Int?{
        if(inGenre == "Discover"){
            return null
        }
        else if(inGenre =="Action"){
            return 28
        }
        else if(inGenre =="Adventure"){
            return 12
        }
        else if(inGenre =="Animation"){
            return 16
        }
        else if(inGenre =="Comedy"){
            return 35
        }
        else{
            return null
        }
    }

    override fun onMovieClick(pos: Int) {
        val layoutInflater = this.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = layoutInflater.inflate(R.layout.movie_popup_layout, null)
        popup = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popup.isOutsideTouchable = true
        popup.isFocusable = true
        popup.setBackgroundDrawable(ColorDrawable(Color.LTGRAY))
        popup.showAtLocation(rlDiscoverMovies, Gravity.CENTER, 0, 0)

        val movieHolder = movieList.get(pos)

        val mCtvTitle = customView.findViewById(R.id.MCtvTitle) as TextView
        val mCtvRating = customView.findViewById(R.id.MCtvRating) as TextView
        val mCtvReleased = customView.findViewById(R.id.MCtvReleased) as TextView
        val mCtvLanguage = customView.findViewById(R.id.MCtvLanguage) as TextView
        val mCtvDescription = customView.findViewById(R.id.MCtvDescription) as TextView
        val btnAddMovieToList = customView.findViewById(R.id.btnAddToList) as Button
//
        mCtvTitle.text = movieHolder.title!!
        mCtvRating.text = mCtvRating.text.toString() + movieHolder.vote_average!!
        mCtvReleased.text = mCtvReleased.text.toString() + movieHolder.release_date!!
        mCtvLanguage.text = mCtvLanguage.text.toString() + movieHolder.original_language!!
        mCtvDescription.text = mCtvDescription.text.toString() + "\n" + movieHolder.overview!!

        btnAddMovieToList.setOnClickListener{
            popup.dismiss()
            addData(movieHolder)
        }
    }

    fun addData(inMovie: ApiData.ResultsItem){
        var checker = true

        for(movie in db.DataDAO().getData()){
            if (inMovie.id == movie.roomMovieId){
                checker = false
            }
        }
        if (checker == true) {
            db.DataDAO().insert(
                RoomMovie(  inMovie.id!!,
                inMovie.title!!,
                inMovie.vote_average!!,
                inMovie.release_date!!,
                inMovie.original_language!!,
                inMovie.overview!!)
            )
        }
    }
}
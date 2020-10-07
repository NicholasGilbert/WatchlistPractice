package com.example.watchlistpractice.activitiy

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.PopupWindow
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.ApiData
import com.example.watchlistpractice.data.Movie
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.fragment.MovieDetailFragment
import com.example.watchlistpractice.support.ListCardAdapter
import com.example.watchlistpractice.support.RetrofitInterface
import com.example.watchlistpractice.support.RoomMovieDatabase
import com.example.watchlistpractice.support.SwipeToDelete
import kotlinx.android.synthetic.main.activity_main.relative_layout_activity_main
import kotlinx.android.synthetic.main.activity_main.edit_text_search
import kotlinx.android.synthetic.main.activity_main.button_search
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), ListCardAdapter.OnMovieListener, ListCardAdapter.DeleteHelper, MovieDetailFragment.OnButtonListener {
    //Variable for Retrofit
    private val RETROFIT_INTERFACE by lazy{
        RetrofitInterface.create()
    }

    //Variable to store movies
    var movieList: ArrayList<Movie> = ArrayList()

    //Variables for recycler view
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListCardAdapter
    lateinit var layoutManager: LinearLayoutManager

    //Variable for popup
    lateinit var popup: PopupWindow

    //Variable for database
    lateinit var database: RoomMovieDatabase

    //Function to show menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Function to get new intent on menu click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_search -> {
                return true
            } R.id.item_my_list ->{
                val sIntent = Intent(this@MainActivity, MyListActivity::class.java)
                startActivity(sIntent)
                return true
            } R.id.item_discover ->{
                val sIntent = Intent(this@MainActivity, DiscoverMoviesActivity::class.java)
                startActivity(sIntent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view_main)

        database = Room.databaseBuilder(applicationContext, RoomMovieDatabase::class.java, "data.db").build()

        button_search.setOnClickListener {
//            movieList = ArrayList()
//            if (edit_text_search.text.toString() != "") {
//                val sInSearch: String = edit_text_search.text.toString()
//                val sCall: Call<ApiData.Response> = RETROFIT_INTERFACE.findMovie(sInSearch)
//                val sRes = sCall!!.enqueue(object: Callback<ApiData.Response> {
//                    override fun onFailure(call: Call<ApiData.Response>, t: Throwable) {
//                        t.printStackTrace()
//                    }
//
//                    override fun onResponse(call: Call<ApiData.Response>, response: Response<ApiData.Response>) {
//                        for (movies in response.body()!!.results!!){
//                            movieList.add(RoomMovie(    movies.id!!,
//                                                        movies.title!!,
//                                                        movies.vote_average!!,
//                                                        movies.release_date!!,
//                                                        movies.original_language!!,
//                                                        movies.overview!!))
//                        }
//                        refreshList()
//                    }
//
//                })
//            }

            if (edit_text_search.text.toString() != "") {
                val inSearch: String = edit_text_search.text.toString()
                CoroutineScope(IO).launch {
                    getData(inSearch)
                }
            }
        }
    }

    //Function when card is clicked
    override fun onMovieClick(movie: Movie) {
        MovieDetailFragment(movie, this).apply {
            show(supportFragmentManager, tag)
        }
//        val customView: View = layoutInflater.inflate(R.layout.popup_movie, null)
//        popup = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        popup.isOutsideTouchable = true
//        popup.isFocusable = true
//        popup.setBackgroundDrawable(ColorDrawable(Color.LTGRAY))
//        popup.showAtLocation(relative_layout_activity_main, Gravity.CENTER, 0, 0)
//
//        val popupTextViewTitle: TextView = customView.findViewById(R.id.text_view_title) as TextView
//        val popupTextViewRating: TextView = customView.findViewById(R.id.text_view_rating) as TextView
//        val popupTextViewReleased: TextView = customView.findViewById(R.id.text_view_released) as TextView
//        val popupTextViewLanguage: TextView = customView.findViewById(R.id.text_view_language) as TextView
//        val popupTextViewDescription: TextView = customView.findViewById(R.id.text_view_description) as TextView
//        val buttonAddMovie: Button = customView.findViewById(R.id.button_add) as Button
//
//        popupTextViewTitle.text = movie.title
//        popupTextViewRating.text = popupTextViewRating.text.toString() + movie.rating!!
//        popupTextViewReleased.text = popupTextViewReleased.text.toString() + movie.release!!
//        popupTextViewLanguage.text = popupTextViewLanguage.text.toString() + movie.language!!
//        popupTextViewDescription.text = popupTextViewDescription.text.toString() + "\n" + movie.description!!
//
//        buttonAddMovie.setOnClickListener{
//            popup.dismiss()
//
//            CoroutineScope(Dispatchers.IO).launch {
//                addData(movie)
//            }
//        }
    }

    fun getData(inString: String){
        movieList = ArrayList()
        val sCall: Call<ApiData.Response> = RETROFIT_INTERFACE.findMovie(inString)
        val sRes = sCall!!.enqueue(object: Callback<ApiData.Response> {
            override fun onFailure(call: Call<ApiData.Response>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<ApiData.Response>, response: Response<ApiData.Response>) {
                for (movies in response.body()!!.results!!){
//                    movieList.add(RoomMovie(movies.id!!,
//                                            movies.title!!,
//                                            movies.vote_average!!,
//                                            movies.release_date!!,
//                                            movies.original_language!!,
//                                            movies.overview!!))
                    movieList.add(Movie(Movie.Builder() .setMovieId(movies.id!!)
                                                        .setTitle(movies.title!!)
                                                        .setRating(movies.vote_average!!)
                                                        .setReleaseDate(movies.release_date!!)
                                                        .setLanguage(movies.original_language!!)
                                                        .setDescription(movies.overview!!)))
                }
                setList(movieList)
            }
        })
    }

    override fun onButtonClick(movie: Movie) {
        CoroutineScope(IO).launch {
            addData(movie)
        }
    }

    //Function to update the recycler view
//    fun refreshList(){
//        adapter = ListCardAdapter(movieList, this, this)
//
//        layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager =  layoutManager
//        recyclerView.adapter = adapter
//    }

    //Function to add data to local database
    fun addData(inMovie: Movie){
        var mChecker = true

        for(movie in database.DataDAO().getData()){
            if (inMovie.movieId == movie.roomMovieId) mChecker = false
        }
        if (mChecker == true) database.DataDAO().insert(RoomMovie(  inMovie.movieId,
                                                                    inMovie.title,
                                                                    inMovie.rating,
                                                                    inMovie.releaseDate,
                                                                    inMovie.language,
                                                                    inMovie.description))
    }

    fun setList(inList: ArrayList<Movie>){
        adapter = ListCardAdapter(inList, this, this)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = adapter
    }

    override fun onSwipe(task: Movie) {
        TODO("Not yet implemented")
    }
}
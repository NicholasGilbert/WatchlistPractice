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
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.ApiData
import com.example.watchlistpractice.data.Movie
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.support.ListCardAdapter
import com.example.watchlistpractice.support.RetrofitInterface
import com.example.watchlistpractice.support.RoomMovieDatabase
import com.example.watchlistpractice.support.SingletonKotlin
import kotlinx.android.synthetic.main.activity_discover_movies.button_search_genre
import kotlinx.android.synthetic.main.activity_discover_movies.relative_layout_activity_discover_movies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscoverMoviesActivity : AppCompatActivity(), ListCardAdapter.OnMovieListener, ListCardAdapter.DeleteHelper {
    //  Variables for spinner
    lateinit var dropdown: Spinner
    lateinit var spinnerAdapter: ArrayAdapter<String>
             val OPTIONS: List<String> = listOf("Discover", "Action", "Adventure", "Animation", "Comedy")

    //Variable for Retrofit
//    private val RETROFIT_INTERFACE by lazy{
//        RetrofitInterface.create()
//    }

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
                val sIntent = Intent(this@DiscoverMoviesActivity, MainActivity::class.java)
                startActivity(sIntent)
                return true
            } R.id.item_my_list ->{
                val sIntent = Intent(this@DiscoverMoviesActivity, MyListActivity::class.java)
                startActivity(sIntent)
                return true
            } R.id.item_discover ->{
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover_movies)

        recyclerView = findViewById(R.id.recycler_view_discover_movie)

        database = Room.databaseBuilder(applicationContext, RoomMovieDatabase::class.java, "data.db").build()

        dropdown = findViewById(R.id.spinner_genre)
        spinnerAdapter = ArrayAdapter(this, R.layout.item_genre, OPTIONS)
        dropdown.adapter = spinnerAdapter

        button_search_genre.setOnClickListener {
            movieList = ArrayList()

            val sGenreId: Int? = getGenreId(dropdown.selectedItem.toString())

//            val sCall: Call<ApiData.Response> = RETROFIT_INTERFACE.discoverMovie(sGenreId)
            val sCall: Call<ApiData.Response> = SingletonKotlin.RETROFIT_INTERFACE.discoverMovie(sGenreId)
            val sRes: Unit = sCall!!.enqueue(object : Callback<ApiData.Response> {
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
                    refreshList()
                }
            })
        }
    }

    //Function when card is clicked
    override fun onMovieClick(movie: Movie) {
        val customView: View = layoutInflater.inflate(R.layout.popup_movie, null)
        popup = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popup.isOutsideTouchable = true
        popup.isFocusable = true
        popup.setBackgroundDrawable(ColorDrawable(Color.LTGRAY))
        popup.showAtLocation(relative_layout_activity_discover_movies, Gravity.CENTER, 0, 0)

        val popupTextViewTitle: TextView = customView.findViewById(R.id.text_view_title) as TextView
        val popupTextViewRating: TextView = customView.findViewById(R.id.text_view_rating) as TextView
        val popupTextViewReleased: TextView = customView.findViewById(R.id.text_view_released) as TextView
        val popupTextViewLanguage: TextView = customView.findViewById(R.id.text_view_language) as TextView
        val popupTextViewDescription: TextView = customView.findViewById(R.id.text_view_description) as TextView
        val buttonAddMovie: Button = customView.findViewById(R.id.button_add) as Button

        popupTextViewTitle.text = movie.title
        popupTextViewRating.text = popupTextViewRating.text.toString() + movie.rating!!
        popupTextViewReleased.text = popupTextViewReleased.text.toString() + movie.releaseDate!!
        popupTextViewLanguage.text = popupTextViewLanguage.text.toString() + movie.language!!
        popupTextViewDescription.text = popupTextViewDescription.text.toString() + "\n" + movie.description!!

        buttonAddMovie.setOnClickListener{
            popup.dismiss()

            CoroutineScope(Dispatchers.IO).launch {
                addData(movie)
            }
        }
    }

    //Function to update the recycler view
    fun refreshList(){
        adapter = ListCardAdapter(movieList, this, this)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = adapter
    }

    //Function to get genre id
    fun getGenreId(inGenre: String): Int?{
        if (inGenre == "Discover") return null
        else if (inGenre == "Action") return 28
        else if (inGenre == "Adventure") return 12
        else if (inGenre == "Animation") return 16
        else if (inGenre == "Comedy") return 35
        else return null
    }

    //Function to add data to local database
    suspend fun addData(inMovie: Movie){
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

    override fun onSwipe(task: Movie) {
        TODO("Not yet implemented")
    }
}
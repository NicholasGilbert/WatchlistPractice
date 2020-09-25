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
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.support.CardAdapter
import com.example.watchlistpractice.support.RetrofitInterface
import com.example.watchlistpractice.support.RoomMovieDatabase
import kotlinx.android.synthetic.main.activity_discover_movies.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscoverMoviesActivity : AppCompatActivity(), CardAdapter.OnMovieListener {
    //  Variables for spinner
    lateinit var mDropdown      : Spinner
    lateinit var mSpinnerAdapter: ArrayAdapter<String>
             val OPTIONS        : List<String> = listOf("Discover", "Action", "Adventure", "Animation", "Comedy")

    //Variable for Retrofit
    private val RETROFIT_INTERFACE by lazy{
        RetrofitInterface.create()
    }

    //Variable to store movies
    var mMovieList: ArrayList<ApiData.ResultsItem> = ArrayList()

    //Variables for recycler view
    lateinit var mRecyclerView  : RecyclerView
    lateinit var mAdapter       : CardAdapter
    lateinit var mLayoutManager : LinearLayoutManager

    //Variable for popup
    lateinit var mPopup: PopupWindow

    //Variable for database
    lateinit var mDatabase: RoomMovieDatabase

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

        mRecyclerView       = findViewById(R.id.recycler_view_discover_movie)

        mDatabase           = Room.databaseBuilder(applicationContext, RoomMovieDatabase::class.java, "data.db").build()

        mDropdown           = findViewById(R.id.spinner_genre)
        mSpinnerAdapter     = ArrayAdapter(this, R.layout.item_genre, OPTIONS)
        mDropdown.adapter   = mSpinnerAdapter

        button_search_genre.setOnClickListener {
            val sGenreId    :Int?                    = getGenreId(mDropdown.selectedItem.toString())

            val sCall       : Call<ApiData.Response> = RETROFIT_INTERFACE.discoverMovie(sGenreId)
            val sRes        : Unit                   = sCall!!.enqueue(object : Callback<ApiData.Response> {
                override fun onFailure(call: Call<ApiData.Response>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<ApiData.Response>, response: Response<ApiData.Response>) {
                    mMovieList = response.body()!!.results!!
                    refreshList()
                }
            })
        }
    }

    //Function when card is clicked
    override fun onMovieClick(pos: Int) {
        val sCustomView     : View           = layoutInflater.inflate(R.layout.popup_movie, null)
        mPopup = PopupWindow(sCustomView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mPopup.isOutsideTouchable = true
        mPopup.isFocusable = true
        mPopup.setBackgroundDrawable(ColorDrawable(Color.LTGRAY))
        mPopup.showAtLocation(relative_layout_activity_discover_movies, Gravity.CENTER, 0, 0)

        val sMovieHolder                : ApiData.ResultsItem = mMovieList.get(pos)

        val sPopupTextViewTitle         : TextView  = sCustomView.findViewById(R.id.text_view_title)        as TextView
        val sPopupTextViewRating        : TextView  = sCustomView.findViewById(R.id.text_view_rating)       as TextView
        val sPopupTextViewReleased      : TextView  = sCustomView.findViewById(R.id.text_view_released)     as TextView
        val sPopupTextViewLanguage      : TextView  = sCustomView.findViewById(R.id.text_view_language)     as TextView
        val sPopupTextViewDescription   : TextView  = sCustomView.findViewById(R.id.text_view_description)  as TextView
        val sButtonAddMovie             : Button    = sCustomView.findViewById(R.id.button_add)             as Button

        sPopupTextViewTitle.text        = sMovieHolder.title!!
        sPopupTextViewRating.text       = sPopupTextViewRating.text.toString() + sMovieHolder.vote_average!!
        sPopupTextViewReleased.text     = sPopupTextViewReleased.text.toString() + sMovieHolder.release_date!!
        sPopupTextViewLanguage.text     = sPopupTextViewLanguage.text.toString() + sMovieHolder.original_language!!
        sPopupTextViewDescription.text  = sPopupTextViewDescription.text.toString() + "\n" + sMovieHolder.overview!!

        sButtonAddMovie.setOnClickListener{
            mPopup.dismiss()

            CoroutineScope(Dispatchers.IO).launch {
                addData(sMovieHolder)
            }
        }
    }

    //Function to update the recycler view
    fun refreshList(){
        mAdapter                    = CardAdapter(mMovieList, this)
        mLayoutManager              = LinearLayoutManager(this)
        mRecyclerView.layoutManager =  mLayoutManager
        mRecyclerView.adapter       = mAdapter
    }

    //Function to get genre id
    fun getGenreId(inGenre: String): Int?{
        if      (inGenre == "Discover")     return null
        else if (inGenre == "Action")       return 28
        else if (inGenre == "Adventure")    return 12
        else if (inGenre == "Animation")    return 16
        else if (inGenre == "Comedy")       return 35
        else                                return null
    }

    //Function to add data to local database
    suspend fun addData(inMovie: ApiData.ResultsItem){
        var mChecker = true

        for(movie in mDatabase.DataDAO().getData()){
            if (inMovie.id == movie.roomMovieId) mChecker = false
        }

        if (mChecker == true) mDatabase.DataDAO()
                                       .insert(RoomMovie(inMovie.id!!,
                                                         inMovie.title!!,
                                                         inMovie.vote_average!!,
                                                         inMovie.release_date!!,
                                                         inMovie.original_language!!,
                                                         inMovie.overview!!))
    }
}
package com.example.watchlistpractice.activitiy

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.fragment.MovieDetailFragment
import com.example.watchlistpractice.support.ListCardAdapter
import com.example.watchlistpractice.support.RoomMovieDatabase
import com.example.watchlistpractice.support.SwipeToDelete
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_list.*
import kotlinx.android.synthetic.main.popup_movie.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyListActivity : AppCompatActivity(), ListCardAdapter.OnMovieListener, ListCardAdapter.DeleteHelper, MovieDetailFragment.OnButtonListener{
    //Variable to store movies
    var movieList: ArrayList<RoomMovie> = ArrayList()

    //Variables for recycler view
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListCardAdapter
    lateinit var layoutManager: LinearLayoutManager

    //Variable for popup
    lateinit var popup: PopupWindow

    //Variable for database
    lateinit var database: RoomMovieDatabase

    //Variable for swipe function
    lateinit var swipe: SwipeToDelete
    lateinit var itemTouchHelper: ItemTouchHelper

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list)

        bottom_nav_list.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_search -> {
                    val sIntent = Intent(this@MyListActivity, MainActivity::class.java)
                    startActivity(sIntent)
                    true
                } R.id.item_my_list ->{
                    true
                } R.id.item_discover ->{
                    val sIntent = Intent(this@MyListActivity, DiscoverMoviesActivity::class.java)
                    startActivity(sIntent)
                    true
                }
                else -> false
            }
        }
        bottom_nav_list.selectedItemId = R.id.item_my_list

        recyclerView = findViewById(R.id.recycler_view_my_list)

        database = Room.databaseBuilder(applicationContext, RoomMovieDatabase::class.java, "data.db").allowMainThreadQueries().build()

        CoroutineScope(IO).launch {
            getList()
        }

//        if(database.DataDAO().getData() != null){
//            movieList = database.DataDAO().getData() as ArrayList<RoomMovie>
//
//            adapter = ListCardAdapter(movieList, this, this)
//            swipe = SwipeToDelete(adapter)
//
//            itemTouchHelper = ItemTouchHelper(swipe)
//            itemTouchHelper.attachToRecyclerView(recyclerView)
//
//            layoutManager = LinearLayoutManager(this)
//            recyclerView.layoutManager =  layoutManager
//            recyclerView.adapter = adapter
//        }

        button_clear_list.setOnClickListener {

            MaterialAlertDialogBuilder(this)
                .setTitle("Clear Movie List")
                .setMessage("Are you sure you want to delete?")
                .setNeutralButton("Cancel") { dialog, which ->
                    // Respond to neutral button press
                }
                .setPositiveButton("Clear") { dialog, which ->
                    CoroutineScope(IO).launch {
                        clearTable()
                    }
                }
                .show()

//            movieList = database.DataDAO().getData() as ArrayList<RoomMovie>
//
//            adapter = ListCardAdapter(ArrayList(), this, this)
//            layoutManager = LinearLayoutManager(this)
//            recyclerView.layoutManager = layoutManager
//            recyclerView.adapter = adapter
//
//            for (movie in movieList) database.DataDAO().delete(movie.roomMovieId)
        }
    }

    override fun onMovieClick(movie: RoomMovie) {
        MovieDetailFragment(movie, this, 2).apply {
            show(supportFragmentManager, tag)
        }
//        val customView:View = layoutInflater.inflate(R.layout.popup_movie, null)
//        popup = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        popup.isOutsideTouchable = true
//        popup.isFocusable = true
//        popup.setBackgroundDrawable(ColorDrawable(Color.LTGRAY))
//        popup.showAtLocation(relative_layout_activity_my_list, Gravity.CENTER, 0, 0)
//
//        val popupTextViewTitle: TextView = customView.findViewById(R.id.text_view_title) as TextView
//        val popupTextViewRating: TextView = customView.findViewById(R.id.text_view_rating) as TextView
//        val popupTextViewReleased: TextView = customView.findViewById(R.id.text_view_released) as TextView
//        val popupTextViewLanguage: TextView = customView.findViewById(R.id.text_view_language) as TextView
//        val popupTextViewDescription: TextView = customView.findViewById(R.id.text_view_description) as TextView
//        val buttonAddMovie: Button = customView.findViewById(R.id.button_add) as Button
//
//        buttonAddMovie.visibility = View.INVISIBLE
//
//        popupTextViewTitle.text = movie.title
//        popupTextViewRating.text = popupTextViewRating.text.toString() + movie.rating!!
//        popupTextViewReleased.text = popupTextViewReleased.text.toString() + movie.release!!
//        popupTextViewLanguage.text = popupTextViewLanguage.text.toString() + movie.language!!
//        popupTextViewDescription.text = popupTextViewDescription.text.toString() + "\n" + movie.description!!
    }

    override fun onSwipe(task: RoomMovie) {
//        database.DataDAO().delete(task.roomMovieId)

        CoroutineScope(IO).launch {
            deleteDb(task.roomMovieId)
        }
    }

    suspend fun deleteDb(movieId: Int){
        database.DataDAO().delete(movieId)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    suspend fun getList(){
        if(database.DataDAO().getData() != null){
            movieList = database.DataDAO().getData() as ArrayList<RoomMovie>

            withContext(Main){
                setList(movieList)
            }
        }
    }

    suspend fun clearTable(){
        for (movie in movieList) database.DataDAO().delete(movie.roomMovieId)

        withContext(Main){
            setList(ArrayList())
        }
    }

    fun setList(inList: ArrayList<RoomMovie>){
        adapter = ListCardAdapter(inList, this, this)
        swipe = SwipeToDelete(adapter)

        itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = adapter
    }

    override fun onButtonClick(movie: RoomMovie) {

    }
}
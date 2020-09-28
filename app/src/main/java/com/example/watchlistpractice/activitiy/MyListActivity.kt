package com.example.watchlistpractice.activitiy

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.support.ListCardAdapter
import com.example.watchlistpractice.support.RoomMovieDatabase
import com.example.watchlistpractice.support.SwipeToDelete
import kotlinx.android.synthetic.main.activity_my_list.relative_layout_activity_my_list
import kotlinx.android.synthetic.main.activity_my_list.button_clear_list

class MyListActivity : AppCompatActivity(), ListCardAdapter.OnMovieListener, ListCardAdapter.DeleteHelper {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list)

        recyclerView = findViewById(R.id.recycler_view_my_list)

        database = Room.databaseBuilder(applicationContext, RoomMovieDatabase::class.java, "data.db").allowMainThreadQueries().build()

        if(database.DataDAO().getData() != null){
            movieList = database.DataDAO().getData() as ArrayList<RoomMovie>

            adapter = ListCardAdapter(movieList, this, this)
            swipe = SwipeToDelete(adapter)

            itemTouchHelper = ItemTouchHelper(swipe)
            itemTouchHelper.attachToRecyclerView(recyclerView)

            layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager =  layoutManager
            recyclerView.adapter = adapter
        }

        button_clear_list.setOnClickListener {
            movieList = database.DataDAO().getData() as ArrayList<RoomMovie>

            adapter = ListCardAdapter(ArrayList(), this, this)
            layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter

            for (movie in movieList) database.DataDAO().delete(movie.roomMovieId)
        }
    }

    override fun onMovieClick(movie: RoomMovie) {
        val customView:View = layoutInflater.inflate(R.layout.popup_movie, null)
        popup = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popup.isOutsideTouchable = true
        popup.isFocusable = true
        popup.setBackgroundDrawable(ColorDrawable(Color.LTGRAY))
        popup.showAtLocation(relative_layout_activity_my_list, Gravity.CENTER, 0, 0)

        val popupTextViewTitle: TextView = customView.findViewById(R.id.text_view_title) as TextView
        val popupTextViewRating: TextView = customView.findViewById(R.id.text_view_rating) as TextView
        val popupTextViewReleased: TextView = customView.findViewById(R.id.text_view_released) as TextView
        val popupTextViewLanguage: TextView = customView.findViewById(R.id.text_view_language) as TextView
        val popuppTextViewDescription: TextView = customView.findViewById(R.id.text_view_description) as TextView
        val buttonAddMovie: Button = customView.findViewById(R.id.button_add) as Button

        buttonAddMovie.visibility = View.INVISIBLE

        popupTextViewTitle.text = movie.title
        popupTextViewRating.text = popupTextViewRating.text.toString() + movie.rating!!
        popupTextViewReleased.text = popupTextViewReleased.text.toString() + movie.release!!
        popupTextViewLanguage.text = popupTextViewLanguage.text.toString() + movie.language!!
        popuppTextViewDescription.text = popuppTextViewDescription.text.toString() + "\n" + movie.description!!
    }

    override fun onSwipe(task: RoomMovie) {
        database.DataDAO().delete(task.roomMovieId)
    }
}
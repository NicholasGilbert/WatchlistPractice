package com.example.watchlistpractice.activitiy

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
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
import com.example.watchlistpractice.data.ApiData
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.support.CardAdapter
import com.example.watchlistpractice.support.ListCardAdapter
import com.example.watchlistpractice.support.RoomMovieDatabase
import com.example.watchlistpractice.support.SwipeToDelete
import kotlinx.android.synthetic.main.activity_my_list.*

class MyListActivity : AppCompatActivity(), ListCardAdapter.OnMovieListener, ListCardAdapter.DeleteHelper {
    //Variable to store movies
    var mMovieList: ArrayList<RoomMovie> = ArrayList()

    //Variables for recycler view
    lateinit var mRecyclerView  : RecyclerView
    lateinit var mAdapter       : ListCardAdapter
    lateinit var mLayoutManager : LinearLayoutManager

    //Variable for database
    lateinit var mDatabase: RoomMovieDatabase

    //Variable for popup
    lateinit var mPopup: PopupWindow

    //Variable for swipe function
    lateinit var mSwipe             : SwipeToDelete
    lateinit var mItemTouchHelper   : ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list)

        mRecyclerView   = findViewById(R.id.recycler_view_my_list)

        mDatabase       = Room.databaseBuilder(applicationContext, RoomMovieDatabase::class.java, "data.db").allowMainThreadQueries().build()

        if(mDatabase.DataDAO().getData() != null){
            mMovieList  = mDatabase.DataDAO().getData() as ArrayList<RoomMovie>
            mAdapter    = ListCardAdapter(mMovieList, this, this)
            mSwipe      = SwipeToDelete(mAdapter)

            mItemTouchHelper = ItemTouchHelper(mSwipe)
            mItemTouchHelper.attachToRecyclerView(mRecyclerView)

            mLayoutManager              = LinearLayoutManager(this)
            mRecyclerView.layoutManager = mLayoutManager
            mRecyclerView.adapter       = mAdapter
        }

        button_clear_list.setOnClickListener {
            mMovieList                  = mDatabase.DataDAO().getData() as ArrayList<RoomMovie>

            mAdapter                    = ListCardAdapter(ArrayList(), this, this)
            mLayoutManager              = LinearLayoutManager(this)
            mRecyclerView.layoutManager = mLayoutManager
            mRecyclerView.adapter       = mAdapter

            for (movie in mMovieList) mDatabase.DataDAO().delete(movie.roomMovieId)
        }
    }

    override fun onMovieClick(pos: Int) {
        val sCustomView     :View           = layoutInflater.inflate(R.layout.popup_movie, null)
        mPopup                      = PopupWindow(sCustomView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mPopup.isOutsideTouchable   = true
        mPopup.isFocusable          = true
        mPopup.setBackgroundDrawable(ColorDrawable(Color.LTGRAY))
        mPopup.showAtLocation(relative_layout_activity_my_list, Gravity.CENTER, 0, 0)

        val movieHolder: RoomMovie = mMovieList.get(pos)

        val sPopupTextViewTitle         : TextView  = sCustomView.findViewById(R.id.text_view_title)        as TextView
        val sPopupTextViewRating        : TextView  = sCustomView.findViewById(R.id.text_view_rating)       as TextView
        val sPopupTextViewReleased      : TextView  = sCustomView.findViewById(R.id.text_view_released)     as TextView
        val sPopupTextViewLanguage      : TextView  = sCustomView.findViewById(R.id.text_view_language)     as TextView
        val sPopupTextViewDescription   : TextView  = sCustomView.findViewById(R.id.text_view_description)  as TextView
        val sButtonAddMovie             : Button    = sCustomView.findViewById(R.id.button_add)             as Button

        sButtonAddMovie.visibility      = View.INVISIBLE

        sPopupTextViewTitle.text        = movieHolder.title!!
        sPopupTextViewRating.text       = sPopupTextViewRating.text.toString() + movieHolder.rating!!
        sPopupTextViewReleased.text     = sPopupTextViewReleased.text.toString() + movieHolder.release!!
        sPopupTextViewLanguage.text     = sPopupTextViewLanguage.text.toString() + movieHolder.language!!
        sPopupTextViewDescription.text  = sPopupTextViewDescription.text.toString() + "\n" + movieHolder.description!!
    }

    override fun onSwipe(task: RoomMovie) {
        mDatabase.DataDAO().delete(task.roomMovieId)
    }
}
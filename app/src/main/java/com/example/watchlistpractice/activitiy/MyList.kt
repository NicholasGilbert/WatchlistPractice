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

class MyList : AppCompatActivity(), ListCardAdapter.OnMovieListener, ListCardAdapter.DeleteHelper {
    var movieList: ArrayList<RoomMovie> = ArrayList()

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListCardAdapter
    lateinit var layoutManager: LinearLayoutManager

    lateinit var db: RoomMovieDatabase

    lateinit var popup: PopupWindow

    lateinit var swipe: SwipeToDelete
    lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list)

        recyclerView = findViewById(R.id.my_list_recycler_view)

        db = Room.databaseBuilder(applicationContext, RoomMovieDatabase::class.java, "data.db").allowMainThreadQueries().build()
        if(db.DataDAO().getData() != null){
            movieList = db.DataDAO().getData() as ArrayList<RoomMovie>
            adapter = ListCardAdapter(movieList, this, this)
            swipe = SwipeToDelete(adapter)

            itemTouchHelper = ItemTouchHelper(swipe)
            itemTouchHelper.attachToRecyclerView(recyclerView)

            layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }

        btnClearList.setOnClickListener {
            movieList = db.DataDAO().getData() as ArrayList<RoomMovie>

            adapter = ListCardAdapter(ArrayList(), this, this)
            layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
//            recyclerView.removeAllViewsInLayout()

            for (movie in movieList){
                db.DataDAO().delete(movie.roomMovieId)
            }
        }
    }

    override fun onMovieClick(pos: Int) {
        val layoutInflater = this.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = layoutInflater.inflate(R.layout.movie_popup_layout, null)
        popup = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popup.isOutsideTouchable = true
        popup.isFocusable = true
        popup.setBackgroundDrawable(ColorDrawable(Color.LTGRAY))
        popup.showAtLocation(rlMyList, Gravity.CENTER, 0, 0)

        val movieHolder = movieList.get(pos)

        val mCtvTitle = customView.findViewById(R.id.MCtvTitle) as TextView
        val mCtvRating = customView.findViewById(R.id.MCtvRating) as TextView
        val mCtvReleased = customView.findViewById(R.id.MCtvReleased) as TextView
        val mCtvLanguage = customView.findViewById(R.id.MCtvLanguage) as TextView
        val mCtvDescription = customView.findViewById(R.id.MCtvDescription) as TextView
        val btnAddMovieToList = customView.findViewById(R.id.btnAddToList) as Button

        btnAddMovieToList.visibility = View.INVISIBLE

        mCtvTitle.text = movieHolder.title!!
        mCtvRating.text = mCtvRating.text.toString() + movieHolder.rating!!
        mCtvReleased.text = mCtvReleased.text.toString() + movieHolder.release!!
        mCtvLanguage.text = mCtvLanguage.text.toString() + movieHolder.language!!
        mCtvDescription.text = mCtvDescription.text.toString() + "\n" + movieHolder.description!!
    }

    override fun onSwipe(task: RoomMovie) {
        db.DataDAO().delete(task.roomMovieId)
    }
}
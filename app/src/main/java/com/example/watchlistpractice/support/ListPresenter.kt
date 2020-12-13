package com.example.watchlistpractice.support

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.watchlistpractice.R
import com.example.watchlistpractice.activitiy.list.MyListActivity
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.fragment.MovieDetailFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListPresenter (val act: MyListActivity): ListCardAdapter.OnMovieListener, ListCardAdapter.DeleteHelper {

    var movieList: ArrayList<RoomMovie> = ArrayList()

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListCardAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var database: RoomMovieDatabase

    lateinit var swipe: SwipeToDelete
    lateinit var itemTouchHelper: ItemTouchHelper

    fun setStatusColor(){
        act.window.statusBarColor = act.resources.getColor(R.color.colorPrimaryDark)
    }

    fun setRecycler(){
        recyclerView = act.findViewById(R.id.recycler_view_my_list)
    }

    fun setDatabase(){
        database = Room.databaseBuilder(act.applicationContext, RoomMovieDatabase::class.java, "data.db").build()
    }

    suspend fun getData(){
        movieList = ArrayList()
        if(database.DataDAO().getData() != null){
            movieList = database.DataDAO().getData() as ArrayList<RoomMovie>

            withContext(Dispatchers.Main){
                setList(movieList)
            }
        }
    }

    fun setList(inList: ArrayList<RoomMovie>){
        adapter = ListCardAdapter(inList, this, this)

        swipe = SwipeToDelete(adapter)

        itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        layoutManager = GridLayoutManager(act, 2)
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = adapter
    }

    override fun onMovieClick(movie: RoomMovie) {
        MovieDetailFragment(movie, act, 2).apply {
            show(act.supportFragmentManager, tag)
        }
    }

    fun clearDialog() {
        MaterialAlertDialogBuilder(act)
            .setTitle("Clear Movie List")
            .setMessage("Are you sure you want to delete?")
            .setNeutralButton("Cancel") { dialog, which ->
                // Respond to neutral button press
            }
            .setPositiveButton("Clear") { dialog, which ->
                CoroutineScope(Dispatchers.IO).launch {
                    clearTable()
                }
            }
            .show()
    }

    suspend fun clearTable(){
        for (movie in movieList) database.DataDAO().delete(movie.roomMovieId)

        withContext(Dispatchers.Main){
            setList(ArrayList())
        }
    }

    override fun onSwipe(task: RoomMovie) {
        CoroutineScope(Dispatchers.IO).launch {
            deleteDb(task.roomMovieId)
        }
    }

    suspend fun deleteDb(movieId: Int){
        database.DataDAO().delete(movieId)
    }
}
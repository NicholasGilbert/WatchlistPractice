package com.example.watchlistpractice.activitiy.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.watchlistpractice.R
import com.example.watchlistpractice.activitiy.list.MyListActivity
import com.example.watchlistpractice.activitiy.discover.DiscoverMoviesActivity
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.fragment.MovieDetailFragment
import com.example.watchlistpractice.support.adapter.ListCardAdapter
import com.example.watchlistpractice.support.database.RoomMovieDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MovieDetailFragment.OnButtonListener, MainContract.View {

    internal lateinit var presenter: MainContract.Presenter

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListCardAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var database: RoomMovieDatabase

    var movieList: MutableList<RoomMovie> = mutableListOf()

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
            }
            R.id.item_my_list -> {
                val sIntent = Intent(this@MainActivity, MyListActivity::class.java)
                startActivity(sIntent)
                return true
            }
            R.id.item_discover -> {
                val sIntent = Intent(this@MainActivity, DiscoverMoviesActivity::class.java)
                startActivity(sIntent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view_main)

        setDatabase()

        setPresenter(MainPresenter(this, database))

        presenter.actSetList()



        bottom_nav_main.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main_item_my_list -> {
                    val sIntent = Intent(this@MainActivity, MyListActivity::class.java)
                    startActivity(sIntent)
                    true
                }
                R.id.main_item_discover -> {
                    val sIntent = Intent(this@MainActivity, DiscoverMoviesActivity::class.java)
                    startActivity(sIntent)
                    true
                }
                else -> false
            }
        }

        fab_search_main.setOnClickListener {
            if (search_edit_text.text.toString() != "") {
                val inSearch: String = search_edit_text.text.toString()
                CoroutineScope(IO).launch {
                    presenter.onSearch(inSearch)
                    updateAdapter()
                }
            }
        }
    }

    override fun setDatabase() {
        database = Room.databaseBuilder(this, RoomMovieDatabase::class.java, "data.db").build()
    }

    override fun onButtonClick(movie: RoomMovie) {
        CoroutineScope(IO).launch {
            presenter.addData(movie)
        }
    }

    override fun setList(
        inMovieListener: ListCardAdapter.OnMovieListener,
        inDelete: ListCardAdapter.DeleteHelper
    ) {
        adapter = ListCardAdapter(movieList, inMovieListener, inDelete)

        layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun onMovieClick(movie: RoomMovie) {
        MovieDetailFragment(movie, this, 1).apply {
            show(this@MainActivity.supportFragmentManager, tag)
        }
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun updateList(inMovieList: MutableList<RoomMovie>) {
        movieList.clear()
        for (movie in inMovieList) {
            movieList.add(movie)
        }

//        updataAdapter()
    }

    fun updateAdapter() {
        runOnUiThread {
            Toast.makeText(applicationContext, movieList.toString(), Toast.LENGTH_SHORT).show()
            adapter.notifyDataSetChanged()
        }
    }
}


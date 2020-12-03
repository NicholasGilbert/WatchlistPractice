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
import com.example.watchlistpractice.fragment.MovieDetailFragment
import com.example.watchlistpractice.support.*
import kotlinx.android.synthetic.main.activity_discover_movies.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.OPTIONS

class DiscoverMoviesActivity : AppCompatActivity(), MovieDetailFragment.OnButtonListener {

    val presenter: DiscoverPresenter = DiscoverPresenter(this)

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

        presenter.setStatusColor()

//        presenter.setToolbar()

        bottom_nav_dis.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.dis_item_search -> {
                    val sIntent = Intent(this@DiscoverMoviesActivity, MainActivity::class.java)
                    startActivity(sIntent)
                    true
                } R.id.dis_item_my_list ->{
                val sIntent = Intent(this@DiscoverMoviesActivity, MyListActivity::class.java)
                startActivity(sIntent)
                true
                }
                else -> false
            }
        }

        presenter.setRecycler()

        presenter.setDatabase()

        presenter.setDropdownSpinner()

        button_search_genre.setOnClickListener {
            presenter.getData()
        }

        fab_search_dis.setOnClickListener {
            presenter.getData()
        }
    }

    override fun onButtonClick(movie: RoomMovie) {
        CoroutineScope(Dispatchers.IO).launch {
            presenter.addData(movie)
        }
    }
}
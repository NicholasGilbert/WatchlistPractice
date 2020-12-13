package com.example.watchlistpractice.activitiy.discover

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import com.example.watchlistpractice.R
import com.example.watchlistpractice.activitiy.main.MainActivity
import com.example.watchlistpractice.activitiy.list.MyListActivity
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.fragment.MovieDetailFragment
import com.example.watchlistpractice.support.*
import kotlinx.android.synthetic.main.activity_discover_movies.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
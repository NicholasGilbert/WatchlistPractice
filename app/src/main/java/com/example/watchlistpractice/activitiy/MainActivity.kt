package com.example.watchlistpractice.activitiy

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.PopupWindow
import androidx.recyclerview.widget.GridLayoutManager
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.ApiData
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.fragment.MovieDetailFragment
import com.example.watchlistpractice.support.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), MovieDetailFragment.OnButtonListener {

    val presenter: MainPresenter = MainPresenter(this)

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
            } R.id.item_my_list ->{
                val sIntent = Intent(this@MainActivity, MyListActivity::class.java)
                startActivity(sIntent)
                return true
            } R.id.item_discover ->{
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

        presenter.setStatusColor()

        presenter.setToolbar()

        bottom_nav_main.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main_item_my_list ->{
                    val sIntent = Intent(this@MainActivity, MyListActivity::class.java)
                    startActivity(sIntent)
                    true
                } R.id.main_item_discover ->{
                    val sIntent = Intent(this@MainActivity, DiscoverMoviesActivity::class.java)
                    startActivity(sIntent)
                    true
                }
                else -> false
            }
        }

        presenter.setRecycler()

        presenter.setDatabase()

        fab_search_main.setOnClickListener {
            if (search_edit_text.text.toString() != "") {
                val inSearch: String = search_edit_text.text.toString()
                CoroutineScope(IO).launch {
                    presenter.getData(inSearch)
                }
            }
        }
    }

    override fun onButtonClick(movie: RoomMovie) {
        CoroutineScope(IO).launch {
            presenter.addData(movie)
        }
    }
}
package com.example.watchlistpractice.activitiy.list

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.watchlistpractice.R
import com.example.watchlistpractice.activitiy.discover.DiscoverMoviesActivity
import com.example.watchlistpractice.activitiy.main.MainActivity
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.fragment.MovieDetailFragment
import com.example.watchlistpractice.support.*
import kotlinx.android.synthetic.main.activity_my_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MyListActivity : AppCompatActivity(), MovieDetailFragment.OnButtonListener{
    val presenter: ListPresenter = ListPresenter(this)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list)

        bottom_nav_list.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.list_item_search -> {
                    val sIntent = Intent(this@MyListActivity, MainActivity::class.java)
                    startActivity(sIntent)
                    true
                }R.id.list_item_discover ->{
                    val sIntent = Intent(this@MyListActivity, DiscoverMoviesActivity::class.java)
                    startActivity(sIntent)
                    true
                }
                else -> false
            }
        }

        presenter.setStatusColor()

        presenter.setRecycler()

        presenter.setDatabase()

        CoroutineScope(IO).launch {
            presenter.getData()
        }

        fab_del_list.setOnClickListener {
            presenter.clearDialog()
        }
    }

    override fun onButtonClick(movie: RoomMovie) {
        TODO("Not yet implemented")
    }
}
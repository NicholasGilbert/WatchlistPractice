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
import com.example.watchlistpractice.support.*
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
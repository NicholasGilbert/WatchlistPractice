package com.example.watchlistpractice

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watchlistpractice.data.ApiData
import com.example.watchlistpractice.support.CardAdapter
import com.example.watchlistpractice.support.RetrofitInterface
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.movie_card_layout.*
import kotlinx.android.synthetic.main.movie_popup_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), CardAdapter.OnMovieListener{
    private val retrofitInterface by lazy{
        RetrofitInterface.create()
    }

    var movieList: ArrayList<ApiData.ResultsItem> = ArrayList()

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CardAdapter
    lateinit var layoutManager: LinearLayoutManager

    lateinit var popup: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)


        btnSearch.setOnClickListener {
            if (etSearch.text.toString() != "") {
                val inSearch = etSearch.text.toString()
                val call = retrofitInterface.findMovie(inSearch)
                val res = call!!.enqueue(object : Callback<ApiData.Response> {
                    override fun onFailure(call: Call<ApiData.Response>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<ApiData.Response>,
                        response: Response<ApiData.Response>
                    ) {
                        movieList = response.body()!!.results!!
                        refreshList()
                    }

                })
            }
        }
    }

    fun refreshList(){
        adapter = CardAdapter(movieList, this)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = adapter
    }

    override fun onMovieClick(pos: Int) {
        val layoutInflater = this.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = layoutInflater.inflate(R.layout.movie_popup_layout, null)
        popup = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popup.isOutsideTouchable = true
        popup.isFocusable = true
        popup.setBackgroundDrawable(ColorDrawable(Color.LTGRAY))
        popup.showAtLocation(rlMain, Gravity.CENTER, 0, 0)

        val movieHolder = movieList.get(pos)

        val mCtvTitle = customView.findViewById(R.id.MCtvTitle) as TextView
        val mCtvRating = customView.findViewById(R.id.MCtvRating) as TextView
        val mCtvReleased = customView.findViewById(R.id.MCtvReleased) as TextView
        val mCtvLanguage = customView.findViewById(R.id.MCtvLanguage) as TextView
        val mCtvDescription = customView.findViewById(R.id.MCtvDescription) as TextView
//
        mCtvTitle.text = movieHolder.title!!
        mCtvRating.text = mCtvRating.text.toString() + movieHolder.vote_average!!
        mCtvReleased.text = mCtvReleased.text.toString() + movieHolder.release_date!!
        mCtvLanguage.text = mCtvLanguage.text.toString() + movieHolder.original_language!!
        mCtvDescription.text = mCtvDescription.text.toString() + "\n" + movieHolder.overview!!
    }
}
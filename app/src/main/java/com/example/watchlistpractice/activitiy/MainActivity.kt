package com.example.watchlistpractice.activitiy

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.ApiData
import com.example.watchlistpractice.data.RoomMovie
import com.example.watchlistpractice.support.CardAdapter
import com.example.watchlistpractice.support.RetrofitInterface
import com.example.watchlistpractice.support.RoomMovieDatabase
import kotlinx.android.synthetic.main.activity_main.*
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

    lateinit var db: RoomMovieDatabase

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_layout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.movieSearchMenu -> {
                Toast.makeText(applicationContext, "click on movie search", Toast.LENGTH_LONG).show()
                true
            }
            R.id.myListMenu ->{
                val intent = Intent(this@MainActivity, MyList::class.java)
                startActivity(intent)
                Toast.makeText(applicationContext, "click on my list" +
                        "", Toast.LENGTH_LONG).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.main_recycler_view)

        db = Room.databaseBuilder(applicationContext, RoomMovieDatabase::class.java, "data.db").allowMainThreadQueries().build()

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

        addData(movieHolder)
    }

    fun addData(inMovie: ApiData.ResultsItem){
        var checker = true

        for(movie in db.DataDAO().getData()){
            if (inMovie.id == movie.roomMovieId){
                checker = false
            }
        }
        if (checker == true) {
            db.DataDAO().insert(RoomMovie(  inMovie.id!!,
                                            inMovie.title!!,
                                            inMovie.vote_average!!,
                                            inMovie.release_date!!,
                                            inMovie.original_language!!,
                                            inMovie.overview!!)
            )
        }
    }
}
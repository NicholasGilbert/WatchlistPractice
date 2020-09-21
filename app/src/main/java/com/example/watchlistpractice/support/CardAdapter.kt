package com.example.watchlistpractice.support

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.ApiData

class CardAdapter(val data: ArrayList<ApiData.ResultsItem>, val onMovieListener: OnMovieListener) : RecyclerView.Adapter<CardAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAdapter.MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.movie_card_layout, parent, false)
        return MovieViewHolder(view, onMovieListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CardAdapter.MovieViewHolder, position: Int) {
        holder.txtTitle.text = data.get(position).title
        holder.txtRating.text = "Rating of " + data.get(position).vote_average.toString() + "/10"
        holder.txtRelease.text = "Released on : " + data.get(position).release_date
    }

    class MovieViewHolder(itemView: View, val onMovieListener: OnMovieListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val txtTitle = itemView.findViewById(R.id.tvTitle) as TextView
        val txtRating = itemView.findViewById(R.id.tvRating) as TextView
        val txtRelease = itemView.findViewById(R.id.tvRelease) as TextView
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            onMovieListener.onMovieClick(adapterPosition)
        }
    }

    interface OnMovieListener{
        fun onMovieClick(pos: Int)
    }
}
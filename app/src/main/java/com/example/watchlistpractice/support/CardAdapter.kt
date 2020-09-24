package com.example.watchlistpractice.support

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.ApiData

class CardAdapter(val sData: ArrayList<ApiData.ResultsItem>, val sOnMovieListener: OnMovieListener) : RecyclerView.Adapter<CardAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val sLayoutInflater : LayoutInflater    = LayoutInflater.from(parent.context)
        val sView           : View              = sLayoutInflater.inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(sView, sOnMovieListener)
    }

    override fun getItemCount(): Int {
        return sData.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.sTextViewTitle.text    = sData.get(position).title
        holder.sTextViewRating.text   = "Rating of " + sData.get(position).vote_average.toString() + "/10"
        holder.sTextViewRelease.text  = "Released on : " + sData.get(position).release_date
    }

    class MovieViewHolder(itemView: View, val sOnMovieListener: OnMovieListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val sTextViewTitle    = itemView.findViewById(R.id.text_view_card_title)  as TextView
        val sTextViewRating   = itemView.findViewById(R.id.text_view_card_rating) as TextView
        val sTextViewRelease  = itemView.findViewById(R.id.text_view_card_release)as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            sOnMovieListener.onMovieClick(adapterPosition)
        }
    }

    interface OnMovieListener{
        fun onMovieClick(pos: Int)
    }
}
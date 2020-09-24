package com.example.watchlistpractice.support

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.RoomMovie

class ListCardAdapter(val sData: ArrayList<RoomMovie>, val sOnMovieListener: OnMovieListener, val sSwipeListener: DeleteHelper) : RecyclerView.Adapter<ListCardAdapter.MovieViewHolder>() {
    interface DeleteHelper{
        fun onSwipe(task: RoomMovie)
    }

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
        holder.sTextViewRating.text   = "Rating of " + sData.get(position).rating.toString() + "/10"
        holder.sTextViewRelease.text  = "Released on : " + sData.get(position).release
    }

    class MovieViewHolder(itemView: View, val sOnMovieListener: OnMovieListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val sTextViewTitle    = itemView.findViewById(R.id.text_view_card_title) as TextView
        val sTextViewRating   = itemView.findViewById(R.id.text_view_card_rating) as TextView
        val sTextViewRelease  = itemView.findViewById(R.id.text_view_card_release) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            sOnMovieListener.onMovieClick(adapterPosition)
        }
    }

    fun deleteItem(pos: Int){
        sSwipeListener.onSwipe(sData.get(pos))
        sData.removeAt(pos)
    }

    interface OnMovieListener{
        fun onMovieClick(pos: Int)
    }
}
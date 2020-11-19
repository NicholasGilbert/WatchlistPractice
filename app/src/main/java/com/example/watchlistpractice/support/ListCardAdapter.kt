package com.example.watchlistpractice.support

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.RoomMovie

class ListCardAdapter(val data: ArrayList<RoomMovie>, val onMovieListener: ListCardAdapter.OnMovieListener, val swipeListener: DeleteHelper) : RecyclerView.Adapter<ListCardAdapter.MovieViewHolder>() {
    interface DeleteHelper{
        fun onSwipe(task: RoomMovie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val sLayoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val sView: View = sLayoutInflater.inflate(R.layout.item_card, parent, false)
        return MovieViewHolder(data, sView, onMovieListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.sTextViewTitle.text = data.get(position).title
        holder.sTextViewRating.text = "Rating of " + data.get(position).rating.toString() + "/10"

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500"+data.get(position).poster)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.sImageViewPoster)
    }

    fun deleteItem(pos: Int){
        swipeListener.onSwipe(data.get(pos))
        data.removeAt(pos)
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, data.size);
    }

    interface OnMovieListener{
        fun onMovieClick(movie: RoomMovie)
    }

    class MovieViewHolder(val sData: ArrayList<RoomMovie>, itemView: View, val sOnMovieListener: ListCardAdapter.OnMovieListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val sImageViewPoster = itemView.findViewById(R.id.image_view_card) as ImageView
        val sTextViewTitle = itemView.findViewById(R.id.text_view_card_title) as TextView
        val sTextViewRating = itemView.findViewById(R.id.text_view_card_rating) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            sOnMovieListener.onMovieClick(sData.get(adapterPosition))
        }
    }
}
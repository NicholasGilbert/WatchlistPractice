package com.example.watchlistpractice.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.RoomMovie
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.popup_movie.*

class MovieDetailFragment(val movie: RoomMovie, val onButtonListener: OnButtonListener, val use: Int) : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.popup_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        text_view_title.text = movie.title
        text_view_rating.text = text_view_rating.text.toString() + movie.rating.toString()
        text_view_released.text = text_view_released.text.toString() + movie.release
        text_view_language.text = text_view_language.text.toString() + movie.language
        text_view_description.text = text_view_description.text.toString() + movie.description

        if(use == 2){
            button_add.visibility = View.INVISIBLE
        }

        button_add.setOnClickListener {
            onButtonListener.onButtonClick(movie)
            dismiss()
        }

        Glide.with(super.getContext()!!)
            .load("https://image.tmdb.org/t/p/w500"+"/zlyhKMi2aLk25nOHnNm43MpZMtQ.jpg")
            .onlyRetrieveFromCache(true)
            .into(image_view_poster)
    }

    interface OnButtonListener{
        fun onButtonClick(movie: RoomMovie)
    }
}
package com.example.watchlistpractice.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.Movie
import com.example.watchlistpractice.data.RoomMovie
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.popup_movie.*

class MovieDetailFragment(val movie: Movie, val onButtonListener: OnButtonListener) : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.popup_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        text_view_title.text = movie.title
        text_view_rating.text = text_view_rating.text.toString() + movie.rating.toString()
        text_view_released.text = text_view_released.text.toString() + movie.releaseDate
        text_view_language.text = text_view_language.text.toString() + movie.language
        text_view_description.text = text_view_description.text.toString() + movie.description

        button_add.setOnClickListener {
            onButtonListener.onButtonClick(movie)
            dismiss()
        }
    }

    interface OnButtonListener{
        fun onButtonClick(movie: Movie)
    }
}
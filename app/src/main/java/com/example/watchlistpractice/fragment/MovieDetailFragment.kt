package com.example.watchlistpractice.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.watchlistpractice.R
import com.example.watchlistpractice.data.RoomMovie
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.popup_movie.*

class MovieDetailFragment(
    val movie: RoomMovie,
    val onButtonListener: OnButtonListener,
    val use: Int
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popup_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_title.text = movie.title
        tv_rating.text = tv_rating.text.toString() + movie.rating.toString()
        tv_released.text = tv_released.text.toString() + movie.release
        tv_language.text = tv_language.text.toString() + movie.language
        tv_description.text = tv_description.text.toString() + movie.description

        if (use == 2) {
            button_add.visibility = View.INVISIBLE
        }

        button_add.setOnClickListener {
            onButtonListener.onButtonClick(movie)
            dismiss()
        }

        Glide.with(super.getContext()!!)
            .load("https://image.tmdb.org/t/p/w500" + "/zlyhKMi2aLk25nOHnNm43MpZMtQ.jpg")
            .onlyRetrieveFromCache(true)
            .into(iv_poster)
    }

    interface OnButtonListener {
        fun onButtonClick(movie: RoomMovie)
    }
}
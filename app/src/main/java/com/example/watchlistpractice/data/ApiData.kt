package com.example.watchlistpractice.data


object ApiData {
    data class Response(val page: Int? = null,
                        val totalPages: Int? = null,
                        val results: ArrayList<ResultsItem>? = null,
                        val totalResults: Int? = null)

    data class ResultsItem (val overview: String? = null,
                            val original_language: String? = null,
                            val originalTitle: String? = null,
                            val video: Boolean? = null,
                            val title: String? = null,
                            val genreIds: List<Int?>? = null,
                            val posterPath: String? = null,
                            val backdropPath: String? = null,
                            val release_date: String? = null,
                            val popularity: Double? = null,
                            val vote_average: Double? = null,
                            val id: Int? = null,
                            val adult: Boolean? = null,
                            val voteCount: Int? = null)
}
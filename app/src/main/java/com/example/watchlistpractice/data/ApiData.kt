package com.example.watchlistpractice.data


object ApiData {
    data class Response(val page: Int? = null,
                        val totalPages: Int? = null,
                        val results: ArrayList<ResultsItem>? = null,
                        val totalResults: Int? = null)

    data class ResultsItem (val overview: String = "-",
                            val original_language: String = "-",
                            val originalTitle: String = "-",
                            val video: Boolean = false,
                            val title: String = "-",
                            val genreIds: List<Int?> = listOf(0),
                            val posterPath: String = "-",
                            val backdropPath: String = "-",
                            val release_date: String = "-",
                            val popularity: Double = 0.0,
                            val vote_average: Double = 0.0,
                            val id: Int = 0,
                            val adult: Boolean = false,
                            val voteCount: Int = 0)
}
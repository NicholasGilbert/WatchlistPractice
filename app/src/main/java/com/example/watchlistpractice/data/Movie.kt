package com.example.watchlistpractice.data

class Movie (builder: Builder){
    val description: String = builder.description
    val language: String = builder.language
    val title: String = builder.title
    val releaseDate: String = builder.releaseDate
    val rating: Double = builder.rating
    val movieId: Int = builder.movieId

    class Builder(){
        var description: String = "-"
        var language: String = "-"
        var title: String = "-"
        var releaseDate: String = "-"
        var rating: Double = 0.0
        var movieId: Int = 0

        fun setDescription(input: String): Builder {
            this.description = input
            return this
        }

        fun setLanguage(input: String): Builder {
            this.language = input
            return this
        }

        fun setTitle(input: String): Builder {
            this.title = input
            return this
        }

        fun setReleaseDate(input: String): Builder {
            this.releaseDate = input
            return this
        }

        fun setRating(input: Double): Builder {
            this.rating = input
            return this
        }

        fun setMovieId(input: Int): Builder {
            this.movieId = input
            return this
        }

        fun create(): Movie {
            return Movie(this)
        }
    }
}
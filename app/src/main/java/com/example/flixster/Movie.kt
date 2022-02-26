package com.example.flixster

import org.json.JSONArray

// represents 1 movie object that displays in our UI
// define what attributes of the movie we want to parse out

data class Movie (
    val movieId: Int,
    private val posterPath: String,
    val title: String,
    val overview: String,
    ) {
    val posterImageUrl = " https://image.tmdb.org/t/p/w342/$posterPath"
    companion object {
        // allows us to call methods without a movie class
        // return type is a list of movie
        fun fromJsonArray(movieJsonArray: JSONArray) : List<Movie> {
            val movies = mutableListOf<Movie>()
            for (i in 0 until movieJsonArray.length()) {
                val movieJson = movieJsonArray.getJSONObject(i)
                movies.add(
                    Movie(
                        movieJson.getInt("id"),
                        movieJson.getString("poster_path"),
                        movieJson.getString("title"),
                        movieJson.getString("overview")
                    )
                )
            }
            return movies
        }
    }
}


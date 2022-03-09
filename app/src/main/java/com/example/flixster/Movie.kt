package com.example.flixster
// purpose of movie class is to represent one movie object that displays in UI
// bundle up pieces of data - title, overview, image url
// declare attributes in data type
// find attributes that we actually want to display
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.json.JSONArray

@Parcelize
data class Movie (
    val movieId: Int,
    val voteAverage : Double,
    private val posterPath: String,
    val title: String,
    val overview: String,
    ) : Parcelable {
    @IgnoredOnParcel
    // default width
    val posterImageUrl = "https://image.tmdb.org/t/p/w342/$posterPath"
    // add a companion object, which allows us to call methods on movie class
    // without an instance of movie
    companion object {
        fun fromJsonArray(movieJsonArray: JSONArray) : List<Movie> {
            // iterate through array and return list of movies
            val movies = mutableListOf<Movie>()
            for (i in 0 until movieJsonArray.length()) {
                val movieJson = movieJsonArray.getJSONObject(i)
                movies.add(
                    Movie(
                        movieJson.getInt("id"),
                        movieJson.getDouble("vote_average"),
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


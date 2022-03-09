package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "MainActivity"
private const val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
// if need to check whether onFailure is working, add _ERROR to the end of api key
// private const val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed_ERROR"
class MainActivity : AppCompatActivity() {

    // define a private instance variable
    private val movies = mutableListOf<Movie>()

    private lateinit var rvMovies: RecyclerView


    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMovies = findViewById(R.id.rvMovies)

        val movieAdapter = MovieAdapter(this, movies)
        rvMovies.adapter = movieAdapter
        rvMovies.layoutManager = LinearLayoutManager(this)

        val client = AsyncHttpClient()
        // params: const val, response handler (an object)
        client.get(NOW_PLAYING_URL, object: JsonHttpResponseHandler()  {
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                // log out the result of whatever happened in callback func
                // log.e is logging at the error level because it's onFailure
                // first param is a tag (class name), second is a message, status code
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                // log.i is at the info level
                // want it to print out the json data
                Log.i(TAG, "onSuccess: JSON data $json")
                // pass in the key to the desired json key
                // movieJsonArray represents list of movies

                try {
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    movies.addAll(Movie.fromJsonArray(movieJsonArray))  // cld throw json exception; use try/catch
                    movieAdapter.notifyDataSetChanged()
                    Log.i(TAG, "Movie List $movies")   //  log out parsed movies
                }
                catch (e: JSONException) {
                    Log.e(TAG, "Encountered exception $e")
                }
            }
        })
    }
}

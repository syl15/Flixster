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

class MainActivity : AppCompatActivity() {
    private val movies = mutableListOf<Movie>()
    private lateinit var rvMovies: RecyclerView

    // 1. define data model class as the data source
    // 2. add the recycler view to the layout
    // 3. create a custom row layout xml file to visualize the item
    // 4. create an adapter and view holder to render the item
    // 5. bind the adapter to the data source to populate the recycler view
    // 6. bind a layout manager to the recycler view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // first param is context (this means main activity)
        val movieAdapter = MovieAdapter(this, movies )
        rvMovies = findViewById(R.id.rvMovies)
        rvMovies.adapter = movieAdapter
        rvMovies.layoutManager = LinearLayoutManager( this)


        val client = AsyncHttpClient()
        // response handler depends on data returned from API


        client.get(NOW_PLAYING_URL, object: JsonHttpResponseHandler() {
            override fun onFailure(
                // can put the parameters all on one line
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                // log e is log at error level so something has gone wrong
               Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess: JSON data $json")
                // use a try catch in case something crashes
                try {
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    movies.addAll(Movie.fromJsonArray(movieJsonArray))
                    movieAdapter.notifyDataSetChanged()
                    Log.i(TAG, "Movie List $movies")

                } catch (e: JSONException) {
                    Log.e(TAG, "Encountered exception $e")
                }

            }

            })
    }
}

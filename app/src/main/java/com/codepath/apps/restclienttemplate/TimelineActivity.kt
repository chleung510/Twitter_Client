package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

class TimelineActivity : AppCompatActivity() {

    lateinit var client: TwitterClient
    lateinit var rvTweets: RecyclerView
    lateinit var adapter: TweetsAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    val tweets = ArrayList<Tweet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client = TwitterApplication.getRestClient(this)
        swipeContainer = findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "Refreshing timeline")
            populateHomeTimeline()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(tweets)
        rvTweets.layoutManager = LinearLayoutManager(this)
        rvTweets.adapter = adapter

        populateHomeTimeline()
    }

    // Inflate menu_main
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Handle clicks of different menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // if menu icon is compose icon...
        if (item.itemId == R.id.compose) {
            // Navigate to compose activity
            val intent =  Intent(this, ComposeActivity::class.java)
            // 1. indicates that we are starting a new activity but also expecting a result from there
            // 2. REQUEST_CODE could be value that we be used for later activity(onActivityResult),
            // and telling android studio which activity we came from.
            startActivityForResult(intent, REQUEST_CODE)
        }
        return super.onOptionsItemSelected(item)
    }

    // This activity will be called when we came back from compose activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        //if result is ok and request code matches
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            // Get data from our intent (our tweet)
            // "tweet" is the key we use in Compose activity with the Parcellable object
            // and we get Parcellable object (Tweet)
            val tweet = data?.getParcelableArrayExtra("tweet") as Tweet

            // Update timeline
            // Modifying the data source of tweets
            tweets.add(0, tweet)

            // Update adapter
            adapter.notifyItemInserted(0)
            rvTweets.smoothScrollToPosition(0)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun populateHomeTimeline() {
        client.getHomeTimeline(object: JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "onSuccess! ")

                try{
                    // For the purpose of clearing out tweets we currently fetched
                    adapter.clear()
                    val jsonArray = json.jsonArray
                    val listOfNewTweetsRetrieved = Tweet.fromJsonArray(jsonArray)
                    tweets.addAll(listOfNewTweetsRetrieved)
                    adapter.notifyDataSetChanged()
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false)
                } catch (e: JSONException) {
                    Log.e(TAG, "JSON Exception $e")
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "onFailure! $statusCode")
            }
        })
    }
    companion object {
        val TAG = "TimeLineActivity"
        val REQUEST_CODE = 10
    }
}
package com.codepath.apps.restclienttemplate

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient
    lateinit var tvCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        client = TwitterApplication.getRestClient(this)
        tvCount = findViewById(R.id.tvWordCount)

        // Handling the user's click on the tweet button
        btnTweet.setOnClickListener {
            // Grab the content of editText (etCompose)
            val tweetContent = etCompose.text.toString()

            // 1. Make sure the tweet isn't empty
            if (tweetContent.isEmpty()) {
                // Third param is to control how long the toast message last on screen.
                Toast.makeText(this, "Empty tweet is not allowed!", Toast.LENGTH_SHORT).show()
            }
            // 2. Make sure the tweet is under character count
            if (tweetContent.length > 140) {
                Toast.makeText(this, "Tweet is too long! Limited to 140 characters!", Toast.LENGTH_SHORT).show()
            } else {
                client.publishTweet(tweetContent, object: JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        Log.i(TAG, "You have successfully tweeted")
                        // To send the new tweet back to TimelineActivity without making another API call

                        // Parcellable object
                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent =  Intent()
                        // putting the data we want to pass in as we switch our activity.
                        intent.putExtra("tweet", tweet) // tweet is a parcellable object
                        setResult(RESULT_OK, intent) // signaling the result is ok alone with intent
                        finish() // for closing out composeActivity and going back to timeLineActivity
                    }
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish tweet", throwable)
                    }
                })
            }
        }

        etCompose.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)
                if (s.length > 280) {
                    // Making the character red or disable the submit btn
                    Toast.makeText(applicationContext, "Maximum Limit Reached", Toast.LENGTH_SHORT)
                        .show()
                    btnTweet.isClickable = false
                    tvCount.setTextColor(Color.RED)
                } else {
                    val countLeft = 280 - count
                    val countInString = countLeft.toString()
                    btnTweet.isClickable = true
                    tvCount.setTextColor(Color.BLACK)
                    tvCount.setText("You have  $countInString characters left out of 280.")
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

            override fun afterTextChanged(s: Editable) { }
        })
    }
    companion object {
        val TAG = "ComposeActivity"
    }
}
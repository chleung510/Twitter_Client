package com.codepath.apps.restclienttemplate.models

import com.codepath.apps.restclienttemplate.TimeFormatter
import org.json.JSONArray
import org.json.JSONObject

class Tweet {

    var body: String = ""
    var createdAt: String = ""
    var user: User? = null

    // to build a tweet object based on json object
    companion object {
        fun fromJson(jsonObject: JSONObject): Tweet {
            val tweet = Tweet()

            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))
            return tweet
        }

        //For populating tweets(25 by default) into an array of Tweets, and returns the list of tweets.
        fun fromJsonArray(jsonArray: JSONArray): List<Tweet> {
            val tweets = ArrayList<Tweet>()

            for (i in 0 until jsonArray.length()){
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets
        }

        fun getFormattedTimestamp(timeStamp: TimeFormatter, jsonObject: JSONObject): String {
            val createdAt = jsonObject.getString("created_at");
            return TimeFormatter.getTimeDifference(createdAt)
        }
    }
}
package com.codepath.apps.restclienttemplate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet

class TweetsAdapter(val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    // For inflating the layout that we want to use with our views.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        val context = parent.context
        val inflator = LayoutInflater.from(context)

        // Inflate our item layout
        val view = inflator.inflate(R.layout.item_tweet, parent, false)

        return ViewHolder(view)
    }

    // Populating data into the items thru holder
    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {
        // Get the data model based on the position.
        val tweet: Tweet = tweets.get(position)

        // Set item views based on views and data model.
        holder.tvUserName.text = tweet.user?.name // the ? is in case for user object that is null
        holder.tvTweetBody.text = tweet.body
        holder.tvTimeStamp.text = tweet.getFormattedTimestamp()


        Glide.with(holder.itemView).load(tweet.user?.publicImageUrl).into(holder.ivProfileImage)
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }



// Add a list of items -- change to type used

    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

    // For showing data in specific way...
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val tvUserName = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvTweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)
        val tvTimeStamp = itemView.findViewById<TextView>(R.id.tvTimeStamp)
    }
}
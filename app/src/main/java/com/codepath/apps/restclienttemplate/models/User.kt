package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

// variables are defined as constructors and return type is Paracelable object.
@Parcelize
class User(var name: String = "",
           var screenName: String = "",
           var publicImageUrl: String = "") : Parcelable{

    companion object {
        // to build an user object based on json object
        fun fromJson(jsonObject: JSONObject):User {
            val user = User()

            user.name = jsonObject.getString("name")
            user.screenName = jsonObject.getString("screen_name")
            user.publicImageUrl = jsonObject.getString("profile_image_url_https")

            return user
        }
    }
}
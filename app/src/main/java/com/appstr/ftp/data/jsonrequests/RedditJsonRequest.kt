package com.appstr.ftp.data.jsonrequests

import com.appstr.ftp.data.RedditJsonChild
import com.google.gson.annotations.SerializedName


data class RedditJsonRequest(

    val requestId: Long,

    var feedType: String = "",

    @SerializedName("data")
    var data: RedditDataJson?

){

    data class RedditDataJson(
        @SerializedName("children")
//        @TypeConverters(ListRedditJsonChildConverter::class)
        var children: List<RedditJsonChild> = listOf(),

        @SerializedName("after")
        var after: String
    )

}

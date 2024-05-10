package com.appstr.ftp.data

import com.google.gson.annotations.SerializedName


data class RedditResponse (

    @SerializedName("kind" ) var kind : String? = null,
    @SerializedName("data" ) var posts : Page?   = Page()

)

data class Page (

    @SerializedName("after"      ) var after     : String?             = null,
    @SerializedName("dist"       ) var dist      : Int?                = null,
    @SerializedName("modhash"    ) var modhash   : String?             = null,
    @SerializedName("geo_filter" ) var geoFilter : String?             = null,
    @SerializedName("children"   ) var posts  : ArrayList<PostMetaData> = arrayListOf(),
    @SerializedName("before"     ) var before    : String?             = null

)

data class PostMetaData (

    @SerializedName("kind" ) var kind : String? = null,
    @SerializedName("data" ) var post : RedditPost?   = RedditPost()

)


//data class MediaEmbed (
//
//
//)
//data class SecureMediaEmbed (
//
//
//)
//data class Gildings (
//
//
//)

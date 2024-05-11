package com.appstr.ftp.data

import com.google.gson.annotations.SerializedName


data class RedditVideo(
    @SerializedName("fallback_url")
    var fallbackUrl: String,
    @SerializedName("scrubber_media_url")
    var mediaUrl: String,
    @SerializedName("dash_url")
    var dashUrl: String,

    @SerializedName("duration")
    var duration: Int,
    @SerializedName("is_gif")
    var isGif: Boolean,
    @SerializedName("width")
    var width: Int,
    @SerializedName("height")
    var height: Int,
    @SerializedName("ups")
    var ups: Int
)
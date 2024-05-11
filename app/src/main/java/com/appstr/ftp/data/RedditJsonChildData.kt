package com.appstr.ftp.data

import android.text.Html
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName


data class RedditJsonChildData (

    val childDataId: Long,

    var feedType: String = "",

    @SerializedName("title")
    var title: String = "",

    @SerializedName("thumbnail")
    var thumbnail: String = "",

    @SerializedName("thumbnail_width")
    var thumbnail_width: Int,

    @SerializedName("thumbnail_height")
    var thumbnail_height: Int,

    @SerializedName("url")
    var url: String = "",

    @SerializedName("stickied")
    var stickied: Boolean = false,

    @SerializedName("media")
    @Embedded
    var media: Media? = null,

    @SerializedName("preview")
    @Embedded
    var preview: Preview? = null,

    @SerializedName("crosspost_parent_list")
//    @TypeConverters(ListCrossPostedConverter::class)
    var crossPostData: List<RedditJsonChildData>?

){
    init {
        decodeEscapeCharsTitle()
    }

    private fun decodeEscapeCharsTitle(){
        if (title.isEmpty()) return

        title =
            Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString()
    }

}